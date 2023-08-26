/*     */ package org.apache.commons.io.output;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ByteArrayOutputStream
/*     */   extends AbstractByteArrayOutputStream
/*     */ {
/*     */   public ByteArrayOutputStream() {
/*  34 */     this(1024);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteArrayOutputStream(int size) {
/*  45 */     if (size < 0) {
/*  46 */       throw new IllegalArgumentException("Negative initial size: " + size);
/*     */     }
/*     */     
/*  49 */     synchronized (this) {
/*  50 */       needNewBuffer(size);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(byte[] b, int off, int len) {
/*  56 */     if (off < 0 || off > b.length || len < 0 || off + len > b.length || off + len < 0)
/*     */     {
/*     */ 
/*     */ 
/*     */       
/*  61 */       throw new IndexOutOfBoundsException();
/*     */     }
/*  63 */     if (len == 0) {
/*     */       return;
/*     */     }
/*  66 */     synchronized (this) {
/*  67 */       writeImpl(b, off, len);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void write(int b) {
/*  73 */     writeImpl(b);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int write(InputStream in) throws IOException {
/*  78 */     return writeImpl(in);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int size() {
/*  83 */     return this.count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void reset() {
/*  91 */     resetImpl();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void writeTo(OutputStream out) throws IOException {
/*  96 */     writeToImpl(out);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static InputStream toBufferedInputStream(InputStream input) throws IOException {
/* 123 */     return toBufferedInputStream(input, 1024);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static InputStream toBufferedInputStream(InputStream input, int size) throws IOException {
/* 151 */     try (ByteArrayOutputStream output = new ByteArrayOutputStream(size)) {
/* 152 */       output.write(input);
/* 153 */       return output.toInputStream();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized InputStream toInputStream() {
/* 159 */     return toInputStream(java.io.ByteArrayInputStream::new);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized byte[] toByteArray() {
/* 164 */     return toByteArrayImpl();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\output\ByteArrayOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */