/*   */ package org.tlauncher.modpack.domain.client.version;
/*   */ 
/*   */ 
/*   */ public class MapMetadataDTO extends MetadataDTO {
/*   */   private List<String> folders;
/*   */   
/* 7 */   public List<String> getFolders() { return this.folders; } public void setFolders(List<String> folders) { this.folders = folders; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof MapMetadataDTO)) return false;  MapMetadataDTO other = (MapMetadataDTO)o; if (!other.canEqual(this)) return false;  Object<String> this$folders = (Object<String>)getFolders(), other$folders = (Object<String>)other.getFolders(); return !((this$folders == null) ? (other$folders != null) : !this$folders.equals(other$folders)); } protected boolean canEqual(Object other) { return other instanceof MapMetadataDTO; } public int hashCode() { int PRIME = 59; result = 1; Object<String> $folders = (Object<String>)getFolders(); return result * 59 + (($folders == null) ? 43 : $folders.hashCode()); } public String toString() { return "MapMetadataDTO(folders=" + getFolders() + ")"; }
/*   */ 
/*   */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\modpack\domain\client\version\MapMetadataDTO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */