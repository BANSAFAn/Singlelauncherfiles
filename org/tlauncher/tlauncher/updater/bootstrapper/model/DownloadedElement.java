/*    */ package org.tlauncher.tlauncher.updater.bootstrapper.model;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.util.List;
/*    */ 
/*    */ public class DownloadedElement {
/*    */   private List<String> url;
/*    */   private String shaCode;
/*    */   private long size;
/*    */   private String storagePath;
/*    */   
/* 12 */   public void setUrl(List<String> url) { this.url = url; } public void setShaCode(String shaCode) { this.shaCode = shaCode; } public void setSize(long size) { this.size = size; } public void setStoragePath(String storagePath) { this.storagePath = storagePath; }
/* 13 */   public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof DownloadedElement)) return false;  DownloadedElement other = (DownloadedElement)o; if (!other.canEqual(this)) return false;  Object<String> this$url = (Object<String>)getUrl(), other$url = (Object<String>)other.getUrl(); if ((this$url == null) ? (other$url != null) : !this$url.equals(other$url)) return false;  Object this$shaCode = getShaCode(), other$shaCode = other.getShaCode(); if ((this$shaCode == null) ? (other$shaCode != null) : !this$shaCode.equals(other$shaCode)) return false;  if (getSize() != other.getSize()) return false;  Object this$storagePath = getStoragePath(), other$storagePath = other.getStoragePath(); return !((this$storagePath == null) ? (other$storagePath != null) : !this$storagePath.equals(other$storagePath)); } protected boolean canEqual(Object other) { return other instanceof DownloadedElement; } public int hashCode() { int PRIME = 59; result = 1; Object<String> $url = (Object<String>)getUrl(); result = result * 59 + (($url == null) ? 43 : $url.hashCode()); Object $shaCode = getShaCode(); result = result * 59 + (($shaCode == null) ? 43 : $shaCode.hashCode()); long $size = getSize(); result = result * 59 + (int)($size >>> 32L ^ $size); Object $storagePath = getStoragePath(); return result * 59 + (($storagePath == null) ? 43 : $storagePath.hashCode()); } public String toString() {
/* 14 */     return "DownloadedElement(url=" + getUrl() + ", shaCode=" + getShaCode() + ", size=" + getSize() + ", storagePath=" + getStoragePath() + ")";
/*    */   }
/* 16 */   public List<String> getUrl() { return this.url; }
/* 17 */   public String getShaCode() { return this.shaCode; } public long getSize() {
/* 18 */     return this.size;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getStoragePath() {
/* 23 */     return this.storagePath;
/*    */   }
/*    */   public boolean notExistOrValid(File folder) {
/* 26 */     File library = new File(folder, this.storagePath);
/* 27 */     if (library.exists() && library.isFile()) {
/* 28 */       String sha = FileUtil.getChecksum(library);
/* 29 */       return !getShaCode().equals(sha);
/*    */     } 
/* 31 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\updater\bootstrapper\model\DownloadedElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */