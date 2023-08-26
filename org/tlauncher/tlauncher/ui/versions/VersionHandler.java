/*     */ package org.tlauncher.tlauncher.ui.versions;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import net.minecraft.launcher.updater.VersionSyncInfo;
/*     */ import org.tlauncher.tlauncher.downloader.Downloader;
/*     */ import org.tlauncher.tlauncher.managers.VersionManager;
/*     */ import org.tlauncher.tlauncher.managers.VersionManagerListener;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.block.Blockable;
/*     */ import org.tlauncher.tlauncher.ui.block.Blocker;
/*     */ import org.tlauncher.tlauncher.ui.scenes.VersionManagerScene;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VersionHandler
/*     */   implements Blockable, VersionHandlerListener
/*     */ {
/*     */   static final int ELEM_WIDTH = 500;
/*     */   public static final String REFRESH_BLOCK = "refresh";
/*     */   public static final String SINGLE_SELECTION_BLOCK = "single-select";
/*     */   public static final String START_DOWNLOAD = "start-download";
/*     */   public static final String STOP_DOWNLOAD = "stop-download";
/*     */   public static final String DELETE_BLOCK = "deleting";
/*     */   private final List<VersionHandlerListener> listeners;
/*     */   private final VersionHandler instance;
/*     */   public final VersionManagerScene scene;
/*     */   final VersionHandlerThread thread;
/*     */   public final VersionList list;
/*     */   final VersionManager vm;
/*     */   final Downloader downloader;
/*     */   List<VersionSyncInfo> selected;
/*     */   List<VersionSyncInfo> downloading;
/*     */   
/*     */   public VersionHandler(VersionManagerScene scene) {
/*  43 */     this.instance = this;
/*  44 */     this.scene = scene;
/*     */     
/*  46 */     this.listeners = Collections.synchronizedList(new ArrayList<>());
/*  47 */     this.downloading = Collections.synchronizedList(new ArrayList<>());
/*     */     
/*  49 */     TLauncher launcher = TLauncher.getInstance();
/*  50 */     this.vm = launcher.getVersionManager();
/*  51 */     this.downloader = launcher.getDownloader();
/*     */     
/*  53 */     this.list = new VersionList(this);
/*     */     
/*  55 */     this.thread = new VersionHandlerThread(this);
/*     */     
/*  57 */     this.vm.addListener(new VersionManagerListener()
/*     */         {
/*     */           public void onVersionsRefreshing(VersionManager manager) {
/*  60 */             VersionHandler.this.instance.onVersionRefreshing(manager);
/*     */           }
/*     */ 
/*     */           
/*     */           public void onVersionsRefreshed(VersionManager manager) {
/*  65 */             VersionHandler.this.instance.onVersionRefreshed(manager);
/*     */           }
/*     */ 
/*     */           
/*     */           public void onVersionsRefreshingFailed(VersionManager manager) {
/*  70 */             onVersionsRefreshed(manager);
/*     */           }
/*     */         });
/*     */     
/*  74 */     onVersionDeselected();
/*     */   }
/*     */   
/*     */   void addListener(VersionHandlerListener listener) {
/*  78 */     this.listeners.add(listener);
/*     */   }
/*     */   
/*     */   void update() {
/*  82 */     if (this.selected != null)
/*  83 */       onVersionSelected(this.selected); 
/*     */   }
/*     */   
/*     */   void refresh() {
/*  87 */     this.vm.startRefresh(true);
/*     */   }
/*     */   
/*     */   void asyncRefresh() {
/*  91 */     this.vm.asyncRefresh();
/*     */   }
/*     */   
/*     */   public void stopRefresh() {
/*  95 */     this.vm.stopRefresh();
/*     */   }
/*     */   
/*     */   void exitEditor() {
/*  99 */     this.list.deselect();
/* 100 */     this.scene.getMainPane().openDefaultScene();
/*     */   }
/*     */   
/*     */   VersionSyncInfo getSelected() {
/* 104 */     return (this.selected == null || this.selected.size() != 1) ? null : this.selected.get(0);
/*     */   }
/*     */   
/*     */   List<VersionSyncInfo> getSelectedList() {
/* 108 */     return this.selected;
/*     */   }
/*     */ 
/*     */   
/*     */   public void block(Object reason) {
/* 113 */     Blocker.block(reason, new Blockable[] { (Blockable)this.list, (Blockable)(this.scene.getMainPane()).defaultScene });
/*     */   }
/*     */ 
/*     */   
/*     */   public void unblock(Object reason) {
/* 118 */     Blocker.unblock(reason, new Blockable[] { (Blockable)this.list, (Blockable)(this.scene.getMainPane()).defaultScene });
/*     */   }
/*     */ 
/*     */   
/*     */   public void onVersionRefreshing(VersionManager vm) {
/* 123 */     Blocker.block(this.instance, "refresh");
/*     */     
/* 125 */     for (VersionHandlerListener listener : this.listeners) {
/* 126 */       listener.onVersionRefreshing(vm);
/*     */     }
/*     */   }
/*     */   
/*     */   public void onVersionRefreshed(VersionManager vm) {
/* 131 */     Blocker.unblock(this.instance, "refresh");
/*     */     
/* 133 */     for (VersionHandlerListener listener : this.listeners) {
/* 134 */       listener.onVersionRefreshed(vm);
/*     */     }
/*     */   }
/*     */   
/*     */   public void onVersionSelected(List<VersionSyncInfo> version) {
/* 139 */     this.selected = version;
/*     */     
/* 141 */     if (version == null || version.isEmpty() || ((VersionSyncInfo)version.get(0)).getID() == null) {
/* 142 */       onVersionDeselected();
/*     */     } else {
/* 144 */       for (VersionHandlerListener listener : this.listeners)
/* 145 */         listener.onVersionSelected(version); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void onVersionDeselected() {
/* 150 */     this.selected = null;
/*     */     
/* 152 */     for (VersionHandlerListener listener : this.listeners) {
/* 153 */       listener.onVersionDeselected();
/*     */     }
/*     */   }
/*     */   
/*     */   public void onVersionDownload(List<VersionSyncInfo> list) {
/* 158 */     this.downloading = list;
/*     */     
/* 160 */     for (VersionHandlerListener listener : this.listeners)
/* 161 */       listener.onVersionDownload(list); 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\versions\VersionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */