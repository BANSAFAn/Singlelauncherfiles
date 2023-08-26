/*    */ package org.apache.http.impl.cookie;
/*    */ 
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.annotation.Obsolete;
/*    */ import org.apache.http.conn.util.PublicSuffixMatcher;
/*    */ import org.apache.http.cookie.CommonCookieAttributeHandler;
/*    */ import org.apache.http.cookie.CookieSpec;
/*    */ import org.apache.http.cookie.CookieSpecProvider;
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
/*    */ @Obsolete
/*    */ @Immutable
/*    */ public class RFC2109SpecProvider
/*    */   implements CookieSpecProvider
/*    */ {
/*    */   private final PublicSuffixMatcher publicSuffixMatcher;
/*    */   private final boolean oneHeader;
/*    */   private volatile CookieSpec cookieSpec;
/*    */   
/*    */   public RFC2109SpecProvider(PublicSuffixMatcher publicSuffixMatcher, boolean oneHeader) {
/* 58 */     this.oneHeader = oneHeader;
/* 59 */     this.publicSuffixMatcher = publicSuffixMatcher;
/*    */   }
/*    */   
/*    */   public RFC2109SpecProvider(PublicSuffixMatcher publicSuffixMatcher) {
/* 63 */     this(publicSuffixMatcher, false);
/*    */   }
/*    */   
/*    */   public RFC2109SpecProvider() {
/* 67 */     this(null, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public CookieSpec create(HttpContext context) {
/* 72 */     if (this.cookieSpec == null) {
/* 73 */       synchronized (this) {
/* 74 */         if (this.cookieSpec == null) {
/* 75 */           this.cookieSpec = new RFC2109Spec(this.oneHeader, new CommonCookieAttributeHandler[] { new RFC2109VersionHandler(), new BasicPathHandler(), PublicSuffixDomainFilter.decorate(new RFC2109DomainHandler(), this.publicSuffixMatcher), new BasicMaxAgeHandler(), new BasicSecureHandler(), new BasicCommentHandler() });
/*    */         }
/*    */       } 
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 86 */     return this.cookieSpec;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\cookie\RFC2109SpecProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */