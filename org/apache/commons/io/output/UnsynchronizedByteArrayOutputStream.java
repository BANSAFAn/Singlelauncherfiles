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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class UnsynchronizedByteArrayOutputStream
/*     */   extends AbstractByteArrayOutputStream
/*     */ {
/*     */   public UnsynchronizedByteArrayOutputStream() {
/*  38 */     this(1024);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UnsynchronizedByteArrayOutputStream(int size) {
/*  48 */     if (size < 0) {
/*  49 */       throw new IllegalArgumentException("Negative initial size: " + size);
/*     */     }
/*  51 */     needNewBuffer(size);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(byte[] b, int off, int len) {
/*  56 */     if (off < 0 || off > b.length || len < 0 || off + len > b.length || off + len < 0) {
/*  57 */       throw new IndexOutOfBoundsException(String.format("offset=%,d, length=%,d", new Object[] { Integer.valueOf(off), Integer.valueOf(len) }));
/*     */     }
/*  59 */     if (len == 0) {
/*     */       return;
/*     */     }
/*  62 */     writeImpl(b, off, len);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(int b) {
/*  67 */     writeImpl(b);
/*     */   }
/*     */ 
/*     */   
/*     */   public int write(InputStream in) throws IOException {
/*  72 */     return writeImpl(in);
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  77 */     return this.count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/*  85 */     resetImpl();
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeTo(OutputStream out) throws IOException {
/*  90 */     writeToImpl(out);
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
/*     */   public static InputStream toBufferedInputStream(InputStream input) throws IOException {
/* 111 */     return toBufferedInputStream(input, 1024);
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
/*     */   public static InputStream toBufferedInputStream(InputStream input, int size) throws IOException {
/* 134 */     try (UnsynchronizedByteArrayOutputStream output = new UnsynchronizedByteArrayOutputStream(size)) {
/* 135 */       output.write(input);
/* 136 */       return output.toInputStream();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream toInputStream() {
/* 142 */     return toInputStream(org.apache.commons.io.input.UnsynchronizedByteArrayInputStream::new);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] toByteArray() {
/* 147 */     return toByteArrayImpl();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\output\UnsynchronizedByteArrayOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */