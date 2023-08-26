/*    */ package org.tlauncher.tlauncher.ui.block;
/*    */ 
/*    */ import java.awt.Container;
/*    */ import java.awt.LayoutManager;
/*    */ import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BlockablePanel
/*    */   extends ExtendedPanel
/*    */   implements Blockable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public BlockablePanel(LayoutManager layout, boolean isDoubleBuffered) {
/* 16 */     super(layout, isDoubleBuffered);
/*    */   }
/*    */   
/*    */   public BlockablePanel(LayoutManager layout) {
/* 20 */     super(layout);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public BlockablePanel() {}
/*    */ 
/*    */   
/*    */   public void block(Object reason) {
/* 29 */     setEnabled(false);
/* 30 */     Blocker.blockComponents((Container)this, reason);
/*    */   }
/*    */ 
/*    */   
/*    */   public void unblock(Object reason) {
/* 35 */     setEnabled(true);
/* 36 */     Blocker.unblockComponents((Container)this, reason);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\block\BlockablePanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */