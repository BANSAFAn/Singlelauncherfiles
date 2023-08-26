/*    */ package org.tlauncher.tlauncher.managers;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.tlauncher.tlauncher.entity.server.RemoteServer;
/*    */ import org.tlauncher.tlauncher.entity.server.ReplacedAddressServer;
/*    */ 
/*    */ public class ServerList {
/*    */   public void setNewServers(List<RemoteServer> newServers) {
/* 10 */     this.newServers = newServers; } public void setRemovedServers(List<String> removedServers) { this.removedServers = removedServers; } public void setClientChangedAddress(List<ReplacedAddressServer> clientChangedAddress) { this.clientChangedAddress = clientChangedAddress; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof ServerList)) return false;  ServerList other = (ServerList)o; if (!other.canEqual(this)) return false;  Object<RemoteServer> this$newServers = (Object<RemoteServer>)getNewServers(), other$newServers = (Object<RemoteServer>)other.getNewServers(); if ((this$newServers == null) ? (other$newServers != null) : !this$newServers.equals(other$newServers)) return false;  Object<String> this$removedServers = (Object<String>)getRemovedServers(), other$removedServers = (Object<String>)other.getRemovedServers(); if ((this$removedServers == null) ? (other$removedServers != null) : !this$removedServers.equals(other$removedServers)) return false;  Object<ReplacedAddressServer> this$clientChangedAddress = (Object<ReplacedAddressServer>)getClientChangedAddress(), other$clientChangedAddress = (Object<ReplacedAddressServer>)other.getClientChangedAddress(); return !((this$clientChangedAddress == null) ? (other$clientChangedAddress != null) : !this$clientChangedAddress.equals(other$clientChangedAddress)); } protected boolean canEqual(Object other) { return other instanceof ServerList; } public int hashCode() { int PRIME = 59; result = 1; Object<RemoteServer> $newServers = (Object<RemoteServer>)getNewServers(); result = result * 59 + (($newServers == null) ? 43 : $newServers.hashCode()); Object<String> $removedServers = (Object<String>)getRemovedServers(); result = result * 59 + (($removedServers == null) ? 43 : $removedServers.hashCode()); Object<ReplacedAddressServer> $clientChangedAddress = (Object<ReplacedAddressServer>)getClientChangedAddress(); return result * 59 + (($clientChangedAddress == null) ? 43 : $clientChangedAddress.hashCode()); } public String toString() { return "ServerList(newServers=" + getNewServers() + ", removedServers=" + getRemovedServers() + ", clientChangedAddress=" + getClientChangedAddress() + ")"; }
/*    */   
/* 12 */   public List<RemoteServer> getNewServers() { return this.newServers; }
/* 13 */   public List<String> getRemovedServers() { return this.removedServers; } public List<ReplacedAddressServer> getClientChangedAddress() {
/* 14 */     return this.clientChangedAddress;
/*    */   }
/* 16 */   private List<RemoteServer> newServers = new ArrayList<>();
/* 17 */   private List<String> removedServers = new ArrayList<>();
/* 18 */   private List<ReplacedAddressServer> clientChangedAddress = new ArrayList<>();
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\managers\ServerList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */