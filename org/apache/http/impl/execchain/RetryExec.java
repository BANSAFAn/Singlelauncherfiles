/*     */ package org.apache.http.impl.execchain;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.NoHttpResponseException;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.client.HttpRequestRetryHandler;
/*     */ import org.apache.http.client.NonRepeatableRequestException;
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
/*     */ public class RetryExec
/*     */   implements ClientExecChain
/*     */ {
/*  62 */   private final Log log = LogFactory.getLog(getClass());
/*     */   
/*     */   private final ClientExecChain requestExecutor;
/*     */   
/*     */   private final HttpRequestRetryHandler retryHandler;
/*     */ 
/*     */   
/*     */   public RetryExec(ClientExecChain requestExecutor, HttpRequestRetryHandler retryHandler) {
/*  70 */     Args.notNull(requestExecutor, "HTTP request executor");
/*  71 */     Args.notNull(retryHandler, "HTTP request retry handler");
/*  72 */     this.requestExecutor = requestExecutor;
/*  73 */     this.retryHandler = retryHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CloseableHttpResponse execute(HttpRoute route, HttpRequestWrapper request, HttpClientContext context, HttpExecutionAware execAware) throws IOException, HttpException {
/*  82 */     Args.notNull(route, "HTTP route");
/*  83 */     Args.notNull(request, "HTTP request");
/*  84 */     Args.notNull(context, "HTTP context");
/*  85 */     Header[] origheaders = request.getAllHeaders();
/*  86 */     for (int execCount = 1;; execCount++) {
/*     */       try {
/*  88 */         return this.requestExecutor.execute(route, request, context, execAware);
/*  89 */       } catch (IOException ex) {
/*  90 */         if (execAware != null && execAware.isAborted()) {
/*  91 */           this.log.debug("Request has been aborted");
/*  92 */           throw ex;
/*     */         } 
/*  94 */         if (this.retryHandler.retryRequest(ex, execCount, (HttpContext)context)) {
/*  95 */           if (this.log.isInfoEnabled()) {
/*  96 */             this.log.info("I/O exception (" + ex.getClass().getName() + ") caught when processing request to " + route + ": " + ex.getMessage());
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 102 */           if (this.log.isDebugEnabled()) {
/* 103 */             this.log.debug(ex.getMessage(), ex);
/*     */           }
/* 105 */           if (!RequestEntityProxy.isRepeatable((HttpRequest)request)) {
/* 106 */             this.log.debug("Cannot retry non-repeatable request");
/* 107 */             throw new NonRepeatableRequestException("Cannot retry request with a non-repeatable request entity", ex);
/*     */           } 
/*     */           
/* 110 */           request.setHeaders(origheaders);
/* 111 */           if (this.log.isInfoEnabled()) {
/* 112 */             this.log.info("Retrying request to " + route);
/*     */           }
/*     */         } else {
/* 115 */           if (ex instanceof NoHttpResponseException) {
/* 116 */             NoHttpResponseException updatedex = new NoHttpResponseException(route.getTargetHost().toHostString() + " failed to respond");
/*     */             
/* 118 */             updatedex.setStackTrace(ex.getStackTrace());
/* 119 */             throw updatedex;
/*     */           } 
/* 121 */           throw ex;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\execchain\RetryExec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */