/*     */ package org.apache.commons.io.input;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BoundedReader
/*     */   extends Reader
/*     */ {
/*     */   private static final int INVALID = -1;
/*     */   private final Reader target;
/*     */   private int charsRead;
/*  45 */   private int markedAt = -1;
/*     */ 
/*     */ 
/*     */   
/*     */   private int readAheadLimit;
/*     */ 
/*     */ 
/*     */   
/*     */   private final int maxCharsFromTargetReader;
/*     */ 
/*     */ 
/*     */   
/*     */   public BoundedReader(Reader target, int maxCharsFromTargetReader) {
/*  58 */     this.target = target;
/*  59 */     this.maxCharsFromTargetReader = maxCharsFromTargetReader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*  69 */     this.target.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() throws IOException {
/*  80 */     this.charsRead = this.markedAt;
/*  81 */     this.target.reset();
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
/*     */   public void mark(int readAheadLimit) throws IOException {
/*  96 */     this.readAheadLimit = readAheadLimit - this.charsRead;
/*     */     
/*  98 */     this.markedAt = this.charsRead;
/*     */     
/* 100 */     this.target.mark(readAheadLimit);
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
/*     */   public int read() throws IOException {
/* 113 */     if (this.charsRead >= this.maxCharsFromTargetReader) {
/* 114 */       return -1;
/*     */     }
/*     */     
/* 117 */     if (this.markedAt >= 0 && this.charsRead - this.markedAt >= this.readAheadLimit) {
/* 118 */       return -1;
/*     */     }
/* 120 */     this.charsRead++;
/* 121 */     return this.target.read();
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
/*     */   public int read(char[] cbuf, int off, int len) throws IOException {
/* 137 */     for (int i = 0; i < len; i++) {
/* 138 */       int c = read();
/* 139 */       if (c == -1) {
/* 140 */         return (i == 0) ? -1 : i;
/*     */       }
/* 142 */       cbuf[off + i] = (char)c;
/*     */     } 
/* 144 */     return len;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\input\BoundedReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */