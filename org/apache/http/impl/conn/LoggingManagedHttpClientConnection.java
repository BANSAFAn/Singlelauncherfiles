/*     */ package org.apache.http.impl.conn;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.Socket;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.config.MessageConstraints;
/*     */ import org.apache.http.entity.ContentLengthStrategy;
/*     */ import org.apache.http.io.HttpMessageParserFactory;
/*     */ import org.apache.http.io.HttpMessageWriterFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ class LoggingManagedHttpClientConnection
/*     */   extends DefaultManagedHttpClientConnection
/*     */ {
/*     */   private final Log log;
/*     */   private final Log headerlog;
/*     */   private final Wire wire;
/*     */   
/*     */   public LoggingManagedHttpClientConnection(String id, Log log, Log headerlog, Log wirelog, int buffersize, int fragmentSizeHint, CharsetDecoder chardecoder, CharsetEncoder charencoder, MessageConstraints constraints, ContentLengthStrategy incomingContentStrategy, ContentLengthStrategy outgoingContentStrategy, HttpMessageWriterFactory<HttpRequest> requestWriterFactory, HttpMessageParserFactory<HttpResponse> responseParserFactory) {
/*  68 */     super(id, buffersize, fragmentSizeHint, chardecoder, charencoder, constraints, incomingContentStrategy, outgoingContentStrategy, requestWriterFactory, responseParserFactory);
/*     */ 
/*     */     
/*  71 */     this.log = log;
/*  72 */     this.headerlog = headerlog;
/*  73 */     this.wire = new Wire(wirelog, id);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*  79 */     if (isOpen()) {
/*  80 */       if (this.log.isDebugEnabled()) {
/*  81 */         this.log.debug(getId() + ": Close connection");
/*     */       }
/*  83 */       super.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSocketTimeout(int timeout) {
/*  89 */     if (this.log.isDebugEnabled()) {
/*  90 */       this.log.debug(getId() + ": set socket timeout to " + timeout);
/*     */     }
/*  92 */     super.setSocketTimeout(timeout);
/*     */   }
/*     */ 
/*     */   
/*     */   public void shutdown() throws IOException {
/*  97 */     if (this.log.isDebugEnabled()) {
/*  98 */       this.log.debug(getId() + ": Shutdown connection");
/*     */     }
/* 100 */     super.shutdown();
/*     */   }
/*     */ 
/*     */   
/*     */   protected InputStream getSocketInputStream(Socket socket) throws IOException {
/* 105 */     InputStream in = super.getSocketInputStream(socket);
/* 106 */     if (this.wire.enabled()) {
/* 107 */       in = new LoggingInputStream(in, this.wire);
/*     */     }
/* 109 */     return in;
/*     */   }
/*     */ 
/*     */   
/*     */   protected OutputStream getSocketOutputStream(Socket socket) throws IOException {
/* 114 */     OutputStream out = super.getSocketOutputStream(socket);
/* 115 */     if (this.wire.enabled()) {
/* 116 */       out = new LoggingOutputStream(out, this.wire);
/*     */     }
/* 118 */     return out;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onResponseReceived(HttpResponse response) {
/* 123 */     if (response != null && this.headerlog.isDebugEnabled()) {
/* 124 */       this.headerlog.debug(getId() + " << " + response.getStatusLine().toString());
/* 125 */       Header[] headers = response.getAllHeaders();
/* 126 */       for (Header header : headers) {
/* 127 */         this.headerlog.debug(getId() + " << " + header.toString());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onRequestSubmitted(HttpRequest request) {
/* 134 */     if (request != null && this.headerlog.isDebugEnabled()) {
/* 135 */       this.headerlog.debug(getId() + " >> " + request.getRequestLine().toString());
/* 136 */       Header[] headers = request.getAllHeaders();
/* 137 */       for (Header header : headers)
/* 138 */         this.headerlog.debug(getId() + " >> " + header.toString()); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\conn\LoggingManagedHttpClientConnection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */