/*    */ package org.tlauncher.tlauncher.ui.editor;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import javax.swing.JComponent;
/*    */ import org.tlauncher.tlauncher.ui.block.Blockable;
/*    */ 
/*    */ 
/*    */ public abstract class EditorHandler
/*    */   implements Blockable
/*    */ {
/*    */   private final String path;
/*    */   private String value;
/*    */   private final List<EditorFieldListener> listeners;
/*    */   
/*    */   public EditorHandler(String path) {
/* 18 */     if (path == null) {
/* 19 */       throw new NullPointerException();
/*    */     }
/* 21 */     this.path = path;
/* 22 */     this
/* 23 */       .listeners = Collections.synchronizedList(new ArrayList<>());
/*    */   }
/*    */   
/*    */   public boolean addListener(EditorFieldListener listener) {
/* 27 */     if (listener == null) {
/* 28 */       throw new NullPointerException();
/*    */     }
/* 30 */     return this.listeners.add(listener);
/*    */   }
/*    */   
/*    */   public boolean removeListener(EditorFieldListener listener) {
/* 34 */     if (listener == null) {
/* 35 */       throw new NullPointerException();
/*    */     }
/* 37 */     return this.listeners.remove(listener);
/*    */   }
/*    */   
/*    */   public void onChange(String newvalue) {
/* 41 */     for (EditorFieldListener listener : this.listeners) {
/* 42 */       listener.onChange(this, this.value, newvalue);
/*    */     }
/* 44 */     this.value = newvalue;
/*    */   }
/*    */   
/*    */   public String getPath() {
/* 48 */     return this.path;
/*    */   }
/*    */   
/*    */   public void updateValue(Object obj) {
/* 52 */     String val = (obj == null) ? null : obj.toString();
/*    */     
/* 54 */     onChange(val);
/* 55 */     setValue0(this.value);
/*    */   }
/*    */   
/*    */   public void setValue(Object obj) {
/* 59 */     String val = (obj == null) ? null : obj.toString();
/*    */     
/* 61 */     setValue0(val);
/*    */   }
/*    */ 
/*    */   
/*    */   public abstract boolean isValid();
/*    */   
/*    */   public abstract JComponent getComponent();
/*    */   
/*    */   public abstract String getValue();
/*    */   
/*    */   protected abstract void setValue0(String paramString);
/*    */   
/*    */   public String toString() {
/* 74 */     return getClass().getSimpleName() + "{path='" + this.path + "', value='" + this.value + "'}";
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\editor\EditorHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */