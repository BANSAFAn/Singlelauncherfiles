/*     */ package org.apache.http.impl.cookie;
/*     */ 
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.conn.util.PublicSuffixMatcher;
/*     */ import org.apache.http.cookie.CommonCookieAttributeHandler;
/*     */ import org.apache.http.cookie.Cookie;
/*     */ import org.apache.http.cookie.CookieOrigin;
/*     */ import org.apache.http.cookie.CookieSpec;
/*     */ import org.apache.http.cookie.CookieSpecProvider;
/*     */ import org.apache.http.cookie.MalformedCookieException;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Immutable
/*     */ public class DefaultCookieSpecProvider
/*     */   implements CookieSpecProvider
/*     */ {
/*     */   private final CompatibilityLevel compatibilityLevel;
/*     */   private final PublicSuffixMatcher publicSuffixMatcher;
/*     */   private final String[] datepatterns;
/*     */   private final boolean oneHeader;
/*     */   private volatile CookieSpec cookieSpec;
/*     */   
/*     */   public enum CompatibilityLevel
/*     */   {
/*  50 */     DEFAULT,
/*  51 */     IE_MEDIUM_SECURITY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultCookieSpecProvider(CompatibilityLevel compatibilityLevel, PublicSuffixMatcher publicSuffixMatcher, String[] datepatterns, boolean oneHeader) {
/*  67 */     this.compatibilityLevel = (compatibilityLevel != null) ? compatibilityLevel : CompatibilityLevel.DEFAULT;
/*  68 */     this.publicSuffixMatcher = publicSuffixMatcher;
/*  69 */     this.datepatterns = datepatterns;
/*  70 */     this.oneHeader = oneHeader;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultCookieSpecProvider(CompatibilityLevel compatibilityLevel, PublicSuffixMatcher publicSuffixMatcher) {
/*  76 */     this(compatibilityLevel, publicSuffixMatcher, null, false);
/*     */   }
/*     */   
/*     */   public DefaultCookieSpecProvider(PublicSuffixMatcher publicSuffixMatcher) {
/*  80 */     this(CompatibilityLevel.DEFAULT, publicSuffixMatcher, null, false);
/*     */   }
/*     */   
/*     */   public DefaultCookieSpecProvider() {
/*  84 */     this(CompatibilityLevel.DEFAULT, null, null, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public CookieSpec create(HttpContext context) {
/*  89 */     if (this.cookieSpec == null) {
/*  90 */       synchronized (this) {
/*  91 */         if (this.cookieSpec == null) {
/*  92 */           RFC2965Spec strict = new RFC2965Spec(this.oneHeader, new CommonCookieAttributeHandler[] { new RFC2965VersionAttributeHandler(), new BasicPathHandler(), PublicSuffixDomainFilter.decorate(new RFC2965DomainAttributeHandler(), this.publicSuffixMatcher), new RFC2965PortAttributeHandler(), new BasicMaxAgeHandler(), new BasicSecureHandler(), new BasicCommentHandler(), new RFC2965CommentUrlAttributeHandler(), new RFC2965DiscardAttributeHandler() });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 103 */           RFC2109Spec obsoleteStrict = new RFC2109Spec(this.oneHeader, new CommonCookieAttributeHandler[] { new RFC2109VersionHandler(), new BasicPathHandler(), PublicSuffixDomainFilter.decorate(new RFC2109DomainHandler(), this.publicSuffixMatcher), new BasicMaxAgeHandler(), new BasicSecureHandler(), new BasicCommentHandler() });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 111 */           (new CommonCookieAttributeHandler[5])[0] = PublicSuffixDomainFilter.decorate(new BasicDomainHandler(), this.publicSuffixMatcher); (new CommonCookieAttributeHandler[5])[1] = (this.compatibilityLevel == CompatibilityLevel.IE_MEDIUM_SECURITY) ? new BasicPathHandler() { public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {} } : new BasicPathHandler(); (new CommonCookieAttributeHandler[5])[2] = new BasicSecureHandler(); (new CommonCookieAttributeHandler[5])[3] = new BasicCommentHandler(); (new String[1])[0] = "EEE, dd-MMM-yy HH:mm:ss z"; NetscapeDraftSpec netscapeDraft = new NetscapeDraftSpec(new CommonCookieAttributeHandler[] { null, null, null, null, new BasicExpiresHandler((this.datepatterns != null) ? (String[])this.datepatterns.clone() : new String[1]) });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 127 */           this.cookieSpec = new DefaultCookieSpec(strict, obsoleteStrict, netscapeDraft);
/*     */         } 
/*     */       } 
/*     */     }
/* 131 */     return this.cookieSpec;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\cookie\DefaultCookieSpecProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */