/*    */ package net.minecraft.launcher.versions;
/*    */ 
/*    */ 
/*    */ public class JavaVersionName
/*    */ {
/*    */   public void setComponent(String component) {
/*  7 */     this.component = component; } public void setMajorVersion(Double majorVersion) { this.majorVersion = majorVersion; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof JavaVersionName)) return false;  JavaVersionName other = (JavaVersionName)o; if (!other.canEqual(this)) return false;  Object this$component = getComponent(), other$component = other.getComponent(); if ((this$component == null) ? (other$component != null) : !this$component.equals(other$component)) return false;  Object this$majorVersion = getMajorVersion(), other$majorVersion = other.getMajorVersion(); return !((this$majorVersion == null) ? (other$majorVersion != null) : !this$majorVersion.equals(other$majorVersion)); } protected boolean canEqual(Object other) { return other instanceof JavaVersionName; } public int hashCode() { int PRIME = 59; result = 1; Object $component = getComponent(); result = result * 59 + (($component == null) ? 43 : $component.hashCode()); Object $majorVersion = getMajorVersion(); return result * 59 + (($majorVersion == null) ? 43 : $majorVersion.hashCode()); } public String toString() { return "JavaVersionName(component=" + getComponent() + ", majorVersion=" + getMajorVersion() + ")"; } public JavaVersionName(String component, Double majorVersion) {
/*  8 */     this.component = component; this.majorVersion = majorVersion;
/*    */   }
/*    */   public JavaVersionName() {}
/* 11 */   public static final JavaVersionName JAVA_8_LEGACY = new JavaVersionName("jre-legacy", Double.valueOf(8.0D));
/*    */   private String component; private Double majorVersion;
/* 13 */   public String getComponent() { return this.component; } public Double getMajorVersion() {
/* 14 */     return this.majorVersion;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\net\minecraft\launcher\versions\JavaVersionName.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */