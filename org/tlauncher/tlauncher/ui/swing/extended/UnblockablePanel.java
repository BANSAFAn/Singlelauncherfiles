/*    */ package org.tlauncher.tlauncher.ui.swing.extended;
/*    */ 
/*    */ import java.awt.LayoutManager;
/*    */ import org.tlauncher.tlauncher.ui.block.Unblockable;
/*    */ 
/*    */ public class UnblockablePanel
/*    */   extends ExtendedPanel implements Unblockable {
/*    */   private static final long serialVersionUID = -5273727580864479391L;
/*    */   
/*    */   public UnblockablePanel(LayoutManager layout, boolean isDoubleBuffered) {
/* 11 */     super(layout, isDoubleBuffered);
/*    */   }
/*    */   
/*    */   public UnblockablePanel(LayoutManager layout) {
/* 15 */     super(layout);
/*    */   }
/*    */   
/*    */   public UnblockablePanel(boolean isDoubleBuffered) {
/* 19 */     super(isDoubleBuffered);
/*    */   }
/*    */   
/*    */   public UnblockablePanel() {}
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\extended\UnblockablePanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */