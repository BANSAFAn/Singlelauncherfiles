/*     */ package org.apache.commons.io.output;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.file.Files;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.apache.commons.io.IOUtils;
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
/*     */ public class DeferredFileOutputStream
/*     */   extends ThresholdingOutputStream
/*     */ {
/*     */   private ByteArrayOutputStream memoryOutputStream;
/*     */   private OutputStream currentOutputStream;
/*     */   private File outputFile;
/*     */   private final String prefix;
/*     */   private final String suffix;
/*     */   private final File directory;
/*     */   private boolean closed;
/*     */   
/*     */   public DeferredFileOutputStream(int threshold, File outputFile) {
/*  84 */     this(threshold, outputFile, null, null, null, 1024);
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
/*     */   private DeferredFileOutputStream(int threshold, File outputFile, String prefix, String suffix, File directory, int initialBufferSize) {
/* 100 */     super(threshold);
/* 101 */     this.outputFile = outputFile;
/* 102 */     this.prefix = prefix;
/* 103 */     this.suffix = suffix;
/* 104 */     this.directory = directory;
/*     */     
/* 106 */     this.memoryOutputStream = new ByteArrayOutputStream(initialBufferSize);
/* 107 */     this.currentOutputStream = this.memoryOutputStream;
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
/*     */   public DeferredFileOutputStream(int threshold, int initialBufferSize, File outputFile) {
/* 121 */     this(threshold, outputFile, null, null, null, initialBufferSize);
/* 122 */     if (initialBufferSize < 0) {
/* 123 */       throw new IllegalArgumentException("Initial buffer size must be atleast 0.");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DeferredFileOutputStream(int threshold, int initialBufferSize, String prefix, String suffix, File directory) {
/* 141 */     this(threshold, null, prefix, suffix, directory, initialBufferSize);
/* 142 */     if (prefix == null) {
/* 143 */       throw new IllegalArgumentException("Temporary file prefix is missing");
/*     */     }
/* 145 */     if (initialBufferSize < 0) {
/* 146 */       throw new IllegalArgumentException("Initial buffer size must be atleast 0.");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DeferredFileOutputStream(int threshold, String prefix, String suffix, File directory) {
/* 164 */     this(threshold, null, prefix, suffix, directory, 1024);
/* 165 */     if (prefix == null) {
/* 166 */       throw new IllegalArgumentException("Temporary file prefix is missing");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 177 */     super.close();
/* 178 */     this.closed = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getData() {
/* 188 */     return (this.memoryOutputStream != null) ? this.memoryOutputStream.toByteArray() : null;
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
/*     */   public File getFile() {
/* 203 */     return this.outputFile;
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
/*     */   protected OutputStream getStream() throws IOException {
/* 216 */     return this.currentOutputStream;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInMemory() {
/* 225 */     return !isThresholdExceeded();
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
/*     */   protected void thresholdReached() throws IOException {
/* 237 */     if (this.prefix != null) {
/* 238 */       this.outputFile = File.createTempFile(this.prefix, this.suffix, this.directory);
/*     */     }
/* 240 */     FileUtils.forceMkdirParent(this.outputFile);
/* 241 */     OutputStream fos = Files.newOutputStream(this.outputFile.toPath(), new java.nio.file.OpenOption[0]);
/*     */     try {
/* 243 */       this.memoryOutputStream.writeTo(fos);
/* 244 */     } catch (IOException e) {
/* 245 */       fos.close();
/* 246 */       throw e;
/*     */     } 
/* 248 */     this.currentOutputStream = fos;
/* 249 */     this.memoryOutputStream = null;
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
/*     */   public InputStream toInputStream() throws IOException {
/* 270 */     if (!this.closed) {
/* 271 */       throw new IOException("Stream not closed");
/*     */     }
/*     */     
/* 274 */     if (isInMemory()) {
/* 275 */       return this.memoryOutputStream.toInputStream();
/*     */     }
/* 277 */     return Files.newInputStream(this.outputFile.toPath(), new java.nio.file.OpenOption[0]);
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
/*     */   public void writeTo(OutputStream outputStream) throws IOException {
/* 291 */     if (!this.closed) {
/* 292 */       throw new IOException("Stream not closed");
/*     */     }
/*     */     
/* 295 */     if (isInMemory()) {
/* 296 */       this.memoryOutputStream.writeTo(outputStream);
/*     */     } else {
/* 298 */       try (InputStream fis = Files.newInputStream(this.outputFile.toPath(), new java.nio.file.OpenOption[0])) {
/* 299 */         IOUtils.copy(fis, outputStream);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\output\DeferredFileOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */