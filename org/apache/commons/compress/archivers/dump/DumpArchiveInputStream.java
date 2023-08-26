/*     */ package org.apache.commons.compress.archivers.dump;
/*     */ 
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.PriorityQueue;
/*     */ import java.util.Queue;
/*     */ import java.util.Stack;
/*     */ import org.apache.commons.compress.archivers.ArchiveEntry;
/*     */ import org.apache.commons.compress.archivers.ArchiveException;
/*     */ import org.apache.commons.compress.archivers.ArchiveInputStream;
/*     */ import org.apache.commons.compress.archivers.zip.ZipEncoding;
/*     */ import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
/*     */ import org.apache.commons.compress.utils.IOUtils;
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
/*     */ public class DumpArchiveInputStream
/*     */   extends ArchiveInputStream
/*     */ {
/*     */   private final DumpArchiveSummary summary;
/*     */   private DumpArchiveEntry active;
/*     */   private boolean isClosed;
/*     */   private boolean hasHitEOF;
/*     */   private long entrySize;
/*     */   private long entryOffset;
/*     */   private int readIdx;
/*  58 */   private final byte[] readBuf = new byte[1024];
/*     */   
/*     */   private byte[] blockBuffer;
/*     */   
/*     */   private int recordOffset;
/*     */   private long filepos;
/*     */   protected TapeInputStream raw;
/*  65 */   private final Map<Integer, Dirent> names = new HashMap<>();
/*     */ 
/*     */   
/*  68 */   private final Map<Integer, DumpArchiveEntry> pending = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Queue<DumpArchiveEntry> queue;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final ZipEncoding zipEncoding;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final String encoding;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DumpArchiveInputStream(InputStream is) throws ArchiveException {
/*  89 */     this(is, null);
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
/*     */   public DumpArchiveInputStream(InputStream is, String encoding) throws ArchiveException {
/* 103 */     this.raw = new TapeInputStream(is);
/* 104 */     this.hasHitEOF = false;
/* 105 */     this.encoding = encoding;
/* 106 */     this.zipEncoding = ZipEncodingHelper.getZipEncoding(encoding);
/*     */ 
/*     */     
/*     */     try {
/* 110 */       byte[] headerBytes = this.raw.readRecord();
/*     */       
/* 112 */       if (!DumpArchiveUtil.verify(headerBytes)) {
/* 113 */         throw new UnrecognizedFormatException();
/*     */       }
/*     */ 
/*     */       
/* 117 */       this.summary = new DumpArchiveSummary(headerBytes, this.zipEncoding);
/*     */ 
/*     */       
/* 120 */       this.raw.resetBlockSize(this.summary.getNTRec(), this.summary.isCompressed());
/*     */ 
/*     */       
/* 123 */       this.blockBuffer = new byte[4096];
/*     */ 
/*     */       
/* 126 */       readCLRI();
/* 127 */       readBITS();
/* 128 */     } catch (IOException ex) {
/* 129 */       throw new ArchiveException(ex.getMessage(), ex);
/*     */     } 
/*     */ 
/*     */     
/* 133 */     Dirent root = new Dirent(2, 2, 4, ".");
/* 134 */     this.names.put(Integer.valueOf(2), root);
/*     */ 
/*     */ 
/*     */     
/* 138 */     this.queue = new PriorityQueue<>(10, (p, q) -> 
/*     */         
/* 140 */         (p.getOriginalName() == null || q.getOriginalName() == null) ? Integer.MAX_VALUE : p.getOriginalName().compareTo(q.getOriginalName()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public int getCount() {
/* 151 */     return (int)getBytesRead();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getBytesRead() {
/* 156 */     return this.raw.getBytesRead();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DumpArchiveSummary getSummary() {
/* 164 */     return this.summary;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readCLRI() throws IOException {
/* 171 */     byte[] buffer = this.raw.readRecord();
/*     */     
/* 173 */     if (!DumpArchiveUtil.verify(buffer)) {
/* 174 */       throw new InvalidFormatException();
/*     */     }
/*     */     
/* 177 */     this.active = DumpArchiveEntry.parse(buffer);
/*     */     
/* 179 */     if (DumpArchiveConstants.SEGMENT_TYPE.CLRI != this.active.getHeaderType()) {
/* 180 */       throw new InvalidFormatException();
/*     */     }
/*     */ 
/*     */     
/* 184 */     if (this.raw.skip(1024L * this.active.getHeaderCount()) == -1L)
/*     */     {
/* 186 */       throw new EOFException();
/*     */     }
/* 188 */     this.readIdx = this.active.getHeaderCount();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readBITS() throws IOException {
/* 195 */     byte[] buffer = this.raw.readRecord();
/*     */     
/* 197 */     if (!DumpArchiveUtil.verify(buffer)) {
/* 198 */       throw new InvalidFormatException();
/*     */     }
/*     */     
/* 201 */     this.active = DumpArchiveEntry.parse(buffer);
/*     */     
/* 203 */     if (DumpArchiveConstants.SEGMENT_TYPE.BITS != this.active.getHeaderType()) {
/* 204 */       throw new InvalidFormatException();
/*     */     }
/*     */ 
/*     */     
/* 208 */     if (this.raw.skip(1024L * this.active.getHeaderCount()) == -1L)
/*     */     {
/* 210 */       throw new EOFException();
/*     */     }
/* 212 */     this.readIdx = this.active.getHeaderCount();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DumpArchiveEntry getNextDumpEntry() throws IOException {
/* 221 */     return getNextEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public DumpArchiveEntry getNextEntry() throws IOException {
/* 226 */     DumpArchiveEntry entry = null;
/* 227 */     String path = null;
/*     */ 
/*     */     
/* 230 */     if (!this.queue.isEmpty()) {
/* 231 */       return this.queue.remove();
/*     */     }
/*     */     
/* 234 */     while (entry == null) {
/* 235 */       if (this.hasHitEOF) {
/* 236 */         return null;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 243 */       while (this.readIdx < this.active.getHeaderCount()) {
/* 244 */         if (!this.active.isSparseRecord(this.readIdx++) && this.raw
/* 245 */           .skip(1024L) == -1L) {
/* 246 */           throw new EOFException();
/*     */         }
/*     */       } 
/*     */       
/* 250 */       this.readIdx = 0;
/* 251 */       this.filepos = this.raw.getBytesRead();
/*     */       
/* 253 */       byte[] headerBytes = this.raw.readRecord();
/*     */       
/* 255 */       if (!DumpArchiveUtil.verify(headerBytes)) {
/* 256 */         throw new InvalidFormatException();
/*     */       }
/*     */       
/* 259 */       this.active = DumpArchiveEntry.parse(headerBytes);
/*     */ 
/*     */       
/* 262 */       while (DumpArchiveConstants.SEGMENT_TYPE.ADDR == this.active.getHeaderType()) {
/* 263 */         if (this.raw.skip(1024L * (this.active
/* 264 */             .getHeaderCount() - this.active
/* 265 */             .getHeaderHoles())) == -1L) {
/* 266 */           throw new EOFException();
/*     */         }
/*     */         
/* 269 */         this.filepos = this.raw.getBytesRead();
/* 270 */         headerBytes = this.raw.readRecord();
/*     */         
/* 272 */         if (!DumpArchiveUtil.verify(headerBytes)) {
/* 273 */           throw new InvalidFormatException();
/*     */         }
/*     */         
/* 276 */         this.active = DumpArchiveEntry.parse(headerBytes);
/*     */       } 
/*     */ 
/*     */       
/* 280 */       if (DumpArchiveConstants.SEGMENT_TYPE.END == this.active.getHeaderType()) {
/* 281 */         this.hasHitEOF = true;
/*     */         
/* 283 */         return null;
/*     */       } 
/*     */       
/* 286 */       entry = this.active;
/*     */       
/* 288 */       if (entry.isDirectory()) {
/* 289 */         readDirectoryEntry(this.active);
/*     */ 
/*     */         
/* 292 */         this.entryOffset = 0L;
/* 293 */         this.entrySize = 0L;
/* 294 */         this.readIdx = this.active.getHeaderCount();
/*     */       } else {
/* 296 */         this.entryOffset = 0L;
/* 297 */         this.entrySize = this.active.getEntrySize();
/* 298 */         this.readIdx = 0;
/*     */       } 
/*     */       
/* 301 */       this.recordOffset = this.readBuf.length;
/*     */       
/* 303 */       path = getPath(entry);
/*     */       
/* 305 */       if (path == null) {
/* 306 */         entry = null;
/*     */       }
/*     */     } 
/*     */     
/* 310 */     entry.setName(path);
/* 311 */     entry.setSimpleName(((Dirent)this.names.get(Integer.valueOf(entry.getIno()))).getName());
/* 312 */     entry.setOffset(this.filepos);
/*     */     
/* 314 */     return entry;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readDirectoryEntry(DumpArchiveEntry entry) throws IOException {
/* 322 */     long size = entry.getEntrySize();
/* 323 */     boolean first = true;
/*     */     
/* 325 */     while (first || DumpArchiveConstants.SEGMENT_TYPE.ADDR == entry
/* 326 */       .getHeaderType()) {
/*     */       
/* 328 */       if (!first) {
/* 329 */         this.raw.readRecord();
/*     */       }
/*     */       
/* 332 */       if (!this.names.containsKey(Integer.valueOf(entry.getIno())) && DumpArchiveConstants.SEGMENT_TYPE.INODE == entry
/* 333 */         .getHeaderType()) {
/* 334 */         this.pending.put(Integer.valueOf(entry.getIno()), entry);
/*     */       }
/*     */       
/* 337 */       int datalen = 1024 * entry.getHeaderCount();
/*     */       
/* 339 */       if (this.blockBuffer.length < datalen) {
/* 340 */         this.blockBuffer = IOUtils.readRange(this.raw, datalen);
/* 341 */         if (this.blockBuffer.length != datalen) {
/* 342 */           throw new EOFException();
/*     */         }
/* 344 */       } else if (this.raw.read(this.blockBuffer, 0, datalen) != datalen) {
/* 345 */         throw new EOFException();
/*     */       } 
/*     */       
/* 348 */       int reclen = 0;
/*     */       int i;
/* 350 */       for (i = 0; i < datalen - 8 && i < size - 8L; 
/* 351 */         i += reclen) {
/* 352 */         int ino = DumpArchiveUtil.convert32(this.blockBuffer, i);
/* 353 */         reclen = DumpArchiveUtil.convert16(this.blockBuffer, i + 4);
/*     */         
/* 355 */         byte type = this.blockBuffer[i + 6];
/*     */         
/* 357 */         String name = DumpArchiveUtil.decode(this.zipEncoding, this.blockBuffer, i + 8, this.blockBuffer[i + 7]);
/*     */         
/* 359 */         if (!".".equals(name) && !"..".equals(name)) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 364 */           Dirent d = new Dirent(ino, entry.getIno(), type, name);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 373 */           this.names.put(Integer.valueOf(ino), d);
/*     */ 
/*     */           
/* 376 */           this.pending.forEach((k, v) -> {
/*     */                 String path = getPath(v);
/*     */                 
/*     */                 if (path != null) {
/*     */                   v.setName(path);
/*     */                   
/*     */                   v.setSimpleName(((Dirent)this.names.get(k)).getName());
/*     */                   
/*     */                   this.queue.add(v);
/*     */                 } 
/*     */               });
/*     */           
/* 388 */           this.queue.forEach(e -> (DumpArchiveEntry)this.pending.remove(Integer.valueOf(e.getIno())));
/*     */         } 
/*     */       } 
/* 391 */       byte[] peekBytes = this.raw.peek();
/*     */       
/* 393 */       if (!DumpArchiveUtil.verify(peekBytes)) {
/* 394 */         throw new InvalidFormatException();
/*     */       }
/*     */       
/* 397 */       entry = DumpArchiveEntry.parse(peekBytes);
/* 398 */       first = false;
/* 399 */       size -= 1024L;
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
/*     */   private String getPath(DumpArchiveEntry entry) {
/* 412 */     Stack<String> elements = new Stack<>();
/* 413 */     Dirent dirent = null;
/*     */     int i;
/* 415 */     for (i = entry.getIno();; i = dirent.getParentIno()) {
/* 416 */       if (!this.names.containsKey(Integer.valueOf(i))) {
/* 417 */         elements.clear();
/*     */         
/*     */         break;
/*     */       } 
/* 421 */       dirent = this.names.get(Integer.valueOf(i));
/* 422 */       elements.push(dirent.getName());
/*     */       
/* 424 */       if (dirent.getIno() == dirent.getParentIno()) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 430 */     if (elements.isEmpty()) {
/* 431 */       this.pending.put(Integer.valueOf(entry.getIno()), entry);
/*     */       
/* 433 */       return null;
/*     */     } 
/*     */ 
/*     */     
/* 437 */     StringBuilder sb = new StringBuilder(elements.pop());
/*     */     
/* 439 */     while (!elements.isEmpty()) {
/* 440 */       sb.append('/');
/* 441 */       sb.append(elements.pop());
/*     */     } 
/*     */     
/* 444 */     return sb.toString();
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
/*     */   public int read(byte[] buf, int off, int len) throws IOException {
/* 462 */     if (len == 0) {
/* 463 */       return 0;
/*     */     }
/* 465 */     int totalRead = 0;
/*     */     
/* 467 */     if (this.hasHitEOF || this.isClosed || this.entryOffset >= this.entrySize) {
/* 468 */       return -1;
/*     */     }
/*     */     
/* 471 */     if (this.active == null) {
/* 472 */       throw new IllegalStateException("No current dump entry");
/*     */     }
/*     */     
/* 475 */     if (len + this.entryOffset > this.entrySize) {
/* 476 */       len = (int)(this.entrySize - this.entryOffset);
/*     */     }
/*     */     
/* 479 */     while (len > 0) {
/* 480 */       int sz = Math.min(len, this.readBuf.length - this.recordOffset);
/*     */ 
/*     */       
/* 483 */       if (this.recordOffset + sz <= this.readBuf.length) {
/* 484 */         System.arraycopy(this.readBuf, this.recordOffset, buf, off, sz);
/* 485 */         totalRead += sz;
/* 486 */         this.recordOffset += sz;
/* 487 */         len -= sz;
/* 488 */         off += sz;
/*     */       } 
/*     */ 
/*     */       
/* 492 */       if (len > 0) {
/* 493 */         if (this.readIdx >= 512) {
/* 494 */           byte[] headerBytes = this.raw.readRecord();
/*     */           
/* 496 */           if (!DumpArchiveUtil.verify(headerBytes)) {
/* 497 */             throw new InvalidFormatException();
/*     */           }
/*     */           
/* 500 */           this.active = DumpArchiveEntry.parse(headerBytes);
/* 501 */           this.readIdx = 0;
/*     */         } 
/*     */         
/* 504 */         if (!this.active.isSparseRecord(this.readIdx++)) {
/* 505 */           int r = this.raw.read(this.readBuf, 0, this.readBuf.length);
/* 506 */           if (r != this.readBuf.length) {
/* 507 */             throw new EOFException();
/*     */           }
/*     */         } else {
/* 510 */           Arrays.fill(this.readBuf, (byte)0);
/*     */         } 
/*     */         
/* 513 */         this.recordOffset = 0;
/*     */       } 
/*     */     } 
/*     */     
/* 517 */     this.entryOffset += totalRead;
/*     */     
/* 519 */     return totalRead;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 527 */     if (!this.isClosed) {
/* 528 */       this.isClosed = true;
/* 529 */       this.raw.close();
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
/*     */   public static boolean matches(byte[] buffer, int length) {
/* 543 */     if (length < 32) {
/* 544 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 548 */     if (length >= 1024) {
/* 549 */       return DumpArchiveUtil.verify(buffer);
/*     */     }
/*     */ 
/*     */     
/* 553 */     return (60012 == DumpArchiveUtil.convert32(buffer, 24));
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\archivers\dump\DumpArchiveInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */