/*    */ package org.tlauncher.tlauncher.ui.modpack.filter;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.launcher.versions.CompleteVersion;
/*    */ import org.tlauncher.modpack.domain.client.GameEntityDTO;
/*    */ import org.tlauncher.modpack.domain.client.ModpackDTO;
/*    */ import org.tlauncher.modpack.domain.client.share.GameType;
/*    */ import org.tlauncher.modpack.domain.client.version.ModpackVersionDTO;
/*    */ import org.tlauncher.modpack.domain.client.version.VersionDTO;
/*    */ import org.tlauncher.tlauncher.modpack.ModpackUtil;
/*    */ import org.tlauncher.tlauncher.ui.modpack.filter.version.IncompatibleFilter;
/*    */ import org.tlauncher.tlauncher.ui.swing.box.ModpackComboBox;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class InstallGameEntityFilter
/*    */   extends BaseModpackFilter<GameEntityDTO>
/*    */ {
/*    */   private ModpackComboBox modpackComboBox;
/*    */   private GameType type;
/*    */   
/*    */   public InstallGameEntityFilter(ModpackComboBox modpackComboBox, GameType type, Filter<GameEntityDTO>... filters) {
/* 26 */     super(filters);
/* 27 */     this.modpackComboBox = modpackComboBox;
/* 28 */     this.type = type;
/* 29 */     if (modpackComboBox.getSelectedIndex() > 0 && type != GameType.MODPACK) {
/* 30 */       ModpackDTO m = ((CompleteVersion)modpackComboBox.getSelectedItem()).getModpack();
/* 31 */       ListGameEntityImpl subElementVersionFilter = new ListGameEntityImpl(type, m);
/* 32 */       Set<Long> incompatible = new HashSet<>();
/* 33 */       ModpackUtil.extractIncompatible((GameEntityDTO)m, incompatible);
/* 34 */       GameType.getSubEntities().forEach(t -> ((List)((ModpackVersionDTO)m.getVersion()).getByType(t).stream().collect(Collectors.toList())).forEach(()));
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 39 */       IncompatibleFilter filter = new IncompatibleFilter(incompatible, ModpackUtil.getAllModpackIds(m));
/* 40 */       addFilter((Filter<GameEntityDTO>)filter);
/* 41 */       addFilter(subElementVersionFilter);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isProper(GameEntityDTO entity) {
/* 47 */     if (!super.isProper(entity)) {
/* 48 */       return false;
/*    */     }
/* 50 */     if (this.type != GameType.MODPACK && this.modpackComboBox.getSelectedIndex() > 0) {
/*    */       
/* 52 */       BaseModpackFilter<VersionDTO> filter = BaseModpackFilter.getBaseModpackStandardFilters(entity, this.type, this.modpackComboBox);
/* 53 */       return (filter.findAll(entity.getVersions()).size() != 0);
/*    */     } 
/* 55 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\modpack\filter\InstallGameEntityFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */