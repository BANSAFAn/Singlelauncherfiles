/*    */ package org.tlauncher.modpack.domain.client;
/*    */ 
/*    */ public class GameVersionDTO {
/*    */   private Long id;
/*    */   private String name;
/*    */   
/*  7 */   public String toString() { return "GameVersionDTO(id=" + getId() + ", name=" + getName() + ", forgeVersions=" + getForgeVersions() + ", forgeVersion=" + getForgeVersion() + ")"; } private List<String> forgeVersions; private ForgeVersionDTO forgeVersion; public int hashCode() { int PRIME = 59; result = 1; Object $id = getId(); result = result * 59 + (($id == null) ? 43 : $id.hashCode()); Object $name = getName(); result = result * 59 + (($name == null) ? 43 : $name.hashCode()); Object<String> $forgeVersions = (Object<String>)getForgeVersions(); result = result * 59 + (($forgeVersions == null) ? 43 : $forgeVersions.hashCode()); Object $forgeVersion = getForgeVersion(); return result * 59 + (($forgeVersion == null) ? 43 : $forgeVersion.hashCode()); } protected boolean canEqual(Object other) { return other instanceof GameVersionDTO; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof GameVersionDTO)) return false;  GameVersionDTO other = (GameVersionDTO)o; if (!other.canEqual(this)) return false;  Object this$id = getId(), other$id = other.getId(); if ((this$id == null) ? (other$id != null) : !this$id.equals(other$id)) return false;  Object this$name = getName(), other$name = other.getName(); if ((this$name == null) ? (other$name != null) : !this$name.equals(other$name)) return false;  Object<String> this$forgeVersions = (Object<String>)getForgeVersions(), other$forgeVersions = (Object<String>)other.getForgeVersions(); if ((this$forgeVersions == null) ? (other$forgeVersions != null) : !this$forgeVersions.equals(other$forgeVersions)) return false;  Object this$forgeVersion = getForgeVersion(), other$forgeVersion = other.getForgeVersion(); return !((this$forgeVersion == null) ? (other$forgeVersion != null) : !this$forgeVersion.equals(other$forgeVersion)); } public void setForgeVersion(ForgeVersionDTO forgeVersion) { this.forgeVersion = forgeVersion; } public void setForgeVersions(List<String> forgeVersions) { this.forgeVersions = forgeVersions; } public void setName(String name) { this.name = name; } public void setId(Long id) { this.id = id; } public ForgeVersionDTO getForgeVersion() { return this.forgeVersion; } public List<String> getForgeVersions() { return this.forgeVersions; } public String getName() { return this.name; } public Long getId() { return this.id; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public GameVersionDTO copyWithoutForges() {
/* 15 */     GameVersionDTO g = new GameVersionDTO();
/* 16 */     g.setId(this.id);
/* 17 */     g.setName(this.name);
/* 18 */     return g;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\modpack\domain\client\GameVersionDTO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */