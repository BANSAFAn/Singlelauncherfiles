/*    */ package org.tlauncher.tlauncher.ui.listener.mods;
/*    */ 
/*    */ import javax.swing.JTable;
/*    */ import org.tlauncher.tlauncher.ui.modpack.right.panel.GameEntityRightPanel;
/*    */ import org.tlauncher.tlauncher.ui.scenes.ModpackScene;
/*    */ 
/*    */ public class RightPanelAdjustmentListener
/*    */   extends ModpackAdjustmentListener {
/*    */   public RightPanelAdjustmentListener(GameEntityRightPanel gerp, ModpackScene scene) {
/* 10 */     super((JTable)gerp);
/* 11 */     this.scene = scene;
/*    */   }
/*    */   private ModpackScene scene;
/*    */   
/*    */   public void processed() {
/* 16 */     GameEntityRightPanel gerp = (GameEntityRightPanel)getTable();
/* 17 */     if (!gerp.isProcessingRequest())
/* 18 */       this.scene.fillGameEntitiesPanel(false); 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\listener\mods\RightPanelAdjustmentListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */