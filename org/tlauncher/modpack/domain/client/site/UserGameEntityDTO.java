/*   */ package org.tlauncher.modpack.domain.client.site;
/*   */ 
/*   */ public class UserGameEntityDTO {
/*   */   private Long id;
/*   */   private String uuid;
/*   */   
/* 7 */   public Long getId() { return this.id; } private GameType gameType; private GameEntityDTO gameEntity; public String getUuid() { return this.uuid; } public GameType getGameType() { return this.gameType; } public GameEntityDTO getGameEntity() { return this.gameEntity; } public void setId(Long id) { this.id = id; } public void setUuid(String uuid) { this.uuid = uuid; } public void setGameType(GameType gameType) { this.gameType = gameType; } public void setGameEntity(GameEntityDTO gameEntity) { this.gameEntity = gameEntity; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof UserGameEntityDTO)) return false;  UserGameEntityDTO other = (UserGameEntityDTO)o; if (!other.canEqual(this)) return false;  Object this$id = getId(), other$id = other.getId(); if ((this$id == null) ? (other$id != null) : !this$id.equals(other$id)) return false;  Object this$uuid = getUuid(), other$uuid = other.getUuid(); if ((this$uuid == null) ? (other$uuid != null) : !this$uuid.equals(other$uuid)) return false;  Object this$gameType = getGameType(), other$gameType = other.getGameType(); if ((this$gameType == null) ? (other$gameType != null) : !this$gameType.equals(other$gameType)) return false;  Object this$gameEntity = getGameEntity(), other$gameEntity = other.getGameEntity(); return !((this$gameEntity == null) ? (other$gameEntity != null) : !this$gameEntity.equals(other$gameEntity)); } protected boolean canEqual(Object other) { return other instanceof UserGameEntityDTO; } public int hashCode() { int PRIME = 59; result = 1; Object $id = getId(); result = result * 59 + (($id == null) ? 43 : $id.hashCode()); Object $uuid = getUuid(); result = result * 59 + (($uuid == null) ? 43 : $uuid.hashCode()); Object $gameType = getGameType(); result = result * 59 + (($gameType == null) ? 43 : $gameType.hashCode()); Object $gameEntity = getGameEntity(); return result * 59 + (($gameEntity == null) ? 43 : $gameEntity.hashCode()); } public String toString() { return "UserGameEntityDTO(id=" + getId() + ", uuid=" + getUuid() + ", gameType=" + getGameType() + ", gameEntity=" + getGameEntity() + ")"; }
/*   */ 
/*   */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\modpack\domain\client\site\UserGameEntityDTO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */