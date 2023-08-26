/*    */ package org.tlauncher.tlauncher.controller;
/*    */ 
/*    */ import com.google.inject.Inject;
/*    */ import java.io.IOException;
/*    */ import java.util.Objects;
/*    */ import org.apache.http.HttpResponse;
/*    */ import org.apache.http.client.ClientProtocolException;
/*    */ import org.apache.http.client.config.RequestConfig;
/*    */ import org.apache.http.client.methods.CloseableHttpResponse;
/*    */ import org.apache.http.client.methods.HttpDelete;
/*    */ import org.apache.http.client.methods.HttpUriRequest;
/*    */ import org.apache.http.impl.client.CloseableHttpClient;
/*    */ import org.apache.http.util.EntityUtils;
/*    */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*    */ 
/*    */ public class StatisticsController {
/*    */   @Inject
/*    */   private CloseableHttpClient closeableHttpClient;
/*    */   @Inject
/*    */   private RequestConfig config;
/*    */   
/*    */   public void removeUserData() throws ClientProtocolException, IOException {
/*    */     CloseableHttpResponse closeableHttpResponse;
/* 24 */     HttpDelete http = new HttpDelete(TLauncher.getInnerSettings().get("statistics.url") + String.format("user/client/%s", new Object[] { TLauncher.getInstance().getConfiguration().getClient().toString() }));
/* 25 */     http.setConfig(this.config);
/* 26 */     HttpResponse hr = null;
/*    */     try {
/* 28 */       closeableHttpResponse = this.closeableHttpClient.execute((HttpUriRequest)http);
/* 29 */       if (closeableHttpResponse.getStatusLine().getStatusCode() >= 300)
/* 30 */         throw new IOException("not proper code " + closeableHttpResponse.getStatusLine().toString()); 
/*    */     } finally {
/* 32 */       if (Objects.nonNull(null))
/* 33 */         EntityUtils.consumeQuietly(closeableHttpResponse.getEntity()); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\controller\StatisticsController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */