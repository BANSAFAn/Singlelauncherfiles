/*    */ package org.tlauncher.tlauncher.ui.modpack.filter;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import org.tlauncher.modpack.domain.client.GameEntityDTO;
/*    */ import org.tlauncher.modpack.domain.client.ModpackDTO;
/*    */ import org.tlauncher.modpack.domain.client.share.GameType;
/*    */ import org.tlauncher.modpack.domain.client.version.VersionDTO;
/*    */ 
/*    */ public class ListGameEntityImpl
/*    */   implements GameEntityFilter
/*    */ {
/*    */   private GameType gameType;
/*    */   private ModpackDTO modpackDTO;
/*    */   
/*    */   public ListGameEntityImpl(GameType gameType, ModpackDTO modpackDTO) {
/* 16 */     this.gameType = gameType;
/* 17 */     this.modpackDTO = modpackDTO;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isProper(GameEntityDTO gameEntity) {
/* 23 */     for (VersionDTO v : gameEntity.getVersions()) {
/* 24 */       if (v.getGameVersions() != null && this.modpackDTO.getVersion().getGameVersions() != null && 
/* 25 */         Collections.disjoint(v.getGameVersions(), this.modpackDTO.getVersion().getGameVersions()))
/* 26 */         return true; 
/*    */     } 
/* 28 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\modpack\filter\ListGameEntityImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */