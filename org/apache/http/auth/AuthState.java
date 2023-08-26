/*     */ package org.apache.http.auth;
/*     */ 
/*     */ import java.util.Queue;
/*     */ import org.apache.http.annotation.NotThreadSafe;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ @NotThreadSafe
/*     */ public class AuthState
/*     */ {
/*  59 */   private AuthProtocolState state = AuthProtocolState.UNCHALLENGED;
/*     */   
/*     */   private AuthScheme authScheme;
/*     */   
/*     */   private AuthScope authScope;
/*     */   private Credentials credentials;
/*     */   private Queue<AuthOption> authOptions;
/*     */   
/*     */   public void reset() {
/*  68 */     this.state = AuthProtocolState.UNCHALLENGED;
/*  69 */     this.authOptions = null;
/*  70 */     this.authScheme = null;
/*  71 */     this.authScope = null;
/*  72 */     this.credentials = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AuthProtocolState getState() {
/*  79 */     return this.state;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setState(AuthProtocolState state) {
/*  86 */     this.state = (state != null) ? state : AuthProtocolState.UNCHALLENGED;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AuthScheme getAuthScheme() {
/*  93 */     return this.authScheme;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Credentials getCredentials() {
/* 100 */     return this.credentials;
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
/*     */   public void update(AuthScheme authScheme, Credentials credentials) {
/* 112 */     Args.notNull(authScheme, "Auth scheme");
/* 113 */     Args.notNull(credentials, "Credentials");
/* 114 */     this.authScheme = authScheme;
/* 115 */     this.credentials = credentials;
/* 116 */     this.authOptions = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Queue<AuthOption> getAuthOptions() {
/* 125 */     return this.authOptions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasAuthOptions() {
/* 135 */     return (this.authOptions != null && !this.authOptions.isEmpty());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void update(Queue<AuthOption> authOptions) {
/* 146 */     Args.notEmpty(authOptions, "Queue of auth options");
/* 147 */     this.authOptions = authOptions;
/* 148 */     this.authScheme = null;
/* 149 */     this.credentials = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void invalidate() {
/* 159 */     reset();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public boolean isValid() {
/* 167 */     return (this.authScheme != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setAuthScheme(AuthScheme authScheme) {
/* 179 */     if (authScheme == null) {
/* 180 */       reset();
/*     */       return;
/*     */     } 
/* 183 */     this.authScheme = authScheme;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setCredentials(Credentials credentials) {
/* 195 */     this.credentials = credentials;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public AuthScope getAuthScope() {
/* 207 */     return this.authScope;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setAuthScope(AuthScope authScope) {
/* 219 */     this.authScope = authScope;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 224 */     StringBuilder buffer = new StringBuilder();
/* 225 */     buffer.append("state:").append(this.state).append(";");
/* 226 */     if (this.authScheme != null) {
/* 227 */       buffer.append("auth scheme:").append(this.authScheme.getSchemeName()).append(";");
/*     */     }
/* 229 */     if (this.credentials != null) {
/* 230 */       buffer.append("credentials present");
/*     */     }
/* 232 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\auth\AuthState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */