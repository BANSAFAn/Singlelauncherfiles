/*    */ package org.tlauncher.tlauncher.controller;
/*    */ 
/*    */ import com.google.inject.Inject;
/*    */ import java.util.Comparator;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.launcher.versions.CompleteVersion;
/*    */ import org.tlauncher.modpack.domain.client.GameEntityDTO;
/*    */ import org.tlauncher.modpack.domain.client.GameVersionDTO;
/*    */ import org.tlauncher.modpack.domain.client.ModDTO;
/*    */ import org.tlauncher.modpack.domain.client.share.ForgeStringComparator;
/*    */ import org.tlauncher.modpack.domain.client.share.GameType;
/*    */ import org.tlauncher.modpack.domain.client.share.NameIdDTO;
/*    */ import org.tlauncher.modpack.domain.client.version.ModpackVersionDTO;
/*    */ import org.tlauncher.tlauncher.managers.ModpackManager;
/*    */ import org.tlauncher.tlauncher.modpack.ModpackUtil;
/*    */ import org.tlauncher.util.U;
/*    */ 
/*    */ public class ModpackConfig
/*    */ {
/*    */   @Inject
/*    */   private ModpackManager manager;
/*    */   
/*    */   public GameVersionDTO findGameVersion(String currentGameVersion) {
/* 24 */     for (GameVersionDTO dto : this.manager.getInfoMod().getGameVersions()) {
/* 25 */       if (dto.getName().equalsIgnoreCase(currentGameVersion) && dto.getForgeVersions().size() > 0) {
/* 26 */         dto.getForgeVersions().sort((Comparator)new ForgeStringComparator());
/* 27 */         return dto;
/*    */       } 
/*    */     } 
/* 30 */     return null;
/*    */   }
/*    */   
/*    */   public void save(CompleteVersion version, String modpackName, boolean tlSkinCapeModBox, NameIdDTO ver) {
/* 34 */     if (!version.getID().equals(modpackName)) {
/* 35 */       this.manager.renameModpack(version, modpackName);
/*    */     } else {
/* 37 */       this.manager.resaveVersion(version);
/*    */     } 
/* 39 */     ModpackVersionDTO mv = (ModpackVersionDTO)version.getModpack().getVersion();
/* 40 */     if (ModpackUtil.useSkinMod(version) && !tlSkinCapeModBox) {
/* 41 */       ModDTO m = new ModDTO();
/* 42 */       m.setId(ModDTO.TL_SKIN_CAPE_ID);
/* 43 */       this.manager.removeEntity((GameEntityDTO)m, m.getVersion(), GameType.MOD, true);
/* 44 */     } else if (!ModpackUtil.useSkinMod(version) && tlSkinCapeModBox) {
/* 45 */       this.manager.installTLSkinCapeMod(mv);
/*    */     } 
/* 47 */     if (Objects.nonNull(ver) && !Objects.equals(ver, mv.getMinecraftVersionName())) {
/*    */       try {
/* 49 */         this.manager.getGameVersion(mv);
/* 50 */         CompleteVersion v = this.manager.getCompleteVersionByMinecraftVersionTypeAndId(mv.getMinecraftVersionTypes().stream().findFirst().get(), ver);
/* 51 */         v.setSkinVersion(version.isSkinVersion());
/* 52 */         v.setID(version.getID());
/* 53 */         v.setModpackDTO(version.getModpack());
/* 54 */         ((ModpackVersionDTO)v.getModpack().getVersion()).setMinecraftVersionName(ver);
/* 55 */         this.manager.resaveVersionWithNewForge(v);
/* 56 */       } catch (Exception e) {
/* 57 */         U.log(new Object[] { e });
/*    */       } 
/*    */     }
/*    */   }
/*    */   
/*    */   public void open(CompleteVersion version) {
/* 63 */     this.manager.openModpackFolder(version);
/*    */   }
/*    */   
/*    */   public void remove(CompleteVersion version) {
/* 67 */     this.manager.removeEntity((GameEntityDTO)version.getModpack(), version.getModpack().getVersion(), GameType.MODPACK, false);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\controller\ModpackConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */