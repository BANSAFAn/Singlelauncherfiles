/*   */ package org.tlauncher.tlauncher.entity.minecraft;
/*   */ public class Availability { private Integer group;
/*   */   private Integer progress;
/*   */   
/* 5 */   public void setGroup(Integer group) { this.group = group; } public void setProgress(Integer progress) { this.progress = progress; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof Availability)) return false;  Availability other = (Availability)o; if (!other.canEqual(this)) return false;  Object this$group = getGroup(), other$group = other.getGroup(); if ((this$group == null) ? (other$group != null) : !this$group.equals(other$group)) return false;  Object this$progress = getProgress(), other$progress = other.getProgress(); return !((this$progress == null) ? (other$progress != null) : !this$progress.equals(other$progress)); } protected boolean canEqual(Object other) { return other instanceof Availability; } public int hashCode() { int PRIME = 59; result = 1; Object $group = getGroup(); result = result * 59 + (($group == null) ? 43 : $group.hashCode()); Object $progress = getProgress(); return result * 59 + (($progress == null) ? 43 : $progress.hashCode()); } public String toString() { return "Availability(group=" + getGroup() + ", progress=" + getProgress() + ")"; }
/*   */   
/* 7 */   public Integer getGroup() { return this.group; } public Integer getProgress() {
/* 8 */     return this.progress;
/*   */   } }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\entity\minecraft\Availability.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */