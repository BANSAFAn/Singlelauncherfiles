/*   */ package org.tlauncher.modpack.domain.client;
/*   */ public class CommentPositionDTO { private Long id;
/*   */   private boolean position;
/*   */   
/* 5 */   public Long getId() { return this.id; } public boolean isPosition() { return this.position; } public void setId(Long id) { this.id = id; } public void setPosition(boolean position) { this.position = position; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof CommentPositionDTO)) return false;  CommentPositionDTO other = (CommentPositionDTO)o; if (!other.canEqual(this)) return false;  if (isPosition() != other.isPosition()) return false;  Object this$id = getId(), other$id = other.getId(); return !((this$id == null) ? (other$id != null) : !this$id.equals(other$id)); } protected boolean canEqual(Object other) { return other instanceof CommentPositionDTO; } public int hashCode() { int PRIME = 59; result = 1; result = result * 59 + (isPosition() ? 79 : 97); Object $id = getId(); return result * 59 + (($id == null) ? 43 : $id.hashCode()); } public String toString() { return "CommentPositionDTO(id=" + getId() + ", position=" + isPosition() + ")"; }
/*   */    }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\modpack\domain\client\CommentPositionDTO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */