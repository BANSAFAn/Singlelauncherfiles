/*    */ package org.tlauncher.util.stream;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class SafeOutputStream
/*    */   extends OutputStream
/*    */ {
/*    */   public void write(byte[] b) {
/*    */     try {
/* 17 */       super.write(b);
/* 18 */     } catch (IOException iOException) {}
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void write(byte[] b, int off, int len) {
/*    */     try {
/* 25 */       super.write(b, off, len);
/* 26 */     } catch (IOException iOException) {}
/*    */   }
/*    */   
/*    */   public void flush() {}
/*    */   
/*    */   public void close() {}
/*    */   
/*    */   public abstract void write(int paramInt);
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlaunche\\util\stream\SafeOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */