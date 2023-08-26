/*    */ package org.tlauncher.tlauncher.listeners.auth;
/*    */ 
/*    */ 
/*    */ public class RefreshTokenListener implements AuthenticatorListener {
/*    */   private final ValidateAccountToken validateAccountToken;
/*    */   private volatile Exception exception;
/*    */   
/*  8 */   public RefreshTokenListener(ValidateAccountToken validateAccountToken) { this.validateAccountToken = validateAccountToken; }
/*  9 */   public void setException(Exception exception) { this.exception = exception; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof RefreshTokenListener)) return false;  RefreshTokenListener other = (RefreshTokenListener)o; if (!other.canEqual(this)) return false;  Object this$validateAccountToken = getValidateAccountToken(), other$validateAccountToken = other.getValidateAccountToken(); if ((this$validateAccountToken == null) ? (other$validateAccountToken != null) : !this$validateAccountToken.equals(other$validateAccountToken)) return false;  Object this$exception = getException(), other$exception = other.getException(); return !((this$exception == null) ? (other$exception != null) : !this$exception.equals(other$exception)); } protected boolean canEqual(Object other) { return other instanceof RefreshTokenListener; } public int hashCode() { int PRIME = 59; result = 1; Object $validateAccountToken = getValidateAccountToken(); result = result * 59 + (($validateAccountToken == null) ? 43 : $validateAccountToken.hashCode()); Object $exception = getException(); return result * 59 + (($exception == null) ? 43 : $exception.hashCode()); } public String toString() { return "RefreshTokenListener(validateAccountToken=" + getValidateAccountToken() + ", exception=" + getException() + ")"; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ValidateAccountToken getValidateAccountToken() {
/* 15 */     return this.validateAccountToken; } public Exception getException() {
/* 16 */     return this.exception;
/*    */   }
/*    */   
/*    */   public void onAuthPassing(Authenticator auth) {
/* 20 */     this.exception = null;
/*    */   }
/*    */   
/*    */   private void unblock() {
/* 24 */     synchronized (this) {
/* 25 */       notifyAll();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onAuthPassingError(Authenticator auth, Exception e) {
/* 31 */     this.exception = e;
/* 32 */     unblock();
/*    */   }
/*    */ 
/*    */   
/*    */   public void onAuthPassed(Authenticator auth) {
/* 37 */     unblock();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\listeners\auth\RefreshTokenListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */