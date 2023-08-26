/*    */ package org.apache.http.protocol;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.InetAddress;
/*    */ import org.apache.http.HttpConnection;
/*    */ import org.apache.http.HttpException;
/*    */ import org.apache.http.HttpHost;
/*    */ import org.apache.http.HttpInetConnection;
/*    */ import org.apache.http.HttpRequest;
/*    */ import org.apache.http.HttpRequestInterceptor;
/*    */ import org.apache.http.HttpVersion;
/*    */ import org.apache.http.ProtocolException;
/*    */ import org.apache.http.ProtocolVersion;
/*    */ import org.apache.http.annotation.Immutable;
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
/*    */ @Immutable
/*    */ public class RequestTargetHost
/*    */   implements HttpRequestInterceptor
/*    */ {
/*    */   public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
/* 61 */     Args.notNull(request, "HTTP request");
/*    */     
/* 63 */     HttpCoreContext corecontext = HttpCoreContext.adapt(context);
/*    */     
/* 65 */     ProtocolVersion ver = request.getRequestLine().getProtocolVersion();
/* 66 */     String method = request.getRequestLine().getMethod();
/* 67 */     if (method.equalsIgnoreCase("CONNECT") && ver.lessEquals((ProtocolVersion)HttpVersion.HTTP_1_0)) {
/*    */       return;
/*    */     }
/*    */     
/* 71 */     if (!request.containsHeader("Host")) {
/* 72 */       HttpHost targethost = corecontext.getTargetHost();
/* 73 */       if (targethost == null) {
/* 74 */         HttpConnection conn = corecontext.getConnection();
/* 75 */         if (conn instanceof HttpInetConnection) {
/*    */ 
/*    */           
/* 78 */           InetAddress address = ((HttpInetConnection)conn).getRemoteAddress();
/* 79 */           int port = ((HttpInetConnection)conn).getRemotePort();
/* 80 */           if (address != null) {
/* 81 */             targethost = new HttpHost(address.getHostName(), port);
/*    */           }
/*    */         } 
/* 84 */         if (targethost == null) {
/* 85 */           if (ver.lessEquals((ProtocolVersion)HttpVersion.HTTP_1_0)) {
/*    */             return;
/*    */           }
/* 88 */           throw new ProtocolException("Target host missing");
/*    */         } 
/*    */       } 
/*    */       
/* 92 */       request.addHeader("Host", targethost.toHostString());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\protocol\RequestTargetHost.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */