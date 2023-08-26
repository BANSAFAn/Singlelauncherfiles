/*    */ package org.tlauncher.util.async;
/*    */ 
/*    */ import java.util.concurrent.ExecutorService;
/*    */ import java.util.concurrent.Executors;
/*    */ import java.util.concurrent.ThreadFactory;
/*    */ 
/*    */ public class AsyncThread
/*    */ {
/*  9 */   private static ExecutorService service = Executors.newCachedThreadPool(new ThreadFactory()
/*    */       {
/*    */         public Thread newThread(Runnable r) {
/* 12 */           return new RunnableThread(r);
/*    */         }
/*    */       });
/*    */   
/*    */   public static void execute(Runnable r) {
/* 17 */     service.execute(r);
/*    */   }
/*    */   
/*    */   public static ExecutorService getService() {
/* 21 */     return service;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlaunche\\util\async\AsyncThread.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */