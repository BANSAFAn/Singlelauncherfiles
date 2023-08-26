/*    */ package org.slf4j.helpers;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Util
/*    */ {
/*    */   public static final void report(String msg, Throwable t) {
/* 37 */     System.err.println(msg);
/* 38 */     System.err.println("Reported exception:");
/* 39 */     t.printStackTrace();
/*    */   }
/*    */   
/*    */   public static final void report(String msg) {
/* 43 */     System.err.println("SLF4J: " + msg);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\slf4j\helpers\Util.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */