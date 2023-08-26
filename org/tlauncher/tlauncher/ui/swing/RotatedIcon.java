/*     */ package org.tlauncher.tlauncher.ui.swing;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import javax.swing.Icon;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RotatedIcon
/*     */   implements Icon
/*     */ {
/*     */   private Icon icon;
/*     */   private Rotate rotate;
/*     */   private double angle;
/*     */   
/*     */   public enum Rotate
/*     */   {
/*  31 */     DOWN, UP, UPSIDE_DOWN, ABOUT_CENTER;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RotatedIcon(Icon icon) {
/*  46 */     this(icon, Rotate.UP);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private RotatedIcon(Icon icon, Rotate rotate) {
/*  56 */     this.icon = icon;
/*  57 */     this.rotate = rotate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RotatedIcon(Icon icon, double angle) {
/*  70 */     this(icon, Rotate.ABOUT_CENTER);
/*  71 */     this.angle = angle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Icon getIcon() {
/*  80 */     return this.icon;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rotate getRotate() {
/*  89 */     return this.rotate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getAngle() {
/*  98 */     return this.angle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getIconWidth() {
/* 112 */     if (this.rotate == Rotate.ABOUT_CENTER) {
/* 113 */       double radians = Math.toRadians(this.angle);
/* 114 */       double sin = Math.abs(Math.sin(radians));
/* 115 */       double cos = Math.abs(Math.cos(radians));
/*     */       
/* 117 */       int width = (int)Math.floor(this.icon.getIconWidth() * cos + this.icon
/* 118 */           .getIconHeight() * sin);
/* 119 */       return width;
/* 120 */     }  if (this.rotate == Rotate.UPSIDE_DOWN) {
/* 121 */       return this.icon.getIconWidth();
/*     */     }
/* 123 */     return this.icon.getIconHeight();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getIconHeight() {
/* 133 */     if (this.rotate == Rotate.ABOUT_CENTER) {
/* 134 */       double radians = Math.toRadians(this.angle);
/* 135 */       double sin = Math.abs(Math.sin(radians));
/* 136 */       double cos = Math.abs(Math.cos(radians));
/* 137 */       int height = (int)Math.floor(this.icon.getIconHeight() * cos + this.icon
/* 138 */           .getIconWidth() * sin);
/* 139 */       return height;
/* 140 */     }  if (this.rotate == Rotate.UPSIDE_DOWN) {
/* 141 */       return this.icon.getIconHeight();
/*     */     }
/* 143 */     return this.icon.getIconWidth();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void paintIcon(Component c, Graphics g, int x, int y) {
/* 156 */     Graphics2D g2 = (Graphics2D)g.create();
/*     */     
/* 158 */     int cWidth = this.icon.getIconWidth() / 2;
/* 159 */     int cHeight = this.icon.getIconHeight() / 2;
/* 160 */     int xAdjustment = (this.icon.getIconWidth() % 2 == 0) ? 0 : -1;
/* 161 */     int yAdjustment = (this.icon.getIconHeight() % 2 == 0) ? 0 : -1;
/*     */     
/* 163 */     if (this.rotate == Rotate.DOWN) {
/* 164 */       g2.translate(x + cHeight, y + cWidth);
/* 165 */       g2.rotate(Math.toRadians(90.0D));
/* 166 */       this.icon.paintIcon(c, g2, -cWidth, yAdjustment - cHeight);
/* 167 */     } else if (this.rotate == Rotate.UP) {
/* 168 */       g2.translate(x + cHeight, y + cWidth);
/* 169 */       g2.rotate(Math.toRadians(-90.0D));
/* 170 */       this.icon.paintIcon(c, g2, xAdjustment - cWidth, -cHeight);
/* 171 */     } else if (this.rotate == Rotate.UPSIDE_DOWN) {
/* 172 */       g2.translate(x + cWidth, y + cHeight);
/* 173 */       g2.rotate(Math.toRadians(180.0D));
/* 174 */       this.icon.paintIcon(c, g2, xAdjustment - cWidth, yAdjustment - cHeight);
/* 175 */     } else if (this.rotate == Rotate.ABOUT_CENTER) {
/* 176 */       g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
/*     */       
/* 178 */       AffineTransform original = g2.getTransform();
/* 179 */       AffineTransform at = new AffineTransform();
/* 180 */       at.concatenate(original);
/* 181 */       at.translate(((getIconWidth() - this.icon.getIconWidth()) / 2), ((
/* 182 */           getIconHeight() - this.icon.getIconHeight()) / 2));
/* 183 */       at.rotate(Math.toRadians(this.angle), (x + cWidth), (y + cHeight));
/* 184 */       g2.setTransform(at);
/* 185 */       this.icon.paintIcon(c, g2, x, y);
/* 186 */       g2.setTransform(original);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\RotatedIcon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */