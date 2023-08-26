/*   */ package org.tlauncher.modpack.domain.client;
/*   */ 
/*   */ 
/*   */ public class ResourcePackDTO
/*   */   extends SubModpackDTO
/*   */ {
/*   */   public String toString() {
/* 8 */     return "ResourcePackDTO(super=" + super.toString() + ")";
/* 9 */   } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof ResourcePackDTO)) return false;  ResourcePackDTO other = (ResourcePackDTO)o; return !other.canEqual(this) ? false : (!!super.equals(o)); } protected boolean canEqual(Object other) { return other instanceof ResourcePackDTO; } public int hashCode() { return super.hashCode(); }
/*   */ 
/*   */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\modpack\domain\client\ResourcePackDTO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */