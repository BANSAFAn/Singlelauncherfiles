/*    */ package org.tlauncher.modpack.domain.client.site;
/*    */ 
/*    */ public class ModerationEntityDTO extends GameEntityDTO {
/*    */   private Status moderationStatus;
/*    */   private String changedAuthor;
/*    */   private String userMessage;
/*    */   private String adminMessage;
/*    */   
/*  9 */   public Status getModerationStatus() { return this.moderationStatus; } public String getChangedAuthor() { return this.changedAuthor; } public String getUserMessage() { return this.userMessage; } public String getAdminMessage() { return this.adminMessage; } public void setModerationStatus(Status moderationStatus) { this.moderationStatus = moderationStatus; } public void setChangedAuthor(String changedAuthor) { this.changedAuthor = changedAuthor; } public void setUserMessage(String userMessage) { this.userMessage = userMessage; } public void setAdminMessage(String adminMessage) { this.adminMessage = adminMessage; }
/* 10 */   public String toString() { return "ModerationEntityDTO(super=" + super.toString() + ", moderationStatus=" + getModerationStatus() + ", changedAuthor=" + getChangedAuthor() + ", userMessage=" + getUserMessage() + ", adminMessage=" + getAdminMessage() + ")"; }
/* 11 */   public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof ModerationEntityDTO)) return false;  ModerationEntityDTO other = (ModerationEntityDTO)o; if (!other.canEqual(this)) return false;  if (!super.equals(o)) return false;  Object this$moderationStatus = getModerationStatus(), other$moderationStatus = other.getModerationStatus(); if ((this$moderationStatus == null) ? (other$moderationStatus != null) : !this$moderationStatus.equals(other$moderationStatus)) return false;  Object this$changedAuthor = getChangedAuthor(), other$changedAuthor = other.getChangedAuthor(); if ((this$changedAuthor == null) ? (other$changedAuthor != null) : !this$changedAuthor.equals(other$changedAuthor)) return false;  Object this$userMessage = getUserMessage(), other$userMessage = other.getUserMessage(); if ((this$userMessage == null) ? (other$userMessage != null) : !this$userMessage.equals(other$userMessage)) return false;  Object this$adminMessage = getAdminMessage(), other$adminMessage = other.getAdminMessage(); return !((this$adminMessage == null) ? (other$adminMessage != null) : !this$adminMessage.equals(other$adminMessage)); } protected boolean canEqual(Object other) { return other instanceof ModerationEntityDTO; } public int hashCode() { int PRIME = 59; result = super.hashCode(); Object $moderationStatus = getModerationStatus(); result = result * 59 + (($moderationStatus == null) ? 43 : $moderationStatus.hashCode()); Object $changedAuthor = getChangedAuthor(); result = result * 59 + (($changedAuthor == null) ? 43 : $changedAuthor.hashCode()); Object $userMessage = getUserMessage(); result = result * 59 + (($userMessage == null) ? 43 : $userMessage.hashCode()); Object $adminMessage = getAdminMessage(); return result * 59 + (($adminMessage == null) ? 43 : $adminMessage.hashCode()); }
/*    */ 
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\modpack\domain\client\site\ModerationEntityDTO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */