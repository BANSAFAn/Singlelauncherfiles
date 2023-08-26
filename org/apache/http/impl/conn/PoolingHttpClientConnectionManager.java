/*     */ package org.apache.http.impl.conn;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.HttpClientConnection;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.config.ConnectionConfig;
/*     */ import org.apache.http.config.Lookup;
/*     */ import org.apache.http.config.Registry;
/*     */ import org.apache.http.config.RegistryBuilder;
/*     */ import org.apache.http.config.SocketConfig;
/*     */ import org.apache.http.conn.ConnectionPoolTimeoutException;
/*     */ import org.apache.http.conn.ConnectionRequest;
/*     */ import org.apache.http.conn.DnsResolver;
/*     */ import org.apache.http.conn.HttpClientConnectionManager;
/*     */ import org.apache.http.conn.HttpClientConnectionOperator;
/*     */ import org.apache.http.conn.HttpConnectionFactory;
/*     */ import org.apache.http.conn.ManagedHttpClientConnection;
/*     */ import org.apache.http.conn.SchemePortResolver;
/*     */ import org.apache.http.conn.routing.HttpRoute;
/*     */ import org.apache.http.conn.socket.ConnectionSocketFactory;
/*     */ import org.apache.http.conn.socket.PlainConnectionSocketFactory;
/*     */ import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
/*     */ import org.apache.http.pool.ConnFactory;
/*     */ import org.apache.http.pool.ConnPoolControl;
/*     */ import org.apache.http.pool.PoolStats;
/*     */ import org.apache.http.protocol.HttpContext;
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
/*     */ @ThreadSafe
/*     */ public class PoolingHttpClientConnectionManager
/*     */   implements HttpClientConnectionManager, ConnPoolControl<HttpRoute>, Closeable
/*     */ {
/* 101 */   private final Log log = LogFactory.getLog(getClass());
/*     */   
/*     */   private final ConfigData configData;
/*     */   private final CPool pool;
/*     */   private final HttpClientConnectionOperator connectionOperator;
/*     */   private final AtomicBoolean isShutDown;
/*     */   
/*     */   private static Registry<ConnectionSocketFactory> getDefaultRegistry() {
/* 109 */     return RegistryBuilder.create().register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", SSLConnectionSocketFactory.getSocketFactory()).build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PoolingHttpClientConnectionManager() {
/* 116 */     this(getDefaultRegistry());
/*     */   }
/*     */   
/*     */   public PoolingHttpClientConnectionManager(long timeToLive, TimeUnit tunit) {
/* 120 */     this(getDefaultRegistry(), null, null, null, timeToLive, tunit);
/*     */   }
/*     */ 
/*     */   
/*     */   public PoolingHttpClientConnectionManager(Registry<ConnectionSocketFactory> socketFactoryRegistry) {
/* 125 */     this(socketFactoryRegistry, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PoolingHttpClientConnectionManager(Registry<ConnectionSocketFactory> socketFactoryRegistry, DnsResolver dnsResolver) {
/* 131 */     this(socketFactoryRegistry, null, dnsResolver);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PoolingHttpClientConnectionManager(Registry<ConnectionSocketFactory> socketFactoryRegistry, HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory) {
/* 137 */     this(socketFactoryRegistry, connFactory, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public PoolingHttpClientConnectionManager(HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory) {
/* 142 */     this(getDefaultRegistry(), connFactory, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PoolingHttpClientConnectionManager(Registry<ConnectionSocketFactory> socketFactoryRegistry, HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory, DnsResolver dnsResolver) {
/* 149 */     this(socketFactoryRegistry, connFactory, null, dnsResolver, -1L, TimeUnit.MILLISECONDS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PoolingHttpClientConnectionManager(Registry<ConnectionSocketFactory> socketFactoryRegistry, HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory, SchemePortResolver schemePortResolver, DnsResolver dnsResolver, long timeToLive, TimeUnit tunit) {
/* 158 */     this(new DefaultHttpClientConnectionOperator((Lookup<ConnectionSocketFactory>)socketFactoryRegistry, schemePortResolver, dnsResolver), connFactory, timeToLive, tunit);
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
/*     */   public PoolingHttpClientConnectionManager(HttpClientConnectionOperator httpClientConnectionOperator, HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory, long timeToLive, TimeUnit tunit) {
/* 173 */     this.configData = new ConfigData();
/* 174 */     this.pool = new CPool(new InternalConnectionFactory(this.configData, connFactory), 2, 20, timeToLive, tunit);
/*     */     
/* 176 */     this.pool.setValidateAfterInactivity(2000);
/* 177 */     this.connectionOperator = (HttpClientConnectionOperator)Args.notNull(httpClientConnectionOperator, "HttpClientConnectionOperator");
/* 178 */     this.isShutDown = new AtomicBoolean(false);
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
/*     */   PoolingHttpClientConnectionManager(CPool pool, Lookup<ConnectionSocketFactory> socketFactoryRegistry, SchemePortResolver schemePortResolver, DnsResolver dnsResolver) {
/* 190 */     this.configData = new ConfigData();
/* 191 */     this.pool = pool;
/* 192 */     this.connectionOperator = new DefaultHttpClientConnectionOperator(socketFactoryRegistry, schemePortResolver, dnsResolver);
/*     */     
/* 194 */     this.isShutDown = new AtomicBoolean(false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void finalize() throws Throwable {
/*     */     try {
/* 200 */       shutdown();
/*     */     } finally {
/* 202 */       super.finalize();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 208 */     shutdown();
/*     */   }
/*     */   
/*     */   private String format(HttpRoute route, Object state) {
/* 212 */     StringBuilder buf = new StringBuilder();
/* 213 */     buf.append("[route: ").append(route).append("]");
/* 214 */     if (state != null) {
/* 215 */       buf.append("[state: ").append(state).append("]");
/*     */     }
/* 217 */     return buf.toString();
/*     */   }
/*     */   
/*     */   private String formatStats(HttpRoute route) {
/* 221 */     StringBuilder buf = new StringBuilder();
/* 222 */     PoolStats totals = this.pool.getTotalStats();
/* 223 */     PoolStats stats = this.pool.getStats(route);
/* 224 */     buf.append("[total kept alive: ").append(totals.getAvailable()).append("; ");
/* 225 */     buf.append("route allocated: ").append(stats.getLeased() + stats.getAvailable());
/* 226 */     buf.append(" of ").append(stats.getMax()).append("; ");
/* 227 */     buf.append("total allocated: ").append(totals.getLeased() + totals.getAvailable());
/* 228 */     buf.append(" of ").append(totals.getMax()).append("]");
/* 229 */     return buf.toString();
/*     */   }
/*     */   
/*     */   private String format(CPoolEntry entry) {
/* 233 */     StringBuilder buf = new StringBuilder();
/* 234 */     buf.append("[id: ").append(entry.getId()).append("]");
/* 235 */     buf.append("[route: ").append(entry.getRoute()).append("]");
/* 236 */     Object state = entry.getState();
/* 237 */     if (state != null) {
/* 238 */       buf.append("[state: ").append(state).append("]");
/*     */     }
/* 240 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConnectionRequest requestConnection(HttpRoute route, Object state) {
/* 247 */     Args.notNull(route, "HTTP route");
/* 248 */     if (this.log.isDebugEnabled()) {
/* 249 */       this.log.debug("Connection request: " + format(route, state) + formatStats(route));
/*     */     }
/* 251 */     final Future<CPoolEntry> future = this.pool.lease(route, state, null);
/* 252 */     return new ConnectionRequest()
/*     */       {
/*     */         public boolean cancel()
/*     */         {
/* 256 */           return future.cancel(true);
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public HttpClientConnection get(long timeout, TimeUnit tunit) throws InterruptedException, ExecutionException, ConnectionPoolTimeoutException {
/* 263 */           return PoolingHttpClientConnectionManager.this.leaseConnection(future, timeout, tunit);
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
/*     */   protected HttpClientConnection leaseConnection(Future<CPoolEntry> future, long timeout, TimeUnit tunit) throws InterruptedException, ExecutionException, ConnectionPoolTimeoutException {
/*     */     try {
/* 276 */       CPoolEntry entry = future.get(timeout, tunit);
/* 277 */       if (entry == null || future.isCancelled()) {
/* 278 */         throw new InterruptedException();
/*     */       }
/* 280 */       Asserts.check((entry.getConnection() != null), "Pool entry with no connection");
/* 281 */       if (this.log.isDebugEnabled()) {
/* 282 */         this.log.debug("Connection leased: " + format(entry) + formatStats((HttpRoute)entry.getRoute()));
/*     */       }
/* 284 */       return CPoolProxy.newProxy(entry);
/* 285 */     } catch (TimeoutException ex) {
/* 286 */       throw new ConnectionPoolTimeoutException("Timeout waiting for connection from pool");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void releaseConnection(HttpClientConnection managedConn, Object state, long keepalive, TimeUnit tunit) {
/* 295 */     Args.notNull(managedConn, "Managed connection");
/* 296 */     synchronized (managedConn) {
/* 297 */       CPoolEntry entry = CPoolProxy.detach(managedConn);
/* 298 */       if (entry == null) {
/*     */         return;
/*     */       }
/* 301 */       ManagedHttpClientConnection conn = (ManagedHttpClientConnection)entry.getConnection();
/*     */       try {
/* 303 */         if (conn.isOpen()) {
/* 304 */           TimeUnit effectiveUnit = (tunit != null) ? tunit : TimeUnit.MILLISECONDS;
/* 305 */           entry.setState(state);
/* 306 */           entry.updateExpiry(keepalive, effectiveUnit);
/* 307 */           if (this.log.isDebugEnabled()) {
/*     */             String s;
/* 309 */             if (keepalive > 0L) {
/* 310 */               s = "for " + (effectiveUnit.toMillis(keepalive) / 1000.0D) + " seconds";
/*     */             } else {
/* 312 */               s = "indefinitely";
/*     */             } 
/* 314 */             this.log.debug("Connection " + format(entry) + " can be kept alive " + s);
/*     */           } 
/*     */         } 
/*     */       } finally {
/* 318 */         this.pool.release(entry, (conn.isOpen() && entry.isRouteComplete()));
/* 319 */         if (this.log.isDebugEnabled()) {
/* 320 */           this.log.debug("Connection released: " + format(entry) + formatStats((HttpRoute)entry.getRoute()));
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void connect(HttpClientConnection managedConn, HttpRoute route, int connectTimeout, HttpContext context) throws IOException {
/*     */     ManagedHttpClientConnection conn;
/*     */     HttpHost host;
/* 332 */     Args.notNull(managedConn, "Managed Connection");
/* 333 */     Args.notNull(route, "HTTP route");
/*     */     
/* 335 */     synchronized (managedConn) {
/* 336 */       CPoolEntry entry = CPoolProxy.getPoolEntry(managedConn);
/* 337 */       conn = (ManagedHttpClientConnection)entry.getConnection();
/*     */     } 
/*     */     
/* 340 */     if (route.getProxyHost() != null) {
/* 341 */       host = route.getProxyHost();
/*     */     } else {
/* 343 */       host = route.getTargetHost();
/*     */     } 
/* 345 */     InetSocketAddress localAddress = route.getLocalSocketAddress();
/* 346 */     SocketConfig socketConfig = this.configData.getSocketConfig(host);
/* 347 */     if (socketConfig == null) {
/* 348 */       socketConfig = this.configData.getDefaultSocketConfig();
/*     */     }
/* 350 */     if (socketConfig == null) {
/* 351 */       socketConfig = SocketConfig.DEFAULT;
/*     */     }
/* 353 */     this.connectionOperator.connect(conn, host, localAddress, connectTimeout, socketConfig, context);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void upgrade(HttpClientConnection managedConn, HttpRoute route, HttpContext context) throws IOException {
/*     */     ManagedHttpClientConnection conn;
/* 362 */     Args.notNull(managedConn, "Managed Connection");
/* 363 */     Args.notNull(route, "HTTP route");
/*     */     
/* 365 */     synchronized (managedConn) {
/* 366 */       CPoolEntry entry = CPoolProxy.getPoolEntry(managedConn);
/* 367 */       conn = (ManagedHttpClientConnection)entry.getConnection();
/*     */     } 
/* 369 */     this.connectionOperator.upgrade(conn, route.getTargetHost(), context);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void routeComplete(HttpClientConnection managedConn, HttpRoute route, HttpContext context) throws IOException {
/* 377 */     Args.notNull(managedConn, "Managed Connection");
/* 378 */     Args.notNull(route, "HTTP route");
/* 379 */     synchronized (managedConn) {
/* 380 */       CPoolEntry entry = CPoolProxy.getPoolEntry(managedConn);
/* 381 */       entry.markRouteComplete();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void shutdown() {
/* 387 */     if (this.isShutDown.compareAndSet(false, true)) {
/* 388 */       this.log.debug("Connection manager is shutting down");
/*     */       try {
/* 390 */         this.pool.shutdown();
/* 391 */       } catch (IOException ex) {
/* 392 */         this.log.debug("I/O exception shutting down connection manager", ex);
/*     */       } 
/* 394 */       this.log.debug("Connection manager shut down");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void closeIdleConnections(long idleTimeout, TimeUnit tunit) {
/* 400 */     if (this.log.isDebugEnabled()) {
/* 401 */       this.log.debug("Closing connections idle longer than " + idleTimeout + " " + tunit);
/*     */     }
/* 403 */     this.pool.closeIdle(idleTimeout, tunit);
/*     */   }
/*     */ 
/*     */   
/*     */   public void closeExpiredConnections() {
/* 408 */     this.log.debug("Closing expired connections");
/* 409 */     this.pool.closeExpired();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxTotal() {
/* 414 */     return this.pool.getMaxTotal();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMaxTotal(int max) {
/* 419 */     this.pool.setMaxTotal(max);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDefaultMaxPerRoute() {
/* 424 */     return this.pool.getDefaultMaxPerRoute();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDefaultMaxPerRoute(int max) {
/* 429 */     this.pool.setDefaultMaxPerRoute(max);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxPerRoute(HttpRoute route) {
/* 434 */     return this.pool.getMaxPerRoute(route);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMaxPerRoute(HttpRoute route, int max) {
/* 439 */     this.pool.setMaxPerRoute(route, max);
/*     */   }
/*     */ 
/*     */   
/*     */   public PoolStats getTotalStats() {
/* 444 */     return this.pool.getTotalStats();
/*     */   }
/*     */ 
/*     */   
/*     */   public PoolStats getStats(HttpRoute route) {
/* 449 */     return this.pool.getStats(route);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<HttpRoute> getRoutes() {
/* 456 */     return this.pool.getRoutes();
/*     */   }
/*     */   
/*     */   public SocketConfig getDefaultSocketConfig() {
/* 460 */     return this.configData.getDefaultSocketConfig();
/*     */   }
/*     */   
/*     */   public void setDefaultSocketConfig(SocketConfig defaultSocketConfig) {
/* 464 */     this.configData.setDefaultSocketConfig(defaultSocketConfig);
/*     */   }
/*     */   
/*     */   public ConnectionConfig getDefaultConnectionConfig() {
/* 468 */     return this.configData.getDefaultConnectionConfig();
/*     */   }
/*     */   
/*     */   public void setDefaultConnectionConfig(ConnectionConfig defaultConnectionConfig) {
/* 472 */     this.configData.setDefaultConnectionConfig(defaultConnectionConfig);
/*     */   }
/*     */   
/*     */   public SocketConfig getSocketConfig(HttpHost host) {
/* 476 */     return this.configData.getSocketConfig(host);
/*     */   }
/*     */   
/*     */   public void setSocketConfig(HttpHost host, SocketConfig socketConfig) {
/* 480 */     this.configData.setSocketConfig(host, socketConfig);
/*     */   }
/*     */   
/*     */   public ConnectionConfig getConnectionConfig(HttpHost host) {
/* 484 */     return this.configData.getConnectionConfig(host);
/*     */   }
/*     */   
/*     */   public void setConnectionConfig(HttpHost host, ConnectionConfig connectionConfig) {
/* 488 */     this.configData.setConnectionConfig(host, connectionConfig);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getValidateAfterInactivity() {
/* 497 */     return this.pool.getValidateAfterInactivity();
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
/*     */   public void setValidateAfterInactivity(int ms) {
/* 512 */     this.pool.setValidateAfterInactivity(ms);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class ConfigData
/*     */   {
/* 524 */     private final Map<HttpHost, SocketConfig> socketConfigMap = new ConcurrentHashMap<HttpHost, SocketConfig>();
/* 525 */     private final Map<HttpHost, ConnectionConfig> connectionConfigMap = new ConcurrentHashMap<HttpHost, ConnectionConfig>();
/*     */     private volatile SocketConfig defaultSocketConfig;
/*     */     
/*     */     public SocketConfig getDefaultSocketConfig() {
/* 529 */       return this.defaultSocketConfig;
/*     */     }
/*     */     private volatile ConnectionConfig defaultConnectionConfig;
/*     */     public void setDefaultSocketConfig(SocketConfig defaultSocketConfig) {
/* 533 */       this.defaultSocketConfig = defaultSocketConfig;
/*     */     }
/*     */     
/*     */     public ConnectionConfig getDefaultConnectionConfig() {
/* 537 */       return this.defaultConnectionConfig;
/*     */     }
/*     */     
/*     */     public void setDefaultConnectionConfig(ConnectionConfig defaultConnectionConfig) {
/* 541 */       this.defaultConnectionConfig = defaultConnectionConfig;
/*     */     }
/*     */     
/*     */     public SocketConfig getSocketConfig(HttpHost host) {
/* 545 */       return this.socketConfigMap.get(host);
/*     */     }
/*     */     
/*     */     public void setSocketConfig(HttpHost host, SocketConfig socketConfig) {
/* 549 */       this.socketConfigMap.put(host, socketConfig);
/*     */     }
/*     */     
/*     */     public ConnectionConfig getConnectionConfig(HttpHost host) {
/* 553 */       return this.connectionConfigMap.get(host);
/*     */     }
/*     */     
/*     */     public void setConnectionConfig(HttpHost host, ConnectionConfig connectionConfig) {
/* 557 */       this.connectionConfigMap.put(host, connectionConfig);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static class InternalConnectionFactory
/*     */     implements ConnFactory<HttpRoute, ManagedHttpClientConnection>
/*     */   {
/*     */     private final PoolingHttpClientConnectionManager.ConfigData configData;
/*     */     
/*     */     private final HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory;
/*     */ 
/*     */     
/*     */     InternalConnectionFactory(PoolingHttpClientConnectionManager.ConfigData configData, HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory) {
/* 571 */       this.configData = (configData != null) ? configData : new PoolingHttpClientConnectionManager.ConfigData();
/* 572 */       this.connFactory = (connFactory != null) ? connFactory : ManagedHttpClientConnectionFactory.INSTANCE;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public ManagedHttpClientConnection create(HttpRoute route) throws IOException {
/* 578 */       ConnectionConfig config = null;
/* 579 */       if (route.getProxyHost() != null) {
/* 580 */         config = this.configData.getConnectionConfig(route.getProxyHost());
/*     */       }
/* 582 */       if (config == null) {
/* 583 */         config = this.configData.getConnectionConfig(route.getTargetHost());
/*     */       }
/* 585 */       if (config == null) {
/* 586 */         config = this.configData.getDefaultConnectionConfig();
/*     */       }
/* 588 */       if (config == null) {
/* 589 */         config = ConnectionConfig.DEFAULT;
/*     */       }
/* 591 */       return (ManagedHttpClientConnection)this.connFactory.create(route, config);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\conn\PoolingHttpClientConnectionManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */