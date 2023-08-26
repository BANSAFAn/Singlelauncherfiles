/*   */ package org.tlauncher.tlauncher.entity.server;
/*   */ public class ReplacedAddressServer { private String oldAddress;
/*   */   private String newAddress;
/*   */   
/* 5 */   public void setOldAddress(String oldAddress) { this.oldAddress = oldAddress; } public void setNewAddress(String newAddress) { this.newAddress = newAddress; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof ReplacedAddressServer)) return false;  ReplacedAddressServer other = (ReplacedAddressServer)o; if (!other.canEqual(this)) return false;  Object this$oldAddress = getOldAddress(), other$oldAddress = other.getOldAddress(); if ((this$oldAddress == null) ? (other$oldAddress != null) : !this$oldAddress.equals(other$oldAddress)) return false;  Object this$newAddress = getNewAddress(), other$newAddress = other.getNewAddress(); return !((this$newAddress == null) ? (other$newAddress != null) : !this$newAddress.equals(other$newAddress)); } protected boolean canEqual(Object other) { return other instanceof ReplacedAddressServer; } public int hashCode() { int PRIME = 59; result = 1; Object $oldAddress = getOldAddress(); result = result * 59 + (($oldAddress == null) ? 43 : $oldAddress.hashCode()); Object $newAddress = getNewAddress(); return result * 59 + (($newAddress == null) ? 43 : $newAddress.hashCode()); } public String toString() { return "ReplacedAddressServer(oldAddress=" + getOldAddress() + ", newAddress=" + getNewAddress() + ")"; }
/*   */   
/* 7 */   public String getOldAddress() { return this.oldAddress; } public String getNewAddress() {
/* 8 */     return this.newAddress;
/*   */   } }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\entity\server\ReplacedAddressServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */