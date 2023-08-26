/*     */ package org.apache.commons.compress.compressors.zstandard;
/*     */ 
/*     */ import com.github.luben.zstd.ZstdOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import org.apache.commons.compress.compressors.CompressorOutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ZstdCompressorOutputStream
/*     */   extends CompressorOutputStream
/*     */ {
/*     */   private final ZstdOutputStream encOS;
/*     */   
/*     */   public ZstdCompressorOutputStream(OutputStream outStream, int level, boolean closeFrameOnFlush, boolean useChecksum) throws IOException {
/*  49 */     this.encOS = new ZstdOutputStream(outStream, level);
/*  50 */     this.encOS.setCloseFrameOnFlush(closeFrameOnFlush);
/*  51 */     this.encOS.setChecksum(useChecksum);
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
/*     */   public ZstdCompressorOutputStream(OutputStream outStream, int level, boolean closeFrameOnFlush) throws IOException {
/*  64 */     this.encOS = new ZstdOutputStream(outStream, level);
/*  65 */     this.encOS.setCloseFrameOnFlush(closeFrameOnFlush);
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
/*     */   public ZstdCompressorOutputStream(OutputStream outStream, int level) throws IOException {
/*  77 */     this.encOS = new ZstdOutputStream(outStream, level);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ZstdCompressorOutputStream(OutputStream outStream) throws IOException {
/*  87 */     this.encOS = new ZstdOutputStream(outStream);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*  92 */     this.encOS.close();
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(int b) throws IOException {
/*  97 */     this.encOS.write(b);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(byte[] buf, int off, int len) throws IOException {
/* 102 */     this.encOS.write(buf, off, len);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 107 */     return this.encOS.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/* 112 */     this.encOS.flush();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\compressors\zstandard\ZstdCompressorOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */