/*     */ package org.apache.commons.compress.compressors.pack200;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.file.Files;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.jar.JarOutputStream;
/*     */ import org.apache.commons.compress.java.util.jar.Pack200;
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
/*     */ public class Pack200Utils
/*     */ {
/*     */   public static void normalize(File jar) throws IOException {
/*  61 */     normalize(jar, jar, null);
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
/*     */   
/*     */   public static void normalize(File jar, Map<String, String> props) throws IOException {
/*  82 */     normalize(jar, jar, props);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void normalize(File from, File to) throws IOException {
/* 107 */     normalize(from, to, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void normalize(File from, File to, Map<String, String> props) throws IOException {
/* 131 */     if (props == null) {
/* 132 */       props = new HashMap<>();
/*     */     }
/* 134 */     props.put("pack.segment.limit", "-1");
/* 135 */     File tempFile = File.createTempFile("commons-compress", "pack200normalize");
/*     */     try {
/* 137 */       try(OutputStream fos = Files.newOutputStream(tempFile.toPath(), new java.nio.file.OpenOption[0]); 
/* 138 */           JarFile jarFile = new JarFile(from)) {
/* 139 */         Pack200.Packer packer = Pack200.newPacker();
/* 140 */         packer.properties().putAll(props);
/* 141 */         packer.pack(jarFile, fos);
/*     */       } 
/* 143 */       Pack200.Unpacker unpacker = Pack200.newUnpacker();
/* 144 */       try (JarOutputStream jos = new JarOutputStream(Files.newOutputStream(to.toPath(), new java.nio.file.OpenOption[0]))) {
/* 145 */         unpacker.unpack(tempFile, jos);
/*     */       } 
/*     */     } finally {
/* 148 */       if (!tempFile.delete())
/* 149 */         tempFile.deleteOnExit(); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\compressors\pack200\Pack200Utils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */