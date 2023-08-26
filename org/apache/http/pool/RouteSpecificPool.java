/*     */ package org.apache.http.pool;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Set;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.Asserts;
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
/*     */ abstract class RouteSpecificPool<T, C, E extends PoolEntry<T, C>>
/*     */ {
/*     */   private final T route;
/*     */   private final Set<E> leased;
/*     */   private final LinkedList<E> available;
/*     */   private final LinkedList<PoolEntryFuture<E>> pending;
/*     */   
/*     */   RouteSpecificPool(T route) {
/*  48 */     this.route = route;
/*  49 */     this.leased = new HashSet<E>();
/*  50 */     this.available = new LinkedList<E>();
/*  51 */     this.pending = new LinkedList<PoolEntryFuture<E>>();
/*     */   }
/*     */   
/*     */   protected abstract E createEntry(C paramC);
/*     */   
/*     */   public final T getRoute() {
/*  57 */     return this.route;
/*     */   }
/*     */   
/*     */   public int getLeasedCount() {
/*  61 */     return this.leased.size();
/*     */   }
/*     */   
/*     */   public int getPendingCount() {
/*  65 */     return this.pending.size();
/*     */   }
/*     */   
/*     */   public int getAvailableCount() {
/*  69 */     return this.available.size();
/*     */   }
/*     */   
/*     */   public int getAllocatedCount() {
/*  73 */     return this.available.size() + this.leased.size();
/*     */   }
/*     */   
/*     */   public E getFree(Object state) {
/*  77 */     if (!this.available.isEmpty()) {
/*  78 */       if (state != null) {
/*  79 */         Iterator<E> iterator = this.available.iterator();
/*  80 */         while (iterator.hasNext()) {
/*  81 */           PoolEntry poolEntry = (PoolEntry)iterator.next();
/*  82 */           if (state.equals(poolEntry.getState())) {
/*  83 */             iterator.remove();
/*  84 */             this.leased.add((E)poolEntry);
/*  85 */             return (E)poolEntry;
/*     */           } 
/*     */         } 
/*     */       } 
/*  89 */       Iterator<E> it = this.available.iterator();
/*  90 */       while (it.hasNext()) {
/*  91 */         PoolEntry poolEntry = (PoolEntry)it.next();
/*  92 */         if (poolEntry.getState() == null) {
/*  93 */           it.remove();
/*  94 */           this.leased.add((E)poolEntry);
/*  95 */           return (E)poolEntry;
/*     */         } 
/*     */       } 
/*     */     } 
/*  99 */     return null;
/*     */   }
/*     */   
/*     */   public E getLastUsed() {
/* 103 */     if (!this.available.isEmpty()) {
/* 104 */       return this.available.getLast();
/*     */     }
/* 106 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean remove(E entry) {
/* 111 */     Args.notNull(entry, "Pool entry");
/* 112 */     if (!this.available.remove(entry) && 
/* 113 */       !this.leased.remove(entry)) {
/* 114 */       return false;
/*     */     }
/*     */     
/* 117 */     return true;
/*     */   }
/*     */   
/*     */   public void free(E entry, boolean reusable) {
/* 121 */     Args.notNull(entry, "Pool entry");
/* 122 */     boolean found = this.leased.remove(entry);
/* 123 */     Asserts.check(found, "Entry %s has not been leased from this pool", entry);
/* 124 */     if (reusable) {
/* 125 */       this.available.addFirst(entry);
/*     */     }
/*     */   }
/*     */   
/*     */   public E add(C conn) {
/* 130 */     E entry = createEntry(conn);
/* 131 */     this.leased.add(entry);
/* 132 */     return entry;
/*     */   }
/*     */   
/*     */   public void queue(PoolEntryFuture<E> future) {
/* 136 */     if (future == null) {
/*     */       return;
/*     */     }
/* 139 */     this.pending.add(future);
/*     */   }
/*     */   
/*     */   public PoolEntryFuture<E> nextPending() {
/* 143 */     return this.pending.poll();
/*     */   }
/*     */   
/*     */   public void unqueue(PoolEntryFuture<E> future) {
/* 147 */     if (future == null) {
/*     */       return;
/*     */     }
/*     */     
/* 151 */     this.pending.remove(future);
/*     */   }
/*     */   
/*     */   public void shutdown() {
/* 155 */     for (PoolEntryFuture<E> future : this.pending) {
/* 156 */       future.cancel(true);
/*     */     }
/* 158 */     this.pending.clear();
/* 159 */     for (PoolEntry poolEntry : this.available) {
/* 160 */       poolEntry.close();
/*     */     }
/* 162 */     this.available.clear();
/* 163 */     for (PoolEntry poolEntry : this.leased) {
/* 164 */       poolEntry.close();
/*     */     }
/* 166 */     this.leased.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 171 */     StringBuilder buffer = new StringBuilder();
/* 172 */     buffer.append("[route: ");
/* 173 */     buffer.append(this.route);
/* 174 */     buffer.append("][leased: ");
/* 175 */     buffer.append(this.leased.size());
/* 176 */     buffer.append("][available: ");
/* 177 */     buffer.append(this.available.size());
/* 178 */     buffer.append("][pending: ");
/* 179 */     buffer.append(this.pending.size());
/* 180 */     buffer.append("]");
/* 181 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\pool\RouteSpecificPool.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */