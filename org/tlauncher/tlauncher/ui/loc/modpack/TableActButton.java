/*    */ package org.tlauncher.tlauncher.ui.loc.modpack;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import javax.swing.JButton;
/*    */ import org.tlauncher.modpack.domain.client.GameEntityDTO;
/*    */ import org.tlauncher.modpack.domain.client.share.GameType;
/*    */ import org.tlauncher.modpack.domain.client.version.VersionDTO;
/*    */ import org.tlauncher.tlauncher.managers.ModpackManager;
/*    */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*    */ import org.tlauncher.tlauncher.ui.loc.ImageUdaterButton;
/*    */ import org.tlauncher.tlauncher.ui.modpack.filter.BaseModpackFilter;
/*    */ import org.tlauncher.tlauncher.ui.swing.box.ModpackComboBox;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class TableActButton
/*    */   extends ModpackActButton
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   protected JButton deniedButton;
/*    */   protected BaseModpackFilter<VersionDTO> filter;
/*    */   protected ModpackManager manager;
/*    */   
/*    */   public TableActButton(GameEntityDTO entity, GameType type, ModpackComboBox localmodpacks) {
/* 27 */     super(entity, type, localmodpacks);
/* 28 */     this.manager = (ModpackManager)TLauncher.getInjector().getInstance(ModpackManager.class);
/* 29 */     Color color = new Color(0, 0, 0, 0);
/* 30 */     this.installButton = (JButton)new ImageUdaterButton(color, "install-game-element.png");
/* 31 */     this.removeButton = (JButton)new ImageUdaterButton(color, "delete-game-element.png");
/* 32 */     this.processButton = (JButton)new ImageUdaterButton(color, "refresh.png");
/* 33 */     this.deniedButton = (JButton)new ImageUdaterButton(color, "modpack-element-denied.png");
/*    */     
/* 35 */     add(this.installButton, "INSTALL");
/* 36 */     add(this.removeButton, "REMOVE");
/* 37 */     add(this.processButton, "PROCESSING");
/* 38 */     add(this.deniedButton, "DENIED");
/*    */   }
/*    */   
/*    */   public abstract void initButton();
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\loc\modpack\TableActButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */