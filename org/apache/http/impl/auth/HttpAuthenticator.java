/*     */ package org.apache.http.impl.auth;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Queue;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.auth.AuthOption;
/*     */ import org.apache.http.auth.AuthProtocolState;
/*     */ import org.apache.http.auth.AuthScheme;
/*     */ import org.apache.http.auth.AuthState;
/*     */ import org.apache.http.auth.AuthenticationException;
/*     */ import org.apache.http.auth.ContextAwareAuthScheme;
/*     */ import org.apache.http.auth.Credentials;
/*     */ import org.apache.http.auth.MalformedChallengeException;
/*     */ import org.apache.http.client.AuthenticationStrategy;
/*     */ import org.apache.http.protocol.HttpContext;
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
/*     */ public class HttpAuthenticator
/*     */ {
/*     */   private final Log log;
/*     */   
/*     */   public HttpAuthenticator(Log log) {
/*  63 */     this.log = (log != null) ? log : LogFactory.getLog(getClass());
/*     */   }
/*     */   
/*     */   public HttpAuthenticator() {
/*  67 */     this(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAuthenticationRequested(HttpHost host, HttpResponse response, AuthenticationStrategy authStrategy, AuthState authState, HttpContext context) {
/*  76 */     if (authStrategy.isAuthenticationRequested(host, response, context)) {
/*  77 */       this.log.debug("Authentication required");
/*  78 */       if (authState.getState() == AuthProtocolState.SUCCESS) {
/*  79 */         authStrategy.authFailed(host, authState.getAuthScheme(), context);
/*     */       }
/*  81 */       return true;
/*     */     } 
/*  83 */     switch (authState.getState()) {
/*     */       case CHALLENGED:
/*     */       case HANDSHAKE:
/*  86 */         this.log.debug("Authentication succeeded");
/*  87 */         authState.setState(AuthProtocolState.SUCCESS);
/*  88 */         authStrategy.authSucceeded(host, authState.getAuthScheme(), context);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case SUCCESS:
/*  95 */         return false;
/*     */     } 
/*     */     authState.setState(AuthProtocolState.UNCHALLENGED);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean handleAuthChallenge(HttpHost host, HttpResponse response, AuthenticationStrategy authStrategy, AuthState authState, HttpContext context) {
/*     */     try {
/* 106 */       if (this.log.isDebugEnabled()) {
/* 107 */         this.log.debug(host.toHostString() + " requested authentication");
/*     */       }
/* 109 */       Map<String, Header> challenges = authStrategy.getChallenges(host, response, context);
/* 110 */       if (challenges.isEmpty()) {
/* 111 */         this.log.debug("Response contains no authentication challenges");
/* 112 */         return false;
/*     */       } 
/*     */       
/* 115 */       AuthScheme authScheme = authState.getAuthScheme();
/* 116 */       switch (authState.getState()) {
/*     */         case FAILURE:
/* 118 */           return false;
/*     */         case SUCCESS:
/* 120 */           authState.reset();
/*     */           break;
/*     */         case CHALLENGED:
/*     */         case HANDSHAKE:
/* 124 */           if (authScheme == null) {
/* 125 */             this.log.debug("Auth scheme is null");
/* 126 */             authStrategy.authFailed(host, null, context);
/* 127 */             authState.reset();
/* 128 */             authState.setState(AuthProtocolState.FAILURE);
/* 129 */             return false;
/*     */           } 
/*     */         case UNCHALLENGED:
/* 132 */           if (authScheme != null) {
/* 133 */             String id = authScheme.getSchemeName();
/* 134 */             Header challenge = challenges.get(id.toLowerCase(Locale.ROOT));
/* 135 */             if (challenge != null) {
/* 136 */               this.log.debug("Authorization challenge processed");
/* 137 */               authScheme.processChallenge(challenge);
/* 138 */               if (authScheme.isComplete()) {
/* 139 */                 this.log.debug("Authentication failed");
/* 140 */                 authStrategy.authFailed(host, authState.getAuthScheme(), context);
/* 141 */                 authState.reset();
/* 142 */                 authState.setState(AuthProtocolState.FAILURE);
/* 143 */                 return false;
/*     */               } 
/* 145 */               authState.setState(AuthProtocolState.HANDSHAKE);
/* 146 */               return true;
/*     */             } 
/*     */             
/* 149 */             authState.reset();
/*     */           } 
/*     */           break;
/*     */       } 
/*     */       
/* 154 */       Queue<AuthOption> authOptions = authStrategy.select(challenges, host, response, context);
/* 155 */       if (authOptions != null && !authOptions.isEmpty()) {
/* 156 */         if (this.log.isDebugEnabled()) {
/* 157 */           this.log.debug("Selected authentication options: " + authOptions);
/*     */         }
/* 159 */         authState.setState(AuthProtocolState.CHALLENGED);
/* 160 */         authState.update(authOptions);
/* 161 */         return true;
/*     */       } 
/* 163 */       return false;
/*     */     }
/* 165 */     catch (MalformedChallengeException ex) {
/* 166 */       if (this.log.isWarnEnabled()) {
/* 167 */         this.log.warn("Malformed challenge: " + ex.getMessage());
/*     */       }
/* 169 */       authState.reset();
/* 170 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void generateAuthResponse(HttpRequest request, AuthState authState, HttpContext context) throws HttpException, IOException {
/*     */     Queue<AuthOption> authOptions;
/* 178 */     AuthScheme authScheme = authState.getAuthScheme();
/* 179 */     Credentials creds = authState.getCredentials();
/* 180 */     switch (authState.getState()) {
/*     */       case FAILURE:
/*     */         return;
/*     */       case SUCCESS:
/* 184 */         ensureAuthScheme(authScheme);
/* 185 */         if (authScheme.isConnectionBased()) {
/*     */           return;
/*     */         }
/*     */         break;
/*     */       case CHALLENGED:
/* 190 */         authOptions = authState.getAuthOptions();
/* 191 */         if (authOptions != null) {
/* 192 */           while (!authOptions.isEmpty()) {
/* 193 */             AuthOption authOption = authOptions.remove();
/* 194 */             authScheme = authOption.getAuthScheme();
/* 195 */             creds = authOption.getCredentials();
/* 196 */             authState.update(authScheme, creds);
/* 197 */             if (this.log.isDebugEnabled()) {
/* 198 */               this.log.debug("Generating response to an authentication challenge using " + authScheme.getSchemeName() + " scheme");
/*     */             }
/*     */             
/*     */             try {
/* 202 */               Header header = doAuth(authScheme, creds, request, context);
/* 203 */               request.addHeader(header);
/*     */               break;
/* 205 */             } catch (AuthenticationException ex) {
/* 206 */               if (this.log.isWarnEnabled()) {
/* 207 */                 this.log.warn(authScheme + " authentication error: " + ex.getMessage());
/*     */               }
/*     */             } 
/*     */           } 
/*     */           return;
/*     */         } 
/* 213 */         ensureAuthScheme(authScheme);
/*     */         break;
/*     */     } 
/* 216 */     if (authScheme != null) {
/*     */       try {
/* 218 */         Header header = doAuth(authScheme, creds, request, context);
/* 219 */         request.addHeader(header);
/* 220 */       } catch (AuthenticationException ex) {
/* 221 */         if (this.log.isErrorEnabled()) {
/* 222 */           this.log.error(authScheme + " authentication error: " + ex.getMessage());
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private void ensureAuthScheme(AuthScheme authScheme) {
/* 229 */     Asserts.notNull(authScheme, "Auth scheme");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Header doAuth(AuthScheme authScheme, Credentials creds, HttpRequest request, HttpContext context) throws AuthenticationException {
/* 238 */     if (authScheme instanceof ContextAwareAuthScheme) {
/* 239 */       return ((ContextAwareAuthScheme)authScheme).authenticate(creds, request, context);
/*     */     }
/* 241 */     return authScheme.authenticate(creds, request);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\auth\HttpAuthenticator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */