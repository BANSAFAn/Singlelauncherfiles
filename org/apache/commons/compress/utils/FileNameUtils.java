/*     */ package org.apache.commons.compress.utils;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.nio.file.Path;
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
/*     */ public class FileNameUtils
/*     */ {
/*     */   private static String fileNameToBaseName(String name) {
/*  31 */     int extensionIndex = name.lastIndexOf('.');
/*  32 */     return (extensionIndex < 0) ? name : name.substring(0, extensionIndex);
/*     */   }
/*     */   
/*     */   private static String fileNameToExtension(String name) {
/*  36 */     int extensionIndex = name.lastIndexOf('.');
/*  37 */     return (extensionIndex < 0) ? "" : name.substring(extensionIndex + 1);
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
/*     */   public static String getBaseName(Path path) {
/*  51 */     if (path == null) {
/*  52 */       return null;
/*     */     }
/*  54 */     return fileNameToBaseName(path.getFileName().toString());
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
/*     */   public static String getBaseName(String filename) {
/*  69 */     if (filename == null) {
/*  70 */       return null;
/*     */     }
/*  72 */     return fileNameToBaseName((new File(filename)).getName());
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
/*     */   public static String getExtension(Path path) {
/*  86 */     if (path == null) {
/*  87 */       return null;
/*     */     }
/*  89 */     return fileNameToExtension(path.getFileName().toString());
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
/*     */   public static String getExtension(String filename) {
/* 104 */     if (filename == null) {
/* 105 */       return null;
/*     */     }
/* 107 */     return fileNameToExtension((new File(filename)).getName());
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compres\\utils\FileNameUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */