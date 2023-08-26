/*    */ package org.tlauncher.util.stream;
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
/*    */ public class StringStream
/*    */   extends SafeOutputStream
/*    */ {
/* 16 */   protected final StringBuilder buffer = new StringBuilder();
/*    */ 
/*    */   
/*    */   protected int caret;
/*    */ 
/*    */ 
/*    */   
/*    */   public void write(int b) {
/* 24 */     write((char)b);
/*    */   }
/*    */   
/*    */   protected synchronized void write(char c) {
/* 28 */     this.buffer.append(c);
/* 29 */     this.caret++;
/*    */   }
/*    */   
/*    */   public void write(char[] c) {
/* 33 */     if (c == null) {
/* 34 */       throw new NullPointerException();
/*    */     }
/* 36 */     if (c.length == 0) {
/*    */       return;
/*    */     }
/* 39 */     for (int i = 0; i < c.length; i++) {
/* 40 */       write(c[i]);
/*    */     }
/*    */   }
/*    */   
/*    */   public synchronized void flush() {
/* 45 */     this.caret = 0;
/* 46 */     this.buffer.setLength(0);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlaunche\\util\stream\StringStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */