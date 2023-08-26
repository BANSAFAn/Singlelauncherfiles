/*    */ package org.tlauncher.util;
/*    */ 
/*    */ public enum Direction {
/*  4 */   TOP_LEFT, TOP, TOP_RIGHT,
/*  5 */   CENTER_LEFT, CENTER, CENTER_RIGHT,
/*  6 */   BOTTOM_LEFT, BOTTOM, BOTTOM_RIGHT;
/*    */   
/*    */   private final String lower;
/*    */   
/*    */   Direction() {
/* 11 */     this.lower = name().toLowerCase();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 17 */     return this.lower;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlaunche\\util\Direction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */