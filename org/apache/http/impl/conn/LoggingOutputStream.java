/*     */ package org.apache.http.impl.conn;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
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
/*     */ class LoggingOutputStream
/*     */   extends OutputStream
/*     */ {
/*     */   private final OutputStream out;
/*     */   private final Wire wire;
/*     */   
/*     */   public LoggingOutputStream(OutputStream out, Wire wire) {
/*  48 */     this.out = out;
/*  49 */     this.wire = wire;
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(int b) throws IOException {
/*     */     try {
/*  55 */       this.wire.output(b);
/*  56 */     } catch (IOException ex) {
/*  57 */       this.wire.output("[write] I/O error: " + ex.getMessage());
/*  58 */       throw ex;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(byte[] b) throws IOException {
/*     */     try {
/*  65 */       this.wire.output(b);
/*  66 */       this.out.write(b);
/*  67 */     } catch (IOException ex) {
/*  68 */       this.wire.output("[write] I/O error: " + ex.getMessage());
/*  69 */       throw ex;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(byte[] b, int off, int len) throws IOException {
/*     */     try {
/*  76 */       this.wire.output(b, off, len);
/*  77 */       this.out.write(b, off, len);
/*  78 */     } catch (IOException ex) {
/*  79 */       this.wire.output("[write] I/O error: " + ex.getMessage());
/*  80 */       throw ex;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/*     */     try {
/*  87 */       this.out.flush();
/*  88 */     } catch (IOException ex) {
/*  89 */       this.wire.output("[flush] I/O error: " + ex.getMessage());
/*  90 */       throw ex;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*     */     try {
/*  97 */       this.out.close();
/*  98 */     } catch (IOException ex) {
/*  99 */       this.wire.output("[close] I/O error: " + ex.getMessage());
/* 100 */       throw ex;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\conn\LoggingOutputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */