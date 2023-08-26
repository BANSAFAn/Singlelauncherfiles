/*     */ package org.apache.http.auth;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.security.Principal;
/*     */ import java.util.Locale;
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
/*     */ @Immutable
/*     */ public class NTUserPrincipal
/*     */   implements Principal, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -6870169797924406894L;
/*     */   private final String username;
/*     */   private final String domain;
/*     */   private final String ntname;
/*     */   
/*     */   public NTUserPrincipal(String domain, String username) {
/*  55 */     Args.notNull(username, "User name");
/*  56 */     this.username = username;
/*  57 */     if (domain != null) {
/*  58 */       this.domain = domain.toUpperCase(Locale.ROOT);
/*     */     } else {
/*  60 */       this.domain = null;
/*     */     } 
/*  62 */     if (this.domain != null && !this.domain.isEmpty()) {
/*  63 */       StringBuilder buffer = new StringBuilder();
/*  64 */       buffer.append(this.domain);
/*  65 */       buffer.append('\\');
/*  66 */       buffer.append(this.username);
/*  67 */       this.ntname = buffer.toString();
/*     */     } else {
/*  69 */       this.ntname = this.username;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/*  75 */     return this.ntname;
/*     */   }
/*     */   
/*     */   public String getDomain() {
/*  79 */     return this.domain;
/*     */   }
/*     */   
/*     */   public String getUsername() {
/*  83 */     return this.username;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  88 */     int hash = 17;
/*  89 */     hash = LangUtils.hashCode(hash, this.username);
/*  90 */     hash = LangUtils.hashCode(hash, this.domain);
/*  91 */     return hash;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  96 */     if (this == o) {
/*  97 */       return true;
/*     */     }
/*  99 */     if (o instanceof NTUserPrincipal) {
/* 100 */       NTUserPrincipal that = (NTUserPrincipal)o;
/* 101 */       if (LangUtils.equals(this.username, that.username) && LangUtils.equals(this.domain, that.domain))
/*     */       {
/* 103 */         return true;
/*     */       }
/*     */     } 
/* 106 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 111 */     return this.ntname;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\auth\NTUserPrincipal.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */