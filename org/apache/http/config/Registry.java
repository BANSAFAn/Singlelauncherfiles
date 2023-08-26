/*    */ package org.apache.http.config;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import org.apache.http.annotation.ThreadSafe;
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
/*    */ @ThreadSafe
/*    */ public final class Registry<I>
/*    */   implements Lookup<I>
/*    */ {
/*    */   private final Map<String, I> map;
/*    */   
/*    */   Registry(Map<String, I> map) {
/* 48 */     this.map = new ConcurrentHashMap<String, I>(map);
/*    */   }
/*    */ 
/*    */   
/*    */   public I lookup(String key) {
/* 53 */     if (key == null) {
/* 54 */       return null;
/*    */     }
/* 56 */     return this.map.get(key.toLowerCase(Locale.ROOT));
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 61 */     return this.map.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\config\Registry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */