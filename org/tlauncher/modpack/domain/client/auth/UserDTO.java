/*    */ package org.tlauncher.modpack.domain.client.auth;
/*    */ 
/*    */ 
/*    */ public class UserDTO extends UUIDUsername {
/*    */   private String faceImage;
/*    */   private boolean alert;
/*    */   private boolean hasAlerts;
/*    */   
/*  9 */   public String getFaceImage() { return this.faceImage; } public boolean isAlert() { return this.alert; } public boolean isHasAlerts() { return this.hasAlerts; } public void setFaceImage(String faceImage) { this.faceImage = faceImage; } public void setAlert(boolean alert) { this.alert = alert; } public void setHasAlerts(boolean hasAlerts) { this.hasAlerts = hasAlerts; }
/* 10 */   public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof UserDTO)) return false;  UserDTO other = (UserDTO)o; return !other.canEqual(this) ? false : (!super.equals(o) ? false : ((isAlert() != other.isAlert()) ? false : (!(isHasAlerts() != other.isHasAlerts())))); } protected boolean canEqual(Object other) { return other instanceof UserDTO; } public int hashCode() { int PRIME = 59; result = super.hashCode(); result = result * 59 + (isAlert() ? 79 : 97); return result * 59 + (isHasAlerts() ? 79 : 97); } public String toString() {
/* 11 */     return "UserDTO(super=" + super.toString() + ", alert=" + isAlert() + ", hasAlerts=" + isHasAlerts() + ")";
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\modpack\domain\client\auth\UserDTO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */