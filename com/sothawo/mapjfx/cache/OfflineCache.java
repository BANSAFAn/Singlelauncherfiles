/*     */ package com.sothawo.mapjfx.cache;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.net.URLEncoder;
/*     */ import java.nio.file.FileSystems;
/*     */ import java.nio.file.FileVisitResult;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.nio.file.SimpleFileVisitor;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.ForkJoinPool;
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.stream.Collectors;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum OfflineCache
/*     */ {
/*  58 */   INSTANCE;
/*     */   
/*     */   private Path cacheDirectory;
/*     */   private boolean active;
/*     */   private boolean urlStreamHandlerFactoryIsInitialized;
/*     */   private final Collection<Pattern> cachePatterns;
/*     */   
/*     */   OfflineCache() {
/*  66 */     this.noCachePatterns = new ArrayList<>();
/*     */     
/*  68 */     this.cachePatterns = new ArrayList<>();
/*     */     
/*  70 */     this.urlStreamHandlerFactoryIsInitialized = false;
/*     */     
/*  72 */     this.active = false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private final Collection<Pattern> noCachePatterns;
/*     */   
/*     */   private static final int PRELOAD_DATABUFFER_SIZE = 1048576;
/*     */ 
/*     */   
/*     */   static void clearDirectory(Path path) throws IOException {
/*  83 */     Files.walkFileTree(path, new DeletingFileVisitor(path));
/*     */   } private static final String TILE_OPENSTREETMAP_ORG = "[a-z]\\.tile\\.openstreetmap\\.org"; private static final Logger logger; static {
/*     */     logger = Logger.getLogger(OfflineCache.class);
/*     */   } public Collection<String> getNoCacheFilters() {
/*  87 */     return (Collection<String>)this.noCachePatterns.stream().map(Pattern::toString).collect(Collectors.toList());
/*     */   }
/*     */   
/*     */   public void clearAllCacheFilters() {
/*  91 */     this.cachePatterns.clear();
/*  92 */     this.noCachePatterns.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCacheFilters(Collection<String> cacheFilters) {
/* 102 */     if (!this.noCachePatterns.isEmpty()) {
/* 103 */       throw new IllegalStateException("cannot set both cacheFilters and noCacheFilters");
/*     */     }
/* 105 */     this.cachePatterns.clear();
/* 106 */     if (null != cacheFilters) {
/* 107 */       cacheFilters.stream().map(Pattern::compile).forEach(this.cachePatterns::add);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNoCacheFilters(Collection<String> noCacheFilters) {
/* 118 */     if (!this.cachePatterns.isEmpty()) {
/* 119 */       throw new IllegalStateException("cannot set both cacheFilters and noCacheFilters");
/*     */     }
/* 121 */     this.noCachePatterns.clear();
/* 122 */     if (null != noCacheFilters) {
/* 123 */       noCacheFilters.stream().map(Pattern::compile).forEach(this.noCachePatterns::add);
/*     */     }
/*     */   }
/*     */   
/*     */   public Path getCacheDirectory() {
/* 128 */     return this.cacheDirectory;
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
/*     */   public void setCacheDirectory(Path cacheDirectory) {
/* 142 */     Path dir = Objects.<Path>requireNonNull(cacheDirectory);
/* 143 */     if (!Files.isDirectory(dir, new java.nio.file.LinkOption[0]) || !Files.isWritable(dir)) {
/* 144 */       throw new IllegalArgumentException("cacheDirectory: " + dir);
/*     */     }
/* 146 */     this.cacheDirectory = dir;
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
/*     */   public void setCacheDirectory(String cacheDirectory) {
/* 160 */     setCacheDirectory(FileSystems.getDefault().getPath(Objects.<String>requireNonNull(cacheDirectory), new String[0]));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean urlShouldBeCached(URL u) {
/* 171 */     String urlString = u.toString();
/*     */     
/* 173 */     if (!this.noCachePatterns.isEmpty()) {
/* 174 */       return this.noCachePatterns.stream()
/* 175 */         .noneMatch(pattern -> pattern.matcher(urlString).matches());
/*     */     }
/*     */     
/* 178 */     if (!this.cachePatterns.isEmpty()) {
/* 179 */       return this.cachePatterns.stream()
/* 180 */         .anyMatch(pattern -> pattern.matcher(urlString).matches());
/*     */     }
/*     */     
/* 183 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isNotActive() {
/* 187 */     return !this.active;
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
/*     */   public void setActive(boolean active) {
/* 201 */     if (active && null == this.cacheDirectory) {
/* 202 */       throw new IllegalArgumentException("cannot setActive when no cacheDirectory is set");
/*     */     }
/* 204 */     if (active) {
/* 205 */       setupURLStreamHandlerFactory();
/*     */     }
/* 207 */     this.active = active;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setupURLStreamHandlerFactory() {
/* 217 */     if (!this.urlStreamHandlerFactoryIsInitialized) {
/*     */       String msg;
/*     */       try {
/* 220 */         URL.setURLStreamHandlerFactory(new CachingURLStreamHandlerFactory(this));
/* 221 */         this.urlStreamHandlerFactoryIsInitialized = true;
/*     */         return;
/* 223 */       } catch (Error e) {
/* 224 */         msg = "cannot setup URLStreamFactoryHandler, it is already set in this application. " + e.getMessage();
/* 225 */         if (logger.isTraceEnabled()) {
/* 226 */           logger.error(msg);
/*     */         }
/* 228 */       } catch (SecurityException e) {
/* 229 */         msg = "cannot setup URLStreamFactoryHandler. " + e.getMessage();
/* 230 */         if (logger.isTraceEnabled()) {
/* 231 */           logger.error(msg);
/*     */         }
/*     */       } 
/* 234 */       throw new IllegalStateException(msg);
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
/*     */   boolean isCached(URL url) {
/*     */     try {
/* 247 */       Path cacheFile = filenameForURL(url);
/* 248 */       return (Files.exists(cacheFile, new java.nio.file.LinkOption[0]) && Files.isReadable(cacheFile) && Files.size(cacheFile) > 0L);
/* 249 */     } catch (IOException e) {
/* 250 */       if (logger.isTraceEnabled()) {
/* 251 */         logger.warn(e.getMessage());
/*     */       }
/*     */       
/* 254 */       return false;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Path filenameForURL(URL url) throws UnsupportedEncodingException {
/* 271 */     if (null == this.cacheDirectory) {
/* 272 */       throw new IllegalStateException("cannot resolve filename for url");
/*     */     }
/* 274 */     String mappedString = Objects.<String>requireNonNull(doMappings(url.toExternalForm()));
/* 275 */     String encodedString = URLEncoder.encode(mappedString, "UTF-8");
/* 276 */     return this.cacheDirectory.resolve(encodedString);
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
/*     */   private String doMappings(String urlString) {
/* 288 */     if (null == urlString || urlString.isEmpty()) {
/* 289 */       return urlString;
/*     */     }
/*     */     
/* 292 */     return urlString.replaceAll("[a-z]\\.tile\\.openstreetmap\\.org", "x.tile.openstreetmap.org");
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
/*     */   void saveCachedDataInfo(Path cacheFile, CachedDataInfo cachedDataInfo) {
/* 304 */     Path cacheDataFile = Paths.get(cacheFile + ".dataInfo", new String[0]);
/* 305 */     try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(cacheDataFile.toFile()))) {
/* 306 */       oos.writeObject(cachedDataInfo);
/* 307 */       oos.flush();
/* 308 */       logger.trace("saving dataInfo " + cachedDataInfo);
/* 309 */       logger.trace("saved dataInfo to " + cacheDataFile);
/* 310 */     } catch (Exception e) {
/* 311 */       logger.warn("could not save dataInfo " + cacheDataFile);
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
/*     */   CachedDataInfo readCachedDataInfo(Path cacheFile) {
/* 323 */     CachedDataInfo cachedDataInfo = null;
/* 324 */     Path cacheDataFile = Paths.get(cacheFile + ".dataInfo", new String[0]);
/* 325 */     if (Files.exists(cacheDataFile, new java.nio.file.LinkOption[0])) {
/* 326 */       try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(cacheDataFile.toFile()))) {
/* 327 */         cachedDataInfo = (CachedDataInfo)ois.readObject();
/* 328 */       } catch (Exception e) {
/* 329 */         logger.warn("could not read dataInfo from " + e.getMessage());
/*     */       } 
/*     */     }
/* 332 */     return cachedDataInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() throws IOException {
/* 340 */     if (null != this.cacheDirectory) {
/* 341 */       clearDirectory(this.cacheDirectory);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void preloadURLs(Collection<String> urls) {
/* 351 */     preloadURLs(urls, 0);
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
/*     */   public void preloadURLs(Collection<String> urls, int parallelism) {
/* 365 */     if (urls == null || isNotActive()) {
/*     */       return;
/*     */     }
/*     */     
/* 369 */     ForkJoinPool customThreadPool = new ForkJoinPool((parallelism < 1) ? Runtime.getRuntime().availableProcessors() : parallelism);
/*     */     try {
/* 371 */       customThreadPool.submit(() -> urls.parallelStream().forEach(()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/*     */     finally {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 386 */       customThreadPool.shutdown();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static class DeletingFileVisitor
/*     */     extends SimpleFileVisitor<Path>
/*     */   {
/*     */     private final Path rootDir;
/*     */ 
/*     */     
/*     */     public DeletingFileVisitor(Path path) {
/* 398 */       this.rootDir = path;
/*     */     }
/*     */ 
/*     */     
/*     */     public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
/* 403 */       if (!attrs.isDirectory()) {
/* 404 */         Files.delete(file);
/*     */       }
/* 406 */       return FileVisitResult.CONTINUE;
/*     */     }
/*     */ 
/*     */     
/*     */     public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
/* 411 */       if (!dir.equals(this.rootDir)) {
/* 412 */         Files.delete(dir);
/*     */       }
/* 414 */       return FileVisitResult.CONTINUE;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\com\sothawo\mapjfx\cache\OfflineCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */