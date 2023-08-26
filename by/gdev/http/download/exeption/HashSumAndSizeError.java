/*    */ package by.gdev.http.download.exeption;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HashSumAndSizeError
/*    */   extends UploadFileException
/*    */ {
/*    */   private static final long serialVersionUID = 6549216849433173596L;
/*    */   
/*    */   public HashSumAndSizeError(String uri, String localPath, String message) {
/* 12 */     super(uri, localPath, message);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\by\gdev\http\download\exeption\HashSumAndSizeError.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */