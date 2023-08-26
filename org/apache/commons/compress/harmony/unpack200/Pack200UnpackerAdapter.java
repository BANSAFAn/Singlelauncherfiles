/*    */ package org.apache.commons.compress.harmony.unpack200;
/*    */ 
/*    */ import java.io.BufferedInputStream;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.nio.file.Files;
/*    */ import java.util.jar.JarOutputStream;
/*    */ import org.apache.commons.compress.harmony.pack200.Pack200Adapter;
/*    */ import org.apache.commons.compress.harmony.pack200.Pack200Exception;
/*    */ import org.apache.commons.compress.java.util.jar.Pack200;
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
/*    */ public class Pack200UnpackerAdapter
/*    */   extends Pack200Adapter
/*    */   implements Pack200.Unpacker
/*    */ {
/*    */   public void unpack(InputStream in, JarOutputStream out) throws IOException {
/* 44 */     if (in == null || out == null) {
/* 45 */       throw new IllegalArgumentException("Must specify both input and output streams");
/*    */     }
/* 47 */     completed(0.0D);
/*    */     try {
/* 49 */       (new Archive(in, out)).unpack();
/* 50 */     } catch (Pack200Exception e) {
/* 51 */       throw new IOException("Failed to unpack Jar:" + e);
/*    */     } 
/* 53 */     completed(1.0D);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void unpack(File file, JarOutputStream out) throws IOException {
/* 64 */     if (file == null || out == null) {
/* 65 */       throw new IllegalArgumentException("Must specify both input and output streams");
/*    */     }
/* 67 */     int size = (int)file.length();
/* 68 */     int bufferSize = (size > 0 && size < 8192) ? size : 8192;
/* 69 */     try (InputStream in = new BufferedInputStream(Files.newInputStream(file.toPath(), new java.nio.file.OpenOption[0]), bufferSize)) {
/* 70 */       unpack(in, out);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\harmon\\unpack200\Pack200UnpackerAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */