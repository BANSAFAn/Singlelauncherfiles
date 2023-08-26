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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Deprecated
/*    */ @Immutable
/*    */ public class RFC2965SpecFactory
/*    */   implements CookieSpecFactory, CookieSpecProvider
/*    */ {
/*    */   private final CookieSpec cookieSpec;
/*    */   
/*    */   public RFC2965SpecFactory(String[] datepatterns, boolean oneHeader) {
/* 57 */     this.cookieSpec = new RFC2965Spec(datepatterns, oneHeader);
/*    */   }
/*    */   
/*    */   public RFC2965SpecFactory() {
/* 61 */     this(null, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public CookieSpec newInstance(HttpParams params) {
/* 66 */     if (params != null) {
/*    */       
/* 68 */       String[] patterns = null;
/* 69 */       Collection<?> param = (Collection)params.getParameter("http.protocol.cookie-datepatterns");
/*    */       
/* 71 */       if (param != null) {
/* 72 */         patterns = new String[param.size()];
/* 73 */         patterns = param.<String>toArray(patterns);
/*    */       } 
/* 75 */       boolean singleHeader = params.getBooleanParameter("http.protocol.single-cookie-header", false);
/*    */ 
/*    */       
/* 78 */       return new RFC2965Spec(patterns, singleHeader);
/*    */     } 
/* 80 */     return new RFC2965Spec();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public CookieSpec create(HttpContext context) {
/* 86 */     return this.cookieSpec;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\cookie\RFC2965SpecFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */