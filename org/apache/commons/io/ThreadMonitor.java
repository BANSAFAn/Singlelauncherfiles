/*     */ package org.apache.commons.io;
/*     */ 
/*     */ import java.time.Duration;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ThreadMonitor
/*     */   implements Runnable
/*     */ {
/*     */   private final Thread thread;
/*     */   private final Duration timeout;
/*     */   
/*     */   static Thread start(Duration timeout) {
/*  56 */     return start(Thread.currentThread(), timeout);
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
/*     */   static Thread start(Thread thread, Duration timeout) {
/*  69 */     if (timeout.isZero() || timeout.isNegative()) {
/*  70 */       return null;
/*     */     }
/*  72 */     ThreadMonitor timout = new ThreadMonitor(thread, timeout);
/*  73 */     Thread monitor = new Thread(timout, ThreadMonitor.class.getSimpleName());
/*  74 */     monitor.setDaemon(true);
/*  75 */     monitor.start();
/*  76 */     return monitor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void stop(Thread thread) {
/*  85 */     if (thread != null) {
/*  86 */       thread.interrupt();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ThreadMonitor(Thread thread, Duration timeout) {
/*  97 */     this.thread = thread;
/*  98 */     this.timeout = timeout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/*     */     try {
/* 110 */       sleep(this.timeout);
/* 111 */       this.thread.interrupt();
/* 112 */     } catch (InterruptedException interruptedException) {}
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
/*     */   private static void sleep(Duration duration) throws InterruptedException {
/* 128 */     long millis = duration.toMillis();
/* 129 */     long finishAtMillis = System.currentTimeMillis() + millis;
/* 130 */     long remainingMillis = millis;
/*     */     do {
/* 132 */       Thread.sleep(remainingMillis);
/* 133 */       remainingMillis = finishAtMillis - System.currentTimeMillis();
/* 134 */     } while (remainingMillis > 0L);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\ThreadMonitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */