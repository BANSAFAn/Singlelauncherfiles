/*     */ package org.apache.commons.io.input;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.util.Objects;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RandomAccessFileInputStream
/*     */   extends InputStream
/*     */ {
/*     */   private final boolean closeOnClose;
/*     */   private final RandomAccessFile randomAccessFile;
/*     */   
/*     */   public RandomAccessFileInputStream(RandomAccessFile file) {
/*  41 */     this(file, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RandomAccessFileInputStream(RandomAccessFile file, boolean closeOnClose) {
/*  51 */     this.randomAccessFile = Objects.<RandomAccessFile>requireNonNull(file, "file");
/*  52 */     this.closeOnClose = closeOnClose;
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
/*     */   public int available() throws IOException {
/*  65 */     long avail = availableLong();
/*  66 */     if (avail > 2147483647L) {
/*  67 */       return Integer.MAX_VALUE;
/*     */     }
/*  69 */     return (int)avail;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long availableLong() throws IOException {
/*  79 */     return this.randomAccessFile.length() - this.randomAccessFile.getFilePointer();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*  84 */     super.close();
/*  85 */     if (this.closeOnClose) {
/*  86 */       this.randomAccessFile.close();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RandomAccessFile getRandomAccessFile() {
/*  96 */     return this.randomAccessFile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCloseOnClose() {
/* 105 */     return this.closeOnClose;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/* 110 */     return this.randomAccessFile.read();
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] bytes) throws IOException {
/* 115 */     return this.randomAccessFile.read(bytes);
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] bytes, int offset, int length) throws IOException {
/* 120 */     return this.randomAccessFile.read(bytes, offset, length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void seek(long position) throws IOException {
/* 131 */     this.randomAccessFile.seek(position);
/*     */   }
/*     */ 
/*     */   
/*     */   public long skip(long skipCount) throws IOException {
/* 136 */     if (skipCount <= 0L) {
/* 137 */       return 0L;
/*     */     }
/* 139 */     long filePointer = this.randomAccessFile.getFilePointer();
/* 140 */     long fileLength = this.randomAccessFile.length();
/* 141 */     if (filePointer >= fileLength) {
/* 142 */       return 0L;
/*     */     }
/* 144 */     long targetPos = filePointer + skipCount;
/* 145 */     long newPos = (targetPos > fileLength) ? (fileLength - 1L) : targetPos;
/* 146 */     if (newPos > 0L) {
/* 147 */       seek(newPos);
/*     */     }
/* 149 */     return this.randomAccessFile.getFilePointer() - filePointer;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\input\RandomAccessFileInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */