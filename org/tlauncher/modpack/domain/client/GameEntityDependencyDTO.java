/*    */ package org.tlauncher.modpack.domain.client;
/*    */ 
/*    */ import org.tlauncher.modpack.domain.client.share.DependencyType;
/*    */ import org.tlauncher.modpack.domain.client.share.GameType;
/*    */ 
/*    */ public class GameEntityDependencyDTO {
/*    */   private GameType gameType;
/*    */   private Long id;
/*    */   
/* 10 */   public GameType getGameType() { return this.gameType; } private String name; private Long gameEntityId; private DependencyType dependencyType; public Long getId() { return this.id; } public String getName() { return this.name; } public Long getGameEntityId() { return this.gameEntityId; } public DependencyType getDependencyType() { return this.dependencyType; } public void setGameType(GameType gameType) { this.gameType = gameType; } public void setId(Long id) { this.id = id; } public void setName(String name) { this.name = name; } public void setGameEntityId(Long gameEntityId) { this.gameEntityId = gameEntityId; } public void setDependencyType(DependencyType dependencyType) { this.dependencyType = dependencyType; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof GameEntityDependencyDTO)) return false;  GameEntityDependencyDTO other = (GameEntityDependencyDTO)o; if (!other.canEqual(this)) return false;  Object this$id = getId(), other$id = other.getId(); if ((this$id == null) ? (other$id != null) : !this$id.equals(other$id)) return false;  Object this$gameEntityId = getGameEntityId(), other$gameEntityId = other.getGameEntityId(); if ((this$gameEntityId == null) ? (other$gameEntityId != null) : !this$gameEntityId.equals(other$gameEntityId)) return false;  Object this$gameType = getGameType(), other$gameType = other.getGameType(); if ((this$gameType == null) ? (other$gameType != null) : !this$gameType.equals(other$gameType)) return false;  Object this$name = getName(), other$name = other.getName(); if ((this$name == null) ? (other$name != null) : !this$name.equals(other$name)) return false;  Object this$dependencyType = getDependencyType(), other$dependencyType = other.getDependencyType(); return !((this$dependencyType == null) ? (other$dependencyType != null) : !this$dependencyType.equals(other$dependencyType)); } protected boolean canEqual(Object other) { return other instanceof GameEntityDependencyDTO; } public int hashCode() { int PRIME = 59; result = 1; Object $id = getId(); result = result * 59 + (($id == null) ? 43 : $id.hashCode()); Object $gameEntityId = getGameEntityId(); result = result * 59 + (($gameEntityId == null) ? 43 : $gameEntityId.hashCode()); Object $gameType = getGameType(); result = result * 59 + (($gameType == null) ? 43 : $gameType.hashCode()); Object $name = getName(); result = result * 59 + (($name == null) ? 43 : $name.hashCode()); Object $dependencyType = getDependencyType(); return result * 59 + (($dependencyType == null) ? 43 : $dependencyType.hashCode()); } public String toString() { return "GameEntityDependencyDTO(gameType=" + getGameType() + ", id=" + getId() + ", name=" + getName() + ", gameEntityId=" + getGameEntityId() + ", dependencyType=" + getDependencyType() + ")"; }
/*    */    public GameEntityDependencyDTO() {} public GameEntityDependencyDTO(GameType gameType, Long id, String name, Long gameEntityId, DependencyType dependencyType) {
/* 12 */     this.gameType = gameType; this.id = id; this.name = name; this.gameEntityId = gameEntityId; this.dependencyType = dependencyType;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\modpack\domain\client\GameEntityDependencyDTO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */