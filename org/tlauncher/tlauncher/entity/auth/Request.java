/*   */ package org.tlauncher.tlauncher.entity.auth;
/*   */ public class Request {
/*   */   private String clientToken;
/*   */   
/* 5 */   public String toString() { return "Request(clientToken=" + getClientToken() + ")"; } public int hashCode() { int PRIME = 59; result = 1; Object $clientToken = getClientToken(); return result * 59 + (($clientToken == null) ? 43 : $clientToken.hashCode()); } protected boolean canEqual(Object other) { return other instanceof Request; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof Request)) return false;  Request other = (Request)o; if (!other.canEqual(this)) return false;  Object this$clientToken = getClientToken(), other$clientToken = other.getClientToken(); return !((this$clientToken == null) ? (other$clientToken != null) : !this$clientToken.equals(other$clientToken)); } public void setClientToken(String clientToken) { this.clientToken = clientToken; }
/*   */    public String getClientToken() {
/* 7 */     return this.clientToken;
/*   */   }
/*   */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\entity\auth\Request.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */