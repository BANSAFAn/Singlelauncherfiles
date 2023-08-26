/*    */ package org.tlauncher.tlauncher.component;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.Paths;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.launcher.versions.CompleteVersion;
/*    */ import net.minecraft.launcher.versions.LogClient;
/*    */ import org.tlauncher.modpack.domain.client.version.MetadataDTO;
/*    */ import org.tlauncher.tlauncher.downloader.Downloadable;
/*    */ import org.tlauncher.tlauncher.downloader.DownloadableContainer;
/*    */ import org.tlauncher.tlauncher.repository.ClientInstanceRepo;
/*    */ import org.tlauncher.util.FileUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LogClientConfigurationComponent
/*    */ {
/*    */   private LogClient.LogClientFile validateLogClientXmlResource(CompleteVersion version, File gameDir) {
/* 22 */     if (isNotNullLogClient(version)) {
/* 23 */       LogClient.LogClientFile l = getLogClient(version).getFile();
/* 24 */       if (!l.getSha1().equals(FileUtil.getChecksum(buildPathLogXmlConfiguration(gameDir, getLogClient(version)).toFile()))) {
/* 25 */         return l;
/*    */       }
/*    */     } 
/* 28 */     return null;
/*    */   }
/*    */   
/*    */   public DownloadableContainer getContainer(CompleteVersion version, File gameDir, boolean forceUpdate) {
/* 32 */     DownloadableContainer container = new DownloadableContainer();
/* 33 */     LogClient.LogClientFile l = validateLogClientXmlResource(version, gameDir);
/* 34 */     if (Objects.isNull(l))
/* 35 */       return container; 
/* 36 */     MetadataDTO m = new MetadataDTO();
/* 37 */     m.setLocalDestination(Paths.get(gameDir.toString(), new String[] { "assets", "log_configs", l.getId() }).toFile());
/* 38 */     m.setSha1(l.getSha1());
/* 39 */     m.setUrl(l.getUrl());
/* 40 */     m.setSize(l.getSize());
/* 41 */     Downloadable d = new Downloadable(ClientInstanceRepo.EMPTY_REPO, m, forceUpdate);
/* 42 */     container.add(d);
/* 43 */     return container;
/*    */   }
/*    */   public Path buildPathLogXmlConfiguration(File gameDir, LogClient l) {
/* 46 */     return Paths.get(gameDir.getAbsolutePath(), new String[] { "assets", "log_configs", l.getFile().getId() });
/*    */   }
/*    */   public LogClient getLogClient(CompleteVersion v) {
/* 49 */     if (Objects.nonNull(v.getLogging())) {
/* 50 */       LogClient c = (LogClient)v.getLogging().get("client");
/* 51 */       if (Objects.nonNull(c) && Objects.nonNull(c.getFile())) {
/* 52 */         return c;
/*    */       }
/*    */     } 
/* 55 */     return null;
/*    */   }
/*    */   public boolean isNotNullLogClient(CompleteVersion v) {
/* 58 */     return !Objects.isNull(getLogClient(v));
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\component\LogClientConfigurationComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */