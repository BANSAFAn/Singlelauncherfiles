/*    */ package org.tlauncher.tlauncher.ui.modpack.filter.version;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import org.tlauncher.modpack.domain.client.GameEntityDTO;
/*    */ import org.tlauncher.modpack.domain.client.GameVersionDTO;
/*    */ import org.tlauncher.modpack.domain.client.ModpackDTO;
/*    */ import org.tlauncher.modpack.domain.client.share.GameType;
/*    */ import org.tlauncher.modpack.domain.client.version.ModpackVersionDTO;
/*    */ import org.tlauncher.modpack.domain.client.version.VersionDTO;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GameVersionFilter
/*    */   extends VersionFilter
/*    */ {
/*    */   public GameVersionFilter(GameEntityDTO entityDTO, GameType gameType, ModpackDTO modpackDTO) {
/* 21 */     super(entityDTO, gameType, modpackDTO);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isProper(VersionDTO versionDTO) {
/* 26 */     GameVersionDTO gvdto = ((ModpackVersionDTO)this.modpackDTO.getVersion()).getGameVersionDTO();
/* 27 */     if (Objects.isNull(gvdto) || versionDTO.getGameVersionsDTO().isEmpty()) {
/* 28 */       return true;
/*    */     }
/* 30 */     return versionDTO.getGameVersionsDTO().stream().filter(e -> e.getId().equals(gvdto.getId())).findAny().isPresent();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\modpack\filter\version\GameVersionFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */