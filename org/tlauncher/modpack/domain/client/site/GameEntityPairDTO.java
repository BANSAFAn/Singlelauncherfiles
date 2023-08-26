/*   */ package org.tlauncher.modpack.domain.client.site;
/*   */ 
/*   */ import org.tlauncher.modpack.domain.client.version.VersionDTO;
/*   */ 
/*   */ public class GameEntityPairDTO {
/*   */   private Long id;
/*   */   
/* 8 */   public Long getId() { return this.id; } private GameEntityDTO gameEntity; private VersionDTO version; public GameEntityDTO getGameEntity() { return this.gameEntity; } public VersionDTO getVersion() { return this.version; } public void setId(Long id) { this.id = id; } public void setGameEntity(GameEntityDTO gameEntity) { this.gameEntity = gameEntity; } public void setVersion(VersionDTO version) { this.version = version; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof GameEntityPairDTO)) return false;  GameEntityPairDTO other = (GameEntityPairDTO)o; if (!other.canEqual(this)) return false;  Object this$id = getId(), other$id = other.getId(); if ((this$id == null) ? (other$id != null) : !this$id.equals(other$id)) return false;  Object this$gameEntity = getGameEntity(), other$gameEntity = other.getGameEntity(); if ((this$gameEntity == null) ? (other$gameEntity != null) : !this$gameEntity.equals(other$gameEntity)) return false;  Object this$version = getVersion(), other$version = other.getVersion(); return !((this$version == null) ? (other$version != null) : !this$version.equals(other$version)); } protected boolean canEqual(Object other) { return other instanceof GameEntityPairDTO; } public int hashCode() { int PRIME = 59; result = 1; Object $id = getId(); result = result * 59 + (($id == null) ? 43 : $id.hashCode()); Object $gameEntity = getGameEntity(); result = result * 59 + (($gameEntity == null) ? 43 : $gameEntity.hashCode()); Object $version = getVersion(); return result * 59 + (($version == null) ? 43 : $version.hashCode()); } public String toString() { return "GameEntityPairDTO(id=" + getId() + ", gameEntity=" + getGameEntity() + ", version=" + getVersion() + ")"; }
/*   */ 
/*   */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\modpack\domain\client\site\GameEntityPairDTO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */