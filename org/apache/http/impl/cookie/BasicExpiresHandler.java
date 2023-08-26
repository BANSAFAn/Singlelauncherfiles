/*    */ package org.apache.http.impl.cookie;
/*    */ 
/*    */ import java.util.Date;
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.client.utils.DateUtils;
/*    */ import org.apache.http.cookie.CommonCookieAttributeHandler;
/*    */ import org.apache.http.cookie.MalformedCookieException;
/*    */ import org.apache.http.cookie.SetCookie;
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
/*    */ @Immutable
/*    */ public class BasicExpiresHandler
/*    */   extends AbstractCookieAttributeHandler
/*    */   implements CommonCookieAttributeHandler
/*    */ {
/*    */   private final String[] datepatterns;
/*    */   
/*    */   public BasicExpiresHandler(String[] datepatterns) {
/* 50 */     Args.notNull(datepatterns, "Array of date patterns");
/* 51 */     this.datepatterns = datepatterns;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void parse(SetCookie cookie, String value) throws MalformedCookieException {
/* 57 */     Args.notNull(cookie, "Cookie");
/* 58 */     if (value == null) {
/* 59 */       throw new MalformedCookieException("Missing value for 'expires' attribute");
/*    */     }
/* 61 */     Date expiry = DateUtils.parseDate(value, this.datepatterns);
/* 62 */     if (expiry == null) {
/* 63 */       throw new MalformedCookieException("Invalid 'expires' attribute: " + value);
/*    */     }
/*    */     
/* 66 */     cookie.setExpiryDate(expiry);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getAttributeName() {
/* 71 */     return "expires";
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\cookie\BasicExpiresHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */