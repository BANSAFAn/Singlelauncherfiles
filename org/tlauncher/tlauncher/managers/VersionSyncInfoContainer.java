/*    */ package org.tlauncher.tlauncher.managers;
/*    */ 
/*    */ import net.minecraft.launcher.updater.VersionSyncInfo;
/*    */ import org.tlauncher.tlauncher.downloader.DownloadableContainer;
/*    */ 
/*    */ public class VersionSyncInfoContainer
/*    */   extends DownloadableContainer {
/*    */   private final VersionSyncInfo version;
/*    */   
/*    */   public VersionSyncInfoContainer(VersionSyncInfo version) {
/* 11 */     if (version == null) {
/* 12 */       throw new NullPointerException();
/*    */     }
/* 14 */     this.version = version;
/*    */   }
/*    */   
/*    */   public VersionSyncInfo getVersion() {
/* 18 */     return this.version;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\managers\VersionSyncInfoContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */