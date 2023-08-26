/*    */ package org.apache.http.impl.pool;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.http.HttpClientConnection;
/*    */ import org.apache.http.HttpHost;
/*    */ import org.apache.http.annotation.ThreadSafe;
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
/*    */ public class BasicPoolEntry
/*    */   extends PoolEntry<HttpHost, HttpClientConnection>
/*    */ {
/*    */   public BasicPoolEntry(String id, HttpHost route, HttpClientConnection conn) {
/* 48 */     super(id, route, conn);
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/*    */     try {
/* 54 */       ((HttpClientConnection)getConnection()).close();
/* 55 */     } catch (IOException ignore) {}
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isClosed() {
/* 61 */     return !((HttpClientConnection)getConnection()).isOpen();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\pool\BasicPoolEntry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */