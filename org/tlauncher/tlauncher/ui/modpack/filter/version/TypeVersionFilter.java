/*    */ package org.tlauncher.tlauncher.ui.modpack.filter.version;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import org.tlauncher.modpack.domain.client.GameEntityDTO;
/*    */ import org.tlauncher.modpack.domain.client.ModpackDTO;
/*    */ import org.tlauncher.modpack.domain.client.share.GameType;
/*    */ import org.tlauncher.modpack.domain.client.share.NameIdDTO;
/*    */ import org.tlauncher.modpack.domain.client.version.VersionDTO;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TypeVersionFilter
/*    */   extends VersionFilter
/*    */ {
/*    */   public TypeVersionFilter(GameEntityDTO entityDTO, GameType gameType, ModpackDTO modpackDTO) {
/* 19 */     super(entityDTO, gameType, modpackDTO);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isProper(VersionDTO versionDTO) {
/* 24 */     if (GameType.MOD.equals(this.gameType)) {
/* 25 */       NameIdDTO nameId = this.modpackDTO.getVersion().findFirstMinecraftVersionType();
/* 26 */       List<NameIdDTO> versionTypes = versionDTO.getMinecraftVersionTypes();
/* 27 */       if (Objects.isNull(nameId) || versionTypes.isEmpty()) {
/* 28 */         return true;
/*    */       }
/* 30 */       return versionTypes.contains(nameId);
/*    */     } 
/* 32 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\modpack\filter\version\TypeVersionFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */