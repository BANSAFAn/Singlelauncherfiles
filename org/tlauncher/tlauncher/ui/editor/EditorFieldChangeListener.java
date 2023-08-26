/*    */ package org.tlauncher.tlauncher.ui.editor;
/*    */ 
/*    */ public abstract class EditorFieldChangeListener
/*    */   extends EditorFieldListener
/*    */ {
/*    */   protected void onChange(EditorHandler handler, String oldValue, String newValue) {
/*  7 */     if (newValue == null && oldValue == null)
/*    */       return; 
/*  9 */     if (newValue != null && newValue.equals(oldValue)) {
/*    */       return;
/*    */     }
/* 12 */     onChange(oldValue, newValue);
/*    */   }
/*    */   
/*    */   public abstract void onChange(String paramString1, String paramString2);
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\editor\EditorFieldChangeListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */