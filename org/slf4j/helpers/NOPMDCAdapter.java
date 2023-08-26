/*    */ package org.slf4j.helpers;
/*    */ 
/*    */ import java.util.Map;
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
/*    */ public class NOPMDCAdapter
/*    */   implements MDCAdapter
/*    */ {
/*    */   public void clear() {}
/*    */   
/*    */   public String get(String key) {
/* 46 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void put(String key, String val) {}
/*    */ 
/*    */   
/*    */   public void remove(String key) {}
/*    */   
/*    */   public Map getCopyOfContextMap() {
/* 56 */     return null;
/*    */   }
/*    */   
/*    */   public void setContextMap(Map contextMap) {}
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\slf4j\helpers\NOPMDCAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */