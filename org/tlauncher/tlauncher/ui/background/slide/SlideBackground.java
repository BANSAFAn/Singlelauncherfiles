/*     */ package org.tlauncher.tlauncher.ui.background.slide;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Image;
/*     */ import java.awt.event.ComponentEvent;
/*     */ import java.awt.event.ComponentListener;
/*     */ import org.tlauncher.tlauncher.ui.background.Background;
/*     */ import org.tlauncher.tlauncher.ui.background.BackgroundHolder;
/*     */ import org.tlauncher.tlauncher.ui.swing.extended.ExtendedComponentAdapter;
/*     */ 
/*     */ public class SlideBackground
/*     */   extends Background
/*     */ {
/*     */   private static final long serialVersionUID = -4479685866688951989L;
/*     */   private final SlideBackgroundThread thread;
/*     */   final BackgroundHolder holder;
/*     */   final ExtendedComponentAdapter listener;
/*     */   private Image oImage;
/*     */   private int oImageWidth;
/*     */   private int oImageHeight;
/*     */   private Image vImage;
/*     */   private int vImageWidth;
/*     */   private int vImageHeight;
/*     */   
/*     */   public SlideBackground(BackgroundHolder holder) {
/*  28 */     super(holder, Color.black);
/*     */     
/*  30 */     this.holder = holder;
/*  31 */     this.thread = new SlideBackgroundThread(this);
/*     */     
/*  33 */     this.thread.setSlide(this.thread.defaultSlide, false);
/*  34 */     this.thread.refreshSlide(false);
/*     */     
/*  36 */     this.listener = new ExtendedComponentAdapter((Component)this, 1000)
/*     */       {
/*     */         public void onComponentResized(ComponentEvent e) {
/*  39 */           SlideBackground.this.updateImage();
/*  40 */           SlideBackground.this.repaint();
/*     */         }
/*     */       };
/*  43 */     addComponentListener((ComponentListener)this.listener);
/*     */   }
/*     */   
/*     */   public SlideBackgroundThread getThread() {
/*  47 */     return this.thread;
/*     */   }
/*     */   
/*     */   public Image getImage() {
/*  51 */     return this.oImage;
/*     */   }
/*     */   
/*     */   public void setImage(Image image) {
/*  55 */     if (image == null) {
/*  56 */       throw new NullPointerException();
/*     */     }
/*  58 */     this.oImage = image;
/*  59 */     this.oImageWidth = image.getWidth(null);
/*  60 */     this.oImageHeight = image.getHeight(null);
/*     */     
/*  62 */     updateImage();
/*     */   }
/*     */   
/*     */   private void updateImage() {
/*  66 */     double width, height, windowWidth = getWidth(), windowHeight = getHeight();
/*     */     
/*  68 */     double ratio = Math.min(this.oImageWidth / windowWidth, this.oImageHeight / windowHeight);
/*     */ 
/*     */     
/*  71 */     if (ratio < 1.0D) {
/*     */ 
/*     */       
/*  74 */       width = this.oImageWidth;
/*  75 */       height = this.oImageHeight;
/*     */     } else {
/*  77 */       width = this.oImageWidth / ratio;
/*  78 */       height = this.oImageHeight / ratio;
/*     */     } 
/*     */     
/*  81 */     this.vImageWidth = (int)width;
/*  82 */     this.vImageHeight = (int)height;
/*     */     
/*  84 */     if (this.vImageWidth == 0 || this.vImageHeight == 0) {
/*  85 */       this.vImage = null;
/*     */     }
/*  87 */     else if (this.oImageWidth == this.vImageWidth && this.oImageHeight == this.vImageHeight) {
/*  88 */       this.vImage = this.oImage;
/*     */     } else {
/*  90 */       this.vImage = this.oImage.getScaledInstance(this.vImageWidth, this.vImageHeight, 4);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void paintBackground(Graphics g) {
/*  95 */     if (this.vImage == null) {
/*  96 */       updateImage();
/*     */     }
/*  98 */     if (this.vImage == null) {
/*     */       return;
/*     */     }
/*     */     
/* 102 */     double windowWidth = getWidth();
/* 103 */     double windowHeight = getHeight();
/*     */     
/* 105 */     double ratio = Math.min(this.vImageWidth / windowWidth, this.vImageHeight / windowHeight);
/* 106 */     double width = this.vImageWidth / ratio, height = this.vImageHeight / ratio;
/*     */ 
/*     */     
/* 109 */     double x = (windowWidth - width) / 2.0D;
/* 110 */     double y = (windowHeight - height) / 2.0D;
/*     */     
/* 112 */     g.drawImage(this.vImage, (int)x, (int)y, (int)width, (int)height, null);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\background\slide\SlideBackground.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */