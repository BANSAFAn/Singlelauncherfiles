/*    */ package org.tlauncher.tlauncher.ui.swing.renderer;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import javax.swing.AbstractCellEditor;
/*    */ import javax.swing.JTable;
/*    */ import javax.swing.table.TableCellEditor;
/*    */ import javax.swing.table.TableCellRenderer;
/*    */ 
/*    */ public class JTableButtonRenderer
/*    */   extends AbstractCellEditor
/*    */   implements TableCellRenderer, TableCellEditor
/*    */ {
/*    */   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
/* 14 */     return (Component)value;
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
/* 19 */     return (Component)value;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getCellEditorValue() {
/* 24 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\renderer\JTableButtonRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */