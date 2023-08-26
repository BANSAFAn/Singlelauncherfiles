/*    */ package org.apache.http.client.methods;
/*    */ 
/*    */ import org.apache.http.Header;
/*    */ import org.apache.http.HttpEntity;
/*    */ import org.apache.http.HttpEntityEnclosingRequest;
/*    */ import org.apache.http.annotation.NotThreadSafe;
/*    */ import org.apache.http.client.utils.CloneUtils;
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
/*    */ 
/*    */ @NotThreadSafe
/*    */ public abstract class HttpEntityEnclosingRequestBase
/*    */   extends HttpRequestBase
/*    */   implements HttpEntityEnclosingRequest
/*    */ {
/*    */   private HttpEntity entity;
/*    */   
/*    */   public HttpEntity getEntity() {
/* 55 */     return this.entity;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setEntity(HttpEntity entity) {
/* 60 */     this.entity = entity;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean expectContinue() {
/* 65 */     Header expect = getFirstHeader("Expect");
/* 66 */     return (expect != null && "100-continue".equalsIgnoreCase(expect.getValue()));
/*    */   }
/*    */ 
/*    */   
/*    */   public Object clone() throws CloneNotSupportedException {
/* 71 */     HttpEntityEnclosingRequestBase clone = (HttpEntityEnclosingRequestBase)super.clone();
/*    */     
/* 73 */     if (this.entity != null) {
/* 74 */       clone.entity = (HttpEntity)CloneUtils.cloneObject(this.entity);
/*    */     }
/* 76 */     return clone;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\client\methods\HttpEntityEnclosingRequestBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */