/*     */ package org.apache.http.impl.execchain;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.ConnectionReuseStrategy;
/*     */ import org.apache.http.HttpClientConnection;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpEntityEnclosingRequest;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpRequestInterceptor;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.auth.AuthProtocolState;
/*     */ import org.apache.http.auth.AuthState;
/*     */ import org.apache.http.client.AuthenticationStrategy;
/*     */ import org.apache.http.client.NonRepeatableRequestException;
/*     */ import org.apache.http.client.UserTokenHandler;
/*     */ import org.apache.http.client.config.RequestConfig;
/*     */ import org.apache.http.client.methods.CloseableHttpResponse;
/*     */ import org.apache.http.client.methods.HttpExecutionAware;
/*     */ import org.apache.http.client.methods.HttpRequestWrapper;
/*     */ import org.apache.http.client.protocol.HttpClientContext;
/*     */ import org.apache.http.concurrent.Cancellable;
/*     */ import org.apache.http.conn.ConnectionKeepAliveStrategy;
/*     */ import org.apache.http.conn.ConnectionRequest;
/*     */ import org.apache.http.conn.HttpClientConnectionManager;
/*     */ import org.apache.http.conn.routing.BasicRouteDirector;
/*     */ import org.apache.http.conn.routing.HttpRoute;
/*     */ import org.apache.http.conn.routing.HttpRouteDirector;
/*     */ import org.apache.http.conn.routing.RouteInfo;
/*     */ import org.apache.http.conn.routing.RouteTracker;
/*     */ import org.apache.http.entity.BufferedHttpEntity;
/*     */ import org.apache.http.impl.auth.HttpAuthenticator;
/*     */ import org.apache.http.impl.conn.ConnectionShutdownException;
/*     */ import org.apache.http.message.BasicHttpRequest;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ import org.apache.http.protocol.HttpProcessor;
/*     */ import org.apache.http.protocol.HttpRequestExecutor;
/*     */ import org.apache.http.protocol.ImmutableHttpProcessor;
/*     */ import org.apache.http.protocol.RequestTargetHost;
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
/*     */ @Immutable
/*     */ public class MainClientExec
/*     */   implements ClientExecChain
/*     */ {
/*  90 */   private final Log log = LogFactory.getLog(getClass());
/*     */ 
/*     */   
/*     */   private final HttpRequestExecutor requestExecutor;
/*     */   
/*     */   private final HttpClientConnectionManager connManager;
/*     */   
/*     */   private final ConnectionReuseStrategy reuseStrategy;
/*     */   
/*     */   private final ConnectionKeepAliveStrategy keepAliveStrategy;
/*     */   
/*     */   private final HttpProcessor proxyHttpProcessor;
/*     */   
/*     */   private final AuthenticationStrategy targetAuthStrategy;
/*     */   
/*     */   private final AuthenticationStrategy proxyAuthStrategy;
/*     */   
/*     */   private final HttpAuthenticator authenticator;
/*     */   
/*     */   private final UserTokenHandler userTokenHandler;
/*     */   
/*     */   private final HttpRouteDirector routeDirector;
/*     */ 
/*     */   
/*     */   public MainClientExec(HttpRequestExecutor requestExecutor, HttpClientConnectionManager connManager, ConnectionReuseStrategy reuseStrategy, ConnectionKeepAliveStrategy keepAliveStrategy, HttpProcessor proxyHttpProcessor, AuthenticationStrategy targetAuthStrategy, AuthenticationStrategy proxyAuthStrategy, UserTokenHandler userTokenHandler) {
/* 115 */     Args.notNull(requestExecutor, "HTTP request executor");
/* 116 */     Args.notNull(connManager, "Client connection manager");
/* 117 */     Args.notNull(reuseStrategy, "Connection reuse strategy");
/* 118 */     Args.notNull(keepAliveStrategy, "Connection keep alive strategy");
/* 119 */     Args.notNull(proxyHttpProcessor, "Proxy HTTP processor");
/* 120 */     Args.notNull(targetAuthStrategy, "Target authentication strategy");
/* 121 */     Args.notNull(proxyAuthStrategy, "Proxy authentication strategy");
/* 122 */     Args.notNull(userTokenHandler, "User token handler");
/* 123 */     this.authenticator = new HttpAuthenticator();
/* 124 */     this.routeDirector = (HttpRouteDirector)new BasicRouteDirector();
/* 125 */     this.requestExecutor = requestExecutor;
/* 126 */     this.connManager = connManager;
/* 127 */     this.reuseStrategy = reuseStrategy;
/* 128 */     this.keepAliveStrategy = keepAliveStrategy;
/* 129 */     this.proxyHttpProcessor = proxyHttpProcessor;
/* 130 */     this.targetAuthStrategy = targetAuthStrategy;
/* 131 */     this.proxyAuthStrategy = proxyAuthStrategy;
/* 132 */     this.userTokenHandler = userTokenHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MainClientExec(HttpRequestExecutor requestExecutor, HttpClientConnectionManager connManager, ConnectionReuseStrategy reuseStrategy, ConnectionKeepAliveStrategy keepAliveStrategy, AuthenticationStrategy targetAuthStrategy, AuthenticationStrategy proxyAuthStrategy, UserTokenHandler userTokenHandler) {
/* 143 */     this(requestExecutor, connManager, reuseStrategy, keepAliveStrategy, (HttpProcessor)new ImmutableHttpProcessor(new HttpRequestInterceptor[] { (HttpRequestInterceptor)new RequestTargetHost() }, ), targetAuthStrategy, proxyAuthStrategy, userTokenHandler);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CloseableHttpResponse execute(HttpRoute route, HttpRequestWrapper request, HttpClientContext context, HttpExecutionAware execAware) throws IOException, HttpException {
/*     */     HttpClientConnection managedConn;
/* 154 */     Args.notNull(route, "HTTP route");
/* 155 */     Args.notNull(request, "HTTP request");
/* 156 */     Args.notNull(context, "HTTP context");
/*     */     
/* 158 */     AuthState targetAuthState = context.getTargetAuthState();
/* 159 */     if (targetAuthState == null) {
/* 160 */       targetAuthState = new AuthState();
/* 161 */       context.setAttribute("http.auth.target-scope", targetAuthState);
/*     */     } 
/* 163 */     AuthState proxyAuthState = context.getProxyAuthState();
/* 164 */     if (proxyAuthState == null) {
/* 165 */       proxyAuthState = new AuthState();
/* 166 */       context.setAttribute("http.auth.proxy-scope", proxyAuthState);
/*     */     } 
/*     */     
/* 169 */     if (request instanceof HttpEntityEnclosingRequest) {
/* 170 */       RequestEntityProxy.enhance((HttpEntityEnclosingRequest)request);
/*     */     }
/*     */     
/* 173 */     Object userToken = context.getUserToken();
/*     */     
/* 175 */     ConnectionRequest connRequest = this.connManager.requestConnection(route, userToken);
/* 176 */     if (execAware != null) {
/* 177 */       if (execAware.isAborted()) {
/* 178 */         connRequest.cancel();
/* 179 */         throw new RequestAbortedException("Request aborted");
/*     */       } 
/* 181 */       execAware.setCancellable((Cancellable)connRequest);
/*     */     } 
/*     */ 
/*     */     
/* 185 */     RequestConfig config = context.getRequestConfig();
/*     */ 
/*     */     
/*     */     try {
/* 189 */       int timeout = config.getConnectionRequestTimeout();
/* 190 */       managedConn = connRequest.get((timeout > 0) ? timeout : 0L, TimeUnit.MILLISECONDS);
/* 191 */     } catch (InterruptedException interrupted) {
/* 192 */       Thread.currentThread().interrupt();
/* 193 */       throw new RequestAbortedException("Request aborted", interrupted);
/* 194 */     } catch (ExecutionException ex) {
/* 195 */       Throwable cause = ex.getCause();
/* 196 */       if (cause == null) {
/* 197 */         cause = ex;
/*     */       }
/* 199 */       throw new RequestAbortedException("Request execution failed", cause);
/*     */     } 
/*     */     
/* 202 */     context.setAttribute("http.connection", managedConn);
/*     */     
/* 204 */     if (config.isStaleConnectionCheckEnabled())
/*     */     {
/* 206 */       if (managedConn.isOpen()) {
/* 207 */         this.log.debug("Stale connection check");
/* 208 */         if (managedConn.isStale()) {
/* 209 */           this.log.debug("Stale connection detected");
/* 210 */           managedConn.close();
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 215 */     ConnectionHolder connHolder = new ConnectionHolder(this.log, this.connManager, managedConn); try {
/*     */       HttpResponse response;
/* 217 */       if (execAware != null) {
/* 218 */         execAware.setCancellable(connHolder);
/*     */       }
/*     */ 
/*     */       
/* 222 */       int execCount = 1;
/*     */       while (true) {
/* 224 */         if (execCount > 1 && !RequestEntityProxy.isRepeatable((HttpRequest)request)) {
/* 225 */           throw new NonRepeatableRequestException("Cannot retry request with a non-repeatable request entity.");
/*     */         }
/*     */ 
/*     */         
/* 229 */         if (execAware != null && execAware.isAborted()) {
/* 230 */           throw new RequestAbortedException("Request aborted");
/*     */         }
/*     */         
/* 233 */         if (!managedConn.isOpen()) {
/* 234 */           this.log.debug("Opening connection " + route);
/*     */           try {
/* 236 */             establishRoute(proxyAuthState, managedConn, route, (HttpRequest)request, context);
/* 237 */           } catch (TunnelRefusedException ex) {
/* 238 */             if (this.log.isDebugEnabled()) {
/* 239 */               this.log.debug(ex.getMessage());
/*     */             }
/* 241 */             HttpResponse httpResponse = ex.getResponse();
/*     */             break;
/*     */           } 
/*     */         } 
/* 245 */         int timeout = config.getSocketTimeout();
/* 246 */         if (timeout >= 0) {
/* 247 */           managedConn.setSocketTimeout(timeout);
/*     */         }
/*     */         
/* 250 */         if (execAware != null && execAware.isAborted()) {
/* 251 */           throw new RequestAbortedException("Request aborted");
/*     */         }
/*     */         
/* 254 */         if (this.log.isDebugEnabled()) {
/* 255 */           this.log.debug("Executing request " + request.getRequestLine());
/*     */         }
/*     */         
/* 258 */         if (!request.containsHeader("Authorization")) {
/* 259 */           if (this.log.isDebugEnabled()) {
/* 260 */             this.log.debug("Target auth state: " + targetAuthState.getState());
/*     */           }
/* 262 */           this.authenticator.generateAuthResponse((HttpRequest)request, targetAuthState, (HttpContext)context);
/*     */         } 
/* 264 */         if (!request.containsHeader("Proxy-Authorization") && !route.isTunnelled()) {
/* 265 */           if (this.log.isDebugEnabled()) {
/* 266 */             this.log.debug("Proxy auth state: " + proxyAuthState.getState());
/*     */           }
/* 268 */           this.authenticator.generateAuthResponse((HttpRequest)request, proxyAuthState, (HttpContext)context);
/*     */         } 
/*     */         
/* 271 */         response = this.requestExecutor.execute((HttpRequest)request, managedConn, (HttpContext)context);
/*     */ 
/*     */         
/* 274 */         if (this.reuseStrategy.keepAlive(response, (HttpContext)context)) {
/*     */           
/* 276 */           long duration = this.keepAliveStrategy.getKeepAliveDuration(response, (HttpContext)context);
/* 277 */           if (this.log.isDebugEnabled()) {
/*     */             String s;
/* 279 */             if (duration > 0L) {
/* 280 */               s = "for " + duration + " " + TimeUnit.MILLISECONDS;
/*     */             } else {
/* 282 */               s = "indefinitely";
/*     */             } 
/* 284 */             this.log.debug("Connection can be kept alive " + s);
/*     */           } 
/* 286 */           connHolder.setValidFor(duration, TimeUnit.MILLISECONDS);
/* 287 */           connHolder.markReusable();
/*     */         } else {
/* 289 */           connHolder.markNonReusable();
/*     */         } 
/*     */         
/* 292 */         if (needAuthentication(targetAuthState, proxyAuthState, route, response, context)) {
/*     */ 
/*     */           
/* 295 */           HttpEntity httpEntity = response.getEntity();
/* 296 */           if (connHolder.isReusable()) {
/* 297 */             EntityUtils.consume(httpEntity);
/*     */           } else {
/* 299 */             managedConn.close();
/* 300 */             if (proxyAuthState.getState() == AuthProtocolState.SUCCESS && proxyAuthState.getAuthScheme() != null && proxyAuthState.getAuthScheme().isConnectionBased()) {
/*     */ 
/*     */               
/* 303 */               this.log.debug("Resetting proxy auth state");
/* 304 */               proxyAuthState.reset();
/*     */             } 
/* 306 */             if (targetAuthState.getState() == AuthProtocolState.SUCCESS && targetAuthState.getAuthScheme() != null && targetAuthState.getAuthScheme().isConnectionBased()) {
/*     */ 
/*     */               
/* 309 */               this.log.debug("Resetting target auth state");
/* 310 */               targetAuthState.reset();
/*     */             } 
/*     */           } 
/*     */           
/* 314 */           HttpRequest original = request.getOriginal();
/* 315 */           if (!original.containsHeader("Authorization")) {
/* 316 */             request.removeHeaders("Authorization");
/*     */           }
/* 318 */           if (!original.containsHeader("Proxy-Authorization")) {
/* 319 */             request.removeHeaders("Proxy-Authorization");
/*     */           }
/*     */           
/*     */           execCount++;
/*     */         } 
/*     */         break;
/*     */       } 
/* 326 */       if (userToken == null) {
/* 327 */         userToken = this.userTokenHandler.getUserToken((HttpContext)context);
/* 328 */         context.setAttribute("http.user-token", userToken);
/*     */       } 
/* 330 */       if (userToken != null) {
/* 331 */         connHolder.setState(userToken);
/*     */       }
/*     */ 
/*     */       
/* 335 */       HttpEntity entity = response.getEntity();
/* 336 */       if (entity == null || !entity.isStreaming()) {
/*     */         
/* 338 */         connHolder.releaseConnection();
/* 339 */         return new HttpResponseProxy(response, null);
/*     */       } 
/* 341 */       return new HttpResponseProxy(response, connHolder);
/*     */     }
/* 343 */     catch (ConnectionShutdownException ex) {
/* 344 */       InterruptedIOException ioex = new InterruptedIOException("Connection has been shut down");
/*     */       
/* 346 */       ioex.initCause((Throwable)ex);
/* 347 */       throw ioex;
/* 348 */     } catch (HttpException ex) {
/* 349 */       connHolder.abortConnection();
/* 350 */       throw ex;
/* 351 */     } catch (IOException ex) {
/* 352 */       connHolder.abortConnection();
/* 353 */       throw ex;
/* 354 */     } catch (RuntimeException ex) {
/* 355 */       connHolder.abortConnection();
/* 356 */       throw ex;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void establishRoute(AuthState proxyAuthState, HttpClientConnection managedConn, HttpRoute route, HttpRequest request, HttpClientContext context) throws HttpException, IOException {
/*     */     int step;
/* 369 */     RequestConfig config = context.getRequestConfig();
/* 370 */     int timeout = config.getConnectTimeout();
/* 371 */     RouteTracker tracker = new RouteTracker(route); do {
/*     */       HttpHost proxy; boolean secure; int hop;
/*     */       boolean bool1;
/* 374 */       HttpRoute fact = tracker.toRoute();
/* 375 */       step = this.routeDirector.nextStep((RouteInfo)route, (RouteInfo)fact);
/*     */       
/* 377 */       switch (step) {
/*     */         
/*     */         case 1:
/* 380 */           this.connManager.connect(managedConn, route, (timeout > 0) ? timeout : 0, (HttpContext)context);
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 385 */           tracker.connectTarget(route.isSecure());
/*     */           break;
/*     */         case 2:
/* 388 */           this.connManager.connect(managedConn, route, (timeout > 0) ? timeout : 0, (HttpContext)context);
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 393 */           proxy = route.getProxyHost();
/* 394 */           tracker.connectProxy(proxy, false);
/*     */           break;
/*     */         case 3:
/* 397 */           secure = createTunnelToTarget(proxyAuthState, managedConn, route, request, context);
/*     */           
/* 399 */           this.log.debug("Tunnel to target created.");
/* 400 */           tracker.tunnelTarget(secure);
/*     */           break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case 4:
/* 408 */           hop = fact.getHopCount() - 1;
/* 409 */           bool1 = createTunnelToProxy(route, hop, context);
/* 410 */           this.log.debug("Tunnel to proxy created.");
/* 411 */           tracker.tunnelProxy(route.getHopTarget(hop), bool1);
/*     */           break;
/*     */         
/*     */         case 5:
/* 415 */           this.connManager.upgrade(managedConn, route, (HttpContext)context);
/* 416 */           tracker.layerProtocol(route.isSecure());
/*     */           break;
/*     */         
/*     */         case -1:
/* 420 */           throw new HttpException("Unable to establish route: planned = " + route + "; current = " + fact);
/*     */         
/*     */         case 0:
/* 423 */           this.connManager.routeComplete(managedConn, route, (HttpContext)context);
/*     */           break;
/*     */         default:
/* 426 */           throw new IllegalStateException("Unknown step indicator " + step + " from RouteDirector.");
/*     */       } 
/*     */ 
/*     */     
/* 430 */     } while (step > 0);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean createTunnelToTarget(AuthState proxyAuthState, HttpClientConnection managedConn, HttpRoute route, HttpRequest request, HttpClientContext context) throws HttpException, IOException {
/* 448 */     RequestConfig config = context.getRequestConfig();
/* 449 */     int timeout = config.getConnectTimeout();
/*     */     
/* 451 */     HttpHost target = route.getTargetHost();
/* 452 */     HttpHost proxy = route.getProxyHost();
/* 453 */     HttpResponse response = null;
/*     */     
/* 455 */     String authority = target.toHostString();
/* 456 */     BasicHttpRequest basicHttpRequest = new BasicHttpRequest("CONNECT", authority, request.getProtocolVersion());
/*     */     
/* 458 */     this.requestExecutor.preProcess((HttpRequest)basicHttpRequest, this.proxyHttpProcessor, (HttpContext)context);
/*     */     
/* 460 */     while (response == null) {
/* 461 */       if (!managedConn.isOpen()) {
/* 462 */         this.connManager.connect(managedConn, route, (timeout > 0) ? timeout : 0, (HttpContext)context);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 469 */       basicHttpRequest.removeHeaders("Proxy-Authorization");
/* 470 */       this.authenticator.generateAuthResponse((HttpRequest)basicHttpRequest, proxyAuthState, (HttpContext)context);
/*     */       
/* 472 */       response = this.requestExecutor.execute((HttpRequest)basicHttpRequest, managedConn, (HttpContext)context);
/*     */       
/* 474 */       int i = response.getStatusLine().getStatusCode();
/* 475 */       if (i < 200) {
/* 476 */         throw new HttpException("Unexpected response to CONNECT request: " + response.getStatusLine());
/*     */       }
/*     */ 
/*     */       
/* 480 */       if (config.isAuthenticationEnabled() && 
/* 481 */         this.authenticator.isAuthenticationRequested(proxy, response, this.proxyAuthStrategy, proxyAuthState, (HttpContext)context))
/*     */       {
/* 483 */         if (this.authenticator.handleAuthChallenge(proxy, response, this.proxyAuthStrategy, proxyAuthState, (HttpContext)context)) {
/*     */ 
/*     */           
/* 486 */           if (this.reuseStrategy.keepAlive(response, (HttpContext)context)) {
/* 487 */             this.log.debug("Connection kept alive");
/*     */             
/* 489 */             HttpEntity entity = response.getEntity();
/* 490 */             EntityUtils.consume(entity);
/*     */           } else {
/* 492 */             managedConn.close();
/*     */           } 
/* 494 */           response = null;
/*     */         } 
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 500 */     int status = response.getStatusLine().getStatusCode();
/*     */     
/* 502 */     if (status > 299) {
/*     */ 
/*     */       
/* 505 */       HttpEntity entity = response.getEntity();
/* 506 */       if (entity != null) {
/* 507 */         response.setEntity((HttpEntity)new BufferedHttpEntity(entity));
/*     */       }
/*     */       
/* 510 */       managedConn.close();
/* 511 */       throw new TunnelRefusedException("CONNECT refused by proxy: " + response.getStatusLine(), response);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 519 */     return false;
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
/*     */   private boolean createTunnelToProxy(HttpRoute route, int hop, HttpClientContext context) throws HttpException {
/* 541 */     throw new HttpException("Proxy chains are not supported.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean needAuthentication(AuthState targetAuthState, AuthState proxyAuthState, HttpRoute route, HttpResponse response, HttpClientContext context) {
/* 550 */     RequestConfig config = context.getRequestConfig();
/* 551 */     if (config.isAuthenticationEnabled()) {
/* 552 */       HttpHost target = context.getTargetHost();
/* 553 */       if (target == null) {
/* 554 */         target = route.getTargetHost();
/*     */       }
/* 556 */       if (target.getPort() < 0) {
/* 557 */         target = new HttpHost(target.getHostName(), route.getTargetHost().getPort(), target.getSchemeName());
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 562 */       boolean targetAuthRequested = this.authenticator.isAuthenticationRequested(target, response, this.targetAuthStrategy, targetAuthState, (HttpContext)context);
/*     */ 
/*     */       
/* 565 */       HttpHost proxy = route.getProxyHost();
/*     */       
/* 567 */       if (proxy == null) {
/* 568 */         proxy = route.getTargetHost();
/*     */       }
/* 570 */       boolean proxyAuthRequested = this.authenticator.isAuthenticationRequested(proxy, response, this.proxyAuthStrategy, proxyAuthState, (HttpContext)context);
/*     */ 
/*     */       
/* 573 */       if (targetAuthRequested) {
/* 574 */         return this.authenticator.handleAuthChallenge(target, response, this.targetAuthStrategy, targetAuthState, (HttpContext)context);
/*     */       }
/*     */       
/* 577 */       if (proxyAuthRequested) {
/* 578 */         return this.authenticator.handleAuthChallenge(proxy, response, this.proxyAuthStrategy, proxyAuthState, (HttpContext)context);
/*     */       }
/*     */     } 
/*     */     
/* 582 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\execchain\MainClientExec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */