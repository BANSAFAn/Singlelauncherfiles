/*    */ package org.tlauncher.tlauncher.exceptions.auth;
/*    */ 
/*    */ import org.tlauncher.tlauncher.entity.auth.Response;
/*    */ 
/*    */ public class AuthenticatorException
/*    */   extends Exception {
/*    */   private static final long serialVersionUID = -6773418626800336871L;
/*    */   private Response response;
/*    */   private String langpath;
/*    */   
/*    */   public AuthenticatorException(Throwable cause) {
/* 12 */     super(cause);
/*    */   }
/*    */   
/*    */   public AuthenticatorException(String message, String langpath) {
/* 16 */     super(message);
/*    */     
/* 18 */     this.langpath = langpath;
/*    */   }
/*    */   
/*    */   public AuthenticatorException(Response response, String langpath) {
/* 22 */     super(response.getErrorMessage());
/*    */     
/* 24 */     this.response = response;
/* 25 */     this.langpath = langpath;
/*    */   }
/*    */ 
/*    */   
/*    */   public AuthenticatorException(String message, String langpath, Throwable cause) {
/* 30 */     super(message, cause);
/*    */     
/* 32 */     this.langpath = langpath;
/*    */   }
/*    */   
/*    */   public String getLangpath() {
/* 36 */     return this.langpath;
/*    */   }
/*    */   
/*    */   public Response getResponse() {
/* 40 */     return this.response;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\exceptions\auth\AuthenticatorException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */