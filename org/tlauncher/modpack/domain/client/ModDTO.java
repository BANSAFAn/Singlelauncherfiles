/*    */ package org.tlauncher.modpack.domain.client;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ public class ModDTO
/*    */   extends SubModpackDTO
/*    */ {
/*    */   public String toString() {
/* 11 */     return "ModDTO(super=" + super.toString() + ")";
/* 12 */   } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof ModDTO)) return false;  ModDTO other = (ModDTO)o; return !other.canEqual(this) ? false : (!!super.equals(o)); } protected boolean canEqual(Object other) { return other instanceof ModDTO; } public int hashCode() { return super.hashCode(); }
/*    */   
/* 14 */   public static final Long OPTIFINE_ID = Long.valueOf(9999999L);
/* 15 */   public static final Long TL_SKIN_ID = Long.valueOf(10000014L);
/* 16 */   public static final Long TL_SKIN_CAPE_ID = Long.valueOf(10000020L);
/* 17 */   public static final Long FABRIC_API_ID = Long.valueOf(306612L);
/* 18 */   public static final Long QUILTED_FABRIC_API_ID = Long.valueOf(634179L);
/* 19 */   public static final Set<Long> SKIN_MODS = new HashSet<Long>() {
/*    */       private static final long serialVersionUID = 1L;
/*    */     };
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\modpack\domain\client\ModDTO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */