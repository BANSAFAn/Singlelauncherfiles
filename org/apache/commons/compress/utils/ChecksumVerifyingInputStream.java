/*     */ package org.apache.commons.compress.utils;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.zip.Checksum;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ChecksumVerifyingInputStream
/*     */   extends InputStream
/*     */ {
/*     */   private final InputStream in;
/*     */   private long bytesRemaining;
/*     */   private final long expectedChecksum;
/*     */   private final Checksum checksum;
/*     */   
/*     */   public ChecksumVerifyingInputStream(Checksum checksum, InputStream in, long size, long expectedChecksum) {
/*  47 */     this.checksum = checksum;
/*  48 */     this.in = in;
/*  49 */     this.expectedChecksum = expectedChecksum;
/*  50 */     this.bytesRemaining = size;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*  55 */     this.in.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getBytesRemaining() {
/*  63 */     return this.bytesRemaining;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/*  74 */     if (this.bytesRemaining <= 0L) {
/*  75 */       return -1;
/*     */     }
/*  77 */     int ret = this.in.read();
/*  78 */     if (ret >= 0) {
/*  79 */       this.checksum.update(ret);
/*  80 */       this.bytesRemaining--;
/*     */     } 
/*  82 */     verify();
/*  83 */     return ret;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int read(byte[] b) throws IOException {
/*  94 */     return read(b, 0, b.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int read(byte[] b, int off, int len) throws IOException {
/* 105 */     if (len == 0) {
/* 106 */       return 0;
/*     */     }
/* 108 */     int ret = this.in.read(b, off, len);
/* 109 */     if (ret >= 0) {
/* 110 */       this.checksum.update(b, off, ret);
/* 111 */       this.bytesRemaining -= ret;
/*     */     } 
/* 113 */     verify();
/* 114 */     return ret;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long skip(long n) throws IOException {
/* 120 */     return (read() >= 0) ? 1L : 0L;
/*     */   }
/*     */   
/*     */   private void verify() throws IOException {
/* 124 */     if (this.bytesRemaining <= 0L && this.expectedChecksum != this.checksum.getValue())
/* 125 */       throw new IOException("Checksum verification failed"); 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compres\\utils\ChecksumVerifyingInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */