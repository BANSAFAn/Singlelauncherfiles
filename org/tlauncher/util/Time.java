/*    */ package org.tlauncher.util;
/*    */ 
/*    */ import java.util.Hashtable;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class Time {
/*  7 */   private static Map<Object, Long> timers = new Hashtable<>();
/*    */   
/*    */   public static void start(Object holder) {
/* 10 */     if (timers.containsKey(holder)) {
/* 11 */       throw new IllegalArgumentException("This holder (" + holder
/* 12 */           .toString() + ") is already in use!");
/*    */     }
/* 14 */     timers.put(holder, Long.valueOf(System.currentTimeMillis()));
/*    */   }
/*    */   
/*    */   public static long stop(Object holder) {
/* 18 */     long current = System.currentTimeMillis();
/*    */     
/* 20 */     Long l = timers.get(holder);
/*    */     
/* 22 */     if (l == null) {
/* 23 */       return 0L;
/*    */     }
/* 25 */     timers.remove(holder);
/* 26 */     return current - l.longValue();
/*    */   }
/*    */   
/*    */   public static void start() {
/* 30 */     start(Thread.currentThread());
/*    */   }
/*    */   
/*    */   public static long stop() {
/* 34 */     return stop(Thread.currentThread());
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlaunche\\util\Time.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */