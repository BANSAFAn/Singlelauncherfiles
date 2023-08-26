/*     */ package org.apache.http.impl.execchain;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.client.ServiceUnavailableRetryStrategy;
/*     */ import org.apache.http.client.methods.CloseableHttpResponse;
/*     */ import org.apache.http.client.methods.HttpExecutionAware;
/*     */ import org.apache.http.client.methods.HttpRequestWrapper;
/*     */ import org.apache.http.client.protocol.HttpClientContext;
/*     */ import org.apache.http.conn.routing.HttpRoute;
/*     */ import org.apache.http.protocol.HttpContext;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ @Immutable
/*     */ public class ServiceUnavailableRetryExec
/*     */   implements ClientExecChain
/*     */ {
/*  61 */   private final Log log = LogFactory.getLog(getClass());
/*     */ 
/*     */   
/*     */   private final ClientExecChain requestExecutor;
/*     */   
/*     */   private final ServiceUnavailableRetryStrategy retryStrategy;
/*     */ 
/*     */   
/*     */   public ServiceUnavailableRetryExec(ClientExecChain requestExecutor, ServiceUnavailableRetryStrategy retryStrategy) {
/*  70 */     Args.notNull(requestExecutor, "HTTP request executor");
/*  71 */     Args.notNull(retryStrategy, "Retry strategy");
/*  72 */     this.requestExecutor = requestExecutor;
/*  73 */     this.retryStrategy = retryStrategy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CloseableHttpResponse execute(HttpRoute route, HttpRequestWrapper request, HttpClientContext context, HttpExecutionAware execAware) throws IOException, HttpException {
/*  82 */     Header[] origheaders = request.getAllHeaders();
/*  83 */     for (int c = 1;; c++) {
/*  84 */       CloseableHttpResponse response = this.requestExecutor.execute(route, request, context, execAware);
/*     */       
/*     */       try {
/*  87 */         if (this.retryStrategy.retryRequest((HttpResponse)response, c, (HttpContext)context)) {
/*  88 */           response.close();
/*  89 */           long nextInterval = this.retryStrategy.getRetryInterval();
/*  90 */           if (nextInterval > 0L) {
/*     */             try {
/*  92 */               this.log.trace("Wait for " + nextInterval);
/*  93 */               Thread.sleep(nextInterval);
/*  94 */             } catch (InterruptedException e) {
/*  95 */               Thread.currentThread().interrupt();
/*  96 */               throw new InterruptedIOException();
/*     */             } 
/*     */           }
/*  99 */           request.setHeaders(origheaders);
/*     */         } else {
/* 101 */           return response;
/*     */         } 
/* 103 */       } catch (RuntimeException ex) {
/* 104 */         response.close();
/* 105 */         throw ex;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\execchain\ServiceUnavailableRetryExec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */