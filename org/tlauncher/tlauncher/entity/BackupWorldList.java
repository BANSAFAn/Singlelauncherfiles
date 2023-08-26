/*    */ package org.tlauncher.tlauncher.entity;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ 
/*    */ public class BackupWorldList
/*    */ {
/*    */   public String toString() {
/*  9 */     return "BackupWorldList(worlds=" + getWorlds() + ")"; } public int hashCode() { int PRIME = 59; result = 1; Object<BackupWorld> $worlds = (Object<BackupWorld>)getWorlds(); return result * 59 + (($worlds == null) ? 43 : $worlds.hashCode()); } protected boolean canEqual(Object other) { return other instanceof BackupWorldList; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof BackupWorldList)) return false;  BackupWorldList other = (BackupWorldList)o; if (!other.canEqual(this)) return false;  Object<BackupWorld> this$worlds = (Object<BackupWorld>)getWorlds(), other$worlds = (Object<BackupWorld>)other.getWorlds(); return !((this$worlds == null) ? (other$worlds != null) : !this$worlds.equals(other$worlds)); } public void setWorlds(Set<BackupWorld> worlds) { this.worlds = worlds; }
/*    */ 
/*    */   
/* 12 */   private Set<BackupWorld> worlds = new HashSet<>(); public Set<BackupWorld> getWorlds() { return this.worlds; }
/*    */   
/*    */   public static class BackupWorld {
/*    */     private String name;
/*    */     private String source;
/*    */     
/* 18 */     public void setName(String name) { this.name = name; } private String destination; private String lastChanged; private boolean backup; public void setSource(String source) { this.source = source; } public void setDestination(String destination) { this.destination = destination; } public void setLastChanged(String lastChanged) { this.lastChanged = lastChanged; } public void setBackup(boolean backup) { this.backup = backup; } public String toString() { return "BackupWorldList.BackupWorld(name=" + getName() + ", source=" + getSource() + ", destination=" + getDestination() + ", lastChanged=" + getLastChanged() + ", backup=" + isBackup() + ")"; }
/* 19 */     public BackupWorld(String name, String source, String destination, String lastChanged, boolean backup) { this.name = name; this.source = source; this.destination = destination; this.lastChanged = lastChanged; this.backup = backup; }
/* 20 */     public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof BackupWorld)) return false;  BackupWorld other = (BackupWorld)o; if (!other.canEqual(this)) return false;  Object this$name = getName(), other$name = other.getName(); if ((this$name == null) ? (other$name != null) : !this$name.equals(other$name)) return false;  Object this$source = getSource(), other$source = other.getSource(); if ((this$source == null) ? (other$source != null) : !this$source.equals(other$source)) return false;  Object this$destination = getDestination(), other$destination = other.getDestination(); if ((this$destination == null) ? (other$destination != null) : !this$destination.equals(other$destination)) return false;  Object this$lastChanged = getLastChanged(), other$lastChanged = other.getLastChanged(); return ((this$lastChanged == null) ? (other$lastChanged != null) : !this$lastChanged.equals(other$lastChanged)) ? false : (!(isBackup() != other.isBackup())); } protected boolean canEqual(Object other) { return other instanceof BackupWorld; } public int hashCode() { int PRIME = 59; result = 1; Object $name = getName(); result = result * 59 + (($name == null) ? 43 : $name.hashCode()); Object $source = getSource(); result = result * 59 + (($source == null) ? 43 : $source.hashCode()); Object $destination = getDestination(); result = result * 59 + (($destination == null) ? 43 : $destination.hashCode()); Object $lastChanged = getLastChanged(); result = result * 59 + (($lastChanged == null) ? 43 : $lastChanged.hashCode()); return result * 59 + (isBackup() ? 79 : 97); }
/*    */      public String getName() {
/* 22 */       return this.name;
/*    */     } public String getSource() {
/* 24 */       return this.source;
/*    */     }
/* 26 */     public String getDestination() { return this.destination; } public String getLastChanged() {
/* 27 */       return this.lastChanged;
/*    */     }
/*    */     
/*    */     public boolean isBackup() {
/* 31 */       return this.backup;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\entity\BackupWorldList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */