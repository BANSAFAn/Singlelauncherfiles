/*    */ package org.apache.commons.compress.compressors.deflate;
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
/*    */ public class DeflateParameters
/*    */ {
/*    */   static final int MAX_LEVEL = 9;
/*    */   static final int MIN_LEVEL = 0;
/*    */   private boolean zlibHeader = true;
/* 34 */   private int compressionLevel = -1;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean withZlibHeader() {
/* 42 */     return this.zlibHeader;
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
/*    */   public void setWithZlibHeader(boolean zlibHeader) {
/* 54 */     this.zlibHeader = zlibHeader;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getCompressionLevel() {
/* 63 */     return this.compressionLevel;
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
/*    */   public void setCompressionLevel(int compressionLevel) {
/* 76 */     if (compressionLevel < 0 || compressionLevel > 9) {
/* 77 */       throw new IllegalArgumentException("Invalid Deflate compression level: " + compressionLevel);
/*    */     }
/* 79 */     this.compressionLevel = compressionLevel;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\compressors\deflate\DeflateParameters.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */