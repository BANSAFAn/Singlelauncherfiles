/*    */ package org.tlauncher.util.async;
/*    */ 
/*    */ public class RunnableThread
/*    */   extends ExtendedThread {
/*    */   private final Runnable r;
/*    */   
/*    */   public RunnableThread(Runnable r) {
/*  8 */     if (r == null)
/*  9 */       throw new NullPointerException(); 
/* 10 */     this.r = r;
/*    */   }
/*    */ 
/*    */   
/*    */   public void run() {
/* 15 */     this.r.run();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlaunche\\util\async\RunnableThread.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */