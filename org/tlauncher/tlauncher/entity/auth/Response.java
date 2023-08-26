/*   */ package org.tlauncher.tlauncher.entity.auth;
/*   */ public class Response {
/*   */   private String error;
/*   */   
/* 5 */   public void setError(String error) { this.error = error; } private String errorMessage; private String cause; public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; } public void setCause(String cause) { this.cause = cause; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof Response)) return false;  Response other = (Response)o; if (!other.canEqual(this)) return false;  Object this$error = getError(), other$error = other.getError(); if ((this$error == null) ? (other$error != null) : !this$error.equals(other$error)) return false;  Object this$errorMessage = getErrorMessage(), other$errorMessage = other.getErrorMessage(); if ((this$errorMessage == null) ? (other$errorMessage != null) : !this$errorMessage.equals(other$errorMessage)) return false;  Object this$cause = getCause(), other$cause = other.getCause(); return !((this$cause == null) ? (other$cause != null) : !this$cause.equals(other$cause)); } protected boolean canEqual(Object other) { return other instanceof Response; } public int hashCode() { int PRIME = 59; result = 1; Object $error = getError(); result = result * 59 + (($error == null) ? 43 : $error.hashCode()); Object $errorMessage = getErrorMessage(); result = result * 59 + (($errorMessage == null) ? 43 : $errorMessage.hashCode()); Object $cause = getCause(); return result * 59 + (($cause == null) ? 43 : $cause.hashCode()); } public String toString() { return "Response(error=" + getError() + ", errorMessage=" + getErrorMessage() + ", cause=" + getCause() + ")"; }
/*   */   
/* 7 */   public String getError() { return this.error; }
/* 8 */   public String getErrorMessage() { return this.errorMessage; } public String getCause() {
/* 9 */     return this.cause;
/*   */   }
/*   */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\entity\auth\Response.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */