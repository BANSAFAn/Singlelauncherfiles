/*    */ package org.slf4j.impl;
/*    */ 
/*    */ import org.slf4j.spi.MDCAdapter;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StaticMDCBinder
/*    */ {
/* 41 */   public static final StaticMDCBinder SINGLETON = new StaticMDCBinder();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MDCAdapter getMDCA() {
/* 51 */     return new Log4jMDCAdapter();
/*    */   }
/*    */   
/*    */   public String getMDCAdapterClassStr() {
/* 55 */     return Log4jMDCAdapter.class.getName();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\slf4j\impl\StaticMDCBinder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */