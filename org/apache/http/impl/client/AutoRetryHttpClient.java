/*     */ package org.apache.http.impl.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.net.URI;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.client.HttpClient;
/*     */ import org.apache.http.client.ResponseHandler;
/*     */ import org.apache.http.client.ServiceUnavailableRetryStrategy;
/*     */ import org.apache.http.client.methods.HttpUriRequest;
/*     */ import org.apache.http.conn.ClientConnectionManager;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.EntityUtils;
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
/*     */ public class AutoRetryHttpClient
/*     */   implements HttpClient
/*     */ {
/*     */   private final HttpClient backend;
/*     */   private final ServiceUnavailableRetryStrategy retryStrategy;
/*  66 */   private final Log log = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */   
/*     */   public AutoRetryHttpClient(HttpClient client, ServiceUnavailableRetryStrategy retryStrategy) {
/*  71 */     Args.notNull(client, "HttpClient");
/*  72 */     Args.notNull(retryStrategy, "ServiceUnavailableRetryStrategy");
/*  73 */     this.backend = client;
/*  74 */     this.retryStrategy = retryStrategy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AutoRetryHttpClient() {
/*  83 */     this(new DefaultHttpClient(), new DefaultServiceUnavailableRetryStrategy());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AutoRetryHttpClient(ServiceUnavailableRetryStrategy config) {
/*  95 */     this(new DefaultHttpClient(), config);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AutoRetryHttpClient(HttpClient client) {
/* 107 */     this(client, new DefaultServiceUnavailableRetryStrategy());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpResponse execute(HttpHost target, HttpRequest request) throws IOException {
/* 113 */     HttpContext defaultContext = null;
/* 114 */     return execute(target, request, defaultContext);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler) throws IOException {
/* 120 */     return execute(target, request, responseHandler, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context) throws IOException {
/* 127 */     HttpResponse resp = execute(target, request, context);
/* 128 */     return (T)responseHandler.handleResponse(resp);
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpResponse execute(HttpUriRequest request) throws IOException {
/* 133 */     HttpContext context = null;
/* 134 */     return execute(request, context);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpResponse execute(HttpUriRequest request, HttpContext context) throws IOException {
/* 140 */     URI uri = request.getURI();
/* 141 */     HttpHost httpHost = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
/*     */     
/* 143 */     return execute(httpHost, (HttpRequest)request, context);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler) throws IOException {
/* 149 */     return execute(request, responseHandler, (HttpContext)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context) throws IOException {
/* 156 */     HttpResponse resp = execute(request, context);
/* 157 */     return (T)responseHandler.handleResponse(resp);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpResponse execute(HttpHost target, HttpRequest request, HttpContext context) throws IOException {
/* 163 */     for (int c = 1;; c++) {
/* 164 */       HttpResponse response = this.backend.execute(target, request, context);
/*     */       try {
/* 166 */         if (this.retryStrategy.retryRequest(response, c, context)) {
/* 167 */           EntityUtils.consume(response.getEntity());
/* 168 */           long nextInterval = this.retryStrategy.getRetryInterval();
/*     */           try {
/* 170 */             this.log.trace("Wait for " + nextInterval);
/* 171 */             Thread.sleep(nextInterval);
/* 172 */           } catch (InterruptedException e) {
/* 173 */             Thread.currentThread().interrupt();
/* 174 */             throw new InterruptedIOException();
/*     */           } 
/*     */         } else {
/* 177 */           return response;
/*     */         } 
/* 179 */       } catch (RuntimeException ex) {
/*     */         try {
/* 181 */           EntityUtils.consume(response.getEntity());
/* 182 */         } catch (IOException ioex) {
/* 183 */           this.log.warn("I/O error consuming response content", ioex);
/*     */         } 
/* 185 */         throw ex;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ClientConnectionManager getConnectionManager() {
/* 192 */     return this.backend.getConnectionManager();
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpParams getParams() {
/* 197 */     return this.backend.getParams();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\client\AutoRetryHttpClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */