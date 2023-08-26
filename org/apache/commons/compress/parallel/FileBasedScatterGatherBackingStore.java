/*    */ package org.apache.commons.compress.parallel;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ import java.io.UncheckedIOException;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
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
/*    */ public class FileBasedScatterGatherBackingStore
/*    */   implements ScatterGatherBackingStore
/*    */ {
/*    */   private final Path target;
/*    */   private final OutputStream outputStream;
/*    */   private boolean closed;
/*    */   
/*    */   public FileBasedScatterGatherBackingStore(File target) throws FileNotFoundException {
/* 40 */     this(target.toPath());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public FileBasedScatterGatherBackingStore(Path target) throws FileNotFoundException {
/* 51 */     this.target = target;
/*    */     try {
/* 53 */       this.outputStream = Files.newOutputStream(target, new java.nio.file.OpenOption[0]);
/* 54 */     } catch (FileNotFoundException ex) {
/* 55 */       throw ex;
/* 56 */     } catch (IOException ex) {
/*    */       
/* 58 */       throw new UncheckedIOException(ex);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public InputStream getInputStream() throws IOException {
/* 64 */     return Files.newInputStream(this.target, new java.nio.file.OpenOption[0]);
/*    */   }
/*    */ 
/*    */   
/*    */   public void closeForWriting() throws IOException {
/* 69 */     if (!this.closed) {
/* 70 */       this.outputStream.close();
/* 71 */       this.closed = true;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void writeOut(byte[] data, int offset, int length) throws IOException {
/* 77 */     this.outputStream.write(data, offset, length);
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/*    */     try {
/* 83 */       closeForWriting();
/*    */     } finally {
/* 85 */       Files.deleteIfExists(this.target);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\parallel\FileBasedScatterGatherBackingStore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */