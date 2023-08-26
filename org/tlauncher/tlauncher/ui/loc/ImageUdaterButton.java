/*     */ package org.tlauncher.tlauncher.ui.loc;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Image;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import javax.swing.ButtonModel;
/*     */ import javax.swing.JComponent;
/*     */ import org.tlauncher.tlauncher.ui.swing.ImageButton;
/*     */ 
/*     */ public class ImageUdaterButton extends ImageButton {
/*     */   public final Color backroundColor;
/*     */   
/*     */   public ImageUdaterButton(Color color, String image) {
/*  18 */     super(image);
/*  19 */     this.defaultImage = image;
/*  20 */     this.backroundColor = color;
/*  21 */     setBackground(color);
/*     */   }
/*     */   public final String defaultImage; private Color modelPressedColor;
/*     */   public ImageUdaterButton(Color color) {
/*  25 */     this.backroundColor = color;
/*  26 */     this.defaultImage = null;
/*  27 */     setContentAreaFilled(false);
/*  28 */     setOpaque(true);
/*  29 */     setBackground(color);
/*     */   }
/*     */   
/*     */   public ImageUdaterButton(String image) {
/*  33 */     super(image);
/*  34 */     this.defaultImage = image;
/*  35 */     this.backroundColor = Color.BLACK;
/*     */   }
/*     */   
/*     */   public ImageUdaterButton(final Color color, final Color color1, final String s, final String s1) {
/*  39 */     this(color, s);
/*  40 */     addMouseListener(new MouseAdapter()
/*     */         {
/*     */           public void mouseEntered(MouseEvent e) {
/*  43 */             if (ImageUdaterButton.this.getModel().isEnabled()) {
/*  44 */               ImageUdaterButton.this.setImage(ImageUdaterButton.loadImage(s1));
/*  45 */               ImageUdaterButton.this.setBackground(color1);
/*     */             } 
/*     */           }
/*     */ 
/*     */           
/*     */           public void mouseExited(MouseEvent e) {
/*  51 */             ImageUdaterButton.this.setImage(ImageUdaterButton.loadImage(s));
/*  52 */             ImageUdaterButton.this.setBackground(color);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public ImageUdaterButton(final Color color, final Color color1, String s) {
/*  59 */     this(color, s);
/*  60 */     addMouseListener(new MouseAdapter()
/*     */         {
/*     */           public void mouseEntered(MouseEvent e) {
/*  63 */             if (ImageUdaterButton.this.getModel().isEnabled()) {
/*  64 */               ImageUdaterButton.this.setBackground(color1);
/*     */             }
/*     */           }
/*     */ 
/*     */           
/*     */           public void mouseExited(MouseEvent e) {
/*  70 */             ImageUdaterButton.this.setBackground(color);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void paintComponent(Graphics g) {
/*  80 */     Rectangle rec = getVisibleRect();
/*  81 */     g.setColor(getBackground());
/*     */     
/*  83 */     ButtonModel buttonModel = getModel();
/*  84 */     if (buttonModel.isPressed() && buttonModel.isEnabled() && 
/*  85 */       this.modelPressedColor != null) {
/*  86 */       g.setColor(this.modelPressedColor);
/*     */     }
/*  88 */     g.fillRect(rec.x, rec.y, rec.width, rec.height);
/*     */     
/*  90 */     ImageUdaterButton imageUdaterButton = this;
/*  91 */     if (this.image != null) {
/*  92 */       paintPicture(g, (JComponent)imageUdaterButton, rec);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void paintPicture(Graphics g, JComponent c, Rectangle rect) {
/*  99 */     Graphics2D g2d = (Graphics2D)g;
/*     */     
/* 101 */     int x = (getWidth() - this.image.getWidth(null)) / 2;
/* 102 */     int y = (getHeight() - this.image.getHeight(null)) / 2;
/* 103 */     g2d.drawImage(this.image, x, y, null);
/*     */   }
/*     */   
/*     */   public String getDefaultImage() {
/* 107 */     return this.defaultImage;
/*     */   }
/*     */   
/*     */   public Color getBackroundColor() {
/* 111 */     return this.backroundColor;
/*     */   }
/*     */   
/*     */   public void setModelPressedColor(Color modelPressedColor) {
/* 115 */     this.modelPressedColor = modelPressedColor;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\loc\ImageUdaterButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */