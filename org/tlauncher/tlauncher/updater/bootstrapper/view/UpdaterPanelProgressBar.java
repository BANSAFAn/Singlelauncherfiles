/*    */ package org.tlauncher.tlauncher.updater.bootstrapper.view;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Graphics2D;
/*    */ import java.awt.Insets;
/*    */ import java.awt.RenderingHints;
/*    */ import javax.swing.JPanel;
/*    */ import org.tlauncher.util.U;
/*    */ 
/*    */ 
/*    */ public class UpdaterPanelProgressBar
/*    */   extends JPanel
/*    */ {
/*    */   private static final long serialVersionUID = -8469500310564854471L;
/* 16 */   protected Insets insets = new Insets(5, 10, 10, 10);
/* 17 */   protected Color background = new Color(255, 255, 255, 220);
/* 18 */   private final Color border = new Color(255, 255, 255, 255);
/*    */ 
/*    */   
/*    */   public void paintComponent(Graphics g0) {
/* 22 */     Graphics2D g = (Graphics2D)g0;
/* 23 */     int arc = 16;
/*    */     
/* 25 */     g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
/*    */ 
/*    */     
/* 28 */     g.setColor(this.background);
/* 29 */     g.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
/*    */     
/* 31 */     g.setColor(this.border);
/* 32 */     for (int x = 1; x < 2; x++) {
/* 33 */       g.drawRoundRect(x - 1, x - 1, getWidth() - 2 * x + 1, 
/* 34 */           getHeight() - 2 * x + 1, arc, arc);
/*    */     }
/*    */     
/* 37 */     Color shadow = U.shiftAlpha(Color.gray, -200);
/*    */     
/* 39 */     for (int i = 2;; i++) {
/* 40 */       shadow = U.shiftAlpha(shadow, -8);
/*    */       
/* 42 */       if (shadow.getAlpha() == 0) {
/*    */         break;
/*    */       }
/* 45 */       g.setColor(shadow);
/* 46 */       g.drawRoundRect(i - 1, i - 1, getWidth() - 2 * i + 1, 
/* 47 */           getHeight() - 2 * i + 1, arc - 2 * i + 1, arc - 2 * i + 1);
/*    */     } 
/*    */ 
/*    */     
/* 51 */     g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
/*    */ 
/*    */     
/* 54 */     super.paintComponent(g0);
/*    */   }
/*    */ 
/*    */   
/*    */   public Insets getInsets() {
/* 59 */     return this.insets;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\updater\bootstrapper\view\UpdaterPanelProgressBar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */