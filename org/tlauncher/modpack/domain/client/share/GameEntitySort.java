/*    */ package org.tlauncher.modpack.domain.client.share;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum GameEntitySort
/*    */ {
/*  7 */   POPULAR("popularInstalled", "DESC"), NAME("name", "ASC"), TOTAL_DOWNLOAD("totalInstalled", "DESC"),
/*  8 */   UPDATE("updated", "DESC"),
/*  9 */   VERSION_UPDATE(
/* 10 */     "updateDate", "DESC"), FAVORITE("updated", "DESC"),
/* 11 */   LIKE(
/* 12 */     "likeSummary", "DESC"); public String getField() {
/*    */     return this.field;
/*    */   } GameEntitySort(String field, String direction) {
/* 15 */     this.field = field;
/* 16 */     this.direction = direction;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDirection() {
/*    */     return this.direction;
/*    */   }
/*    */   
/*    */   public static GameEntitySort[] getClientSortList() {
/* 25 */     return new GameEntitySort[] { POPULAR, NAME, TOTAL_DOWNLOAD, UPDATE };
/*    */   }
/*    */   
/*    */   private String field;
/*    */   private String direction;
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\modpack\domain\client\share\GameEntitySort.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */