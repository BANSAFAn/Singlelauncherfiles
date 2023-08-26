/*    */ package org.apache.http.impl.pool;
/*    */ 
/*    */ import java.util.concurrent.atomic.AtomicLong;
/*    */ import org.apache.http.HttpClientConnection;
/*    */ import org.apache.http.HttpHost;
/*    */ import org.apache.http.annotation.ThreadSafe;
/*    */ import org.apache.http.config.ConnectionConfig;
/*    */ import org.apache.http.config.SocketConfig;
/*    */ import org.apache.http.params.HttpParams;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @ThreadSafe
/*    */ public class BasicConnPool
/*    */   extends AbstractConnPool<HttpHost, HttpClientConnection, BasicPoolEntry>
/*    */ {
/* 54 */   private static final AtomicLong COUNTER = new AtomicLong();
/*    */   
/*    */   public BasicConnPool(ConnFactory<HttpHost, HttpClientConnection> connFactory) {
/* 57 */     super(connFactory, 2, 20);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public BasicConnPool(HttpParams params) {
/* 65 */     super(new BasicConnFactory(params), 2, 20);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BasicConnPool(SocketConfig sconfig, ConnectionConfig cconfig) {
/* 72 */     super(new BasicConnFactory(sconfig, cconfig), 2, 20);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BasicConnPool() {
/* 79 */     super(new BasicConnFactory(SocketConfig.DEFAULT, ConnectionConfig.DEFAULT), 2, 20);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected BasicPoolEntry createEntry(HttpHost host, HttpClientConnection conn) {
/* 86 */     return new BasicPoolEntry(Long.toString(COUNTER.getAndIncrement()), host, conn);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean validate(BasicPoolEntry entry) {
/* 91 */     return !((HttpClientConnection)entry.getConnection()).isStale();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\pool\BasicConnPool.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */