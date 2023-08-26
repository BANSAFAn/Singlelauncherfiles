/*    */ package org.apache.commons.compress.compressors.deflate;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.util.zip.Deflater;
/*    */ import java.util.zip.DeflaterOutputStream;
/*    */ import org.apache.commons.compress.compressors.CompressorOutputStream;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DeflateCompressorOutputStream
/*    */   extends CompressorOutputStream
/*    */ {
/*    */   private final DeflaterOutputStream out;
/*    */   private final Deflater deflater;
/*    */   
/*    */   public DeflateCompressorOutputStream(OutputStream outputStream) {
/* 41 */     this(outputStream, new DeflateParameters());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DeflateCompressorOutputStream(OutputStream outputStream, DeflateParameters parameters) {
/* 51 */     this.deflater = new Deflater(parameters.getCompressionLevel(), !parameters.withZlibHeader());
/* 52 */     this.out = new DeflaterOutputStream(outputStream, this.deflater);
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(int b) throws IOException {
/* 57 */     this.out.write(b);
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(byte[] buf, int off, int len) throws IOException {
/* 62 */     this.out.write(buf, off, len);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void flush() throws IOException {
/* 73 */     this.out.flush();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void finish() throws IOException {
/* 82 */     this.out.finish();
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/*    */     try {
/* 88 */       this.out.close();
/*    */     } finally {
/* 90 */       this.deflater.end();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\compressors\deflate\DeflateCompressorOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */