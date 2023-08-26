/*    */ package org.tlauncher.tlauncher.ui.browser;
/*    */ 
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Image;
/*    */ import javax.swing.JPanel;
/*    */ import org.tlauncher.tlauncher.ui.block.Blockable;
/*    */ import org.tlauncher.tlauncher.ui.images.ImageCache;
/*    */ 
/*    */ public class BrowserFallback
/*    */   extends JPanel
/*    */   implements Blockable
/*    */ {
/*    */   private final BrowserHolder holder;
/*    */   private Image image;
/*    */   private int imageWidth;
/*    */   private int imageHeight;
/*    */   
/*    */   BrowserFallback(BrowserHolder holder) {
/* 19 */     this.holder = holder;
/*    */   }
/*    */   
/*    */   private void updateImage() {
/* 23 */     this.image = ImageCache.getImage("plains.png");
/*    */     
/* 25 */     this.imageWidth = this.image.getWidth(null);
/* 26 */     this.imageHeight = this.image.getHeight(null);
/*    */   }
/*    */   
/*    */   public void paintComponent(Graphics g) {
/*    */     double width, height;
/* 31 */     if (this.image == null) {
/*    */       return;
/*    */     }
/* 34 */     double windowWidth = getWidth(), windowHeight = getHeight();
/*    */     
/* 36 */     double ratio = Math.min(this.imageWidth / windowWidth, this.imageHeight / windowHeight);
/*    */ 
/*    */     
/* 39 */     if (ratio < 1.0D) {
/* 40 */       width = this.imageWidth;
/* 41 */       height = this.imageHeight;
/*    */     } else {
/* 43 */       width = this.imageWidth / ratio;
/* 44 */       height = this.imageHeight / ratio;
/*    */     } 
/*    */ 
/*    */     
/* 48 */     double x = (windowWidth - width) / 2.0D;
/* 49 */     double y = (windowHeight - height) / 2.0D;
/*    */     
/* 51 */     g.drawImage(this.image, (int)x, (int)y, (int)width, (int)height, null);
/*    */   }
/*    */ 
/*    */   
/*    */   public void block(Object reason) {
/* 56 */     this.holder.removeAll();
/* 57 */     this.holder.setCenter(this.holder.browser);
/*    */   }
/*    */ 
/*    */   
/*    */   public void unblock(Object reason) {
/* 62 */     updateImage();
/*    */     
/* 64 */     this.holder.removeAll();
/* 65 */     this.holder.setCenter(this);
/*    */     
/* 67 */     if (this.holder.browser != null)
/* 68 */       this.holder.browser.cleanupResources(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\browser\BrowserFallback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */