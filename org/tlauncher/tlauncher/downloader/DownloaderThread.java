/*     */ package org.tlauncher.tlauncher.downloader;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.URLEncoder;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.Files;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.client.config.RequestConfig;
/*     */ import org.apache.http.client.methods.CloseableHttpResponse;
/*     */ import org.apache.http.client.methods.HttpGet;
/*     */ import org.apache.http.client.methods.HttpUriRequest;
/*     */ import org.apache.http.util.EntityUtils;
/*     */ import org.tlauncher.tlauncher.repository.ClientInstanceRepo;
/*     */ import org.tlauncher.tlauncher.repository.Repo;
/*     */ import org.tlauncher.util.FileUtil;
/*     */ import org.tlauncher.util.U;
/*     */ import org.tlauncher.util.async.ExtendedThread;
/*     */ 
/*     */ 
/*     */ public class DownloaderThread
/*     */   extends ExtendedThread
/*     */ {
/*     */   private static final String ITERATION_BLOCK = "iteration";
/*     */   private final int ID;
/*     */   private final String LOGGER_PREFIX;
/*     */   private final Downloader downloader;
/*     */   private final List<Downloadable> list;
/*     */   private Downloadable current;
/*     */   private boolean launched;
/*     */   private boolean initDownloadedLink;
/*  40 */   private String incorrectUrl = "";
/*     */   private boolean usedProxy;
/*     */   
/*     */   DownloaderThread(Downloader d, int id) {
/*  44 */     super("DT#" + id);
/*     */     
/*  46 */     this.ID = id;
/*  47 */     this.LOGGER_PREFIX = "[D#" + id + "]";
/*     */     
/*  49 */     this.downloader = d;
/*  50 */     this.list = new ArrayList<>();
/*     */     
/*  52 */     startAndWait();
/*     */   }
/*     */   
/*     */   int getID() {
/*  56 */     return this.ID;
/*     */   }
/*     */   
/*     */   void add(Downloadable d) {
/*  60 */     this.list.add(d);
/*     */   }
/*     */   
/*     */   void startDownload() {
/*  64 */     this.launched = true;
/*  65 */     unlockThread("iteration");
/*     */   }
/*     */   
/*     */   void stopDownload() {
/*  69 */     this.launched = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void run() {
/*     */     while (true) {
/*  75 */       this.launched = true;
/*  76 */       for (Downloadable d : this.list) {
/*  77 */         this.current = d;
/*  78 */         onStart();
/*  79 */         if (this.usedProxy || (System.currentTimeMillis() - this.downloader.getFirstVisitTime() > 60000L && this.downloader
/*  80 */           .getSpeed() < 0.15D)) {
/*  81 */           this.usedProxy = true;
/*  82 */           d.getRepository().reorderedRepoSetFirstProxy();
/*     */         } 
/*  84 */         int attempt = 0;
/*  85 */         Throwable error = null;
/*  86 */         this.initDownloadedLink = false; int max;
/*  87 */         while (attempt < (max = this.downloader.getConfiguration().getTries(d.isFast()))) {
/*  88 */           attempt++;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  94 */           int timeout = attempt * this.downloader.getConfiguration().getTimeout();
/*     */           
/*     */           try {
/*  97 */             download(timeout);
/*     */             break;
/*  99 */           } catch (GaveUpDownloadException e) {
/* 100 */             dlog("File is not reachable at all.");
/* 101 */             error = e;
/* 102 */           } catch (AbortedDownloadException e) {
/* 103 */             dlog("This download process has been aborted.");
/* 104 */             error = e;
/*     */             
/*     */             break;
/*     */           } 
/* 108 */           if (attempt < max) {
/*     */             continue;
/*     */           }
/* 111 */           FileUtil.deleteFile(d.getMetadataDTO().getLocalDestination());
/*     */           
/* 113 */           for (File file : d.getAdditionalDestinations()) {
/* 114 */             FileUtil.deleteFile(file);
/*     */           }
/* 116 */           dlog("Gave up trying to download this file.", error);
/*     */           
/* 118 */           onError(error);
/*     */         } 
/*     */         
/* 121 */         if (error instanceof AbortedDownloadException) {
/* 122 */           tlog(new Object[] { "Thread is aborting..." });
/*     */           
/* 124 */           for (Downloadable downloadable : this.list) {
/* 125 */             downloadable.onAbort((AbortedDownloadException)error);
/*     */           }
/*     */           break;
/*     */         } 
/*     */       } 
/* 130 */       this.list.clear();
/* 131 */       lockThread("iteration");
/* 132 */       this.launched = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void download(int timeout) throws GaveUpDownloadException, AbortedDownloadException {
/* 137 */     boolean hasRepo = this.current.hasRepository();
/* 138 */     int attempt = 0, max = hasRepo ? this.current.getRepository().getCount() : 1;
/*     */     
/* 140 */     IOException cause = null;
/* 141 */     while (attempt < max) {
/*     */       
/* 143 */       String url = "";
/*     */       try {
/* 145 */         if (hasRepo)
/* 146 */         { Repo r = this.current.getRepository();
/* 147 */           if (r.equals(ClientInstanceRepo.EMPTY_REPO) && attempt == 1) {
/*     */             
/* 149 */             url = this.current.getRepository().getRepo(attempt) + URLEncoder.encode(this.current.getMetadataDTO().getUrl(), StandardCharsets.UTF_8.name());
/*     */           } else {
/* 151 */             url = this.current.getRepository().getRepo(attempt) + this.current.getMetadataDTO().getUrl();
/*     */           }  }
/* 153 */         else { url = this.current.getMetadataDTO().getUrl(); }
/*     */         
/* 155 */         downloadURL(url, timeout);
/* 156 */         dlog("done " + url);
/*     */         return;
/* 158 */       } catch (IOException e) {
/* 159 */         dlog("Failed to download from: ", url, e);
/* 160 */         if (!this.initDownloadedLink) {
/* 161 */           this.incorrectUrl = url;
/* 162 */           this.initDownloadedLink = true;
/*     */         } 
/* 164 */         cause = e;
/*     */         
/* 166 */         attempt++;
/*     */       } 
/* 168 */     }  throw new GaveUpDownloadException(this.incorrectUrl + " -> " + this.current.getMetadataDTO().getLocalDestination(), cause);
/*     */   }
/*     */ 
/*     */   
/*     */   private void downloadURL(String url, int timeout) throws IOException, AbortedDownloadException {
/* 173 */     if (!this.launched)
/* 174 */       throw new AbortedDownloadException(); 
/* 175 */     InputStream in = null;
/* 176 */     OutputStream out = null;
/* 177 */     File file = this.current.getMetadataDTO().getLocalDestination();
/* 178 */     File temp = FileUtil.makeTemp(new File(file.getAbsolutePath() + ".tlauncherdownload"));
/* 179 */     HttpGet g = new HttpGet(url);
/*     */     
/*     */     try {
/* 182 */       RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(timeout).setSocketTimeout(timeout).build();
/* 183 */       g.setConfig(requestConfig);
/* 184 */       CloseableHttpResponse r = this.downloader.getClient().execute((HttpUriRequest)g);
/* 185 */       HttpEntity entity = r.getEntity();
/* 186 */       if (r.getStatusLine().getStatusCode() != 200) {
/* 187 */         EntityUtils.consume(entity);
/* 188 */         throw new IOException(String.valueOf(r.getStatusLine().getStatusCode()));
/*     */       } 
/* 190 */       in = new BufferedInputStream(entity.getContent());
/* 191 */       if (this.current.getMetadataDTO().getSize() == 0L)
/* 192 */         this.current.getMetadataDTO().setSize(entity.getContentLength()); 
/* 193 */       this.current.setAlreadyDownloaded(0L);
/*     */       
/* 195 */       out = new BufferedOutputStream(new FileOutputStream(temp));
/*     */       
/* 197 */       long downloaded_s = System.currentTimeMillis(), speed_s = downloaded_s;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 206 */       byte[] buffer = new byte[65536];
/*     */       
/* 208 */       int curread = in.read(buffer);
/* 209 */       while (curread != -1) {
/* 210 */         if (!this.launched) {
/* 211 */           throw new AbortedDownloadException();
/*     */         }
/* 213 */         out.write(buffer, 0, curread);
/* 214 */         curread = in.read(buffer);
/* 215 */         if (curread == -1)
/*     */           break; 
/* 217 */         this.current.setAlreadyDownloaded(this.current.getAlreadyDownloaded() + curread);
/*     */         
/* 219 */         long speed_e = System.currentTimeMillis() - speed_s;
/* 220 */         if (speed_e < 300L)
/*     */           continue; 
/* 222 */         speed_s = System.currentTimeMillis();
/* 223 */         this.downloader.onProgress();
/*     */       } 
/*     */     } finally {
/* 226 */       g.abort();
/* 227 */       IOUtils.closeQuietly(in);
/* 228 */       IOUtils.closeQuietly(out);
/*     */     } 
/* 230 */     Files.deleteIfExists(file.toPath());
/* 231 */     Files.move(temp.toPath(), file.toPath(), new java.nio.file.CopyOption[0]);
/* 232 */     List<File> copies = this.current.getAdditionalDestinations();
/* 233 */     if (copies.size() > 0) {
/* 234 */       dlog("Found additional destinations. Copying...");
/* 235 */       for (File copy : copies) {
/* 236 */         dlog("Copying " + copy + "...");
/*     */         
/* 238 */         FileUtil.copyFile(file, copy, this.current.isForce());
/*     */         
/* 240 */         dlog("Success!");
/*     */       } 
/* 242 */       dlog("Copying completed.");
/*     */     } 
/* 244 */     onComplete();
/*     */   }
/*     */   
/*     */   private void onStart() {
/* 248 */     this.current.onStart();
/*     */   }
/*     */   
/*     */   private void onError(Throwable e) {
/* 252 */     this.current.onError(e);
/* 253 */     this.downloader.onFileComplete(this.current);
/*     */   }
/*     */   
/*     */   private void onComplete() throws RetryDownloadException {
/* 257 */     this.current.onComplete();
/* 258 */     this.downloader.onProgress();
/* 259 */     this.downloader.onFileComplete(this.current);
/*     */   }
/*     */   
/*     */   private void tlog(Object... o) {
/* 263 */     U.plog(new Object[] { this.LOGGER_PREFIX, o });
/*     */   }
/*     */   
/*     */   private void dlog(String message, String url, Throwable ex) {
/* 267 */     if (ex == null) {
/* 268 */       U.plog(new Object[] { this.LOGGER_PREFIX, "> ", message, url });
/*     */     } else {
/* 270 */       U.plog(new Object[] { this.LOGGER_PREFIX, "> ", message, url, ex });
/*     */     } 
/*     */   }
/*     */   private void dlog(String message) {
/* 274 */     dlog(message, "", (Throwable)null);
/*     */   }
/*     */   
/*     */   private void dlog(String message, Throwable ex) {
/* 278 */     dlog(message, "", ex);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\downloader\DownloaderThread.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */