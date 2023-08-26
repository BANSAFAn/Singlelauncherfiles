/*    */ package net.minecraft.launcher.updater;
/*    */ 
/*    */ import com.google.gson.JsonSyntaxException;
/*    */ import java.io.IOException;
/*    */ import java.util.Collections;
/*    */ import net.minecraft.launcher.versions.CompleteVersion;
/*    */ import net.minecraft.launcher.versions.PartialVersion;
/*    */ import net.minecraft.launcher.versions.Version;
/*    */ import org.tlauncher.tlauncher.repository.ClientInstanceRepo;
/*    */ import org.tlauncher.tlauncher.repository.Repo;
/*    */ import org.tlauncher.util.Time;
/*    */ import org.tlauncher.util.TlauncherUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class UpgratedRepository
/*    */   extends RepositoryBasedVersionList
/*    */ {
/*    */   UpgratedRepository(Repo repo) {
/* 23 */     super(repo);
/*    */   }
/*    */ 
/*    */   
/*    */   public VersionList.RawVersionList getRawList() throws IOException {
/* 28 */     Object lock = new Object();
/* 29 */     Time.start(lock);
/*    */     
/* 31 */     VersionList.RawVersionList list = (VersionList.RawVersionList)this.gson.fromJson(this.repo.getUrl("version_manifest.json"), VersionList.RawVersionList.class);
/*    */     
/* 33 */     for (PartialVersion version : list.versions) {
/* 34 */       version.setVersionList(this);
/*    */     }
/* 36 */     log(new Object[] { "Got in", Long.valueOf(Time.stop(lock)), "ms" });
/*    */     
/* 38 */     for (Version version : list.getVersions())
/* 39 */       version.setSource(this.repo); 
/* 40 */     return list;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public CompleteVersion getCompleteVersion(Version version) throws JsonSyntaxException, IOException {
/* 46 */     if (version instanceof CompleteVersion) {
/* 47 */       return (CompleteVersion)version;
/*    */     }
/* 49 */     if (version == null)
/* 50 */       throw new NullPointerException("Version cannot be NULL!"); 
/* 51 */     String value = ClientInstanceRepo.EMPTY_REPO.getUrl(version.getUrl());
/* 52 */     CompleteVersion complete = (CompleteVersion)this.gson.fromJson(value, CompleteVersion.class);
/* 53 */     TlauncherUtil.processRemoteVersionToSave(complete, value, this.gson);
/* 54 */     complete.getModifiedVersion().setUpdatedTime(version.getUpdatedTime());
/* 55 */     complete.setID(version.getID());
/* 56 */     complete.setVersionList(this);
/* 57 */     complete.setUpdatedTime(version.getUpdatedTime());
/* 58 */     Collections.replaceAll(this.versions, version, complete);
/* 59 */     complete.setSource(this.repo);
/* 60 */     return complete;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\net\minecraft\launche\\updater\UpgratedRepository.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */