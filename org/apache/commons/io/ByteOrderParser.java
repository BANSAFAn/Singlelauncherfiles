/*    */ package org.apache.commons.io;
/*    */ 
/*    */ import java.nio.ByteOrder;
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
/*    */ public final class ByteOrderParser
/*    */ {
/*    */   public static ByteOrder parseByteOrder(String value) {
/* 56 */     if (ByteOrder.BIG_ENDIAN.toString().equals(value)) {
/* 57 */       return ByteOrder.BIG_ENDIAN;
/*    */     }
/* 59 */     if (ByteOrder.LITTLE_ENDIAN.toString().equals(value)) {
/* 60 */       return ByteOrder.LITTLE_ENDIAN;
/*    */     }
/* 62 */     throw new IllegalArgumentException("Unsupported byte order setting: " + value + ", expected one of " + ByteOrder.LITTLE_ENDIAN + ", " + ByteOrder.BIG_ENDIAN);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\ByteOrderParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */