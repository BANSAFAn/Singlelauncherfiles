/*    */ package org.tlauncher.tlauncher.ui.accounts;
/*    */ 
/*    */ import org.tlauncher.tlauncher.ui.center.CenterPanel;
/*    */ import org.tlauncher.tlauncher.ui.loc.LocalizableTextField;
/*    */ 
/*    */ public class UsernameField
/*    */   extends LocalizableTextField {
/*    */   private static final long serialVersionUID = -5813187607562947592L;
/*    */   private UsernameState state;
/*    */   String username;
/*    */   
/*    */   public UsernameField(CenterPanel pan, UsernameState state) {
/* 13 */     super(pan, "account.username");
/* 14 */     setState(state);
/*    */   }
/*    */   
/*    */   public UsernameState getState() {
/* 18 */     return this.state;
/*    */   }
/*    */   
/*    */   public void setState(UsernameState state) {
/* 22 */     if (state == null) {
/* 23 */       throw new NullPointerException();
/*    */     }
/* 25 */     this.state = state;
/* 26 */     setPlaceholder(state.placeholder);
/*    */   }
/*    */   
/*    */   public enum UsernameState {
/* 30 */     USERNAME("account.username"),
/* 31 */     EMAIL_LOGIN("account.email"),
/* 32 */     EMAIL("account.email.restrict");
/*    */     
/*    */     private final String placeholder;
/*    */     
/*    */     UsernameState(String placeholder) {
/* 37 */       this.placeholder = placeholder;
/*    */     }
/*    */     
/*    */     public String getPlaceholder() {
/* 41 */       return this.placeholder;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\accounts\UsernameField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */