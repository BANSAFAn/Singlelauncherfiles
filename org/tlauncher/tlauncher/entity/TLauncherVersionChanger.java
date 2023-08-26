/*    */ package org.tlauncher.tlauncher.entity;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.launcher.versions.CompleteVersion;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ 
/*    */ public class TLauncherVersionChanger {
/*    */   private Set<String> tlauncherSkinCapeVersion;
/*    */   private double version;
/*    */   private List<TLauncherLib> libraries;
/*    */   private List<TLauncherLib> additionalMods;
/*    */   
/* 16 */   public void setTlauncherSkinCapeVersion(Set<String> tlauncherSkinCapeVersion) { this.tlauncherSkinCapeVersion = tlauncherSkinCapeVersion; } public void setVersion(double version) { this.version = version; } public void setLibraries(List<TLauncherLib> libraries) { this.libraries = libraries; } public void setAdditionalMods(List<TLauncherLib> additionalMods) { this.additionalMods = additionalMods; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof TLauncherVersionChanger)) return false;  TLauncherVersionChanger other = (TLauncherVersionChanger)o; if (!other.canEqual(this)) return false;  Object<String> this$tlauncherSkinCapeVersion = (Object<String>)getTlauncherSkinCapeVersion(), other$tlauncherSkinCapeVersion = (Object<String>)other.getTlauncherSkinCapeVersion(); if ((this$tlauncherSkinCapeVersion == null) ? (other$tlauncherSkinCapeVersion != null) : !this$tlauncherSkinCapeVersion.equals(other$tlauncherSkinCapeVersion)) return false;  if (Double.compare(getVersion(), other.getVersion()) != 0) return false;  Object<TLauncherLib> this$libraries = (Object<TLauncherLib>)getLibraries(), other$libraries = (Object<TLauncherLib>)other.getLibraries(); if ((this$libraries == null) ? (other$libraries != null) : !this$libraries.equals(other$libraries)) return false;  Object<TLauncherLib> this$additionalMods = (Object<TLauncherLib>)getAdditionalMods(), other$additionalMods = (Object<TLauncherLib>)other.getAdditionalMods(); return !((this$additionalMods == null) ? (other$additionalMods != null) : !this$additionalMods.equals(other$additionalMods)); } protected boolean canEqual(Object other) { return other instanceof TLauncherVersionChanger; } public int hashCode() { int PRIME = 59; result = 1; Object<String> $tlauncherSkinCapeVersion = (Object<String>)getTlauncherSkinCapeVersion(); result = result * 59 + (($tlauncherSkinCapeVersion == null) ? 43 : $tlauncherSkinCapeVersion.hashCode()); long $version = Double.doubleToLongBits(getVersion()); result = result * 59 + (int)($version >>> 32L ^ $version); Object<TLauncherLib> $libraries = (Object<TLauncherLib>)getLibraries(); result = result * 59 + (($libraries == null) ? 43 : $libraries.hashCode()); Object<TLauncherLib> $additionalMods = (Object<TLauncherLib>)getAdditionalMods(); return result * 59 + (($additionalMods == null) ? 43 : $additionalMods.hashCode()); } public String toString() { return "TLauncherVersionChanger(tlauncherSkinCapeVersion=" + getTlauncherSkinCapeVersion() + ", version=" + getVersion() + ", libraries=" + getLibraries() + ", additionalMods=" + getAdditionalMods() + ")"; }
/*    */   
/* 18 */   public Set<String> getTlauncherSkinCapeVersion() { return this.tlauncherSkinCapeVersion; }
/* 19 */   public double getVersion() { return this.version; }
/* 20 */   public List<TLauncherLib> getLibraries() { return this.libraries; } public List<TLauncherLib> getAdditionalMods() {
/* 21 */     return this.additionalMods;
/*    */   }
/* 23 */   public static final List<String> defaultVersionTypes = Lists.newArrayList((Object[])new String[] { "Forge", "OptiFine", "Fabric" });
/*    */   
/*    */   public List<TLauncherLib> getAddedMods(CompleteVersion v) {
/* 26 */     String id = getVersionIDForUserSkinCapeVersion(v);
/* 27 */     return (List<TLauncherLib>)this.additionalMods.stream().filter(l -> l.getSupports().contains(id)).collect(Collectors.toList());
/*    */   }
/*    */   
/*    */   public List<TLauncherLib> getAddedMods(CompleteVersion v, boolean tlSkinType) {
/* 31 */     return (List<TLauncherLib>)getAddedMods(v).stream().filter(l -> l.isProperAccountTypeLib(tlSkinType))
/* 32 */       .collect(Collectors.toList());
/*    */   }
/*    */   
/*    */   public String getVersionIDForUserSkinCapeVersion(CompleteVersion complete) {
/* 36 */     if (complete.isActivateSkinCapeForUserVersion() && Objects.nonNull(complete.getJar())) {
/*    */       
/* 38 */       Optional<String> res = defaultVersionTypes.stream().filter(s -> StringUtils.containsIgnoreCase(complete.getID(), s)).findFirst();
/* 39 */       if (res.isPresent())
/* 40 */         return (String)res.get() + " " + complete.getJar(); 
/*    */     } 
/* 42 */     return complete.getID();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\entity\TLauncherVersionChanger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */