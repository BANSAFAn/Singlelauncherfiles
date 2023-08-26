/*    */ package org.tlauncher.tlauncher.ui.ui;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Rectangle;
/*    */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*    */ import org.tlauncher.tlauncher.ui.model.CategoryComboBoxModel;
/*    */ 
/*    */ public class CategoryComboBoxUI
/*    */   extends CreationModpackComboBoxUI
/*    */ {
/*    */   public String getText(Object value) {
/* 13 */     CategoryComboBoxModel ccbm = (CategoryComboBoxModel)value;
/*    */     
/* 15 */     if (!ccbm.getSelectedCategories().isEmpty()) {
/* 16 */       return Localizable.get("modpack.selected") + ": " + ccbm.getSelectedCategories().size();
/*    */     }
/* 18 */     return Localizable.get("modpack.all");
/*    */   }
/*    */ 
/*    */   
/*    */   public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
/* 23 */     g.setColor(Color.WHITE);
/* 24 */     g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
/* 25 */     paintText(g, bounds, getText(this.comboBox.getModel()));
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\u\\ui\CategoryComboBoxUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */