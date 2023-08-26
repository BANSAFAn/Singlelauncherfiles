/*     */ package org.apache.commons.io.output;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.Charset;
/*     */ import org.apache.commons.io.Charsets;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LockableFileWriter
/*     */   extends Writer
/*     */ {
/*     */   private static final String LCK = ".lck";
/*     */   private final Writer out;
/*     */   private final File lockFile;
/*     */   
/*     */   public LockableFileWriter(String fileName) throws IOException {
/*  71 */     this(fileName, false, (String)null);
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
/*     */   public LockableFileWriter(String fileName, boolean append) throws IOException {
/*  83 */     this(fileName, append, (String)null);
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
/*     */   public LockableFileWriter(String fileName, boolean append, String lockDir) throws IOException {
/*  96 */     this(new File(fileName), append, lockDir);
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
/*     */   public LockableFileWriter(File file) throws IOException {
/* 108 */     this(file, false, (String)null);
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
/*     */   public LockableFileWriter(File file, boolean append) throws IOException {
/* 120 */     this(file, append, (String)null);
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
/*     */   @Deprecated
/*     */   public LockableFileWriter(File file, boolean append, String lockDir) throws IOException {
/* 135 */     this(file, Charset.defaultCharset(), append, lockDir);
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
/*     */   public LockableFileWriter(File file, Charset charset) throws IOException {
/* 148 */     this(file, charset, false, (String)null);
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
/*     */   public LockableFileWriter(File file, String charsetName) throws IOException {
/* 163 */     this(file, charsetName, false, (String)null);
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
/*     */   public LockableFileWriter(File file, Charset charset, boolean append, String lockDir) throws IOException {
/* 180 */     file = file.getAbsoluteFile();
/* 181 */     if (file.getParentFile() != null) {
/* 182 */       FileUtils.forceMkdir(file.getParentFile());
/*     */     }
/* 184 */     if (file.isDirectory()) {
/* 185 */       throw new IOException("File specified is a directory");
/*     */     }
/*     */ 
/*     */     
/* 189 */     if (lockDir == null) {
/* 190 */       lockDir = System.getProperty("java.io.tmpdir");
/*     */     }
/* 192 */     File lockDirFile = new File(lockDir);
/* 193 */     FileUtils.forceMkdir(lockDirFile);
/* 194 */     testLockDir(lockDirFile);
/* 195 */     this.lockFile = new File(lockDirFile, file.getName() + ".lck");
/*     */ 
/*     */     
/* 198 */     createLock();
/*     */ 
/*     */     
/* 201 */     this.out = initWriter(file, charset, append);
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
/*     */   public LockableFileWriter(File file, String charsetName, boolean append, String lockDir) throws IOException {
/* 219 */     this(file, Charsets.toCharset(charsetName), append, lockDir);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void testLockDir(File lockDir) throws IOException {
/* 230 */     if (!lockDir.exists()) {
/* 231 */       throw new IOException("Could not find lockDir: " + lockDir
/* 232 */           .getAbsolutePath());
/*     */     }
/* 234 */     if (!lockDir.canWrite()) {
/* 235 */       throw new IOException("Could not write to lockDir: " + lockDir
/* 236 */           .getAbsolutePath());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void createLock() throws IOException {
/* 246 */     synchronized (LockableFileWriter.class) {
/* 247 */       if (!this.lockFile.createNewFile()) {
/* 248 */         throw new IOException("Can't write file, lock " + this.lockFile
/* 249 */             .getAbsolutePath() + " exists");
/*     */       }
/* 251 */       this.lockFile.deleteOnExit();
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
/*     */   private Writer initWriter(File file, Charset charset, boolean append) throws IOException {
/* 266 */     boolean fileExistedAlready = file.exists();
/*     */     try {
/* 268 */       return new OutputStreamWriter(new FileOutputStream(file.getAbsolutePath(), append), 
/* 269 */           Charsets.toCharset(charset));
/*     */     }
/* 271 */     catch (IOException|RuntimeException ex) {
/* 272 */       FileUtils.deleteQuietly(this.lockFile);
/* 273 */       if (!fileExistedAlready) {
/* 274 */         FileUtils.deleteQuietly(file);
/*     */       }
/* 276 */       throw ex;
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
/*     */     try {
/* 288 */       this.out.close();
/*     */     } finally {
/* 290 */       FileUtils.delete(this.lockFile);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(int c) throws IOException {
/* 301 */     this.out.write(c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(char[] cbuf) throws IOException {
/* 311 */     this.out.write(cbuf);
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
/*     */   public void write(char[] cbuf, int off, int len) throws IOException {
/* 323 */     this.out.write(cbuf, off, len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(String str) throws IOException {
/* 333 */     this.out.write(str);
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
/*     */   public void write(String str, int off, int len) throws IOException {
/* 345 */     this.out.write(str, off, len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/* 354 */     this.out.flush();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\output\LockableFileWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */