/*     */ package org.tlauncher.tlauncher.ui.login;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Font;
/*     */ import java.awt.GridLayout;
/*     */ import java.awt.event.ComponentEvent;
/*     */ import java.awt.event.ComponentListener;
/*     */ import java.awt.event.ItemEvent;
/*     */ import java.util.Objects;
/*     */ import javafx.application.Platform;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.SpringLayout;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.border.CompoundBorder;
/*     */ import javax.swing.border.EmptyBorder;
/*     */ import org.tlauncher.tlauncher.managers.ProfileManager;
/*     */ import org.tlauncher.tlauncher.minecraft.auth.Account;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.block.BlockablePanel;
/*     */ import org.tlauncher.tlauncher.ui.loc.LocalizableCheckbox;
/*     */ import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
/*     */ import org.tlauncher.util.OS;
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
/*     */ public class AccountPanel
/*     */   extends BlockablePanel
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private static boolean helperOpen = false;
/*     */   public final AccountComboBox accountComboBox;
/*     */   public final LoginForm loginForm;
/*     */   public final UsernameField username;
/*     */   public final LocalizableCheckbox chooseTypeAccount;
/*     */   private final ExtendedPanel accountPanel;
/*     */   private Account.AccountType type;
/*     */   
/*     */   public AccountPanel(LoginForm lf, boolean officialAccountType) {
/*  47 */     this.loginForm = lf;
/*  48 */     ProfileManager profileManager = TLauncher.getInstance().getProfileManager();
/*  49 */     this.username = new UsernameField("");
/*     */     
/*  51 */     if (Objects.isNull(lf.global.get("login.account")) && profileManager.hasSelectedAccount()) {
/*  52 */       this.username.setValue(profileManager.getSelectedAccount().getDisplayName());
/*  53 */     } else if (Objects.nonNull(lf.global.get("login.account"))) {
/*  54 */       this.username.setValue(lf.global.get("login.account"));
/*  55 */     }  Border empty = new EmptyBorder(0, 9, 0, 0);
/*  56 */     CompoundBorder border = new CompoundBorder(BorderFactory.createEmptyBorder(), empty);
/*  57 */     this.username.setBorder(border);
/*  58 */     this.accountComboBox = new AccountComboBox(lf);
/*     */     
/*  60 */     this.chooseTypeAccount = new LocalizableCheckbox("loginform.checkbox.account");
/*  61 */     this.chooseTypeAccount.setForeground(Color.WHITE);
/*  62 */     this.chooseTypeAccount.setIconTextGap(14);
/*  63 */     this.chooseTypeAccount.setFocusPainted(false);
/*  64 */     SpringLayout springLayout = new SpringLayout();
/*  65 */     setLayout(springLayout);
/*     */     
/*  67 */     this.accountPanel = new ExtendedPanel(new GridLayout(1, 1));
/*  68 */     springLayout.putConstraint("North", (Component)this.accountPanel, 11, "North", (Component)this);
/*  69 */     springLayout.putConstraint("West", (Component)this.accountPanel, 0, "West", (Component)this);
/*  70 */     springLayout.putConstraint("South", (Component)this.accountPanel, 35, "North", (Component)this);
/*  71 */     springLayout.putConstraint("East", (Component)this.accountPanel, 0, "East", (Component)this);
/*  72 */     add((Component)this.accountPanel);
/*  73 */     if (officialAccountType) {
/*  74 */       this.accountPanel.add((Component)this.accountComboBox);
/*     */       
/*  76 */       this.type = Account.AccountType.MOJANG;
/*  77 */       lf.addLoginProcessListener(this.accountComboBox);
/*     */     } else {
/*  79 */       this.accountPanel.add((Component)this.username);
/*  80 */       this.type = Account.AccountType.FREE;
/*  81 */       lf.addLoginProcessListener(this.username);
/*     */     } 
/*     */     
/*  84 */     if (!OS.is(new OS[] { OS.WINDOWS })) {
/*  85 */       Font f = this.chooseTypeAccount.getFont();
/*  86 */       this.chooseTypeAccount.setFont(new Font(f.getFamily(), f.getStyle(), 11));
/*     */     } 
/*     */     
/*  89 */     springLayout.putConstraint("North", (Component)this.chooseTypeAccount, 44, "North", (Component)this);
/*  90 */     springLayout.putConstraint("South", (Component)this.chooseTypeAccount, -11, "South", (Component)this);
/*  91 */     springLayout.putConstraint("West", (Component)this.chooseTypeAccount, -4, "West", (Component)this.accountPanel);
/*  92 */     springLayout.putConstraint("East", (Component)this.chooseTypeAccount, 0, "East", (Component)this.accountPanel);
/*  93 */     add((Component)this.chooseTypeAccount);
/*  94 */     this.chooseTypeAccount.getModel().setSelected(officialAccountType);
/*     */     
/*  96 */     this.chooseTypeAccount.addItemListener(e -> {
/*     */           if (e.getStateChange() == 1) {
/*     */             Platform.runLater(new Runnable()
/*     */                 {
/*     */                   public void run()
/*     */                   {
/* 102 */                     AccountPanel.this.changeTypeAccount(Account.AccountType.SPECIAL);
/* 103 */                     TLauncher.getInstance().getConfiguration().set("chooser.type.account", Boolean.valueOf(true));
/* 104 */                     if (TLauncher.getInstance().getConfiguration().isFirstRun() && !AccountPanel.helperOpen) {
/* 105 */                       AccountPanel.this.loginForm.pane.openAccountEditor();
/* 106 */                       AccountPanel.this.loginForm.pane.accountEditor.setShownAccountHelper(true, true);
/* 107 */                       AccountPanel.helperOpen = true;
/* 108 */                       AccountPanel.this.loginForm.pane.accountEditor.addComponentListener(new ComponentListener()
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
/* 127 */                               AccountPanel.this.loginForm.pane.accountEditor.setShownAccountHelper(false, false);
/*     */                             }
/*     */                           });
/*     */                     } 
/*     */                   }
/*     */                 });
/*     */           } else {
/*     */             changeTypeAccount(Account.AccountType.FREE);
/*     */             TLauncher.getInstance().getConfiguration().set("chooser.type.account", Boolean.valueOf(false));
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void changeTypeAccount(Account.AccountType type) {
/* 148 */     if (type.equals(Account.AccountType.SPECIAL)) {
/*     */       
/* 150 */       if (this.accountPanel.contains((Component)this.username)) {
/* 151 */         this.accountPanel.remove((Component)this.username);
/*     */       }
/* 153 */       this.accountPanel.add((Component)this.accountComboBox);
/*     */       
/* 155 */       this.loginForm.addLoginProcessListener(this.accountComboBox, 0);
/* 156 */       this.loginForm.removeLoginProcessListener(this.username);
/* 157 */       this.type = Account.AccountType.SPECIAL;
/*     */     }
/*     */     else {
/*     */       
/* 161 */       if (this.accountPanel.contains((Component)this.accountComboBox))
/* 162 */         this.accountPanel.remove((Component)this.accountComboBox); 
/* 163 */       this.accountPanel.add((Component)this.username);
/*     */       
/* 165 */       this.loginForm.addLoginProcessListener(this.username, 0);
/* 166 */       this.loginForm.removeLoginProcessListener(this.accountComboBox);
/* 167 */       this.type = Account.AccountType.FREE;
/*     */     } 
/* 169 */     revalidate();
/* 170 */     repaint();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUsername() {
/* 177 */     if (this.type.equals(Account.AccountType.FREE)) {
/* 178 */       return this.username.getUsername();
/*     */     }
/* 180 */     return ((Account)this.accountComboBox.getSelectedItem()).getUsername();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Account.AccountType getTypeAccountShow() {
/* 188 */     return this.type;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\login\AccountPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */