/*    */ package org.apache.http.protocol;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.http.HttpException;
/*    */ import org.apache.http.HttpResponse;
/*    */ import org.apache.http.HttpResponseInterceptor;
/*    */ import org.apache.http.annotation.ThreadSafe;
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
/*    */ @ThreadSafe
/*    */ public class ResponseDate
/*    */   implements HttpResponseInterceptor
/*    */ {
/* 49 */   private static final HttpDateGenerator DATE_GENERATOR = new HttpDateGenerator();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
/* 58 */     Args.notNull(response, "HTTP response");
/* 59 */     int status = response.getStatusLine().getStatusCode();
/* 60 */     if (status >= 200 && !response.containsHeader("Date")) {
/*    */       
/* 62 */       String httpdate = DATE_GENERATOR.getCurrentDate();
/* 63 */       response.setHeader("Date", httpdate);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\protocol\ResponseDate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */