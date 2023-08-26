/*    */ package org.apache.http.auth;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.security.Principal;
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.util.Args;
/*    */ import org.apache.http.util.LangUtils;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ @Immutable
/*    */ public final class BasicUserPrincipal
/*    */   implements Principal, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -2266305184969850467L;
/*    */   private final String username;
/*    */   
/*    */   public BasicUserPrincipal(String username) {
/* 50 */     Args.notNull(username, "User name");
/* 51 */     this.username = username;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 56 */     return this.username;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 61 */     int hash = 17;
/* 62 */     hash = LangUtils.hashCode(hash, this.username);
/* 63 */     return hash;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 68 */     if (this == o) {
/* 69 */       return true;
/*    */     }
/* 71 */     if (o instanceof BasicUserPrincipal) {
/* 72 */       BasicUserPrincipal that = (BasicUserPrincipal)o;
/* 73 */       if (LangUtils.equals(this.username, that.username)) {
/* 74 */         return true;
/*    */       }
/*    */     } 
/* 77 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 82 */     StringBuilder buffer = new StringBuilder();
/* 83 */     buffer.append("[principal: ");
/* 84 */     buffer.append(this.username);
/* 85 */     buffer.append("]");
/* 86 */     return buffer.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\auth\BasicUserPrincipal.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */