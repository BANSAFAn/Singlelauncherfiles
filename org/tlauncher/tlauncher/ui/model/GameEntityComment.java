/*   */ package org.tlauncher.tlauncher.ui.model;
/*   */ public class GameEntityComment { private String description;
/*   */   private Long topicId;
/*   */   
/* 5 */   public void setDescription(String description) { this.description = description; } public void setTopicId(Long topicId) { this.topicId = topicId; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof GameEntityComment)) return false;  GameEntityComment other = (GameEntityComment)o; if (!other.canEqual(this)) return false;  Object this$description = getDescription(), other$description = other.getDescription(); if ((this$description == null) ? (other$description != null) : !this$description.equals(other$description)) return false;  Object this$topicId = getTopicId(), other$topicId = other.getTopicId(); return !((this$topicId == null) ? (other$topicId != null) : !this$topicId.equals(other$topicId)); } protected boolean canEqual(Object other) { return other instanceof GameEntityComment; } public int hashCode() { int PRIME = 59; result = 1; Object $description = getDescription(); result = result * 59 + (($description == null) ? 43 : $description.hashCode()); Object $topicId = getTopicId(); return result * 59 + (($topicId == null) ? 43 : $topicId.hashCode()); } public String toString() { return "GameEntityComment(description=" + getDescription() + ", topicId=" + getTopicId() + ")"; }
/*   */ 
/*   */   
/* 8 */   public String getDescription() { return this.description; } public Long getTopicId() {
/* 9 */     return this.topicId;
/*   */   } }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\model\GameEntityComment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */