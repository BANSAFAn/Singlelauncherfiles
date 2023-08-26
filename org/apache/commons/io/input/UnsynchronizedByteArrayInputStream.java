/*     */ package org.apache.commons.io.input;
/*     */ 
/*     */ import java.io.InputStream;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UnsynchronizedByteArrayInputStream
/*     */   extends InputStream
/*     */ {
/*     */   public static final int END_OF_STREAM = -1;
/*     */   private final byte[] data;
/*     */   private final int eod;
/*     */   private int offset;
/*     */   private int markedOffset;
/*     */   
/*     */   public UnsynchronizedByteArrayInputStream(byte[] data) {
/*  68 */     this.data = Objects.<byte[]>requireNonNull(data, "data");
/*  69 */     this.offset = 0;
/*  70 */     this.eod = data.length;
/*  71 */     this.markedOffset = this.offset;
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
/*     */   public UnsynchronizedByteArrayInputStream(byte[] data, int offset) {
/*  83 */     Objects.requireNonNull(data, "data");
/*  84 */     if (offset < 0) {
/*  85 */       throw new IllegalArgumentException("offset cannot be negative");
/*     */     }
/*  87 */     this.data = data;
/*  88 */     this.offset = Math.min(offset, (data.length > 0) ? data.length : offset);
/*  89 */     this.eod = data.length;
/*  90 */     this.markedOffset = this.offset;
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
/*     */   public UnsynchronizedByteArrayInputStream(byte[] data, int offset, int length) {
/* 104 */     if (offset < 0) {
/* 105 */       throw new IllegalArgumentException("offset cannot be negative");
/*     */     }
/* 107 */     if (length < 0) {
/* 108 */       throw new IllegalArgumentException("length cannot be negative");
/*     */     }
/* 110 */     this.data = Objects.<byte[]>requireNonNull(data, "data");
/* 111 */     this.offset = Math.min(offset, (data.length > 0) ? data.length : offset);
/* 112 */     this.eod = Math.min(this.offset + length, data.length);
/* 113 */     this.markedOffset = this.offset;
/*     */   }
/*     */ 
/*     */   
/*     */   public int available() {
/* 118 */     return (this.offset < this.eod) ? (this.eod - this.offset) : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read() {
/* 123 */     return (this.offset < this.eod) ? (this.data[this.offset++] & 0xFF) : -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] dest) {
/* 128 */     Objects.requireNonNull(dest, "dest");
/* 129 */     return read(dest, 0, dest.length);
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] dest, int off, int len) {
/* 134 */     Objects.requireNonNull(dest, "dest");
/* 135 */     if (off < 0 || len < 0 || off + len > dest.length) {
/* 136 */       throw new IndexOutOfBoundsException();
/*     */     }
/*     */     
/* 139 */     if (this.offset >= this.eod) {
/* 140 */       return -1;
/*     */     }
/*     */     
/* 143 */     int actualLen = this.eod - this.offset;
/* 144 */     if (len < actualLen) {
/* 145 */       actualLen = len;
/*     */     }
/* 147 */     if (actualLen <= 0) {
/* 148 */       return 0;
/*     */     }
/* 150 */     System.arraycopy(this.data, this.offset, dest, off, actualLen);
/* 151 */     this.offset += actualLen;
/* 152 */     return actualLen;
/*     */   }
/*     */ 
/*     */   
/*     */   public long skip(long n) {
/* 157 */     if (n < 0L) {
/* 158 */       throw new IllegalArgumentException("Skipping backward is not supported");
/*     */     }
/*     */     
/* 161 */     long actualSkip = (this.eod - this.offset);
/* 162 */     if (n < actualSkip) {
/* 163 */       actualSkip = n;
/*     */     }
/*     */     
/* 166 */     this.offset = (int)(this.offset + actualSkip);
/* 167 */     return actualSkip;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean markSupported() {
/* 172 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void mark(int readlimit) {
/* 178 */     this.markedOffset = this.offset;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 184 */     this.offset = this.markedOffset;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\input\UnsynchronizedByteArrayInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */