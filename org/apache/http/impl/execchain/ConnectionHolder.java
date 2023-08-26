/*     */ package org.apache.http.impl.execchain;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.http.HttpClientConnection;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.concurrent.Cancellable;
/*     */ import org.apache.http.conn.ConnectionReleaseTrigger;
/*     */ import org.apache.http.conn.HttpClientConnectionManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ class ConnectionHolder
/*     */   implements ConnectionReleaseTrigger, Cancellable, Closeable
/*     */ {
/*     */   private final Log log;
/*     */   private final HttpClientConnectionManager manager;
/*     */   private final HttpClientConnection managedConn;
/*     */   private final AtomicBoolean released;
/*     */   private volatile boolean reusable;
/*     */   private volatile Object state;
/*     */   private volatile long validDuration;
/*     */   private volatile TimeUnit tunit;
/*     */   
/*     */   public ConnectionHolder(Log log, HttpClientConnectionManager manager, HttpClientConnection managedConn) {
/*  65 */     this.log = log;
/*  66 */     this.manager = manager;
/*  67 */     this.managedConn = managedConn;
/*  68 */     this.released = new AtomicBoolean(false);
/*     */   }
/*     */   
/*     */   public boolean isReusable() {
/*  72 */     return this.reusable;
/*     */   }
/*     */   
/*     */   public void markReusable() {
/*  76 */     this.reusable = true;
/*     */   }
/*     */   
/*     */   public void markNonReusable() {
/*  80 */     this.reusable = false;
/*     */   }
/*     */   
/*     */   public void setState(Object state) {
/*  84 */     this.state = state;
/*     */   }
/*     */   
/*     */   public void setValidFor(long duration, TimeUnit tunit) {
/*  88 */     synchronized (this.managedConn) {
/*  89 */       this.validDuration = duration;
/*  90 */       this.tunit = tunit;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void releaseConnection(boolean reusable) {
/*  95 */     if (this.released.compareAndSet(false, true)) {
/*  96 */       synchronized (this.managedConn) {
/*  97 */         if (reusable) {
/*  98 */           this.manager.releaseConnection(this.managedConn, this.state, this.validDuration, this.tunit);
/*     */         } else {
/*     */           
/*     */           try {
/* 102 */             this.managedConn.close();
/* 103 */             this.log.debug("Connection discarded");
/* 104 */           } catch (IOException ex) {
/* 105 */             if (this.log.isDebugEnabled()) {
/* 106 */               this.log.debug(ex.getMessage(), ex);
/*     */             }
/*     */           } finally {
/* 109 */             this.manager.releaseConnection(this.managedConn, null, 0L, TimeUnit.MILLISECONDS);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void releaseConnection() {
/* 119 */     releaseConnection(this.reusable);
/*     */   }
/*     */ 
/*     */   
/*     */   public void abortConnection() {
/* 124 */     if (this.released.compareAndSet(false, true)) {
/* 125 */       synchronized (this.managedConn) {
/*     */         try {
/* 127 */           this.managedConn.shutdown();
/* 128 */           this.log.debug("Connection discarded");
/* 129 */         } catch (IOException ex) {
/* 130 */           if (this.log.isDebugEnabled()) {
/* 131 */             this.log.debug(ex.getMessage(), ex);
/*     */           }
/*     */         } finally {
/* 134 */           this.manager.releaseConnection(this.managedConn, null, 0L, TimeUnit.MILLISECONDS);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean cancel() {
/* 143 */     boolean alreadyReleased = this.released.get();
/* 144 */     this.log.debug("Cancelling request execution");
/* 145 */     abortConnection();
/* 146 */     return !alreadyReleased;
/*     */   }
/*     */   
/*     */   public boolean isReleased() {
/* 150 */     return this.released.get();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 155 */     releaseConnection(false);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\execchain\ConnectionHolder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */