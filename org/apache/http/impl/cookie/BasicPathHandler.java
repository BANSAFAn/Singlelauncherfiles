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
/*    */ import org.apache.http.util.TextUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
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
/*    */ public class BasicPathHandler
/*    */   implements CommonCookieAttributeHandler
/*    */ {
/*    */   public void parse(SetCookie cookie, String value) throws MalformedCookieException {
/* 54 */     Args.notNull(cookie, "Cookie");
/* 55 */     cookie.setPath(!TextUtils.isBlank(value) ? value : "/");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
/* 61 */     if (!match(cookie, origin)) {
/* 62 */       throw new CookieRestrictionViolationException("Illegal 'path' attribute \"" + cookie.getPath() + "\". Path of origin: \"" + origin.getPath() + "\"");
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   static boolean pathMatch(String uriPath, String cookiePath) {
/* 69 */     String normalizedCookiePath = cookiePath;
/* 70 */     if (normalizedCookiePath == null) {
/* 71 */       normalizedCookiePath = "/";
/*    */     }
/* 73 */     if (normalizedCookiePath.length() > 1 && normalizedCookiePath.endsWith("/")) {
/* 74 */       normalizedCookiePath = normalizedCookiePath.substring(0, normalizedCookiePath.length() - 1);
/*    */     }
/* 76 */     if (uriPath.startsWith(normalizedCookiePath)) {
/* 77 */       if (normalizedCookiePath.equals("/")) {
/* 78 */         return true;
/*    */       }
/* 80 */       if (uriPath.length() == normalizedCookiePath.length()) {
/* 81 */         return true;
/*    */       }
/* 83 */       if (uriPath.charAt(normalizedCookiePath.length()) == '/') {
/* 84 */         return true;
/*    */       }
/*    */     } 
/* 87 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean match(Cookie cookie, CookieOrigin origin) {
/* 92 */     Args.notNull(cookie, "Cookie");
/* 93 */     Args.notNull(origin, "Cookie origin");
/* 94 */     return pathMatch(origin.getPath(), cookie.getPath());
/*    */   }
/*    */ 
/*    */   
/*    */   public String getAttributeName() {
/* 99 */     return "path";
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\cookie\BasicPathHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */