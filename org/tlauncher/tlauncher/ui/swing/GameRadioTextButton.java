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
/*    */ import java.awt.image.ImageObserver;
/*    */ import org.tlauncher.tlauncher.ui.images.ImageCache;
/*    */ import org.tlauncher.tlauncher.ui.loc.LocalizableRadioButton;
/*    */ 
/*    */ public class GameRadioTextButton extends LocalizableRadioButton {
/* 18 */   protected Color defaultColor = new Color(60, 170, 232);
/* 19 */   private Color under = new Color(255, 202, 41);
/*    */   private boolean mouseUnder;
/*    */   
/*    */   public GameRadioTextButton(String string) {
/* 23 */     super(string);
/*    */     
/* 25 */     addMouseListener(new MouseAdapter()
/*    */         {
/*    */           public void mouseEntered(MouseEvent e) {
/* 28 */             GameRadioTextButton.this.mouseUnder = true;
/* 29 */             GameRadioTextButton.this.repaint();
/*    */           }
/*    */ 
/*    */           
/*    */           public void mouseExited(MouseEvent e) {
/* 34 */             GameRadioTextButton.this.mouseUnder = false;
/* 35 */             GameRadioTextButton.this.repaint();
/*    */           }
/*    */         });
/* 38 */     setForeground(Color.WHITE);
/* 39 */     setPreferredSize(new Dimension(129, 55));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void paintComponent(Graphics gr) {
/* 44 */     super.paintComponent(gr);
/* 45 */     Rectangle rec = getVisibleRect();
/* 46 */     Graphics2D g2 = (Graphics2D)gr;
/* 47 */     paintBackground(g2, rec);
/* 48 */     paintText(g2, rec);
/*    */   }
/*    */   protected void paintBackground(Graphics2D g2, Rectangle rec) {
/* 51 */     int y = rec.y;
/* 52 */     g2.drawImage(ImageCache.getBufferedImage("modpack-radio-button-background.png"), 0, 0, (ImageObserver)null);
/*    */     
/* 54 */     if (isSelected() || this.mouseUnder) {
/* 55 */       y = rec.y + rec.height - 9;
/* 56 */       g2.setColor(this.under);
/* 57 */       for (; y < rec.height + rec.y; y++) {
/* 58 */         g2.drawLine(rec.x, y, rec.x + rec.width, y);
/*    */       }
/*    */     } else {
/* 61 */       y = rec.y + rec.height - 9;
/* 62 */       g2.setColor(this.defaultColor);
/* 63 */       for (; y < rec.height + rec.y; y++) {
/* 64 */         g2.drawLine(rec.x, y, rec.x + rec.width, y);
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   protected void paintText(Graphics2D g, Rectangle textRect) {
/* 70 */     g.setColor(getForeground());
/* 71 */     String text = getText();
/* 72 */     Graphics2D g2d = g;
/* 73 */     g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
/* 74 */     FontMetrics fm = g2d.getFontMetrics();
/* 75 */     Rectangle2D r = fm.getStringBounds(text, g2d);
/* 76 */     g.setFont(getFont());
/* 77 */     int x = (getWidth() - (int)r.getWidth()) / 2;
/* 78 */     int y = (getHeight() - (int)r.getHeight()) / 2 + fm.getAscent();
/* 79 */     g2d.drawString(text, x, y);
/* 80 */     g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\GameRadioTextButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */