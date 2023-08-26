/*     */ package org.apache.commons.io.input;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WindowsLineEndingInputStream
/*     */   extends InputStream
/*     */ {
/*     */   private boolean slashRSeen;
/*     */   private boolean slashNSeen;
/*     */   private boolean injectSlashN;
/*     */   private boolean eofSeen;
/*     */   private final InputStream target;
/*     */   private final boolean ensureLineFeedAtEndOfFile;
/*     */   
/*     */   public WindowsLineEndingInputStream(InputStream in, boolean ensureLineFeedAtEndOfFile) {
/*  52 */     this.target = in;
/*  53 */     this.ensureLineFeedAtEndOfFile = ensureLineFeedAtEndOfFile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int readWithUpdate() throws IOException {
/*  62 */     int target = this.target.read();
/*  63 */     this.eofSeen = (target == -1);
/*  64 */     if (this.eofSeen) {
/*  65 */       return target;
/*     */     }
/*  67 */     this.slashRSeen = (target == 13);
/*  68 */     this.slashNSeen = (target == 10);
/*  69 */     return target;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/*  77 */     if (this.eofSeen) {
/*  78 */       return eofGame();
/*     */     }
/*  80 */     if (this.injectSlashN) {
/*  81 */       this.injectSlashN = false;
/*  82 */       return 10;
/*     */     } 
/*  84 */     boolean prevWasSlashR = this.slashRSeen;
/*  85 */     int target = readWithUpdate();
/*  86 */     if (this.eofSeen) {
/*  87 */       return eofGame();
/*     */     }
/*  89 */     if (target == 10 && !prevWasSlashR) {
/*  90 */       this.injectSlashN = true;
/*  91 */       return 13;
/*     */     } 
/*  93 */     return target;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int eofGame() {
/* 101 */     if (!this.ensureLineFeedAtEndOfFile) {
/* 102 */       return -1;
/*     */     }
/* 104 */     if (!this.slashNSeen && !this.slashRSeen) {
/* 105 */       this.slashRSeen = true;
/* 106 */       return 13;
/*     */     } 
/* 108 */     if (!this.slashNSeen) {
/* 109 */       this.slashRSeen = false;
/* 110 */       this.slashNSeen = true;
/* 111 */       return 10;
/*     */     } 
/* 113 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 122 */     super.close();
/* 123 */     this.target.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void mark(int readlimit) {
/* 131 */     throw UnsupportedOperationExceptions.mark();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\input\WindowsLineEndingInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */