/*     */ package org.apache.commons.io.input;
/*     */ 
/*     */ import java.io.Reader;
/*     */ import java.io.Serializable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CharSequenceReader
/*     */   extends Reader
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 3724187752191401220L;
/*     */   private final CharSequence charSequence;
/*     */   private int idx;
/*     */   private int mark;
/*     */   private final int start;
/*     */   private final Integer end;
/*     */   
/*     */   public CharSequenceReader(CharSequence charSequence) {
/*  77 */     this(charSequence, 0);
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
/*     */   public CharSequenceReader(CharSequence charSequence, int start) {
/*  96 */     this(charSequence, start, 2147483647);
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
/*     */   public CharSequenceReader(CharSequence charSequence, int start, int end) {
/* 118 */     if (start < 0) {
/* 119 */       throw new IllegalArgumentException("Start index is less than zero: " + start);
/*     */     }
/*     */     
/* 122 */     if (end < start) {
/* 123 */       throw new IllegalArgumentException("End index is less than start " + start + ": " + end);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 129 */     this.charSequence = (charSequence != null) ? charSequence : "";
/* 130 */     this.start = start;
/* 131 */     this.end = Integer.valueOf(end);
/*     */     
/* 133 */     this.idx = start;
/* 134 */     this.mark = start;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int start() {
/* 143 */     return Math.min(this.charSequence.length(), this.start);
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
/*     */   private int end() {
/* 156 */     return Math.min(this.charSequence.length(), (this.end == null) ? Integer.MAX_VALUE : this.end.intValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/* 164 */     this.idx = this.start;
/* 165 */     this.mark = this.start;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean ready() {
/* 175 */     return (this.idx < end());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void mark(int readAheadLimit) {
/* 185 */     this.mark = this.idx;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean markSupported() {
/* 195 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int read() {
/* 206 */     if (this.idx >= end()) {
/* 207 */       return -1;
/*     */     }
/* 209 */     return this.charSequence.charAt(this.idx++);
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
/*     */   public int read(char[] array, int offset, int length) {
/* 223 */     if (this.idx >= end()) {
/* 224 */       return -1;
/*     */     }
/* 226 */     Objects.requireNonNull(array, "array");
/* 227 */     if (length < 0 || offset < 0 || offset + length > array.length) {
/* 228 */       throw new IndexOutOfBoundsException("Array Size=" + array.length + ", offset=" + offset + ", length=" + length);
/*     */     }
/*     */ 
/*     */     
/* 232 */     if (this.charSequence instanceof String) {
/* 233 */       int j = Math.min(length, end() - this.idx);
/* 234 */       ((String)this.charSequence).getChars(this.idx, this.idx + j, array, offset);
/* 235 */       this.idx += j;
/* 236 */       return j;
/*     */     } 
/* 238 */     if (this.charSequence instanceof StringBuilder) {
/* 239 */       int j = Math.min(length, end() - this.idx);
/* 240 */       ((StringBuilder)this.charSequence).getChars(this.idx, this.idx + j, array, offset);
/* 241 */       this.idx += j;
/* 242 */       return j;
/*     */     } 
/* 244 */     if (this.charSequence instanceof StringBuffer) {
/* 245 */       int j = Math.min(length, end() - this.idx);
/* 246 */       ((StringBuffer)this.charSequence).getChars(this.idx, this.idx + j, array, offset);
/* 247 */       this.idx += j;
/* 248 */       return j;
/*     */     } 
/*     */     
/* 251 */     int count = 0;
/* 252 */     for (int i = 0; i < length; i++) {
/* 253 */       int c = read();
/* 254 */       if (c == -1) {
/* 255 */         return count;
/*     */       }
/* 257 */       array[offset + i] = (char)c;
/* 258 */       count++;
/*     */     } 
/* 260 */     return count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 269 */     this.idx = this.mark;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long skip(long n) {
/* 280 */     if (n < 0L) {
/* 281 */       throw new IllegalArgumentException("Number of characters to skip is less than zero: " + n);
/*     */     }
/*     */     
/* 284 */     if (this.idx >= end()) {
/* 285 */       return 0L;
/*     */     }
/* 287 */     int dest = (int)Math.min(end(), this.idx + n);
/* 288 */     int count = dest - this.idx;
/* 289 */     this.idx = dest;
/* 290 */     return count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 301 */     CharSequence subSequence = this.charSequence.subSequence(start(), end());
/* 302 */     return subSequence.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\input\CharSequenceReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */