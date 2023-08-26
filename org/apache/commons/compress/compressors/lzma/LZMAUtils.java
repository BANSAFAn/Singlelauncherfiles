/*     */ package org.apache.commons.compress.compressors.lzma;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.compress.compressors.FileNameUtil;
/*     */ import org.apache.commons.compress.utils.OsgiUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LZMAUtils
/*     */ {
/*     */   private static final FileNameUtil fileNameUtil;
/*  39 */   private static final byte[] HEADER_MAGIC = new byte[] { 93, 0, 0 };
/*     */   private static volatile CachedAvailability cachedLZMAAvailability;
/*     */   
/*     */   enum CachedAvailability
/*     */   {
/*  44 */     DONT_CACHE, CACHED_AVAILABLE, CACHED_UNAVAILABLE;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*  50 */     Map<String, String> uncompressSuffix = new HashMap<>();
/*  51 */     uncompressSuffix.put(".lzma", "");
/*  52 */     uncompressSuffix.put("-lzma", "");
/*  53 */     fileNameUtil = new FileNameUtil(uncompressSuffix, ".lzma");
/*  54 */     cachedLZMAAvailability = CachedAvailability.DONT_CACHE;
/*  55 */     setCacheLZMAAvailablity(!OsgiUtils.isRunningInOsgiEnvironment());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean matches(byte[] signature, int length) {
/*  70 */     if (length < HEADER_MAGIC.length) {
/*  71 */       return false;
/*     */     }
/*     */     
/*  74 */     for (int i = 0; i < HEADER_MAGIC.length; i++) {
/*  75 */       if (signature[i] != HEADER_MAGIC[i]) {
/*  76 */         return false;
/*     */       }
/*     */     } 
/*     */     
/*  80 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isLZMACompressionAvailable() {
/*  89 */     CachedAvailability cachedResult = cachedLZMAAvailability;
/*  90 */     if (cachedResult != CachedAvailability.DONT_CACHE) {
/*  91 */       return (cachedResult == CachedAvailability.CACHED_AVAILABLE);
/*     */     }
/*  93 */     return internalIsLZMACompressionAvailable();
/*     */   }
/*     */   
/*     */   private static boolean internalIsLZMACompressionAvailable() {
/*     */     try {
/*  98 */       LZMACompressorInputStream.matches(null, 0);
/*  99 */       return true;
/* 100 */     } catch (NoClassDefFoundError error) {
/* 101 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isCompressedFilename(String fileName) {
/* 113 */     return fileNameUtil.isCompressedFilename(fileName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getUncompressedFilename(String fileName) {
/* 127 */     return fileNameUtil.getUncompressedFilename(fileName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getCompressedFilename(String fileName) {
/* 138 */     return fileNameUtil.getCompressedFilename(fileName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setCacheLZMAAvailablity(boolean doCache) {
/* 148 */     if (!doCache) {
/* 149 */       cachedLZMAAvailability = CachedAvailability.DONT_CACHE;
/* 150 */     } else if (cachedLZMAAvailability == CachedAvailability.DONT_CACHE) {
/* 151 */       boolean hasLzma = internalIsLZMACompressionAvailable();
/* 152 */       cachedLZMAAvailability = hasLzma ? CachedAvailability.CACHED_AVAILABLE : CachedAvailability.CACHED_UNAVAILABLE;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static CachedAvailability getCachedLZMAAvailability() {
/* 159 */     return cachedLZMAAvailability;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\compressors\lzma\LZMAUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */