/*    */ package org.tlauncher.tlauncher.entity.auth;
/*    */ 
/*    */ public class RefreshResponse extends Response {
/*    */   private String accessToken;
/*    */   private String clientToken;
/*    */   
/*  7 */   public void setAccessToken(String accessToken) { this.accessToken = accessToken; } private GameProfile selectedProfile; private User user; public void setClientToken(String clientToken) { this.clientToken = clientToken; } public void setSelectedProfile(GameProfile selectedProfile) { this.selectedProfile = selectedProfile; } public void setUser(User user) { this.user = user; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof RefreshResponse)) return false;  RefreshResponse other = (RefreshResponse)o; if (!other.canEqual(this)) return false;  Object this$accessToken = getAccessToken(), other$accessToken = other.getAccessToken(); if ((this$accessToken == null) ? (other$accessToken != null) : !this$accessToken.equals(other$accessToken)) return false;  Object this$clientToken = getClientToken(), other$clientToken = other.getClientToken(); if ((this$clientToken == null) ? (other$clientToken != null) : !this$clientToken.equals(other$clientToken)) return false;  Object this$selectedProfile = getSelectedProfile(), other$selectedProfile = other.getSelectedProfile(); if ((this$selectedProfile == null) ? (other$selectedProfile != null) : !this$selectedProfile.equals(other$selectedProfile)) return false;  Object this$user = getUser(), other$user = other.getUser(); return !((this$user == null) ? (other$user != null) : !this$user.equals(other$user)); } protected boolean canEqual(Object other) { return other instanceof RefreshResponse; } public int hashCode() { int PRIME = 59; result = 1; Object $accessToken = getAccessToken(); result = result * 59 + (($accessToken == null) ? 43 : $accessToken.hashCode()); Object $clientToken = getClientToken(); result = result * 59 + (($clientToken == null) ? 43 : $clientToken.hashCode()); Object $selectedProfile = getSelectedProfile(); result = result * 59 + (($selectedProfile == null) ? 43 : $selectedProfile.hashCode()); Object $user = getUser(); return result * 59 + (($user == null) ? 43 : $user.hashCode()); } public String toString() { return "RefreshResponse(accessToken=" + getAccessToken() + ", clientToken=" + getClientToken() + ", selectedProfile=" + getSelectedProfile() + ", user=" + getUser() + ")"; }
/*    */   
/*  9 */   public String getAccessToken() { return this.accessToken; }
/* 10 */   public String getClientToken() { return this.clientToken; }
/* 11 */   public GameProfile getSelectedProfile() { return this.selectedProfile; } public User getUser() {
/* 12 */     return this.user;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\entity\auth\RefreshResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */