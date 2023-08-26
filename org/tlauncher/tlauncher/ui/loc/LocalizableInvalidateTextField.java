/*    */ package org.tlauncher.tlauncher.ui.loc;
/*    */ 
/*    */ import org.tlauncher.tlauncher.ui.center.CenterPanel;
/*    */ import org.tlauncher.tlauncher.ui.text.InvalidateTextField;
/*    */ 
/*    */ public class LocalizableInvalidateTextField
/*    */   extends InvalidateTextField
/*    */   implements LocalizableComponent
/*    */ {
/*    */   private static final long serialVersionUID = -3999545292427982797L;
/*    */   private String placeholderPath;
/*    */   
/*    */   private LocalizableInvalidateTextField(CenterPanel panel, String placeholderPath, String value) {
/* 14 */     super(panel, null, value);
/*    */     
/* 16 */     this.placeholderPath = placeholderPath;
/* 17 */     setValue(value);
/*    */   }
/*    */   
/*    */   protected LocalizableInvalidateTextField(String placeholderPath) {
/* 21 */     this((CenterPanel)null, placeholderPath, (String)null);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setPlaceholder(String placeholderPath) {
/* 26 */     this.placeholderPath = placeholderPath;
/* 27 */     super.setPlaceholder((Localizable.get() == null) ? placeholderPath : 
/* 28 */         Localizable.get().get(placeholderPath));
/*    */   }
/*    */   
/*    */   public String getPlaceholderPath() {
/* 32 */     return this.placeholderPath;
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateLocale() {
/* 37 */     setPlaceholder(this.placeholderPath);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\loc\LocalizableInvalidateTextField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */