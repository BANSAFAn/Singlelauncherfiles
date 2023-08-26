/*     */ package org.tlauncher.tlauncher.ui.versions;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Image;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.util.List;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.JPopupMenu;
/*     */ import net.minecraft.launcher.updater.VersionSyncInfo;
/*     */ import org.tlauncher.tlauncher.managers.VersionManager;
/*     */ import org.tlauncher.tlauncher.ui.block.Blockable;
/*     */ import org.tlauncher.tlauncher.ui.images.ImageCache;
/*     */ import org.tlauncher.tlauncher.ui.loc.ImageUdaterButton;
/*     */ import org.tlauncher.tlauncher.ui.loc.LocalizableMenuItem;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VersionRefreshButton
/*     */   extends ImageUdaterButton
/*     */   implements VersionHandlerListener, Blockable
/*     */ {
/*     */   private static final long serialVersionUID = -7148657244927244061L;
/*     */   private static final String PREFIX = "version.manager.refresher.";
/*     */   private static final String MENU = "version.manager.refresher.menu.";
/*     */   final VersionHandler handler;
/*     */   private final JPopupMenu menu;
/*     */   private final LocalizableMenuItem local;
/*     */   private final LocalizableMenuItem remote;
/*     */   private ButtonState state;
/*     */   
/*     */   VersionRefreshButton(VersionList list) {
/*  33 */     super(GREEN_COLOR);
/*  34 */     this.handler = list.handler;
/*     */     
/*  36 */     this.menu = new JPopupMenu();
/*     */     
/*  38 */     this.local = new LocalizableMenuItem("version.manager.refresher.menu.local");
/*  39 */     this.local.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {
/*  42 */             VersionRefreshButton.this.handler.refresh();
/*     */           }
/*     */         });
/*  45 */     this.menu.add((JMenuItem)this.local);
/*     */     
/*  47 */     this.remote = new LocalizableMenuItem("version.manager.refresher.menu.remote");
/*  48 */     this.remote.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {
/*  51 */             VersionRefreshButton.this.handler.asyncRefresh();
/*     */           }
/*     */         });
/*  54 */     this.menu.add((JMenuItem)this.remote);
/*     */     
/*  56 */     addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {
/*  59 */             VersionRefreshButton.this.handler.asyncRefresh();
/*     */           }
/*     */         });
/*     */     
/*  63 */     setState(ButtonState.REFRESH);
/*  64 */     this.handler.addListener(this);
/*     */   }
/*     */   
/*     */   void onPressed() {
/*  68 */     switch (this.state) {
/*     */       case CANCEL:
/*  70 */         this.handler.stopRefresh();
/*     */         break;
/*     */       case REFRESH:
/*  73 */         this.menu.show((Component)this, 0, getHeight());
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setState(ButtonState state) {
/*  79 */     if (state == null) {
/*  80 */       throw new NullPointerException();
/*     */     }
/*  82 */     this.state = state;
/*  83 */     setImage(state.image);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onVersionRefreshing(VersionManager vm) {
/*  88 */     setState(ButtonState.CANCEL);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onVersionRefreshed(VersionManager vm) {
/*  93 */     setState(ButtonState.REFRESH);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onVersionSelected(List<VersionSyncInfo> versions) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void onVersionDeselected() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void onVersionDownload(List<VersionSyncInfo> list) {}
/*     */ 
/*     */   
/*     */   public void block(Object reason) {
/* 110 */     if (!reason.equals("refresh")) {
/* 111 */       setEnabled(false);
/*     */     }
/*     */   }
/*     */   
/*     */   public void unblock(Object reason) {
/* 116 */     setEnabled(true);
/*     */   }
/*     */   
/*     */   enum ButtonState {
/* 120 */     REFRESH("refresh.png"), CANCEL("cancel.png");
/*     */     
/*     */     final Image image;
/*     */     
/*     */     ButtonState(String image) {
/* 125 */       this.image = ImageCache.getImage(image);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\versions\VersionRefreshButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */