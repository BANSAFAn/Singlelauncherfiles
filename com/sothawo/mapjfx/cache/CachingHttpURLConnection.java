/*     */ package com.sothawo.mapjfx.cache;
/*     */ 
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.ProtocolException;
/*     */ import java.net.URL;
/*     */ import java.nio.file.Path;
/*     */ import java.security.Permission;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ class CachingHttpURLConnection
/*     */   extends HttpURLConnection
/*     */ {
/*  41 */   private static final Logger logger = Logger.getLogger(CachingHttpURLConnection.class);
/*     */ 
/*     */ 
/*     */   
/*     */   private final HttpURLConnection delegate;
/*     */ 
/*     */ 
/*     */   
/*     */   private final Path cacheFile;
/*     */ 
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
/*     */   private CachingHttpURLConnection(URL u) {
/*  67 */     super(u);
/*  68 */     this.cache = null;
/*  69 */     this.delegate = null;
/*  70 */     this.cacheFile = null;
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
/*     */   public CachingHttpURLConnection(OfflineCache cache, HttpURLConnection delegate) throws IOException {
/*  84 */     super(delegate.getURL());
/*  85 */     this.cache = cache;
/*  86 */     this.delegate = delegate;
/*  87 */     this.cacheFile = cache.filenameForURL(delegate.getURL());
/*     */     
/*  89 */     this.cachedDataInfo = cache.readCachedDataInfo(this.cacheFile);
/*  90 */     this.readFromCache = (cache.isCached(delegate.getURL()) && null != this.cachedDataInfo);
/*  91 */     if (!this.readFromCache) {
/*  92 */       this.cachedDataInfo = new CachedDataInfo();
/*     */     }
/*     */     
/*  95 */     if (logger.isTraceEnabled()) {
/*  96 */       logger.trace("in cache: " + this.readFromCache + ", URL: " + delegate.getURL().toExternalForm() + ", cache file: " + this.cacheFile);
/*     */     }
/*     */   }
/*     */   
/*     */   public void connect() throws IOException {
/* 101 */     if (!this.readFromCache) {
/* 102 */       if (logger.isTraceEnabled()) {
/* 103 */         logger.trace("connect to " + this.delegate.getURL().toExternalForm());
/*     */       }
/* 105 */       this.delegate.connect();
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getHeaderFieldKey(int n) {
/* 110 */     return this.delegate.getHeaderFieldKey(n);
/*     */   }
/*     */   
/*     */   public void setFixedLengthStreamingMode(int contentLength) {
/* 114 */     this.delegate.setFixedLengthStreamingMode(contentLength);
/*     */   }
/*     */   
/*     */   public void setFixedLengthStreamingMode(long contentLength) {
/* 118 */     this.delegate.setFixedLengthStreamingMode(contentLength);
/*     */   } public void addRequestProperty(String key, String value) {
/* 120 */     this.delegate.addRequestProperty(key, value);
/*     */   }
/*     */   
/*     */   public void setChunkedStreamingMode(int chunklen) {
/* 124 */     this.delegate.setChunkedStreamingMode(chunklen);
/*     */   }
/*     */   
/*     */   public String getHeaderField(int n) {
/* 128 */     return this.delegate.getHeaderField(n);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void disconnect() {
/* 134 */     if (!this.readFromCache) {
/* 135 */       this.delegate.disconnect();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getAllowUserInteraction() {
/* 141 */     return this.delegate.getAllowUserInteraction();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getConnectTimeout() {
/* 146 */     return this.readFromCache ? 10 : this.delegate.getConnectTimeout();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getContent() throws IOException {
/* 151 */     return this.delegate.getContent();
/*     */   }
/*     */   
/*     */   public Object getContent(Class[] classes) throws IOException {
/* 155 */     return this.delegate.getContent(classes);
/*     */   }
/*     */   
/*     */   public String getContentEncoding() {
/* 159 */     if (!this.readFromCache) {
/* 160 */       this.cachedDataInfo.setContentEncoding(this.delegate.getContentEncoding());
/*     */     }
/* 162 */     return this.cachedDataInfo.getContentEncoding();
/*     */   }
/*     */   
/*     */   public int getContentLength() {
/* 166 */     return this.readFromCache ? -1 : this.delegate.getContentLength();
/*     */   }
/*     */   
/*     */   public long getContentLengthLong() {
/* 170 */     return this.readFromCache ? -1L : this.delegate.getContentLengthLong();
/*     */   }
/*     */   
/*     */   public String getContentType() {
/* 174 */     if (!this.readFromCache) {
/* 175 */       this.cachedDataInfo.setContentType(this.delegate.getContentType());
/*     */     }
/* 177 */     return this.cachedDataInfo.getContentType();
/*     */   }
/*     */   
/*     */   public long getDate() {
/* 181 */     return this.readFromCache ? 0L : this.delegate.getDate();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getDefaultUseCaches() {
/* 186 */     return this.delegate.getDefaultUseCaches();
/*     */   }
/*     */   
/*     */   public boolean getDoInput() {
/* 190 */     return this.delegate.getDoInput();
/*     */   }
/*     */   
/*     */   public boolean getDoOutput() {
/* 194 */     return this.delegate.getDoOutput();
/*     */   }
/*     */   
/*     */   public InputStream getErrorStream() {
/* 198 */     return this.delegate.getErrorStream();
/*     */   }
/*     */   
/*     */   public long getExpiration() {
/* 202 */     return this.readFromCache ? 0L : this.delegate.getExpiration();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getHeaderField(String name) {
/* 207 */     return this.delegate.getHeaderField(name);
/*     */   }
/*     */   
/*     */   public long getHeaderFieldDate(String name, long Default) {
/* 211 */     return this.delegate.getHeaderFieldDate(name, Default);
/*     */   }
/*     */   
/*     */   public int getHeaderFieldInt(String name, int Default) {
/* 215 */     return this.delegate.getHeaderFieldInt(name, Default);
/*     */   }
/*     */ 
/*     */   
/*     */   public long getHeaderFieldLong(String name, long Default) {
/* 220 */     return this.delegate.getHeaderFieldLong(name, Default);
/*     */   }
/*     */   
/*     */   public Map<String, List<String>> getHeaderFields() {
/* 224 */     if (!this.readFromCache) {
/* 225 */       setHeaderFieldsInCachedDataInfo();
/*     */     }
/* 227 */     return this.cachedDataInfo.getHeaderFields();
/*     */   }
/*     */   
/*     */   private void setHeaderFieldsInCachedDataInfo() {
/* 231 */     this.cachedDataInfo.setHeaderFields(this.delegate.getHeaderFields());
/*     */   }
/*     */   
/*     */   public long getIfModifiedSince() {
/* 235 */     return this.delegate.getIfModifiedSince();
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
/* 246 */     if (null == this.inputStream) {
/* 247 */       if (this.readFromCache) {
/* 248 */         this.inputStream = new FileInputStream(this.cacheFile.toFile());
/*     */       } else {
/*     */         
/* 251 */         WriteCacheFileInputStream wis = new WriteCacheFileInputStream(this.delegate.getInputStream(), new FileOutputStream(this.cacheFile.toFile()));
/* 252 */         wis.onInputStreamClose(() -> {
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
/* 263 */               } catch (IOException e) {
/*     */                 if (logger.isTraceEnabled()) {
/*     */                   logger.warn("cannot retrieve response code");
/*     */                 }
/*     */               } 
/*     */             });
/* 269 */         this.inputStream = wis;
/*     */       } 
/*     */     }
/* 272 */     return this.inputStream;
/*     */   }
/*     */   
/*     */   public boolean getInstanceFollowRedirects() {
/* 276 */     return this.delegate.getInstanceFollowRedirects();
/*     */   }
/*     */   
/*     */   public long getLastModified() {
/* 280 */     return this.readFromCache ? 0L : this.delegate.getLastModified();
/*     */   }
/*     */ 
/*     */   
/*     */   public OutputStream getOutputStream() throws IOException {
/* 285 */     return this.delegate.getOutputStream();
/*     */   }
/*     */   
/*     */   public Permission getPermission() throws IOException {
/* 289 */     return this.delegate.getPermission();
/*     */   }
/*     */   
/*     */   public int getReadTimeout() {
/* 293 */     return this.delegate.getReadTimeout();
/*     */   }
/*     */   
/*     */   public String getRequestMethod() {
/* 297 */     return this.delegate.getRequestMethod();
/*     */   }
/*     */   
/*     */   public Map<String, List<String>> getRequestProperties() {
/* 301 */     return this.delegate.getRequestProperties();
/*     */   }
/*     */   
/*     */   public String getRequestProperty(String key) {
/* 305 */     return this.delegate.getRequestProperty(key);
/*     */   }
/*     */   
/*     */   public int getResponseCode() throws IOException {
/* 309 */     return this.readFromCache ? 200 : this.delegate.getResponseCode();
/*     */   }
/*     */   
/*     */   public String getResponseMessage() throws IOException {
/* 313 */     return this.readFromCache ? "OK" : this.delegate.getResponseMessage();
/*     */   }
/*     */   
/*     */   public URL getURL() {
/* 317 */     return this.delegate.getURL();
/*     */   }
/*     */   
/*     */   public boolean getUseCaches() {
/* 321 */     return this.delegate.getUseCaches();
/*     */   }
/*     */   
/*     */   public void setAllowUserInteraction(boolean allowuserinteraction) {
/* 325 */     this.delegate.setAllowUserInteraction(allowuserinteraction);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setConnectTimeout(int timeout) {
/* 330 */     this.delegate.setConnectTimeout(timeout);
/*     */   }
/*     */   
/*     */   public void setDefaultUseCaches(boolean defaultusecaches) {
/* 334 */     this.delegate.setDefaultUseCaches(defaultusecaches);
/*     */   }
/*     */   
/*     */   public void setDoInput(boolean doinput) {
/* 338 */     this.delegate.setDoInput(doinput);
/*     */   }
/*     */   
/*     */   public void setDoOutput(boolean dooutput) {
/* 342 */     this.delegate.setDoOutput(dooutput);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setIfModifiedSince(long ifmodifiedsince) {
/* 347 */     this.delegate.setIfModifiedSince(ifmodifiedsince);
/*     */   }
/*     */   
/*     */   public void setInstanceFollowRedirects(boolean followRedirects) {
/* 351 */     this.delegate.setInstanceFollowRedirects(followRedirects);
/*     */   }
/*     */   
/*     */   public void setReadTimeout(int timeout) {
/* 355 */     this.delegate.setReadTimeout(timeout);
/*     */   }
/*     */   
/*     */   public void setRequestMethod(String method) throws ProtocolException {
/* 359 */     this.delegate.setRequestMethod(method);
/*     */   }
/*     */   
/*     */   public void setRequestProperty(String key, String value) {
/* 363 */     this.delegate.setRequestProperty(key, value);
/*     */   }
/*     */   
/*     */   public void setUseCaches(boolean usecaches) {
/* 367 */     this.delegate.setUseCaches(usecaches);
/*     */   }
/*     */   
/*     */   public boolean usingProxy() {
/* 371 */     return this.delegate.usingProxy();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\com\sothawo\mapjfx\cache\CachingHttpURLConnection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */