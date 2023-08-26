/*      */ package org.apache.http.impl.client;
/*      */ 
/*      */ import java.io.Closeable;
/*      */ import java.io.IOException;
/*      */ import java.net.ProxySelector;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import javax.net.ssl.HostnameVerifier;
/*      */ import javax.net.ssl.SSLContext;
/*      */ import javax.net.ssl.SSLSocketFactory;
/*      */ import org.apache.http.ConnectionReuseStrategy;
/*      */ import org.apache.http.Header;
/*      */ import org.apache.http.HttpHost;
/*      */ import org.apache.http.HttpRequestInterceptor;
/*      */ import org.apache.http.HttpResponseInterceptor;
/*      */ import org.apache.http.annotation.NotThreadSafe;
/*      */ import org.apache.http.auth.AuthSchemeProvider;
/*      */ import org.apache.http.client.AuthenticationStrategy;
/*      */ import org.apache.http.client.BackoffManager;
/*      */ import org.apache.http.client.ConnectionBackoffStrategy;
/*      */ import org.apache.http.client.CookieStore;
/*      */ import org.apache.http.client.CredentialsProvider;
/*      */ import org.apache.http.client.HttpRequestRetryHandler;
/*      */ import org.apache.http.client.RedirectStrategy;
/*      */ import org.apache.http.client.ServiceUnavailableRetryStrategy;
/*      */ import org.apache.http.client.UserTokenHandler;
/*      */ import org.apache.http.client.config.RequestConfig;
/*      */ import org.apache.http.client.entity.InputStreamFactory;
/*      */ import org.apache.http.client.protocol.RequestAcceptEncoding;
/*      */ import org.apache.http.client.protocol.RequestAddCookies;
/*      */ import org.apache.http.client.protocol.RequestAuthCache;
/*      */ import org.apache.http.client.protocol.RequestClientConnControl;
/*      */ import org.apache.http.client.protocol.RequestDefaultHeaders;
/*      */ import org.apache.http.client.protocol.RequestExpectContinue;
/*      */ import org.apache.http.client.protocol.ResponseContentEncoding;
/*      */ import org.apache.http.client.protocol.ResponseProcessCookies;
/*      */ import org.apache.http.config.ConnectionConfig;
/*      */ import org.apache.http.config.Lookup;
/*      */ import org.apache.http.config.Registry;
/*      */ import org.apache.http.config.RegistryBuilder;
/*      */ import org.apache.http.config.SocketConfig;
/*      */ import org.apache.http.conn.ConnectionKeepAliveStrategy;
/*      */ import org.apache.http.conn.DnsResolver;
/*      */ import org.apache.http.conn.HttpClientConnectionManager;
/*      */ import org.apache.http.conn.SchemePortResolver;
/*      */ import org.apache.http.conn.routing.HttpRoutePlanner;
/*      */ import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
/*      */ import org.apache.http.conn.socket.PlainConnectionSocketFactory;
/*      */ import org.apache.http.conn.ssl.DefaultHostnameVerifier;
/*      */ import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
/*      */ import org.apache.http.conn.ssl.X509HostnameVerifier;
/*      */ import org.apache.http.conn.util.PublicSuffixMatcher;
/*      */ import org.apache.http.conn.util.PublicSuffixMatcherLoader;
/*      */ import org.apache.http.cookie.CookieSpecProvider;
/*      */ import org.apache.http.impl.NoConnectionReuseStrategy;
/*      */ import org.apache.http.impl.auth.BasicSchemeFactory;
/*      */ import org.apache.http.impl.auth.DigestSchemeFactory;
/*      */ import org.apache.http.impl.auth.KerberosSchemeFactory;
/*      */ import org.apache.http.impl.auth.NTLMSchemeFactory;
/*      */ import org.apache.http.impl.auth.SPNegoSchemeFactory;
/*      */ import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
/*      */ import org.apache.http.impl.conn.DefaultRoutePlanner;
/*      */ import org.apache.http.impl.conn.DefaultSchemePortResolver;
/*      */ import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
/*      */ import org.apache.http.impl.conn.SystemDefaultRoutePlanner;
/*      */ import org.apache.http.impl.execchain.BackoffStrategyExec;
/*      */ import org.apache.http.impl.execchain.ClientExecChain;
/*      */ import org.apache.http.impl.execchain.MainClientExec;
/*      */ import org.apache.http.impl.execchain.ProtocolExec;
/*      */ import org.apache.http.impl.execchain.RedirectExec;
/*      */ import org.apache.http.impl.execchain.RetryExec;
/*      */ import org.apache.http.impl.execchain.ServiceUnavailableRetryExec;
/*      */ import org.apache.http.protocol.HttpProcessor;
/*      */ import org.apache.http.protocol.HttpProcessorBuilder;
/*      */ import org.apache.http.protocol.HttpRequestExecutor;
/*      */ import org.apache.http.protocol.ImmutableHttpProcessor;
/*      */ import org.apache.http.protocol.RequestContent;
/*      */ import org.apache.http.protocol.RequestTargetHost;
/*      */ import org.apache.http.protocol.RequestUserAgent;
/*      */ import org.apache.http.ssl.SSLContexts;
/*      */ import org.apache.http.util.TextUtils;
/*      */ import org.apache.http.util.VersionInfo;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @NotThreadSafe
/*      */ public class HttpClientBuilder
/*      */ {
/*      */   private HttpRequestExecutor requestExec;
/*      */   private HostnameVerifier hostnameVerifier;
/*      */   private LayeredConnectionSocketFactory sslSocketFactory;
/*      */   private SSLContext sslContext;
/*      */   private HttpClientConnectionManager connManager;
/*      */   private boolean connManagerShared;
/*      */   private SchemePortResolver schemePortResolver;
/*      */   private ConnectionReuseStrategy reuseStrategy;
/*      */   private ConnectionKeepAliveStrategy keepAliveStrategy;
/*      */   private AuthenticationStrategy targetAuthStrategy;
/*      */   private AuthenticationStrategy proxyAuthStrategy;
/*      */   private UserTokenHandler userTokenHandler;
/*      */   private HttpProcessor httpprocessor;
/*      */   private DnsResolver dnsResolver;
/*      */   private LinkedList<HttpRequestInterceptor> requestFirst;
/*      */   private LinkedList<HttpRequestInterceptor> requestLast;
/*      */   private LinkedList<HttpResponseInterceptor> responseFirst;
/*      */   private LinkedList<HttpResponseInterceptor> responseLast;
/*      */   private HttpRequestRetryHandler retryHandler;
/*      */   private HttpRoutePlanner routePlanner;
/*      */   private RedirectStrategy redirectStrategy;
/*      */   private ConnectionBackoffStrategy connectionBackoffStrategy;
/*      */   private BackoffManager backoffManager;
/*      */   private ServiceUnavailableRetryStrategy serviceUnavailStrategy;
/*      */   private Lookup<AuthSchemeProvider> authSchemeRegistry;
/*      */   private Lookup<CookieSpecProvider> cookieSpecRegistry;
/*      */   private Map<String, InputStreamFactory> contentDecoderMap;
/*      */   private CookieStore cookieStore;
/*      */   private CredentialsProvider credentialsProvider;
/*      */   private String userAgent;
/*      */   private HttpHost proxy;
/*      */   private Collection<? extends Header> defaultHeaders;
/*      */   private SocketConfig defaultSocketConfig;
/*      */   private ConnectionConfig defaultConnectionConfig;
/*      */   private RequestConfig defaultRequestConfig;
/*      */   private boolean evictExpiredConnections;
/*      */   private boolean evictIdleConnections;
/*      */   private long maxIdleTime;
/*      */   private TimeUnit maxIdleTimeUnit;
/*      */   private boolean systemProperties;
/*      */   private boolean redirectHandlingDisabled;
/*      */   private boolean automaticRetriesDisabled;
/*      */   private boolean contentCompressionDisabled;
/*      */   private boolean cookieManagementDisabled;
/*      */   private boolean authCachingDisabled;
/*      */   private boolean connectionStateDisabled;
/*  209 */   private int maxConnTotal = 0;
/*  210 */   private int maxConnPerRoute = 0;
/*      */   
/*  212 */   private long connTimeToLive = -1L;
/*  213 */   private TimeUnit connTimeToLiveTimeUnit = TimeUnit.MILLISECONDS;
/*      */   
/*      */   private List<Closeable> closeables;
/*      */   
/*      */   private PublicSuffixMatcher publicSuffixMatcher;
/*      */   
/*      */   public static HttpClientBuilder create() {
/*  220 */     return new HttpClientBuilder();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setRequestExecutor(HttpRequestExecutor requestExec) {
/*  231 */     this.requestExec = requestExec;
/*  232 */     return this;
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
/*      */   @Deprecated
/*      */   public final HttpClientBuilder setHostnameVerifier(X509HostnameVerifier hostnameVerifier) {
/*  247 */     this.hostnameVerifier = (HostnameVerifier)hostnameVerifier;
/*  248 */     return this;
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
/*      */   public final HttpClientBuilder setSSLHostnameVerifier(HostnameVerifier hostnameVerifier) {
/*  262 */     this.hostnameVerifier = hostnameVerifier;
/*  263 */     return this;
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
/*      */   public final HttpClientBuilder setPublicSuffixMatcher(PublicSuffixMatcher publicSuffixMatcher) {
/*  276 */     this.publicSuffixMatcher = publicSuffixMatcher;
/*  277 */     return this;
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
/*      */   @Deprecated
/*      */   public final HttpClientBuilder setSslcontext(SSLContext sslcontext) {
/*  292 */     return setSSLContext(sslcontext);
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
/*      */   public final HttpClientBuilder setSSLContext(SSLContext sslContext) {
/*  304 */     this.sslContext = sslContext;
/*  305 */     return this;
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
/*      */   public final HttpClientBuilder setSSLSocketFactory(LayeredConnectionSocketFactory sslSocketFactory) {
/*  317 */     this.sslSocketFactory = sslSocketFactory;
/*  318 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setMaxConnTotal(int maxConnTotal) {
/*  329 */     this.maxConnTotal = maxConnTotal;
/*  330 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setMaxConnPerRoute(int maxConnPerRoute) {
/*  341 */     this.maxConnPerRoute = maxConnPerRoute;
/*  342 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setDefaultSocketConfig(SocketConfig config) {
/*  353 */     this.defaultSocketConfig = config;
/*  354 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setDefaultConnectionConfig(ConnectionConfig config) {
/*  365 */     this.defaultConnectionConfig = config;
/*  366 */     return this;
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
/*      */   public final HttpClientBuilder setConnectionTimeToLive(long connTimeToLive, TimeUnit connTimeToLiveTimeUnit) {
/*  379 */     this.connTimeToLive = connTimeToLive;
/*  380 */     this.connTimeToLiveTimeUnit = connTimeToLiveTimeUnit;
/*  381 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setConnectionManager(HttpClientConnectionManager connManager) {
/*  389 */     this.connManager = connManager;
/*  390 */     return this;
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
/*      */   public final HttpClientBuilder setConnectionManagerShared(boolean shared) {
/*  409 */     this.connManagerShared = shared;
/*  410 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setConnectionReuseStrategy(ConnectionReuseStrategy reuseStrategy) {
/*  418 */     this.reuseStrategy = reuseStrategy;
/*  419 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setKeepAliveStrategy(ConnectionKeepAliveStrategy keepAliveStrategy) {
/*  427 */     this.keepAliveStrategy = keepAliveStrategy;
/*  428 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setTargetAuthenticationStrategy(AuthenticationStrategy targetAuthStrategy) {
/*  437 */     this.targetAuthStrategy = targetAuthStrategy;
/*  438 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setProxyAuthenticationStrategy(AuthenticationStrategy proxyAuthStrategy) {
/*  447 */     this.proxyAuthStrategy = proxyAuthStrategy;
/*  448 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setUserTokenHandler(UserTokenHandler userTokenHandler) {
/*  459 */     this.userTokenHandler = userTokenHandler;
/*  460 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder disableConnectionState() {
/*  467 */     this.connectionStateDisabled = true;
/*  468 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setSchemePortResolver(SchemePortResolver schemePortResolver) {
/*  476 */     this.schemePortResolver = schemePortResolver;
/*  477 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setUserAgent(String userAgent) {
/*  488 */     this.userAgent = userAgent;
/*  489 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setDefaultHeaders(Collection<? extends Header> defaultHeaders) {
/*  500 */     this.defaultHeaders = defaultHeaders;
/*  501 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder addInterceptorFirst(HttpResponseInterceptor itcp) {
/*  512 */     if (itcp == null) {
/*  513 */       return this;
/*      */     }
/*  515 */     if (this.responseFirst == null) {
/*  516 */       this.responseFirst = new LinkedList<HttpResponseInterceptor>();
/*      */     }
/*  518 */     this.responseFirst.addFirst(itcp);
/*  519 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder addInterceptorLast(HttpResponseInterceptor itcp) {
/*  530 */     if (itcp == null) {
/*  531 */       return this;
/*      */     }
/*  533 */     if (this.responseLast == null) {
/*  534 */       this.responseLast = new LinkedList<HttpResponseInterceptor>();
/*      */     }
/*  536 */     this.responseLast.addLast(itcp);
/*  537 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder addInterceptorFirst(HttpRequestInterceptor itcp) {
/*  547 */     if (itcp == null) {
/*  548 */       return this;
/*      */     }
/*  550 */     if (this.requestFirst == null) {
/*  551 */       this.requestFirst = new LinkedList<HttpRequestInterceptor>();
/*      */     }
/*  553 */     this.requestFirst.addFirst(itcp);
/*  554 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder addInterceptorLast(HttpRequestInterceptor itcp) {
/*  564 */     if (itcp == null) {
/*  565 */       return this;
/*      */     }
/*  567 */     if (this.requestLast == null) {
/*  568 */       this.requestLast = new LinkedList<HttpRequestInterceptor>();
/*      */     }
/*  570 */     this.requestLast.addLast(itcp);
/*  571 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder disableCookieManagement() {
/*  581 */     this.cookieManagementDisabled = true;
/*  582 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder disableContentCompression() {
/*  592 */     this.contentCompressionDisabled = true;
/*  593 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder disableAuthCaching() {
/*  603 */     this.authCachingDisabled = true;
/*  604 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setHttpProcessor(HttpProcessor httpprocessor) {
/*  611 */     this.httpprocessor = httpprocessor;
/*  612 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setDnsResolver(DnsResolver dnsResolver) {
/*  621 */     this.dnsResolver = dnsResolver;
/*  622 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setRetryHandler(HttpRequestRetryHandler retryHandler) {
/*  632 */     this.retryHandler = retryHandler;
/*  633 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder disableAutomaticRetries() {
/*  640 */     this.automaticRetriesDisabled = true;
/*  641 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setProxy(HttpHost proxy) {
/*  651 */     this.proxy = proxy;
/*  652 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setRoutePlanner(HttpRoutePlanner routePlanner) {
/*  659 */     this.routePlanner = routePlanner;
/*  660 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setRedirectStrategy(RedirectStrategy redirectStrategy) {
/*  671 */     this.redirectStrategy = redirectStrategy;
/*  672 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder disableRedirectHandling() {
/*  679 */     this.redirectHandlingDisabled = true;
/*  680 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setConnectionBackoffStrategy(ConnectionBackoffStrategy connectionBackoffStrategy) {
/*  688 */     this.connectionBackoffStrategy = connectionBackoffStrategy;
/*  689 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setBackoffManager(BackoffManager backoffManager) {
/*  696 */     this.backoffManager = backoffManager;
/*  697 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setServiceUnavailableRetryStrategy(ServiceUnavailableRetryStrategy serviceUnavailStrategy) {
/*  705 */     this.serviceUnavailStrategy = serviceUnavailStrategy;
/*  706 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setDefaultCookieStore(CookieStore cookieStore) {
/*  714 */     this.cookieStore = cookieStore;
/*  715 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setDefaultCredentialsProvider(CredentialsProvider credentialsProvider) {
/*  725 */     this.credentialsProvider = credentialsProvider;
/*  726 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setDefaultAuthSchemeRegistry(Lookup<AuthSchemeProvider> authSchemeRegistry) {
/*  736 */     this.authSchemeRegistry = authSchemeRegistry;
/*  737 */     return this;
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
/*      */   public final HttpClientBuilder setDefaultCookieSpecRegistry(Lookup<CookieSpecProvider> cookieSpecRegistry) {
/*  750 */     this.cookieSpecRegistry = cookieSpecRegistry;
/*  751 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setContentDecoderRegistry(Map<String, InputStreamFactory> contentDecoderMap) {
/*  761 */     this.contentDecoderMap = contentDecoderMap;
/*  762 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setDefaultRequestConfig(RequestConfig config) {
/*  771 */     this.defaultRequestConfig = config;
/*  772 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder useSystemProperties() {
/*  780 */     this.systemProperties = true;
/*  781 */     return this;
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
/*      */   public final HttpClientBuilder evictExpiredConnections() {
/*  803 */     this.evictExpiredConnections = true;
/*  804 */     return this;
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
/*      */   @Deprecated
/*      */   public final HttpClientBuilder evictIdleConnections(Long maxIdleTime, TimeUnit maxIdleTimeUnit) {
/*  834 */     return evictIdleConnections(maxIdleTime.longValue(), maxIdleTimeUnit);
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
/*      */   public final HttpClientBuilder evictIdleConnections(long maxIdleTime, TimeUnit maxIdleTimeUnit) {
/*  861 */     this.evictIdleConnections = true;
/*  862 */     this.maxIdleTime = maxIdleTime;
/*  863 */     this.maxIdleTimeUnit = maxIdleTimeUnit;
/*  864 */     return this;
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
/*      */   protected ClientExecChain createMainExec(HttpRequestExecutor requestExec, HttpClientConnectionManager connManager, ConnectionReuseStrategy reuseStrategy, ConnectionKeepAliveStrategy keepAliveStrategy, HttpProcessor proxyHttpProcessor, AuthenticationStrategy targetAuthStrategy, AuthenticationStrategy proxyAuthStrategy, UserTokenHandler userTokenHandler) {
/*  888 */     return (ClientExecChain)new MainClientExec(requestExec, connManager, reuseStrategy, keepAliveStrategy, proxyHttpProcessor, targetAuthStrategy, proxyAuthStrategy, userTokenHandler);
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
/*      */   protected ClientExecChain decorateMainExec(ClientExecChain mainExec) {
/*  903 */     return mainExec;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ClientExecChain decorateProtocolExec(ClientExecChain protocolExec) {
/*  910 */     return protocolExec;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void addCloseable(Closeable closeable) {
/*  917 */     if (closeable == null) {
/*      */       return;
/*      */     }
/*  920 */     if (this.closeables == null) {
/*  921 */       this.closeables = new ArrayList<Closeable>();
/*      */     }
/*  923 */     this.closeables.add(closeable);
/*      */   }
/*      */   
/*      */   private static String[] split(String s) {
/*  927 */     if (TextUtils.isBlank(s)) {
/*  928 */       return null;
/*      */     }
/*  930 */     return s.split(" *, *"); } public CloseableHttpClient build() { PoolingHttpClientConnectionManager poolingHttpClientConnectionManager; DefaultClientConnectionReuseStrategy defaultClientConnectionReuseStrategy; RetryExec retryExec;
/*      */     RedirectExec redirectExec;
/*      */     ServiceUnavailableRetryExec serviceUnavailableRetryExec;
/*      */     BackoffStrategyExec backoffStrategyExec;
/*      */     DefaultRoutePlanner defaultRoutePlanner;
/*      */     Registry registry;
/*  936 */     PublicSuffixMatcher publicSuffixMatcherCopy = this.publicSuffixMatcher;
/*  937 */     if (publicSuffixMatcherCopy == null) {
/*  938 */       publicSuffixMatcherCopy = PublicSuffixMatcherLoader.getDefault();
/*      */     }
/*      */     
/*  941 */     HttpRequestExecutor requestExecCopy = this.requestExec;
/*  942 */     if (requestExecCopy == null) {
/*  943 */       requestExecCopy = new HttpRequestExecutor();
/*      */     }
/*  945 */     HttpClientConnectionManager connManagerCopy = this.connManager;
/*  946 */     if (connManagerCopy == null) {
/*  947 */       SSLConnectionSocketFactory sSLConnectionSocketFactory; LayeredConnectionSocketFactory sslSocketFactoryCopy = this.sslSocketFactory;
/*  948 */       if (sslSocketFactoryCopy == null) {
/*  949 */         DefaultHostnameVerifier defaultHostnameVerifier; String[] supportedProtocols = this.systemProperties ? split(System.getProperty("https.protocols")) : null;
/*      */         
/*  951 */         String[] supportedCipherSuites = this.systemProperties ? split(System.getProperty("https.cipherSuites")) : null;
/*      */         
/*  953 */         HostnameVerifier hostnameVerifierCopy = this.hostnameVerifier;
/*  954 */         if (hostnameVerifierCopy == null) {
/*  955 */           defaultHostnameVerifier = new DefaultHostnameVerifier(publicSuffixMatcherCopy);
/*      */         }
/*  957 */         if (this.sslContext != null) {
/*  958 */           sSLConnectionSocketFactory = new SSLConnectionSocketFactory(this.sslContext, supportedProtocols, supportedCipherSuites, (HostnameVerifier)defaultHostnameVerifier);
/*      */         
/*      */         }
/*  961 */         else if (this.systemProperties) {
/*  962 */           sSLConnectionSocketFactory = new SSLConnectionSocketFactory((SSLSocketFactory)SSLSocketFactory.getDefault(), supportedProtocols, supportedCipherSuites, (HostnameVerifier)defaultHostnameVerifier);
/*      */         }
/*      */         else {
/*      */           
/*  966 */           sSLConnectionSocketFactory = new SSLConnectionSocketFactory(SSLContexts.createDefault(), (HostnameVerifier)defaultHostnameVerifier);
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  973 */       PoolingHttpClientConnectionManager poolingmgr = new PoolingHttpClientConnectionManager(RegistryBuilder.create().register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", sSLConnectionSocketFactory).build(), null, null, this.dnsResolver, this.connTimeToLive, (this.connTimeToLiveTimeUnit != null) ? this.connTimeToLiveTimeUnit : TimeUnit.MILLISECONDS);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  983 */       if (this.defaultSocketConfig != null) {
/*  984 */         poolingmgr.setDefaultSocketConfig(this.defaultSocketConfig);
/*      */       }
/*  986 */       if (this.defaultConnectionConfig != null) {
/*  987 */         poolingmgr.setDefaultConnectionConfig(this.defaultConnectionConfig);
/*      */       }
/*  989 */       if (this.systemProperties) {
/*  990 */         String s = System.getProperty("http.keepAlive", "true");
/*  991 */         if ("true".equalsIgnoreCase(s)) {
/*  992 */           s = System.getProperty("http.maxConnections", "5");
/*  993 */           int max = Integer.parseInt(s);
/*  994 */           poolingmgr.setDefaultMaxPerRoute(max);
/*  995 */           poolingmgr.setMaxTotal(2 * max);
/*      */         } 
/*      */       } 
/*  998 */       if (this.maxConnTotal > 0) {
/*  999 */         poolingmgr.setMaxTotal(this.maxConnTotal);
/*      */       }
/* 1001 */       if (this.maxConnPerRoute > 0) {
/* 1002 */         poolingmgr.setDefaultMaxPerRoute(this.maxConnPerRoute);
/*      */       }
/* 1004 */       poolingHttpClientConnectionManager = poolingmgr;
/*      */     } 
/* 1006 */     ConnectionReuseStrategy reuseStrategyCopy = this.reuseStrategy;
/* 1007 */     if (reuseStrategyCopy == null) {
/* 1008 */       if (this.systemProperties) {
/* 1009 */         String s = System.getProperty("http.keepAlive", "true");
/* 1010 */         if ("true".equalsIgnoreCase(s)) {
/* 1011 */           defaultClientConnectionReuseStrategy = DefaultClientConnectionReuseStrategy.INSTANCE;
/*      */         } else {
/* 1013 */           NoConnectionReuseStrategy noConnectionReuseStrategy = NoConnectionReuseStrategy.INSTANCE;
/*      */         } 
/*      */       } else {
/* 1016 */         defaultClientConnectionReuseStrategy = DefaultClientConnectionReuseStrategy.INSTANCE;
/*      */       } 
/*      */     }
/* 1019 */     ConnectionKeepAliveStrategy keepAliveStrategyCopy = this.keepAliveStrategy;
/* 1020 */     if (keepAliveStrategyCopy == null) {
/* 1021 */       keepAliveStrategyCopy = DefaultConnectionKeepAliveStrategy.INSTANCE;
/*      */     }
/* 1023 */     AuthenticationStrategy targetAuthStrategyCopy = this.targetAuthStrategy;
/* 1024 */     if (targetAuthStrategyCopy == null) {
/* 1025 */       targetAuthStrategyCopy = TargetAuthenticationStrategy.INSTANCE;
/*      */     }
/* 1027 */     AuthenticationStrategy proxyAuthStrategyCopy = this.proxyAuthStrategy;
/* 1028 */     if (proxyAuthStrategyCopy == null) {
/* 1029 */       proxyAuthStrategyCopy = ProxyAuthenticationStrategy.INSTANCE;
/*      */     }
/* 1031 */     UserTokenHandler userTokenHandlerCopy = this.userTokenHandler;
/* 1032 */     if (userTokenHandlerCopy == null) {
/* 1033 */       if (!this.connectionStateDisabled) {
/* 1034 */         userTokenHandlerCopy = DefaultUserTokenHandler.INSTANCE;
/*      */       } else {
/* 1036 */         userTokenHandlerCopy = NoopUserTokenHandler.INSTANCE;
/*      */       } 
/*      */     }
/*      */     
/* 1040 */     String userAgentCopy = this.userAgent;
/* 1041 */     if (userAgentCopy == null) {
/* 1042 */       if (this.systemProperties) {
/* 1043 */         userAgentCopy = System.getProperty("http.agent");
/*      */       }
/* 1045 */       if (userAgentCopy == null) {
/* 1046 */         userAgentCopy = VersionInfo.getUserAgent("Apache-HttpClient", "org.apache.http.client", getClass());
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1051 */     ClientExecChain execChain = createMainExec(requestExecCopy, (HttpClientConnectionManager)poolingHttpClientConnectionManager, (ConnectionReuseStrategy)defaultClientConnectionReuseStrategy, keepAliveStrategyCopy, (HttpProcessor)new ImmutableHttpProcessor(new HttpRequestInterceptor[] { (HttpRequestInterceptor)new RequestTargetHost(), (HttpRequestInterceptor)new RequestUserAgent(userAgentCopy) }, ), targetAuthStrategyCopy, proxyAuthStrategyCopy, userTokenHandlerCopy);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1061 */     execChain = decorateMainExec(execChain);
/*      */     
/* 1063 */     HttpProcessor httpprocessorCopy = this.httpprocessor;
/* 1064 */     if (httpprocessorCopy == null) {
/*      */       
/* 1066 */       HttpProcessorBuilder b = HttpProcessorBuilder.create();
/* 1067 */       if (this.requestFirst != null) {
/* 1068 */         for (HttpRequestInterceptor i : this.requestFirst) {
/* 1069 */           b.addFirst(i);
/*      */         }
/*      */       }
/* 1072 */       if (this.responseFirst != null) {
/* 1073 */         for (HttpResponseInterceptor i : this.responseFirst) {
/* 1074 */           b.addFirst(i);
/*      */         }
/*      */       }
/* 1077 */       b.addAll(new HttpRequestInterceptor[] { (HttpRequestInterceptor)new RequestDefaultHeaders(this.defaultHeaders), (HttpRequestInterceptor)new RequestContent(), (HttpRequestInterceptor)new RequestTargetHost(), (HttpRequestInterceptor)new RequestClientConnControl(), (HttpRequestInterceptor)new RequestUserAgent(userAgentCopy), (HttpRequestInterceptor)new RequestExpectContinue() });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1084 */       if (!this.cookieManagementDisabled) {
/* 1085 */         b.add((HttpRequestInterceptor)new RequestAddCookies());
/*      */       }
/* 1087 */       if (!this.contentCompressionDisabled) {
/* 1088 */         if (this.contentDecoderMap != null) {
/* 1089 */           List<String> encodings = new ArrayList<String>(this.contentDecoderMap.keySet());
/* 1090 */           Collections.sort(encodings);
/* 1091 */           b.add((HttpRequestInterceptor)new RequestAcceptEncoding(encodings));
/*      */         } else {
/* 1093 */           b.add((HttpRequestInterceptor)new RequestAcceptEncoding());
/*      */         } 
/*      */       }
/* 1096 */       if (!this.authCachingDisabled) {
/* 1097 */         b.add((HttpRequestInterceptor)new RequestAuthCache());
/*      */       }
/* 1099 */       if (!this.cookieManagementDisabled) {
/* 1100 */         b.add((HttpResponseInterceptor)new ResponseProcessCookies());
/*      */       }
/* 1102 */       if (!this.contentCompressionDisabled) {
/* 1103 */         if (this.contentDecoderMap != null) {
/* 1104 */           RegistryBuilder<InputStreamFactory> b2 = RegistryBuilder.create();
/* 1105 */           for (Map.Entry<String, InputStreamFactory> entry : this.contentDecoderMap.entrySet()) {
/* 1106 */             b2.register(entry.getKey(), entry.getValue());
/*      */           }
/* 1108 */           b.add((HttpResponseInterceptor)new ResponseContentEncoding((Lookup)b2.build()));
/*      */         } else {
/* 1110 */           b.add((HttpResponseInterceptor)new ResponseContentEncoding());
/*      */         } 
/*      */       }
/* 1113 */       if (this.requestLast != null) {
/* 1114 */         for (HttpRequestInterceptor i : this.requestLast) {
/* 1115 */           b.addLast(i);
/*      */         }
/*      */       }
/* 1118 */       if (this.responseLast != null) {
/* 1119 */         for (HttpResponseInterceptor i : this.responseLast) {
/* 1120 */           b.addLast(i);
/*      */         }
/*      */       }
/* 1123 */       httpprocessorCopy = b.build();
/*      */     } 
/* 1125 */     ProtocolExec protocolExec = new ProtocolExec(execChain, httpprocessorCopy);
/*      */     
/* 1127 */     ClientExecChain clientExecChain1 = decorateProtocolExec((ClientExecChain)protocolExec);
/*      */ 
/*      */     
/* 1130 */     if (!this.automaticRetriesDisabled) {
/* 1131 */       HttpRequestRetryHandler retryHandlerCopy = this.retryHandler;
/* 1132 */       if (retryHandlerCopy == null) {
/* 1133 */         retryHandlerCopy = DefaultHttpRequestRetryHandler.INSTANCE;
/*      */       }
/* 1135 */       retryExec = new RetryExec(clientExecChain1, retryHandlerCopy);
/*      */     } 
/*      */     
/* 1138 */     HttpRoutePlanner routePlannerCopy = this.routePlanner;
/* 1139 */     if (routePlannerCopy == null) {
/* 1140 */       DefaultSchemePortResolver defaultSchemePortResolver; SchemePortResolver schemePortResolverCopy = this.schemePortResolver;
/* 1141 */       if (schemePortResolverCopy == null) {
/* 1142 */         defaultSchemePortResolver = DefaultSchemePortResolver.INSTANCE;
/*      */       }
/* 1144 */       if (this.proxy != null) {
/* 1145 */         DefaultProxyRoutePlanner defaultProxyRoutePlanner = new DefaultProxyRoutePlanner(this.proxy, (SchemePortResolver)defaultSchemePortResolver);
/* 1146 */       } else if (this.systemProperties) {
/* 1147 */         SystemDefaultRoutePlanner systemDefaultRoutePlanner = new SystemDefaultRoutePlanner((SchemePortResolver)defaultSchemePortResolver, ProxySelector.getDefault());
/*      */       } else {
/*      */         
/* 1150 */         defaultRoutePlanner = new DefaultRoutePlanner((SchemePortResolver)defaultSchemePortResolver);
/*      */       } 
/*      */     } 
/*      */     
/* 1154 */     if (!this.redirectHandlingDisabled) {
/* 1155 */       RedirectStrategy redirectStrategyCopy = this.redirectStrategy;
/* 1156 */       if (redirectStrategyCopy == null) {
/* 1157 */         redirectStrategyCopy = DefaultRedirectStrategy.INSTANCE;
/*      */       }
/* 1159 */       redirectExec = new RedirectExec((ClientExecChain)retryExec, (HttpRoutePlanner)defaultRoutePlanner, redirectStrategyCopy);
/*      */     } 
/*      */ 
/*      */     
/* 1163 */     ServiceUnavailableRetryStrategy serviceUnavailStrategyCopy = this.serviceUnavailStrategy;
/* 1164 */     if (serviceUnavailStrategyCopy != null) {
/* 1165 */       serviceUnavailableRetryExec = new ServiceUnavailableRetryExec((ClientExecChain)redirectExec, serviceUnavailStrategyCopy);
/*      */     }
/*      */     
/* 1168 */     if (this.backoffManager != null && this.connectionBackoffStrategy != null) {
/* 1169 */       backoffStrategyExec = new BackoffStrategyExec((ClientExecChain)serviceUnavailableRetryExec, this.connectionBackoffStrategy, this.backoffManager);
/*      */     }
/*      */     
/* 1172 */     Lookup<AuthSchemeProvider> authSchemeRegistryCopy = this.authSchemeRegistry;
/* 1173 */     if (authSchemeRegistryCopy == null) {
/* 1174 */       registry = RegistryBuilder.create().register("Basic", new BasicSchemeFactory()).register("Digest", new DigestSchemeFactory()).register("NTLM", new NTLMSchemeFactory()).register("Negotiate", new SPNegoSchemeFactory()).register("Kerberos", new KerberosSchemeFactory()).build();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1182 */     Lookup<CookieSpecProvider> cookieSpecRegistryCopy = this.cookieSpecRegistry;
/* 1183 */     if (cookieSpecRegistryCopy == null) {
/* 1184 */       cookieSpecRegistryCopy = CookieSpecRegistries.createDefault(publicSuffixMatcherCopy);
/*      */     }
/*      */     
/* 1187 */     CookieStore defaultCookieStore = this.cookieStore;
/* 1188 */     if (defaultCookieStore == null) {
/* 1189 */       defaultCookieStore = new BasicCookieStore();
/*      */     }
/*      */     
/* 1192 */     CredentialsProvider defaultCredentialsProvider = this.credentialsProvider;
/* 1193 */     if (defaultCredentialsProvider == null) {
/* 1194 */       if (this.systemProperties) {
/* 1195 */         defaultCredentialsProvider = new SystemDefaultCredentialsProvider();
/*      */       } else {
/* 1197 */         defaultCredentialsProvider = new BasicCredentialsProvider();
/*      */       } 
/*      */     }
/*      */     
/* 1201 */     List<Closeable> closeablesCopy = (this.closeables != null) ? new ArrayList<Closeable>(this.closeables) : null;
/* 1202 */     if (!this.connManagerShared) {
/* 1203 */       if (closeablesCopy == null) {
/* 1204 */         closeablesCopy = new ArrayList<Closeable>(1);
/*      */       }
/* 1206 */       final PoolingHttpClientConnectionManager cm = poolingHttpClientConnectionManager;
/*      */       
/* 1208 */       if (this.evictExpiredConnections || this.evictIdleConnections) {
/* 1209 */         final IdleConnectionEvictor connectionEvictor = new IdleConnectionEvictor((HttpClientConnectionManager)poolingHttpClientConnectionManager1, (this.maxIdleTime > 0L) ? this.maxIdleTime : 10L, (this.maxIdleTimeUnit != null) ? this.maxIdleTimeUnit : TimeUnit.SECONDS);
/*      */         
/* 1211 */         closeablesCopy.add(new Closeable()
/*      */             {
/*      */               public void close() throws IOException
/*      */               {
/* 1215 */                 connectionEvictor.shutdown();
/*      */               }
/*      */             });
/*      */         
/* 1219 */         connectionEvictor.start();
/*      */       } 
/* 1221 */       closeablesCopy.add(new Closeable()
/*      */           {
/*      */             public void close() throws IOException
/*      */             {
/* 1225 */               cm.shutdown();
/*      */             }
/*      */           });
/*      */     } 
/*      */ 
/*      */     
/* 1231 */     return new InternalHttpClient((ClientExecChain)backoffStrategyExec, (HttpClientConnectionManager)poolingHttpClientConnectionManager, (HttpRoutePlanner)defaultRoutePlanner, cookieSpecRegistryCopy, (Lookup<AuthSchemeProvider>)registry, defaultCookieStore, defaultCredentialsProvider, (this.defaultRequestConfig != null) ? this.defaultRequestConfig : RequestConfig.DEFAULT, closeablesCopy); }
/*      */ 
/*      */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\client\HttpClientBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */