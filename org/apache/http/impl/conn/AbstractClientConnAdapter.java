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
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.conn.ClientConnectionManager;
/*     */ import org.apache.http.conn.ManagedClientConnection;
/*     */ import org.apache.http.conn.OperatedClientConnection;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class AbstractClientConnAdapter
/*     */   implements ManagedClientConnection, HttpContext
/*     */ {
/*     */   private final ClientConnectionManager connManager;
/*     */   private volatile OperatedClientConnection wrappedConnection;
/*     */   private volatile boolean markedReusable;
/*     */   private volatile boolean released;
/*     */   private volatile long duration;
/*     */   
/*     */   protected AbstractClientConnAdapter(ClientConnectionManager mgr, OperatedClientConnection conn) {
/* 104 */     this.connManager = mgr;
/* 105 */     this.wrappedConnection = conn;
/* 106 */     this.markedReusable = false;
/* 107 */     this.released = false;
/* 108 */     this.duration = Long.MAX_VALUE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized void detach() {
/* 116 */     this.wrappedConnection = null;
/* 117 */     this.duration = Long.MAX_VALUE;
/*     */   }
/*     */   
/*     */   protected OperatedClientConnection getWrappedConnection() {
/* 121 */     return this.wrappedConnection;
/*     */   }
/*     */   
/*     */   protected ClientConnectionManager getManager() {
/* 125 */     return this.connManager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected final void assertNotAborted() throws InterruptedIOException {
/* 133 */     if (isReleased()) {
/* 134 */       throw new InterruptedIOException("Connection has been shut down");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isReleased() {
/* 143 */     return this.released;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void assertValid(OperatedClientConnection wrappedConn) throws ConnectionShutdownException {
/* 154 */     if (isReleased() || wrappedConn == null) {
/* 155 */       throw new ConnectionShutdownException();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpen() {
/* 161 */     OperatedClientConnection conn = getWrappedConnection();
/* 162 */     if (conn == null) {
/* 163 */       return false;
/*     */     }
/*     */     
/* 166 */     return conn.isOpen();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStale() {
/* 171 */     if (isReleased()) {
/* 172 */       return true;
/*     */     }
/* 174 */     OperatedClientConnection conn = getWrappedConnection();
/* 175 */     if (conn == null) {
/* 176 */       return true;
/*     */     }
/*     */     
/* 179 */     return conn.isStale();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSocketTimeout(int timeout) {
/* 184 */     OperatedClientConnection conn = getWrappedConnection();
/* 185 */     assertValid(conn);
/* 186 */     conn.setSocketTimeout(timeout);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSocketTimeout() {
/* 191 */     OperatedClientConnection conn = getWrappedConnection();
/* 192 */     assertValid(conn);
/* 193 */     return conn.getSocketTimeout();
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpConnectionMetrics getMetrics() {
/* 198 */     OperatedClientConnection conn = getWrappedConnection();
/* 199 */     assertValid(conn);
/* 200 */     return conn.getMetrics();
/*     */   }
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/* 205 */     OperatedClientConnection conn = getWrappedConnection();
/* 206 */     assertValid(conn);
/* 207 */     conn.flush();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isResponseAvailable(int timeout) throws IOException {
/* 212 */     OperatedClientConnection conn = getWrappedConnection();
/* 213 */     assertValid(conn);
/* 214 */     return conn.isResponseAvailable(timeout);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void receiveResponseEntity(HttpResponse response) throws HttpException, IOException {
/* 220 */     OperatedClientConnection conn = getWrappedConnection();
/* 221 */     assertValid(conn);
/* 222 */     unmarkReusable();
/* 223 */     conn.receiveResponseEntity(response);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpResponse receiveResponseHeader() throws HttpException, IOException {
/* 229 */     OperatedClientConnection conn = getWrappedConnection();
/* 230 */     assertValid(conn);
/* 231 */     unmarkReusable();
/* 232 */     return conn.receiveResponseHeader();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendRequestEntity(HttpEntityEnclosingRequest request) throws HttpException, IOException {
/* 238 */     OperatedClientConnection conn = getWrappedConnection();
/* 239 */     assertValid(conn);
/* 240 */     unmarkReusable();
/* 241 */     conn.sendRequestEntity(request);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendRequestHeader(HttpRequest request) throws HttpException, IOException {
/* 247 */     OperatedClientConnection conn = getWrappedConnection();
/* 248 */     assertValid(conn);
/* 249 */     unmarkReusable();
/* 250 */     conn.sendRequestHeader(request);
/*     */   }
/*     */ 
/*     */   
/*     */   public InetAddress getLocalAddress() {
/* 255 */     OperatedClientConnection conn = getWrappedConnection();
/* 256 */     assertValid(conn);
/* 257 */     return conn.getLocalAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLocalPort() {
/* 262 */     OperatedClientConnection conn = getWrappedConnection();
/* 263 */     assertValid(conn);
/* 264 */     return conn.getLocalPort();
/*     */   }
/*     */ 
/*     */   
/*     */   public InetAddress getRemoteAddress() {
/* 269 */     OperatedClientConnection conn = getWrappedConnection();
/* 270 */     assertValid(conn);
/* 271 */     return conn.getRemoteAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRemotePort() {
/* 276 */     OperatedClientConnection conn = getWrappedConnection();
/* 277 */     assertValid(conn);
/* 278 */     return conn.getRemotePort();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSecure() {
/* 283 */     OperatedClientConnection conn = getWrappedConnection();
/* 284 */     assertValid(conn);
/* 285 */     return conn.isSecure();
/*     */   }
/*     */ 
/*     */   
/*     */   public void bind(Socket socket) throws IOException {
/* 290 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public Socket getSocket() {
/* 295 */     OperatedClientConnection conn = getWrappedConnection();
/* 296 */     assertValid(conn);
/* 297 */     if (!isOpen()) {
/* 298 */       return null;
/*     */     }
/* 300 */     return conn.getSocket();
/*     */   }
/*     */ 
/*     */   
/*     */   public SSLSession getSSLSession() {
/* 305 */     OperatedClientConnection conn = getWrappedConnection();
/* 306 */     assertValid(conn);
/* 307 */     if (!isOpen()) {
/* 308 */       return null;
/*     */     }
/*     */     
/* 311 */     SSLSession result = null;
/* 312 */     Socket sock = conn.getSocket();
/* 313 */     if (sock instanceof SSLSocket) {
/* 314 */       result = ((SSLSocket)sock).getSession();
/*     */     }
/* 316 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public void markReusable() {
/* 321 */     this.markedReusable = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void unmarkReusable() {
/* 326 */     this.markedReusable = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isMarkedReusable() {
/* 331 */     return this.markedReusable;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setIdleDuration(long duration, TimeUnit unit) {
/* 336 */     if (duration > 0L) {
/* 337 */       this.duration = unit.toMillis(duration);
/*     */     } else {
/* 339 */       this.duration = -1L;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void releaseConnection() {
/* 345 */     if (this.released) {
/*     */       return;
/*     */     }
/* 348 */     this.released = true;
/* 349 */     this.connManager.releaseConnection(this, this.duration, TimeUnit.MILLISECONDS);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void abortConnection() {
/* 354 */     if (this.released) {
/*     */       return;
/*     */     }
/* 357 */     this.released = true;
/* 358 */     unmarkReusable();
/*     */     try {
/* 360 */       shutdown();
/* 361 */     } catch (IOException ignore) {}
/*     */     
/* 363 */     this.connManager.releaseConnection(this, this.duration, TimeUnit.MILLISECONDS);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getAttribute(String id) {
/* 368 */     OperatedClientConnection conn = getWrappedConnection();
/* 369 */     assertValid(conn);
/* 370 */     if (conn instanceof HttpContext) {
/* 371 */       return ((HttpContext)conn).getAttribute(id);
/*     */     }
/* 373 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object removeAttribute(String id) {
/* 379 */     OperatedClientConnection conn = getWrappedConnection();
/* 380 */     assertValid(conn);
/* 381 */     if (conn instanceof HttpContext) {
/* 382 */       return ((HttpContext)conn).removeAttribute(id);
/*     */     }
/* 384 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAttribute(String id, Object obj) {
/* 390 */     OperatedClientConnection conn = getWrappedConnection();
/* 391 */     assertValid(conn);
/* 392 */     if (conn instanceof HttpContext)
/* 393 */       ((HttpContext)conn).setAttribute(id, obj); 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\conn\AbstractClientConnAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */