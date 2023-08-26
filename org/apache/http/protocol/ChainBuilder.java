/*     */ package org.apache.http.protocol;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Map;
/*     */ import org.apache.http.annotation.NotThreadSafe;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @NotThreadSafe
/*     */ final class ChainBuilder<E>
/*     */ {
/*  52 */   private final LinkedList<E> list = new LinkedList<E>();
/*  53 */   private final Map<Class<?>, E> uniqueClasses = new HashMap<Class<?>, E>();
/*     */ 
/*     */   
/*     */   private void ensureUnique(E e) {
/*  57 */     E previous = this.uniqueClasses.remove(e.getClass());
/*  58 */     if (previous != null) {
/*  59 */       this.list.remove(previous);
/*     */     }
/*  61 */     this.uniqueClasses.put(e.getClass(), e);
/*     */   }
/*     */   
/*     */   public ChainBuilder<E> addFirst(E e) {
/*  65 */     if (e == null) {
/*  66 */       return this;
/*     */     }
/*  68 */     ensureUnique(e);
/*  69 */     this.list.addFirst(e);
/*  70 */     return this;
/*     */   }
/*     */   
/*     */   public ChainBuilder<E> addLast(E e) {
/*  74 */     if (e == null) {
/*  75 */       return this;
/*     */     }
/*  77 */     ensureUnique(e);
/*  78 */     this.list.addLast(e);
/*  79 */     return this;
/*     */   }
/*     */   
/*     */   public ChainBuilder<E> addAllFirst(Collection<E> c) {
/*  83 */     if (c == null) {
/*  84 */       return this;
/*     */     }
/*  86 */     for (E e : c) {
/*  87 */       addFirst(e);
/*     */     }
/*  89 */     return this;
/*     */   }
/*     */   
/*     */   public ChainBuilder<E> addAllFirst(E... c) {
/*  93 */     if (c == null) {
/*  94 */       return this;
/*     */     }
/*  96 */     for (E e : c) {
/*  97 */       addFirst(e);
/*     */     }
/*  99 */     return this;
/*     */   }
/*     */   
/*     */   public ChainBuilder<E> addAllLast(Collection<E> c) {
/* 103 */     if (c == null) {
/* 104 */       return this;
/*     */     }
/* 106 */     for (E e : c) {
/* 107 */       addLast(e);
/*     */     }
/* 109 */     return this;
/*     */   }
/*     */   
/*     */   public ChainBuilder<E> addAllLast(E... c) {
/* 113 */     if (c == null) {
/* 114 */       return this;
/*     */     }
/* 116 */     for (E e : c) {
/* 117 */       addLast(e);
/*     */     }
/* 119 */     return this;
/*     */   }
/*     */   
/*     */   public LinkedList<E> build() {
/* 123 */     return new LinkedList<E>(this.list);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\protocol\ChainBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */