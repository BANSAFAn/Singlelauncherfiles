/*    */ package org.apache.commons.io.output;
/*    */ 
/*    */ import java.io.FilterOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
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
/*    */ public class ChunkedOutputStream
/*    */   extends FilterOutputStream
/*    */ {
/*    */   private static final int DEFAULT_CHUNK_SIZE = 4096;
/*    */   private final int chunkSize;
/*    */   
/*    */   public ChunkedOutputStream(OutputStream stream, int chunkSize) {
/* 50 */     super(stream);
/* 51 */     if (chunkSize <= 0) {
/* 52 */       throw new IllegalArgumentException();
/*    */     }
/* 54 */     this.chunkSize = chunkSize;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ChunkedOutputStream(OutputStream stream) {
/* 63 */     this(stream, 4096);
/*    */   }
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
/*    */   public void write(byte[] data, int srcOffset, int length) throws IOException {
/* 77 */     int bytes = length;
/* 78 */     int dstOffset = srcOffset;
/* 79 */     while (bytes > 0) {
/* 80 */       int chunk = Math.min(bytes, this.chunkSize);
/* 81 */       this.out.write(data, dstOffset, chunk);
/* 82 */       bytes -= chunk;
/* 83 */       dstOffset += chunk;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\output\ChunkedOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */