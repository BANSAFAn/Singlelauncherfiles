/*     */ package org.tlauncher.tlauncher.listeners.auth;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Objects;
/*     */ import javax.swing.SwingUtilities;
/*     */ import org.tlauncher.tlauncher.configuration.Configuration;
/*     */ import org.tlauncher.tlauncher.exceptions.auth.AuthenticatorException;
/*     */ import org.tlauncher.tlauncher.exceptions.auth.BlockedUserException;
/*     */ import org.tlauncher.tlauncher.managers.ProfileManager;
/*     */ import org.tlauncher.tlauncher.minecraft.auth.Account;
/*     */ import org.tlauncher.tlauncher.minecraft.auth.Authenticator;
/*     */ import org.tlauncher.tlauncher.minecraft.user.InvalidStatusCodeException;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.MainPane;
/*     */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*     */ import org.tlauncher.tlauncher.ui.listener.auth.AuthenticatorListener;
/*     */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*     */ import org.tlauncher.util.MinecraftUtil;
/*     */ import org.tlauncher.util.U;
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
/*     */ public class AuthenticatorSaveListener
/*     */   implements AuthenticatorListener
/*     */ {
/*     */   private static final String fieldConfig = "mojang.account.protection.hide";
/*     */   
/*     */   public void onAuthPassing(Authenticator auth) {}
/*     */   
/*     */   public void onAuthPassingError(Authenticator auth, Exception e) {
/*  38 */     showError(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onAuthPassed(Authenticator auth) {
/*  43 */     ProfileManager pm = TLauncher.getInstance().getProfileManager();
/*     */     
/*     */     try {
/*  46 */       U.log(new Object[] { "onAuthPassed" });
/*  47 */       pm.save(auth.getAccount());
/*  48 */     } catch (IOException exception) {
/*  49 */       showError(exception);
/*     */     } 
/*  51 */     Configuration c = TLauncher.getInstance().getConfiguration();
/*  52 */     if (Account.AccountType.OFFICIAL_ACCOUNTS.contains(auth.getAccount().getType()) && !c.getBoolean("mojang.account.protection.hide") && 
/*  53 */       Alert.showWarningMessageWithCheckBox(Localizable.get("account.protection.message.title"), 
/*  54 */         Localizable.get("account.protection.message"), 350, 
/*  55 */         Localizable.get("account.message.show.again"))) {
/*  56 */       c.set("mojang.account.protection.hide", Boolean.valueOf(true));
/*     */     }
/*     */     
/*  59 */     if (auth.getAccount().getType().equals(Account.AccountType.FREE)) {
/*     */       
/*  61 */       String u = auth.getAccount().getUsername();
/*  62 */       Configuration con = TLauncher.getInstance().getConfiguration();
/*  63 */       if (Objects.nonNull(u) && !con.getBoolean("not.proper.username.warning") && 
/*  64 */         !MinecraftUtil.isUsernameValid(u) && 
/*  65 */         Alert.showWarningMessageWithCheckBox("", "auth.error.username.not.valid", 400)) {
/*  66 */         con.set("not.proper.username.warning", Boolean.valueOf(true));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void showError(Throwable e) {
/*  73 */     String description = "unknown";
/*  74 */     if (e instanceof AuthenticatorException) {
/*  75 */       AuthenticatorException ae = (AuthenticatorException)e;
/*     */       
/*  77 */       if (ae.getLangpath() != null)
/*  78 */         description = ae.getLangpath(); 
/*  79 */       if (ae.getCause() instanceof org.tlauncher.tlauncher.minecraft.user.gos.GameOwnershipValidationException) {
/*  80 */         description = "no.ownership.found";
/*     */       }
/*  82 */       if (ae.getCause() instanceof org.tlauncher.tlauncher.minecraft.user.xb.xsts.ChildAccountException) {
/*  83 */         description = "child.limit";
/*     */       }
/*  85 */       if (Objects.nonNull(ae.getCause()) && Objects.nonNull(ae.getCause().getCause()) && ae
/*  86 */         .getCause().getCause() instanceof InvalidStatusCodeException && ((InvalidStatusCodeException)e
/*  87 */         .getCause().getCause()).getResponse()
/*  88 */         .contains("http://support.xbox.com/xbox-live/country-not-authorized")) {
/*  89 */         U.log(new Object[] { "set limit" });
/*  90 */         description = "limit.country";
/*     */       } 
/*  92 */     } else if (e instanceof by.gdev.util.excepiton.NotAllowWriteFileOperation) {
/*  93 */       description = "";
/*  94 */       Alert.showErrorHtml("auth.error.title", Localizable.get("auth.error.can.not.write", new Object[] { e.getMessage() }));
/*     */       return;
/*     */     } 
/*  97 */     if (e instanceof BlockedUserException) {
/*  98 */       Alert.showErrorHtml("auth.error.title", Localizable.get("auth.error." + description, new Object[] { ((BlockedUserException)e).getMinutes() }));
/*     */       
/*     */       return;
/*     */     } 
/* 102 */     Alert.showErrorHtml("auth.error.title", "auth.error." + description);
/*     */     
/* 104 */     if (e instanceof org.tlauncher.tlauncher.exceptions.auth.NotCorrectTokenOrIdException)
/* 105 */       SwingUtilities.invokeLater(() -> {
/*     */             MainPane m = (TLauncher.getInstance().getFrame()).mp;
/*     */             m.openAccountEditor();
/*     */           }); 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\listeners\auth\AuthenticatorSaveListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */