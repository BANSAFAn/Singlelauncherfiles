/*     */ package org.apache.http.impl.conn;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.net.Socket;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import javax.net.ssl.SSLSocket;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.HttpResponseFactory;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.conn.ManagedHttpClientConnection;
/*     */ import org.apache.http.conn.OperatedClientConnection;
/*     */ import org.apache.http.impl.SocketHttpClientConnection;
/*     */ import org.apache.http.io.HttpMessageParser;
/*     */ import org.apache.http.io.SessionInputBuffer;
/*     */ import org.apache.http.io.SessionOutputBuffer;
/*     */ import org.apache.http.params.BasicHttpParams;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.params.HttpProtocolParams;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ import org.apache.http.util.Args;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class DefaultClientConnection
/*     */   extends SocketHttpClientConnection
/*     */   implements OperatedClientConnection, ManagedHttpClientConnection, HttpContext
/*     */ {
/*  72 */   private final Log log = LogFactory.getLog(getClass());
/*  73 */   private final Log headerLog = LogFactory.getLog("org.apache.http.headers");
/*  74 */   private final Log wireLog = LogFactory.getLog("org.apache.http.wire");
/*     */ 
/*     */   
/*     */   private volatile Socket socket;
/*     */ 
/*     */   
/*     */   private HttpHost targetHost;
/*     */ 
/*     */   
/*     */   private boolean connSecure;
/*     */ 
/*     */   
/*     */   private volatile boolean shutdown;
/*     */ 
/*     */   
/*     */   private final Map<String, Object> attributes;
/*     */ 
/*     */   
/*     */   public DefaultClientConnection() {
/*  93 */     this.attributes = new HashMap<String, Object>();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getId() {
/*  98 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public final HttpHost getTargetHost() {
/* 103 */     return this.targetHost;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isSecure() {
/* 108 */     return this.connSecure;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Socket getSocket() {
/* 113 */     return this.socket;
/*     */   }
/*     */ 
/*     */   
/*     */   public SSLSession getSSLSession() {
/* 118 */     if (this.socket instanceof SSLSocket) {
/* 119 */       return ((SSLSocket)this.socket).getSession();
/*     */     }
/* 121 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void opening(Socket sock, HttpHost target) throws IOException {
/* 127 */     assertNotOpen();
/* 128 */     this.socket = sock;
/* 129 */     this.targetHost = target;
/*     */ 
/*     */     
/* 132 */     if (this.shutdown) {
/* 133 */       sock.close();
/*     */       
/* 135 */       throw new InterruptedIOException("Connection already shutdown");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void openCompleted(boolean secure, HttpParams params) throws IOException {
/* 141 */     Args.notNull(params, "Parameters");
/* 142 */     assertNotOpen();
/* 143 */     this.connSecure = secure;
/* 144 */     bind(this.socket, params);
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
/*     */   public void shutdown() throws IOException {
/* 162 */     this.shutdown = true;
/*     */     try {
/* 164 */       super.shutdown();
/* 165 */       if (this.log.isDebugEnabled()) {
/* 166 */         this.log.debug("Connection " + this + " shut down");
/*     */       }
/* 168 */       Socket sock = this.socket;
/* 169 */       if (sock != null) {
/* 170 */         sock.close();
/*     */       }
/* 172 */     } catch (IOException ex) {
/* 173 */       this.log.debug("I/O error shutting down connection", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*     */     try {
/* 180 */       super.close();
/* 181 */       if (this.log.isDebugEnabled()) {
/* 182 */         this.log.debug("Connection " + this + " closed");
/*     */       }
/* 184 */     } catch (IOException ex) {
/* 185 */       this.log.debug("I/O error closing connection", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SessionInputBuffer createSessionInputBuffer(Socket socket, int buffersize, HttpParams params) throws IOException {
/* 194 */     SessionInputBuffer inbuffer = super.createSessionInputBuffer(socket, (buffersize > 0) ? buffersize : 8192, params);
/*     */ 
/*     */ 
/*     */     
/* 198 */     if (this.wireLog.isDebugEnabled()) {
/* 199 */       inbuffer = new LoggingSessionInputBuffer(inbuffer, new Wire(this.wireLog), HttpProtocolParams.getHttpElementCharset(params));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 204 */     return inbuffer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SessionOutputBuffer createSessionOutputBuffer(Socket socket, int buffersize, HttpParams params) throws IOException {
/* 212 */     SessionOutputBuffer outbuffer = super.createSessionOutputBuffer(socket, (buffersize > 0) ? buffersize : 8192, params);
/*     */ 
/*     */ 
/*     */     
/* 216 */     if (this.wireLog.isDebugEnabled()) {
/* 217 */       outbuffer = new LoggingSessionOutputBuffer(outbuffer, new Wire(this.wireLog), HttpProtocolParams.getHttpElementCharset(params));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 222 */     return outbuffer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected HttpMessageParser<HttpResponse> createResponseParser(SessionInputBuffer buffer, HttpResponseFactory responseFactory, HttpParams params) {
/* 231 */     return (HttpMessageParser<HttpResponse>)new DefaultHttpResponseParser(buffer, null, responseFactory, params);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void bind(Socket socket) throws IOException {
/* 237 */     bind(socket, (HttpParams)new BasicHttpParams());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void update(Socket sock, HttpHost target, boolean secure, HttpParams params) throws IOException {
/* 245 */     assertOpen();
/* 246 */     Args.notNull(target, "Target host");
/* 247 */     Args.notNull(params, "Parameters");
/*     */     
/* 249 */     if (sock != null) {
/* 250 */       this.socket = sock;
/* 251 */       bind(sock, params);
/*     */     } 
/* 253 */     this.targetHost = target;
/* 254 */     this.connSecure = secure;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpResponse receiveResponseHeader() throws HttpException, IOException {
/* 259 */     HttpResponse response = super.receiveResponseHeader();
/* 260 */     if (this.log.isDebugEnabled()) {
/* 261 */       this.log.debug("Receiving response: " + response.getStatusLine());
/*     */     }
/* 263 */     if (this.headerLog.isDebugEnabled()) {
/* 264 */       this.headerLog.debug("<< " + response.getStatusLine().toString());
/* 265 */       Header[] headers = response.getAllHeaders();
/* 266 */       for (Header header : headers) {
/* 267 */         this.headerLog.debug("<< " + header.toString());
/*     */       }
/*     */     } 
/* 270 */     return response;
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendRequestHeader(HttpRequest request) throws HttpException, IOException {
/* 275 */     if (this.log.isDebugEnabled()) {
/* 276 */       this.log.debug("Sending request: " + request.getRequestLine());
/*     */     }
/* 278 */     super.sendRequestHeader(request);
/* 279 */     if (this.headerLog.isDebugEnabled()) {
/* 280 */       this.headerLog.debug(">> " + request.getRequestLine().toString());
/* 281 */       Header[] headers = request.getAllHeaders();
/* 282 */       for (Header header : headers) {
/* 283 */         this.headerLog.debug(">> " + header.toString());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getAttribute(String id) {
/* 290 */     return this.attributes.get(id);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object removeAttribute(String id) {
/* 295 */     return this.attributes.remove(id);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAttribute(String id, Object obj) {
/* 300 */     this.attributes.put(id, obj);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\conn\DefaultClientConnection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */