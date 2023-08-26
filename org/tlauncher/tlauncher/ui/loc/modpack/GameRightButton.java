/*    */ package org.tlauncher.tlauncher.ui.loc.modpack;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.awt.event.MouseListener;
/*    */ import javax.swing.JButton;
/*    */ import org.tlauncher.modpack.domain.client.GameEntityDTO;
/*    */ import org.tlauncher.modpack.domain.client.share.GameType;
/*    */ import org.tlauncher.tlauncher.managers.ModpackManager;
/*    */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*    */ import org.tlauncher.tlauncher.ui.loc.RoundUpdaterButton;
/*    */ import org.tlauncher.tlauncher.ui.modpack.filter.InstallGameEntityFilter;
/*    */ import org.tlauncher.tlauncher.ui.swing.box.ModpackComboBox;
/*    */ import org.tlauncher.util.SwingUtil;
/*    */ import org.tlauncher.util.swing.FontTL;
/*    */ 
/*    */ 
/*    */ public abstract class GameRightButton
/*    */   extends ModpackActButton
/*    */ {
/*    */   InstallGameEntityFilter installGameEntityFilter;
/*    */   
/*    */   public GameRightButton(GameEntityDTO entity, GameType type, ModpackComboBox localmodpacks) {
/* 24 */     super(entity, type, localmodpacks);
/*    */     class Button
/*    */       extends RoundUpdaterButton {
/*    */       public Button(String value) {
/* 28 */         super(Color.WHITE, new Color(0, 183, 81), new Color(0, 222, 99), value);
/*    */       }
/*    */     };
/*    */     
/* 32 */     this.installButton = (JButton)new Button("loginform.enter.install");
/*    */     
/* 34 */     this.removeButton = (JButton)new Button(this, "modpack.remove.button");
/* 35 */     this.processButton = (JButton)new Button(this, "modpack.process.button");
/* 36 */     this.installGameEntityFilter = new InstallGameEntityFilter(localmodpacks, type, new org.tlauncher.tlauncher.ui.modpack.filter.Filter[0]);
/* 37 */     add(this.installButton, "INSTALL");
/* 38 */     add(this.removeButton, "REMOVE");
/* 39 */     add(this.processButton, "PROCESSING");
/* 40 */     SwingUtil.changeFontFamily(this.installButton, FontTL.ROBOTO_REGULAR, 12);
/* 41 */     SwingUtil.changeFontFamily(this.removeButton, FontTL.ROBOTO_REGULAR, 12);
/* 42 */     SwingUtil.changeFontFamily(this.processButton, FontTL.ROBOTO_REGULAR, 12);
/*    */     
/* 44 */     initButton();
/* 45 */     ModpackManager manager = (ModpackManager)TLauncher.getInjector().getInstance(ModpackManager.class);
/* 46 */     this.removeButton.addActionListener(e -> manager.removeEntity(getEntity(), entity.getVersion(), getType()));
/* 47 */     this.installButton.addActionListener(e -> manager.installEntity(this.entity, null, getType(), true));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void initButton() {
/* 55 */     setTypeButton("INSTALL");
/* 56 */     getSelectedModpackData().stream().filter(e -> this.entity.getId().equals(e.getId())).findFirst().ifPresent(e -> {
/*    */           setVisible(false);
/*    */           setTypeButton("REMOVE");
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public synchronized void addMouseListener(MouseListener l) {
/* 65 */     if (l instanceof org.tlauncher.tlauncher.ui.listener.BlockClickListener) {
/*    */       return;
/*    */     }
/* 68 */     super.addMouseListener(l);
/*    */   }
/*    */   
/*    */   public abstract void updateRow();
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\loc\modpack\GameRightButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */