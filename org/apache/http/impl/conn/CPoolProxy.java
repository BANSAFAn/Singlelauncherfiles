/*     */ package org.apache.http.impl.conn;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.Socket;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import org.apache.http.HttpClientConnection;
/*     */ import org.apache.http.HttpConnectionMetrics;
/*     */ import org.apache.http.HttpEntityEnclosingRequest;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.conn.ManagedHttpClientConnection;
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
/*     */ @NotThreadSafe
/*     */ class CPoolProxy
/*     */   implements ManagedHttpClientConnection, HttpContext
/*     */ {
/*     */   private volatile CPoolEntry poolEntry;
/*     */   
/*     */   CPoolProxy(CPoolEntry entry) {
/*  55 */     this.poolEntry = entry;
/*     */   }
/*     */   
/*     */   CPoolEntry getPoolEntry() {
/*  59 */     return this.poolEntry;
/*     */   }
/*     */   
/*     */   CPoolEntry detach() {
/*  63 */     CPoolEntry local = this.poolEntry;
/*  64 */     this.poolEntry = null;
/*  65 */     return local;
/*     */   }
/*     */   
/*     */   ManagedHttpClientConnection getConnection() {
/*  69 */     CPoolEntry local = this.poolEntry;
/*  70 */     if (local == null) {
/*  71 */       return null;
/*     */     }
/*  73 */     return (ManagedHttpClientConnection)local.getConnection();
/*     */   }
/*     */   
/*     */   ManagedHttpClientConnection getValidConnection() {
/*  77 */     ManagedHttpClientConnection conn = getConnection();
/*  78 */     if (conn == null) {
/*  79 */       throw new ConnectionShutdownException();
/*     */     }
/*  81 */     return conn;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*  86 */     CPoolEntry local = this.poolEntry;
/*  87 */     if (local != null) {
/*  88 */       local.closeConnection();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void shutdown() throws IOException {
/*  94 */     CPoolEntry local = this.poolEntry;
/*  95 */     if (local != null) {
/*  96 */       local.shutdownConnection();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpen() {
/* 102 */     CPoolEntry local = this.poolEntry;
/* 103 */     if (local != null) {
/* 104 */       return !local.isClosed();
/*     */     }
/* 106 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStale() {
/* 112 */     ManagedHttpClientConnection managedHttpClientConnection = getConnection();
/* 113 */     if (managedHttpClientConnection != null) {
/* 114 */       return managedHttpClientConnection.isStale();
/*     */     }
/* 116 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSocketTimeout(int timeout) {
/* 122 */     getValidConnection().setSocketTimeout(timeout);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSocketTimeout() {
/* 127 */     return getValidConnection().getSocketTimeout();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getId() {
/* 132 */     return getValidConnection().getId();
/*     */   }
/*     */ 
/*     */   
/*     */   public void bind(Socket socket) throws IOException {
/* 137 */     getValidConnection().bind(socket);
/*     */   }
/*     */ 
/*     */   
/*     */   public Socket getSocket() {
/* 142 */     return getValidConnection().getSocket();
/*     */   }
/*     */ 
/*     */   
/*     */   public SSLSession getSSLSession() {
/* 147 */     return getValidConnection().getSSLSession();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isResponseAvailable(int timeout) throws IOException {
/* 152 */     return getValidConnection().isResponseAvailable(timeout);
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendRequestHeader(HttpRequest request) throws HttpException, IOException {
/* 157 */     getValidConnection().sendRequestHeader(request);
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendRequestEntity(HttpEntityEnclosingRequest request) throws HttpException, IOException {
/* 162 */     getValidConnection().sendRequestEntity(request);
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpResponse receiveResponseHeader() throws HttpException, IOException {
/* 167 */     return getValidConnection().receiveResponseHeader();
/*     */   }
/*     */ 
/*     */   
/*     */   public void receiveResponseEntity(HttpResponse response) throws HttpException, IOException {
/* 172 */     getValidConnection().receiveResponseEntity(response);
/*     */   }
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/* 177 */     getValidConnection().flush();
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpConnectionMetrics getMetrics() {
/* 182 */     return getValidConnection().getMetrics();
/*     */   }
/*     */ 
/*     */   
/*     */   public InetAddress getLocalAddress() {
/* 187 */     return getValidConnection().getLocalAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLocalPort() {
/* 192 */     return getValidConnection().getLocalPort();
/*     */   }
/*     */ 
/*     */   
/*     */   public InetAddress getRemoteAddress() {
/* 197 */     return getValidConnection().getRemoteAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRemotePort() {
/* 202 */     return getValidConnection().getRemotePort();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getAttribute(String id) {
/* 207 */     ManagedHttpClientConnection conn = getValidConnection();
/* 208 */     if (conn instanceof HttpContext) {
/* 209 */       return ((HttpContext)conn).getAttribute(id);
/*     */     }
/* 211 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAttribute(String id, Object obj) {
/* 217 */     ManagedHttpClientConnection conn = getValidConnection();
/* 218 */     if (conn instanceof HttpContext) {
/* 219 */       ((HttpContext)conn).setAttribute(id, obj);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Object removeAttribute(String id) {
/* 225 */     ManagedHttpClientConnection conn = getValidConnection();
/* 226 */     if (conn instanceof HttpContext) {
/* 227 */       return ((HttpContext)conn).removeAttribute(id);
/*     */     }
/* 229 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 235 */     StringBuilder sb = new StringBuilder("CPoolProxy{");
/* 236 */     ManagedHttpClientConnection conn = getConnection();
/* 237 */     if (conn != null) {
/* 238 */       sb.append(conn);
/*     */     } else {
/* 240 */       sb.append("detached");
/*     */     } 
/* 242 */     sb.append('}');
/* 243 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public static HttpClientConnection newProxy(CPoolEntry poolEntry) {
/* 247 */     return (HttpClientConnection)new CPoolProxy(poolEntry);
/*     */   }
/*     */   
/*     */   private static CPoolProxy getProxy(HttpClientConnection conn) {
/* 251 */     if (!CPoolProxy.class.isInstance(conn)) {
/* 252 */       throw new IllegalStateException("Unexpected connection proxy class: " + conn.getClass());
/*     */     }
/* 254 */     return CPoolProxy.class.cast(conn);
/*     */   }
/*     */   
/*     */   public static CPoolEntry getPoolEntry(HttpClientConnection proxy) {
/* 258 */     CPoolEntry entry = getProxy(proxy).getPoolEntry();
/* 259 */     if (entry == null) {
/* 260 */       throw new ConnectionShutdownException();
/*     */     }
/* 262 */     return entry;
/*     */   }
/*     */   
/*     */   public static CPoolEntry detach(HttpClientConnection conn) {
/* 266 */     return getProxy(conn).detach();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\conn\CPoolProxy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */