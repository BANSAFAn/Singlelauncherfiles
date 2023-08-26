/*     */ package org.apache.http.impl.client;
/*     */ 
/*     */ import java.util.concurrent.FutureTask;
/*     */ import org.apache.http.client.methods.HttpUriRequest;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HttpRequestFutureTask<V>
/*     */   extends FutureTask<V>
/*     */ {
/*     */   private final HttpUriRequest request;
/*     */   private final HttpRequestTaskCallable<V> callable;
/*     */   
/*     */   public HttpRequestFutureTask(HttpUriRequest request, HttpRequestTaskCallable<V> httpCallable) {
/*  47 */     super(httpCallable);
/*  48 */     this.request = request;
/*  49 */     this.callable = httpCallable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean cancel(boolean mayInterruptIfRunning) {
/*  58 */     this.callable.cancel();
/*  59 */     if (mayInterruptIfRunning) {
/*  60 */       this.request.abort();
/*     */     }
/*  62 */     return super.cancel(mayInterruptIfRunning);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long scheduledTime() {
/*  69 */     return this.callable.getScheduled();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long startedTime() {
/*  76 */     return this.callable.getStarted();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long endedTime() {
/*  83 */     if (isDone()) {
/*  84 */       return this.callable.getEnded();
/*     */     }
/*  86 */     throw new IllegalStateException("Task is not done yet");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long requestDuration() {
/*  95 */     if (isDone()) {
/*  96 */       return endedTime() - startedTime();
/*     */     }
/*  98 */     throw new IllegalStateException("Task is not done yet");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long taskDuration() {
/* 106 */     if (isDone()) {
/* 107 */       return endedTime() - scheduledTime();
/*     */     }
/* 109 */     throw new IllegalStateException("Task is not done yet");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 115 */     return this.request.getRequestLine().getUri();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\client\HttpRequestFutureTask.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */