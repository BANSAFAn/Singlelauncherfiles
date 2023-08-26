/*     */ package org.apache.commons.io;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LineIterator
/*     */   implements Iterator<String>, Closeable
/*     */ {
/*     */   private final BufferedReader bufferedReader;
/*     */   private String cachedLine;
/*     */   private boolean finished;
/*     */   
/*     */   public LineIterator(Reader reader) throws IllegalArgumentException {
/*  68 */     if (reader == null) {
/*  69 */       throw new IllegalArgumentException("Reader must not be null");
/*     */     }
/*  71 */     if (reader instanceof BufferedReader) {
/*  72 */       this.bufferedReader = (BufferedReader)reader;
/*     */     } else {
/*  74 */       this.bufferedReader = new BufferedReader(reader);
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
/*     */   public boolean hasNext() {
/*  88 */     if (this.cachedLine != null) {
/*  89 */       return true;
/*     */     }
/*  91 */     if (this.finished) {
/*  92 */       return false;
/*     */     }
/*     */     try {
/*     */       while (true) {
/*  96 */         String line = this.bufferedReader.readLine();
/*  97 */         if (line == null) {
/*  98 */           this.finished = true;
/*  99 */           return false;
/*     */         } 
/* 101 */         if (isValidLine(line)) {
/* 102 */           this.cachedLine = line;
/* 103 */           return true;
/*     */         } 
/*     */       } 
/* 106 */     } catch (IOException ioe) {
/* 107 */       IOUtils.closeQuietly(this, ioe::addSuppressed);
/* 108 */       throw new IllegalStateException(ioe);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isValidLine(String line) {
/* 119 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String next() {
/* 130 */     return nextLine();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String nextLine() {
/* 140 */     if (!hasNext()) {
/* 141 */       throw new NoSuchElementException("No more lines");
/*     */     }
/* 143 */     String currentLine = this.cachedLine;
/* 144 */     this.cachedLine = null;
/* 145 */     return currentLine;
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
/*     */   public void close() throws IOException {
/* 159 */     this.finished = true;
/* 160 */     this.cachedLine = null;
/* 161 */     IOUtils.close(this.bufferedReader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove() {
/* 171 */     throw new UnsupportedOperationException("remove not supported");
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
/*     */   @Deprecated
/*     */   public static void closeQuietly(LineIterator iterator) {
/* 184 */     IOUtils.closeQuietly(iterator);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\LineIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */