/*     */ package org.apache.http.impl.conn.tsccm;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.conn.ClientConnectionManager;
/*     */ import org.apache.http.conn.ClientConnectionOperator;
/*     */ import org.apache.http.conn.ClientConnectionRequest;
/*     */ import org.apache.http.conn.ConnectionPoolTimeoutException;
/*     */ import org.apache.http.conn.ManagedClientConnection;
/*     */ import org.apache.http.conn.params.ConnPerRoute;
/*     */ import org.apache.http.conn.params.ConnPerRouteBean;
/*     */ import org.apache.http.conn.routing.HttpRoute;
/*     */ import org.apache.http.conn.scheme.SchemeRegistry;
/*     */ import org.apache.http.impl.conn.DefaultClientConnectionOperator;
/*     */ import org.apache.http.impl.conn.SchemeRegistryFactory;
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
/*     */ public class ThreadSafeClientConnManager
/*     */   implements ClientConnectionManager
/*     */ {
/*     */   private final Log log;
/*     */   protected final SchemeRegistry schemeRegistry;
/*     */   protected final AbstractConnPool connectionPool;
/*     */   protected final ConnPoolByRoute pool;
/*     */   protected final ClientConnectionOperator connOperator;
/*     */   protected final ConnPerRouteBean connPerRoute;
/*     */   
/*     */   public ThreadSafeClientConnManager(SchemeRegistry schreg) {
/*  94 */     this(schreg, -1L, TimeUnit.MILLISECONDS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ThreadSafeClientConnManager() {
/* 101 */     this(SchemeRegistryFactory.createDefault());
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
/*     */   public ThreadSafeClientConnManager(SchemeRegistry schreg, long connTTL, TimeUnit connTTLTimeUnit) {
/* 115 */     this(schreg, connTTL, connTTLTimeUnit, new ConnPerRouteBean());
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
/*     */   
/*     */   public ThreadSafeClientConnManager(SchemeRegistry schreg, long connTTL, TimeUnit connTTLTimeUnit, ConnPerRouteBean connPerRoute) {
/* 133 */     Args.notNull(schreg, "Scheme registry");
/* 134 */     this.log = LogFactory.getLog(getClass());
/* 135 */     this.schemeRegistry = schreg;
/* 136 */     this.connPerRoute = connPerRoute;
/* 137 */     this.connOperator = createConnectionOperator(schreg);
/* 138 */     this.pool = createConnectionPool(connTTL, connTTLTimeUnit);
/* 139 */     this.connectionPool = this.pool;
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
/*     */   @Deprecated
/*     */   public ThreadSafeClientConnManager(HttpParams params, SchemeRegistry schreg) {
/* 153 */     Args.notNull(schreg, "Scheme registry");
/* 154 */     this.log = LogFactory.getLog(getClass());
/* 155 */     this.schemeRegistry = schreg;
/* 156 */     this.connPerRoute = new ConnPerRouteBean();
/* 157 */     this.connOperator = createConnectionOperator(schreg);
/* 158 */     this.pool = (ConnPoolByRoute)createConnectionPool(params);
/* 159 */     this.connectionPool = this.pool;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void finalize() throws Throwable {
/*     */     try {
/* 165 */       shutdown();
/*     */     } finally {
/* 167 */       super.finalize();
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
/*     */   @Deprecated
/*     */   protected AbstractConnPool createConnectionPool(HttpParams params) {
/* 180 */     return new ConnPoolByRoute(this.connOperator, params);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ConnPoolByRoute createConnectionPool(long connTTL, TimeUnit connTTLTimeUnit) {
/* 191 */     return new ConnPoolByRoute(this.connOperator, (ConnPerRoute)this.connPerRoute, 20, connTTL, connTTLTimeUnit);
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
/*     */   
/*     */   protected ClientConnectionOperator createConnectionOperator(SchemeRegistry schreg) {
/* 209 */     return (ClientConnectionOperator)new DefaultClientConnectionOperator(schreg);
/*     */   }
/*     */ 
/*     */   
/*     */   public SchemeRegistry getSchemeRegistry() {
/* 214 */     return this.schemeRegistry;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClientConnectionRequest requestConnection(final HttpRoute route, Object state) {
/* 222 */     final PoolEntryRequest poolRequest = this.pool.requestPoolEntry(route, state);
/*     */ 
/*     */     
/* 225 */     return new ClientConnectionRequest()
/*     */       {
/*     */         public void abortRequest()
/*     */         {
/* 229 */           poolRequest.abortRequest();
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public ManagedClientConnection getConnection(long timeout, TimeUnit tunit) throws InterruptedException, ConnectionPoolTimeoutException {
/* 236 */           Args.notNull(route, "Route");
/*     */           
/* 238 */           if (ThreadSafeClientConnManager.this.log.isDebugEnabled()) {
/* 239 */             ThreadSafeClientConnManager.this.log.debug("Get connection: " + route + ", timeout = " + timeout);
/*     */           }
/*     */           
/* 242 */           BasicPoolEntry entry = poolRequest.getPoolEntry(timeout, tunit);
/* 243 */           return (ManagedClientConnection)new BasicPooledConnAdapter(ThreadSafeClientConnManager.this, entry);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void releaseConnection(ManagedClientConnection conn, long validDuration, TimeUnit timeUnit) {
/* 252 */     Args.check(conn instanceof BasicPooledConnAdapter, "Connection class mismatch, connection not obtained from this manager");
/*     */     
/* 254 */     BasicPooledConnAdapter hca = (BasicPooledConnAdapter)conn;
/* 255 */     if (hca.getPoolEntry() != null) {
/* 256 */       Asserts.check((hca.getManager() == this), "Connection not obtained from this manager");
/*     */     }
/* 258 */     synchronized (hca) {
/* 259 */       BasicPoolEntry entry = (BasicPoolEntry)hca.getPoolEntry();
/* 260 */       if (entry == null) {
/*     */         return;
/*     */       }
/*     */       
/*     */       try {
/* 265 */         if (hca.isOpen() && !hca.isMarkedReusable())
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 274 */           hca.shutdown();
/*     */         }
/* 276 */       } catch (IOException iox) {
/* 277 */         if (this.log.isDebugEnabled()) {
/* 278 */           this.log.debug("Exception shutting down released connection.", iox);
/*     */         }
/*     */       } finally {
/*     */         
/* 282 */         boolean reusable = hca.isMarkedReusable();
/* 283 */         if (this.log.isDebugEnabled()) {
/* 284 */           if (reusable) {
/* 285 */             this.log.debug("Released connection is reusable.");
/*     */           } else {
/* 287 */             this.log.debug("Released connection is not reusable.");
/*     */           } 
/*     */         }
/* 290 */         hca.detach();
/* 291 */         this.pool.freeEntry(entry, reusable, validDuration, timeUnit);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void shutdown() {
/* 298 */     this.log.debug("Shutting down");
/* 299 */     this.pool.shutdown();
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
/*     */   public int getConnectionsInPool(HttpRoute route) {
/* 313 */     return this.pool.getConnectionsInPool(route);
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
/*     */   public int getConnectionsInPool() {
/* 325 */     return this.pool.getConnectionsInPool();
/*     */   }
/*     */ 
/*     */   
/*     */   public void closeIdleConnections(long idleTimeout, TimeUnit tunit) {
/* 330 */     if (this.log.isDebugEnabled()) {
/* 331 */       this.log.debug("Closing connections idle longer than " + idleTimeout + " " + tunit);
/*     */     }
/* 333 */     this.pool.closeIdleConnections(idleTimeout, tunit);
/*     */   }
/*     */ 
/*     */   
/*     */   public void closeExpiredConnections() {
/* 338 */     this.log.debug("Closing expired connections");
/* 339 */     this.pool.closeExpiredConnections();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxTotal() {
/* 346 */     return this.pool.getMaxTotalConnections();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxTotal(int max) {
/* 353 */     this.pool.setMaxTotalConnections(max);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDefaultMaxPerRoute() {
/* 360 */     return this.connPerRoute.getDefaultMaxPerRoute();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultMaxPerRoute(int max) {
/* 367 */     this.connPerRoute.setDefaultMaxPerRoute(max);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxForRoute(HttpRoute route) {
/* 374 */     return this.connPerRoute.getMaxForRoute(route);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxForRoute(HttpRoute route, int max) {
/* 381 */     this.connPerRoute.setMaxForRoute(route, max);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\conn\tsccm\ThreadSafeClientConnManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */