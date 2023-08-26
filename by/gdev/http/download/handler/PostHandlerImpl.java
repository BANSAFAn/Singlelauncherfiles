/*    */ package by.gdev.http.download.handler;
/*    */ 
/*    */ import by.gdev.http.download.exeption.HashSumAndSizeError;
/*    */ import by.gdev.http.download.model.Headers;
/*    */ import by.gdev.http.upload.download.downloader.DownloadElement;
/*    */ import by.gdev.util.DesktopUtil;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PostHandlerImpl
/*    */   implements PostHandler
/*    */ {
/* 20 */   private static final Logger log = LoggerFactory.getLogger(PostHandlerImpl.class);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void postProcessDownloadElement(DownloadElement element) {
/*    */     try {
/* 27 */       String shaLocalFile = DesktopUtil.getChecksum(new File(element
/* 28 */             .getPathToDownload() + element.getMetadata().getPath()), Headers.SHA1.getValue());
/* 29 */       long sizeLocalFile = (new File(element.getPathToDownload() + element.getMetadata().getPath())).length();
/* 30 */       if (sizeLocalFile != element.getMetadata().getSize() && 
/* 31 */         StringUtils.isEmpty(element.getMetadata().getLink()))
/* 32 */         element.setError((Throwable)new HashSumAndSizeError(element.getMetadata().getRelativeUrl(), element
/* 33 */               .getPathToDownload() + element.getMetadata().getPath(), "The size should be " + element
/* 34 */               .getMetadata().getSize())); 
/* 35 */       if (!shaLocalFile.equals(element.getMetadata().getSha1()) && 
/* 36 */         StringUtils.isEmpty(element.getMetadata().getLink()))
/* 37 */         element.setError((Throwable)new HashSumAndSizeError((String)element
/* 38 */               .getRepo().getRepositories().get(0) + element.getMetadata().getRelativeUrl(), element
/* 39 */               .getPathToDownload() + element.getMetadata().getPath(), "The hash sum should be " + element
/* 40 */               .getMetadata().getSha1())); 
/* 41 */     } catch (IOException|java.security.NoSuchAlgorithmException e) {
/* 42 */       log.error("Erorr", e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\by\gdev\http\download\handler\PostHandlerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */