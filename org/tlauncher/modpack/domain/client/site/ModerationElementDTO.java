/*    */ package org.tlauncher.modpack.domain.client.site;
/*    */ 
/*    */ import org.tlauncher.modpack.domain.client.version.VersionDTO;
/*    */ 
/*    */ public class ModerationElementDTO {
/*    */   private GameEntityDTO gameEntity;
/*    */   private CommonPage<VersionDTO> versions;
/*    */   private List<String> changedFields;
/*    */   
/* 10 */   public GameEntityDTO getGameEntity() { return this.gameEntity; } public CommonPage<VersionDTO> getVersions() { return this.versions; } public List<String> getChangedFields() { return this.changedFields; } public void setGameEntity(GameEntityDTO gameEntity) { this.gameEntity = gameEntity; } public void setVersions(CommonPage<VersionDTO> versions) { this.versions = versions; } public void setChangedFields(List<String> changedFields) { this.changedFields = changedFields; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof ModerationElementDTO)) return false;  ModerationElementDTO other = (ModerationElementDTO)o; if (!other.canEqual(this)) return false;  Object this$gameEntity = getGameEntity(), other$gameEntity = other.getGameEntity(); if ((this$gameEntity == null) ? (other$gameEntity != null) : !this$gameEntity.equals(other$gameEntity)) return false;  Object<VersionDTO> this$versions = (Object<VersionDTO>)getVersions(), other$versions = (Object<VersionDTO>)other.getVersions(); if ((this$versions == null) ? (other$versions != null) : !this$versions.equals(other$versions)) return false;  Object<String> this$changedFields = (Object<String>)getChangedFields(), other$changedFields = (Object<String>)other.getChangedFields(); return !((this$changedFields == null) ? (other$changedFields != null) : !this$changedFields.equals(other$changedFields)); } protected boolean canEqual(Object other) { return other instanceof ModerationElementDTO; } public int hashCode() { int PRIME = 59; result = 1; Object $gameEntity = getGameEntity(); result = result * 59 + (($gameEntity == null) ? 43 : $gameEntity.hashCode()); Object<VersionDTO> $versions = (Object<VersionDTO>)getVersions(); result = result * 59 + (($versions == null) ? 43 : $versions.hashCode()); Object<String> $changedFields = (Object<String>)getChangedFields(); return result * 59 + (($changedFields == null) ? 43 : $changedFields.hashCode()); } public String toString() { return "ModerationElementDTO(gameEntity=" + getGameEntity() + ", versions=" + getVersions() + ", changedFields=" + getChangedFields() + ")"; }
/*    */ 
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\modpack\domain\client\site\ModerationElementDTO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */