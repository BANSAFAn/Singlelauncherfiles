/*   */ package org.tlauncher.modpack.domain.client;
/*   */ public class TagDTO {
/*   */   private String name;
/*   */   
/* 5 */   public String getName() { return this.name; } public void setName(String name) { this.name = name; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof TagDTO)) return false;  TagDTO other = (TagDTO)o; if (!other.canEqual(this)) return false;  Object this$name = getName(), other$name = other.getName(); return !((this$name == null) ? (other$name != null) : !this$name.equals(other$name)); } protected boolean canEqual(Object other) { return other instanceof TagDTO; } public int hashCode() { int PRIME = 59; result = 1; Object $name = getName(); return result * 59 + (($name == null) ? 43 : $name.hashCode()); } public String toString() { return "TagDTO(name=" + getName() + ")"; }
/*   */ 
/*   */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\modpack\domain\client\TagDTO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */