/*    */ package org.tlauncher.tlauncher.downloader;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import org.tlauncher.modpack.domain.client.version.MetadataDTO;
/*    */ import org.tlauncher.util.FileUtil;
/*    */ 
/*    */ public class DefaultDownloadableContainerHandler
/*    */   extends DownloadableContainerHandlerAdapter
/*    */ {
/*    */   public void onComplete(DownloadableContainer c, Downloadable d) throws RetryDownloadException {
/* 11 */     MetadataDTO m = d.getMetadataDTO();
/* 12 */     if (m.getSha1() == null)
/*    */       return; 
/* 14 */     String hash = FileUtil.getChecksum(m.getLocalDestination());
/* 15 */     if (Objects.isNull(hash) || hash.equals(m.getSha1()))
/*    */       return; 
/* 17 */     throw new RetryDownloadException(String.format("illegal hash, got: %s; expected: %s", new Object[] { hash, m
/* 18 */             .getSha1() }));
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\downloader\DefaultDownloadableContainerHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */