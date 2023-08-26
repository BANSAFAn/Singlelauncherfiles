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
/*    */ class UnsupportedOperationExceptions
/*    */ {
/*    */   private static final String MARK_RESET = "mark/reset";
/*    */   
/*    */   static UnsupportedOperationException mark() {
/* 38 */     return method("mark/reset");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static UnsupportedOperationException method(String method) {
/* 48 */     return new UnsupportedOperationException(method + " not supported");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static UnsupportedOperationException reset() {
/* 58 */     return method("mark/reset");
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\input\UnsupportedOperationExceptions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */