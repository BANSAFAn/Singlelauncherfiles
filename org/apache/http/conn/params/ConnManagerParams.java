/*     */ package org.apache.http.conn.params;
/*     */ 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ @Immutable
/*     */ public final class ConnManagerParams
/*     */   implements ConnManagerPNames
/*     */ {
/*     */   public static final int DEFAULT_MAX_TOTAL_CONNECTIONS = 20;
/*     */   
/*     */   @Deprecated
/*     */   public static long getTimeout(HttpParams params) {
/*  63 */     Args.notNull(params, "HTTP parameters");
/*  64 */     return params.getLongParameter("http.conn-manager.timeout", 0L);
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
/*     */   @Deprecated
/*     */   public static void setTimeout(HttpParams params, long timeout) {
/*  79 */     Args.notNull(params, "HTTP parameters");
/*  80 */     params.setLongParameter("http.conn-manager.timeout", timeout);
/*     */   }
/*     */ 
/*     */   
/*  84 */   private static final ConnPerRoute DEFAULT_CONN_PER_ROUTE = new ConnPerRoute()
/*     */     {
/*     */       public int getMaxForRoute(HttpRoute route)
/*     */       {
/*  88 */         return 2;
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setMaxConnectionsPerRoute(HttpParams params, ConnPerRoute connPerRoute) {
/* 102 */     Args.notNull(params, "HTTP parameters");
/* 103 */     params.setParameter("http.conn-manager.max-per-route", connPerRoute);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ConnPerRoute getMaxConnectionsPerRoute(HttpParams params) {
/* 114 */     Args.notNull(params, "HTTP parameters");
/* 115 */     ConnPerRoute connPerRoute = (ConnPerRoute)params.getParameter("http.conn-manager.max-per-route");
/* 116 */     if (connPerRoute == null) {
/* 117 */       connPerRoute = DEFAULT_CONN_PER_ROUTE;
/*     */     }
/* 119 */     return connPerRoute;
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
/*     */   public static void setMaxTotalConnections(HttpParams params, int maxTotalConnections) {
/* 131 */     Args.notNull(params, "HTTP parameters");
/* 132 */     params.setIntParameter("http.conn-manager.max-total", maxTotalConnections);
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
/*     */   public static int getMaxTotalConnections(HttpParams params) {
/* 144 */     Args.notNull(params, "HTTP parameters");
/* 145 */     return params.getIntParameter("http.conn-manager.max-total", 20);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\conn\params\ConnManagerParams.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */