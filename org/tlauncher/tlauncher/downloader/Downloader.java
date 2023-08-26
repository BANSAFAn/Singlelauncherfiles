/*     */ package org.tlauncher.tlauncher.downloader;
/*     */ 
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import javax.net.ssl.HostnameVerifier;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.config.RegistryBuilder;
/*     */ import org.apache.http.conn.HttpClientConnectionManager;
/*     */ import org.apache.http.conn.socket.PlainConnectionSocketFactory;
/*     */ import org.apache.http.conn.ssl.NoopHostnameVerifier;
/*     */ import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
/*     */ import org.apache.http.impl.client.CloseableHttpClient;
/*     */ import org.apache.http.impl.client.HttpClientBuilder;
/*     */ import org.apache.http.impl.client.HttpClients;
/*     */ import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
/*     */ import org.apache.http.message.BasicHeaderElementIterator;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ import org.apache.http.ssl.SSLContextBuilder;
/*     */ import org.apache.http.util.Args;
/*     */ import org.tlauncher.tlauncher.configuration.enums.ConnectionQuality;
/*     */ import org.tlauncher.util.U;
/*     */ import org.tlauncher.util.async.ExtendedThread;
/*     */ 
/*     */ public class Downloader
/*     */   extends ExtendedThread {
/*     */   public static final int MAX_THREADS = 3;
/*     */   private static final String DOWNLOAD_BLOCK = "download";
/*     */   static final String ITERATION_BLOCK = "iteration";
/*     */   private final DownloaderThread[] threads;
/*     */   private final List<Downloadable> list;
/*     */   private final List<DownloaderListener> listeners;
/*     */   private final List<DownloadableContainer> downloadableContainers;
/*  40 */   private final AtomicInteger remainingObjects = new AtomicInteger(); private final Object workLock;
/*     */   private ConnectionQuality configuration;
/*     */   private int runningThreads;
/*     */   private int workingThreads;
/*     */   private final CloseableHttpClient client;
/*     */   private boolean aborted;
/*     */   private boolean haveWork;
/*     */   private double averageProgress;
/*     */   private double remainedData;
/*     */   private long firstVisitTime;
/*     */   private volatile double speed;
/*     */   
/*     */   public Downloader(ConnectionQuality configuration) {
/*  53 */     super("MD");
/*     */     
/*  55 */     setConfiguration(configuration);
/*     */     
/*  57 */     this.threads = new DownloaderThread[3];
/*  58 */     this.list = Collections.synchronizedList(new ArrayList<>());
/*  59 */     this.listeners = Collections.synchronizedList(new ArrayList<>());
/*  60 */     this.downloadableContainers = new ArrayList<>();
/*  61 */     this.workLock = new Object();
/*  62 */     PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
/*  63 */     cm.setDefaultMaxPerRoute(5);
/*  64 */     cm.setMaxTotal(20);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  75 */     HttpClientBuilder builder = HttpClients.custom().setKeepAliveStrategy((response, context) -> { Args.notNull(response, "HTTP response"); BasicHeaderElementIterator basicHeaderElementIterator = new BasicHeaderElementIterator(response.headerIterator("Keep-Alive")); if (basicHeaderElementIterator.hasNext()) { log(new Object[] { "used keep alive 5000" }); return 5000L; }  return -1L; }).setConnectionManager((HttpClientConnectionManager)cm).evictIdleConnections(10L, TimeUnit.SECONDS);
/*     */ 
/*     */     
/*     */     try {
/*  79 */       SSLContext sslContext = (new SSLContextBuilder()).loadTrustMaterial(null, (x509CertChain, authType) -> true).build();
/*  80 */       builder.setSSLContext(sslContext)
/*  81 */         .setConnectionManager((HttpClientConnectionManager)new PoolingHttpClientConnectionManager(
/*  82 */             RegistryBuilder.create().register("http", PlainConnectionSocketFactory.INSTANCE)
/*  83 */             .register("https", new SSLConnectionSocketFactory(sslContext, (HostnameVerifier)NoopHostnameVerifier.INSTANCE))
/*     */             
/*  85 */             .build()));
/*  86 */     } catch (Exception e) {
/*  87 */       U.log(new Object[] { e });
/*     */     } 
/*  89 */     this.client = builder.build();
/*  90 */     startAndWait();
/*     */   }
/*     */   
/*     */   private static void log(Object... o) {
/*  94 */     U.log(new Object[] { "[Downloader2]", o });
/*     */   }
/*     */   
/*     */   public ConnectionQuality getConfiguration() {
/*  98 */     return this.configuration;
/*     */   }
/*     */   
/*     */   public void setConfiguration(ConnectionQuality configuration) {
/* 102 */     if (configuration == null) {
/* 103 */       throw new NullPointerException();
/*     */     }
/* 105 */     log(new Object[] { "Loaded configuration:", configuration });
/* 106 */     this.configuration = configuration;
/*     */   }
/*     */   
/*     */   public int getRemaining() {
/* 110 */     return this.remainingObjects.get();
/*     */   }
/*     */   
/*     */   public double getProgress() {
/* 114 */     return this.averageProgress;
/*     */   }
/*     */   
/*     */   public double getSpeed() {
/* 118 */     return this.speed;
/*     */   }
/*     */   
/*     */   public void add(Downloadable d) {
/* 122 */     if (d == null) {
/* 123 */       throw new NullPointerException();
/*     */     }
/* 125 */     this.list.add(d);
/*     */   }
/*     */   
/*     */   public void add(DownloadableContainer c) {
/* 129 */     if (c == null)
/* 130 */       throw new NullPointerException(); 
/* 131 */     this.downloadableContainers.add(c);
/* 132 */     this.list.addAll(c.list);
/*     */   }
/*     */   
/*     */   public List<DownloadableContainer> getDownloadableContainers() {
/* 136 */     return this.downloadableContainers;
/*     */   }
/*     */   
/*     */   public void addAll(Downloadable... ds) {
/* 140 */     if (ds == null) {
/* 141 */       throw new NullPointerException();
/*     */     }
/* 143 */     for (int i = 0; i < ds.length; i++) {
/* 144 */       if (ds[i] == null) {
/* 145 */         throw new NullPointerException("Downloadable at " + i + " is NULL!");
/*     */       }
/* 147 */       this.list.add(ds[i]);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addAll(Collection<Downloadable> coll) {
/* 152 */     if (coll == null) {
/* 153 */       throw new NullPointerException();
/*     */     }
/* 155 */     int i = -1;
/*     */     
/* 157 */     for (Downloadable d : coll) {
/* 158 */       i++;
/*     */       
/* 160 */       if (d == null) {
/* 161 */         throw new NullPointerException("Downloadable at" + i + " is NULL!");
/*     */       }
/* 163 */       this.list.add(d);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addListener(DownloaderListener listener) {
/* 168 */     if (listener == null) {
/* 169 */       throw new NullPointerException();
/*     */     }
/* 171 */     this.listeners.add(listener);
/*     */   }
/*     */   
/*     */   public boolean startDownload() {
/* 175 */     boolean haveWork = !this.list.isEmpty();
/* 176 */     if (haveWork)
/* 177 */       unlockThread("iteration"); 
/* 178 */     return haveWork;
/*     */   }
/*     */   
/*     */   public void startDownloadAndWait() {
/* 182 */     if (startDownload())
/* 183 */       waitWork(); 
/*     */   }
/*     */   
/*     */   private void waitWork() {
/* 187 */     this.haveWork = true;
/*     */     
/* 189 */     while (this.haveWork) {
/* 190 */       synchronized (this.workLock) {
/*     */         try {
/* 192 */           this.workLock.wait();
/* 193 */         } catch (InterruptedException e) {
/* 194 */           e.printStackTrace();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void notifyWork() {
/* 201 */     this.haveWork = false;
/*     */     
/* 203 */     synchronized (this.workLock) {
/* 204 */       this.workLock.notifyAll();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void stopDownload() {
/* 209 */     if (!isThreadLocked())
/*     */       try {
/* 211 */         Thread.sleep(2000L);
/* 212 */       } catch (InterruptedException e) {
/* 213 */         e.printStackTrace();
/*     */       }  
/* 215 */     if (!isThreadLocked()) {
/* 216 */       throw new IllegalArgumentException();
/*     */     }
/* 218 */     for (int i = 0; i < this.runningThreads; i++) {
/* 219 */       this.threads[i].stopDownload();
/*     */     }
/* 221 */     this.aborted = true;
/*     */     
/* 223 */     if (isThreadLocked())
/* 224 */       tryUnlock("download"); 
/*     */   }
/*     */   
/*     */   public void stopDownloadAndWait() {
/* 228 */     stopDownload();
/* 229 */     waitForThreads();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/* 235 */     checkCurrent();
/*     */ 
/*     */ 
/*     */     
/*     */     while (true) {
/* 240 */       lockThread("iteration");
/*     */       
/* 242 */       log(new Object[] { "Files in queue", Integer.valueOf(this.list.size()) });
/*     */       
/* 244 */       synchronized (this.list) {
/* 245 */         sortOut();
/*     */       } 
/*     */       
/* 248 */       for (int i = 0; i < this.runningThreads; i++) {
/* 249 */         this.threads[i].startDownload();
/*     */       }
/* 251 */       lockThread("download");
/*     */       
/* 253 */       if (this.aborted) {
/* 254 */         waitForThreads();
/* 255 */         onAbort();
/*     */         
/* 257 */         this.aborted = false;
/*     */       } 
/*     */       
/* 260 */       notifyWork();
/*     */       
/* 262 */       this.averageProgress = 0.0D;
/* 263 */       this.workingThreads = 0;
/* 264 */       this.remainingObjects.set(0);
/* 265 */       this.list.clear();
/* 266 */       this.downloadableContainers.clear();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void sortOut() {
/* 271 */     int size = this.list.size();
/*     */     
/* 273 */     if (size == 0) {
/*     */       return;
/*     */     }
/* 276 */     int downloadablesAtThread = U.getMaxMultiply(size, 3), x = 0;
/*     */     
/* 278 */     log(new Object[] { "Starting download " + size + " files..." });
/* 279 */     onStart(size);
/*     */     
/* 281 */     int max = this.configuration.getMaxThreads();
/* 282 */     boolean[] workers = new boolean[max];
/*     */     
/* 284 */     this.firstVisitTime = System.currentTimeMillis();
/*     */     
/* 286 */     while (size > 0) {
/* 287 */       for (int i = 0; i < max; i++) {
/* 288 */         workers[i] = true;
/* 289 */         size -= downloadablesAtThread;
/*     */         
/* 291 */         if (this.threads[i] == null)
/* 292 */           this.threads[i] = new DownloaderThread(this, ++this.runningThreads); 
/*     */         int y;
/* 294 */         for (y = x; y < x + downloadablesAtThread; y++) {
/* 295 */           this.threads[i].add(this.list.get(y));
/*     */         }
/* 297 */         x = y;
/*     */         
/* 299 */         if (size == 0)
/*     */           break; 
/*     */       } 
/* 302 */       downloadablesAtThread = U.getMaxMultiply(size, 3);
/*     */     } 
/*     */     
/* 305 */     for (boolean worker : workers) {
/* 306 */       if (worker)
/* 307 */         this.workingThreads++; 
/*     */     } 
/*     */   }
/*     */   private void onStart(int size) {
/* 311 */     for (DownloaderListener listener : this.listeners) {
/* 312 */       listener.onDownloaderStart(this, size);
/*     */     }
/* 314 */     this.remainingObjects.addAndGet(size);
/*     */   }
/*     */   
/*     */   private void onAbort() {
/* 318 */     for (DownloaderListener listener : this.listeners)
/* 319 */       listener.onDownloaderAbort(this); 
/*     */   }
/*     */   
/*     */   void onProgress() {
/* 323 */     long downloaded = 0L;
/* 324 */     long totalFilesSize = 0L;
/*     */     
/* 326 */     for (Downloadable downloadable : this.list) {
/* 327 */       totalFilesSize += downloadable.getMetadataDTO().getSize();
/* 328 */       downloaded += downloadable.getAlreadyDownloaded();
/*     */     } 
/* 330 */     long currentTime = System.currentTimeMillis() - this.firstVisitTime;
/* 331 */     this.speed = downloaded / 1048576.0D * currentTime * 1000.0D;
/* 332 */     this.remainedData = (totalFilesSize - downloaded) / 1048576.0D;
/* 333 */     if (totalFilesSize != 0L)
/* 334 */       this.averageProgress = downloaded / totalFilesSize * 100.0D; 
/* 335 */     for (DownloaderListener listener : this.listeners)
/* 336 */       listener.onDownloaderProgress(this, this.averageProgress, this.speed, this.remainedData); 
/*     */   }
/*     */   
/*     */   void onFileComplete(Downloadable file) {
/* 340 */     int remaining = this.remainingObjects.decrementAndGet();
/*     */     
/* 342 */     for (DownloaderListener listener : this.listeners) {
/* 343 */       listener.onDownloaderFileComplete(this, file);
/*     */     }
/* 345 */     if (remaining < 1)
/* 346 */       onComplete(); 
/*     */   }
/*     */   
/*     */   private void onComplete() {
/* 350 */     for (DownloaderListener listener : this.listeners) {
/* 351 */       listener.onDownloaderComplete(this);
/*     */     }
/* 353 */     unlockThread("download");
/*     */   }
/*     */   private void waitForThreads() {
/*     */     boolean blocked;
/* 357 */     log(new Object[] { "Waiting for", Integer.valueOf(this.workingThreads), "threads..." });
/*     */ 
/*     */ 
/*     */     
/*     */     do {
/* 362 */       blocked = true;
/*     */       
/* 364 */       for (int i = 0; i < this.workingThreads; i++) {
/* 365 */         if (!this.threads[i].isThreadLocked())
/* 366 */           blocked = false; 
/*     */       } 
/* 368 */     } while (!blocked);
/*     */ 
/*     */ 
/*     */     
/* 372 */     log(new Object[] { "All threads are blocked by now" });
/*     */   }
/*     */   
/*     */   public CloseableHttpClient getClient() {
/* 376 */     return this.client;
/*     */   }
/*     */   
/*     */   public double getRemainedData() {
/* 380 */     return this.remainedData;
/*     */   }
/*     */   
/*     */   public long getFirstVisitTime() {
/* 384 */     return this.firstVisitTime;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\downloader\Downloader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */