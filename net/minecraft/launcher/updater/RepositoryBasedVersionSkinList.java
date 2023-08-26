/*    */ package net.minecraft.launcher.updater;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.launcher.versions.PartialVersion;
/*    */ import net.minecraft.launcher.versions.Version;
/*    */ import org.tlauncher.tlauncher.repository.Repo;
/*    */ import org.tlauncher.util.Time;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RepositoryBasedVersionSkinList
/*    */   extends RepositoryBasedVersionList
/*    */ {
/*    */   public RepositoryBasedVersionSkinList(Repo repo) {
/* 16 */     super(repo);
/*    */   }
/*    */ 
/*    */   
/*    */   public VersionList.RawVersionList getRawList() throws IOException {
/* 21 */     Object lock = new Object();
/* 22 */     Time.start(lock);
/* 23 */     VersionList.RawVersionList list = (VersionList.RawVersionList)this.gson.fromJson(getUrl("versions/versions-1.0.json"), VersionList.RawVersionList.class);
/*    */     
/* 25 */     for (PartialVersion version : list.versions) {
/* 26 */       version.setVersionList(this);
/*    */     }
/* 28 */     log(new Object[] { "Got in", Long.valueOf(Time.stop(lock)), "ms" });
/* 29 */     for (Version version : list.getVersions())
/* 30 */       version.setSource(this.repo); 
/* 31 */     return list;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\net\minecraft\launche\\updater\RepositoryBasedVersionSkinList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */