/*     */ package org.apache.http.pool;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.concurrent.FutureCallback;
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
/*     */ @ThreadSafe
/*     */ public abstract class AbstractConnPool<T, C, E extends PoolEntry<T, C>>
/*     */   implements ConnPool<T, E>, ConnPoolControl<T>
/*     */ {
/*     */   private final Lock lock;
/*     */   private final ConnFactory<T, C> connFactory;
/*     */   private final Map<T, RouteSpecificPool<T, C, E>> routeToPool;
/*     */   private final Set<E> leased;
/*     */   private final LinkedList<E> available;
/*     */   private final LinkedList<PoolEntryFuture<E>> pending;
/*     */   private final Map<T, Integer> maxPerRoute;
/*     */   private volatile boolean isShutDown;
/*     */   private volatile int defaultMaxPerRoute;
/*     */   private volatile int maxTotal;
/*     */   private volatile int validateAfterInactivity;
/*     */   
/*     */   public AbstractConnPool(ConnFactory<T, C> connFactory, int defaultMaxPerRoute, int maxTotal) {
/*  85 */     this.connFactory = (ConnFactory<T, C>)Args.notNull(connFactory, "Connection factory");
/*  86 */     this.defaultMaxPerRoute = Args.positive(defaultMaxPerRoute, "Max per route value");
/*  87 */     this.maxTotal = Args.positive(maxTotal, "Max total value");
/*  88 */     this.lock = new ReentrantLock();
/*  89 */     this.routeToPool = new HashMap<T, RouteSpecificPool<T, C, E>>();
/*  90 */     this.leased = new HashSet<E>();
/*  91 */     this.available = new LinkedList<E>();
/*  92 */     this.pending = new LinkedList<PoolEntryFuture<E>>();
/*  93 */     this.maxPerRoute = new HashMap<T, Integer>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onLease(E entry) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onRelease(E entry) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onReuse(E entry) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean validate(E entry) {
/* 123 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isShutdown() {
/* 127 */     return this.isShutDown;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void shutdown() throws IOException {
/* 134 */     if (this.isShutDown) {
/*     */       return;
/*     */     }
/* 137 */     this.isShutDown = true;
/* 138 */     this.lock.lock();
/*     */     try {
/* 140 */       for (PoolEntry poolEntry : this.available) {
/* 141 */         poolEntry.close();
/*     */       }
/* 143 */       for (PoolEntry poolEntry : this.leased) {
/* 144 */         poolEntry.close();
/*     */       }
/* 146 */       for (RouteSpecificPool<T, C, E> pool : this.routeToPool.values()) {
/* 147 */         pool.shutdown();
/*     */       }
/* 149 */       this.routeToPool.clear();
/* 150 */       this.leased.clear();
/* 151 */       this.available.clear();
/*     */     } finally {
/* 153 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   private RouteSpecificPool<T, C, E> getPool(final T route) {
/* 158 */     RouteSpecificPool<T, C, E> pool = this.routeToPool.get(route);
/* 159 */     if (pool == null) {
/* 160 */       pool = new RouteSpecificPool<T, C, E>(route)
/*     */         {
/*     */           protected E createEntry(C conn)
/*     */           {
/* 164 */             return AbstractConnPool.this.createEntry(route, conn);
/*     */           }
/*     */         };
/*     */       
/* 168 */       this.routeToPool.put(route, pool);
/*     */     } 
/* 170 */     return pool;
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
/*     */   public Future<E> lease(final T route, final Object state, FutureCallback<E> callback) {
/* 183 */     Args.notNull(route, "Route");
/* 184 */     Asserts.check(!this.isShutDown, "Connection pool shut down");
/* 185 */     return new PoolEntryFuture<E>(this.lock, callback)
/*     */       {
/*     */ 
/*     */ 
/*     */         
/*     */         public E getPoolEntry(long timeout, TimeUnit tunit) throws InterruptedException, TimeoutException, IOException
/*     */         {
/* 192 */           PoolEntry poolEntry = (PoolEntry)AbstractConnPool.this.getPoolEntryBlocking((T)route, state, timeout, tunit, this);
/* 193 */           AbstractConnPool.this.onLease(poolEntry);
/* 194 */           return (E)poolEntry;
/*     */         }
/*     */       };
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
/*     */   public Future<E> lease(T route, Object state) {
/* 217 */     return lease(route, state, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private E getPoolEntryBlocking(T route, Object state, long timeout, TimeUnit tunit, PoolEntryFuture<E> future) throws IOException, InterruptedException, TimeoutException {
/* 226 */     Date deadline = null;
/* 227 */     if (timeout > 0L) {
/* 228 */       deadline = new Date(System.currentTimeMillis() + tunit.toMillis(timeout));
/*     */     }
/*     */ 
/*     */     
/* 232 */     this.lock.lock();
/*     */     try {
/* 234 */       RouteSpecificPool<T, C, E> pool = getPool(route);
/* 235 */       E entry = null;
/* 236 */       while (entry == null) {
/* 237 */         Asserts.check(!this.isShutDown, "Connection pool shut down");
/*     */         while (true) {
/* 239 */           entry = pool.getFree(state);
/* 240 */           if (entry == null) {
/*     */             break;
/*     */           }
/* 243 */           if (entry.isExpired(System.currentTimeMillis())) {
/* 244 */             entry.close();
/* 245 */           } else if (this.validateAfterInactivity > 0 && 
/* 246 */             entry.getUpdated() + this.validateAfterInactivity <= System.currentTimeMillis() && 
/* 247 */             !validate(entry)) {
/* 248 */             entry.close();
/*     */           } 
/*     */ 
/*     */           
/* 252 */           if (entry.isClosed()) {
/* 253 */             this.available.remove(entry);
/* 254 */             pool.free(entry, false);
/*     */             continue;
/*     */           } 
/*     */           break;
/*     */         } 
/* 259 */         if (entry != null) {
/* 260 */           this.available.remove(entry);
/* 261 */           this.leased.add(entry);
/* 262 */           onReuse(entry);
/* 263 */           return entry;
/*     */         } 
/*     */ 
/*     */         
/* 267 */         int maxPerRoute = getMax(route);
/*     */         
/* 269 */         int excess = Math.max(0, pool.getAllocatedCount() + 1 - maxPerRoute);
/* 270 */         if (excess > 0) {
/* 271 */           for (int i = 0; i < excess; i++) {
/* 272 */             E lastUsed = pool.getLastUsed();
/* 273 */             if (lastUsed == null) {
/*     */               break;
/*     */             }
/* 276 */             lastUsed.close();
/* 277 */             this.available.remove(lastUsed);
/* 278 */             pool.remove(lastUsed);
/*     */           } 
/*     */         }
/*     */         
/* 282 */         if (pool.getAllocatedCount() < maxPerRoute) {
/* 283 */           int totalUsed = this.leased.size();
/* 284 */           int freeCapacity = Math.max(this.maxTotal - totalUsed, 0);
/* 285 */           if (freeCapacity > 0) {
/* 286 */             int totalAvailable = this.available.size();
/* 287 */             if (totalAvailable > freeCapacity - 1 && 
/* 288 */               !this.available.isEmpty()) {
/* 289 */               PoolEntry poolEntry = (PoolEntry)this.available.removeLast();
/* 290 */               poolEntry.close();
/* 291 */               RouteSpecificPool<T, C, E> otherpool = getPool((T)poolEntry.getRoute());
/* 292 */               otherpool.remove((E)poolEntry);
/*     */             } 
/*     */             
/* 295 */             C conn = this.connFactory.create(route);
/* 296 */             entry = pool.add(conn);
/* 297 */             this.leased.add(entry);
/* 298 */             return entry;
/*     */           } 
/*     */         } 
/*     */         
/* 302 */         boolean success = false;
/*     */         try {
/* 304 */           pool.queue(future);
/* 305 */           this.pending.add(future);
/* 306 */           success = future.await(deadline);
/*     */         
/*     */         }
/*     */         finally {
/*     */ 
/*     */           
/* 312 */           pool.unqueue(future);
/* 313 */           this.pending.remove(future);
/*     */         } 
/*     */         
/* 316 */         if (!success && deadline != null && deadline.getTime() <= System.currentTimeMillis()) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */       
/* 321 */       throw new TimeoutException("Timeout waiting for connection");
/*     */     } finally {
/* 323 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void release(E entry, boolean reusable) {
/* 329 */     this.lock.lock();
/*     */     try {
/* 331 */       if (this.leased.remove(entry)) {
/* 332 */         RouteSpecificPool<T, C, E> pool = getPool((T)entry.getRoute());
/* 333 */         pool.free(entry, reusable);
/* 334 */         if (reusable && !this.isShutDown) {
/* 335 */           this.available.addFirst(entry);
/* 336 */           onRelease(entry);
/*     */         } else {
/* 338 */           entry.close();
/*     */         } 
/* 340 */         PoolEntryFuture<E> future = pool.nextPending();
/* 341 */         if (future != null) {
/* 342 */           this.pending.remove(future);
/*     */         } else {
/* 344 */           future = this.pending.poll();
/*     */         } 
/* 346 */         if (future != null) {
/* 347 */           future.wakeup();
/*     */         }
/*     */       } 
/*     */     } finally {
/* 351 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   private int getMax(T route) {
/* 356 */     Integer v = this.maxPerRoute.get(route);
/* 357 */     if (v != null) {
/* 358 */       return v.intValue();
/*     */     }
/* 360 */     return this.defaultMaxPerRoute;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxTotal(int max) {
/* 366 */     Args.positive(max, "Max value");
/* 367 */     this.lock.lock();
/*     */     try {
/* 369 */       this.maxTotal = max;
/*     */     } finally {
/* 371 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxTotal() {
/* 377 */     this.lock.lock();
/*     */     try {
/* 379 */       return this.maxTotal;
/*     */     } finally {
/* 381 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDefaultMaxPerRoute(int max) {
/* 387 */     Args.positive(max, "Max per route value");
/* 388 */     this.lock.lock();
/*     */     try {
/* 390 */       this.defaultMaxPerRoute = max;
/*     */     } finally {
/* 392 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDefaultMaxPerRoute() {
/* 398 */     this.lock.lock();
/*     */     try {
/* 400 */       return this.defaultMaxPerRoute;
/*     */     } finally {
/* 402 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMaxPerRoute(T route, int max) {
/* 408 */     Args.notNull(route, "Route");
/* 409 */     Args.positive(max, "Max per route value");
/* 410 */     this.lock.lock();
/*     */     try {
/* 412 */       this.maxPerRoute.put(route, Integer.valueOf(max));
/*     */     } finally {
/* 414 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxPerRoute(T route) {
/* 420 */     Args.notNull(route, "Route");
/* 421 */     this.lock.lock();
/*     */     try {
/* 423 */       return getMax(route);
/*     */     } finally {
/* 425 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public PoolStats getTotalStats() {
/* 431 */     this.lock.lock();
/*     */     try {
/* 433 */       return new PoolStats(this.leased.size(), this.pending.size(), this.available.size(), this.maxTotal);
/*     */     
/*     */     }
/*     */     finally {
/*     */ 
/*     */       
/* 439 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public PoolStats getStats(T route) {
/* 445 */     Args.notNull(route, "Route");
/* 446 */     this.lock.lock();
/*     */     try {
/* 448 */       RouteSpecificPool<T, C, E> pool = getPool(route);
/* 449 */       return new PoolStats(pool.getLeasedCount(), pool.getPendingCount(), pool.getAvailableCount(), getMax(route));
/*     */     
/*     */     }
/*     */     finally {
/*     */ 
/*     */       
/* 455 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<T> getRoutes() {
/* 466 */     this.lock.lock();
/*     */     try {
/* 468 */       return new HashSet(this.routeToPool.keySet());
/*     */     } finally {
/* 470 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void enumAvailable(PoolEntryCallback<T, C> callback) {
/* 480 */     this.lock.lock();
/*     */     try {
/* 482 */       Iterator<E> it = this.available.iterator();
/* 483 */       while (it.hasNext()) {
/* 484 */         PoolEntry<T, C> poolEntry = (PoolEntry)it.next();
/* 485 */         callback.process(poolEntry);
/* 486 */         if (poolEntry.isClosed()) {
/* 487 */           RouteSpecificPool<T, C, E> pool = getPool(poolEntry.getRoute());
/* 488 */           pool.remove((E)poolEntry);
/* 489 */           it.remove();
/*     */         } 
/*     */       } 
/* 492 */       purgePoolMap();
/*     */     } finally {
/* 494 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void enumLeased(PoolEntryCallback<T, C> callback) {
/* 504 */     this.lock.lock();
/*     */     try {
/* 506 */       Iterator<E> it = this.leased.iterator();
/* 507 */       while (it.hasNext()) {
/* 508 */         PoolEntry<T, C> poolEntry = (PoolEntry)it.next();
/* 509 */         callback.process(poolEntry);
/*     */       } 
/*     */     } finally {
/* 512 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void purgePoolMap() {
/* 517 */     Iterator<Map.Entry<T, RouteSpecificPool<T, C, E>>> it = this.routeToPool.entrySet().iterator();
/* 518 */     while (it.hasNext()) {
/* 519 */       Map.Entry<T, RouteSpecificPool<T, C, E>> entry = it.next();
/* 520 */       RouteSpecificPool<T, C, E> pool = entry.getValue();
/* 521 */       if (pool.getPendingCount() + pool.getAllocatedCount() == 0) {
/* 522 */         it.remove();
/*     */       }
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
/*     */   public void closeIdle(long idletime, TimeUnit tunit) {
/* 535 */     Args.notNull(tunit, "Time unit");
/* 536 */     long time = tunit.toMillis(idletime);
/* 537 */     if (time < 0L) {
/* 538 */       time = 0L;
/*     */     }
/* 540 */     final long deadline = System.currentTimeMillis() - time;
/* 541 */     enumAvailable(new PoolEntryCallback<T, C>()
/*     */         {
/*     */           public void process(PoolEntry<T, C> entry)
/*     */           {
/* 545 */             if (entry.getUpdated() <= deadline) {
/* 546 */               entry.close();
/*     */             }
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void closeExpired() {
/* 557 */     final long now = System.currentTimeMillis();
/* 558 */     enumAvailable(new PoolEntryCallback<T, C>()
/*     */         {
/*     */           public void process(PoolEntry<T, C> entry)
/*     */           {
/* 562 */             if (entry.isExpired(now)) {
/* 563 */               entry.close();
/*     */             }
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getValidateAfterInactivity() {
/* 575 */     return this.validateAfterInactivity;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValidateAfterInactivity(int ms) {
/* 583 */     this.validateAfterInactivity = ms;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 588 */     StringBuilder buffer = new StringBuilder();
/* 589 */     buffer.append("[leased: ");
/* 590 */     buffer.append(this.leased);
/* 591 */     buffer.append("][available: ");
/* 592 */     buffer.append(this.available);
/* 593 */     buffer.append("][pending: ");
/* 594 */     buffer.append(this.pending);
/* 595 */     buffer.append("]");
/* 596 */     return buffer.toString();
/*     */   }
/*     */   
/*     */   protected abstract E createEntry(T paramT, C paramC);
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\pool\AbstractConnPool.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */