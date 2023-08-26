/*     */ package org.apache.http.client.protocol;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HeaderIterator;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.HttpResponseInterceptor;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.client.CookieStore;
/*     */ import org.apache.http.cookie.Cookie;
/*     */ import org.apache.http.cookie.CookieOrigin;
/*     */ import org.apache.http.cookie.CookieSpec;
/*     */ import org.apache.http.cookie.MalformedCookieException;
/*     */ import org.apache.http.protocol.HttpContext;
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
/*     */ @Immutable
/*     */ public class ResponseProcessCookies
/*     */   implements HttpResponseInterceptor
/*     */ {
/*  59 */   private final Log log = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
/*  68 */     Args.notNull(response, "HTTP request");
/*  69 */     Args.notNull(context, "HTTP context");
/*     */     
/*  71 */     HttpClientContext clientContext = HttpClientContext.adapt(context);
/*     */ 
/*     */     
/*  74 */     CookieSpec cookieSpec = clientContext.getCookieSpec();
/*  75 */     if (cookieSpec == null) {
/*  76 */       this.log.debug("Cookie spec not specified in HTTP context");
/*     */       
/*     */       return;
/*     */     } 
/*  80 */     CookieStore cookieStore = clientContext.getCookieStore();
/*  81 */     if (cookieStore == null) {
/*  82 */       this.log.debug("Cookie store not specified in HTTP context");
/*     */       
/*     */       return;
/*     */     } 
/*  86 */     CookieOrigin cookieOrigin = clientContext.getCookieOrigin();
/*  87 */     if (cookieOrigin == null) {
/*  88 */       this.log.debug("Cookie origin not specified in HTTP context");
/*     */       return;
/*     */     } 
/*  91 */     HeaderIterator it = response.headerIterator("Set-Cookie");
/*  92 */     processCookies(it, cookieSpec, cookieOrigin, cookieStore);
/*     */ 
/*     */     
/*  95 */     if (cookieSpec.getVersion() > 0) {
/*     */ 
/*     */       
/*  98 */       it = response.headerIterator("Set-Cookie2");
/*  99 */       processCookies(it, cookieSpec, cookieOrigin, cookieStore);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processCookies(HeaderIterator iterator, CookieSpec cookieSpec, CookieOrigin cookieOrigin, CookieStore cookieStore) {
/* 108 */     while (iterator.hasNext()) {
/* 109 */       Header header = iterator.nextHeader();
/*     */       try {
/* 111 */         List<Cookie> cookies = cookieSpec.parse(header, cookieOrigin);
/* 112 */         for (Cookie cookie : cookies) {
/*     */           try {
/* 114 */             cookieSpec.validate(cookie, cookieOrigin);
/* 115 */             cookieStore.addCookie(cookie);
/*     */             
/* 117 */             if (this.log.isDebugEnabled()) {
/* 118 */               this.log.debug("Cookie accepted [" + formatCooke(cookie) + "]");
/*     */             }
/* 120 */           } catch (MalformedCookieException ex) {
/* 121 */             if (this.log.isWarnEnabled()) {
/* 122 */               this.log.warn("Cookie rejected [" + formatCooke(cookie) + "] " + ex.getMessage());
/*     */             }
/*     */           }
/*     */         
/*     */         } 
/* 127 */       } catch (MalformedCookieException ex) {
/* 128 */         if (this.log.isWarnEnabled()) {
/* 129 */           this.log.warn("Invalid cookie header: \"" + header + "\". " + ex.getMessage());
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static String formatCooke(Cookie cookie) {
/* 137 */     StringBuilder buf = new StringBuilder();
/* 138 */     buf.append(cookie.getName());
/* 139 */     buf.append("=\"");
/* 140 */     String v = cookie.getValue();
/* 141 */     if (v != null) {
/* 142 */       if (v.length() > 100) {
/* 143 */         v = v.substring(0, 100) + "...";
/*     */       }
/* 145 */       buf.append(v);
/*     */     } 
/* 147 */     buf.append("\"");
/* 148 */     buf.append(", version:");
/* 149 */     buf.append(Integer.toString(cookie.getVersion()));
/* 150 */     buf.append(", domain:");
/* 151 */     buf.append(cookie.getDomain());
/* 152 */     buf.append(", path:");
/* 153 */     buf.append(cookie.getPath());
/* 154 */     buf.append(", expiry:");
/* 155 */     buf.append(cookie.getExpiryDate());
/* 156 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\client\protocol\ResponseProcessCookies.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */