/*    */ package org.tlauncher.tlauncher.ui.loc;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Graphics2D;
/*    */ import java.awt.RenderingHints;
/*    */ import java.awt.event.MouseAdapter;
/*    */ import java.awt.event.MouseEvent;
/*    */ import javax.swing.JComponent;
/*    */ 
/*    */ public class RoundUpdaterButton
/*    */   extends UpdaterButton {
/*    */   public static Color TEXT_COLOR;
/* 14 */   int ARC_SIZE = 10;
/*    */   int i;
/* 16 */   public RoundUpdaterButton(Color colorText, final Color background, final Color mouseUnder, String value) { super(background, value);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 30 */     this.i = 0; setOpaque(false); TEXT_COLOR = colorText;
/*    */     addMouseListener(new MouseAdapter() {
/*    */           public void mouseEntered(MouseEvent e) { RoundUpdaterButton.this.setBackground(mouseUnder); } public void mouseExited(MouseEvent e) { RoundUpdaterButton.this.setBackground(background); }
/* 33 */         }); } protected void paintComponent(Graphics g0) { int x = 0;
/* 34 */     Graphics2D g = (Graphics2D)g0;
/* 35 */     g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
/*    */     
/* 37 */     g.setColor(getBackground());
/* 38 */     g.fillRoundRect(x, x, getWidth(), getHeight(), this.ARC_SIZE, this.ARC_SIZE);
/*    */     
/* 40 */     g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
/* 41 */     g0.setColor(TEXT_COLOR);
/* 42 */     paintText(g0, (JComponent)this, getVisibleRect(), getText()); }
/*    */ 
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\loc\RoundUpdaterButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */