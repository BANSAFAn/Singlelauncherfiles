/*     */ package org.apache.commons.io.output;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.SequenceInputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.apache.commons.io.input.ClosedInputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractByteArrayOutputStream
/*     */   extends OutputStream
/*     */ {
/*     */   static final int DEFAULT_SIZE = 1024;
/*  65 */   private final List<byte[]> buffers = (List)new ArrayList<>();
/*     */ 
/*     */   
/*     */   private int currentBufferIndex;
/*     */ 
/*     */   
/*     */   private int filledBufferSum;
/*     */ 
/*     */   
/*     */   private byte[] currentBuffer;
/*     */ 
/*     */   
/*     */   protected int count;
/*     */ 
/*     */   
/*     */   private boolean reuseBuffers = true;
/*     */ 
/*     */   
/*     */   protected void needNewBuffer(int newcount) {
/*  84 */     if (this.currentBufferIndex < this.buffers.size() - 1) {
/*     */       
/*  86 */       this.filledBufferSum += this.currentBuffer.length;
/*     */       
/*  88 */       this.currentBufferIndex++;
/*  89 */       this.currentBuffer = this.buffers.get(this.currentBufferIndex);
/*     */     } else {
/*     */       int newBufferSize;
/*     */       
/*  93 */       if (this.currentBuffer == null) {
/*  94 */         newBufferSize = newcount;
/*  95 */         this.filledBufferSum = 0;
/*     */       } else {
/*  97 */         newBufferSize = Math.max(this.currentBuffer.length << 1, newcount - this.filledBufferSum);
/*     */ 
/*     */         
/* 100 */         this.filledBufferSum += this.currentBuffer.length;
/*     */       } 
/*     */       
/* 103 */       this.currentBufferIndex++;
/* 104 */       this.currentBuffer = IOUtils.byteArray(newBufferSize);
/* 105 */       this.buffers.add(this.currentBuffer);
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
/*     */   public abstract void write(byte[] paramArrayOfbyte, int paramInt1, int paramInt2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeImpl(byte[] b, int off, int len) {
/* 125 */     int newcount = this.count + len;
/* 126 */     int remaining = len;
/* 127 */     int inBufferPos = this.count - this.filledBufferSum;
/* 128 */     while (remaining > 0) {
/* 129 */       int part = Math.min(remaining, this.currentBuffer.length - inBufferPos);
/* 130 */       System.arraycopy(b, off + len - remaining, this.currentBuffer, inBufferPos, part);
/* 131 */       remaining -= part;
/* 132 */       if (remaining > 0) {
/* 133 */         needNewBuffer(newcount);
/* 134 */         inBufferPos = 0;
/*     */       } 
/*     */     } 
/* 137 */     this.count = newcount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void write(int paramInt);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeImpl(int b) {
/* 152 */     int inBufferPos = this.count - this.filledBufferSum;
/* 153 */     if (inBufferPos == this.currentBuffer.length) {
/* 154 */       needNewBuffer(this.count + 1);
/* 155 */       inBufferPos = 0;
/*     */     } 
/* 157 */     this.currentBuffer[inBufferPos] = (byte)b;
/* 158 */     this.count++;
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
/*     */   public abstract int write(InputStream paramInputStream) throws IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int writeImpl(InputStream in) throws IOException {
/* 187 */     int readCount = 0;
/* 188 */     int inBufferPos = this.count - this.filledBufferSum;
/* 189 */     int n = in.read(this.currentBuffer, inBufferPos, this.currentBuffer.length - inBufferPos);
/* 190 */     while (n != -1) {
/* 191 */       readCount += n;
/* 192 */       inBufferPos += n;
/* 193 */       this.count += n;
/* 194 */       if (inBufferPos == this.currentBuffer.length) {
/* 195 */         needNewBuffer(this.currentBuffer.length);
/* 196 */         inBufferPos = 0;
/*     */       } 
/* 198 */       n = in.read(this.currentBuffer, inBufferPos, this.currentBuffer.length - inBufferPos);
/*     */     } 
/* 200 */     return readCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract int size();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void reset();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void resetImpl() {
/* 232 */     this.count = 0;
/* 233 */     this.filledBufferSum = 0;
/* 234 */     this.currentBufferIndex = 0;
/* 235 */     if (this.reuseBuffers) {
/* 236 */       this.currentBuffer = this.buffers.get(this.currentBufferIndex);
/*     */     } else {
/*     */       
/* 239 */       this.currentBuffer = null;
/* 240 */       int size = ((byte[])this.buffers.get(0)).length;
/* 241 */       this.buffers.clear();
/* 242 */       needNewBuffer(size);
/* 243 */       this.reuseBuffers = true;
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
/*     */   public abstract void writeTo(OutputStream paramOutputStream) throws IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeToImpl(OutputStream out) throws IOException {
/* 266 */     int remaining = this.count;
/* 267 */     for (byte[] buf : this.buffers) {
/* 268 */       int c = Math.min(buf.length, remaining);
/* 269 */       out.write(buf, 0, c);
/* 270 */       remaining -= c;
/* 271 */       if (remaining == 0) {
/*     */         break;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract InputStream toInputStream();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected <T extends InputStream> InputStream toInputStream(InputStreamConstructor<T> isConstructor) {
/* 307 */     int remaining = this.count;
/* 308 */     if (remaining == 0) {
/* 309 */       return (InputStream)ClosedInputStream.CLOSED_INPUT_STREAM;
/*     */     }
/* 311 */     List<T> list = new ArrayList<>(this.buffers.size());
/* 312 */     for (byte[] buf : this.buffers) {
/* 313 */       int c = Math.min(buf.length, remaining);
/* 314 */       list.add(isConstructor.construct(buf, 0, c));
/* 315 */       remaining -= c;
/* 316 */       if (remaining == 0) {
/*     */         break;
/*     */       }
/*     */     } 
/* 320 */     this.reuseBuffers = false;
/* 321 */     return new SequenceInputStream(Collections.enumeration(list));
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
/*     */   public abstract byte[] toByteArray();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected byte[] toByteArrayImpl() {
/* 361 */     int remaining = this.count;
/* 362 */     if (remaining == 0) {
/* 363 */       return IOUtils.EMPTY_BYTE_ARRAY;
/*     */     }
/* 365 */     byte[] newbuf = IOUtils.byteArray(remaining);
/* 366 */     int pos = 0;
/* 367 */     for (byte[] buf : this.buffers) {
/* 368 */       int c = Math.min(buf.length, remaining);
/* 369 */       System.arraycopy(buf, 0, newbuf, pos, c);
/* 370 */       pos += c;
/* 371 */       remaining -= c;
/* 372 */       if (remaining == 0) {
/*     */         break;
/*     */       }
/*     */     } 
/* 376 */     return newbuf;
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
/*     */   @Deprecated
/*     */   public String toString() {
/* 390 */     return new String(toByteArray(), Charset.defaultCharset());
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
/*     */   public String toString(String enc) throws UnsupportedEncodingException {
/* 403 */     return new String(toByteArray(), enc);
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
/*     */   public String toString(Charset charset) {
/* 416 */     return new String(toByteArray(), charset);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   protected static interface InputStreamConstructor<T extends InputStream> {
/*     */     T construct(byte[] param1ArrayOfbyte, int param1Int1, int param1Int2);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\output\AbstractByteArrayOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */