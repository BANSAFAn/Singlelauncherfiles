/*    */ package org.tlauncher.tlauncher.ui.login.buttons;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.event.ActionEvent;
/*    */ import javax.swing.SwingUtilities;
/*    */ import org.tlauncher.tlauncher.managers.ModpackManager;
/*    */ import org.tlauncher.tlauncher.managers.VersionManager;
/*    */ import org.tlauncher.tlauncher.managers.VersionManagerListener;
/*    */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*    */ import org.tlauncher.tlauncher.ui.block.Blockable;
/*    */ import org.tlauncher.tlauncher.ui.block.Blocker;
/*    */ import org.tlauncher.tlauncher.ui.scenes.ModpackScene;
/*    */ import org.tlauncher.tlauncher.ui.scenes.PseudoScene;
/*    */ import org.tlauncher.util.U;
/*    */ import org.tlauncher.util.async.AsyncThread;
/*    */ 
/*    */ public class ModpackButton extends MainImageButton implements Blockable, VersionManagerListener {
/*    */   public ModpackButton(Color color, String image, String mouseUnderImage) {
/* 19 */     super(color, image, mouseUnderImage);
/* 20 */     addActionListener(e -> {
/*    */           ModpackScene scene = (TLauncher.getInstance().getFrame()).mp.modpackScene;
/*    */ 
/*    */ 
/*    */ 
/*    */           
/*    */           AsyncThread.execute(());
/*    */         });
/*    */ 
/*    */ 
/*    */     
/* 31 */     Blocker.block(this, Boolean.valueOf(false));
/* 32 */     ((VersionManager)TLauncher.getInstance().getManager().getComponent(VersionManager.class)).addListener(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void block(Object reason) {
/* 37 */     setEnabled(false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void unblock(Object reason) {
/* 42 */     setEnabled(true);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onVersionsRefreshing(VersionManager manager) {
/* 47 */     block(Boolean.valueOf(false));
/*    */   }
/*    */ 
/*    */   
/*    */   public void onVersionsRefreshingFailed(VersionManager manager) {
/* 52 */     unblock(Boolean.valueOf(true));
/*    */   }
/*    */ 
/*    */   
/*    */   public void onVersionsRefreshed(VersionManager manager) {
/* 57 */     unblock(Boolean.valueOf(true));
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\login\buttons\ModpackButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */