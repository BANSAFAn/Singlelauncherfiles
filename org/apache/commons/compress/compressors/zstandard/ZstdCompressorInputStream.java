/*     */ package org.apache.commons.compress.compressors.zstandard;
/*     */ 
/*     */ import com.github.luben.zstd.BufferPool;
/*     */ import com.github.luben.zstd.ZstdInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import org.apache.commons.compress.compressors.CompressorInputStream;
/*     */ import org.apache.commons.compress.utils.CountingInputStream;
/*     */ import org.apache.commons.compress.utils.IOUtils;
/*     */ import org.apache.commons.compress.utils.InputStreamStatistics;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ZstdCompressorInputStream
/*     */   extends CompressorInputStream
/*     */   implements InputStreamStatistics
/*     */ {
/*     */   private final CountingInputStream countingStream;
/*     */   private final ZstdInputStream decIS;
/*     */   
/*     */   public ZstdCompressorInputStream(InputStream in) throws IOException {
/*  45 */     this.decIS = new ZstdInputStream((InputStream)(this.countingStream = new CountingInputStream(in)));
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
/*     */   public ZstdCompressorInputStream(InputStream in, BufferPool bufferPool) throws IOException {
/*  61 */     this.decIS = new ZstdInputStream((InputStream)(this.countingStream = new CountingInputStream(in)), bufferPool);
/*     */   }
/*     */ 
/*     */   
/*     */   public int available() throws IOException {
/*  66 */     return this.decIS.available();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*  71 */     this.decIS.close();
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] b) throws IOException {
/*  76 */     return read(b, 0, b.length);
/*     */   }
/*     */ 
/*     */   
/*     */   public long skip(long n) throws IOException {
/*  81 */     return IOUtils.skip((InputStream)this.decIS, n);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void mark(int readlimit) {
/*  86 */     this.decIS.mark(readlimit);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean markSupported() {
/*  91 */     return this.decIS.markSupported();
/*     */   }
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/*  96 */     int ret = this.decIS.read();
/*  97 */     count((ret == -1) ? 0 : 1);
/*  98 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] buf, int off, int len) throws IOException {
/* 103 */     if (len == 0) {
/* 104 */       return 0;
/*     */     }
/* 106 */     int ret = this.decIS.read(buf, off, len);
/* 107 */     count(ret);
/* 108 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 113 */     return this.decIS.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void reset() throws IOException {
/* 118 */     this.decIS.reset();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getCompressedCount() {
/* 126 */     return this.countingStream.getBytesRead();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\compressors\zstandard\ZstdCompressorInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */