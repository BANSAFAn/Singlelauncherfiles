/*     */ package org.apache.http.impl.conn;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.util.Date;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.HttpClientConnection;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.annotation.GuardedBy;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.config.ConnectionConfig;
/*     */ import org.apache.http.config.Lookup;
/*     */ import org.apache.http.config.Registry;
/*     */ import org.apache.http.config.RegistryBuilder;
/*     */ import org.apache.http.config.SocketConfig;
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
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.Asserts;
/*     */ import org.apache.http.util.LangUtils;
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
/*     */ public class BasicHttpClientConnectionManager
/*     */   implements HttpClientConnectionManager, Closeable
/*     */ {
/*  85 */   private final Log log = LogFactory.getLog(getClass());
/*     */   
/*     */   private final HttpClientConnectionOperator connectionOperator;
/*     */   
/*     */   private final HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory;
/*     */   
/*     */   @GuardedBy("this")
/*     */   private ManagedHttpClientConnection conn;
/*     */   
/*     */   @GuardedBy("this")
/*     */   private HttpRoute route;
/*     */   
/*     */   @GuardedBy("this")
/*     */   private Object state;
/*     */   
/*     */   @GuardedBy("this")
/*     */   private long updated;
/*     */   
/*     */   @GuardedBy("this")
/*     */   private long expiry;
/*     */   
/*     */   @GuardedBy("this")
/*     */   private boolean leased;
/*     */   
/*     */   @GuardedBy("this")
/*     */   private SocketConfig socketConfig;
/*     */   
/*     */   @GuardedBy("this")
/*     */   private ConnectionConfig connConfig;
/*     */   private final AtomicBoolean isShutdown;
/*     */   
/*     */   private static Registry<ConnectionSocketFactory> getDefaultRegistry() {
/* 117 */     return RegistryBuilder.create().register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", SSLConnectionSocketFactory.getSocketFactory()).build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicHttpClientConnectionManager(Lookup<ConnectionSocketFactory> socketFactoryRegistry, HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory, SchemePortResolver schemePortResolver, DnsResolver dnsResolver) {
/* 128 */     this(new DefaultHttpClientConnectionOperator(socketFactoryRegistry, schemePortResolver, dnsResolver), connFactory);
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
/*     */   public BasicHttpClientConnectionManager(HttpClientConnectionOperator httpClientConnectionOperator, HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory) {
/* 141 */     this.connectionOperator = (HttpClientConnectionOperator)Args.notNull(httpClientConnectionOperator, "Connection operator");
/* 142 */     this.connFactory = (connFactory != null) ? connFactory : ManagedHttpClientConnectionFactory.INSTANCE;
/* 143 */     this.expiry = Long.MAX_VALUE;
/* 144 */     this.socketConfig = SocketConfig.DEFAULT;
/* 145 */     this.connConfig = ConnectionConfig.DEFAULT;
/* 146 */     this.isShutdown = new AtomicBoolean(false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicHttpClientConnectionManager(Lookup<ConnectionSocketFactory> socketFactoryRegistry, HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory) {
/* 152 */     this(socketFactoryRegistry, connFactory, null, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public BasicHttpClientConnectionManager(Lookup<ConnectionSocketFactory> socketFactoryRegistry) {
/* 157 */     this(socketFactoryRegistry, null, null, null);
/*     */   }
/*     */   
/*     */   public BasicHttpClientConnectionManager() {
/* 161 */     this((Lookup<ConnectionSocketFactory>)getDefaultRegistry(), null, null, null);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void finalize() throws Throwable {
/*     */     try {
/* 167 */       shutdown();
/*     */     } finally {
/* 169 */       super.finalize();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 175 */     shutdown();
/*     */   }
/*     */   
/*     */   HttpRoute getRoute() {
/* 179 */     return this.route;
/*     */   }
/*     */   
/*     */   Object getState() {
/* 183 */     return this.state;
/*     */   }
/*     */   
/*     */   public synchronized SocketConfig getSocketConfig() {
/* 187 */     return this.socketConfig;
/*     */   }
/*     */   
/*     */   public synchronized void setSocketConfig(SocketConfig socketConfig) {
/* 191 */     this.socketConfig = (socketConfig != null) ? socketConfig : SocketConfig.DEFAULT;
/*     */   }
/*     */   
/*     */   public synchronized ConnectionConfig getConnectionConfig() {
/* 195 */     return this.connConfig;
/*     */   }
/*     */   
/*     */   public synchronized void setConnectionConfig(ConnectionConfig connConfig) {
/* 199 */     this.connConfig = (connConfig != null) ? connConfig : ConnectionConfig.DEFAULT;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ConnectionRequest requestConnection(final HttpRoute route, final Object state) {
/* 206 */     Args.notNull(route, "Route");
/* 207 */     return new ConnectionRequest()
/*     */       {
/*     */         
/*     */         public boolean cancel()
/*     */         {
/* 212 */           return false;
/*     */         }
/*     */ 
/*     */         
/*     */         public HttpClientConnection get(long timeout, TimeUnit tunit) {
/* 217 */           return BasicHttpClientConnectionManager.this.getConnection(route, state);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void closeConnection() {
/* 225 */     if (this.conn != null) {
/* 226 */       this.log.debug("Closing connection");
/*     */       try {
/* 228 */         this.conn.close();
/* 229 */       } catch (IOException iox) {
/* 230 */         if (this.log.isDebugEnabled()) {
/* 231 */           this.log.debug("I/O exception closing connection", iox);
/*     */         }
/*     */       } 
/* 234 */       this.conn = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void shutdownConnection() {
/* 239 */     if (this.conn != null) {
/* 240 */       this.log.debug("Shutting down connection");
/*     */       try {
/* 242 */         this.conn.shutdown();
/* 243 */       } catch (IOException iox) {
/* 244 */         if (this.log.isDebugEnabled()) {
/* 245 */           this.log.debug("I/O exception shutting down connection", iox);
/*     */         }
/*     */       } 
/* 248 */       this.conn = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void checkExpiry() {
/* 253 */     if (this.conn != null && System.currentTimeMillis() >= this.expiry) {
/* 254 */       if (this.log.isDebugEnabled()) {
/* 255 */         this.log.debug("Connection expired @ " + new Date(this.expiry));
/*     */       }
/* 257 */       closeConnection();
/*     */     } 
/*     */   }
/*     */   
/*     */   synchronized HttpClientConnection getConnection(HttpRoute route, Object state) {
/* 262 */     Asserts.check(!this.isShutdown.get(), "Connection manager has been shut down");
/* 263 */     if (this.log.isDebugEnabled()) {
/* 264 */       this.log.debug("Get connection for route " + route);
/*     */     }
/* 266 */     Asserts.check(!this.leased, "Connection is still allocated");
/* 267 */     if (!LangUtils.equals(this.route, route) || !LangUtils.equals(this.state, state)) {
/* 268 */       closeConnection();
/*     */     }
/* 270 */     this.route = route;
/* 271 */     this.state = state;
/* 272 */     checkExpiry();
/* 273 */     if (this.conn == null) {
/* 274 */       this.conn = (ManagedHttpClientConnection)this.connFactory.create(route, this.connConfig);
/*     */     }
/* 276 */     this.leased = true;
/* 277 */     return (HttpClientConnection)this.conn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void releaseConnection(HttpClientConnection conn, Object state, long keepalive, TimeUnit tunit) {
/* 285 */     Args.notNull(conn, "Connection");
/* 286 */     Asserts.check((conn == this.conn), "Connection not obtained from this manager");
/* 287 */     if (this.log.isDebugEnabled()) {
/* 288 */       this.log.debug("Releasing connection " + conn);
/*     */     }
/* 290 */     if (this.isShutdown.get()) {
/*     */       return;
/*     */     }
/*     */     try {
/* 294 */       this.updated = System.currentTimeMillis();
/* 295 */       if (!this.conn.isOpen()) {
/* 296 */         this.conn = null;
/* 297 */         this.route = null;
/* 298 */         this.conn = null;
/* 299 */         this.expiry = Long.MAX_VALUE;
/*     */       } else {
/* 301 */         this.state = state;
/* 302 */         if (this.log.isDebugEnabled()) {
/*     */           String s;
/* 304 */           if (keepalive > 0L) {
/* 305 */             s = "for " + keepalive + " " + tunit;
/*     */           } else {
/* 307 */             s = "indefinitely";
/*     */           } 
/* 309 */           this.log.debug("Connection can be kept alive " + s);
/*     */         } 
/* 311 */         if (keepalive > 0L) {
/* 312 */           this.expiry = this.updated + tunit.toMillis(keepalive);
/*     */         } else {
/* 314 */           this.expiry = Long.MAX_VALUE;
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 318 */       this.leased = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void connect(HttpClientConnection conn, HttpRoute route, int connectTimeout, HttpContext context) throws IOException {
/*     */     HttpHost host;
/* 328 */     Args.notNull(conn, "Connection");
/* 329 */     Args.notNull(route, "HTTP route");
/* 330 */     Asserts.check((conn == this.conn), "Connection not obtained from this manager");
/*     */     
/* 332 */     if (route.getProxyHost() != null) {
/* 333 */       host = route.getProxyHost();
/*     */     } else {
/* 335 */       host = route.getTargetHost();
/*     */     } 
/* 337 */     InetSocketAddress localAddress = route.getLocalSocketAddress();
/* 338 */     this.connectionOperator.connect(this.conn, host, localAddress, connectTimeout, this.socketConfig, context);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void upgrade(HttpClientConnection conn, HttpRoute route, HttpContext context) throws IOException {
/* 347 */     Args.notNull(conn, "Connection");
/* 348 */     Args.notNull(route, "HTTP route");
/* 349 */     Asserts.check((conn == this.conn), "Connection not obtained from this manager");
/* 350 */     this.connectionOperator.upgrade(this.conn, route.getTargetHost(), context);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void routeComplete(HttpClientConnection conn, HttpRoute route, HttpContext context) throws IOException {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void closeExpiredConnections() {
/* 362 */     if (this.isShutdown.get()) {
/*     */       return;
/*     */     }
/* 365 */     if (!this.leased) {
/* 366 */       checkExpiry();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void closeIdleConnections(long idletime, TimeUnit tunit) {
/* 372 */     Args.notNull(tunit, "Time unit");
/* 373 */     if (this.isShutdown.get()) {
/*     */       return;
/*     */     }
/* 376 */     if (!this.leased) {
/* 377 */       long time = tunit.toMillis(idletime);
/* 378 */       if (time < 0L) {
/* 379 */         time = 0L;
/*     */       }
/* 381 */       long deadline = System.currentTimeMillis() - time;
/* 382 */       if (this.updated <= deadline) {
/* 383 */         closeConnection();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void shutdown() {
/* 390 */     if (this.isShutdown.compareAndSet(false, true))
/* 391 */       shutdownConnection(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\conn\BasicHttpClientConnectionManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */