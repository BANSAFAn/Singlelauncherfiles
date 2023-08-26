/*    */ package org.tlauncher.tlauncher.ui.ui;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Graphics2D;
/*    */ import java.awt.Rectangle;
/*    */ import java.awt.image.BufferedImage;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.plaf.basic.BasicProgressBarUI;
/*    */ 
/*    */ public class FancyProgressBar
/*    */   extends BasicProgressBarUI {
/* 13 */   public final Color border = new Color(156, 155, 155);
/* 14 */   public final Color bottomBorderLine = new Color(146, 154, 140);
/* 15 */   public final Color REST_COLOR = new Color(200, 203, 199);
/*    */   
/*    */   public static final int PROGRESS_HEIGHT = 24;
/*    */   BufferedImage image;
/*    */   
/*    */   public FancyProgressBar(BufferedImage bufferedImage) {
/* 21 */     this.image = bufferedImage;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void paintDeterminate(Graphics g, JComponent c) {
/* 26 */     Graphics2D g2d = (Graphics2D)g.create();
/* 27 */     Rectangle rec = this.progressBar.getVisibleRect();
/* 28 */     double complete = this.progressBar.getPercentComplete();
/* 29 */     int width = this.progressBar.getWidth();
/* 30 */     int height = this.progressBar.getHeight();
/*    */     
/* 32 */     int completeWidth = (int)(complete * width);
/*    */     
/* 34 */     g2d.setColor(this.REST_COLOR);
/* 35 */     g2d.fillRect(rec.x + completeWidth, rec.y, width, height);
/*    */ 
/*    */     
/* 38 */     g2d.setColor(this.border);
/* 39 */     g2d.drawLine(completeWidth, 0, rec.width, 0);
/* 40 */     g2d.drawLine(completeWidth, rec.height - 1, rec.width, rec.height - 1);
/* 41 */     g2d.drawLine(rec.x + rec.width - 1, rec.y, rec.x + rec.width - 1, rec.y + rec.height);
/* 42 */     g2d.setColor(this.bottomBorderLine);
/* 43 */     g2d.drawLine(completeWidth, rec.height - 1, rec.width, rec.height - 1);
/*    */ 
/*    */     
/* 46 */     if (completeWidth > 0)
/* 47 */       g2d.drawImage(this.image.getSubimage(0, 0, completeWidth, 24), rec.x, rec.y, completeWidth, rec.height + 1, null); 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\u\\ui\FancyProgressBar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */