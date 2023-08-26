/*     */ package org.apache.http.impl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import org.apache.http.HttpClientConnection;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpEntityEnclosingRequest;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpMessage;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.config.MessageConstraints;
/*     */ import org.apache.http.entity.ContentLengthStrategy;
/*     */ import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
/*     */ import org.apache.http.impl.io.DefaultHttpResponseParserFactory;
/*     */ import org.apache.http.io.HttpMessageParser;
/*     */ import org.apache.http.io.HttpMessageParserFactory;
/*     */ import org.apache.http.io.HttpMessageWriter;
/*     */ import org.apache.http.io.HttpMessageWriterFactory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class DefaultBHttpClientConnection
/*     */   extends BHttpConnectionBase
/*     */   implements HttpClientConnection
/*     */ {
/*     */   private final HttpMessageParser<HttpResponse> responseParser;
/*     */   private final HttpMessageWriter<HttpRequest> requestWriter;
/*     */   
/*     */   public DefaultBHttpClientConnection(int buffersize, int fragmentSizeHint, CharsetDecoder chardecoder, CharsetEncoder charencoder, MessageConstraints constraints, ContentLengthStrategy incomingContentStrategy, ContentLengthStrategy outgoingContentStrategy, HttpMessageWriterFactory<HttpRequest> requestWriterFactory, HttpMessageParserFactory<HttpResponse> responseParserFactory) {
/*  97 */     super(buffersize, fragmentSizeHint, chardecoder, charencoder, constraints, incomingContentStrategy, outgoingContentStrategy);
/*     */     
/*  99 */     this.requestWriter = ((requestWriterFactory != null) ? requestWriterFactory : DefaultHttpRequestWriterFactory.INSTANCE).create(getSessionOutputBuffer());
/*     */     
/* 101 */     this.responseParser = ((responseParserFactory != null) ? responseParserFactory : DefaultHttpResponseParserFactory.INSTANCE).create(getSessionInputBuffer(), constraints);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultBHttpClientConnection(int buffersize, CharsetDecoder chardecoder, CharsetEncoder charencoder, MessageConstraints constraints) {
/* 110 */     this(buffersize, buffersize, chardecoder, charencoder, constraints, (ContentLengthStrategy)null, (ContentLengthStrategy)null, (HttpMessageWriterFactory<HttpRequest>)null, (HttpMessageParserFactory<HttpResponse>)null);
/*     */   }
/*     */   
/*     */   public DefaultBHttpClientConnection(int buffersize) {
/* 114 */     this(buffersize, buffersize, (CharsetDecoder)null, (CharsetEncoder)null, (MessageConstraints)null, (ContentLengthStrategy)null, (ContentLengthStrategy)null, (HttpMessageWriterFactory<HttpRequest>)null, (HttpMessageParserFactory<HttpResponse>)null);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onResponseReceived(HttpResponse response) {}
/*     */ 
/*     */   
/*     */   protected void onRequestSubmitted(HttpRequest request) {}
/*     */ 
/*     */   
/*     */   public void bind(Socket socket) throws IOException {
/* 125 */     super.bind(socket);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isResponseAvailable(int timeout) throws IOException {
/* 130 */     ensureOpen();
/*     */     try {
/* 132 */       return awaitInput(timeout);
/* 133 */     } catch (SocketTimeoutException ex) {
/* 134 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendRequestHeader(HttpRequest request) throws HttpException, IOException {
/* 141 */     Args.notNull(request, "HTTP request");
/* 142 */     ensureOpen();
/* 143 */     this.requestWriter.write((HttpMessage)request);
/* 144 */     onRequestSubmitted(request);
/* 145 */     incrementRequestCount();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendRequestEntity(HttpEntityEnclosingRequest request) throws HttpException, IOException {
/* 151 */     Args.notNull(request, "HTTP request");
/* 152 */     ensureOpen();
/* 153 */     HttpEntity entity = request.getEntity();
/* 154 */     if (entity == null) {
/*     */       return;
/*     */     }
/* 157 */     OutputStream outstream = prepareOutput((HttpMessage)request);
/* 158 */     entity.writeTo(outstream);
/* 159 */     outstream.close();
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpResponse receiveResponseHeader() throws HttpException, IOException {
/* 164 */     ensureOpen();
/* 165 */     HttpResponse response = (HttpResponse)this.responseParser.parse();
/* 166 */     onResponseReceived(response);
/* 167 */     if (response.getStatusLine().getStatusCode() >= 200) {
/* 168 */       incrementResponseCount();
/*     */     }
/* 170 */     return response;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void receiveResponseEntity(HttpResponse response) throws HttpException, IOException {
/* 176 */     Args.notNull(response, "HTTP response");
/* 177 */     ensureOpen();
/* 178 */     HttpEntity entity = prepareInput((HttpMessage)response);
/* 179 */     response.setEntity(entity);
/*     */   }
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/* 184 */     ensureOpen();
/* 185 */     doFlush();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\DefaultBHttpClientConnection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */