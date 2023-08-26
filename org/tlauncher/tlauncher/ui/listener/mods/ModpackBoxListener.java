/*    */ package org.tlauncher.tlauncher.ui.listener.mods;
/*    */ 
/*    */ import java.awt.event.ItemEvent;
/*    */ import java.awt.event.ItemListener;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.launcher.updater.VersionSyncInfo;
/*    */ import net.minecraft.launcher.versions.CompleteVersion;
/*    */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*    */ import org.tlauncher.tlauncher.ui.login.VersionComboBox;
/*    */ import org.tlauncher.tlauncher.ui.swing.box.ModpackComboBox;
/*    */ 
/*    */ public class ModpackBoxListener
/*    */   implements ItemListener
/*    */ {
/*    */   public void itemStateChanged(ItemEvent e) {
/* 16 */     if (1 == e.getStateChange()) {
/* 17 */       CompleteVersion completeVersion = (CompleteVersion)((ModpackComboBox)e.getSource()).getSelectedValue();
/* 18 */       VersionComboBox versions = (TLauncher.getInstance().getFrame()).mp.defaultScene.loginForm.versions;
/* 19 */       for (int i = 0; i < versions.getModel().getSize(); i++) {
/* 20 */         VersionSyncInfo vsi = versions.getModel().getElementAt(i);
/* 21 */         if (Objects.nonNull(vsi) && Objects.equals(vsi.getID(), completeVersion.getID())) {
/* 22 */           versions.getModel().setSelectedItem(vsi);
/*    */           return;
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\listener\mods\ModpackBoxListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */