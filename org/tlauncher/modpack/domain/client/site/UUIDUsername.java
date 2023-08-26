/*    */ package org.tlauncher.modpack.domain.client.site;
/*    */ 
/*    */ public class UUIDUsername {
/*    */   private String uuid;
/*    */   private String username;
/*    */   private String email;
/*    */   private AuthorityName authority;
/*    */   
/*  9 */   public String getUuid() { return this.uuid; } public String getUsername() { return this.username; } public String getEmail() { return this.email; } public AuthorityName getAuthority() { return this.authority; } public void setUuid(String uuid) { this.uuid = uuid; } public void setUsername(String username) { this.username = username; } public void setEmail(String email) { this.email = email; } public void setAuthority(AuthorityName authority) { this.authority = authority; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof UUIDUsername)) return false;  UUIDUsername other = (UUIDUsername)o; if (!other.canEqual(this)) return false;  Object this$uuid = getUuid(), other$uuid = other.getUuid(); if ((this$uuid == null) ? (other$uuid != null) : !this$uuid.equals(other$uuid)) return false;  Object this$username = getUsername(), other$username = other.getUsername(); if ((this$username == null) ? (other$username != null) : !this$username.equals(other$username)) return false;  Object this$email = getEmail(), other$email = other.getEmail(); if ((this$email == null) ? (other$email != null) : !this$email.equals(other$email)) return false;  Object this$authority = getAuthority(), other$authority = other.getAuthority(); return !((this$authority == null) ? (other$authority != null) : !this$authority.equals(other$authority)); } protected boolean canEqual(Object other) { return other instanceof UUIDUsername; } public int hashCode() { int PRIME = 59; result = 1; Object $uuid = getUuid(); result = result * 59 + (($uuid == null) ? 43 : $uuid.hashCode()); Object $username = getUsername(); result = result * 59 + (($username == null) ? 43 : $username.hashCode()); Object $email = getEmail(); result = result * 59 + (($email == null) ? 43 : $email.hashCode()); Object $authority = getAuthority(); return result * 59 + (($authority == null) ? 43 : $authority.hashCode()); } public String toString() { return "UUIDUsername(uuid=" + getUuid() + ", username=" + getUsername() + ", email=" + getEmail() + ", authority=" + getAuthority() + ")"; } public UUIDUsername(String uuid, String username, String email, AuthorityName authority) {
/* 10 */     this.uuid = uuid; this.username = username; this.email = email; this.authority = authority;
/*    */   }
/*    */   
/*    */   public UUIDUsername() {}
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\modpack\domain\client\site\UUIDUsername.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */