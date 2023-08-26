/*    */ package org.tlauncher.modpack.domain.client;
/*    */ public class BasicId {
/*    */   private Long id;
/*    */   
/*  5 */   public String toString() { return "BasicId(id=" + getId() + ")"; } public int hashCode() { int PRIME = 59; result = 1; Object $id = getId(); return result * 59 + (($id == null) ? 43 : $id.hashCode()); } protected boolean canEqual(Object other) { return other instanceof BasicId; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof BasicId)) return false;  BasicId other = (BasicId)o; if (!other.canEqual(this)) return false;  Object this$id = getId(), other$id = other.getId(); return !((this$id == null) ? (other$id != null) : !this$id.equals(other$id)); } public void setId(Long id) { this.id = id; } public Long getId() { return this.id; }
/*    */ 
/*    */ 
/*    */   
/*    */   public static BasicId create(Long id) {
/* 10 */     BasicId b = new BasicId();
/* 11 */     b.setId(id);
/* 12 */     return b;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\modpack\domain\client\BasicId.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */