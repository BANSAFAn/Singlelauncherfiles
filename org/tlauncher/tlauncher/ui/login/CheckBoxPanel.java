/*     */ package org.tlauncher.tlauncher.ui.login;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Font;
/*     */ import java.awt.event.ComponentEvent;
/*     */ import java.awt.event.ComponentListener;
/*     */ import java.awt.event.ItemEvent;
/*     */ import java.awt.event.ItemListener;
/*     */ import javafx.application.Platform;
/*     */ import net.minecraft.launcher.updater.VersionSyncInfo;
/*     */ import org.tlauncher.tlauncher.configuration.Configuration;
/*     */ import org.tlauncher.tlauncher.listeners.auth.LoginProcessListener;
/*     */ import org.tlauncher.tlauncher.minecraft.auth.Account;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*     */ import org.tlauncher.tlauncher.ui.block.BlockablePanel;
/*     */ import org.tlauncher.tlauncher.ui.loc.LocalizableCheckbox;
/*     */ import org.tlauncher.tlauncher.ui.swing.CheckBoxListener;
/*     */ import org.tlauncher.util.OS;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CheckBoxPanel
/*     */   extends BlockablePanel
/*     */   implements LoginProcessListener
/*     */ {
/*     */   private static final long serialVersionUID = 768489049585749260L;
/*     */   public final LocalizableCheckbox forceupdate;
/*     */   private static boolean helperOpen = false;
/*     */   private final LocalizableCheckbox chooseTypeAccount;
/*     */   private final Configuration settings;
/*     */   private boolean state;
/*     */   private final LoginForm loginForm;
/*     */   private final InputPanel inputPanel;
/*     */   
/*     */   CheckBoxPanel(LoginForm lf, InputPanel input, boolean chooserState) {
/*  42 */     this.loginForm = lf;
/*  43 */     this.inputPanel = input;
/*  44 */     this.forceupdate = new LocalizableCheckbox("loginform.checkbox.forceupdate");
/*  45 */     this.forceupdate.setFocusable(false);
/*  46 */     this.chooseTypeAccount = new LocalizableCheckbox("loginform.checkbox.account");
/*  47 */     this.settings = TLauncher.getInstance().getConfiguration();
/*     */     
/*  49 */     if (!OS.is(new OS[] { OS.WINDOWS })) {
/*  50 */       Font f = this.chooseTypeAccount.getFont();
/*  51 */       this.chooseTypeAccount.setFont(new Font(f.getFamily(), f.getStyle(), 11));
/*  52 */       this.forceupdate.setFont(new Font(f.getFamily(), f.getStyle(), 11));
/*     */     } 
/*  54 */     add((Component)this.forceupdate);
/*  55 */     add((Component)this.chooseTypeAccount);
/*  56 */     this.chooseTypeAccount.getModel().setSelected(chooserState);
/*     */     
/*  58 */     this.forceupdate.addItemListener((ItemListener)new CheckBoxListener()
/*     */         {
/*     */           public void itemStateChanged(boolean newstate)
/*     */           {
/*  62 */             CheckBoxPanel.this.state = newstate;
/*  63 */             CheckBoxPanel.this.loginForm.play.updateState();
/*     */           }
/*     */         });
/*     */     
/*  67 */     this.chooseTypeAccount.addItemListener(e -> {
/*     */           if (e.getStateChange() == 1) {
/*     */             Platform.runLater(new Runnable()
/*     */                 {
/*     */                   public void run()
/*     */                   {
/*  73 */                     CheckBoxPanel.this.inputPanel.changeTypeAccount(Account.AccountType.SPECIAL);
/*  74 */                     TLauncher.getInstance().getConfiguration().set("chooser.type.account", Boolean.valueOf(true));
/*  75 */                     if (CheckBoxPanel.this.settings.isFirstRun() && !CheckBoxPanel.helperOpen) {
/*  76 */                       CheckBoxPanel.this.loginForm.pane.openAccountEditor();
/*  77 */                       CheckBoxPanel.this.loginForm.pane.accountEditor.setShownAccountHelper(true, true);
/*  78 */                       CheckBoxPanel.helperOpen = true;
/*  79 */                       CheckBoxPanel.this.loginForm.pane.accountEditor.addComponentListener(new ComponentListener()
/*     */                           {
/*     */                             public void componentShown(ComponentEvent e) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                             
/*     */                             public void componentResized(ComponentEvent e) {}
/*     */ 
/*     */ 
/*     */ 
/*     */                             
/*     */                             public void componentMoved(ComponentEvent e) {}
/*     */ 
/*     */ 
/*     */ 
/*     */                             
/*     */                             public void componentHidden(ComponentEvent e) {
/*  98 */                               CheckBoxPanel.this.loginForm.pane.accountEditor.setShownAccountHelper(false, false);
/*     */                             }
/*     */                           });
/*     */                     } 
/*     */                   }
/*     */                 });
/*     */           } else {
/*     */             this.inputPanel.changeTypeAccount(Account.AccountType.FREE);
/*     */             TLauncher.getInstance().getConfiguration().set("chooser.type.account", Boolean.valueOf(false));
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void validatePreGameLaunch() throws LoginException {
/* 115 */     VersionSyncInfo syncInfo = this.loginForm.versions.getVersion();
/* 116 */     if (syncInfo == null)
/*     */       return; 
/* 118 */     boolean supporting = syncInfo.hasRemote(), installed = syncInfo.isInstalled();
/* 119 */     if (this.state) {
/* 120 */       if (!supporting) {
/* 121 */         Alert.showLocError("forceupdate.local");
/* 122 */         throw new LoginException("Cannot update local version!");
/*     */       } 
/*     */       
/* 125 */       if (installed && !Alert.showLocQuestion("forceupdate.question"))
/* 126 */         throw new LoginException("User has cancelled force updating."); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\login\CheckBoxPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */