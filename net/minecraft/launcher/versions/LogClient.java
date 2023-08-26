/*    */ package net.minecraft.launcher.versions;public class LogClient { private String argument;
/*    */   private LogClientFile file;
/*    */   private String type;
/*    */   
/*  5 */   public void setArgument(String argument) { this.argument = argument; } public void setFile(LogClientFile file) { this.file = file; } public void setType(String type) { this.type = type; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof LogClient)) return false;  LogClient other = (LogClient)o; if (!other.canEqual(this)) return false;  Object this$argument = getArgument(), other$argument = other.getArgument(); if ((this$argument == null) ? (other$argument != null) : !this$argument.equals(other$argument)) return false;  Object this$file = getFile(), other$file = other.getFile(); if ((this$file == null) ? (other$file != null) : !this$file.equals(other$file)) return false;  Object this$type = getType(), other$type = other.getType(); return !((this$type == null) ? (other$type != null) : !this$type.equals(other$type)); } protected boolean canEqual(Object other) { return other instanceof LogClient; } public int hashCode() { int PRIME = 59; result = 1; Object $argument = getArgument(); result = result * 59 + (($argument == null) ? 43 : $argument.hashCode()); Object $file = getFile(); result = result * 59 + (($file == null) ? 43 : $file.hashCode()); Object $type = getType(); return result * 59 + (($type == null) ? 43 : $type.hashCode()); } public String toString() { return "LogClient(argument=" + getArgument() + ", file=" + getFile() + ", type=" + getType() + ")"; }
/*    */ 
/*    */   
/*  8 */   public String getArgument() { return this.argument; }
/*  9 */   public LogClientFile getFile() { return this.file; } public String getType() {
/* 10 */     return this.type;
/*    */   } public class LogClientFile {
/* 12 */     private String id; private String sha1; public void setId(String id) { this.id = id; } private long size; private String url; public void setSha1(String sha1) { this.sha1 = sha1; } public void setSize(long size) { this.size = size; } public void setUrl(String url) { this.url = url; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof LogClientFile)) return false;  LogClientFile other = (LogClientFile)o; if (!other.canEqual(this)) return false;  Object this$id = getId(), other$id = other.getId(); if ((this$id == null) ? (other$id != null) : !this$id.equals(other$id)) return false;  Object this$sha1 = getSha1(), other$sha1 = other.getSha1(); if ((this$sha1 == null) ? (other$sha1 != null) : !this$sha1.equals(other$sha1)) return false;  if (getSize() != other.getSize()) return false;  Object this$url = getUrl(), other$url = other.getUrl(); return !((this$url == null) ? (other$url != null) : !this$url.equals(other$url)); } protected boolean canEqual(Object other) { return other instanceof LogClientFile; } public int hashCode() { int PRIME = 59; result = 1; Object $id = getId(); result = result * 59 + (($id == null) ? 43 : $id.hashCode()); Object $sha1 = getSha1(); result = result * 59 + (($sha1 == null) ? 43 : $sha1.hashCode()); long $size = getSize(); result = result * 59 + (int)($size >>> 32L ^ $size); Object $url = getUrl(); return result * 59 + (($url == null) ? 43 : $url.hashCode()); } public String toString() { return "LogClient.LogClientFile(id=" + getId() + ", sha1=" + getSha1() + ", size=" + getSize() + ", url=" + getUrl() + ")"; }
/*    */     
/* 14 */     public String getId() { return this.id; }
/* 15 */     public String getSha1() { return this.sha1; }
/* 16 */     public long getSize() { return this.size; } public String getUrl() {
/* 17 */       return this.url;
/*    */     }
/*    */   } }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\net\minecraft\launcher\versions\LogClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */