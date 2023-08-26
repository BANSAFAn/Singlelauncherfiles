/*   */ package org.tlauncher.modpack.domain.client;
/*   */ 
/*   */ 
/*   */ public class ShaderpackDTO
/*   */   extends SubModpackDTO
/*   */ {
/*   */   public String toString() {
/* 8 */     return "ShaderpackDTO(super=" + super.toString() + ")";
/* 9 */   } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof ShaderpackDTO)) return false;  ShaderpackDTO other = (ShaderpackDTO)o; return !other.canEqual(this) ? false : (!!super.equals(o)); } protected boolean canEqual(Object other) { return other instanceof ShaderpackDTO; } public int hashCode() { return super.hashCode(); }
/*   */ 
/*   */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\modpack\domain\client\ShaderpackDTO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */