/*     */ package org.apache.commons.compress.compressors.xz;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XZUtils
/*     */ {
/*     */   private static final FileNameUtil fileNameUtil;
/*  42 */   private static final byte[] HEADER_MAGIC = new byte[] { -3, 55, 122, 88, 90, 0 };
/*     */   private static volatile CachedAvailability cachedXZAvailability;
/*     */   
/*     */   enum CachedAvailability
/*     */   {
/*  47 */     DONT_CACHE, CACHED_AVAILABLE, CACHED_UNAVAILABLE;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*  53 */     Map<String, String> uncompressSuffix = new HashMap<>();
/*  54 */     uncompressSuffix.put(".txz", ".tar");
/*  55 */     uncompressSuffix.put(".xz", "");
/*  56 */     uncompressSuffix.put("-xz", "");
/*  57 */     fileNameUtil = new FileNameUtil(uncompressSuffix, ".xz");
/*  58 */     cachedXZAvailability = CachedAvailability.DONT_CACHE;
/*  59 */     setCacheXZAvailablity(!OsgiUtils.isRunningInOsgiEnvironment());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean matches(byte[] signature, int length) {
/*  79 */     if (length < HEADER_MAGIC.length) {
/*  80 */       return false;
/*     */     }
/*     */     
/*  83 */     for (int i = 0; i < HEADER_MAGIC.length; i++) {
/*  84 */       if (signature[i] != HEADER_MAGIC[i]) {
/*  85 */         return false;
/*     */       }
/*     */     } 
/*     */     
/*  89 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isXZCompressionAvailable() {
/*  98 */     CachedAvailability cachedResult = cachedXZAvailability;
/*  99 */     if (cachedResult != CachedAvailability.DONT_CACHE) {
/* 100 */       return (cachedResult == CachedAvailability.CACHED_AVAILABLE);
/*     */     }
/* 102 */     return internalIsXZCompressionAvailable();
/*     */   }
/*     */   
/*     */   private static boolean internalIsXZCompressionAvailable() {
/*     */     try {
/* 107 */       XZCompressorInputStream.matches(null, 0);
/* 108 */       return true;
/* 109 */     } catch (NoClassDefFoundError error) {
/* 110 */       return false;
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
/* 122 */     return fileNameUtil.isCompressedFilename(fileName);
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
/*     */ 
/*     */   
/*     */   public static String getUncompressedFilename(String fileName) {
/* 139 */     return fileNameUtil.getUncompressedFilename(fileName);
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
/*     */   public static String getCompressedFilename(String fileName) {
/* 154 */     return fileNameUtil.getCompressedFilename(fileName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setCacheXZAvailablity(boolean doCache) {
/* 165 */     if (!doCache) {
/* 166 */       cachedXZAvailability = CachedAvailability.DONT_CACHE;
/* 167 */     } else if (cachedXZAvailability == CachedAvailability.DONT_CACHE) {
/* 168 */       boolean hasXz = internalIsXZCompressionAvailable();
/* 169 */       cachedXZAvailability = hasXz ? CachedAvailability.CACHED_AVAILABLE : CachedAvailability.CACHED_UNAVAILABLE;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static CachedAvailability getCachedXZAvailability() {
/* 176 */     return cachedXZAvailability;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\compressors\xz\XZUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */