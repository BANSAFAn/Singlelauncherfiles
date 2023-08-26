/*    */ package org.tlauncher.tlauncher.ui.swing.renderer;
/*    */ import java.awt.Component;
/*    */ import java.awt.Dimension;
/*    */ import javax.swing.BorderFactory;
/*    */ import javax.swing.DefaultListCellRenderer;
/*    */ import javax.swing.ImageIcon;
/*    */ import javax.swing.JLabel;
/*    */ import javax.swing.JList;
/*    */ 
/*    */ public class PictureListRenderer extends DefaultListCellRenderer {
/*    */   public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
/* 12 */     int gup = 20, width = 294;
/* 13 */     JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
/* 14 */     label.setIcon((ImageIcon)value);
/* 15 */     label.setPreferredSize(new Dimension(width + gup, 190));
/* 16 */     label.setText("");
/* 17 */     label.setOpaque(false);
/* 18 */     if (index == 2) {
/* 19 */       label.setPreferredSize(new Dimension(width, 190));
/*    */     } else {
/* 21 */       label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, gup));
/*    */     } 
/* 23 */     return label;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\renderer\PictureListRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */