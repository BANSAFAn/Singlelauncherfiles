/*    */ package org.tlauncher.tlauncher.ui.swing.renderer;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Component;
/*    */ import java.awt.Dimension;
/*    */ import java.util.Objects;
/*    */ import javax.swing.BorderFactory;
/*    */ import javax.swing.DefaultListCellRenderer;
/*    */ import javax.swing.JLabel;
/*    */ import javax.swing.JList;
/*    */ import org.tlauncher.tlauncher.ui.ui.CreationModpackComboBoxUI;
/*    */ import org.tlauncher.util.ColorUtil;
/*    */ import org.tlauncher.util.SwingUtil;
/*    */ import org.tlauncher.util.swing.FontTL;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class ModpackComboBoxRendererBasic
/*    */   extends DefaultListCellRenderer
/*    */ {
/*    */   private static final long serialVersionUID = 2710505952547859346L;
/* 25 */   public static final Color LINE = new Color(149, 149, 149);
/*    */   
/*    */   static final int GUP_LEFT = 13;
/* 28 */   public static final Color TEXT_COLOR = new Color(25, 25, 25);
/*    */ 
/*    */ 
/*    */   
/*    */   public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
/* 33 */     JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
/* 34 */     if (label == null) {
/* 35 */       return null;
/*    */     }
/* 37 */     SwingUtil.changeFontFamily(label, FontTL.ROBOTO_MEDIUM, 14, ColorUtil.COLOR_25);
/*    */     
/* 39 */     label.setPreferredSize(new Dimension(238, 44));
/* 40 */     label.setOpaque(true);
/* 41 */     label.setBackground(Color.WHITE);
/* 42 */     label.setForeground(TEXT_COLOR);
/* 43 */     if (isSelected && index != -1) {
/* 44 */       label.setBackground(new Color(235, 235, 235));
/*    */     } else {
/* 46 */       label.setBackground(Color.white);
/*    */     } 
/* 48 */     label.setBorder(BorderFactory.createEmptyBorder(0, 13, 0, 0));
/*    */     
/* 50 */     label.setText(Objects.isNull(value) ? "" : getRenderText(value));
/* 51 */     CreationModpackComboBoxUI.PositionIcon pi = getRenderIcon(value);
/* 52 */     if (Objects.nonNull(pi)) {
/* 53 */       label.setIcon(pi.getIcon());
/* 54 */       label.setHorizontalTextPosition(pi.getIconPosition());
/*    */     } 
/* 56 */     return label;
/*    */   }
/*    */   
/*    */   public abstract String getRenderText(Object paramObject);
/*    */   
/*    */   public CreationModpackComboBoxUI.PositionIcon getRenderIcon(Object value) {
/* 62 */     return CreationModpackComboBoxUI.getRenderIcon(value);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\renderer\ModpackComboBoxRendererBasic.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */