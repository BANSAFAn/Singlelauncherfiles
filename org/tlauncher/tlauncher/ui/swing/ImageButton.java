/*     */ package org.tlauncher.tlauncher.ui.swing;
/*     */ 
/*     */ import java.awt.AlphaComposite;
/*     */ import java.awt.Composite;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Image;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.event.KeyListener;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseListener;
/*     */ import java.awt.image.ImageObserver;
/*     */ import org.tlauncher.tlauncher.ui.images.ImageCache;
/*     */ import org.tlauncher.tlauncher.ui.swing.extended.ExtendedButton;
/*     */ 
/*     */ public class ImageButton
/*     */   extends ExtendedButton {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected Image image;
/*  22 */   protected ImageRotation rotation = ImageRotation.CENTER;
/*  23 */   private int margin = 4;
/*     */   
/*     */   private boolean pressed;
/*     */   
/*     */   protected ImageButton() {
/*  28 */     initListeners();
/*     */   }
/*     */ 
/*     */   
/*     */   public ImageButton(String label, Image image, ImageRotation rotation, int margin) {
/*  33 */     super(label);
/*     */     
/*  35 */     this.image = image;
/*  36 */     this.rotation = rotation;
/*  37 */     this.margin = margin;
/*     */     
/*  39 */     initImage();
/*  40 */     initListeners();
/*     */   }
/*     */   
/*     */   public ImageButton(String label, Image image, ImageRotation rotation) {
/*  44 */     this(label, image, rotation, 4);
/*     */   }
/*     */   
/*     */   public ImageButton(String label, Image image) {
/*  48 */     this(label, image, ImageRotation.CENTER);
/*     */   }
/*     */   
/*     */   public ImageButton(Image image) {
/*  52 */     this((String)null, image);
/*     */   }
/*     */   
/*     */   public ImageButton(String imagepath) {
/*  56 */     this((String)null, loadImage(imagepath));
/*     */   }
/*     */ 
/*     */   
/*     */   public ImageButton(String label, String imagepath, ImageRotation rotation, int margin) {
/*  61 */     this(label, loadImage(imagepath), rotation, margin);
/*     */   }
/*     */   
/*     */   public ImageButton(String label, String imagepath, ImageRotation rotation) {
/*  65 */     this(label, loadImage(imagepath), rotation);
/*     */   }
/*     */   
/*     */   public ImageButton(String label, String imagepath) {
/*  69 */     this(label, loadImage(imagepath));
/*     */   }
/*     */   
/*     */   public Image getImage() {
/*  73 */     return this.image;
/*     */   }
/*     */   
/*     */   public void setImage(Image image) {
/*  77 */     this.image = image;
/*     */     
/*  79 */     initImage();
/*  80 */     repaint();
/*     */   }
/*     */   
/*     */   public ImageRotation getRotation() {
/*  84 */     return this.rotation;
/*     */   }
/*     */   
/*     */   public int getImageMargin() {
/*  88 */     return this.margin;
/*     */   }
/*     */ 
/*     */   
/*     */   public void update(Graphics g) {
/*  93 */     super.update(g);
/*  94 */     paint(g);
/*     */   }
/*     */ 
/*     */   
/*     */   public void paintComponent(Graphics g0) {
/*     */     int twidth;
/* 100 */     if (this.image == null) {
/*     */       return;
/*     */     }
/* 103 */     Graphics2D g = (Graphics2D)g0;
/*     */     
/* 105 */     String text = getText();
/* 106 */     boolean drawtext = (text != null && text.length() > 0);
/* 107 */     FontMetrics fm = g.getFontMetrics();
/*     */     
/* 109 */     float opacity = isEnabled() ? 1.0F : 0.5F;
/* 110 */     int width = getWidth(), height = getHeight(), rmargin = this.margin;
/* 111 */     int offset = this.pressed ? 1 : 0;
/* 112 */     int iwidth = this.image.getWidth(null), iheight = this.image.getHeight(null);
/* 113 */     int ix = 0, iy = height / 2 - iheight / 2;
/*     */     
/* 115 */     if (drawtext) {
/* 116 */       twidth = fm.stringWidth(text);
/*     */     } else {
/* 118 */       twidth = rmargin = 0;
/*     */     } 
/* 120 */     switch (this.rotation) {
/*     */       case LEFT:
/* 122 */         ix = width / 2 - twidth / 2 - iwidth - rmargin;
/*     */         break;
/*     */       case CENTER:
/* 125 */         ix = width / 2 - iwidth / 2;
/*     */         break;
/*     */       case RIGHT:
/* 128 */         ix = width / 2 + twidth / 2 + rmargin;
/*     */         break;
/*     */       default:
/* 131 */         throw new IllegalStateException("Unknown rotation!");
/*     */     } 
/* 133 */     Composite c = g.getComposite();
/* 134 */     g.setComposite(AlphaComposite.getInstance(3, opacity));
/*     */     
/* 136 */     g.drawImage(this.image, ix + offset, iy + offset, (ImageObserver)null);
/* 137 */     g.setComposite(c);
/*     */     
/* 139 */     this.pressed = false;
/*     */   }
/*     */   
/*     */   protected static Image loadImage(String path) {
/* 143 */     return ImageCache.getImage(path);
/*     */   }
/*     */   
/*     */   protected void initImage() {
/* 147 */     if (this.image == null) {
/*     */       return;
/*     */     }
/* 150 */     setPreferredSize(new Dimension(this.image.getWidth(null) + 10, this.image
/* 151 */           .getHeight(null) + 10));
/*     */   }
/*     */   
/*     */   private void initListeners() {
/* 155 */     initImage();
/*     */     
/* 157 */     addMouseListener(new MouseListener()
/*     */         {
/*     */           public void mouseClicked(MouseEvent e) {}
/*     */ 
/*     */ 
/*     */           
/*     */           public void mouseEntered(MouseEvent e) {}
/*     */ 
/*     */ 
/*     */           
/*     */           public void mouseExited(MouseEvent e) {}
/*     */ 
/*     */ 
/*     */           
/*     */           public void mousePressed(MouseEvent e) {
/* 172 */             ImageButton.this.pressed = true;
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public void mouseReleased(MouseEvent e) {}
/*     */         });
/* 180 */     addKeyListener(new KeyListener()
/*     */         {
/*     */           public void keyPressed(KeyEvent e) {
/* 183 */             if (e.getKeyCode() != 32)
/*     */               return; 
/* 185 */             ImageButton.this.pressed = true;
/*     */           }
/*     */ 
/*     */           
/*     */           public void keyReleased(KeyEvent e) {
/* 190 */             ImageButton.this.pressed = false;
/*     */           }
/*     */ 
/*     */           
/*     */           public void keyTyped(KeyEvent e) {}
/*     */         });
/*     */   }
/*     */   
/*     */   public enum ImageRotation
/*     */   {
/* 200 */     LEFT, CENTER, RIGHT;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\ImageButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */