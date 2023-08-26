/*    */ package org.apache.http.protocol;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import org.apache.http.annotation.ThreadSafe;
/*    */ import org.apache.http.util.Args;
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
/*    */ @ThreadSafe
/*    */ public class BasicHttpContext
/*    */   implements HttpContext
/*    */ {
/*    */   private final HttpContext parentContext;
/*    */   private final Map<String, Object> map;
/*    */   
/*    */   public BasicHttpContext() {
/* 51 */     this(null);
/*    */   }
/*    */ 
/*    */   
/*    */   public BasicHttpContext(HttpContext parentContext) {
/* 56 */     this.map = new ConcurrentHashMap<String, Object>();
/* 57 */     this.parentContext = parentContext;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getAttribute(String id) {
/* 62 */     Args.notNull(id, "Id");
/* 63 */     Object obj = this.map.get(id);
/* 64 */     if (obj == null && this.parentContext != null) {
/* 65 */       obj = this.parentContext.getAttribute(id);
/*    */     }
/* 67 */     return obj;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setAttribute(String id, Object obj) {
/* 72 */     Args.notNull(id, "Id");
/* 73 */     if (obj != null) {
/* 74 */       this.map.put(id, obj);
/*    */     } else {
/* 76 */       this.map.remove(id);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public Object removeAttribute(String id) {
/* 82 */     Args.notNull(id, "Id");
/* 83 */     return this.map.remove(id);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void clear() {
/* 90 */     this.map.clear();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 95 */     return this.map.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\protocol\BasicHttpContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */