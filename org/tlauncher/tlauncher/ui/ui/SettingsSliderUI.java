/*    */ package org.tlauncher.tlauncher.ui.ui;
/*    */ import java.awt.Color;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Rectangle;
/*    */ 
/*    */ public class SettingsSliderUI extends BasicSliderUI {
/*  8 */   public static Color TRACK_LEFT = new Color(0, 174, 239);
/*  9 */   public static Color TRACK_RIGHT = new Color(181, 181, 181);
/* 10 */   public static Color THUMB_COLOR = new Color(0, 174, 239);
/* 11 */   public static Color SERIFS = new Color(37, 37, 37);
/*    */   
/*    */   private final JSlider jSlider;
/*    */   
/*    */   public SettingsSliderUI(JSlider b) {
/* 16 */     super(b);
/* 17 */     this.jSlider = b;
/* 18 */     uninstallListeners(this.jSlider);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected Dimension getThumbSize() {
/* 24 */     Dimension size = new Dimension();
/*    */     
/* 26 */     size.width = 19;
/* 27 */     size.height = 19;
/*    */     
/* 29 */     return size;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void paintTrack(Graphics g) {
/* 35 */     Rectangle trackBounds = this.trackRect;
/* 36 */     int cy = trackBounds.height / 2 - 2;
/* 37 */     int cw = trackBounds.width;
/* 38 */     g.translate(trackBounds.x, trackBounds.y + cy);
/*    */     
/* 40 */     g.setColor(TRACK_LEFT);
/* 41 */     g.fillRect(0, 0, this.thumbRect.x, 2);
/*    */     
/* 43 */     g.setColor(TRACK_RIGHT);
/* 44 */     g.fillRect(this.thumbRect.x, 0, this.trackRect.width - this.thumbRect.x, 2);
/*    */ 
/*    */     
/* 47 */     int major = this.jSlider.getMajorTickSpacing();
/* 48 */     int width = this.jSlider.getMaximum() - this.jSlider.getMinimum();
/* 49 */     int count = width / major;
/* 50 */     g.setColor(SERIFS);
/* 51 */     for (int i = 0; i < count; i++) {
/* 52 */       g.fillRect(i * cw / count, 0, 2, 2);
/*    */     }
/* 54 */     if (count == 0) {
/* 55 */       g.fillRect(0, 0, 2, 2);
/*    */     } else {
/* 57 */       g.fillRect(count * cw / count, 0, 2, 2);
/* 58 */     }  g.translate(-trackBounds.x, -(trackBounds.y + cy));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void paintHorizontalLabel(Graphics g, int value, Component label) {
/* 63 */     label.setForeground(new Color(96, 96, 96));
/* 64 */     super.paintHorizontalLabel(g, value, label);
/*    */   }
/*    */ 
/*    */   
/*    */   public void paintThumb(Graphics g) {
/* 69 */     Rectangle knobBounds = this.thumbRect;
/* 70 */     Graphics2D graphics2d = (Graphics2D)g;
/* 71 */     graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
/* 72 */     g.setColor(THUMB_COLOR);
/* 73 */     g.fillOval(knobBounds.x, knobBounds.y, knobBounds.width, knobBounds.height);
/* 74 */     graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
/*    */   }
/*    */   
/*    */   public void paintFocus(Graphics g) {}
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\u\\ui\SettingsSliderUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */