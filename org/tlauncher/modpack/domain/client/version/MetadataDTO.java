/*    */ package org.tlauncher.modpack.domain.client.version;
/*    */ 
/*    */ import java.io.File;
/*    */ 
/*    */ public class MetadataDTO {
/*    */   protected String sha1;
/*    */   protected long size;
/*    */   
/*  9 */   public String getSha1() { return this.sha1; } protected String path; protected String url; @JsonIgnore protected transient File localDestination; public long getSize() { return this.size; } public String getPath() { return this.path; } public String getUrl() { return this.url; } public File getLocalDestination() { return this.localDestination; } public void setSha1(String sha1) { this.sha1 = sha1; } public void setSize(long size) { this.size = size; } public void setPath(String path) { this.path = path; } public void setUrl(String url) { this.url = url; } @JsonIgnore public void setLocalDestination(File localDestination) { this.localDestination = localDestination; } public String toString() { return "MetadataDTO(sha1=" + getSha1() + ", size=" + getSize() + ", path=" + getPath() + ", url=" + getUrl() + ", localDestination=" + getLocalDestination() + ")"; }
/* 10 */   public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof MetadataDTO)) return false;  MetadataDTO other = (MetadataDTO)o; if (!other.canEqual(this)) return false;  if (getSize() != other.getSize()) return false;  Object this$sha1 = getSha1(), other$sha1 = other.getSha1(); if ((this$sha1 == null) ? (other$sha1 != null) : !this$sha1.equals(other$sha1)) return false;  Object this$path = getPath(), other$path = other.getPath(); if ((this$path == null) ? (other$path != null) : !this$path.equals(other$path)) return false;  Object this$url = getUrl(), other$url = other.getUrl(); return !((this$url == null) ? (other$url != null) : !this$url.equals(other$url)); } protected boolean canEqual(Object other) { return other instanceof MetadataDTO; } public int hashCode() { int PRIME = 59; result = 1; long $size = getSize(); result = result * 59 + (int)($size ^ $size >>> 32L); Object $sha1 = getSha1(); result = result * 59 + (($sha1 == null) ? 43 : $sha1.hashCode()); Object $path = getPath(); result = result * 59 + (($path == null) ? 43 : $path.hashCode()); Object $url = getUrl(); return result * 59 + (($url == null) ? 43 : $url.hashCode()); }
/*    */ 
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\modpack\domain\client\version\MetadataDTO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */