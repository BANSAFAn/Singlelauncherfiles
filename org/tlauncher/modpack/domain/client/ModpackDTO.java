/*    */ package org.tlauncher.modpack.domain.client;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import org.tlauncher.modpack.domain.client.version.ModpackVersionDTO;
/*    */ import org.tlauncher.modpack.domain.client.version.VersionDTO;
/*    */ 
/*    */ public class ModpackDTO extends GameEntityDTO {
/*    */   private boolean modpackMemory;
/*    */   private int memory;
/*    */   
/* 11 */   public boolean isModpackMemory() { return this.modpackMemory; } public int getMemory() { return this.memory; } public void setModpackMemory(boolean modpackMemory) { this.modpackMemory = modpackMemory; } public void setMemory(int memory) { this.memory = memory; }
/* 12 */   public String toString() { return "ModpackDTO(super=" + super.toString() + ", modpackMemory=" + isModpackMemory() + ", memory=" + getMemory() + ")"; }
/* 13 */   public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof ModpackDTO)) return false;  ModpackDTO other = (ModpackDTO)o; return !other.canEqual(this) ? false : (!super.equals(o) ? false : ((isModpackMemory() != other.isModpackMemory()) ? false : (!(getMemory() != other.getMemory())))); } protected boolean canEqual(Object other) { return other instanceof ModpackDTO; } public int hashCode() { int PRIME = 59; result = super.hashCode(); result = result * 59 + (isModpackMemory() ? 79 : 97); return result * 59 + getMemory(); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void copy(ModpackDTO c) {
/* 20 */     c.setId(getId());
/* 21 */     c.setAuthor(getAuthor());
/* 22 */     c.setCategories(new CheckNullList<>(getCategories()));
/* 23 */     c.setDescription(getDescription());
/* 24 */     c.setInstalled(getInstalled());
/* 25 */     c.setDownloadMonth(getDownloadMonth());
/* 26 */     c.setMemory(this.memory);
/* 27 */     c.setModpackMemory(this.modpackMemory);
/* 28 */     c.setOfficialSite(getOfficialSite());
/* 29 */     c.setName(getName());
/* 30 */     c.setPicture(getPicture());
/* 31 */     c.setPictures(new CheckNullList<>(getPictures()));
/* 32 */     c.setPopulateStatus(isPopulateStatus());
/* 33 */     c.setTags(new CheckNullList<>(getTags()));
/* 34 */     c.setUserInstall(isUserInstall());
/* 35 */     ModpackVersionDTO version = (ModpackVersionDTO)getVersion();
/* 36 */     ModpackVersionDTO modpackVersionDTO = new ModpackVersionDTO();
/* 37 */     version.copy(modpackVersionDTO);
/* 38 */     c.setVersion((VersionDTO)modpackVersionDTO);
/* 39 */     c.setVersions(new CheckNullList<>(getVersions()));
/*    */   }
/*    */   
/*    */   public static class CheckNullList<E> extends ArrayList<E> {
/*    */     public CheckNullList(Collection<? extends E> c) {
/* 44 */       super((c == null) ? new ArrayList<>() : c);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\modpack\domain\client\ModpackDTO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */