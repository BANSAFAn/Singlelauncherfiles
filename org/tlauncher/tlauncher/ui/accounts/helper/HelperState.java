/*    */ package org.tlauncher.tlauncher.ui.accounts.helper;
/*    */ 
/*    */ import org.tlauncher.tlauncher.ui.loc.LocalizableMenuItem;
/*    */ 
/*    */ public enum HelperState {
/*  6 */   PREMIUM, FREE, HELP(false), NONE;
/*    */ 
/*    */   
/*    */   public final boolean showInList;
/*    */ 
/*    */   
/*    */   public final LocalizableMenuItem item;
/*    */ 
/*    */   
/*    */   HelperState(boolean showInList) {
/* 16 */     this.item = new LocalizableMenuItem("auth.helper." + toString());
/* 17 */     this.showInList = showInList;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 22 */     return super.toString().toLowerCase();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\accounts\helper\HelperState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */