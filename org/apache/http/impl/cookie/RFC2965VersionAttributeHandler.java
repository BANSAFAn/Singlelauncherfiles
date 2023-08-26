/*    */ package org.apache.http.impl.cookie;
/*    */ 
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.cookie.ClientCookie;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Immutable
/*    */ public class RFC2965VersionAttributeHandler
/*    */   implements CommonCookieAttributeHandler
/*    */ {
/*    */   public void parse(SetCookie cookie, String value) throws MalformedCookieException {
/* 59 */     Args.notNull(cookie, "Cookie");
/* 60 */     if (value == null) {
/* 61 */       throw new MalformedCookieException("Missing value for version attribute");
/*    */     }
/*    */     
/* 64 */     int version = -1;
/*    */     try {
/* 66 */       version = Integer.parseInt(value);
/* 67 */     } catch (NumberFormatException e) {
/* 68 */       version = -1;
/*    */     } 
/* 70 */     if (version < 0) {
/* 71 */       throw new MalformedCookieException("Invalid cookie version.");
/*    */     }
/* 73 */     cookie.setVersion(version);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
/* 82 */     Args.notNull(cookie, "Cookie");
/* 83 */     if (cookie instanceof org.apache.http.cookie.SetCookie2 && 
/* 84 */       cookie instanceof ClientCookie && !((ClientCookie)cookie).containsAttribute("version"))
/*    */     {
/* 86 */       throw new CookieRestrictionViolationException("Violates RFC 2965. Version attribute is required.");
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean match(Cookie cookie, CookieOrigin origin) {
/* 94 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getAttributeName() {
/* 99 */     return "version";
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\cookie\RFC2965VersionAttributeHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */