/*    */ package org.tlauncher.tlauncher.listeners.auth;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import org.tlauncher.tlauncher.managers.ProfileManager;
/*    */ import org.tlauncher.tlauncher.minecraft.auth.Account;
/*    */ import org.tlauncher.tlauncher.minecraft.auth.Authenticator;
/*    */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*    */ import org.tlauncher.tlauncher.ui.login.LoginException;
/*    */ import org.tlauncher.util.U;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ValidateAccountToken
/*    */   implements LoginProcessListener
/*    */ {
/* 19 */   private final RefreshTokenListener refreshTokenListener = new RefreshTokenListener(this);
/*    */ 
/*    */   
/*    */   public void validatePreGameLaunch() throws LoginException {
/* 23 */     ProfileManager profile = TLauncher.getInstance().getProfileManager();
/* 24 */     Account ac = profile.getSelectedAccount();
/* 25 */     Authenticator authenticator = Authenticator.instanceFor(ac);
/* 26 */     authenticator.addListener(new AuthenticatorSaveListener());
/* 27 */     authenticator.addListener(this.refreshTokenListener);
/*    */     try {
/* 29 */       synchronized (this.refreshTokenListener) {
/* 30 */         CompletableFuture.runAsync((Runnable)authenticator);
/* 31 */         this.refreshTokenListener.wait();
/*    */       } 
/* 33 */     } catch (InterruptedException e) {
/* 34 */       U.log(new Object[] { e });
/*    */     } 
/* 36 */     if (this.refreshTokenListener.getException() instanceof org.tlauncher.tlauncher.exceptions.auth.NotCorrectTokenOrIdException) {
/*    */       try {
/* 38 */         profile.remove(ac);
/* 39 */       } catch (IOException e) {
/* 40 */         U.log(new Object[] { e });
/*    */       } 
/* 42 */       throw new LoginException(this.refreshTokenListener.getException().getMessage());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\listeners\auth\ValidateAccountToken.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */