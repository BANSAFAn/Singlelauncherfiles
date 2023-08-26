/*    */ package org.tlauncher.tlauncher.entity.minecraft;
/*    */ 
/*    */ 
/*    */ public class JavaVersionDescription {
/*    */   private Availability availability;
/*    */   
/*  7 */   public void setAvailability(Availability availability) { this.availability = availability; } private MetadataDTO manifest; private JavaVersion version; public void setManifest(MetadataDTO manifest) { this.manifest = manifest; } public void setVersion(JavaVersion version) { this.version = version; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof JavaVersionDescription)) return false;  JavaVersionDescription other = (JavaVersionDescription)o; if (!other.canEqual(this)) return false;  Object this$availability = getAvailability(), other$availability = other.getAvailability(); if ((this$availability == null) ? (other$availability != null) : !this$availability.equals(other$availability)) return false;  Object this$manifest = getManifest(), other$manifest = other.getManifest(); if ((this$manifest == null) ? (other$manifest != null) : !this$manifest.equals(other$manifest)) return false;  Object this$version = getVersion(), other$version = other.getVersion(); return !((this$version == null) ? (other$version != null) : !this$version.equals(other$version)); } protected boolean canEqual(Object other) { return other instanceof JavaVersionDescription; } public int hashCode() { int PRIME = 59; result = 1; Object $availability = getAvailability(); result = result * 59 + (($availability == null) ? 43 : $availability.hashCode()); Object $manifest = getManifest(); result = result * 59 + (($manifest == null) ? 43 : $manifest.hashCode()); Object $version = getVersion(); return result * 59 + (($version == null) ? 43 : $version.hashCode()); } public String toString() { return "JavaVersionDescription(availability=" + getAvailability() + ", manifest=" + getManifest() + ", version=" + getVersion() + ")"; }
/*    */   
/*  9 */   public Availability getAvailability() { return this.availability; }
/* 10 */   public MetadataDTO getManifest() { return this.manifest; } public JavaVersion getVersion() {
/* 11 */     return this.version;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\entity\minecraft\JavaVersionDescription.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */