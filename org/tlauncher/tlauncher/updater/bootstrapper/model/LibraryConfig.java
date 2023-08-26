/*    */ package org.tlauncher.tlauncher.updater.bootstrapper.model;
/*    */ 
/*    */ public class LibraryConfig {
/*    */   private double versionClient;
/*    */   private List<DownloadedElement> libraries;
/*    */   
/*  7 */   public void setVersionClient(double versionClient) { this.versionClient = versionClient; } public void setLibraries(List<DownloadedElement> libraries) { this.libraries = libraries; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof LibraryConfig)) return false;  LibraryConfig other = (LibraryConfig)o; if (!other.canEqual(this)) return false;  if (Double.compare(getVersionClient(), other.getVersionClient()) != 0) return false;  Object<DownloadedElement> this$libraries = (Object<DownloadedElement>)getLibraries(), other$libraries = (Object<DownloadedElement>)other.getLibraries(); return !((this$libraries == null) ? (other$libraries != null) : !this$libraries.equals(other$libraries)); } protected boolean canEqual(Object other) { return other instanceof LibraryConfig; } public int hashCode() { int PRIME = 59; result = 1; long $versionClient = Double.doubleToLongBits(getVersionClient()); result = result * 59 + (int)($versionClient >>> 32L ^ $versionClient); Object<DownloadedElement> $libraries = (Object<DownloadedElement>)getLibraries(); return result * 59 + (($libraries == null) ? 43 : $libraries.hashCode()); } public String toString() { return "LibraryConfig(versionClient=" + getVersionClient() + ", libraries=" + getLibraries() + ")"; }
/*    */ 
/*    */   
/* 10 */   public double getVersionClient() { return this.versionClient; } public List<DownloadedElement> getLibraries() {
/* 11 */     return this.libraries;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\updater\bootstrapper\model\LibraryConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */