/*    */ package org.tlauncher.util.statistics;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import net.minecraft.launcher.updater.VersionSyncInfo;
/*    */ import net.minecraft.launcher.versions.Version;
/*    */ import org.tlauncher.statistics.InstallVersionDTO;
/*    */ import org.tlauncher.tlauncher.downloader.Downloader;
/*    */ import org.tlauncher.tlauncher.downloader.DownloaderAdapterListener;
/*    */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*    */ import org.tlauncher.util.U;
/*    */ 
/*    */ public class InstallVersionListener extends DownloaderAdapterListener {
/*    */   private String version;
/*    */   
/*    */   public void onDownloaderStart(Downloader d, int files) {
/*    */     try {
/* 17 */       Version v = ((VersionSyncInfo)(TLauncher.getInstance().getFrame()).mp.defaultScene.loginForm.versions.getSelectedValue()).getRemote();
/* 18 */       if (v == null) {
/* 19 */         v = ((VersionSyncInfo)(TLauncher.getInstance().getFrame()).mp.defaultScene.loginForm.versions.getSelectedValue()).getLocal();
/*    */       }
/* 21 */       if (v == null) {
/* 22 */         this.version = "version_not_found";
/*    */         return;
/*    */       } 
/* 25 */       this.version = v.getID();
/* 26 */     } catch (NullPointerException exception) {
/* 27 */       U.log(new Object[] { exception });
/* 28 */       this.version = "version_not_found";
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onDownloaderComplete(Downloader d) {
/* 35 */     InstallVersionDTO v = new InstallVersionDTO();
/* 36 */     v.setInstallVersion(this.version);
/* 37 */     StatisticsUtil.startSending("save/install/version", v, Maps.newHashMap());
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlaunche\\util\statistics\InstallVersionListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */