/*   */ package by.gdev.util.model;
/*   */ public class GPUDescription {
/*   */   String name;
/*   */   
/* 5 */   public void setName(String name) { this.name = name; } String chipType; String memory; public void setChipType(String chipType) { this.chipType = chipType; } public void setMemory(String memory) { this.memory = memory; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof GPUDescription)) return false;  GPUDescription other = (GPUDescription)o; if (!other.canEqual(this)) return false;  Object this$name = getName(), other$name = other.getName(); if ((this$name == null) ? (other$name != null) : !this$name.equals(other$name)) return false;  Object this$chipType = getChipType(), other$chipType = other.getChipType(); if ((this$chipType == null) ? (other$chipType != null) : !this$chipType.equals(other$chipType)) return false;  Object this$memory = getMemory(), other$memory = other.getMemory(); return !((this$memory == null) ? (other$memory != null) : !this$memory.equals(other$memory)); } protected boolean canEqual(Object other) { return other instanceof GPUDescription; } public int hashCode() { int PRIME = 59; result = 1; Object $name = getName(); result = result * 59 + (($name == null) ? 43 : $name.hashCode()); Object $chipType = getChipType(); result = result * 59 + (($chipType == null) ? 43 : $chipType.hashCode()); Object $memory = getMemory(); return result * 59 + (($memory == null) ? 43 : $memory.hashCode()); } public String toString() { return "GPUDescription(name=" + getName() + ", chipType=" + getChipType() + ", memory=" + getMemory() + ")"; }
/*   */   
/* 7 */   public String getName() { return this.name; }
/* 8 */   public String getChipType() { return this.chipType; } public String getMemory() {
/* 9 */     return this.memory;
/*   */   }
/*   */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\by\gde\\util\model\GPUDescription.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */