/*     */ package com.sothawo.mapjfx.cache;
/*     */ 
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.ProtocolException;
/*     */ import java.net.URL;
/*     */ import java.nio.file.Path;
/*     */ import java.security.Permission;
/*     */ import java.security.Principal;
/*     */ import java.security.cert.Certificate;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.net.ssl.HostnameVerifier;
/*     */ import javax.net.ssl.HttpsURLConnection;
/*     */ import javax.net.ssl.SSLPeerUnverifiedException;
/*     */ import javax.net.ssl.SSLSocketFactory;
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
/*     */ class CachingHttpsURLConnection
/*     */   extends HttpsURLConnection
/*     */ {
/*  46 */   private static final Logger logger = Logger.getLogger(CachingHttpsURLConnection.class);
/*     */ 
/*     */ 
/*     */   
/*     */   private final HttpsURLConnection delegate;
/*     */ 
/*     */ 
/*     */   
/*     */   private final Path cacheFile;
/*     */ 
/*     */   
/*     */   private final OfflineCache cache;
/*     */ 
/*     */   
/*     */   private boolean readFromCache = false;
/*     */ 
/*     */   
/*     */   private InputStream inputStream;
/*     */ 
/*     */   
/*     */   private CachedDataInfo cachedDataInfo;
/*     */ 
/*     */ 
/*     */   
/*     */   private CachingHttpsURLConnection(URL url) {
/*  71 */     super(url);
/*  72 */     this.cache = null;
/*  73 */     this.delegate = null;
/*  74 */     this.cacheFile = null;
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
/*     */   public CachingHttpsURLConnection(OfflineCache cache, HttpsURLConnection delegate) throws IOException {
/*  89 */     super(delegate.getURL());
/*  90 */     this.cache = cache;
/*  91 */     this.delegate = delegate;
/*  92 */     this.cacheFile = cache.filenameForURL(delegate.getURL());
/*     */     
/*  94 */     this.cachedDataInfo = cache.readCachedDataInfo(this.cacheFile);
/*  95 */     this.readFromCache = (cache.isCached(delegate.getURL()) && null != this.cachedDataInfo);
/*  96 */     if (!this.readFromCache) {
/*  97 */       this.cachedDataInfo = new CachedDataInfo();
/*     */     }
/*     */     
/* 100 */     if (logger.isTraceEnabled()) {
/* 101 */       logger.trace("in cache: " + this.readFromCache + ", URL: " + delegate.getURL().toExternalForm() + ", cache file: " + this.cacheFile);
/*     */     }
/*     */   }
/*     */   
/*     */   public void connect() throws IOException {
/* 106 */     if (!this.readFromCache) {
/* 107 */       if (logger.isTraceEnabled()) {
/* 108 */         logger.trace("connect to " + this.delegate.getURL().toExternalForm());
/*     */       }
/* 110 */       this.delegate.connect();
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getCipherSuite() {
/* 115 */     return this.delegate.getCipherSuite();
/*     */   }
/*     */   
/*     */   public Certificate[] getLocalCertificates() {
/* 119 */     return this.delegate.getLocalCertificates();
/*     */   }
/*     */   
/*     */   public Certificate[] getServerCertificates() throws SSLPeerUnverifiedException {
/* 123 */     return this.delegate.getServerCertificates();
/*     */   }
/*     */   
/*     */   public Principal getPeerPrincipal() throws SSLPeerUnverifiedException {
/* 127 */     return this.delegate.getPeerPrincipal();
/*     */   }
/*     */   
/*     */   public Principal getLocalPrincipal() {
/* 131 */     return this.delegate.getLocalPrincipal();
/*     */   }
/*     */   
/*     */   public String getHeaderFieldKey(int n) {
/* 135 */     return this.delegate.getHeaderFieldKey(n);
/*     */   }
/*     */   
/*     */   public void setFixedLengthStreamingMode(int contentLength) {
/* 139 */     this.delegate.setFixedLengthStreamingMode(contentLength);
/*     */   } public void addRequestProperty(String key, String value) {
/* 141 */     this.delegate.addRequestProperty(key, value);
/*     */   }
/*     */   
/*     */   public void setFixedLengthStreamingMode(long contentLength) {
/* 145 */     this.delegate.setFixedLengthStreamingMode(contentLength);
/*     */   }
/*     */   
/*     */   public void setChunkedStreamingMode(int chunklen) {
/* 149 */     this.delegate.setChunkedStreamingMode(chunklen);
/*     */   }
/*     */   
/*     */   public String getHeaderField(int n) {
/* 153 */     return this.delegate.getHeaderField(n);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void disconnect() {
/* 160 */     if (!this.readFromCache) {
/* 161 */       this.delegate.disconnect();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getAllowUserInteraction() {
/* 167 */     return this.delegate.getAllowUserInteraction();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getConnectTimeout() {
/* 172 */     return this.readFromCache ? 10 : this.delegate.getConnectTimeout();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getContent() throws IOException {
/* 177 */     return this.delegate.getContent();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getContent(Class[] classes) throws IOException {
/* 182 */     return this.delegate.getContent(classes);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getContentEncoding() {
/* 187 */     if (!this.readFromCache) {
/* 188 */       this.cachedDataInfo.setContentEncoding(this.delegate.getContentEncoding());
/*     */     }
/* 190 */     return this.cachedDataInfo.getContentEncoding();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getContentLength() {
/* 195 */     return this.readFromCache ? -1 : this.delegate.getContentLength();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getContentLengthLong() {
/* 200 */     return this.readFromCache ? -1L : this.delegate.getContentLengthLong();
/*     */   }
/*     */   
/*     */   public String getContentType() {
/* 204 */     if (!this.readFromCache) {
/* 205 */       this.cachedDataInfo.setContentType(this.delegate.getContentType());
/*     */     }
/* 207 */     return this.cachedDataInfo.getContentType();
/*     */   }
/*     */   
/*     */   public long getDate() {
/* 211 */     return this.readFromCache ? 0L : this.delegate.getDate();
/*     */   }
/*     */   
/*     */   public boolean getDefaultUseCaches() {
/* 215 */     return this.delegate.getDefaultUseCaches();
/*     */   }
/*     */   
/*     */   public boolean getDoInput() {
/* 219 */     return this.delegate.getDoInput();
/*     */   }
/*     */   
/*     */   public boolean getDoOutput() {
/* 223 */     return this.delegate.getDoOutput();
/*     */   }
/*     */   
/*     */   public InputStream getErrorStream() {
/* 227 */     return this.delegate.getErrorStream();
/*     */   }
/*     */   
/*     */   public long getExpiration() {
/* 231 */     return this.readFromCache ? 0L : this.delegate.getExpiration();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getHeaderField(String name) {
/* 236 */     return this.delegate.getHeaderField(name);
/*     */   }
/*     */   
/*     */   public long getHeaderFieldDate(String name, long Default) {
/* 240 */     return this.delegate.getHeaderFieldDate(name, Default);
/*     */   }
/*     */   
/*     */   public int getHeaderFieldInt(String name, int Default) {
/* 244 */     return this.delegate.getHeaderFieldInt(name, Default);
/*     */   }
/*     */ 
/*     */   
/*     */   public long getHeaderFieldLong(String name, long Default) {
/* 249 */     return this.delegate.getHeaderFieldLong(name, Default);
/*     */   }
/*     */   
/*     */   public Map<String, List<String>> getHeaderFields() {
/* 253 */     if (!this.readFromCache) {
/* 254 */       setHeaderFieldsInCachedDataInfo();
/*     */     }
/* 256 */     return this.cachedDataInfo.getHeaderFields();
/*     */   }
/*     */   
/*     */   private void setHeaderFieldsInCachedDataInfo() {
/* 260 */     this.cachedDataInfo.setHeaderFields(this.delegate.getHeaderFields());
/*     */   }
/*     */   
/*     */   public HostnameVerifier getHostnameVerifier() {
/* 264 */     return this.delegate.getHostnameVerifier();
/*     */   }
/*     */   
/*     */   public long getIfModifiedSince() {
/* 268 */     return this.delegate.getIfModifiedSince();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream getInputStream() throws IOException {
/* 279 */     if (null == this.inputStream) {
/* 280 */       if (this.readFromCache) {
/* 281 */         this.inputStream = new FileInputStream(this.cacheFile.toFile());
/*     */       } else {
/*     */         
/* 284 */         WriteCacheFileInputStream wis = new WriteCacheFileInputStream(this.delegate.getInputStream(), new FileOutputStream(this.cacheFile.toFile()));
/* 285 */         wis.onInputStreamClose(() -> {
/*     */               try {
/*     */                 this.cachedDataInfo.setFromHttpUrlConnection(this.delegate);
/*     */                 
/*     */                 int responseCode = this.delegate.getResponseCode();
/*     */                 
/*     */                 if (responseCode == 200) {
/*     */                   this.cache.saveCachedDataInfo(this.cacheFile, this.cachedDataInfo);
/*     */                 } else if (logger.isTraceEnabled()) {
/*     */                   logger.warn("not caching because of response code " + responseCode + ": " + getURL());
/*     */                 } 
/* 296 */               } catch (IOException e) {
/*     */                 if (logger.isTraceEnabled()) {
/*     */                   logger.warn("cannot retrieve response code");
/*     */                 }
/*     */               } 
/*     */             });
/* 302 */         this.inputStream = wis;
/*     */       } 
/*     */     }
/* 305 */     return this.inputStream;
/*     */   }
/*     */   
/*     */   public boolean getInstanceFollowRedirects() {
/* 309 */     return this.delegate.getInstanceFollowRedirects();
/*     */   }
/*     */   
/*     */   public long getLastModified() {
/* 313 */     return this.readFromCache ? 0L : this.delegate.getLastModified();
/*     */   }
/*     */ 
/*     */   
/*     */   public OutputStream getOutputStream() throws IOException {
/* 318 */     return this.delegate.getOutputStream();
/*     */   }
/*     */ 
/*     */   
/*     */   public Permission getPermission() throws IOException {
/* 323 */     return this.delegate.getPermission();
/*     */   }
/*     */   
/*     */   public int getReadTimeout() {
/* 327 */     return this.delegate.getReadTimeout();
/*     */   }
/*     */   
/*     */   public String getRequestMethod() {
/* 331 */     return this.delegate.getRequestMethod();
/*     */   }
/*     */   
/*     */   public Map<String, List<String>> getRequestProperties() {
/* 335 */     return this.delegate.getRequestProperties();
/*     */   }
/*     */   
/*     */   public String getRequestProperty(String key) {
/* 339 */     return this.delegate.getRequestProperty(key);
/*     */   }
/*     */   
/*     */   public int getResponseCode() throws IOException {
/* 343 */     return this.readFromCache ? 200 : this.delegate.getResponseCode();
/*     */   }
/*     */   
/*     */   public String getResponseMessage() throws IOException {
/* 347 */     return this.readFromCache ? "OK" : this.delegate.getResponseMessage();
/*     */   }
/*     */   
/*     */   public SSLSocketFactory getSSLSocketFactory() {
/* 351 */     return this.delegate.getSSLSocketFactory();
/*     */   }
/*     */ 
/*     */   
/*     */   public URL getURL() {
/* 356 */     return this.delegate.getURL();
/*     */   }
/*     */   
/*     */   public boolean getUseCaches() {
/* 360 */     return this.delegate.getUseCaches();
/*     */   }
/*     */   
/*     */   public void setAllowUserInteraction(boolean allowuserinteraction) {
/* 364 */     this.delegate.setAllowUserInteraction(allowuserinteraction);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setConnectTimeout(int timeout) {
/* 369 */     this.delegate.setConnectTimeout(timeout);
/*     */   }
/*     */   
/*     */   public void setDefaultUseCaches(boolean defaultusecaches) {
/* 373 */     this.delegate.setDefaultUseCaches(defaultusecaches);
/*     */   }
/*     */   
/*     */   public void setDoInput(boolean doinput) {
/* 377 */     this.delegate.setDoInput(doinput);
/*     */   }
/*     */   
/*     */   public void setDoOutput(boolean dooutput) {
/* 381 */     this.delegate.setDoOutput(dooutput);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setHostnameVerifier(HostnameVerifier hostnameVerifier) {
/* 386 */     this.delegate.setHostnameVerifier(hostnameVerifier);
/*     */   }
/*     */   
/*     */   public void setIfModifiedSince(long ifmodifiedsince) {
/* 390 */     this.delegate.setIfModifiedSince(ifmodifiedsince);
/*     */   }
/*     */   
/*     */   public void setInstanceFollowRedirects(boolean followRedirects) {
/* 394 */     this.delegate.setInstanceFollowRedirects(followRedirects);
/*     */   }
/*     */   
/*     */   public void setReadTimeout(int timeout) {
/* 398 */     this.delegate.setReadTimeout(timeout);
/*     */   }
/*     */   
/*     */   public void setRequestMethod(String method) throws ProtocolException {
/* 402 */     this.delegate.setRequestMethod(method);
/*     */   }
/*     */   
/*     */   public void setRequestProperty(String key, String value) {
/* 406 */     this.delegate.setRequestProperty(key, value);
/*     */   }
/*     */   
/*     */   public void setSSLSocketFactory(SSLSocketFactory sslSocketFactory) {
/* 410 */     this.delegate.setSSLSocketFactory(sslSocketFactory);
/*     */   }
/*     */   
/*     */   public void setUseCaches(boolean usecaches) {
/* 414 */     this.delegate.setUseCaches(usecaches);
/*     */   }
/*     */   
/*     */   public boolean usingProxy() {
/* 418 */     return this.delegate.usingProxy();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\com\sothawo\mapjfx\cache\CachingHttpsURLConnection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */