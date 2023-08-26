/*     */ package org.apache.http.client.protocol;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpRequestInterceptor;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.auth.AuthProtocolState;
/*     */ import org.apache.http.auth.AuthScheme;
/*     */ import org.apache.http.auth.AuthScope;
/*     */ import org.apache.http.auth.AuthState;
/*     */ import org.apache.http.auth.Credentials;
/*     */ import org.apache.http.client.AuthCache;
/*     */ import org.apache.http.client.CredentialsProvider;
/*     */ import org.apache.http.conn.routing.RouteInfo;
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
/*     */ public class RequestAuthCache
/*     */   implements HttpRequestInterceptor
/*     */ {
/*  60 */   private final Log log = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
/*  69 */     Args.notNull(request, "HTTP request");
/*  70 */     Args.notNull(context, "HTTP context");
/*     */     
/*  72 */     HttpClientContext clientContext = HttpClientContext.adapt(context);
/*     */     
/*  74 */     AuthCache authCache = clientContext.getAuthCache();
/*  75 */     if (authCache == null) {
/*  76 */       this.log.debug("Auth cache not set in the context");
/*     */       
/*     */       return;
/*     */     } 
/*  80 */     CredentialsProvider credsProvider = clientContext.getCredentialsProvider();
/*  81 */     if (credsProvider == null) {
/*  82 */       this.log.debug("Credentials provider not set in the context");
/*     */       
/*     */       return;
/*     */     } 
/*  86 */     RouteInfo route = clientContext.getHttpRoute();
/*  87 */     if (route == null) {
/*  88 */       this.log.debug("Route info not set in the context");
/*     */       
/*     */       return;
/*     */     } 
/*  92 */     HttpHost target = clientContext.getTargetHost();
/*  93 */     if (target == null) {
/*  94 */       this.log.debug("Target host not set in the context");
/*     */       
/*     */       return;
/*     */     } 
/*  98 */     if (target.getPort() < 0) {
/*  99 */       target = new HttpHost(target.getHostName(), route.getTargetHost().getPort(), target.getSchemeName());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 105 */     AuthState targetState = clientContext.getTargetAuthState();
/* 106 */     if (targetState != null && targetState.getState() == AuthProtocolState.UNCHALLENGED) {
/* 107 */       AuthScheme authScheme = authCache.get(target);
/* 108 */       if (authScheme != null) {
/* 109 */         doPreemptiveAuth(target, authScheme, targetState, credsProvider);
/*     */       }
/*     */     } 
/*     */     
/* 113 */     HttpHost proxy = route.getProxyHost();
/* 114 */     AuthState proxyState = clientContext.getProxyAuthState();
/* 115 */     if (proxy != null && proxyState != null && proxyState.getState() == AuthProtocolState.UNCHALLENGED) {
/* 116 */       AuthScheme authScheme = authCache.get(proxy);
/* 117 */       if (authScheme != null) {
/* 118 */         doPreemptiveAuth(proxy, authScheme, proxyState, credsProvider);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void doPreemptiveAuth(HttpHost host, AuthScheme authScheme, AuthState authState, CredentialsProvider credsProvider) {
/* 128 */     String schemeName = authScheme.getSchemeName();
/* 129 */     if (this.log.isDebugEnabled()) {
/* 130 */       this.log.debug("Re-using cached '" + schemeName + "' auth scheme for " + host);
/*     */     }
/*     */     
/* 133 */     AuthScope authScope = new AuthScope(host, AuthScope.ANY_REALM, schemeName);
/* 134 */     Credentials creds = credsProvider.getCredentials(authScope);
/*     */     
/* 136 */     if (creds != null) {
/* 137 */       if ("BASIC".equalsIgnoreCase(authScheme.getSchemeName())) {
/* 138 */         authState.setState(AuthProtocolState.CHALLENGED);
/*     */       } else {
/* 140 */         authState.setState(AuthProtocolState.SUCCESS);
/*     */       } 
/* 142 */       authState.update(authScheme, creds);
/*     */     } else {
/* 144 */       this.log.debug("No credentials for preemptive authentication");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\client\protocol\RequestAuthCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */