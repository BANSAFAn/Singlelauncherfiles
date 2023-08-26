/*    */ package org.apache.commons.compress.utils;
/*    */ 
/*    */ import java.io.InputStream;
/*    */ import java.util.zip.CRC32;
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
/*    */ public class CRC32VerifyingInputStream
/*    */   extends ChecksumVerifyingInputStream
/*    */ {
/*    */   public CRC32VerifyingInputStream(InputStream in, long size, int expectedCrc32) {
/* 39 */     this(in, size, expectedCrc32 & 0xFFFFFFFFL);
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
/*    */   public CRC32VerifyingInputStream(InputStream in, long size, long expectedCrc32) {
/* 51 */     super(new CRC32(), in, size, expectedCrc32);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compres\\utils\CRC32VerifyingInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */