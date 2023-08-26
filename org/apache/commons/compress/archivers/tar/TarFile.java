/*     */ package org.apache.commons.compress.archivers.tar;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.Closeable;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.SeekableByteChannel;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.compress.archivers.zip.ZipEncoding;
/*     */ import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
/*     */ import org.apache.commons.compress.utils.ArchiveUtils;
/*     */ import org.apache.commons.compress.utils.BoundedArchiveInputStream;
/*     */ import org.apache.commons.compress.utils.BoundedInputStream;
/*     */ import org.apache.commons.compress.utils.BoundedSeekableByteChannelInputStream;
/*     */ import org.apache.commons.compress.utils.SeekableInMemoryByteChannel;
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
/*     */ public class TarFile
/*     */   implements Closeable
/*     */ {
/*     */   private static final int SMALL_BUFFER_SIZE = 256;
/*  51 */   private final byte[] smallBuf = new byte[256];
/*     */ 
/*     */   
/*     */   private final SeekableByteChannel archive;
/*     */ 
/*     */   
/*     */   private final ZipEncoding zipEncoding;
/*     */ 
/*     */   
/*  60 */   private final LinkedList<TarArchiveEntry> entries = new LinkedList<>();
/*     */ 
/*     */   
/*     */   private final int blockSize;
/*     */   
/*     */   private final boolean lenient;
/*     */   
/*     */   private final int recordSize;
/*     */   
/*     */   private final ByteBuffer recordBuffer;
/*     */   
/*  71 */   private final List<TarArchiveStructSparse> globalSparseHeaders = new ArrayList<>();
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean hasHitEOF;
/*     */ 
/*     */   
/*     */   private TarArchiveEntry currEntry;
/*     */ 
/*     */   
/*  81 */   private Map<String, String> globalPaxHeaders = new HashMap<>();
/*     */   
/*  83 */   private final Map<String, List<InputStream>> sparseInputStreams = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TarFile(byte[] content) throws IOException {
/*  92 */     this((SeekableByteChannel)new SeekableInMemoryByteChannel(content));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TarFile(byte[] content, String encoding) throws IOException {
/* 103 */     this((SeekableByteChannel)new SeekableInMemoryByteChannel(content), 10240, 512, encoding, false);
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
/*     */   public TarFile(byte[] content, boolean lenient) throws IOException {
/* 116 */     this((SeekableByteChannel)new SeekableInMemoryByteChannel(content), 10240, 512, null, lenient);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TarFile(File archive) throws IOException {
/* 126 */     this(archive.toPath());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TarFile(File archive, String encoding) throws IOException {
/* 137 */     this(archive.toPath(), encoding);
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
/*     */   public TarFile(File archive, boolean lenient) throws IOException {
/* 150 */     this(archive.toPath(), lenient);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TarFile(Path archivePath) throws IOException {
/* 160 */     this(Files.newByteChannel(archivePath, new java.nio.file.OpenOption[0]), 10240, 512, null, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TarFile(Path archivePath, String encoding) throws IOException {
/* 171 */     this(Files.newByteChannel(archivePath, new java.nio.file.OpenOption[0]), 10240, 512, encoding, false);
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
/*     */   public TarFile(Path archivePath, boolean lenient) throws IOException {
/* 184 */     this(Files.newByteChannel(archivePath, new java.nio.file.OpenOption[0]), 10240, 512, null, lenient);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TarFile(SeekableByteChannel content) throws IOException {
/* 194 */     this(content, 10240, 512, null, false);
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
/*     */   public TarFile(SeekableByteChannel archive, int blockSize, int recordSize, String encoding, boolean lenient) throws IOException {
/* 210 */     this.archive = archive;
/* 211 */     this.hasHitEOF = false;
/* 212 */     this.zipEncoding = ZipEncodingHelper.getZipEncoding(encoding);
/* 213 */     this.recordSize = recordSize;
/* 214 */     this.recordBuffer = ByteBuffer.allocate(this.recordSize);
/* 215 */     this.blockSize = blockSize;
/* 216 */     this.lenient = lenient;
/*     */     
/*     */     TarArchiveEntry entry;
/* 219 */     while ((entry = getNextTarEntry()) != null) {
/* 220 */       this.entries.add(entry);
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
/*     */   
/*     */   private TarArchiveEntry getNextTarEntry() throws IOException {
/* 238 */     if (isAtEOF()) {
/* 239 */       return null;
/*     */     }
/*     */     
/* 242 */     if (this.currEntry != null) {
/*     */       
/* 244 */       repositionForwardTo(this.currEntry.getDataOffset() + this.currEntry.getSize());
/* 245 */       throwExceptionIfPositionIsNotInArchive();
/* 246 */       skipRecordPadding();
/*     */     } 
/*     */     
/* 249 */     ByteBuffer headerBuf = getRecord();
/* 250 */     if (null == headerBuf) {
/*     */       
/* 252 */       this.currEntry = null;
/* 253 */       return null;
/*     */     } 
/*     */     
/*     */     try {
/* 257 */       long position = this.archive.position();
/* 258 */       this.currEntry = new TarArchiveEntry(this.globalPaxHeaders, headerBuf.array(), this.zipEncoding, this.lenient, position);
/* 259 */     } catch (IllegalArgumentException e) {
/* 260 */       throw new IOException("Error detected parsing the header", e);
/*     */     } 
/*     */     
/* 263 */     if (this.currEntry.isGNULongLinkEntry()) {
/* 264 */       byte[] longLinkData = getLongNameData();
/* 265 */       if (longLinkData == null)
/*     */       {
/*     */ 
/*     */         
/* 269 */         return null;
/*     */       }
/* 271 */       this.currEntry.setLinkName(this.zipEncoding.decode(longLinkData));
/*     */     } 
/*     */     
/* 274 */     if (this.currEntry.isGNULongNameEntry()) {
/* 275 */       byte[] longNameData = getLongNameData();
/* 276 */       if (longNameData == null)
/*     */       {
/*     */ 
/*     */         
/* 280 */         return null;
/*     */       }
/*     */ 
/*     */       
/* 284 */       String name = this.zipEncoding.decode(longNameData);
/* 285 */       this.currEntry.setName(name);
/* 286 */       if (this.currEntry.isDirectory() && !name.endsWith("/")) {
/* 287 */         this.currEntry.setName(name + "/");
/*     */       }
/*     */     } 
/*     */     
/* 291 */     if (this.currEntry.isGlobalPaxHeader()) {
/* 292 */       readGlobalPaxHeaders();
/*     */     }
/*     */     
/*     */     try {
/* 296 */       if (this.currEntry.isPaxHeader()) {
/* 297 */         paxHeaders();
/* 298 */       } else if (!this.globalPaxHeaders.isEmpty()) {
/* 299 */         applyPaxHeadersToCurrentEntry(this.globalPaxHeaders, this.globalSparseHeaders);
/*     */       } 
/* 301 */     } catch (NumberFormatException e) {
/* 302 */       throw new IOException("Error detected parsing the pax header", e);
/*     */     } 
/*     */     
/* 305 */     if (this.currEntry.isOldGNUSparse()) {
/* 306 */       readOldGNUSparse();
/*     */     }
/*     */     
/* 309 */     return this.currEntry;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readOldGNUSparse() throws IOException {
/* 319 */     if (this.currEntry.isExtended()) {
/*     */       TarArchiveSparseEntry entry;
/*     */       do {
/* 322 */         ByteBuffer headerBuf = getRecord();
/* 323 */         if (headerBuf == null) {
/* 324 */           throw new IOException("premature end of tar archive. Didn't find extended_header after header with extended flag.");
/*     */         }
/* 326 */         entry = new TarArchiveSparseEntry(headerBuf.array());
/* 327 */         this.currEntry.getSparseHeaders().addAll(entry.getSparseHeaders());
/* 328 */         this.currEntry.setDataOffset(this.currEntry.getDataOffset() + this.recordSize);
/* 329 */       } while (entry.isExtended());
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 334 */     buildSparseInputStreams();
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
/*     */   private void buildSparseInputStreams() throws IOException {
/* 346 */     List<InputStream> streams = new ArrayList<>();
/*     */     
/* 348 */     List<TarArchiveStructSparse> sparseHeaders = this.currEntry.getOrderedSparseHeaders();
/*     */ 
/*     */     
/* 351 */     InputStream zeroInputStream = new TarArchiveSparseZeroInputStream();
/*     */     
/* 353 */     long offset = 0L;
/* 354 */     long numberOfZeroBytesInSparseEntry = 0L;
/* 355 */     for (TarArchiveStructSparse sparseHeader : sparseHeaders) {
/* 356 */       long zeroBlockSize = sparseHeader.getOffset() - offset;
/* 357 */       if (zeroBlockSize < 0L)
/*     */       {
/* 359 */         throw new IOException("Corrupted struct sparse detected");
/*     */       }
/*     */ 
/*     */       
/* 363 */       if (zeroBlockSize > 0L) {
/* 364 */         streams.add(new BoundedInputStream(zeroInputStream, zeroBlockSize));
/* 365 */         numberOfZeroBytesInSparseEntry += zeroBlockSize;
/*     */       } 
/*     */ 
/*     */       
/* 369 */       if (sparseHeader.getNumbytes() > 0L) {
/*     */         
/* 371 */         long start = this.currEntry.getDataOffset() + sparseHeader.getOffset() - numberOfZeroBytesInSparseEntry;
/* 372 */         if (start + sparseHeader.getNumbytes() < start)
/*     */         {
/* 374 */           throw new IOException("Unreadable TAR archive, sparse block offset or length too big");
/*     */         }
/* 376 */         streams.add(new BoundedSeekableByteChannelInputStream(start, sparseHeader.getNumbytes(), this.archive));
/*     */       } 
/*     */       
/* 379 */       offset = sparseHeader.getOffset() + sparseHeader.getNumbytes();
/*     */     } 
/*     */     
/* 382 */     this.sparseInputStreams.put(this.currEntry.getName(), streams);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void applyPaxHeadersToCurrentEntry(Map<String, String> headers, List<TarArchiveStructSparse> sparseHeaders) throws IOException {
/* 392 */     this.currEntry.updateEntryFromPaxHeaders(headers);
/* 393 */     this.currEntry.setSparseHeaders(sparseHeaders);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void paxHeaders() throws IOException {
/*     */     Map<String, String> headers;
/* 426 */     List<TarArchiveStructSparse> sparseHeaders = new ArrayList<>();
/*     */     
/* 428 */     try (InputStream input = getInputStream(this.currEntry)) {
/* 429 */       headers = TarUtils.parsePaxHeaders(input, sparseHeaders, this.globalPaxHeaders, this.currEntry.getSize());
/*     */     } 
/*     */ 
/*     */     
/* 433 */     if (headers.containsKey("GNU.sparse.map")) {
/* 434 */       sparseHeaders = new ArrayList<>(TarUtils.parseFromPAX01SparseHeaders(headers.get("GNU.sparse.map")));
/*     */     }
/* 436 */     getNextTarEntry();
/* 437 */     if (this.currEntry == null) {
/* 438 */       throw new IOException("premature end of tar archive. Didn't find any entry after PAX header.");
/*     */     }
/* 440 */     applyPaxHeadersToCurrentEntry(headers, sparseHeaders);
/*     */ 
/*     */     
/* 443 */     if (this.currEntry.isPaxGNU1XSparse()) {
/* 444 */       try (InputStream input = getInputStream(this.currEntry)) {
/* 445 */         sparseHeaders = TarUtils.parsePAX1XSparseHeaders(input, this.recordSize);
/*     */       } 
/* 447 */       this.currEntry.setSparseHeaders(sparseHeaders);
/*     */       
/* 449 */       this.currEntry.setDataOffset(this.currEntry.getDataOffset() + this.recordSize);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 454 */     buildSparseInputStreams();
/*     */   }
/*     */   
/*     */   private void readGlobalPaxHeaders() throws IOException {
/* 458 */     try (InputStream input = getInputStream(this.currEntry)) {
/* 459 */       this.globalPaxHeaders = TarUtils.parsePaxHeaders(input, this.globalSparseHeaders, this.globalPaxHeaders, this.currEntry
/* 460 */           .getSize());
/*     */     } 
/* 462 */     getNextTarEntry();
/*     */     
/* 464 */     if (this.currEntry == null) {
/* 465 */       throw new IOException("Error detected parsing the pax header");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private byte[] getLongNameData() throws IOException {
/* 476 */     ByteArrayOutputStream longName = new ByteArrayOutputStream();
/*     */     
/* 478 */     try (InputStream in = getInputStream(this.currEntry)) {
/* 479 */       int i; while ((i = in.read(this.smallBuf)) >= 0) {
/* 480 */         longName.write(this.smallBuf, 0, i);
/*     */       }
/*     */     } 
/* 483 */     getNextTarEntry();
/* 484 */     if (this.currEntry == null)
/*     */     {
/*     */       
/* 487 */       return null;
/*     */     }
/* 489 */     byte[] longNameData = longName.toByteArray();
/*     */     
/* 491 */     int length = longNameData.length;
/* 492 */     while (length > 0 && longNameData[length - 1] == 0) {
/* 493 */       length--;
/*     */     }
/* 495 */     if (length != longNameData.length) {
/* 496 */       byte[] l = new byte[length];
/* 497 */       System.arraycopy(longNameData, 0, l, 0, length);
/* 498 */       longNameData = l;
/*     */     } 
/* 500 */     return longNameData;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void skipRecordPadding() throws IOException {
/* 510 */     if (!isDirectory() && this.currEntry.getSize() > 0L && this.currEntry.getSize() % this.recordSize != 0L) {
/* 511 */       long numRecords = this.currEntry.getSize() / this.recordSize + 1L;
/* 512 */       long padding = numRecords * this.recordSize - this.currEntry.getSize();
/* 513 */       repositionForwardBy(padding);
/* 514 */       throwExceptionIfPositionIsNotInArchive();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void repositionForwardTo(long newPosition) throws IOException {
/* 519 */     long currPosition = this.archive.position();
/* 520 */     if (newPosition < currPosition) {
/* 521 */       throw new IOException("trying to move backwards inside of the archive");
/*     */     }
/* 523 */     this.archive.position(newPosition);
/*     */   }
/*     */   
/*     */   private void repositionForwardBy(long offset) throws IOException {
/* 527 */     repositionForwardTo(this.archive.position() + offset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void throwExceptionIfPositionIsNotInArchive() throws IOException {
/* 535 */     if (this.archive.size() < this.archive.position()) {
/* 536 */       throw new IOException("Truncated TAR archive");
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
/*     */ 
/*     */   
/*     */   private ByteBuffer getRecord() throws IOException {
/* 555 */     ByteBuffer headerBuf = readRecord();
/* 556 */     setAtEOF(isEOFRecord(headerBuf));
/* 557 */     if (isAtEOF() && headerBuf != null) {
/*     */       
/* 559 */       tryToConsumeSecondEOFRecord();
/* 560 */       consumeRemainderOfLastBlock();
/* 561 */       headerBuf = null;
/*     */     } 
/* 563 */     return headerBuf;
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
/*     */   private void tryToConsumeSecondEOFRecord() throws IOException {
/* 580 */     boolean shouldReset = true;
/*     */     try {
/* 582 */       shouldReset = !isEOFRecord(readRecord());
/*     */     } finally {
/* 584 */       if (shouldReset) {
/* 585 */         this.archive.position(this.archive.position() - this.recordSize);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void consumeRemainderOfLastBlock() throws IOException {
/* 596 */     long bytesReadOfLastBlock = this.archive.position() % this.blockSize;
/* 597 */     if (bytesReadOfLastBlock > 0L) {
/* 598 */       repositionForwardBy(this.blockSize - bytesReadOfLastBlock);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ByteBuffer readRecord() throws IOException {
/* 609 */     this.recordBuffer.rewind();
/* 610 */     int readNow = this.archive.read(this.recordBuffer);
/* 611 */     if (readNow != this.recordSize) {
/* 612 */       return null;
/*     */     }
/* 614 */     return this.recordBuffer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<TarArchiveEntry> getEntries() {
/* 623 */     return new ArrayList<>(this.entries);
/*     */   }
/*     */   
/*     */   private boolean isEOFRecord(ByteBuffer headerBuf) {
/* 627 */     return (headerBuf == null || ArchiveUtils.isArrayZero(headerBuf.array(), this.recordSize));
/*     */   }
/*     */   
/*     */   protected final boolean isAtEOF() {
/* 631 */     return this.hasHitEOF;
/*     */   }
/*     */   
/*     */   protected final void setAtEOF(boolean b) {
/* 635 */     this.hasHitEOF = b;
/*     */   }
/*     */   
/*     */   private boolean isDirectory() {
/* 639 */     return (this.currEntry != null && this.currEntry.isDirectory());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream getInputStream(TarArchiveEntry entry) throws IOException {
/*     */     try {
/* 650 */       return (InputStream)new BoundedTarEntryInputStream(entry, this.archive);
/* 651 */     } catch (RuntimeException ex) {
/* 652 */       throw new IOException("Corrupted TAR archive. Can't read entry", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 658 */     this.archive.close();
/*     */   }
/*     */ 
/*     */   
/*     */   private final class BoundedTarEntryInputStream
/*     */     extends BoundedArchiveInputStream
/*     */   {
/*     */     private final SeekableByteChannel channel;
/*     */     
/*     */     private final TarArchiveEntry entry;
/*     */     private long entryOffset;
/*     */     private int currentSparseInputStreamIndex;
/*     */     
/*     */     BoundedTarEntryInputStream(TarArchiveEntry entry, SeekableByteChannel channel) throws IOException {
/* 672 */       super(entry.getDataOffset(), entry.getRealSize());
/* 673 */       if (channel.size() - entry.getSize() < entry.getDataOffset()) {
/* 674 */         throw new IOException("entry size exceeds archive size");
/*     */       }
/* 676 */       this.entry = entry;
/* 677 */       this.channel = channel;
/*     */     }
/*     */     
/*     */     protected int read(long pos, ByteBuffer buf) throws IOException {
/*     */       int totalRead;
/* 682 */       if (this.entryOffset >= this.entry.getRealSize()) {
/* 683 */         return -1;
/*     */       }
/*     */ 
/*     */       
/* 687 */       if (this.entry.isSparse()) {
/* 688 */         totalRead = readSparse(this.entryOffset, buf, buf.limit());
/*     */       } else {
/* 690 */         totalRead = readArchive(pos, buf);
/*     */       } 
/*     */       
/* 693 */       if (totalRead == -1) {
/* 694 */         if ((buf.array()).length > 0) {
/* 695 */           throw new IOException("Truncated TAR archive");
/*     */         }
/* 697 */         TarFile.this.setAtEOF(true);
/*     */       } else {
/* 699 */         this.entryOffset += totalRead;
/* 700 */         buf.flip();
/*     */       } 
/* 702 */       return totalRead;
/*     */     }
/*     */ 
/*     */     
/*     */     private int readSparse(long pos, ByteBuffer buf, int numToRead) throws IOException {
/* 707 */       List<InputStream> entrySparseInputStreams = (List<InputStream>)TarFile.this.sparseInputStreams.get(this.entry.getName());
/* 708 */       if (entrySparseInputStreams == null || entrySparseInputStreams.isEmpty()) {
/* 709 */         return readArchive(this.entry.getDataOffset() + pos, buf);
/*     */       }
/*     */       
/* 712 */       if (this.currentSparseInputStreamIndex >= entrySparseInputStreams.size()) {
/* 713 */         return -1;
/*     */       }
/*     */       
/* 716 */       InputStream currentInputStream = entrySparseInputStreams.get(this.currentSparseInputStreamIndex);
/* 717 */       byte[] bufArray = new byte[numToRead];
/* 718 */       int readLen = currentInputStream.read(bufArray);
/* 719 */       if (readLen != -1) {
/* 720 */         buf.put(bufArray, 0, readLen);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 725 */       if (this.currentSparseInputStreamIndex == entrySparseInputStreams.size() - 1) {
/* 726 */         return readLen;
/*     */       }
/*     */ 
/*     */       
/* 730 */       if (readLen == -1) {
/* 731 */         this.currentSparseInputStreamIndex++;
/* 732 */         return readSparse(pos, buf, numToRead);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 737 */       if (readLen < numToRead) {
/* 738 */         this.currentSparseInputStreamIndex++;
/* 739 */         int readLenOfNext = readSparse(pos + readLen, buf, numToRead - readLen);
/* 740 */         if (readLenOfNext == -1) {
/* 741 */           return readLen;
/*     */         }
/*     */         
/* 744 */         return readLen + readLenOfNext;
/*     */       } 
/*     */ 
/*     */       
/* 748 */       return readLen;
/*     */     }
/*     */     
/*     */     private int readArchive(long pos, ByteBuffer buf) throws IOException {
/* 752 */       this.channel.position(pos);
/* 753 */       return this.channel.read(buf);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\archivers\tar\TarFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */