/*    */ package org.tlauncher.util.async;
/*    */ 
/*    */ public class AsyncObjectGotErrorException
/*    */   extends AsyncObjectException {
/*    */   private static final long serialVersionUID = -1016561584766422788L;
/*    */   private final AsyncObject<?> object;
/*    */   
/*    */   public AsyncObjectGotErrorException(AsyncObject<?> object, Throwable error) {
/*  9 */     super(error);
/*    */     
/* 11 */     this.object = object;
/*    */   }
/*    */   
/*    */   public AsyncObject<?> getObject() {
/* 15 */     return this.object;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlaunche\\util\async\AsyncObjectGotErrorException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */