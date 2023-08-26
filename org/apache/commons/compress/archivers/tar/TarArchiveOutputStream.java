/*     */ package org.apache.commons.compress.archivers.tar;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.StringWriter;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.RoundingMode;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.FileTime;
/*     */ import java.time.Instant;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.commons.compress.archivers.ArchiveEntry;
/*     */ import org.apache.commons.compress.archivers.ArchiveOutputStream;
/*     */ import org.apache.commons.compress.archivers.zip.ZipEncoding;
/*     */ import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
/*     */ import org.apache.commons.compress.utils.CountingOutputStream;
/*     */ import org.apache.commons.compress.utils.ExactMath;
/*     */ import org.apache.commons.compress.utils.FixedLengthBlockOutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TarArchiveOutputStream
/*     */   extends ArchiveOutputStream
/*     */ {
/*     */   public static final int LONGFILE_ERROR = 0;
/*     */   public static final int LONGFILE_TRUNCATE = 1;
/*     */   public static final int LONGFILE_GNU = 2;
/*     */   public static final int LONGFILE_POSIX = 3;
/*     */   public static final int BIGNUMBER_ERROR = 0;
/*     */   public static final int BIGNUMBER_STAR = 1;
/*     */   public static final int BIGNUMBER_POSIX = 2;
/*     */   private static final int RECORD_SIZE = 512;
/*     */   private long currSize;
/*     */   private String currName;
/*     */   private long currBytes;
/*     */   private final byte[] recordBuf;
/*     */   private int longFileMode;
/*     */   private int bigNumberMode;
/*     */   private int recordsWritten;
/*     */   private final int recordsPerBlock;
/*     */   private boolean closed;
/*     */   private boolean haveUnclosedEntry;
/*     */   private boolean finished;
/*     */   private final FixedLengthBlockOutputStream out;
/*     */   private final CountingOutputStream countingOut;
/*     */   private final ZipEncoding zipEncoding;
/*     */   final String encoding;
/*     */   private boolean addPaxHeadersForNonAsciiNames;
/* 131 */   private static final ZipEncoding ASCII = ZipEncodingHelper.getZipEncoding("ASCII");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int BLOCK_SIZE_UNSPECIFIED = -511;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TarArchiveOutputStream(OutputStream os) {
/* 143 */     this(os, -511);
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
/*     */   public TarArchiveOutputStream(OutputStream os, String encoding) {
/* 156 */     this(os, -511, encoding);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TarArchiveOutputStream(OutputStream os, int blockSize) {
/* 166 */     this(os, blockSize, (String)null);
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
/*     */   @Deprecated
/*     */   public TarArchiveOutputStream(OutputStream os, int blockSize, int recordSize) {
/* 182 */     this(os, blockSize, recordSize, (String)null);
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
/*     */   @Deprecated
/*     */   public TarArchiveOutputStream(OutputStream os, int blockSize, int recordSize, String encoding) {
/* 199 */     this(os, blockSize, encoding);
/* 200 */     if (recordSize != 512) {
/* 201 */       throw new IllegalArgumentException("Tar record size must always be 512 bytes. Attempt to set size of " + recordSize);
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
/*     */   public TarArchiveOutputStream(OutputStream os, int blockSize, String encoding) {
/*     */     int realBlockSize;
/*     */     this.longFileMode = 0;
/*     */     this.bigNumberMode = 0;
/* 218 */     if (-511 == blockSize) {
/* 219 */       realBlockSize = 512;
/*     */     } else {
/* 221 */       realBlockSize = blockSize;
/*     */     } 
/*     */     
/* 224 */     if (realBlockSize <= 0 || realBlockSize % 512 != 0) {
/* 225 */       throw new IllegalArgumentException("Block size must be a multiple of 512 bytes. Attempt to use set size of " + blockSize);
/*     */     }
/* 227 */     this.out = new FixedLengthBlockOutputStream((OutputStream)(this.countingOut = new CountingOutputStream(os)), 512);
/*     */     
/* 229 */     this.encoding = encoding;
/* 230 */     this.zipEncoding = ZipEncodingHelper.getZipEncoding(encoding);
/*     */     
/* 232 */     this.recordBuf = new byte[512];
/* 233 */     this.recordsPerBlock = realBlockSize / 512;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLongFileMode(int longFileMode) {
/* 244 */     this.longFileMode = longFileMode;
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
/*     */   public void setBigNumberMode(int bigNumberMode) {
/* 257 */     this.bigNumberMode = bigNumberMode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAddPaxHeadersForNonAsciiNames(boolean b) {
/* 267 */     this.addPaxHeadersForNonAsciiNames = b;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public int getCount() {
/* 273 */     return (int)getBytesWritten();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getBytesWritten() {
/* 278 */     return this.countingOut.getBytesWritten();
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
/*     */   public void finish() throws IOException {
/* 292 */     if (this.finished) {
/* 293 */       throw new IOException("This archive has already been finished");
/*     */     }
/*     */     
/* 296 */     if (this.haveUnclosedEntry) {
/* 297 */       throw new IOException("This archive contains unclosed entries.");
/*     */     }
/* 299 */     writeEOFRecord();
/* 300 */     writeEOFRecord();
/* 301 */     padAsNeeded();
/* 302 */     this.out.flush();
/* 303 */     this.finished = true;
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
/* 314 */       if (!this.finished) {
/* 315 */         finish();
/*     */       }
/*     */     } finally {
/* 318 */       if (!this.closed) {
/* 319 */         this.out.close();
/* 320 */         this.closed = true;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public int getRecordSize() {
/* 333 */     return 512;
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
/*     */   public void putArchiveEntry(ArchiveEntry archiveEntry) throws IOException {
/* 355 */     if (this.finished) {
/* 356 */       throw new IOException("Stream has already been finished");
/*     */     }
/* 358 */     TarArchiveEntry entry = (TarArchiveEntry)archiveEntry;
/* 359 */     if (entry.isGlobalPaxHeader()) {
/* 360 */       byte[] data = encodeExtendedPaxHeadersContents(entry.getExtraPaxHeaders());
/* 361 */       entry.setSize(data.length);
/* 362 */       entry.writeEntryHeader(this.recordBuf, this.zipEncoding, (this.bigNumberMode == 1));
/* 363 */       writeRecord(this.recordBuf);
/* 364 */       this.currSize = entry.getSize();
/* 365 */       this.currBytes = 0L;
/* 366 */       this.haveUnclosedEntry = true;
/* 367 */       write(data);
/* 368 */       closeArchiveEntry();
/*     */     } else {
/* 370 */       Map<String, String> paxHeaders = new HashMap<>();
/* 371 */       String entryName = entry.getName();
/* 372 */       boolean paxHeaderContainsPath = handleLongName(entry, entryName, paxHeaders, "path", (byte)76, "file name");
/*     */       
/* 374 */       String linkName = entry.getLinkName();
/*     */       
/* 376 */       boolean paxHeaderContainsLinkPath = (linkName != null && !linkName.isEmpty() && handleLongName(entry, linkName, paxHeaders, "linkpath", (byte)75, "link name"));
/*     */ 
/*     */       
/* 379 */       if (this.bigNumberMode == 2) {
/* 380 */         addPaxHeadersForBigNumbers(paxHeaders, entry);
/* 381 */       } else if (this.bigNumberMode != 1) {
/* 382 */         failForBigNumbers(entry);
/*     */       } 
/*     */       
/* 385 */       if (this.addPaxHeadersForNonAsciiNames && !paxHeaderContainsPath && 
/* 386 */         !ASCII.canEncode(entryName)) {
/* 387 */         paxHeaders.put("path", entryName);
/*     */       }
/*     */       
/* 390 */       if (this.addPaxHeadersForNonAsciiNames && !paxHeaderContainsLinkPath && (entry
/* 391 */         .isLink() || entry.isSymbolicLink()) && 
/* 392 */         !ASCII.canEncode(linkName)) {
/* 393 */         paxHeaders.put("linkpath", linkName);
/*     */       }
/* 395 */       paxHeaders.putAll(entry.getExtraPaxHeaders());
/*     */       
/* 397 */       if (!paxHeaders.isEmpty()) {
/* 398 */         writePaxHeaders(entry, entryName, paxHeaders);
/*     */       }
/*     */       
/* 401 */       entry.writeEntryHeader(this.recordBuf, this.zipEncoding, (this.bigNumberMode == 1));
/* 402 */       writeRecord(this.recordBuf);
/*     */       
/* 404 */       this.currBytes = 0L;
/*     */       
/* 406 */       if (entry.isDirectory()) {
/* 407 */         this.currSize = 0L;
/*     */       } else {
/* 409 */         this.currSize = entry.getSize();
/*     */       } 
/* 411 */       this.currName = entryName;
/* 412 */       this.haveUnclosedEntry = true;
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
/*     */   public void closeArchiveEntry() throws IOException {
/* 426 */     if (this.finished) {
/* 427 */       throw new IOException("Stream has already been finished");
/*     */     }
/* 429 */     if (!this.haveUnclosedEntry) {
/* 430 */       throw new IOException("No current entry to close");
/*     */     }
/* 432 */     this.out.flushBlock();
/* 433 */     if (this.currBytes < this.currSize) {
/* 434 */       throw new IOException("Entry '" + this.currName + "' closed at '" + this.currBytes + "' before the '" + this.currSize + "' bytes specified in the header were written");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 439 */     this.recordsWritten = ExactMath.add(this.recordsWritten, this.currSize / 512L);
/*     */     
/* 441 */     if (0L != this.currSize % 512L) {
/* 442 */       this.recordsWritten++;
/*     */     }
/* 444 */     this.haveUnclosedEntry = false;
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
/*     */   public void write(byte[] wBuf, int wOffset, int numToWrite) throws IOException {
/* 459 */     if (!this.haveUnclosedEntry) {
/* 460 */       throw new IllegalStateException("No current tar entry");
/*     */     }
/* 462 */     if (this.currBytes + numToWrite > this.currSize) {
/* 463 */       throw new IOException("Request to write '" + numToWrite + "' bytes exceeds size in header of '" + this.currSize + "' bytes for entry '" + this.currName + "'");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 468 */     this.out.write(wBuf, wOffset, numToWrite);
/* 469 */     this.currBytes += numToWrite;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void writePaxHeaders(TarArchiveEntry entry, String entryName, Map<String, String> headers) throws IOException {
/* 480 */     String name = "./PaxHeaders.X/" + stripTo7Bits(entryName);
/* 481 */     if (name.length() >= 100) {
/* 482 */       name = name.substring(0, 99);
/*     */     }
/* 484 */     TarArchiveEntry pex = new TarArchiveEntry(name, (byte)120);
/*     */     
/* 486 */     transferModTime(entry, pex);
/*     */     
/* 488 */     byte[] data = encodeExtendedPaxHeadersContents(headers);
/* 489 */     pex.setSize(data.length);
/* 490 */     putArchiveEntry(pex);
/* 491 */     write(data);
/* 492 */     closeArchiveEntry();
/*     */   }
/*     */   
/*     */   private byte[] encodeExtendedPaxHeadersContents(Map<String, String> headers) {
/* 496 */     StringWriter w = new StringWriter();
/* 497 */     headers.forEach((k, v) -> {
/*     */           int len = k.length() + v.length() + 3 + 2;
/*     */ 
/*     */           
/*     */           String line = len + " " + k + "=" + v + "\n";
/*     */ 
/*     */           
/*     */           int actualLength;
/*     */           
/*     */           for (actualLength = (line.getBytes(StandardCharsets.UTF_8)).length; len != actualLength; actualLength = (line.getBytes(StandardCharsets.UTF_8)).length) {
/*     */             len = actualLength;
/*     */             
/*     */             line = len + " " + k + "=" + v + "\n";
/*     */           } 
/*     */           
/*     */           w.write(line);
/*     */         });
/*     */     
/* 515 */     return w.toString().getBytes(StandardCharsets.UTF_8);
/*     */   }
/*     */   
/*     */   private String stripTo7Bits(String name) {
/* 519 */     int length = name.length();
/* 520 */     StringBuilder result = new StringBuilder(length);
/* 521 */     for (int i = 0; i < length; i++) {
/* 522 */       char stripped = (char)(name.charAt(i) & 0x7F);
/* 523 */       if (shouldBeReplaced(stripped)) {
/* 524 */         result.append("_");
/*     */       } else {
/* 526 */         result.append(stripped);
/*     */       } 
/*     */     } 
/* 529 */     return result.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean shouldBeReplaced(char c) {
/* 537 */     return (c == '\000' || c == '/' || c == '\\');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeEOFRecord() throws IOException {
/* 547 */     Arrays.fill(this.recordBuf, (byte)0);
/* 548 */     writeRecord(this.recordBuf);
/*     */   }
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/* 553 */     this.out.flush();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ArchiveEntry createArchiveEntry(File inputFile, String entryName) throws IOException {
/* 559 */     if (this.finished) {
/* 560 */       throw new IOException("Stream has already been finished");
/*     */     }
/* 562 */     return new TarArchiveEntry(inputFile, entryName);
/*     */   }
/*     */ 
/*     */   
/*     */   public ArchiveEntry createArchiveEntry(Path inputPath, String entryName, LinkOption... options) throws IOException {
/* 567 */     if (this.finished) {
/* 568 */       throw new IOException("Stream has already been finished");
/*     */     }
/* 570 */     return new TarArchiveEntry(inputPath, entryName, options);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeRecord(byte[] record) throws IOException {
/* 580 */     if (record.length != 512) {
/* 581 */       throw new IOException("Record to write has length '" + record.length + "' which is not the record size of '" + 'Ȁ' + "'");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 587 */     this.out.write(record);
/* 588 */     this.recordsWritten++;
/*     */   }
/*     */   
/*     */   private void padAsNeeded() throws IOException {
/* 592 */     int start = this.recordsWritten % this.recordsPerBlock;
/* 593 */     if (start != 0) {
/* 594 */       for (int i = start; i < this.recordsPerBlock; i++) {
/* 595 */         writeEOFRecord();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void addPaxHeadersForBigNumbers(Map<String, String> paxHeaders, TarArchiveEntry entry) {
/* 602 */     addPaxHeaderForBigNumber(paxHeaders, "size", entry.getSize(), 8589934591L);
/*     */     
/* 604 */     addPaxHeaderForBigNumber(paxHeaders, "gid", entry.getLongGroupId(), 2097151L);
/*     */     
/* 606 */     addFileTimePaxHeaderForBigNumber(paxHeaders, "mtime", entry
/* 607 */         .getLastModifiedTime(), 8589934591L);
/* 608 */     addFileTimePaxHeader(paxHeaders, "atime", entry.getLastAccessTime());
/* 609 */     if (entry.getStatusChangeTime() != null) {
/* 610 */       addFileTimePaxHeader(paxHeaders, "ctime", entry.getStatusChangeTime());
/*     */     } else {
/*     */       
/* 613 */       addFileTimePaxHeader(paxHeaders, "ctime", entry.getCreationTime());
/*     */     } 
/* 615 */     addPaxHeaderForBigNumber(paxHeaders, "uid", entry.getLongUserId(), 2097151L);
/*     */ 
/*     */     
/* 618 */     addFileTimePaxHeader(paxHeaders, "LIBARCHIVE.creationtime", entry.getCreationTime());
/*     */     
/* 620 */     addPaxHeaderForBigNumber(paxHeaders, "SCHILY.devmajor", entry
/* 621 */         .getDevMajor(), 2097151L);
/* 622 */     addPaxHeaderForBigNumber(paxHeaders, "SCHILY.devminor", entry
/* 623 */         .getDevMinor(), 2097151L);
/*     */     
/* 625 */     failForBigNumber("mode", entry.getMode(), 2097151L);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void addPaxHeaderForBigNumber(Map<String, String> paxHeaders, String header, long value, long maxValue) {
/* 631 */     if (value < 0L || value > maxValue) {
/* 632 */       paxHeaders.put(header, String.valueOf(value));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void addFileTimePaxHeaderForBigNumber(Map<String, String> paxHeaders, String header, FileTime value, long maxValue) {
/* 639 */     if (value != null) {
/* 640 */       Instant instant = value.toInstant();
/* 641 */       long seconds = instant.getEpochSecond();
/* 642 */       int nanos = instant.getNano();
/* 643 */       if (nanos == 0) {
/* 644 */         addPaxHeaderForBigNumber(paxHeaders, header, seconds, maxValue);
/*     */       } else {
/* 646 */         addInstantPaxHeader(paxHeaders, header, seconds, nanos);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void addFileTimePaxHeader(Map<String, String> paxHeaders, String header, FileTime value) {
/* 653 */     if (value != null) {
/* 654 */       Instant instant = value.toInstant();
/* 655 */       long seconds = instant.getEpochSecond();
/* 656 */       int nanos = instant.getNano();
/* 657 */       if (nanos == 0) {
/* 658 */         paxHeaders.put(header, String.valueOf(seconds));
/*     */       } else {
/* 660 */         addInstantPaxHeader(paxHeaders, header, seconds, nanos);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void addInstantPaxHeader(Map<String, String> paxHeaders, String header, long seconds, int nanos) {
/* 667 */     BigDecimal bdSeconds = BigDecimal.valueOf(seconds);
/* 668 */     BigDecimal bdNanos = BigDecimal.valueOf(nanos).movePointLeft(9).setScale(7, RoundingMode.DOWN);
/* 669 */     BigDecimal timestamp = bdSeconds.add(bdNanos);
/* 670 */     paxHeaders.put(header, timestamp.toPlainString());
/*     */   }
/*     */   
/*     */   private void failForBigNumbers(TarArchiveEntry entry) {
/* 674 */     failForBigNumber("entry size", entry.getSize(), 8589934591L);
/* 675 */     failForBigNumberWithPosixMessage("group id", entry.getLongGroupId(), 2097151L);
/* 676 */     failForBigNumber("last modification time", entry
/* 677 */         .getLastModifiedTime().to(TimeUnit.SECONDS), 8589934591L);
/*     */     
/* 679 */     failForBigNumber("user id", entry.getLongUserId(), 2097151L);
/* 680 */     failForBigNumber("mode", entry.getMode(), 2097151L);
/* 681 */     failForBigNumber("major device number", entry.getDevMajor(), 2097151L);
/*     */     
/* 683 */     failForBigNumber("minor device number", entry.getDevMinor(), 2097151L);
/*     */   }
/*     */ 
/*     */   
/*     */   private void failForBigNumber(String field, long value, long maxValue) {
/* 688 */     failForBigNumber(field, value, maxValue, "");
/*     */   }
/*     */ 
/*     */   
/*     */   private void failForBigNumberWithPosixMessage(String field, long value, long maxValue) {
/* 693 */     failForBigNumber(field, value, maxValue, " Use STAR or POSIX extensions to overcome this limit");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void failForBigNumber(String field, long value, long maxValue, String additionalMsg) {
/* 699 */     if (value < 0L || value > maxValue) {
/* 700 */       throw new IllegalArgumentException(field + " '" + value + "' is too big ( > " + maxValue + " )." + additionalMsg);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean handleLongName(TarArchiveEntry entry, String name, Map<String, String> paxHeaders, String paxHeaderName, byte linkType, String fieldName) throws IOException {
/* 730 */     ByteBuffer encodedName = this.zipEncoding.encode(name);
/* 731 */     int len = encodedName.limit() - encodedName.position();
/* 732 */     if (len >= 100) {
/*     */       
/* 734 */       if (this.longFileMode == 3) {
/* 735 */         paxHeaders.put(paxHeaderName, name);
/* 736 */         return true;
/*     */       } 
/* 738 */       if (this.longFileMode == 2) {
/*     */ 
/*     */         
/* 741 */         TarArchiveEntry longLinkEntry = new TarArchiveEntry("././@LongLink", linkType);
/*     */ 
/*     */         
/* 744 */         longLinkEntry.setSize(len + 1L);
/* 745 */         transferModTime(entry, longLinkEntry);
/* 746 */         putArchiveEntry(longLinkEntry);
/* 747 */         write(encodedName.array(), encodedName.arrayOffset(), len);
/* 748 */         write(0);
/* 749 */         closeArchiveEntry();
/* 750 */       } else if (this.longFileMode != 1) {
/* 751 */         throw new IllegalArgumentException(fieldName + " '" + name + "' is too long ( > " + 'd' + " bytes)");
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 756 */     return false;
/*     */   }
/*     */   
/*     */   private void transferModTime(TarArchiveEntry from, TarArchiveEntry to) {
/* 760 */     long fromModTimeSeconds = from.getLastModifiedTime().to(TimeUnit.SECONDS);
/* 761 */     if (fromModTimeSeconds < 0L || fromModTimeSeconds > 8589934591L) {
/* 762 */       fromModTimeSeconds = 0L;
/*     */     }
/* 764 */     to.setLastModifiedTime(FileTime.from(fromModTimeSeconds, TimeUnit.SECONDS));
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\archivers\tar\TarArchiveOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */