/*    */ package org.tlauncher.tlauncher.exceptions.auth;
/*    */ public class BlockedUserException extends AuthenticatorException {
/*    */   private String minutes;
/*    */   private static final long serialVersionUID = -8707094238355495569L;
/*    */   
/*  6 */   public void setMinutes(String minutes) { this.minutes = minutes; } public String toString() { return "BlockedUserException(minutes=" + getMinutes() + ")"; }
/*  7 */   public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof BlockedUserException)) return false;  BlockedUserException other = (BlockedUserException)o; if (!other.canEqual(this)) return false;  if (!super.equals(o)) return false;  Object this$minutes = getMinutes(), other$minutes = other.getMinutes(); return !((this$minutes == null) ? (other$minutes != null) : !this$minutes.equals(other$minutes)); } protected boolean canEqual(Object other) { return other instanceof BlockedUserException; } public int hashCode() { int PRIME = 59; result = super.hashCode(); Object $minutes = getMinutes(); return result * 59 + (($minutes == null) ? 43 : $minutes.hashCode()); }
/*    */    public String getMinutes() {
/*  9 */     return this.minutes;
/*    */   }
/*    */   public BlockedUserException(String minutes, String cause) {
/* 12 */     super(cause, "user.blocked");
/* 13 */     this.minutes = minutes;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\exceptions\auth\BlockedUserException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */