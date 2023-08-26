/*    */ package org.tlauncher.tlauncher.ui.login;
/*    */ 
/*    */ public class LoginWaitException extends LoginException {
/*    */   private final LoginWaitTask waitTask;
/*    */   
/*    */   public LoginWaitException(String reason, LoginWaitTask waitTask) {
/*  7 */     super(reason);
/*    */     
/*  9 */     if (waitTask == null) {
/* 10 */       throw new NullPointerException("wait task");
/*    */     }
/* 12 */     this.waitTask = waitTask;
/*    */   }
/*    */   
/*    */   public LoginWaitTask getWaitTask() {
/* 16 */     return this.waitTask;
/*    */   }
/*    */   
/*    */   public static interface LoginWaitTask {
/*    */     void runTask() throws LoginException;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\login\LoginWaitException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */