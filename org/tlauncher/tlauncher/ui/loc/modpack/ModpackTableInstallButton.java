/*    */ package org.tlauncher.tlauncher.ui.loc.modpack;
/*    */ 
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.awt.event.ActionListener;
/*    */ import java.io.IOException;
/*    */ import net.minecraft.launcher.versions.CompleteVersion;
/*    */ import org.tlauncher.modpack.domain.client.GameEntityDTO;
/*    */ import org.tlauncher.modpack.domain.client.GameVersionDTO;
/*    */ import org.tlauncher.modpack.domain.client.share.GameType;
/*    */ import org.tlauncher.modpack.domain.client.version.ModpackVersionDTO;
/*    */ import org.tlauncher.modpack.domain.client.version.VersionDTO;
/*    */ import org.tlauncher.tlauncher.managers.ModpackManager;
/*    */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*    */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*    */ import org.tlauncher.tlauncher.ui.swing.box.ModpackComboBox;
/*    */ import org.tlauncher.util.U;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ModpackTableInstallButton
/*    */   extends TableActButton
/*    */ {
/*    */   private VersionDTO selectedVersion;
/* 24 */   private ModpackManager manager = (ModpackManager)TLauncher.getInjector().getInstance(ModpackManager.class);
/*    */ 
/*    */   
/*    */   public ModpackTableInstallButton(GameEntityDTO entity, final GameType type, ModpackComboBox localmodpacks) {
/* 28 */     super(entity, type, localmodpacks);
/* 29 */     this.installButton.addActionListener(new ActionListener()
/*    */         {
/*    */           public void actionPerformed(ActionEvent e) {
/* 32 */             ModpackTableInstallButton.this.manager.installEntity(ModpackTableInstallButton.this.getEntity(), ModpackTableInstallButton.this.selectedVersion, type, true);
/*    */           }
/*    */         });
/* 35 */     this.removeButton.addActionListener(e -> this.manager.removeEntity(entity, this.selectedVersion, type));
/*    */ 
/*    */     
/* 38 */     this.deniedButton.addActionListener(new ActionListener()
/*    */         {
/*    */           public void actionPerformed(ActionEvent e) {
/* 41 */             Alert.showLocMessageWithoutTitle("modpack.table.denied.button");
/*    */           }
/*    */         });
/*    */   }
/*    */ 
/*    */   
/*    */   public void initButton() {
/* 48 */     for (GameEntityDTO e : getSelectedModpackData()) {
/* 49 */       if (this.entity.getId().equals(e.getId())) {
/* 50 */         this.selectedVersion = e.getVersion();
/* 51 */         setTypeButton("REMOVE");
/*    */         return;
/*    */       } 
/*    */     } 
/* 55 */     if (!GameType.MODPACK.equals(this.type)) {
/* 56 */       if (this.localmodpacks.getSelectedIndex() > 0) {
/*    */         
/* 58 */         ModpackVersionDTO modpackVersionDTO = (ModpackVersionDTO)((CompleteVersion)this.localmodpacks.getSelectedItem()).getModpack().getVersion();
/*    */         
/*    */         try {
/* 61 */           GameVersionDTO gv = this.manager.getGameVersion(modpackVersionDTO);
/* 62 */           if (this.entity.getVersion().getGameVersionsDTO().contains(gv)) {
/* 63 */             setTypeButton("INSTALL");
/*    */             return;
/*    */           } 
/* 66 */         } catch (IOException e1) {
/* 67 */           U.log(new Object[] { e1 });
/*    */         } 
/*    */       } 
/* 70 */       setTypeButton("DENIED");
/*    */     } else {
/* 72 */       setTypeButton("INSTALL");
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\loc\modpack\ModpackTableInstallButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */