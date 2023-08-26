/*    */ package org.tlauncher.tlauncher.ui.swing.extended;
/*    */ 
/*    */ import java.awt.AlphaComposite;
/*    */ import java.awt.Composite;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Graphics2D;
/*    */ import javax.swing.Icon;
/*    */ import javax.swing.JLabel;
/*    */ import org.tlauncher.tlauncher.ui.TLauncherFrame;
/*    */ 
/*    */ 
/*    */ public class ExtendedLabel
/*    */   extends JLabel
/*    */ {
/* 15 */   private static final AlphaComposite disabledAlphaComposite = AlphaComposite.getInstance(3, 0.5F);
/*    */   
/*    */   public ExtendedLabel(String text, Icon icon, int horizontalAlignment) {
/* 18 */     super(text, icon, horizontalAlignment);
/* 19 */     setFont(getFont().deriveFont(TLauncherFrame.fontSize));
/* 20 */     setOpaque(false);
/*    */   }
/*    */   
/*    */   public ExtendedLabel(String text, int horizontalAlignment) {
/* 24 */     this(text, (Icon)null, horizontalAlignment);
/*    */   }
/*    */   
/*    */   public ExtendedLabel(String text) {
/* 28 */     this(text, (Icon)null, 10);
/*    */   }
/*    */   
/*    */   public ExtendedLabel(Icon image, int horizontalAlignment) {
/* 32 */     this((String)null, image, horizontalAlignment);
/*    */   }
/*    */   
/*    */   public ExtendedLabel(Icon image) {
/* 36 */     this((String)null, image, 0);
/*    */   }
/*    */   
/*    */   public ExtendedLabel() {
/* 40 */     this((String)null, (Icon)null, 10);
/*    */   }
/*    */ 
/*    */   
/*    */   public void paintComponent(Graphics g0) {
/* 45 */     if (isEnabled()) {
/* 46 */       super.paintComponent(g0);
/*    */       
/*    */       return;
/*    */     } 
/* 50 */     Graphics2D g = (Graphics2D)g0;
/* 51 */     Composite oldComposite = g.getComposite();
/*    */     
/* 53 */     g.setComposite(disabledAlphaComposite);
/* 54 */     super.paintComponent(g);
/* 55 */     g.setComposite(oldComposite);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\extended\ExtendedLabel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */