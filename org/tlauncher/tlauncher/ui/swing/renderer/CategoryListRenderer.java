/*    */ package org.tlauncher.tlauncher.ui.swing.renderer;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Component;
/*    */ import java.util.Set;
/*    */ import javax.swing.BorderFactory;
/*    */ import javax.swing.JComboBox;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.JList;
/*    */ import javax.swing.border.CompoundBorder;
/*    */ import org.tlauncher.modpack.domain.client.share.CategoryDTO;
/*    */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*    */ import org.tlauncher.tlauncher.ui.model.CategoryComboBoxModel;
/*    */ 
/*    */ public class CategoryListRenderer
/*    */   extends ModpackComboBoxRendererBasic {
/*    */   private JComboBox<CategoryDTO> categoriesBox;
/*    */   
/*    */   public CategoryListRenderer(JComboBox<CategoryDTO> categoriesBox) {
/* 20 */     this.categoriesBox = categoriesBox;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
/* 27 */     if (value == null) {
/* 28 */       return null;
/*    */     }
/* 30 */     Set<CategoryDTO> set = ((CategoryComboBoxModel)this.categoriesBox.getModel()).getSelectedCategories();
/* 31 */     CategoryDTO cat = (CategoryDTO)value;
/* 32 */     JComponent c = (JComponent)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
/* 33 */     if (set.contains(cat)) {
/* 34 */       c.setBackground(new Color(225, 225, 225));
/*    */     }
/*    */     
/* 37 */     if (cat.getNesting() != 0) {
/*    */       
/* 39 */       CompoundBorder border = new CompoundBorder(c.getBorder(), BorderFactory.createEmptyBorder(0, (cat.getNesting() == 1) ? 15 : 30, 0, 0));
/* 40 */       c.setBorder(border);
/*    */     } 
/* 42 */     return c;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getRenderText(Object value) {
/* 47 */     CategoryDTO cat = (CategoryDTO)value;
/* 48 */     return Localizable.get("modpack." + cat.getName());
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\renderer\CategoryListRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */