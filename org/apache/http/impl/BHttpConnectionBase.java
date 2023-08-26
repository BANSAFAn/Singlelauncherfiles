/*     */ package org.apache.http.impl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.InetAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketAddress;
/*     */ import java.net.SocketException;
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.apache.http.ConnectionClosedException;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpConnection;
/*     */ import org.apache.http.HttpConnectionMetrics;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpInetConnection;
/*     */ import org.apache.http.HttpMessage;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.config.MessageConstraints;
/*     */ import org.apache.http.entity.BasicHttpEntity;
/*     */ import org.apache.http.entity.ContentLengthStrategy;
/*     */ import org.apache.http.impl.entity.LaxContentLengthStrategy;
/*     */ import org.apache.http.impl.entity.StrictContentLengthStrategy;
/*     */ import org.apache.http.impl.io.ChunkedInputStream;
/*     */ import org.apache.http.impl.io.ChunkedOutputStream;
/*     */ import org.apache.http.impl.io.ContentLengthInputStream;
/*     */ import org.apache.http.impl.io.ContentLengthOutputStream;
/*     */ import org.apache.http.impl.io.EmptyInputStream;
/*     */ import org.apache.http.impl.io.HttpTransportMetricsImpl;
/*     */ import org.apache.http.impl.io.IdentityInputStream;
/*     */ import org.apache.http.impl.io.IdentityOutputStream;
/*     */ import org.apache.http.impl.io.SessionInputBufferImpl;
/*     */ import org.apache.http.impl.io.SessionOutputBufferImpl;
/*     */ import org.apache.http.io.HttpTransportMetrics;
/*     */ import org.apache.http.io.SessionInputBuffer;
/*     */ import org.apache.http.io.SessionOutputBuffer;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.NetUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class BHttpConnectionBase
/*     */   implements HttpConnection, HttpInetConnection
/*     */ {
/*     */   private final SessionInputBufferImpl inbuffer;
/*     */   private final SessionOutputBufferImpl outbuffer;
/*     */   private final MessageConstraints messageConstraints;
/*     */   private final HttpConnectionMetricsImpl connMetrics;
/*     */   private final ContentLengthStrategy incomingContentStrategy;
/*     */   private final ContentLengthStrategy outgoingContentStrategy;
/*     */   private final AtomicReference<Socket> socketHolder;
/*     */   
/*     */   protected BHttpConnectionBase(int buffersize, int fragmentSizeHint, CharsetDecoder chardecoder, CharsetEncoder charencoder, MessageConstraints messageConstraints, ContentLengthStrategy incomingContentStrategy, ContentLengthStrategy outgoingContentStrategy) {
/* 114 */     Args.positive(buffersize, "Buffer size");
/* 115 */     HttpTransportMetricsImpl inTransportMetrics = new HttpTransportMetricsImpl();
/* 116 */     HttpTransportMetricsImpl outTransportMetrics = new HttpTransportMetricsImpl();
/* 117 */     this.inbuffer = new SessionInputBufferImpl(inTransportMetrics, buffersize, -1, (messageConstraints != null) ? messageConstraints : MessageConstraints.DEFAULT, chardecoder);
/*     */     
/* 119 */     this.outbuffer = new SessionOutputBufferImpl(outTransportMetrics, buffersize, fragmentSizeHint, charencoder);
/*     */     
/* 121 */     this.messageConstraints = messageConstraints;
/* 122 */     this.connMetrics = new HttpConnectionMetricsImpl((HttpTransportMetrics)inTransportMetrics, (HttpTransportMetrics)outTransportMetrics);
/* 123 */     this.incomingContentStrategy = (incomingContentStrategy != null) ? incomingContentStrategy : (ContentLengthStrategy)LaxContentLengthStrategy.INSTANCE;
/*     */     
/* 125 */     this.outgoingContentStrategy = (outgoingContentStrategy != null) ? outgoingContentStrategy : (ContentLengthStrategy)StrictContentLengthStrategy.INSTANCE;
/*     */     
/* 127 */     this.socketHolder = new AtomicReference<Socket>();
/*     */   }
/*     */   
/*     */   protected void ensureOpen() throws IOException {
/* 131 */     Socket socket = this.socketHolder.get();
/* 132 */     if (socket == null) {
/* 133 */       throw new ConnectionClosedException("Connection is closed");
/*     */     }
/* 135 */     if (!this.inbuffer.isBound()) {
/* 136 */       this.inbuffer.bind(getSocketInputStream(socket));
/*     */     }
/* 138 */     if (!this.outbuffer.isBound()) {
/* 139 */       this.outbuffer.bind(getSocketOutputStream(socket));
/*     */     }
/*     */   }
/*     */   
/*     */   protected InputStream getSocketInputStream(Socket socket) throws IOException {
/* 144 */     return socket.getInputStream();
/*     */   }
/*     */   
/*     */   protected OutputStream getSocketOutputStream(Socket socket) throws IOException {
/* 148 */     return socket.getOutputStream();
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
/*     */   protected void bind(Socket socket) throws IOException {
/* 162 */     Args.notNull(socket, "Socket");
/* 163 */     this.socketHolder.set(socket);
/* 164 */     this.inbuffer.bind(null);
/* 165 */     this.outbuffer.bind(null);
/*     */   }
/*     */   
/*     */   protected SessionInputBuffer getSessionInputBuffer() {
/* 169 */     return (SessionInputBuffer)this.inbuffer;
/*     */   }
/*     */   
/*     */   protected SessionOutputBuffer getSessionOutputBuffer() {
/* 173 */     return (SessionOutputBuffer)this.outbuffer;
/*     */   }
/*     */   
/*     */   protected void doFlush() throws IOException {
/* 177 */     this.outbuffer.flush();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpen() {
/* 182 */     return (this.socketHolder.get() != null);
/*     */   }
/*     */   
/*     */   protected Socket getSocket() {
/* 186 */     return this.socketHolder.get();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected OutputStream createOutputStream(long len, SessionOutputBuffer outbuffer) {
/* 192 */     if (len == -2L)
/* 193 */       return (OutputStream)new ChunkedOutputStream(2048, outbuffer); 
/* 194 */     if (len == -1L) {
/* 195 */       return (OutputStream)new IdentityOutputStream(outbuffer);
/*     */     }
/* 197 */     return (OutputStream)new ContentLengthOutputStream(outbuffer, len);
/*     */   }
/*     */ 
/*     */   
/*     */   protected OutputStream prepareOutput(HttpMessage message) throws HttpException {
/* 202 */     long len = this.outgoingContentStrategy.determineLength(message);
/* 203 */     return createOutputStream(len, (SessionOutputBuffer)this.outbuffer);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected InputStream createInputStream(long len, SessionInputBuffer inbuffer) {
/* 209 */     if (len == -2L)
/* 210 */       return (InputStream)new ChunkedInputStream(inbuffer, this.messageConstraints); 
/* 211 */     if (len == -1L)
/* 212 */       return (InputStream)new IdentityInputStream(inbuffer); 
/* 213 */     if (len == 0L) {
/* 214 */       return (InputStream)EmptyInputStream.INSTANCE;
/*     */     }
/* 216 */     return (InputStream)new ContentLengthInputStream(inbuffer, len);
/*     */   }
/*     */ 
/*     */   
/*     */   protected HttpEntity prepareInput(HttpMessage message) throws HttpException {
/* 221 */     BasicHttpEntity entity = new BasicHttpEntity();
/*     */     
/* 223 */     long len = this.incomingContentStrategy.determineLength(message);
/* 224 */     InputStream instream = createInputStream(len, (SessionInputBuffer)this.inbuffer);
/* 225 */     if (len == -2L) {
/* 226 */       entity.setChunked(true);
/* 227 */       entity.setContentLength(-1L);
/* 228 */       entity.setContent(instream);
/* 229 */     } else if (len == -1L) {
/* 230 */       entity.setChunked(false);
/* 231 */       entity.setContentLength(-1L);
/* 232 */       entity.setContent(instream);
/*     */     } else {
/* 234 */       entity.setChunked(false);
/* 235 */       entity.setContentLength(len);
/* 236 */       entity.setContent(instream);
/*     */     } 
/*     */     
/* 239 */     Header contentTypeHeader = message.getFirstHeader("Content-Type");
/* 240 */     if (contentTypeHeader != null) {
/* 241 */       entity.setContentType(contentTypeHeader);
/*     */     }
/* 243 */     Header contentEncodingHeader = message.getFirstHeader("Content-Encoding");
/* 244 */     if (contentEncodingHeader != null) {
/* 245 */       entity.setContentEncoding(contentEncodingHeader);
/*     */     }
/* 247 */     return (HttpEntity)entity;
/*     */   }
/*     */ 
/*     */   
/*     */   public InetAddress getLocalAddress() {
/* 252 */     Socket socket = this.socketHolder.get();
/* 253 */     return (socket != null) ? socket.getLocalAddress() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLocalPort() {
/* 258 */     Socket socket = this.socketHolder.get();
/* 259 */     return (socket != null) ? socket.getLocalPort() : -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public InetAddress getRemoteAddress() {
/* 264 */     Socket socket = this.socketHolder.get();
/* 265 */     return (socket != null) ? socket.getInetAddress() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRemotePort() {
/* 270 */     Socket socket = this.socketHolder.get();
/* 271 */     return (socket != null) ? socket.getPort() : -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSocketTimeout(int timeout) {
/* 276 */     Socket socket = this.socketHolder.get();
/* 277 */     if (socket != null) {
/*     */       try {
/* 279 */         socket.setSoTimeout(timeout);
/* 280 */       } catch (SocketException ignore) {}
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSocketTimeout() {
/* 290 */     Socket socket = this.socketHolder.get();
/* 291 */     if (socket != null) {
/*     */       try {
/* 293 */         return socket.getSoTimeout();
/* 294 */       } catch (SocketException ignore) {
/* 295 */         return -1;
/*     */       } 
/*     */     }
/* 298 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void shutdown() throws IOException {
/* 304 */     Socket socket = this.socketHolder.getAndSet(null);
/* 305 */     if (socket != null) {
/*     */ 
/*     */       
/* 308 */       try { socket.setSoLinger(true, 0); }
/* 309 */       catch (IOException ex) {  }
/*     */       finally
/* 311 */       { socket.close(); }
/*     */     
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 318 */     Socket socket = this.socketHolder.getAndSet(null);
/* 319 */     if (socket != null) {
/*     */       try {
/* 321 */         this.inbuffer.clear();
/* 322 */         this.outbuffer.flush();
/*     */         try {
/*     */           try {
/* 325 */             socket.shutdownOutput();
/* 326 */           } catch (IOException ignore) {}
/*     */           
/*     */           try {
/* 329 */             socket.shutdownInput();
/* 330 */           } catch (IOException ignore) {}
/*     */         }
/* 332 */         catch (UnsupportedOperationException ignore) {}
/*     */       }
/*     */       finally {
/*     */         
/* 336 */         socket.close();
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private int fillInputBuffer(int timeout) throws IOException {
/* 342 */     Socket socket = this.socketHolder.get();
/* 343 */     int oldtimeout = socket.getSoTimeout();
/*     */     try {
/* 345 */       socket.setSoTimeout(timeout);
/* 346 */       return this.inbuffer.fillBuffer();
/*     */     } finally {
/* 348 */       socket.setSoTimeout(oldtimeout);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean awaitInput(int timeout) throws IOException {
/* 353 */     if (this.inbuffer.hasBufferedData()) {
/* 354 */       return true;
/*     */     }
/* 356 */     fillInputBuffer(timeout);
/* 357 */     return this.inbuffer.hasBufferedData();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStale() {
/* 362 */     if (!isOpen()) {
/* 363 */       return true;
/*     */     }
/*     */     try {
/* 366 */       int bytesRead = fillInputBuffer(1);
/* 367 */       return (bytesRead < 0);
/* 368 */     } catch (SocketTimeoutException ex) {
/* 369 */       return false;
/* 370 */     } catch (IOException ex) {
/* 371 */       return true;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void incrementRequestCount() {
/* 376 */     this.connMetrics.incrementRequestCount();
/*     */   }
/*     */   
/*     */   protected void incrementResponseCount() {
/* 380 */     this.connMetrics.incrementResponseCount();
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpConnectionMetrics getMetrics() {
/* 385 */     return this.connMetrics;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 390 */     Socket socket = this.socketHolder.get();
/* 391 */     if (socket != null) {
/* 392 */       StringBuilder buffer = new StringBuilder();
/* 393 */       SocketAddress remoteAddress = socket.getRemoteSocketAddress();
/* 394 */       SocketAddress localAddress = socket.getLocalSocketAddress();
/* 395 */       if (remoteAddress != null && localAddress != null) {
/* 396 */         NetUtils.formatAddress(buffer, localAddress);
/* 397 */         buffer.append("<->");
/* 398 */         NetUtils.formatAddress(buffer, remoteAddress);
/*     */       } 
/* 400 */       return buffer.toString();
/*     */     } 
/* 402 */     return "[Not bound]";
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\BHttpConnectionBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */