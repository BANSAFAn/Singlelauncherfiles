/*    */ package org.apache.commons.compress.compressors.gzip;
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
/*    */ 
/*    */ 
/*    */ public class GzipUtils
/*    */ {
/*    */   private static final FileNameUtil fileNameUtil;
/*    */   
/*    */   static {
/* 38 */     Map<String, String> uncompressSuffix = new LinkedHashMap<>();
/*    */     
/* 40 */     uncompressSuffix.put(".tgz", ".tar");
/* 41 */     uncompressSuffix.put(".taz", ".tar");
/* 42 */     uncompressSuffix.put(".svgz", ".svg");
/* 43 */     uncompressSuffix.put(".cpgz", ".cpio");
/* 44 */     uncompressSuffix.put(".wmz", ".wmf");
/* 45 */     uncompressSuffix.put(".emz", ".emf");
/* 46 */     uncompressSuffix.put(".gz", "");
/* 47 */     uncompressSuffix.put(".z", "");
/* 48 */     uncompressSuffix.put("-gz", "");
/* 49 */     uncompressSuffix.put("-z", "");
/* 50 */     uncompressSuffix.put("_z", "");
/* 51 */     fileNameUtil = new FileNameUtil(uncompressSuffix, ".gz");
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
/* 66 */     return fileNameUtil.isCompressedFilename(fileName);
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
/* 83 */     return fileNameUtil.getUncompressedFilename(fileName);
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
/*    */   public static String getCompressedFilename(String fileName) {
/* 98 */     return fileNameUtil.getCompressedFilename(fileName);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\compressors\gzip\GzipUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */