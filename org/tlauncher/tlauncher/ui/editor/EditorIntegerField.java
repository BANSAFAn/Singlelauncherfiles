/*    */ package org.tlauncher.tlauncher.ui.editor;
/*    */ 
/*    */ public class EditorIntegerField
/*    */   extends EditorTextField
/*    */ {
/*    */   private static final long serialVersionUID = -7930510655707946312L;
/*    */   
/*    */   public EditorIntegerField() {}
/*    */   
/*    */   public EditorIntegerField(String prompt) {
/* 11 */     super(prompt);
/*    */   }
/*    */   
/*    */   public int getIntegerValue() {
/*    */     try {
/* 16 */       return Integer.parseInt(getSettingsValue());
/* 17 */     } catch (Exception exception) {
/*    */ 
/*    */       
/* 20 */       return -1;
/*    */     } 
/*    */   }
/*    */   
/*    */   public boolean isValueValid() {
/*    */     try {
/* 26 */       Integer.parseInt(getSettingsValue());
/* 27 */     } catch (Exception e) {
/* 28 */       return false;
/*    */     } 
/* 30 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\editor\EditorIntegerField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */