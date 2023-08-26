/*     */ package org.apache.commons.io.input.buffer;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Objects;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CircularBufferInputStream
/*     */   extends InputStream
/*     */ {
/*     */   protected final InputStream in;
/*     */   protected final CircularByteBuffer buffer;
/*     */   protected final int bufferSize;
/*     */   private boolean eof;
/*     */   
/*     */   public CircularBufferInputStream(InputStream inputStream, int bufferSize) {
/*  52 */     if (bufferSize <= 0) {
/*  53 */       throw new IllegalArgumentException("Invalid bufferSize: " + bufferSize);
/*     */     }
/*  55 */     this.in = Objects.<InputStream>requireNonNull(inputStream, "inputStream");
/*  56 */     this.buffer = new CircularByteBuffer(bufferSize);
/*  57 */     this.bufferSize = bufferSize;
/*  58 */     this.eof = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CircularBufferInputStream(InputStream inputStream) {
/*  68 */     this(inputStream, 8192);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void fillBuffer() throws IOException {
/*  77 */     if (this.eof) {
/*     */       return;
/*     */     }
/*  80 */     int space = this.buffer.getSpace();
/*  81 */     byte[] buf = IOUtils.byteArray(space);
/*  82 */     while (space > 0) {
/*  83 */       int res = this.in.read(buf, 0, space);
/*  84 */       if (res == -1) {
/*  85 */         this.eof = true;
/*     */         return;
/*     */       } 
/*  88 */       if (res > 0) {
/*  89 */         this.buffer.add(buf, 0, res);
/*  90 */         space -= res;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean haveBytes(int count) throws IOException {
/* 103 */     if (this.buffer.getCurrentNumberOfBytes() < count) {
/* 104 */       fillBuffer();
/*     */     }
/* 106 */     return this.buffer.hasBytes();
/*     */   }
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/* 111 */     if (!haveBytes(1)) {
/* 112 */       return -1;
/*     */     }
/* 114 */     return this.buffer.read() & 0xFF;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] buffer) throws IOException {
/* 119 */     return read(buffer, 0, buffer.length);
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] targetBuffer, int offset, int length) throws IOException {
/* 124 */     Objects.requireNonNull(targetBuffer, "targetBuffer");
/* 125 */     if (offset < 0) {
/* 126 */       throw new IllegalArgumentException("Offset must not be negative");
/*     */     }
/* 128 */     if (length < 0) {
/* 129 */       throw new IllegalArgumentException("Length must not be negative");
/*     */     }
/* 131 */     if (!haveBytes(length)) {
/* 132 */       return -1;
/*     */     }
/* 134 */     int result = Math.min(length, this.buffer.getCurrentNumberOfBytes());
/* 135 */     for (int i = 0; i < result; i++) {
/* 136 */       targetBuffer[offset + i] = this.buffer.read();
/*     */     }
/* 138 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 143 */     this.in.close();
/* 144 */     this.eof = true;
/* 145 */     this.buffer.clear();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\input\buffer\CircularBufferInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */