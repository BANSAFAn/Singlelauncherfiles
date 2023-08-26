/*    */ package org.tlauncher.modpack.domain.client;
/*    */ 
/*    */ import org.tlauncher.modpack.domain.client.share.StateGameElement;
/*    */ 
/*    */ public class SubModpackDTO
/*    */   extends GameEntityDTO {
/*    */   public StateGameElement getStateGameElement() {
/*  8 */     return this.stateGameElement; } public void setStateGameElement(StateGameElement stateGameElement) { this.stateGameElement = stateGameElement; }
/*  9 */   public String toString() { return "SubModpackDTO(super=" + super.toString() + ", stateGameElement=" + getStateGameElement() + ")"; }
/* 10 */   public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof SubModpackDTO)) return false;  SubModpackDTO other = (SubModpackDTO)o; if (!other.canEqual(this)) return false;  if (!super.equals(o)) return false;  Object this$stateGameElement = getStateGameElement(), other$stateGameElement = other.getStateGameElement(); return !((this$stateGameElement == null) ? (other$stateGameElement != null) : !this$stateGameElement.equals(other$stateGameElement)); } protected boolean canEqual(Object other) { return other instanceof SubModpackDTO; } public int hashCode() { int PRIME = 59; result = super.hashCode(); Object $stateGameElement = getStateGameElement(); return result * 59 + (($stateGameElement == null) ? 43 : $stateGameElement.hashCode()); }
/*    */   
/* 12 */   private StateGameElement stateGameElement = StateGameElement.ACTIVE;
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\modpack\domain\client\SubModpackDTO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */