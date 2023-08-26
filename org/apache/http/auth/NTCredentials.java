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
/*     */ 
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
/*     */ public class NTCredentials
/*     */   implements Credentials, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -7385699315228907265L;
/*     */   private final NTUserPrincipal principal;
/*     */   private final String password;
/*     */   private final String workstation;
/*     */   
/*     */   @Deprecated
/*     */   public NTCredentials(String usernamePassword) {
/*     */     String username;
/*  67 */     Args.notNull(usernamePassword, "Username:password string");
/*     */     
/*  69 */     int atColon = usernamePassword.indexOf(':');
/*  70 */     if (atColon >= 0) {
/*  71 */       username = usernamePassword.substring(0, atColon);
/*  72 */       this.password = usernamePassword.substring(atColon + 1);
/*     */     } else {
/*  74 */       username = usernamePassword;
/*  75 */       this.password = null;
/*     */     } 
/*  77 */     int atSlash = username.indexOf('/');
/*  78 */     if (atSlash >= 0) {
/*  79 */       this.principal = new NTUserPrincipal(username.substring(0, atSlash).toUpperCase(Locale.ROOT), username.substring(atSlash + 1));
/*     */     }
/*     */     else {
/*     */       
/*  83 */       this.principal = new NTUserPrincipal(null, username.substring(atSlash + 1));
/*     */     } 
/*     */ 
/*     */     
/*  87 */     this.workstation = null;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NTCredentials(String userName, String password, String workstation, String domain) {
/* 105 */     Args.notNull(userName, "User name");
/* 106 */     this.principal = new NTUserPrincipal(domain, userName);
/* 107 */     this.password = password;
/* 108 */     if (workstation != null) {
/* 109 */       this.workstation = workstation.toUpperCase(Locale.ROOT);
/*     */     } else {
/* 111 */       this.workstation = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Principal getUserPrincipal() {
/* 117 */     return this.principal;
/*     */   }
/*     */   
/*     */   public String getUserName() {
/* 121 */     return this.principal.getUsername();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPassword() {
/* 126 */     return this.password;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDomain() {
/* 135 */     return this.principal.getDomain();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getWorkstation() {
/* 144 */     return this.workstation;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 149 */     int hash = 17;
/* 150 */     hash = LangUtils.hashCode(hash, this.principal);
/* 151 */     hash = LangUtils.hashCode(hash, this.workstation);
/* 152 */     return hash;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 157 */     if (this == o) {
/* 158 */       return true;
/*     */     }
/* 160 */     if (o instanceof NTCredentials) {
/* 161 */       NTCredentials that = (NTCredentials)o;
/* 162 */       if (LangUtils.equals(this.principal, that.principal) && LangUtils.equals(this.workstation, that.workstation))
/*     */       {
/* 164 */         return true;
/*     */       }
/*     */     } 
/* 167 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 172 */     StringBuilder buffer = new StringBuilder();
/* 173 */     buffer.append("[principal: ");
/* 174 */     buffer.append(this.principal);
/* 175 */     buffer.append("][workstation: ");
/* 176 */     buffer.append(this.workstation);
/* 177 */     buffer.append("]");
/* 178 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\auth\NTCredentials.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */