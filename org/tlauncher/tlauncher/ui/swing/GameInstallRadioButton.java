/*    */ package org.tlauncher.tlauncher.ui.swing;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Graphics2D;
/*    */ import java.awt.Rectangle;
/*    */ 
/*    */ public class GameInstallRadioButton
/*    */   extends GameRadioTextButton {
/*    */   public GameInstallRadioButton(String string) {
/* 10 */     super(string);
/* 11 */     this.defaultColor = Color.WHITE;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void paintBackground(Graphics2D g2, Rectangle rec) {
/* 16 */     if (getModel().isSelected()) {
/* 17 */       g2.setColor(this.defaultColor);
/*    */     } else {
/* 19 */       g2.setColor(getBackground());
/*    */     } 
/* 21 */     g2.fillRect(rec.x, rec.y, rec.width, rec.height);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\GameInstallRadioButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */