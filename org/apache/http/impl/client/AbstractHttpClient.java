/*      */ package org.apache.http.impl.client;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.lang.reflect.UndeclaredThrowableException;
/*      */ import org.apache.commons.logging.Log;
/*      */ import org.apache.commons.logging.LogFactory;
/*      */ import org.apache.http.ConnectionReuseStrategy;
/*      */ import org.apache.http.HttpException;
/*      */ import org.apache.http.HttpHost;
/*      */ import org.apache.http.HttpRequest;
/*      */ import org.apache.http.HttpRequestInterceptor;
/*      */ import org.apache.http.HttpResponse;
/*      */ import org.apache.http.HttpResponseInterceptor;
/*      */ import org.apache.http.annotation.GuardedBy;
/*      */ import org.apache.http.annotation.ThreadSafe;
/*      */ import org.apache.http.auth.AuthSchemeFactory;
/*      */ import org.apache.http.auth.AuthSchemeRegistry;
/*      */ import org.apache.http.client.AuthenticationHandler;
/*      */ import org.apache.http.client.AuthenticationStrategy;
/*      */ import org.apache.http.client.BackoffManager;
/*      */ import org.apache.http.client.ClientProtocolException;
/*      */ import org.apache.http.client.ConnectionBackoffStrategy;
/*      */ import org.apache.http.client.CookieStore;
/*      */ import org.apache.http.client.CredentialsProvider;
/*      */ import org.apache.http.client.HttpRequestRetryHandler;
/*      */ import org.apache.http.client.RedirectHandler;
/*      */ import org.apache.http.client.RedirectStrategy;
/*      */ import org.apache.http.client.RequestDirector;
/*      */ import org.apache.http.client.UserTokenHandler;
/*      */ import org.apache.http.client.config.RequestConfig;
/*      */ import org.apache.http.client.methods.CloseableHttpResponse;
/*      */ import org.apache.http.client.params.HttpClientParamConfig;
/*      */ import org.apache.http.conn.ClientConnectionManager;
/*      */ import org.apache.http.conn.ClientConnectionManagerFactory;
/*      */ import org.apache.http.conn.ConnectionKeepAliveStrategy;
/*      */ import org.apache.http.conn.routing.HttpRoute;
/*      */ import org.apache.http.conn.routing.HttpRoutePlanner;
/*      */ import org.apache.http.conn.scheme.SchemeRegistry;
/*      */ import org.apache.http.cookie.CookieSpecFactory;
/*      */ import org.apache.http.cookie.CookieSpecRegistry;
/*      */ import org.apache.http.impl.DefaultConnectionReuseStrategy;
/*      */ import org.apache.http.impl.auth.BasicSchemeFactory;
/*      */ import org.apache.http.impl.auth.DigestSchemeFactory;
/*      */ import org.apache.http.impl.auth.KerberosSchemeFactory;
/*      */ import org.apache.http.impl.auth.NTLMSchemeFactory;
/*      */ import org.apache.http.impl.auth.SPNegoSchemeFactory;
/*      */ import org.apache.http.impl.conn.BasicClientConnectionManager;
/*      */ import org.apache.http.impl.conn.DefaultHttpRoutePlanner;
/*      */ import org.apache.http.impl.conn.SchemeRegistryFactory;
/*      */ import org.apache.http.impl.cookie.BestMatchSpecFactory;
/*      */ import org.apache.http.impl.cookie.BrowserCompatSpecFactory;
/*      */ import org.apache.http.impl.cookie.IgnoreSpecFactory;
/*      */ import org.apache.http.impl.cookie.NetscapeDraftSpecFactory;
/*      */ import org.apache.http.impl.cookie.RFC2109SpecFactory;
/*      */ import org.apache.http.impl.cookie.RFC2965SpecFactory;
/*      */ import org.apache.http.params.HttpParams;
/*      */ import org.apache.http.protocol.BasicHttpContext;
/*      */ import org.apache.http.protocol.BasicHttpProcessor;
/*      */ import org.apache.http.protocol.DefaultedHttpContext;
/*      */ import org.apache.http.protocol.HttpContext;
/*      */ import org.apache.http.protocol.HttpProcessor;
/*      */ import org.apache.http.protocol.HttpRequestExecutor;
/*      */ import org.apache.http.protocol.ImmutableHttpProcessor;
/*      */ import org.apache.http.util.Args;
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
/*      */ @ThreadSafe
/*      */ public abstract class AbstractHttpClient
/*      */   extends CloseableHttpClient
/*      */ {
/*  201 */   private final Log log = LogFactory.getLog(getClass());
/*      */ 
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private HttpParams defaultParams;
/*      */ 
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private HttpRequestExecutor requestExec;
/*      */ 
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private ClientConnectionManager connManager;
/*      */ 
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private ConnectionReuseStrategy reuseStrategy;
/*      */ 
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private ConnectionKeepAliveStrategy keepAliveStrategy;
/*      */ 
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private CookieSpecRegistry supportedCookieSpecs;
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private AuthSchemeRegistry supportedAuthSchemes;
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private BasicHttpProcessor mutableProcessor;
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private ImmutableHttpProcessor protocolProcessor;
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private HttpRequestRetryHandler retryHandler;
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private RedirectStrategy redirectStrategy;
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private AuthenticationStrategy targetAuthStrategy;
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private AuthenticationStrategy proxyAuthStrategy;
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private CookieStore cookieStore;
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private CredentialsProvider credsProvider;
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private HttpRoutePlanner routePlanner;
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private UserTokenHandler userTokenHandler;
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private ConnectionBackoffStrategy connectionBackoffStrategy;
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private BackoffManager backoffManager;
/*      */ 
/*      */ 
/*      */   
/*      */   protected AbstractHttpClient(ClientConnectionManager conman, HttpParams params) {
/*  288 */     this.defaultParams = params;
/*  289 */     this.connManager = conman;
/*      */   }
/*      */ 
/*      */   
/*      */   protected abstract HttpParams createHttpParams();
/*      */ 
/*      */   
/*      */   protected abstract BasicHttpProcessor createHttpProcessor();
/*      */ 
/*      */   
/*      */   protected HttpContext createHttpContext() {
/*  300 */     BasicHttpContext basicHttpContext = new BasicHttpContext();
/*  301 */     basicHttpContext.setAttribute("http.scheme-registry", getConnectionManager().getSchemeRegistry());
/*      */ 
/*      */     
/*  304 */     basicHttpContext.setAttribute("http.authscheme-registry", getAuthSchemes());
/*      */ 
/*      */     
/*  307 */     basicHttpContext.setAttribute("http.cookiespec-registry", getCookieSpecs());
/*      */ 
/*      */     
/*  310 */     basicHttpContext.setAttribute("http.cookie-store", getCookieStore());
/*      */ 
/*      */     
/*  313 */     basicHttpContext.setAttribute("http.auth.credentials-provider", getCredentialsProvider());
/*      */ 
/*      */     
/*  316 */     return (HttpContext)basicHttpContext;
/*      */   }
/*      */   
/*      */   protected ClientConnectionManager createClientConnectionManager() {
/*      */     BasicClientConnectionManager basicClientConnectionManager;
/*  321 */     SchemeRegistry registry = SchemeRegistryFactory.createDefault();
/*      */     
/*  323 */     ClientConnectionManager connManager = null;
/*  324 */     HttpParams params = getParams();
/*      */     
/*  326 */     ClientConnectionManagerFactory factory = null;
/*      */     
/*  328 */     String className = (String)params.getParameter("http.connection-manager.factory-class-name");
/*      */     
/*  330 */     if (className != null) {
/*      */       try {
/*  332 */         Class<?> clazz = Class.forName(className);
/*  333 */         factory = (ClientConnectionManagerFactory)clazz.newInstance();
/*  334 */       } catch (ClassNotFoundException ex) {
/*  335 */         throw new IllegalStateException("Invalid class name: " + className);
/*  336 */       } catch (IllegalAccessException ex) {
/*  337 */         throw new IllegalAccessError(ex.getMessage());
/*  338 */       } catch (InstantiationException ex) {
/*  339 */         throw new InstantiationError(ex.getMessage());
/*      */       } 
/*      */     }
/*  342 */     if (factory != null) {
/*  343 */       connManager = factory.newInstance(params, registry);
/*      */     } else {
/*  345 */       basicClientConnectionManager = new BasicClientConnectionManager(registry);
/*      */     } 
/*      */     
/*  348 */     return (ClientConnectionManager)basicClientConnectionManager;
/*      */   }
/*      */ 
/*      */   
/*      */   protected AuthSchemeRegistry createAuthSchemeRegistry() {
/*  353 */     AuthSchemeRegistry registry = new AuthSchemeRegistry();
/*  354 */     registry.register("Basic", (AuthSchemeFactory)new BasicSchemeFactory());
/*      */ 
/*      */     
/*  357 */     registry.register("Digest", (AuthSchemeFactory)new DigestSchemeFactory());
/*      */ 
/*      */     
/*  360 */     registry.register("NTLM", (AuthSchemeFactory)new NTLMSchemeFactory());
/*      */ 
/*      */     
/*  363 */     registry.register("Negotiate", (AuthSchemeFactory)new SPNegoSchemeFactory());
/*      */ 
/*      */     
/*  366 */     registry.register("Kerberos", (AuthSchemeFactory)new KerberosSchemeFactory());
/*      */ 
/*      */     
/*  369 */     return registry;
/*      */   }
/*      */ 
/*      */   
/*      */   protected CookieSpecRegistry createCookieSpecRegistry() {
/*  374 */     CookieSpecRegistry registry = new CookieSpecRegistry();
/*  375 */     registry.register("default", (CookieSpecFactory)new BestMatchSpecFactory());
/*      */ 
/*      */     
/*  378 */     registry.register("best-match", (CookieSpecFactory)new BestMatchSpecFactory());
/*      */ 
/*      */     
/*  381 */     registry.register("compatibility", (CookieSpecFactory)new BrowserCompatSpecFactory());
/*      */ 
/*      */     
/*  384 */     registry.register("netscape", (CookieSpecFactory)new NetscapeDraftSpecFactory());
/*      */ 
/*      */     
/*  387 */     registry.register("rfc2109", (CookieSpecFactory)new RFC2109SpecFactory());
/*      */ 
/*      */     
/*  390 */     registry.register("rfc2965", (CookieSpecFactory)new RFC2965SpecFactory());
/*      */ 
/*      */     
/*  393 */     registry.register("ignoreCookies", (CookieSpecFactory)new IgnoreSpecFactory());
/*      */ 
/*      */     
/*  396 */     return registry;
/*      */   }
/*      */   
/*      */   protected HttpRequestExecutor createRequestExecutor() {
/*  400 */     return new HttpRequestExecutor();
/*      */   }
/*      */   
/*      */   protected ConnectionReuseStrategy createConnectionReuseStrategy() {
/*  404 */     return (ConnectionReuseStrategy)new DefaultConnectionReuseStrategy();
/*      */   }
/*      */   
/*      */   protected ConnectionKeepAliveStrategy createConnectionKeepAliveStrategy() {
/*  408 */     return new DefaultConnectionKeepAliveStrategy();
/*      */   }
/*      */   
/*      */   protected HttpRequestRetryHandler createHttpRequestRetryHandler() {
/*  412 */     return new DefaultHttpRequestRetryHandler();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   protected RedirectHandler createRedirectHandler() {
/*  420 */     return new DefaultRedirectHandler();
/*      */   }
/*      */   
/*      */   protected AuthenticationStrategy createTargetAuthenticationStrategy() {
/*  424 */     return new TargetAuthenticationStrategy();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   protected AuthenticationHandler createTargetAuthenticationHandler() {
/*  432 */     return new DefaultTargetAuthenticationHandler();
/*      */   }
/*      */   
/*      */   protected AuthenticationStrategy createProxyAuthenticationStrategy() {
/*  436 */     return new ProxyAuthenticationStrategy();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   protected AuthenticationHandler createProxyAuthenticationHandler() {
/*  444 */     return new DefaultProxyAuthenticationHandler();
/*      */   }
/*      */   
/*      */   protected CookieStore createCookieStore() {
/*  448 */     return new BasicCookieStore();
/*      */   }
/*      */   
/*      */   protected CredentialsProvider createCredentialsProvider() {
/*  452 */     return new BasicCredentialsProvider();
/*      */   }
/*      */   
/*      */   protected HttpRoutePlanner createHttpRoutePlanner() {
/*  456 */     return (HttpRoutePlanner)new DefaultHttpRoutePlanner(getConnectionManager().getSchemeRegistry());
/*      */   }
/*      */   
/*      */   protected UserTokenHandler createUserTokenHandler() {
/*  460 */     return new DefaultUserTokenHandler();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final synchronized HttpParams getParams() {
/*  466 */     if (this.defaultParams == null) {
/*  467 */       this.defaultParams = createHttpParams();
/*      */     }
/*  469 */     return this.defaultParams;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void setParams(HttpParams params) {
/*  479 */     this.defaultParams = params;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final synchronized ClientConnectionManager getConnectionManager() {
/*  485 */     if (this.connManager == null) {
/*  486 */       this.connManager = createClientConnectionManager();
/*      */     }
/*  488 */     return this.connManager;
/*      */   }
/*      */ 
/*      */   
/*      */   public final synchronized HttpRequestExecutor getRequestExecutor() {
/*  493 */     if (this.requestExec == null) {
/*  494 */       this.requestExec = createRequestExecutor();
/*      */     }
/*  496 */     return this.requestExec;
/*      */   }
/*      */ 
/*      */   
/*      */   public final synchronized AuthSchemeRegistry getAuthSchemes() {
/*  501 */     if (this.supportedAuthSchemes == null) {
/*  502 */       this.supportedAuthSchemes = createAuthSchemeRegistry();
/*      */     }
/*  504 */     return this.supportedAuthSchemes;
/*      */   }
/*      */   
/*      */   public synchronized void setAuthSchemes(AuthSchemeRegistry registry) {
/*  508 */     this.supportedAuthSchemes = registry;
/*      */   }
/*      */   
/*      */   public final synchronized ConnectionBackoffStrategy getConnectionBackoffStrategy() {
/*  512 */     return this.connectionBackoffStrategy;
/*      */   }
/*      */   
/*      */   public synchronized void setConnectionBackoffStrategy(ConnectionBackoffStrategy strategy) {
/*  516 */     this.connectionBackoffStrategy = strategy;
/*      */   }
/*      */   
/*      */   public final synchronized CookieSpecRegistry getCookieSpecs() {
/*  520 */     if (this.supportedCookieSpecs == null) {
/*  521 */       this.supportedCookieSpecs = createCookieSpecRegistry();
/*      */     }
/*  523 */     return this.supportedCookieSpecs;
/*      */   }
/*      */   
/*      */   public final synchronized BackoffManager getBackoffManager() {
/*  527 */     return this.backoffManager;
/*      */   }
/*      */   
/*      */   public synchronized void setBackoffManager(BackoffManager manager) {
/*  531 */     this.backoffManager = manager;
/*      */   }
/*      */   
/*      */   public synchronized void setCookieSpecs(CookieSpecRegistry registry) {
/*  535 */     this.supportedCookieSpecs = registry;
/*      */   }
/*      */   
/*      */   public final synchronized ConnectionReuseStrategy getConnectionReuseStrategy() {
/*  539 */     if (this.reuseStrategy == null) {
/*  540 */       this.reuseStrategy = createConnectionReuseStrategy();
/*      */     }
/*  542 */     return this.reuseStrategy;
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setReuseStrategy(ConnectionReuseStrategy strategy) {
/*  547 */     this.reuseStrategy = strategy;
/*      */   }
/*      */ 
/*      */   
/*      */   public final synchronized ConnectionKeepAliveStrategy getConnectionKeepAliveStrategy() {
/*  552 */     if (this.keepAliveStrategy == null) {
/*  553 */       this.keepAliveStrategy = createConnectionKeepAliveStrategy();
/*      */     }
/*  555 */     return this.keepAliveStrategy;
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setKeepAliveStrategy(ConnectionKeepAliveStrategy strategy) {
/*  560 */     this.keepAliveStrategy = strategy;
/*      */   }
/*      */ 
/*      */   
/*      */   public final synchronized HttpRequestRetryHandler getHttpRequestRetryHandler() {
/*  565 */     if (this.retryHandler == null) {
/*  566 */       this.retryHandler = createHttpRequestRetryHandler();
/*      */     }
/*  568 */     return this.retryHandler;
/*      */   }
/*      */   
/*      */   public synchronized void setHttpRequestRetryHandler(HttpRequestRetryHandler handler) {
/*  572 */     this.retryHandler = handler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public final synchronized RedirectHandler getRedirectHandler() {
/*  580 */     return createRedirectHandler();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public synchronized void setRedirectHandler(RedirectHandler handler) {
/*  588 */     this.redirectStrategy = new DefaultRedirectStrategyAdaptor(handler);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final synchronized RedirectStrategy getRedirectStrategy() {
/*  595 */     if (this.redirectStrategy == null) {
/*  596 */       this.redirectStrategy = new DefaultRedirectStrategy();
/*      */     }
/*  598 */     return this.redirectStrategy;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void setRedirectStrategy(RedirectStrategy strategy) {
/*  605 */     this.redirectStrategy = strategy;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public final synchronized AuthenticationHandler getTargetAuthenticationHandler() {
/*  613 */     return createTargetAuthenticationHandler();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public synchronized void setTargetAuthenticationHandler(AuthenticationHandler handler) {
/*  621 */     this.targetAuthStrategy = new AuthenticationStrategyAdaptor(handler);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final synchronized AuthenticationStrategy getTargetAuthenticationStrategy() {
/*  628 */     if (this.targetAuthStrategy == null) {
/*  629 */       this.targetAuthStrategy = createTargetAuthenticationStrategy();
/*      */     }
/*  631 */     return this.targetAuthStrategy;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void setTargetAuthenticationStrategy(AuthenticationStrategy strategy) {
/*  638 */     this.targetAuthStrategy = strategy;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public final synchronized AuthenticationHandler getProxyAuthenticationHandler() {
/*  646 */     return createProxyAuthenticationHandler();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public synchronized void setProxyAuthenticationHandler(AuthenticationHandler handler) {
/*  654 */     this.proxyAuthStrategy = new AuthenticationStrategyAdaptor(handler);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final synchronized AuthenticationStrategy getProxyAuthenticationStrategy() {
/*  661 */     if (this.proxyAuthStrategy == null) {
/*  662 */       this.proxyAuthStrategy = createProxyAuthenticationStrategy();
/*      */     }
/*  664 */     return this.proxyAuthStrategy;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void setProxyAuthenticationStrategy(AuthenticationStrategy strategy) {
/*  671 */     this.proxyAuthStrategy = strategy;
/*      */   }
/*      */   
/*      */   public final synchronized CookieStore getCookieStore() {
/*  675 */     if (this.cookieStore == null) {
/*  676 */       this.cookieStore = createCookieStore();
/*      */     }
/*  678 */     return this.cookieStore;
/*      */   }
/*      */   
/*      */   public synchronized void setCookieStore(CookieStore cookieStore) {
/*  682 */     this.cookieStore = cookieStore;
/*      */   }
/*      */   
/*      */   public final synchronized CredentialsProvider getCredentialsProvider() {
/*  686 */     if (this.credsProvider == null) {
/*  687 */       this.credsProvider = createCredentialsProvider();
/*      */     }
/*  689 */     return this.credsProvider;
/*      */   }
/*      */   
/*      */   public synchronized void setCredentialsProvider(CredentialsProvider credsProvider) {
/*  693 */     this.credsProvider = credsProvider;
/*      */   }
/*      */   
/*      */   public final synchronized HttpRoutePlanner getRoutePlanner() {
/*  697 */     if (this.routePlanner == null) {
/*  698 */       this.routePlanner = createHttpRoutePlanner();
/*      */     }
/*  700 */     return this.routePlanner;
/*      */   }
/*      */   
/*      */   public synchronized void setRoutePlanner(HttpRoutePlanner routePlanner) {
/*  704 */     this.routePlanner = routePlanner;
/*      */   }
/*      */   
/*      */   public final synchronized UserTokenHandler getUserTokenHandler() {
/*  708 */     if (this.userTokenHandler == null) {
/*  709 */       this.userTokenHandler = createUserTokenHandler();
/*      */     }
/*  711 */     return this.userTokenHandler;
/*      */   }
/*      */   
/*      */   public synchronized void setUserTokenHandler(UserTokenHandler handler) {
/*  715 */     this.userTokenHandler = handler;
/*      */   }
/*      */   
/*      */   protected final synchronized BasicHttpProcessor getHttpProcessor() {
/*  719 */     if (this.mutableProcessor == null) {
/*  720 */       this.mutableProcessor = createHttpProcessor();
/*      */     }
/*  722 */     return this.mutableProcessor;
/*      */   }
/*      */   
/*      */   private synchronized HttpProcessor getProtocolProcessor() {
/*  726 */     if (this.protocolProcessor == null) {
/*      */       
/*  728 */       BasicHttpProcessor proc = getHttpProcessor();
/*      */       
/*  730 */       int reqc = proc.getRequestInterceptorCount();
/*  731 */       HttpRequestInterceptor[] reqinterceptors = new HttpRequestInterceptor[reqc];
/*  732 */       for (int i = 0; i < reqc; i++) {
/*  733 */         reqinterceptors[i] = proc.getRequestInterceptor(i);
/*      */       }
/*  735 */       int resc = proc.getResponseInterceptorCount();
/*  736 */       HttpResponseInterceptor[] resinterceptors = new HttpResponseInterceptor[resc];
/*  737 */       for (int j = 0; j < resc; j++) {
/*  738 */         resinterceptors[j] = proc.getResponseInterceptor(j);
/*      */       }
/*  740 */       this.protocolProcessor = new ImmutableHttpProcessor(reqinterceptors, resinterceptors);
/*      */     } 
/*  742 */     return (HttpProcessor)this.protocolProcessor;
/*      */   }
/*      */   
/*      */   public synchronized int getResponseInterceptorCount() {
/*  746 */     return getHttpProcessor().getResponseInterceptorCount();
/*      */   }
/*      */   
/*      */   public synchronized HttpResponseInterceptor getResponseInterceptor(int index) {
/*  750 */     return getHttpProcessor().getResponseInterceptor(index);
/*      */   }
/*      */   
/*      */   public synchronized HttpRequestInterceptor getRequestInterceptor(int index) {
/*  754 */     return getHttpProcessor().getRequestInterceptor(index);
/*      */   }
/*      */   
/*      */   public synchronized int getRequestInterceptorCount() {
/*  758 */     return getHttpProcessor().getRequestInterceptorCount();
/*      */   }
/*      */   
/*      */   public synchronized void addResponseInterceptor(HttpResponseInterceptor itcp) {
/*  762 */     getHttpProcessor().addInterceptor(itcp);
/*  763 */     this.protocolProcessor = null;
/*      */   }
/*      */   
/*      */   public synchronized void addResponseInterceptor(HttpResponseInterceptor itcp, int index) {
/*  767 */     getHttpProcessor().addInterceptor(itcp, index);
/*  768 */     this.protocolProcessor = null;
/*      */   }
/*      */   
/*      */   public synchronized void clearResponseInterceptors() {
/*  772 */     getHttpProcessor().clearResponseInterceptors();
/*  773 */     this.protocolProcessor = null;
/*      */   }
/*      */   
/*      */   public synchronized void removeResponseInterceptorByClass(Class<? extends HttpResponseInterceptor> clazz) {
/*  777 */     getHttpProcessor().removeResponseInterceptorByClass(clazz);
/*  778 */     this.protocolProcessor = null;
/*      */   }
/*      */   
/*      */   public synchronized void addRequestInterceptor(HttpRequestInterceptor itcp) {
/*  782 */     getHttpProcessor().addInterceptor(itcp);
/*  783 */     this.protocolProcessor = null;
/*      */   }
/*      */   
/*      */   public synchronized void addRequestInterceptor(HttpRequestInterceptor itcp, int index) {
/*  787 */     getHttpProcessor().addInterceptor(itcp, index);
/*  788 */     this.protocolProcessor = null;
/*      */   }
/*      */   
/*      */   public synchronized void clearRequestInterceptors() {
/*  792 */     getHttpProcessor().clearRequestInterceptors();
/*  793 */     this.protocolProcessor = null;
/*      */   }
/*      */   
/*      */   public synchronized void removeRequestInterceptorByClass(Class<? extends HttpRequestInterceptor> clazz) {
/*  797 */     getHttpProcessor().removeRequestInterceptorByClass(clazz);
/*  798 */     this.protocolProcessor = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final CloseableHttpResponse doExecute(HttpHost target, HttpRequest request, HttpContext context) throws IOException, ClientProtocolException {
/*      */     DefaultedHttpContext defaultedHttpContext;
/*  806 */     Args.notNull(request, "HTTP request");
/*      */ 
/*      */ 
/*      */     
/*  810 */     HttpContext execContext = null;
/*  811 */     RequestDirector director = null;
/*  812 */     HttpRoutePlanner routePlanner = null;
/*  813 */     ConnectionBackoffStrategy connectionBackoffStrategy = null;
/*  814 */     BackoffManager backoffManager = null;
/*      */ 
/*      */ 
/*      */     
/*  818 */     synchronized (this) {
/*      */       
/*  820 */       HttpContext defaultContext = createHttpContext();
/*  821 */       if (context == null) {
/*  822 */         execContext = defaultContext;
/*      */       } else {
/*  824 */         defaultedHttpContext = new DefaultedHttpContext(context, defaultContext);
/*      */       } 
/*  826 */       HttpParams params = determineParams(request);
/*  827 */       RequestConfig config = HttpClientParamConfig.getRequestConfig(params);
/*  828 */       defaultedHttpContext.setAttribute("http.request-config", config);
/*      */ 
/*      */       
/*  831 */       director = createClientRequestDirector(getRequestExecutor(), getConnectionManager(), getConnectionReuseStrategy(), getConnectionKeepAliveStrategy(), getRoutePlanner(), getProtocolProcessor(), getHttpRequestRetryHandler(), getRedirectStrategy(), getTargetAuthenticationStrategy(), getProxyAuthenticationStrategy(), getUserTokenHandler(), params);
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
/*  844 */       routePlanner = getRoutePlanner();
/*  845 */       connectionBackoffStrategy = getConnectionBackoffStrategy();
/*  846 */       backoffManager = getBackoffManager();
/*      */     } 
/*      */     
/*      */     try {
/*  850 */       if (connectionBackoffStrategy != null && backoffManager != null) {
/*  851 */         CloseableHttpResponse out; HttpHost targetForRoute = (target != null) ? target : (HttpHost)determineParams(request).getParameter("http.default-host");
/*      */ 
/*      */         
/*  854 */         HttpRoute route = routePlanner.determineRoute(targetForRoute, request, (HttpContext)defaultedHttpContext);
/*      */ 
/*      */         
/*      */         try {
/*  858 */           out = CloseableHttpResponseProxy.newProxy(director.execute(target, request, (HttpContext)defaultedHttpContext));
/*      */         }
/*  860 */         catch (RuntimeException re) {
/*  861 */           if (connectionBackoffStrategy.shouldBackoff(re)) {
/*  862 */             backoffManager.backOff(route);
/*      */           }
/*  864 */           throw re;
/*  865 */         } catch (Exception e) {
/*  866 */           if (connectionBackoffStrategy.shouldBackoff(e)) {
/*  867 */             backoffManager.backOff(route);
/*      */           }
/*  869 */           if (e instanceof HttpException) {
/*  870 */             throw (HttpException)e;
/*      */           }
/*  872 */           if (e instanceof IOException) {
/*  873 */             throw (IOException)e;
/*      */           }
/*  875 */           throw new UndeclaredThrowableException(e);
/*      */         } 
/*  877 */         if (connectionBackoffStrategy.shouldBackoff((HttpResponse)out)) {
/*  878 */           backoffManager.backOff(route);
/*      */         } else {
/*  880 */           backoffManager.probe(route);
/*      */         } 
/*  882 */         return out;
/*      */       } 
/*  884 */       return CloseableHttpResponseProxy.newProxy(director.execute(target, request, (HttpContext)defaultedHttpContext));
/*      */     
/*      */     }
/*  887 */     catch (HttpException httpException) {
/*  888 */       throw new ClientProtocolException(httpException);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   protected RequestDirector createClientRequestDirector(HttpRequestExecutor requestExec, ClientConnectionManager conman, ConnectionReuseStrategy reustrat, ConnectionKeepAliveStrategy kastrat, HttpRoutePlanner rouplan, HttpProcessor httpProcessor, HttpRequestRetryHandler retryHandler, RedirectHandler redirectHandler, AuthenticationHandler targetAuthHandler, AuthenticationHandler proxyAuthHandler, UserTokenHandler userTokenHandler, HttpParams params) {
/*  909 */     return new DefaultRequestDirector(requestExec, conman, reustrat, kastrat, rouplan, httpProcessor, retryHandler, redirectHandler, targetAuthHandler, proxyAuthHandler, userTokenHandler, params);
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
/*      */   @Deprecated
/*      */   protected RequestDirector createClientRequestDirector(HttpRequestExecutor requestExec, ClientConnectionManager conman, ConnectionReuseStrategy reustrat, ConnectionKeepAliveStrategy kastrat, HttpRoutePlanner rouplan, HttpProcessor httpProcessor, HttpRequestRetryHandler retryHandler, RedirectStrategy redirectStrategy, AuthenticationHandler targetAuthHandler, AuthenticationHandler proxyAuthHandler, UserTokenHandler userTokenHandler, HttpParams params) {
/*  941 */     return new DefaultRequestDirector(this.log, requestExec, conman, reustrat, kastrat, rouplan, httpProcessor, retryHandler, redirectStrategy, targetAuthHandler, proxyAuthHandler, userTokenHandler, params);
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
/*      */   protected RequestDirector createClientRequestDirector(HttpRequestExecutor requestExec, ClientConnectionManager conman, ConnectionReuseStrategy reustrat, ConnectionKeepAliveStrategy kastrat, HttpRoutePlanner rouplan, HttpProcessor httpProcessor, HttpRequestRetryHandler retryHandler, RedirectStrategy redirectStrategy, AuthenticationStrategy targetAuthStrategy, AuthenticationStrategy proxyAuthStrategy, UserTokenHandler userTokenHandler, HttpParams params) {
/*  974 */     return new DefaultRequestDirector(this.log, requestExec, conman, reustrat, kastrat, rouplan, httpProcessor, retryHandler, redirectStrategy, targetAuthStrategy, proxyAuthStrategy, userTokenHandler, params);
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
/*      */   protected HttpParams determineParams(HttpRequest req) {
/* 1007 */     return (HttpParams)new ClientParamsStack(null, getParams(), req.getParams(), null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void close() {
/* 1014 */     getConnectionManager().shutdown();
/*      */   }
/*      */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\client\AbstractHttpClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */