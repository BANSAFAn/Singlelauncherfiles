/*    */ package org.apache.commons.io.input;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
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
/*    */ public class MarkShieldInputStream
/*    */   extends ProxyInputStream
/*    */ {
/*    */   public MarkShieldInputStream(InputStream in) {
/* 45 */     super(in);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void mark(int readlimit) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean markSupported() {
/* 56 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void reset() throws IOException {
/* 62 */     throw UnsupportedOperationExceptions.reset();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\input\MarkShieldInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */