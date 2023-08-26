/*     */ package org.tlauncher.tlauncher.ui.versions;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Image;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.JPopupMenu;
/*     */ import net.minecraft.launcher.updater.VersionSyncInfo;
/*     */ import org.tlauncher.tlauncher.downloader.DownloadableContainer;
/*     */ import org.tlauncher.tlauncher.managers.VersionManager;
/*     */ import org.tlauncher.tlauncher.managers.VersionSyncInfoContainer;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*     */ import org.tlauncher.tlauncher.ui.block.Blockable;
/*     */ import org.tlauncher.tlauncher.ui.block.Unblockable;
/*     */ import org.tlauncher.tlauncher.ui.images.ImageCache;
/*     */ import org.tlauncher.tlauncher.ui.loc.ImageUdaterButton;
/*     */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*     */ import org.tlauncher.tlauncher.ui.loc.LocalizableMenuItem;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VersionDownloadButton
/*     */   extends ImageUdaterButton
/*     */   implements VersionHandlerListener, Unblockable
/*     */ {
/*     */   private static final String SELECTION_BLOCK = "selection";
/*     */   private static final String PREFIX = "version.manager.downloader.";
/*     */   private static final String WARNING = "version.manager.downloader.warning.";
/*     */   private static final String WARNING_TITLE = "version.manager.downloader.warning.title";
/*     */   private static final String WARNING_FORCE = "version.manager.downloader.warning.force.";
/*     */   private static final String ERROR = "version.manager.downloader.error.";
/*     */   private static final String ERROR_TITLE = "version.manager.downloader.error.title";
/*     */   private static final String INFO = "version.manager.downloader.info.";
/*     */   private static final String INFO_TITLE = "version.manager.downloader.info.title";
/*     */   private static final String MENU = "version.manager.downloader.menu.";
/*     */   final VersionHandler handler;
/*     */   final Blockable blockable;
/*     */   private final JPopupMenu menu;
/*     */   private final LocalizableMenuItem ordinary;
/*     */   private final LocalizableMenuItem force;
/*     */   private ButtonState state;
/*     */   private boolean downloading;
/*     */   private boolean aborted;
/*     */   boolean forceDownload;
/*     */   
/*     */   VersionDownloadButton(VersionList list) {
/*  54 */     super(Color.WHITE);
/*  55 */     this.handler = list.handler;
/*  56 */     this.blockable = new Blockable()
/*     */       {
/*     */         public void block(Object reason) {
/*  59 */           VersionDownloadButton.this.setEnabled(false);
/*     */         }
/*     */ 
/*     */         
/*     */         public void unblock(Object reason) {
/*  64 */           VersionDownloadButton.this.setEnabled(true);
/*     */         }
/*     */       };
/*     */     
/*  68 */     this.menu = new JPopupMenu();
/*     */     
/*  70 */     this.ordinary = new LocalizableMenuItem("version.manager.downloader.menu.ordinary");
/*  71 */     this.ordinary.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {
/*  74 */             VersionDownloadButton.this.forceDownload = false;
/*  75 */             VersionDownloadButton.this.onDownloadCalled();
/*     */           }
/*     */         });
/*  78 */     this.menu.add((JMenuItem)this.ordinary);
/*     */     
/*  80 */     this.force = new LocalizableMenuItem("version.manager.downloader.menu.force");
/*  81 */     this.force.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {}
/*     */         });
/*     */ 
/*     */     
/*  87 */     this.menu.add((JMenuItem)this.force);
/*     */     
/*  89 */     addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {
/*  92 */             VersionDownloadButton.this.onPressed();
/*     */           }
/*     */         });
/*     */     
/*  96 */     setState(ButtonState.DOWNLOAD);
/*  97 */     this.handler.addListener(this);
/*     */   }
/*     */   
/*     */   void setState(ButtonState state) {
/* 101 */     if (state == null) {
/* 102 */       throw new NullPointerException();
/*     */     }
/* 104 */     this.state = state;
/* 105 */     setImage(state.image);
/*     */   }
/*     */   
/*     */   void onPressed() {
/* 109 */     switch (this.state) {
/*     */       case DOWNLOAD:
/* 111 */         this.forceDownload = true;
/* 112 */         onDownloadCalled();
/*     */         break;
/*     */       case STOP:
/* 115 */         onStopCalled();
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   void onDownloadPressed() {
/* 121 */     this.menu.show((Component)this, 0, getHeight());
/*     */   }
/*     */   
/*     */   void onDownloadCalled() {
/* 125 */     if (this.state != ButtonState.DOWNLOAD) {
/* 126 */       throw new IllegalStateException();
/*     */     }
/* 128 */     this.handler.thread.startThread.iterate();
/*     */   }
/*     */   
/*     */   void onStopCalled() {
/* 132 */     if (this.state != ButtonState.STOP) {
/* 133 */       throw new IllegalStateException();
/*     */     }
/* 135 */     this.handler.thread.stopThread.iterate();
/*     */   }
/*     */   
/*     */   void startDownload() {
/* 139 */     this.aborted = false;
/* 140 */     List<VersionSyncInfo> list = this.handler.getSelectedList();
/*     */     
/* 142 */     if (list == null || list.isEmpty()) {
/*     */       return;
/*     */     }
/* 145 */     int countLocal = 0;
/* 146 */     VersionSyncInfo local = null;
/*     */     
/* 148 */     for (VersionSyncInfo version : list) {
/*     */       
/* 150 */       if (this.forceDownload) {
/* 151 */         if (!version.hasRemote()) {
/* 152 */           Alert.showError(Localizable.get("version.manager.downloader.error.title"), Localizable.get("version.manager.downloader.error.local", new Object[] { version.getID() })); return;
/*     */         } 
/* 154 */         if (version.isUpToDate() && version.isInstalled()) {
/* 155 */           countLocal++;
/* 156 */           local = version;
/*     */         } 
/*     */       } 
/*     */     } 
/* 160 */     if (countLocal > 0) {
/* 161 */       String suffix; Object var; String title = Localizable.get("version.manager.downloader.warning.title");
/*     */ 
/*     */ 
/*     */       
/* 165 */       if (countLocal == 1) {
/* 166 */         suffix = "single";
/* 167 */         var = local.getID();
/*     */       } else {
/* 169 */         suffix = "multiply";
/* 170 */         var = Integer.valueOf(countLocal);
/*     */       } 
/*     */       
/* 173 */       if (!Alert.showQuestion(title, Localizable.get("version.manager.downloader.warning.force." + suffix, new Object[] { var }))) {
/*     */         return;
/*     */       }
/*     */     } 
/* 177 */     List<VersionSyncInfoContainer> containers = new ArrayList<>();
/* 178 */     VersionManager manager = TLauncher.getInstance().getVersionManager();
/*     */     
/*     */     try {
/* 181 */       this.downloading = true;
/*     */       
/* 183 */       for (VersionSyncInfo version : list) {
/*     */         try {
/* 185 */           version.resolveCompleteVersion(manager, this.forceDownload);
/* 186 */           VersionSyncInfoContainer container = manager.downloadVersion(version, false, this.forceDownload);
/*     */           
/* 188 */           if (this.aborted) {
/*     */             return;
/*     */           }
/* 191 */           if (!container.getList().isEmpty()) {
/* 192 */             containers.add(container);
/*     */           }
/* 194 */         } catch (Exception e) {
/* 195 */           Alert.showError(Localizable.get("version.manager.downloader.error.title"), Localizable.get("version.manager.downloader.error.getting", new Object[] { version.getID() }), e);
/*     */           
/*     */           return;
/*     */         } 
/*     */       } 
/*     */       
/* 201 */       if (containers.isEmpty()) {
/* 202 */         Alert.showMessage(Localizable.get("version.manager.downloader.info.title"), Localizable.get("version.manager.downloader.info.no-needed"));
/*     */         
/*     */         return;
/*     */       } 
/* 206 */       if (containers.size() > 1) {
/* 207 */         DownloadableContainer.removeDuplicates(containers);
/*     */       }
/* 209 */       if (this.aborted) {
/*     */         return;
/*     */       }
/* 212 */       for (DownloadableContainer c : containers) {
/* 213 */         this.handler.downloader.add(c);
/*     */       }
/* 215 */       this.handler.downloading = list;
/* 216 */       this.handler.onVersionDownload(list);
/*     */       
/* 218 */       this.handler.downloader.startDownloadAndWait();
/*     */     } finally {
/*     */       
/* 221 */       this.downloading = false;
/*     */     } 
/*     */     
/* 224 */     this.handler.downloading.clear();
/*     */     
/* 226 */     for (VersionSyncInfoContainer container : containers) {
/* 227 */       List<Throwable> errors = container.getErrors();
/* 228 */       VersionSyncInfo version = container.getVersion();
/*     */       
/* 230 */       if (errors.isEmpty()) {
/*     */         try {
/* 232 */           manager.getLocalList().saveVersion(version.getCompleteVersion(this.forceDownload));
/* 233 */         } catch (IOException e) {
/* 234 */           Alert.showError(Localizable.get("version.manager.downloader.error.title"), Localizable.get("version.manager.downloader.error.saving", new Object[] { version.getID() }), e); return;
/*     */         } 
/*     */         continue;
/*     */       } 
/* 238 */       if (!(errors.get(0) instanceof org.tlauncher.tlauncher.downloader.AbortedDownloadException)) {
/* 239 */         Alert.showError(Localizable.get("version.manager.downloader.error.title"), Localizable.get("version.manager.downloader.error.downloading", new Object[] { version.getID() }), errors);
/*     */       }
/*     */     } 
/*     */     
/* 243 */     this.handler.refresh();
/*     */   }
/*     */   
/*     */   void stopDownload() {
/* 247 */     this.aborted = true;
/*     */     
/* 249 */     if (this.downloading)
/* 250 */       this.handler.downloader.stopDownloadAndWait(); 
/*     */   }
/*     */   
/*     */   public enum ButtonState {
/* 254 */     DOWNLOAD("down.png"), STOP("cancel.png");
/*     */     
/*     */     final Image image;
/*     */     
/*     */     ButtonState(String image) {
/* 259 */       this.image = ImageCache.getImage(image);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onVersionRefreshing(VersionManager vm) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void onVersionRefreshed(VersionManager vm) {}
/*     */ 
/*     */   
/*     */   public void onVersionSelected(List<VersionSyncInfo> versions) {
/* 273 */     if (!this.downloading) {
/* 274 */       this.blockable.unblock("selection");
/*     */     }
/*     */   }
/*     */   
/*     */   public void onVersionDeselected() {
/* 279 */     if (!this.downloading)
/* 280 */       this.blockable.block("selection"); 
/*     */   }
/*     */   
/*     */   public void onVersionDownload(List<VersionSyncInfo> list) {}
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\versions\VersionDownloadButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */