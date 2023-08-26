/*     */ package org.apache.http.pool;
/*     */ 
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.http.annotation.GuardedBy;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.util.Args;
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
/*     */ public abstract class PoolEntry<T, C>
/*     */ {
/*     */   private final String id;
/*     */   private final T route;
/*     */   private final C conn;
/*     */   private final long created;
/*     */   private final long validityDeadline;
/*     */   @GuardedBy("this")
/*     */   private long updated;
/*     */   @GuardedBy("this")
/*     */   private long expiry;
/*     */   private volatile Object state;
/*     */   
/*     */   public PoolEntry(String id, T route, C conn, long timeToLive, TimeUnit tunit) {
/*  81 */     Args.notNull(route, "Route");
/*  82 */     Args.notNull(conn, "Connection");
/*  83 */     Args.notNull(tunit, "Time unit");
/*  84 */     this.id = id;
/*  85 */     this.route = route;
/*  86 */     this.conn = conn;
/*  87 */     this.created = System.currentTimeMillis();
/*  88 */     if (timeToLive > 0L) {
/*  89 */       this.validityDeadline = this.created + tunit.toMillis(timeToLive);
/*     */     } else {
/*  91 */       this.validityDeadline = Long.MAX_VALUE;
/*     */     } 
/*  93 */     this.expiry = this.validityDeadline;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PoolEntry(String id, T route, C conn) {
/* 104 */     this(id, route, conn, 0L, TimeUnit.MILLISECONDS);
/*     */   }
/*     */   
/*     */   public String getId() {
/* 108 */     return this.id;
/*     */   }
/*     */   
/*     */   public T getRoute() {
/* 112 */     return this.route;
/*     */   }
/*     */   
/*     */   public C getConnection() {
/* 116 */     return this.conn;
/*     */   }
/*     */   
/*     */   public long getCreated() {
/* 120 */     return this.created;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getValidityDeadline() {
/* 127 */     return this.validityDeadline;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public long getValidUnit() {
/* 135 */     return this.validityDeadline;
/*     */   }
/*     */   
/*     */   public Object getState() {
/* 139 */     return this.state;
/*     */   }
/*     */   
/*     */   public void setState(Object state) {
/* 143 */     this.state = state;
/*     */   }
/*     */   
/*     */   public synchronized long getUpdated() {
/* 147 */     return this.updated;
/*     */   }
/*     */   
/*     */   public synchronized long getExpiry() {
/* 151 */     return this.expiry;
/*     */   }
/*     */   public synchronized void updateExpiry(long time, TimeUnit tunit) {
/*     */     long newExpiry;
/* 155 */     Args.notNull(tunit, "Time unit");
/* 156 */     this.updated = System.currentTimeMillis();
/*     */     
/* 158 */     if (time > 0L) {
/* 159 */       newExpiry = this.updated + tunit.toMillis(time);
/*     */     } else {
/* 161 */       newExpiry = Long.MAX_VALUE;
/*     */     } 
/* 163 */     this.expiry = Math.min(newExpiry, this.validityDeadline);
/*     */   }
/*     */   
/*     */   public synchronized boolean isExpired(long now) {
/* 167 */     return (now >= this.expiry);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void close();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract boolean isClosed();
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 183 */     StringBuilder buffer = new StringBuilder();
/* 184 */     buffer.append("[id:");
/* 185 */     buffer.append(this.id);
/* 186 */     buffer.append("][route:");
/* 187 */     buffer.append(this.route);
/* 188 */     buffer.append("][state:");
/* 189 */     buffer.append(this.state);
/* 190 */     buffer.append("]");
/* 191 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\pool\PoolEntry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */