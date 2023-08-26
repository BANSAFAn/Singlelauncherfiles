/*   */ package org.tlauncher.tlauncher.ui.background;
/*   */ 
/*   */ import java.awt.Color;
/*   */ 
/*   */ public abstract class AnimatedBackground extends Background {
/*   */   private static final long serialVersionUID = -7203733710324519015L;
/*   */   
/*   */   public AnimatedBackground(BackgroundHolder holder, Color coverColor) {
/* 9 */     super(holder, coverColor);
/*   */   }
/*   */   
/*   */   public abstract void startBackground();
/*   */   
/*   */   public abstract void stopBackground();
/*   */   
/*   */   public abstract void suspendBackground();
/*   */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\background\AnimatedBackground.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */