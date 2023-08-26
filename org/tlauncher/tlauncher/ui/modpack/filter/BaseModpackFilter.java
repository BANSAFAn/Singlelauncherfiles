/*    */ package org.tlauncher.tlauncher.ui.modpack.filter;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import net.minecraft.launcher.versions.CompleteVersion;
/*    */ import org.tlauncher.modpack.domain.client.GameEntityDTO;
/*    */ import org.tlauncher.modpack.domain.client.ModpackDTO;
/*    */ import org.tlauncher.modpack.domain.client.share.GameType;
/*    */ import org.tlauncher.modpack.domain.client.version.VersionDTO;
/*    */ import org.tlauncher.tlauncher.ui.modpack.filter.version.GameVersionFilter;
/*    */ import org.tlauncher.tlauncher.ui.modpack.filter.version.TypeVersionFilter;
/*    */ import org.tlauncher.tlauncher.ui.swing.box.ModpackComboBox;
/*    */ import org.tlauncher.util.U;
/*    */ 
/*    */ public class BaseModpackFilter<T>
/*    */ {
/* 18 */   private final List<Filter<T>> filters = new ArrayList<>();
/*    */   
/*    */   public BaseModpackFilter(Filter<T>... filters) {
/* 21 */     this.filters.addAll(Arrays.asList(filters));
/*    */   }
/*    */   
/*    */   public boolean isProper(T entity) {
/*    */     try {
/* 26 */       for (Filter<T> filter : this.filters) {
/* 27 */         if (!filter.isProper(entity)) {
/* 28 */           return false;
/*    */         }
/*    */       } 
/* 31 */     } catch (NullPointerException t) {
/* 32 */       U.log(new Object[] { entity });
/* 33 */       throw t;
/*    */     } 
/* 35 */     return true;
/*    */   }
/*    */   
/*    */   public void addFilter(Filter<T> filter) {
/* 39 */     this.filters.add(filter);
/*    */   }
/*    */   
/*    */   public List<T> findAll(List<? extends T> gameEntities) {
/* 43 */     List<T> res = new ArrayList<>();
/* 44 */     for (T g : gameEntities) {
/* 45 */       if (isProper(g)) {
/* 46 */         res.add(g);
/*    */       }
/*    */     } 
/* 49 */     return res;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 54 */     return getClass().getName() + "{filters=" + this.filters + '}';
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static BaseModpackFilter<VersionDTO> getBaseModpackStandardFilters(GameEntityDTO entity, GameType type, ModpackComboBox modpackComboBox) {
/* 60 */     BaseModpackFilter<VersionDTO> filter = new BaseModpackFilter<>((Filter<VersionDTO>[])new Filter[0]);
/* 61 */     if (modpackComboBox.getSelectedIndex() > 0 && type != GameType.MODPACK) {
/* 62 */       ModpackDTO modpackDTO = ((CompleteVersion)modpackComboBox.getSelectedValue()).getModpack();
/* 63 */       return getBaseModpackStandardFilters(entity, type, modpackDTO);
/*    */     } 
/* 65 */     return filter;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static BaseModpackFilter<VersionDTO> getBaseModpackStandardFilters(GameEntityDTO entity, GameType type, ModpackDTO modpackDTO) {
/* 71 */     BaseModpackFilter<VersionDTO> filter = new BaseModpackFilter<>((Filter<VersionDTO>[])new Filter[0]);
/* 72 */     filter.addFilter((Filter<VersionDTO>)new GameVersionFilter(entity, type, modpackDTO));
/* 73 */     filter.addFilter((Filter<VersionDTO>)new TypeVersionFilter(entity, type, modpackDTO));
/* 74 */     return filter;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\modpack\filter\BaseModpackFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */