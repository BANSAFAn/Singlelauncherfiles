/*    */ package org.apache.http.impl.client;
/*    */ 
/*    */ import org.apache.http.annotation.NotThreadSafe;
/*    */ import org.apache.http.conn.routing.HttpRoute;
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
/*    */ @Deprecated
/*    */ @NotThreadSafe
/*    */ public class RoutedRequest
/*    */ {
/*    */   protected final RequestWrapper request;
/*    */   protected final HttpRoute route;
/*    */   
/*    */   public RoutedRequest(RequestWrapper req, HttpRoute route) {
/* 55 */     this.request = req;
/* 56 */     this.route = route;
/*    */   }
/*    */   
/*    */   public final RequestWrapper getRequest() {
/* 60 */     return this.request;
/*    */   }
/*    */   
/*    */   public final HttpRoute getRoute() {
/* 64 */     return this.route;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\client\RoutedRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */