/*    */ package org.apache.commons.io.output;
/*    */ 
/*    */ import java.io.FilterWriter;
/*    */ import java.io.IOException;
/*    */ import java.io.Writer;
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
/*    */ public class ChunkedWriter
/*    */   extends FilterWriter
/*    */ {
/*    */   private static final int DEFAULT_CHUNK_SIZE = 4096;
/*    */   private final int chunkSize;
/*    */   
/*    */   public ChunkedWriter(Writer writer, int chunkSize) {
/* 50 */     super(writer);
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
/*    */   public ChunkedWriter(Writer writer) {
/* 62 */     this(writer, 4096);
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
/*    */   public void write(char[] data, int srcOffset, int length) throws IOException {
/* 75 */     int bytes = length;
/* 76 */     int dstOffset = srcOffset;
/* 77 */     while (bytes > 0) {
/* 78 */       int chunk = Math.min(bytes, this.chunkSize);
/* 79 */       this.out.write(data, dstOffset, chunk);
/* 80 */       bytes -= chunk;
/* 81 */       dstOffset += chunk;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\output\ChunkedWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */