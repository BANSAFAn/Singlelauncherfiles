/*    */ package org.tlauncher.modpack.domain.client.version;
/*    */ 
/*    */ 
/*    */ public class ModVersionDTO extends VersionDTO {
/*    */   private String downForge;
/*    */   private String upForge;
/*    */   private List<String> incompatibleMods;
/*    */   
/*  9 */   public String getDownForge() { return this.downForge; } public String getUpForge() { return this.upForge; } public List<String> getIncompatibleMods() { return this.incompatibleMods; } public void setDownForge(String downForge) { this.downForge = downForge; } public void setUpForge(String upForge) { this.upForge = upForge; } public void setIncompatibleMods(List<String> incompatibleMods) { this.incompatibleMods = incompatibleMods; }
/* 10 */   public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof ModVersionDTO)) return false;  ModVersionDTO other = (ModVersionDTO)o; if (!other.canEqual(this)) return false;  if (!super.equals(o)) return false;  Object this$downForge = getDownForge(), other$downForge = other.getDownForge(); if ((this$downForge == null) ? (other$downForge != null) : !this$downForge.equals(other$downForge)) return false;  Object this$upForge = getUpForge(), other$upForge = other.getUpForge(); if ((this$upForge == null) ? (other$upForge != null) : !this$upForge.equals(other$upForge)) return false;  Object<String> this$incompatibleMods = (Object<String>)getIncompatibleMods(), other$incompatibleMods = (Object<String>)other.getIncompatibleMods(); return !((this$incompatibleMods == null) ? (other$incompatibleMods != null) : !this$incompatibleMods.equals(other$incompatibleMods)); } protected boolean canEqual(Object other) { return other instanceof ModVersionDTO; } public int hashCode() { int PRIME = 59; result = super.hashCode(); Object $downForge = getDownForge(); result = result * 59 + (($downForge == null) ? 43 : $downForge.hashCode()); Object $upForge = getUpForge(); result = result * 59 + (($upForge == null) ? 43 : $upForge.hashCode()); Object<String> $incompatibleMods = (Object<String>)getIncompatibleMods(); return result * 59 + (($incompatibleMods == null) ? 43 : $incompatibleMods.hashCode()); } public String toString() {
/* 11 */     return "ModVersionDTO(super=" + super.toString() + ", downForge=" + getDownForge() + ", upForge=" + getUpForge() + ", incompatibleMods=" + getIncompatibleMods() + ")";
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\modpack\domain\client\version\ModVersionDTO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */