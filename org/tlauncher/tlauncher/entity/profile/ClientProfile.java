/*    */ package org.tlauncher.tlauncher.entity.profile;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.UUID;
/*    */ import org.tlauncher.tlauncher.minecraft.auth.Account;
/*    */ import org.tlauncher.tlauncher.minecraft.auth.AuthenticatorDatabase;
/*    */ 
/*    */ public class ClientProfile
/*    */ {
/*    */   public void setClientToken(UUID clientToken) {
/* 14 */     this.clientToken = clientToken; } public void setAccounts(Map<String, Account> accounts) { this.accounts = accounts; } public void setAuthenticationDatabase(AuthenticatorDatabase authenticationDatabase) { this.authenticationDatabase = authenticationDatabase; } public void setSelectedAccountUUID(String selectedAccountUUID) { this.selectedAccountUUID = selectedAccountUUID; } public void setFreeAccountUUID(String freeAccountUUID) { this.freeAccountUUID = freeAccountUUID; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof ClientProfile)) return false;  ClientProfile other = (ClientProfile)o; if (!other.canEqual(this)) return false;  Object this$clientToken = getClientToken(), other$clientToken = other.getClientToken(); if ((this$clientToken == null) ? (other$clientToken != null) : !this$clientToken.equals(other$clientToken)) return false;  Object<String, Account> this$accounts = (Object<String, Account>)getAccounts(), other$accounts = (Object<String, Account>)other.getAccounts(); if ((this$accounts == null) ? (other$accounts != null) : !this$accounts.equals(other$accounts)) return false;  Object this$authenticationDatabase = getAuthenticationDatabase(), other$authenticationDatabase = other.getAuthenticationDatabase(); if ((this$authenticationDatabase == null) ? (other$authenticationDatabase != null) : !this$authenticationDatabase.equals(other$authenticationDatabase)) return false;  Object this$selectedAccountUUID = getSelectedAccountUUID(), other$selectedAccountUUID = other.getSelectedAccountUUID(); if ((this$selectedAccountUUID == null) ? (other$selectedAccountUUID != null) : !this$selectedAccountUUID.equals(other$selectedAccountUUID)) return false;  Object this$freeAccountUUID = getFreeAccountUUID(), other$freeAccountUUID = other.getFreeAccountUUID(); return !((this$freeAccountUUID == null) ? (other$freeAccountUUID != null) : !this$freeAccountUUID.equals(other$freeAccountUUID)); } protected boolean canEqual(Object other) { return other instanceof ClientProfile; } public int hashCode() { int PRIME = 59; result = 1; Object $clientToken = getClientToken(); result = result * 59 + (($clientToken == null) ? 43 : $clientToken.hashCode()); Object<String, Account> $accounts = (Object<String, Account>)getAccounts(); result = result * 59 + (($accounts == null) ? 43 : $accounts.hashCode()); Object $authenticationDatabase = getAuthenticationDatabase(); result = result * 59 + (($authenticationDatabase == null) ? 43 : $authenticationDatabase.hashCode()); Object $selectedAccountUUID = getSelectedAccountUUID(); result = result * 59 + (($selectedAccountUUID == null) ? 43 : $selectedAccountUUID.hashCode()); Object $freeAccountUUID = getFreeAccountUUID(); return result * 59 + (($freeAccountUUID == null) ? 43 : $freeAccountUUID.hashCode()); } public String toString() { return "ClientProfile(clientToken=" + getClientToken() + ", accounts=" + getAccounts() + ", authenticationDatabase=" + getAuthenticationDatabase() + ", selectedAccountUUID=" + getSelectedAccountUUID() + ", freeAccountUUID=" + getFreeAccountUUID() + ")"; }
/*    */   
/* 16 */   private UUID clientToken = UUID.randomUUID(); public UUID getClientToken() { return this.clientToken; }
/* 17 */    private Map<String, Account> accounts = Collections.synchronizedMap(new HashMap<>()); private AuthenticatorDatabase authenticationDatabase; public Map<String, Account> getAccounts() { return this.accounts; }
/*    */    private volatile String selectedAccountUUID; private volatile String freeAccountUUID; public AuthenticatorDatabase getAuthenticationDatabase() {
/* 19 */     return this.authenticationDatabase; } public String getSelectedAccountUUID() {
/* 20 */     return this.selectedAccountUUID;
/*    */   }
/*    */   
/*    */   public String getFreeAccountUUID() {
/* 24 */     return this.freeAccountUUID;
/*    */   }
/*    */   public boolean isSelected() {
/* 27 */     return Objects.nonNull(this.selectedAccountUUID);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\entity\profile\ClientProfile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */