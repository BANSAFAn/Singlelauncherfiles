/*    */ package org.tlauncher.util.async;
/*    */ 
/*    */ public abstract class LoopedThread extends ExtendedThread {
/*    */   protected static final String LOOPED_BLOCK = "iteration";
/*    */   
/*    */   public LoopedThread(String name) {
/*  7 */     super(name);
/*    */   }
/*    */   
/*    */   public LoopedThread() {
/* 11 */     this("LoopedThread");
/*    */   }
/*    */ 
/*    */   
/*    */   protected final void lockThread(String reason) {
/* 16 */     if (reason == null) {
/* 17 */       throw new NullPointerException();
/*    */     }
/* 19 */     if (!reason.equals("iteration")) {
/* 20 */       throw new IllegalArgumentException("Illegal block reason. Expected: iteration, got: " + reason);
/*    */     }
/* 22 */     super.lockThread(reason);
/*    */   }
/*    */   
/*    */   public final boolean isIterating() {
/* 26 */     return !isThreadLocked();
/*    */   }
/*    */   
/*    */   public final void iterate() {
/* 30 */     if (!isIterating()) {
/* 31 */       unlockThread("iteration");
/*    */     }
/*    */   }
/*    */   
/*    */   public final void run() {
/*    */     while (true) {
/* 37 */       lockThread("iteration");
/* 38 */       iterateOnce();
/*    */     } 
/*    */   }
/*    */   
/*    */   protected abstract void iterateOnce();
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlaunche\\util\async\LoopedThread.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */