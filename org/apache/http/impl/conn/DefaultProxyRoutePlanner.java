/*    */ package org.apache.http.impl.conn;
/*    */ 
/*    */ import org.apache.http.HttpException;
/*    */ import org.apache.http.HttpHost;
/*    */ import org.apache.http.HttpRequest;
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.conn.SchemePortResolver;
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
/*    */ @Immutable
/*    */ public class DefaultProxyRoutePlanner
/*    */   extends DefaultRoutePlanner
/*    */ {
/*    */   private final HttpHost proxy;
/*    */   
/*    */   public DefaultProxyRoutePlanner(HttpHost proxy, SchemePortResolver schemePortResolver) {
/* 50 */     super(schemePortResolver);
/* 51 */     this.proxy = (HttpHost)Args.notNull(proxy, "Proxy host");
/*    */   }
/*    */   
/*    */   public DefaultProxyRoutePlanner(HttpHost proxy) {
/* 55 */     this(proxy, null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected HttpHost determineProxy(HttpHost target, HttpRequest request, HttpContext context) throws HttpException {
/* 63 */     return this.proxy;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\conn\DefaultProxyRoutePlanner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */