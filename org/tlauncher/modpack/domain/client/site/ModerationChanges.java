/*   */ package org.tlauncher.modpack.domain.client.site;
/*   */ 
/*   */ public class ModerationChanges {
/*   */   private List<String> gameEntityChanges;
/*   */   private List<List<String>> versionChanges;
/*   */   
/* 7 */   public List<String> getGameEntityChanges() { return this.gameEntityChanges; } public List<List<String>> getVersionChanges() { return this.versionChanges; } public void setGameEntityChanges(List<String> gameEntityChanges) { this.gameEntityChanges = gameEntityChanges; } public void setVersionChanges(List<List<String>> versionChanges) { this.versionChanges = versionChanges; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof ModerationChanges)) return false;  ModerationChanges other = (ModerationChanges)o; if (!other.canEqual(this)) return false;  Object<String> this$gameEntityChanges = (Object<String>)getGameEntityChanges(), other$gameEntityChanges = (Object<String>)other.getGameEntityChanges(); if ((this$gameEntityChanges == null) ? (other$gameEntityChanges != null) : !this$gameEntityChanges.equals(other$gameEntityChanges)) return false;  Object<List<String>> this$versionChanges = (Object<List<String>>)getVersionChanges(), other$versionChanges = (Object<List<String>>)other.getVersionChanges(); return !((this$versionChanges == null) ? (other$versionChanges != null) : !this$versionChanges.equals(other$versionChanges)); } protected boolean canEqual(Object other) { return other instanceof ModerationChanges; } public int hashCode() { int PRIME = 59; result = 1; Object<String> $gameEntityChanges = (Object<String>)getGameEntityChanges(); result = result * 59 + (($gameEntityChanges == null) ? 43 : $gameEntityChanges.hashCode()); Object<List<String>> $versionChanges = (Object<List<String>>)getVersionChanges(); return result * 59 + (($versionChanges == null) ? 43 : $versionChanges.hashCode()); } public String toString() { return "ModerationChanges(gameEntityChanges=" + getGameEntityChanges() + ", versionChanges=" + getVersionChanges() + ")"; }
/*   */ 
/*   */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\modpack\domain\client\site\ModerationChanges.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */