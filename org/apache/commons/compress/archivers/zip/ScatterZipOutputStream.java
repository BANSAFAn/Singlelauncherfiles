/*     */ package org.apache.commons.compress.archivers.zip;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Iterator;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import org.apache.commons.compress.parallel.FileBasedScatterGatherBackingStore;
/*     */ import org.apache.commons.compress.parallel.ScatterGatherBackingStore;
/*     */ import org.apache.commons.compress.utils.BoundedInputStream;
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
/*     */ public class ScatterZipOutputStream
/*     */   implements Closeable
/*     */ {
/*  52 */   private final Queue<CompressedEntry> items = new ConcurrentLinkedQueue<>();
/*     */   private final ScatterGatherBackingStore backingStore;
/*     */   private final StreamCompressor streamCompressor;
/*  55 */   private final AtomicBoolean isClosed = new AtomicBoolean();
/*     */   private ZipEntryWriter zipEntryWriter;
/*     */   
/*     */   private static class CompressedEntry {
/*     */     final ZipArchiveEntryRequest zipArchiveEntryRequest;
/*     */     final long crc;
/*     */     final long compressedSize;
/*     */     final long size;
/*     */     
/*     */     public CompressedEntry(ZipArchiveEntryRequest zipArchiveEntryRequest, long crc, long compressedSize, long size) {
/*  65 */       this.zipArchiveEntryRequest = zipArchiveEntryRequest;
/*  66 */       this.crc = crc;
/*  67 */       this.compressedSize = compressedSize;
/*  68 */       this.size = size;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ZipArchiveEntry transferToArchiveEntry() {
/*  78 */       ZipArchiveEntry entry = this.zipArchiveEntryRequest.getZipArchiveEntry();
/*  79 */       entry.setCompressedSize(this.compressedSize);
/*  80 */       entry.setSize(this.size);
/*  81 */       entry.setCrc(this.crc);
/*  82 */       entry.setMethod(this.zipArchiveEntryRequest.getMethod());
/*  83 */       return entry;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public ScatterZipOutputStream(ScatterGatherBackingStore backingStore, StreamCompressor streamCompressor) {
/*  89 */     this.backingStore = backingStore;
/*  90 */     this.streamCompressor = streamCompressor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addArchiveEntry(ZipArchiveEntryRequest zipArchiveEntryRequest) throws IOException {
/* 100 */     try (InputStream payloadStream = zipArchiveEntryRequest.getPayloadStream()) {
/* 101 */       this.streamCompressor.deflate(payloadStream, zipArchiveEntryRequest.getMethod());
/*     */     } 
/* 103 */     this.items.add(new CompressedEntry(zipArchiveEntryRequest, this.streamCompressor.getCrc32(), this.streamCompressor
/* 104 */           .getBytesWrittenForLastEntry(), this.streamCompressor.getBytesRead()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeTo(ZipArchiveOutputStream target) throws IOException {
/* 115 */     this.backingStore.closeForWriting();
/* 116 */     try (InputStream data = this.backingStore.getInputStream()) {
/* 117 */       for (CompressedEntry compressedEntry : this.items) {
/* 118 */         try (BoundedInputStream rawStream = new BoundedInputStream(data, compressedEntry.compressedSize)) {
/*     */           
/* 120 */           target.addRawArchiveEntry(compressedEntry.transferToArchiveEntry(), (InputStream)rawStream);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static class ZipEntryWriter implements Closeable {
/*     */     private final Iterator<ScatterZipOutputStream.CompressedEntry> itemsIterator;
/*     */     private final InputStream itemsIteratorData;
/*     */     
/*     */     public ZipEntryWriter(ScatterZipOutputStream scatter) throws IOException {
/* 131 */       scatter.backingStore.closeForWriting();
/* 132 */       this.itemsIterator = scatter.items.iterator();
/* 133 */       this.itemsIteratorData = scatter.backingStore.getInputStream();
/*     */     }
/*     */ 
/*     */     
/*     */     public void close() throws IOException {
/* 138 */       if (this.itemsIteratorData != null) {
/* 139 */         this.itemsIteratorData.close();
/*     */       }
/*     */     }
/*     */     
/*     */     public void writeNextZipEntry(ZipArchiveOutputStream target) throws IOException {
/* 144 */       ScatterZipOutputStream.CompressedEntry compressedEntry = this.itemsIterator.next();
/* 145 */       try (BoundedInputStream rawStream = new BoundedInputStream(this.itemsIteratorData, compressedEntry.compressedSize)) {
/* 146 */         target.addRawArchiveEntry(compressedEntry.transferToArchiveEntry(), (InputStream)rawStream);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ZipEntryWriter zipEntryWriter() throws IOException {
/* 157 */     if (this.zipEntryWriter == null) {
/* 158 */       this.zipEntryWriter = new ZipEntryWriter(this);
/*     */     }
/* 160 */     return this.zipEntryWriter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 169 */     if (!this.isClosed.compareAndSet(false, true)) {
/*     */       return;
/*     */     }
/*     */     try {
/* 173 */       if (this.zipEntryWriter != null) {
/* 174 */         this.zipEntryWriter.close();
/*     */       }
/* 176 */       this.backingStore.close();
/*     */     } finally {
/* 178 */       this.streamCompressor.close();
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
/*     */   public static ScatterZipOutputStream fileBased(File file) throws FileNotFoundException {
/* 190 */     return pathBased(file.toPath(), -1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ScatterZipOutputStream pathBased(Path path) throws FileNotFoundException {
/* 201 */     return pathBased(path, -1);
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
/*     */   public static ScatterZipOutputStream fileBased(File file, int compressionLevel) throws FileNotFoundException {
/* 213 */     return pathBased(file.toPath(), compressionLevel);
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
/*     */   public static ScatterZipOutputStream pathBased(Path path, int compressionLevel) throws FileNotFoundException {
/* 225 */     FileBasedScatterGatherBackingStore fileBasedScatterGatherBackingStore = new FileBasedScatterGatherBackingStore(path);
/*     */     
/* 227 */     StreamCompressor sc = StreamCompressor.create(compressionLevel, (ScatterGatherBackingStore)fileBasedScatterGatherBackingStore);
/* 228 */     return new ScatterZipOutputStream((ScatterGatherBackingStore)fileBasedScatterGatherBackingStore, sc);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\archivers\zip\ScatterZipOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */