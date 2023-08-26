/*    */ package org.tlauncher.tlauncher.ui.login;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import java.awt.Dimension;
/*    */ import javax.swing.Box;
/*    */ import javax.swing.JButton;
/*    */ import org.tlauncher.tlauncher.minecraft.auth.Account;
/*    */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*    */ import org.tlauncher.tlauncher.ui.swing.extended.VPanel;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class InputPanel
/*    */   extends VPanel
/*    */ {
/*    */   private static final long serialVersionUID = -4418780541141471184L;
/*    */   public final LoginForm loginForm;
/*    */   public final UsernameField username;
/*    */   public final VersionComboBox version;
/*    */   public final CheckBoxPanel checkbox;
/*    */   public final AccountComboBox accountComboBox;
/*    */   private Account.AccountType type;
/*    */   
/*    */   public InputPanel(LoginForm lf) {
/* 26 */     boolean chooser = TLauncher.getInstance().getConfiguration().getBoolean("chooser.type.account");
/* 27 */     this.loginForm = lf;
/* 28 */     this.username = new UsernameField("test");
/* 29 */     this.accountComboBox = new AccountComboBox(lf);
/* 30 */     if (chooser) {
/* 31 */       add((Component)this.accountComboBox, Box.createRigidArea(new Dimension(1, 3)));
/* 32 */       this.type = Account.AccountType.MOJANG;
/* 33 */       lf.addLoginProcessListener(this.accountComboBox);
/*    */     } else {
/* 35 */       add((Component)this.username, Box.createRigidArea(new Dimension(1, 3)));
/* 36 */       this.type = Account.AccountType.FREE;
/* 37 */       lf.addLoginProcessListener(this.username);
/*    */     } 
/*    */     
/* 40 */     this.version = new VersionComboBox(lf);
/*    */     
/* 42 */     add((Component)this.version);
/*    */     
/* 44 */     this.checkbox = new CheckBoxPanel(lf, this, chooser);
/* 45 */     add((Component)this.checkbox);
/* 46 */     add(new JButton("test"));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void changeTypeAccount(Account.AccountType type) {
/* 55 */     if (type.equals(Account.AccountType.SPECIAL)) {
/*    */       
/* 57 */       if (contains((Component)this.username)) {
/* 58 */         remove((Component)this.username);
/*    */       }
/* 60 */       add((Component)this.accountComboBox, 0);
/*    */       
/* 62 */       this.loginForm.addLoginProcessListener(this.accountComboBox);
/* 63 */       this.loginForm.removeLoginProcessListener(this.username);
/* 64 */       this.type = Account.AccountType.SPECIAL;
/*    */     }
/*    */     else {
/*    */       
/* 68 */       if (contains((Component)this.accountComboBox))
/* 69 */         remove((Component)this.accountComboBox); 
/* 70 */       add((Component)this.username, 0);
/*    */       
/* 72 */       this.loginForm.addLoginProcessListener(this.username);
/* 73 */       this.loginForm.removeLoginProcessListener(this.accountComboBox);
/* 74 */       this.type = Account.AccountType.FREE;
/*    */     } 
/* 76 */     revalidate();
/* 77 */     repaint();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getUsername() {
/* 84 */     if (this.type.equals(Account.AccountType.FREE)) {
/* 85 */       return this.username.getUsername();
/*    */     }
/* 87 */     return ((Account)this.accountComboBox.getSelectedItem()).getUsername();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Account.AccountType getTypeAccountShow() {
/* 95 */     return this.type;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\login\InputPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */