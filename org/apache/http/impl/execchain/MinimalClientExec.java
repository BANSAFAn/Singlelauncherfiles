/*     */ package org.apache.http.impl.execchain;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.ConnectionReuseStrategy;
/*     */ import org.apache.http.HttpClientConnection;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpRequestInterceptor;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.ProtocolException;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.client.config.RequestConfig;
/*     */ import org.apache.http.client.methods.CloseableHttpResponse;
/*     */ import org.apache.http.client.methods.HttpExecutionAware;
/*     */ import org.apache.http.client.methods.HttpRequestWrapper;
/*     */ import org.apache.http.client.methods.HttpUriRequest;
/*     */ import org.apache.http.client.protocol.HttpClientContext;
/*     */ import org.apache.http.client.protocol.RequestClientConnControl;
/*     */ import org.apache.http.client.utils.URIUtils;
/*     */ import org.apache.http.concurrent.Cancellable;
/*     */ import org.apache.http.conn.ConnectionKeepAliveStrategy;
/*     */ import org.apache.http.conn.ConnectionRequest;
/*     */ import org.apache.http.conn.HttpClientConnectionManager;
/*     */ import org.apache.http.conn.routing.HttpRoute;
/*     */ import org.apache.http.impl.conn.ConnectionShutdownException;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ import org.apache.http.protocol.HttpProcessor;
/*     */ import org.apache.http.protocol.HttpRequestExecutor;
/*     */ import org.apache.http.protocol.ImmutableHttpProcessor;
/*     */ import org.apache.http.protocol.RequestContent;
/*     */ import org.apache.http.protocol.RequestTargetHost;
/*     */ import org.apache.http.protocol.RequestUserAgent;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.VersionInfo;
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
/*     */ public class MinimalClientExec
/*     */   implements ClientExecChain
/*     */ {
/*  83 */   private final Log log = LogFactory.getLog(getClass());
/*     */   
/*     */   private final HttpRequestExecutor requestExecutor;
/*     */   
/*     */   private final HttpClientConnectionManager connManager;
/*     */   
/*     */   private final ConnectionReuseStrategy reuseStrategy;
/*     */   
/*     */   private final ConnectionKeepAliveStrategy keepAliveStrategy;
/*     */   
/*     */   private final HttpProcessor httpProcessor;
/*     */   
/*     */   public MinimalClientExec(HttpRequestExecutor requestExecutor, HttpClientConnectionManager connManager, ConnectionReuseStrategy reuseStrategy, ConnectionKeepAliveStrategy keepAliveStrategy) {
/*  96 */     Args.notNull(requestExecutor, "HTTP request executor");
/*  97 */     Args.notNull(connManager, "Client connection manager");
/*  98 */     Args.notNull(reuseStrategy, "Connection reuse strategy");
/*  99 */     Args.notNull(keepAliveStrategy, "Connection keep alive strategy");
/* 100 */     this.httpProcessor = (HttpProcessor)new ImmutableHttpProcessor(new HttpRequestInterceptor[] { (HttpRequestInterceptor)new RequestContent(), (HttpRequestInterceptor)new RequestTargetHost(), (HttpRequestInterceptor)new RequestClientConnControl(), (HttpRequestInterceptor)new RequestUserAgent(VersionInfo.getUserAgent("Apache-HttpClient", "org.apache.http.client", getClass())) });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 106 */     this.requestExecutor = requestExecutor;
/* 107 */     this.connManager = connManager;
/* 108 */     this.reuseStrategy = reuseStrategy;
/* 109 */     this.keepAliveStrategy = keepAliveStrategy;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static void rewriteRequestURI(HttpRequestWrapper request, HttpRoute route) throws ProtocolException {
/*     */     try {
/* 116 */       URI uri = request.getURI();
/* 117 */       if (uri != null) {
/*     */         
/* 119 */         if (uri.isAbsolute()) {
/* 120 */           uri = URIUtils.rewriteURI(uri, null, true);
/*     */         } else {
/* 122 */           uri = URIUtils.rewriteURI(uri);
/*     */         } 
/* 124 */         request.setURI(uri);
/*     */       } 
/* 126 */     } catch (URISyntaxException ex) {
/* 127 */       throw new ProtocolException("Invalid URI: " + request.getRequestLine().getUri(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CloseableHttpResponse execute(HttpRoute route, HttpRequestWrapper request, HttpClientContext context, HttpExecutionAware execAware) throws IOException, HttpException {
/*     */     HttpClientConnection managedConn;
/* 137 */     Args.notNull(route, "HTTP route");
/* 138 */     Args.notNull(request, "HTTP request");
/* 139 */     Args.notNull(context, "HTTP context");
/*     */     
/* 141 */     rewriteRequestURI(request, route);
/*     */     
/* 143 */     ConnectionRequest connRequest = this.connManager.requestConnection(route, null);
/* 144 */     if (execAware != null) {
/* 145 */       if (execAware.isAborted()) {
/* 146 */         connRequest.cancel();
/* 147 */         throw new RequestAbortedException("Request aborted");
/*     */       } 
/* 149 */       execAware.setCancellable((Cancellable)connRequest);
/*     */     } 
/*     */ 
/*     */     
/* 153 */     RequestConfig config = context.getRequestConfig();
/*     */ 
/*     */     
/*     */     try {
/* 157 */       int timeout = config.getConnectionRequestTimeout();
/* 158 */       managedConn = connRequest.get((timeout > 0) ? timeout : 0L, TimeUnit.MILLISECONDS);
/* 159 */     } catch (InterruptedException interrupted) {
/* 160 */       Thread.currentThread().interrupt();
/* 161 */       throw new RequestAbortedException("Request aborted", interrupted);
/* 162 */     } catch (ExecutionException ex) {
/* 163 */       Throwable cause = ex.getCause();
/* 164 */       if (cause == null) {
/* 165 */         cause = ex;
/*     */       }
/* 167 */       throw new RequestAbortedException("Request execution failed", cause);
/*     */     } 
/*     */     
/* 170 */     ConnectionHolder releaseTrigger = new ConnectionHolder(this.log, this.connManager, managedConn);
/*     */     try {
/* 172 */       if (execAware != null) {
/* 173 */         if (execAware.isAborted()) {
/* 174 */           releaseTrigger.close();
/* 175 */           throw new RequestAbortedException("Request aborted");
/*     */         } 
/* 177 */         execAware.setCancellable(releaseTrigger);
/*     */       } 
/*     */ 
/*     */       
/* 181 */       if (!managedConn.isOpen()) {
/* 182 */         int i = config.getConnectTimeout();
/* 183 */         this.connManager.connect(managedConn, route, (i > 0) ? i : 0, (HttpContext)context);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 188 */         this.connManager.routeComplete(managedConn, route, (HttpContext)context);
/*     */       } 
/* 190 */       int timeout = config.getSocketTimeout();
/* 191 */       if (timeout >= 0) {
/* 192 */         managedConn.setSocketTimeout(timeout);
/*     */       }
/*     */       
/* 195 */       HttpHost target = null;
/* 196 */       HttpRequest original = request.getOriginal();
/* 197 */       if (original instanceof HttpUriRequest) {
/* 198 */         URI uri = ((HttpUriRequest)original).getURI();
/* 199 */         if (uri.isAbsolute()) {
/* 200 */           target = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
/*     */         }
/*     */       } 
/* 203 */       if (target == null) {
/* 204 */         target = route.getTargetHost();
/*     */       }
/*     */       
/* 207 */       context.setAttribute("http.target_host", target);
/* 208 */       context.setAttribute("http.request", request);
/* 209 */       context.setAttribute("http.connection", managedConn);
/* 210 */       context.setAttribute("http.route", route);
/*     */       
/* 212 */       this.httpProcessor.process((HttpRequest)request, (HttpContext)context);
/* 213 */       HttpResponse response = this.requestExecutor.execute((HttpRequest)request, managedConn, (HttpContext)context);
/* 214 */       this.httpProcessor.process(response, (HttpContext)context);
/*     */ 
/*     */       
/* 217 */       if (this.reuseStrategy.keepAlive(response, (HttpContext)context)) {
/*     */         
/* 219 */         long duration = this.keepAliveStrategy.getKeepAliveDuration(response, (HttpContext)context);
/* 220 */         releaseTrigger.setValidFor(duration, TimeUnit.MILLISECONDS);
/* 221 */         releaseTrigger.markReusable();
/*     */       } else {
/* 223 */         releaseTrigger.markNonReusable();
/*     */       } 
/*     */ 
/*     */       
/* 227 */       HttpEntity entity = response.getEntity();
/* 228 */       if (entity == null || !entity.isStreaming()) {
/*     */         
/* 230 */         releaseTrigger.releaseConnection();
/* 231 */         return new HttpResponseProxy(response, null);
/*     */       } 
/* 233 */       return new HttpResponseProxy(response, releaseTrigger);
/*     */     }
/* 235 */     catch (ConnectionShutdownException ex) {
/* 236 */       InterruptedIOException ioex = new InterruptedIOException("Connection has been shut down");
/*     */       
/* 238 */       ioex.initCause((Throwable)ex);
/* 239 */       throw ioex;
/* 240 */     } catch (HttpException ex) {
/* 241 */       releaseTrigger.abortConnection();
/* 242 */       throw ex;
/* 243 */     } catch (IOException ex) {
/* 244 */       releaseTrigger.abortConnection();
/* 245 */       throw ex;
/* 246 */     } catch (RuntimeException ex) {
/* 247 */       releaseTrigger.abortConnection();
/* 248 */       throw ex;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\execchain\MinimalClientExec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */