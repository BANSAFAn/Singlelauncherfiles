/*    */ package org.apache.http.client.protocol;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.List;
/*    */ import org.apache.http.HttpException;
/*    */ import org.apache.http.HttpRequest;
/*    */ import org.apache.http.HttpRequestInterceptor;
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.client.config.RequestConfig;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Immutable
/*    */ public class RequestAcceptEncoding
/*    */   implements HttpRequestInterceptor
/*    */ {
/*    */   private final String acceptEncoding;
/*    */   
/*    */   public RequestAcceptEncoding(List<String> encodings) {
/* 57 */     if (encodings != null && !encodings.isEmpty()) {
/* 58 */       StringBuilder buf = new StringBuilder();
/* 59 */       for (int i = 0; i < encodings.size(); i++) {
/* 60 */         if (i > 0) {
/* 61 */           buf.append(",");
/*    */         }
/* 63 */         buf.append(encodings.get(i));
/*    */       } 
/* 65 */       this.acceptEncoding = buf.toString();
/*    */     } else {
/* 67 */       this.acceptEncoding = "gzip,deflate";
/*    */     } 
/*    */   }
/*    */   
/*    */   public RequestAcceptEncoding() {
/* 72 */     this(null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
/* 80 */     HttpClientContext clientContext = HttpClientContext.adapt(context);
/* 81 */     RequestConfig requestConfig = clientContext.getRequestConfig();
/*    */ 
/*    */     
/* 84 */     if (!request.containsHeader("Accept-Encoding") && requestConfig.isContentCompressionEnabled())
/* 85 */       request.addHeader("Accept-Encoding", this.acceptEncoding); 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\client\protocol\RequestAcceptEncoding.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */