/*     */ package org.apache.http.impl.cookie;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.cookie.ClientCookie;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Immutable
/*     */ public class RFC2965DomainAttributeHandler
/*     */   implements CommonCookieAttributeHandler
/*     */ {
/*     */   public void parse(SetCookie cookie, String domain) throws MalformedCookieException {
/*  61 */     Args.notNull(cookie, "Cookie");
/*  62 */     if (domain == null) {
/*  63 */       throw new MalformedCookieException("Missing value for domain attribute");
/*     */     }
/*     */     
/*  66 */     if (domain.trim().isEmpty()) {
/*  67 */       throw new MalformedCookieException("Blank value for domain attribute");
/*     */     }
/*     */     
/*  70 */     String s = domain;
/*  71 */     s = s.toLowerCase(Locale.ROOT);
/*  72 */     if (!domain.startsWith("."))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  78 */       s = '.' + s;
/*     */     }
/*  80 */     cookie.setDomain(s);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean domainMatch(String host, String domain) {
/* 100 */     boolean match = (host.equals(domain) || (domain.startsWith(".") && host.endsWith(domain)));
/*     */ 
/*     */     
/* 103 */     return match;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
/* 112 */     Args.notNull(cookie, "Cookie");
/* 113 */     Args.notNull(origin, "Cookie origin");
/* 114 */     String host = origin.getHost().toLowerCase(Locale.ROOT);
/* 115 */     if (cookie.getDomain() == null) {
/* 116 */       throw new CookieRestrictionViolationException("Invalid cookie state: domain not specified");
/*     */     }
/*     */     
/* 119 */     String cookieDomain = cookie.getDomain().toLowerCase(Locale.ROOT);
/*     */     
/* 121 */     if (cookie instanceof ClientCookie && ((ClientCookie)cookie).containsAttribute("domain")) {
/*     */ 
/*     */       
/* 124 */       if (!cookieDomain.startsWith(".")) {
/* 125 */         throw new CookieRestrictionViolationException("Domain attribute \"" + cookie.getDomain() + "\" violates RFC 2109: domain must start with a dot");
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 131 */       int dotIndex = cookieDomain.indexOf('.', 1);
/* 132 */       if ((dotIndex < 0 || dotIndex == cookieDomain.length() - 1) && !cookieDomain.equals(".local"))
/*     */       {
/* 134 */         throw new CookieRestrictionViolationException("Domain attribute \"" + cookie.getDomain() + "\" violates RFC 2965: the value contains no embedded dots " + "and the value is not .local");
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 141 */       if (!domainMatch(host, cookieDomain)) {
/* 142 */         throw new CookieRestrictionViolationException("Domain attribute \"" + cookie.getDomain() + "\" violates RFC 2965: effective host name does not " + "domain-match domain attribute.");
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 149 */       String effectiveHostWithoutDomain = host.substring(0, host.length() - cookieDomain.length());
/*     */       
/* 151 */       if (effectiveHostWithoutDomain.indexOf('.') != -1) {
/* 152 */         throw new CookieRestrictionViolationException("Domain attribute \"" + cookie.getDomain() + "\" violates RFC 2965: " + "effective host minus domain may not contain any dots");
/*     */ 
/*     */       
/*     */       }
/*     */ 
/*     */     
/*     */     }
/* 159 */     else if (!cookie.getDomain().equals(host)) {
/* 160 */       throw new CookieRestrictionViolationException("Illegal domain attribute: \"" + cookie.getDomain() + "\"." + "Domain of origin: \"" + host + "\"");
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
/*     */   public boolean match(Cookie cookie, CookieOrigin origin) {
/* 173 */     Args.notNull(cookie, "Cookie");
/* 174 */     Args.notNull(origin, "Cookie origin");
/* 175 */     String host = origin.getHost().toLowerCase(Locale.ROOT);
/* 176 */     String cookieDomain = cookie.getDomain();
/*     */ 
/*     */ 
/*     */     
/* 180 */     if (!domainMatch(host, cookieDomain)) {
/* 181 */       return false;
/*     */     }
/*     */     
/* 184 */     String effectiveHostWithoutDomain = host.substring(0, host.length() - cookieDomain.length());
/*     */     
/* 186 */     return (effectiveHostWithoutDomain.indexOf('.') == -1);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAttributeName() {
/* 191 */     return "domain";
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\cookie\RFC2965DomainAttributeHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */