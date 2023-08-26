/*    */ package org.tlauncher.tlauncher.ui.swing;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Component;
/*    */ import javax.swing.BorderFactory;
/*    */ import javax.swing.DefaultListCellRenderer;
/*    */ import javax.swing.JLabel;
/*    */ import javax.swing.JList;
/*    */ import org.tlauncher.tlauncher.ui.converter.StringConverter;
/*    */ 
/*    */ public class DefaultConverterCellRenderer<T>
/*    */   extends ConverterCellRenderer<T>
/*    */ {
/*    */   private final DefaultListCellRenderer defaultRenderer;
/* 15 */   public static final Color DARK_COLOR_TEXT = new Color(77, 77, 77);
/* 16 */   public static final Color OVER_ITEM = new Color(235, 235, 235);
/*    */   
/*    */   public DefaultConverterCellRenderer(StringConverter<T> converter) {
/* 19 */     super(converter);
/*    */     
/* 21 */     this.defaultRenderer = new DefaultListCellRenderer();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Component getListCellRendererComponent(JList<? extends T> list, T value, int index, boolean isSelected, boolean cellHasFocus) {
/* 28 */     JLabel renderer = (JLabel)this.defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
/*    */     
/* 30 */     if (isSelected) {
/* 31 */       renderer.setBackground(OVER_ITEM);
/*    */     } else {
/* 33 */       renderer.setBackground(Color.white);
/*    */     } 
/* 35 */     renderer.setForeground(DARK_COLOR_TEXT);
/* 36 */     renderer.setOpaque(true);
/* 37 */     renderer.setText(this.converter.toString(value));
/* 38 */     renderer.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 0));
/* 39 */     return renderer;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\DefaultConverterCellRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */