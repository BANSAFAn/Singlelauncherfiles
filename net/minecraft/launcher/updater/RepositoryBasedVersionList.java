/*    */ package net.minecraft.launcher.updater;
/*    */ 
/*    */ import com.google.gson.JsonSyntaxException;
/*    */ import java.io.IOException;
/*    */ import net.minecraft.launcher.versions.CompleteVersion;
/*    */ import net.minecraft.launcher.versions.Version;
/*    */ import org.tlauncher.tlauncher.repository.Repo;
/*    */ import org.tlauncher.util.OS;
/*    */ 
/*    */ public class RepositoryBasedVersionList
/*    */   extends RemoteVersionList {
/*    */   protected final Repo repo;
/*    */   
/*    */   RepositoryBasedVersionList(Repo repo) {
/* 15 */     if (repo == null) {
/* 16 */       throw new NullPointerException();
/*    */     }
/* 18 */     this.repo = repo;
/*    */   }
/*    */ 
/*    */   
/*    */   public VersionList.RawVersionList getRawList() throws IOException {
/* 23 */     VersionList.RawVersionList rawList = super.getRawList();
/*    */     
/* 25 */     for (Version version : rawList.getVersions()) {
/* 26 */       version.setSource(this.repo);
/*    */     }
/* 28 */     return rawList;
/*    */   }
/*    */ 
/*    */   
/*    */   public CompleteVersion getCompleteVersion(Version version) throws JsonSyntaxException, IOException {
/* 33 */     CompleteVersion complete = super.getCompleteVersion(version);
/* 34 */     complete.setSource(this.repo);
/*    */     
/* 36 */     return complete;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hasAllFiles(CompleteVersion paramCompleteVersion, OS paramOperatingSystem) {
/* 41 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getUrl(String uri) throws IOException {
/* 46 */     return this.repo.getUrl(uri);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\net\minecraft\launche\\updater\RepositoryBasedVersionList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */