/*    */ package org.tlauncher.tlauncher.ui.modpack.filter.version;
/*    */ 
/*    */ import org.tlauncher.modpack.domain.client.GameEntityDTO;
/*    */ import org.tlauncher.modpack.domain.client.ModpackDTO;
/*    */ import org.tlauncher.modpack.domain.client.share.GameType;
/*    */ import org.tlauncher.modpack.domain.client.version.VersionDTO;
/*    */ import org.tlauncher.tlauncher.ui.modpack.filter.Filter;
/*    */ 
/*    */ public abstract class VersionFilter
/*    */   implements Filter<VersionDTO> {
/*    */   protected GameEntityDTO entityDTO;
/*    */   protected GameType gameType;
/*    */   
/*    */   public VersionFilter(GameEntityDTO entityDTO, GameType gameType, ModpackDTO modpackDTO) {
/* 15 */     this.entityDTO = entityDTO;
/* 16 */     this.gameType = gameType;
/* 17 */     this.modpackDTO = modpackDTO;
/*    */   }
/*    */   
/*    */   protected ModpackDTO modpackDTO;
/*    */   
/*    */   public abstract boolean isProper(VersionDTO paramVersionDTO);
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\modpack\filter\version\VersionFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */