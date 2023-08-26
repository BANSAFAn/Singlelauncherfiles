/*    */ package org.tlauncher.tlauncher.ui.login.buttons;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Component;
/*    */ import java.awt.GridLayout;
/*    */ import org.tlauncher.tlauncher.ui.block.Blockable;
/*    */ import org.tlauncher.tlauncher.ui.block.Blocker;
/*    */ import org.tlauncher.tlauncher.ui.login.LoginForm;
/*    */ import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
/*    */ 
/*    */ public class ButtonPanel extends ExtendedPanel implements Blockable {
/* 12 */   public final Color mouseUnderButton = new Color(82, 127, 53);
/*    */   
/*    */   public final LoginForm loginForm;
/*    */   
/*    */   public final RefreshButton refresh;
/*    */   
/*    */   public final FolderButton folder;
/*    */   
/*    */   public final SettingsButton settings;
/*    */   private final ModpackButton mainImageButton;
/*    */   
/*    */   public ButtonPanel(LoginForm lf) {
/* 24 */     this.loginForm = lf;
/* 25 */     setLayout(new GridLayout(0, 4));
/*    */     
/* 27 */     this.mainImageButton = new ModpackButton(new Color(113, 169, 76), "modpack-tl-new.png", "modpack-tl-active-new.png");
/* 28 */     add((Component)this.mainImageButton);
/*    */     
/* 30 */     this.refresh = new RefreshButton(lf);
/* 31 */     add((Component)this.refresh);
/*    */     
/* 33 */     this.folder = new FolderButton(lf);
/* 34 */     add((Component)this.folder);
/*    */     
/* 36 */     this.settings = new SettingsButton(lf);
/* 37 */     add((Component)this.settings);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void block(Object reason) {
/* 43 */     Blocker.block(this.mainImageButton, "update");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void unblock(Object reason) {
/* 49 */     Blocker.unblock(this.mainImageButton, "update");
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\login\buttons\ButtonPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */