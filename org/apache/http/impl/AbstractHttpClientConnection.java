/*     */ package org.apache.http.impl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.SocketTimeoutException;
/*     */ import org.apache.http.HttpClientConnection;
/*     */ import org.apache.http.HttpConnectionMetrics;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpEntityEnclosingRequest;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpMessage;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.HttpResponseFactory;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.entity.ContentLengthStrategy;
/*     */ import org.apache.http.impl.entity.EntityDeserializer;
/*     */ import org.apache.http.impl.entity.EntitySerializer;
/*     */ import org.apache.http.impl.entity.LaxContentLengthStrategy;
/*     */ import org.apache.http.impl.entity.StrictContentLengthStrategy;
/*     */ import org.apache.http.impl.io.DefaultHttpResponseParser;
/*     */ import org.apache.http.impl.io.HttpRequestWriter;
/*     */ import org.apache.http.io.EofSensor;
/*     */ import org.apache.http.io.HttpMessageParser;
/*     */ import org.apache.http.io.HttpMessageWriter;
/*     */ import org.apache.http.io.HttpTransportMetrics;
/*     */ import org.apache.http.io.SessionInputBuffer;
/*     */ import org.apache.http.io.SessionOutputBuffer;
/*     */ import org.apache.http.params.HttpParams;
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
/*     */ @Deprecated
/*     */ @NotThreadSafe
/*     */ public abstract class AbstractHttpClientConnection
/*     */   implements HttpClientConnection
/*     */ {
/*     */   private final EntitySerializer entityserializer;
/*     */   private final EntityDeserializer entitydeserializer;
/*  82 */   private SessionInputBuffer inbuffer = null;
/*  83 */   private SessionOutputBuffer outbuffer = null;
/*  84 */   private EofSensor eofSensor = null;
/*  85 */   private HttpMessageParser<HttpResponse> responseParser = null;
/*  86 */   private HttpMessageWriter<HttpRequest> requestWriter = null;
/*  87 */   private HttpConnectionMetricsImpl metrics = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractHttpClientConnection() {
/*  99 */     this.entityserializer = createEntitySerializer();
/* 100 */     this.entitydeserializer = createEntityDeserializer();
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
/*     */   protected abstract void assertOpen() throws IllegalStateException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected EntityDeserializer createEntityDeserializer() {
/* 122 */     return new EntityDeserializer((ContentLengthStrategy)new LaxContentLengthStrategy());
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
/*     */   protected EntitySerializer createEntitySerializer() {
/* 137 */     return new EntitySerializer((ContentLengthStrategy)new StrictContentLengthStrategy());
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
/*     */   protected HttpResponseFactory createHttpResponseFactory() {
/* 151 */     return DefaultHttpResponseFactory.INSTANCE;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected HttpMessageParser<HttpResponse> createResponseParser(SessionInputBuffer buffer, HttpResponseFactory responseFactory, HttpParams params) {
/* 173 */     return (HttpMessageParser<HttpResponse>)new DefaultHttpResponseParser(buffer, null, responseFactory, params);
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
/*     */ 
/*     */   
/*     */   protected HttpMessageWriter<HttpRequest> createRequestWriter(SessionOutputBuffer buffer, HttpParams params) {
/* 193 */     return (HttpMessageWriter<HttpRequest>)new HttpRequestWriter(buffer, null, params);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected HttpConnectionMetricsImpl createConnectionMetrics(HttpTransportMetrics inTransportMetric, HttpTransportMetrics outTransportMetric) {
/* 202 */     return new HttpConnectionMetricsImpl(inTransportMetric, outTransportMetric);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void init(SessionInputBuffer inbuffer, SessionOutputBuffer outbuffer, HttpParams params) {
/* 225 */     this.inbuffer = (SessionInputBuffer)Args.notNull(inbuffer, "Input session buffer");
/* 226 */     this.outbuffer = (SessionOutputBuffer)Args.notNull(outbuffer, "Output session buffer");
/* 227 */     if (inbuffer instanceof EofSensor) {
/* 228 */       this.eofSensor = (EofSensor)inbuffer;
/*     */     }
/* 230 */     this.responseParser = createResponseParser(inbuffer, createHttpResponseFactory(), params);
/*     */ 
/*     */ 
/*     */     
/* 234 */     this.requestWriter = createRequestWriter(outbuffer, params);
/*     */     
/* 236 */     this.metrics = createConnectionMetrics(inbuffer.getMetrics(), outbuffer.getMetrics());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isResponseAvailable(int timeout) throws IOException {
/* 242 */     assertOpen();
/*     */     try {
/* 244 */       return this.inbuffer.isDataAvailable(timeout);
/* 245 */     } catch (SocketTimeoutException ex) {
/* 246 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendRequestHeader(HttpRequest request) throws HttpException, IOException {
/* 252 */     Args.notNull(request, "HTTP request");
/* 253 */     assertOpen();
/* 254 */     this.requestWriter.write((HttpMessage)request);
/* 255 */     this.metrics.incrementRequestCount();
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendRequestEntity(HttpEntityEnclosingRequest request) throws HttpException, IOException {
/* 260 */     Args.notNull(request, "HTTP request");
/* 261 */     assertOpen();
/* 262 */     if (request.getEntity() == null) {
/*     */       return;
/*     */     }
/* 265 */     this.entityserializer.serialize(this.outbuffer, (HttpMessage)request, request.getEntity());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doFlush() throws IOException {
/* 272 */     this.outbuffer.flush();
/*     */   }
/*     */   
/*     */   public void flush() throws IOException {
/* 276 */     assertOpen();
/* 277 */     doFlush();
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpResponse receiveResponseHeader() throws HttpException, IOException {
/* 282 */     assertOpen();
/* 283 */     HttpResponse response = (HttpResponse)this.responseParser.parse();
/* 284 */     if (response.getStatusLine().getStatusCode() >= 200) {
/* 285 */       this.metrics.incrementResponseCount();
/*     */     }
/* 287 */     return response;
/*     */   }
/*     */ 
/*     */   
/*     */   public void receiveResponseEntity(HttpResponse response) throws HttpException, IOException {
/* 292 */     Args.notNull(response, "HTTP response");
/* 293 */     assertOpen();
/* 294 */     HttpEntity entity = this.entitydeserializer.deserialize(this.inbuffer, (HttpMessage)response);
/* 295 */     response.setEntity(entity);
/*     */   }
/*     */   
/*     */   protected boolean isEof() {
/* 299 */     return (this.eofSensor != null && this.eofSensor.isEof());
/*     */   }
/*     */   
/*     */   public boolean isStale() {
/* 303 */     if (!isOpen()) {
/* 304 */       return true;
/*     */     }
/* 306 */     if (isEof()) {
/* 307 */       return true;
/*     */     }
/*     */     try {
/* 310 */       this.inbuffer.isDataAvailable(1);
/* 311 */       return isEof();
/* 312 */     } catch (SocketTimeoutException ex) {
/* 313 */       return false;
/* 314 */     } catch (IOException ex) {
/* 315 */       return true;
/*     */     } 
/*     */   }
/*     */   
/*     */   public HttpConnectionMetrics getMetrics() {
/* 320 */     return this.metrics;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\AbstractHttpClientConnection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */