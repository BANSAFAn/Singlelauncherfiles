/*    */ package org.tlauncher.tlauncher.ui.editor;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import javax.swing.JComponent;
/*    */ import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
/*    */ import org.tlauncher.tlauncher.ui.swing.extended.VPanel;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EditorPair
/*    */ {
/*    */   private final LocalizableLabel label;
/*    */   private final List<? extends EditorHandler> handlers;
/*    */   private final JComponent[] fields;
/*    */   private final VPanel panel;
/*    */   
/*    */   public EditorPair(String labelPath, List<? extends EditorHandler> handlers) {
/* 20 */     this.label = new LocalizableLabel(labelPath);
/*    */     
/* 22 */     this.label.setFont(this.label.getFont().deriveFont(1));
/* 23 */     int num = handlers.size();
/*    */     
/* 25 */     this.fields = new JComponent[num];
/*    */     
/* 27 */     for (int i = 0; i < num; i++) {
/* 28 */       this.fields[i] = ((EditorHandler)handlers.get(i)).getComponent();
/* 29 */       this.fields[i].setAlignmentX(0.0F);
/*    */     } 
/*    */     
/* 32 */     this.handlers = handlers;
/*    */     
/* 34 */     this.panel = new VPanel();
/* 35 */     this.panel.add((Component[])this.fields);
/*    */   }
/*    */   
/*    */   public EditorPair(String labelPath, EditorHandler... handlers) {
/* 39 */     this(labelPath, Arrays.asList(handlers));
/*    */   }
/*    */   
/*    */   public List<? extends EditorHandler> getHandlers() {
/* 43 */     return this.handlers;
/*    */   }
/*    */   
/*    */   public LocalizableLabel getLabel() {
/* 47 */     return this.label;
/*    */   }
/*    */   
/*    */   public Component[] getFields() {
/* 51 */     return (Component[])this.fields;
/*    */   }
/*    */   
/*    */   public VPanel getPanel() {
/* 55 */     return this.panel;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\editor\EditorPair.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */