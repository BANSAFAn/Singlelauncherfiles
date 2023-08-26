/*    */ package org.tlauncher.tlauncher.minecraft.user;
/*    */ public class MicrosoftOAuthExchangeCode { private final RedirectUrl redirectUrl;
/*    */   private String code;
/*    */   
/*  5 */   public void setCode(String code) { this.code = code; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof MicrosoftOAuthExchangeCode)) return false;  MicrosoftOAuthExchangeCode other = (MicrosoftOAuthExchangeCode)o; if (!other.canEqual(this)) return false;  Object this$redirectUrl = getRedirectUrl(), other$redirectUrl = other.getRedirectUrl(); if ((this$redirectUrl == null) ? (other$redirectUrl != null) : !this$redirectUrl.equals(other$redirectUrl)) return false;  Object this$code = getCode(), other$code = other.getCode(); return !((this$code == null) ? (other$code != null) : !this$code.equals(other$code)); } protected boolean canEqual(Object other) { return other instanceof MicrosoftOAuthExchangeCode; } public int hashCode() { int PRIME = 59; result = 1; Object $redirectUrl = getRedirectUrl(); result = result * 59 + (($redirectUrl == null) ? 43 : $redirectUrl.hashCode()); Object $code = getCode(); return result * 59 + (($code == null) ? 43 : $code.hashCode()); } public String toString() { return "MicrosoftOAuthExchangeCode(redirectUrl=" + getRedirectUrl() + ", code=" + getCode() + ")"; }
/*    */   
/*  7 */   public RedirectUrl getRedirectUrl() { return this.redirectUrl; } public String getCode() {
/*  8 */     return this.code;
/*    */   }
/*    */   public MicrosoftOAuthExchangeCode(String code, RedirectUrl redirectUrl) {
/* 11 */     this.code = code;
/* 12 */     this.redirectUrl = redirectUrl;
/*    */   } }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraf\\user\MicrosoftOAuthExchangeCode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */