/*    */ package org.tlauncher.tlauncher.ui.background;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Graphics;
/*    */ import javax.swing.JComponent;
/*    */ import org.tlauncher.tlauncher.ui.swing.extended.ExtendedLayeredPane;
/*    */ 
/*    */ public abstract class Background
/*    */   extends ExtendedLayeredPane {
/*    */   private static final long serialVersionUID = -1353975966057230209L;
/*    */   protected Color coverColor;
/*    */   
/*    */   public Background(BackgroundHolder holder, Color coverColor) {
/* 14 */     super((JComponent)holder);
/*    */     
/* 16 */     this.coverColor = coverColor;
/*    */   }
/*    */   
/*    */   public Color getCoverColor() {
/* 20 */     return this.coverColor;
/*    */   }
/*    */ 
/*    */   
/*    */   public final void paint(Graphics g) {
/* 25 */     paintBackground(g);
/* 26 */     super.paint(g);
/*    */   }
/*    */   
/*    */   public abstract void paintBackground(Graphics paramGraphics);
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\background\Background.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */