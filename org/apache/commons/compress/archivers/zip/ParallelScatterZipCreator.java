/*     */ package org.apache.commons.compress.archivers.zip;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.UncheckedIOException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.Deque;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ConcurrentLinkedDeque;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import org.apache.commons.compress.parallel.FileBasedScatterGatherBackingStore;
/*     */ import org.apache.commons.compress.parallel.InputStreamSupplier;
/*     */ import org.apache.commons.compress.parallel.ScatterGatherBackingStore;
/*     */ import org.apache.commons.compress.parallel.ScatterGatherBackingStoreSupplier;
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
/*     */ public class ParallelScatterZipCreator
/*     */ {
/*  56 */   private final Deque<ScatterZipOutputStream> streams = new ConcurrentLinkedDeque<>();
/*     */   private final ExecutorService es;
/*     */   private final ScatterGatherBackingStoreSupplier backingStoreSupplier;
/*  59 */   private final Deque<Future<? extends ScatterZipOutputStream>> futures = new ConcurrentLinkedDeque<>();
/*     */   
/*  61 */   private final long startedAt = System.currentTimeMillis();
/*     */   private long compressionDoneAt;
/*     */   private long scatterDoneAt;
/*     */   private final int compressionLevel;
/*     */   
/*     */   private static class DefaultBackingStoreSupplier implements ScatterGatherBackingStoreSupplier {
/*  67 */     final AtomicInteger storeNum = new AtomicInteger(0);
/*     */ 
/*     */     
/*     */     public ScatterGatherBackingStore get() throws IOException {
/*  71 */       Path tempFile = Files.createTempFile("parallelscatter", "n" + this.storeNum.incrementAndGet(), (FileAttribute<?>[])new FileAttribute[0]);
/*  72 */       return (ScatterGatherBackingStore)new FileBasedScatterGatherBackingStore(tempFile);
/*     */     }
/*     */     
/*     */     private DefaultBackingStoreSupplier() {} }
/*     */   
/*     */   private ScatterZipOutputStream createDeferred(ScatterGatherBackingStoreSupplier scatterGatherBackingStoreSupplier) throws IOException {
/*  78 */     ScatterGatherBackingStore bs = scatterGatherBackingStoreSupplier.get();
/*     */     
/*  80 */     StreamCompressor sc = StreamCompressor.create(this.compressionLevel, bs);
/*  81 */     return new ScatterZipOutputStream(bs, sc);
/*     */   }
/*     */   
/*  84 */   private final ThreadLocal<ScatterZipOutputStream> tlScatterStreams = new ThreadLocal<ScatterZipOutputStream>()
/*     */     {
/*     */       protected ScatterZipOutputStream initialValue() {
/*     */         try {
/*  88 */           ScatterZipOutputStream scatterStream = ParallelScatterZipCreator.this.createDeferred(ParallelScatterZipCreator.this.backingStoreSupplier);
/*  89 */           ParallelScatterZipCreator.this.streams.add(scatterStream);
/*  90 */           return scatterStream;
/*  91 */         } catch (IOException e) {
/*  92 */           throw new UncheckedIOException(e);
/*     */         } 
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParallelScatterZipCreator() {
/* 102 */     this(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParallelScatterZipCreator(ExecutorService executorService) {
/* 112 */     this(executorService, new DefaultBackingStoreSupplier(null));
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
/*     */   public ParallelScatterZipCreator(ExecutorService executorService, ScatterGatherBackingStoreSupplier backingStoreSupplier) {
/* 124 */     this(executorService, backingStoreSupplier, -1);
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
/*     */   public ParallelScatterZipCreator(ExecutorService executorService, ScatterGatherBackingStoreSupplier backingStoreSupplier, int compressionLevel) throws IllegalArgumentException {
/* 141 */     if ((compressionLevel < 0 || compressionLevel > 9) && compressionLevel != -1)
/*     */     {
/* 143 */       throw new IllegalArgumentException("Compression level is expected between -1~9");
/*     */     }
/*     */     
/* 146 */     this.backingStoreSupplier = backingStoreSupplier;
/* 147 */     this.es = executorService;
/* 148 */     this.compressionLevel = compressionLevel;
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
/*     */   public void addArchiveEntry(ZipArchiveEntry zipArchiveEntry, InputStreamSupplier source) {
/* 162 */     submitStreamAwareCallable(createCallable(zipArchiveEntry, source));
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
/*     */   public void addArchiveEntry(ZipArchiveEntryRequestSupplier zipArchiveEntryRequestSupplier) {
/* 175 */     submitStreamAwareCallable(createCallable(zipArchiveEntryRequestSupplier));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void submit(Callable<? extends Object> callable) {
/* 186 */     submitStreamAwareCallable(() -> {
/*     */           callable.call();
/*     */           return this.tlScatterStreams.get();
/*     */         });
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
/*     */   public final void submitStreamAwareCallable(Callable<? extends ScatterZipOutputStream> callable) {
/* 201 */     this.futures.add(this.es.submit(callable));
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
/*     */ 
/*     */   
/*     */   public final Callable<ScatterZipOutputStream> createCallable(ZipArchiveEntry zipArchiveEntry, InputStreamSupplier source) {
/* 224 */     int method = zipArchiveEntry.getMethod();
/* 225 */     if (method == -1) {
/* 226 */       throw new IllegalArgumentException("Method must be set on zipArchiveEntry: " + zipArchiveEntry);
/*     */     }
/* 228 */     ZipArchiveEntryRequest zipArchiveEntryRequest = ZipArchiveEntryRequest.createZipArchiveEntryRequest(zipArchiveEntry, source);
/* 229 */     return () -> {
/*     */         ScatterZipOutputStream scatterStream = this.tlScatterStreams.get();
/*     */         scatterStream.addArchiveEntry(zipArchiveEntryRequest);
/*     */         return scatterStream;
/*     */       };
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
/*     */   public final Callable<ScatterZipOutputStream> createCallable(ZipArchiveEntryRequestSupplier zipArchiveEntryRequestSupplier) {
/* 253 */     return () -> {
/*     */         ScatterZipOutputStream scatterStream = this.tlScatterStreams.get();
/*     */         scatterStream.addArchiveEntry(zipArchiveEntryRequestSupplier.get());
/*     */         return scatterStream;
/*     */       };
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
/*     */ 
/*     */   
/*     */   public void writeTo(ZipArchiveOutputStream targetStream) throws IOException, InterruptedException, ExecutionException {
/*     */     try {
/*     */       try {
/* 282 */         for (Future<?> future : this.futures) {
/* 283 */           future.get();
/*     */         }
/*     */       } finally {
/* 286 */         this.es.shutdown();
/*     */       } 
/*     */       
/* 289 */       this.es.awaitTermination(60000L, TimeUnit.SECONDS);
/*     */ 
/*     */       
/* 292 */       this.compressionDoneAt = System.currentTimeMillis();
/*     */       
/* 294 */       for (Future<? extends ScatterZipOutputStream> future : this.futures) {
/* 295 */         ScatterZipOutputStream scatterStream = future.get();
/* 296 */         scatterStream.zipEntryWriter().writeNextZipEntry(targetStream);
/*     */       } 
/*     */       
/* 299 */       for (ScatterZipOutputStream scatterStream : this.streams) {
/* 300 */         scatterStream.close();
/*     */       }
/*     */       
/* 303 */       this.scatterDoneAt = System.currentTimeMillis();
/*     */     } finally {
/* 305 */       closeAll();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ScatterStatistics getStatisticsMessage() {
/* 315 */     return new ScatterStatistics(this.compressionDoneAt - this.startedAt, this.scatterDoneAt - this.compressionDoneAt);
/*     */   }
/*     */   
/*     */   private void closeAll() {
/* 319 */     for (ScatterZipOutputStream scatterStream : this.streams) {
/*     */       try {
/* 321 */         scatterStream.close();
/* 322 */       } catch (IOException iOException) {}
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\archivers\zip\ParallelScatterZipCreator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */