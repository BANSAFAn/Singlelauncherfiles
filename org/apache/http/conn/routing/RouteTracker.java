/*     */ package org.apache.http.conn.routing;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.Asserts;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @NotThreadSafe
/*     */ public final class RouteTracker
/*     */   implements RouteInfo, Cloneable
/*     */ {
/*     */   private final HttpHost targetHost;
/*     */   private final InetAddress localAddress;
/*     */   private boolean connected;
/*     */   private HttpHost[] proxyChain;
/*     */   private RouteInfo.TunnelType tunnelled;
/*     */   private RouteInfo.LayerType layered;
/*     */   private boolean secure;
/*     */   
/*     */   public RouteTracker(HttpHost target, InetAddress local) {
/*  82 */     Args.notNull(target, "Target host");
/*  83 */     this.targetHost = target;
/*  84 */     this.localAddress = local;
/*  85 */     this.tunnelled = RouteInfo.TunnelType.PLAIN;
/*  86 */     this.layered = RouteInfo.LayerType.PLAIN;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/*  93 */     this.connected = false;
/*  94 */     this.proxyChain = null;
/*  95 */     this.tunnelled = RouteInfo.TunnelType.PLAIN;
/*  96 */     this.layered = RouteInfo.LayerType.PLAIN;
/*  97 */     this.secure = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RouteTracker(HttpRoute route) {
/* 108 */     this(route.getTargetHost(), route.getLocalAddress());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void connectTarget(boolean secure) {
/* 118 */     Asserts.check(!this.connected, "Already connected");
/* 119 */     this.connected = true;
/* 120 */     this.secure = secure;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void connectProxy(HttpHost proxy, boolean secure) {
/* 131 */     Args.notNull(proxy, "Proxy host");
/* 132 */     Asserts.check(!this.connected, "Already connected");
/* 133 */     this.connected = true;
/* 134 */     this.proxyChain = new HttpHost[] { proxy };
/* 135 */     this.secure = secure;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void tunnelTarget(boolean secure) {
/* 145 */     Asserts.check(this.connected, "No tunnel unless connected");
/* 146 */     Asserts.notNull(this.proxyChain, "No tunnel without proxy");
/* 147 */     this.tunnelled = RouteInfo.TunnelType.TUNNELLED;
/* 148 */     this.secure = secure;
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
/*     */   public final void tunnelProxy(HttpHost proxy, boolean secure) {
/* 161 */     Args.notNull(proxy, "Proxy host");
/* 162 */     Asserts.check(this.connected, "No tunnel unless connected");
/* 163 */     Asserts.notNull(this.proxyChain, "No tunnel without proxy");
/*     */     
/* 165 */     HttpHost[] proxies = new HttpHost[this.proxyChain.length + 1];
/* 166 */     System.arraycopy(this.proxyChain, 0, proxies, 0, this.proxyChain.length);
/*     */     
/* 168 */     proxies[proxies.length - 1] = proxy;
/*     */     
/* 170 */     this.proxyChain = proxies;
/* 171 */     this.secure = secure;
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
/*     */   public final void layerProtocol(boolean secure) {
/* 183 */     Asserts.check(this.connected, "No layered protocol unless connected");
/* 184 */     this.layered = RouteInfo.LayerType.LAYERED;
/* 185 */     this.secure = secure;
/*     */   }
/*     */ 
/*     */   
/*     */   public final HttpHost getTargetHost() {
/* 190 */     return this.targetHost;
/*     */   }
/*     */ 
/*     */   
/*     */   public final InetAddress getLocalAddress() {
/* 195 */     return this.localAddress;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int getHopCount() {
/* 200 */     int hops = 0;
/* 201 */     if (this.connected) {
/* 202 */       if (this.proxyChain == null) {
/* 203 */         hops = 1;
/*     */       } else {
/* 205 */         hops = this.proxyChain.length + 1;
/*     */       } 
/*     */     }
/* 208 */     return hops;
/*     */   }
/*     */ 
/*     */   
/*     */   public final HttpHost getHopTarget(int hop) {
/* 213 */     Args.notNegative(hop, "Hop index");
/* 214 */     int hopcount = getHopCount();
/* 215 */     Args.check((hop < hopcount), "Hop index exceeds tracked route length");
/* 216 */     HttpHost result = null;
/* 217 */     if (hop < hopcount - 1) {
/* 218 */       result = this.proxyChain[hop];
/*     */     } else {
/* 220 */       result = this.targetHost;
/*     */     } 
/*     */     
/* 223 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public final HttpHost getProxyHost() {
/* 228 */     return (this.proxyChain == null) ? null : this.proxyChain[0];
/*     */   }
/*     */   
/*     */   public final boolean isConnected() {
/* 232 */     return this.connected;
/*     */   }
/*     */ 
/*     */   
/*     */   public final RouteInfo.TunnelType getTunnelType() {
/* 237 */     return this.tunnelled;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isTunnelled() {
/* 242 */     return (this.tunnelled == RouteInfo.TunnelType.TUNNELLED);
/*     */   }
/*     */ 
/*     */   
/*     */   public final RouteInfo.LayerType getLayerType() {
/* 247 */     return this.layered;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isLayered() {
/* 252 */     return (this.layered == RouteInfo.LayerType.LAYERED);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isSecure() {
/* 257 */     return this.secure;
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
/*     */   public final HttpRoute toRoute() {
/* 269 */     return !this.connected ? null : new HttpRoute(this.targetHost, this.localAddress, this.proxyChain, this.secure, this.tunnelled, this.layered);
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
/*     */   public final boolean equals(Object o) {
/* 285 */     if (o == this) {
/* 286 */       return true;
/*     */     }
/* 288 */     if (!(o instanceof RouteTracker)) {
/* 289 */       return false;
/*     */     }
/*     */     
/* 292 */     RouteTracker that = (RouteTracker)o;
/* 293 */     return (this.connected == that.connected && this.secure == that.secure && this.tunnelled == that.tunnelled && this.layered == that.layered && LangUtils.equals(this.targetHost, that.targetHost) && LangUtils.equals(this.localAddress, that.localAddress) && LangUtils.equals((Object[])this.proxyChain, (Object[])that.proxyChain));
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
/*     */   public final int hashCode() {
/* 314 */     int hash = 17;
/* 315 */     hash = LangUtils.hashCode(hash, this.targetHost);
/* 316 */     hash = LangUtils.hashCode(hash, this.localAddress);
/* 317 */     if (this.proxyChain != null) {
/* 318 */       for (HttpHost element : this.proxyChain) {
/* 319 */         hash = LangUtils.hashCode(hash, element);
/*     */       }
/*     */     }
/* 322 */     hash = LangUtils.hashCode(hash, this.connected);
/* 323 */     hash = LangUtils.hashCode(hash, this.secure);
/* 324 */     hash = LangUtils.hashCode(hash, this.tunnelled);
/* 325 */     hash = LangUtils.hashCode(hash, this.layered);
/* 326 */     return hash;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String toString() {
/* 336 */     StringBuilder cab = new StringBuilder(50 + getHopCount() * 30);
/*     */     
/* 338 */     cab.append("RouteTracker[");
/* 339 */     if (this.localAddress != null) {
/* 340 */       cab.append(this.localAddress);
/* 341 */       cab.append("->");
/*     */     } 
/* 343 */     cab.append('{');
/* 344 */     if (this.connected) {
/* 345 */       cab.append('c');
/*     */     }
/* 347 */     if (this.tunnelled == RouteInfo.TunnelType.TUNNELLED) {
/* 348 */       cab.append('t');
/*     */     }
/* 350 */     if (this.layered == RouteInfo.LayerType.LAYERED) {
/* 351 */       cab.append('l');
/*     */     }
/* 353 */     if (this.secure) {
/* 354 */       cab.append('s');
/*     */     }
/* 356 */     cab.append("}->");
/* 357 */     if (this.proxyChain != null) {
/* 358 */       for (HttpHost element : this.proxyChain) {
/* 359 */         cab.append(element);
/* 360 */         cab.append("->");
/*     */       } 
/*     */     }
/* 363 */     cab.append(this.targetHost);
/* 364 */     cab.append(']');
/*     */     
/* 366 */     return cab.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException {
/* 373 */     return super.clone();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\conn\routing\RouteTracker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */