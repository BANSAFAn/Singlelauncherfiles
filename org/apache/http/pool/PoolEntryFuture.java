/*     */ package org.apache.http.pool;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Date;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import java.util.concurrent.locks.Condition;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.concurrent.FutureCallback;
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
/*     */ @ThreadSafe
/*     */ abstract class PoolEntryFuture<T>
/*     */   implements Future<T>
/*     */ {
/*     */   private final Lock lock;
/*     */   private final FutureCallback<T> callback;
/*     */   private final Condition condition;
/*     */   private volatile boolean cancelled;
/*     */   private volatile boolean completed;
/*     */   private T result;
/*     */   
/*     */   PoolEntryFuture(Lock lock, FutureCallback<T> callback) {
/*  54 */     this.lock = lock;
/*  55 */     this.condition = lock.newCondition();
/*  56 */     this.callback = callback;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean cancel(boolean mayInterruptIfRunning) {
/*  61 */     this.lock.lock();
/*     */     try {
/*  63 */       if (this.completed) {
/*  64 */         return false;
/*     */       }
/*  66 */       this.completed = true;
/*  67 */       this.cancelled = true;
/*  68 */       if (this.callback != null) {
/*  69 */         this.callback.cancelled();
/*     */       }
/*  71 */       this.condition.signalAll();
/*  72 */       return true;
/*     */     } finally {
/*  74 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCancelled() {
/*  80 */     return this.cancelled;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDone() {
/*  85 */     return this.completed;
/*     */   }
/*     */ 
/*     */   
/*     */   public T get() throws InterruptedException, ExecutionException {
/*     */     try {
/*  91 */       return get(0L, TimeUnit.MILLISECONDS);
/*  92 */     } catch (TimeoutException ex) {
/*  93 */       throw new ExecutionException(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
/* 101 */     Args.notNull(unit, "Time unit");
/* 102 */     this.lock.lock();
/*     */     try {
/* 104 */       if (this.completed) {
/* 105 */         return this.result;
/*     */       }
/* 107 */       this.result = getPoolEntry(timeout, unit);
/* 108 */       this.completed = true;
/* 109 */       if (this.callback != null) {
/* 110 */         this.callback.completed(this.result);
/*     */       }
/* 112 */       return this.result;
/* 113 */     } catch (IOException ex) {
/* 114 */       this.completed = true;
/* 115 */       this.result = null;
/* 116 */       if (this.callback != null) {
/* 117 */         this.callback.failed(ex);
/*     */       }
/* 119 */       throw new ExecutionException(ex);
/*     */     } finally {
/* 121 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract T getPoolEntry(long paramLong, TimeUnit paramTimeUnit) throws IOException, InterruptedException, TimeoutException;
/*     */   
/*     */   public boolean await(Date deadline) throws InterruptedException {
/* 129 */     this.lock.lock(); try {
/*     */       boolean success;
/* 131 */       if (this.cancelled) {
/* 132 */         throw new InterruptedException("Operation interrupted");
/*     */       }
/*     */       
/* 135 */       if (deadline != null) {
/* 136 */         success = this.condition.awaitUntil(deadline);
/*     */       } else {
/* 138 */         this.condition.await();
/* 139 */         success = true;
/*     */       } 
/* 141 */       if (this.cancelled) {
/* 142 */         throw new InterruptedException("Operation interrupted");
/*     */       }
/* 144 */       return success;
/*     */     } finally {
/* 146 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void wakeup() {
/* 152 */     this.lock.lock();
/*     */     try {
/* 154 */       this.condition.signalAll();
/*     */     } finally {
/* 156 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\pool\PoolEntryFuture.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */