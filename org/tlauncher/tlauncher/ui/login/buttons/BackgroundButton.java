/*    */ package org.tlauncher.tlauncher.ui.login.buttons;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Graphics;
/*    */ import org.tlauncher.tlauncher.ui.loc.LocalizableButton;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BackgroundButton
/*    */   extends LocalizableButton
/*    */ {
/*    */   private Color defaultColor;
/*    */   private Color clickColor;
/*    */   
/*    */   public BackgroundButton(Color background) {
/* 16 */     setContentAreaFilled(false);
/* 17 */     setOpaque(true);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void paintComponent(Graphics g) {
/* 22 */     if (getModel().isPressed()) {
/* 23 */       g.setColor(this.clickColor);
/*    */     } else {
/* 25 */       g.setColor(this.defaultColor);
/*    */     } 
/* 27 */     g.fillRect(0, 0, getWidth(), getHeight());
/* 28 */     super.paintComponent(g);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\login\buttons\BackgroundButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */