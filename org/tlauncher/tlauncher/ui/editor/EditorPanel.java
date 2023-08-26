/*    */ package org.tlauncher.tlauncher.ui.editor;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import java.awt.Container;
/*    */ import java.awt.GridBagConstraints;
/*    */ import java.awt.GridBagLayout;
/*    */ import java.awt.Insets;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.swing.BoxLayout;
/*    */ import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
/*    */ import org.tlauncher.tlauncher.ui.swing.ScrollPane;
/*    */ import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
/*    */ import org.tlauncher.tlauncher.ui.swing.extended.VPanel;
/*    */ 
/*    */ public class EditorPanel
/*    */   extends AbstractEditorPanel
/*    */ {
/*    */   private static final long serialVersionUID = 3428243378644563729L;
/*    */   protected final ExtendedPanel container;
/*    */   protected final ScrollPane scroll;
/*    */   private final List<ExtendedPanel> panels;
/*    */   private final List<GridBagConstraints> constraints;
/*    */   protected final List<EditorHandler> handlers;
/*    */   private byte paneNum;
/*    */   private byte rowNum;
/*    */   
/*    */   public EditorPanel(Insets insets) {
/* 29 */     super(insets);
/*    */     
/* 31 */     this.container = new ExtendedPanel();
/* 32 */     this.container.setLayout(new BoxLayout((Container)this.container, 3));
/*    */     
/* 34 */     this.panels = new ArrayList<>();
/* 35 */     this.constraints = new ArrayList<>();
/*    */     
/* 37 */     this.handlers = new ArrayList<>();
/*    */     
/* 39 */     this.scroll = new ScrollPane((Component)this.container);
/* 40 */     add((Component)this.messagePanel, (Component)this.scroll);
/*    */   }
/*    */   
/*    */   public EditorPanel() {
/* 44 */     this(smallSquareNoTopInsets);
/*    */   } protected void add(EditorPair pair) {
/*    */     ExtendedPanel panel;
/*    */     GridBagConstraints c;
/* 48 */     LocalizableLabel label = pair.getLabel();
/* 49 */     VPanel vPanel = pair.getPanel();
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 54 */     if (this.paneNum == this.panels.size()) {
/* 55 */       panel = new ExtendedPanel(new GridBagLayout());
/* 56 */       panel.getInsets().set(0, 0, 0, 0);
/*    */       
/* 58 */       c = new GridBagConstraints();
/* 59 */       c.fill = 2;
/*    */       
/* 61 */       this.container.add((Component)panel, (Component)del(0));
/*    */       
/* 63 */       this.panels.add(panel);
/* 64 */       this.constraints.add(c);
/*    */     } else {
/* 66 */       panel = this.panels.get(this.paneNum);
/* 67 */       c = this.constraints.get(this.paneNum);
/*    */     } 
/*    */     
/* 70 */     c.anchor = 17;
/* 71 */     c.gridy = this.rowNum;
/* 72 */     c.gridx = 0;
/* 73 */     c.weightx = 0.1D;
/* 74 */     panel.add((Component)label, c);
/*    */     
/* 76 */     c.anchor = 13;
/* 77 */     c.gridy = this.rowNum = (byte)(this.rowNum + 1);
/* 78 */     c.gridx = 1;
/* 79 */     c.weightx = 1.0D;
/* 80 */     panel.add((Component)vPanel, c);
/*    */     
/* 82 */     this.handlers.addAll(pair.getHandlers());
/*    */   }
/*    */   
/*    */   protected void nextPane() {
/* 86 */     this.rowNum = 0;
/* 87 */     this.paneNum = (byte)(this.paneNum + 1);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\editor\EditorPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */