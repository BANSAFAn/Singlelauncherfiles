/*    */ package org.tlauncher.tlauncher.entity.auth;
/*    */ 
/*    */ 
/*    */ public class RefreshRequest extends Request {
/*    */   private String accessToken;
/*    */   
/*  7 */   public void setAccessToken(String accessToken) { this.accessToken = accessToken; } private GameProfile selectedProfile; public void setSelectedProfile(GameProfile selectedProfile) { this.selectedProfile = selectedProfile; } public void setRequestUser(boolean requestUser) { this.requestUser = requestUser; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof RefreshRequest)) return false;  RefreshRequest other = (RefreshRequest)o; if (!other.canEqual(this)) return false;  Object this$accessToken = getAccessToken(), other$accessToken = other.getAccessToken(); if ((this$accessToken == null) ? (other$accessToken != null) : !this$accessToken.equals(other$accessToken)) return false;  Object this$selectedProfile = getSelectedProfile(), other$selectedProfile = other.getSelectedProfile(); return ((this$selectedProfile == null) ? (other$selectedProfile != null) : !this$selectedProfile.equals(other$selectedProfile)) ? false : (!(isRequestUser() != other.isRequestUser())); } protected boolean canEqual(Object other) { return other instanceof RefreshRequest; } public int hashCode() { int PRIME = 59; result = 1; Object $accessToken = getAccessToken(); result = result * 59 + (($accessToken == null) ? 43 : $accessToken.hashCode()); Object $selectedProfile = getSelectedProfile(); result = result * 59 + (($selectedProfile == null) ? 43 : $selectedProfile.hashCode()); return result * 59 + (isRequestUser() ? 79 : 97); } public String toString() { return "RefreshRequest(accessToken=" + getAccessToken() + ", selectedProfile=" + getSelectedProfile() + ", requestUser=" + isRequestUser() + ")"; }
/*    */   
/*  9 */   public String getAccessToken() { return this.accessToken; } public GameProfile getSelectedProfile() {
/* 10 */     return this.selectedProfile;
/*    */   } private boolean requestUser = true;
/*    */   public boolean isRequestUser() {
/* 13 */     return this.requestUser;
/*    */   }
/*    */   public RefreshRequest(Authenticator auth) {
/* 16 */     setClientToken(Authenticator.getClientToken().toString());
/* 17 */     setAccessToken(auth.getAccount().getAccessToken());
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\entity\auth\RefreshRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */