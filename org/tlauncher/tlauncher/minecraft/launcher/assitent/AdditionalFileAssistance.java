/*    */ package org.tlauncher.tlauncher.minecraft.launcher.assitent;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.net.URL;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import org.tlauncher.modpack.domain.client.version.MetadataDTO;
/*    */ import org.tlauncher.tlauncher.downloader.DefaultDownloadableContainerHandler;
/*    */ import org.tlauncher.tlauncher.downloader.Downloadable;
/*    */ import org.tlauncher.tlauncher.downloader.DownloadableContainer;
/*    */ import org.tlauncher.tlauncher.downloader.DownloadableContainerHandler;
/*    */ import org.tlauncher.tlauncher.downloader.mods.GameEntityDownloader;
/*    */ import org.tlauncher.tlauncher.minecraft.launcher.MinecraftException;
/*    */ import org.tlauncher.tlauncher.minecraft.launcher.MinecraftLauncher;
/*    */ import org.tlauncher.tlauncher.repository.ClientInstanceRepo;
/*    */ import org.tlauncher.util.FileUtil;
/*    */ import org.tlauncher.util.MinecraftUtil;
/*    */ import org.tlauncher.util.U;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AdditionalFileAssistance
/*    */   extends MinecraftLauncherAssistantWrapper
/*    */ {
/*    */   public void collectResources(MinecraftLauncher launcher) throws MinecraftException {
/* 27 */     List<MetadataDTO> list = launcher.getVersion().getAdditionalFiles();
/* 28 */     if (Objects.isNull(list))
/*    */       return; 
/* 30 */     DownloadableContainer c = new DownloadableContainer();
/*    */     try {
/* 32 */       for (MetadataDTO m : list) {
/* 33 */         m.setLocalDestination(new File(MinecraftUtil.getWorkingDirectory().getAbsolutePath(), m.getPath()));
/* 34 */         if (notExistsOrCorrect(m) && !copyFromLocalRepo(m)) {
/* 35 */           c.add((Downloadable)new AdditionalFileDownloader(m));
/*    */         }
/*    */       }
/*    */     
/* 39 */     } catch (IOException e) {
/* 40 */       U.log(new Object[] { e });
/* 41 */       throw new MinecraftException(e.getMessage(), "download-jar", e);
/*    */     } 
/*    */     
/* 44 */     if (!c.getList().isEmpty()) {
/* 45 */       c.addHandler((DownloadableContainerHandler)new DefaultDownloadableContainerHandler());
/* 46 */       launcher.getDownloader().add(c);
/*    */     } 
/*    */   }
/*    */   
/*    */   private boolean copyFromLocalRepo(MetadataDTO m) throws IOException {
/* 51 */     File source = new File(MinecraftUtil.getWorkingDirectory().getAbsolutePath(), (new URL(m.getUrl())).getPath());
/* 52 */     File target = new File(MinecraftUtil.getWorkingDirectory(), m.getPath());
/*    */     
/* 54 */     if (m.getSha1().equalsIgnoreCase(FileUtil.getChecksum(source))) {
/* 55 */       if (source.equals(target))
/* 56 */         return true; 
/* 57 */       FileUtil.copyFile(source, target, true);
/* 58 */       return true;
/*    */     } 
/* 60 */     return false;
/*    */   }
/*    */   
/*    */   private boolean notExistsOrCorrect(MetadataDTO m) {
/* 64 */     File target = m.getLocalDestination();
/*    */     
/* 66 */     if (!target.exists())
/* 67 */       return true; 
/* 68 */     return !m.getSha1().equalsIgnoreCase(FileUtil.getChecksum(target));
/*    */   }
/*    */   
/*    */   private static class AdditionalFileDownloader extends GameEntityDownloader {
/*    */     AdditionalFileDownloader(MetadataDTO metadata) {
/* 73 */       super(ClientInstanceRepo.EMPTY_REPO, true, metadata);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraft\launcher\assitent\AdditionalFileAssistance.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */