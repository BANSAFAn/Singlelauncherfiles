/*    */ package org.tlauncher.tlauncher.ui.review;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Graphics;
/*    */ import javax.swing.JButton;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.JFrame;
/*    */ import javax.swing.JProgressBar;
/*    */ 
/*    */ 
/*    */ public class ProgressBar
/*    */   extends JFrame
/*    */ {
/*    */   private JProgressBar progress;
/*    */   private JFrame parent;
/*    */   
/*    */   public ProgressBar(JFrame parent, JButton cancel) {
/* 19 */     this.parent = parent;
/* 20 */     this.progress = new JProgressBar();
/* 21 */     this.progress.setPreferredSize(new Dimension(180, 20));
/* 22 */     this.progress.setIndeterminate(true);
/*    */     
/* 24 */     setUndecorated(true);
/* 25 */     setSize(180, 75);
/* 26 */     add(this.progress, "Center");
/* 27 */     add(cancel, "South");
/* 28 */     parent.setGlassPane(new JComponent()
/*    */         {
/*    */           protected void paintComponent(Graphics g) {
/* 31 */             g.setColor(new Color(0, 0, 0, 50));
/* 32 */             g.fillRect(0, 0, getWidth(), getHeight());
/* 33 */             super.paintComponent(g);
/*    */           }
/*    */         });
/* 36 */     pack();
/*    */   }
/*    */ 
/*    */   
/*    */   public void setVisible(boolean b) {
/* 41 */     if (b) {
/* 42 */       setLocation(this.parent.getX() + this.parent.getWidth() / 2 - getWidth() / 2, this.parent
/* 43 */           .getY() + this.parent.getHeight() / 2 - getHeight() / 2);
/* 44 */       this.parent.setEnabled(false);
/*    */     } else {
/*    */       
/* 47 */       this.parent.setEnabled(true);
/*    */     } 
/* 49 */     this.parent.getGlassPane().setVisible(b);
/* 50 */     super.setVisible(b);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\review\ProgressBar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */