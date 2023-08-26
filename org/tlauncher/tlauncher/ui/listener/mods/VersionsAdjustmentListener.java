/*    */ package org.tlauncher.tlauncher.ui.listener.mods;
/*    */ 
/*    */ import javax.swing.JTable;
/*    */ import org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene;
/*    */ 
/*    */ public class VersionsAdjustmentListener extends ModpackAdjustmentListener {
/*    */   private CompleteSubEntityScene.FullGameEntity fullGameEntity;
/*    */   
/*    */   public VersionsAdjustmentListener(JTable gerp, CompleteSubEntityScene.FullGameEntity fullGameEntity) {
/* 10 */     super(gerp);
/* 11 */     this.fullGameEntity = fullGameEntity;
/*    */   }
/*    */ 
/*    */   
/*    */   public void processed() {
/* 16 */     if (!this.fullGameEntity.isProcessingRequest())
/* 17 */       this.fullGameEntity.fillVersions(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\listener\mods\VersionsAdjustmentListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */