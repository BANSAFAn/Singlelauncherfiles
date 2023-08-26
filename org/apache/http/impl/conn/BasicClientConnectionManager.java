/*     */ package org.apache.http.impl.conn;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.HttpClientConnection;
/*     */ import org.apache.http.annotation.GuardedBy;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.conn.ClientConnectionManager;
/*     */ import org.apache.http.conn.ClientConnectionOperator;
/*     */ import org.apache.http.conn.ClientConnectionRequest;
/*     */ import org.apache.http.conn.ManagedClientConnection;
/*     */ import org.apache.http.conn.OperatedClientConnection;
/*     */ import org.apache.http.conn.routing.HttpRoute;
/*     */ import org.apache.http.conn.scheme.SchemeRegistry;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.Asserts;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ @ThreadSafe
/*     */ public class BasicClientConnectionManager
/*     */   implements ClientConnectionManager
/*     */ {
/*  73 */   private final Log log = LogFactory.getLog(getClass());
/*     */   
/*  75 */   private static final AtomicLong COUNTER = new AtomicLong();
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String MISUSE_MESSAGE = "Invalid use of BasicClientConnManager: connection still allocated.\nMake sure to release the connection before allocating another one.";
/*     */ 
/*     */ 
/*     */   
/*     */   private final SchemeRegistry schemeRegistry;
/*     */ 
/*     */ 
/*     */   
/*     */   private final ClientConnectionOperator connOperator;
/*     */ 
/*     */ 
/*     */   
/*     */   @GuardedBy("this")
/*     */   private HttpPoolEntry poolEntry;
/*     */ 
/*     */ 
/*     */   
/*     */   @GuardedBy("this")
/*     */   private ManagedClientConnectionImpl conn;
/*     */ 
/*     */   
/*     */   @GuardedBy("this")
/*     */   private volatile boolean shutdown;
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicClientConnectionManager(SchemeRegistry schreg) {
/* 106 */     Args.notNull(schreg, "Scheme registry");
/* 107 */     this.schemeRegistry = schreg;
/* 108 */     this.connOperator = createConnectionOperator(schreg);
/*     */   }
/*     */   
/*     */   public BasicClientConnectionManager() {
/* 112 */     this(SchemeRegistryFactory.createDefault());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void finalize() throws Throwable {
/*     */     try {
/* 118 */       shutdown();
/*     */     } finally {
/* 120 */       super.finalize();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public SchemeRegistry getSchemeRegistry() {
/* 126 */     return this.schemeRegistry;
/*     */   }
/*     */   
/*     */   protected ClientConnectionOperator createConnectionOperator(SchemeRegistry schreg) {
/* 130 */     return new DefaultClientConnectionOperator(schreg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ClientConnectionRequest requestConnection(final HttpRoute route, final Object state) {
/* 138 */     return new ClientConnectionRequest()
/*     */       {
/*     */         public void abortRequest() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public ManagedClientConnection getConnection(long timeout, TimeUnit tunit) {
/* 148 */           return BasicClientConnectionManager.this.getConnection(route, state);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void assertNotShutdown() {
/* 156 */     Asserts.check(!this.shutdown, "Connection manager has been shut down");
/*     */   }
/*     */   
/*     */   ManagedClientConnection getConnection(HttpRoute route, Object state) {
/* 160 */     Args.notNull(route, "Route");
/* 161 */     synchronized (this) {
/* 162 */       assertNotShutdown();
/* 163 */       if (this.log.isDebugEnabled()) {
/* 164 */         this.log.debug("Get connection for route " + route);
/*     */       }
/* 166 */       Asserts.check((this.conn == null), "Invalid use of BasicClientConnManager: connection still allocated.\nMake sure to release the connection before allocating another one.");
/* 167 */       if (this.poolEntry != null && !this.poolEntry.getPlannedRoute().equals(route)) {
/* 168 */         this.poolEntry.close();
/* 169 */         this.poolEntry = null;
/*     */       } 
/* 171 */       if (this.poolEntry == null) {
/* 172 */         String id = Long.toString(COUNTER.getAndIncrement());
/* 173 */         OperatedClientConnection opconn = this.connOperator.createConnection();
/* 174 */         this.poolEntry = new HttpPoolEntry(this.log, id, route, opconn, 0L, TimeUnit.MILLISECONDS);
/*     */       } 
/* 176 */       long now = System.currentTimeMillis();
/* 177 */       if (this.poolEntry.isExpired(now)) {
/* 178 */         this.poolEntry.close();
/* 179 */         this.poolEntry.getTracker().reset();
/*     */       } 
/* 181 */       this.conn = new ManagedClientConnectionImpl(this, this.connOperator, this.poolEntry);
/* 182 */       return this.conn;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void shutdownConnection(HttpClientConnection conn) {
/*     */     try {
/* 188 */       conn.shutdown();
/* 189 */     } catch (IOException iox) {
/* 190 */       if (this.log.isDebugEnabled()) {
/* 191 */         this.log.debug("I/O exception shutting down connection", iox);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void releaseConnection(ManagedClientConnection conn, long keepalive, TimeUnit tunit) {
/* 198 */     Args.check(conn instanceof ManagedClientConnectionImpl, "Connection class mismatch, connection not obtained from this manager");
/*     */     
/* 200 */     ManagedClientConnectionImpl managedConn = (ManagedClientConnectionImpl)conn;
/* 201 */     synchronized (managedConn) {
/* 202 */       if (this.log.isDebugEnabled()) {
/* 203 */         this.log.debug("Releasing connection " + conn);
/*     */       }
/* 205 */       if (managedConn.getPoolEntry() == null) {
/*     */         return;
/*     */       }
/* 208 */       ClientConnectionManager manager = managedConn.getManager();
/* 209 */       Asserts.check((manager == this), "Connection not obtained from this manager");
/* 210 */       synchronized (this) {
/* 211 */         if (this.shutdown) {
/* 212 */           shutdownConnection((HttpClientConnection)managedConn);
/*     */           return;
/*     */         } 
/*     */         try {
/* 216 */           if (managedConn.isOpen() && !managedConn.isMarkedReusable()) {
/* 217 */             shutdownConnection((HttpClientConnection)managedConn);
/*     */           }
/* 219 */           if (managedConn.isMarkedReusable()) {
/* 220 */             this.poolEntry.updateExpiry(keepalive, (tunit != null) ? tunit : TimeUnit.MILLISECONDS);
/* 221 */             if (this.log.isDebugEnabled()) {
/*     */               String s;
/* 223 */               if (keepalive > 0L) {
/* 224 */                 s = "for " + keepalive + " " + tunit;
/*     */               } else {
/* 226 */                 s = "indefinitely";
/*     */               } 
/* 228 */               this.log.debug("Connection can be kept alive " + s);
/*     */             } 
/*     */           } 
/*     */         } finally {
/* 232 */           managedConn.detach();
/* 233 */           this.conn = null;
/* 234 */           if (this.poolEntry.isClosed()) {
/* 235 */             this.poolEntry = null;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void closeExpiredConnections() {
/* 244 */     synchronized (this) {
/* 245 */       assertNotShutdown();
/* 246 */       long now = System.currentTimeMillis();
/* 247 */       if (this.poolEntry != null && this.poolEntry.isExpired(now)) {
/* 248 */         this.poolEntry.close();
/* 249 */         this.poolEntry.getTracker().reset();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void closeIdleConnections(long idletime, TimeUnit tunit) {
/* 256 */     Args.notNull(tunit, "Time unit");
/* 257 */     synchronized (this) {
/* 258 */       assertNotShutdown();
/* 259 */       long time = tunit.toMillis(idletime);
/* 260 */       if (time < 0L) {
/* 261 */         time = 0L;
/*     */       }
/* 263 */       long deadline = System.currentTimeMillis() - time;
/* 264 */       if (this.poolEntry != null && this.poolEntry.getUpdated() <= deadline) {
/* 265 */         this.poolEntry.close();
/* 266 */         this.poolEntry.getTracker().reset();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void shutdown() {
/* 273 */     synchronized (this) {
/* 274 */       this.shutdown = true;
/*     */       try {
/* 276 */         if (this.poolEntry != null) {
/* 277 */           this.poolEntry.close();
/*     */         }
/*     */       } finally {
/* 280 */         this.poolEntry = null;
/* 281 */         this.conn = null;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\conn\BasicClientConnectionManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */