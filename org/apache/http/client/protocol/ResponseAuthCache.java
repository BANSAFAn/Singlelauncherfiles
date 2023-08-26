/*     */ package org.apache.http.client.protocol;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.HttpResponseInterceptor;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.auth.AuthProtocolState;
/*     */ import org.apache.http.auth.AuthScheme;
/*     */ import org.apache.http.auth.AuthState;
/*     */ import org.apache.http.client.AuthCache;
/*     */ import org.apache.http.conn.scheme.Scheme;
/*     */ import org.apache.http.conn.scheme.SchemeRegistry;
/*     */ import org.apache.http.impl.client.BasicAuthCache;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ @Immutable
/*     */ public class ResponseAuthCache
/*     */   implements HttpResponseInterceptor
/*     */ {
/*  64 */   private final Log log = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
/*     */     BasicAuthCache basicAuthCache;
/*  73 */     Args.notNull(response, "HTTP request");
/*  74 */     Args.notNull(context, "HTTP context");
/*  75 */     AuthCache authCache = (AuthCache)context.getAttribute("http.auth.auth-cache");
/*     */     
/*  77 */     HttpHost target = (HttpHost)context.getAttribute("http.target_host");
/*  78 */     AuthState targetState = (AuthState)context.getAttribute("http.auth.target-scope");
/*  79 */     if (target != null && targetState != null) {
/*  80 */       if (this.log.isDebugEnabled()) {
/*  81 */         this.log.debug("Target auth state: " + targetState.getState());
/*     */       }
/*  83 */       if (isCachable(targetState)) {
/*  84 */         SchemeRegistry schemeRegistry = (SchemeRegistry)context.getAttribute("http.scheme-registry");
/*     */         
/*  86 */         if (target.getPort() < 0) {
/*  87 */           Scheme scheme = schemeRegistry.getScheme(target);
/*  88 */           target = new HttpHost(target.getHostName(), scheme.resolvePort(target.getPort()), target.getSchemeName());
/*     */         } 
/*     */         
/*  91 */         if (authCache == null) {
/*  92 */           basicAuthCache = new BasicAuthCache();
/*  93 */           context.setAttribute("http.auth.auth-cache", basicAuthCache);
/*     */         } 
/*  95 */         switch (targetState.getState()) {
/*     */           case CHALLENGED:
/*  97 */             cache((AuthCache)basicAuthCache, target, targetState.getAuthScheme());
/*     */             break;
/*     */           case FAILURE:
/* 100 */             uncache((AuthCache)basicAuthCache, target, targetState.getAuthScheme());
/*     */             break;
/*     */         } 
/*     */       } 
/*     */     } 
/* 105 */     HttpHost proxy = (HttpHost)context.getAttribute("http.proxy_host");
/* 106 */     AuthState proxyState = (AuthState)context.getAttribute("http.auth.proxy-scope");
/* 107 */     if (proxy != null && proxyState != null) {
/* 108 */       if (this.log.isDebugEnabled()) {
/* 109 */         this.log.debug("Proxy auth state: " + proxyState.getState());
/*     */       }
/* 111 */       if (isCachable(proxyState)) {
/* 112 */         if (basicAuthCache == null) {
/* 113 */           basicAuthCache = new BasicAuthCache();
/* 114 */           context.setAttribute("http.auth.auth-cache", basicAuthCache);
/*     */         } 
/* 116 */         switch (proxyState.getState()) {
/*     */           case CHALLENGED:
/* 118 */             cache((AuthCache)basicAuthCache, proxy, proxyState.getAuthScheme());
/*     */             break;
/*     */           case FAILURE:
/* 121 */             uncache((AuthCache)basicAuthCache, proxy, proxyState.getAuthScheme());
/*     */             break;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private boolean isCachable(AuthState authState) {
/* 128 */     AuthScheme authScheme = authState.getAuthScheme();
/* 129 */     if (authScheme == null || !authScheme.isComplete()) {
/* 130 */       return false;
/*     */     }
/* 132 */     String schemeName = authScheme.getSchemeName();
/* 133 */     return (schemeName.equalsIgnoreCase("Basic") || schemeName.equalsIgnoreCase("Digest"));
/*     */   }
/*     */ 
/*     */   
/*     */   private void cache(AuthCache authCache, HttpHost host, AuthScheme authScheme) {
/* 138 */     if (this.log.isDebugEnabled()) {
/* 139 */       this.log.debug("Caching '" + authScheme.getSchemeName() + "' auth scheme for " + host);
/*     */     }
/*     */     
/* 142 */     authCache.put(host, authScheme);
/*     */   }
/*     */   
/*     */   private void uncache(AuthCache authCache, HttpHost host, AuthScheme authScheme) {
/* 146 */     if (this.log.isDebugEnabled()) {
/* 147 */       this.log.debug("Removing from cache '" + authScheme.getSchemeName() + "' auth scheme for " + host);
/*     */     }
/*     */     
/* 150 */     authCache.remove(host);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\client\protocol\ResponseAuthCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */