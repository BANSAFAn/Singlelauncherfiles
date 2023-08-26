/*    */ package org.tlauncher.tlauncher.ui.editor;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Insets;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.JLabel;
/*    */ import org.tlauncher.tlauncher.ui.center.CenterPanel;
/*    */ import org.tlauncher.tlauncher.ui.center.CenterPanelTheme;
/*    */ import org.tlauncher.tlauncher.ui.images.ImageCache;
/*    */ import org.tlauncher.tlauncher.ui.images.ImageIcon;
/*    */ import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
/*    */ 
/*    */ public abstract class AbstractEditorPanel
/*    */   extends CenterPanel {
/*    */   protected final List<EditorHandler> handlers;
/*    */   
/*    */   public AbstractEditorPanel(CenterPanelTheme theme, Insets insets) {
/* 20 */     super(theme, insets);
/*    */     
/* 22 */     this.handlers = new ArrayList<>();
/*    */   }
/*    */   
/*    */   public AbstractEditorPanel(Insets insets) {
/* 26 */     this(null, insets);
/*    */   }
/*    */   
/*    */   public AbstractEditorPanel() {
/* 30 */     this(null, null);
/*    */   }
/*    */   
/*    */   protected boolean checkValues() {
/* 34 */     boolean allValid = true;
/*    */     
/* 36 */     for (EditorHandler handler : this.handlers) {
/* 37 */       boolean valid = handler.isValid();
/*    */       
/* 39 */       setValid(handler, valid);
/*    */       
/* 41 */       if (!valid) {
/* 42 */         allValid = false;
/*    */       }
/*    */     } 
/* 45 */     return allValid;
/*    */   }
/*    */   
/*    */   protected void setValid(EditorHandler handler, boolean valid) {
/* 49 */     Color color = valid ? getTheme().getBackground() : getTheme().getFailure();
/*    */     
/* 51 */     handler.getComponent().setOpaque(!valid);
/* 52 */     handler.getComponent().setBackground(color);
/*    */   }
/*    */   
/*    */   protected JComponent createTip(String label, boolean warning) {
/* 56 */     LocalizableLabel tip = new LocalizableLabel(label);
/*    */     
/* 58 */     if (warning) {
/* 59 */       ImageIcon.setup((JLabel)tip, ImageCache.getIcon("warning.png", 16, 16));
/*    */     }
/* 61 */     return (JComponent)tip;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\editor\AbstractEditorPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */