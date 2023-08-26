/*    */ package org.tlauncher.modpack.domain.client.auth;
/*    */ 
/*    */ 
/*    */ public class JwtAuthenticationRequest implements Serializable {
/*    */   private static final long serialVersionUID = -8445943548965154778L;
/*    */   private String username;
/*    */   private String password;
/*    */   private String email;
/*    */   
/* 10 */   public String getUsername() { return this.username; } public String getPassword() { return this.password; } public String getEmail() { return this.email; } public void setUsername(String username) { this.username = username; } public void setPassword(String password) { this.password = password; } public void setEmail(String email) { this.email = email; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof JwtAuthenticationRequest)) return false;  JwtAuthenticationRequest other = (JwtAuthenticationRequest)o; if (!other.canEqual(this)) return false;  Object this$username = getUsername(), other$username = other.getUsername(); if ((this$username == null) ? (other$username != null) : !this$username.equals(other$username)) return false;  Object this$password = getPassword(), other$password = other.getPassword(); if ((this$password == null) ? (other$password != null) : !this$password.equals(other$password)) return false;  Object this$email = getEmail(), other$email = other.getEmail(); return !((this$email == null) ? (other$email != null) : !this$email.equals(other$email)); } protected boolean canEqual(Object other) { return other instanceof JwtAuthenticationRequest; } public int hashCode() { int PRIME = 59; result = 1; Object $username = getUsername(); result = result * 59 + (($username == null) ? 43 : $username.hashCode()); Object $password = getPassword(); result = result * 59 + (($password == null) ? 43 : $password.hashCode()); Object $email = getEmail(); return result * 59 + (($email == null) ? 43 : $email.hashCode()); } public String toString() { return "JwtAuthenticationRequest(username=" + getUsername() + ", password=" + getPassword() + ", email=" + getEmail() + ")"; }
/*    */ 
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\modpack\domain\client\auth\JwtAuthenticationRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */