/*    */ package org.apache.http.conn.params;
/*    */ 
/*    */ import java.net.InetAddress;
/*    */ import org.apache.http.HttpHost;
/*    */ import org.apache.http.annotation.NotThreadSafe;
/*    */ import org.apache.http.conn.routing.HttpRoute;
/*    */ import org.apache.http.params.HttpAbstractParamBean;
/*    */ import org.apache.http.params.HttpParams;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
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
/*    */ @NotThreadSafe
/*    */ public class ConnRouteParamBean
/*    */   extends HttpAbstractParamBean
/*    */ {
/*    */   public ConnRouteParamBean(HttpParams params) {
/* 52 */     super(params);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setDefaultProxy(HttpHost defaultProxy) {
/* 57 */     this.params.setParameter("http.route.default-proxy", defaultProxy);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setLocalAddress(InetAddress address) {
/* 62 */     this.params.setParameter("http.route.local-address", address);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setForcedRoute(HttpRoute route) {
/* 67 */     this.params.setParameter("http.route.forced-route", route);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\conn\params\ConnRouteParamBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */