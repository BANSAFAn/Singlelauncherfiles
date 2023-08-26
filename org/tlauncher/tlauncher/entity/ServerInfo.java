/*    */ package org.tlauncher.tlauncher.entity;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ public class ServerInfo
/*    */ {
/*    */   protected String serverId;
/*    */   protected String address;
/*    */   protected String minVersion;
/*    */   
/*    */   public void setServerId(String serverId) {
/* 14 */     this.serverId = serverId; } public void setAddress(String address) { this.address = address; } public void setMinVersion(String minVersion) { this.minVersion = minVersion; } public void setIgnoreVersions(List<String> ignoreVersions) { this.ignoreVersions = ignoreVersions; } public void setIncludeVersions(List<String> includeVersions) { this.includeVersions = includeVersions; } public void setRedirectAddress(String redirectAddress) { this.redirectAddress = redirectAddress; } public void setMojangAccount(boolean mojangAccount) { this.mojangAccount = mojangAccount; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof ServerInfo)) return false;  ServerInfo other = (ServerInfo)o; if (!other.canEqual(this)) return false;  Object this$serverId = getServerId(), other$serverId = other.getServerId(); if ((this$serverId == null) ? (other$serverId != null) : !this$serverId.equals(other$serverId)) return false;  Object this$address = getAddress(), other$address = other.getAddress(); if ((this$address == null) ? (other$address != null) : !this$address.equals(other$address)) return false;  Object this$minVersion = getMinVersion(), other$minVersion = other.getMinVersion(); if ((this$minVersion == null) ? (other$minVersion != null) : !this$minVersion.equals(other$minVersion)) return false;  Object<String> this$ignoreVersions = (Object<String>)getIgnoreVersions(), other$ignoreVersions = (Object<String>)other.getIgnoreVersions(); if ((this$ignoreVersions == null) ? (other$ignoreVersions != null) : !this$ignoreVersions.equals(other$ignoreVersions)) return false;  Object<String> this$includeVersions = (Object<String>)getIncludeVersions(), other$includeVersions = (Object<String>)other.getIncludeVersions(); if ((this$includeVersions == null) ? (other$includeVersions != null) : !this$includeVersions.equals(other$includeVersions)) return false;  Object this$redirectAddress = getRedirectAddress(), other$redirectAddress = other.getRedirectAddress(); return ((this$redirectAddress == null) ? (other$redirectAddress != null) : !this$redirectAddress.equals(other$redirectAddress)) ? false : (!(isMojangAccount() != other.isMojangAccount())); } protected boolean canEqual(Object other) { return other instanceof ServerInfo; } public int hashCode() { int PRIME = 59; result = 1; Object $serverId = getServerId(); result = result * 59 + (($serverId == null) ? 43 : $serverId.hashCode()); Object $address = getAddress(); result = result * 59 + (($address == null) ? 43 : $address.hashCode()); Object $minVersion = getMinVersion(); result = result * 59 + (($minVersion == null) ? 43 : $minVersion.hashCode()); Object<String> $ignoreVersions = (Object<String>)getIgnoreVersions(); result = result * 59 + (($ignoreVersions == null) ? 43 : $ignoreVersions.hashCode()); Object<String> $includeVersions = (Object<String>)getIncludeVersions(); result = result * 59 + (($includeVersions == null) ? 43 : $includeVersions.hashCode()); Object $redirectAddress = getRedirectAddress(); result = result * 59 + (($redirectAddress == null) ? 43 : $redirectAddress.hashCode()); return result * 59 + (isMojangAccount() ? 79 : 97); } public String toString() {
/* 15 */     return "ServerInfo(serverId=" + getServerId() + ", address=" + getAddress() + ", minVersion=" + getMinVersion() + ", ignoreVersions=" + getIgnoreVersions() + ", includeVersions=" + getIncludeVersions() + ", redirectAddress=" + getRedirectAddress() + ", mojangAccount=" + isMojangAccount() + ")";
/*    */   }
/*    */   public String getServerId() {
/* 18 */     return this.serverId;
/*    */   } public String getAddress() {
/* 20 */     return this.address;
/*    */   } public String getMinVersion() {
/* 22 */     return this.minVersion;
/* 23 */   } protected List<String> ignoreVersions = new ArrayList<>(); public List<String> getIgnoreVersions() { return this.ignoreVersions; }
/* 24 */    private String redirectAddress; protected List<String> includeVersions = new ArrayList<>(); private boolean mojangAccount; public List<String> getIncludeVersions() { return this.includeVersions; }
/* 25 */   public String getRedirectAddress() { return this.redirectAddress; } public boolean isMojangAccount() {
/* 26 */     return this.mojangAccount;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\entity\ServerInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */