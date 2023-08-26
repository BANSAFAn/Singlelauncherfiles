/*    */ package org.tlauncher.tlauncher.ui.login;
/*    */ 
/*    */ import java.awt.event.FocusEvent;
/*    */ import java.awt.event.FocusListener;
/*    */ import org.tlauncher.tlauncher.listeners.auth.LoginProcessListener;
/*    */ import org.tlauncher.tlauncher.managers.ProfileManager;
/*    */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*    */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*    */ import org.tlauncher.tlauncher.ui.block.Blockable;
/*    */ import org.tlauncher.tlauncher.ui.loc.LocalizableTextField;
/*    */ 
/*    */ public class UsernameField
/*    */   extends LocalizableTextField
/*    */   implements Blockable, LoginProcessListener {
/* 15 */   private final ProfileManager profileManager = TLauncher.getInstance().getProfileManager();
/*    */   
/*    */   public UsernameField(String login) {
/* 18 */     super("account.username");
/*    */     
/* 20 */     addFocusListener(new FocusListener()
/*    */         {
/*    */           public void focusGained(FocusEvent e) {
/* 23 */             UsernameField.this.setBackground(UsernameField.this.getTheme().getBackground());
/*    */           }
/*    */ 
/*    */           
/*    */           public void focusLost(FocusEvent e) {
/* 28 */             UsernameField.this.profileManager.updateFreeAccountField(UsernameField.this.getUsername());
/*    */           }
/*    */         });
/* 31 */     setValue(login);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getUsername() {
/* 36 */     return getValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public void block(Object reason) {
/* 41 */     setEnabled(false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void unblock(Object reason) {
/* 46 */     setEnabled(true);
/*    */   }
/*    */ 
/*    */   
/*    */   public void validatePreGameLaunch() throws LoginException {
/* 51 */     String u = getUsername();
/* 52 */     if (u != null) {
/* 53 */       this.profileManager.updateFreeAccountField(u);
/*    */       return;
/*    */     } 
/* 56 */     setBackground(getTheme().getFailure());
/* 57 */     Alert.showLocError("auth.error.nousername");
/* 58 */     throw new LoginException("Invalid user name!");
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\login\UsernameField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */