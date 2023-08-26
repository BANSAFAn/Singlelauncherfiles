/*    */ package org.tlauncher.tlauncher.ui.util;
/*    */ 
/*    */ import java.awt.Dimension;
/*    */ import javax.swing.BoxLayout;
/*    */ import javax.swing.JFrame;
/*    */ import javax.swing.JLabel;
/*    */ import javax.swing.JPanel;
/*    */ import javax.swing.text.JTextComponent;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ViewlUtil
/*    */ {
/*    */   public static final int minCountCharater = 12;
/*    */   
/*    */   public static Dimension calculateSizeReview(JFrame parent) {
/* 17 */     Dimension dimension = parent.getSize();
/* 18 */     dimension.setSize(dimension.getWidth() * 2.0D / 3.0D, dimension.getHeight() * 4.0D / 5.0D + 10.0D);
/* 19 */     return dimension;
/*    */   }
/*    */   public static JPanel createBoxYPanel(JLabel label, JTextComponent component) {
/* 22 */     JPanel p = new JPanel();
/* 23 */     p.setAlignmentX(0.0F);
/* 24 */     BoxLayout layout = new BoxLayout(p, 1);
/* 25 */     p.setLayout(layout);
/* 26 */     label.setAlignmentX(0.0F);
/* 27 */     component.setAlignmentY(0.0F);
/* 28 */     p.add(label);
/* 29 */     p.add(component);
/* 30 */     return p;
/*    */   }
/*    */   
/*    */   public static String addSpaces(String line, String serverName) {
/* 34 */     int addedSpaces = 12 - serverName.length();
/* 35 */     if (addedSpaces <= 0) {
/* 36 */       return line;
/*    */     }
/* 38 */     StringBuilder builder = new StringBuilder(line);
/* 39 */     for (int i = 0; i < addedSpaces; i++) {
/* 40 */       builder.append(' ');
/*    */     }
/* 42 */     return builder.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\u\\util\ViewlUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */