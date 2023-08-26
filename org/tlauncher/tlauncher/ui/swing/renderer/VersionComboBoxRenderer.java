/*    */ package org.tlauncher.tlauncher.ui.swing.renderer;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import javax.swing.DefaultListCellRenderer;
/*    */ import javax.swing.JLabel;
/*    */ import javax.swing.JList;
/*    */ import net.minecraft.launcher.versions.CompleteVersion;
/*    */ 
/*    */ public class VersionComboBoxRenderer
/*    */   extends DefaultListCellRenderer {
/*    */   public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
/* 12 */     JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
/*    */     
/* 14 */     if (value != null)
/* 15 */       label.setText(((CompleteVersion)value).getID()); 
/* 16 */     return label;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\renderer\VersionComboBoxRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */