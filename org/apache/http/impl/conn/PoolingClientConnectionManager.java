/*     */ package org.apache.http.impl.conn;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.conn.ClientConnectionManager;
/*     */ import org.apache.http.conn.ClientConnectionOperator;
/*     */ import org.apache.http.conn.ClientConnectionRequest;
/*     */ import org.apache.http.conn.ConnectionPoolTimeoutException;
/*     */ import org.apache.http.conn.DnsResolver;
/*     */ import org.apache.http.conn.ManagedClientConnection;
/*     */ import org.apache.http.conn.routing.HttpRoute;
/*     */ import org.apache.http.conn.scheme.SchemeRegistry;
/*     */ import org.apache.http.pool.ConnPoolControl;
/*     */ import org.apache.http.pool.PoolStats;
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
/*     */ @Deprecated
/*     */ @ThreadSafe
/*     */ public class PoolingClientConnectionManager
/*     */   implements ClientConnectionManager, ConnPoolControl<HttpRoute>
/*     */ {
/*  75 */   private final Log log = LogFactory.getLog(getClass());
/*     */   
/*     */   private final SchemeRegistry schemeRegistry;
/*     */   
/*     */   private final HttpConnPool pool;
/*     */   
/*     */   private final ClientConnectionOperator operator;
/*     */   
/*     */   private final DnsResolver dnsResolver;
/*     */ 
/*     */   
/*     */   public PoolingClientConnectionManager(SchemeRegistry schreg) {
/*  87 */     this(schreg, -1L, TimeUnit.MILLISECONDS);
/*     */   }
/*     */   
/*     */   public PoolingClientConnectionManager(SchemeRegistry schreg, DnsResolver dnsResolver) {
/*  91 */     this(schreg, -1L, TimeUnit.MILLISECONDS, dnsResolver);
/*     */   }
/*     */   
/*     */   public PoolingClientConnectionManager() {
/*  95 */     this(SchemeRegistryFactory.createDefault());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PoolingClientConnectionManager(SchemeRegistry schemeRegistry, long timeToLive, TimeUnit tunit) {
/* 101 */     this(schemeRegistry, timeToLive, tunit, new SystemDefaultDnsResolver());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PoolingClientConnectionManager(SchemeRegistry schemeRegistry, long timeToLive, TimeUnit tunit, DnsResolver dnsResolver) {
/* 108 */     Args.notNull(schemeRegistry, "Scheme registry");
/* 109 */     Args.notNull(dnsResolver, "DNS resolver");
/* 110 */     this.schemeRegistry = schemeRegistry;
/* 111 */     this.dnsResolver = dnsResolver;
/* 112 */     this.operator = createConnectionOperator(schemeRegistry);
/* 113 */     this.pool = new HttpConnPool(this.log, this.operator, 2, 20, timeToLive, tunit);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void finalize() throws Throwable {
/*     */     try {
/* 119 */       shutdown();
/*     */     } finally {
/* 121 */       super.finalize();
/*     */     } 
/*     */   }
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
/*     */   protected ClientConnectionOperator createConnectionOperator(SchemeRegistry schreg) {
/* 138 */     return new DefaultClientConnectionOperator(schreg, this.dnsResolver);
/*     */   }
/*     */ 
/*     */   
/*     */   public SchemeRegistry getSchemeRegistry() {
/* 143 */     return this.schemeRegistry;
/*     */   }
/*     */   
/*     */   private String format(HttpRoute route, Object state) {
/* 147 */     StringBuilder buf = new StringBuilder();
/* 148 */     buf.append("[route: ").append(route).append("]");
/* 149 */     if (state != null) {
/* 150 */       buf.append("[state: ").append(state).append("]");
/*     */     }
/* 152 */     return buf.toString();
/*     */   }
/*     */   
/*     */   private String formatStats(HttpRoute route) {
/* 156 */     StringBuilder buf = new StringBuilder();
/* 157 */     PoolStats totals = this.pool.getTotalStats();
/* 158 */     PoolStats stats = this.pool.getStats(route);
/* 159 */     buf.append("[total kept alive: ").append(totals.getAvailable()).append("; ");
/* 160 */     buf.append("route allocated: ").append(stats.getLeased() + stats.getAvailable());
/* 161 */     buf.append(" of ").append(stats.getMax()).append("; ");
/* 162 */     buf.append("total allocated: ").append(totals.getLeased() + totals.getAvailable());
/* 163 */     buf.append(" of ").append(totals.getMax()).append("]");
/* 164 */     return buf.toString();
/*     */   }
/*     */   
/*     */   private String format(HttpPoolEntry entry) {
/* 168 */     StringBuilder buf = new StringBuilder();
/* 169 */     buf.append("[id: ").append(entry.getId()).append("]");
/* 170 */     buf.append("[route: ").append(entry.getRoute()).append("]");
/* 171 */     Object state = entry.getState();
/* 172 */     if (state != null) {
/* 173 */       buf.append("[state: ").append(state).append("]");
/*     */     }
/* 175 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClientConnectionRequest requestConnection(HttpRoute route, Object state) {
/* 182 */     Args.notNull(route, "HTTP route");
/* 183 */     if (this.log.isDebugEnabled()) {
/* 184 */       this.log.debug("Connection request: " + format(route, state) + formatStats(route));
/*     */     }
/* 186 */     final Future<HttpPoolEntry> future = this.pool.lease(route, state);
/*     */     
/* 188 */     return new ClientConnectionRequest()
/*     */       {
/*     */         public void abortRequest()
/*     */         {
/* 192 */           future.cancel(true);
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public ManagedClientConnection getConnection(long timeout, TimeUnit tunit) throws InterruptedException, ConnectionPoolTimeoutException {
/* 199 */           return PoolingClientConnectionManager.this.leaseConnection(future, timeout, tunit);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ManagedClientConnection leaseConnection(Future<HttpPoolEntry> future, long timeout, TimeUnit tunit) throws InterruptedException, ConnectionPoolTimeoutException {
/*     */     try {
/* 212 */       HttpPoolEntry entry = future.get(timeout, tunit);
/* 213 */       if (entry == null || future.isCancelled()) {
/* 214 */         throw new InterruptedException();
/*     */       }
/* 216 */       Asserts.check((entry.getConnection() != null), "Pool entry with no connection");
/* 217 */       if (this.log.isDebugEnabled()) {
/* 218 */         this.log.debug("Connection leased: " + format(entry) + formatStats((HttpRoute)entry.getRoute()));
/*     */       }
/* 220 */       return new ManagedClientConnectionImpl(this, this.operator, entry);
/* 221 */     } catch (ExecutionException ex) {
/* 222 */       Throwable cause = ex.getCause();
/* 223 */       if (cause == null) {
/* 224 */         cause = ex;
/*     */       }
/* 226 */       this.log.error("Unexpected exception leasing connection from pool", cause);
/*     */       
/* 228 */       throw new InterruptedException();
/* 229 */     } catch (TimeoutException ex) {
/* 230 */       throw new ConnectionPoolTimeoutException("Timeout waiting for connection from pool");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void releaseConnection(ManagedClientConnection conn, long keepalive, TimeUnit tunit) {
/* 238 */     Args.check(conn instanceof ManagedClientConnectionImpl, "Connection class mismatch, connection not obtained from this manager");
/*     */     
/* 240 */     ManagedClientConnectionImpl managedConn = (ManagedClientConnectionImpl)conn;
/* 241 */     Asserts.check((managedConn.getManager() == this), "Connection not obtained from this manager");
/* 242 */     synchronized (managedConn) {
/* 243 */       HttpPoolEntry entry = managedConn.detach();
/* 244 */       if (entry == null) {
/*     */         return;
/*     */       }
/*     */       try {
/* 248 */         if (managedConn.isOpen() && !managedConn.isMarkedReusable()) {
/*     */           try {
/* 250 */             managedConn.shutdown();
/* 251 */           } catch (IOException iox) {
/* 252 */             if (this.log.isDebugEnabled()) {
/* 253 */               this.log.debug("I/O exception shutting down released connection", iox);
/*     */             }
/*     */           } 
/*     */         }
/*     */         
/* 258 */         if (managedConn.isMarkedReusable()) {
/* 259 */           entry.updateExpiry(keepalive, (tunit != null) ? tunit : TimeUnit.MILLISECONDS);
/* 260 */           if (this.log.isDebugEnabled()) {
/*     */             String s;
/* 262 */             if (keepalive > 0L) {
/* 263 */               s = "for " + keepalive + " " + tunit;
/*     */             } else {
/* 265 */               s = "indefinitely";
/*     */             } 
/* 267 */             this.log.debug("Connection " + format(entry) + " can be kept alive " + s);
/*     */           } 
/*     */         } 
/*     */       } finally {
/* 271 */         this.pool.release(entry, managedConn.isMarkedReusable());
/*     */       } 
/* 273 */       if (this.log.isDebugEnabled()) {
/* 274 */         this.log.debug("Connection released: " + format(entry) + formatStats((HttpRoute)entry.getRoute()));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void shutdown() {
/* 281 */     this.log.debug("Connection manager is shutting down");
/*     */     try {
/* 283 */       this.pool.shutdown();
/* 284 */     } catch (IOException ex) {
/* 285 */       this.log.debug("I/O exception shutting down connection manager", ex);
/*     */     } 
/* 287 */     this.log.debug("Connection manager shut down");
/*     */   }
/*     */ 
/*     */   
/*     */   public void closeIdleConnections(long idleTimeout, TimeUnit tunit) {
/* 292 */     if (this.log.isDebugEnabled()) {
/* 293 */       this.log.debug("Closing connections idle longer than " + idleTimeout + " " + tunit);
/*     */     }
/* 295 */     this.pool.closeIdle(idleTimeout, tunit);
/*     */   }
/*     */ 
/*     */   
/*     */   public void closeExpiredConnections() {
/* 300 */     this.log.debug("Closing expired connections");
/* 301 */     this.pool.closeExpired();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxTotal() {
/* 306 */     return this.pool.getMaxTotal();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMaxTotal(int max) {
/* 311 */     this.pool.setMaxTotal(max);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDefaultMaxPerRoute() {
/* 316 */     return this.pool.getDefaultMaxPerRoute();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDefaultMaxPerRoute(int max) {
/* 321 */     this.pool.setDefaultMaxPerRoute(max);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxPerRoute(HttpRoute route) {
/* 326 */     return this.pool.getMaxPerRoute(route);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMaxPerRoute(HttpRoute route, int max) {
/* 331 */     this.pool.setMaxPerRoute(route, max);
/*     */   }
/*     */ 
/*     */   
/*     */   public PoolStats getTotalStats() {
/* 336 */     return this.pool.getTotalStats();
/*     */   }
/*     */ 
/*     */   
/*     */   public PoolStats getStats(HttpRoute route) {
/* 341 */     return this.pool.getStats(route);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\conn\PoolingClientConnectionManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */