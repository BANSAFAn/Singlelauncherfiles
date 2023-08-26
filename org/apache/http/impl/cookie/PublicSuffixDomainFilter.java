/*     */ package org.apache.http.impl.cookie;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.conn.util.PublicSuffixList;
/*     */ import org.apache.http.conn.util.PublicSuffixMatcher;
/*     */ import org.apache.http.cookie.CommonCookieAttributeHandler;
/*     */ import org.apache.http.cookie.Cookie;
/*     */ import org.apache.http.cookie.CookieOrigin;
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
/*     */ public class PublicSuffixDomainFilter
/*     */   implements CommonCookieAttributeHandler
/*     */ {
/*     */   private final CommonCookieAttributeHandler handler;
/*     */   private final PublicSuffixMatcher publicSuffixMatcher;
/*     */   private final Map<String, Boolean> localDomainMap;
/*     */   
/*     */   private static Map<String, Boolean> createLocalDomainMap() {
/*  61 */     ConcurrentHashMap<String, Boolean> map = new ConcurrentHashMap<String, Boolean>();
/*  62 */     map.put(".localhost.", Boolean.TRUE);
/*  63 */     map.put(".test.", Boolean.TRUE);
/*  64 */     map.put(".local.", Boolean.TRUE);
/*  65 */     map.put(".local", Boolean.TRUE);
/*  66 */     map.put(".localdomain", Boolean.TRUE);
/*  67 */     return map;
/*     */   }
/*     */ 
/*     */   
/*     */   public PublicSuffixDomainFilter(CommonCookieAttributeHandler handler, PublicSuffixMatcher publicSuffixMatcher) {
/*  72 */     this.handler = (CommonCookieAttributeHandler)Args.notNull(handler, "Cookie handler");
/*  73 */     this.publicSuffixMatcher = (PublicSuffixMatcher)Args.notNull(publicSuffixMatcher, "Public suffix matcher");
/*  74 */     this.localDomainMap = createLocalDomainMap();
/*     */   }
/*     */ 
/*     */   
/*     */   public PublicSuffixDomainFilter(CommonCookieAttributeHandler handler, PublicSuffixList suffixList) {
/*  79 */     Args.notNull(handler, "Cookie handler");
/*  80 */     Args.notNull(suffixList, "Public suffix list");
/*  81 */     this.handler = handler;
/*  82 */     this.publicSuffixMatcher = new PublicSuffixMatcher(suffixList.getRules(), suffixList.getExceptions());
/*  83 */     this.localDomainMap = createLocalDomainMap();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean match(Cookie cookie, CookieOrigin origin) {
/*  91 */     String host = cookie.getDomain();
/*  92 */     int i = host.indexOf('.');
/*  93 */     if (i >= 0) {
/*  94 */       String domain = host.substring(i);
/*  95 */       if (!this.localDomainMap.containsKey(domain) && 
/*  96 */         this.publicSuffixMatcher.matches(host)) {
/*  97 */         return false;
/*     */       
/*     */       }
/*     */     }
/* 101 */     else if (!host.equalsIgnoreCase(origin.getHost()) && 
/* 102 */       this.publicSuffixMatcher.matches(host)) {
/* 103 */       return false;
/*     */     } 
/*     */ 
/*     */     
/* 107 */     return this.handler.match(cookie, origin);
/*     */   }
/*     */ 
/*     */   
/*     */   public void parse(SetCookie cookie, String value) throws MalformedCookieException {
/* 112 */     this.handler.parse(cookie, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
/* 117 */     this.handler.validate(cookie, origin);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAttributeName() {
/* 122 */     return this.handler.getAttributeName();
/*     */   }
/*     */ 
/*     */   
/*     */   public static CommonCookieAttributeHandler decorate(CommonCookieAttributeHandler handler, PublicSuffixMatcher publicSuffixMatcher) {
/* 127 */     Args.notNull(handler, "Cookie attribute handler");
/* 128 */     return (publicSuffixMatcher != null) ? new PublicSuffixDomainFilter(handler, publicSuffixMatcher) : handler;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\cookie\PublicSuffixDomainFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */