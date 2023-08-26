/*     */ package org.apache.http.impl.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.http.ConnectionReuseStrategy;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.client.ClientProtocolException;
/*     */ import org.apache.http.client.config.RequestConfig;
/*     */ import org.apache.http.client.methods.CloseableHttpResponse;
/*     */ import org.apache.http.client.methods.Configurable;
/*     */ import org.apache.http.client.methods.HttpExecutionAware;
/*     */ import org.apache.http.client.methods.HttpRequestWrapper;
/*     */ import org.apache.http.client.protocol.HttpClientContext;
/*     */ import org.apache.http.conn.ClientConnectionManager;
/*     */ import org.apache.http.conn.ClientConnectionRequest;
/*     */ import org.apache.http.conn.HttpClientConnectionManager;
/*     */ import org.apache.http.conn.ManagedClientConnection;
/*     */ import org.apache.http.conn.routing.HttpRoute;
/*     */ import org.apache.http.conn.scheme.SchemeRegistry;
/*     */ import org.apache.http.impl.DefaultConnectionReuseStrategy;
/*     */ import org.apache.http.impl.execchain.MinimalClientExec;
/*     */ import org.apache.http.params.BasicHttpParams;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.protocol.BasicHttpContext;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ import org.apache.http.protocol.HttpRequestExecutor;
/*     */ import org.apache.http.util.Args;
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
/*     */ @ThreadSafe
/*     */ class MinimalHttpClient
/*     */   extends CloseableHttpClient
/*     */ {
/*     */   private final HttpClientConnectionManager connManager;
/*     */   private final MinimalClientExec requestExecutor;
/*     */   private final HttpParams params;
/*     */   
/*     */   public MinimalHttpClient(HttpClientConnectionManager connManager) {
/*  75 */     this.connManager = (HttpClientConnectionManager)Args.notNull(connManager, "HTTP connection manager");
/*  76 */     this.requestExecutor = new MinimalClientExec(new HttpRequestExecutor(), connManager, (ConnectionReuseStrategy)DefaultConnectionReuseStrategy.INSTANCE, DefaultConnectionKeepAliveStrategy.INSTANCE);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  81 */     this.params = (HttpParams)new BasicHttpParams();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected CloseableHttpResponse doExecute(HttpHost target, HttpRequest request, HttpContext context) throws IOException, ClientProtocolException {
/*  89 */     Args.notNull(target, "Target host");
/*  90 */     Args.notNull(request, "HTTP request");
/*  91 */     HttpExecutionAware execAware = null;
/*  92 */     if (request instanceof HttpExecutionAware) {
/*  93 */       execAware = (HttpExecutionAware)request;
/*     */     }
/*     */     try {
/*  96 */       HttpRequestWrapper wrapper = HttpRequestWrapper.wrap(request);
/*  97 */       HttpClientContext localcontext = HttpClientContext.adapt((context != null) ? context : (HttpContext)new BasicHttpContext());
/*     */       
/*  99 */       HttpRoute route = new HttpRoute(target);
/* 100 */       RequestConfig config = null;
/* 101 */       if (request instanceof Configurable) {
/* 102 */         config = ((Configurable)request).getConfig();
/*     */       }
/* 104 */       if (config != null) {
/* 105 */         localcontext.setRequestConfig(config);
/*     */       }
/* 107 */       return this.requestExecutor.execute(route, wrapper, localcontext, execAware);
/* 108 */     } catch (HttpException httpException) {
/* 109 */       throw new ClientProtocolException(httpException);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpParams getParams() {
/* 115 */     return this.params;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 120 */     this.connManager.shutdown();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ClientConnectionManager getConnectionManager() {
/* 126 */     return new ClientConnectionManager()
/*     */       {
/*     */         public void shutdown()
/*     */         {
/* 130 */           MinimalHttpClient.this.connManager.shutdown();
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public ClientConnectionRequest requestConnection(HttpRoute route, Object state) {
/* 136 */           throw new UnsupportedOperationException();
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public void releaseConnection(ManagedClientConnection conn, long validDuration, TimeUnit timeUnit) {
/* 143 */           throw new UnsupportedOperationException();
/*     */         }
/*     */ 
/*     */         
/*     */         public SchemeRegistry getSchemeRegistry() {
/* 148 */           throw new UnsupportedOperationException();
/*     */         }
/*     */ 
/*     */         
/*     */         public void closeIdleConnections(long idletime, TimeUnit tunit) {
/* 153 */           MinimalHttpClient.this.connManager.closeIdleConnections(idletime, tunit);
/*     */         }
/*     */ 
/*     */         
/*     */         public void closeExpiredConnections() {
/* 158 */           MinimalHttpClient.this.connManager.closeExpiredConnections();
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\client\MinimalHttpClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */