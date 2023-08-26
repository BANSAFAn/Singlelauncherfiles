/*    */ package org.tlauncher.tlauncher.ui.swing.util;
/*    */ 
/*    */ 
/*    */ public enum Orientation
/*    */ {
/*  6 */   TOP(1), LEFT(2), BOTTOM(3),
/*  7 */   RIGHT(4), CENTER(0);
/*    */   
/*    */   private final int swingAlias;
/*    */   
/*    */   Orientation(int swingAlias) {
/* 12 */     this.swingAlias = swingAlias;
/*    */   }
/*    */   
/*    */   public int getSwingAlias() {
/* 16 */     return this.swingAlias;
/*    */   }
/*    */   
/*    */   public static Orientation fromSwingConstant(int orientation) {
/* 20 */     for (Orientation current : values()) {
/* 21 */       if (orientation == current.getSwingAlias())
/* 22 */         return current; 
/* 23 */     }  return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swin\\util\Orientation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */