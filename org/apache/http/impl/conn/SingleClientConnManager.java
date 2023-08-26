/*     */ package org.apache.http.impl.conn;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.annotation.GuardedBy;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.conn.ClientConnectionManager;
/*     */ import org.apache.http.conn.ClientConnectionOperator;
/*     */ import org.apache.http.conn.ClientConnectionRequest;
/*     */ import org.apache.http.conn.ManagedClientConnection;
/*     */ import org.apache.http.conn.routing.HttpRoute;
/*     */ import org.apache.http.conn.routing.RouteTracker;
/*     */ import org.apache.http.conn.scheme.SchemeRegistry;
/*     */ import org.apache.http.params.HttpParams;
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
/*     */ @Deprecated
/*     */ @ThreadSafe
/*     */ public class SingleClientConnManager
/*     */   implements ClientConnectionManager
/*     */ {
/*  68 */   private final Log log = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String MISUSE_MESSAGE = "Invalid use of SingleClientConnManager: connection still allocated.\nMake sure to release the connection before allocating another one.";
/*     */ 
/*     */ 
/*     */   
/*     */   protected final SchemeRegistry schemeRegistry;
/*     */ 
/*     */ 
/*     */   
/*     */   protected final ClientConnectionOperator connOperator;
/*     */ 
/*     */ 
/*     */   
/*     */   protected final boolean alwaysShutDown;
/*     */ 
/*     */ 
/*     */   
/*     */   @GuardedBy("this")
/*     */   protected volatile PoolEntry uniquePoolEntry;
/*     */ 
/*     */ 
/*     */   
/*     */   @GuardedBy("this")
/*     */   protected volatile ConnAdapter managedConn;
/*     */ 
/*     */ 
/*     */   
/*     */   @GuardedBy("this")
/*     */   protected volatile long lastReleaseTime;
/*     */ 
/*     */ 
/*     */   
/*     */   @GuardedBy("this")
/*     */   protected volatile long connectionExpiresTime;
/*     */ 
/*     */ 
/*     */   
/*     */   protected volatile boolean isShutDown;
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public SingleClientConnManager(HttpParams params, SchemeRegistry schreg) {
/* 114 */     this(schreg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SingleClientConnManager(SchemeRegistry schreg) {
/* 122 */     Args.notNull(schreg, "Scheme registry");
/* 123 */     this.schemeRegistry = schreg;
/* 124 */     this.connOperator = createConnectionOperator(schreg);
/* 125 */     this.uniquePoolEntry = new PoolEntry();
/* 126 */     this.managedConn = null;
/* 127 */     this.lastReleaseTime = -1L;
/* 128 */     this.alwaysShutDown = false;
/* 129 */     this.isShutDown = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SingleClientConnManager() {
/* 136 */     this(SchemeRegistryFactory.createDefault());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void finalize() throws Throwable {
/*     */     try {
/* 142 */       shutdown();
/*     */     } finally {
/* 144 */       super.finalize();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public SchemeRegistry getSchemeRegistry() {
/* 150 */     return this.schemeRegistry;
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
/*     */   
/*     */   protected ClientConnectionOperator createConnectionOperator(SchemeRegistry schreg) {
/* 167 */     return new DefaultClientConnectionOperator(schreg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void assertStillUp() throws IllegalStateException {
/* 176 */     Asserts.check(!this.isShutDown, "Manager is shut down");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ClientConnectionRequest requestConnection(final HttpRoute route, final Object state) {
/* 184 */     return new ClientConnectionRequest()
/*     */       {
/*     */         public void abortRequest() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public ManagedClientConnection getConnection(long timeout, TimeUnit tunit) {
/* 194 */           return SingleClientConnManager.this.getConnection(route, state);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ManagedClientConnection getConnection(HttpRoute route, Object state) {
/* 210 */     Args.notNull(route, "Route");
/* 211 */     assertStillUp();
/*     */     
/* 213 */     if (this.log.isDebugEnabled()) {
/* 214 */       this.log.debug("Get connection for route " + route);
/*     */     }
/*     */     
/* 217 */     synchronized (this) {
/*     */       
/* 219 */       Asserts.check((this.managedConn == null), "Invalid use of SingleClientConnManager: connection still allocated.\nMake sure to release the connection before allocating another one.");
/*     */ 
/*     */       
/* 222 */       boolean recreate = false;
/* 223 */       boolean shutdown = false;
/*     */ 
/*     */       
/* 226 */       closeExpiredConnections();
/*     */       
/* 228 */       if (this.uniquePoolEntry.connection.isOpen()) {
/* 229 */         RouteTracker tracker = this.uniquePoolEntry.tracker;
/* 230 */         shutdown = (tracker == null || !tracker.toRoute().equals(route));
/*     */ 
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */ 
/*     */         
/* 238 */         recreate = true;
/*     */       } 
/*     */       
/* 241 */       if (shutdown) {
/* 242 */         recreate = true;
/*     */         try {
/* 244 */           this.uniquePoolEntry.shutdown();
/* 245 */         } catch (IOException iox) {
/* 246 */           this.log.debug("Problem shutting down connection.", iox);
/*     */         } 
/*     */       } 
/*     */       
/* 250 */       if (recreate) {
/* 251 */         this.uniquePoolEntry = new PoolEntry();
/*     */       }
/*     */       
/* 254 */       this.managedConn = new ConnAdapter(this.uniquePoolEntry, route);
/*     */       
/* 256 */       return this.managedConn;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void releaseConnection(ManagedClientConnection conn, long validDuration, TimeUnit timeUnit) {
/* 264 */     Args.check(conn instanceof ConnAdapter, "Connection class mismatch, connection not obtained from this manager");
/*     */     
/* 266 */     assertStillUp();
/*     */     
/* 268 */     if (this.log.isDebugEnabled()) {
/* 269 */       this.log.debug("Releasing connection " + conn);
/*     */     }
/*     */     
/* 272 */     ConnAdapter sca = (ConnAdapter)conn;
/* 273 */     synchronized (sca) {
/* 274 */       if (sca.poolEntry == null) {
/*     */         return;
/*     */       }
/*     */       
/* 278 */       ClientConnectionManager manager = sca.getManager();
/* 279 */       Asserts.check((manager == this), "Connection not obtained from this manager");
/*     */       
/*     */       try {
/* 282 */         if (sca.isOpen() && (this.alwaysShutDown || !sca.isMarkedReusable())) {
/*     */ 
/*     */           
/* 285 */           if (this.log.isDebugEnabled()) {
/* 286 */             this.log.debug("Released connection open but not reusable.");
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 293 */           sca.shutdown();
/*     */         } 
/* 295 */       } catch (IOException iox) {
/* 296 */         if (this.log.isDebugEnabled()) {
/* 297 */           this.log.debug("Exception shutting down released connection.", iox);
/*     */         }
/*     */       } finally {
/*     */         
/* 301 */         sca.detach();
/* 302 */         synchronized (this) {
/* 303 */           this.managedConn = null;
/* 304 */           this.lastReleaseTime = System.currentTimeMillis();
/* 305 */           if (validDuration > 0L) {
/* 306 */             this.connectionExpiresTime = timeUnit.toMillis(validDuration) + this.lastReleaseTime;
/*     */           } else {
/* 308 */             this.connectionExpiresTime = Long.MAX_VALUE;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void closeExpiredConnections() {
/* 317 */     long time = this.connectionExpiresTime;
/* 318 */     if (System.currentTimeMillis() >= time) {
/* 319 */       closeIdleConnections(0L, TimeUnit.MILLISECONDS);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void closeIdleConnections(long idletime, TimeUnit tunit) {
/* 325 */     assertStillUp();
/*     */ 
/*     */     
/* 328 */     Args.notNull(tunit, "Time unit");
/*     */     
/* 330 */     synchronized (this) {
/* 331 */       if (this.managedConn == null && this.uniquePoolEntry.connection.isOpen()) {
/* 332 */         long cutoff = System.currentTimeMillis() - tunit.toMillis(idletime);
/*     */         
/* 334 */         if (this.lastReleaseTime <= cutoff) {
/*     */           try {
/* 336 */             this.uniquePoolEntry.close();
/* 337 */           } catch (IOException iox) {
/*     */             
/* 339 */             this.log.debug("Problem closing idle connection.", iox);
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void shutdown() {
/* 348 */     this.isShutDown = true;
/* 349 */     synchronized (this) {
/*     */       try {
/* 351 */         if (this.uniquePoolEntry != null) {
/* 352 */           this.uniquePoolEntry.shutdown();
/*     */         }
/* 354 */       } catch (IOException iox) {
/*     */         
/* 356 */         this.log.debug("Problem while shutting down manager.", iox);
/*     */       } finally {
/* 358 */         this.uniquePoolEntry = null;
/* 359 */         this.managedConn = null;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void revokeConnection() {
/* 365 */     ConnAdapter conn = this.managedConn;
/* 366 */     if (conn == null) {
/*     */       return;
/*     */     }
/* 369 */     conn.detach();
/*     */     
/* 371 */     synchronized (this) {
/*     */       try {
/* 373 */         this.uniquePoolEntry.shutdown();
/* 374 */       } catch (IOException iox) {
/*     */         
/* 376 */         this.log.debug("Problem while shutting down connection.", iox);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected class PoolEntry
/*     */     extends AbstractPoolEntry
/*     */   {
/*     */     protected PoolEntry() {
/* 391 */       super(SingleClientConnManager.this.connOperator, null);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected void close() throws IOException {
/* 398 */       shutdownEntry();
/* 399 */       if (this.connection.isOpen()) {
/* 400 */         this.connection.close();
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected void shutdown() throws IOException {
/* 408 */       shutdownEntry();
/* 409 */       if (this.connection.isOpen()) {
/* 410 */         this.connection.shutdown();
/*     */       }
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
/*     */   protected class ConnAdapter
/*     */     extends AbstractPooledConnAdapter
/*     */   {
/*     */     protected ConnAdapter(SingleClientConnManager.PoolEntry entry, HttpRoute route) {
/* 428 */       super(SingleClientConnManager.this, entry);
/* 429 */       markReusable();
/* 430 */       entry.route = route;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\conn\SingleClientConnManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */