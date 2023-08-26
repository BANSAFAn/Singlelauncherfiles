/*    */ package org.tlauncher.tlauncher.ui.login.buttons;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Component;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.event.ActionEvent;
/*    */ import javax.swing.JMenuItem;
/*    */ import javax.swing.JPopupMenu;
/*    */ import javax.swing.SwingUtilities;
/*    */ import org.tlauncher.tlauncher.configuration.Configuration;
/*    */ import org.tlauncher.tlauncher.entity.ConfigEnum;
/*    */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*    */ import org.tlauncher.tlauncher.ui.block.Blockable;
/*    */ import org.tlauncher.tlauncher.ui.block.Blocker;
/*    */ import org.tlauncher.tlauncher.ui.images.ImageCache;
/*    */ import org.tlauncher.tlauncher.ui.loc.LocalizableMenuItem;
/*    */ import org.tlauncher.tlauncher.ui.login.LoginForm;
/*    */ import org.tlauncher.tlauncher.ui.scenes.PseudoScene;
/*    */ 
/*    */ public class SettingsButton extends MainImageButton implements Blockable {
/*    */   private static final long serialVersionUID = 1321382157134544911L;
/*    */   private final LoginForm lf;
/*    */   
/*    */   SettingsButton(LoginForm loginform) {
/* 25 */     super(DARK_GREEN_COLOR, "settings-mouse-under.png", "settings.png");
/*    */     
/* 27 */     this.lf = loginform;
/*    */     
/* 29 */     this.image = ImageCache.getImage("settings-mouse-under.png");
/*    */     
/* 31 */     this.popup = new JPopupMenu();
/*    */     
/* 33 */     this.settings = new LocalizableMenuItem("loginform.button.settings.launcher");
/* 34 */     this.settings.addActionListener(e -> this.lf.pane.setScene((PseudoScene)this.lf.pane.settingsScene));
/* 35 */     this.popup.add((JMenuItem)this.settings);
/*    */     
/* 37 */     this.versionManager = new LocalizableMenuItem("loginform.button.settings.version");
/* 38 */     this.versionManager.addActionListener(e -> {
/*    */           if (this.lf.scene.settingsForm.isVisible()) {
/*    */             this.lf.scene.setSidePanel(null);
/*    */           }
/*    */           
/*    */           this.lf.pane.setScene((PseudoScene)this.lf.pane.versionManager);
/*    */         });
/* 45 */     this.popup.add((JMenuItem)this.versionManager);
/* 46 */     Configuration c = TLauncher.getInstance().getConfiguration();
/* 47 */     if (c.getBoolean(ConfigEnum.UPDATER_LAUNCHER)) {
/* 48 */       LocalizableMenuItem updater = new LocalizableMenuItem("updater.update.now");
/* 49 */       updater.setForeground(Color.RED);
/* 50 */       this.popup.add((JMenuItem)updater);
/* 51 */       updater.addActionListener(l -> {
/*    */             c.set("updater.delay", Integer.valueOf(0));
/*    */             
/*    */             TLauncher.getInstance().getUpdater().asyncFindUpdate();
/*    */           });
/*    */     } 
/* 57 */     setPreferredSize(new Dimension(30, getHeight()));
/* 58 */     addActionListener(e -> callPopup());
/*    */   }
/*    */   private final JPopupMenu popup; private final LocalizableMenuItem versionManager; private final LocalizableMenuItem settings;
/*    */   void callPopup() {
/* 62 */     SwingUtilities.invokeLater(() -> {
/*    */           this.lf.defocus();
/*    */           this.popup.show((Component)this, 0, getHeight());
/*    */         });
/*    */   }
/*    */ 
/*    */   
/*    */   public void block(Object reason) {
/* 70 */     if (reason.equals("auth") || reason.equals("launch")) {
/* 71 */       Blocker.blockComponents(reason, new Component[] { (Component)this.versionManager });
/*    */     }
/*    */   }
/*    */   
/*    */   public void unblock(Object reason) {
/* 76 */     Blocker.unblockComponents(reason, new Component[] { (Component)this.versionManager });
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\login\buttons\SettingsButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */