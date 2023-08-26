/*    */ package org.apache.http.impl.cookie;
/*    */ 
/*    */ import java.util.Date;
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.cookie.CommonCookieAttributeHandler;
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
/*    */ @Immutable
/*    */ public class LaxMaxAgeHandler
/*    */   extends AbstractCookieAttributeHandler
/*    */   implements CommonCookieAttributeHandler
/*    */ {
/* 48 */   private static final Pattern MAX_AGE_PATTERN = Pattern.compile("^\\-?[0-9]+$");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void parse(SetCookie cookie, String value) throws MalformedCookieException {
/* 56 */     Args.notNull(cookie, "Cookie");
/* 57 */     if (TextUtils.isBlank(value)) {
/*    */       return;
/*    */     }
/* 60 */     Matcher matcher = MAX_AGE_PATTERN.matcher(value);
/* 61 */     if (matcher.matches()) {
/*    */       int age;
/*    */       try {
/* 64 */         age = Integer.parseInt(value);
/* 65 */       } catch (NumberFormatException e) {
/*    */         return;
/*    */       } 
/* 68 */       Date expiryDate = (age >= 0) ? new Date(System.currentTimeMillis() + age * 1000L) : new Date(Long.MIN_VALUE);
/*    */       
/* 70 */       cookie.setExpiryDate(expiryDate);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String getAttributeName() {
/* 76 */     return "max-age";
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\cookie\LaxMaxAgeHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */