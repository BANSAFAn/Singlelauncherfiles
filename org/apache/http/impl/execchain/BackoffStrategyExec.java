/*     */ package org.apache.http.impl.execchain;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.UndeclaredThrowableException;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.client.BackoffManager;
/*     */ import org.apache.http.client.ConnectionBackoffStrategy;
/*     */ import org.apache.http.client.methods.CloseableHttpResponse;
/*     */ import org.apache.http.client.methods.HttpExecutionAware;
/*     */ import org.apache.http.client.methods.HttpRequestWrapper;
/*     */ import org.apache.http.client.protocol.HttpClientContext;
/*     */ import org.apache.http.conn.routing.HttpRoute;
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
/*     */ @Immutable
/*     */ public class BackoffStrategyExec
/*     */   implements ClientExecChain
/*     */ {
/*     */   private final ClientExecChain requestExecutor;
/*     */   private final ConnectionBackoffStrategy connectionBackoffStrategy;
/*     */   private final BackoffManager backoffManager;
/*     */   
/*     */   public BackoffStrategyExec(ClientExecChain requestExecutor, ConnectionBackoffStrategy connectionBackoffStrategy, BackoffManager backoffManager) {
/*  59 */     Args.notNull(requestExecutor, "HTTP client request executor");
/*  60 */     Args.notNull(connectionBackoffStrategy, "Connection backoff strategy");
/*  61 */     Args.notNull(backoffManager, "Backoff manager");
/*  62 */     this.requestExecutor = requestExecutor;
/*  63 */     this.connectionBackoffStrategy = connectionBackoffStrategy;
/*  64 */     this.backoffManager = backoffManager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CloseableHttpResponse execute(HttpRoute route, HttpRequestWrapper request, HttpClientContext context, HttpExecutionAware execAware) throws IOException, HttpException {
/*  73 */     Args.notNull(route, "HTTP route");
/*  74 */     Args.notNull(request, "HTTP request");
/*  75 */     Args.notNull(context, "HTTP context");
/*  76 */     CloseableHttpResponse out = null;
/*     */     try {
/*  78 */       out = this.requestExecutor.execute(route, request, context, execAware);
/*  79 */     } catch (Exception ex) {
/*  80 */       if (out != null) {
/*  81 */         out.close();
/*     */       }
/*  83 */       if (this.connectionBackoffStrategy.shouldBackoff(ex)) {
/*  84 */         this.backoffManager.backOff(route);
/*     */       }
/*  86 */       if (ex instanceof RuntimeException) {
/*  87 */         throw (RuntimeException)ex;
/*     */       }
/*  89 */       if (ex instanceof HttpException) {
/*  90 */         throw (HttpException)ex;
/*     */       }
/*  92 */       if (ex instanceof IOException) {
/*  93 */         throw (IOException)ex;
/*     */       }
/*  95 */       throw new UndeclaredThrowableException(ex);
/*     */     } 
/*  97 */     if (this.connectionBackoffStrategy.shouldBackoff((HttpResponse)out)) {
/*  98 */       this.backoffManager.backOff(route);
/*     */     } else {
/* 100 */       this.backoffManager.probe(route);
/*     */     } 
/* 102 */     return out;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\execchain\BackoffStrategyExec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */