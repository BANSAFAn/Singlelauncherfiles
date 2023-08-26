/*    */ package org.tlauncher.tlauncher.ui.loc;
/*    */ 
/*    */ import org.tlauncher.tlauncher.ui.swing.extended.ExtendedButton;
/*    */ 
/*    */ public class LocalizableButton
/*    */   extends ExtendedButton
/*    */   implements LocalizableComponent
/*    */ {
/*    */   private static final long serialVersionUID = 1073130908385613323L;
/*    */   private String path;
/*    */   private String[] variables;
/*    */   
/*    */   protected LocalizableButton() {}
/*    */   
/*    */   public LocalizableButton(String path) {
/* 16 */     this();
/* 17 */     setText(path);
/*    */   }
/*    */   
/*    */   public LocalizableButton(String path, Object... vars) {
/* 21 */     this();
/* 22 */     setText(path, vars);
/*    */   }
/*    */   
/*    */   public void setText(String path, Object... vars) {
/* 26 */     this.path = path;
/* 27 */     this.variables = Localizable.checkVariables(vars);
/*    */     
/* 29 */     String value = Localizable.get(path);
/* 30 */     for (int i = 0; i < this.variables.length; i++) {
/* 31 */       value = value.replace("%" + i, this.variables[i]);
/*    */     }
/* 33 */     super.setText(value);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setText(String path) {
/* 38 */     setText(path, Localizable.EMPTY_VARS);
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateLocale() {
/* 43 */     setText(this.path, (Object[])this.variables);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\loc\LocalizableButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */