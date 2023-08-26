/*    */ package org.tlauncher.tlauncher.ui.swing;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Cursor;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.GradientPaint;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Graphics2D;
/*    */ import java.awt.RenderingHints;
/*    */ import javax.swing.ButtonModel;
/*    */ import javax.swing.JButton;
/*    */ 
/*    */ public class TransparentButton
/*    */   extends JButton
/*    */ {
/*    */   private static final long serialVersionUID = -5329305793566047719L;
/*    */   
/*    */   protected TransparentButton() {
/* 19 */     setBorderPainted(false);
/* 20 */     setContentAreaFilled(false);
/* 21 */     setFocusPainted(false);
/* 22 */     setOpaque(false);
/* 23 */     setForeground(Color.white);
/* 24 */     setPreferredSize(new Dimension(27, 27));
/* 25 */     setCursor(Cursor.getPredefinedCursor(12));
/*    */   }
/*    */   
/*    */   public TransparentButton(String text) {
/* 29 */     this();
/* 30 */     setText(text);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void paintComponent(Graphics g) {
/* 35 */     ButtonModel buttonModel = getModel();
/* 36 */     Graphics2D gd = (Graphics2D)g.create();
/*    */     
/* 38 */     gd.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
/*    */     
/* 40 */     gd.setPaint(new GradientPaint(0.0F, 0.0F, Color.decode("#67c7f4"), 0.0F, 
/* 41 */           getHeight(), Color.decode("#379fc9")));
/*    */     
/* 43 */     if (buttonModel.isRollover()) {
/* 44 */       gd.setPaint(new GradientPaint(0.0F, 0.0F, Color.decode("#7bd2f6"), 0.0F, 
/* 45 */             getHeight(), Color.decode("#43b3d5")));
/* 46 */       if (buttonModel.isPressed()) {
/* 47 */         gd.setPaint(new GradientPaint(0.0F, 0.0F, Color.decode("#379fc9"), 0.0F, 
/* 48 */               getHeight(), Color.decode("#4fb2dd")));
/*    */       } else {
/* 50 */         setForeground(Color.white);
/*    */       } 
/*    */     } 
/* 53 */     gd.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
/* 54 */     gd.dispose();
/* 55 */     super.paintComponent(g);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\TransparentButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */