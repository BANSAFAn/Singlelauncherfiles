/*    */ package org.tlauncher.tlauncher.ui.loc;
/*    */ 
/*    */ import org.tlauncher.tlauncher.ui.TLauncherFrame;
/*    */ import org.tlauncher.tlauncher.ui.swing.extended.ExtendedLabel;
/*    */ 
/*    */ public class LocalizableLabel
/*    */   extends ExtendedLabel
/*    */   implements LocalizableComponent {
/*    */   private static final long serialVersionUID = 7628068160047735335L;
/*    */   protected String path;
/*    */   protected String[] variables;
/*    */   
/*    */   public LocalizableLabel(String path, Object... vars) {
/* 14 */     setText(path, vars);
/* 15 */     setFont(getFont().deriveFont(TLauncherFrame.fontSize));
/*    */   }
/*    */   
/*    */   public LocalizableLabel(String path) {
/* 19 */     this(path, Localizable.EMPTY_VARS);
/*    */   }
/*    */   
/*    */   public LocalizableLabel() {
/* 23 */     this((String)null);
/*    */   }
/*    */   
/*    */   public LocalizableLabel(int horizontalAlignment) {
/* 27 */     this((String)null);
/* 28 */     setHorizontalAlignment(horizontalAlignment);
/*    */   }
/*    */   
/*    */   public void setText(String path, Object... vars) {
/* 32 */     this.path = path;
/* 33 */     this.variables = Localizable.checkVariables(vars);
/*    */     
/* 35 */     String value = Localizable.get(path);
/* 36 */     for (int i = 0; i < this.variables.length; i++) {
/* 37 */       value = value.replace("%" + i, this.variables[i]);
/*    */     }
/* 39 */     setRawText(value);
/*    */   }
/*    */   
/*    */   protected void setRawText(String value) {
/* 43 */     super.setText(value);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setText(String path) {
/* 48 */     setText(path, Localizable.EMPTY_VARS);
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateLocale() {
/* 53 */     setText(this.path, (Object[])this.variables);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\loc\LocalizableLabel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */