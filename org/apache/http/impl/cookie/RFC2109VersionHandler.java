/*    */ package org.apache.http.impl.cookie;
/*    */ 
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.cookie.CommonCookieAttributeHandler;
/*    */ import org.apache.http.cookie.Cookie;
/*    */ import org.apache.http.cookie.CookieOrigin;
/*    */ import org.apache.http.cookie.CookieRestrictionViolationException;
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
/*    */ public class RFC2109VersionHandler
/*    */   extends AbstractCookieAttributeHandler
/*    */   implements CommonCookieAttributeHandler
/*    */ {
/*    */   public void parse(SetCookie cookie, String value) throws MalformedCookieException {
/* 53 */     Args.notNull(cookie, "Cookie");
/* 54 */     if (value == null) {
/* 55 */       throw new MalformedCookieException("Missing value for version attribute");
/*    */     }
/* 57 */     if (value.trim().isEmpty()) {
/* 58 */       throw new MalformedCookieException("Blank value for version attribute");
/*    */     }
/*    */     try {
/* 61 */       cookie.setVersion(Integer.parseInt(value));
/* 62 */     } catch (NumberFormatException e) {
/* 63 */       throw new MalformedCookieException("Invalid version: " + e.getMessage());
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
/* 71 */     Args.notNull(cookie, "Cookie");
/* 72 */     if (cookie.getVersion() < 0) {
/* 73 */       throw new CookieRestrictionViolationException("Cookie version may not be negative");
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public String getAttributeName() {
/* 79 */     return "version";
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\cookie\RFC2109VersionHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */