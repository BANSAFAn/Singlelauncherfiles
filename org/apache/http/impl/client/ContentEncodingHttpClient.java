/*    */ package org.apache.http.impl.client;
/*    */ 
/*    */ import org.apache.http.HttpRequestInterceptor;
/*    */ import org.apache.http.HttpResponseInterceptor;
/*    */ import org.apache.http.annotation.ThreadSafe;
/*    */ import org.apache.http.client.protocol.RequestAcceptEncoding;
/*    */ import org.apache.http.client.protocol.ResponseContentEncoding;
/*    */ import org.apache.http.conn.ClientConnectionManager;
/*    */ import org.apache.http.params.HttpParams;
/*    */ import org.apache.http.protocol.BasicHttpProcessor;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
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
/*    */ @ThreadSafe
/*    */ public class ContentEncodingHttpClient
/*    */   extends DefaultHttpClient
/*    */ {
/*    */   public ContentEncodingHttpClient(ClientConnectionManager conman, HttpParams params) {
/* 63 */     super(conman, params);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ContentEncodingHttpClient(HttpParams params) {
/* 70 */     this(null, params);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ContentEncodingHttpClient() {
/* 77 */     this(null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected BasicHttpProcessor createHttpProcessor() {
/* 85 */     BasicHttpProcessor result = super.createHttpProcessor();
/*    */     
/* 87 */     result.addRequestInterceptor((HttpRequestInterceptor)new RequestAcceptEncoding());
/* 88 */     result.addResponseInterceptor((HttpResponseInterceptor)new ResponseContentEncoding());
/*    */     
/* 90 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\client\ContentEncodingHttpClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */