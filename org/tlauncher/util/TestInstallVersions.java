/*    */ package org.tlauncher.util;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import net.minecraft.launcher.updater.LocalVersionList;
/*    */ import net.minecraft.launcher.updater.VersionFilter;
/*    */ import net.minecraft.launcher.updater.VersionSyncInfo;
/*    */ import org.apache.log4j.Logger;
/*    */ import org.tlauncher.tlauncher.configuration.Configuration;
/*    */ import org.tlauncher.tlauncher.repository.ClientInstanceRepo;
/*    */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*    */ 
/*    */ 
/*    */ public class TestInstallVersions
/*    */ {
/* 16 */   private static final Logger log = Logger.getLogger(TestInstallVersions.class);
/*    */ 
/*    */   
/*    */   public static void install(Configuration conf) {
/*    */     try {
/* 21 */       if (conf.getBoolean("run.all.tlauncher.versions") || conf.getBoolean("run.all.official.versions")) {
/* 22 */         U.sleepFor(5000L);
/* 23 */         log.info("******version should be updated******");
/* 24 */         FileUtil.deleteDirectory(new File(MinecraftUtil.getWorkingDirectory(), "versions"));
/* 25 */         FileUtil.createFolder(new File(MinecraftUtil.getWorkingDirectory(), "versions"));
/* 26 */         TLauncher.getInstance().getVersionManager().refresh();
/*    */       } 
/*    */       
/* 29 */       if (conf.getBoolean("run.all.tlauncher.versions")) {
/* 30 */         log.info("******runAllTLauncherVersions = true******");
/*    */         
/* 32 */         LocalVersionList lvl = new LocalVersionList(MinecraftUtil.getWorkingDirectory());
/* 33 */         TLauncher t = TLauncher.getInstance();
/* 34 */         t.getVersionManager().getVersions().stream()
/* 35 */           .filter(v -> (v.getAvailableVersion().getSource().equals(ClientInstanceRepo.EXTRA_VERSION_REPO) || v.getAvailableVersion().getSource().equals(ClientInstanceRepo.SKIN_VERSION_REPO)))
/*    */           
/* 37 */           .forEach(v -> {
/*    */               log.info(v.getID());
/*    */               VersionSyncInfo vsi = t.getVersionManager().getVersionSyncInfo(v.getID());
/*    */               try {
/*    */                 lvl.saveVersion(vsi.getCompleteVersion(false).resolve(t.getVersionManager(), false));
/* 42 */               } catch (IOException e) {
/*    */                 log.warn("error", e);
/*    */               } 
/*    */             });
/*    */       } 
/* 47 */       if (conf.getBoolean("run.all.official.versions")) {
/* 48 */         log.info("******runAllOfficialVersions = true******");
/* 49 */         LocalVersionList lvl = new LocalVersionList(MinecraftUtil.getWorkingDirectory());
/* 50 */         FileUtil.deleteDirectory(MinecraftUtil.getWorkingDirectory("versions"));
/* 51 */         TLauncher t = TLauncher.getInstance();
/* 52 */         t.getVersionManager().getVersions(new VersionFilter(), false).stream()
/* 53 */           .filter(v -> v.getAvailableVersion().getSource().equals(ClientInstanceRepo.OFFICIAL_VERSION_REPO))
/* 54 */           .forEach(v -> {
/*    */               log.info(v.getID());
/*    */               VersionSyncInfo vsi = t.getVersionManager().getVersionSyncInfo(v.getID());
/*    */               try {
/*    */                 lvl.saveVersion(vsi.getCompleteVersion(false).resolve(t.getVersionManager(), false));
/* 59 */               } catch (IOException e) {
/*    */                 log.warn("error", e);
/*    */               } 
/*    */             });
/*    */       } 
/*    */     } catch (Throwable $ex) {
/*    */       throw $ex;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlaunche\\util\TestInstallVersions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */