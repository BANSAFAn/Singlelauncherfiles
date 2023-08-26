/*    */ package by.gdev.http.upload.download.downloader;
/*    */ 
/*    */ import java.time.Duration;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DownloaderStatus
/*    */ {
/*    */   private Duration duration;
/*    */   private double speed;
/*    */   private long downloadSize;
/*    */   private long allDownloadSize;
/*    */   private Integer leftFiles;
/*    */   private Integer allFiles;
/*    */   private List<Throwable> throwables;
/*    */   private DownloaderStatusEnum downloaderStatusEnum;
/*    */   
/*    */   public void setDuration(Duration duration) {
/* 21 */     this.duration = duration; } public void setSpeed(double speed) { this.speed = speed; } public void setDownloadSize(long downloadSize) { this.downloadSize = downloadSize; } public void setAllDownloadSize(long allDownloadSize) { this.allDownloadSize = allDownloadSize; } public void setLeftFiles(Integer leftFiles) { this.leftFiles = leftFiles; } public void setAllFiles(Integer allFiles) { this.allFiles = allFiles; } public void setThrowables(List<Throwable> throwables) { this.throwables = throwables; } public void setDownloaderStatusEnum(DownloaderStatusEnum downloaderStatusEnum) { this.downloaderStatusEnum = downloaderStatusEnum; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof DownloaderStatus)) return false;  DownloaderStatus other = (DownloaderStatus)o; if (!other.canEqual(this)) return false;  Object this$duration = getDuration(), other$duration = other.getDuration(); if ((this$duration == null) ? (other$duration != null) : !this$duration.equals(other$duration)) return false;  if (Double.compare(getSpeed(), other.getSpeed()) != 0) return false;  if (getDownloadSize() != other.getDownloadSize()) return false;  if (getAllDownloadSize() != other.getAllDownloadSize()) return false;  Object this$leftFiles = getLeftFiles(), other$leftFiles = other.getLeftFiles(); if ((this$leftFiles == null) ? (other$leftFiles != null) : !this$leftFiles.equals(other$leftFiles)) return false;  Object this$allFiles = getAllFiles(), other$allFiles = other.getAllFiles(); if ((this$allFiles == null) ? (other$allFiles != null) : !this$allFiles.equals(other$allFiles)) return false;  Object<Throwable> this$throwables = (Object<Throwable>)getThrowables(), other$throwables = (Object<Throwable>)other.getThrowables(); if ((this$throwables == null) ? (other$throwables != null) : !this$throwables.equals(other$throwables)) return false;  Object this$downloaderStatusEnum = getDownloaderStatusEnum(), other$downloaderStatusEnum = other.getDownloaderStatusEnum(); return !((this$downloaderStatusEnum == null) ? (other$downloaderStatusEnum != null) : !this$downloaderStatusEnum.equals(other$downloaderStatusEnum)); } protected boolean canEqual(Object other) { return other instanceof DownloaderStatus; } public int hashCode() { int PRIME = 59; result = 1; Object $duration = getDuration(); result = result * 59 + (($duration == null) ? 43 : $duration.hashCode()); long $speed = Double.doubleToLongBits(getSpeed()); result = result * 59 + (int)($speed >>> 32L ^ $speed); long $downloadSize = getDownloadSize(); result = result * 59 + (int)($downloadSize >>> 32L ^ $downloadSize); long $allDownloadSize = getAllDownloadSize(); result = result * 59 + (int)($allDownloadSize >>> 32L ^ $allDownloadSize); Object $leftFiles = getLeftFiles(); result = result * 59 + (($leftFiles == null) ? 43 : $leftFiles.hashCode()); Object $allFiles = getAllFiles(); result = result * 59 + (($allFiles == null) ? 43 : $allFiles.hashCode()); Object<Throwable> $throwables = (Object<Throwable>)getThrowables(); result = result * 59 + (($throwables == null) ? 43 : $throwables.hashCode()); Object $downloaderStatusEnum = getDownloaderStatusEnum(); return result * 59 + (($downloaderStatusEnum == null) ? 43 : $downloaderStatusEnum.hashCode()); } public String toString() { return "DownloaderStatus(duration=" + getDuration() + ", speed=" + getSpeed() + ", downloadSize=" + getDownloadSize() + ", allDownloadSize=" + getAllDownloadSize() + ", leftFiles=" + getLeftFiles() + ", allFiles=" + getAllFiles() + ", throwables=" + getThrowables() + ", downloaderStatusEnum=" + getDownloaderStatusEnum() + ")"; }
/*    */ 
/*    */ 
/*    */   
/*    */   public Duration getDuration() {
/* 26 */     return this.duration;
/*    */   }
/*    */   
/*    */   public double getSpeed() {
/* 30 */     return this.speed;
/*    */   }
/*    */   
/*    */   public long getDownloadSize() {
/* 34 */     return this.downloadSize;
/*    */   }
/*    */   
/*    */   public long getAllDownloadSize() {
/* 38 */     return this.allDownloadSize;
/* 39 */   } public Integer getLeftFiles() { return this.leftFiles; } public Integer getAllFiles() {
/* 40 */     return this.allFiles;
/*    */   }
/*    */   
/*    */   public List<Throwable> getThrowables() {
/* 44 */     return this.throwables; } public DownloaderStatusEnum getDownloaderStatusEnum() {
/* 45 */     return this.downloaderStatusEnum;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\by\gdev\htt\\upload\download\downloader\DownloaderStatus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */