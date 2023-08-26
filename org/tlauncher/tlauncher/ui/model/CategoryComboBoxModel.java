/*    */ package org.tlauncher.tlauncher.ui.model;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.HashSet;
/*    */ import java.util.Objects;
/*    */ import java.util.Set;
/*    */ import javax.swing.DefaultComboBoxModel;
/*    */ import javax.swing.SwingUtilities;
/*    */ import org.tlauncher.modpack.domain.client.share.CategoryDTO;
/*    */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CategoryComboBoxModel
/*    */   extends DefaultComboBoxModel<CategoryDTO>
/*    */ {
/*    */   private static final long serialVersionUID = -216867993953483715L;
/* 19 */   private Set<CategoryDTO> set = Collections.synchronizedSet(new HashSet<>());
/*    */   
/*    */   public CategoryComboBoxModel(CategoryDTO[] items) {
/* 22 */     super(items);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setSelectedItem(Object anObject) {
/* 27 */     if (Objects.nonNull(anObject)) {
/* 28 */       CategoryDTO c = (CategoryDTO)anObject;
/* 29 */       if (c.getId().longValue() == 0L)
/* 30 */       { this.set.clear(); }
/* 31 */       else { if (this.set.size() > 4 && !this.set.contains(c)) {
/* 32 */           SwingUtilities.invokeLater(() -> Alert.showLocMessageWithoutTitle("modpack.selected.so.much")); return;
/*    */         } 
/* 34 */         if (this.set.contains(c)) {
/* 35 */           this.set.remove(c);
/*    */         } else {
/* 37 */           this.set.add(c);
/*    */         }  }
/* 39 */        fireContentsChanged(this, 0, getSize());
/*    */     } 
/*    */     
/* 42 */     super.setSelectedItem((Object)null);
/*    */   }
/*    */   
/*    */   public Set<CategoryDTO> getSelectedCategories() {
/* 46 */     return this.set;
/*    */   }
/*    */   
/*    */   public void cleanAllSelection() {
/* 50 */     this.set.clear();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\model\CategoryComboBoxModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */