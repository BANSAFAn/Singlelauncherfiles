/*     */ package org.apache.http.auth;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.security.Principal;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.LangUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class UsernamePasswordCredentials
/*     */   implements Credentials, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 243343858802739403L;
/*     */   private final BasicUserPrincipal principal;
/*     */   private final String password;
/*     */   
/*     */   @Deprecated
/*     */   public UsernamePasswordCredentials(String usernamePassword) {
/*  60 */     Args.notNull(usernamePassword, "Username:password string");
/*  61 */     int atColon = usernamePassword.indexOf(':');
/*  62 */     if (atColon >= 0) {
/*  63 */       this.principal = new BasicUserPrincipal(usernamePassword.substring(0, atColon));
/*  64 */       this.password = usernamePassword.substring(atColon + 1);
/*     */     } else {
/*  66 */       this.principal = new BasicUserPrincipal(usernamePassword);
/*  67 */       this.password = null;
/*     */     } 
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
/*     */   public UsernamePasswordCredentials(String userName, String password) {
/*  80 */     Args.notNull(userName, "Username");
/*  81 */     this.principal = new BasicUserPrincipal(userName);
/*  82 */     this.password = password;
/*     */   }
/*     */ 
/*     */   
/*     */   public Principal getUserPrincipal() {
/*  87 */     return this.principal;
/*     */   }
/*     */   
/*     */   public String getUserName() {
/*  91 */     return this.principal.getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPassword() {
/*  96 */     return this.password;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 101 */     return this.principal.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 106 */     if (this == o) {
/* 107 */       return true;
/*     */     }
/* 109 */     if (o instanceof UsernamePasswordCredentials) {
/* 110 */       UsernamePasswordCredentials that = (UsernamePasswordCredentials)o;
/* 111 */       if (LangUtils.equals(this.principal, that.principal)) {
/* 112 */         return true;
/*     */       }
/*     */     } 
/* 115 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 120 */     return this.principal.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\auth\UsernamePasswordCredentials.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */