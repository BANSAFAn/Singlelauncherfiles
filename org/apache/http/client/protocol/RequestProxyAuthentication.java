/*    */ package org.apache.http.client.protocol;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.http.HttpException;
/*    */ import org.apache.http.HttpRequest;
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.auth.AuthState;
/*    */ import org.apache.http.conn.HttpRoutedConnection;
/*    */ import org.apache.http.conn.routing.HttpRoute;
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
/*    */ @Deprecated
/*    */ @Immutable
/*    */ public class RequestProxyAuthentication
/*    */   extends RequestAuthenticationBase
/*    */ {
/*    */   public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
/* 62 */     Args.notNull(request, "HTTP request");
/* 63 */     Args.notNull(context, "HTTP context");
/*    */     
/* 65 */     if (request.containsHeader("Proxy-Authorization")) {
/*    */       return;
/*    */     }
/*    */     
/* 69 */     HttpRoutedConnection conn = (HttpRoutedConnection)context.getAttribute("http.connection");
/*    */     
/* 71 */     if (conn == null) {
/* 72 */       this.log.debug("HTTP connection not set in the context");
/*    */       return;
/*    */     } 
/* 75 */     HttpRoute route = conn.getRoute();
/* 76 */     if (route.isTunnelled()) {
/*    */       return;
/*    */     }
/*    */ 
/*    */     
/* 81 */     AuthState authState = (AuthState)context.getAttribute("http.auth.proxy-scope");
/*    */     
/* 83 */     if (authState == null) {
/* 84 */       this.log.debug("Proxy auth state not set in the context");
/*    */       return;
/*    */     } 
/* 87 */     if (this.log.isDebugEnabled()) {
/* 88 */       this.log.debug("Proxy auth state: " + authState.getState());
/*    */     }
/* 90 */     process(authState, request, context);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\client\protocol\RequestProxyAuthentication.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */