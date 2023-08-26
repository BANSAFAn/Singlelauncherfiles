/*    */ package org.apache.http.protocol;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.http.HttpException;
/*    */ import org.apache.http.HttpRequest;
/*    */ import org.apache.http.HttpRequestInterceptor;
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.params.HttpParams;
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
/*    */ @Immutable
/*    */ public class RequestUserAgent
/*    */   implements HttpRequestInterceptor
/*    */ {
/*    */   private final String userAgent;
/*    */   
/*    */   public RequestUserAgent(String userAgent) {
/* 54 */     this.userAgent = userAgent;
/*    */   }
/*    */   
/*    */   public RequestUserAgent() {
/* 58 */     this(null);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
/* 64 */     Args.notNull(request, "HTTP request");
/* 65 */     if (!request.containsHeader("User-Agent")) {
/* 66 */       String s = null;
/* 67 */       HttpParams params = request.getParams();
/* 68 */       if (params != null) {
/* 69 */         s = (String)params.getParameter("http.useragent");
/*    */       }
/* 71 */       if (s == null) {
/* 72 */         s = this.userAgent;
/*    */       }
/* 74 */       if (s != null)
/* 75 */         request.addHeader("User-Agent", s); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\protocol\RequestUserAgent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */