/*     */ package org.tlauncher.tlauncher.ui.background;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics;
/*     */ import org.tlauncher.tlauncher.ui.swing.ResizeableComponent;
/*     */ import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
/*     */ import org.tlauncher.util.U;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BackgroundCover
/*     */   extends ExtendedPanel
/*     */   implements ResizeableComponent
/*     */ {
/*     */   private static final long serialVersionUID = -1801217638400760969L;
/*     */   private static final double opacityStep = 0.01D;
/*     */   private static final int timeFrame = 5;
/*     */   private final BackgroundHolder parent;
/*     */   private final Object animationLock;
/*     */   private double opacity;
/*     */   private Color opacityColor;
/*     */   private Color color;
/*     */   
/*     */   BackgroundCover(BackgroundHolder parent, Color opacityColor, double opacity) {
/*  26 */     if (parent == null) {
/*  27 */       throw new NullPointerException();
/*     */     }
/*  29 */     this.parent = parent;
/*     */     
/*  31 */     setColor(opacityColor, false);
/*  32 */     setBgOpacity(opacity, false);
/*     */     
/*  34 */     this.animationLock = new Object();
/*     */   }
/*     */   
/*     */   BackgroundCover(BackgroundHolder parent) {
/*  38 */     this(parent, Color.white, 0.0D);
/*     */   }
/*     */   
/*     */   public void makeCover(boolean animate) {
/*  42 */     synchronized (this.animationLock) {
/*  43 */       if (animate)
/*  44 */         while (this.opacity < 1.0D) {
/*  45 */           setBgOpacity(this.opacity + 0.01D, true);
/*  46 */           U.sleepFor(5L);
/*     */         }  
/*  48 */       setBgOpacity(1.0D, true);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void makeCover() {
/*  53 */     makeCover(true);
/*     */   }
/*     */   
/*     */   public void removeCover(boolean animate) {
/*  57 */     synchronized (this.animationLock) {
/*  58 */       if (animate)
/*  59 */         while (this.opacity > 0.0D) {
/*  60 */           setBgOpacity(this.opacity - 0.01D, true);
/*  61 */           U.sleepFor(5L);
/*     */         }  
/*  63 */       setBgOpacity(0.0D, true);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void removeCover() {
/*  68 */     removeCover(true);
/*     */   }
/*     */   
/*     */   public boolean isCovered() {
/*  72 */     return (this.opacity == 1.0D);
/*     */   }
/*     */   
/*     */   public void toggleCover(boolean animate) {
/*  76 */     if (isCovered()) {
/*  77 */       removeCover(animate);
/*     */     } else {
/*  79 */       makeCover(animate);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void paint(Graphics g) {
/*  84 */     g.setColor(this.opacityColor);
/*  85 */     g.fillRect(0, 0, getWidth(), getHeight());
/*     */   }
/*     */   
/*     */   public double getBgOpacity() {
/*  89 */     return this.opacity;
/*     */   }
/*     */   
/*     */   public void setBgOpacity(double opacity, boolean repaint) {
/*  93 */     if (opacity < 0.0D) {
/*  94 */       opacity = 0.0D;
/*  95 */     } else if (opacity > 1.0D) {
/*  96 */       opacity = 1.0D;
/*     */     } 
/*  98 */     this.opacity = opacity;
/*  99 */     this
/* 100 */       .opacityColor = new Color(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), (int)(255.0D * opacity));
/*     */     
/* 102 */     if (repaint)
/* 103 */       repaint(); 
/*     */   }
/*     */   
/*     */   public Color getColor() {
/* 107 */     return this.color;
/*     */   }
/*     */   
/*     */   public void setColor(Color color, boolean repaint) {
/* 111 */     if (color == null) {
/* 112 */       throw new NullPointerException();
/*     */     }
/* 114 */     this.color = color;
/*     */     
/* 116 */     if (repaint) {
/* 117 */       repaint();
/*     */     }
/*     */   }
/*     */   
/*     */   public void onResize() {
/* 122 */     setSize(this.parent.getWidth(), this.parent.getHeight());
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\background\BackgroundCover.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */