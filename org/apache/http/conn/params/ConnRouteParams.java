/*     */ package org.apache.http.conn.params;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.conn.routing.HttpRoute;
/*     */ import org.apache.http.params.HttpParams;
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
/*     */ @Deprecated
/*     */ @Immutable
/*     */ public class ConnRouteParams
/*     */   implements ConnRoutePNames
/*     */ {
/*  54 */   public static final HttpHost NO_HOST = new HttpHost("127.0.0.255", 0, "no-host");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  61 */   public static final HttpRoute NO_ROUTE = new HttpRoute(NO_HOST);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HttpHost getDefaultProxy(HttpParams params) {
/*  80 */     Args.notNull(params, "Parameters");
/*  81 */     HttpHost proxy = (HttpHost)params.getParameter("http.route.default-proxy");
/*     */     
/*  83 */     if (proxy != null && NO_HOST.equals(proxy))
/*     */     {
/*  85 */       proxy = null;
/*     */     }
/*  87 */     return proxy;
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
/*     */   public static void setDefaultProxy(HttpParams params, HttpHost proxy) {
/* 102 */     Args.notNull(params, "Parameters");
/* 103 */     params.setParameter("http.route.default-proxy", proxy);
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
/*     */   public static HttpRoute getForcedRoute(HttpParams params) {
/* 118 */     Args.notNull(params, "Parameters");
/* 119 */     HttpRoute route = (HttpRoute)params.getParameter("http.route.forced-route");
/*     */     
/* 121 */     if (route != null && NO_ROUTE.equals(route))
/*     */     {
/* 123 */       route = null;
/*     */     }
/* 125 */     return route;
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
/*     */   public static void setForcedRoute(HttpParams params, HttpRoute route) {
/* 140 */     Args.notNull(params, "Parameters");
/* 141 */     params.setParameter("http.route.forced-route", route);
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
/*     */   public static InetAddress getLocalAddress(HttpParams params) {
/* 157 */     Args.notNull(params, "Parameters");
/* 158 */     InetAddress local = (InetAddress)params.getParameter("http.route.local-address");
/*     */ 
/*     */     
/* 161 */     return local;
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
/*     */   public static void setLocalAddress(HttpParams params, InetAddress local) {
/* 173 */     Args.notNull(params, "Parameters");
/* 174 */     params.setParameter("http.route.local-address", local);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\conn\params\ConnRouteParams.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */