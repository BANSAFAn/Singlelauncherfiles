/*     */ package org.apache.http.impl.cookie;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import java.util.StringTokenizer;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.cookie.Cookie;
/*     */ import org.apache.http.cookie.CookieOrigin;
/*     */ import org.apache.http.cookie.CookieRestrictionViolationException;
/*     */ import org.apache.http.cookie.MalformedCookieException;
/*     */ import org.apache.http.cookie.SetCookie;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.TextUtils;
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
/*     */ public class NetscapeDomainHandler
/*     */   extends BasicDomainHandler
/*     */ {
/*     */   public void parse(SetCookie cookie, String value) throws MalformedCookieException {
/*  55 */     Args.notNull(cookie, "Cookie");
/*  56 */     if (TextUtils.isBlank(value)) {
/*  57 */       throw new MalformedCookieException("Blank or null value for domain attribute");
/*     */     }
/*  59 */     cookie.setDomain(value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
/*  65 */     String host = origin.getHost();
/*  66 */     String domain = cookie.getDomain();
/*  67 */     if (!host.equals(domain) && !BasicDomainHandler.domainMatch(domain, host)) {
/*  68 */       throw new CookieRestrictionViolationException("Illegal domain attribute \"" + domain + "\". Domain of origin: \"" + host + "\"");
/*     */     }
/*     */     
/*  71 */     if (host.contains(".")) {
/*  72 */       int domainParts = (new StringTokenizer(domain, ".")).countTokens();
/*     */       
/*  74 */       if (isSpecialDomain(domain)) {
/*  75 */         if (domainParts < 2) {
/*  76 */           throw new CookieRestrictionViolationException("Domain attribute \"" + domain + "\" violates the Netscape cookie specification for " + "special domains");
/*     */ 
/*     */         
/*     */         }
/*     */       
/*     */       }
/*  82 */       else if (domainParts < 3) {
/*  83 */         throw new CookieRestrictionViolationException("Domain attribute \"" + domain + "\" violates the Netscape cookie specification");
/*     */       } 
/*     */     } 
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
/*     */   private static boolean isSpecialDomain(String domain) {
/*  98 */     String ucDomain = domain.toUpperCase(Locale.ROOT);
/*  99 */     return (ucDomain.endsWith(".COM") || ucDomain.endsWith(".EDU") || ucDomain.endsWith(".NET") || ucDomain.endsWith(".GOV") || ucDomain.endsWith(".MIL") || ucDomain.endsWith(".ORG") || ucDomain.endsWith(".INT"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean match(Cookie cookie, CookieOrigin origin) {
/* 110 */     Args.notNull(cookie, "Cookie");
/* 111 */     Args.notNull(origin, "Cookie origin");
/* 112 */     String host = origin.getHost();
/* 113 */     String domain = cookie.getDomain();
/* 114 */     if (domain == null) {
/* 115 */       return false;
/*     */     }
/* 117 */     return host.endsWith(domain);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAttributeName() {
/* 122 */     return "domain";
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\cookie\NetscapeDomainHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */