/*    */ package org.tlauncher.tlauncher.minecraft.auth;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.UUID;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import org.tlauncher.tlauncher.exceptions.auth.AuthenticatorException;
/*    */ import org.tlauncher.tlauncher.listeners.auth.AuthenticatorSaveListener;
/*    */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*    */ import org.tlauncher.tlauncher.ui.listener.auth.AuthenticatorListener;
/*    */ import org.tlauncher.util.U;
/*    */ 
/*    */ 
/*    */ public abstract class Authenticator
/*    */   implements Runnable
/*    */ {
/*    */   protected final Account account;
/* 19 */   private final String logPrefix = '[' + getClass().getSimpleName() + ']';
/* 20 */   private final List<AuthenticatorListener> listeners = new ArrayList<>();
/*    */   
/*    */   public final Account getAccount() {
/* 23 */     return this.account;
/*    */   }
/*    */   
/*    */   Authenticator(Account account) {
/* 27 */     if (account == null) {
/* 28 */       throw new NullPointerException("account");
/*    */     }
/* 30 */     this.account = account;
/*    */   }
/*    */   
/*    */   public static Authenticator instanceFor(Account account) {
/* 34 */     if (account == null)
/* 35 */       throw new NullPointerException("account"); 
/* 36 */     switch (account.getType()) {
/*    */       case TLAUNCHER:
/* 38 */         return new TlauncherAuthenticator(account);
/*    */       case MOJANG:
/* 40 */         return new MojangAuthenticator(account, TLauncher.getInnerSettings().get("authserver.mojang"));
/*    */       case MICROSOFT:
/* 42 */         return new MicrosoftAuthenticator(account);
/*    */       case FREE:
/* 44 */         return new FreeAuthentication(account);
/*    */     } 
/* 46 */     throw new IllegalArgumentException("illegal account type");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void run() {
/* 52 */     this.listeners.forEach(e -> e.onAuthPassing(this));
/*    */     try {
/* 54 */       pass();
/* 55 */       this.listeners.forEach(e -> e.onAuthPassed(this));
/* 56 */     } catch (Exception error) {
/* 57 */       log(new Object[] { "Cannot authenticate:", error });
/* 58 */       this.listeners.forEach(e -> e.onAuthPassingError(this, error));
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static UUID getClientToken() {
/* 65 */     return TLauncher.getInstance().getProfileManager().getClientToken();
/*    */   }
/*    */   
/*    */   protected void log(Object... o) {
/* 69 */     U.log(new Object[] { this.logPrefix, o });
/*    */   }
/*    */   
/*    */   public void addListener(AuthenticatorListener listener) {
/* 73 */     this.listeners.add(listener);
/*    */   }
/*    */   
/*    */   public void removeListener(AuthenticatorListener listener) {
/* 77 */     this.listeners.remove(listener);
/*    */   }
/*    */   
/*    */   public static void authenticate(Account acc, AuthenticatorListener authenticatorListener) {
/* 81 */     Authenticator authenticator = instanceFor(acc);
/* 82 */     authenticator.addListener((AuthenticatorListener)new AuthenticatorSaveListener());
/* 83 */     if (Objects.nonNull(authenticatorListener))
/* 84 */       authenticator.addListener(authenticatorListener); 
/* 85 */     CompletableFuture.runAsync(authenticator);
/*    */   }
/*    */   
/*    */   protected abstract void pass() throws AuthenticatorException;
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraft\auth\Authenticator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */