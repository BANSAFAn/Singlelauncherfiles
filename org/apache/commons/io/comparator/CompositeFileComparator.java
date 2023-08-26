/*     */ package org.apache.commons.io.comparator;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CompositeFileComparator
/*     */   extends AbstractFileComparator
/*     */   implements Serializable
/*     */ {
/*  46 */   private static final Comparator<?>[] EMPTY_COMPARATOR_ARRAY = (Comparator<?>[])new Comparator[0];
/*     */   private static final long serialVersionUID = -2224170307287243428L;
/*  48 */   private static final Comparator<?>[] NO_COMPARATORS = (Comparator<?>[])new Comparator[0];
/*     */ 
/*     */ 
/*     */   
/*     */   private final Comparator<File>[] delegates;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompositeFileComparator(Comparator<File>... delegates) {
/*  58 */     if (delegates == null) {
/*  59 */       this.delegates = (Comparator<File>[])NO_COMPARATORS;
/*     */     } else {
/*  61 */       this.delegates = (Comparator<File>[])new Comparator[delegates.length];
/*  62 */       System.arraycopy(delegates, 0, this.delegates, 0, delegates.length);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompositeFileComparator(Iterable<Comparator<File>> delegates) {
/*  73 */     if (delegates == null) {
/*  74 */       this.delegates = (Comparator<File>[])NO_COMPARATORS;
/*     */     } else {
/*  76 */       List<Comparator<File>> list = new ArrayList<>();
/*  77 */       for (Comparator<File> comparator : delegates) {
/*  78 */         list.add(comparator);
/*     */       }
/*  80 */       this.delegates = (Comparator<File>[])list.<Comparator<?>>toArray(EMPTY_COMPARATOR_ARRAY);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compare(File file1, File file2) {
/*  94 */     int result = 0;
/*  95 */     for (Comparator<File> delegate : this.delegates) {
/*  96 */       result = delegate.compare(file1, file2);
/*  97 */       if (result != 0) {
/*     */         break;
/*     */       }
/*     */     } 
/* 101 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 111 */     StringBuilder builder = new StringBuilder();
/* 112 */     builder.append(super.toString());
/* 113 */     builder.append('{');
/* 114 */     for (int i = 0; i < this.delegates.length; i++) {
/* 115 */       if (i > 0) {
/* 116 */         builder.append(',');
/*     */       }
/* 118 */       builder.append(this.delegates[i]);
/*     */     } 
/* 120 */     builder.append('}');
/* 121 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\comparator\CompositeFileComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */