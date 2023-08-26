/*   */ package org.tlauncher.tlauncher.entity.minecraft;
/*   */ public class JavaVersion { private String name;
/*   */   private String released;
/*   */   
/* 5 */   public void setName(String name) { this.name = name; } public void setReleased(String released) { this.released = released; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof JavaVersion)) return false;  JavaVersion other = (JavaVersion)o; if (!other.canEqual(this)) return false;  Object this$name = getName(), other$name = other.getName(); if ((this$name == null) ? (other$name != null) : !this$name.equals(other$name)) return false;  Object this$released = getReleased(), other$released = other.getReleased(); return !((this$released == null) ? (other$released != null) : !this$released.equals(other$released)); } protected boolean canEqual(Object other) { return other instanceof JavaVersion; } public int hashCode() { int PRIME = 59; result = 1; Object $name = getName(); result = result * 59 + (($name == null) ? 43 : $name.hashCode()); Object $released = getReleased(); return result * 59 + (($released == null) ? 43 : $released.hashCode()); } public String toString() { return "JavaVersion(name=" + getName() + ", released=" + getReleased() + ")"; }
/*   */    public String getName() {
/* 7 */     return this.name;
/*   */   } public String getReleased() {
/* 9 */     return this.released;
/*   */   } }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\entity\minecraft\JavaVersion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */