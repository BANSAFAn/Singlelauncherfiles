/*     */ package org.apache.commons.io.input;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
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
/*     */ public class SequenceReader
/*     */   extends Reader
/*     */ {
/*     */   private Reader reader;
/*     */   private Iterator<? extends Reader> readers;
/*     */   
/*     */   public SequenceReader(Iterable<? extends Reader> readers) {
/*  43 */     this.readers = ((Iterable<? extends Reader>)Objects.<Iterable<? extends Reader>>requireNonNull(readers, "readers")).iterator();
/*  44 */     this.reader = nextReader();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SequenceReader(Reader... readers) {
/*  53 */     this(Arrays.asList(readers));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*  63 */     this.readers = null;
/*  64 */     this.reader = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Reader nextReader() {
/*  73 */     return this.readers.hasNext() ? this.readers.next() : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/*  83 */     int c = -1;
/*  84 */     while (this.reader != null) {
/*  85 */       c = this.reader.read();
/*  86 */       if (c != -1) {
/*     */         break;
/*     */       }
/*  89 */       this.reader = nextReader();
/*     */     } 
/*  91 */     return c;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int read(char[] cbuf, int off, int len) throws IOException {
/* 101 */     Objects.requireNonNull(cbuf, "cbuf");
/* 102 */     if (len < 0 || off < 0 || off + len > cbuf.length) {
/* 103 */       throw new IndexOutOfBoundsException("Array Size=" + cbuf.length + ", offset=" + off + ", length=" + len);
/*     */     }
/* 105 */     int count = 0;
/* 106 */     while (this.reader != null) {
/* 107 */       int readLen = this.reader.read(cbuf, off, len);
/* 108 */       if (readLen == -1) {
/* 109 */         this.reader = nextReader(); continue;
/*     */       } 
/* 111 */       count += readLen;
/* 112 */       off += readLen;
/* 113 */       len -= readLen;
/* 114 */       if (len <= 0) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */     
/* 119 */     if (count > 0) {
/* 120 */       return count;
/*     */     }
/* 122 */     return -1;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\input\SequenceReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */