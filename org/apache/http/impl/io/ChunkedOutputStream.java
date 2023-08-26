/*     */ package org.apache.http.impl.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.io.SessionOutputBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @NotThreadSafe
/*     */ public class ChunkedOutputStream
/*     */   extends OutputStream
/*     */ {
/*     */   private final SessionOutputBuffer out;
/*     */   private final byte[] cache;
/*  56 */   private int cachePosition = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean wroteLastChunk = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean closed = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public ChunkedOutputStream(SessionOutputBuffer out, int bufferSize) throws IOException {
/*  75 */     this(bufferSize, out);
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
/*     */   public ChunkedOutputStream(SessionOutputBuffer out) throws IOException {
/*  90 */     this(2048, out);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChunkedOutputStream(int bufferSize, SessionOutputBuffer out) {
/* 101 */     this.cache = new byte[bufferSize];
/* 102 */     this.out = out;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void flushCache() throws IOException {
/* 109 */     if (this.cachePosition > 0) {
/* 110 */       this.out.writeLine(Integer.toHexString(this.cachePosition));
/* 111 */       this.out.write(this.cache, 0, this.cachePosition);
/* 112 */       this.out.writeLine("");
/* 113 */       this.cachePosition = 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void flushCacheWithAppend(byte[] bufferToAppend, int off, int len) throws IOException {
/* 122 */     this.out.writeLine(Integer.toHexString(this.cachePosition + len));
/* 123 */     this.out.write(this.cache, 0, this.cachePosition);
/* 124 */     this.out.write(bufferToAppend, off, len);
/* 125 */     this.out.writeLine("");
/* 126 */     this.cachePosition = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writeClosingChunk() throws IOException {
/* 131 */     this.out.writeLine("0");
/* 132 */     this.out.writeLine("");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void finish() throws IOException {
/* 142 */     if (!this.wroteLastChunk) {
/* 143 */       flushCache();
/* 144 */       writeClosingChunk();
/* 145 */       this.wroteLastChunk = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(int b) throws IOException {
/* 152 */     if (this.closed) {
/* 153 */       throw new IOException("Attempted write to closed stream.");
/*     */     }
/* 155 */     this.cache[this.cachePosition] = (byte)b;
/* 156 */     this.cachePosition++;
/* 157 */     if (this.cachePosition == this.cache.length) {
/* 158 */       flushCache();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(byte[] b) throws IOException {
/* 168 */     write(b, 0, b.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(byte[] src, int off, int len) throws IOException {
/* 177 */     if (this.closed) {
/* 178 */       throw new IOException("Attempted write to closed stream.");
/*     */     }
/* 180 */     if (len >= this.cache.length - this.cachePosition) {
/* 181 */       flushCacheWithAppend(src, off, len);
/*     */     } else {
/* 183 */       System.arraycopy(src, off, this.cache, this.cachePosition, len);
/* 184 */       this.cachePosition += len;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/* 193 */     flushCache();
/* 194 */     this.out.flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 202 */     if (!this.closed) {
/* 203 */       this.closed = true;
/* 204 */       finish();
/* 205 */       this.out.flush();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\io\ChunkedOutputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */