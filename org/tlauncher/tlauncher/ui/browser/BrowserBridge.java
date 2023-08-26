/*    */ package org.tlauncher.tlauncher.ui.browser;
/*    */ 
/*    */ import org.tlauncher.tlauncher.ui.login.LoginFormHelper;
/*    */ 
/*    */ public class BrowserBridge {
/*    */   private final BrowserHolder holder;
/*    */   
/*    */   BrowserBridge(BrowserPanel browser) {
/*  9 */     this.holder = browser.holder;
/*    */   }
/*    */   
/*    */   public void toggleHelper() {
/* 13 */     if (this.holder.pane.defaultScene.loginFormHelper.getState() == LoginFormHelper.LoginFormHelperState.NONE) {
/* 14 */       this.holder.pane.defaultScene.loginFormHelper.setState(LoginFormHelper.LoginFormHelperState.SHOWN);
/*    */     } else {
/* 16 */       this.holder.pane.defaultScene.loginFormHelper.setState(LoginFormHelper.LoginFormHelperState.NONE);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\browser\BrowserBridge.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */