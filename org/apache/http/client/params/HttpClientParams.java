/*     */ package org.apache.http.client.params;
/*     */ 
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.params.HttpConnectionParams;
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
/*     */ @Deprecated
/*     */ @Immutable
/*     */ public class HttpClientParams
/*     */ {
/*     */   public static boolean isRedirecting(HttpParams params) {
/*  50 */     Args.notNull(params, "HTTP parameters");
/*  51 */     return params.getBooleanParameter("http.protocol.handle-redirects", true);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void setRedirecting(HttpParams params, boolean value) {
/*  56 */     Args.notNull(params, "HTTP parameters");
/*  57 */     params.setBooleanParameter("http.protocol.handle-redirects", value);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isAuthenticating(HttpParams params) {
/*  62 */     Args.notNull(params, "HTTP parameters");
/*  63 */     return params.getBooleanParameter("http.protocol.handle-authentication", true);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void setAuthenticating(HttpParams params, boolean value) {
/*  68 */     Args.notNull(params, "HTTP parameters");
/*  69 */     params.setBooleanParameter("http.protocol.handle-authentication", value);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getCookiePolicy(HttpParams params) {
/*  74 */     Args.notNull(params, "HTTP parameters");
/*  75 */     String cookiePolicy = (String)params.getParameter("http.protocol.cookie-policy");
/*     */     
/*  77 */     if (cookiePolicy == null) {
/*  78 */       return "best-match";
/*     */     }
/*  80 */     return cookiePolicy;
/*     */   }
/*     */   
/*     */   public static void setCookiePolicy(HttpParams params, String cookiePolicy) {
/*  84 */     Args.notNull(params, "HTTP parameters");
/*  85 */     params.setParameter("http.protocol.cookie-policy", cookiePolicy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setConnectionManagerTimeout(HttpParams params, long timeout) {
/*  94 */     Args.notNull(params, "HTTP parameters");
/*  95 */     params.setLongParameter("http.conn-manager.timeout", timeout);
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
/*     */   public static long getConnectionManagerTimeout(HttpParams params) {
/* 108 */     Args.notNull(params, "HTTP parameters");
/* 109 */     Long timeout = (Long)params.getParameter("http.conn-manager.timeout");
/* 110 */     if (timeout != null) {
/* 111 */       return timeout.longValue();
/*     */     }
/* 113 */     return HttpConnectionParams.getConnectionTimeout(params);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\client\params\HttpClientParams.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */