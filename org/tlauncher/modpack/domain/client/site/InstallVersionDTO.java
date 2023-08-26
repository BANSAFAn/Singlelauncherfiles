/*   */ package org.tlauncher.modpack.domain.client.site;
/*   */ 
/*   */ public class InstallVersionDTO {
/*   */   private GameType type;
/*   */   
/* 6 */   public GameType getType() { return this.type; } private Long gameEntityId; private Long versionId; public Long getGameEntityId() { return this.gameEntityId; } public Long getVersionId() { return this.versionId; } public void setType(GameType type) { this.type = type; } public void setGameEntityId(Long gameEntityId) { this.gameEntityId = gameEntityId; } public void setVersionId(Long versionId) { this.versionId = versionId; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof InstallVersionDTO)) return false;  InstallVersionDTO other = (InstallVersionDTO)o; if (!other.canEqual(this)) return false;  Object this$gameEntityId = getGameEntityId(), other$gameEntityId = other.getGameEntityId(); if ((this$gameEntityId == null) ? (other$gameEntityId != null) : !this$gameEntityId.equals(other$gameEntityId)) return false;  Object this$versionId = getVersionId(), other$versionId = other.getVersionId(); if ((this$versionId == null) ? (other$versionId != null) : !this$versionId.equals(other$versionId)) return false;  Object this$type = getType(), other$type = other.getType(); return !((this$type == null) ? (other$type != null) : !this$type.equals(other$type)); } protected boolean canEqual(Object other) { return other instanceof InstallVersionDTO; } public int hashCode() { int PRIME = 59; result = 1; Object $gameEntityId = getGameEntityId(); result = result * 59 + (($gameEntityId == null) ? 43 : $gameEntityId.hashCode()); Object $versionId = getVersionId(); result = result * 59 + (($versionId == null) ? 43 : $versionId.hashCode()); Object $type = getType(); return result * 59 + (($type == null) ? 43 : $type.hashCode()); } public String toString() { return "InstallVersionDTO(type=" + getType() + ", gameEntityId=" + getGameEntityId() + ", versionId=" + getVersionId() + ")"; }
/*   */ 
/*   */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\modpack\domain\client\site\InstallVersionDTO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */