/*    */ package org.apache.commons.compress.archivers.zip;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.nio.ByteOrder;
/*    */ import org.apache.commons.compress.utils.BitInputStream;
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
/*    */ class BitStream
/*    */   extends BitInputStream
/*    */ {
/*    */   BitStream(InputStream in) {
/* 37 */     super(in, ByteOrder.LITTLE_ENDIAN);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   int nextBit() throws IOException {
/* 46 */     return (int)readBits(1);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   long nextBits(int n) throws IOException {
/* 56 */     if (n < 0 || n > 8) {
/* 57 */       throw new IOException("Trying to read " + n + " bits, at most 8 are allowed");
/*    */     }
/* 59 */     return readBits(n);
/*    */   }
/*    */   
/*    */   int nextByte() throws IOException {
/* 63 */     return (int)readBits(8);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\archivers\zip\BitStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */