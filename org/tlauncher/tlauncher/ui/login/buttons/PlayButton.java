/*     */ package org.tlauncher.tlauncher.ui.login.buttons;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.event.ActionEvent;
/*     */ import net.minecraft.launcher.updater.VersionSyncInfo;
/*     */ import org.tlauncher.tlauncher.ui.block.Blockable;
/*     */ import org.tlauncher.tlauncher.ui.block.Blocker;
/*     */ import org.tlauncher.tlauncher.ui.button.RoundImageButton;
/*     */ import org.tlauncher.tlauncher.ui.images.ImageCache;
/*     */ import org.tlauncher.tlauncher.ui.login.LoginForm;
/*     */ 
/*     */ 
/*     */ public class PlayButton
/*     */   extends RoundImageButton
/*     */   implements Blockable, LoginForm.LoginStateListener
/*     */ {
/*     */   private static final long serialVersionUID = 6944074583143406549L;
/*     */   private PlayButtonState state;
/*     */   private final LoginForm loginForm;
/*     */   
/*     */   public PlayButton(LoginForm lf) {
/*  22 */     super(ImageCache.getImage("play.png"), ImageCache.getImage("play-active.png"));
/*  23 */     setForeground(Color.WHITE);
/*  24 */     this.loginForm = lf;
/*  25 */     addActionListener(e -> {
/*     */           switch (this.state) {
/*     */             case CANCEL:
/*     */               this.loginForm.stopLauncher();
/*     */               return;
/*     */           } 
/*     */ 
/*     */           
/*     */           this.loginForm.startLauncher();
/*     */         });
/*  35 */     setFont(getFont().deriveFont(1).deriveFont(18.0F));
/*  36 */     setState(PlayButtonState.PLAY);
/*     */   }
/*     */   
/*     */   public PlayButtonState getState() {
/*  40 */     return this.state;
/*     */   }
/*     */   
/*     */   public void setState(PlayButtonState state) {
/*  44 */     if (state == null) {
/*  45 */       throw new NullPointerException();
/*     */     }
/*  47 */     this.state = state;
/*  48 */     setText(state.getPath());
/*     */     
/*  50 */     if (state == PlayButtonState.CANCEL) {
/*  51 */       setEnabled(true);
/*     */     }
/*     */   }
/*     */   
/*     */   public void updateState() {
/*  56 */     if (this.loginForm.versions == null)
/*     */       return; 
/*  58 */     VersionSyncInfo vs = this.loginForm.versions.getVersion();
/*  59 */     if (vs == null) {
/*     */       return;
/*     */     }
/*  62 */     boolean installed = vs.isInstalled(), force = this.loginForm.versionPanel.forceupdate.getState();
/*     */     
/*  64 */     if (!installed) {
/*  65 */       setState(PlayButtonState.INSTALL);
/*     */     } else {
/*  67 */       setState(force ? PlayButtonState.REINSTALL : PlayButtonState.PLAY);
/*     */     } 
/*     */   }
/*     */   
/*  71 */   public enum PlayButtonState { REINSTALL("loginform.enter.reinstall"), INSTALL("loginform.enter.install"), PLAY("loginform.enter"), CANCEL("loginform.enter.cancel");
/*     */     
/*     */     private final String path;
/*     */ 
/*     */     
/*     */     PlayButtonState(String path) {
/*  77 */       this.path = path;
/*     */     }
/*     */     
/*     */     public String getPath() {
/*  81 */       return this.path;
/*     */     } }
/*     */ 
/*     */ 
/*     */   
/*     */   public void loginStateChanged(LoginForm.LoginState state) {
/*  87 */     if (state == LoginForm.LoginState.LAUNCHING) {
/*  88 */       setState(PlayButtonState.CANCEL);
/*  89 */       setEnabled(false);
/*     */     } else {
/*  91 */       updateState();
/*  92 */       setEnabled(!Blocker.isBlocked(this));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void block(Object reason) {
/*  98 */     if (this.state != PlayButtonState.CANCEL) {
/*  99 */       setEnabled(false);
/*     */     }
/*     */   }
/*     */   
/*     */   public void unblock(Object reason) {
/* 104 */     setEnabled(true);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\login\buttons\PlayButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */