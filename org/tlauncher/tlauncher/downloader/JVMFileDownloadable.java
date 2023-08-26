/*    */ package org.tlauncher.tlauncher.downloader;
/*    */ 
/*    */ import org.tlauncher.tlauncher.entity.minecraft.JVMFile;
/*    */ import org.tlauncher.tlauncher.repository.ClientInstanceRepo;
/*    */ 
/*    */ public class JVMFileDownloadable extends Downloadable {
/*    */   private JVMFile jvmFile;
/*    */   
/*  9 */   public void setJvmFile(JVMFile jvmFile) { this.jvmFile = jvmFile; } public String toString() { return "JVMFileDownloadable(jvmFile=" + getJvmFile() + ")"; }
/* 10 */   public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof JVMFileDownloadable)) return false;  JVMFileDownloadable other = (JVMFileDownloadable)o; if (!other.canEqual(this)) return false;  if (!super.equals(o)) return false;  Object this$jvmFile = getJvmFile(), other$jvmFile = other.getJvmFile(); return !((this$jvmFile == null) ? (other$jvmFile != null) : !this$jvmFile.equals(other$jvmFile)); } protected boolean canEqual(Object other) { return other instanceof JVMFileDownloadable; } public int hashCode() { int PRIME = 59; result = super.hashCode(); Object $jvmFile = getJvmFile(); return result * 59 + (($jvmFile == null) ? 43 : $jvmFile.hashCode()); }
/*    */   
/*    */   public JVMFile getJvmFile() {
/* 13 */     return this.jvmFile;
/*    */   }
/*    */   public JVMFileDownloadable(JVMFile file) {
/* 16 */     super(ClientInstanceRepo.EMPTY_REPO, file.getDownloads().getRaw());
/* 17 */     setJvmFile(file);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\downloader\JVMFileDownloadable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */