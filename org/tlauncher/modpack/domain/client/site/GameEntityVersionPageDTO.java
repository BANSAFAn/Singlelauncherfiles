/*    */ package org.tlauncher.modpack.domain.client.site;
/*    */ 
/*    */ import org.tlauncher.modpack.domain.client.version.VersionDTO;
/*    */ 
/*    */ public class GameEntityVersionPageDTO {
/*    */   private GameEntityDTO gameEntity;
/*    */   private CommonPage<VersionDTO> versionPages;
/*    */   
/*  9 */   public GameEntityDTO getGameEntity() { return this.gameEntity; } public CommonPage<VersionDTO> getVersionPages() { return this.versionPages; } public void setGameEntity(GameEntityDTO gameEntity) { this.gameEntity = gameEntity; } public void setVersionPages(CommonPage<VersionDTO> versionPages) { this.versionPages = versionPages; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof GameEntityVersionPageDTO)) return false;  GameEntityVersionPageDTO other = (GameEntityVersionPageDTO)o; if (!other.canEqual(this)) return false;  Object this$gameEntity = getGameEntity(), other$gameEntity = other.getGameEntity(); if ((this$gameEntity == null) ? (other$gameEntity != null) : !this$gameEntity.equals(other$gameEntity)) return false;  Object<VersionDTO> this$versionPages = (Object<VersionDTO>)getVersionPages(), other$versionPages = (Object<VersionDTO>)other.getVersionPages(); return !((this$versionPages == null) ? (other$versionPages != null) : !this$versionPages.equals(other$versionPages)); } protected boolean canEqual(Object other) { return other instanceof GameEntityVersionPageDTO; } public int hashCode() { int PRIME = 59; result = 1; Object $gameEntity = getGameEntity(); result = result * 59 + (($gameEntity == null) ? 43 : $gameEntity.hashCode()); Object<VersionDTO> $versionPages = (Object<VersionDTO>)getVersionPages(); return result * 59 + (($versionPages == null) ? 43 : $versionPages.hashCode()); } public String toString() { return "GameEntityVersionPageDTO(gameEntity=" + getGameEntity() + ", versionPages=" + getVersionPages() + ")"; } public GameEntityVersionPageDTO(GameEntityDTO gameEntity, CommonPage<VersionDTO> versionPages) {
/* 10 */     this.gameEntity = gameEntity; this.versionPages = versionPages;
/*    */   }
/*    */   
/*    */   public GameEntityVersionPageDTO() {}
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\modpack\domain\client\site\GameEntityVersionPageDTO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */