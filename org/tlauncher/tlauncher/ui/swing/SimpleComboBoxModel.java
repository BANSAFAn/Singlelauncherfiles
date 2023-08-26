/*     */ package org.tlauncher.tlauncher.ui.swing;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Vector;
/*     */ import javax.swing.DefaultComboBoxModel;
/*     */ 
/*     */ public class SimpleComboBoxModel<E>
/*     */   extends DefaultComboBoxModel<E>
/*     */ {
/*     */   private static final long serialVersionUID = 5950434966721171811L;
/*     */   protected List<E> objects;
/*     */   protected Object selectedObject;
/*     */   
/*     */   public SimpleComboBoxModel() {
/*  19 */     this.objects = Collections.synchronizedList(new ArrayList<>());
/*     */   }
/*     */   
/*     */   public SimpleComboBoxModel(E[] items) {
/*  23 */     this.objects = Collections.synchronizedList(new ArrayList<>());
/*     */ 
/*     */     
/*  26 */     for (int i = 0, c = items.length; i < c; i++) {
/*  27 */       this.objects.add(items[i]);
/*     */     }
/*  29 */     if (getSize() > 0)
/*  30 */       this.selectedObject = getElementAt(0); 
/*     */   }
/*     */   
/*     */   public SimpleComboBoxModel(Vector<E> v) {
/*  34 */     this.objects = v;
/*     */     
/*  36 */     if (getSize() > 0) {
/*  37 */       this.selectedObject = getElementAt(0);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setSelectedItem(Object anObject) {
/*  42 */     if ((this.selectedObject != null && 
/*  43 */       !this.selectedObject.equals(anObject)) || (this.selectedObject == null && anObject != null)) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  49 */       this.selectedObject = anObject;
/*  50 */       fireContentsChanged(this, -1, -1);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getSelectedItem() {
/*  56 */     return this.selectedObject;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSize() {
/*  61 */     return this.objects.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public E getElementAt(int index) {
/*  66 */     if (index >= 0 && index < this.objects.size())
/*  67 */       return this.objects.get(index); 
/*  68 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getIndexOf(Object anObject) {
/*  73 */     return this.objects.indexOf(anObject);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addElement(E anObject) {
/*  78 */     this.objects.add(anObject);
/*     */     
/*  80 */     int size = this.objects.size(), index = this.objects.size() - 1;
/*  81 */     fireIntervalAdded(this, index, index);
/*     */     
/*  83 */     if (size == 1 && this.selectedObject == null && anObject != null)
/*  84 */       setSelectedItem(anObject); 
/*     */   }
/*     */   
/*     */   public void addElements(Collection<E> list) {
/*  88 */     if (list.size() == 0) {
/*     */       return;
/*     */     }
/*     */     
/*  92 */     int size = list.size();
/*  93 */     int index0 = this.objects.size();
/*  94 */     int index1 = index0 + size - 1;
/*     */     
/*  96 */     this.objects.addAll(list);
/*  97 */     fireIntervalAdded(this, index0, index1);
/*     */     
/*  99 */     if (this.selectedObject == null) {
/*     */       
/* 101 */       Iterator<E> iterator = list.iterator();
/*     */ 
/*     */       
/* 104 */       while (iterator.hasNext()) {
/* 105 */         E elem = iterator.next();
/*     */         
/* 107 */         if (elem == null) {
/*     */           continue;
/*     */         }
/* 110 */         setSelectedItem(elem);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void insertElementAt(E anObject, int index) {
/* 118 */     this.objects.add(index, anObject);
/* 119 */     fireIntervalAdded(this, index, index);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeElementAt(int index) {
/* 124 */     if (getElementAt(index) == this.selectedObject)
/*     */     {
/* 126 */       if (index == 0) {
/* 127 */         setSelectedItem((getSize() == 1) ? null : getElementAt(index + 1));
/*     */       } else {
/* 129 */         setSelectedItem(getElementAt(index - 1));
/*     */       } 
/*     */     }
/*     */     
/* 133 */     this.objects.remove(index);
/* 134 */     fireIntervalRemoved(this, index, index);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeElement(Object anObject) {
/* 139 */     int index = this.objects.indexOf(anObject);
/*     */     
/* 141 */     if (index != -1) {
/* 142 */       removeElementAt(index);
/*     */     }
/*     */   }
/*     */   
/*     */   public void removeAllElements() {
/* 147 */     int size = this.objects.size();
/*     */     
/* 149 */     if (size > 0) {
/* 150 */       int firstIndex = 0;
/* 151 */       int lastIndex = size - 1;
/*     */       
/* 153 */       this.objects.clear();
/*     */       
/* 155 */       this.selectedObject = null;
/* 156 */       fireIntervalRemoved(this, firstIndex, lastIndex);
/*     */     } else {
/* 158 */       this.selectedObject = null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\SimpleComboBoxModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */