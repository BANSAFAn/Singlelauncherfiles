/*    */ package org.tlauncher.tlauncher.updater.bootstrapper.model;
/*    */ 
/*    */ import org.tlauncher.util.OS;
/*    */ 
/*    */ public class JavaConfig {
/*    */   Map<OS, Map<OS.Arch, JavaDownloadedElement>> config;
/*    */   
/*  8 */   public String toString() { return "JavaConfig(config=" + getConfig() + ")"; } public int hashCode() { int PRIME = 59; result = 1; Object<OS, Map<OS.Arch, JavaDownloadedElement>> $config = (Object<OS, Map<OS.Arch, JavaDownloadedElement>>)getConfig(); return result * 59 + (($config == null) ? 43 : $config.hashCode()); } protected boolean canEqual(Object other) { return other instanceof JavaConfig; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof JavaConfig)) return false;  JavaConfig other = (JavaConfig)o; if (!other.canEqual(this)) return false;  Object<OS, Map<OS.Arch, JavaDownloadedElement>> this$config = (Object<OS, Map<OS.Arch, JavaDownloadedElement>>)getConfig(), other$config = (Object<OS, Map<OS.Arch, JavaDownloadedElement>>)other.getConfig(); return !((this$config == null) ? (other$config != null) : !this$config.equals(other$config)); } public void setConfig(Map<OS, Map<OS.Arch, JavaDownloadedElement>> config) { this.config = config; }
/*    */    public Map<OS, Map<OS.Arch, JavaDownloadedElement>> getConfig() {
/* 10 */     return this.config;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\updater\bootstrapper\model\JavaConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */