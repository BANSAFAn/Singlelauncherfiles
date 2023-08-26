/*     */ package by.gdev.http.download.impl;
/*     */ 
/*     */ import by.gdev.http.download.exeption.StatusExeption;
/*     */ import by.gdev.http.download.service.Downloader;
/*     */ import by.gdev.http.upload.download.downloader.DownloadElement;
/*     */ import by.gdev.http.upload.download.downloader.DownloaderContainer;
/*     */ import by.gdev.http.upload.download.downloader.DownloaderStatus;
/*     */ import by.gdev.http.upload.download.downloader.DownloaderStatusEnum;
/*     */ import by.gdev.util.model.download.Metadata;
/*     */ import com.google.common.eventbus.EventBus;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.time.Duration;
/*     */ import java.time.LocalTime;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import org.apache.http.client.config.RequestConfig;
/*     */ import org.apache.http.impl.client.CloseableHttpClient;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DownloaderImpl
/*     */   implements Downloader
/*     */ {
/*     */   private String pathToDownload;
/*     */   private EventBus eventBus;
/*  40 */   private static final Logger log = LoggerFactory.getLogger(DownloaderImpl.class); private CloseableHttpClient httpclient; private RequestConfig requestConfig; public void setPathToDownload(String pathToDownload) {
/*  41 */     this.pathToDownload = pathToDownload; } public void setEventBus(EventBus eventBus) { this.eventBus = eventBus; } public void setHttpclient(CloseableHttpClient httpclient) { this.httpclient = httpclient; } public void setRequestConfig(RequestConfig requestConfig) { this.requestConfig = requestConfig; } public void setDownloadElements(Queue<DownloadElement> downloadElements) { this.downloadElements = downloadElements; } public void setProcessedElements(List<DownloadElement> processedElements) { this.processedElements = processedElements; } public void setAllConteinerSize(List<Long> allConteinerSize) { this.allConteinerSize = allConteinerSize; } public void setStatus(DownloaderStatusEnum status) { this.status = status; } public void setRunnable(DownloadRunnableImpl runnable) { this.runnable = runnable; } public void setAllCountElement(Integer allCountElement) { this.allCountElement = allCountElement; } public void setFullDownloadSize(long fullDownloadSize) { this.fullDownloadSize = fullDownloadSize; } public void setDownloadBytesNow1(long downloadBytesNow1) { this.downloadBytesNow1 = downloadBytesNow1; } public void setStart(LocalTime start) { this.start = start; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof DownloaderImpl)) return false;  DownloaderImpl other = (DownloaderImpl)o; if (!other.canEqual(this)) return false;  Object this$pathToDownload = getPathToDownload(), other$pathToDownload = other.getPathToDownload(); if ((this$pathToDownload == null) ? (other$pathToDownload != null) : !this$pathToDownload.equals(other$pathToDownload)) return false;  Object this$eventBus = getEventBus(), other$eventBus = other.getEventBus(); if ((this$eventBus == null) ? (other$eventBus != null) : !this$eventBus.equals(other$eventBus)) return false;  Object this$httpclient = getHttpclient(), other$httpclient = other.getHttpclient(); if ((this$httpclient == null) ? (other$httpclient != null) : !this$httpclient.equals(other$httpclient)) return false;  Object this$requestConfig = getRequestConfig(), other$requestConfig = other.getRequestConfig(); if ((this$requestConfig == null) ? (other$requestConfig != null) : !this$requestConfig.equals(other$requestConfig)) return false;  Object<DownloadElement> this$downloadElements = (Object<DownloadElement>)getDownloadElements(), other$downloadElements = (Object<DownloadElement>)other.getDownloadElements(); if ((this$downloadElements == null) ? (other$downloadElements != null) : !this$downloadElements.equals(other$downloadElements)) return false;  Object<DownloadElement> this$processedElements = (Object<DownloadElement>)getProcessedElements(), other$processedElements = (Object<DownloadElement>)other.getProcessedElements(); if ((this$processedElements == null) ? (other$processedElements != null) : !this$processedElements.equals(other$processedElements)) return false;  Object<Long> this$allConteinerSize = (Object<Long>)getAllConteinerSize(), other$allConteinerSize = (Object<Long>)other.getAllConteinerSize(); if ((this$allConteinerSize == null) ? (other$allConteinerSize != null) : !this$allConteinerSize.equals(other$allConteinerSize)) return false;  Object this$status = getStatus(), other$status = other.getStatus(); if ((this$status == null) ? (other$status != null) : !this$status.equals(other$status)) return false;  Object this$runnable = getRunnable(), other$runnable = other.getRunnable(); if ((this$runnable == null) ? (other$runnable != null) : !this$runnable.equals(other$runnable)) return false;  Object this$allCountElement = getAllCountElement(), other$allCountElement = other.getAllCountElement(); if ((this$allCountElement == null) ? (other$allCountElement != null) : !this$allCountElement.equals(other$allCountElement)) return false;  if (getFullDownloadSize() != other.getFullDownloadSize()) return false;  if (getDownloadBytesNow1() != other.getDownloadBytesNow1()) return false;  Object this$start = getStart(), other$start = other.getStart(); return !((this$start == null) ? (other$start != null) : !this$start.equals(other$start)); } protected boolean canEqual(Object other) { return other instanceof DownloaderImpl; } public int hashCode() { int PRIME = 59; result = 1; Object $pathToDownload = getPathToDownload(); result = result * 59 + (($pathToDownload == null) ? 43 : $pathToDownload.hashCode()); Object $eventBus = getEventBus(); result = result * 59 + (($eventBus == null) ? 43 : $eventBus.hashCode()); Object $httpclient = getHttpclient(); result = result * 59 + (($httpclient == null) ? 43 : $httpclient.hashCode()); Object $requestConfig = getRequestConfig(); result = result * 59 + (($requestConfig == null) ? 43 : $requestConfig.hashCode()); Object<DownloadElement> $downloadElements = (Object<DownloadElement>)getDownloadElements(); result = result * 59 + (($downloadElements == null) ? 43 : $downloadElements.hashCode()); Object<DownloadElement> $processedElements = (Object<DownloadElement>)getProcessedElements(); result = result * 59 + (($processedElements == null) ? 43 : $processedElements.hashCode()); Object<Long> $allConteinerSize = (Object<Long>)getAllConteinerSize(); result = result * 59 + (($allConteinerSize == null) ? 43 : $allConteinerSize.hashCode()); Object $status = getStatus(); result = result * 59 + (($status == null) ? 43 : $status.hashCode()); Object $runnable = getRunnable(); result = result * 59 + (($runnable == null) ? 43 : $runnable.hashCode()); Object $allCountElement = getAllCountElement(); result = result * 59 + (($allCountElement == null) ? 43 : $allCountElement.hashCode()); long $fullDownloadSize = getFullDownloadSize(); result = result * 59 + (int)($fullDownloadSize >>> 32L ^ $fullDownloadSize); long $downloadBytesNow1 = getDownloadBytesNow1(); result = result * 59 + (int)($downloadBytesNow1 >>> 32L ^ $downloadBytesNow1); Object $start = getStart(); return result * 59 + (($start == null) ? 43 : $start.hashCode()); } public String toString() { return "DownloaderImpl(pathToDownload=" + getPathToDownload() + ", eventBus=" + getEventBus() + ", httpclient=" + getHttpclient() + ", requestConfig=" + getRequestConfig() + ", downloadElements=" + getDownloadElements() + ", processedElements=" + getProcessedElements() + ", allConteinerSize=" + getAllConteinerSize() + ", status=" + getStatus() + ", runnable=" + getRunnable() + ", allCountElement=" + getAllCountElement() + ", fullDownloadSize=" + getFullDownloadSize() + ", downloadBytesNow1=" + getDownloadBytesNow1() + ", start=" + getStart() + ")"; } public DownloaderImpl(String pathToDownload, EventBus eventBus, CloseableHttpClient httpclient, RequestConfig requestConfig, Queue<DownloadElement> downloadElements, List<DownloadElement> processedElements, List<Long> allConteinerSize, DownloaderStatusEnum status, DownloadRunnableImpl runnable, Integer allCountElement, long fullDownloadSize, long downloadBytesNow1, LocalTime start) {
/*  42 */     this.pathToDownload = pathToDownload; this.eventBus = eventBus; this.httpclient = httpclient; this.requestConfig = requestConfig; this.downloadElements = downloadElements; this.processedElements = processedElements; this.allConteinerSize = allConteinerSize; this.status = status; this.runnable = runnable; this.allCountElement = allCountElement; this.fullDownloadSize = fullDownloadSize; this.downloadBytesNow1 = downloadBytesNow1; this.start = start;
/*     */   }
/*     */   
/*     */   public String getPathToDownload()
/*     */   {
/*  47 */     return this.pathToDownload;
/*  48 */   } public EventBus getEventBus() { return this.eventBus; }
/*  49 */   public CloseableHttpClient getHttpclient() { return this.httpclient; } public RequestConfig getRequestConfig() {
/*  50 */     return this.requestConfig;
/*     */   }
/*     */ 
/*     */   
/*  54 */   private Queue<DownloadElement> downloadElements = new ConcurrentLinkedQueue<>(); public Queue<DownloadElement> getDownloadElements() { return this.downloadElements; }
/*     */ 
/*     */ 
/*     */   
/*  58 */   private List<DownloadElement> processedElements = Collections.synchronizedList(new ArrayList<>()); public List<DownloadElement> getProcessedElements() { return this.processedElements; }
/*     */   
/*     */   private volatile DownloaderStatusEnum status; private DownloadRunnableImpl runnable; private volatile Integer allCountElement; private long fullDownloadSize;
/*     */   private long downloadBytesNow1;
/*  62 */   private List<Long> allConteinerSize = new ArrayList<>(); private LocalTime start; public List<Long> getAllConteinerSize() { return this.allConteinerSize; }
/*  63 */   public DownloaderStatusEnum getStatus() { return this.status; }
/*  64 */   public DownloadRunnableImpl getRunnable() { return this.runnable; }
/*  65 */   public Integer getAllCountElement() { return this.allCountElement; }
/*  66 */   public long getFullDownloadSize() { return this.fullDownloadSize; }
/*  67 */   public long getDownloadBytesNow1() { return this.downloadBytesNow1; } public LocalTime getStart() {
/*  68 */     return this.start;
/*     */   }
/*     */   public DownloaderImpl(EventBus eventBus, CloseableHttpClient httpclient, RequestConfig requestConfig) {
/*  71 */     this.eventBus = eventBus;
/*  72 */     this.httpclient = httpclient;
/*  73 */     this.requestConfig = requestConfig;
/*  74 */     this.status = DownloaderStatusEnum.IDLE;
/*  75 */     this.runnable = new DownloadRunnableImpl(this.downloadElements, this.processedElements, httpclient, requestConfig, eventBus);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addContainer(DownloaderContainer container) {
/*  80 */     if (Objects.nonNull(container.getRepo().getResources())) {
/*  81 */       container.getRepo().getResources().forEach(metadata -> {
/*     */             DownloadElement element = new DownloadElement();
/*     */             element.setMetadata(metadata);
/*     */             element.setRepo(container.getRepo());
/*     */             element.setPathToDownload(container.getDestinationRepositories());
/*     */             element.setHandlers(container.getHandlers());
/*     */             this.downloadElements.add(element);
/*     */           });
/*     */     }
/*  90 */     this.pathToDownload = container.getDestinationRepositories();
/*  91 */     this.allConteinerSize.add(Long.valueOf(container.getContainerSize()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void startDownload(boolean sync) throws InterruptedException, ExecutionException, StatusExeption, IOException {
/*  96 */     this.fullDownloadSize = totalDownloadSize(this.allConteinerSize);
/*  97 */     this.start = LocalTime.now();
/*  98 */     if (this.status.equals(DownloaderStatusEnum.IDLE)) {
/*  99 */       this.status = DownloaderStatusEnum.WORK;
/* 100 */       this.runnable.setStatus(this.status);
/* 101 */       this.allCountElement = Integer.valueOf(this.downloadElements.size());
/* 102 */       List<CompletableFuture<Void>> listThread = new ArrayList<>();
/* 103 */       for (int i = 0; i < 3; i++)
/* 104 */         listThread.add(CompletableFuture.runAsync(this.runnable)); 
/* 105 */       if (sync) {
/* 106 */         waitThreadDone(listThread);
/*     */       } else {
/*     */         
/* 109 */         CompletableFuture.runAsync(() -> {
/*     */               try {
/*     */                 waitThreadDone(listThread);
/* 112 */               } catch (IOException|InterruptedException e) {
/*     */                 log.error("Error", e);
/*     */               } 
/* 115 */             }).get();
/*     */       } 
/*     */     } else {
/* 118 */       throw new StatusExeption(this.status.toString());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void cancelDownload() {
/* 126 */     this.status = DownloaderStatusEnum.CANCEL;
/* 127 */     this.runnable.setStatus(DownloaderStatusEnum.CANCEL);
/*     */   }
/*     */   
/*     */   private DownloaderStatus buildDownloaderStatus() throws IOException {
/* 131 */     DownloaderStatus statusDownload = new DownloaderStatus();
/* 132 */     long downloadBytesNow = 0L;
/* 133 */     List<DownloadElement> list = new ArrayList<>(this.processedElements);
/* 134 */     List<Throwable> errorList = new ArrayList<>();
/* 135 */     double thirty = Duration.between(this.start, LocalTime.now()).getSeconds();
/* 136 */     for (DownloadElement elem : list) {
/* 137 */       downloadBytesNow += elem.getDownloadBytes();
/* 138 */       if (Objects.nonNull(elem.getError()))
/* 139 */         errorList.add(elem.getError()); 
/*     */     } 
/* 141 */     statusDownload.setThrowables(errorList);
/* 142 */     statusDownload.setDownloadSize(sizeDownloadNow());
/* 143 */     statusDownload.setSpeed((downloadBytesNow / 1048576L) / thirty);
/* 144 */     statusDownload.setDownloaderStatusEnum(this.status);
/* 145 */     statusDownload.setAllDownloadSize(this.fullDownloadSize);
/* 146 */     statusDownload.setLeftFiles(Integer.valueOf(this.processedElements.size()));
/* 147 */     statusDownload.setAllFiles(this.allCountElement);
/* 148 */     return statusDownload;
/*     */   }
/*     */   
/*     */   private void waitThreadDone(List<CompletableFuture<Void>> listThread) throws InterruptedException, IOException {
/* 152 */     LocalTime start = LocalTime.now();
/* 153 */     boolean workedAnyThread = true;
/* 154 */     while (workedAnyThread) {
/* 155 */       workedAnyThread = false;
/* 156 */       Thread.sleep(50L);
/* 157 */       workedAnyThread = listThread.stream().anyMatch(e -> !e.isDone());
/* 158 */       if (start.isBefore(LocalTime.now())) {
/* 159 */         start = start.plusSeconds(1L);
/* 160 */         if (this.allCountElement.intValue() != 0 && 
/* 161 */           start.getSecond() != start.plusSeconds(1L).getSecond()) {
/* 162 */           this.eventBus.post(buildDownloaderStatus());
/*     */         }
/*     */       } 
/*     */     } 
/* 166 */     this.status = DownloaderStatusEnum.DONE;
/* 167 */     this.eventBus.post(buildDownloaderStatus());
/*     */   }
/*     */   
/*     */   private long totalDownloadSize(List<Long> containerSize) {
/* 171 */     long sum = 0L;
/* 172 */     for (Iterator<Long> iterator = containerSize.iterator(); iterator.hasNext(); ) { long l = ((Long)iterator.next()).longValue();
/* 173 */       sum += l; }
/* 174 */      return sum;
/*     */   }
/*     */   
/*     */   private long sizeDownloadNow() throws IOException {
/* 178 */     return Files.walk(Paths.get(this.pathToDownload, new String[0]), new java.nio.file.FileVisitOption[0]).filter(p -> p.toFile().isFile()).mapToLong(p -> p.toFile().length()).sum();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\by\gdev\http\download\impl\DownloaderImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */