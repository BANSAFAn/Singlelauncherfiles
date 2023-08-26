/*     */ package org.apache.http.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class ByteArrayBuffer
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 4359112959524048036L;
/*     */   private byte[] buffer;
/*     */   private int len;
/*     */   
/*     */   public ByteArrayBuffer(int capacity) {
/*  55 */     Args.notNegative(capacity, "Buffer capacity");
/*  56 */     this.buffer = new byte[capacity];
/*     */   }
/*     */   
/*     */   private void expand(int newlen) {
/*  60 */     byte[] newbuffer = new byte[Math.max(this.buffer.length << 1, newlen)];
/*  61 */     System.arraycopy(this.buffer, 0, newbuffer, 0, this.len);
/*  62 */     this.buffer = newbuffer;
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
/*     */   public void append(byte[] b, int off, int len) {
/*  78 */     if (b == null) {
/*     */       return;
/*     */     }
/*  81 */     if (off < 0 || off > b.length || len < 0 || off + len < 0 || off + len > b.length)
/*     */     {
/*  83 */       throw new IndexOutOfBoundsException("off: " + off + " len: " + len + " b.length: " + b.length);
/*     */     }
/*  85 */     if (len == 0) {
/*     */       return;
/*     */     }
/*  88 */     int newlen = this.len + len;
/*  89 */     if (newlen > this.buffer.length) {
/*  90 */       expand(newlen);
/*     */     }
/*  92 */     System.arraycopy(b, off, this.buffer, this.len, len);
/*  93 */     this.len = newlen;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void append(int b) {
/* 103 */     int newlen = this.len + 1;
/* 104 */     if (newlen > this.buffer.length) {
/* 105 */       expand(newlen);
/*     */     }
/* 107 */     this.buffer[this.len] = (byte)b;
/* 108 */     this.len = newlen;
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
/*     */   public void append(char[] b, int off, int len) {
/* 126 */     if (b == null) {
/*     */       return;
/*     */     }
/* 129 */     if (off < 0 || off > b.length || len < 0 || off + len < 0 || off + len > b.length)
/*     */     {
/* 131 */       throw new IndexOutOfBoundsException("off: " + off + " len: " + len + " b.length: " + b.length);
/*     */     }
/* 133 */     if (len == 0) {
/*     */       return;
/*     */     }
/* 136 */     int oldlen = this.len;
/* 137 */     int newlen = oldlen + len;
/* 138 */     if (newlen > this.buffer.length) {
/* 139 */       expand(newlen);
/*     */     }
/* 141 */     for (int i1 = off, i2 = oldlen; i2 < newlen; i1++, i2++) {
/* 142 */       this.buffer[i2] = (byte)b[i1];
/*     */     }
/* 144 */     this.len = newlen;
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
/*     */   public void append(CharArrayBuffer b, int off, int len) {
/* 163 */     if (b == null) {
/*     */       return;
/*     */     }
/* 166 */     append(b.buffer(), off, len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 173 */     this.len = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] toByteArray() {
/* 182 */     byte[] b = new byte[this.len];
/* 183 */     if (this.len > 0) {
/* 184 */       System.arraycopy(this.buffer, 0, b, 0, this.len);
/*     */     }
/* 186 */     return b;
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
/*     */   public int byteAt(int i) {
/* 200 */     return this.buffer[i];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int capacity() {
/* 211 */     return this.buffer.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/* 220 */     return this.len;
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
/*     */   public void ensureCapacity(int required) {
/* 234 */     if (required <= 0) {
/*     */       return;
/*     */     }
/* 237 */     int available = this.buffer.length - this.len;
/* 238 */     if (required > available) {
/* 239 */       expand(this.len + required);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] buffer() {
/* 249 */     return this.buffer;
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
/*     */   public void setLength(int len) {
/* 263 */     if (len < 0 || len > this.buffer.length) {
/* 264 */       throw new IndexOutOfBoundsException("len: " + len + " < 0 or > buffer len: " + this.buffer.length);
/*     */     }
/* 266 */     this.len = len;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 276 */     return (this.len == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFull() {
/* 286 */     return (this.len == this.buffer.length);
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
/*     */   public int indexOf(byte b, int from, int to) {
/* 313 */     int beginIndex = from;
/* 314 */     if (beginIndex < 0) {
/* 315 */       beginIndex = 0;
/*     */     }
/* 317 */     int endIndex = to;
/* 318 */     if (endIndex > this.len) {
/* 319 */       endIndex = this.len;
/*     */     }
/* 321 */     if (beginIndex > endIndex) {
/* 322 */       return -1;
/*     */     }
/* 324 */     for (int i = beginIndex; i < endIndex; i++) {
/* 325 */       if (this.buffer[i] == b) {
/* 326 */         return i;
/*     */       }
/*     */     } 
/* 329 */     return -1;
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
/*     */   public int indexOf(byte b) {
/* 345 */     return indexOf(b, 0, this.len);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\htt\\util\ByteArrayBuffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */