/*     */ package org.apache.http.client.protocol;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpRequestInterceptor;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.client.CookieStore;
/*     */ import org.apache.http.client.config.RequestConfig;
/*     */ import org.apache.http.client.methods.HttpUriRequest;
/*     */ import org.apache.http.config.Lookup;
/*     */ import org.apache.http.conn.routing.RouteInfo;
/*     */ import org.apache.http.cookie.Cookie;
/*     */ import org.apache.http.cookie.CookieOrigin;
/*     */ import org.apache.http.cookie.CookieSpec;
/*     */ import org.apache.http.cookie.CookieSpecProvider;
/*     */ import org.apache.http.protocol.HttpContext;
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
/*     */ @Immutable
/*     */ public class RequestAddCookies
/*     */   implements HttpRequestInterceptor
/*     */ {
/*  69 */   private final Log log = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
/*  78 */     Args.notNull(request, "HTTP request");
/*  79 */     Args.notNull(context, "HTTP context");
/*     */     
/*  81 */     String method = request.getRequestLine().getMethod();
/*  82 */     if (method.equalsIgnoreCase("CONNECT")) {
/*     */       return;
/*     */     }
/*     */     
/*  86 */     HttpClientContext clientContext = HttpClientContext.adapt(context);
/*     */ 
/*     */     
/*  89 */     CookieStore cookieStore = clientContext.getCookieStore();
/*  90 */     if (cookieStore == null) {
/*  91 */       this.log.debug("Cookie store not specified in HTTP context");
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  96 */     Lookup<CookieSpecProvider> registry = clientContext.getCookieSpecRegistry();
/*  97 */     if (registry == null) {
/*  98 */       this.log.debug("CookieSpec registry not specified in HTTP context");
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 103 */     HttpHost targetHost = clientContext.getTargetHost();
/* 104 */     if (targetHost == null) {
/* 105 */       this.log.debug("Target host not set in the context");
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 110 */     RouteInfo route = clientContext.getHttpRoute();
/* 111 */     if (route == null) {
/* 112 */       this.log.debug("Connection route not set in the context");
/*     */       
/*     */       return;
/*     */     } 
/* 116 */     RequestConfig config = clientContext.getRequestConfig();
/* 117 */     String policy = config.getCookieSpec();
/* 118 */     if (policy == null) {
/* 119 */       policy = "default";
/*     */     }
/* 121 */     if (this.log.isDebugEnabled()) {
/* 122 */       this.log.debug("CookieSpec selected: " + policy);
/*     */     }
/*     */     
/* 125 */     URI requestURI = null;
/* 126 */     if (request instanceof HttpUriRequest) {
/* 127 */       requestURI = ((HttpUriRequest)request).getURI();
/*     */     } else {
/*     */       try {
/* 130 */         requestURI = new URI(request.getRequestLine().getUri());
/* 131 */       } catch (URISyntaxException ignore) {}
/*     */     } 
/*     */     
/* 134 */     String path = (requestURI != null) ? requestURI.getPath() : null;
/* 135 */     String hostName = targetHost.getHostName();
/* 136 */     int port = targetHost.getPort();
/* 137 */     if (port < 0) {
/* 138 */       port = route.getTargetHost().getPort();
/*     */     }
/*     */     
/* 141 */     CookieOrigin cookieOrigin = new CookieOrigin(hostName, (port >= 0) ? port : 0, !TextUtils.isEmpty(path) ? path : "/", route.isSecure());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 148 */     CookieSpecProvider provider = (CookieSpecProvider)registry.lookup(policy);
/* 149 */     if (provider == null) {
/* 150 */       if (this.log.isDebugEnabled()) {
/* 151 */         this.log.debug("Unsupported cookie policy: " + policy);
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/* 156 */     CookieSpec cookieSpec = provider.create((HttpContext)clientContext);
/*     */     
/* 158 */     List<Cookie> cookies = cookieStore.getCookies();
/*     */     
/* 160 */     List<Cookie> matchedCookies = new ArrayList<Cookie>();
/* 161 */     Date now = new Date();
/* 162 */     boolean expired = false;
/* 163 */     for (Cookie cookie : cookies) {
/* 164 */       if (!cookie.isExpired(now)) {
/* 165 */         if (cookieSpec.match(cookie, cookieOrigin)) {
/* 166 */           if (this.log.isDebugEnabled()) {
/* 167 */             this.log.debug("Cookie " + cookie + " match " + cookieOrigin);
/*     */           }
/* 169 */           matchedCookies.add(cookie);
/*     */         }  continue;
/*     */       } 
/* 172 */       if (this.log.isDebugEnabled()) {
/* 173 */         this.log.debug("Cookie " + cookie + " expired");
/*     */       }
/* 175 */       expired = true;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 181 */     if (expired) {
/* 182 */       cookieStore.clearExpired(now);
/*     */     }
/*     */     
/* 185 */     if (!matchedCookies.isEmpty()) {
/* 186 */       List<Header> headers = cookieSpec.formatCookies(matchedCookies);
/* 187 */       for (Header header : headers) {
/* 188 */         request.addHeader(header);
/*     */       }
/*     */     } 
/*     */     
/* 192 */     int ver = cookieSpec.getVersion();
/* 193 */     if (ver > 0) {
/* 194 */       Header header = cookieSpec.getVersionHeader();
/* 195 */       if (header != null)
/*     */       {
/* 197 */         request.addHeader(header);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 203 */     context.setAttribute("http.cookie-spec", cookieSpec);
/* 204 */     context.setAttribute("http.cookie-origin", cookieOrigin);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\client\protocol\RequestAddCookies.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */