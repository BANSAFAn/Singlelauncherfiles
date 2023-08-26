/*    */ package org.apache.http.impl.cookie;
/*    */ 
/*    */ import java.util.Date;
/*    */ import org.apache.http.annotation.Immutable;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Immutable
/*    */ public class BasicMaxAgeHandler
/*    */   extends AbstractCookieAttributeHandler
/*    */   implements CommonCookieAttributeHandler
/*    */ {
/*    */   public void parse(SetCookie cookie, String value) throws MalformedCookieException {
/*    */     int age;
/* 52 */     Args.notNull(cookie, "Cookie");
/* 53 */     if (value == null) {
/* 54 */       throw new MalformedCookieException("Missing value for 'max-age' attribute");
/*    */     }
/*    */     
/*    */     try {
/* 58 */       age = Integer.parseInt(value);
/* 59 */     } catch (NumberFormatException e) {
/* 60 */       throw new MalformedCookieException("Invalid 'max-age' attribute: " + value);
/*    */     } 
/*    */     
/* 63 */     if (age < 0) {
/* 64 */       throw new MalformedCookieException("Negative 'max-age' attribute: " + value);
/*    */     }
/*    */     
/* 67 */     cookie.setExpiryDate(new Date(System.currentTimeMillis() + age * 1000L));
/*    */   }
/*    */ 
/*    */   
/*    */   public String getAttributeName() {
/* 72 */     return "max-age";
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\cookie\BasicMaxAgeHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */