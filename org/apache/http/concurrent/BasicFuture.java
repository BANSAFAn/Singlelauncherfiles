/*     */ package org.apache.http.concurrent;
/*     */ 
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
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
/*     */ public class BasicFuture<T>
/*     */   implements Future<T>, Cancellable
/*     */ {
/*     */   private final FutureCallback<T> callback;
/*     */   private volatile boolean completed;
/*     */   private volatile boolean cancelled;
/*     */   private volatile T result;
/*     */   private volatile Exception ex;
/*     */   
/*     */   public BasicFuture(FutureCallback<T> callback) {
/*  55 */     this.callback = callback;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCancelled() {
/*  60 */     return this.cancelled;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDone() {
/*  65 */     return this.completed;
/*     */   }
/*     */   
/*     */   private T getResult() throws ExecutionException {
/*  69 */     if (this.ex != null) {
/*  70 */       throw new ExecutionException(this.ex);
/*     */     }
/*  72 */     return this.result;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized T get() throws InterruptedException, ExecutionException {
/*  77 */     while (!this.completed) {
/*  78 */       wait();
/*     */     }
/*  80 */     return getResult();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
/*  86 */     Args.notNull(unit, "Time unit");
/*  87 */     long msecs = unit.toMillis(timeout);
/*  88 */     long startTime = (msecs <= 0L) ? 0L : System.currentTimeMillis();
/*  89 */     long waitTime = msecs;
/*  90 */     if (this.completed)
/*  91 */       return getResult(); 
/*  92 */     if (waitTime <= 0L) {
/*  93 */       throw new TimeoutException();
/*     */     }
/*     */     while (true) {
/*  96 */       wait(waitTime);
/*  97 */       if (this.completed) {
/*  98 */         return getResult();
/*     */       }
/* 100 */       waitTime = msecs - System.currentTimeMillis() - startTime;
/* 101 */       if (waitTime <= 0L) {
/* 102 */         throw new TimeoutException();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean completed(T result) {
/* 110 */     synchronized (this) {
/* 111 */       if (this.completed) {
/* 112 */         return false;
/*     */       }
/* 114 */       this.completed = true;
/* 115 */       this.result = result;
/* 116 */       notifyAll();
/*     */     } 
/* 118 */     if (this.callback != null) {
/* 119 */       this.callback.completed(result);
/*     */     }
/* 121 */     return true;
/*     */   }
/*     */   
/*     */   public boolean failed(Exception exception) {
/* 125 */     synchronized (this) {
/* 126 */       if (this.completed) {
/* 127 */         return false;
/*     */       }
/* 129 */       this.completed = true;
/* 130 */       this.ex = exception;
/* 131 */       notifyAll();
/*     */     } 
/* 133 */     if (this.callback != null) {
/* 134 */       this.callback.failed(exception);
/*     */     }
/* 136 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean cancel(boolean mayInterruptIfRunning) {
/* 141 */     synchronized (this) {
/* 142 */       if (this.completed) {
/* 143 */         return false;
/*     */       }
/* 145 */       this.completed = true;
/* 146 */       this.cancelled = true;
/* 147 */       notifyAll();
/*     */     } 
/* 149 */     if (this.callback != null) {
/* 150 */       this.callback.cancelled();
/*     */     }
/* 152 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean cancel() {
/* 157 */     return cancel(true);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\concurrent\BasicFuture.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */