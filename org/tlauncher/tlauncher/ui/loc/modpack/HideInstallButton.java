/*    */ package org.tlauncher.tlauncher.ui.loc.modpack;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import org.tlauncher.modpack.domain.client.GameEntityDTO;
/*    */ import org.tlauncher.modpack.domain.client.share.GameType;
/*    */ import org.tlauncher.tlauncher.ui.loc.UpdaterFullButton;
/*    */ import org.tlauncher.tlauncher.ui.swing.box.ModpackComboBox;
/*    */ 
/*    */ public abstract class HideInstallButton
/*    */   extends UpdaterFullButton {
/*    */   private static final long serialVersionUID = -5663659688051961310L;
/* 12 */   public static final Color MOUSE_UNDER = new Color(95, 198, 255);
/* 13 */   public static final Color DEFAULT_BACKGROUND = new Color(63, 186, 255);
/*    */   
/*    */   protected ModpackComboBox localmodpacks;
/*    */   
/*    */   public HideInstallButton(String text, String image, ModpackComboBox localmodpacks, GameType type, GameEntityDTO entity) {
/* 18 */     super(DEFAULT_BACKGROUND, text, image);
/*    */     
/* 20 */     this.localmodpacks = localmodpacks;
/* 21 */     this.type = type;
/* 22 */     this.entity = entity;
/*    */   }
/*    */   protected GameType type; protected GameEntityDTO entity;
/*    */   
/*    */   public HideInstallButton(ModpackComboBox localmodpacks, GameType type, GameEntityDTO entity, String text, boolean remote, String image) {
/* 27 */     super(DEFAULT_BACKGROUND, "loginform.enter.install", image);
/* 28 */     this.localmodpacks = localmodpacks;
/* 29 */     this.type = type;
/* 30 */     this.entity = entity;
/*    */   }
/*    */   
/*    */   public abstract void init();
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\loc\modpack\HideInstallButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */