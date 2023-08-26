/*    */ package org.tlauncher.tlauncher.ui.loc;
/*    */ import java.awt.Color;
/*    */ import java.awt.FontMetrics;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Graphics2D;
/*    */ import java.awt.Rectangle;
/*    */ import java.awt.RenderingHints;
/*    */ 
/*    */ public class UpdaterButton extends LocalizableButton {
/*    */   protected Color unEnableColor;
/*    */   
/*    */   public UpdaterButton(Color color, String value) {
/* 13 */     this.backgroundColor = color;
/* 14 */     setText(value);
/* 15 */     setOpaque(true);
/* 16 */     setBackground(color);
/*    */   }
/*    */   private Color backgroundColor;
/*    */   public UpdaterButton(Color color, Color UnEnableColor, String value) {
/* 20 */     this(color, value);
/* 21 */     this.unEnableColor = UnEnableColor;
/*    */   }
/*    */   
/*    */   public UpdaterButton(Color color, Color unEnableColor, Color foreground, String value) {
/* 25 */     this(color, unEnableColor, value);
/* 26 */     setForeground(foreground);
/*    */   }
/*    */   
/*    */   public UpdaterButton(Color color) {
/* 30 */     this.backgroundColor = color;
/* 31 */     setText((String)null);
/* 32 */     setContentAreaFilled(false);
/* 33 */     setOpaque(true);
/* 34 */     setBackground(this.backgroundColor);
/*    */   }
/*    */   
/*    */   public UpdaterButton() {
/* 38 */     setText((String)null);
/* 39 */     setContentAreaFilled(false);
/* 40 */     setOpaque(true);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void paintComponent(Graphics g) {
/* 46 */     Rectangle rec = getVisibleRect();
/* 47 */     g.setColor(getBackground());
/* 48 */     g.fillRect(rec.x, rec.y, rec.width, rec.height);
/*    */     
/* 50 */     String text = getText();
/*    */     
/* 52 */     ButtonModel buttonModel = getModel();
/* 53 */     Color colorText = getForeground();
/*    */     
/* 55 */     if (buttonModel.isRollover() && this.unEnableColor != null && this.model.isEnabled()) {
/* 56 */       g.setColor(this.unEnableColor);
/*    */     }
/* 58 */     g.fillRect(rec.x, rec.y, rec.width, rec.height);
/* 59 */     g.setColor(colorText);
/* 60 */     UpdaterButton updaterButton = this;
/* 61 */     if (text != null) {
/* 62 */       paintText(g, (JComponent)updaterButton, rec, text);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected void paintText(Graphics g, JComponent c, Rectangle textRect, String text) {
/* 68 */     Graphics2D g2d = (Graphics2D)g;
/* 69 */     g2d.setFont(getFont());
/* 70 */     g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
/* 71 */     FontMetrics fm = g2d.getFontMetrics();
/* 72 */     Rectangle2D r = fm.getStringBounds(text, g2d);
/* 73 */     int x = (getWidth() - (int)r.getWidth()) / 2;
/* 74 */     int y = (getHeight() - (int)r.getHeight()) / 2 + fm.getAscent();
/* 75 */     g2d.drawString(text, x, y);
/* 76 */     g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
/*    */   }
/*    */   
/*    */   public Color getBackgroundColor() {
/* 80 */     return this.backgroundColor;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\loc\UpdaterButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */