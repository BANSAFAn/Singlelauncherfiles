/*     */ package org.apache.http.impl.cookie;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.cookie.CommonCookieAttributeHandler;
/*     */ import org.apache.http.cookie.Cookie;
/*     */ import org.apache.http.cookie.CookieOrigin;
/*     */ import org.apache.http.cookie.CookieRestrictionViolationException;
/*     */ import org.apache.http.cookie.MalformedCookieException;
/*     */ import org.apache.http.cookie.SetCookie;
/*     */ import org.apache.http.util.Args;
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
/*     */ 
/*     */ @Immutable
/*     */ public class RFC2109DomainHandler
/*     */   implements CommonCookieAttributeHandler
/*     */ {
/*     */   public void parse(SetCookie cookie, String value) throws MalformedCookieException {
/*  55 */     Args.notNull(cookie, "Cookie");
/*  56 */     if (value == null) {
/*  57 */       throw new MalformedCookieException("Missing value for domain attribute");
/*     */     }
/*  59 */     if (value.trim().isEmpty()) {
/*  60 */       throw new MalformedCookieException("Blank value for domain attribute");
/*     */     }
/*  62 */     cookie.setDomain(value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
/*  68 */     Args.notNull(cookie, "Cookie");
/*  69 */     Args.notNull(origin, "Cookie origin");
/*  70 */     String host = origin.getHost();
/*  71 */     String domain = cookie.getDomain();
/*  72 */     if (domain == null) {
/*  73 */       throw new CookieRestrictionViolationException("Cookie domain may not be null");
/*     */     }
/*  75 */     if (!domain.equals(host)) {
/*  76 */       int dotIndex = domain.indexOf('.');
/*  77 */       if (dotIndex == -1) {
/*  78 */         throw new CookieRestrictionViolationException("Domain attribute \"" + domain + "\" does not match the host \"" + host + "\"");
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  84 */       if (!domain.startsWith(".")) {
/*  85 */         throw new CookieRestrictionViolationException("Domain attribute \"" + domain + "\" violates RFC 2109: domain must start with a dot");
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*  90 */       dotIndex = domain.indexOf('.', 1);
/*  91 */       if (dotIndex < 0 || dotIndex == domain.length() - 1) {
/*  92 */         throw new CookieRestrictionViolationException("Domain attribute \"" + domain + "\" violates RFC 2109: domain must contain an embedded dot");
/*     */       }
/*     */ 
/*     */       
/*  96 */       host = host.toLowerCase(Locale.ROOT);
/*  97 */       if (!host.endsWith(domain)) {
/*  98 */         throw new CookieRestrictionViolationException("Illegal domain attribute \"" + domain + "\". Domain of origin: \"" + host + "\"");
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 103 */       String hostWithoutDomain = host.substring(0, host.length() - domain.length());
/* 104 */       if (hostWithoutDomain.indexOf('.') != -1) {
/* 105 */         throw new CookieRestrictionViolationException("Domain attribute \"" + domain + "\" violates RFC 2109: host minus domain may not contain any dots");
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean match(Cookie cookie, CookieOrigin origin) {
/* 114 */     Args.notNull(cookie, "Cookie");
/* 115 */     Args.notNull(origin, "Cookie origin");
/* 116 */     String host = origin.getHost();
/* 117 */     String domain = cookie.getDomain();
/* 118 */     if (domain == null) {
/* 119 */       return false;
/*     */     }
/* 121 */     return (host.equals(domain) || (domain.startsWith(".") && host.endsWith(domain)));
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAttributeName() {
/* 126 */     return "domain";
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\cookie\RFC2109DomainHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */