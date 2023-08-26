/*    */ package org.tlauncher.modpack.comparator;
/*    */ 
/*    */ import java.util.Comparator;
/*    */ import org.tlauncher.modpack.domain.client.GameVersionDTO;
/*    */ import org.tlauncher.modpack.domain.client.share.ForgeStringComparator;
/*    */ 
/*    */ public class GameVersionDTOComparator
/*    */   implements Comparator<GameVersionDTO> {
/*  9 */   private ForgeStringComparator f = new ForgeStringComparator();
/*    */ 
/*    */   
/*    */   public int compare(GameVersionDTO o1, GameVersionDTO o2) {
/* 13 */     return this.f.compare(o1.getName(), o2.getName());
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\modpack\comparator\GameVersionDTOComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */