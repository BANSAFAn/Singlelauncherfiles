/*    */ package org.apache.http.client.protocol;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.apache.http.HttpException;
/*    */ import org.apache.http.HttpRequest;
/*    */ import org.apache.http.HttpRequestInterceptor;
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.conn.routing.RouteInfo;
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
/*    */ @Immutable
/*    */ public class RequestClientConnControl
/*    */   implements HttpRequestInterceptor
/*    */ {
/* 53 */   private final Log log = LogFactory.getLog(getClass());
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static final String PROXY_CONN_DIRECTIVE = "Proxy-Connection";
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
/* 64 */     Args.notNull(request, "HTTP request");
/*    */     
/* 66 */     String method = request.getRequestLine().getMethod();
/* 67 */     if (method.equalsIgnoreCase("CONNECT")) {
/* 68 */       request.setHeader("Proxy-Connection", "Keep-Alive");
/*    */       
/*    */       return;
/*    */     } 
/* 72 */     HttpClientContext clientContext = HttpClientContext.adapt(context);
/*    */ 
/*    */     
/* 75 */     RouteInfo route = clientContext.getHttpRoute();
/* 76 */     if (route == null) {
/* 77 */       this.log.debug("Connection route not set in the context");
/*    */       
/*    */       return;
/*    */     } 
/* 81 */     if ((route.getHopCount() == 1 || route.isTunnelled()) && 
/* 82 */       !request.containsHeader("Connection")) {
/* 83 */       request.addHeader("Connection", "Keep-Alive");
/*    */     }
/*    */     
/* 86 */     if (route.getHopCount() == 2 && !route.isTunnelled() && 
/* 87 */       !request.containsHeader("Proxy-Connection"))
/* 88 */       request.addHeader("Proxy-Connection", "Keep-Alive"); 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\client\protocol\RequestClientConnControl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */