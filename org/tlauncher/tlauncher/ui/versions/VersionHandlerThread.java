/*     */ package org.tlauncher.tlauncher.ui.versions;
/*     */ 
/*     */ import org.tlauncher.tlauncher.managers.ProfileManager;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*     */ import org.tlauncher.tlauncher.ui.block.Blocker;
/*     */ import org.tlauncher.util.U;
/*     */ import org.tlauncher.util.async.LoopedThread;
/*     */ 
/*     */ public class VersionHandlerThread
/*     */ {
/*  12 */   public final String START_DOWNLOAD = "start-download"; public final String STOP_DOWNLOAD = "stop-download"; public final String DELETE_BLOCK = "deleting";
/*     */   
/*     */   private final VersionHandler handler;
/*     */   
/*     */   final StartDownloadThread startThread;
/*     */   
/*     */   final StopDownloadThread stopThread;
/*     */   
/*     */   final VersionDeleteThread deleteThread;
/*     */ 
/*     */   
/*     */   VersionHandlerThread(VersionHandler handler) {
/*  24 */     this.handler = handler;
/*     */     
/*  26 */     this.startThread = new StartDownloadThread(this);
/*  27 */     this.stopThread = new StopDownloadThread(this);
/*  28 */     this.deleteThread = new VersionDeleteThread(this);
/*     */   }
/*     */   
/*     */   class StartDownloadThread extends LoopedThread {
/*     */     private final VersionHandler handler;
/*     */     private final VersionDownloadButton button;
/*     */     
/*     */     StartDownloadThread(VersionHandlerThread parent) {
/*  36 */       super("StartDownloadThread");
/*     */       
/*  38 */       this.handler = parent.handler;
/*  39 */       this.button = this.handler.list.download;
/*     */       
/*  41 */       startAndWait();
/*     */     }
/*     */ 
/*     */     
/*     */     protected void iterateOnce() {
/*  46 */       ProfileManager p = TLauncher.getInstance().getProfileManager();
/*  47 */       if (!p.hasSelectedAccount()) {
/*  48 */         Alert.showLocError("", "auth.error.nousername", null);
/*     */         return;
/*     */       } 
/*  51 */       this.button.setState(VersionDownloadButton.ButtonState.STOP);
/*  52 */       Blocker.block(this.handler, "start-download");
/*     */       
/*  54 */       this.button.startDownload();
/*     */       
/*  56 */       Blocker.unblock(this.handler, "start-download");
/*     */       
/*  58 */       this.button.setState(VersionDownloadButton.ButtonState.DOWNLOAD);
/*     */     }
/*     */   }
/*     */   
/*     */   class StopDownloadThread extends LoopedThread {
/*     */     private final VersionHandler handler;
/*     */     private final VersionDownloadButton button;
/*     */     
/*     */     StopDownloadThread(VersionHandlerThread parent) {
/*  67 */       super("StopDownloadThread");
/*     */       
/*  69 */       this.handler = parent.handler;
/*  70 */       this.button = this.handler.list.download;
/*     */       
/*  72 */       startAndWait();
/*     */     }
/*     */ 
/*     */     
/*     */     protected void iterateOnce() {
/*  77 */       Blocker.block(this.button.blockable, "stop-download");
/*     */       
/*  79 */       while (!this.handler.downloader.isThreadLocked()) {
/*  80 */         U.sleepFor(1000L);
/*     */       }
/*  82 */       this.button.stopDownload();
/*     */     }
/*     */   }
/*     */   
/*     */   class VersionDeleteThread extends LoopedThread {
/*     */     private final VersionHandler handler;
/*     */     private final VersionRemoveButton button;
/*     */     
/*     */     VersionDeleteThread(VersionHandlerThread parent) {
/*  91 */       super("VersionDeleteThread");
/*     */       
/*  93 */       this.handler = parent.handler;
/*  94 */       this.button = this.handler.list.remove;
/*     */       
/*  96 */       startAndWait();
/*     */     }
/*     */ 
/*     */     
/*     */     protected void iterateOnce() {
/* 101 */       Blocker.block(this.handler, "deleting");
/*     */       
/* 103 */       this.button.delete();
/*     */       
/* 105 */       Blocker.unblock(this.handler, "deleting");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\versions\VersionHandlerThread.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */