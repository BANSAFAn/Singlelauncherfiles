/*    */ package by.gdev.http.download.config;
/*    */ 
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import org.apache.http.conn.ConnectionKeepAliveStrategy;
/*    */ import org.apache.http.conn.HttpClientConnectionManager;
/*    */ import org.apache.http.impl.client.CloseableHttpClient;
/*    */ import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
/*    */ import org.apache.http.impl.client.HttpClients;
/*    */ import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HttpClientConfig
/*    */ {
/*    */   public static CloseableHttpClient getInstanceHttpClient() {
/* 17 */     PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
/* 18 */     cm.setDefaultMaxPerRoute(5);
/* 19 */     cm.setMaxTotal(20);
/*    */ 
/*    */     
/* 22 */     CloseableHttpClient builder = HttpClients.custom().setKeepAliveStrategy((ConnectionKeepAliveStrategy)DefaultConnectionKeepAliveStrategy.INSTANCE).setConnectionManager((HttpClientConnectionManager)cm).evictIdleConnections(10L, TimeUnit.SECONDS).build();
/* 23 */     return builder;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\by\gdev\http\download\config\HttpClientConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */