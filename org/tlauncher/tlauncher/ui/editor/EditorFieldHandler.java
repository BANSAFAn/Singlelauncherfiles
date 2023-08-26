/*    */ package org.tlauncher.tlauncher.ui.editor;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import java.awt.Container;
/*    */ import java.awt.event.FocusListener;
/*    */ import javax.swing.JComponent;
/*    */ import org.tlauncher.tlauncher.ui.block.Blocker;
/*    */ 
/*    */ public class EditorFieldHandler
/*    */   extends EditorHandler
/*    */ {
/*    */   private final EditorField field;
/*    */   private final JComponent comp;
/*    */   
/*    */   public EditorFieldHandler(String path, JComponent component, FocusListener focus) {
/* 16 */     super(path);
/*    */     
/* 18 */     if (component == null) {
/* 19 */       throw new NullPointerException("comp");
/*    */     }
/* 21 */     if (!(component instanceof EditorField)) {
/* 22 */       throw new IllegalArgumentException();
/*    */     }
/* 24 */     if (focus != null) {
/* 25 */       addFocus(component, focus);
/*    */     }
/* 27 */     this.comp = component;
/* 28 */     this.field = (EditorField)component;
/*    */   }
/*    */   
/*    */   public EditorFieldHandler(String path, JComponent comp) {
/* 32 */     this(path, comp, null);
/*    */   }
/*    */ 
/*    */   
/*    */   public JComponent getComponent() {
/* 37 */     return this.comp;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getValue() {
/* 42 */     return this.field.getSettingsValue();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void setValue0(String s) {
/* 47 */     this.field.setSettingsValue(s);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isValid() {
/* 52 */     return this.field.isValueValid();
/*    */   }
/*    */   
/*    */   private void addFocus(Component comp, FocusListener focus) {
/* 56 */     comp.addFocusListener(focus);
/*    */     
/* 58 */     if (comp instanceof Container)
/* 59 */       for (Component curComp : ((Container)comp).getComponents()) {
/* 60 */         addFocus(curComp, focus);
/*    */       } 
/*    */   }
/*    */   
/*    */   public void block(Object reason) {
/* 65 */     Blocker.blockComponents(reason, new Component[] { getComponent() });
/*    */   }
/*    */ 
/*    */   
/*    */   public void unblock(Object reason) {
/* 70 */     Blocker.unblockComponents(reason, new Component[] { getComponent() });
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\editor\EditorFieldHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */