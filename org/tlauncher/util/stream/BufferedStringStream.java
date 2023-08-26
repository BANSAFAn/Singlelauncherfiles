/*    */ package org.tlauncher.util.stream;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BufferedStringStream
/*    */   extends StringStream
/*    */ {
/*    */   public void write(char b) {
/* 11 */     super.write(b);
/*    */     
/* 13 */     if (b == '\n')
/* 14 */       flush(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlaunche\\util\stream\BufferedStringStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */