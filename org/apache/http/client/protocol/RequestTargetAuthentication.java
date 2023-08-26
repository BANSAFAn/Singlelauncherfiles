/*    */ package org.apache.http.client.protocol;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.http.HttpException;
/*    */ import org.apache.http.HttpRequest;
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.auth.AuthState;
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
/*    */ @Deprecated
/*    */ @Immutable
/*    */ public class RequestTargetAuthentication
/*    */   extends RequestAuthenticationBase
/*    */ {
/*    */   public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
/* 59 */     Args.notNull(request, "HTTP request");
/* 60 */     Args.notNull(context, "HTTP context");
/*    */     
/* 62 */     String method = request.getRequestLine().getMethod();
/* 63 */     if (method.equalsIgnoreCase("CONNECT")) {
/*    */       return;
/*    */     }
/*    */     
/* 67 */     if (request.containsHeader("Authorization")) {
/*    */       return;
/*    */     }
/*    */ 
/*    */     
/* 72 */     AuthState authState = (AuthState)context.getAttribute("http.auth.target-scope");
/*    */     
/* 74 */     if (authState == null) {
/* 75 */       this.log.debug("Target auth state not set in the context");
/*    */       return;
/*    */     } 
/* 78 */     if (this.log.isDebugEnabled()) {
/* 79 */       this.log.debug("Target auth state: " + authState.getState());
/*    */     }
/* 81 */     process(authState, request, context);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\client\protocol\RequestTargetAuthentication.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */