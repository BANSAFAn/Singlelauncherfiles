/*    */ package org.apache.commons.io;
/*    */ 
/*    */ import java.io.IOException;
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
/*    */ public class IOIndexedException
/*    */   extends IOException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private final int index;
/*    */   
/*    */   public IOIndexedException(int index, Throwable cause) {
/* 39 */     super(toMessage(index, cause), cause);
/* 40 */     this.index = index;
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
/*    */   protected static String toMessage(int index, Throwable cause) {
/* 52 */     String unspecified = "Null";
/* 53 */     String name = (cause == null) ? "Null" : cause.getClass().getSimpleName();
/* 54 */     String msg = (cause == null) ? "Null" : cause.getMessage();
/* 55 */     return String.format("%s #%,d: %s", new Object[] { name, Integer.valueOf(index), msg });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getIndex() {
/* 64 */     return this.index;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\IOIndexedException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */