/*   */ package org.tlauncher.statistics;
/*   */ 
/*   */ public class InstallVersionDTO {
/*   */   private String installVersion;
/*   */   
/* 6 */   public String getInstallVersion() { return this.installVersion; } public void setInstallVersion(String installVersion) { this.installVersion = installVersion; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof InstallVersionDTO)) return false;  InstallVersionDTO other = (InstallVersionDTO)o; if (!other.canEqual(this)) return false;  Object this$installVersion = getInstallVersion(), other$installVersion = other.getInstallVersion(); return !((this$installVersion == null) ? (other$installVersion != null) : !this$installVersion.equals(other$installVersion)); } protected boolean canEqual(Object other) { return other instanceof InstallVersionDTO; } public int hashCode() { int PRIME = 59; result = 1; Object $installVersion = getInstallVersion(); return result * 59 + (($installVersion == null) ? 43 : $installVersion.hashCode()); } public String toString() { return "InstallVersionDTO(installVersion=" + getInstallVersion() + ")"; }
/*   */ 
/*   */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\statistics\InstallVersionDTO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */