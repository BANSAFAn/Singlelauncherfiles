/*    */ package org.tlauncher.tlauncher.ui.ui;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Graphics2D;
/*    */ import java.awt.Rectangle;
/*    */ import java.awt.image.BufferedImage;
/*    */ import java.util.Objects;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.plaf.basic.BasicProgressBarUI;
/*    */ import org.tlauncher.util.U;
/*    */ 
/*    */ public class PreloaderProgressUI
/*    */   extends BasicProgressBarUI
/*    */ {
/* 16 */   public final Color border = new Color(156, 155, 155);
/* 17 */   public final Color bottomBorderLine = new Color(146, 154, 140);
/* 18 */   public final Color REST_COLOR = new Color(200, 203, 199);
/*    */   
/*    */   public static final int PROGRESS_HEIGHT = 24;
/*    */   public static final int PROGRESS_BAR_WIDTH = 40;
/*    */   BufferedImage bottom;
/*    */   BufferedImage top;
/*    */   
/*    */   public PreloaderProgressUI(BufferedImage bottom, BufferedImage top) {
/* 26 */     this.bottom = bottom;
/* 27 */     this.top = top;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void paintDeterminate(Graphics g, JComponent c) {
/* 32 */     Graphics2D g2d = (Graphics2D)g.create();
/* 33 */     Rectangle rec = this.progressBar.getVisibleRect();
/* 34 */     double complete = this.progressBar.getPercentComplete();
/* 35 */     int width = this.progressBar.getWidth();
/* 36 */     int height = this.progressBar.getHeight();
/*    */     
/* 38 */     int completeWidth = (int)(complete * width);
/*    */     
/* 40 */     g2d.setColor(this.REST_COLOR);
/* 41 */     g2d.fillRect(rec.x + completeWidth, rec.y, width, height);
/*    */ 
/*    */     
/* 44 */     g2d.setColor(this.border);
/* 45 */     g2d.drawLine(completeWidth, 0, rec.width, 0);
/* 46 */     g2d.drawLine(completeWidth, rec.height - 1, rec.width, rec.height - 1);
/* 47 */     g2d.drawLine(rec.x + rec.width - 1, rec.y, rec.x + rec.width - 1, rec.y + rec.height);
/* 48 */     g2d.setColor(this.bottomBorderLine);
/* 49 */     g2d.drawLine(completeWidth, rec.height - 1, rec.width, rec.height - 1);
/*    */ 
/*    */     
/* 52 */     if (completeWidth > 0) {
/* 53 */       g2d.drawImage(this.bottom.getSubimage(0, 0, completeWidth, 24), rec.x, rec.y, completeWidth, rec.height + 1, null);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void paintIndeterminate(Graphics g, JComponent c) {
/* 60 */     if (!(g instanceof Graphics2D)) {
/*    */       return;
/*    */     }
/* 63 */     Rectangle rec = null;
/*    */     try {
/* 65 */       Graphics2D g2d = (Graphics2D)g;
/* 66 */       rec = this.progressBar.getVisibleRect();
/* 67 */       this.boxRect = getBox(this.boxRect);
/* 68 */       g2d.drawImage(this.bottom, rec.x, rec.y, this.bottom.getWidth(), this.bottom.getHeight(), null);
/* 69 */       g2d.drawImage(this.top, this.boxRect.x, this.boxRect.y, this.boxRect.width, this.boxRect.height, null);
/* 70 */     } catch (NullPointerException e) {
/* 71 */       U.log(new Object[] { "bottom is null " + Objects.isNull(this.bottom) });
/* 72 */       U.log(new Object[] { "rec is null " + Objects.isNull(rec) });
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected Rectangle getBox(Rectangle r) {
/* 79 */     r.x += 4;
/* 80 */     if (r.x > this.progressBar.getWidth()) {
/* 81 */       r.x = 0;
/*    */     }
/* 83 */     r.height = 24;
/* 84 */     r.width = 40;
/* 85 */     return r;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\u\\ui\PreloaderProgressUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */