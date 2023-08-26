/*    */ package org.tlauncher.tlauncher.ui.swing;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Graphics;
/*    */ import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
/*    */ 
/*    */ 
/*    */ public class Del
/*    */   extends ExtendedPanel
/*    */ {
/*    */   private static final int TOP = -1;
/*    */   public static final int CENTER = 0;
/*    */   private static final int BOTTOM = 1;
/*    */   private static final long serialVersionUID = -2252007533187803762L;
/*    */   private int size;
/*    */   private int aligment;
/*    */   private Color color;
/*    */   
/*    */   public Del(int size, int aligment, Color color) {
/* 21 */     this.size = size;
/* 22 */     this.aligment = aligment;
/* 23 */     this.color = color;
/*    */   }
/*    */   
/*    */   public Del(int size, int aligment, int width, int height, Color color) {
/* 27 */     this.size = size;
/* 28 */     this.aligment = aligment;
/* 29 */     this.color = color;
/*    */     
/* 31 */     setPreferredSize(new Dimension(width, height));
/*    */   }
/*    */ 
/*    */   
/*    */   public void paint(Graphics g) {
/* 36 */     g.setColor(this.color);
/* 37 */     switch (this.aligment) {
/*    */       case -1:
/* 39 */         g.fillRect(0, 0, getWidth(), this.size);
/*    */         break;
/*    */       case 0:
/* 42 */         g.fillRect(0, getHeight() / 2 - this.size, getWidth(), this.size);
/*    */         break;
/*    */       case 1:
/* 45 */         g.fillRect(0, getHeight() - this.size, getWidth(), this.size);
/*    */         break;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\Del.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */