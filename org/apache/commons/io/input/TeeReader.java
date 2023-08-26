/*     */ package org.apache.commons.io.input;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.nio.CharBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TeeReader
/*     */   extends ProxyReader
/*     */ {
/*     */   private final Writer branch;
/*     */   private final boolean closeBranch;
/*     */   
/*     */   public TeeReader(Reader input, Writer branch) {
/*  57 */     this(input, branch, false);
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
/*     */   public TeeReader(Reader input, Writer branch, boolean closeBranch) {
/*  70 */     super(input);
/*  71 */     this.branch = branch;
/*  72 */     this.closeBranch = closeBranch;
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
/*     */     try {
/*  84 */       super.close();
/*     */     } finally {
/*  86 */       if (this.closeBranch) {
/*  87 */         this.branch.close();
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
/*     */   public int read() throws IOException {
/* 100 */     int ch = super.read();
/* 101 */     if (ch != -1) {
/* 102 */       this.branch.write(ch);
/*     */     }
/* 104 */     return ch;
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
/*     */   public int read(char[] chr) throws IOException {
/* 116 */     int n = super.read(chr);
/* 117 */     if (n != -1) {
/* 118 */       this.branch.write(chr, 0, n);
/*     */     }
/* 120 */     return n;
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
/*     */   public int read(char[] chr, int st, int end) throws IOException {
/* 134 */     int n = super.read(chr, st, end);
/* 135 */     if (n != -1) {
/* 136 */       this.branch.write(chr, st, n);
/*     */     }
/* 138 */     return n;
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
/*     */   public int read(CharBuffer target) throws IOException {
/* 150 */     int originalPosition = target.position();
/* 151 */     int n = super.read(target);
/* 152 */     if (n != -1) {
/*     */ 
/*     */       
/* 155 */       int newPosition = target.position();
/* 156 */       int newLimit = target.limit();
/*     */       try {
/* 158 */         target.position(originalPosition).limit(newPosition);
/* 159 */         this.branch.append(target);
/*     */       } finally {
/*     */         
/* 162 */         target.position(newPosition).limit(newLimit);
/*     */       } 
/*     */     } 
/* 165 */     return n;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\input\TeeReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */