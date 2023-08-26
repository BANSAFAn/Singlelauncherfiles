/*     */ package org.tlauncher.tlauncher.ui.login.buttons;
/*     */ 
/*     */ import java.awt.Image;
/*     */ import java.awt.event.ActionEvent;
/*     */ import org.tlauncher.tlauncher.managers.ComponentManager;
/*     */ import org.tlauncher.tlauncher.managers.ComponentManagerListener;
/*     */ import org.tlauncher.tlauncher.managers.ComponentManagerListenerHelper;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.block.Blockable;
/*     */ import org.tlauncher.tlauncher.ui.block.Blocker;
/*     */ import org.tlauncher.tlauncher.ui.images.ImageCache;
/*     */ import org.tlauncher.tlauncher.ui.login.LoginForm;
/*     */ import org.tlauncher.tlauncher.updater.client.Updater;
/*     */ import org.tlauncher.tlauncher.updater.client.UpdaterListener;
/*     */ 
/*     */ public class RefreshButton
/*     */   extends MainImageButton
/*     */   implements Blockable, ComponentManagerListener, UpdaterListener
/*     */ {
/*     */   private static final long serialVersionUID = -1334187593288746348L;
/*     */   private static final int TYPE_REFRESH = 0;
/*     */   private static final int TYPE_CANCEL = 1;
/*     */   private LoginForm lf;
/*     */   private int type;
/*  25 */   private final Image refresh = ImageCache.getImage("refresh-mouse-under.png"); private final Image cancel = ImageCache.getImage("cancel.png");
/*     */   private Updater updaterFlag;
/*     */   
/*     */   private RefreshButton(LoginForm loginform, int type) {
/*  29 */     super(DARK_GREEN_COLOR, "refresh-mouse-under.png", "refresh-gray.png");
/*  30 */     this.image = this.refresh;
/*  31 */     this.lf = loginform;
/*     */     
/*  33 */     setType(type, false);
/*     */     
/*  35 */     addActionListener(e -> onPressButton());
/*  36 */     ((ComponentManagerListenerHelper)TLauncher.getInstance().getManager().getComponent(ComponentManagerListenerHelper.class)).addListener(this);
/*  37 */     TLauncher.getInstance().getUpdater().addListener(this);
/*     */   }
/*     */   
/*     */   RefreshButton(LoginForm loginform) {
/*  41 */     this(loginform, 0);
/*     */   }
/*     */   
/*     */   private void onPressButton() {
/*  45 */     switch (this.type) {
/*     */       case 0:
/*  47 */         if (this.updaterFlag != null) {
/*  48 */           this.updaterFlag.asyncFindUpdate();
/*     */         }
/*  50 */         TLauncher.getInstance().getManager().startAsyncRefresh();
/*     */         break;
/*     */       case 1:
/*  53 */         TLauncher.getInstance().getManager().stopRefresh();
/*     */         break;
/*     */       default:
/*  56 */         throw new IllegalArgumentException("Unknown type: " + this.type + ". Use RefreshButton.TYPE_* constants.");
/*     */     } 
/*     */     
/*  59 */     this.lf.defocus();
/*     */   }
/*     */   
/*     */   void setType(int type) {
/*  63 */     setType(type, true);
/*     */   }
/*     */ 
/*     */   
/*     */   private void setType(int type, boolean repaint) {
/*     */     Image image;
/*  69 */     switch (type) {
/*     */       case 0:
/*  71 */         image = this.refresh;
/*     */         break;
/*     */       case 1:
/*  74 */         image = this.cancel;
/*     */         break;
/*     */       default:
/*  77 */         throw new IllegalArgumentException("Unknown type: " + type + ". Use RefreshButton.TYPE_* constants.");
/*     */     } 
/*     */     
/*  80 */     this.type = type;
/*  81 */     this.image = image;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onUpdaterRequesting(Updater u) {}
/*     */ 
/*     */   
/*     */   public void onUpdaterErrored(Updater.SearchFailed failed) {
/*  90 */     this.updaterFlag = failed.getUpdater();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdaterSucceeded(Updater.SearchSucceeded succeeded) {
/*  95 */     this.updaterFlag = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onComponentsRefreshing(ComponentManager manager) {
/* 100 */     Blocker.block(this, "refresh");
/*     */   }
/*     */ 
/*     */   
/*     */   public void onComponentsRefreshed(ComponentManager manager) {
/* 105 */     Blocker.unblock(this, "refresh");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void block(Object reason) {
/* 112 */     if (reason.equals("refresh")) {
/* 113 */       setType(1);
/*     */     } else {
/* 115 */       setEnabled(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void unblock(Object reason) {
/* 120 */     if (reason.equals("refresh"))
/* 121 */       setType(0); 
/* 122 */     setEnabled(true);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\login\buttons\RefreshButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */