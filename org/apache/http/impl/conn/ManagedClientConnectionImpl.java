/*     */ package org.apache.http.impl.conn;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.Socket;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import javax.net.ssl.SSLSocket;
/*     */ import org.apache.http.HttpConnectionMetrics;
/*     */ import org.apache.http.HttpEntityEnclosingRequest;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.conn.ClientConnectionManager;
/*     */ import org.apache.http.conn.ClientConnectionOperator;
/*     */ import org.apache.http.conn.ManagedClientConnection;
/*     */ import org.apache.http.conn.OperatedClientConnection;
/*     */ import org.apache.http.conn.routing.HttpRoute;
/*     */ import org.apache.http.conn.routing.RouteTracker;
/*     */ import org.apache.http.params.HttpParams;
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
/*     */ @Deprecated
/*     */ @NotThreadSafe
/*     */ class ManagedClientConnectionImpl
/*     */   implements ManagedClientConnection
/*     */ {
/*     */   private final ClientConnectionManager manager;
/*     */   private final ClientConnectionOperator operator;
/*     */   private volatile HttpPoolEntry poolEntry;
/*     */   private volatile boolean reusable;
/*     */   private volatile long duration;
/*     */   
/*     */   ManagedClientConnectionImpl(ClientConnectionManager manager, ClientConnectionOperator operator, HttpPoolEntry entry) {
/*  76 */     Args.notNull(manager, "Connection manager");
/*  77 */     Args.notNull(operator, "Connection operator");
/*  78 */     Args.notNull(entry, "HTTP pool entry");
/*  79 */     this.manager = manager;
/*  80 */     this.operator = operator;
/*  81 */     this.poolEntry = entry;
/*  82 */     this.reusable = false;
/*  83 */     this.duration = Long.MAX_VALUE;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getId() {
/*  88 */     return null;
/*     */   }
/*     */   
/*     */   HttpPoolEntry getPoolEntry() {
/*  92 */     return this.poolEntry;
/*     */   }
/*     */   
/*     */   HttpPoolEntry detach() {
/*  96 */     HttpPoolEntry local = this.poolEntry;
/*  97 */     this.poolEntry = null;
/*  98 */     return local;
/*     */   }
/*     */   
/*     */   public ClientConnectionManager getManager() {
/* 102 */     return this.manager;
/*     */   }
/*     */   
/*     */   private OperatedClientConnection getConnection() {
/* 106 */     HttpPoolEntry local = this.poolEntry;
/* 107 */     if (local == null) {
/* 108 */       return null;
/*     */     }
/* 110 */     return (OperatedClientConnection)local.getConnection();
/*     */   }
/*     */   
/*     */   private OperatedClientConnection ensureConnection() {
/* 114 */     HttpPoolEntry local = this.poolEntry;
/* 115 */     if (local == null) {
/* 116 */       throw new ConnectionShutdownException();
/*     */     }
/* 118 */     return (OperatedClientConnection)local.getConnection();
/*     */   }
/*     */   
/*     */   private HttpPoolEntry ensurePoolEntry() {
/* 122 */     HttpPoolEntry local = this.poolEntry;
/* 123 */     if (local == null) {
/* 124 */       throw new ConnectionShutdownException();
/*     */     }
/* 126 */     return local;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 131 */     HttpPoolEntry local = this.poolEntry;
/* 132 */     if (local != null) {
/* 133 */       OperatedClientConnection conn = (OperatedClientConnection)local.getConnection();
/* 134 */       local.getTracker().reset();
/* 135 */       conn.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void shutdown() throws IOException {
/* 141 */     HttpPoolEntry local = this.poolEntry;
/* 142 */     if (local != null) {
/* 143 */       OperatedClientConnection conn = (OperatedClientConnection)local.getConnection();
/* 144 */       local.getTracker().reset();
/* 145 */       conn.shutdown();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpen() {
/* 151 */     OperatedClientConnection conn = getConnection();
/* 152 */     if (conn != null) {
/* 153 */       return conn.isOpen();
/*     */     }
/* 155 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStale() {
/* 161 */     OperatedClientConnection conn = getConnection();
/* 162 */     if (conn != null) {
/* 163 */       return conn.isStale();
/*     */     }
/* 165 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSocketTimeout(int timeout) {
/* 171 */     OperatedClientConnection conn = ensureConnection();
/* 172 */     conn.setSocketTimeout(timeout);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSocketTimeout() {
/* 177 */     OperatedClientConnection conn = ensureConnection();
/* 178 */     return conn.getSocketTimeout();
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpConnectionMetrics getMetrics() {
/* 183 */     OperatedClientConnection conn = ensureConnection();
/* 184 */     return conn.getMetrics();
/*     */   }
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/* 189 */     OperatedClientConnection conn = ensureConnection();
/* 190 */     conn.flush();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isResponseAvailable(int timeout) throws IOException {
/* 195 */     OperatedClientConnection conn = ensureConnection();
/* 196 */     return conn.isResponseAvailable(timeout);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void receiveResponseEntity(HttpResponse response) throws HttpException, IOException {
/* 202 */     OperatedClientConnection conn = ensureConnection();
/* 203 */     conn.receiveResponseEntity(response);
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpResponse receiveResponseHeader() throws HttpException, IOException {
/* 208 */     OperatedClientConnection conn = ensureConnection();
/* 209 */     return conn.receiveResponseHeader();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendRequestEntity(HttpEntityEnclosingRequest request) throws HttpException, IOException {
/* 215 */     OperatedClientConnection conn = ensureConnection();
/* 216 */     conn.sendRequestEntity(request);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendRequestHeader(HttpRequest request) throws HttpException, IOException {
/* 222 */     OperatedClientConnection conn = ensureConnection();
/* 223 */     conn.sendRequestHeader(request);
/*     */   }
/*     */ 
/*     */   
/*     */   public InetAddress getLocalAddress() {
/* 228 */     OperatedClientConnection conn = ensureConnection();
/* 229 */     return conn.getLocalAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLocalPort() {
/* 234 */     OperatedClientConnection conn = ensureConnection();
/* 235 */     return conn.getLocalPort();
/*     */   }
/*     */ 
/*     */   
/*     */   public InetAddress getRemoteAddress() {
/* 240 */     OperatedClientConnection conn = ensureConnection();
/* 241 */     return conn.getRemoteAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRemotePort() {
/* 246 */     OperatedClientConnection conn = ensureConnection();
/* 247 */     return conn.getRemotePort();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSecure() {
/* 252 */     OperatedClientConnection conn = ensureConnection();
/* 253 */     return conn.isSecure();
/*     */   }
/*     */ 
/*     */   
/*     */   public void bind(Socket socket) throws IOException {
/* 258 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public Socket getSocket() {
/* 263 */     OperatedClientConnection conn = ensureConnection();
/* 264 */     return conn.getSocket();
/*     */   }
/*     */ 
/*     */   
/*     */   public SSLSession getSSLSession() {
/* 269 */     OperatedClientConnection conn = ensureConnection();
/* 270 */     SSLSession result = null;
/* 271 */     Socket sock = conn.getSocket();
/* 272 */     if (sock instanceof SSLSocket) {
/* 273 */       result = ((SSLSocket)sock).getSession();
/*     */     }
/* 275 */     return result;
/*     */   }
/*     */   
/*     */   public Object getAttribute(String id) {
/* 279 */     OperatedClientConnection conn = ensureConnection();
/* 280 */     if (conn instanceof HttpContext) {
/* 281 */       return ((HttpContext)conn).getAttribute(id);
/*     */     }
/* 283 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object removeAttribute(String id) {
/* 288 */     OperatedClientConnection conn = ensureConnection();
/* 289 */     if (conn instanceof HttpContext) {
/* 290 */       return ((HttpContext)conn).removeAttribute(id);
/*     */     }
/* 292 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAttribute(String id, Object obj) {
/* 297 */     OperatedClientConnection conn = ensureConnection();
/* 298 */     if (conn instanceof HttpContext) {
/* 299 */       ((HttpContext)conn).setAttribute(id, obj);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpRoute getRoute() {
/* 305 */     HttpPoolEntry local = ensurePoolEntry();
/* 306 */     return local.getEffectiveRoute();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void open(HttpRoute route, HttpContext context, HttpParams params) throws IOException {
/*     */     OperatedClientConnection conn;
/* 314 */     Args.notNull(route, "Route");
/* 315 */     Args.notNull(params, "HTTP parameters");
/*     */     
/* 317 */     synchronized (this) {
/* 318 */       if (this.poolEntry == null) {
/* 319 */         throw new ConnectionShutdownException();
/*     */       }
/* 321 */       RouteTracker tracker = this.poolEntry.getTracker();
/* 322 */       Asserts.notNull(tracker, "Route tracker");
/* 323 */       Asserts.check(!tracker.isConnected(), "Connection already open");
/* 324 */       conn = (OperatedClientConnection)this.poolEntry.getConnection();
/*     */     } 
/*     */     
/* 327 */     HttpHost proxy = route.getProxyHost();
/* 328 */     this.operator.openConnection(conn, (proxy != null) ? proxy : route.getTargetHost(), route.getLocalAddress(), context, params);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 334 */     synchronized (this) {
/* 335 */       if (this.poolEntry == null) {
/* 336 */         throw new InterruptedIOException();
/*     */       }
/* 338 */       RouteTracker tracker = this.poolEntry.getTracker();
/* 339 */       if (proxy == null) {
/* 340 */         tracker.connectTarget(conn.isSecure());
/*     */       } else {
/* 342 */         tracker.connectProxy(proxy, conn.isSecure());
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void tunnelTarget(boolean secure, HttpParams params) throws IOException {
/*     */     HttpHost target;
/*     */     OperatedClientConnection conn;
/* 350 */     Args.notNull(params, "HTTP parameters");
/*     */ 
/*     */     
/* 353 */     synchronized (this) {
/* 354 */       if (this.poolEntry == null) {
/* 355 */         throw new ConnectionShutdownException();
/*     */       }
/* 357 */       RouteTracker tracker = this.poolEntry.getTracker();
/* 358 */       Asserts.notNull(tracker, "Route tracker");
/* 359 */       Asserts.check(tracker.isConnected(), "Connection not open");
/* 360 */       Asserts.check(!tracker.isTunnelled(), "Connection is already tunnelled");
/* 361 */       target = tracker.getTargetHost();
/* 362 */       conn = (OperatedClientConnection)this.poolEntry.getConnection();
/*     */     } 
/*     */     
/* 365 */     conn.update(null, target, secure, params);
/*     */     
/* 367 */     synchronized (this) {
/* 368 */       if (this.poolEntry == null) {
/* 369 */         throw new InterruptedIOException();
/*     */       }
/* 371 */       RouteTracker tracker = this.poolEntry.getTracker();
/* 372 */       tracker.tunnelTarget(secure);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void tunnelProxy(HttpHost next, boolean secure, HttpParams params) throws IOException {
/*     */     OperatedClientConnection conn;
/* 379 */     Args.notNull(next, "Next proxy");
/* 380 */     Args.notNull(params, "HTTP parameters");
/*     */     
/* 382 */     synchronized (this) {
/* 383 */       if (this.poolEntry == null) {
/* 384 */         throw new ConnectionShutdownException();
/*     */       }
/* 386 */       RouteTracker tracker = this.poolEntry.getTracker();
/* 387 */       Asserts.notNull(tracker, "Route tracker");
/* 388 */       Asserts.check(tracker.isConnected(), "Connection not open");
/* 389 */       conn = (OperatedClientConnection)this.poolEntry.getConnection();
/*     */     } 
/*     */     
/* 392 */     conn.update(null, next, secure, params);
/*     */     
/* 394 */     synchronized (this) {
/* 395 */       if (this.poolEntry == null) {
/* 396 */         throw new InterruptedIOException();
/*     */       }
/* 398 */       RouteTracker tracker = this.poolEntry.getTracker();
/* 399 */       tracker.tunnelProxy(next, secure);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void layerProtocol(HttpContext context, HttpParams params) throws IOException {
/*     */     HttpHost target;
/*     */     OperatedClientConnection conn;
/* 406 */     Args.notNull(params, "HTTP parameters");
/*     */ 
/*     */     
/* 409 */     synchronized (this) {
/* 410 */       if (this.poolEntry == null) {
/* 411 */         throw new ConnectionShutdownException();
/*     */       }
/* 413 */       RouteTracker tracker = this.poolEntry.getTracker();
/* 414 */       Asserts.notNull(tracker, "Route tracker");
/* 415 */       Asserts.check(tracker.isConnected(), "Connection not open");
/* 416 */       Asserts.check(tracker.isTunnelled(), "Protocol layering without a tunnel not supported");
/* 417 */       Asserts.check(!tracker.isLayered(), "Multiple protocol layering not supported");
/* 418 */       target = tracker.getTargetHost();
/* 419 */       conn = (OperatedClientConnection)this.poolEntry.getConnection();
/*     */     } 
/* 421 */     this.operator.updateSecureConnection(conn, target, context, params);
/*     */     
/* 423 */     synchronized (this) {
/* 424 */       if (this.poolEntry == null) {
/* 425 */         throw new InterruptedIOException();
/*     */       }
/* 427 */       RouteTracker tracker = this.poolEntry.getTracker();
/* 428 */       tracker.layerProtocol(conn.isSecure());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getState() {
/* 434 */     HttpPoolEntry local = ensurePoolEntry();
/* 435 */     return local.getState();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setState(Object state) {
/* 440 */     HttpPoolEntry local = ensurePoolEntry();
/* 441 */     local.setState(state);
/*     */   }
/*     */ 
/*     */   
/*     */   public void markReusable() {
/* 446 */     this.reusable = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void unmarkReusable() {
/* 451 */     this.reusable = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isMarkedReusable() {
/* 456 */     return this.reusable;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setIdleDuration(long duration, TimeUnit unit) {
/* 461 */     if (duration > 0L) {
/* 462 */       this.duration = unit.toMillis(duration);
/*     */     } else {
/* 464 */       this.duration = -1L;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void releaseConnection() {
/* 470 */     synchronized (this) {
/* 471 */       if (this.poolEntry == null) {
/*     */         return;
/*     */       }
/* 474 */       this.manager.releaseConnection(this, this.duration, TimeUnit.MILLISECONDS);
/* 475 */       this.poolEntry = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void abortConnection() {
/* 481 */     synchronized (this) {
/* 482 */       if (this.poolEntry == null) {
/*     */         return;
/*     */       }
/* 485 */       this.reusable = false;
/* 486 */       OperatedClientConnection conn = (OperatedClientConnection)this.poolEntry.getConnection();
/*     */       try {
/* 488 */         conn.shutdown();
/* 489 */       } catch (IOException ignore) {}
/*     */       
/* 491 */       this.manager.releaseConnection(this, this.duration, TimeUnit.MILLISECONDS);
/* 492 */       this.poolEntry = null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\conn\ManagedClientConnectionImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */