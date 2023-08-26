/*    */ package org.tlauncher.tlauncher.ui.background;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Component;
/*    */ import javax.swing.JComponent;
/*    */ import org.tlauncher.tlauncher.ui.MainPane;
/*    */ import org.tlauncher.tlauncher.ui.background.slide.SlideBackground;
/*    */ import org.tlauncher.tlauncher.ui.swing.extended.ExtendedLayeredPane;
/*    */ 
/*    */ public class BackgroundHolder
/*    */   extends ExtendedLayeredPane
/*    */ {
/*    */   private static final long serialVersionUID = 8722087129402330131L;
/*    */   public final MainPane pane;
/*    */   private Background currentBackground;
/*    */   public final BackgroundCover cover;
/*    */   public final SlideBackground SLIDE_BACKGROUND;
/*    */   
/*    */   public BackgroundHolder(MainPane parent) {
/* 20 */     super((JComponent)parent);
/*    */     
/* 22 */     this.pane = parent;
/* 23 */     this.cover = new BackgroundCover(this);
/*    */     
/* 25 */     this.SLIDE_BACKGROUND = new SlideBackground(this);
/*    */     
/* 27 */     add((Component)this.cover, Integer.valueOf(2147483647));
/*    */   }
/*    */   
/*    */   public Background getBackgroundPane() {
/* 31 */     return this.currentBackground;
/*    */   }
/*    */   
/*    */   public void setBackground(Background background, boolean animate) {
/* 35 */     if (background == null) {
/* 36 */       throw new NullPointerException();
/*    */     }
/* 38 */     Color coverColor = background.getCoverColor();
/* 39 */     if (coverColor == null) {
/* 40 */       coverColor = Color.black;
/*    */     }
/* 42 */     this.cover.setColor(coverColor, animate);
/*    */     
/* 44 */     this.cover.makeCover(animate);
/*    */     
/* 46 */     if (this.currentBackground != null)
/* 47 */       remove((Component)this.currentBackground); 
/* 48 */     this.currentBackground = background;
/* 49 */     add((Component)this.currentBackground);
/*    */     
/* 51 */     this.cover.removeCover(animate);
/*    */   }
/*    */   
/*    */   public void showBackground() {
/* 55 */     this.cover.removeCover();
/*    */   }
/*    */   
/*    */   public void hideBackground() {
/* 59 */     this.cover.makeCover();
/*    */   }
/*    */   
/*    */   public void startBackground() {
/* 63 */     if (this.currentBackground == null) {
/*    */       return;
/*    */     }
/* 66 */     if (this.currentBackground instanceof AnimatedBackground)
/* 67 */       ((AnimatedBackground)this.currentBackground).startBackground(); 
/*    */   }
/*    */   
/*    */   public void suspendBackground() {
/* 71 */     if (this.currentBackground == null) {
/*    */       return;
/*    */     }
/* 74 */     if (this.currentBackground instanceof AnimatedBackground)
/* 75 */       ((AnimatedBackground)this.currentBackground).suspendBackground(); 
/*    */   }
/*    */   
/*    */   public void stopBackground() {
/* 79 */     if (this.currentBackground == null) {
/*    */       return;
/*    */     }
/* 82 */     if (this.currentBackground instanceof AnimatedBackground)
/* 83 */       ((AnimatedBackground)this.currentBackground).stopBackground(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\background\BackgroundHolder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */