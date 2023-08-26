/*    */ package org.tlauncher.tlauncher.ui.server;
/*    */ 
/*    */ import java.awt.BorderLayout;
/*    */ import java.awt.Color;
/*    */ import java.awt.Component;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.event.MouseAdapter;
/*    */ import java.awt.event.MouseEvent;
/*    */ import java.awt.event.MouseListener;
/*    */ import javax.swing.BorderFactory;
/*    */ import javax.swing.Icon;
/*    */ import javax.swing.JLabel;
/*    */ import javax.swing.JPanel;
/*    */ import org.tlauncher.tlauncher.ui.images.ImageIcon;
/*    */ import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
/*    */ 
/*    */ 
/*    */ public class BackPanel
/*    */   extends JPanel
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private final JLabel backLabel;
/* 23 */   public static final Color BACKGROUND_COLOR = new Color(60, 170, 232);
/*    */   
/*    */   public BackPanel(String titleName, MouseListener listener, ImageIcon icon) {
/* 26 */     this.backLabel = new JLabel((Icon)icon);
/* 27 */     setLayout(new BorderLayout(0, 0));
/* 28 */     setBackground(BACKGROUND_COLOR);
/*    */     
/* 30 */     this.backLabel.setPreferredSize(new Dimension(65, 25));
/* 31 */     this.backLabel.setBackground(new Color(46, 131, 177));
/* 32 */     this.backLabel.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));
/* 33 */     this.backLabel.addMouseListener(listener);
/* 34 */     add(this.backLabel, "West");
/* 35 */     LocalizableLabel label = new LocalizableLabel(titleName);
/*    */     
/* 37 */     label.setFont(label.getFont().deriveFont(1, 16.0F));
/* 38 */     label.setForeground(Color.WHITE);
/* 39 */     label.setHorizontalAlignment(0);
/* 40 */     add((Component)label, "Center");
/* 41 */     this.backLabel.addMouseListener(new MouseAdapter()
/*    */         {
/*    */           public void mouseEntered(MouseEvent e) {
/* 44 */             BackPanel.this.backLabel.setOpaque(true);
/* 45 */             BackPanel.this.backLabel.repaint();
/*    */           }
/*    */ 
/*    */           
/*    */           public void mouseExited(MouseEvent e) {
/* 50 */             BackPanel.this.backLabel.setOpaque(false);
/* 51 */             BackPanel.this.backLabel.repaint();
/*    */           }
/*    */         });
/*    */   }
/*    */   public void addBackListener(MouseListener listener) {
/* 56 */     this.backLabel.addMouseListener(listener);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\server\BackPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */