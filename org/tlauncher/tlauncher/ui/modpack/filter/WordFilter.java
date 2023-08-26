/*    */ package org.tlauncher.tlauncher.ui.modpack.filter;
/*    */ 
/*    */ import org.tlauncher.modpack.domain.client.GameEntityDTO;
/*    */ 
/*    */ public class WordFilter extends NameFilter {
/*    */   private final String word;
/*    */   
/*    */   public WordFilter(String word) {
/*  9 */     super(word);
/* 10 */     this.word = word;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isProper(GameEntityDTO gameEntity) {
/* 16 */     if (super.isProper(gameEntity) || gameEntity
/* 17 */       .getShortDescription().toLowerCase().contains(this.word.toLowerCase())) {
/* 18 */       return true;
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 25 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\modpack\filter\WordFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */