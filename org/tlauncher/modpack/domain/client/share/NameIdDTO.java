/*    */ package org.tlauncher.modpack.domain.client.share;
/*    */ 
/*    */ public class NameIdDTO
/*    */ {
/*    */   private Long id;
/*    */   private String name;
/*    */   
/*    */   public Long getId() {
/*  9 */     return this.id; } public String getName() { return this.name; } public void setId(Long id) { this.id = id; } public void setName(String name) { this.name = name; }
/* 10 */   public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof NameIdDTO)) return false;  NameIdDTO other = (NameIdDTO)o; if (!other.canEqual(this)) return false;  Object this$id = getId(), other$id = other.getId(); if ((this$id == null) ? (other$id != null) : !this$id.equals(other$id)) return false;  Object this$name = getName(), other$name = other.getName(); return !((this$name == null) ? (other$name != null) : !this$name.equals(other$name)); } protected boolean canEqual(Object other) { return other instanceof NameIdDTO; } public int hashCode() { int PRIME = 59; result = 1; Object $id = getId(); result = result * 59 + (($id == null) ? 43 : $id.hashCode()); Object $name = getName(); return result * 59 + (($name == null) ? 43 : $name.hashCode()); }
/* 11 */   public String toString() { return "NameIdDTO(id=" + getId() + ", name=" + getName() + ")"; } public NameIdDTO(Long id, String name) {
/* 12 */     this.id = id; this.name = name;
/*    */   }
/*    */   
/*    */   public NameIdDTO() {}
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\modpack\domain\client\share\NameIdDTO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */