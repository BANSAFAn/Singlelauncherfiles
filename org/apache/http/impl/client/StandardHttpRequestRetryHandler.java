/*    */ package org.apache.http.impl.client;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import org.apache.http.HttpRequest;
/*    */ import org.apache.http.annotation.Immutable;
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
/*    */ public class StandardHttpRequestRetryHandler
/*    */   extends DefaultHttpRequestRetryHandler
/*    */ {
/*    */   private final Map<String, Boolean> idempotentMethods;
/*    */   
/*    */   public StandardHttpRequestRetryHandler(int retryCount, boolean requestSentRetryEnabled) {
/* 57 */     super(retryCount, requestSentRetryEnabled);
/* 58 */     this.idempotentMethods = new ConcurrentHashMap<String, Boolean>();
/* 59 */     this.idempotentMethods.put("GET", Boolean.TRUE);
/* 60 */     this.idempotentMethods.put("HEAD", Boolean.TRUE);
/* 61 */     this.idempotentMethods.put("PUT", Boolean.TRUE);
/* 62 */     this.idempotentMethods.put("DELETE", Boolean.TRUE);
/* 63 */     this.idempotentMethods.put("OPTIONS", Boolean.TRUE);
/* 64 */     this.idempotentMethods.put("TRACE", Boolean.TRUE);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public StandardHttpRequestRetryHandler() {
/* 71 */     this(3, false);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean handleAsIdempotent(HttpRequest request) {
/* 76 */     String method = request.getRequestLine().getMethod().toUpperCase(Locale.ROOT);
/* 77 */     Boolean b = this.idempotentMethods.get(method);
/* 78 */     return (b != null && b.booleanValue());
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\client\StandardHttpRequestRetryHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */