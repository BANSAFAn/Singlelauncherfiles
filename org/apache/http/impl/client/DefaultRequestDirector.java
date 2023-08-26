/*      */ package org.apache.http.impl.client;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.InterruptedIOException;
/*      */ import java.net.URI;
/*      */ import java.net.URISyntaxException;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import org.apache.commons.logging.Log;
/*      */ import org.apache.commons.logging.LogFactory;
/*      */ import org.apache.http.ConnectionReuseStrategy;
/*      */ import org.apache.http.HttpClientConnection;
/*      */ import org.apache.http.HttpEntity;
/*      */ import org.apache.http.HttpEntityEnclosingRequest;
/*      */ import org.apache.http.HttpException;
/*      */ import org.apache.http.HttpHost;
/*      */ import org.apache.http.HttpRequest;
/*      */ import org.apache.http.HttpResponse;
/*      */ import org.apache.http.NoHttpResponseException;
/*      */ import org.apache.http.ProtocolException;
/*      */ import org.apache.http.ProtocolVersion;
/*      */ import org.apache.http.annotation.NotThreadSafe;
/*      */ import org.apache.http.auth.AuthProtocolState;
/*      */ import org.apache.http.auth.AuthScheme;
/*      */ import org.apache.http.auth.AuthState;
/*      */ import org.apache.http.auth.Credentials;
/*      */ import org.apache.http.auth.UsernamePasswordCredentials;
/*      */ import org.apache.http.client.AuthenticationHandler;
/*      */ import org.apache.http.client.AuthenticationStrategy;
/*      */ import org.apache.http.client.HttpRequestRetryHandler;
/*      */ import org.apache.http.client.NonRepeatableRequestException;
/*      */ import org.apache.http.client.RedirectException;
/*      */ import org.apache.http.client.RedirectHandler;
/*      */ import org.apache.http.client.RedirectStrategy;
/*      */ import org.apache.http.client.RequestDirector;
/*      */ import org.apache.http.client.UserTokenHandler;
/*      */ import org.apache.http.client.methods.AbortableHttpRequest;
/*      */ import org.apache.http.client.methods.HttpUriRequest;
/*      */ import org.apache.http.client.params.HttpClientParams;
/*      */ import org.apache.http.client.utils.URIUtils;
/*      */ import org.apache.http.conn.BasicManagedEntity;
/*      */ import org.apache.http.conn.ClientConnectionManager;
/*      */ import org.apache.http.conn.ClientConnectionRequest;
/*      */ import org.apache.http.conn.ConnectionKeepAliveStrategy;
/*      */ import org.apache.http.conn.ConnectionReleaseTrigger;
/*      */ import org.apache.http.conn.ManagedClientConnection;
/*      */ import org.apache.http.conn.routing.BasicRouteDirector;
/*      */ import org.apache.http.conn.routing.HttpRoute;
/*      */ import org.apache.http.conn.routing.HttpRoutePlanner;
/*      */ import org.apache.http.conn.routing.RouteInfo;
/*      */ import org.apache.http.conn.scheme.Scheme;
/*      */ import org.apache.http.entity.BufferedHttpEntity;
/*      */ import org.apache.http.impl.auth.BasicScheme;
/*      */ import org.apache.http.impl.conn.ConnectionShutdownException;
/*      */ import org.apache.http.message.BasicHttpRequest;
/*      */ import org.apache.http.params.HttpConnectionParams;
/*      */ import org.apache.http.params.HttpParams;
/*      */ import org.apache.http.params.HttpProtocolParams;
/*      */ import org.apache.http.protocol.HttpContext;
/*      */ import org.apache.http.protocol.HttpProcessor;
/*      */ import org.apache.http.protocol.HttpRequestExecutor;
/*      */ import org.apache.http.util.Args;
/*      */ import org.apache.http.util.EntityUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @Deprecated
/*      */ @NotThreadSafe
/*      */ public class DefaultRequestDirector
/*      */   implements RequestDirector
/*      */ {
/*      */   private final Log log;
/*      */   protected final ClientConnectionManager connManager;
/*      */   protected final HttpRoutePlanner routePlanner;
/*      */   protected final ConnectionReuseStrategy reuseStrategy;
/*      */   protected final ConnectionKeepAliveStrategy keepAliveStrategy;
/*      */   protected final HttpRequestExecutor requestExec;
/*      */   protected final HttpProcessor httpProcessor;
/*      */   protected final HttpRequestRetryHandler retryHandler;
/*      */   @Deprecated
/*      */   protected final RedirectHandler redirectHandler;
/*      */   protected final RedirectStrategy redirectStrategy;
/*      */   @Deprecated
/*      */   protected final AuthenticationHandler targetAuthHandler;
/*      */   protected final AuthenticationStrategy targetAuthStrategy;
/*      */   @Deprecated
/*      */   protected final AuthenticationHandler proxyAuthHandler;
/*      */   protected final AuthenticationStrategy proxyAuthStrategy;
/*      */   protected final UserTokenHandler userTokenHandler;
/*      */   protected final HttpParams params;
/*      */   protected ManagedClientConnection managedConn;
/*      */   protected final AuthState targetAuthState;
/*      */   protected final AuthState proxyAuthState;
/*      */   private final HttpAuthenticator authenticator;
/*      */   private int execCount;
/*      */   private int redirectCount;
/*      */   private final int maxRedirects;
/*      */   private HttpHost virtualHost;
/*      */   
/*      */   @Deprecated
/*      */   public DefaultRequestDirector(HttpRequestExecutor requestExec, ClientConnectionManager conman, ConnectionReuseStrategy reustrat, ConnectionKeepAliveStrategy kastrat, HttpRoutePlanner rouplan, HttpProcessor httpProcessor, HttpRequestRetryHandler retryHandler, RedirectHandler redirectHandler, AuthenticationHandler targetAuthHandler, AuthenticationHandler proxyAuthHandler, UserTokenHandler userTokenHandler, HttpParams params) {
/*  219 */     this(LogFactory.getLog(DefaultRequestDirector.class), requestExec, conman, reustrat, kastrat, rouplan, httpProcessor, retryHandler, new DefaultRedirectStrategyAdaptor(redirectHandler), new AuthenticationStrategyAdaptor(targetAuthHandler), new AuthenticationStrategyAdaptor(proxyAuthHandler), userTokenHandler, params);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public DefaultRequestDirector(Log log, HttpRequestExecutor requestExec, ClientConnectionManager conman, ConnectionReuseStrategy reustrat, ConnectionKeepAliveStrategy kastrat, HttpRoutePlanner rouplan, HttpProcessor httpProcessor, HttpRequestRetryHandler retryHandler, RedirectStrategy redirectStrategy, AuthenticationHandler targetAuthHandler, AuthenticationHandler proxyAuthHandler, UserTokenHandler userTokenHandler, HttpParams params) {
/*  244 */     this(LogFactory.getLog(DefaultRequestDirector.class), requestExec, conman, reustrat, kastrat, rouplan, httpProcessor, retryHandler, redirectStrategy, new AuthenticationStrategyAdaptor(targetAuthHandler), new AuthenticationStrategyAdaptor(proxyAuthHandler), userTokenHandler, params);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DefaultRequestDirector(Log log, HttpRequestExecutor requestExec, ClientConnectionManager conman, ConnectionReuseStrategy reustrat, ConnectionKeepAliveStrategy kastrat, HttpRoutePlanner rouplan, HttpProcessor httpProcessor, HttpRequestRetryHandler retryHandler, RedirectStrategy redirectStrategy, AuthenticationStrategy targetAuthStrategy, AuthenticationStrategy proxyAuthStrategy, UserTokenHandler userTokenHandler, HttpParams params) {
/*  271 */     Args.notNull(log, "Log");
/*  272 */     Args.notNull(requestExec, "Request executor");
/*  273 */     Args.notNull(conman, "Client connection manager");
/*  274 */     Args.notNull(reustrat, "Connection reuse strategy");
/*  275 */     Args.notNull(kastrat, "Connection keep alive strategy");
/*  276 */     Args.notNull(rouplan, "Route planner");
/*  277 */     Args.notNull(httpProcessor, "HTTP protocol processor");
/*  278 */     Args.notNull(retryHandler, "HTTP request retry handler");
/*  279 */     Args.notNull(redirectStrategy, "Redirect strategy");
/*  280 */     Args.notNull(targetAuthStrategy, "Target authentication strategy");
/*  281 */     Args.notNull(proxyAuthStrategy, "Proxy authentication strategy");
/*  282 */     Args.notNull(userTokenHandler, "User token handler");
/*  283 */     Args.notNull(params, "HTTP parameters");
/*  284 */     this.log = log;
/*  285 */     this.authenticator = new HttpAuthenticator(log);
/*  286 */     this.requestExec = requestExec;
/*  287 */     this.connManager = conman;
/*  288 */     this.reuseStrategy = reustrat;
/*  289 */     this.keepAliveStrategy = kastrat;
/*  290 */     this.routePlanner = rouplan;
/*  291 */     this.httpProcessor = httpProcessor;
/*  292 */     this.retryHandler = retryHandler;
/*  293 */     this.redirectStrategy = redirectStrategy;
/*  294 */     this.targetAuthStrategy = targetAuthStrategy;
/*  295 */     this.proxyAuthStrategy = proxyAuthStrategy;
/*  296 */     this.userTokenHandler = userTokenHandler;
/*  297 */     this.params = params;
/*      */     
/*  299 */     if (redirectStrategy instanceof DefaultRedirectStrategyAdaptor) {
/*  300 */       this.redirectHandler = ((DefaultRedirectStrategyAdaptor)redirectStrategy).getHandler();
/*      */     } else {
/*  302 */       this.redirectHandler = null;
/*      */     } 
/*  304 */     if (targetAuthStrategy instanceof AuthenticationStrategyAdaptor) {
/*  305 */       this.targetAuthHandler = ((AuthenticationStrategyAdaptor)targetAuthStrategy).getHandler();
/*      */     } else {
/*  307 */       this.targetAuthHandler = null;
/*      */     } 
/*  309 */     if (proxyAuthStrategy instanceof AuthenticationStrategyAdaptor) {
/*  310 */       this.proxyAuthHandler = ((AuthenticationStrategyAdaptor)proxyAuthStrategy).getHandler();
/*      */     } else {
/*  312 */       this.proxyAuthHandler = null;
/*      */     } 
/*      */     
/*  315 */     this.managedConn = null;
/*      */     
/*  317 */     this.execCount = 0;
/*  318 */     this.redirectCount = 0;
/*  319 */     this.targetAuthState = new AuthState();
/*  320 */     this.proxyAuthState = new AuthState();
/*  321 */     this.maxRedirects = this.params.getIntParameter("http.protocol.max-redirects", 100);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private RequestWrapper wrapRequest(HttpRequest request) throws ProtocolException {
/*  327 */     if (request instanceof HttpEntityEnclosingRequest) {
/*  328 */       return new EntityEnclosingRequestWrapper((HttpEntityEnclosingRequest)request);
/*      */     }
/*      */     
/*  331 */     return new RequestWrapper(request);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void rewriteRequestURI(RequestWrapper request, HttpRoute route) throws ProtocolException {
/*      */     try {
/*  342 */       URI uri = request.getURI();
/*  343 */       if (route.getProxyHost() != null && !route.isTunnelled()) {
/*      */         
/*  345 */         if (!uri.isAbsolute()) {
/*  346 */           HttpHost target = route.getTargetHost();
/*  347 */           uri = URIUtils.rewriteURI(uri, target, true);
/*      */         } else {
/*  349 */           uri = URIUtils.rewriteURI(uri);
/*      */         }
/*      */       
/*      */       }
/*  353 */       else if (uri.isAbsolute()) {
/*  354 */         uri = URIUtils.rewriteURI(uri, null, true);
/*      */       } else {
/*  356 */         uri = URIUtils.rewriteURI(uri);
/*      */       } 
/*      */       
/*  359 */       request.setURI(uri);
/*      */     }
/*  361 */     catch (URISyntaxException ex) {
/*  362 */       throw new ProtocolException("Invalid URI: " + request.getRequestLine().getUri(), ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public HttpResponse execute(HttpHost targetHost, HttpRequest request, HttpContext context) throws HttpException, IOException {
/*  374 */     context.setAttribute("http.auth.target-scope", this.targetAuthState);
/*  375 */     context.setAttribute("http.auth.proxy-scope", this.proxyAuthState);
/*      */     
/*  377 */     HttpHost target = targetHost;
/*      */     
/*  379 */     HttpRequest orig = request;
/*  380 */     RequestWrapper origWrapper = wrapRequest(orig);
/*  381 */     origWrapper.setParams(this.params);
/*  382 */     HttpRoute origRoute = determineRoute(target, (HttpRequest)origWrapper, context);
/*      */     
/*  384 */     this.virtualHost = (HttpHost)origWrapper.getParams().getParameter("http.virtual-host");
/*      */ 
/*      */     
/*  387 */     if (this.virtualHost != null && this.virtualHost.getPort() == -1) {
/*  388 */       HttpHost host = (target != null) ? target : origRoute.getTargetHost();
/*  389 */       int port = host.getPort();
/*  390 */       if (port != -1) {
/*  391 */         this.virtualHost = new HttpHost(this.virtualHost.getHostName(), port, this.virtualHost.getSchemeName());
/*      */       }
/*      */     } 
/*      */     
/*  395 */     RoutedRequest roureq = new RoutedRequest(origWrapper, origRoute);
/*      */     
/*  397 */     boolean reuse = false;
/*  398 */     boolean done = false;
/*      */     try {
/*  400 */       HttpResponse response = null;
/*  401 */       while (!done) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  407 */         RequestWrapper wrapper = roureq.getRequest();
/*  408 */         HttpRoute route = roureq.getRoute();
/*  409 */         response = null;
/*      */ 
/*      */         
/*  412 */         Object userToken = context.getAttribute("http.user-token");
/*      */ 
/*      */         
/*  415 */         if (this.managedConn == null) {
/*  416 */           ClientConnectionRequest connRequest = this.connManager.requestConnection(route, userToken);
/*      */           
/*  418 */           if (orig instanceof AbortableHttpRequest) {
/*  419 */             ((AbortableHttpRequest)orig).setConnectionRequest(connRequest);
/*      */           }
/*      */           
/*  422 */           long timeout = HttpClientParams.getConnectionManagerTimeout(this.params);
/*      */           try {
/*  424 */             this.managedConn = connRequest.getConnection(timeout, TimeUnit.MILLISECONDS);
/*  425 */           } catch (InterruptedException interrupted) {
/*  426 */             Thread.currentThread().interrupt();
/*  427 */             throw new InterruptedIOException();
/*      */           } 
/*      */           
/*  430 */           if (HttpConnectionParams.isStaleCheckingEnabled(this.params))
/*      */           {
/*  432 */             if (this.managedConn.isOpen()) {
/*  433 */               this.log.debug("Stale connection check");
/*  434 */               if (this.managedConn.isStale()) {
/*  435 */                 this.log.debug("Stale connection detected");
/*  436 */                 this.managedConn.close();
/*      */               } 
/*      */             } 
/*      */           }
/*      */         } 
/*      */         
/*  442 */         if (orig instanceof AbortableHttpRequest) {
/*  443 */           ((AbortableHttpRequest)orig).setReleaseTrigger((ConnectionReleaseTrigger)this.managedConn);
/*      */         }
/*      */         
/*      */         try {
/*  447 */           tryConnect(roureq, context);
/*  448 */         } catch (TunnelRefusedException ex) {
/*  449 */           if (this.log.isDebugEnabled()) {
/*  450 */             this.log.debug(ex.getMessage());
/*      */           }
/*  452 */           response = ex.getResponse();
/*      */           
/*      */           break;
/*      */         } 
/*  456 */         String userinfo = wrapper.getURI().getUserInfo();
/*  457 */         if (userinfo != null) {
/*  458 */           this.targetAuthState.update((AuthScheme)new BasicScheme(), (Credentials)new UsernamePasswordCredentials(userinfo));
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*  463 */         if (this.virtualHost != null) {
/*  464 */           target = this.virtualHost;
/*      */         } else {
/*  466 */           URI requestURI = wrapper.getURI();
/*  467 */           if (requestURI.isAbsolute()) {
/*  468 */             target = URIUtils.extractHost(requestURI);
/*      */           }
/*      */         } 
/*  471 */         if (target == null) {
/*  472 */           target = route.getTargetHost();
/*      */         }
/*      */ 
/*      */         
/*  476 */         wrapper.resetHeaders();
/*      */         
/*  478 */         rewriteRequestURI(wrapper, route);
/*      */ 
/*      */         
/*  481 */         context.setAttribute("http.target_host", target);
/*  482 */         context.setAttribute("http.route", route);
/*  483 */         context.setAttribute("http.connection", this.managedConn);
/*      */ 
/*      */         
/*  486 */         this.requestExec.preProcess((HttpRequest)wrapper, this.httpProcessor, context);
/*      */         
/*  488 */         response = tryExecute(roureq, context);
/*  489 */         if (response == null) {
/*      */           continue;
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*  495 */         response.setParams(this.params);
/*  496 */         this.requestExec.postProcess(response, this.httpProcessor, context);
/*      */ 
/*      */ 
/*      */         
/*  500 */         reuse = this.reuseStrategy.keepAlive(response, context);
/*  501 */         if (reuse) {
/*      */           
/*  503 */           long duration = this.keepAliveStrategy.getKeepAliveDuration(response, context);
/*  504 */           if (this.log.isDebugEnabled()) {
/*      */             String s;
/*  506 */             if (duration > 0L) {
/*  507 */               s = "for " + duration + " " + TimeUnit.MILLISECONDS;
/*      */             } else {
/*  509 */               s = "indefinitely";
/*      */             } 
/*  511 */             this.log.debug("Connection can be kept alive " + s);
/*      */           } 
/*  513 */           this.managedConn.setIdleDuration(duration, TimeUnit.MILLISECONDS);
/*      */         } 
/*      */         
/*  516 */         RoutedRequest followup = handleResponse(roureq, response, context);
/*  517 */         if (followup == null) {
/*  518 */           done = true;
/*      */         } else {
/*  520 */           if (reuse) {
/*      */             
/*  522 */             HttpEntity entity = response.getEntity();
/*  523 */             EntityUtils.consume(entity);
/*      */ 
/*      */             
/*  526 */             this.managedConn.markReusable();
/*      */           } else {
/*  528 */             this.managedConn.close();
/*  529 */             if (this.proxyAuthState.getState().compareTo((Enum)AuthProtocolState.CHALLENGED) > 0 && this.proxyAuthState.getAuthScheme() != null && this.proxyAuthState.getAuthScheme().isConnectionBased()) {
/*      */ 
/*      */               
/*  532 */               this.log.debug("Resetting proxy auth state");
/*  533 */               this.proxyAuthState.reset();
/*      */             } 
/*  535 */             if (this.targetAuthState.getState().compareTo((Enum)AuthProtocolState.CHALLENGED) > 0 && this.targetAuthState.getAuthScheme() != null && this.targetAuthState.getAuthScheme().isConnectionBased()) {
/*      */ 
/*      */               
/*  538 */               this.log.debug("Resetting target auth state");
/*  539 */               this.targetAuthState.reset();
/*      */             } 
/*      */           } 
/*      */           
/*  543 */           if (!followup.getRoute().equals(roureq.getRoute())) {
/*  544 */             releaseConnection();
/*      */           }
/*  546 */           roureq = followup;
/*      */         } 
/*      */         
/*  549 */         if (this.managedConn != null) {
/*  550 */           if (userToken == null) {
/*  551 */             userToken = this.userTokenHandler.getUserToken(context);
/*  552 */             context.setAttribute("http.user-token", userToken);
/*      */           } 
/*  554 */           if (userToken != null) {
/*  555 */             this.managedConn.setState(userToken);
/*      */           }
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  563 */       if (response == null || response.getEntity() == null || !response.getEntity().isStreaming()) {
/*      */ 
/*      */         
/*  566 */         if (reuse) {
/*  567 */           this.managedConn.markReusable();
/*      */         }
/*  569 */         releaseConnection();
/*      */       } else {
/*      */         
/*  572 */         HttpEntity entity = response.getEntity();
/*  573 */         BasicManagedEntity basicManagedEntity = new BasicManagedEntity(entity, this.managedConn, reuse);
/*  574 */         response.setEntity((HttpEntity)basicManagedEntity);
/*      */       } 
/*      */       
/*  577 */       return response;
/*      */     }
/*  579 */     catch (ConnectionShutdownException ex) {
/*  580 */       InterruptedIOException ioex = new InterruptedIOException("Connection has been shut down");
/*      */       
/*  582 */       ioex.initCause((Throwable)ex);
/*  583 */       throw ioex;
/*  584 */     } catch (HttpException ex) {
/*  585 */       abortConnection();
/*  586 */       throw ex;
/*  587 */     } catch (IOException ex) {
/*  588 */       abortConnection();
/*  589 */       throw ex;
/*  590 */     } catch (RuntimeException ex) {
/*  591 */       abortConnection();
/*  592 */       throw ex;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void tryConnect(RoutedRequest req, HttpContext context) throws HttpException, IOException {
/*  602 */     HttpRoute route = req.getRoute();
/*  603 */     RequestWrapper requestWrapper = req.getRequest();
/*      */     
/*  605 */     int connectCount = 0;
/*      */     while (true) {
/*  607 */       context.setAttribute("http.request", requestWrapper);
/*      */       
/*  609 */       connectCount++;
/*      */       try {
/*  611 */         if (!this.managedConn.isOpen()) {
/*  612 */           this.managedConn.open(route, context, this.params);
/*      */         } else {
/*  614 */           this.managedConn.setSocketTimeout(HttpConnectionParams.getSoTimeout(this.params));
/*      */         } 
/*  616 */         establishRoute(route, context);
/*      */       }
/*  618 */       catch (IOException ex) {
/*      */         try {
/*  620 */           this.managedConn.close();
/*  621 */         } catch (IOException ignore) {}
/*      */         
/*  623 */         if (this.retryHandler.retryRequest(ex, connectCount, context)) {
/*  624 */           if (this.log.isInfoEnabled()) {
/*  625 */             this.log.info("I/O exception (" + ex.getClass().getName() + ") caught when connecting to " + route + ": " + ex.getMessage());
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  630 */             if (this.log.isDebugEnabled()) {
/*  631 */               this.log.debug(ex.getMessage(), ex);
/*      */             }
/*  633 */             this.log.info("Retrying connect to " + route);
/*      */           }  continue;
/*      */         } 
/*  636 */         throw ex;
/*      */       } 
/*      */       break;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private HttpResponse tryExecute(RoutedRequest req, HttpContext context) throws HttpException, IOException {
/*  647 */     RequestWrapper wrapper = req.getRequest();
/*  648 */     HttpRoute route = req.getRoute();
/*  649 */     HttpResponse response = null;
/*      */     
/*  651 */     Exception retryReason = null;
/*      */     
/*      */     while (true) {
/*  654 */       this.execCount++;
/*      */       
/*  656 */       wrapper.incrementExecCount();
/*  657 */       if (!wrapper.isRepeatable()) {
/*  658 */         this.log.debug("Cannot retry non-repeatable request");
/*  659 */         if (retryReason != null) {
/*  660 */           throw new NonRepeatableRequestException("Cannot retry request with a non-repeatable request entity.  The cause lists the reason the original request failed.", retryReason);
/*      */         }
/*      */ 
/*      */         
/*  664 */         throw new NonRepeatableRequestException("Cannot retry request with a non-repeatable request entity.");
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/*  670 */         if (!this.managedConn.isOpen())
/*      */         {
/*      */           
/*  673 */           if (!route.isTunnelled()) {
/*  674 */             this.log.debug("Reopening the direct connection.");
/*  675 */             this.managedConn.open(route, context, this.params);
/*      */           } else {
/*      */             
/*  678 */             this.log.debug("Proxied connection. Need to start over.");
/*      */             
/*      */             break;
/*      */           } 
/*      */         }
/*  683 */         if (this.log.isDebugEnabled()) {
/*  684 */           this.log.debug("Attempt " + this.execCount + " to execute request");
/*      */         }
/*  686 */         response = this.requestExec.execute((HttpRequest)wrapper, (HttpClientConnection)this.managedConn, context);
/*      */       
/*      */       }
/*  689 */       catch (IOException ex) {
/*  690 */         this.log.debug("Closing the connection.");
/*      */         try {
/*  692 */           this.managedConn.close();
/*  693 */         } catch (IOException ignore) {}
/*      */         
/*  695 */         if (this.retryHandler.retryRequest(ex, wrapper.getExecCount(), context)) {
/*  696 */           if (this.log.isInfoEnabled()) {
/*  697 */             this.log.info("I/O exception (" + ex.getClass().getName() + ") caught when processing request to " + route + ": " + ex.getMessage());
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  703 */           if (this.log.isDebugEnabled()) {
/*  704 */             this.log.debug(ex.getMessage(), ex);
/*      */           }
/*  706 */           if (this.log.isInfoEnabled()) {
/*  707 */             this.log.info("Retrying request to " + route);
/*      */           }
/*  709 */           retryReason = ex; continue;
/*      */         } 
/*  711 */         if (ex instanceof NoHttpResponseException) {
/*  712 */           NoHttpResponseException updatedex = new NoHttpResponseException(route.getTargetHost().toHostString() + " failed to respond");
/*      */           
/*  714 */           updatedex.setStackTrace(ex.getStackTrace());
/*  715 */           throw updatedex;
/*      */         } 
/*  717 */         throw ex;
/*      */       } 
/*      */       
/*      */       break;
/*      */     } 
/*  722 */     return response;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void releaseConnection() {
/*      */     try {
/*  735 */       this.managedConn.releaseConnection();
/*  736 */     } catch (IOException ignored) {
/*  737 */       this.log.debug("IOException releasing connection", ignored);
/*      */     } 
/*  739 */     this.managedConn = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected HttpRoute determineRoute(HttpHost targetHost, HttpRequest request, HttpContext context) throws HttpException {
/*  763 */     return this.routePlanner.determineRoute((targetHost != null) ? targetHost : (HttpHost)request.getParams().getParameter("http.default-host"), request, context);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void establishRoute(HttpRoute route, HttpContext context) throws HttpException, IOException {
/*      */     int step;
/*  782 */     BasicRouteDirector basicRouteDirector = new BasicRouteDirector(); do {
/*      */       boolean secure; int hop;
/*      */       boolean bool1;
/*  785 */       HttpRoute fact = this.managedConn.getRoute();
/*  786 */       step = basicRouteDirector.nextStep((RouteInfo)route, (RouteInfo)fact);
/*      */       
/*  788 */       switch (step) {
/*      */         
/*      */         case 1:
/*      */         case 2:
/*  792 */           this.managedConn.open(route, context, this.params);
/*      */           break;
/*      */         
/*      */         case 3:
/*  796 */           secure = createTunnelToTarget(route, context);
/*  797 */           this.log.debug("Tunnel to target created.");
/*  798 */           this.managedConn.tunnelTarget(secure, this.params);
/*      */           break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case 4:
/*  806 */           hop = fact.getHopCount() - 1;
/*  807 */           bool1 = createTunnelToProxy(route, hop, context);
/*  808 */           this.log.debug("Tunnel to proxy created.");
/*  809 */           this.managedConn.tunnelProxy(route.getHopTarget(hop), bool1, this.params);
/*      */           break;
/*      */ 
/*      */ 
/*      */         
/*      */         case 5:
/*  815 */           this.managedConn.layerProtocol(context, this.params);
/*      */           break;
/*      */         
/*      */         case -1:
/*  819 */           throw new HttpException("Unable to establish route: planned = " + route + "; current = " + fact);
/*      */         
/*      */         case 0:
/*      */           break;
/*      */         
/*      */         default:
/*  825 */           throw new IllegalStateException("Unknown step indicator " + step + " from RouteDirector.");
/*      */       } 
/*      */ 
/*      */     
/*  829 */     } while (step > 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean createTunnelToTarget(HttpRoute route, HttpContext context) throws HttpException, IOException {
/*  857 */     HttpHost proxy = route.getProxyHost();
/*  858 */     HttpHost target = route.getTargetHost();
/*  859 */     HttpResponse response = null;
/*      */     
/*      */     while (true) {
/*  862 */       if (!this.managedConn.isOpen()) {
/*  863 */         this.managedConn.open(route, context, this.params);
/*      */       }
/*      */       
/*  866 */       HttpRequest connect = createConnectRequest(route, context);
/*  867 */       connect.setParams(this.params);
/*      */ 
/*      */       
/*  870 */       context.setAttribute("http.target_host", target);
/*  871 */       context.setAttribute("http.route", route);
/*  872 */       context.setAttribute("http.proxy_host", proxy);
/*  873 */       context.setAttribute("http.connection", this.managedConn);
/*  874 */       context.setAttribute("http.request", connect);
/*      */       
/*  876 */       this.requestExec.preProcess(connect, this.httpProcessor, context);
/*      */       
/*  878 */       response = this.requestExec.execute(connect, (HttpClientConnection)this.managedConn, context);
/*      */       
/*  880 */       response.setParams(this.params);
/*  881 */       this.requestExec.postProcess(response, this.httpProcessor, context);
/*      */       
/*  883 */       int i = response.getStatusLine().getStatusCode();
/*  884 */       if (i < 200) {
/*  885 */         throw new HttpException("Unexpected response to CONNECT request: " + response.getStatusLine());
/*      */       }
/*      */ 
/*      */       
/*  889 */       if (HttpClientParams.isAuthenticating(this.params)) {
/*  890 */         if (this.authenticator.isAuthenticationRequested(proxy, response, this.proxyAuthStrategy, this.proxyAuthState, context))
/*      */         {
/*  892 */           if (this.authenticator.authenticate(proxy, response, this.proxyAuthStrategy, this.proxyAuthState, context)) {
/*      */ 
/*      */             
/*  895 */             if (this.reuseStrategy.keepAlive(response, context)) {
/*  896 */               this.log.debug("Connection kept alive");
/*      */               
/*  898 */               HttpEntity entity = response.getEntity();
/*  899 */               EntityUtils.consume(entity); continue;
/*      */             } 
/*  901 */             this.managedConn.close();
/*      */ 
/*      */             
/*      */             continue;
/*      */           } 
/*      */         }
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/*      */     
/*  912 */     int status = response.getStatusLine().getStatusCode();
/*      */     
/*  914 */     if (status > 299) {
/*      */ 
/*      */       
/*  917 */       HttpEntity entity = response.getEntity();
/*  918 */       if (entity != null) {
/*  919 */         response.setEntity((HttpEntity)new BufferedHttpEntity(entity));
/*      */       }
/*      */       
/*  922 */       this.managedConn.close();
/*  923 */       throw new TunnelRefusedException("CONNECT refused by proxy: " + response.getStatusLine(), response);
/*      */     } 
/*      */ 
/*      */     
/*  927 */     this.managedConn.markReusable();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  933 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean createTunnelToProxy(HttpRoute route, int hop, HttpContext context) throws HttpException, IOException {
/*  969 */     throw new HttpException("Proxy chains are not supported.");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected HttpRequest createConnectRequest(HttpRoute route, HttpContext context) {
/*  989 */     HttpHost target = route.getTargetHost();
/*      */     
/*  991 */     String host = target.getHostName();
/*  992 */     int port = target.getPort();
/*  993 */     if (port < 0) {
/*  994 */       Scheme scheme = this.connManager.getSchemeRegistry().getScheme(target.getSchemeName());
/*      */       
/*  996 */       port = scheme.getDefaultPort();
/*      */     } 
/*      */     
/*  999 */     StringBuilder buffer = new StringBuilder(host.length() + 6);
/* 1000 */     buffer.append(host);
/* 1001 */     buffer.append(':');
/* 1002 */     buffer.append(Integer.toString(port));
/*      */     
/* 1004 */     String authority = buffer.toString();
/* 1005 */     ProtocolVersion ver = HttpProtocolParams.getVersion(this.params);
/* 1006 */     return (HttpRequest)new BasicHttpRequest("CONNECT", authority, ver);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected RoutedRequest handleResponse(RoutedRequest roureq, HttpResponse response, HttpContext context) throws HttpException, IOException {
/* 1031 */     HttpRoute route = roureq.getRoute();
/* 1032 */     RequestWrapper request = roureq.getRequest();
/*      */     
/* 1034 */     HttpParams params = request.getParams();
/*      */     
/* 1036 */     if (HttpClientParams.isAuthenticating(params)) {
/* 1037 */       HttpHost target = (HttpHost)context.getAttribute("http.target_host");
/* 1038 */       if (target == null) {
/* 1039 */         target = route.getTargetHost();
/*      */       }
/* 1041 */       if (target.getPort() < 0) {
/* 1042 */         Scheme scheme = this.connManager.getSchemeRegistry().getScheme(target);
/* 1043 */         target = new HttpHost(target.getHostName(), scheme.getDefaultPort(), target.getSchemeName());
/*      */       } 
/*      */       
/* 1046 */       boolean targetAuthRequested = this.authenticator.isAuthenticationRequested(target, response, this.targetAuthStrategy, this.targetAuthState, context);
/*      */ 
/*      */       
/* 1049 */       HttpHost proxy = route.getProxyHost();
/*      */       
/* 1051 */       if (proxy == null) {
/* 1052 */         proxy = route.getTargetHost();
/*      */       }
/* 1054 */       boolean proxyAuthRequested = this.authenticator.isAuthenticationRequested(proxy, response, this.proxyAuthStrategy, this.proxyAuthState, context);
/*      */ 
/*      */       
/* 1057 */       if (targetAuthRequested && 
/* 1058 */         this.authenticator.authenticate(target, response, this.targetAuthStrategy, this.targetAuthState, context))
/*      */       {
/*      */         
/* 1061 */         return roureq;
/*      */       }
/*      */       
/* 1064 */       if (proxyAuthRequested && 
/* 1065 */         this.authenticator.authenticate(proxy, response, this.proxyAuthStrategy, this.proxyAuthState, context))
/*      */       {
/*      */         
/* 1068 */         return roureq;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1073 */     if (HttpClientParams.isRedirecting(params) && this.redirectStrategy.isRedirected((HttpRequest)request, response, context)) {
/*      */ 
/*      */       
/* 1076 */       if (this.redirectCount >= this.maxRedirects) {
/* 1077 */         throw new RedirectException("Maximum redirects (" + this.maxRedirects + ") exceeded");
/*      */       }
/*      */       
/* 1080 */       this.redirectCount++;
/*      */ 
/*      */       
/* 1083 */       this.virtualHost = null;
/*      */       
/* 1085 */       HttpUriRequest redirect = this.redirectStrategy.getRedirect((HttpRequest)request, response, context);
/* 1086 */       HttpRequest orig = request.getOriginal();
/* 1087 */       redirect.setHeaders(orig.getAllHeaders());
/*      */       
/* 1089 */       URI uri = redirect.getURI();
/* 1090 */       HttpHost newTarget = URIUtils.extractHost(uri);
/* 1091 */       if (newTarget == null) {
/* 1092 */         throw new ProtocolException("Redirect URI does not specify a valid host name: " + uri);
/*      */       }
/*      */ 
/*      */       
/* 1096 */       if (!route.getTargetHost().equals(newTarget)) {
/* 1097 */         this.log.debug("Resetting target auth state");
/* 1098 */         this.targetAuthState.reset();
/* 1099 */         AuthScheme authScheme = this.proxyAuthState.getAuthScheme();
/* 1100 */         if (authScheme != null && authScheme.isConnectionBased()) {
/* 1101 */           this.log.debug("Resetting proxy auth state");
/* 1102 */           this.proxyAuthState.reset();
/*      */         } 
/*      */       } 
/*      */       
/* 1106 */       RequestWrapper wrapper = wrapRequest((HttpRequest)redirect);
/* 1107 */       wrapper.setParams(params);
/*      */       
/* 1109 */       HttpRoute newRoute = determineRoute(newTarget, (HttpRequest)wrapper, context);
/* 1110 */       RoutedRequest newRequest = new RoutedRequest(wrapper, newRoute);
/*      */       
/* 1112 */       if (this.log.isDebugEnabled()) {
/* 1113 */         this.log.debug("Redirecting to '" + uri + "' via " + newRoute);
/*      */       }
/*      */       
/* 1116 */       return newRequest;
/*      */     } 
/*      */     
/* 1119 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void abortConnection() {
/* 1129 */     ManagedClientConnection mcc = this.managedConn;
/* 1130 */     if (mcc != null) {
/*      */ 
/*      */       
/* 1133 */       this.managedConn = null;
/*      */       try {
/* 1135 */         mcc.abortConnection();
/* 1136 */       } catch (IOException ex) {
/* 1137 */         if (this.log.isDebugEnabled()) {
/* 1138 */           this.log.debug(ex.getMessage(), ex);
/*      */         }
/*      */       } 
/*      */       
/*      */       try {
/* 1143 */         mcc.releaseConnection();
/* 1144 */       } catch (IOException ignored) {
/* 1145 */         this.log.debug("Error releasing connection", ignored);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\client\DefaultRequestDirector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */