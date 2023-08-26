/*    */ package org.tlauncher.tlauncher.ui.editor;
/*    */ 
/*    */ import org.tlauncher.tlauncher.ui.loc.LocalizableTextField;
/*    */ 
/*    */ public class EditorTextField
/*    */   extends LocalizableTextField
/*    */   implements EditorField {
/*    */   private static final long serialVersionUID = 3920711425159165958L;
/*    */   private final boolean canBeEmpty;
/*    */   
/*    */   public EditorTextField(String prompt, boolean canBeEmpty) {
/* 12 */     super(prompt);
/*    */     
/* 14 */     this.canBeEmpty = canBeEmpty;
/* 15 */     setColumns(1);
/*    */   }
/*    */   
/*    */   public EditorTextField(String prompt) {
/* 19 */     this(prompt, false);
/*    */   }
/*    */   
/*    */   public EditorTextField(boolean canBeEmpty) {
/* 23 */     this((String)null, canBeEmpty);
/*    */   }
/*    */   
/*    */   public EditorTextField() {
/* 27 */     this(false);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSettingsValue() {
/* 32 */     return getValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public void setSettingsValue(String value) {
/* 37 */     setText(value);
/* 38 */     setCaretPosition(0);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isValueValid() {
/* 43 */     String text = getValue();
/* 44 */     return (text != null || this.canBeEmpty);
/*    */   }
/*    */ 
/*    */   
/*    */   public void block(Object reason) {
/* 49 */     setEnabled(false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void unblock(Object reason) {
/* 54 */     setEnabled(true);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\editor\EditorTextField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */