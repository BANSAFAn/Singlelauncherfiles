/*     */ package org.apache.http.impl.conn;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.annotation.ThreadSafe;
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
/*     */ @Deprecated
/*     */ @ThreadSafe
/*     */ public class DefaultHttpRoutePlanner
/*     */   implements HttpRoutePlanner
/*     */ {
/*     */   protected final SchemeRegistry schemeRegistry;
/*     */   
/*     */   public DefaultHttpRoutePlanner(SchemeRegistry schreg) {
/*  77 */     Args.notNull(schreg, "Scheme registry");
/*  78 */     this.schemeRegistry = schreg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpRoute determineRoute(HttpHost target, HttpRequest request, HttpContext context) throws HttpException {
/*     */     Scheme schm;
/*  87 */     Args.notNull(request, "HTTP request");
/*     */ 
/*     */     
/*  90 */     HttpRoute route = ConnRouteParams.getForcedRoute(request.getParams());
/*     */     
/*  92 */     if (route != null) {
/*  93 */       return route;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  99 */     Asserts.notNull(target, "Target host");
/*     */     
/* 101 */     InetAddress local = ConnRouteParams.getLocalAddress(request.getParams());
/*     */     
/* 103 */     HttpHost proxy = ConnRouteParams.getDefaultProxy(request.getParams());
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 108 */       schm = this.schemeRegistry.getScheme(target.getSchemeName());
/* 109 */     } catch (IllegalStateException ex) {
/* 110 */       throw new HttpException(ex.getMessage());
/*     */     } 
/*     */ 
/*     */     
/* 114 */     boolean secure = schm.isLayered();
/*     */     
/* 116 */     if (proxy == null) {
/* 117 */       route = new HttpRoute(target, local, secure);
/*     */     } else {
/* 119 */       route = new HttpRoute(target, local, proxy, secure);
/*     */     } 
/* 121 */     return route;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\conn\DefaultHttpRoutePlanner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */