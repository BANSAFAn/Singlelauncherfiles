/*    */ package org.tlauncher.tlauncher.ui.model;
/*    */ 
/*    */ import org.tlauncher.tlauncher.ui.modpack.DiscussionPanel;
/*    */ 
/*    */ public class InsertCommentDTO extends CommentDTO {
/*    */   private DiscussionPanel.Comment parent;
/*    */   
/*  8 */   public void setParent(DiscussionPanel.Comment parent) { this.parent = parent; } public String toString() { return "InsertCommentDTO(parent=" + getParent() + ")"; }
/*  9 */   public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof InsertCommentDTO)) return false;  InsertCommentDTO other = (InsertCommentDTO)o; if (!other.canEqual(this)) return false;  if (!super.equals(o)) return false;  Object this$parent = getParent(), other$parent = other.getParent(); return !((this$parent == null) ? (other$parent != null) : !this$parent.equals(other$parent)); } protected boolean canEqual(Object other) { return other instanceof InsertCommentDTO; } public int hashCode() { int PRIME = 59; result = super.hashCode(); Object $parent = getParent(); return result * 59 + (($parent == null) ? 43 : $parent.hashCode()); }
/*    */    public DiscussionPanel.Comment getParent() {
/* 11 */     return this.parent;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\model\InsertCommentDTO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */