/*     */ package org.apache.commons.compress.archivers.cpio;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import org.apache.commons.compress.archivers.ArchiveEntry;
/*     */ import org.apache.commons.compress.archivers.ArchiveOutputStream;
/*     */ import org.apache.commons.compress.archivers.zip.ZipEncoding;
/*     */ import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
/*     */ import org.apache.commons.compress.utils.ArchiveUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CpioArchiveOutputStream
/*     */   extends ArchiveOutputStream
/*     */   implements CpioConstants
/*     */ {
/*     */   private CpioArchiveEntry entry;
/*     */   private boolean closed;
/*     */   private boolean finished;
/*     */   private final short entryFormat;
/*  82 */   private final HashMap<String, CpioArchiveEntry> names = new HashMap<>();
/*     */ 
/*     */   
/*     */   private long crc;
/*     */   
/*     */   private long written;
/*     */   
/*     */   private final OutputStream out;
/*     */   
/*     */   private final int blockSize;
/*     */   
/*  93 */   private long nextArtificalDeviceAndInode = 1L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final ZipEncoding zipEncoding;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final String encoding;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CpioArchiveOutputStream(OutputStream out, short format) {
/* 114 */     this(out, format, 512, "US-ASCII");
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
/*     */   public CpioArchiveOutputStream(OutputStream out, short format, int blockSize) {
/* 132 */     this(out, format, blockSize, "US-ASCII");
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
/*     */   public CpioArchiveOutputStream(OutputStream out, short format, int blockSize, String encoding) {
/* 153 */     this.out = out;
/* 154 */     switch (format) {
/*     */       case 1:
/*     */       case 2:
/*     */       case 4:
/*     */       case 8:
/*     */         break;
/*     */       default:
/* 161 */         throw new IllegalArgumentException("Unknown format: " + format);
/*     */     } 
/*     */     
/* 164 */     this.entryFormat = format;
/* 165 */     this.blockSize = blockSize;
/* 166 */     this.encoding = encoding;
/* 167 */     this.zipEncoding = ZipEncodingHelper.getZipEncoding(encoding);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CpioArchiveOutputStream(OutputStream out) {
/* 178 */     this(out, (short)1);
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
/*     */   public CpioArchiveOutputStream(OutputStream out, String encoding) {
/* 193 */     this(out, (short)1, 512, encoding);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void ensureOpen() throws IOException {
/* 203 */     if (this.closed) {
/* 204 */       throw new IOException("Stream closed");
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
/*     */   public void putArchiveEntry(ArchiveEntry entry) throws IOException {
/* 224 */     if (this.finished) {
/* 225 */       throw new IOException("Stream has already been finished");
/*     */     }
/*     */     
/* 228 */     CpioArchiveEntry e = (CpioArchiveEntry)entry;
/* 229 */     ensureOpen();
/* 230 */     if (this.entry != null) {
/* 231 */       closeArchiveEntry();
/*     */     }
/* 233 */     if (e.getTime() == -1L) {
/* 234 */       e.setTime(System.currentTimeMillis() / 1000L);
/*     */     }
/*     */     
/* 237 */     short format = e.getFormat();
/* 238 */     if (format != this.entryFormat) {
/* 239 */       throw new IOException("Header format: " + format + " does not match existing format: " + this.entryFormat);
/*     */     }
/*     */     
/* 242 */     if (this.names.put(e.getName(), e) != null) {
/* 243 */       throw new IOException("Duplicate entry: " + e.getName());
/*     */     }
/*     */     
/* 246 */     writeHeader(e);
/* 247 */     this.entry = e;
/* 248 */     this.written = 0L;
/*     */   }
/*     */   private void writeHeader(CpioArchiveEntry e) throws IOException {
/*     */     boolean swapHalfWord;
/* 252 */     switch (e.getFormat()) {
/*     */       case 1:
/* 254 */         this.out.write(ArchiveUtils.toAsciiBytes("070701"));
/* 255 */         count(6);
/* 256 */         writeNewEntry(e);
/*     */         return;
/*     */       case 2:
/* 259 */         this.out.write(ArchiveUtils.toAsciiBytes("070702"));
/* 260 */         count(6);
/* 261 */         writeNewEntry(e);
/*     */         return;
/*     */       case 4:
/* 264 */         this.out.write(ArchiveUtils.toAsciiBytes("070707"));
/* 265 */         count(6);
/* 266 */         writeOldAsciiEntry(e);
/*     */         return;
/*     */       case 8:
/* 269 */         swapHalfWord = true;
/* 270 */         writeBinaryLong(29127L, 2, true);
/* 271 */         writeOldBinaryEntry(e, true);
/*     */         return;
/*     */     } 
/* 274 */     throw new IOException("Unknown format " + e.getFormat());
/*     */   }
/*     */ 
/*     */   
/*     */   private void writeNewEntry(CpioArchiveEntry entry) throws IOException {
/* 279 */     long inode = entry.getInode();
/* 280 */     long devMin = entry.getDeviceMin();
/*     */     
/* 282 */     inode = devMin = 0L;
/*     */     
/* 284 */     inode = this.nextArtificalDeviceAndInode & 0xFFFFFFFFFFFFFFFFL;
/* 285 */     devMin = this.nextArtificalDeviceAndInode++ >> 32L & 0xFFFFFFFFFFFFFFFFL;
/*     */     
/* 287 */     this
/* 288 */       .nextArtificalDeviceAndInode = Math.max(this.nextArtificalDeviceAndInode, inode + 4294967296L * devMin) + 1L;
/*     */ 
/*     */ 
/*     */     
/* 292 */     writeAsciiLong(inode, 8, 16);
/* 293 */     writeAsciiLong(entry.getMode(), 8, 16);
/* 294 */     writeAsciiLong(entry.getUID(), 8, 16);
/* 295 */     writeAsciiLong(entry.getGID(), 8, 16);
/* 296 */     writeAsciiLong(entry.getNumberOfLinks(), 8, 16);
/* 297 */     writeAsciiLong(entry.getTime(), 8, 16);
/* 298 */     writeAsciiLong(entry.getSize(), 8, 16);
/* 299 */     writeAsciiLong(entry.getDeviceMaj(), 8, 16);
/* 300 */     writeAsciiLong(devMin, 8, 16);
/* 301 */     writeAsciiLong(entry.getRemoteDeviceMaj(), 8, 16);
/* 302 */     writeAsciiLong(entry.getRemoteDeviceMin(), 8, 16);
/* 303 */     byte[] name = encode(entry.getName());
/* 304 */     writeAsciiLong(name.length + 1L, 8, 16);
/* 305 */     writeAsciiLong(entry.getChksum(), 8, 16);
/* 306 */     writeCString(name);
/* 307 */     pad(entry.getHeaderPadCount(name.length));
/*     */   }
/*     */ 
/*     */   
/*     */   private void writeOldAsciiEntry(CpioArchiveEntry entry) throws IOException {
/* 312 */     long inode = entry.getInode();
/* 313 */     long device = entry.getDevice();
/*     */     
/* 315 */     inode = device = 0L;
/*     */     
/* 317 */     inode = this.nextArtificalDeviceAndInode & 0x3FFFFL;
/* 318 */     device = this.nextArtificalDeviceAndInode++ >> 18L & 0x3FFFFL;
/*     */     
/* 320 */     this
/* 321 */       .nextArtificalDeviceAndInode = Math.max(this.nextArtificalDeviceAndInode, inode + 262144L * device) + 1L;
/*     */ 
/*     */ 
/*     */     
/* 325 */     writeAsciiLong(device, 6, 8);
/* 326 */     writeAsciiLong(inode, 6, 8);
/* 327 */     writeAsciiLong(entry.getMode(), 6, 8);
/* 328 */     writeAsciiLong(entry.getUID(), 6, 8);
/* 329 */     writeAsciiLong(entry.getGID(), 6, 8);
/* 330 */     writeAsciiLong(entry.getNumberOfLinks(), 6, 8);
/* 331 */     writeAsciiLong(entry.getRemoteDevice(), 6, 8);
/* 332 */     writeAsciiLong(entry.getTime(), 11, 8);
/* 333 */     byte[] name = encode(entry.getName());
/* 334 */     writeAsciiLong(name.length + 1L, 6, 8);
/* 335 */     writeAsciiLong(entry.getSize(), 11, 8);
/* 336 */     writeCString(name);
/*     */   }
/*     */ 
/*     */   
/*     */   private void writeOldBinaryEntry(CpioArchiveEntry entry, boolean swapHalfWord) throws IOException {
/* 341 */     long inode = entry.getInode();
/* 342 */     long device = entry.getDevice();
/*     */     
/* 344 */     inode = device = 0L;
/*     */     
/* 346 */     inode = this.nextArtificalDeviceAndInode & 0xFFFFL;
/* 347 */     device = this.nextArtificalDeviceAndInode++ >> 16L & 0xFFFFL;
/*     */     
/* 349 */     this
/* 350 */       .nextArtificalDeviceAndInode = Math.max(this.nextArtificalDeviceAndInode, inode + 65536L * device) + 1L;
/*     */ 
/*     */ 
/*     */     
/* 354 */     writeBinaryLong(device, 2, swapHalfWord);
/* 355 */     writeBinaryLong(inode, 2, swapHalfWord);
/* 356 */     writeBinaryLong(entry.getMode(), 2, swapHalfWord);
/* 357 */     writeBinaryLong(entry.getUID(), 2, swapHalfWord);
/* 358 */     writeBinaryLong(entry.getGID(), 2, swapHalfWord);
/* 359 */     writeBinaryLong(entry.getNumberOfLinks(), 2, swapHalfWord);
/* 360 */     writeBinaryLong(entry.getRemoteDevice(), 2, swapHalfWord);
/* 361 */     writeBinaryLong(entry.getTime(), 4, swapHalfWord);
/* 362 */     byte[] name = encode(entry.getName());
/* 363 */     writeBinaryLong(name.length + 1L, 2, swapHalfWord);
/* 364 */     writeBinaryLong(entry.getSize(), 4, swapHalfWord);
/* 365 */     writeCString(name);
/* 366 */     pad(entry.getHeaderPadCount(name.length));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void closeArchiveEntry() throws IOException {
/* 377 */     if (this.finished) {
/* 378 */       throw new IOException("Stream has already been finished");
/*     */     }
/*     */     
/* 381 */     ensureOpen();
/*     */     
/* 383 */     if (this.entry == null) {
/* 384 */       throw new IOException("Trying to close non-existent entry");
/*     */     }
/*     */     
/* 387 */     if (this.entry.getSize() != this.written) {
/* 388 */       throw new IOException("Invalid entry size (expected " + this.entry
/* 389 */           .getSize() + " but got " + this.written + " bytes)");
/*     */     }
/*     */     
/* 392 */     pad(this.entry.getDataPadCount());
/* 393 */     if (this.entry.getFormat() == 2 && this.crc != this.entry
/* 394 */       .getChksum()) {
/* 395 */       throw new IOException("CRC Error");
/*     */     }
/* 397 */     this.entry = null;
/* 398 */     this.crc = 0L;
/* 399 */     this.written = 0L;
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
/*     */   public void write(byte[] b, int off, int len) throws IOException {
/* 419 */     ensureOpen();
/* 420 */     if (off < 0 || len < 0 || off > b.length - len) {
/* 421 */       throw new IndexOutOfBoundsException();
/*     */     }
/* 423 */     if (len == 0) {
/*     */       return;
/*     */     }
/*     */     
/* 427 */     if (this.entry == null) {
/* 428 */       throw new IOException("No current CPIO entry");
/*     */     }
/* 430 */     if (this.written + len > this.entry.getSize()) {
/* 431 */       throw new IOException("Attempt to write past end of STORED entry");
/*     */     }
/* 433 */     this.out.write(b, off, len);
/* 434 */     this.written += len;
/* 435 */     if (this.entry.getFormat() == 2) {
/* 436 */       for (int pos = 0; pos < len; pos++) {
/* 437 */         this.crc += (b[pos] & 0xFF);
/* 438 */         this.crc &= 0xFFFFFFFFL;
/*     */       } 
/*     */     }
/* 441 */     count(len);
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
/* 455 */     ensureOpen();
/* 456 */     if (this.finished) {
/* 457 */       throw new IOException("This archive has already been finished");
/*     */     }
/*     */     
/* 460 */     if (this.entry != null) {
/* 461 */       throw new IOException("This archive contains unclosed entries.");
/*     */     }
/* 463 */     this.entry = new CpioArchiveEntry(this.entryFormat);
/* 464 */     this.entry.setName("TRAILER!!!");
/* 465 */     this.entry.setNumberOfLinks(1L);
/* 466 */     writeHeader(this.entry);
/* 467 */     closeArchiveEntry();
/*     */     
/* 469 */     int lengthOfLastBlock = (int)(getBytesWritten() % this.blockSize);
/* 470 */     if (lengthOfLastBlock != 0) {
/* 471 */       pad(this.blockSize - lengthOfLastBlock);
/*     */     }
/*     */     
/* 474 */     this.finished = true;
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
/*     */   public void close() throws IOException {
/*     */     try {
/* 487 */       if (!this.finished) {
/* 488 */         finish();
/*     */       }
/*     */     } finally {
/* 491 */       if (!this.closed) {
/* 492 */         this.out.close();
/* 493 */         this.closed = true;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void pad(int count) throws IOException {
/* 499 */     if (count > 0) {
/* 500 */       byte[] buff = new byte[count];
/* 501 */       this.out.write(buff);
/* 502 */       count(count);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void writeBinaryLong(long number, int length, boolean swapHalfWord) throws IOException {
/* 508 */     byte[] tmp = CpioUtil.long2byteArray(number, length, swapHalfWord);
/* 509 */     this.out.write(tmp);
/* 510 */     count(tmp.length);
/*     */   }
/*     */   
/*     */   private void writeAsciiLong(long number, int length, int radix) throws IOException {
/*     */     String tmpStr;
/* 515 */     StringBuilder tmp = new StringBuilder();
/*     */     
/* 517 */     if (radix == 16) {
/* 518 */       tmp.append(Long.toHexString(number));
/* 519 */     } else if (radix == 8) {
/* 520 */       tmp.append(Long.toOctalString(number));
/*     */     } else {
/* 522 */       tmp.append(number);
/*     */     } 
/*     */     
/* 525 */     if (tmp.length() <= length) {
/* 526 */       int insertLength = length - tmp.length();
/* 527 */       for (int pos = 0; pos < insertLength; pos++) {
/* 528 */         tmp.insert(0, "0");
/*     */       }
/* 530 */       tmpStr = tmp.toString();
/*     */     } else {
/* 532 */       tmpStr = tmp.substring(tmp.length() - length);
/*     */     } 
/* 534 */     byte[] b = ArchiveUtils.toAsciiBytes(tmpStr);
/* 535 */     this.out.write(b);
/* 536 */     count(b.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private byte[] encode(String str) throws IOException {
/* 547 */     ByteBuffer buf = this.zipEncoding.encode(str);
/* 548 */     int len = buf.limit() - buf.position();
/* 549 */     return Arrays.copyOfRange(buf.array(), buf.arrayOffset(), buf.arrayOffset() + len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeCString(byte[] str) throws IOException {
/* 558 */     this.out.write(str);
/* 559 */     this.out.write(0);
/* 560 */     count(str.length + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArchiveEntry createArchiveEntry(File inputFile, String entryName) throws IOException {
/* 571 */     if (this.finished) {
/* 572 */       throw new IOException("Stream has already been finished");
/*     */     }
/* 574 */     return new CpioArchiveEntry(inputFile, entryName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArchiveEntry createArchiveEntry(Path inputPath, String entryName, LinkOption... options) throws IOException {
/* 585 */     if (this.finished) {
/* 586 */       throw new IOException("Stream has already been finished");
/*     */     }
/* 588 */     return new CpioArchiveEntry(inputPath, entryName, options);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\archivers\cpio\CpioArchiveOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */