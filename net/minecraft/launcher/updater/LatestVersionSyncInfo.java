/*    */ package net.minecraft.launcher.updater;
/*    */ 
/*    */ import net.minecraft.launcher.versions.ReleaseType;
/*    */ import net.minecraft.launcher.versions.Version;
/*    */ 
/*    */ public class LatestVersionSyncInfo
/*    */   extends VersionSyncInfo {
/*    */   private final ReleaseType type;
/*    */   
/*    */   public LatestVersionSyncInfo(ReleaseType type, Version localVersion, Version remoteVersion) {
/* 11 */     super(localVersion, remoteVersion);
/*    */     
/* 13 */     if (type == null) {
/* 14 */       throw new NullPointerException("ReleaseType cannot be NULL!");
/*    */     }
/* 16 */     this.type = type;
/* 17 */     setID("latest-" + type.toString());
/*    */   }
/*    */   
/*    */   public LatestVersionSyncInfo(ReleaseType type, VersionSyncInfo syncInfo) {
/* 21 */     this(type, syncInfo.getLocal(), syncInfo.getRemote());
/*    */   }
/*    */   
/*    */   public String getVersionID() {
/* 25 */     if (this.localVersion != null) {
/* 26 */       return this.localVersion.getID();
/*    */     }
/* 28 */     if (this.remoteVersion != null) {
/* 29 */       return this.remoteVersion.getID();
/*    */     }
/* 31 */     return null;
/*    */   }
/*    */   
/*    */   public ReleaseType getReleaseType() {
/* 35 */     return this.type;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 40 */     return getClass().getSimpleName() + "{id='" + getID() + "', releaseType=" + this.type + ",\nlocal=" + this.localVersion + ",\nremote=" + this.remoteVersion + ", isInstalled=" + 
/*    */ 
/*    */       
/* 43 */       isInstalled() + ", hasRemote=" + hasRemote() + ", isUpToDate=" + 
/* 44 */       isUpToDate() + "}";
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\net\minecraft\launche\\updater\LatestVersionSyncInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */