/*    */ package org.apache.commons.compress.compressors.bzip2;
/*    */ 
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.Map;
/*    */ import org.apache.commons.compress.compressors.FileNameUtil;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class BZip2Utils
/*    */ {
/*    */   private static final FileNameUtil fileNameUtil;
/*    */   
/*    */   static {
/* 36 */     Map<String, String> uncompressSuffix = new LinkedHashMap<>();
/*    */ 
/*    */ 
/*    */     
/* 40 */     uncompressSuffix.put(".tar.bz2", ".tar");
/* 41 */     uncompressSuffix.put(".tbz2", ".tar");
/* 42 */     uncompressSuffix.put(".tbz", ".tar");
/* 43 */     uncompressSuffix.put(".bz2", "");
/* 44 */     uncompressSuffix.put(".bz", "");
/* 45 */     fileNameUtil = new FileNameUtil(uncompressSuffix, ".bz2");
/*    */   }
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
/*    */ 
/*    */   
/*    */   public static boolean isCompressedFilename(String fileName) {
/* 60 */     return fileNameUtil.isCompressedFilename(fileName);
/*    */   }
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
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String getUncompressedFilename(String fileName) {
/* 77 */     return fileNameUtil.getUncompressedFilename(fileName);
/*    */   }
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
/*    */   
/*    */   public static String getCompressedFilename(String fileName) {
/* 91 */     return fileNameUtil.getCompressedFilename(fileName);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\compressors\bzip2\BZip2Utils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */