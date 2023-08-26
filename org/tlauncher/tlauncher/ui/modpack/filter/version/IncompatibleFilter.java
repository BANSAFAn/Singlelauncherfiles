/*    */ package org.tlauncher.tlauncher.ui.modpack.filter.version;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import org.tlauncher.modpack.domain.client.GameEntityDTO;
/*    */ import org.tlauncher.modpack.domain.client.GameEntityDependencyDTO;
/*    */ import org.tlauncher.modpack.domain.client.share.DependencyType;
/*    */ import org.tlauncher.tlauncher.modpack.ModpackUtil;
/*    */ import org.tlauncher.tlauncher.ui.modpack.filter.GameEntityFilter;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IncompatibleFilter
/*    */   implements GameEntityFilter
/*    */ {
/*    */   private Set<Long> modpackIncompatible;
/*    */   private Set<Long> modapckIds;
/*    */   
/*    */   public IncompatibleFilter(Set<Long> modpackIncompatible, Set<Long> modapckIds) {
/* 24 */     this.modpackIncompatible = modpackIncompatible;
/* 25 */     this.modapckIds = modapckIds;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isProper(GameEntityDTO entity) {
/* 31 */     if (this.modpackIncompatible.contains(entity.getId()))
/* 32 */       return false; 
/* 33 */     if (entity.getDependencies() != null) {
/* 34 */       for (GameEntityDependencyDTO d : entity.getDependencies()) {
/* 35 */         if (d.getDependencyType() == DependencyType.REQUIRED && this.modpackIncompatible.contains(d.getGameEntityId())) {
/* 36 */           return false;
/*    */         }
/*    */       } 
/*    */     }
/* 40 */     Set<Long> set = new HashSet<>();
/* 41 */     ModpackUtil.extractIncompatible(entity, set);
/* 42 */     return Collections.disjoint(this.modapckIds, set);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\modpack\filter\version\IncompatibleFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */