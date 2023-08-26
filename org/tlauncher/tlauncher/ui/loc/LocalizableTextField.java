/*    */ package org.tlauncher.tlauncher.ui.loc;
/*    */ 
/*    */ import org.tlauncher.tlauncher.ui.TLauncherFrame;
/*    */ import org.tlauncher.tlauncher.ui.center.CenterPanel;
/*    */ import org.tlauncher.tlauncher.ui.text.ExtendedTextField;
/*    */ 
/*    */ public class LocalizableTextField
/*    */   extends ExtendedTextField
/*    */   implements LocalizableComponent
/*    */ {
/*    */   private static final long serialVersionUID = 359096767189321072L;
/*    */   protected String placeholderPath;
/*    */   protected String[] variables;
/*    */   
/*    */   public LocalizableTextField(CenterPanel panel, String placeholderPath, String value) {
/* 16 */     super(panel, null, value);
/*    */     
/* 18 */     setValue(value);
/* 19 */     setPlaceholder(placeholderPath);
/* 20 */     setFont(getFont().deriveFont(TLauncherFrame.fontSize));
/*    */   }
/*    */   
/*    */   public LocalizableTextField(CenterPanel panel, String placeholderPath) {
/* 24 */     this(panel, placeholderPath, (String)null);
/*    */   }
/*    */   
/*    */   public LocalizableTextField(String placeholderPath) {
/* 28 */     this((CenterPanel)null, placeholderPath, (String)null);
/*    */   }
/*    */   
/*    */   public LocalizableTextField() {
/* 32 */     this((CenterPanel)null, (String)null, (String)null);
/*    */   }
/*    */   
/*    */   public void setPlaceholder(String placeholderPath, Object... vars) {
/* 36 */     this.placeholderPath = placeholderPath;
/* 37 */     this.variables = Localizable.checkVariables(vars);
/*    */     
/* 39 */     String value = Localizable.get(placeholderPath);
/*    */     
/* 41 */     for (int i = 0; i < this.variables.length; i++) {
/* 42 */       value = value.replace("%" + i, this.variables[i]);
/*    */     }
/* 44 */     super.setPlaceholder(value);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setPlaceholder(String placeholderPath) {
/* 49 */     setPlaceholder(placeholderPath, Localizable.EMPTY_VARS);
/*    */   }
/*    */   
/*    */   public String getPlaceholderPath() {
/* 53 */     return this.placeholderPath;
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateLocale() {
/* 58 */     setPlaceholder(this.placeholderPath, (Object[])this.variables);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\loc\LocalizableTextField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */