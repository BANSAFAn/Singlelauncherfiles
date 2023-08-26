/*    */ package org.tlauncher.tlauncher.ui;
/*    */ 
/*    */ import java.awt.Graphics;
/*    */ import java.awt.image.BufferedImage;
/*    */ import javax.swing.JPanel;
/*    */ import org.tlauncher.tlauncher.ui.images.ImageCache;
/*    */ 
/*    */ public class BackgroundPanel
/*    */   extends JPanel {
/*    */   private final BufferedImage image;
/*    */   
/*    */   public BackgroundPanel(String name) {
/* 13 */     this.image = ImageCache.get(name);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void paintComponent(Graphics g) {
/* 19 */     g.drawImage(this.image, 0, 0, null);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\BackgroundPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */