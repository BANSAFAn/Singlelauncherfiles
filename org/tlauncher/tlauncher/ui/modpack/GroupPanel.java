/*    */ package org.tlauncher.tlauncher.ui.modpack;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import java.awt.GridBagConstraints;
/*    */ import javax.swing.AbstractButton;
/*    */ import javax.swing.ButtonGroup;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GroupPanel
/*    */   extends ShadowPanel
/*    */ {
/*    */   private static final long serialVersionUID = -408137826817737500L;
/* 14 */   private GridBagConstraints gbc = new GridBagConstraints();
/*    */   private ButtonGroup group;
/*    */   
/* 17 */   public GroupPanel(int colorStarted) { super(colorStarted);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 25 */     this.group = new ButtonGroup(); this.gbc.fill = 1; this.gbc.gridy = this.gbc.gridx = 0;
/*    */     this.gbc.weightx = 1.0D;
/*    */     this.gbc.weighty = 1.0D;
/* 28 */     this.gbc.anchor = 10; } public void addInGroup(AbstractButton button, int index) { this.gbc.gridx = index;
/* 29 */     this.group.add(button);
/* 30 */     add(button, this.gbc); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Component add(Component comp, int index) {
/* 36 */     this.gbc.gridx = index;
/* 37 */     add(comp, this.gbc);
/* 38 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\modpack\GroupPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */