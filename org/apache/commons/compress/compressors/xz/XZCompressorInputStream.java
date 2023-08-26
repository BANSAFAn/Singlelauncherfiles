/*     */ package org.apache.commons.compress.compressors.xz;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import org.apache.commons.compress.MemoryLimitException;
/*     */ import org.apache.commons.compress.compressors.CompressorInputStream;
/*     */ import org.apache.commons.compress.utils.CountingInputStream;
/*     */ import org.apache.commons.compress.utils.IOUtils;
/*     */ import org.apache.commons.compress.utils.InputStreamStatistics;
/*     */ import org.tukaani.xz.MemoryLimitException;
/*     */ import org.tukaani.xz.SingleXZInputStream;
/*     */ import org.tukaani.xz.XZ;
/*     */ import org.tukaani.xz.XZInputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XZCompressorInputStream
/*     */   extends CompressorInputStream
/*     */   implements InputStreamStatistics
/*     */ {
/*     */   private final CountingInputStream countingStream;
/*     */   private final InputStream in;
/*     */   
/*     */   public static boolean matches(byte[] signature, int length) {
/*  51 */     if (length < XZ.HEADER_MAGIC.length) {
/*  52 */       return false;
/*     */     }
/*     */     
/*  55 */     for (int i = 0; i < XZ.HEADER_MAGIC.length; i++) {
/*  56 */       if (signature[i] != XZ.HEADER_MAGIC[i]) {
/*  57 */         return false;
/*     */       }
/*     */     } 
/*     */     
/*  61 */     return true;
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
/*     */   public XZCompressorInputStream(InputStream inputStream) throws IOException {
/*  79 */     this(inputStream, false);
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
/*     */   public XZCompressorInputStream(InputStream inputStream, boolean decompressConcatenated) throws IOException {
/* 102 */     this(inputStream, decompressConcatenated, -1);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public XZCompressorInputStream(InputStream inputStream, boolean decompressConcatenated, int memoryLimitInKb) throws IOException {
/* 130 */     this.countingStream = new CountingInputStream(inputStream);
/* 131 */     if (decompressConcatenated) {
/* 132 */       this.in = (InputStream)new XZInputStream((InputStream)this.countingStream, memoryLimitInKb);
/*     */     } else {
/* 134 */       this.in = (InputStream)new SingleXZInputStream((InputStream)this.countingStream, memoryLimitInKb);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/*     */     try {
/* 141 */       int ret = this.in.read();
/* 142 */       count((ret == -1) ? -1 : 1);
/* 143 */       return ret;
/* 144 */     } catch (MemoryLimitException e) {
/* 145 */       throw new MemoryLimitException(e.getMemoryNeeded(), e.getMemoryLimit(), e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] buf, int off, int len) throws IOException {
/* 151 */     if (len == 0) {
/* 152 */       return 0;
/*     */     }
/*     */     try {
/* 155 */       int ret = this.in.read(buf, off, len);
/* 156 */       count(ret);
/* 157 */       return ret;
/* 158 */     } catch (MemoryLimitException e) {
/*     */       
/* 160 */       throw new MemoryLimitException(e.getMemoryNeeded(), e.getMemoryLimit(), e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public long skip(long n) throws IOException {
/*     */     try {
/* 167 */       return IOUtils.skip(this.in, n);
/* 168 */     } catch (MemoryLimitException e) {
/*     */       
/* 170 */       throw new MemoryLimitException(e.getMemoryNeeded(), e.getMemoryLimit(), e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int available() throws IOException {
/* 176 */     return this.in.available();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 181 */     this.in.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getCompressedCount() {
/* 189 */     return this.countingStream.getBytesRead();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\compressors\xz\XZCompressorInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */