/*     */ package org.apache.http.conn.routing;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.LangUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class HttpRoute
/*     */   implements RouteInfo, Cloneable
/*     */ {
/*     */   private final HttpHost targetHost;
/*     */   private final InetAddress localAddress;
/*     */   private final List<HttpHost> proxyChain;
/*     */   private final RouteInfo.TunnelType tunnelled;
/*     */   private final RouteInfo.LayerType layered;
/*     */   private final boolean secure;
/*     */   
/*     */   private HttpRoute(HttpHost target, InetAddress local, List<HttpHost> proxies, boolean secure, RouteInfo.TunnelType tunnelled, RouteInfo.LayerType layered) {
/*  73 */     Args.notNull(target, "Target host");
/*  74 */     this.targetHost = normalize(target);
/*  75 */     this.localAddress = local;
/*  76 */     if (proxies != null && !proxies.isEmpty()) {
/*  77 */       this.proxyChain = new ArrayList<HttpHost>(proxies);
/*     */     } else {
/*  79 */       this.proxyChain = null;
/*     */     } 
/*  81 */     if (tunnelled == RouteInfo.TunnelType.TUNNELLED) {
/*  82 */       Args.check((this.proxyChain != null), "Proxy required if tunnelled");
/*     */     }
/*  84 */     this.secure = secure;
/*  85 */     this.tunnelled = (tunnelled != null) ? tunnelled : RouteInfo.TunnelType.PLAIN;
/*  86 */     this.layered = (layered != null) ? layered : RouteInfo.LayerType.PLAIN;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int getDefaultPort(String schemeName) {
/*  91 */     if ("http".equalsIgnoreCase(schemeName))
/*  92 */       return 80; 
/*  93 */     if ("https".equalsIgnoreCase(schemeName)) {
/*  94 */       return 443;
/*     */     }
/*  96 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static HttpHost normalize(HttpHost target) {
/* 103 */     if (target.getPort() >= 0) {
/* 104 */       return target;
/*     */     }
/* 106 */     InetAddress address = target.getAddress();
/* 107 */     String schemeName = target.getSchemeName();
/* 108 */     if (address != null) {
/* 109 */       return new HttpHost(address, getDefaultPort(schemeName), schemeName);
/*     */     }
/* 111 */     String hostName = target.getHostName();
/* 112 */     return new HttpHost(hostName, getDefaultPort(schemeName), schemeName);
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
/*     */   public HttpRoute(HttpHost target, InetAddress local, HttpHost[] proxies, boolean secure, RouteInfo.TunnelType tunnelled, RouteInfo.LayerType layered) {
/* 132 */     this(target, local, (proxies != null) ? Arrays.<HttpHost>asList(proxies) : null, secure, tunnelled, layered);
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
/*     */   public HttpRoute(HttpHost target, InetAddress local, HttpHost proxy, boolean secure, RouteInfo.TunnelType tunnelled, RouteInfo.LayerType layered) {
/* 155 */     this(target, local, (proxy != null) ? Collections.<HttpHost>singletonList(proxy) : null, secure, tunnelled, layered);
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
/*     */   public HttpRoute(HttpHost target, InetAddress local, boolean secure) {
/* 170 */     this(target, local, Collections.emptyList(), secure, RouteInfo.TunnelType.PLAIN, RouteInfo.LayerType.PLAIN);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpRoute(HttpHost target) {
/* 180 */     this(target, (InetAddress)null, Collections.emptyList(), false, RouteInfo.TunnelType.PLAIN, RouteInfo.LayerType.PLAIN);
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
/*     */   public HttpRoute(HttpHost target, InetAddress local, HttpHost proxy, boolean secure) {
/* 199 */     this(target, local, Collections.singletonList(Args.notNull(proxy, "Proxy host")), secure, secure ? RouteInfo.TunnelType.TUNNELLED : RouteInfo.TunnelType.PLAIN, secure ? RouteInfo.LayerType.LAYERED : RouteInfo.LayerType.PLAIN);
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
/*     */   public HttpRoute(HttpHost target, HttpHost proxy) {
/* 213 */     this(target, null, proxy, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public final HttpHost getTargetHost() {
/* 218 */     return this.targetHost;
/*     */   }
/*     */ 
/*     */   
/*     */   public final InetAddress getLocalAddress() {
/* 223 */     return this.localAddress;
/*     */   }
/*     */   
/*     */   public final InetSocketAddress getLocalSocketAddress() {
/* 227 */     return (this.localAddress != null) ? new InetSocketAddress(this.localAddress, 0) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int getHopCount() {
/* 232 */     return (this.proxyChain != null) ? (this.proxyChain.size() + 1) : 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public final HttpHost getHopTarget(int hop) {
/* 237 */     Args.notNegative(hop, "Hop index");
/* 238 */     int hopcount = getHopCount();
/* 239 */     Args.check((hop < hopcount), "Hop index exceeds tracked route length");
/* 240 */     if (hop < hopcount - 1) {
/* 241 */       return this.proxyChain.get(hop);
/*     */     }
/* 243 */     return this.targetHost;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpHost getProxyHost() {
/* 249 */     return (this.proxyChain != null && !this.proxyChain.isEmpty()) ? this.proxyChain.get(0) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public final RouteInfo.TunnelType getTunnelType() {
/* 254 */     return this.tunnelled;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isTunnelled() {
/* 259 */     return (this.tunnelled == RouteInfo.TunnelType.TUNNELLED);
/*     */   }
/*     */ 
/*     */   
/*     */   public final RouteInfo.LayerType getLayerType() {
/* 264 */     return this.layered;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isLayered() {
/* 269 */     return (this.layered == RouteInfo.LayerType.LAYERED);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isSecure() {
/* 274 */     return this.secure;
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
/*     */   public final boolean equals(Object obj) {
/* 287 */     if (this == obj) {
/* 288 */       return true;
/*     */     }
/* 290 */     if (obj instanceof HttpRoute) {
/* 291 */       HttpRoute that = (HttpRoute)obj;
/* 292 */       return (this.secure == that.secure && this.tunnelled == that.tunnelled && this.layered == that.layered && LangUtils.equals(this.targetHost, that.targetHost) && LangUtils.equals(this.localAddress, that.localAddress) && LangUtils.equals(this.proxyChain, that.proxyChain));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 301 */     return false;
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
/*     */   public final int hashCode() {
/* 313 */     int hash = 17;
/* 314 */     hash = LangUtils.hashCode(hash, this.targetHost);
/* 315 */     hash = LangUtils.hashCode(hash, this.localAddress);
/* 316 */     if (this.proxyChain != null) {
/* 317 */       for (HttpHost element : this.proxyChain) {
/* 318 */         hash = LangUtils.hashCode(hash, element);
/*     */       }
/*     */     }
/* 321 */     hash = LangUtils.hashCode(hash, this.secure);
/* 322 */     hash = LangUtils.hashCode(hash, this.tunnelled);
/* 323 */     hash = LangUtils.hashCode(hash, this.layered);
/* 324 */     return hash;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String toString() {
/* 334 */     StringBuilder cab = new StringBuilder(50 + getHopCount() * 30);
/* 335 */     if (this.localAddress != null) {
/* 336 */       cab.append(this.localAddress);
/* 337 */       cab.append("->");
/*     */     } 
/* 339 */     cab.append('{');
/* 340 */     if (this.tunnelled == RouteInfo.TunnelType.TUNNELLED) {
/* 341 */       cab.append('t');
/*     */     }
/* 343 */     if (this.layered == RouteInfo.LayerType.LAYERED) {
/* 344 */       cab.append('l');
/*     */     }
/* 346 */     if (this.secure) {
/* 347 */       cab.append('s');
/*     */     }
/* 349 */     cab.append("}->");
/* 350 */     if (this.proxyChain != null) {
/* 351 */       for (HttpHost aProxyChain : this.proxyChain) {
/* 352 */         cab.append(aProxyChain);
/* 353 */         cab.append("->");
/*     */       } 
/*     */     }
/* 356 */     cab.append(this.targetHost);
/* 357 */     return cab.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException {
/* 363 */     return super.clone();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\conn\routing\HttpRoute.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */