/*    */ package org.apache.http.impl.cookie;
/*    */ 
/*    */ import org.apache.http.annotation.ThreadSafe;
/*    */ import org.apache.http.cookie.CommonCookieAttributeHandler;
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
/*    */ public class RFC6265LaxSpec
/*    */   extends RFC6265CookieSpecBase
/*    */ {
/*    */   public RFC6265LaxSpec() {
/* 45 */     super(new CommonCookieAttributeHandler[] { new BasicPathHandler(), new BasicDomainHandler(), new LaxMaxAgeHandler(), new BasicSecureHandler(), new LaxExpiresHandler() });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   RFC6265LaxSpec(CommonCookieAttributeHandler... handlers) {
/* 53 */     super(handlers);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 58 */     return "rfc6265-lax";
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\cookie\RFC6265LaxSpec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */