/*    */ package org.tlauncher.tlauncher.entity.minecraft;
/*    */ public class JVMFile {
/*    */   private DownloadJVMFile downloads;
/*    */   private String targetPath;
/*    */   
/*  6 */   public void setDownloads(DownloadJVMFile downloads) { this.downloads = downloads; } private boolean executable; private String type; private String target; public void setTargetPath(String targetPath) { this.targetPath = targetPath; } public void setExecutable(boolean executable) { this.executable = executable; } public void setType(String type) { this.type = type; } public void setTarget(String target) { this.target = target; } public String toString() { return "JVMFile(downloads=" + getDownloads() + ", targetPath=" + getTargetPath() + ", executable=" + isExecutable() + ", type=" + getType() + ", target=" + getTarget() + ")"; }
/*  7 */   public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof JVMFile)) return false;  JVMFile other = (JVMFile)o; if (!other.canEqual(this)) return false;  Object this$downloads = getDownloads(), other$downloads = other.getDownloads(); if ((this$downloads == null) ? (other$downloads != null) : !this$downloads.equals(other$downloads)) return false;  Object this$targetPath = getTargetPath(), other$targetPath = other.getTargetPath(); if ((this$targetPath == null) ? (other$targetPath != null) : !this$targetPath.equals(other$targetPath)) return false;  if (isExecutable() != other.isExecutable()) return false;  Object this$type = getType(), other$type = other.getType(); if ((this$type == null) ? (other$type != null) : !this$type.equals(other$type)) return false;  Object this$target = getTarget(), other$target = other.getTarget(); return !((this$target == null) ? (other$target != null) : !this$target.equals(other$target)); } protected boolean canEqual(Object other) { return other instanceof JVMFile; } public int hashCode() { int PRIME = 59; result = 1; Object $downloads = getDownloads(); result = result * 59 + (($downloads == null) ? 43 : $downloads.hashCode()); Object $targetPath = getTargetPath(); result = result * 59 + (($targetPath == null) ? 43 : $targetPath.hashCode()); result = result * 59 + (isExecutable() ? 79 : 97); Object $type = getType(); result = result * 59 + (($type == null) ? 43 : $type.hashCode()); Object $target = getTarget(); return result * 59 + (($target == null) ? 43 : $target.hashCode()); }
/*    */    public DownloadJVMFile getDownloads() {
/*  9 */     return this.downloads;
/*    */   }
/*    */   
/*    */   public String getTargetPath() {
/* 13 */     return this.targetPath;
/* 14 */   } public boolean isExecutable() { return this.executable; }
/* 15 */   public String getType() { return this.type; } public String getTarget() {
/* 16 */     return this.target;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\entity\minecraft\JVMFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */