/*    */ package org.apache.commons.io.input.buffer;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.Objects;
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
/*    */ public class PeekableInputStream
/*    */   extends CircularBufferInputStream
/*    */ {
/*    */   public PeekableInputStream(InputStream inputStream, int bufferSize) {
/* 36 */     super(inputStream, bufferSize);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PeekableInputStream(InputStream inputStream) {
/* 45 */     super(inputStream);
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
/*    */   public boolean peek(byte[] sourceBuffer) throws IOException {
/* 57 */     Objects.requireNonNull(sourceBuffer, "sourceBuffer");
/* 58 */     return peek(sourceBuffer, 0, sourceBuffer.length);
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
/*    */   public boolean peek(byte[] sourceBuffer, int offset, int length) throws IOException {
/* 72 */     Objects.requireNonNull(sourceBuffer, "sourceBuffer");
/* 73 */     if (sourceBuffer.length > this.bufferSize) {
/* 74 */       throw new IllegalArgumentException("Peek request size of " + sourceBuffer.length + " bytes exceeds buffer size of " + this.bufferSize + " bytes");
/*    */     }
/*    */     
/* 77 */     if (this.buffer.getCurrentNumberOfBytes() < sourceBuffer.length) {
/* 78 */       fillBuffer();
/*    */     }
/* 80 */     return this.buffer.peek(sourceBuffer, offset, length);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\input\buffer\PeekableInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */