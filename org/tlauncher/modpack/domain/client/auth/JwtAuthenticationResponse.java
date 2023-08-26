/*    */ package org.tlauncher.modpack.domain.client.auth;
/*    */ 
/*    */ 
/*    */ public class JwtAuthenticationResponse implements Serializable {
/*    */   private static final long serialVersionUID = 1250166508152483573L;
/*    */   private String token;
/*    */   
/*  8 */   public String getToken() { return this.token; } private Collection<String> authorities; private Integer codeError; private String causeError; public Collection<String> getAuthorities() { return this.authorities; } public Integer getCodeError() { return this.codeError; } public String getCauseError() { return this.causeError; } public void setToken(String token) { this.token = token; } public void setAuthorities(Collection<String> authorities) { this.authorities = authorities; } public void setCodeError(Integer codeError) { this.codeError = codeError; } public void setCauseError(String causeError) { this.causeError = causeError; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof JwtAuthenticationResponse)) return false;  JwtAuthenticationResponse other = (JwtAuthenticationResponse)o; if (!other.canEqual(this)) return false;  Object this$codeError = getCodeError(), other$codeError = other.getCodeError(); if ((this$codeError == null) ? (other$codeError != null) : !this$codeError.equals(other$codeError)) return false;  Object this$token = getToken(), other$token = other.getToken(); if ((this$token == null) ? (other$token != null) : !this$token.equals(other$token)) return false;  Object<String> this$authorities = (Object<String>)getAuthorities(), other$authorities = (Object<String>)other.getAuthorities(); if ((this$authorities == null) ? (other$authorities != null) : !this$authorities.equals(other$authorities)) return false;  Object this$causeError = getCauseError(), other$causeError = other.getCauseError(); return !((this$causeError == null) ? (other$causeError != null) : !this$causeError.equals(other$causeError)); } protected boolean canEqual(Object other) { return other instanceof JwtAuthenticationResponse; } public int hashCode() { int PRIME = 59; result = 1; Object $codeError = getCodeError(); result = result * 59 + (($codeError == null) ? 43 : $codeError.hashCode()); Object $token = getToken(); result = result * 59 + (($token == null) ? 43 : $token.hashCode()); Object<String> $authorities = (Object<String>)getAuthorities(); result = result * 59 + (($authorities == null) ? 43 : $authorities.hashCode()); Object $causeError = getCauseError(); return result * 59 + (($causeError == null) ? 43 : $causeError.hashCode()); } public String toString() { return "JwtAuthenticationResponse(token=" + getToken() + ", authorities=" + getAuthorities() + ", codeError=" + getCodeError() + ", causeError=" + getCauseError() + ")"; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JwtAuthenticationResponse() {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JwtAuthenticationResponse(String token) {
/* 21 */     this.token = token;
/*    */   }
/*    */   
/*    */   public JwtAuthenticationResponse(String token, Collection<String> authorities) {
/* 25 */     this.token = token;
/* 26 */     this.authorities = authorities;
/*    */   }
/*    */ 
/*    */   
/*    */   public JwtAuthenticationResponse(String token, Collection<String> authorities, Integer codeError, String causeError) {
/* 31 */     this.token = token;
/* 32 */     this.authorities = authorities;
/* 33 */     this.codeError = codeError;
/* 34 */     this.causeError = causeError;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\modpack\domain\client\auth\JwtAuthenticationResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */