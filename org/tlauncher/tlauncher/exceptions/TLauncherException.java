/*    */ package org.tlauncher.tlauncher.exceptions;
/*    */ 
/*    */ public class TLauncherException extends RuntimeException {
/*    */   private static final long serialVersionUID = 5812333186574527445L;
/*    */   
/*    */   public TLauncherException(String message, Throwable e) {
/*  7 */     super(message, e);
/*    */     
/*  9 */     e.printStackTrace();
/*    */   }
/*    */   
/*    */   public TLauncherException(String message) {
/* 13 */     super(message);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\exceptions\TLauncherException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */