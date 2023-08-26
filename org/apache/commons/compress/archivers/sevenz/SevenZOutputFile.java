/*     */ package org.apache.commons.compress.archivers.sevenz;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.Closeable;
/*     */ import java.io.DataOutput;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.nio.channels.SeekableByteChannel;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.StandardOpenOption;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.BitSet;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.EnumSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.zip.CRC32;
/*     */ import org.apache.commons.compress.archivers.ArchiveEntry;
/*     */ import org.apache.commons.compress.utils.CountingOutputStream;
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
/*     */ public class SevenZOutputFile
/*     */   implements Closeable
/*     */ {
/*     */   private final SeekableByteChannel channel;
/*  60 */   private final List<SevenZArchiveEntry> files = new ArrayList<>();
/*     */   private int numNonEmptyStreams;
/*  62 */   private final CRC32 crc32 = new CRC32();
/*  63 */   private final CRC32 compressedCrc32 = new CRC32();
/*     */   
/*     */   private long fileBytesWritten;
/*     */   private boolean finished;
/*     */   private CountingOutputStream currentOutputStream;
/*     */   private CountingOutputStream[] additionalCountingStreams;
/*  69 */   private Iterable<? extends SevenZMethodConfiguration> contentMethods = Collections.singletonList(new SevenZMethodConfiguration(SevenZMethod.LZMA2));
/*  70 */   private final Map<SevenZArchiveEntry, long[]> additionalSizes = (Map)new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SevenZOutputFile(File fileName) throws IOException {
/*  79 */     this(Files.newByteChannel(fileName.toPath(), 
/*  80 */           EnumSet.of(StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING), (FileAttribute<?>[])new FileAttribute[0]));
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
/*     */   public SevenZOutputFile(SeekableByteChannel channel) throws IOException {
/*  96 */     this.channel = channel;
/*  97 */     channel.position(32L);
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
/*     */   public void setContentCompression(SevenZMethod method) {
/* 113 */     setContentMethods(Collections.singletonList(new SevenZMethodConfiguration(method)));
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
/*     */   public void setContentMethods(Iterable<? extends SevenZMethodConfiguration> methods) {
/* 131 */     this.contentMethods = reverse(methods);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*     */     try {
/* 142 */       if (!this.finished) {
/* 143 */         finish();
/*     */       }
/*     */     } finally {
/* 146 */       this.channel.close();
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
/*     */   public SevenZArchiveEntry createArchiveEntry(File inputFile, String entryName) {
/* 159 */     SevenZArchiveEntry entry = new SevenZArchiveEntry();
/* 160 */     entry.setDirectory(inputFile.isDirectory());
/* 161 */     entry.setName(entryName);
/* 162 */     entry.setLastModifiedDate(new Date(inputFile.lastModified()));
/* 163 */     return entry;
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
/*     */   public SevenZArchiveEntry createArchiveEntry(Path inputPath, String entryName, LinkOption... options) throws IOException {
/* 179 */     SevenZArchiveEntry entry = new SevenZArchiveEntry();
/* 180 */     entry.setDirectory(Files.isDirectory(inputPath, options));
/* 181 */     entry.setName(entryName);
/* 182 */     entry.setLastModifiedDate(new Date(Files.getLastModifiedTime(inputPath, options).toMillis()));
/* 183 */     return entry;
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
/*     */   public void putArchiveEntry(ArchiveEntry archiveEntry) {
/* 195 */     SevenZArchiveEntry entry = (SevenZArchiveEntry)archiveEntry;
/* 196 */     this.files.add(entry);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void closeArchiveEntry() throws IOException {
/* 204 */     if (this.currentOutputStream != null) {
/* 205 */       this.currentOutputStream.flush();
/* 206 */       this.currentOutputStream.close();
/*     */     } 
/*     */     
/* 209 */     SevenZArchiveEntry entry = this.files.get(this.files.size() - 1);
/* 210 */     if (this.fileBytesWritten > 0L) {
/* 211 */       entry.setHasStream(true);
/* 212 */       this.numNonEmptyStreams++;
/* 213 */       entry.setSize(this.currentOutputStream.getBytesWritten());
/* 214 */       entry.setCompressedSize(this.fileBytesWritten);
/* 215 */       entry.setCrcValue(this.crc32.getValue());
/* 216 */       entry.setCompressedCrcValue(this.compressedCrc32.getValue());
/* 217 */       entry.setHasCrc(true);
/* 218 */       if (this.additionalCountingStreams != null) {
/* 219 */         long[] sizes = new long[this.additionalCountingStreams.length];
/* 220 */         Arrays.setAll(sizes, i -> this.additionalCountingStreams[i].getBytesWritten());
/* 221 */         this.additionalSizes.put(entry, sizes);
/*     */       } 
/*     */     } else {
/* 224 */       entry.setHasStream(false);
/* 225 */       entry.setSize(0L);
/* 226 */       entry.setCompressedSize(0L);
/* 227 */       entry.setHasCrc(false);
/*     */     } 
/* 229 */     this.currentOutputStream = null;
/* 230 */     this.additionalCountingStreams = null;
/* 231 */     this.crc32.reset();
/* 232 */     this.compressedCrc32.reset();
/* 233 */     this.fileBytesWritten = 0L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(int b) throws IOException {
/* 242 */     getCurrentOutputStream().write(b);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(byte[] b) throws IOException {
/* 251 */     write(b, 0, b.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(byte[] b, int off, int len) throws IOException {
/* 262 */     if (len > 0) {
/* 263 */       getCurrentOutputStream().write(b, off, len);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(InputStream inputStream) throws IOException {
/* 274 */     byte[] buffer = new byte[8024];
/* 275 */     int n = 0;
/* 276 */     while (-1 != (n = inputStream.read(buffer))) {
/* 277 */       write(buffer, 0, n);
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
/*     */   public void write(Path path, OpenOption... options) throws IOException {
/* 289 */     try (InputStream in = new BufferedInputStream(Files.newInputStream(path, options))) {
/* 290 */       write(in);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void finish() throws IOException {
/* 300 */     if (this.finished) {
/* 301 */       throw new IOException("This archive has already been finished");
/*     */     }
/* 303 */     this.finished = true;
/*     */     
/* 305 */     long headerPosition = this.channel.position();
/*     */     
/* 307 */     ByteArrayOutputStream headerBaos = new ByteArrayOutputStream();
/* 308 */     DataOutputStream header = new DataOutputStream(headerBaos);
/*     */     
/* 310 */     writeHeader(header);
/* 311 */     header.flush();
/* 312 */     byte[] headerBytes = headerBaos.toByteArray();
/* 313 */     this.channel.write(ByteBuffer.wrap(headerBytes));
/*     */     
/* 315 */     CRC32 crc32 = new CRC32();
/* 316 */     crc32.update(headerBytes);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 324 */     ByteBuffer bb = ByteBuffer.allocate(SevenZFile.sevenZSignature.length + 2 + 4 + 8 + 8 + 4).order(ByteOrder.LITTLE_ENDIAN);
/*     */     
/* 326 */     this.channel.position(0L);
/* 327 */     bb.put(SevenZFile.sevenZSignature);
/*     */     
/* 329 */     bb.put((byte)0).put((byte)2);
/*     */ 
/*     */     
/* 332 */     bb.putInt(0);
/*     */ 
/*     */     
/* 335 */     bb.putLong(headerPosition - 32L)
/* 336 */       .putLong(0xFFFFFFFFL & headerBytes.length)
/* 337 */       .putInt((int)crc32.getValue());
/* 338 */     crc32.reset();
/* 339 */     crc32.update(bb.array(), SevenZFile.sevenZSignature.length + 6, 20);
/* 340 */     bb.putInt(SevenZFile.sevenZSignature.length + 2, (int)crc32.getValue());
/* 341 */     bb.flip();
/* 342 */     this.channel.write(bb);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private OutputStream getCurrentOutputStream() throws IOException {
/* 351 */     if (this.currentOutputStream == null) {
/* 352 */       this.currentOutputStream = setupFileOutputStream();
/*     */     }
/* 354 */     return (OutputStream)this.currentOutputStream;
/*     */   }
/*     */   
/*     */   private CountingOutputStream setupFileOutputStream() throws IOException {
/* 358 */     if (this.files.isEmpty()) {
/* 359 */       throw new IllegalStateException("No current 7z entry");
/*     */     }
/*     */ 
/*     */     
/* 363 */     OutputStream outputStream = new OutputStreamWrapper();
/* 364 */     ArrayList<CountingOutputStream> moreStreams = new ArrayList<>();
/* 365 */     boolean first = true;
/* 366 */     for (SevenZMethodConfiguration m : getContentMethods(this.files.get(this.files.size() - 1))) {
/* 367 */       CountingOutputStream countingOutputStream; if (!first) {
/* 368 */         CountingOutputStream cos = new CountingOutputStream(outputStream);
/* 369 */         moreStreams.add(cos);
/* 370 */         countingOutputStream = cos;
/*     */       } 
/* 372 */       outputStream = Coders.addEncoder((OutputStream)countingOutputStream, m.getMethod(), m.getOptions());
/* 373 */       first = false;
/*     */     } 
/* 375 */     if (!moreStreams.isEmpty()) {
/* 376 */       this.additionalCountingStreams = moreStreams.<CountingOutputStream>toArray(new CountingOutputStream[0]);
/*     */     }
/* 378 */     return new CountingOutputStream(outputStream)
/*     */       {
/*     */         public void write(int b) throws IOException {
/* 381 */           super.write(b);
/* 382 */           SevenZOutputFile.this.crc32.update(b);
/*     */         }
/*     */ 
/*     */         
/*     */         public void write(byte[] b) throws IOException {
/* 387 */           super.write(b);
/* 388 */           SevenZOutputFile.this.crc32.update(b);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public void write(byte[] b, int off, int len) throws IOException {
/* 394 */           super.write(b, off, len);
/* 395 */           SevenZOutputFile.this.crc32.update(b, off, len);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private Iterable<? extends SevenZMethodConfiguration> getContentMethods(SevenZArchiveEntry entry) {
/* 401 */     Iterable<? extends SevenZMethodConfiguration> ms = entry.getContentMethods();
/* 402 */     return (ms == null) ? this.contentMethods : ms;
/*     */   }
/*     */   
/*     */   private void writeHeader(DataOutput header) throws IOException {
/* 406 */     header.write(1);
/*     */     
/* 408 */     header.write(4);
/* 409 */     writeStreamsInfo(header);
/* 410 */     writeFilesInfo(header);
/* 411 */     header.write(0);
/*     */   }
/*     */   
/*     */   private void writeStreamsInfo(DataOutput header) throws IOException {
/* 415 */     if (this.numNonEmptyStreams > 0) {
/* 416 */       writePackInfo(header);
/* 417 */       writeUnpackInfo(header);
/*     */     } 
/*     */     
/* 420 */     writeSubStreamsInfo(header);
/*     */     
/* 422 */     header.write(0);
/*     */   }
/*     */   
/*     */   private void writePackInfo(DataOutput header) throws IOException {
/* 426 */     header.write(6);
/*     */     
/* 428 */     writeUint64(header, 0L);
/* 429 */     writeUint64(header, 0xFFFFFFFFL & this.numNonEmptyStreams);
/*     */     
/* 431 */     header.write(9);
/* 432 */     for (SevenZArchiveEntry entry : this.files) {
/* 433 */       if (entry.hasStream()) {
/* 434 */         writeUint64(header, entry.getCompressedSize());
/*     */       }
/*     */     } 
/*     */     
/* 438 */     header.write(10);
/* 439 */     header.write(1);
/* 440 */     for (SevenZArchiveEntry entry : this.files) {
/* 441 */       if (entry.hasStream()) {
/* 442 */         header.writeInt(Integer.reverseBytes((int)entry.getCompressedCrcValue()));
/*     */       }
/*     */     } 
/*     */     
/* 446 */     header.write(0);
/*     */   }
/*     */   
/*     */   private void writeUnpackInfo(DataOutput header) throws IOException {
/* 450 */     header.write(7);
/*     */     
/* 452 */     header.write(11);
/* 453 */     writeUint64(header, this.numNonEmptyStreams);
/* 454 */     header.write(0);
/* 455 */     for (SevenZArchiveEntry entry : this.files) {
/* 456 */       if (entry.hasStream()) {
/* 457 */         writeFolder(header, entry);
/*     */       }
/*     */     } 
/*     */     
/* 461 */     header.write(12);
/* 462 */     for (SevenZArchiveEntry entry : this.files) {
/* 463 */       if (entry.hasStream()) {
/* 464 */         long[] moreSizes = this.additionalSizes.get(entry);
/* 465 */         if (moreSizes != null) {
/* 466 */           for (long s : moreSizes) {
/* 467 */             writeUint64(header, s);
/*     */           }
/*     */         }
/* 470 */         writeUint64(header, entry.getSize());
/*     */       } 
/*     */     } 
/*     */     
/* 474 */     header.write(10);
/* 475 */     header.write(1);
/* 476 */     for (SevenZArchiveEntry entry : this.files) {
/* 477 */       if (entry.hasStream()) {
/* 478 */         header.writeInt(Integer.reverseBytes((int)entry.getCrcValue()));
/*     */       }
/*     */     } 
/*     */     
/* 482 */     header.write(0);
/*     */   }
/*     */   
/*     */   private void writeFolder(DataOutput header, SevenZArchiveEntry entry) throws IOException {
/* 486 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 487 */     int numCoders = 0;
/* 488 */     for (SevenZMethodConfiguration m : getContentMethods(entry)) {
/* 489 */       numCoders++;
/* 490 */       writeSingleCodec(m, bos);
/*     */     } 
/*     */     
/* 493 */     writeUint64(header, numCoders);
/* 494 */     header.write(bos.toByteArray()); long i;
/* 495 */     for (i = 0L; i < (numCoders - 1); i++) {
/* 496 */       writeUint64(header, i + 1L);
/* 497 */       writeUint64(header, i);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void writeSingleCodec(SevenZMethodConfiguration m, OutputStream bos) throws IOException {
/* 502 */     byte[] id = m.getMethod().getId();
/*     */     
/* 504 */     byte[] properties = Coders.findByMethod(m.getMethod()).getOptionsAsProperties(m.getOptions());
/*     */     
/* 506 */     int codecFlags = id.length;
/* 507 */     if (properties.length > 0) {
/* 508 */       codecFlags |= 0x20;
/*     */     }
/* 510 */     bos.write(codecFlags);
/* 511 */     bos.write(id);
/*     */     
/* 513 */     if (properties.length > 0) {
/* 514 */       bos.write(properties.length);
/* 515 */       bos.write(properties);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void writeSubStreamsInfo(DataOutput header) throws IOException {
/* 520 */     header.write(8);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 530 */     header.write(0);
/*     */   }
/*     */   
/*     */   private void writeFilesInfo(DataOutput header) throws IOException {
/* 534 */     header.write(5);
/*     */     
/* 536 */     writeUint64(header, this.files.size());
/*     */     
/* 538 */     writeFileEmptyStreams(header);
/* 539 */     writeFileEmptyFiles(header);
/* 540 */     writeFileAntiItems(header);
/* 541 */     writeFileNames(header);
/* 542 */     writeFileCTimes(header);
/* 543 */     writeFileATimes(header);
/* 544 */     writeFileMTimes(header);
/* 545 */     writeFileWindowsAttributes(header);
/* 546 */     header.write(0);
/*     */   }
/*     */   
/*     */   private void writeFileEmptyStreams(DataOutput header) throws IOException {
/* 550 */     boolean hasEmptyStreams = this.files.stream().anyMatch(entry -> !entry.hasStream());
/* 551 */     if (hasEmptyStreams) {
/* 552 */       header.write(14);
/* 553 */       BitSet emptyStreams = new BitSet(this.files.size());
/* 554 */       for (int i = 0; i < this.files.size(); i++) {
/* 555 */         emptyStreams.set(i, !((SevenZArchiveEntry)this.files.get(i)).hasStream());
/*     */       }
/* 557 */       ByteArrayOutputStream baos = new ByteArrayOutputStream();
/* 558 */       DataOutputStream out = new DataOutputStream(baos);
/* 559 */       writeBits(out, emptyStreams, this.files.size());
/* 560 */       out.flush();
/* 561 */       byte[] contents = baos.toByteArray();
/* 562 */       writeUint64(header, contents.length);
/* 563 */       header.write(contents);
/*     */     } 
/*     */   }
/*     */   private void writeFileEmptyFiles(DataOutput header) throws IOException {
/*     */     int i;
/* 568 */     boolean hasEmptyFiles = false;
/* 569 */     int emptyStreamCounter = 0;
/* 570 */     BitSet emptyFiles = new BitSet(0);
/* 571 */     for (SevenZArchiveEntry file1 : this.files) {
/* 572 */       if (!file1.hasStream()) {
/* 573 */         boolean isDir = file1.isDirectory();
/* 574 */         emptyFiles.set(emptyStreamCounter++, !isDir);
/* 575 */         i = hasEmptyFiles | (!isDir ? 1 : 0);
/*     */       } 
/*     */     } 
/* 578 */     if (i != 0) {
/* 579 */       header.write(15);
/* 580 */       ByteArrayOutputStream baos = new ByteArrayOutputStream();
/* 581 */       DataOutputStream out = new DataOutputStream(baos);
/* 582 */       writeBits(out, emptyFiles, emptyStreamCounter);
/* 583 */       out.flush();
/* 584 */       byte[] contents = baos.toByteArray();
/* 585 */       writeUint64(header, contents.length);
/* 586 */       header.write(contents);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void writeFileAntiItems(DataOutput header) throws IOException {
/* 591 */     boolean hasAntiItems = false;
/* 592 */     BitSet antiItems = new BitSet(0);
/* 593 */     int antiItemCounter = 0;
/* 594 */     for (SevenZArchiveEntry file1 : this.files) {
/* 595 */       if (!file1.hasStream()) {
/* 596 */         boolean isAnti = file1.isAntiItem();
/* 597 */         antiItems.set(antiItemCounter++, isAnti);
/* 598 */         hasAntiItems |= isAnti;
/*     */       } 
/*     */     } 
/* 601 */     if (hasAntiItems) {
/* 602 */       header.write(16);
/* 603 */       ByteArrayOutputStream baos = new ByteArrayOutputStream();
/* 604 */       DataOutputStream out = new DataOutputStream(baos);
/* 605 */       writeBits(out, antiItems, antiItemCounter);
/* 606 */       out.flush();
/* 607 */       byte[] contents = baos.toByteArray();
/* 608 */       writeUint64(header, contents.length);
/* 609 */       header.write(contents);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void writeFileNames(DataOutput header) throws IOException {
/* 614 */     header.write(17);
/*     */     
/* 616 */     ByteArrayOutputStream baos = new ByteArrayOutputStream();
/* 617 */     DataOutputStream out = new DataOutputStream(baos);
/* 618 */     out.write(0);
/* 619 */     for (SevenZArchiveEntry entry : this.files) {
/* 620 */       out.write(entry.getName().getBytes(StandardCharsets.UTF_16LE));
/* 621 */       out.writeShort(0);
/*     */     } 
/* 623 */     out.flush();
/* 624 */     byte[] contents = baos.toByteArray();
/* 625 */     writeUint64(header, contents.length);
/* 626 */     header.write(contents);
/*     */   }
/*     */   
/*     */   private void writeFileCTimes(DataOutput header) throws IOException {
/* 630 */     int numCreationDates = 0;
/* 631 */     for (SevenZArchiveEntry entry : this.files) {
/* 632 */       if (entry.getHasCreationDate()) {
/* 633 */         numCreationDates++;
/*     */       }
/*     */     } 
/* 636 */     if (numCreationDates > 0) {
/* 637 */       header.write(18);
/*     */       
/* 639 */       ByteArrayOutputStream baos = new ByteArrayOutputStream();
/* 640 */       DataOutputStream out = new DataOutputStream(baos);
/* 641 */       if (numCreationDates != this.files.size()) {
/* 642 */         out.write(0);
/* 643 */         BitSet cTimes = new BitSet(this.files.size());
/* 644 */         for (int i = 0; i < this.files.size(); i++) {
/* 645 */           cTimes.set(i, ((SevenZArchiveEntry)this.files.get(i)).getHasCreationDate());
/*     */         }
/* 647 */         writeBits(out, cTimes, this.files.size());
/*     */       } else {
/* 649 */         out.write(1);
/*     */       } 
/* 651 */       out.write(0);
/* 652 */       for (SevenZArchiveEntry entry : this.files) {
/* 653 */         if (entry.getHasCreationDate()) {
/* 654 */           out.writeLong(Long.reverseBytes(
/* 655 */                 SevenZArchiveEntry.javaTimeToNtfsTime(entry.getCreationDate())));
/*     */         }
/*     */       } 
/* 658 */       out.flush();
/* 659 */       byte[] contents = baos.toByteArray();
/* 660 */       writeUint64(header, contents.length);
/* 661 */       header.write(contents);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void writeFileATimes(DataOutput header) throws IOException {
/* 666 */     int numAccessDates = 0;
/* 667 */     for (SevenZArchiveEntry entry : this.files) {
/* 668 */       if (entry.getHasAccessDate()) {
/* 669 */         numAccessDates++;
/*     */       }
/*     */     } 
/* 672 */     if (numAccessDates > 0) {
/* 673 */       header.write(19);
/*     */       
/* 675 */       ByteArrayOutputStream baos = new ByteArrayOutputStream();
/* 676 */       DataOutputStream out = new DataOutputStream(baos);
/* 677 */       if (numAccessDates != this.files.size()) {
/* 678 */         out.write(0);
/* 679 */         BitSet aTimes = new BitSet(this.files.size());
/* 680 */         for (int i = 0; i < this.files.size(); i++) {
/* 681 */           aTimes.set(i, ((SevenZArchiveEntry)this.files.get(i)).getHasAccessDate());
/*     */         }
/* 683 */         writeBits(out, aTimes, this.files.size());
/*     */       } else {
/* 685 */         out.write(1);
/*     */       } 
/* 687 */       out.write(0);
/* 688 */       for (SevenZArchiveEntry entry : this.files) {
/* 689 */         if (entry.getHasAccessDate()) {
/* 690 */           out.writeLong(Long.reverseBytes(
/* 691 */                 SevenZArchiveEntry.javaTimeToNtfsTime(entry.getAccessDate())));
/*     */         }
/*     */       } 
/* 694 */       out.flush();
/* 695 */       byte[] contents = baos.toByteArray();
/* 696 */       writeUint64(header, contents.length);
/* 697 */       header.write(contents);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void writeFileMTimes(DataOutput header) throws IOException {
/* 702 */     int numLastModifiedDates = 0;
/* 703 */     for (SevenZArchiveEntry entry : this.files) {
/* 704 */       if (entry.getHasLastModifiedDate()) {
/* 705 */         numLastModifiedDates++;
/*     */       }
/*     */     } 
/* 708 */     if (numLastModifiedDates > 0) {
/* 709 */       header.write(20);
/*     */       
/* 711 */       ByteArrayOutputStream baos = new ByteArrayOutputStream();
/* 712 */       DataOutputStream out = new DataOutputStream(baos);
/* 713 */       if (numLastModifiedDates != this.files.size()) {
/* 714 */         out.write(0);
/* 715 */         BitSet mTimes = new BitSet(this.files.size());
/* 716 */         for (int i = 0; i < this.files.size(); i++) {
/* 717 */           mTimes.set(i, ((SevenZArchiveEntry)this.files.get(i)).getHasLastModifiedDate());
/*     */         }
/* 719 */         writeBits(out, mTimes, this.files.size());
/*     */       } else {
/* 721 */         out.write(1);
/*     */       } 
/* 723 */       out.write(0);
/* 724 */       for (SevenZArchiveEntry entry : this.files) {
/* 725 */         if (entry.getHasLastModifiedDate()) {
/* 726 */           out.writeLong(Long.reverseBytes(
/* 727 */                 SevenZArchiveEntry.javaTimeToNtfsTime(entry.getLastModifiedDate())));
/*     */         }
/*     */       } 
/* 730 */       out.flush();
/* 731 */       byte[] contents = baos.toByteArray();
/* 732 */       writeUint64(header, contents.length);
/* 733 */       header.write(contents);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void writeFileWindowsAttributes(DataOutput header) throws IOException {
/* 738 */     int numWindowsAttributes = 0;
/* 739 */     for (SevenZArchiveEntry entry : this.files) {
/* 740 */       if (entry.getHasWindowsAttributes()) {
/* 741 */         numWindowsAttributes++;
/*     */       }
/*     */     } 
/* 744 */     if (numWindowsAttributes > 0) {
/* 745 */       header.write(21);
/*     */       
/* 747 */       ByteArrayOutputStream baos = new ByteArrayOutputStream();
/* 748 */       DataOutputStream out = new DataOutputStream(baos);
/* 749 */       if (numWindowsAttributes != this.files.size()) {
/* 750 */         out.write(0);
/* 751 */         BitSet attributes = new BitSet(this.files.size());
/* 752 */         for (int i = 0; i < this.files.size(); i++) {
/* 753 */           attributes.set(i, ((SevenZArchiveEntry)this.files.get(i)).getHasWindowsAttributes());
/*     */         }
/* 755 */         writeBits(out, attributes, this.files.size());
/*     */       } else {
/* 757 */         out.write(1);
/*     */       } 
/* 759 */       out.write(0);
/* 760 */       for (SevenZArchiveEntry entry : this.files) {
/* 761 */         if (entry.getHasWindowsAttributes()) {
/* 762 */           out.writeInt(Integer.reverseBytes(entry.getWindowsAttributes()));
/*     */         }
/*     */       } 
/* 765 */       out.flush();
/* 766 */       byte[] contents = baos.toByteArray();
/* 767 */       writeUint64(header, contents.length);
/* 768 */       header.write(contents);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void writeUint64(DataOutput header, long value) throws IOException {
/* 773 */     int firstByte = 0;
/* 774 */     int mask = 128;
/*     */     int i;
/* 776 */     for (i = 0; i < 8; i++) {
/* 777 */       if (value < 1L << 7 * (i + 1)) {
/* 778 */         firstByte = (int)(firstByte | value >>> 8 * i);
/*     */         break;
/*     */       } 
/* 781 */       firstByte |= mask;
/* 782 */       mask >>>= 1;
/*     */     } 
/* 784 */     header.write(firstByte);
/* 785 */     for (; i > 0; i--) {
/* 786 */       header.write((int)(0xFFL & value));
/* 787 */       value >>>= 8L;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void writeBits(DataOutput header, BitSet bits, int length) throws IOException {
/* 792 */     int cache = 0;
/* 793 */     int shift = 7;
/* 794 */     for (int i = 0; i < length; i++) {
/* 795 */       cache |= (bits.get(i) ? 1 : 0) << shift;
/* 796 */       if (--shift < 0) {
/* 797 */         header.write(cache);
/* 798 */         shift = 7;
/* 799 */         cache = 0;
/*     */       } 
/*     */     } 
/* 802 */     if (shift != 7) {
/* 803 */       header.write(cache);
/*     */     }
/*     */   }
/*     */   
/*     */   private static <T> Iterable<T> reverse(Iterable<T> i) {
/* 808 */     LinkedList<T> l = new LinkedList<>();
/* 809 */     for (T t : i) {
/* 810 */       l.addFirst(t);
/*     */     }
/* 812 */     return l;
/*     */   }
/*     */   
/*     */   private class OutputStreamWrapper extends OutputStream {
/*     */     private static final int BUF_SIZE = 8192;
/* 817 */     private final ByteBuffer buffer = ByteBuffer.allocate(8192);
/*     */     
/*     */     public void write(int b) throws IOException {
/* 820 */       this.buffer.clear();
/* 821 */       this.buffer.put((byte)b).flip();
/* 822 */       SevenZOutputFile.this.channel.write(this.buffer);
/* 823 */       SevenZOutputFile.this.compressedCrc32.update(b);
/* 824 */       SevenZOutputFile.this.fileBytesWritten++;
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(byte[] b) throws IOException {
/* 829 */       write(b, 0, b.length);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void write(byte[] b, int off, int len) throws IOException {
/* 835 */       if (len > 8192) {
/* 836 */         SevenZOutputFile.this.channel.write(ByteBuffer.wrap(b, off, len));
/*     */       } else {
/* 838 */         this.buffer.clear();
/* 839 */         this.buffer.put(b, off, len).flip();
/* 840 */         SevenZOutputFile.this.channel.write(this.buffer);
/*     */       } 
/* 842 */       SevenZOutputFile.this.compressedCrc32.update(b, off, len);
/* 843 */       SevenZOutputFile.this.fileBytesWritten = SevenZOutputFile.this.fileBytesWritten + len;
/*     */     }
/*     */     
/*     */     public void flush() throws IOException {}
/*     */     
/*     */     public void close() throws IOException {}
/*     */     
/*     */     private OutputStreamWrapper() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\archivers\sevenz\SevenZOutputFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */