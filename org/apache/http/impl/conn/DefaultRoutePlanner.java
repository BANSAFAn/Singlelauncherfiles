/*     */ package org.apache.http.impl.conn;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.ProtocolException;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.client.config.RequestConfig;
/*     */ import org.apache.http.client.protocol.HttpClientContext;
/*     */ import org.apache.http.conn.SchemePortResolver;
/*     */ import org.apache.http.conn.UnsupportedSchemeException;
/*     */ import org.apache.http.conn.routing.HttpRoute;
/*     */ import org.apache.http.conn.routing.HttpRoutePlanner;
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
/*     */ public class DefaultRoutePlanner
/*     */   implements HttpRoutePlanner
/*     */ {
/*     */   private final SchemePortResolver schemePortResolver;
/*     */   
/*     */   public DefaultRoutePlanner(SchemePortResolver schemePortResolver) {
/*  59 */     this.schemePortResolver = (schemePortResolver != null) ? schemePortResolver : DefaultSchemePortResolver.INSTANCE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpRoute determineRoute(HttpHost host, HttpRequest request, HttpContext context) throws HttpException {
/*     */     HttpHost httpHost1;
/*  68 */     Args.notNull(request, "Request");
/*  69 */     if (host == null) {
/*  70 */       throw new ProtocolException("Target host is not specified");
/*     */     }
/*  72 */     HttpClientContext clientContext = HttpClientContext.adapt(context);
/*  73 */     RequestConfig config = clientContext.getRequestConfig();
/*  74 */     InetAddress local = config.getLocalAddress();
/*  75 */     HttpHost proxy = config.getProxy();
/*  76 */     if (proxy == null) {
/*  77 */       proxy = determineProxy(host, request, context);
/*     */     }
/*     */ 
/*     */     
/*  81 */     if (host.getPort() <= 0) {
/*     */       try {
/*  83 */         httpHost1 = new HttpHost(host.getHostName(), this.schemePortResolver.resolve(host), host.getSchemeName());
/*     */ 
/*     */       
/*     */       }
/*  87 */       catch (UnsupportedSchemeException ex) {
/*  88 */         throw new HttpException(ex.getMessage());
/*     */       } 
/*     */     } else {
/*  91 */       httpHost1 = host;
/*     */     } 
/*  93 */     boolean secure = httpHost1.getSchemeName().equalsIgnoreCase("https");
/*  94 */     if (proxy == null) {
/*  95 */       return new HttpRoute(httpHost1, local, secure);
/*     */     }
/*  97 */     return new HttpRoute(httpHost1, local, proxy, secure);
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
/*     */   protected HttpHost determineProxy(HttpHost target, HttpRequest request, HttpContext context) throws HttpException {
/* 110 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\conn\DefaultRoutePlanner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */