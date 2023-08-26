/*    */ package org.tlauncher.modpack.domain.client;
/*    */ 
/*    */ public class PageIndexDTO {
/*    */   private Long id;
/*    */   private String topic;
/*    */   private String uri;
/*    */   private boolean ready;
/*    */   private Long gameEntityId;
/*    */   
/* 10 */   public Long getId() { return this.id; } private PageIndexEnum indexType; private GameType type; private Date updated; private Date updatedElement; private Long watched; private boolean engTranslation; public String getTopic() { return this.topic; } public String getUri() { return this.uri; } public boolean isReady() { return this.ready; } public Long getGameEntityId() { return this.gameEntityId; } public PageIndexEnum getIndexType() { return this.indexType; } public GameType getType() { return this.type; } public Date getUpdated() { return this.updated; } public Date getUpdatedElement() { return this.updatedElement; } public Long getWatched() { return this.watched; } public boolean isEngTranslation() { return this.engTranslation; } public void setId(Long id) { this.id = id; } public void setTopic(String topic) { this.topic = topic; } public void setUri(String uri) { this.uri = uri; } public void setReady(boolean ready) { this.ready = ready; } public void setGameEntityId(Long gameEntityId) { this.gameEntityId = gameEntityId; } public void setIndexType(PageIndexEnum indexType) { this.indexType = indexType; } public void setType(GameType type) { this.type = type; } public void setUpdated(Date updated) { this.updated = updated; } public void setUpdatedElement(Date updatedElement) { this.updatedElement = updatedElement; } public void setWatched(Long watched) { this.watched = watched; } public void setEngTranslation(boolean engTranslation) { this.engTranslation = engTranslation; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof PageIndexDTO)) return false;  PageIndexDTO other = (PageIndexDTO)o; if (!other.canEqual(this)) return false;  if (isReady() != other.isReady()) return false;  if (isEngTranslation() != other.isEngTranslation()) return false;  Object this$id = getId(), other$id = other.getId(); if ((this$id == null) ? (other$id != null) : !this$id.equals(other$id)) return false;  Object this$gameEntityId = getGameEntityId(), other$gameEntityId = other.getGameEntityId(); if ((this$gameEntityId == null) ? (other$gameEntityId != null) : !this$gameEntityId.equals(other$gameEntityId)) return false;  Object this$watched = getWatched(), other$watched = other.getWatched(); if ((this$watched == null) ? (other$watched != null) : !this$watched.equals(other$watched)) return false;  Object this$topic = getTopic(), other$topic = other.getTopic(); if ((this$topic == null) ? (other$topic != null) : !this$topic.equals(other$topic)) return false;  Object this$uri = getUri(), other$uri = other.getUri(); if ((this$uri == null) ? (other$uri != null) : !this$uri.equals(other$uri)) return false;  Object this$indexType = getIndexType(), other$indexType = other.getIndexType(); if ((this$indexType == null) ? (other$indexType != null) : !this$indexType.equals(other$indexType)) return false;  Object this$type = getType(), other$type = other.getType(); if ((this$type == null) ? (other$type != null) : !this$type.equals(other$type)) return false;  Object this$updated = getUpdated(), other$updated = other.getUpdated(); if ((this$updated == null) ? (other$updated != null) : !this$updated.equals(other$updated)) return false;  Object this$updatedElement = getUpdatedElement(), other$updatedElement = other.getUpdatedElement(); return !((this$updatedElement == null) ? (other$updatedElement != null) : !this$updatedElement.equals(other$updatedElement)); } protected boolean canEqual(Object other) { return other instanceof PageIndexDTO; } public int hashCode() { int PRIME = 59; result = 1; result = result * 59 + (isReady() ? 79 : 97); result = result * 59 + (isEngTranslation() ? 79 : 97); Object $id = getId(); result = result * 59 + (($id == null) ? 43 : $id.hashCode()); Object $gameEntityId = getGameEntityId(); result = result * 59 + (($gameEntityId == null) ? 43 : $gameEntityId.hashCode()); Object $watched = getWatched(); result = result * 59 + (($watched == null) ? 43 : $watched.hashCode()); Object $topic = getTopic(); result = result * 59 + (($topic == null) ? 43 : $topic.hashCode()); Object $uri = getUri(); result = result * 59 + (($uri == null) ? 43 : $uri.hashCode()); Object $indexType = getIndexType(); result = result * 59 + (($indexType == null) ? 43 : $indexType.hashCode()); Object $type = getType(); result = result * 59 + (($type == null) ? 43 : $type.hashCode()); Object $updated = getUpdated(); result = result * 59 + (($updated == null) ? 43 : $updated.hashCode()); Object $updatedElement = getUpdatedElement(); return result * 59 + (($updatedElement == null) ? 43 : $updatedElement.hashCode()); } public String toString() { return "PageIndexDTO(id=" + getId() + ", topic=" + getTopic() + ", uri=" + getUri() + ", ready=" + isReady() + ", gameEntityId=" + getGameEntityId() + ", indexType=" + getIndexType() + ", type=" + getType() + ", updated=" + getUpdated() + ", updatedElement=" + getUpdatedElement() + ", watched=" + getWatched() + ", engTranslation=" + isEngTranslation() + ")"; }
/*    */ 
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\modpack\domain\client\PageIndexDTO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */