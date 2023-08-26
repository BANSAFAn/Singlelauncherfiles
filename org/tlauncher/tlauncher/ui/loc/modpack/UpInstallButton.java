/*    */ package org.tlauncher.tlauncher.ui.loc.modpack;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.event.MouseAdapter;
/*    */ import java.awt.event.MouseEvent;
/*    */ import java.util.List;
/*    */ import net.minecraft.launcher.versions.CompleteVersion;
/*    */ import org.tlauncher.modpack.domain.client.GameEntityDTO;
/*    */ import org.tlauncher.modpack.domain.client.ModpackDTO;
/*    */ import org.tlauncher.modpack.domain.client.share.GameType;
/*    */ import org.tlauncher.modpack.domain.client.version.ModpackVersionDTO;
/*    */ import org.tlauncher.tlauncher.managers.ModpackManager;
/*    */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*    */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*    */ import org.tlauncher.tlauncher.ui.swing.box.ModpackComboBox;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UpInstallButton
/*    */   extends HideInstallButton
/*    */ {
/*    */   private ModpackManager manager;
/*    */   
/*    */   public UpInstallButton(final GameEntityDTO entity, final GameType type, ModpackComboBox localmodpacks) {
/* 25 */     super(Localizable.get("loginform.enter.install").toUpperCase(), "up-install.png", localmodpacks, type, entity);
/* 26 */     this.manager = (ModpackManager)TLauncher.getInjector().getInstance(ModpackManager.class);
/* 27 */     setBackground(DEFAULT_BACKGROUND);
/* 28 */     setHorizontalTextPosition(4);
/* 29 */     setForeground(Color.WHITE);
/*    */     
/* 31 */     addMouseListener(new MouseAdapter()
/*    */         {
/*    */           public void mouseEntered(MouseEvent e) {
/* 34 */             UpInstallButton.this.setBackground(HideInstallButton.MOUSE_UNDER);
/*    */           }
/*    */ 
/*    */           
/*    */           public void mouseExited(MouseEvent e) {
/* 39 */             UpInstallButton.this.setBackground(HideInstallButton.DEFAULT_BACKGROUND);
/*    */           }
/*    */ 
/*    */           
/*    */           public void mousePressed(MouseEvent e) {
/* 44 */             UpInstallButton.this.setVisible(false);
/* 45 */             UpInstallButton.this.manager.installEntity(entity, null, type, true);
/*    */           }
/*    */         });
/* 48 */     init();
/*    */   }
/*    */ 
/*    */   
/*    */   public void init() {
/* 53 */     switch (this.type) {
/*    */       case MODPACK:
/* 55 */         modpackInit();
/*    */         return;
/*    */     } 
/* 58 */     if (this.localmodpacks.getSelectedIndex() > 0) {
/* 59 */       for (GameEntityDTO m : ((ModpackVersionDTO)((CompleteVersion)this.localmodpacks.getSelectedValue())
/* 60 */         .getModpack().getVersion()).getByType(this.type)) {
/* 61 */         if (this.entity.getId().equals(m.getId())) {
/* 62 */           setVisible(false);
/*    */           return;
/*    */         } 
/*    */       } 
/*    */     }
/* 67 */     setVisible(true);
/*    */   }
/*    */   
/*    */   private void modpackInit() {
/* 71 */     List<ModpackDTO> list = this.localmodpacks.getModpacks();
/* 72 */     for (ModpackDTO m : list) {
/* 73 */       if (this.entity.getId().equals(m.getId())) {
/* 74 */         setVisible(false);
/*    */         return;
/*    */       } 
/*    */     } 
/* 78 */     setVisible(true);
/*    */   }
/*    */   
/*    */   public void installEntity(GameEntityDTO e, GameType type) {
/* 82 */     if (this.entity.getId().equals(e.getId())) {
/* 83 */       setVisible(false);
/*    */     }
/*    */   }
/*    */   
/*    */   public void removeEntity(GameEntityDTO e) {
/* 88 */     init();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\loc\modpack\UpInstallButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */