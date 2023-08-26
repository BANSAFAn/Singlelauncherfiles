/*    */ package org.tlauncher.modpack.domain.client.share;
/*    */ public class MinecraftVersionDTO { private Long id;
/*    */   private String name;
/*    */   
/*  5 */   public String toString() { return "MinecraftVersionDTO(id=" + getId() + ", name=" + getName() + ", value=" + getValue() + ", maturity=" + getMaturity() + ")"; } private String value; private VersionMaturity maturity; public int hashCode() { int PRIME = 59; result = 1; Object $id = getId(); result = result * 59 + (($id == null) ? 43 : $id.hashCode()); Object $name = getName(); result = result * 59 + (($name == null) ? 43 : $name.hashCode()); Object $value = getValue(); result = result * 59 + (($value == null) ? 43 : $value.hashCode()); Object $maturity = getMaturity(); return result * 59 + (($maturity == null) ? 43 : $maturity.hashCode()); } protected boolean canEqual(Object other) { return other instanceof MinecraftVersionDTO; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof MinecraftVersionDTO)) return false;  MinecraftVersionDTO other = (MinecraftVersionDTO)o; if (!other.canEqual(this)) return false;  Object this$id = getId(), other$id = other.getId(); if ((this$id == null) ? (other$id != null) : !this$id.equals(other$id)) return false;  Object this$name = getName(), other$name = other.getName(); if ((this$name == null) ? (other$name != null) : !this$name.equals(other$name)) return false;  Object this$value = getValue(), other$value = other.getValue(); if ((this$value == null) ? (other$value != null) : !this$value.equals(other$value)) return false;  Object this$maturity = getMaturity(), other$maturity = other.getMaturity(); return !((this$maturity == null) ? (other$maturity != null) : !this$maturity.equals(other$maturity)); } public void setMaturity(VersionMaturity maturity) { this.maturity = maturity; } public void setValue(String value) { this.value = value; } public void setName(String name) { this.name = name; } public void setId(Long id) { this.id = id; } public VersionMaturity getMaturity() { return this.maturity; } public String getValue() { return this.value; } public String getName() { return this.name; } public Long getId() { return this.id; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NameIdDTO createFromCurrent() {
/* 13 */     return new NameIdDTO(this.id, this.name);
/*    */   } }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\modpack\domain\client\share\MinecraftVersionDTO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */