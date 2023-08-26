/*    */ package org.apache.http.conn.params;
/*    */ 
/*    */ import org.apache.http.annotation.NotThreadSafe;
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
/*    */ @Deprecated
/*    */ @NotThreadSafe
/*    */ public class ConnManagerParamBean
/*    */   extends HttpAbstractParamBean
/*    */ {
/*    */   public ConnManagerParamBean(HttpParams params) {
/* 48 */     super(params);
/*    */   }
/*    */   
/*    */   public void setTimeout(long timeout) {
/* 52 */     this.params.setLongParameter("http.conn-manager.timeout", timeout);
/*    */   }
/*    */   
/*    */   public void setMaxTotalConnections(int maxConnections) {
/* 56 */     this.params.setIntParameter("http.conn-manager.max-total", maxConnections);
/*    */   }
/*    */   
/*    */   public void setConnectionsPerRoute(ConnPerRouteBean connPerRoute) {
/* 60 */     this.params.setParameter("http.conn-manager.max-per-route", connPerRoute);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\conn\params\ConnManagerParamBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */