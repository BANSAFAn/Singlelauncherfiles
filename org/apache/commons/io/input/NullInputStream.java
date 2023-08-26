/*     */ package org.apache.commons.io.input;
/*     */ 
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NullInputStream
/*     */   extends InputStream
/*     */ {
/*     */   private final long size;
/*     */   private long position;
/*  72 */   private long mark = -1L;
/*     */ 
/*     */   
/*     */   private long readlimit;
/*     */   
/*     */   private boolean eof;
/*     */   
/*     */   private final boolean throwEofException;
/*     */   
/*     */   private final boolean markSupported;
/*     */ 
/*     */   
/*     */   public NullInputStream() {
/*  85 */     this(0L, true, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NullInputStream(long size) {
/*  95 */     this(size, true, false);
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
/*     */   public NullInputStream(long size, boolean markSupported, boolean throwEofException) {
/* 110 */     this.size = size;
/* 111 */     this.markSupported = markSupported;
/* 112 */     this.throwEofException = throwEofException;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getPosition() {
/* 121 */     return this.position;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getSize() {
/* 130 */     return this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int available() {
/* 140 */     long avail = this.size - this.position;
/* 141 */     if (avail <= 0L) {
/* 142 */       return 0;
/*     */     }
/* 144 */     if (avail > 2147483647L) {
/* 145 */       return Integer.MAX_VALUE;
/*     */     }
/* 147 */     return (int)avail;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 158 */     this.eof = false;
/* 159 */     this.position = 0L;
/* 160 */     this.mark = -1L;
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
/*     */   public synchronized void mark(int readlimit) {
/* 172 */     if (!this.markSupported) {
/* 173 */       throw UnsupportedOperationExceptions.mark();
/*     */     }
/* 175 */     this.mark = this.position;
/* 176 */     this.readlimit = readlimit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean markSupported() {
/* 186 */     return this.markSupported;
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
/*     */   public int read() throws IOException {
/* 201 */     if (this.eof) {
/* 202 */       throw new IOException("Read after end of file");
/*     */     }
/* 204 */     if (this.position == this.size) {
/* 205 */       return doEndOfFile();
/*     */     }
/* 207 */     this.position++;
/* 208 */     return processByte();
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
/*     */   public int read(byte[] bytes) throws IOException {
/* 224 */     return read(bytes, 0, bytes.length);
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
/*     */   public int read(byte[] bytes, int offset, int length) throws IOException {
/* 242 */     if (this.eof) {
/* 243 */       throw new IOException("Read after end of file");
/*     */     }
/* 245 */     if (this.position == this.size) {
/* 246 */       return doEndOfFile();
/*     */     }
/* 248 */     this.position += length;
/* 249 */     int returnLength = length;
/* 250 */     if (this.position > this.size) {
/* 251 */       returnLength = length - (int)(this.position - this.size);
/* 252 */       this.position = this.size;
/*     */     } 
/* 254 */     processBytes(bytes, offset, returnLength);
/* 255 */     return returnLength;
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
/*     */   public synchronized void reset() throws IOException {
/* 268 */     if (!this.markSupported) {
/* 269 */       throw UnsupportedOperationExceptions.reset();
/*     */     }
/* 271 */     if (this.mark < 0L) {
/* 272 */       throw new IOException("No position has been marked");
/*     */     }
/* 274 */     if (this.position > this.mark + this.readlimit) {
/* 275 */       throw new IOException("Marked position [" + this.mark + "] is no longer valid - passed the read limit [" + this.readlimit + "]");
/*     */     }
/*     */ 
/*     */     
/* 279 */     this.position = this.mark;
/* 280 */     this.eof = false;
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
/*     */   public long skip(long numberOfBytes) throws IOException {
/* 296 */     if (this.eof) {
/* 297 */       throw new IOException("Skip after end of file");
/*     */     }
/* 299 */     if (this.position == this.size) {
/* 300 */       return doEndOfFile();
/*     */     }
/* 302 */     this.position += numberOfBytes;
/* 303 */     long returnLength = numberOfBytes;
/* 304 */     if (this.position > this.size) {
/* 305 */       returnLength = numberOfBytes - this.position - this.size;
/* 306 */       this.position = this.size;
/*     */     } 
/* 308 */     return returnLength;
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
/*     */   protected int processByte() {
/* 320 */     return 0;
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
/*     */   protected void processBytes(byte[] bytes, int offset, int length) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int doEndOfFile() throws EOFException {
/* 346 */     this.eof = true;
/* 347 */     if (this.throwEofException) {
/* 348 */       throw new EOFException();
/*     */     }
/* 350 */     return -1;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\input\NullInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */