/*    */ package org.tlauncher.tlauncher.ui.block;
/*    */ 
/*    */ import javax.swing.JLayeredPane;
/*    */ 
/*    */ public class BlockableLayeredPane
/*    */   extends JLayeredPane implements Blockable {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public void block(Object reason) {
/* 10 */     Blocker.blockComponents(this, reason);
/*    */   }
/*    */ 
/*    */   
/*    */   public void unblock(Object reason) {
/* 15 */     Blocker.unblockComponents(this, reason);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\block\BlockableLayeredPane.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */