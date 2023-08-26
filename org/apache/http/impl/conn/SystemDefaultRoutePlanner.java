/*     */ package org.apache.http.impl.conn;
/*     */ 
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Proxy;
/*     */ import java.net.ProxySelector;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.List;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.conn.SchemePortResolver;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class SystemDefaultRoutePlanner
/*     */   extends DefaultRoutePlanner
/*     */ {
/*     */   private final ProxySelector proxySelector;
/*     */   
/*     */   public SystemDefaultRoutePlanner(SchemePortResolver schemePortResolver, ProxySelector proxySelector) {
/*  60 */     super(schemePortResolver);
/*  61 */     this.proxySelector = (proxySelector != null) ? proxySelector : ProxySelector.getDefault();
/*     */   }
/*     */   
/*     */   public SystemDefaultRoutePlanner(ProxySelector proxySelector) {
/*  65 */     this(null, proxySelector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected HttpHost determineProxy(HttpHost target, HttpRequest request, HttpContext context) throws HttpException {
/*     */     URI targetURI;
/*     */     try {
/*  75 */       targetURI = new URI(target.toURI());
/*  76 */     } catch (URISyntaxException ex) {
/*  77 */       throw new HttpException("Cannot convert host to URI: " + target, ex);
/*     */     } 
/*  79 */     List<Proxy> proxies = this.proxySelector.select(targetURI);
/*  80 */     Proxy p = chooseProxy(proxies);
/*  81 */     HttpHost result = null;
/*  82 */     if (p.type() == Proxy.Type.HTTP) {
/*     */       
/*  84 */       if (!(p.address() instanceof InetSocketAddress)) {
/*  85 */         throw new HttpException("Unable to handle non-Inet proxy address: " + p.address());
/*     */       }
/*  87 */       InetSocketAddress isa = (InetSocketAddress)p.address();
/*     */       
/*  89 */       result = new HttpHost(getHost(isa), isa.getPort());
/*     */     } 
/*     */     
/*  92 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getHost(InetSocketAddress isa) {
/* 100 */     return isa.isUnresolved() ? isa.getHostName() : isa.getAddress().getHostAddress();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Proxy chooseProxy(List<Proxy> proxies) {
/* 106 */     Proxy result = null;
/*     */     
/* 108 */     for (int i = 0; result == null && i < proxies.size(); i++) {
/* 109 */       Proxy p = proxies.get(i);
/* 110 */       switch (p.type()) {
/*     */         
/*     */         case DIRECT:
/*     */         case HTTP:
/* 114 */           result = p;
/*     */           break;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     } 
/* 123 */     if (result == null)
/*     */     {
/*     */ 
/*     */       
/* 127 */       result = Proxy.NO_PROXY;
/*     */     }
/* 129 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\conn\SystemDefaultRoutePlanner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */