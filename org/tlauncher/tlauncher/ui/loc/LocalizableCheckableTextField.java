/*    */ package org.tlauncher.tlauncher.ui.loc;
/*    */ 
/*    */ import org.tlauncher.tlauncher.ui.center.CenterPanel;
/*    */ import org.tlauncher.tlauncher.ui.text.CheckableTextField;
/*    */ 
/*    */ public abstract class LocalizableCheckableTextField
/*    */   extends CheckableTextField
/*    */   implements LocalizableComponent
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private String placeholderPath;
/*    */   
/*    */   private LocalizableCheckableTextField(CenterPanel panel, String placeholderPath, String value) {
/* 14 */     super(panel, null, null);
/*    */     
/* 16 */     this.placeholderPath = placeholderPath;
/* 17 */     setValue(value);
/*    */   }
/*    */ 
/*    */   
/*    */   public LocalizableCheckableTextField(CenterPanel panel, String placeholderPath) {
/* 22 */     this(panel, placeholderPath, (String)null);
/*    */   }
/*    */   
/*    */   public LocalizableCheckableTextField(String placeholderPath, String value) {
/* 26 */     this((CenterPanel)null, placeholderPath, value);
/*    */   }
/*    */   
/*    */   public LocalizableCheckableTextField(String placeholderPath) {
/* 30 */     this((CenterPanel)null, placeholderPath, (String)null);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setPlaceholder(String placeholderPath) {
/* 35 */     this.placeholderPath = placeholderPath;
/* 36 */     super.setPlaceholder((Localizable.get() == null) ? placeholderPath : 
/* 37 */         Localizable.get().get(placeholderPath));
/*    */   }
/*    */   
/*    */   public String getPlaceholderPath() {
/* 41 */     return this.placeholderPath;
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateLocale() {
/* 46 */     setPlaceholder(this.placeholderPath);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\loc\LocalizableCheckableTextField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */