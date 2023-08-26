/*    */ package org.tlauncher.tlauncher.entity;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.Set;
/*    */ import java.util.regex.Pattern;
/*    */ import net.minecraft.launcher.versions.CompleteVersion;
/*    */ import net.minecraft.launcher.versions.Library;
/*    */ import net.minecraft.launcher.versions.json.Argument;
/*    */ import net.minecraft.launcher.versions.json.ArgumentType;
/*    */ import org.tlauncher.modpack.domain.client.version.MetadataDTO;
/*    */ import org.tlauncher.tlauncher.downloader.Downloadable;
/*    */ import org.tlauncher.tlauncher.minecraft.auth.Account;
/*    */ import org.tlauncher.tlauncher.repository.Repo;
/*    */ import org.tlauncher.util.OS;
/*    */ 
/*    */ public class TLauncherLib extends Library {
/*    */   public static final String USER_CONFIG_SKIN_VERSION = "userConfigSkinVersion";
/*    */   private Pattern pattern;
/*    */   private Map<ArgumentType, List<Argument>> arguments;
/*    */   private String mainClass;
/*    */   
/* 25 */   public void setPattern(Pattern pattern) { this.pattern = pattern; } public void setArguments(Map<ArgumentType, List<Argument>> arguments) { this.arguments = arguments; } public void setMainClass(String mainClass) { this.mainClass = mainClass; } public void setRequires(List<Library> requires) { this.requires = requires; } public void setSupports(List<String> supports) { this.supports = supports; } public void setAccountTypes(Set<Account.AccountType> accountTypes) { this.accountTypes = accountTypes; } public void setDownloads(Map<String, MetadataDTO> downloads) { this.downloads = downloads; }
/* 26 */   public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof TLauncherLib)) return false;  TLauncherLib other = (TLauncherLib)o; if (!other.canEqual(this)) return false;  if (!super.equals(o)) return false;  Object this$pattern = getPattern(), other$pattern = other.getPattern(); if ((this$pattern == null) ? (other$pattern != null) : !this$pattern.equals(other$pattern)) return false;  Object<ArgumentType, List<Argument>> this$arguments = (Object<ArgumentType, List<Argument>>)getArguments(), other$arguments = (Object<ArgumentType, List<Argument>>)other.getArguments(); if ((this$arguments == null) ? (other$arguments != null) : !this$arguments.equals(other$arguments)) return false;  Object this$mainClass = getMainClass(), other$mainClass = other.getMainClass(); if ((this$mainClass == null) ? (other$mainClass != null) : !this$mainClass.equals(other$mainClass)) return false;  Object<Library> this$requires = (Object<Library>)getRequires(), other$requires = (Object<Library>)other.getRequires(); if ((this$requires == null) ? (other$requires != null) : !this$requires.equals(other$requires)) return false;  Object<String> this$supports = (Object<String>)getSupports(), other$supports = (Object<String>)other.getSupports(); if ((this$supports == null) ? (other$supports != null) : !this$supports.equals(other$supports)) return false;  Object<Account.AccountType> this$accountTypes = (Object<Account.AccountType>)getAccountTypes(), other$accountTypes = (Object<Account.AccountType>)other.getAccountTypes(); if ((this$accountTypes == null) ? (other$accountTypes != null) : !this$accountTypes.equals(other$accountTypes)) return false;  Object<String, MetadataDTO> this$downloads = (Object<String, MetadataDTO>)getDownloads(), other$downloads = (Object<String, MetadataDTO>)other.getDownloads(); return !((this$downloads == null) ? (other$downloads != null) : !this$downloads.equals(other$downloads)); } protected boolean canEqual(Object other) { return other instanceof TLauncherLib; } public int hashCode() { int PRIME = 59; result = super.hashCode(); Object $pattern = getPattern(); result = result * 59 + (($pattern == null) ? 43 : $pattern.hashCode()); Object<ArgumentType, List<Argument>> $arguments = (Object<ArgumentType, List<Argument>>)getArguments(); result = result * 59 + (($arguments == null) ? 43 : $arguments.hashCode()); Object $mainClass = getMainClass(); result = result * 59 + (($mainClass == null) ? 43 : $mainClass.hashCode()); Object<Library> $requires = (Object<Library>)getRequires(); result = result * 59 + (($requires == null) ? 43 : $requires.hashCode()); Object<String> $supports = (Object<String>)getSupports(); result = result * 59 + (($supports == null) ? 43 : $supports.hashCode()); Object<Account.AccountType> $accountTypes = (Object<Account.AccountType>)getAccountTypes(); result = result * 59 + (($accountTypes == null) ? 43 : $accountTypes.hashCode()); Object<String, MetadataDTO> $downloads = (Object<String, MetadataDTO>)getDownloads(); return result * 59 + (($downloads == null) ? 43 : $downloads.hashCode()); } public String toString() {
/* 27 */     return "TLauncherLib(super=" + super.toString() + ", pattern=" + getPattern() + ", arguments=" + getArguments() + ", mainClass=" + getMainClass() + ", requires=" + getRequires() + ", supports=" + getSupports() + ", accountTypes=" + getAccountTypes() + ", downloads=" + getDownloads() + ")";
/*    */   }
/*    */   
/*    */   public Pattern getPattern() {
/* 31 */     return this.pattern;
/* 32 */   } public Map<ArgumentType, List<Argument>> getArguments() { return this.arguments; } public String getMainClass() {
/* 33 */     return this.mainClass;
/* 34 */   } private List<String> supports; private Set<Account.AccountType> accountTypes; private List<Library> requires = new ArrayList<>(); private Map<String, MetadataDTO> downloads; public List<Library> getRequires() { return this.requires; }
/* 35 */   public List<String> getSupports() { return this.supports; }
/* 36 */   public Set<Account.AccountType> getAccountTypes() { return this.accountTypes; } public Map<String, MetadataDTO> getDownloads() {
/* 37 */     return this.downloads;
/*    */   }
/*    */   public TLauncherLib() {
/* 40 */     setUrl("/libraries/");
/*    */   }
/*    */   
/*    */   public boolean matches(Library lib) {
/* 44 */     return (this.pattern != null && this.pattern.matcher(lib.getName()).matches());
/*    */   }
/*    */   
/*    */   public boolean isSupport(String version) {
/* 48 */     if (Objects.isNull(this.supports))
/* 49 */       return false; 
/* 50 */     return this.supports.contains(version);
/*    */   }
/*    */   public boolean isProperAccountTypeLib(boolean tlAccountImpl) {
/* 53 */     if (Objects.isNull(this.accountTypes))
/* 54 */       return true; 
/* 55 */     if (tlAccountImpl) {
/* 56 */       return this.accountTypes.contains(Account.AccountType.TLAUNCHER);
/*    */     }
/* 58 */     return (this.accountTypes.contains(Account.AccountType.MOJANG) || this.accountTypes.contains(Account.AccountType.MICROSOFT));
/*    */   }
/*    */   
/*    */   public boolean isApply(Library lib, CompleteVersion v) {
/* 62 */     return (matches(lib) && (Objects.isNull(this.supports) || isSupport(v.getID()) || v.isActivateSkinCapeForUserVersion()));
/*    */   }
/*    */ 
/*    */   
/*    */   public Downloadable getDownloadable(Repo versionSource, File file, OS os) {
/* 67 */     U.log(new Object[] { "getting downloadable", getName(), versionSource, file, os });
/* 68 */     return super.getDownloadable(ClientInstanceRepo.EXTRA_VERSION_REPO, file, os);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\entity\TLauncherLib.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */