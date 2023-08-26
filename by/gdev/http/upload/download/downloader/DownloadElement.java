/*    */ package by.gdev.http.upload.download.downloader;
/*    */ 
/*    */ import by.gdev.http.download.handler.PostHandler;
/*    */ import by.gdev.util.model.download.Metadata;
/*    */ import by.gdev.util.model.download.Repo;
/*    */ import java.time.LocalTime;
/*    */ import java.util.List;
/*    */ 
/*    */ public class DownloadElement {
/*    */   private List<PostHandler> handlers;
/*    */   private Metadata metadata;
/*    */   private String pathToDownload;
/*    */   private LocalTime start;
/*    */   private LocalTime end;
/*    */   private Repo repo;
/*    */   private volatile long downloadBytes;
/*    */   private volatile Throwable error;
/*    */   
/*    */   public void setHandlers(List<PostHandler> handlers) {
/* 20 */     this.handlers = handlers; } public void setMetadata(Metadata metadata) { this.metadata = metadata; } public void setPathToDownload(String pathToDownload) { this.pathToDownload = pathToDownload; } public void setStart(LocalTime start) { this.start = start; } public void setEnd(LocalTime end) { this.end = end; } public void setRepo(Repo repo) { this.repo = repo; } public void setDownloadBytes(long downloadBytes) { this.downloadBytes = downloadBytes; } public void setError(Throwable error) { this.error = error; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof DownloadElement)) return false;  DownloadElement other = (DownloadElement)o; if (!other.canEqual(this)) return false;  Object<PostHandler> this$handlers = (Object<PostHandler>)getHandlers(), other$handlers = (Object<PostHandler>)other.getHandlers(); if ((this$handlers == null) ? (other$handlers != null) : !this$handlers.equals(other$handlers)) return false;  Object this$metadata = getMetadata(), other$metadata = other.getMetadata(); if ((this$metadata == null) ? (other$metadata != null) : !this$metadata.equals(other$metadata)) return false;  Object this$pathToDownload = getPathToDownload(), other$pathToDownload = other.getPathToDownload(); if ((this$pathToDownload == null) ? (other$pathToDownload != null) : !this$pathToDownload.equals(other$pathToDownload)) return false;  Object this$start = getStart(), other$start = other.getStart(); if ((this$start == null) ? (other$start != null) : !this$start.equals(other$start)) return false;  Object this$end = getEnd(), other$end = other.getEnd(); if ((this$end == null) ? (other$end != null) : !this$end.equals(other$end)) return false;  Object this$repo = getRepo(), other$repo = other.getRepo(); if ((this$repo == null) ? (other$repo != null) : !this$repo.equals(other$repo)) return false;  if (getDownloadBytes() != other.getDownloadBytes()) return false;  Object this$error = getError(), other$error = other.getError(); return !((this$error == null) ? (other$error != null) : !this$error.equals(other$error)); } protected boolean canEqual(Object other) { return other instanceof DownloadElement; } public int hashCode() { int PRIME = 59; result = 1; Object<PostHandler> $handlers = (Object<PostHandler>)getHandlers(); result = result * 59 + (($handlers == null) ? 43 : $handlers.hashCode()); Object $metadata = getMetadata(); result = result * 59 + (($metadata == null) ? 43 : $metadata.hashCode()); Object $pathToDownload = getPathToDownload(); result = result * 59 + (($pathToDownload == null) ? 43 : $pathToDownload.hashCode()); Object $start = getStart(); result = result * 59 + (($start == null) ? 43 : $start.hashCode()); Object $end = getEnd(); result = result * 59 + (($end == null) ? 43 : $end.hashCode()); Object $repo = getRepo(); result = result * 59 + (($repo == null) ? 43 : $repo.hashCode()); long $downloadBytes = getDownloadBytes(); result = result * 59 + (int)($downloadBytes >>> 32L ^ $downloadBytes); Object $error = getError(); return result * 59 + (($error == null) ? 43 : $error.hashCode()); } public String toString() { return "DownloadElement(handlers=" + getHandlers() + ", metadata=" + getMetadata() + ", pathToDownload=" + getPathToDownload() + ", start=" + getStart() + ", end=" + getEnd() + ", repo=" + getRepo() + ", downloadBytes=" + getDownloadBytes() + ", error=" + getError() + ")"; }
/*    */   
/* 22 */   public List<PostHandler> getHandlers() { return this.handlers; }
/* 23 */   public Metadata getMetadata() { return this.metadata; } public String getPathToDownload() {
/* 24 */     return this.pathToDownload;
/*    */   }
/*    */   
/*    */   public LocalTime getStart() {
/* 28 */     return this.start;
/* 29 */   } public LocalTime getEnd() { return this.end; }
/* 30 */   public Repo getRepo() { return this.repo; }
/* 31 */   public long getDownloadBytes() { return this.downloadBytes; } public Throwable getError() {
/* 32 */     return this.error;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\by\gdev\htt\\upload\download\downloader\DownloadElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */