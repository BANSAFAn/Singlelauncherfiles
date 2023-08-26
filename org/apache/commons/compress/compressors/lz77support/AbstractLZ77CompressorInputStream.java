/*     */ package org.apache.commons.compress.compressors.lz77support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Arrays;
/*     */ import org.apache.commons.compress.compressors.CompressorInputStream;
/*     */ import org.apache.commons.compress.utils.ByteUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractLZ77CompressorInputStream
/*     */   extends CompressorInputStream
/*     */   implements InputStreamStatistics
/*     */ {
/*     */   private final int windowSize;
/*     */   private final byte[] buf;
/*     */   private int writeIndex;
/*     */   private int readIndex;
/*     */   private final CountingInputStream in;
/*     */   private long bytesRemaining;
/*     */   private int backReferenceOffset;
/*     */   private int size;
/* 112 */   private final byte[] oneByte = new byte[1];
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 117 */   protected final ByteUtils.ByteSupplier supplier = this::readOneByte;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractLZ77CompressorInputStream(InputStream is, int windowSize) {
/* 130 */     this.in = new CountingInputStream(is);
/* 131 */     if (windowSize <= 0) {
/* 132 */       throw new IllegalArgumentException("windowSize must be bigger than 0");
/*     */     }
/* 134 */     this.windowSize = windowSize;
/* 135 */     this.buf = new byte[3 * windowSize];
/* 136 */     this.writeIndex = this.readIndex = 0;
/* 137 */     this.bytesRemaining = 0L;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/* 143 */     return (read(this.oneByte, 0, 1) == -1) ? -1 : (this.oneByte[0] & 0xFF);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 149 */     this.in.close();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int available() {
/* 155 */     return this.writeIndex - this.readIndex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSize() {
/* 164 */     return this.size;
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
/*     */   public void prefill(byte[] data) {
/* 179 */     if (this.writeIndex != 0) {
/* 180 */       throw new IllegalStateException("The stream has already been read from, can't prefill anymore");
/*     */     }
/*     */     
/* 183 */     int len = Math.min(this.windowSize, data.length);
/*     */     
/* 185 */     System.arraycopy(data, data.length - len, this.buf, 0, len);
/* 186 */     this.writeIndex += len;
/* 187 */     this.readIndex += len;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getCompressedCount() {
/* 195 */     return this.in.getBytesRead();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void startLiteral(long length) {
/* 205 */     if (length < 0L) {
/* 206 */       throw new IllegalArgumentException("length must not be negative");
/*     */     }
/* 208 */     this.bytesRemaining = length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final boolean hasMoreDataInBlock() {
/* 216 */     return (this.bytesRemaining > 0L);
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
/*     */   protected final int readLiteral(byte[] b, int off, int len) throws IOException {
/* 235 */     int avail = available();
/* 236 */     if (len > avail) {
/* 237 */       tryToReadLiteral(len - avail);
/*     */     }
/* 239 */     return readFromBuffer(b, off, len);
/*     */   }
/*     */ 
/*     */   
/*     */   private void tryToReadLiteral(int bytesToRead) throws IOException {
/* 244 */     int reallyTryToRead = Math.min((int)Math.min(bytesToRead, this.bytesRemaining), this.buf.length - this.writeIndex);
/*     */ 
/*     */     
/* 247 */     int bytesRead = (reallyTryToRead > 0) ? IOUtils.readFully((InputStream)this.in, this.buf, this.writeIndex, reallyTryToRead) : 0;
/*     */     
/* 249 */     count(bytesRead);
/* 250 */     if (reallyTryToRead != bytesRead) {
/* 251 */       throw new IOException("Premature end of stream reading literal");
/*     */     }
/* 253 */     this.writeIndex += reallyTryToRead;
/* 254 */     this.bytesRemaining -= reallyTryToRead;
/*     */   }
/*     */   
/*     */   private int readFromBuffer(byte[] b, int off, int len) {
/* 258 */     int readable = Math.min(len, available());
/* 259 */     if (readable > 0) {
/* 260 */       System.arraycopy(this.buf, this.readIndex, b, off, readable);
/* 261 */       this.readIndex += readable;
/* 262 */       if (this.readIndex > 2 * this.windowSize) {
/* 263 */         slideBuffer();
/*     */       }
/*     */     } 
/* 266 */     this.size += readable;
/* 267 */     return readable;
/*     */   }
/*     */   
/*     */   private void slideBuffer() {
/* 271 */     System.arraycopy(this.buf, this.windowSize, this.buf, 0, this.windowSize * 2);
/* 272 */     this.writeIndex -= this.windowSize;
/* 273 */     this.readIndex -= this.windowSize;
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
/*     */   protected final void startBackReference(int offset, long length) {
/* 285 */     if (offset <= 0 || offset > this.writeIndex) {
/* 286 */       throw new IllegalArgumentException("offset must be bigger than 0 but not bigger than the number of bytes available for back-references");
/*     */     }
/*     */     
/* 289 */     if (length < 0L) {
/* 290 */       throw new IllegalArgumentException("length must not be negative");
/*     */     }
/* 292 */     this.backReferenceOffset = offset;
/* 293 */     this.bytesRemaining = length;
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
/*     */   protected final int readBackReference(byte[] b, int off, int len) {
/* 309 */     int avail = available();
/* 310 */     if (len > avail) {
/* 311 */       tryToCopy(len - avail);
/*     */     }
/* 313 */     return readFromBuffer(b, off, len);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void tryToCopy(int bytesToCopy) {
/* 319 */     int copy = Math.min((int)Math.min(bytesToCopy, this.bytesRemaining), this.buf.length - this.writeIndex);
/*     */     
/* 321 */     if (copy != 0)
/*     */     {
/* 323 */       if (this.backReferenceOffset == 1) {
/* 324 */         byte last = this.buf[this.writeIndex - 1];
/* 325 */         Arrays.fill(this.buf, this.writeIndex, this.writeIndex + copy, last);
/* 326 */         this.writeIndex += copy;
/* 327 */       } else if (copy < this.backReferenceOffset) {
/* 328 */         System.arraycopy(this.buf, this.writeIndex - this.backReferenceOffset, this.buf, this.writeIndex, copy);
/* 329 */         this.writeIndex += copy;
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 334 */         int fullRots = copy / this.backReferenceOffset;
/* 335 */         for (int i = 0; i < fullRots; i++) {
/* 336 */           System.arraycopy(this.buf, this.writeIndex - this.backReferenceOffset, this.buf, this.writeIndex, this.backReferenceOffset);
/* 337 */           this.writeIndex += this.backReferenceOffset;
/*     */         } 
/*     */         
/* 340 */         int pad = copy - this.backReferenceOffset * fullRots;
/* 341 */         if (pad > 0) {
/* 342 */           System.arraycopy(this.buf, this.writeIndex - this.backReferenceOffset, this.buf, this.writeIndex, pad);
/* 343 */           this.writeIndex += pad;
/*     */         } 
/*     */       }  } 
/* 346 */     this.bytesRemaining -= copy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final int readOneByte() throws IOException {
/* 356 */     int b = this.in.read();
/* 357 */     if (b != -1) {
/* 358 */       count(1);
/* 359 */       return b & 0xFF;
/*     */     } 
/* 361 */     return -1;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\compressors\lz77support\AbstractLZ77CompressorInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */