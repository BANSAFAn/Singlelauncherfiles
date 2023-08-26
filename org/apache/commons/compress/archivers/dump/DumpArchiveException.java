/*    */ package org.apache.commons.compress.archivers.dump;
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
/*    */ public class DumpArchiveException
/*    */   extends IOException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public DumpArchiveException() {}
/*    */   
/*    */   public DumpArchiveException(String msg) {
/* 34 */     super(msg);
/*    */   }
/*    */   
/*    */   public DumpArchiveException(Throwable cause) {
/* 38 */     initCause(cause);
/*    */   }
/*    */   
/*    */   public DumpArchiveException(String msg, Throwable cause) {
/* 42 */     super(msg);
/* 43 */     initCause(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\archivers\dump\DumpArchiveException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */