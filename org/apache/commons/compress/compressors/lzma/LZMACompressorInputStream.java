/*     */ package org.apache.commons.compress.compressors.lzma;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import org.apache.commons.compress.MemoryLimitException;
/*     */ import org.apache.commons.compress.compressors.CompressorInputStream;
/*     */ import org.apache.commons.compress.utils.CountingInputStream;
/*     */ import org.apache.commons.compress.utils.IOUtils;
/*     */ import org.apache.commons.compress.utils.InputStreamStatistics;
/*     */ import org.tukaani.xz.LZMAInputStream;
/*     */ import org.tukaani.xz.MemoryLimitException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LZMACompressorInputStream
/*     */   extends CompressorInputStream
/*     */   implements InputStreamStatistics
/*     */ {
/*     */   private final CountingInputStream countingStream;
/*     */   private final InputStream in;
/*     */   
/*     */   public LZMACompressorInputStream(InputStream inputStream) throws IOException {
/*  55 */     this.in = (InputStream)new LZMAInputStream((InputStream)(this.countingStream = new CountingInputStream(inputStream)), -1);
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
/*     */   public LZMACompressorInputStream(InputStream inputStream, int memoryLimitInKb) throws IOException {
/*     */     try {
/*  78 */       this.in = (InputStream)new LZMAInputStream((InputStream)(this.countingStream = new CountingInputStream(inputStream)), memoryLimitInKb);
/*  79 */     } catch (MemoryLimitException e) {
/*     */       
/*  81 */       throw new MemoryLimitException(e.getMemoryNeeded(), e.getMemoryLimit(), e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/*  88 */     int ret = this.in.read();
/*  89 */     count((ret == -1) ? 0 : 1);
/*  90 */     return ret;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int read(byte[] buf, int off, int len) throws IOException {
/*  96 */     int ret = this.in.read(buf, off, len);
/*  97 */     count(ret);
/*  98 */     return ret;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long skip(long n) throws IOException {
/* 104 */     return IOUtils.skip(this.in, n);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int available() throws IOException {
/* 110 */     return this.in.available();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 116 */     this.in.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getCompressedCount() {
/* 124 */     return this.countingStream.getBytesRead();
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
/*     */   public static boolean matches(byte[] signature, int length) {
/* 139 */     return (signature != null && length >= 3 && signature[0] == 93 && signature[1] == 0 && signature[2] == 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\compressors\lzma\LZMACompressorInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */