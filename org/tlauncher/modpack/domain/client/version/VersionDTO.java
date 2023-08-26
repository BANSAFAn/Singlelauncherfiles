/*    */ package org.tlauncher.modpack.domain.client.version;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.tlauncher.modpack.domain.client.GameVersionDTO;
/*    */ import org.tlauncher.modpack.domain.client.share.NameIdDTO;
/*    */ 
/*    */ public class VersionDTO {
/*    */   private Long id;
/*    */   private String name;
/*    */   private Date date;
/*    */   private Long updatedDate;
/*    */   private Long updateDate;
/*    */   private Type type;
/*    */   
/* 15 */   public String toString() { return "VersionDTO(id=" + getId() + ", name=" + getName() + ", date=" + getDate() + ", updatedDate=" + getUpdatedDate() + ", updateDate=" + getUpdateDate() + ", type=" + getType() + ", gameVersions=" + getGameVersions() + ", gameVersionsDTO=" + getGameVersionsDTO() + ", javaVersions=" + getJavaVersions() + ", metadata=" + getMetadata() + ", installed=" + getInstalled() + ", available=" + isAvailable() + ", minecraftVersionTypes=" + getMinecraftVersionTypes() + ")"; } private List<String> gameVersions; private List<GameVersionDTO> gameVersionsDTO; private List<JavaEnum> javaVersions; private MetadataDTO metadata; private Long installed; private boolean available; private List<NameIdDTO> minecraftVersionTypes; public int hashCode() { int PRIME = 59; result = 1; result = result * 59 + (isAvailable() ? 79 : 97); Object $id = getId(); result = result * 59 + (($id == null) ? 43 : $id.hashCode()); Object $updatedDate = getUpdatedDate(); result = result * 59 + (($updatedDate == null) ? 43 : $updatedDate.hashCode()); Object $updateDate = getUpdateDate(); result = result * 59 + (($updateDate == null) ? 43 : $updateDate.hashCode()); Object $installed = getInstalled(); result = result * 59 + (($installed == null) ? 43 : $installed.hashCode()); Object $name = getName(); result = result * 59 + (($name == null) ? 43 : $name.hashCode()); Object $date = getDate(); result = result * 59 + (($date == null) ? 43 : $date.hashCode()); Object $type = getType(); result = result * 59 + (($type == null) ? 43 : $type.hashCode()); Object<String> $gameVersions = (Object<String>)getGameVersions(); result = result * 59 + (($gameVersions == null) ? 43 : $gameVersions.hashCode()); Object<GameVersionDTO> $gameVersionsDTO = (Object<GameVersionDTO>)getGameVersionsDTO(); result = result * 59 + (($gameVersionsDTO == null) ? 43 : $gameVersionsDTO.hashCode()); Object<JavaEnum> $javaVersions = (Object<JavaEnum>)getJavaVersions(); result = result * 59 + (($javaVersions == null) ? 43 : $javaVersions.hashCode()); Object $metadata = getMetadata(); result = result * 59 + (($metadata == null) ? 43 : $metadata.hashCode()); Object<NameIdDTO> $minecraftVersionTypes = (Object<NameIdDTO>)getMinecraftVersionTypes(); return result * 59 + (($minecraftVersionTypes == null) ? 43 : $minecraftVersionTypes.hashCode()); } protected boolean canEqual(Object other) { return other instanceof VersionDTO; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof VersionDTO)) return false;  VersionDTO other = (VersionDTO)o; if (!other.canEqual(this)) return false;  if (isAvailable() != other.isAvailable()) return false;  Object this$id = getId(), other$id = other.getId(); if ((this$id == null) ? (other$id != null) : !this$id.equals(other$id)) return false;  Object this$updatedDate = getUpdatedDate(), other$updatedDate = other.getUpdatedDate(); if ((this$updatedDate == null) ? (other$updatedDate != null) : !this$updatedDate.equals(other$updatedDate)) return false;  Object this$updateDate = getUpdateDate(), other$updateDate = other.getUpdateDate(); if ((this$updateDate == null) ? (other$updateDate != null) : !this$updateDate.equals(other$updateDate)) return false;  Object this$installed = getInstalled(), other$installed = other.getInstalled(); if ((this$installed == null) ? (other$installed != null) : !this$installed.equals(other$installed)) return false;  Object this$name = getName(), other$name = other.getName(); if ((this$name == null) ? (other$name != null) : !this$name.equals(other$name)) return false;  Object this$date = getDate(), other$date = other.getDate(); if ((this$date == null) ? (other$date != null) : !this$date.equals(other$date)) return false;  Object this$type = getType(), other$type = other.getType(); if ((this$type == null) ? (other$type != null) : !this$type.equals(other$type)) return false;  Object<String> this$gameVersions = (Object<String>)getGameVersions(), other$gameVersions = (Object<String>)other.getGameVersions(); if ((this$gameVersions == null) ? (other$gameVersions != null) : !this$gameVersions.equals(other$gameVersions)) return false;  Object<GameVersionDTO> this$gameVersionsDTO = (Object<GameVersionDTO>)getGameVersionsDTO(), other$gameVersionsDTO = (Object<GameVersionDTO>)other.getGameVersionsDTO(); if ((this$gameVersionsDTO == null) ? (other$gameVersionsDTO != null) : !this$gameVersionsDTO.equals(other$gameVersionsDTO)) return false;  Object<JavaEnum> this$javaVersions = (Object<JavaEnum>)getJavaVersions(), other$javaVersions = (Object<JavaEnum>)other.getJavaVersions(); if ((this$javaVersions == null) ? (other$javaVersions != null) : !this$javaVersions.equals(other$javaVersions)) return false;  Object this$metadata = getMetadata(), other$metadata = other.getMetadata(); if ((this$metadata == null) ? (other$metadata != null) : !this$metadata.equals(other$metadata)) return false;  Object<NameIdDTO> this$minecraftVersionTypes = (Object<NameIdDTO>)getMinecraftVersionTypes(), other$minecraftVersionTypes = (Object<NameIdDTO>)other.getMinecraftVersionTypes(); return !((this$minecraftVersionTypes == null) ? (other$minecraftVersionTypes != null) : !this$minecraftVersionTypes.equals(other$minecraftVersionTypes)); } public void setMinecraftVersionTypes(List<NameIdDTO> minecraftVersionTypes) { this.minecraftVersionTypes = minecraftVersionTypes; } public void setAvailable(boolean available) { this.available = available; } public void setInstalled(Long installed) { this.installed = installed; } public void setMetadata(MetadataDTO metadata) { this.metadata = metadata; } public void setJavaVersions(List<JavaEnum> javaVersions) { this.javaVersions = javaVersions; } public void setGameVersionsDTO(List<GameVersionDTO> gameVersionsDTO) { this.gameVersionsDTO = gameVersionsDTO; } public void setGameVersions(List<String> gameVersions) { this.gameVersions = gameVersions; } public void setType(Type type) { this.type = type; } public void setUpdateDate(Long updateDate) { this.updateDate = updateDate; } public void setUpdatedDate(Long updatedDate) { this.updatedDate = updatedDate; } public void setDate(Date date) { this.date = date; } public void setName(String name) { this.name = name; } public void setId(Long id) { this.id = id; } public List<NameIdDTO> getMinecraftVersionTypes() { return this.minecraftVersionTypes; } public boolean isAvailable() { return this.available; } public Long getInstalled() { return this.installed; } public MetadataDTO getMetadata() { return this.metadata; } public List<JavaEnum> getJavaVersions() { return this.javaVersions; } public List<GameVersionDTO> getGameVersionsDTO() { return this.gameVersionsDTO; } public List<String> getGameVersions() { return this.gameVersions; } public Type getType() { return this.type; } public Long getUpdateDate() { return this.updateDate; } public Long getUpdatedDate() { return this.updatedDate; } public Date getDate() { return this.date; } public String getName() { return this.name; } public Long getId() { return this.id; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NameIdDTO findFirstMinecraftVersionType() {
/* 33 */     if (Objects.isNull(this.minecraftVersionTypes)) {
/* 34 */       return null;
/*    */     }
/* 36 */     Optional<NameIdDTO> op = this.minecraftVersionTypes.stream().findFirst();
/* 37 */     return op.isPresent() ? op.get() : null;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\modpack\domain\client\version\VersionDTO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */