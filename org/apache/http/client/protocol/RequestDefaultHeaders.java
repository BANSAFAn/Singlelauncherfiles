/*    */ package org.apache.http.client.protocol;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.Collection;
/*    */ import org.apache.http.Header;
/*    */ import org.apache.http.HttpException;
/*    */ import org.apache.http.HttpRequest;
/*    */ import org.apache.http.HttpRequestInterceptor;
/*    */ import org.apache.http.annotation.Immutable;
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
/*    */ @Immutable
/*    */ public class RequestDefaultHeaders
/*    */   implements HttpRequestInterceptor
/*    */ {
/*    */   private final Collection<? extends Header> defaultHeaders;
/*    */   
/*    */   public RequestDefaultHeaders(Collection<? extends Header> defaultHeaders) {
/* 58 */     this.defaultHeaders = defaultHeaders;
/*    */   }
/*    */   
/*    */   public RequestDefaultHeaders() {
/* 62 */     this(null);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
/* 68 */     Args.notNull(request, "HTTP request");
/*    */     
/* 70 */     String method = request.getRequestLine().getMethod();
/* 71 */     if (method.equalsIgnoreCase("CONNECT")) {
/*    */       return;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 77 */     Collection<? extends Header> defHeaders = (Collection<? extends Header>)request.getParams().getParameter("http.default-headers");
/*    */     
/* 79 */     if (defHeaders == null) {
/* 80 */       defHeaders = this.defaultHeaders;
/*    */     }
/*    */     
/* 83 */     if (defHeaders != null)
/* 84 */       for (Header defHeader : defHeaders)
/* 85 */         request.addHeader(defHeader);  
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\client\protocol\RequestDefaultHeaders.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */