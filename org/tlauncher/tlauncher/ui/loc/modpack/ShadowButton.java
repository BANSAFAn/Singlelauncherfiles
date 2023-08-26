/*    */ package org.tlauncher.tlauncher.ui.loc.modpack;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Rectangle;
/*    */ import javax.swing.ButtonModel;
/*    */ import org.tlauncher.tlauncher.ui.loc.UpdaterFullButton;
/*    */ import org.tlauncher.util.SwingUtil;
/*    */ 
/*    */ public class ShadowButton extends UpdaterFullButton {
/*    */   public ShadowButton(Color color, Color mouseUnder, String value, String image) {
/* 12 */     super(color, mouseUnder, value, image);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void paintBackground(Rectangle rec, Graphics g) {
/* 19 */     ButtonModel buttonModel = getModel();
/* 20 */     if (buttonModel.isRollover()) {
/* 21 */       g.setColor(this.unEnableColor);
/*    */     } else {
/* 23 */       g.setColor(this.backgroundColor);
/*    */     } 
/* 25 */     SwingUtil.paintShadowLine(rec, g, getBackground().getRed(), 12);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\loc\modpack\ShadowButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */