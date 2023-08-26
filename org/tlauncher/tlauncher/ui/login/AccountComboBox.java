/*     */ package org.tlauncher.tlauncher.ui.login;
/*     */ 
/*     */ import com.google.common.base.Objects;
/*     */ import java.awt.event.ItemEvent;
/*     */ import java.awt.event.ItemListener;
/*     */ import java.util.Collection;
/*     */ import javax.swing.ListCellRenderer;
/*     */ import javax.swing.SwingUtilities;
/*     */ import org.tlauncher.tlauncher.entity.profile.ClientProfile;
/*     */ import org.tlauncher.tlauncher.listeners.auth.LoginProcessListener;
/*     */ import org.tlauncher.tlauncher.managers.ModpackManager;
/*     */ import org.tlauncher.tlauncher.managers.ProfileManager;
/*     */ import org.tlauncher.tlauncher.managers.ProfileManagerListener;
/*     */ import org.tlauncher.tlauncher.minecraft.auth.Account;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*     */ import org.tlauncher.tlauncher.ui.block.Blockable;
/*     */ import org.tlauncher.tlauncher.ui.swing.AccountCellRenderer;
/*     */ import org.tlauncher.tlauncher.ui.swing.SimpleComboBoxModel;
/*     */ import org.tlauncher.tlauncher.ui.swing.extended.ExtendedComboBox;
/*     */ import org.tlauncher.util.U;
/*     */ 
/*     */ public class AccountComboBox
/*     */   extends ExtendedComboBox<Account>
/*     */   implements Blockable, LoginProcessListener, ProfileManagerListener {
/*     */   private static final long serialVersionUID = 6618039863712810645L;
/*  27 */   private static final Account EMPTY = AccountCellRenderer.EMPTY; private static final Account MANAGE = AccountCellRenderer.MANAGE;
/*     */   
/*     */   private final ProfileManager profileManager;
/*     */   
/*     */   private final LoginForm loginForm;
/*     */   private final SimpleComboBoxModel<Account> model;
/*     */   
/*     */   AccountComboBox(LoginForm lf) {
/*  35 */     super((ListCellRenderer)new AccountCellRenderer());
/*     */     
/*  37 */     this.loginForm = lf;
/*  38 */     this.model = getSimpleModel();
/*     */     
/*  40 */     this.profileManager = TLauncher.getInstance().getProfileManager();
/*  41 */     this.profileManager.addListener(this);
/*     */     
/*  43 */     addItemListener(e -> {
/*     */           Account selected = (Account)getSelectedItem();
/*     */           if (selected == null || selected.equals(EMPTY))
/*     */             return; 
/*     */           if (selected.equals(MANAGE)) {
/*     */             this.loginForm.pane.openAccountEditor();
/*     */             return;
/*     */           } 
/*     */           this.profileManager.selectAccount(selected);
/*     */         });
/*  53 */     addItemListener((ItemListener)TLauncher.getInjector().getInstance(ModpackManager.class));
/*     */   }
/*     */   
/*     */   public Account getAccount() {
/*  57 */     Account value = (Account)getSelectedItem();
/*  58 */     return (value == null || value.equals(EMPTY)) ? null : value;
/*     */   }
/*     */   
/*     */   public void setAccount(Account account) {
/*  62 */     if (account == null)
/*     */       return; 
/*  64 */     if (account.equals(getAccount())) {
/*     */       return;
/*     */     }
/*  67 */     setSelectedItem(account);
/*     */   }
/*     */ 
/*     */   
/*     */   public void validatePreGameLaunch() throws LoginException {
/*  72 */     Account account = getAccount();
/*  73 */     if (account == null || EMPTY == account || MANAGE == account) {
/*  74 */       this.loginForm.pane.openAccountEditor();
/*  75 */       Alert.showLocError("account.empty.error");
/*  76 */       throw new LoginException("Account list is empty!");
/*     */     } 
/*     */     
/*  79 */     U.log(new Object[] { "AccountComboBox.validte pre game launch username " + account.toString() + " " + account.getType() });
/*     */   }
/*     */   
/*     */   public void refreshAccounts(ClientProfile cl) {
/*  83 */     SwingUtilities.invokeLater(() -> {
/*     */           removeAllItems();
/*     */           Collection<Account> list = cl.getAccounts().values();
/*     */           if (list.isEmpty()) {
/*     */             addItem(EMPTY);
/*     */           } else {
/*     */             for (Account account : list) {
/*     */               if (Objects.equal(account.getShortUUID(), cl.getSelectedAccountUUID())) {
/*     */                 this.model.setSelectedItem(account);
/*     */               }
/*     */             } 
/*     */             this.model.addElements(list);
/*     */           } 
/*     */           addItem(MANAGE);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fireRefreshed(ClientProfile cp) {
/* 104 */     refreshAccounts(cp);
/*     */   }
/*     */ 
/*     */   
/*     */   public void fireClientProfileChanged(ClientProfile cp) {
/* 109 */     fireRefreshed(cp);
/*     */   }
/*     */ 
/*     */   
/*     */   public void block(Object reason) {
/* 114 */     setEnabled(false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void unblock(Object reason) {
/* 119 */     setEnabled(true);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\login\AccountComboBox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */