/*    */ package org.tlauncher.tlauncher.ui.modpack;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Graphics2D;
/*    */ import java.awt.Rectangle;
/*    */ import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
/*    */ 
/*    */ public class ShadowPanel
/*    */   extends ExtendedPanel
/*    */ {
/*    */   private int colorStarted;
/*    */   
/*    */   public ShadowPanel(int colorStarted) {
/* 15 */     this.colorStarted = colorStarted;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void paintComponent(Graphics g0) {
/* 20 */     super.paintComponent(g0);
/* 21 */     Rectangle rec = getVisibleRect();
/* 22 */     int y = rec.y, i = this.colorStarted;
/*    */     
/* 24 */     Graphics2D g2 = (Graphics2D)g0;
/* 25 */     for (; y < rec.height + rec.y; y++) {
/* 26 */       g2.setColor(new Color(i, i, i));
/* 27 */       if (i != 255) {
/* 28 */         i++;
/*    */       }
/* 30 */       g2.drawLine(rec.x, y, rec.x + rec.width, y);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\modpack\ShadowPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */