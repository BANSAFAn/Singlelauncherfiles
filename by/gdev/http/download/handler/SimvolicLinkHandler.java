/*    */ package by.gdev.http.download.handler;
/*    */ 
/*    */ import by.gdev.http.upload.download.downloader.DownloadElement;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.Paths;
/*    */ import java.nio.file.attribute.FileAttribute;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ public class SimvolicLinkHandler
/*    */   implements PostHandler
/*    */ {
/* 17 */   private static final Logger log = LoggerFactory.getLogger(SimvolicLinkHandler.class);
/*    */ 
/*    */ 
/*    */   
/*    */   public void postProcessDownloadElement(DownloadElement e) {
/* 22 */     if (!StringUtils.isEmpty(e.getMetadata().getLink()))
/*    */       try {
/* 24 */         Path target = Paths.get(e.getPathToDownload(), new String[] { e.getMetadata().getLink() });
/* 25 */         Path link = Paths.get(e.getPathToDownload(), new String[] { e.getMetadata().getPath() });
/* 26 */         if (Files.exists(link, new java.nio.file.LinkOption[0])) {
/* 27 */           Files.delete(link);
/*    */         }
/* 29 */         Files.createSymbolicLink(link.toAbsolutePath(), target.toAbsolutePath(), (FileAttribute<?>[])new FileAttribute[0]);
/* 30 */       } catch (IOException ex) {
/* 31 */         log.error("Error to create simvolic link", ex);
/*    */       }  
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\by\gdev\http\download\handler\SimvolicLinkHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */