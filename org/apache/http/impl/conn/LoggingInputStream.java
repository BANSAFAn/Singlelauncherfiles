/*     */ package org.apache.http.impl.conn;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
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
/*     */ @NotThreadSafe
/*     */ class LoggingInputStream
/*     */   extends InputStream
/*     */ {
/*     */   private final InputStream in;
/*     */   private final Wire wire;
/*     */   
/*     */   public LoggingInputStream(InputStream in, Wire wire) {
/*  48 */     this.in = in;
/*  49 */     this.wire = wire;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/*     */     try {
/*  55 */       int b = this.in.read();
/*  56 */       if (b == -1) {
/*  57 */         this.wire.input("end of stream");
/*     */       } else {
/*  59 */         this.wire.input(b);
/*     */       } 
/*  61 */       return b;
/*  62 */     } catch (IOException ex) {
/*  63 */       this.wire.input("[read] I/O error: " + ex.getMessage());
/*  64 */       throw ex;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] b) throws IOException {
/*     */     try {
/*  71 */       int bytesRead = this.in.read(b);
/*  72 */       if (bytesRead == -1) {
/*  73 */         this.wire.input("end of stream");
/*  74 */       } else if (bytesRead > 0) {
/*  75 */         this.wire.input(b, 0, bytesRead);
/*     */       } 
/*  77 */       return bytesRead;
/*  78 */     } catch (IOException ex) {
/*  79 */       this.wire.input("[read] I/O error: " + ex.getMessage());
/*  80 */       throw ex;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] b, int off, int len) throws IOException {
/*     */     try {
/*  87 */       int bytesRead = this.in.read(b, off, len);
/*  88 */       if (bytesRead == -1) {
/*  89 */         this.wire.input("end of stream");
/*  90 */       } else if (bytesRead > 0) {
/*  91 */         this.wire.input(b, off, bytesRead);
/*     */       } 
/*  93 */       return bytesRead;
/*  94 */     } catch (IOException ex) {
/*  95 */       this.wire.input("[read] I/O error: " + ex.getMessage());
/*  96 */       throw ex;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public long skip(long n) throws IOException {
/*     */     try {
/* 103 */       return super.skip(n);
/* 104 */     } catch (IOException ex) {
/* 105 */       this.wire.input("[skip] I/O error: " + ex.getMessage());
/* 106 */       throw ex;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int available() throws IOException {
/*     */     try {
/* 113 */       return this.in.available();
/* 114 */     } catch (IOException ex) {
/* 115 */       this.wire.input("[available] I/O error : " + ex.getMessage());
/* 116 */       throw ex;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void mark(int readlimit) {
/* 122 */     super.mark(readlimit);
/*     */   }
/*     */ 
/*     */   
/*     */   public void reset() throws IOException {
/* 127 */     super.reset();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean markSupported() {
/* 132 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*     */     try {
/* 138 */       this.in.close();
/* 139 */     } catch (IOException ex) {
/* 140 */       this.wire.input("[close] I/O error: " + ex.getMessage());
/* 141 */       throw ex;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\conn\LoggingInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */