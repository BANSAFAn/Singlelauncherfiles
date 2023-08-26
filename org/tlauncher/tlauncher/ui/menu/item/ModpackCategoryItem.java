/*    */ package org.tlauncher.tlauncher.ui.menu.item;
/*    */ import java.awt.FontMetrics;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Graphics2D;
/*    */ import java.awt.Rectangle;
/*    */ import java.awt.RenderingHints;
/*    */ import java.awt.geom.Rectangle2D;
/*    */ import javax.swing.JComponent;
/*    */ 
/*    */ public class ModpackCategoryItem extends LocalizableMenuItem {
/*    */   public ModpackCategoryItem(String path) {
/* 12 */     super(path);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void paintComponent(Graphics g) {
/* 18 */     Rectangle r = getVisibleRect();
/* 19 */     g.setColor(ColorUtil.BLUE_MODPACK);
/* 20 */     g.fillRect(r.x, r.y, r.width, r.height);
/* 21 */     paintText(g, (JComponent)this, r, getText());
/*    */   }
/*    */ 
/*    */   
/*    */   protected void paintText(Graphics g, JComponent c, Rectangle r, String text) {
/* 26 */     Graphics2D g2d = (Graphics2D)g;
/* 27 */     g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
/* 28 */     FontMetrics fm = g2d.getFontMetrics();
/* 29 */     Rectangle2D textRect = fm.getStringBounds(text, g2d);
/* 30 */     g.setFont(getFont());
/* 31 */     g.setColor(Color.WHITE);
/* 32 */     int x = (int)(r.getWidth() - textRect.getWidth()) / 2;
/* 33 */     int y = (int)(r.getHeight() - (int)textRect.getHeight()) / 2 + fm.getAscent() - 1;
/* 34 */     g2d.drawString(text, x, y);
/* 35 */     g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\menu\item\ModpackCategoryItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */