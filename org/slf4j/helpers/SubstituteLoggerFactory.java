/*    */ package org.slf4j.helpers;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.slf4j.ILoggerFactory;
/*    */ import org.slf4j.Logger;
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
/*    */ 
/*    */ 
/*    */ public class SubstituteLoggerFactory
/*    */   implements ILoggerFactory
/*    */ {
/* 47 */   final List loggerNameList = new ArrayList();
/*    */   
/*    */   public Logger getLogger(String name) {
/* 50 */     synchronized (this.loggerNameList) {
/* 51 */       this.loggerNameList.add(name);
/*    */     } 
/* 53 */     return NOPLogger.NOP_LOGGER;
/*    */   }
/*    */   
/*    */   public List getLoggerNameList() {
/* 57 */     List copy = new ArrayList();
/* 58 */     synchronized (this.loggerNameList) {
/* 59 */       copy.addAll(this.loggerNameList);
/*    */     } 
/* 61 */     return copy;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\slf4j\helpers\SubstituteLoggerFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */