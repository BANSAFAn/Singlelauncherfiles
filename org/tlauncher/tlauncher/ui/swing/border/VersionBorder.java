/*    */ package org.tlauncher.tlauncher.ui.swing.border;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Component;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Insets;
/*    */ import javax.swing.border.MatteBorder;
/*    */ 
/*    */ 
/*    */ public class VersionBorder
/*    */   extends MatteBorder
/*    */ {
/* 13 */   public static final Color SEPARATOR_COLOR = new Color(220, 220, 220);
/*    */   
/*    */   public VersionBorder(int top, int left, int bottom, int right, Color matteColor) {
/* 16 */     super(top, left, bottom, right, matteColor);
/*    */   }
/*    */ 
/*    */   
/*    */   public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
/* 21 */     Insets insets = getBorderInsets(c);
/* 22 */     Color oldColor = g.getColor();
/* 23 */     g.translate(x, y);
/*    */ 
/*    */     
/* 26 */     if (this.tileIcon != null) {
/* 27 */       this.color = (this.tileIcon.getIconWidth() == -1) ? Color.gray : null;
/*    */     }
/*    */     
/* 30 */     if (this.color != null) {
/* 31 */       g.setColor(SEPARATOR_COLOR);
/* 32 */       g.drawLine(0, height - 1, width, height - 1);
/*    */     }
/* 34 */     else if (this.tileIcon != null) {
/* 35 */       int tileW = this.tileIcon.getIconWidth();
/* 36 */       int tileH = this.tileIcon.getIconHeight();
/* 37 */       paintEdge(c, g, 0, 0, width - insets.right, insets.top, tileW, tileH);
/* 38 */       paintEdge(c, g, 0, insets.top, insets.left, height - insets.top, tileW, tileH);
/* 39 */       paintEdge(c, g, insets.left, height - insets.bottom, width - insets.left, insets.bottom, tileW, tileH);
/*    */       
/* 41 */       paintEdge(c, g, width - insets.right, 0, insets.right, height - insets.bottom, tileW, tileH);
/*    */     } 
/* 43 */     g.translate(-x, -y);
/* 44 */     g.setColor(oldColor);
/*    */   }
/*    */   
/*    */   private void paintEdge(Component c, Graphics g, int x, int y, int width, int height, int tileW, int tileH) {
/* 48 */     g = g.create(x, y, width, height);
/* 49 */     int sY = -(y % tileH);
/* 50 */     for (x = -(x % tileW); x < width; x += tileW) {
/* 51 */       for (y = sY; y < height; y += tileH) {
/* 52 */         this.tileIcon.paintIcon(c, g, x, y);
/*    */       }
/*    */     } 
/* 55 */     g.dispose();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\border\VersionBorder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */