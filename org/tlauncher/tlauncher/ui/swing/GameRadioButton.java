/*    */ package org.tlauncher.tlauncher.ui.swing;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.FontMetrics;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Graphics2D;
/*    */ import java.awt.Rectangle;
/*    */ import java.awt.RenderingHints;
/*    */ import java.awt.event.MouseAdapter;
/*    */ import java.awt.event.MouseEvent;
/*    */ import java.awt.geom.Rectangle2D;
/*    */ import org.tlauncher.tlauncher.ui.loc.LocalizableRadioButton;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GameRadioButton
/*    */   extends LocalizableRadioButton
/*    */ {
/* 20 */   private Color selected = new Color(60, 170, 232);
/* 21 */   private Color under = new Color(255, 202, 41);
/*    */   private boolean mouseUnder = false;
/*    */   
/*    */   public GameRadioButton(String string) {
/* 25 */     super(string);
/* 26 */     setPreferredSize(new Dimension(149, 52));
/* 27 */     addMouseListener(new MouseAdapter()
/*    */         {
/*    */           public void mouseEntered(MouseEvent e) {
/* 30 */             GameRadioButton.this.mouseUnder = true;
/*    */           }
/*    */ 
/*    */           
/*    */           public void mouseExited(MouseEvent e) {
/* 35 */             GameRadioButton.this.mouseUnder = false;
/*    */           }
/*    */         });
/* 38 */     setForeground(Color.BLACK);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void paintComponent(Graphics g) {
/* 44 */     super.paintComponent(g);
/* 45 */     Rectangle rec = getVisibleRect();
/* 46 */     int i = 242, y = rec.y;
/* 47 */     Graphics2D g2 = (Graphics2D)g;
/* 48 */     for (; y < rec.height + rec.y; y++) {
/*    */       
/* 50 */       g2.setColor(new Color(i, i, i));
/* 51 */       if (i != 255) {
/* 52 */         i++;
/*    */       }
/* 54 */       g2.drawLine(rec.x, y, rec.x + rec.width, y);
/*    */     } 
/*    */     
/* 57 */     if (isSelected()) {
/* 58 */       y = rec.y + rec.height - 3;
/* 59 */       g2.setColor(this.selected);
/* 60 */       for (; y < rec.height + rec.y; y++) {
/* 61 */         g2.drawLine(rec.x, y, rec.x + rec.width, y);
/*    */       }
/* 63 */     } else if (this.mouseUnder) {
/* 64 */       y = rec.y + rec.height - 3;
/* 65 */       g2.setColor(this.under);
/* 66 */       for (; y < rec.height + rec.y; y++) {
/* 67 */         g2.drawLine(rec.x, y, rec.x + rec.width, y);
/*    */       }
/*    */     } 
/*    */     
/* 71 */     paintText(g2, rec);
/*    */   }
/*    */   
/*    */   protected void paintText(Graphics2D g, Rectangle textRect) {
/* 75 */     g.setColor(getForeground());
/* 76 */     String text = getText();
/* 77 */     Graphics2D g2d = g;
/* 78 */     g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
/* 79 */     FontMetrics fm = g2d.getFontMetrics();
/* 80 */     Rectangle2D r = fm.getStringBounds(text, g2d);
/* 81 */     g.setFont(getFont());
/* 82 */     int x = (getWidth() - (int)r.getWidth()) / 2;
/* 83 */     int y = (getHeight() - (int)r.getHeight()) / 2 + fm.getAscent();
/* 84 */     g2d.drawString(text, x, y);
/* 85 */     g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\GameRadioButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */