/*    */ package org.tlauncher.tlauncher.entity.server;
/*    */ 
/*    */ import java.util.Date;
/*    */ 
/*    */ public class RemoteServer extends Server {
/*    */   private List<Long> removedTime;
/*    */   
/*  8 */   public void setRemovedTime(List<Long> removedTime) { this.removedTime = removedTime; } public void setRecoveryServerTime(Integer recoveryServerTime) { this.recoveryServerTime = recoveryServerTime; } public void setMaxRemovingCountServer(Integer maxRemovingCountServer) { this.maxRemovingCountServer = maxRemovingCountServer; } public void setAddedDate(Date addedDate) { this.addedDate = addedDate; } public void setState(ServerState state) { this.state = state; } public void setLocales(Set<String> locales) { this.locales = locales; } public void setOfficialAccount(boolean officialAccount) { this.officialAccount = officialAccount; } public void setRemote(boolean remote) { this.remote = remote; } public String toString() { return "RemoteServer(removedTime=" + getRemovedTime() + ", recoveryServerTime=" + getRecoveryServerTime() + ", maxRemovingCountServer=" + getMaxRemovingCountServer() + ", addedDate=" + getAddedDate() + ", state=" + getState() + ", locales=" + getLocales() + ", officialAccount=" + isOfficialAccount() + ", remote=" + isRemote() + ")"; }
/*    */   
/*    */   public List<Long> getRemovedTime() {
/* 11 */     return this.removedTime;
/* 12 */   } private Integer recoveryServerTime = Integer.valueOf(0); public Integer getRecoveryServerTime() { return this.recoveryServerTime; }
/* 13 */    private Date addedDate; private ServerState state; private Set<String> locales; private Integer maxRemovingCountServer = Integer.valueOf(0); @SerializedName(value = "mojangAccount", alternate = {"officialAccount"}) private boolean officialAccount; private boolean remote; public Integer getMaxRemovingCountServer() { return this.maxRemovingCountServer; }
/* 14 */   public Date getAddedDate() { return this.addedDate; }
/* 15 */   public ServerState getState() { return this.state; } public Set<String> getLocales() {
/* 16 */     return this.locales;
/*    */   } public boolean isOfficialAccount() {
/* 18 */     return this.officialAccount;
/*    */   } public boolean isRemote() {
/* 20 */     return this.remote;
/*    */   }
/*    */   public RemoteServer() {
/* 23 */     this.removedTime = new ArrayList<>();
/* 24 */     this.locales = new HashSet<>();
/*    */   }
/*    */ 
/*    */   
/*    */   public void initRemote() {
/* 29 */     setRemote(true);
/* 30 */     this.addedDate = new Date();
/* 31 */     setState(ServerState.ACTIVE);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 36 */     return super.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 41 */     return super.equals(o);
/*    */   }
/*    */   
/*    */   public enum ServerState
/*    */   {
/* 46 */     DEACTIVATED,
/*    */     
/* 48 */     ACTIVE;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\entity\server\RemoteServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */