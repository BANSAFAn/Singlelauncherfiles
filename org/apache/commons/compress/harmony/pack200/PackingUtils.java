/*     */ package org.apache.commons.compress.harmony.pack200;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.jar.JarInputStream;
/*     */ import java.util.jar.JarOutputStream;
/*     */ import java.util.jar.Manifest;
/*     */ import java.util.logging.FileHandler;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.LogManager;
/*     */ import java.util.logging.LogRecord;
/*     */ import java.util.logging.Logger;
/*     */ import java.util.logging.SimpleFormatter;
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
/*     */ public class PackingUtils
/*     */ {
/*  47 */   private static PackingLogger packingLogger = new PackingLogger("org.harmony.apache.pack200", null); static {
/*  48 */     LogManager.getLogManager().addLogger(packingLogger);
/*     */   }
/*     */   
/*     */   private static class PackingLogger
/*     */     extends Logger {
/*     */     private boolean verbose = false;
/*     */     
/*     */     protected PackingLogger(String name, String resourceBundleName) {
/*  56 */       super(name, resourceBundleName);
/*     */     }
/*     */ 
/*     */     
/*     */     public void log(LogRecord logRecord) {
/*  61 */       if (this.verbose) {
/*  62 */         super.log(logRecord);
/*     */       }
/*     */     }
/*     */     
/*     */     public void setVerbose(boolean isVerbose) {
/*  67 */       this.verbose = isVerbose;
/*     */     }
/*     */   }
/*     */   
/*     */   public static void config(PackingOptions options) throws IOException {
/*  72 */     String logFileName = options.getLogFile();
/*  73 */     if (logFileName != null) {
/*  74 */       FileHandler fileHandler = new FileHandler(logFileName, false);
/*  75 */       fileHandler.setFormatter(new SimpleFormatter());
/*  76 */       packingLogger.addHandler(fileHandler);
/*  77 */       packingLogger.setUseParentHandlers(false);
/*     */     } 
/*     */     
/*  80 */     packingLogger.setVerbose(options.isVerbose());
/*     */   }
/*     */   
/*     */   public static void log(String message) {
/*  84 */     packingLogger.log(Level.INFO, message);
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
/*     */   public static void copyThroughJar(JarInputStream jarInputStream, OutputStream outputStream) throws IOException {
/*  96 */     Manifest manifest = jarInputStream.getManifest();
/*  97 */     try (JarOutputStream jarOutputStream = new JarOutputStream(outputStream, manifest)) {
/*  98 */       jarOutputStream.setComment("PACK200");
/*  99 */       log("Packed META-INF/MANIFEST.MF");
/*     */       
/* 101 */       byte[] bytes = new byte[16384];
/*     */       
/*     */       JarEntry jarEntry;
/* 104 */       while ((jarEntry = jarInputStream.getNextJarEntry()) != null) {
/* 105 */         jarOutputStream.putNextEntry(jarEntry); int bytesRead;
/* 106 */         while ((bytesRead = jarInputStream.read(bytes)) != -1) {
/* 107 */           jarOutputStream.write(bytes, 0, bytesRead);
/*     */         }
/* 109 */         log("Packed " + jarEntry.getName());
/*     */       } 
/* 111 */       jarInputStream.close();
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
/*     */   public static void copyThroughJar(JarFile jarFile, OutputStream outputStream) throws IOException {
/* 123 */     try (JarOutputStream jarOutputStream = new JarOutputStream(outputStream)) {
/* 124 */       jarOutputStream.setComment("PACK200");
/* 125 */       byte[] bytes = new byte[16384];
/* 126 */       Enumeration<JarEntry> entries = jarFile.entries();
/* 127 */       while (entries.hasMoreElements()) {
/* 128 */         JarEntry jarEntry = entries.nextElement();
/* 129 */         jarOutputStream.putNextEntry(jarEntry);
/* 130 */         try (InputStream inputStream = jarFile.getInputStream(jarEntry)) {
/*     */           int bytesRead;
/* 132 */           while ((bytesRead = inputStream.read(bytes)) != -1) {
/* 133 */             jarOutputStream.write(bytes, 0, bytesRead);
/*     */           }
/* 135 */           jarOutputStream.closeEntry();
/* 136 */           log("Packed " + jarEntry.getName());
/*     */         } 
/*     */       } 
/* 139 */       jarFile.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static List<Archive.PackingFile> getPackingFileListFromJar(JarInputStream jarInputStream, boolean keepFileOrder) throws IOException {
/* 145 */     List<Archive.PackingFile> packingFileList = new ArrayList<>();
/*     */ 
/*     */     
/* 148 */     Manifest manifest = jarInputStream.getManifest();
/* 149 */     if (manifest != null) {
/* 150 */       ByteArrayOutputStream baos = new ByteArrayOutputStream();
/* 151 */       manifest.write(baos);
/* 152 */       packingFileList.add(new Archive.PackingFile("META-INF/MANIFEST.MF", baos.toByteArray(), 0L));
/*     */     } 
/*     */ 
/*     */     
/*     */     JarEntry jarEntry;
/*     */     
/* 158 */     while ((jarEntry = jarInputStream.getNextJarEntry()) != null) {
/* 159 */       byte[] bytes = readJarEntry(jarEntry, new BufferedInputStream(jarInputStream));
/* 160 */       packingFileList.add(new Archive.PackingFile(bytes, jarEntry));
/*     */     } 
/*     */ 
/*     */     
/* 164 */     if (!keepFileOrder) {
/* 165 */       reorderPackingFiles(packingFileList);
/*     */     }
/* 167 */     return packingFileList;
/*     */   }
/*     */ 
/*     */   
/*     */   public static List<Archive.PackingFile> getPackingFileListFromJar(JarFile jarFile, boolean keepFileOrder) throws IOException {
/* 172 */     List<Archive.PackingFile> packingFileList = new ArrayList<>();
/* 173 */     Enumeration<JarEntry> jarEntries = jarFile.entries();
/* 174 */     while (jarEntries.hasMoreElements()) {
/* 175 */       JarEntry jarEntry = jarEntries.nextElement();
/* 176 */       try (InputStream inputStream = jarFile.getInputStream(jarEntry)) {
/* 177 */         byte[] bytes = readJarEntry(jarEntry, new BufferedInputStream(inputStream));
/* 178 */         packingFileList.add(new Archive.PackingFile(bytes, jarEntry));
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 183 */     if (!keepFileOrder) {
/* 184 */       reorderPackingFiles(packingFileList);
/*     */     }
/* 186 */     return packingFileList;
/*     */   }
/*     */   
/*     */   private static byte[] readJarEntry(JarEntry jarEntry, InputStream inputStream) throws IOException {
/* 190 */     long size = jarEntry.getSize();
/* 191 */     if (size > 2147483647L)
/*     */     {
/* 193 */       throw new IllegalArgumentException("Large Class!");
/*     */     }
/* 195 */     if (size < 0L) {
/* 196 */       size = 0L;
/*     */     }
/* 198 */     byte[] bytes = new byte[(int)size];
/* 199 */     if (inputStream.read(bytes) != size) {
/* 200 */       throw new IllegalArgumentException("Error reading from stream");
/*     */     }
/* 202 */     return bytes;
/*     */   }
/*     */   
/*     */   private static void reorderPackingFiles(List<Archive.PackingFile> packingFileList) {
/* 206 */     Iterator<Archive.PackingFile> iterator = packingFileList.iterator();
/* 207 */     while (iterator.hasNext()) {
/* 208 */       Archive.PackingFile packingFile = iterator.next();
/* 209 */       if (packingFile.isDirectory())
/*     */       {
/* 211 */         iterator.remove();
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 217 */     packingFileList.sort((arg0, arg1) -> {
/*     */           String fileName0 = arg0.getName();
/*     */           String fileName1 = arg1.getName();
/*     */           return fileName0.equals(fileName1) ? 0 : ("META-INF/MANIFEST.MF".equals(fileName0) ? -1 : ("META-INF/MANIFEST.MF".equals(fileName1) ? 1 : fileName0.compareTo(fileName1)));
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\harmony\pack200\PackingUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */