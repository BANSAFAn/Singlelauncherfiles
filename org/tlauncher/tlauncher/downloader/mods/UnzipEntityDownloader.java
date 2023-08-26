/*    */ package org.tlauncher.tlauncher.downloader.mods;
/*    */ 
/*    */ import java.io.File;
/*    */ import org.tlauncher.modpack.domain.client.version.MetadataDTO;
/*    */ import org.tlauncher.tlauncher.downloader.RetryDownloadException;
/*    */ import org.tlauncher.tlauncher.repository.ClientInstanceRepo;
/*    */ import org.tlauncher.util.FileUtil;
/*    */ 
/*    */ public class UnzipEntityDownloader
/*    */   extends GameEntityDownloader
/*    */ {
/*    */   public UnzipEntityDownloader(boolean forceDownload, MetadataDTO metadata) {
/* 13 */     super(ClientInstanceRepo.createModpackRepo(), forceDownload, metadata);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onComplete() throws RetryDownloadException {
/* 18 */     super.onComplete();
/*    */     try {
/* 20 */       File f = getMetadataDTO().getLocalDestination();
/* 21 */       FileUtil.unzipUniversal(f, f.getParentFile());
/* 22 */     } catch (Throwable t) {
/* 23 */       throw new RetryDownloadException("cannot unpack archive", t);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\downloader\mods\UnzipEntityDownloader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */