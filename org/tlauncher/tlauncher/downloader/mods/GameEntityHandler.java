/*    */ package org.tlauncher.tlauncher.downloader.mods;
/*    */ 
/*    */ import org.tlauncher.tlauncher.downloader.Downloadable;
/*    */ import org.tlauncher.tlauncher.downloader.DownloadableContainer;
/*    */ import org.tlauncher.tlauncher.downloader.DownloadableContainerHandler;
/*    */ import org.tlauncher.tlauncher.downloader.RetryDownloadException;
/*    */ import org.tlauncher.util.FileUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GameEntityHandler
/*    */   implements DownloadableContainerHandler
/*    */ {
/*    */   public void onAbort(DownloadableContainer c) {}
/*    */   
/*    */   public void onError(DownloadableContainer c, Downloadable d, Throwable e) {}
/*    */   
/*    */   public void onComplete(DownloadableContainer c, Downloadable d) throws RetryDownloadException {
/* 26 */     if (d instanceof GameEntityDownloader) {
/* 27 */       GameEntityDownloader g = (GameEntityDownloader)d;
/* 28 */       String fileHash = FileUtil.getChecksum(d.getMetadataDTO().getLocalDestination(), "sha1");
/*    */       
/* 30 */       if (fileHash == null || fileHash.equals(g.getMetadataDTO().getSha1())) {
/*    */         return;
/*    */       }
/* 33 */       throw new RetryDownloadException("illegal library hash. got: " + fileHash + "; expected: " + g.getMetadataDTO().getSha1());
/*    */     } 
/*    */   }
/*    */   
/*    */   public void onFullComplete(DownloadableContainer c) {}
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\downloader\mods\GameEntityHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */