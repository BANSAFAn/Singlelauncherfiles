/*    */ package org.apache.http.impl.client;
/*    */ 
/*    */ import org.apache.http.HttpResponse;
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.client.ServiceUnavailableRetryStrategy;
/*    */ import org.apache.http.protocol.HttpContext;
/*    */ import org.apache.http.util.Args;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Immutable
/*    */ public class DefaultServiceUnavailableRetryStrategy
/*    */   implements ServiceUnavailableRetryStrategy
/*    */ {
/*    */   private final int maxRetries;
/*    */   private final long retryInterval;
/*    */   
/*    */   public DefaultServiceUnavailableRetryStrategy(int maxRetries, int retryInterval) {
/* 61 */     Args.positive(maxRetries, "Max retries");
/* 62 */     Args.positive(retryInterval, "Retry interval");
/* 63 */     this.maxRetries = maxRetries;
/* 64 */     this.retryInterval = retryInterval;
/*    */   }
/*    */   
/*    */   public DefaultServiceUnavailableRetryStrategy() {
/* 68 */     this(1, 1000);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean retryRequest(HttpResponse response, int executionCount, HttpContext context) {
/* 73 */     return (executionCount <= this.maxRetries && response.getStatusLine().getStatusCode() == 503);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public long getRetryInterval() {
/* 79 */     return this.retryInterval;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\client\DefaultServiceUnavailableRetryStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */