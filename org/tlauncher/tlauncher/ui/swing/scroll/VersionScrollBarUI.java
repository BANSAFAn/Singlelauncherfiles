/*    */ package org.tlauncher.tlauncher.ui.swing.scroll;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Graphics2D;
/*    */ import java.awt.Rectangle;
/*    */ import javax.swing.JButton;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.plaf.basic.BasicScrollBarUI;
/*    */ 
/*    */ public class VersionScrollBarUI
/*    */   extends BasicScrollBarUI {
/* 14 */   protected int heightThubm = 16;
/*    */   public int getHeightThubm() {
/* 16 */     return this.heightThubm;
/*    */   }
/*    */   
/*    */   public void setHeightThubm(int heightThubm) {
/* 20 */     this.heightThubm = heightThubm;
/*    */   }
/*    */   
/* 23 */   protected int gapThubm = 6;
/*    */   
/* 25 */   protected Color lineColor = new Color(255, 255, 255);
/* 26 */   protected Color trackColor = new Color(30, 134, 187);
/* 27 */   protected Color thumbColor = new Color(191, 219, 235);
/*    */   
/*    */   protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
/* 30 */     Graphics2D g2 = (Graphics2D)g;
/* 31 */     Rectangle rec = g2.getClipBounds();
/* 32 */     g2.setColor(this.trackColor);
/* 33 */     g2.fillRect(rec.x, rec.y, rec.width, rec.height);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
/* 38 */     Graphics2D g2 = (Graphics2D)g;
/* 39 */     Rectangle rec = thumbBounds;
/* 40 */     g2.setColor(this.thumbColor);
/* 41 */     g2.fillRect(rec.x, rec.y, rec.width, rec.height);
/*    */     
/* 43 */     int width = rec.width - rec.width / 3;
/* 44 */     int startX = rec.x + rec.width / 6 + 1;
/* 45 */     int startY = rec.y + rec.height / 2 - this.heightThubm / 2;
/* 46 */     for (int i = 0; i < 4; i++, startY += this.gapThubm) {
/* 47 */       drawLines(g2, startX, startY, width);
/*    */     }
/*    */   }
/*    */   
/*    */   private void drawLines(Graphics2D g2, int startX, int startY, int width) {
/* 52 */     g2.setColor(Color.WHITE);
/* 53 */     g2.drawLine(startX, startY, startX + width - 1, startY);
/* 54 */     g2.setColor(new Color(190, 190, 190));
/* 55 */     g2.drawLine(startX, startY + 1, startX + width - 1, startY + 1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Dimension getMinimumThumbSize() {
/* 60 */     return new Dimension(10, 80);
/*    */   }
/*    */ 
/*    */   
/*    */   protected JButton createDecreaseButton(int orientation) {
/* 65 */     return createZeroButton();
/*    */   }
/*    */ 
/*    */   
/*    */   protected JButton createIncreaseButton(int orientation) {
/* 70 */     return createZeroButton();
/*    */   }
/*    */   
/*    */   private JButton createZeroButton() {
/* 74 */     JButton jbutton = new JButton();
/* 75 */     jbutton.setPreferredSize(new Dimension(0, 0));
/* 76 */     jbutton.setMinimumSize(new Dimension(0, 0));
/* 77 */     jbutton.setMaximumSize(new Dimension(0, 0));
/* 78 */     return jbutton;
/*    */   }
/*    */   public int getGapThubm() {
/* 81 */     return this.gapThubm;
/*    */   }
/*    */   
/*    */   public void setGapThubm(int gapThubm) {
/* 85 */     this.gapThubm = gapThubm;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\scroll\VersionScrollBarUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */