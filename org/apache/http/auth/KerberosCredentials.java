/*    */ package org.apache.http.auth;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.security.Principal;
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.ietf.jgss.GSSCredential;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Immutable
/*    */ public class KerberosCredentials
/*    */   implements Credentials, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 487421613855550713L;
/*    */   private final GSSCredential gssCredential;
/*    */   
/*    */   public KerberosCredentials(GSSCredential gssCredential) {
/* 54 */     this.gssCredential = gssCredential;
/*    */   }
/*    */   
/*    */   public GSSCredential getGSSCredential() {
/* 58 */     return this.gssCredential;
/*    */   }
/*    */ 
/*    */   
/*    */   public Principal getUserPrincipal() {
/* 63 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getPassword() {
/* 68 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\auth\KerberosCredentials.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */