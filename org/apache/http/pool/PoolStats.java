/*     */ package org.apache.http.pool;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.apache.http.annotation.Immutable;
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
/*     */ @Immutable
/*     */ public class PoolStats
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -2807686144795228544L;
/*     */   private final int leased;
/*     */   private final int pending;
/*     */   private final int available;
/*     */   private final int max;
/*     */   
/*     */   public PoolStats(int leased, int pending, int free, int max) {
/*  53 */     this.leased = leased;
/*  54 */     this.pending = pending;
/*  55 */     this.available = free;
/*  56 */     this.max = max;
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
/*     */   public int getLeased() {
/*  69 */     return this.leased;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPending() {
/*  79 */     return this.pending;
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
/*     */   public int getAvailable() {
/*  91 */     return this.available;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMax() {
/* 100 */     return this.max;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 105 */     StringBuilder buffer = new StringBuilder();
/* 106 */     buffer.append("[leased: ");
/* 107 */     buffer.append(this.leased);
/* 108 */     buffer.append("; pending: ");
/* 109 */     buffer.append(this.pending);
/* 110 */     buffer.append("; available: ");
/* 111 */     buffer.append(this.available);
/* 112 */     buffer.append("; max: ");
/* 113 */     buffer.append(this.max);
/* 114 */     buffer.append("]");
/* 115 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\pool\PoolStats.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */