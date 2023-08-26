/*     */ package org.tlauncher.util.async;
/*     */ 
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import org.tlauncher.util.U;
/*     */ 
/*     */ public abstract class ExtendedThread
/*     */   extends Thread {
/*   8 */   private static AtomicInteger threadNum = new AtomicInteger();
/*     */   
/*     */   private final ExtendedThreadCaller caller;
/*  11 */   private final Object monitor = new Object();
/*     */   private volatile String blockReason;
/*     */   
/*     */   public ExtendedThread(String name) {
/*  15 */     super(name + "#" + threadNum.incrementAndGet());
/*     */     
/*  17 */     this.caller = new ExtendedThreadCaller();
/*     */   }
/*     */   
/*     */   public ExtendedThread() {
/*  21 */     this("ExtendedThread");
/*     */   }
/*     */   
/*     */   public ExtendedThreadCaller getCaller() {
/*  25 */     return this.caller;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startAndWait() {
/*  33 */     start();
/*     */     
/*  35 */     while (!isThreadLocked()) {
/*  36 */       U.sleepFor(100L);
/*     */     }
/*     */   }
/*     */   
/*     */   public abstract void run();
/*     */   
/*     */   protected void lockThread(String reason) {
/*  43 */     if (reason == null) {
/*  44 */       throw new NullPointerException();
/*     */     }
/*  46 */     checkCurrent();
/*     */     
/*  48 */     this.blockReason = reason;
/*     */ 
/*     */ 
/*     */     
/*  52 */     synchronized (this.monitor) {
/*  53 */       while (this.blockReason != null) {
/*     */         try {
/*  55 */           this.monitor.wait();
/*  56 */         } catch (InterruptedException e) {
/*  57 */           e.printStackTrace();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void unlockThread(String reason) {
/*  65 */     if (reason == null) {
/*  66 */       throw new NullPointerException();
/*     */     }
/*     */ 
/*     */     
/*  70 */     if (!reason.equals(this.blockReason)) {
/*  71 */       throw new IllegalStateException("Unlocking denied! Locked with: " + this.blockReason + ", tried to unlock with: " + reason);
/*     */     }
/*  73 */     this.blockReason = null;
/*     */     
/*  75 */     synchronized (this.monitor) {
/*  76 */       this.monitor.notifyAll();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tryUnlock(String reason) {
/*  83 */     if (reason == null) {
/*  84 */       throw new NullPointerException();
/*     */     }
/*  86 */     if (reason.equals(this.blockReason))
/*  87 */       unlockThread(reason); 
/*     */   }
/*     */   
/*     */   public boolean isThreadLocked() {
/*  91 */     return (this.blockReason != null);
/*     */   }
/*     */   
/*     */   public boolean isCurrent() {
/*  95 */     return Thread.currentThread().equals(this);
/*     */   }
/*     */   
/*     */   protected void checkCurrent() {
/*  99 */     if (!isCurrent())
/* 100 */       throw new IllegalStateException("Illegal thread!"); 
/*     */   }
/*     */   
/*     */   protected void threadLog(Object... o) {
/* 104 */     U.log(new Object[] { "[" + getName() + "]", o });
/*     */   }
/*     */   
/*     */   public class ExtendedThreadCaller extends RuntimeException {
/*     */     private ExtendedThreadCaller() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlaunche\\util\async\ExtendedThread.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */