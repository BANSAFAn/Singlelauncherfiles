/*    */ package org.apache.http.protocol;
/*    */ 
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
/*    */ 
/*    */ 
/*    */ @Deprecated
/*    */ public final class DefaultedHttpContext
/*    */   implements HttpContext
/*    */ {
/*    */   private final HttpContext local;
/*    */   private final HttpContext defaults;
/*    */   
/*    */   public DefaultedHttpContext(HttpContext local, HttpContext defaults) {
/* 50 */     this.local = (HttpContext)Args.notNull(local, "HTTP context");
/* 51 */     this.defaults = defaults;
/*    */   }
/*    */   
/*    */   public Object getAttribute(String id) {
/* 55 */     Object obj = this.local.getAttribute(id);
/* 56 */     if (obj == null) {
/* 57 */       return this.defaults.getAttribute(id);
/*    */     }
/* 59 */     return obj;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object removeAttribute(String id) {
/* 64 */     return this.local.removeAttribute(id);
/*    */   }
/*    */   
/*    */   public void setAttribute(String id, Object obj) {
/* 68 */     this.local.setAttribute(id, obj);
/*    */   }
/*    */   
/*    */   public HttpContext getDefaults() {
/* 72 */     return this.defaults;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 77 */     StringBuilder buf = new StringBuilder();
/* 78 */     buf.append("[local: ").append(this.local);
/* 79 */     buf.append("defaults: ").append(this.defaults);
/* 80 */     buf.append("]");
/* 81 */     return buf.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\protocol\DefaultedHttpContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */