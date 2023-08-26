/*     */ package org.apache.http.impl.conn;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Proxy;
/*     */ import java.net.ProxySelector;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.List;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.conn.params.ConnRouteParams;
/*     */ import org.apache.http.conn.routing.HttpRoute;
/*     */ import org.apache.http.conn.routing.HttpRoutePlanner;
/*     */ import org.apache.http.conn.scheme.Scheme;
/*     */ import org.apache.http.conn.scheme.SchemeRegistry;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.Asserts;
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
/*     */ @Deprecated
/*     */ @NotThreadSafe
/*     */ public class ProxySelectorRoutePlanner
/*     */   implements HttpRoutePlanner
/*     */ {
/*     */   protected final SchemeRegistry schemeRegistry;
/*     */   protected ProxySelector proxySelector;
/*     */   
/*     */   public ProxySelectorRoutePlanner(SchemeRegistry schreg, ProxySelector prosel) {
/*  93 */     Args.notNull(schreg, "SchemeRegistry");
/*  94 */     this.schemeRegistry = schreg;
/*  95 */     this.proxySelector = prosel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProxySelector getProxySelector() {
/* 104 */     return this.proxySelector;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProxySelector(ProxySelector prosel) {
/* 114 */     this.proxySelector = prosel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpRoute determineRoute(HttpHost target, HttpRequest request, HttpContext context) throws HttpException {
/* 123 */     Args.notNull(request, "HTTP request");
/*     */ 
/*     */     
/* 126 */     HttpRoute route = ConnRouteParams.getForcedRoute(request.getParams());
/*     */     
/* 128 */     if (route != null) {
/* 129 */       return route;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 135 */     Asserts.notNull(target, "Target host");
/*     */     
/* 137 */     InetAddress local = ConnRouteParams.getLocalAddress(request.getParams());
/*     */     
/* 139 */     HttpHost proxy = determineProxy(target, request, context);
/*     */     
/* 141 */     Scheme schm = this.schemeRegistry.getScheme(target.getSchemeName());
/*     */ 
/*     */ 
/*     */     
/* 145 */     boolean secure = schm.isLayered();
/*     */     
/* 147 */     if (proxy == null) {
/* 148 */       route = new HttpRoute(target, local, secure);
/*     */     } else {
/* 150 */       route = new HttpRoute(target, local, proxy, secure);
/*     */     } 
/* 152 */     return route;
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
/*     */   
/*     */   protected HttpHost determineProxy(HttpHost target, HttpRequest request, HttpContext context) throws HttpException {
/* 173 */     ProxySelector psel = this.proxySelector;
/* 174 */     if (psel == null) {
/* 175 */       psel = ProxySelector.getDefault();
/*     */     }
/* 177 */     if (psel == null) {
/* 178 */       return null;
/*     */     }
/*     */     
/* 181 */     URI targetURI = null;
/*     */     try {
/* 183 */       targetURI = new URI(target.toURI());
/* 184 */     } catch (URISyntaxException usx) {
/* 185 */       throw new HttpException("Cannot convert host to URI: " + target, usx);
/*     */     } 
/*     */     
/* 188 */     List<Proxy> proxies = psel.select(targetURI);
/*     */     
/* 190 */     Proxy p = chooseProxy(proxies, target, request, context);
/*     */     
/* 192 */     HttpHost result = null;
/* 193 */     if (p.type() == Proxy.Type.HTTP) {
/*     */       
/* 195 */       if (!(p.address() instanceof InetSocketAddress)) {
/* 196 */         throw new HttpException("Unable to handle non-Inet proxy address: " + p.address());
/*     */       }
/*     */       
/* 199 */       InetSocketAddress isa = (InetSocketAddress)p.address();
/*     */       
/* 201 */       result = new HttpHost(getHost(isa), isa.getPort());
/*     */     } 
/*     */     
/* 204 */     return result;
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
/*     */   protected String getHost(InetSocketAddress isa) {
/* 223 */     return isa.isUnresolved() ? isa.getHostName() : isa.getAddress().getHostAddress();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Proxy chooseProxy(List<Proxy> proxies, HttpHost target, HttpRequest request, HttpContext context) {
/* 248 */     Args.notEmpty(proxies, "List of proxies");
/*     */     
/* 250 */     Proxy result = null;
/*     */ 
/*     */     
/* 253 */     for (int i = 0; result == null && i < proxies.size(); i++) {
/*     */       
/* 255 */       Proxy p = proxies.get(i);
/* 256 */       switch (p.type()) {
/*     */         
/*     */         case DIRECT:
/*     */         case HTTP:
/* 260 */           result = p;
/*     */           break;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     } 
/* 270 */     if (result == null)
/*     */     {
/*     */ 
/*     */       
/* 274 */       result = Proxy.NO_PROXY;
/*     */     }
/*     */     
/* 277 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\conn\ProxySelectorRoutePlanner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */