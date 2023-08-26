/*     */ package org.apache.http.impl.client;
/*     */ 
/*     */ import java.net.ProxySelector;
/*     */ import org.apache.http.ConnectionReuseStrategy;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.conn.ClientConnectionManager;
/*     */ import org.apache.http.conn.routing.HttpRoutePlanner;
/*     */ import org.apache.http.impl.DefaultConnectionReuseStrategy;
/*     */ import org.apache.http.impl.NoConnectionReuseStrategy;
/*     */ import org.apache.http.impl.conn.PoolingClientConnectionManager;
/*     */ import org.apache.http.impl.conn.ProxySelectorRoutePlanner;
/*     */ import org.apache.http.impl.conn.SchemeRegistryFactory;
/*     */ import org.apache.http.params.HttpParams;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ @ThreadSafe
/*     */ public class SystemDefaultHttpClient
/*     */   extends DefaultHttpClient
/*     */ {
/*     */   public SystemDefaultHttpClient(HttpParams params) {
/* 112 */     super((ClientConnectionManager)null, params);
/*     */   }
/*     */   
/*     */   public SystemDefaultHttpClient() {
/* 116 */     super((ClientConnectionManager)null, (HttpParams)null);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ClientConnectionManager createClientConnectionManager() {
/* 121 */     PoolingClientConnectionManager connmgr = new PoolingClientConnectionManager(SchemeRegistryFactory.createSystemDefault());
/*     */     
/* 123 */     String s = System.getProperty("http.keepAlive", "true");
/* 124 */     if ("true".equalsIgnoreCase(s)) {
/* 125 */       s = System.getProperty("http.maxConnections", "5");
/* 126 */       int max = Integer.parseInt(s);
/* 127 */       connmgr.setDefaultMaxPerRoute(max);
/* 128 */       connmgr.setMaxTotal(2 * max);
/*     */     } 
/* 130 */     return (ClientConnectionManager)connmgr;
/*     */   }
/*     */ 
/*     */   
/*     */   protected HttpRoutePlanner createHttpRoutePlanner() {
/* 135 */     return (HttpRoutePlanner)new ProxySelectorRoutePlanner(getConnectionManager().getSchemeRegistry(), ProxySelector.getDefault());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected ConnectionReuseStrategy createConnectionReuseStrategy() {
/* 141 */     String s = System.getProperty("http.keepAlive", "true");
/* 142 */     if ("true".equalsIgnoreCase(s)) {
/* 143 */       return (ConnectionReuseStrategy)new DefaultConnectionReuseStrategy();
/*     */     }
/* 145 */     return (ConnectionReuseStrategy)new NoConnectionReuseStrategy();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\client\SystemDefaultHttpClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */