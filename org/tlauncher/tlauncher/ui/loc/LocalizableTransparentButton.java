/*    */ package org.tlauncher.tlauncher.ui.loc;
/*    */ 
/*    */ import org.tlauncher.tlauncher.ui.swing.TransparentButton;
/*    */ 
/*    */ public class LocalizableTransparentButton
/*    */   extends TransparentButton
/*    */   implements LocalizableComponent {
/*    */   private static final long serialVersionUID = -1357535949476677157L;
/*    */   private String path;
/*    */   private String[] variables;
/*    */   
/*    */   public LocalizableTransparentButton(String path, Object... vars) {
/* 13 */     setOpaque(false);
/* 14 */     setText(path, vars);
/*    */   }
/*    */   
/*    */   void setText(String path, Object... vars) {
/* 18 */     this.path = path;
/* 19 */     this.variables = Localizable.checkVariables(vars);
/*    */     
/* 21 */     String value = Localizable.get(path);
/* 22 */     for (int i = 0; i < this.variables.length; i++) {
/* 23 */       value = value.replace("%" + i, this.variables[i]);
/*    */     }
/* 25 */     super.setText(value);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setText(String path) {
/* 30 */     setText(path, Localizable.EMPTY_VARS);
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateLocale() {
/* 35 */     setText(this.path, (Object[])this.variables);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\loc\LocalizableTransparentButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */