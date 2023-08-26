/*     */ package org.tlauncher.tlauncher.ui.loc;
/*     */ import java.awt.Color;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Image;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import javax.swing.ButtonModel;
/*     */ import javax.swing.JComponent;
/*     */ import org.tlauncher.tlauncher.ui.images.ImageCache;
/*     */ 
/*     */ public class UpdaterFullButton extends UpdaterButton {
/*     */   private static final long serialVersionUID = 992760417140310208L;
/*     */   protected Color unEnableColor;
/*     */   
/*     */   public UpdaterFullButton(Color color, String value, String image) {
/*  20 */     super(color, value);
/*  21 */     this.backgroundColor = color;
/*  22 */     this.image = ImageCache.getImage(image);
/*  23 */     setForeground(Color.WHITE);
/*  24 */     setHorizontalTextPosition(4);
/*     */   }
/*     */   protected Color backgroundColor; private Image image; private Image imageUp;
/*     */   public UpdaterFullButton(Color color, final Color mouseUnder, String value, String image) {
/*  28 */     super(color, value);
/*  29 */     setHorizontalTextPosition(4);
/*  30 */     this.backgroundColor = color;
/*  31 */     this.image = ImageCache.getImage(image);
/*  32 */     setForeground(Color.WHITE);
/*     */     
/*  34 */     addMouseListener(new MouseAdapter()
/*     */         {
/*     */           public void mouseEntered(MouseEvent e) {
/*  37 */             UpdaterFullButton.this.setBackground(mouseUnder);
/*     */           }
/*     */ 
/*     */           
/*     */           public void mouseExited(MouseEvent e) {
/*  42 */             UpdaterFullButton.this.setBackground(UpdaterFullButton.this.backgroundColor);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public UpdaterFullButton(Color color, final Color mouseUnder, String value, String image, String imageUp) {
/*  48 */     super(color, value);
/*  49 */     setHorizontalTextPosition(4);
/*  50 */     this.backgroundColor = color;
/*  51 */     this.image = ImageCache.getImage(image);
/*  52 */     this.imageUp = ImageCache.getImage(imageUp);
/*  53 */     setForeground(Color.WHITE);
/*     */     
/*  55 */     addMouseListener(new MouseAdapter()
/*     */         {
/*     */           public void mouseEntered(MouseEvent e) {
/*  58 */             UpdaterFullButton.this.setBackground(mouseUnder);
/*     */           }
/*     */ 
/*     */           
/*     */           public void mouseExited(MouseEvent e) {
/*  63 */             UpdaterFullButton.this.setBackground(UpdaterFullButton.this.backgroundColor);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void paintComponent(Graphics g) {
/*  71 */     super.paintComponent(g);
/*  72 */     Rectangle rec = getVisibleRect();
/*     */     
/*  74 */     String text = getText();
/*  75 */     UpdaterFullButton updaterFullButton = this;
/*     */     
/*  77 */     paintBackground(rec, g);
/*  78 */     paintText(g, (JComponent)updaterFullButton, rec, text);
/*  79 */     paintPicture(g, (JComponent)updaterFullButton, rec, getModel().isRollover());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void paintPicture(Graphics g, JComponent c, Rectangle rect, boolean rollover) {
/*  84 */     if (this.image == null)
/*     */       return; 
/*  86 */     if (getHorizontalTextPosition() == 4) {
/*  87 */       Graphics2D g2d = (Graphics2D)g;
/*  88 */       int x = (getInsets()).left;
/*  89 */       int y = (getHeight() - this.image.getHeight(null)) / 2;
/*     */       
/*  91 */       if (rollover && this.imageUp != null) {
/*  92 */         g2d.drawImage(this.imageUp, x, y, (ImageObserver)null);
/*     */       } else {
/*  94 */         g2d.drawImage(this.image, x, y, (ImageObserver)null);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void paintText(Graphics g, JComponent c, Rectangle textRect, String text) {
/* 101 */     if (text == null)
/*     */       return; 
/* 103 */     if (getHorizontalTextPosition() == 4) {
/* 104 */       g.setColor(getForeground());
/* 105 */       Graphics2D g2d = (Graphics2D)g;
/* 106 */       g2d.setFont(getFont());
/* 107 */       g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
/* 108 */       FontMetrics fm = g2d.getFontMetrics();
/* 109 */       Rectangle2D r = fm.getStringBounds(text, g2d);
/*     */       
/* 111 */       int x = (getInsets()).left + this.image.getWidth(null) + getIconTextGap();
/* 112 */       int y = (getHeight() - (int)r.getHeight()) / 2 + fm.getAscent() - 1;
/* 113 */       g2d.drawString(text, x, y);
/* 114 */       g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void paintBackground(Rectangle rec, Graphics g) {
/* 119 */     ButtonModel buttonModel = getModel();
/* 120 */     g.setColor(getBackground());
/* 121 */     if (buttonModel.isPressed()) {
/* 122 */       g.setColor(getBackground());
/* 123 */       g.fillRect(rec.x, rec.y, rec.width, rec.height);
/* 124 */     } else if (!buttonModel.isEnabled()) {
/* 125 */       if (this.unEnableColor == null) {
/* 126 */         g.setColor(getForeground().darker());
/*     */       } else {
/* 128 */         g.setColor(this.unEnableColor);
/*     */       }
/*     */     
/* 131 */     } else if (buttonModel.isRollover()) {
/* 132 */       g.setColor(this.unEnableColor);
/*     */     } 
/* 134 */     g.fillRect(rec.x, rec.y, rec.width, rec.height);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\loc\UpdaterFullButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */