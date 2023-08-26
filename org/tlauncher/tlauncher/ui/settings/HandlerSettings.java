/*    */ package org.tlauncher.tlauncher.ui.settings;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.tlauncher.tlauncher.ui.editor.EditorField;
/*    */ import org.tlauncher.tlauncher.ui.editor.EditorFieldChangeListener;
/*    */ 
/*    */ 
/*    */ class HandlerSettings
/*    */ {
/*    */   private String key;
/*    */   private EditorField editorField;
/*    */   private List<EditorFieldChangeListener> listeners;
/*    */   
/*    */   public HandlerSettings(String key, EditorField editorField) {
/* 16 */     this.listeners = new ArrayList<>();
/* 17 */     this.key = key;
/* 18 */     this.editorField = editorField;
/*    */   }
/*    */   
/*    */   public HandlerSettings(String key, EditorField editorField, EditorFieldChangeListener listener) {
/* 22 */     this(key, editorField);
/* 23 */     this.listeners.add(listener);
/*    */   }
/*    */   
/*    */   public String getKey() {
/* 27 */     return this.key;
/*    */   }
/*    */   
/*    */   public void setKey(String key) {
/* 31 */     this.key = key;
/*    */   }
/*    */   
/*    */   public EditorField getEditorField() {
/* 35 */     return this.editorField;
/*    */   }
/*    */   
/*    */   public void setEditorField(EditorField editorField) {
/* 39 */     this.editorField = editorField;
/*    */   }
/*    */   
/*    */   public void onChange(String oldValue, String newValue) {
/* 43 */     for (EditorFieldChangeListener editorFieldListener : this.listeners)
/* 44 */       editorFieldListener.onChange(oldValue, newValue); 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\settings\HandlerSettings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */