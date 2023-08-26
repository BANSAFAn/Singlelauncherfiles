/*    */ package org.tlauncher.tlauncher.ui.modpack.right.panel;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.swing.table.AbstractTableModel;
/*    */ import org.tlauncher.modpack.domain.client.GameEntityDTO;
/*    */ 
/*    */ public class RightTableModel<T extends GameEntityDTO>
/*    */   extends AbstractTableModel
/*    */ {
/*    */   private static final long serialVersionUID = -4513723800530818L;
/* 12 */   private List<T> data = new ArrayList<>();
/*    */ 
/*    */   
/*    */   public int getRowCount() {
/* 16 */     return this.data.size();
/*    */   }
/*    */ 
/*    */   
/*    */   public int getColumnCount() {
/* 21 */     return 1;
/*    */   }
/*    */ 
/*    */   
/*    */   public T getValueAt(int rowIndex, int columnIndex) {
/* 26 */     return this.data.get(rowIndex);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isCellEditable(int rowIndex, int columnIndex) {
/* 31 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<?> getColumnClass(int columnIndex) {
/* 36 */     return GameEntityDTO.class;
/*    */   }
/*    */   public void addElements(List<T> list, boolean clean) {
/* 39 */     if (clean)
/* 40 */       this.data.clear(); 
/* 41 */     this.data.addAll(list);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\modpack\right\panel\RightTableModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */