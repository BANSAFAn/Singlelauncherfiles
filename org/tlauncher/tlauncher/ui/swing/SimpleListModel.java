/*    */ package org.tlauncher.tlauncher.ui.swing;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.Vector;
/*    */ import javax.swing.AbstractListModel;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SimpleListModel<E>
/*    */   extends AbstractListModel<E>
/*    */ {
/*    */   private static final long serialVersionUID = 727845864028652893L;
/* 18 */   protected final Vector<E> vector = new Vector<>();
/*    */ 
/*    */ 
/*    */   
/*    */   public int getSize() {
/* 23 */     return this.vector.size();
/*    */   }
/*    */ 
/*    */   
/*    */   public E getElementAt(int index) {
/* 28 */     if (index < 0 || index >= getSize())
/* 29 */       return null; 
/* 30 */     return this.vector.get(index);
/*    */   }
/*    */   
/*    */   public void add(E elem) {
/* 34 */     int index = this.vector.size();
/* 35 */     this.vector.add(elem);
/*    */     
/* 37 */     fireIntervalAdded(this, index, index);
/*    */   }
/*    */   
/*    */   public boolean remove(E elem) {
/* 41 */     int index = indexOf(elem);
/* 42 */     boolean rv = this.vector.removeElement(elem);
/*    */     
/* 44 */     if (index >= 0) {
/* 45 */       fireIntervalRemoved(this, index, index);
/*    */     }
/* 47 */     return rv;
/*    */   }
/*    */   
/*    */   public void addAll(Collection<E> elem) {
/* 51 */     int size = elem.size();
/* 52 */     if (size == 0) {
/*    */       return;
/*    */     }
/* 55 */     int index0 = this.vector.size();
/* 56 */     int index1 = index0 + size - 1;
/*    */     
/* 58 */     this.vector.addAll(elem);
/*    */     
/* 60 */     fireIntervalAdded(this, index0, index1);
/*    */   }
/*    */   
/*    */   public void clear() {
/* 64 */     int index1 = this.vector.size() - 1;
/* 65 */     this.vector.clear();
/*    */     
/* 67 */     if (index1 >= 0)
/* 68 */       fireIntervalRemoved(this, 0, index1); 
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 72 */     return this.vector.isEmpty();
/*    */   }
/*    */   
/*    */   public boolean contains(E elem) {
/* 76 */     return this.vector.contains(elem);
/*    */   }
/*    */   
/*    */   public int indexOf(E elem) {
/* 80 */     return this.vector.indexOf(elem);
/*    */   }
/*    */   
/*    */   public int indexOf(E elem, int index) {
/* 84 */     return this.vector.indexOf(elem, index);
/*    */   }
/*    */   
/*    */   public E elementAt(int index) {
/* 88 */     return this.vector.elementAt(index);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 93 */     return this.vector.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\SimpleListModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */