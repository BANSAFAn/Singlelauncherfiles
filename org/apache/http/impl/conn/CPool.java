/*    */ package org.apache.http.impl.conn;
/*    */ 
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import java.util.concurrent.atomic.AtomicLong;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.apache.http.annotation.ThreadSafe;
/*    */ import org.apache.http.conn.ManagedHttpClientConnection;
/*    */ import org.apache.http.conn.routing.HttpRoute;
/*    */ import org.apache.http.pool.AbstractConnPool;
/*    */ import org.apache.http.pool.ConnFactory;
/*    */ import org.apache.http.pool.PoolEntry;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @ThreadSafe
/*    */ class CPool
/*    */   extends AbstractConnPool<HttpRoute, ManagedHttpClientConnection, CPoolEntry>
/*    */ {
/* 46 */   private static final AtomicLong COUNTER = new AtomicLong();
/*    */   
/* 48 */   private final Log log = LogFactory.getLog(CPool.class);
/*    */   
/*    */   private final long timeToLive;
/*    */   
/*    */   private final TimeUnit tunit;
/*    */ 
/*    */   
/*    */   public CPool(ConnFactory<HttpRoute, ManagedHttpClientConnection> connFactory, int defaultMaxPerRoute, int maxTotal, long timeToLive, TimeUnit tunit) {
/* 56 */     super(connFactory, defaultMaxPerRoute, maxTotal);
/* 57 */     this.timeToLive = timeToLive;
/* 58 */     this.tunit = tunit;
/*    */   }
/*    */ 
/*    */   
/*    */   protected CPoolEntry createEntry(HttpRoute route, ManagedHttpClientConnection conn) {
/* 63 */     String id = Long.toString(COUNTER.getAndIncrement());
/* 64 */     return new CPoolEntry(this.log, id, route, conn, this.timeToLive, this.tunit);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean validate(CPoolEntry entry) {
/* 69 */     return !((ManagedHttpClientConnection)entry.getConnection()).isStale();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\conn\CPool.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */