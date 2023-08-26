/*     */ package org.apache.http.impl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.http.HttpConnectionMetrics;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpEntityEnclosingRequest;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpMessage;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpRequestFactory;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.HttpServerConnection;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.entity.ContentLengthStrategy;
/*     */ import org.apache.http.impl.entity.DisallowIdentityContentLengthStrategy;
/*     */ import org.apache.http.impl.entity.EntityDeserializer;
/*     */ import org.apache.http.impl.entity.EntitySerializer;
/*     */ import org.apache.http.impl.entity.LaxContentLengthStrategy;
/*     */ import org.apache.http.impl.entity.StrictContentLengthStrategy;
/*     */ import org.apache.http.impl.io.DefaultHttpRequestParser;
/*     */ import org.apache.http.impl.io.HttpResponseWriter;
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
/*     */ @Deprecated
/*     */ @NotThreadSafe
/*     */ public abstract class AbstractHttpServerConnection
/*     */   implements HttpServerConnection
/*     */ {
/*     */   private final EntitySerializer entityserializer;
/*     */   private final EntityDeserializer entitydeserializer;
/*  81 */   private SessionInputBuffer inbuffer = null;
/*  82 */   private SessionOutputBuffer outbuffer = null;
/*  83 */   private EofSensor eofSensor = null;
/*  84 */   private HttpMessageParser<HttpRequest> requestParser = null;
/*  85 */   private HttpMessageWriter<HttpResponse> responseWriter = null;
/*  86 */   private HttpConnectionMetricsImpl metrics = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractHttpServerConnection() {
/*  98 */     this.entityserializer = createEntitySerializer();
/*  99 */     this.entitydeserializer = createEntityDeserializer();
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
/* 121 */     return new EntityDeserializer((ContentLengthStrategy)new DisallowIdentityContentLengthStrategy((ContentLengthStrategy)new LaxContentLengthStrategy(0)));
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
/*     */   protected HttpRequestFactory createHttpRequestFactory() {
/* 151 */     return DefaultHttpRequestFactory.INSTANCE;
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
/*     */   protected HttpMessageParser<HttpRequest> createRequestParser(SessionInputBuffer buffer, HttpRequestFactory requestFactory, HttpParams params) {
/* 173 */     return (HttpMessageParser<HttpRequest>)new DefaultHttpRequestParser(buffer, null, requestFactory, params);
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
/*     */   protected HttpMessageWriter<HttpResponse> createResponseWriter(SessionOutputBuffer buffer, HttpParams params) {
/* 193 */     return (HttpMessageWriter<HttpResponse>)new HttpResponseWriter(buffer, null, params);
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
/* 230 */     this.requestParser = createRequestParser(inbuffer, createHttpRequestFactory(), params);
/*     */ 
/*     */ 
/*     */     
/* 234 */     this.responseWriter = createResponseWriter(outbuffer, params);
/*     */     
/* 236 */     this.metrics = createConnectionMetrics(inbuffer.getMetrics(), outbuffer.getMetrics());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpRequest receiveRequestHeader() throws HttpException, IOException {
/* 243 */     assertOpen();
/* 244 */     HttpRequest request = (HttpRequest)this.requestParser.parse();
/* 245 */     this.metrics.incrementRequestCount();
/* 246 */     return request;
/*     */   }
/*     */ 
/*     */   
/*     */   public void receiveRequestEntity(HttpEntityEnclosingRequest request) throws HttpException, IOException {
/* 251 */     Args.notNull(request, "HTTP request");
/* 252 */     assertOpen();
/* 253 */     HttpEntity entity = this.entitydeserializer.deserialize(this.inbuffer, (HttpMessage)request);
/* 254 */     request.setEntity(entity);
/*     */   }
/*     */   
/*     */   protected void doFlush() throws IOException {
/* 258 */     this.outbuffer.flush();
/*     */   }
/*     */   
/*     */   public void flush() throws IOException {
/* 262 */     assertOpen();
/* 263 */     doFlush();
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendResponseHeader(HttpResponse response) throws HttpException, IOException {
/* 268 */     Args.notNull(response, "HTTP response");
/* 269 */     assertOpen();
/* 270 */     this.responseWriter.write((HttpMessage)response);
/* 271 */     if (response.getStatusLine().getStatusCode() >= 200) {
/* 272 */       this.metrics.incrementResponseCount();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendResponseEntity(HttpResponse response) throws HttpException, IOException {
/* 278 */     if (response.getEntity() == null) {
/*     */       return;
/*     */     }
/* 281 */     this.entityserializer.serialize(this.outbuffer, (HttpMessage)response, response.getEntity());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isEof() {
/* 288 */     return (this.eofSensor != null && this.eofSensor.isEof());
/*     */   }
/*     */   
/*     */   public boolean isStale() {
/* 292 */     if (!isOpen()) {
/* 293 */       return true;
/*     */     }
/* 295 */     if (isEof()) {
/* 296 */       return true;
/*     */     }
/*     */     try {
/* 299 */       this.inbuffer.isDataAvailable(1);
/* 300 */       return isEof();
/* 301 */     } catch (IOException ex) {
/* 302 */       return true;
/*     */     } 
/*     */   }
/*     */   
/*     */   public HttpConnectionMetrics getMetrics() {
/* 307 */     return this.metrics;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\AbstractHttpServerConnection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */