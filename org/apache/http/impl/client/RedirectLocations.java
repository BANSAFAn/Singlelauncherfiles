/*     */ package org.apache.http.impl.client;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.util.AbstractList;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
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
/*     */ @NotThreadSafe
/*     */ public class RedirectLocations
/*     */   extends AbstractList<Object>
/*     */ {
/*  54 */   private final Set<URI> unique = new HashSet<URI>();
/*  55 */   private final List<URI> all = new ArrayList<URI>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(URI uri) {
/*  62 */     return this.unique.contains(uri);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(URI uri) {
/*  69 */     this.unique.add(uri);
/*  70 */     this.all.add(uri);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean remove(URI uri) {
/*  77 */     boolean removed = this.unique.remove(uri);
/*  78 */     if (removed) {
/*  79 */       Iterator<URI> it = this.all.iterator();
/*  80 */       while (it.hasNext()) {
/*  81 */         URI current = it.next();
/*  82 */         if (current.equals(uri)) {
/*  83 */           it.remove();
/*     */         }
/*     */       } 
/*     */     } 
/*  87 */     return removed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<URI> getAll() {
/*  98 */     return new ArrayList<URI>(this.all);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public URI get(int index) {
/* 114 */     return this.all.get(index);
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
/*     */   public int size() {
/* 127 */     return this.all.size();
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
/*     */   public Object set(int index, Object element) {
/* 153 */     URI removed = this.all.set(index, (URI)element);
/* 154 */     this.unique.remove(removed);
/* 155 */     this.unique.add((URI)element);
/* 156 */     if (this.all.size() != this.unique.size()) {
/* 157 */       this.unique.addAll(this.all);
/*     */     }
/* 159 */     return removed;
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
/*     */   public void add(int index, Object element) {
/* 185 */     this.all.add(index, (URI)element);
/* 186 */     this.unique.add((URI)element);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URI remove(int index) {
/* 204 */     URI removed = this.all.remove(index);
/* 205 */     this.unique.remove(removed);
/* 206 */     if (this.all.size() != this.unique.size()) {
/* 207 */       this.unique.addAll(this.all);
/*     */     }
/* 209 */     return removed;
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
/*     */ 
/*     */   
/*     */   public boolean contains(Object o) {
/* 224 */     return this.unique.contains(o);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\client\RedirectLocations.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */