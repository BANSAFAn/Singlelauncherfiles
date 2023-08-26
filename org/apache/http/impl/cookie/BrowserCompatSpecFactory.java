/*    */ package org.apache.http.impl.cookie;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.cookie.CookieSpec;
/*    */ import org.apache.http.cookie.CookieSpecFactory;
/*    */ import org.apache.http.cookie.CookieSpecProvider;
/*    */ import org.apache.http.params.HttpParams;
/*    */ import org.apache.http.protocol.HttpContext;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
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
/*    */ @Immutable
/*    */ public class BrowserCompatSpecFactory
/*    */   implements CookieSpecFactory, CookieSpecProvider
/*    */ {
/*    */   private final SecurityLevel securityLevel;
/*    */   private final CookieSpec cookieSpec;
/*    */   
/*    */   public enum SecurityLevel
/*    */   {
/* 54 */     SECURITYLEVEL_DEFAULT,
/* 55 */     SECURITYLEVEL_IE_MEDIUM;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BrowserCompatSpecFactory(String[] datepatterns, SecurityLevel securityLevel) {
/* 63 */     this.securityLevel = securityLevel;
/* 64 */     this.cookieSpec = new BrowserCompatSpec(datepatterns, securityLevel);
/*    */   }
/*    */   
/*    */   public BrowserCompatSpecFactory(String[] datepatterns) {
/* 68 */     this(null, SecurityLevel.SECURITYLEVEL_DEFAULT);
/*    */   }
/*    */   
/*    */   public BrowserCompatSpecFactory() {
/* 72 */     this(null, SecurityLevel.SECURITYLEVEL_DEFAULT);
/*    */   }
/*    */ 
/*    */   
/*    */   public CookieSpec newInstance(HttpParams params) {
/* 77 */     if (params != null) {
/*    */       
/* 79 */       String[] patterns = null;
/* 80 */       Collection<?> param = (Collection)params.getParameter("http.protocol.cookie-datepatterns");
/*    */       
/* 82 */       if (param != null) {
/* 83 */         patterns = new String[param.size()];
/* 84 */         patterns = param.<String>toArray(patterns);
/*    */       } 
/* 86 */       return new BrowserCompatSpec(patterns, this.securityLevel);
/*    */     } 
/* 88 */     return new BrowserCompatSpec(null, this.securityLevel);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public CookieSpec create(HttpContext context) {
/* 94 */     return this.cookieSpec;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\cookie\BrowserCompatSpecFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */