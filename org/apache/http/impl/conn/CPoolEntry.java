/*    */ package org.apache.http.impl.conn;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.Date;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.http.HttpClientConnection;
/*    */ import org.apache.http.annotation.ThreadSafe;
/*    */ import org.apache.http.conn.ManagedHttpClientConnection;
/*    */ import org.apache.http.conn.routing.HttpRoute;
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
/*    */ @ThreadSafe
/*    */ class CPoolEntry
/*    */   extends PoolEntry<HttpRoute, ManagedHttpClientConnection>
/*    */ {
/*    */   private final Log log;
/*    */   private volatile boolean routeComplete;
/*    */   
/*    */   public CPoolEntry(Log log, String id, HttpRoute route, ManagedHttpClientConnection conn, long timeToLive, TimeUnit tunit) {
/* 55 */     super(id, route, conn, timeToLive, tunit);
/* 56 */     this.log = log;
/*    */   }
/*    */   
/*    */   public void markRouteComplete() {
/* 60 */     this.routeComplete = true;
/*    */   }
/*    */   
/*    */   public boolean isRouteComplete() {
/* 64 */     return this.routeComplete;
/*    */   }
/*    */   
/*    */   public void closeConnection() throws IOException {
/* 68 */     HttpClientConnection conn = (HttpClientConnection)getConnection();
/* 69 */     conn.close();
/*    */   }
/*    */   
/*    */   public void shutdownConnection() throws IOException {
/* 73 */     HttpClientConnection conn = (HttpClientConnection)getConnection();
/* 74 */     conn.shutdown();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isExpired(long now) {
/* 79 */     boolean expired = super.isExpired(now);
/* 80 */     if (expired && this.log.isDebugEnabled()) {
/* 81 */       this.log.debug("Connection " + this + " expired @ " + new Date(getExpiry()));
/*    */     }
/* 83 */     return expired;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isClosed() {
/* 88 */     HttpClientConnection conn = (HttpClientConnection)getConnection();
/* 89 */     return !conn.isOpen();
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/*    */     try {
/* 95 */       closeConnection();
/* 96 */     } catch (IOException ex) {
/* 97 */       this.log.debug("I/O error closing connection", ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\conn\CPoolEntry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */