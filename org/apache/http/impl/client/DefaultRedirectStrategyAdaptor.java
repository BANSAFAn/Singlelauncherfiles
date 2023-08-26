/*    */ package org.apache.http.impl.client;
/*    */ 
/*    */ import java.net.URI;
/*    */ import org.apache.http.HttpRequest;
/*    */ import org.apache.http.HttpResponse;
/*    */ import org.apache.http.ProtocolException;
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.client.RedirectHandler;
/*    */ import org.apache.http.client.RedirectStrategy;
/*    */ import org.apache.http.client.methods.HttpGet;
/*    */ import org.apache.http.client.methods.HttpHead;
/*    */ import org.apache.http.client.methods.HttpUriRequest;
/*    */ import org.apache.http.protocol.HttpContext;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
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
/*    */ class DefaultRedirectStrategyAdaptor
/*    */   implements RedirectStrategy
/*    */ {
/*    */   private final RedirectHandler handler;
/*    */   
/*    */   public DefaultRedirectStrategyAdaptor(RedirectHandler handler) {
/* 54 */     this.handler = handler;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
/* 62 */     return this.handler.isRedirectRequested(response, context);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public HttpUriRequest getRedirect(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
/* 70 */     URI uri = this.handler.getLocationURI(response, context);
/* 71 */     String method = request.getRequestLine().getMethod();
/* 72 */     if (method.equalsIgnoreCase("HEAD")) {
/* 73 */       return (HttpUriRequest)new HttpHead(uri);
/*    */     }
/* 75 */     return (HttpUriRequest)new HttpGet(uri);
/*    */   }
/*    */ 
/*    */   
/*    */   public RedirectHandler getHandler() {
/* 80 */     return this.handler;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\client\DefaultRedirectStrategyAdaptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */