/*    */ package org.tlauncher.tlauncher.minecraft.launcher.assitent;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.util.List;
/*    */ import org.tlauncher.modpack.domain.client.version.MetadataDTO;
/*    */ import org.tlauncher.tlauncher.downloader.DefaultDownloadableContainerHandler;
/*    */ import org.tlauncher.tlauncher.downloader.Downloadable;
/*    */ import org.tlauncher.tlauncher.downloader.DownloadableContainer;
/*    */ import org.tlauncher.tlauncher.downloader.DownloadableContainerHandler;
/*    */ import org.tlauncher.tlauncher.entity.AdditionalAsset;
/*    */ import org.tlauncher.tlauncher.minecraft.launcher.MinecraftException;
/*    */ import org.tlauncher.tlauncher.minecraft.launcher.MinecraftLauncher;
/*    */ import org.tlauncher.tlauncher.repository.ClientInstanceRepo;
/*    */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*    */ import org.tlauncher.util.FileUtil;
/*    */ import org.tlauncher.util.MinecraftUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SoundAssist
/*    */   extends MinecraftLauncherAssistantWrapper
/*    */ {
/*    */   public void collectResources(MinecraftLauncher launcher) throws MinecraftException {
/* 26 */     List<AdditionalAsset> assets = TLauncher.getInstance().getAdditionalAssetsComponent().getAdditionalAssets();
/* 27 */     DownloadableContainer c = new DownloadableContainer();
/*    */     try {
/* 29 */       for (AdditionalAsset r : assets) {
/* 30 */         if (r.getVersions().contains(launcher.getVersion().getJar()) || r
/* 31 */           .getVersions().contains(launcher.getVersionName())) {
/* 32 */           for (MetadataDTO m : r.getFiles()) {
/* 33 */             if (notExistsOrCorrect(launcher.getRunningMinecraftDir(), m) && !copyFromLocalRepo(launcher.getRunningMinecraftDir(), m)) {
/* 34 */               m.setLocalDestination(new File(MinecraftUtil.getTLauncherFile("repo"), m.getPath()));
/* 35 */               c.add(new Downloadable(ClientInstanceRepo.EXTRA_VERSION_REPO, m, true));
/*    */             } 
/*    */           } 
/*    */         }
/*    */       } 
/* 40 */     } catch (IOException e) {
/* 41 */       throw new MinecraftException(e.getMessage(), "download-jar", e);
/*    */     } 
/* 43 */     if (!c.getList().isEmpty()) {
/* 44 */       c.addHandler((DownloadableContainerHandler)new DefaultDownloadableContainerHandler());
/* 45 */       launcher.getDownloader().add(c);
/*    */     } 
/*    */   }
/*    */   
/*    */   private boolean notExistsOrCorrect(File file, MetadataDTO m) {
/* 50 */     File f = new File(file, m.getPath());
/* 51 */     if (!f.exists())
/* 52 */       return true; 
/* 53 */     return !m.getSha1().equalsIgnoreCase(FileUtil.getChecksum(f));
/*    */   }
/*    */ 
/*    */   
/*    */   private boolean copyFromLocalRepo(File file, MetadataDTO m) throws IOException {
/* 58 */     File source = MinecraftUtil.getTLauncherFile("repo/" + m.getPath());
/* 59 */     File target = new File(file, m.getPath());
/* 60 */     if (m.getSha1().equalsIgnoreCase(FileUtil.getChecksum(source))) {
/* 61 */       FileUtil.copyFile(source, target, true);
/* 62 */       return true;
/*    */     } 
/* 64 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraft\launcher\assitent\SoundAssist.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */