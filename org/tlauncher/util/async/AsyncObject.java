/*    */ package org.tlauncher.util.async;
/*    */ 
/*    */ public abstract class AsyncObject<E>
/*    */   extends ExtendedThread {
/*    */   private boolean gotValue;
/*    */   private E value;
/*    */   private AsyncObjectGotErrorException error;
/*    */   
/*    */   protected AsyncObject() {
/* 10 */     super("AsyncObject");
/*    */   }
/*    */ 
/*    */   
/*    */   public void run() {
/*    */     try {
/* 16 */       this.value = execute();
/* 17 */     } catch (Throwable e) {
/* 18 */       this.error = new AsyncObjectGotErrorException(this, e);
/*    */       
/*    */       return;
/*    */     } 
/* 22 */     this.gotValue = true;
/*    */   }
/*    */   
/*    */   public E getValue() throws AsyncObjectNotReadyException, AsyncObjectGotErrorException {
/* 26 */     if (this.error != null) {
/* 27 */       throw this.error;
/*    */     }
/* 29 */     if (!this.gotValue) {
/* 30 */       throw new AsyncObjectNotReadyException();
/*    */     }
/* 32 */     return this.value;
/*    */   }
/*    */   
/*    */   public AsyncObjectGotErrorException getError() {
/* 36 */     return this.error;
/*    */   }
/*    */   
/*    */   protected abstract E execute() throws AsyncObjectGotErrorException;
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlaunche\\util\async\AsyncObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */