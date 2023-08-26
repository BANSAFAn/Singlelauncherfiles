/*    */ package org.tlauncher.tlauncher.entity.auth;
/*    */ 
/*    */ import org.tlauncher.tlauncher.minecraft.auth.Agent;
/*    */ import org.tlauncher.tlauncher.minecraft.auth.Authenticator;
/*    */ 
/*    */ public class AuthenticationRequest extends Request {
/*  7 */   public void setAgent(Agent agent) { this.agent = agent; } public void setUsername(String username) { this.username = username; } public void setPassword(String password) { this.password = password; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof AuthenticationRequest)) return false;  AuthenticationRequest other = (AuthenticationRequest)o; if (!other.canEqual(this)) return false;  Object this$agent = getAgent(), other$agent = other.getAgent(); if ((this$agent == null) ? (other$agent != null) : !this$agent.equals(other$agent)) return false;  Object this$username = getUsername(), other$username = other.getUsername(); if ((this$username == null) ? (other$username != null) : !this$username.equals(other$username)) return false;  Object this$password = getPassword(), other$password = other.getPassword(); return !((this$password == null) ? (other$password != null) : !this$password.equals(other$password)); } protected boolean canEqual(Object other) { return other instanceof AuthenticationRequest; } public int hashCode() { int PRIME = 59; result = 1; Object $agent = getAgent(); result = result * 59 + (($agent == null) ? 43 : $agent.hashCode()); Object $username = getUsername(); result = result * 59 + (($username == null) ? 43 : $username.hashCode()); Object $password = getPassword(); return result * 59 + (($password == null) ? 43 : $password.hashCode()); } public String toString() { return "AuthenticationRequest(agent=" + getAgent() + ", username=" + getUsername() + ", password=" + getPassword() + ")"; }
/*    */   
/*  9 */   private String username; private Agent agent = Agent.MINECRAFT; private String password; public Agent getAgent() { return this.agent; }
/* 10 */   public String getUsername() { return this.username; } public String getPassword() { return this.password; }
/*    */   
/*    */   public AuthenticationRequest(Authenticator auth) {
/* 13 */     this.username = auth.getAccount().getUsername();
/* 14 */     this.password = auth.getAccount().getPassword();
/* 15 */     setClientToken(Authenticator.getClientToken().toString());
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\entity\auth\AuthenticationRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */