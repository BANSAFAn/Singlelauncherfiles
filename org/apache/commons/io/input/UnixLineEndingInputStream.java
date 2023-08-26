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
/*     */ public class UnixLineEndingInputStream
/*     */   extends InputStream
/*     */ {
/*     */   private boolean slashNSeen;
/*     */   private boolean slashRSeen;
/*     */   private boolean eofSeen;
/*     */   private final InputStream target;
/*     */   private final boolean ensureLineFeedAtEndOfFile;
/*     */   
/*     */   public UnixLineEndingInputStream(InputStream in, boolean ensureLineFeedAtEndOfFile) {
/*  50 */     this.target = in;
/*  51 */     this.ensureLineFeedAtEndOfFile = ensureLineFeedAtEndOfFile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int readWithUpdate() throws IOException {
/*  60 */     int target = this.target.read();
/*  61 */     this.eofSeen = (target == -1);
/*  62 */     if (this.eofSeen) {
/*  63 */       return target;
/*     */     }
/*  65 */     this.slashNSeen = (target == 10);
/*  66 */     this.slashRSeen = (target == 13);
/*  67 */     return target;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/*  75 */     boolean previousWasSlashR = this.slashRSeen;
/*  76 */     if (this.eofSeen) {
/*  77 */       return eofGame(previousWasSlashR);
/*     */     }
/*  79 */     int target = readWithUpdate();
/*  80 */     if (this.eofSeen) {
/*  81 */       return eofGame(previousWasSlashR);
/*     */     }
/*  83 */     if (this.slashRSeen) {
/*  84 */       return 10;
/*     */     }
/*     */     
/*  87 */     if (previousWasSlashR && this.slashNSeen) {
/*  88 */       return read();
/*     */     }
/*     */     
/*  91 */     return target;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int eofGame(boolean previousWasSlashR) {
/* 100 */     if (previousWasSlashR || !this.ensureLineFeedAtEndOfFile) {
/* 101 */       return -1;
/*     */     }
/* 103 */     if (!this.slashNSeen) {
/* 104 */       this.slashNSeen = true;
/* 105 */       return 10;
/*     */     } 
/* 107 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 116 */     super.close();
/* 117 */     this.target.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void mark(int readlimit) {
/* 125 */     throw UnsupportedOperationExceptions.mark();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\input\UnixLineEndingInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */