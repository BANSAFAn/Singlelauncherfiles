/*   */ package org.tlauncher.modpack.domain.client;
/*   */ 
/*   */ public class MapDTO
/*   */   extends SubModpackDTO
/*   */ {
/*   */   public String toString() {
/* 7 */     return "MapDTO(super=" + super.toString() + ")";
/* 8 */   } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof MapDTO)) return false;  MapDTO other = (MapDTO)o; return !other.canEqual(this) ? false : (!!super.equals(o)); } protected boolean canEqual(Object other) { return other instanceof MapDTO; } public int hashCode() { return super.hashCode(); }
/*   */ 
/*   */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\modpack\domain\client\MapDTO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */