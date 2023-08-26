/*    */ package org.tlauncher.tlauncher.ui.editor;
/*    */ 
/*    */ import org.tlauncher.tlauncher.ui.loc.LocalizableCheckbox;
/*    */ 
/*    */ public class EditorCheckBox
/*    */   extends LocalizableCheckbox implements EditorField {
/*    */   private static final long serialVersionUID = -2540132118355226609L;
/*    */   
/*    */   public EditorCheckBox(String path) {
/* 10 */     super(path, LocalizableCheckbox.PANEL_TYPE.SETTINGS);
/* 11 */     setFocusable(false);
/* 12 */     setIconTextGap(10);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSettingsValue() {
/* 17 */     return isSelected() ? "true" : "false";
/*    */   }
/*    */ 
/*    */   
/*    */   public void setSettingsValue(String value) {
/* 22 */     setSelected(Boolean.parseBoolean(value));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isValueValid() {
/* 27 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void block(Object reason) {
/* 32 */     setEnabled(false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void unblock(Object reason) {
/* 37 */     setEnabled(true);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\editor\EditorCheckBox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */