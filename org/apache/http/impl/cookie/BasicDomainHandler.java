/*     */ package org.apache.http.impl.cookie;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.conn.util.InetAddressUtils;
/*     */ import org.apache.http.cookie.ClientCookie;
/*     */ import org.apache.http.cookie.CommonCookieAttributeHandler;
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
/*     */ public class BasicDomainHandler
/*     */   implements CommonCookieAttributeHandler
/*     */ {
/*     */   public void parse(SetCookie cookie, String value) throws MalformedCookieException {
/*  57 */     Args.notNull(cookie, "Cookie");
/*  58 */     if (TextUtils.isBlank(value)) {
/*  59 */       throw new MalformedCookieException("Blank or null value for domain attribute");
/*     */     }
/*     */     
/*  62 */     if (value.endsWith(".")) {
/*     */       return;
/*     */     }
/*  65 */     String domain = value;
/*  66 */     if (domain.startsWith(".")) {
/*  67 */       domain = domain.substring(1);
/*     */     }
/*  69 */     domain = domain.toLowerCase(Locale.ROOT);
/*  70 */     cookie.setDomain(domain);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
/*  76 */     Args.notNull(cookie, "Cookie");
/*  77 */     Args.notNull(origin, "Cookie origin");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  83 */     String host = origin.getHost();
/*  84 */     String domain = cookie.getDomain();
/*  85 */     if (domain == null) {
/*  86 */       throw new CookieRestrictionViolationException("Cookie 'domain' may not be null");
/*     */     }
/*  88 */     if (!host.equals(domain) && !domainMatch(domain, host)) {
/*  89 */       throw new CookieRestrictionViolationException("Illegal 'domain' attribute \"" + domain + "\". Domain of origin: \"" + host + "\"");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static boolean domainMatch(String domain, String host) {
/*  95 */     if (InetAddressUtils.isIPv4Address(host) || InetAddressUtils.isIPv6Address(host)) {
/*  96 */       return false;
/*     */     }
/*  98 */     String normalizedDomain = domain.startsWith(".") ? domain.substring(1) : domain;
/*  99 */     if (host.endsWith(normalizedDomain)) {
/* 100 */       int prefix = host.length() - normalizedDomain.length();
/*     */       
/* 102 */       if (prefix == 0) {
/* 103 */         return true;
/*     */       }
/* 105 */       if (prefix > 1 && host.charAt(prefix - 1) == '.') {
/* 106 */         return true;
/*     */       }
/*     */     } 
/* 109 */     return false;
/*     */   }
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
/* 121 */     if (domain.startsWith(".")) {
/* 122 */       domain = domain.substring(1);
/*     */     }
/* 124 */     domain = domain.toLowerCase(Locale.ROOT);
/* 125 */     if (host.equals(domain)) {
/* 126 */       return true;
/*     */     }
/* 128 */     if (cookie instanceof ClientCookie && (
/* 129 */       (ClientCookie)cookie).containsAttribute("domain")) {
/* 130 */       return domainMatch(domain, host);
/*     */     }
/*     */     
/* 133 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAttributeName() {
/* 138 */     return "domain";
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\cookie\BasicDomainHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */