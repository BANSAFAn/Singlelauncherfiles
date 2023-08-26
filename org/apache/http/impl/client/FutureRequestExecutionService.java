/*     */ package org.apache.http.impl.client;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.client.HttpClient;
/*     */ import org.apache.http.client.ResponseHandler;
/*     */ import org.apache.http.client.methods.HttpUriRequest;
/*     */ import org.apache.http.concurrent.FutureCallback;
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
/*     */ @ThreadSafe
/*     */ public class FutureRequestExecutionService
/*     */   implements Closeable
/*     */ {
/*     */   private final HttpClient httpclient;
/*     */   private final ExecutorService executorService;
/*  50 */   private final FutureRequestExecutionMetrics metrics = new FutureRequestExecutionMetrics();
/*  51 */   private final AtomicBoolean closed = new AtomicBoolean(false);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FutureRequestExecutionService(HttpClient httpclient, ExecutorService executorService) {
/*  69 */     this.httpclient = httpclient;
/*  70 */     this.executorService = executorService;
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
/*     */   public <T> HttpRequestFutureTask<T> execute(HttpUriRequest request, HttpContext context, ResponseHandler<T> responseHandler) {
/*  88 */     return execute(request, context, responseHandler, null);
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
/*     */   
/*     */   public <T> HttpRequestFutureTask<T> execute(HttpUriRequest request, HttpContext context, ResponseHandler<T> responseHandler, FutureCallback<T> callback) {
/* 112 */     if (this.closed.get()) {
/* 113 */       throw new IllegalStateException("Close has been called on this httpclient instance.");
/*     */     }
/* 115 */     this.metrics.getScheduledConnections().incrementAndGet();
/* 116 */     HttpRequestTaskCallable<T> callable = new HttpRequestTaskCallable<T>(this.httpclient, request, context, responseHandler, callback, this.metrics);
/*     */     
/* 118 */     HttpRequestFutureTask<T> httpRequestFutureTask = new HttpRequestFutureTask<T>(request, callable);
/*     */     
/* 120 */     this.executorService.execute(httpRequestFutureTask);
/*     */     
/* 122 */     return httpRequestFutureTask;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FutureRequestExecutionMetrics metrics() {
/* 130 */     return this.metrics;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 135 */     this.closed.set(true);
/* 136 */     this.executorService.shutdownNow();
/* 137 */     if (this.httpclient instanceof Closeable)
/* 138 */       ((Closeable)this.httpclient).close(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\client\FutureRequestExecutionService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */