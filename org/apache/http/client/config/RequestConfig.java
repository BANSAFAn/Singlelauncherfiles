/*     */ package org.apache.http.client.config;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import java.util.Collection;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.annotation.Immutable;
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
/*     */ public class RequestConfig
/*     */   implements Cloneable
/*     */ {
/*  44 */   public static final RequestConfig DEFAULT = (new Builder()).build();
/*     */   
/*     */   private final boolean expectContinueEnabled;
/*     */   
/*     */   private final HttpHost proxy;
/*     */   
/*     */   private final InetAddress localAddress;
/*     */   
/*     */   private final boolean staleConnectionCheckEnabled;
/*     */   private final String cookieSpec;
/*     */   private final boolean redirectsEnabled;
/*     */   private final boolean relativeRedirectsAllowed;
/*     */   private final boolean circularRedirectsAllowed;
/*     */   private final int maxRedirects;
/*     */   private final boolean authenticationEnabled;
/*     */   private final Collection<String> targetPreferredAuthSchemes;
/*     */   private final Collection<String> proxyPreferredAuthSchemes;
/*     */   private final int connectionRequestTimeout;
/*     */   private final int connectTimeout;
/*     */   private final int socketTimeout;
/*     */   private final boolean contentCompressionEnabled;
/*     */   
/*     */   protected RequestConfig() {
/*  67 */     this(false, null, null, false, null, false, false, false, 0, false, null, null, 0, 0, 0, true);
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
/*     */   RequestConfig(boolean expectContinueEnabled, HttpHost proxy, InetAddress localAddress, boolean staleConnectionCheckEnabled, String cookieSpec, boolean redirectsEnabled, boolean relativeRedirectsAllowed, boolean circularRedirectsAllowed, int maxRedirects, boolean authenticationEnabled, Collection<String> targetPreferredAuthSchemes, Collection<String> proxyPreferredAuthSchemes, int connectionRequestTimeout, int connectTimeout, int socketTimeout, boolean contentCompressionEnabled) {
/*  88 */     this.expectContinueEnabled = expectContinueEnabled;
/*  89 */     this.proxy = proxy;
/*  90 */     this.localAddress = localAddress;
/*  91 */     this.staleConnectionCheckEnabled = staleConnectionCheckEnabled;
/*  92 */     this.cookieSpec = cookieSpec;
/*  93 */     this.redirectsEnabled = redirectsEnabled;
/*  94 */     this.relativeRedirectsAllowed = relativeRedirectsAllowed;
/*  95 */     this.circularRedirectsAllowed = circularRedirectsAllowed;
/*  96 */     this.maxRedirects = maxRedirects;
/*  97 */     this.authenticationEnabled = authenticationEnabled;
/*  98 */     this.targetPreferredAuthSchemes = targetPreferredAuthSchemes;
/*  99 */     this.proxyPreferredAuthSchemes = proxyPreferredAuthSchemes;
/* 100 */     this.connectionRequestTimeout = connectionRequestTimeout;
/* 101 */     this.connectTimeout = connectTimeout;
/* 102 */     this.socketTimeout = socketTimeout;
/* 103 */     this.contentCompressionEnabled = contentCompressionEnabled;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isExpectContinueEnabled() {
/* 129 */     return this.expectContinueEnabled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpHost getProxy() {
/* 139 */     return this.proxy;
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
/*     */   public InetAddress getLocalAddress() {
/* 154 */     return this.localAddress;
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
/*     */   @Deprecated
/*     */   public boolean isStaleConnectionCheckEnabled() {
/* 171 */     return this.staleConnectionCheckEnabled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCookieSpec() {
/* 182 */     return this.cookieSpec;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRedirectsEnabled() {
/* 192 */     return this.redirectsEnabled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRelativeRedirectsAllowed() {
/* 203 */     return this.relativeRedirectsAllowed;
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
/*     */   public boolean isCircularRedirectsAllowed() {
/* 215 */     return this.circularRedirectsAllowed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxRedirects() {
/* 226 */     return this.maxRedirects;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAuthenticationEnabled() {
/* 236 */     return this.authenticationEnabled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<String> getTargetPreferredAuthSchemes() {
/* 247 */     return this.targetPreferredAuthSchemes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<String> getProxyPreferredAuthSchemes() {
/* 258 */     return this.proxyPreferredAuthSchemes;
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
/*     */   public int getConnectionRequestTimeout() {
/* 274 */     return this.connectionRequestTimeout;
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
/*     */   public int getConnectTimeout() {
/* 289 */     return this.connectTimeout;
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
/*     */   public int getSocketTimeout() {
/* 305 */     return this.socketTimeout;
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
/*     */   @Deprecated
/*     */   public boolean isDecompressionEnabled() {
/* 319 */     return this.contentCompressionEnabled;
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
/*     */   public boolean isContentCompressionEnabled() {
/* 331 */     return this.contentCompressionEnabled;
/*     */   }
/*     */ 
/*     */   
/*     */   protected RequestConfig clone() throws CloneNotSupportedException {
/* 336 */     return (RequestConfig)super.clone();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 341 */     StringBuilder builder = new StringBuilder();
/* 342 */     builder.append("[");
/* 343 */     builder.append("expectContinueEnabled=").append(this.expectContinueEnabled);
/* 344 */     builder.append(", proxy=").append(this.proxy);
/* 345 */     builder.append(", localAddress=").append(this.localAddress);
/* 346 */     builder.append(", cookieSpec=").append(this.cookieSpec);
/* 347 */     builder.append(", redirectsEnabled=").append(this.redirectsEnabled);
/* 348 */     builder.append(", relativeRedirectsAllowed=").append(this.relativeRedirectsAllowed);
/* 349 */     builder.append(", maxRedirects=").append(this.maxRedirects);
/* 350 */     builder.append(", circularRedirectsAllowed=").append(this.circularRedirectsAllowed);
/* 351 */     builder.append(", authenticationEnabled=").append(this.authenticationEnabled);
/* 352 */     builder.append(", targetPreferredAuthSchemes=").append(this.targetPreferredAuthSchemes);
/* 353 */     builder.append(", proxyPreferredAuthSchemes=").append(this.proxyPreferredAuthSchemes);
/* 354 */     builder.append(", connectionRequestTimeout=").append(this.connectionRequestTimeout);
/* 355 */     builder.append(", connectTimeout=").append(this.connectTimeout);
/* 356 */     builder.append(", socketTimeout=").append(this.socketTimeout);
/* 357 */     builder.append(", contentCompressionEnabled=").append(this.contentCompressionEnabled);
/* 358 */     builder.append("]");
/* 359 */     return builder.toString();
/*     */   }
/*     */   
/*     */   public static Builder custom() {
/* 363 */     return new Builder();
/*     */   }
/*     */ 
/*     */   
/*     */   public static Builder copy(RequestConfig config) {
/* 368 */     return (new Builder()).setExpectContinueEnabled(config.isExpectContinueEnabled()).setProxy(config.getProxy()).setLocalAddress(config.getLocalAddress()).setStaleConnectionCheckEnabled(config.isStaleConnectionCheckEnabled()).setCookieSpec(config.getCookieSpec()).setRedirectsEnabled(config.isRedirectsEnabled()).setRelativeRedirectsAllowed(config.isRelativeRedirectsAllowed()).setCircularRedirectsAllowed(config.isCircularRedirectsAllowed()).setMaxRedirects(config.getMaxRedirects()).setAuthenticationEnabled(config.isAuthenticationEnabled()).setTargetPreferredAuthSchemes(config.getTargetPreferredAuthSchemes()).setProxyPreferredAuthSchemes(config.getProxyPreferredAuthSchemes()).setConnectionRequestTimeout(config.getConnectionRequestTimeout()).setConnectTimeout(config.getConnectTimeout()).setSocketTimeout(config.getSocketTimeout()).setDecompressionEnabled(config.isDecompressionEnabled()).setContentCompressionEnabled(config.isContentCompressionEnabled());
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
/*     */   public static class Builder
/*     */   {
/*     */     private boolean staleConnectionCheckEnabled = false;
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
/*     */     private boolean redirectsEnabled = true;
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
/* 411 */     private int maxRedirects = 50;
/*     */     private boolean relativeRedirectsAllowed = true;
/*     */     private boolean authenticationEnabled = true;
/* 414 */     private int connectionRequestTimeout = -1;
/* 415 */     private int connectTimeout = -1;
/* 416 */     private int socketTimeout = -1; private boolean contentCompressionEnabled = true; private boolean expectContinueEnabled;
/*     */     private HttpHost proxy;
/*     */     private InetAddress localAddress;
/*     */     
/*     */     public Builder setExpectContinueEnabled(boolean expectContinueEnabled) {
/* 421 */       this.expectContinueEnabled = expectContinueEnabled;
/* 422 */       return this;
/*     */     }
/*     */     private String cookieSpec; private boolean circularRedirectsAllowed; private Collection<String> targetPreferredAuthSchemes; private Collection<String> proxyPreferredAuthSchemes;
/*     */     public Builder setProxy(HttpHost proxy) {
/* 426 */       this.proxy = proxy;
/* 427 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setLocalAddress(InetAddress localAddress) {
/* 431 */       this.localAddress = localAddress;
/* 432 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public Builder setStaleConnectionCheckEnabled(boolean staleConnectionCheckEnabled) {
/* 441 */       this.staleConnectionCheckEnabled = staleConnectionCheckEnabled;
/* 442 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setCookieSpec(String cookieSpec) {
/* 446 */       this.cookieSpec = cookieSpec;
/* 447 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setRedirectsEnabled(boolean redirectsEnabled) {
/* 451 */       this.redirectsEnabled = redirectsEnabled;
/* 452 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setRelativeRedirectsAllowed(boolean relativeRedirectsAllowed) {
/* 456 */       this.relativeRedirectsAllowed = relativeRedirectsAllowed;
/* 457 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setCircularRedirectsAllowed(boolean circularRedirectsAllowed) {
/* 461 */       this.circularRedirectsAllowed = circularRedirectsAllowed;
/* 462 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setMaxRedirects(int maxRedirects) {
/* 466 */       this.maxRedirects = maxRedirects;
/* 467 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setAuthenticationEnabled(boolean authenticationEnabled) {
/* 471 */       this.authenticationEnabled = authenticationEnabled;
/* 472 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setTargetPreferredAuthSchemes(Collection<String> targetPreferredAuthSchemes) {
/* 476 */       this.targetPreferredAuthSchemes = targetPreferredAuthSchemes;
/* 477 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setProxyPreferredAuthSchemes(Collection<String> proxyPreferredAuthSchemes) {
/* 481 */       this.proxyPreferredAuthSchemes = proxyPreferredAuthSchemes;
/* 482 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setConnectionRequestTimeout(int connectionRequestTimeout) {
/* 486 */       this.connectionRequestTimeout = connectionRequestTimeout;
/* 487 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setConnectTimeout(int connectTimeout) {
/* 491 */       this.connectTimeout = connectTimeout;
/* 492 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setSocketTimeout(int socketTimeout) {
/* 496 */       this.socketTimeout = socketTimeout;
/* 497 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public Builder setDecompressionEnabled(boolean decompressionEnabled) {
/* 506 */       this.contentCompressionEnabled = decompressionEnabled;
/* 507 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setContentCompressionEnabled(boolean contentCompressionEnabled) {
/* 511 */       this.contentCompressionEnabled = contentCompressionEnabled;
/* 512 */       return this;
/*     */     }
/*     */     
/*     */     public RequestConfig build() {
/* 516 */       return new RequestConfig(this.expectContinueEnabled, this.proxy, this.localAddress, this.staleConnectionCheckEnabled, this.cookieSpec, this.redirectsEnabled, this.relativeRedirectsAllowed, this.circularRedirectsAllowed, this.maxRedirects, this.authenticationEnabled, this.targetPreferredAuthSchemes, this.proxyPreferredAuthSchemes, this.connectionRequestTimeout, this.connectTimeout, this.socketTimeout, this.contentCompressionEnabled);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\client\config\RequestConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */