/*    */ package org.tlauncher.tlauncher.ui.button;
/*    */ 
/*    */ import com.google.common.base.Strings;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Graphics2D;
/*    */ import java.awt.event.MouseAdapter;
/*    */ import java.awt.event.MouseEvent;
/*    */ import java.awt.image.BufferedImage;
/*    */ import javax.swing.BorderFactory;
/*    */ import javax.swing.JComponent;
/*    */ import org.tlauncher.tlauncher.ui.images.ImageCache;
/*    */ import org.tlauncher.tlauncher.ui.loc.LocalizableButton;
/*    */ import org.tlauncher.util.SwingUtil;
/*    */ import org.tlauncher.util.U;
/*    */ 
/*    */ public class RoundImageButton extends LocalizableButton {
/*    */   public RoundImageButton(final BufferedImage image, final BufferedImage mouseUnderImage) {
/* 19 */     super("");
/* 20 */     this.current = image;
/* 21 */     setOpaque(false);
/* 22 */     setBorder(BorderFactory.createEmptyBorder());
/* 23 */     addMouseListener(new MouseAdapter()
/*    */         {
/*    */           public void mouseEntered(MouseEvent e) {
/* 26 */             RoundImageButton.this.current = mouseUnderImage;
/* 27 */             RoundImageButton.this.repaint();
/*    */           }
/*    */ 
/*    */           
/*    */           public void mouseExited(MouseEvent e) {
/* 32 */             RoundImageButton.this.current = image;
/* 33 */             RoundImageButton.this.repaint();
/*    */           }
/*    */         });
/*    */   }
/*    */   private BufferedImage current;
/*    */   public RoundImageButton(String image, String mouseUnderUrl) {
/* 39 */     this(ImageCache.loadImage(U.makeURL(image), true), 
/* 40 */         ImageCache.loadImage(U.makeURL(mouseUnderUrl), true));
/*    */   }
/*    */   
/*    */   public Dimension getImageSize() {
/* 44 */     return new Dimension(this.current.getWidth(), this.current.getHeight());
/*    */   }
/*    */ 
/*    */   
/*    */   protected void paintComponent(Graphics g) {
/* 49 */     g.drawImage(this.current, 0, 0, null);
/* 50 */     String text = getText();
/* 51 */     if (!Strings.isNullOrEmpty(text)) {
/* 52 */       SwingUtil.paintText((Graphics2D)g, (JComponent)this, text);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public Dimension getPreferredSize() {
/* 58 */     return new Dimension(this.current.getWidth(), this.current.getHeight());
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\button\RoundImageButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */