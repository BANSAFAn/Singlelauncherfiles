/*     */ package org.tlauncher.modpack.domain.client.version;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.tlauncher.modpack.domain.client.ForgeVersionDTO;
/*     */ import org.tlauncher.modpack.domain.client.GameEntityDTO;
/*     */ import org.tlauncher.modpack.domain.client.GameVersionDTO;
/*     */ import org.tlauncher.modpack.domain.client.MapDTO;
/*     */ import org.tlauncher.modpack.domain.client.ModDTO;
/*     */ import org.tlauncher.modpack.domain.client.ModpackDTO;
/*     */ import org.tlauncher.modpack.domain.client.ResourcePackDTO;
/*     */ import org.tlauncher.modpack.domain.client.ShaderpackDTO;
/*     */ import org.tlauncher.modpack.domain.client.share.GameType;
/*     */ import org.tlauncher.modpack.domain.client.share.NameIdDTO;
/*     */ 
/*     */ 
/*     */ public class ModpackVersionDTO
/*     */   extends VersionDTO
/*     */ {
/*     */   public List<ModDTO> getMods() {
/*  21 */     return this.mods; } public List<ResourcePackDTO> getResourcePacks() { return this.resourcePacks; } public List<MapDTO> getMaps() { return this.maps; } public List<ShaderpackDTO> getShaderpacks() { return this.shaderpacks; } public MetadataDTO getAdditionalFile() { return this.additionalFile; } public String getForgeVersion() { return this.forgeVersion; } public String getGameVersion() { return this.gameVersion; } public ForgeVersionDTO getForgeVersionDTO() { return this.forgeVersionDTO; } public GameVersionDTO getGameVersionDTO() { return this.gameVersionDTO; } public NameIdDTO getMinecraftVersionName() { return this.minecraftVersionName; } public void setMods(List<ModDTO> mods) { this.mods = mods; } public void setResourcePacks(List<ResourcePackDTO> resourcePacks) { this.resourcePacks = resourcePacks; } public void setMaps(List<MapDTO> maps) { this.maps = maps; } public void setShaderpacks(List<ShaderpackDTO> shaderpacks) { this.shaderpacks = shaderpacks; } public void setAdditionalFile(MetadataDTO additionalFile) { this.additionalFile = additionalFile; } public void setForgeVersion(String forgeVersion) { this.forgeVersion = forgeVersion; } public void setGameVersion(String gameVersion) { this.gameVersion = gameVersion; } public void setForgeVersionDTO(ForgeVersionDTO forgeVersionDTO) { this.forgeVersionDTO = forgeVersionDTO; } public void setGameVersionDTO(GameVersionDTO gameVersionDTO) { this.gameVersionDTO = gameVersionDTO; } public void setMinecraftVersionName(NameIdDTO minecraftVersionName) { this.minecraftVersionName = minecraftVersionName; }
/*  22 */   public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof ModpackVersionDTO)) return false;  ModpackVersionDTO other = (ModpackVersionDTO)o; if (!other.canEqual(this)) return false;  if (!super.equals(o)) return false;  Object<ModDTO> this$mods = (Object<ModDTO>)getMods(), other$mods = (Object<ModDTO>)other.getMods(); if ((this$mods == null) ? (other$mods != null) : !this$mods.equals(other$mods)) return false;  Object<ResourcePackDTO> this$resourcePacks = (Object<ResourcePackDTO>)getResourcePacks(), other$resourcePacks = (Object<ResourcePackDTO>)other.getResourcePacks(); if ((this$resourcePacks == null) ? (other$resourcePacks != null) : !this$resourcePacks.equals(other$resourcePacks)) return false;  Object<MapDTO> this$maps = (Object<MapDTO>)getMaps(), other$maps = (Object<MapDTO>)other.getMaps(); if ((this$maps == null) ? (other$maps != null) : !this$maps.equals(other$maps)) return false;  Object<ShaderpackDTO> this$shaderpacks = (Object<ShaderpackDTO>)getShaderpacks(), other$shaderpacks = (Object<ShaderpackDTO>)other.getShaderpacks(); if ((this$shaderpacks == null) ? (other$shaderpacks != null) : !this$shaderpacks.equals(other$shaderpacks)) return false;  Object this$additionalFile = getAdditionalFile(), other$additionalFile = other.getAdditionalFile(); if ((this$additionalFile == null) ? (other$additionalFile != null) : !this$additionalFile.equals(other$additionalFile)) return false;  Object this$forgeVersion = getForgeVersion(), other$forgeVersion = other.getForgeVersion(); if ((this$forgeVersion == null) ? (other$forgeVersion != null) : !this$forgeVersion.equals(other$forgeVersion)) return false;  Object this$gameVersion = getGameVersion(), other$gameVersion = other.getGameVersion(); if ((this$gameVersion == null) ? (other$gameVersion != null) : !this$gameVersion.equals(other$gameVersion)) return false;  Object this$forgeVersionDTO = getForgeVersionDTO(), other$forgeVersionDTO = other.getForgeVersionDTO(); if ((this$forgeVersionDTO == null) ? (other$forgeVersionDTO != null) : !this$forgeVersionDTO.equals(other$forgeVersionDTO)) return false;  Object this$gameVersionDTO = getGameVersionDTO(), other$gameVersionDTO = other.getGameVersionDTO(); if ((this$gameVersionDTO == null) ? (other$gameVersionDTO != null) : !this$gameVersionDTO.equals(other$gameVersionDTO)) return false;  Object this$minecraftVersionName = getMinecraftVersionName(), other$minecraftVersionName = other.getMinecraftVersionName(); return !((this$minecraftVersionName == null) ? (other$minecraftVersionName != null) : !this$minecraftVersionName.equals(other$minecraftVersionName)); } protected boolean canEqual(Object other) { return other instanceof ModpackVersionDTO; } public int hashCode() { int PRIME = 59; result = super.hashCode(); Object<ModDTO> $mods = (Object<ModDTO>)getMods(); result = result * 59 + (($mods == null) ? 43 : $mods.hashCode()); Object<ResourcePackDTO> $resourcePacks = (Object<ResourcePackDTO>)getResourcePacks(); result = result * 59 + (($resourcePacks == null) ? 43 : $resourcePacks.hashCode()); Object<MapDTO> $maps = (Object<MapDTO>)getMaps(); result = result * 59 + (($maps == null) ? 43 : $maps.hashCode()); Object<ShaderpackDTO> $shaderpacks = (Object<ShaderpackDTO>)getShaderpacks(); result = result * 59 + (($shaderpacks == null) ? 43 : $shaderpacks.hashCode()); Object $additionalFile = getAdditionalFile(); result = result * 59 + (($additionalFile == null) ? 43 : $additionalFile.hashCode()); Object $forgeVersion = getForgeVersion(); result = result * 59 + (($forgeVersion == null) ? 43 : $forgeVersion.hashCode()); Object $gameVersion = getGameVersion(); result = result * 59 + (($gameVersion == null) ? 43 : $gameVersion.hashCode()); Object $forgeVersionDTO = getForgeVersionDTO(); result = result * 59 + (($forgeVersionDTO == null) ? 43 : $forgeVersionDTO.hashCode()); Object $gameVersionDTO = getGameVersionDTO(); result = result * 59 + (($gameVersionDTO == null) ? 43 : $gameVersionDTO.hashCode()); Object $minecraftVersionName = getMinecraftVersionName(); return result * 59 + (($minecraftVersionName == null) ? 43 : $minecraftVersionName.hashCode()); } public String toString() {
/*  23 */     return "ModpackVersionDTO(super=" + super.toString() + ", mods=" + getMods() + ", resourcePacks=" + getResourcePacks() + ", maps=" + getMaps() + ", shaderpacks=" + getShaderpacks() + ", additionalFile=" + getAdditionalFile() + ", forgeVersion=" + getForgeVersion() + ", gameVersion=" + getGameVersion() + ", forgeVersionDTO=" + getForgeVersionDTO() + ", gameVersionDTO=" + getGameVersionDTO() + ", minecraftVersionName=" + getMinecraftVersionName() + ")";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  42 */   private List<ModDTO> mods = new ArrayList<>();
/*  43 */   private List<ResourcePackDTO> resourcePacks = new ArrayList<>();
/*  44 */   private List<MapDTO> maps = new ArrayList<>();
/*  45 */   private List<ShaderpackDTO> shaderpacks = new ArrayList<>(); private MetadataDTO additionalFile; private String forgeVersion; private String gameVersion; public List<? extends GameEntityDTO> getByType(GameType type) {
/*     */     List<ModDTO> list4;
/*     */     List<MapDTO> list3;
/*     */     List<ResourcePackDTO> list2;
/*     */     List<ShaderpackDTO> list1;
/*  50 */     List<? extends GameEntityDTO> list = new ArrayList<>();
/*  51 */     switch (type) {
/*     */       case MOD:
/*  53 */         list4 = this.mods;
/*     */         break;
/*     */       case null:
/*  56 */         list3 = this.maps;
/*     */         break;
/*     */       case RESOURCEPACK:
/*  59 */         list2 = this.resourcePacks;
/*     */         break;
/*     */       case SHADERPACK:
/*  62 */         list1 = this.shaderpacks;
/*     */         break;
/*     */     } 
/*  65 */     return (List)list1;
/*     */   }
/*     */   private ForgeVersionDTO forgeVersionDTO; private GameVersionDTO gameVersionDTO; private NameIdDTO minecraftVersionName;
/*     */   
/*     */   public void add(GameType type, GameEntityDTO e) {
/*  70 */     switch (type) {
/*     */       case MOD:
/*  72 */         this.mods.add((ModDTO)e);
/*     */         return;
/*     */       case null:
/*  75 */         this.maps.add((MapDTO)e);
/*     */         return;
/*     */       case RESOURCEPACK:
/*  78 */         this.resourcePacks.add((ResourcePackDTO)e);
/*     */         return;
/*     */       case SHADERPACK:
/*  81 */         this.shaderpacks.add((ShaderpackDTO)e);
/*     */         return;
/*     */     } 
/*     */   }
/*     */   
/*     */   public ModpackVersionDTO copy(ModpackVersionDTO version) {
/*  87 */     version.setId(getId());
/*  88 */     version.setForgeVersionDTO(getForgeVersionDTO());
/*  89 */     version.setGameVersionDTO(getGameVersionDTO());
/*  90 */     version.setAdditionalFile(this.additionalFile);
/*     */     
/*  92 */     version.setGameVersions(getGameVersions());
/*     */     
/*  94 */     version.setMaps((List<MapDTO>)new ModpackDTO.CheckNullList(this.maps));
/*  95 */     version.setMods((List<ModDTO>)new ModpackDTO.CheckNullList(this.mods));
/*  96 */     version.setShaderpacks((List<ShaderpackDTO>)new ModpackDTO.CheckNullList(this.shaderpacks));
/*  97 */     version.setName(getName());
/*  98 */     version.setResourcePacks((List<ResourcePackDTO>)new ModpackDTO.CheckNullList(this.resourcePacks));
/*  99 */     version.setType(getType());
/* 100 */     version.setUpdatedDate(getUpdatedDate());
/* 101 */     version.setJavaVersions(getJavaVersions());
/* 102 */     version.setMinecraftVersionTypes(getMinecraftVersionTypes());
/* 103 */     version.setMinecraftVersionName(getMinecraftVersionName());
/* 104 */     return version;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\modpack\domain\client\version\ModpackVersionDTO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */