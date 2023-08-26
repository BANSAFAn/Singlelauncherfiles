/*     */ package org.tlauncher.tlauncher.ui.swing;
/*     */ import java.awt.AlphaComposite;
/*     */ import java.awt.Color;
/*     */ import java.awt.Composite;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Image;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import org.tlauncher.tlauncher.ui.images.ImageCache;
/*     */ import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
/*     */ import org.tlauncher.util.U;
/*     */ 
/*     */ public class ImagePanel extends ExtendedPanel {
/*  16 */   protected final Object animationLock = new Object();
/*     */   
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   public static final float DEFAULT_ACTIVE_OPACITY = 1.0F;
/*     */   public static final float DEFAULT_NON_ACTIVE_OPACITY = 0.75F;
/*     */   private Image originalImage;
/*     */   private Image image;
/*     */   private float activeOpacity;
/*     */   private float nonActiveOpacity;
/*     */   private boolean antiAlias;
/*     */   private int timeFrame;
/*     */   private float opacity;
/*     */   private boolean hover;
/*     */   private boolean shown;
/*     */   private boolean animating;
/*     */   
/*     */   public ImagePanel(String image, float activeOpacity, float nonActiveOpacity, boolean shown) {
/*  34 */     this(ImageCache.getImage(image), activeOpacity, nonActiveOpacity, shown);
/*     */   }
/*     */ 
/*     */   
/*     */   public ImagePanel(String image) {
/*  39 */     this(image, 1.0F, 0.75F, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public ImagePanel(Image image, float activeOpacity, float nonActiveOpacity, boolean shown) {
/*  44 */     setImage(image);
/*     */     
/*  46 */     setActiveOpacity(activeOpacity);
/*  47 */     setNonActiveOpacity(nonActiveOpacity);
/*     */     
/*  49 */     this.shown = shown;
/*  50 */     this.opacity = shown ? nonActiveOpacity : 0.0F;
/*  51 */     this.timeFrame = 10;
/*     */     
/*  53 */     setBackground(new Color(0, 0, 0, 0));
/*     */     
/*  55 */     addMouseListenerOriginally(new MouseAdapter()
/*     */         {
/*     */           public void mouseClicked(MouseEvent e) {
/*  58 */             ImagePanel.this.onClick();
/*     */           }
/*     */ 
/*     */           
/*     */           public void mouseEntered(MouseEvent e) {
/*  63 */             ImagePanel.this.onMouseEntered();
/*     */           }
/*     */ 
/*     */           
/*     */           public void mouseExited(MouseEvent e) {
/*  68 */             ImagePanel.this.onMouseExited();
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public void setImage(Image image, boolean resetSize) {
/*  74 */     synchronized (this.animationLock) {
/*  75 */       this.originalImage = image;
/*  76 */       this.image = image;
/*     */       
/*  78 */       if (resetSize && image != null)
/*  79 */         setSize(image.getWidth(null), image.getHeight(null)); 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void setImage(Image image) {
/*  84 */     setImage(image, true);
/*     */   }
/*     */   
/*     */   protected void setActiveOpacity(float opacity) {
/*  88 */     if (opacity > 1.0F || opacity < 0.0F) {
/*  89 */       throw new IllegalArgumentException("Invalid opacity! Condition: 0.0F <= opacity (got: " + opacity + ") <= 1.0F");
/*     */     }
/*     */ 
/*     */     
/*  93 */     this.activeOpacity = opacity;
/*     */   }
/*     */   
/*     */   protected void setNonActiveOpacity(float opacity) {
/*  97 */     if (opacity > 1.0F || opacity < 0.0F) {
/*  98 */       throw new IllegalArgumentException("Invalid opacity! Condition: 0.0F <= opacity (got: " + opacity + ") <= 1.0F");
/*     */     }
/*     */ 
/*     */     
/* 102 */     this.nonActiveOpacity = opacity;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void paintComponent(Graphics g0) {
/* 108 */     if (this.image == null) {
/*     */       return;
/*     */     }
/* 111 */     Graphics2D g = (Graphics2D)g0;
/* 112 */     Composite oldComp = g.getComposite();
/*     */     
/* 114 */     g.setComposite(AlphaComposite.getInstance(3, this.opacity));
/*     */     
/* 116 */     g.drawImage(this.image, 0, 0, getWidth(), getHeight(), null);
/*     */     
/* 118 */     g.setComposite(oldComp);
/*     */   }
/*     */ 
/*     */   
/*     */   public void show() {
/* 123 */     if (this.shown)
/*     */       return; 
/* 125 */     this.shown = true;
/*     */     
/* 127 */     synchronized (this.animationLock) {
/* 128 */       this.animating = true;
/* 129 */       setVisible(true);
/* 130 */       this.opacity = 0.0F;
/*     */       
/* 132 */       float selectedOpacity = this.hover ? this.activeOpacity : this.nonActiveOpacity;
/*     */       
/* 134 */       while (this.opacity < selectedOpacity) {
/* 135 */         this.opacity += 0.01F;
/* 136 */         if (this.opacity > selectedOpacity) {
/* 137 */           this.opacity = selectedOpacity;
/*     */         }
/* 139 */         repaint();
/* 140 */         U.sleepFor(this.timeFrame);
/*     */       } 
/*     */       
/* 143 */       this.animating = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void hide() {
/* 149 */     if (!this.shown) {
/*     */       return;
/*     */     }
/* 152 */     this.shown = false;
/*     */     
/* 154 */     synchronized (this.animationLock) {
/* 155 */       this.animating = true;
/*     */       
/* 157 */       while (this.opacity > 0.0F) {
/* 158 */         this.opacity -= 0.01F;
/*     */         
/* 160 */         if (this.opacity < 0.0F) {
/* 161 */           this.opacity = 0.0F;
/*     */         }
/* 163 */         repaint();
/* 164 */         U.sleepFor(this.timeFrame);
/*     */       } 
/*     */       
/* 167 */       setVisible(false);
/* 168 */       this.animating = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setPreferredSize() {
/* 173 */     if (this.image == null) {
/*     */       return;
/*     */     }
/* 176 */     setPreferredSize(new Dimension(this.image.getWidth(null), this.image.getHeight(null)));
/*     */   }
/*     */   
/*     */   protected boolean onClick() {
/* 180 */     return this.shown;
/*     */   }
/*     */   
/*     */   protected void onMouseEntered() {
/* 184 */     this.hover = true;
/*     */     
/* 186 */     if (this.animating || !this.shown) {
/*     */       return;
/*     */     }
/* 189 */     this.opacity = this.activeOpacity;
/* 190 */     repaint();
/*     */   }
/*     */   
/*     */   protected void onMouseExited() {
/* 194 */     this.hover = false;
/*     */     
/* 196 */     if (this.animating || !this.shown)
/*     */       return; 
/* 198 */     this.opacity = this.nonActiveOpacity;
/* 199 */     repaint();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\ImagePanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */