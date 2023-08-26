/*    */ package org.tlauncher.tlauncher.ui.ui;
/*    */ import java.awt.Color;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Graphics2D;
/*    */ import java.awt.Image;
/*    */ import java.awt.Rectangle;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.JRadioButton;
/*    */ import javax.swing.plaf.basic.BasicButtonUI;
/*    */ 
/*    */ public class RadioSettingsUI extends BasicButtonUI {
/*    */   public RadioSettingsUI(Image image) {
/* 13 */     this.backgroundImage = image;
/*    */   }
/*    */   Image backgroundImage;
/*    */   
/*    */   public void paint(Graphics g, JComponent c) {
/* 18 */     JRadioButton button = (JRadioButton)c;
/* 19 */     Rectangle rec = c.getVisibleRect();
/* 20 */     paintBackground(g, rec, button.isSelected());
/* 21 */     paintText(g, rec, button);
/*    */   }
/*    */   
/*    */   private void paintText(Graphics g, Rectangle rec, JRadioButton comp) {
/* 25 */     Graphics2D g2d = (Graphics2D)g;
/* 26 */     SwingUtil.paintText(g2d, comp, comp.getText());
/*    */   }
/*    */   
/*    */   private void paintBackground(Graphics g, Rectangle rec, boolean state) {
/* 30 */     if (state) {
/* 31 */       g.setColor(Color.WHITE);
/* 32 */       g.fillRect(rec.x, rec.y, (int)rec.getWidth(), (int)rec.getHeight());
/*    */     } else {
/* 34 */       g.drawImage(this.backgroundImage, rec.x, rec.y, null);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\u\\ui\RadioSettingsUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */