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
/*    */ public class RFC6265StrictSpec
/*    */   extends RFC6265CookieSpecBase
/*    */ {
/* 44 */   static final String[] DATE_PATTERNS = new String[] { "EEE, dd MMM yyyy HH:mm:ss zzz", "EEE, dd-MMM-yy HH:mm:ss zzz", "EEE MMM d HH:mm:ss yyyy" };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public RFC6265StrictSpec() {
/* 51 */     super(new CommonCookieAttributeHandler[] { new BasicPathHandler(), new BasicDomainHandler(), new BasicMaxAgeHandler(), new BasicSecureHandler(), new BasicExpiresHandler(DATE_PATTERNS) });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   RFC6265StrictSpec(CommonCookieAttributeHandler... handlers) {
/* 59 */     super(handlers);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 64 */     return "rfc6265-strict";
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\cookie\RFC6265StrictSpec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */