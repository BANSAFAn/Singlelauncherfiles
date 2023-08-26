/*    */ package org.tlauncher.tlauncher.minecraft.auth;
/*    */ 
/*    */ import org.tlauncher.tlauncher.entity.auth.Response;
/*    */ import org.tlauncher.tlauncher.exceptions.auth.AuthenticatorException;
/*    */ import org.tlauncher.tlauncher.exceptions.auth.ServiceUnavailableException;
/*    */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*    */ 
/*    */ public class TlauncherAuthenticator
/*    */   extends StandardAuthenticator {
/*    */   public TlauncherAuthenticator(Account account) {
/* 11 */     super(account, TLauncher.getInnerSettings().get("skin.authentication.authenticate"), 
/* 12 */         TLauncher.getInnerSettings().get("skin.authentication.refresh"));
/*    */   }
/*    */ 
/*    */   
/*    */   protected AuthenticatorException getException(Response result) {
/* 17 */     AuthenticatorException exception = super.getException(result);
/*    */     
/* 19 */     if (exception.getClass().equals(AuthenticatorException.class) && 
/* 20 */       "ServiceUnavailableException".equals(result.getError())) {
/* 21 */       return (AuthenticatorException)new ServiceUnavailableException(result.getErrorMessage());
/*    */     }
/*    */     
/* 24 */     return exception;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraft\auth\TlauncherAuthenticator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */