/*    */ package org.tlauncher.tlauncher.ui.modpack.filter.version;
/*    */ 
/*    */ import java.util.Comparator;
/*    */ import org.tlauncher.modpack.domain.client.GameEntityDTO;
/*    */ import org.tlauncher.modpack.domain.client.ModpackDTO;
/*    */ import org.tlauncher.modpack.domain.client.share.GameType;
/*    */ import org.tlauncher.modpack.domain.client.version.ModVersionDTO;
/*    */ import org.tlauncher.modpack.domain.client.version.ModpackVersionDTO;
/*    */ import org.tlauncher.modpack.domain.client.version.VersionDTO;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ForgeModFilter
/*    */   extends VersionFilter
/*    */ {
/*    */   public ForgeModFilter(GameEntityDTO entityDTO, GameType gameType, ModpackDTO modpackDTO) {
/* 20 */     super(entityDTO, gameType, modpackDTO);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isProper(VersionDTO versionDTO) {
/* 25 */     if (this.gameType != GameType.MOD && versionDTO instanceof ModVersionDTO) {
/* 26 */       ModVersionDTO dto = (ModVersionDTO)versionDTO;
/* 27 */       ForgeComparator f = new ForgeComparator();
/* 28 */       String version = ((ModpackVersionDTO)this.modpackDTO.getVersion()).getForgeVersion();
/* 29 */       if (dto.getDownForge() != null && f.compare(dto.getDownForge(), version) > 0)
/* 30 */         return false; 
/* 31 */       if (dto.getUpForge() != null && f.compare(version, dto.getUpForge()) == 1) {
/* 32 */         return false;
/*    */       }
/*    */     } 
/* 35 */     return true;
/*    */   }
/*    */   
/*    */   class ForgeComparator
/*    */     implements Comparator<String> {
/*    */     public int compare(String o1, String o2) {
/* 41 */       String[] versions1 = o1.split("-")[1].split("\\.");
/* 42 */       String[] versions2 = o2.split("-")[1].split("\\.");
/* 43 */       for (int i = 0; i < versions1.length; i++) {
/* 44 */         int res = versions1[i].compareTo(versions2[i]);
/* 45 */         if (res != 0)
/* 46 */           return res; 
/*    */       } 
/* 48 */       return 0;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\modpack\filter\version\ForgeModFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */