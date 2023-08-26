/*     */ package by.gdev.http.download.impl;
/*     */ 
/*     */ import by.gdev.http.download.exeption.UploadFileException;
/*     */ import by.gdev.http.download.handler.PostHandler;
/*     */ import by.gdev.http.upload.download.downloader.DownloadElement;
/*     */ import by.gdev.http.upload.download.downloader.DownloaderStatusEnum;
/*     */ import com.google.common.eventbus.EventBus;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Queue;
/*     */ import org.apache.http.client.config.RequestConfig;
/*     */ import org.apache.http.client.methods.HttpGet;
/*     */ import org.apache.http.impl.client.CloseableHttpClient;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public class DownloadRunnableImpl
/*     */   implements Runnable {
/*     */   private volatile DownloaderStatusEnum status;
/*     */   private Queue<DownloadElement> downloadElements;
/*     */   private List<DownloadElement> processedElements;
/*     */   private CloseableHttpClient httpclient;
/*     */   private RequestConfig requestConfig;
/*  29 */   private static final Logger log = LoggerFactory.getLogger(DownloadRunnableImpl.class); private EventBus eventBus; public void setStatus(DownloaderStatusEnum status) {
/*  30 */     this.status = status; } public void setDownloadElements(Queue<DownloadElement> downloadElements) { this.downloadElements = downloadElements; } public void setProcessedElements(List<DownloadElement> processedElements) { this.processedElements = processedElements; } public void setHttpclient(CloseableHttpClient httpclient) { this.httpclient = httpclient; } public void setRequestConfig(RequestConfig requestConfig) { this.requestConfig = requestConfig; } public void setEventBus(EventBus eventBus) { this.eventBus = eventBus; } public void setDEFAULT_MAX_ATTEMPTS(int DEFAULT_MAX_ATTEMPTS) { this.DEFAULT_MAX_ATTEMPTS = DEFAULT_MAX_ATTEMPTS; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof DownloadRunnableImpl)) return false;  DownloadRunnableImpl other = (DownloadRunnableImpl)o; if (!other.canEqual(this)) return false;  Object this$status = getStatus(), other$status = other.getStatus(); if ((this$status == null) ? (other$status != null) : !this$status.equals(other$status)) return false;  Object<DownloadElement> this$downloadElements = (Object<DownloadElement>)getDownloadElements(), other$downloadElements = (Object<DownloadElement>)other.getDownloadElements(); if ((this$downloadElements == null) ? (other$downloadElements != null) : !this$downloadElements.equals(other$downloadElements)) return false;  Object<DownloadElement> this$processedElements = (Object<DownloadElement>)getProcessedElements(), other$processedElements = (Object<DownloadElement>)other.getProcessedElements(); if ((this$processedElements == null) ? (other$processedElements != null) : !this$processedElements.equals(other$processedElements)) return false;  Object this$httpclient = getHttpclient(), other$httpclient = other.getHttpclient(); if ((this$httpclient == null) ? (other$httpclient != null) : !this$httpclient.equals(other$httpclient)) return false;  Object this$requestConfig = getRequestConfig(), other$requestConfig = other.getRequestConfig(); if ((this$requestConfig == null) ? (other$requestConfig != null) : !this$requestConfig.equals(other$requestConfig)) return false;  Object this$eventBus = getEventBus(), other$eventBus = other.getEventBus(); return ((this$eventBus == null) ? (other$eventBus != null) : !this$eventBus.equals(other$eventBus)) ? false : (!(getDEFAULT_MAX_ATTEMPTS() != other.getDEFAULT_MAX_ATTEMPTS())); } protected boolean canEqual(Object other) { return other instanceof DownloadRunnableImpl; } public int hashCode() { int PRIME = 59; result = 1; Object $status = getStatus(); result = result * 59 + (($status == null) ? 43 : $status.hashCode()); Object<DownloadElement> $downloadElements = (Object<DownloadElement>)getDownloadElements(); result = result * 59 + (($downloadElements == null) ? 43 : $downloadElements.hashCode()); Object<DownloadElement> $processedElements = (Object<DownloadElement>)getProcessedElements(); result = result * 59 + (($processedElements == null) ? 43 : $processedElements.hashCode()); Object $httpclient = getHttpclient(); result = result * 59 + (($httpclient == null) ? 43 : $httpclient.hashCode()); Object $requestConfig = getRequestConfig(); result = result * 59 + (($requestConfig == null) ? 43 : $requestConfig.hashCode()); Object $eventBus = getEventBus(); result = result * 59 + (($eventBus == null) ? 43 : $eventBus.hashCode()); return result * 59 + getDEFAULT_MAX_ATTEMPTS(); } public String toString() { return "DownloadRunnableImpl(status=" + getStatus() + ", downloadElements=" + getDownloadElements() + ", processedElements=" + getProcessedElements() + ", httpclient=" + getHttpclient() + ", requestConfig=" + getRequestConfig() + ", eventBus=" + getEventBus() + ", DEFAULT_MAX_ATTEMPTS=" + getDEFAULT_MAX_ATTEMPTS() + ")"; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DownloaderStatusEnum getStatus() {
/*  36 */     return this.status;
/*  37 */   } public Queue<DownloadElement> getDownloadElements() { return this.downloadElements; }
/*  38 */   public List<DownloadElement> getProcessedElements() { return this.processedElements; }
/*  39 */   public CloseableHttpClient getHttpclient() { return this.httpclient; }
/*  40 */   public RequestConfig getRequestConfig() { return this.requestConfig; } public EventBus getEventBus() {
/*  41 */     return this.eventBus;
/*  42 */   } private int DEFAULT_MAX_ATTEMPTS = 3; public int getDEFAULT_MAX_ATTEMPTS() { return this.DEFAULT_MAX_ATTEMPTS; }
/*     */   
/*     */   public DownloadRunnableImpl(Queue<DownloadElement> downloadElements, List<DownloadElement> processedElements, CloseableHttpClient httpclient, RequestConfig requestConfig, EventBus eventBus) {
/*  45 */     this.downloadElements = downloadElements;
/*  46 */     this.processedElements = processedElements;
/*  47 */     this.httpclient = httpclient;
/*  48 */     this.requestConfig = requestConfig;
/*  49 */     this.eventBus = eventBus;
/*     */   }
/*     */ 
/*     */   
/*     */   public void run() {
/*  54 */     while (this.status.equals(DownloaderStatusEnum.WORK) && 
/*  55 */       !this.status.equals(DownloaderStatusEnum.CANCEL)) {
/*     */ 
/*     */       
/*  58 */       DownloadElement element = this.downloadElements.poll();
/*  59 */       if (Objects.nonNull(element)) {
/*     */         try {
/*  61 */           download(element);
/*  62 */           element.getHandlers().forEach(post -> post.postProcessDownloadElement(element));
/*  63 */         } catch (Throwable e1) {
/*  64 */           element.setError((Throwable)new UploadFileException((String)element.getRepo().getRepositories().get(0) + element.getMetadata().getRelativeUrl(), element
/*  65 */                 .getMetadata().getPath(), e1.getLocalizedMessage()));
/*     */         } 
/*     */         continue;
/*     */       } 
/*     */       break;
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
/*     */   private void download(DownloadElement element) throws IOException, InterruptedException {
/*  83 */     this.processedElements.add(element);
/*  84 */     File file = new File(element.getPathToDownload() + element.getMetadata().getPath());
/*  85 */     for (int attempt = 0; attempt < this.DEFAULT_MAX_ATTEMPTS; attempt++) {
/*     */       try {
/*  87 */         HttpGet httpGet; BufferedInputStream in = null;
/*  88 */         BufferedOutputStream out = null;
/*  89 */         boolean resume = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }
/* 127 */       catch (SocketTimeoutException e) {
/* 128 */         if (attempt == this.DEFAULT_MAX_ATTEMPTS)
/* 129 */           throw new SocketTimeoutException(); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\by\gdev\http\download\impl\DownloadRunnableImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */