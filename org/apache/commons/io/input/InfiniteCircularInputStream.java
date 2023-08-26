/*    */ package org.apache.commons.io.input;
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
/*    */ public class InfiniteCircularInputStream
/*    */   extends CircularInputStream
/*    */ {
/*    */   public InfiniteCircularInputStream(byte[] repeatContent) {
/* 41 */     super(repeatContent, -1L);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\input\InfiniteCircularInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */