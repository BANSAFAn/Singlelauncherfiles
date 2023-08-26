/*     */ package com.sothawo.mapjfx.cache;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.Proxy;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.net.URLStreamHandler;
/*     */ import java.net.URLStreamHandlerFactory;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import javax.net.ssl.HttpsURLConnection;
/*     */ import org.apache.log4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class CachingURLStreamHandlerFactory
/*     */   implements URLStreamHandlerFactory
/*     */ {
/*     */   public static final String PROTO_HTTP = "http";
/*     */   public static final String PROTO_HTTPS = "https";
/*  45 */   private static final Logger logger = Logger.getLogger(CachingURLStreamHandlerFactory.class);
/*     */ 
/*     */   
/*     */   private final OfflineCache cache;
/*     */ 
/*     */   
/*  51 */   private final Map<String, URLStreamHandler> handlers = new ConcurrentHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   CachingURLStreamHandlerFactory(OfflineCache cache) {
/*  61 */     this.cache = cache;
/*     */     
/*  63 */     URLStreamHandler urlStreamHandler = getURLStreamHandler("http");
/*  64 */     if (urlStreamHandler != null) {
/*  65 */       this.handlers.put("http", urlStreamHandler);
/*     */     }
/*     */     
/*  68 */     urlStreamHandler = getURLStreamHandler("https");
/*  69 */     if (urlStreamHandler != null) {
/*  70 */       this.handlers.put("https", getURLStreamHandler("https"));
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
/*     */   private URLStreamHandler getURLStreamHandler(String protocol) {
/*     */     try {
/*  83 */       Method method = URL.class.getDeclaredMethod("getURLStreamHandler", new Class[] { String.class });
/*  84 */       method.setAccessible(true);
/*  85 */       return (URLStreamHandler)method.invoke((Object)null, new Object[] { protocol });
/*  86 */     } catch (Exception e) {
/*  87 */       if (logger.isTraceEnabled()) {
/*  88 */         logger.warn("could not access URL.getUrlStreamHandler for protocol " + protocol);
/*     */       }
/*  90 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URLStreamHandler createURLStreamHandler(final String protocol) {
/*  98 */     if (null == protocol) {
/*  99 */       throw new IllegalArgumentException("null protocol not allowed");
/*     */     }
/* 101 */     if (logger.isTraceEnabled()) {
/* 102 */       logger.trace("need to create URLStreamHandler for protocol " + protocol);
/*     */     }
/*     */     
/* 105 */     final String proto = protocol.toLowerCase();
/* 106 */     if ("http".equals(proto) || "https".equals(proto)) {
/* 107 */       if (this.handlers.get(protocol) == null) {
/* 108 */         logger.warn("default protocol handler for protocol {} not available " + protocol);
/* 109 */         return null;
/*     */       } 
/* 111 */       return new URLStreamHandler()
/*     */         {
/*     */           protected URLConnection openConnection(URL url) throws IOException {
/* 114 */             if (CachingURLStreamHandlerFactory.logger.isTraceEnabled()) {
/* 115 */               CachingURLStreamHandlerFactory.logger.trace("should open connection to " + url.toExternalForm());
/*     */             }
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 121 */             URLConnection defaultUrlConnection = (new URL(protocol, url.getHost(), url.getPort(), url.getFile(), (URLStreamHandler)CachingURLStreamHandlerFactory.this.handlers.get(protocol))).openConnection();
/*     */             
/* 123 */             if (!CachingURLStreamHandlerFactory.this.cache.urlShouldBeCached(url)) {
/* 124 */               if (CachingURLStreamHandlerFactory.logger.isTraceEnabled()) {
/* 125 */                 CachingURLStreamHandlerFactory.logger.trace("not using cache for " + url);
/*     */               }
/* 127 */               return defaultUrlConnection;
/*     */             } 
/*     */             
/* 130 */             Path cacheFile = CachingURLStreamHandlerFactory.this.cache.filenameForURL(url);
/*     */             
/* 132 */             if (CachingURLStreamHandlerFactory.this.cache.isCached(url))
/*     */             {
/*     */               
/* 135 */               return new CachingHttpURLConnection(CachingURLStreamHandlerFactory.this.cache, (HttpURLConnection)defaultUrlConnection);
/*     */             }
/* 137 */             switch (proto) {
/*     */               case "http":
/* 139 */                 return new CachingHttpURLConnection(CachingURLStreamHandlerFactory.this.cache, (HttpURLConnection)defaultUrlConnection);
/*     */               case "https":
/* 141 */                 return new CachingHttpsURLConnection(CachingURLStreamHandlerFactory.this.cache, (HttpsURLConnection)defaultUrlConnection);
/*     */             } 
/*     */             
/* 144 */             throw new IOException("no matching handler");
/*     */           }
/*     */ 
/*     */           
/*     */           protected URLConnection openConnection(URL u, Proxy p) throws IOException {
/* 149 */             if (CachingURLStreamHandlerFactory.logger.isTraceEnabled()) {
/* 150 */               CachingURLStreamHandlerFactory.logger.trace("should open connection to" + u.toExternalForm());
/*     */             }
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 156 */             URLConnection defaultUrlConnection = (new URL(protocol, u.getHost(), u.getPort(), u.getFile(), (URLStreamHandler)CachingURLStreamHandlerFactory.this.handlers.get(protocol))).openConnection(p);
/*     */             
/* 158 */             return defaultUrlConnection;
/*     */           }
/*     */         };
/*     */     } 
/*     */     
/* 163 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\com\sothawo\mapjfx\cache\CachingURLStreamHandlerFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */