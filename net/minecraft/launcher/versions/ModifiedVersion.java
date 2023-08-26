/*    */ package net.minecraft.launcher.versions;
/*    */ 
/*    */ import org.tlauncher.modpack.domain.client.version.MetadataDTO;
/*    */ 
/*    */ public class ModifiedVersion {
/*    */   private boolean activateSkinCapeForUserVersion;
/*    */   private boolean skipHashsumValidation;
/*    */   private List<Library> modsLibraries;
/*    */   private List<MetadataDTO> additionalFiles;
/*    */   
/* 11 */   public void setActivateSkinCapeForUserVersion(boolean activateSkinCapeForUserVersion) { this.activateSkinCapeForUserVersion = activateSkinCapeForUserVersion; } public void setSkipHashsumValidation(boolean skipHashsumValidation) { this.skipHashsumValidation = skipHashsumValidation; } public void setModsLibraries(List<Library> modsLibraries) { this.modsLibraries = modsLibraries; } public void setAdditionalFiles(List<MetadataDTO> additionalFiles) { this.additionalFiles = additionalFiles; } public void setTlauncherVersion(Integer tlauncherVersion) { this.tlauncherVersion = tlauncherVersion; } public void setModpack(ModpackDTO modpack) { this.modpack = modpack; } public void setSource(Repo source) { this.source = source; } public void setJar(String jar) { this.jar = jar; } public void setSkinVersion(boolean skinVersion) { this.skinVersion = skinVersion; } public void setUrl(String url) { this.url = url; } public void setRemoteVersion(String remoteVersion) { this.remoteVersion = remoteVersion; } public void setUpdatedTime(Date updatedTime) { this.updatedTime = updatedTime; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof ModifiedVersion)) return false;  ModifiedVersion other = (ModifiedVersion)o; if (!other.canEqual(this)) return false;  if (isActivateSkinCapeForUserVersion() != other.isActivateSkinCapeForUserVersion()) return false;  if (isSkipHashsumValidation() != other.isSkipHashsumValidation()) return false;  Object<Library> this$modsLibraries = (Object<Library>)getModsLibraries(), other$modsLibraries = (Object<Library>)other.getModsLibraries(); if ((this$modsLibraries == null) ? (other$modsLibraries != null) : !this$modsLibraries.equals(other$modsLibraries)) return false;  Object<MetadataDTO> this$additionalFiles = (Object<MetadataDTO>)getAdditionalFiles(), other$additionalFiles = (Object<MetadataDTO>)other.getAdditionalFiles(); if ((this$additionalFiles == null) ? (other$additionalFiles != null) : !this$additionalFiles.equals(other$additionalFiles)) return false;  Object this$tlauncherVersion = getTlauncherVersion(), other$tlauncherVersion = other.getTlauncherVersion(); if ((this$tlauncherVersion == null) ? (other$tlauncherVersion != null) : !this$tlauncherVersion.equals(other$tlauncherVersion)) return false;  Object this$modpack = getModpack(), other$modpack = other.getModpack(); if ((this$modpack == null) ? (other$modpack != null) : !this$modpack.equals(other$modpack)) return false;  Object this$source = getSource(), other$source = other.getSource(); if ((this$source == null) ? (other$source != null) : !this$source.equals(other$source)) return false;  Object this$jar = getJar(), other$jar = other.getJar(); if ((this$jar == null) ? (other$jar != null) : !this$jar.equals(other$jar)) return false;  if (isSkinVersion() != other.isSkinVersion()) return false;  Object this$url = getUrl(), other$url = other.getUrl(); if ((this$url == null) ? (other$url != null) : !this$url.equals(other$url)) return false;  Object this$updatedTime = getUpdatedTime(), other$updatedTime = other.getUpdatedTime(); return !((this$updatedTime == null) ? (other$updatedTime != null) : !this$updatedTime.equals(other$updatedTime)); } protected boolean canEqual(Object other) { return other instanceof ModifiedVersion; } public int hashCode() { int PRIME = 59; result = 1; result = result * 59 + (isActivateSkinCapeForUserVersion() ? 79 : 97); result = result * 59 + (isSkipHashsumValidation() ? 79 : 97); Object<Library> $modsLibraries = (Object<Library>)getModsLibraries(); result = result * 59 + (($modsLibraries == null) ? 43 : $modsLibraries.hashCode()); Object<MetadataDTO> $additionalFiles = (Object<MetadataDTO>)getAdditionalFiles(); result = result * 59 + (($additionalFiles == null) ? 43 : $additionalFiles.hashCode()); Object $tlauncherVersion = getTlauncherVersion(); result = result * 59 + (($tlauncherVersion == null) ? 43 : $tlauncherVersion.hashCode()); Object $modpack = getModpack(); result = result * 59 + (($modpack == null) ? 43 : $modpack.hashCode()); Object $source = getSource(); result = result * 59 + (($source == null) ? 43 : $source.hashCode()); Object $jar = getJar(); result = result * 59 + (($jar == null) ? 43 : $jar.hashCode()); result = result * 59 + (isSkinVersion() ? 79 : 97); Object $url = getUrl(); result = result * 59 + (($url == null) ? 43 : $url.hashCode()); Object $updatedTime = getUpdatedTime(); return result * 59 + (($updatedTime == null) ? 43 : $updatedTime.hashCode()); } public String toString() { return "ModifiedVersion(activateSkinCapeForUserVersion=" + isActivateSkinCapeForUserVersion() + ", skipHashsumValidation=" + isSkipHashsumValidation() + ", modsLibraries=" + getModsLibraries() + ", additionalFiles=" + getAdditionalFiles() + ", tlauncherVersion=" + getTlauncherVersion() + ", modpack=" + getModpack() + ", source=" + getSource() + ", jar=" + getJar() + ", skinVersion=" + isSkinVersion() + ", url=" + getUrl() + ", remoteVersion=" + getRemoteVersion() + ", updatedTime=" + getUpdatedTime() + ")"; }
/*    */   
/*    */   public boolean isActivateSkinCapeForUserVersion() {
/* 14 */     return this.activateSkinCapeForUserVersion;
/*    */   } public boolean isSkipHashsumValidation() {
/* 16 */     return this.skipHashsumValidation;
/*    */   } public List<Library> getModsLibraries() {
/* 18 */     return this.modsLibraries;
/*    */   } public List<MetadataDTO> getAdditionalFiles() {
/* 20 */     return this.additionalFiles;
/*    */   }
/* 22 */   private ModpackDTO modpack; private Repo source; private String jar; private boolean skinVersion; private Integer tlauncherVersion = Integer.valueOf(0); private String url; private transient String remoteVersion; private Date updatedTime; public Integer getTlauncherVersion() { return this.tlauncherVersion; }
/*    */    public ModpackDTO getModpack() {
/* 24 */     return this.modpack;
/*    */   } public Repo getSource() {
/* 26 */     return this.source;
/*    */   } public String getJar() {
/* 28 */     return this.jar;
/*    */   } public boolean isSkinVersion() {
/* 30 */     return this.skinVersion;
/*    */   } public String getUrl() {
/* 32 */     return this.url;
/*    */   } public String getRemoteVersion() {
/* 34 */     return this.remoteVersion;
/*    */   } public Date getUpdatedTime() {
/* 36 */     return this.updatedTime;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\net\minecraft\launcher\versions\ModifiedVersion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */