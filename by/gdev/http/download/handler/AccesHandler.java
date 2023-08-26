/*    */ package by.gdev.http.download.handler;
/*    */ 
/*    */ import by.gdev.http.upload.download.downloader.DownloadElement;
/*    */ import by.gdev.util.DesktopUtil;
/*    */ import by.gdev.util.OSInfo;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Paths;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AccesHandler
/*    */   implements PostHandler
/*    */ {
/* 17 */   private static final Logger log = LoggerFactory.getLogger(AccesHandler.class);
/*    */ 
/*    */ 
/*    */   
/*    */   public void postProcessDownloadElement(DownloadElement e) {
/* 22 */     if (e.getMetadata().isExecutable())
/* 23 */       if ((((OSInfo.getOSType() == OSInfo.OSType.LINUX) ? 1 : 0) | ((OSInfo.getOSType() == OSInfo.OSType.MACOSX) ? 1 : 0)) != 0)
/*    */         try {
/* 25 */           Files.setPosixFilePermissions(Paths.get(e.getPathToDownload() + e.getMetadata().getPath(), new String[0]), DesktopUtil.PERMISSIONS);
/* 26 */         } catch (IOException e1) {
/* 27 */           log.error("Error set file permission", e1);
/*    */         }   
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\by\gdev\http\download\handler\AccesHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */