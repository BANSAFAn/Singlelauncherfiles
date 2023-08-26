/*    */ package org.apache.http.impl.client;
/*    */ 
/*    */ import org.apache.http.HeaderElement;
/*    */ import org.apache.http.HttpResponse;
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.conn.ConnectionKeepAliveStrategy;
/*    */ import org.apache.http.message.BasicHeaderElementIterator;
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
/*    */ @Immutable
/*    */ public class DefaultConnectionKeepAliveStrategy
/*    */   implements ConnectionKeepAliveStrategy
/*    */ {
/* 51 */   public static final DefaultConnectionKeepAliveStrategy INSTANCE = new DefaultConnectionKeepAliveStrategy();
/*    */ 
/*    */   
/*    */   public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
/* 55 */     Args.notNull(response, "HTTP response");
/* 56 */     BasicHeaderElementIterator basicHeaderElementIterator = new BasicHeaderElementIterator(response.headerIterator("Keep-Alive"));
/*    */     
/* 58 */     while (basicHeaderElementIterator.hasNext()) {
/* 59 */       HeaderElement he = basicHeaderElementIterator.nextElement();
/* 60 */       String param = he.getName();
/* 61 */       String value = he.getValue();
/* 62 */       if (value != null && param.equalsIgnoreCase("timeout")) {
/*    */         try {
/* 64 */           return Long.parseLong(value) * 1000L;
/* 65 */         } catch (NumberFormatException ignore) {}
/*    */       }
/*    */     } 
/*    */     
/* 69 */     return -1L;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\client\DefaultConnectionKeepAliveStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */