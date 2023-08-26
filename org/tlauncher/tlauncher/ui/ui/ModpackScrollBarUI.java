/*    */ package org.tlauncher.tlauncher.ui.ui;
/*    */ import java.awt.Graphics2D;
/*    */ import java.awt.Rectangle;
/*    */ import java.awt.RenderingHints;
/*    */ import javax.swing.JButton;
/*    */ 
/*    */ public class ModpackScrollBarUI extends BasicScrollBarUI {
/*  8 */   protected int heightThubm = 16;
/*    */   public int getHeightThubm() {
/* 10 */     return this.heightThubm;
/*    */   }
/*    */   
/*    */   public void setHeightThubm(int heightThubm) {
/* 14 */     this.heightThubm = heightThubm;
/*    */   }
/*    */   
/* 17 */   protected int gapThubm = 6;
/* 18 */   int arc = 10;
/* 19 */   protected Color trackColor = new Color(215, 215, 215);
/* 20 */   protected Color thumbColor = new Color(182, 182, 182);
/*    */   
/*    */   private static final int gup = 2;
/*    */   
/*    */   protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
/* 25 */     Graphics2D g2 = (Graphics2D)g.create();
/* 26 */     RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
/* 27 */     qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
/* 28 */     g2.setRenderingHints(qualityHints);
/*    */     
/* 30 */     Component parent = c.getParent();
/*    */     
/* 32 */     Rectangle rec = this.trackRect;
/* 33 */     if (parent != null) {
/* 34 */       Color bg = parent.getBackground();
/* 35 */       g2.setColor(bg);
/* 36 */       g2.fillRect(rec.x, rec.y, rec.width, rec.height);
/*    */     } 
/*    */     
/* 39 */     g2.setColor(this.trackColor);
/* 40 */     g2.fillRoundRect(rec.x, rec.y + 2, rec.width, rec.height - 4, this.arc, this.arc);
/* 41 */     g2.dispose();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
/* 47 */     Graphics2D g2 = (Graphics2D)g.create();
/* 48 */     RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
/* 49 */     qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
/* 50 */     g2.setRenderingHints(qualityHints);
/* 51 */     Rectangle rec = thumbBounds;
/* 52 */     g2.setColor(this.thumbColor);
/* 53 */     g2.fillRoundRect(rec.x, rec.y + 2, rec.width, rec.height - 4, this.arc, this.arc);
/* 54 */     g2.dispose();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Dimension getMinimumThumbSize() {
/* 61 */     return new Dimension(10, 80);
/*    */   }
/*    */ 
/*    */   
/*    */   protected JButton createDecreaseButton(int orientation) {
/* 66 */     return createZeroButton();
/*    */   }
/*    */ 
/*    */   
/*    */   protected JButton createIncreaseButton(int orientation) {
/* 71 */     return createZeroButton();
/*    */   }
/*    */   
/*    */   private JButton createZeroButton() {
/* 75 */     JButton jbutton = new JButton();
/* 76 */     jbutton.setPreferredSize(new Dimension(0, 0));
/* 77 */     return jbutton;
/*    */   }
/*    */   public int getGapThubm() {
/* 80 */     return this.gapThubm;
/*    */   }
/*    */   
/*    */   public void setGapThubm(int gapThubm) {
/* 84 */     this.gapThubm = gapThubm;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\u\\ui\ModpackScrollBarUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */