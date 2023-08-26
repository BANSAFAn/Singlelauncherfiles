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
/*    */ public class RFC2965SpecProvider
/*    */   implements CookieSpecProvider
/*    */ {
/*    */   private final PublicSuffixMatcher publicSuffixMatcher;
/*    */   private final boolean oneHeader;
/*    */   private volatile CookieSpec cookieSpec;
/*    */   
/*    */   public RFC2965SpecProvider(PublicSuffixMatcher publicSuffixMatcher, boolean oneHeader) {
/* 58 */     this.oneHeader = oneHeader;
/* 59 */     this.publicSuffixMatcher = publicSuffixMatcher;
/*    */   }
/*    */   
/*    */   public RFC2965SpecProvider(PublicSuffixMatcher publicSuffixMatcher) {
/* 63 */     this(publicSuffixMatcher, false);
/*    */   }
/*    */   
/*    */   public RFC2965SpecProvider() {
/* 67 */     this(null, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public CookieSpec create(HttpContext context) {
/* 72 */     if (this.cookieSpec == null) {
/* 73 */       synchronized (this) {
/* 74 */         if (this.cookieSpec == null) {
/* 75 */           this.cookieSpec = new RFC2965Spec(this.oneHeader, new CommonCookieAttributeHandler[] { new RFC2965VersionAttributeHandler(), new BasicPathHandler(), PublicSuffixDomainFilter.decorate(new RFC2965DomainAttributeHandler(), this.publicSuffixMatcher), new RFC2965PortAttributeHandler(), new BasicMaxAgeHandler(), new BasicSecureHandler(), new BasicCommentHandler(), new RFC2965CommentUrlAttributeHandler(), new RFC2965DiscardAttributeHandler() });
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
/*    */ 
/*    */ 
/*    */     
/* 89 */     return this.cookieSpec;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\cookie\RFC2965SpecProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */