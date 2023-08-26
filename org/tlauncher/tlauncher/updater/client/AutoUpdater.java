/*    */ package org.tlauncher.tlauncher.updater.client;
/*    */ 
/*    */ import org.tlauncher.tlauncher.entity.ConfigEnum;
/*    */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*    */ import org.tlauncher.tlauncher.ui.updater.AutoUpdaterFrame;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AutoUpdater
/*    */   extends Updater
/*    */ {
/*    */   private final TLauncher t;
/*    */   private final AutoUpdaterFrame frame;
/*    */   private final UpdaterListener updaterL;
/*    */   private final UpdateListener updateL;
/*    */   
/*    */   public AutoUpdater(final TLauncher tlauncher) {
/* 20 */     this.t = tlauncher;
/*    */     
/* 22 */     this.frame = new AutoUpdaterFrame(this);
/*    */     
/* 24 */     addListener(new UpdaterAdapter()
/*    */         {
/*    */           public void onUpdaterSucceeded(Updater.SearchSucceeded succeeded) {
/* 27 */             if (TLauncher.getVersion() < succeeded.response.getVersion())
/* 28 */               tlauncher.getConfiguration().set(ConfigEnum.UPDATER_LAUNCHER, Boolean.valueOf(true)); 
/*    */           }
/*    */         });
/* 31 */     addListener((UpdaterListener)this.frame);
/*    */     
/* 33 */     this.updaterL = new UpdaterAdapter()
/*    */       {
/*    */         public void onUpdaterSucceeded(Updater.SearchSucceeded succeeded)
/*    */         {
/* 37 */           Update upd = succeeded.getResponse();
/*    */           
/* 39 */           if (upd.isApplicable()) {
/* 40 */             upd.addListener(AutoUpdater.this.updateL);
/* 41 */             upd.download();
/*    */           } 
/*    */         }
/*    */       };
/*    */     
/* 46 */     this.updateL = new UpdateListener()
/*    */       {
/*    */         public void onUpdateError(Update u, Throwable e) {}
/*    */ 
/*    */ 
/*    */ 
/*    */         
/*    */         public void onUpdateDownloading(Update u) {}
/*    */ 
/*    */ 
/*    */         
/*    */         public void onUpdateDownloadError(Update u, Throwable e) {}
/*    */ 
/*    */ 
/*    */         
/*    */         public void onUpdateReady(Update u) {
/* 62 */           u.apply();
/*    */         }
/*    */ 
/*    */ 
/*    */         
/*    */         public void onUpdateApplying(Update u) {}
/*    */ 
/*    */         
/*    */         public void onUpdateApplyError(Update u, Throwable e) {
/* 71 */           System.exit(0);
/*    */         }
/*    */       };
/*    */     
/* 75 */     addListener(this.updaterL);
/*    */   }
/*    */   
/*    */   public TLauncher getLauncher() {
/* 79 */     return this.t;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Updater.SearchResult findUpdate0() {
/* 84 */     return super.findUpdate0();
/*    */   }
/*    */   
/*    */   public void cancelUpdate() {
/* 88 */     this.t.getDownloader().stopDownload();
/*    */   }
/*    */   
/*    */   public boolean isClosed() {
/* 92 */     return this.frame.isClosed();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\updater\client\AutoUpdater.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */