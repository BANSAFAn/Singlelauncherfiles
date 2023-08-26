/*    */ package org.tlauncher.tlauncher.minecraft.user.oauth;
/*    */ 
/*    */ 
/*    */ public class OAuthApplication
/*    */ {
/*    */   public void setBasicURL(String basicURL) {
/*  7 */     this.basicURL = basicURL; } public void setTokenURL(String tokenURL) { this.tokenURL = tokenURL; } public void setRedirectURL(String redirectURL) { this.redirectURL = redirectURL; } public void setBackButtonURL(String backButtonURL) { this.backButtonURL = backButtonURL; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof OAuthApplication)) return false;  OAuthApplication other = (OAuthApplication)o; if (!other.canEqual(this)) return false;  Object this$clientId = getClientId(), other$clientId = other.getClientId(); if ((this$clientId == null) ? (other$clientId != null) : !this$clientId.equals(other$clientId)) return false;  Object this$scope = getScope(), other$scope = other.getScope(); if ((this$scope == null) ? (other$scope != null) : !this$scope.equals(other$scope)) return false;  if (isUseWeirdXboxTokenPrefix() != other.isUseWeirdXboxTokenPrefix()) return false;  Object this$basicURL = getBasicURL(), other$basicURL = other.getBasicURL(); if ((this$basicURL == null) ? (other$basicURL != null) : !this$basicURL.equals(other$basicURL)) return false;  Object this$tokenURL = getTokenURL(), other$tokenURL = other.getTokenURL(); if ((this$tokenURL == null) ? (other$tokenURL != null) : !this$tokenURL.equals(other$tokenURL)) return false;  Object this$redirectURL = getRedirectURL(), other$redirectURL = other.getRedirectURL(); if ((this$redirectURL == null) ? (other$redirectURL != null) : !this$redirectURL.equals(other$redirectURL)) return false;  Object this$backButtonURL = getBackButtonURL(), other$backButtonURL = other.getBackButtonURL(); return !((this$backButtonURL == null) ? (other$backButtonURL != null) : !this$backButtonURL.equals(other$backButtonURL)); } protected boolean canEqual(Object other) { return other instanceof OAuthApplication; } public int hashCode() { int PRIME = 59; result = 1; Object $clientId = getClientId(); result = result * 59 + (($clientId == null) ? 43 : $clientId.hashCode()); Object $scope = getScope(); result = result * 59 + (($scope == null) ? 43 : $scope.hashCode()); result = result * 59 + (isUseWeirdXboxTokenPrefix() ? 79 : 97); Object $basicURL = getBasicURL(); result = result * 59 + (($basicURL == null) ? 43 : $basicURL.hashCode()); Object $tokenURL = getTokenURL(); result = result * 59 + (($tokenURL == null) ? 43 : $tokenURL.hashCode()); Object $redirectURL = getRedirectURL(); result = result * 59 + (($redirectURL == null) ? 43 : $redirectURL.hashCode()); Object $backButtonURL = getBackButtonURL(); return result * 59 + (($backButtonURL == null) ? 43 : $backButtonURL.hashCode()); } public String toString() { return "OAuthApplication(clientId=" + getClientId() + ", scope=" + getScope() + ", useWeirdXboxTokenPrefix=" + isUseWeirdXboxTokenPrefix() + ", basicURL=" + getBasicURL() + ", tokenURL=" + getTokenURL() + ", redirectURL=" + getRedirectURL() + ", backButtonURL=" + getBackButtonURL() + ")"; } public OAuthApplication(String clientId, String scope, String basicURL, String tokenURL, String redirectURL, String backButtonURL) {
/*  8 */     this.clientId = clientId; this.scope = scope; this.basicURL = basicURL; this.tokenURL = tokenURL; this.redirectURL = redirectURL; this.backButtonURL = backButtonURL;
/*    */   }
/*    */   
/* 11 */   public static OAuthApplication TLAUNCHER_PARAMETERS = new OAuthApplication("2b8231e2-3b7e-4252-90d9-0732cf38e529", "Xboxlive.signin offline_access", "https://login.live.com/oauth20_authorize.srf", "https://login.live.com/oauth20_token.srf", "https://login.microsoftonline.com/common/oauth2/nativeclient", "https://login.microsoftonline.com/common/oauth2/nativeclient?error=access_denied");
/*    */   
/*    */   private final String clientId;
/*    */   
/*    */   private final String scope;
/*    */   
/*    */   public String getClientId() {
/* 18 */     return this.clientId;
/* 19 */   } public String getScope() { return this.scope; }
/* 20 */   private final boolean useWeirdXboxTokenPrefix = true; private String basicURL; public boolean isUseWeirdXboxTokenPrefix() { getClass(); return true; } private String tokenURL; private String redirectURL; private String backButtonURL; public String getBasicURL() {
/* 21 */     return this.basicURL; }
/* 22 */   public String getTokenURL() { return this.tokenURL; }
/* 23 */   public String getRedirectURL() { return this.redirectURL; } public String getBackButtonURL() {
/* 24 */     return this.backButtonURL;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraf\\user\oauth\OAuthApplication.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */