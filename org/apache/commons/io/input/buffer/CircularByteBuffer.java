/*     */ package org.apache.commons.io.input.buffer;
/*     */ 
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
/*     */ public class CircularByteBuffer
/*     */ {
/*     */   private final byte[] buffer;
/*     */   private int startOffset;
/*     */   private int endOffset;
/*     */   private int currentNumberOfBytes;
/*     */   
/*     */   public CircularByteBuffer(int size) {
/*  42 */     this.buffer = IOUtils.byteArray(size);
/*  43 */     this.startOffset = 0;
/*  44 */     this.endOffset = 0;
/*  45 */     this.currentNumberOfBytes = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CircularByteBuffer() {
/*  52 */     this(8192);
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
/*     */   public byte read() {
/*  64 */     if (this.currentNumberOfBytes <= 0) {
/*  65 */       throw new IllegalStateException("No bytes available.");
/*     */     }
/*  67 */     byte b = this.buffer[this.startOffset];
/*  68 */     this.currentNumberOfBytes--;
/*  69 */     if (++this.startOffset == this.buffer.length) {
/*  70 */       this.startOffset = 0;
/*     */     }
/*  72 */     return b;
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
/*     */   public void read(byte[] targetBuffer, int targetOffset, int length) {
/*  90 */     Objects.requireNonNull(targetBuffer, "targetBuffer");
/*  91 */     if (targetOffset < 0 || targetOffset >= targetBuffer.length) {
/*  92 */       throw new IllegalArgumentException("Invalid offset: " + targetOffset);
/*     */     }
/*  94 */     if (length < 0 || length > this.buffer.length) {
/*  95 */       throw new IllegalArgumentException("Invalid length: " + length);
/*     */     }
/*  97 */     if (targetOffset + length > targetBuffer.length) {
/*  98 */       throw new IllegalArgumentException("The supplied byte array contains only " + targetBuffer.length + " bytes, but offset, and length would require " + (targetOffset + length - 1));
/*     */     }
/*     */ 
/*     */     
/* 102 */     if (this.currentNumberOfBytes < length) {
/* 103 */       throw new IllegalStateException("Currently, there are only " + this.currentNumberOfBytes + "in the buffer, not " + length);
/*     */     }
/*     */     
/* 106 */     int offset = targetOffset;
/* 107 */     for (int i = 0; i < length; i++) {
/* 108 */       targetBuffer[offset++] = this.buffer[this.startOffset];
/* 109 */       this.currentNumberOfBytes--;
/* 110 */       if (++this.startOffset == this.buffer.length) {
/* 111 */         this.startOffset = 0;
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
/*     */   
/*     */   public void add(byte value) {
/* 125 */     if (this.currentNumberOfBytes >= this.buffer.length) {
/* 126 */       throw new IllegalStateException("No space available");
/*     */     }
/* 128 */     this.buffer[this.endOffset] = value;
/* 129 */     this.currentNumberOfBytes++;
/* 130 */     if (++this.endOffset == this.buffer.length) {
/* 131 */       this.endOffset = 0;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean peek(byte[] sourceBuffer, int offset, int length) {
/* 151 */     Objects.requireNonNull(sourceBuffer, "Buffer");
/* 152 */     if (offset < 0 || offset >= sourceBuffer.length) {
/* 153 */       throw new IllegalArgumentException("Invalid offset: " + offset);
/*     */     }
/* 155 */     if (length < 0 || length > this.buffer.length) {
/* 156 */       throw new IllegalArgumentException("Invalid length: " + length);
/*     */     }
/* 158 */     if (length < this.currentNumberOfBytes) {
/* 159 */       return false;
/*     */     }
/* 161 */     int localOffset = this.startOffset;
/* 162 */     for (int i = 0; i < length; i++) {
/* 163 */       if (this.buffer[localOffset] != sourceBuffer[i + offset]) {
/* 164 */         return false;
/*     */       }
/* 166 */       if (++localOffset == this.buffer.length) {
/* 167 */         localOffset = 0;
/*     */       }
/*     */     } 
/* 170 */     return true;
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
/*     */   public void add(byte[] targetBuffer, int offset, int length) {
/* 187 */     Objects.requireNonNull(targetBuffer, "Buffer");
/* 188 */     if (offset < 0 || offset >= targetBuffer.length) {
/* 189 */       throw new IllegalArgumentException("Invalid offset: " + offset);
/*     */     }
/* 191 */     if (length < 0) {
/* 192 */       throw new IllegalArgumentException("Invalid length: " + length);
/*     */     }
/* 194 */     if (this.currentNumberOfBytes + length > this.buffer.length) {
/* 195 */       throw new IllegalStateException("No space available");
/*     */     }
/* 197 */     for (int i = 0; i < length; i++) {
/* 198 */       this.buffer[this.endOffset] = targetBuffer[offset + i];
/* 199 */       if (++this.endOffset == this.buffer.length) {
/* 200 */         this.endOffset = 0;
/*     */       }
/*     */     } 
/* 203 */     this.currentNumberOfBytes += length;
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
/*     */   public boolean hasSpace() {
/* 215 */     return (this.currentNumberOfBytes < this.buffer.length);
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
/*     */   public boolean hasSpace(int count) {
/* 227 */     return (this.currentNumberOfBytes + count <= this.buffer.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasBytes() {
/* 236 */     return (this.currentNumberOfBytes > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSpace() {
/* 245 */     return this.buffer.length - this.currentNumberOfBytes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCurrentNumberOfBytes() {
/* 254 */     return this.currentNumberOfBytes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 261 */     this.startOffset = 0;
/* 262 */     this.endOffset = 0;
/* 263 */     this.currentNumberOfBytes = 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\input\buffer\CircularByteBuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */