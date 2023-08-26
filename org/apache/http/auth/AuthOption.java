/*    */ package org.apache.http.auth;
/*    */ 
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.util.Args;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Immutable
/*    */ public final class AuthOption
/*    */ {
/*    */   private final AuthScheme authScheme;
/*    */   private final Credentials creds;
/*    */   
/*    */   public AuthOption(AuthScheme authScheme, Credentials creds) {
/* 43 */     Args.notNull(authScheme, "Auth scheme");
/* 44 */     Args.notNull(creds, "User credentials");
/* 45 */     this.authScheme = authScheme;
/* 46 */     this.creds = creds;
/*    */   }
/*    */   
/*    */   public AuthScheme getAuthScheme() {
/* 50 */     return this.authScheme;
/*    */   }
/*    */   
/*    */   public Credentials getCredentials() {
/* 54 */     return this.creds;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 59 */     return this.authScheme.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\auth\AuthOption.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */