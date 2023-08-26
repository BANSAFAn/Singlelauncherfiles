/*     */ package org.apache.http.client.entity;
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
/*     */ @NotThreadSafe
/*     */ class LazyDecompressingInputStream
/*     */   extends InputStream
/*     */ {
/*     */   private final InputStream wrappedStream;
/*     */   private final InputStreamFactory inputStreamFactory;
/*     */   private InputStream wrapperStream;
/*     */   
/*     */   public LazyDecompressingInputStream(InputStream wrappedStream, InputStreamFactory inputStreamFactory) {
/*  48 */     this.wrappedStream = wrappedStream;
/*  49 */     this.inputStreamFactory = inputStreamFactory;
/*     */   }
/*     */   
/*     */   private void initWrapper() throws IOException {
/*  53 */     if (this.wrapperStream == null) {
/*  54 */       this.wrapperStream = this.inputStreamFactory.create(this.wrappedStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/*  60 */     initWrapper();
/*  61 */     return this.wrapperStream.read();
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] b) throws IOException {
/*  66 */     initWrapper();
/*  67 */     return this.wrapperStream.read(b);
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] b, int off, int len) throws IOException {
/*  72 */     initWrapper();
/*  73 */     return this.wrapperStream.read(b, off, len);
/*     */   }
/*     */ 
/*     */   
/*     */   public long skip(long n) throws IOException {
/*  78 */     initWrapper();
/*  79 */     return this.wrapperStream.skip(n);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean markSupported() {
/*  84 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int available() throws IOException {
/*  89 */     initWrapper();
/*  90 */     return this.wrapperStream.available();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*     */     try {
/*  96 */       if (this.wrapperStream != null) {
/*  97 */         this.wrapperStream.close();
/*     */       }
/*     */     } finally {
/* 100 */       this.wrappedStream.close();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\client\entity\LazyDecompressingInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */