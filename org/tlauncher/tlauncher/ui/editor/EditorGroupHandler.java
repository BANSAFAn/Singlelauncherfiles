/*    */ package org.tlauncher.tlauncher.ui.editor;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ 
/*    */ public class EditorGroupHandler {
/*    */   private final List<EditorFieldChangeListener> listeners;
/*    */   private final int checkedLimit;
/*    */   private int changedFlag;
/*    */   private int checkedFlag;
/*    */   
/*    */   public EditorGroupHandler(List<? extends EditorHandler> handlers) {
/* 15 */     if (handlers == null) {
/* 16 */       throw new NullPointerException();
/*    */     }
/* 18 */     this.checkedLimit = handlers.size();
/*    */     
/* 20 */     EditorFieldListener listener = new EditorFieldListener()
/*    */       {
/*    */         protected void onChange(EditorHandler handler, String oldValue, String newValue)
/*    */         {
/* 24 */           if (newValue == null)
/*    */             return; 
/* 26 */           if (!newValue.equals(oldValue)) {
/* 27 */             ++EditorGroupHandler.this.changedFlag;
/*    */           }
/* 29 */           ++EditorGroupHandler.this.checkedFlag;
/*    */           
/* 31 */           if (EditorGroupHandler.this.checkedFlag == EditorGroupHandler.this.checkedLimit) {
/*    */             
/* 33 */             if (EditorGroupHandler.this.changedFlag > 0)
/* 34 */               for (EditorFieldChangeListener listener : EditorGroupHandler.this.listeners) {
/* 35 */                 listener.onChange(null, null);
/*    */               } 
/* 37 */             EditorGroupHandler.this.checkedFlag = EditorGroupHandler.this.changedFlag = 0;
/*    */           } 
/*    */         }
/*    */       };
/*    */     
/* 42 */     for (int i = 0; i < handlers.size(); i++) {
/* 43 */       EditorHandler handler = handlers.get(i);
/* 44 */       if (handler == null) {
/* 45 */         throw new NullPointerException("Handler is NULL at " + i);
/*    */       }
/* 47 */       handler.addListener(listener);
/*    */     } 
/*    */     
/* 50 */     this.listeners = Collections.synchronizedList(new ArrayList<>());
/*    */   }
/*    */   
/*    */   public EditorGroupHandler(EditorHandler... handlers) {
/* 54 */     this(Arrays.asList(handlers));
/*    */   }
/*    */   
/*    */   public boolean addListener(EditorFieldChangeListener listener) {
/* 58 */     if (listener == null) {
/* 59 */       throw new NullPointerException();
/*    */     }
/* 61 */     return this.listeners.add(listener);
/*    */   }
/*    */   
/*    */   public boolean removeListener(EditorFieldChangeListener listener) {
/* 65 */     if (listener == null) {
/* 66 */       throw new NullPointerException();
/*    */     }
/* 68 */     return this.listeners.remove(listener);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\editor\EditorGroupHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */