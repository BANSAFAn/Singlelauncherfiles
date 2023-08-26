/*    */ package org.tlauncher.tlauncher.entity;
/*    */ 
/*    */ 
/*    */ public class AdditionalAsset {
/*    */   private List<String> versions;
/*    */   private List<MetadataDTO> files;
/*    */   
/*  8 */   public void setVersions(List<String> versions) { this.versions = versions; } public void setFiles(List<MetadataDTO> files) { this.files = files; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof AdditionalAsset)) return false;  AdditionalAsset other = (AdditionalAsset)o; if (!other.canEqual(this)) return false;  Object<String> this$versions = (Object<String>)getVersions(), other$versions = (Object<String>)other.getVersions(); if ((this$versions == null) ? (other$versions != null) : !this$versions.equals(other$versions)) return false;  Object<MetadataDTO> this$files = (Object<MetadataDTO>)getFiles(), other$files = (Object<MetadataDTO>)other.getFiles(); return !((this$files == null) ? (other$files != null) : !this$files.equals(other$files)); } protected boolean canEqual(Object other) { return other instanceof AdditionalAsset; } public int hashCode() { int PRIME = 59; result = 1; Object<String> $versions = (Object<String>)getVersions(); result = result * 59 + (($versions == null) ? 43 : $versions.hashCode()); Object<MetadataDTO> $files = (Object<MetadataDTO>)getFiles(); return result * 59 + (($files == null) ? 43 : $files.hashCode()); } public String toString() { return "AdditionalAsset(versions=" + getVersions() + ", files=" + getFiles() + ")"; }
/*    */   
/* 10 */   public List<String> getVersions() { return this.versions; } public List<MetadataDTO> getFiles() {
/* 11 */     return this.files;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\entity\AdditionalAsset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */