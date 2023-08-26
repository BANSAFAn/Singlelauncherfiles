/*     */ package org.apache.http.impl.conn.tsccm;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.annotation.GuardedBy;
/*     */ import org.apache.http.conn.ConnectionPoolTimeoutException;
/*     */ import org.apache.http.conn.OperatedClientConnection;
/*     */ import org.apache.http.conn.routing.HttpRoute;
/*     */ import org.apache.http.impl.conn.IdleConnectionHandler;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public abstract class AbstractConnPool
/*     */ {
/*  91 */   private final Log log = LogFactory.getLog(getClass()); @GuardedBy("poolLock")
/*  92 */   protected Set<BasicPoolEntry> leasedConnections = new HashSet<BasicPoolEntry>();
/*  93 */   protected IdleConnectionHandler idleConnHandler = new IdleConnectionHandler();
/*  94 */   protected final Lock poolLock = new ReentrantLock();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GuardedBy("poolLock")
/*     */   protected int numConnections;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected volatile boolean isShutDown;
/*     */ 
/*     */ 
/*     */   
/*     */   protected Set<BasicPoolEntryRef> issuedConnections;
/*     */ 
/*     */ 
/*     */   
/*     */   protected ReferenceQueue<Object> refQueue;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enableConnectionGC() throws IllegalStateException {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final BasicPoolEntry getEntry(HttpRoute route, Object state, long timeout, TimeUnit tunit) throws ConnectionPoolTimeoutException, InterruptedException {
/* 124 */     return requestPoolEntry(route, state).getPoolEntry(timeout, tunit);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract PoolEntryRequest requestPoolEntry(HttpRoute paramHttpRoute, Object paramObject);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void freeEntry(BasicPoolEntry paramBasicPoolEntry, boolean paramBoolean, long paramLong, TimeUnit paramTimeUnit);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleReference(Reference<?> ref) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void handleLostEntry(HttpRoute paramHttpRoute);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void closeIdleConnections(long idletime, TimeUnit tunit) {
/* 167 */     Args.notNull(tunit, "Time unit");
/*     */     
/* 169 */     this.poolLock.lock();
/*     */     try {
/* 171 */       this.idleConnHandler.closeIdleConnections(tunit.toMillis(idletime));
/*     */     } finally {
/* 173 */       this.poolLock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void closeExpiredConnections() {
/* 178 */     this.poolLock.lock();
/*     */     try {
/* 180 */       this.idleConnHandler.closeExpiredConnections();
/*     */     } finally {
/* 182 */       this.poolLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void deleteClosedConnections();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void shutdown() {
/* 198 */     this.poolLock.lock();
/*     */     
/*     */     try {
/* 201 */       if (this.isShutDown) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 206 */       Iterator<BasicPoolEntry> iter = this.leasedConnections.iterator();
/* 207 */       while (iter.hasNext()) {
/* 208 */         BasicPoolEntry entry = iter.next();
/* 209 */         iter.remove();
/* 210 */         closeConnection(entry.getConnection());
/*     */       } 
/* 212 */       this.idleConnHandler.removeAll();
/*     */       
/* 214 */       this.isShutDown = true;
/*     */     } finally {
/*     */       
/* 217 */       this.poolLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void closeConnection(OperatedClientConnection conn) {
/* 228 */     if (conn != null)
/*     */       try {
/* 230 */         conn.close();
/* 231 */       } catch (IOException ex) {
/* 232 */         this.log.debug("I/O error closing connection", ex);
/*     */       }  
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\conn\tsccm\AbstractConnPool.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */