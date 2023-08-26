/*    */ package org.tlauncher.tlauncher.ui.swing.border;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Component;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Insets;
/*    */ import javax.swing.border.MatteBorder;
/*    */ 
/*    */ public class TipBorder
/*    */   extends MatteBorder {
/*    */   private final BORDER_POS pos;
/*    */   
/*    */   public enum BORDER_POS {
/* 14 */     UP, BOTTOM, LEFT, RIGHT;
/*    */   }
/*    */   
/*    */   public static TipBorder createInstance(int width, BORDER_POS pos, Color matteColor) {
/* 18 */     switch (pos) {
/*    */       case UP:
/* 20 */         return new TipBorder(width, 0, 0, 0, matteColor, pos);
/*    */       case RIGHT:
/* 22 */         return new TipBorder(0, 0, 0, width, matteColor, pos);
/*    */       case BOTTOM:
/* 24 */         return new TipBorder(0, 0, width, 0, matteColor, pos);
/*    */       case LEFT:
/* 26 */         return new TipBorder(0, width, 0, 0, matteColor, pos);
/*    */     } 
/* 28 */     throw new IllegalArgumentException(pos.toString());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private TipBorder(int top, int left, int bottom, int right, Color matteColor, BORDER_POS pos) {
/* 34 */     super(top, left, bottom, right, matteColor);
/* 35 */     this.pos = pos;
/*    */   }
/*    */ 
/*    */   
/*    */   public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
/* 40 */     Insets insets = getBorderInsets(c);
/* 41 */     Color oldColor = g.getColor();
/* 42 */     g.translate(x, y);
/*    */ 
/*    */     
/* 45 */     if (this.tileIcon != null) {
/* 46 */       this.color = (this.tileIcon.getIconWidth() == -1) ? Color.gray : null;
/*    */     }
/*    */     
/* 49 */     if (this.color != null) {
/* 50 */       int bottom, top; g.setColor(this.color);
/* 51 */       int[] xT = new int[0], yT = new int[0];
/* 52 */       switch (this.pos) {
/*    */         case BOTTOM:
/* 54 */           bottom = insets.bottom;
/* 55 */           xT = new int[] { width - 2 * bottom, width - 2 * bottom + bottom / 2, width - 2 * bottom + bottom };
/* 56 */           yT = new int[] { height - bottom, height, height - bottom };
/*    */           break;
/*    */         case UP:
/* 59 */           top = insets.top;
/* 60 */           xT = new int[] { width - 2 * top, width - 2 * top + top / 2, width - 2 * top + top };
/* 61 */           yT = new int[] { 0 + top, 0, 0 + top };
/*    */           break;
/*    */       } 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 79 */       g.fillPolygon(xT, yT, 3);
/*    */       
/* 81 */       g.translate(-x, -y);
/* 82 */       g.setColor(oldColor);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\border\TipBorder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */