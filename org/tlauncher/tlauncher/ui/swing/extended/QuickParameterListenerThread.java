/*    */ package org.tlauncher.tlauncher.ui.swing.extended;
/*    */ 
/*    */ import org.tlauncher.tlauncher.ui.swing.util.IntegerArrayGetter;
/*    */ import org.tlauncher.util.U;
/*    */ import org.tlauncher.util.async.LoopedThread;
/*    */ 
/*    */ public class QuickParameterListenerThread
/*    */   extends LoopedThread
/*    */ {
/*    */   public static final int DEFAULT_TICK = 500;
/*    */   private final IntegerArrayGetter paramGetter;
/*    */   private final Runnable runnable;
/*    */   private final int tick;
/*    */   
/*    */   QuickParameterListenerThread(IntegerArrayGetter getter, Runnable run, int tick) {
/* 16 */     super("QuickParameterListenerThread");
/*    */     
/* 18 */     if (getter == null) {
/* 19 */       throw new NullPointerException("Getter is NULL!");
/*    */     }
/* 21 */     if (run == null) {
/* 22 */       throw new NullPointerException("Runnable is NULL!");
/*    */     }
/* 24 */     if (tick < 0) {
/* 25 */       throw new IllegalArgumentException("Tick must be positive!");
/*    */     }
/* 27 */     this.paramGetter = getter;
/* 28 */     this.runnable = run;
/*    */     
/* 30 */     this.tick = tick;
/*    */     
/* 32 */     setPriority(1);
/* 33 */     startAndWait();
/*    */   }
/*    */   
/*    */   QuickParameterListenerThread(IntegerArrayGetter getter, Runnable run) {
/* 37 */     this(getter, run, 500);
/*    */   }
/*    */   
/*    */   void startListening() {
/* 41 */     iterate();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void iterateOnce() {
/* 46 */     int[] initial = this.paramGetter.getIntegerArray();
/* 47 */     int i = 0;
/*    */ 
/*    */     
/*    */     while (true) {
/* 51 */       sleep();
/*    */       
/* 53 */       int[] newvalue = this.paramGetter.getIntegerArray();
/* 54 */       boolean equal = true;
/*    */       
/* 56 */       for (i = 0; i < initial.length; i++) {
/* 57 */         if (initial[i] != newvalue[i]) {
/* 58 */           equal = false;
/*    */         }
/*    */       } 
/* 61 */       initial = newvalue;
/*    */       
/* 63 */       if (!equal) {
/*    */         continue;
/*    */       }
/*    */       
/*    */       break;
/*    */     } 
/* 69 */     this.runnable.run();
/*    */   }
/*    */   
/*    */   private void sleep() {
/* 73 */     U.sleepFor(this.tick);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\extended\QuickParameterListenerThread.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */