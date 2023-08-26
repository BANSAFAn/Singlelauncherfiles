/*    */ package org.tlauncher.modpack.domain.client.share;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.tlauncher.modpack.domain.client.GameVersionDTO;
/*    */ import org.tlauncher.modpack.domain.client.ModpackDTO;
/*    */ import org.tlauncher.modpack.domain.client.ResourcePackDTO;
/*    */ 
/*    */ public class InfoMod {
/*  9 */   public List<GameVersionDTO> getGameVersions() { return this.gameVersions; } public List<ModpackDTO> getModPacks() { return this.modPacks; } public List<ModDTO> getMods() { return this.mods; } public List<ResourcePackDTO> getResourcePacks() { return this.resourcePacks; } public List<MapDTO> getMaps() { return this.maps; } public List<ShaderpackDTO> getShaderpacks() { return this.shaderpacks; } public void setGameVersions(List<GameVersionDTO> gameVersions) { this.gameVersions = gameVersions; } public void setModPacks(List<ModpackDTO> modPacks) { this.modPacks = modPacks; } public void setMods(List<ModDTO> mods) { this.mods = mods; } public void setResourcePacks(List<ResourcePackDTO> resourcePacks) { this.resourcePacks = resourcePacks; } public void setMaps(List<MapDTO> maps) { this.maps = maps; } public void setShaderpacks(List<ShaderpackDTO> shaderpacks) { this.shaderpacks = shaderpacks; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof InfoMod)) return false;  InfoMod other = (InfoMod)o; if (!other.canEqual(this)) return false;  Object<GameVersionDTO> this$gameVersions = (Object<GameVersionDTO>)getGameVersions(), other$gameVersions = (Object<GameVersionDTO>)other.getGameVersions(); if ((this$gameVersions == null) ? (other$gameVersions != null) : !this$gameVersions.equals(other$gameVersions)) return false;  Object<ModpackDTO> this$modPacks = (Object<ModpackDTO>)getModPacks(), other$modPacks = (Object<ModpackDTO>)other.getModPacks(); if ((this$modPacks == null) ? (other$modPacks != null) : !this$modPacks.equals(other$modPacks)) return false;  Object<ModDTO> this$mods = (Object<ModDTO>)getMods(), other$mods = (Object<ModDTO>)other.getMods(); if ((this$mods == null) ? (other$mods != null) : !this$mods.equals(other$mods)) return false;  Object<ResourcePackDTO> this$resourcePacks = (Object<ResourcePackDTO>)getResourcePacks(), other$resourcePacks = (Object<ResourcePackDTO>)other.getResourcePacks(); if ((this$resourcePacks == null) ? (other$resourcePacks != null) : !this$resourcePacks.equals(other$resourcePacks)) return false;  Object<MapDTO> this$maps = (Object<MapDTO>)getMaps(), other$maps = (Object<MapDTO>)other.getMaps(); if ((this$maps == null) ? (other$maps != null) : !this$maps.equals(other$maps)) return false;  Object<ShaderpackDTO> this$shaderpacks = (Object<ShaderpackDTO>)getShaderpacks(), other$shaderpacks = (Object<ShaderpackDTO>)other.getShaderpacks(); return !((this$shaderpacks == null) ? (other$shaderpacks != null) : !this$shaderpacks.equals(other$shaderpacks)); } protected boolean canEqual(Object other) { return other instanceof InfoMod; } public int hashCode() { int PRIME = 59; result = 1; Object<GameVersionDTO> $gameVersions = (Object<GameVersionDTO>)getGameVersions(); result = result * 59 + (($gameVersions == null) ? 43 : $gameVersions.hashCode()); Object<ModpackDTO> $modPacks = (Object<ModpackDTO>)getModPacks(); result = result * 59 + (($modPacks == null) ? 43 : $modPacks.hashCode()); Object<ModDTO> $mods = (Object<ModDTO>)getMods(); result = result * 59 + (($mods == null) ? 43 : $mods.hashCode()); Object<ResourcePackDTO> $resourcePacks = (Object<ResourcePackDTO>)getResourcePacks(); result = result * 59 + (($resourcePacks == null) ? 43 : $resourcePacks.hashCode()); Object<MapDTO> $maps = (Object<MapDTO>)getMaps(); result = result * 59 + (($maps == null) ? 43 : $maps.hashCode()); Object<ShaderpackDTO> $shaderpacks = (Object<ShaderpackDTO>)getShaderpacks(); return result * 59 + (($shaderpacks == null) ? 43 : $shaderpacks.hashCode()); } public String toString() { return "InfoMod(gameVersions=" + getGameVersions() + ", modPacks=" + getModPacks() + ", mods=" + getMods() + ", resourcePacks=" + getResourcePacks() + ", maps=" + getMaps() + ", shaderpacks=" + getShaderpacks() + ")"; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   List<GameVersionDTO> gameVersions = new ArrayList<>();
/* 20 */   List<ModpackDTO> modPacks = new ArrayList<>();
/* 21 */   List<ModDTO> mods = new ArrayList<>();
/* 22 */   List<ResourcePackDTO> resourcePacks = new ArrayList<>();
/* 23 */   List<MapDTO> maps = new ArrayList<>();
/* 24 */   List<ShaderpackDTO> shaderpacks = new ArrayList<>();
/*    */ 
/*    */   
/*    */   public List<? extends GameEntityDTO> getByType(GameType type) {
/* 28 */     switch (type) {
/*    */       case MOD:
/* 30 */         return (List)this.mods;
/*    */       case null:
/* 32 */         return (List)this.maps;
/*    */       case RESOURCEPACK:
/* 34 */         return (List)this.resourcePacks;
/*    */       case MODPACK:
/* 36 */         return (List)this.modPacks;
/*    */       case SHADERPACK:
/* 38 */         return (List)this.shaderpacks;
/*    */     } 
/* 40 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\modpack\domain\client\share\InfoMod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */