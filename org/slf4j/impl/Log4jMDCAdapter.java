/*    */ package org.slf4j.impl;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import org.apache.log4j.MDC;
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
/*    */ public class Log4jMDCAdapter
/*    */   implements MDCAdapter
/*    */ {
/*    */   public void clear() {
/* 36 */     Map map = MDC.getContext();
/* 37 */     if (map != null) {
/* 38 */       map.clear();
/*    */     }
/*    */   }
/*    */   
/*    */   public String get(String key) {
/* 43 */     return (String)MDC.get(key);
/*    */   }
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
/*    */   public void put(String key, String val) {
/* 59 */     MDC.put(key, val);
/*    */   }
/*    */   
/*    */   public void remove(String key) {
/* 63 */     MDC.remove(key);
/*    */   }
/*    */   
/*    */   public Map getCopyOfContextMap() {
/* 67 */     Map<?, ?> old = MDC.getContext();
/* 68 */     if (old != null) {
/* 69 */       return new HashMap<Object, Object>(old);
/*    */     }
/* 71 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setContextMap(Map contextMap) {
/* 76 */     Map old = MDC.getContext();
/* 77 */     if (old == null) {
/* 78 */       Iterator<Map.Entry> entrySetIterator = contextMap.entrySet().iterator();
/* 79 */       while (entrySetIterator.hasNext()) {
/* 80 */         Map.Entry mapEntry = entrySetIterator.next();
/* 81 */         MDC.put((String)mapEntry.getKey(), mapEntry.getValue());
/*    */       } 
/*    */     } else {
/* 84 */       old.clear();
/* 85 */       old.putAll(contextMap);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\slf4j\impl\Log4jMDCAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */