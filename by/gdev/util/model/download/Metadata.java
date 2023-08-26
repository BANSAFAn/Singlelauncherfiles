/*    */ package by.gdev.util.model.download;
/*    */ 
/*    */ import java.nio.file.Path;
/*    */ import java.security.NoSuchAlgorithmException;
/*    */ import java.util.List;
/*    */ 
/*    */ public class Metadata {
/*    */   private String sha1;
/*    */   private long size;
/*    */   private String path;
/*    */   private List<String> urls;
/*    */   private String relativeUrl;
/*    */   private boolean executable;
/*    */   private String link;
/*    */   
/* 16 */   public void setSha1(String sha1) { this.sha1 = sha1; } public void setSize(long size) { this.size = size; } public void setPath(String path) { this.path = path; } public void setUrls(List<String> urls) { this.urls = urls; } public void setRelativeUrl(String relativeUrl) { this.relativeUrl = relativeUrl; } public void setExecutable(boolean executable) { this.executable = executable; } public void setLink(String link) { this.link = link; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof Metadata)) return false;  Metadata other = (Metadata)o; if (!other.canEqual(this)) return false;  Object this$sha1 = getSha1(), other$sha1 = other.getSha1(); if ((this$sha1 == null) ? (other$sha1 != null) : !this$sha1.equals(other$sha1)) return false;  if (getSize() != other.getSize()) return false;  Object this$path = getPath(), other$path = other.getPath(); if ((this$path == null) ? (other$path != null) : !this$path.equals(other$path)) return false;  Object<String> this$urls = (Object<String>)getUrls(), other$urls = (Object<String>)other.getUrls(); if ((this$urls == null) ? (other$urls != null) : !this$urls.equals(other$urls)) return false;  Object this$relativeUrl = getRelativeUrl(), other$relativeUrl = other.getRelativeUrl(); if ((this$relativeUrl == null) ? (other$relativeUrl != null) : !this$relativeUrl.equals(other$relativeUrl)) return false;  if (isExecutable() != other.isExecutable()) return false;  Object this$link = getLink(), other$link = other.getLink(); return !((this$link == null) ? (other$link != null) : !this$link.equals(other$link)); } protected boolean canEqual(Object other) { return other instanceof Metadata; } public int hashCode() { int PRIME = 59; result = 1; Object $sha1 = getSha1(); result = result * 59 + (($sha1 == null) ? 43 : $sha1.hashCode()); long $size = getSize(); result = result * 59 + (int)($size >>> 32L ^ $size); Object $path = getPath(); result = result * 59 + (($path == null) ? 43 : $path.hashCode()); Object<String> $urls = (Object<String>)getUrls(); result = result * 59 + (($urls == null) ? 43 : $urls.hashCode()); Object $relativeUrl = getRelativeUrl(); result = result * 59 + (($relativeUrl == null) ? 43 : $relativeUrl.hashCode()); result = result * 59 + (isExecutable() ? 79 : 97); Object $link = getLink(); return result * 59 + (($link == null) ? 43 : $link.hashCode()); } public String toString() { return "Metadata(sha1=" + getSha1() + ", size=" + getSize() + ", path=" + getPath() + ", urls=" + getUrls() + ", relativeUrl=" + getRelativeUrl() + ", executable=" + isExecutable() + ", link=" + getLink() + ")"; }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getSha1() {
/* 21 */     return this.sha1;
/*    */   }
/*    */   
/*    */   public long getSize() {
/* 25 */     return this.size;
/*    */   }
/*    */   
/*    */   public String getPath() {
/* 29 */     return this.path;
/*    */   }
/*    */   
/*    */   public List<String> getUrls() {
/* 33 */     return this.urls;
/*    */   }
/*    */   
/*    */   public String getRelativeUrl() {
/* 37 */     return this.relativeUrl;
/*    */   }
/*    */   
/*    */   public boolean isExecutable() {
/* 41 */     return this.executable;
/*    */   }
/*    */   
/*    */   public String getLink() {
/* 45 */     return this.link;
/*    */   }
/*    */   public static Metadata createMetadata(Path config) throws NoSuchAlgorithmException, IOException {
/* 48 */     Metadata metadata = new Metadata();
/* 49 */     metadata.setPath(config.toString().replace("\\", "/"));
/* 50 */     metadata.setRelativeUrl(config.toString().replace("\\", "/"));
/* 51 */     metadata.setSha1(DesktopUtil.getChecksum(config.toFile(), "sha-1"));
/* 52 */     return metadata;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\by\gde\\util\model\download\Metadata.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */