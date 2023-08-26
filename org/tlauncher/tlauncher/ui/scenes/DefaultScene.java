/*     */ package org.tlauncher.tlauncher.ui.scenes;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import javax.swing.JLayeredPane;
/*     */ import org.tlauncher.tlauncher.ui.MainPane;
/*     */ import org.tlauncher.tlauncher.ui.SideNotifier;
/*     */ import org.tlauncher.tlauncher.ui.login.LoginForm;
/*     */ import org.tlauncher.tlauncher.ui.login.LoginFormHelper;
/*     */ import org.tlauncher.tlauncher.ui.menu.PopupMenuView;
/*     */ import org.tlauncher.tlauncher.ui.settings.SettingsPanel;
/*     */ import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
/*     */ 
/*     */ public class DefaultScene
/*     */   extends PseudoScene {
/*  16 */   public static final Dimension SETTINGS_SIZE = new Dimension(700, 550);
/*     */ 
/*     */   
/*     */   public static final int EDGE = 7;
/*     */ 
/*     */   
/*     */   public final SideNotifier notifier;
/*     */   
/*     */   public final LoginForm loginForm;
/*     */   
/*     */   public final LoginFormHelper loginFormHelper;
/*     */   
/*     */   public final SettingsPanel settingsForm;
/*     */   
/*     */   private SidePanel sidePanel;
/*     */   
/*     */   private ExtendedPanel sidePanelComp;
/*     */   
/*     */   private PopupMenuView popupMenuView;
/*     */ 
/*     */   
/*     */   public DefaultScene(MainPane main) {
/*  38 */     super(main);
/*     */     
/*  40 */     this.notifier = main.notifier;
/*     */     
/*  42 */     this.settingsForm = new SettingsPanel(this);
/*  43 */     this.settingsForm.setSize(SETTINGS_SIZE);
/*  44 */     this.settingsForm.setVisible(false);
/*  45 */     add((Component)this.settingsForm);
/*     */     
/*  47 */     this.loginForm = new LoginForm(this);
/*  48 */     this.popupMenuView = new PopupMenuView((JLayeredPane)this);
/*  49 */     add((Component)this.popupMenuView);
/*     */ 
/*     */     
/*  52 */     add((Component)this.loginForm);
/*     */     
/*  54 */     this.loginFormHelper = new LoginFormHelper(this);
/*  55 */     add((Component)this.loginFormHelper);
/*     */   }
/*     */   
/*     */   public ExtendedPanel getSidePanelComp() {
/*  59 */     return this.sidePanelComp;
/*     */   }
/*     */   
/*     */   public void setSidePanelComp(ExtendedPanel sidePanelComp) {
/*  63 */     this.sidePanelComp = sidePanelComp;
/*     */   }
/*     */   
/*     */   public PopupMenuView getPopupMenuView() {
/*  67 */     return this.popupMenuView;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onResize() {
/*  72 */     if (this.parent == null) {
/*     */       return;
/*     */     }
/*  75 */     setBounds(0, 0, this.parent.getWidth(), this.parent.getHeight());
/*     */ 
/*     */     
/*  78 */     updateCoords();
/*     */     
/*  80 */     this.loginFormHelper.onResize();
/*     */   }
/*     */   
/*     */   private void updateCoords() {
/*  84 */     int w = getWidth(), h = getHeight(), hw = w / 2;
/*  85 */     int y = h - this.loginForm.getHeight();
/*  86 */     this.loginForm.setLocation(0, y);
/*     */     
/*  88 */     if (this.sidePanel != null) {
/*  89 */       this.sidePanelComp.setLocation(hw - this.sidePanelComp.getWidth() / 2, 7);
/*     */     }
/*     */   }
/*     */   
/*     */   public SidePanel getSidePanel() {
/*  94 */     return this.sidePanel;
/*     */   }
/*     */   
/*     */   public void setSidePanel(SidePanel side) {
/*  98 */     if (this.sidePanel == side) {
/*     */       return;
/*     */     }
/* 101 */     boolean noSidePanel = (side == null);
/*     */     
/* 103 */     if (this.sidePanelComp != null) {
/* 104 */       this.sidePanelComp.setVisible(false);
/*     */     }
/* 106 */     this.sidePanel = side;
/* 107 */     this.sidePanelComp = noSidePanel ? null : getSidePanelComp(side);
/*     */     
/* 109 */     if (!noSidePanel) {
/* 110 */       this.sidePanelComp.setVisible(true);
/*     */     }
/* 112 */     (getMainPane()).browser.setBrowserContentShown("settings", (side == null));
/* 113 */     updateCoords();
/*     */   }
/*     */   
/*     */   public void toggleSidePanel(SidePanel side) {
/* 117 */     if (this.sidePanel == side)
/* 118 */       side = null; 
/* 119 */     setSidePanel(side);
/*     */   }
/*     */   
/*     */   public ExtendedPanel getSidePanelComp(SidePanel side) {
/* 123 */     if (side == null) {
/* 124 */       throw new NullPointerException("side");
/*     */     }
/* 126 */     switch (side) {
/*     */       case SETTINGS:
/* 128 */         return (ExtendedPanel)this.settingsForm;
/*     */     } 
/* 130 */     throw new RuntimeException("unknown side:" + side);
/*     */   }
/*     */   
/*     */   public enum SidePanel
/*     */   {
/* 135 */     SETTINGS;
/*     */     
/*     */     public final boolean requiresShow;
/*     */     
/*     */     SidePanel(boolean requiresShow) {
/* 140 */       this.requiresShow = requiresShow;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\scenes\DefaultScene.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */