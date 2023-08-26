/*    */ package org.tlauncher.tlauncher.ui.loc.modpack;
/*    */ 
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.awt.event.ActionListener;
/*    */ import net.minecraft.launcher.versions.CompleteVersion;
/*    */ import org.tlauncher.modpack.domain.client.GameEntityDTO;
/*    */ import org.tlauncher.modpack.domain.client.share.GameType;
/*    */ import org.tlauncher.modpack.domain.client.version.VersionDTO;
/*    */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*    */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*    */ import org.tlauncher.tlauncher.ui.modpack.filter.BaseModpackFilter;
/*    */ import org.tlauncher.tlauncher.ui.swing.box.ModpackComboBox;
/*    */ 
/*    */ 
/*    */ public class ModpackTableVersionButton
/*    */   extends TableActButton
/*    */ {
/*    */   private VersionDTO versionDTO;
/*    */   
/*    */   public ModpackTableVersionButton(final GameEntityDTO entity, final GameType type, final ModpackComboBox localmodpacks, final VersionDTO versionDTO, BaseModpackFilter<VersionDTO> filter) {
/* 21 */     super(entity, type, localmodpacks);
/* 22 */     this.versionDTO = versionDTO;
/* 23 */     this.filter = filter;
/* 24 */     initButton();
/* 25 */     this.installButton.addActionListener(new ActionListener()
/*    */         {
/*    */           public void actionPerformed(ActionEvent e) {
/* 28 */             ModpackTableVersionButton.this.manager.installEntity(entity, versionDTO, type, true);
/*    */           }
/*    */         });
/* 31 */     this.removeButton.addActionListener(new ActionListener()
/*    */         {
/*    */           public void actionPerformed(ActionEvent e) {
/* 34 */             ModpackTableVersionButton.this.manager.removeEntity(entity, versionDTO, type, false);
/*    */           }
/*    */         });
/* 37 */     this.deniedButton.addActionListener(new ActionListener()
/*    */         {
/*    */           public void actionPerformed(ActionEvent e) {
/* 40 */             if (Alert.showQuestion(Localizable.get("launcher.warning.title"), 
/* 41 */                 Localizable.get("modpack.filter.version.denied", new Object[] { ((CompleteVersion)this.val$localmodpacks.getSelectedValue()).getID() }))) {
/* 42 */               ModpackTableVersionButton.this.manager.installEntity(entity, versionDTO, type, true);
/*    */             }
/*    */           }
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void initButton() {
/* 52 */     if (findLocal() != null) {
/* 53 */       setTypeButton("REMOVE");
/* 54 */       if (this.type == GameType.MOD && !this.filter.isProper(this.versionDTO)) {
/* 55 */         this.firstDeniedState = true;
/*    */       }
/*    */       return;
/*    */     } 
/* 59 */     if (this.type == GameType.MODPACK) {
/* 60 */       setTypeButton("INSTALL");
/*    */       return;
/*    */     } 
/* 63 */     if (this.filter.isProper(this.versionDTO)) {
/* 64 */       setTypeButton("INSTALL");
/*    */     } else {
/* 66 */       setTypeButton("DENIED");
/* 67 */       this.firstDeniedState = true;
/*    */     } 
/*    */   }
/*    */   
/*    */   private GameEntityDTO findLocal() {
/* 72 */     for (GameEntityDTO e : getSelectedModpackData()) {
/* 73 */       if (this.entity.getId().equals(e.getId()) && e.getVersion().getId().equals(this.versionDTO.getId())) {
/* 74 */         return e;
/*    */       }
/*    */     } 
/* 77 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\loc\modpack\ModpackTableVersionButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */