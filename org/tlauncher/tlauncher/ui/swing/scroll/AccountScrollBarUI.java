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
/*    */ public class AccountScrollBarUI
/*    */   extends BasicScrollBarUI {
/* 14 */   protected int heightThubm = 46;
/* 15 */   protected Color trackColor = new Color(225, 234, 238);
/* 16 */   protected Color thumbColor = new Color(205, 223, 233);
/*    */   
/*    */   protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
/* 19 */     Graphics2D g2 = (Graphics2D)g;
/* 20 */     Rectangle rec = g2.getClipBounds();
/* 21 */     g2.setColor(this.trackColor);
/* 22 */     g2.fillRect(rec.x, rec.y, rec.width, rec.height);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
/* 27 */     Graphics2D g2 = (Graphics2D)g;
/* 28 */     Rectangle rec = thumbBounds;
/* 29 */     g2.setColor(this.thumbColor);
/* 30 */     g2.fillRect(rec.x, rec.y, rec.width, rec.height);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Dimension getMinimumThumbSize() {
/* 35 */     return new Dimension(7, 46);
/*    */   }
/*    */ 
/*    */   
/*    */   protected JButton createDecreaseButton(int orientation) {
/* 40 */     return createZeroButton();
/*    */   }
/*    */ 
/*    */   
/*    */   protected JButton createIncreaseButton(int orientation) {
/* 45 */     return createZeroButton();
/*    */   }
/*    */   
/*    */   private JButton createZeroButton() {
/* 49 */     JButton jbutton = new JButton();
/* 50 */     jbutton.setPreferredSize(new Dimension(0, 0));
/* 51 */     jbutton.setMinimumSize(new Dimension(0, 0));
/* 52 */     jbutton.setMaximumSize(new Dimension(0, 0));
/* 53 */     return jbutton;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\scroll\AccountScrollBarUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */