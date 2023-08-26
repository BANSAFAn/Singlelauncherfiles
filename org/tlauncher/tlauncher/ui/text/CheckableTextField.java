/*    */ package org.tlauncher.tlauncher.ui.text;
/*    */ 
/*    */ import org.tlauncher.tlauncher.ui.center.CenterPanel;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class CheckableTextField
/*    */   extends ExtendedTextField
/*    */ {
/*    */   private static final long serialVersionUID = 2835507963141686372L;
/*    */   private CenterPanel parent;
/*    */   
/*    */   protected CheckableTextField(CenterPanel panel, String placeholder, String value) {
/* 20 */     super(panel, placeholder, value);
/* 21 */     this.parent = panel;
/*    */   }
/*    */   
/*    */   public CheckableTextField(String placeholder, String value) {
/* 25 */     this((CenterPanel)null, placeholder, value);
/*    */   }
/*    */   
/*    */   public CheckableTextField(String placeholder) {
/* 29 */     this((CenterPanel)null, placeholder, (String)null);
/*    */   }
/*    */   
/*    */   public CheckableTextField(CenterPanel panel) {
/* 33 */     this(panel, (String)null, (String)null);
/*    */   }
/*    */   
/*    */   boolean check() {
/* 37 */     String text = getValue(), result = check(text);
/*    */     
/* 39 */     if (result == null)
/* 40 */       return setValid(); 
/* 41 */     return setInvalid(result);
/*    */   }
/*    */   
/*    */   public boolean setInvalid(String reason) {
/* 45 */     setBackground(getTheme().getFailure());
/* 46 */     setForeground(getTheme().getFocus());
/*    */     
/* 48 */     if (this.parent != null) {
/* 49 */       this.parent.setError(reason);
/*    */     }
/* 51 */     return false;
/*    */   }
/*    */   
/*    */   public boolean setValid() {
/* 55 */     setBackground(getTheme().getBackground());
/* 56 */     setForeground(getTheme().getFocus());
/*    */     
/* 58 */     if (this.parent != null) {
/* 59 */       this.parent.setError(null);
/*    */     }
/* 61 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void updateStyle() {
/* 66 */     super.updateStyle();
/* 67 */     check();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onChange() {
/* 72 */     check();
/*    */   }
/*    */   
/*    */   protected abstract String check(String paramString);
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\text\CheckableTextField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */