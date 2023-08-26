/*    */ package org.tlauncher.tlauncher.downloader;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ public class RetryDownloadException
/*    */   extends IOException
/*    */ {
/*    */   private static final long serialVersionUID = 2968569164701826930L;
/*    */   
/*    */   public RetryDownloadException(String message) {
/* 12 */     super(message);
/*    */   }
/*    */   
/*    */   public RetryDownloadException(String message, Throwable cause) {
/* 16 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\downloader\RetryDownloadException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */