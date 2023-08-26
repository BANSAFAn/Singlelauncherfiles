/*    */ package org.tlauncher.tlauncher.ui.ui;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Graphics2D;
/*    */ import java.awt.Rectangle;
/*    */ 
/*    */ public class ModpackSliderUI extends SettingsSliderUI {
/*    */   public ModpackSliderUI(JSlider b) {
/*  8 */     super(b);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void paintThumb(Graphics g) {
/* 14 */     super.paintThumb(g);
/* 15 */     g.setColor(Color.BLACK);
/* 16 */     g.setFont(this.slider.getFont());
/* 17 */     Rectangle knobBounds = this.thumbRect;
/* 18 */     Graphics2D graphics2d = (Graphics2D)g;
/* 19 */     graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
/* 20 */     if (9999 < this.slider.getValue()) {
/* 21 */       graphics2d.drawString("" + this.slider.getValue(), knobBounds.x - 20, knobBounds.y - 8);
/* 22 */     } else if (9999 > this.slider.getValue() && 1000 <= this.slider.getValue()) {
/* 23 */       graphics2d.drawString("" + this.slider.getValue(), knobBounds.x - 10, knobBounds.y - 8);
/*    */     } else {
/* 25 */       graphics2d.drawString("" + this.slider.getValue(), knobBounds.x, knobBounds.y - 8);
/*    */     } 
/* 27 */     graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\u\\ui\ModpackSliderUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */