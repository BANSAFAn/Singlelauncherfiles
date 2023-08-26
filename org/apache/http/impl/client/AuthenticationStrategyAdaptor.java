/*     */ package org.apache.http.impl.client;
/*     */ 
/*     */ import java.util.LinkedList;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Queue;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.auth.AuthOption;
/*     */ import org.apache.http.auth.AuthScheme;
/*     */ import org.apache.http.auth.AuthScope;
/*     */ import org.apache.http.auth.AuthenticationException;
/*     */ import org.apache.http.auth.Credentials;
/*     */ import org.apache.http.auth.MalformedChallengeException;
/*     */ import org.apache.http.client.AuthCache;
/*     */ import org.apache.http.client.AuthenticationHandler;
/*     */ import org.apache.http.client.AuthenticationStrategy;
/*     */ import org.apache.http.client.CredentialsProvider;
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
/*     */ @Deprecated
/*     */ @Immutable
/*     */ class AuthenticationStrategyAdaptor
/*     */   implements AuthenticationStrategy
/*     */ {
/*  63 */   private final Log log = LogFactory.getLog(getClass());
/*     */   
/*     */   private final AuthenticationHandler handler;
/*     */ 
/*     */   
/*     */   public AuthenticationStrategyAdaptor(AuthenticationHandler handler) {
/*  69 */     this.handler = handler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAuthenticationRequested(HttpHost authhost, HttpResponse response, HttpContext context) {
/*  77 */     return this.handler.isAuthenticationRequested(response, context);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Header> getChallenges(HttpHost authhost, HttpResponse response, HttpContext context) throws MalformedChallengeException {
/*  85 */     return this.handler.getChallenges(response, context);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Queue<AuthOption> select(Map<String, Header> challenges, HttpHost authhost, HttpResponse response, HttpContext context) throws MalformedChallengeException {
/*     */     AuthScheme authScheme;
/*  94 */     Args.notNull(challenges, "Map of auth challenges");
/*  95 */     Args.notNull(authhost, "Host");
/*  96 */     Args.notNull(response, "HTTP response");
/*  97 */     Args.notNull(context, "HTTP context");
/*     */     
/*  99 */     Queue<AuthOption> options = new LinkedList<AuthOption>();
/* 100 */     CredentialsProvider credsProvider = (CredentialsProvider)context.getAttribute("http.auth.credentials-provider");
/*     */     
/* 102 */     if (credsProvider == null) {
/* 103 */       this.log.debug("Credentials provider not set in the context");
/* 104 */       return options;
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 109 */       authScheme = this.handler.selectScheme(challenges, response, context);
/* 110 */     } catch (AuthenticationException ex) {
/* 111 */       if (this.log.isWarnEnabled()) {
/* 112 */         this.log.warn(ex.getMessage(), (Throwable)ex);
/*     */       }
/* 114 */       return options;
/*     */     } 
/* 116 */     String id = authScheme.getSchemeName();
/* 117 */     Header challenge = challenges.get(id.toLowerCase(Locale.ROOT));
/* 118 */     authScheme.processChallenge(challenge);
/*     */     
/* 120 */     AuthScope authScope = new AuthScope(authhost.getHostName(), authhost.getPort(), authScheme.getRealm(), authScheme.getSchemeName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 126 */     Credentials credentials = credsProvider.getCredentials(authScope);
/* 127 */     if (credentials != null) {
/* 128 */       options.add(new AuthOption(authScheme, credentials));
/*     */     }
/* 130 */     return options;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void authSucceeded(HttpHost authhost, AuthScheme authScheme, HttpContext context) {
/* 136 */     AuthCache authCache = (AuthCache)context.getAttribute("http.auth.auth-cache");
/* 137 */     if (isCachable(authScheme)) {
/* 138 */       if (authCache == null) {
/* 139 */         authCache = new BasicAuthCache();
/* 140 */         context.setAttribute("http.auth.auth-cache", authCache);
/*     */       } 
/* 142 */       if (this.log.isDebugEnabled()) {
/* 143 */         this.log.debug("Caching '" + authScheme.getSchemeName() + "' auth scheme for " + authhost);
/*     */       }
/*     */       
/* 146 */       authCache.put(authhost, authScheme);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void authFailed(HttpHost authhost, AuthScheme authScheme, HttpContext context) {
/* 153 */     AuthCache authCache = (AuthCache)context.getAttribute("http.auth.auth-cache");
/* 154 */     if (authCache == null) {
/*     */       return;
/*     */     }
/* 157 */     if (this.log.isDebugEnabled()) {
/* 158 */       this.log.debug("Removing from cache '" + authScheme.getSchemeName() + "' auth scheme for " + authhost);
/*     */     }
/*     */     
/* 161 */     authCache.remove(authhost);
/*     */   }
/*     */   
/*     */   private boolean isCachable(AuthScheme authScheme) {
/* 165 */     if (authScheme == null || !authScheme.isComplete()) {
/* 166 */       return false;
/*     */     }
/* 168 */     String schemeName = authScheme.getSchemeName();
/* 169 */     return (schemeName.equalsIgnoreCase("Basic") || schemeName.equalsIgnoreCase("Digest"));
/*     */   }
/*     */ 
/*     */   
/*     */   public AuthenticationHandler getHandler() {
/* 174 */     return this.handler;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\client\AuthenticationStrategyAdaptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */