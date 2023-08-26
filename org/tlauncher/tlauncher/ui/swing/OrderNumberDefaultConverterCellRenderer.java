/*    */ package org.tlauncher.tlauncher.ui.swing;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import javax.swing.JLabel;
/*    */ import javax.swing.JList;
/*    */ import org.tlauncher.tlauncher.ui.converter.StringConverter;
/*    */ 
/*    */ 
/*    */ public class OrderNumberDefaultConverterCellRenderer<T>
/*    */   extends DefaultConverterCellRenderer<T>
/*    */ {
/*    */   public OrderNumberDefaultConverterCellRenderer(StringConverter<T> converter) {
/* 13 */     super(converter);
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getListCellRendererComponent(JList<? extends T> list, T value, int index, boolean isSelected, boolean cellHasFocus) {
/* 18 */     JLabel l = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
/* 19 */     l.setText((index + 1) + ") " + l.getText());
/* 20 */     return l;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\OrderNumberDefaultConverterCellRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */