/*    */ package org.tlauncher.tlauncher.ui.listener.mods;
/*    */ 
/*    */ import java.awt.event.AdjustmentEvent;
/*    */ import java.awt.event.AdjustmentListener;
/*    */ import javax.swing.JScrollBar;
/*    */ import javax.swing.JTable;
/*    */ 
/*    */ public abstract class ModpackAdjustmentListener implements AdjustmentListener {
/*    */   private JTable table;
/*    */   
/*    */   public ModpackAdjustmentListener(JTable table) {
/* 12 */     this.table = table;
/* 13 */   } public void setTable(JTable table) { this.table = table; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof ModpackAdjustmentListener)) return false;  ModpackAdjustmentListener other = (ModpackAdjustmentListener)o; if (!other.canEqual(this)) return false;  Object this$table = getTable(), other$table = other.getTable(); return !((this$table == null) ? (other$table != null) : !this$table.equals(other$table)); } protected boolean canEqual(Object other) { return other instanceof ModpackAdjustmentListener; } public int hashCode() { int PRIME = 59; result = 1; Object $table = getTable(); return result * 59 + (($table == null) ? 43 : $table.hashCode()); } public String toString() { return "ModpackAdjustmentListener(table=" + getTable() + ")"; }
/*    */    public JTable getTable() {
/* 15 */     return this.table;
/*    */   }
/*    */   
/*    */   public void adjustmentValueChanged(AdjustmentEvent e) {
/* 19 */     if (!e.getValueIsAdjusting() && this.table.getModel().getRowCount() != 0) {
/* 20 */       JScrollBar scrollBar = (JScrollBar)e.getAdjustable();
/* 21 */       int extent = scrollBar.getModel().getExtent();
/* 22 */       int maximum = scrollBar.getModel().getMaximum();
/* 23 */       if (extent + e.getValue() == maximum)
/* 24 */         processed(); 
/*    */     } 
/*    */   }
/*    */   
/*    */   public abstract void processed();
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\listener\mods\ModpackAdjustmentListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */