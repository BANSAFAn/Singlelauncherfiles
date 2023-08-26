/*    */ package org.tlauncher.tlauncher.minecraft.auth;
/*    */ 
/*    */ import org.tlauncher.tlauncher.exceptions.auth.AuthenticatorException;
/*    */ 
/*    */ class MojangAuthenticator
/*    */   extends StandardAuthenticator {
/*    */   public MojangAuthenticator(Account account, String url) {
/*  8 */     super(account, url + "authenticate", url + "refresh");
/*    */   }
/*    */ 
/*    */   
/*    */   protected void pass() throws AuthenticatorException {
/* 13 */     super.pass();
/* 14 */     if ((getAccount().getProfiles()).length == 0)
/* 15 */       throw new AuthenticatorException("not proper type", "not.mojang.account"); 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraft\auth\MojangAuthenticator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */