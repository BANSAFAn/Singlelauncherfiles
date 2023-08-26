/*     */ package org.apache.commons.compress.archivers;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.file.Files;
/*     */ import java.util.Enumeration;
/*     */ import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
/*     */ import org.apache.commons.compress.archivers.sevenz.SevenZFile;
/*     */ import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
/*     */ import org.apache.commons.compress.archivers.tar.TarFile;
/*     */ import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
/*     */ import org.apache.commons.compress.archivers.zip.ZipFile;
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
/*     */ public final class Lister
/*     */ {
/*  43 */   private static final ArchiveStreamFactory FACTORY = ArchiveStreamFactory.DEFAULT;
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
/*     */   public static void main(String[] args) throws ArchiveException, IOException {
/*  59 */     if (args.length == 0) {
/*  60 */       usage();
/*     */       return;
/*     */     } 
/*  63 */     System.out.println("Analysing " + args[0]);
/*  64 */     File f = new File(args[0]);
/*  65 */     if (!f.isFile()) {
/*  66 */       System.err.println(f + " doesn't exist or is a directory");
/*     */     }
/*  68 */     String format = (args.length > 1) ? args[1] : detectFormat(f);
/*  69 */     if ("7z".equalsIgnoreCase(format)) {
/*  70 */       list7z(f);
/*  71 */     } else if ("zipfile".equals(format)) {
/*  72 */       listZipUsingZipFile(f);
/*  73 */     } else if ("tarfile".equals(format)) {
/*  74 */       listZipUsingTarFile(f);
/*     */     } else {
/*  76 */       listStream(f, args);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void listStream(File f, String[] args) throws ArchiveException, IOException {
/*  81 */     try(InputStream fis = new BufferedInputStream(Files.newInputStream(f.toPath(), new java.nio.file.OpenOption[0])); 
/*  82 */         ArchiveInputStream ais = createArchiveInputStream(args, fis)) {
/*  83 */       System.out.println("Created " + ais.toString());
/*     */       ArchiveEntry ae;
/*  85 */       while ((ae = ais.getNextEntry()) != null) {
/*  86 */         System.out.println(ae.getName());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static ArchiveInputStream createArchiveInputStream(String[] args, InputStream fis) throws ArchiveException {
/*  93 */     if (args.length > 1) {
/*  94 */       return FACTORY.createArchiveInputStream(args[1], fis);
/*     */     }
/*  96 */     return FACTORY.createArchiveInputStream(fis);
/*     */   }
/*     */   
/*     */   private static String detectFormat(File f) throws ArchiveException, IOException {
/* 100 */     try (InputStream fis = new BufferedInputStream(Files.newInputStream(f.toPath(), new java.nio.file.OpenOption[0]))) {
/* 101 */       return ArchiveStreamFactory.detect(fis);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void list7z(File f) throws IOException {
/* 106 */     try (SevenZFile z = new SevenZFile(f)) {
/* 107 */       System.out.println("Created " + z);
/*     */       SevenZArchiveEntry sevenZArchiveEntry;
/* 109 */       while ((sevenZArchiveEntry = z.getNextEntry()) != null) {
/*     */         
/* 111 */         String name = (sevenZArchiveEntry.getName() == null) ? (z.getDefaultName() + " (entry name was null)") : sevenZArchiveEntry.getName();
/* 112 */         System.out.println(name);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void listZipUsingZipFile(File f) throws IOException {
/* 118 */     try (ZipFile z = new ZipFile(f)) {
/* 119 */       System.out.println("Created " + z);
/* 120 */       for (Enumeration<ZipArchiveEntry> en = z.getEntries(); en.hasMoreElements();) {
/* 121 */         System.out.println(((ZipArchiveEntry)en.nextElement()).getName());
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void listZipUsingTarFile(File f) throws IOException {
/* 127 */     try (TarFile t = new TarFile(f)) {
/* 128 */       System.out.println("Created " + t);
/* 129 */       t.getEntries().forEach(en -> System.out.println(en.getName()));
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void usage() {
/* 134 */     System.out.println("Parameters: archive-name [archive-type]\n");
/* 135 */     System.out.println("The magic archive-type 'zipfile' prefers ZipFile over ZipArchiveInputStream");
/* 136 */     System.out.println("The magic archive-type 'tarfile' prefers TarFile over TarArchiveInputStream");
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\archivers\Lister.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */