/*     */ package org.apache.commons.compress.compressors.gzip;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataInput;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.zip.CRC32;
/*     */ import java.util.zip.DataFormatException;
/*     */ import java.util.zip.Inflater;
/*     */ import org.apache.commons.compress.compressors.CompressorInputStream;
/*     */ import org.apache.commons.compress.utils.ByteUtils;
/*     */ import org.apache.commons.compress.utils.CountingInputStream;
/*     */ import org.apache.commons.compress.utils.IOUtils;
/*     */ import org.apache.commons.compress.utils.InputStreamStatistics;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GzipCompressorInputStream
/*     */   extends CompressorInputStream
/*     */   implements InputStreamStatistics
/*     */ {
/*     */   private static final int FHCRC = 2;
/*     */   private static final int FEXTRA = 4;
/*     */   private static final int FNAME = 8;
/*     */   private static final int FCOMMENT = 16;
/*     */   private static final int FRESERVED = 224;
/*     */   private final CountingInputStream countingStream;
/*     */   private final InputStream in;
/*     */   private final boolean decompressConcatenated;
/*  98 */   private final byte[] buf = new byte[8192];
/*     */ 
/*     */   
/*     */   private int bufUsed;
/*     */ 
/*     */   
/* 104 */   private Inflater inf = new Inflater(true);
/*     */ 
/*     */   
/* 107 */   private final CRC32 crc = new CRC32();
/*     */ 
/*     */   
/*     */   private boolean endReached;
/*     */ 
/*     */   
/* 113 */   private final byte[] oneByte = new byte[1];
/*     */   
/* 115 */   private final GzipParameters parameters = new GzipParameters();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GzipCompressorInputStream(InputStream inputStream) throws IOException {
/* 132 */     this(inputStream, false);
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
/*     */   public GzipCompressorInputStream(InputStream inputStream, boolean decompressConcatenated) throws IOException {
/* 158 */     this.countingStream = new CountingInputStream(inputStream);
/*     */ 
/*     */     
/* 161 */     if (this.countingStream.markSupported()) {
/* 162 */       this.in = (InputStream)this.countingStream;
/*     */     } else {
/* 164 */       this.in = new BufferedInputStream((InputStream)this.countingStream);
/*     */     } 
/*     */     
/* 167 */     this.decompressConcatenated = decompressConcatenated;
/* 168 */     init(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GzipParameters getMetaData() {
/* 178 */     return this.parameters;
/*     */   }
/*     */   
/*     */   private boolean init(boolean isFirstMember) throws IOException {
/* 182 */     assert isFirstMember || this.decompressConcatenated;
/*     */ 
/*     */     
/* 185 */     int magic0 = this.in.read();
/*     */ 
/*     */ 
/*     */     
/* 189 */     if (magic0 == -1 && !isFirstMember) {
/* 190 */       return false;
/*     */     }
/*     */     
/* 193 */     if (magic0 != 31 || this.in.read() != 139) {
/* 194 */       throw new IOException(isFirstMember ? "Input is not in the .gz format" : "Garbage after a valid .gz stream");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 200 */     DataInput inData = new DataInputStream(this.in);
/* 201 */     int method = inData.readUnsignedByte();
/* 202 */     if (method != 8) {
/* 203 */       throw new IOException("Unsupported compression method " + method + " in the .gz header");
/*     */     }
/*     */ 
/*     */     
/* 207 */     int flg = inData.readUnsignedByte();
/* 208 */     if ((flg & 0xE0) != 0) {
/* 209 */       throw new IOException("Reserved flags are set in the .gz header");
/*     */     }
/*     */ 
/*     */     
/* 213 */     this.parameters.setModificationTime(ByteUtils.fromLittleEndian(inData, 4) * 1000L);
/* 214 */     switch (inData.readUnsignedByte()) {
/*     */       case 2:
/* 216 */         this.parameters.setCompressionLevel(9);
/*     */         break;
/*     */       case 4:
/* 219 */         this.parameters.setCompressionLevel(1);
/*     */         break;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 225 */     this.parameters.setOperatingSystem(inData.readUnsignedByte());
/*     */ 
/*     */     
/* 228 */     if ((flg & 0x4) != 0) {
/* 229 */       int xlen = inData.readUnsignedByte();
/* 230 */       xlen |= inData.readUnsignedByte() << 8;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 235 */       while (xlen-- > 0) {
/* 236 */         inData.readUnsignedByte();
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 241 */     if ((flg & 0x8) != 0) {
/* 242 */       this.parameters.setFilename(new String(readToNull(inData), StandardCharsets.ISO_8859_1));
/*     */     }
/*     */ 
/*     */     
/* 246 */     if ((flg & 0x10) != 0) {
/* 247 */       this.parameters.setComment(new String(readToNull(inData), StandardCharsets.ISO_8859_1));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 255 */     if ((flg & 0x2) != 0) {
/* 256 */       inData.readShort();
/*     */     }
/*     */ 
/*     */     
/* 260 */     this.inf.reset();
/* 261 */     this.crc.reset();
/*     */     
/* 263 */     return true;
/*     */   }
/*     */   
/*     */   private static byte[] readToNull(DataInput inData) throws IOException {
/* 267 */     try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
/* 268 */       int b = 0;
/* 269 */       while ((b = inData.readUnsignedByte()) != 0) {
/* 270 */         bos.write(b);
/*     */       }
/* 272 */       return bos.toByteArray();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/* 278 */     return (read(this.oneByte, 0, 1) == -1) ? -1 : (this.oneByte[0] & 0xFF);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int read(byte[] b, int off, int len) throws IOException {
/* 288 */     if (len == 0) {
/* 289 */       return 0;
/*     */     }
/* 291 */     if (this.endReached) {
/* 292 */       return -1;
/*     */     }
/*     */     
/* 295 */     int size = 0;
/*     */     
/* 297 */     while (len > 0) {
/* 298 */       int ret; if (this.inf.needsInput()) {
/*     */ 
/*     */         
/* 301 */         this.in.mark(this.buf.length);
/*     */         
/* 303 */         this.bufUsed = this.in.read(this.buf);
/* 304 */         if (this.bufUsed == -1) {
/* 305 */           throw new EOFException();
/*     */         }
/*     */         
/* 308 */         this.inf.setInput(this.buf, 0, this.bufUsed);
/*     */       } 
/*     */ 
/*     */       
/*     */       try {
/* 313 */         ret = this.inf.inflate(b, off, len);
/* 314 */       } catch (DataFormatException e) {
/* 315 */         throw new IOException("Gzip-compressed data is corrupt");
/*     */       } 
/*     */       
/* 318 */       this.crc.update(b, off, ret);
/* 319 */       off += ret;
/* 320 */       len -= ret;
/* 321 */       size += ret;
/* 322 */       count(ret);
/*     */       
/* 324 */       if (this.inf.finished()) {
/*     */ 
/*     */         
/* 327 */         this.in.reset();
/*     */         
/* 329 */         int skipAmount = this.bufUsed - this.inf.getRemaining();
/* 330 */         if (IOUtils.skip(this.in, skipAmount) != skipAmount) {
/* 331 */           throw new IOException();
/*     */         }
/*     */         
/* 334 */         this.bufUsed = 0;
/*     */         
/* 336 */         DataInput inData = new DataInputStream(this.in);
/*     */ 
/*     */         
/* 339 */         long crcStored = ByteUtils.fromLittleEndian(inData, 4);
/*     */         
/* 341 */         if (crcStored != this.crc.getValue()) {
/* 342 */           throw new IOException("Gzip-compressed data is corrupt (CRC32 error)");
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 347 */         long isize = ByteUtils.fromLittleEndian(inData, 4);
/*     */         
/* 349 */         if (isize != (this.inf.getBytesWritten() & 0xFFFFFFFFL)) {
/* 350 */           throw new IOException("Gzip-compressed data is corrupt(uncompressed size mismatch)");
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 355 */         if (!this.decompressConcatenated || !init(false)) {
/* 356 */           this.inf.end();
/* 357 */           this.inf = null;
/* 358 */           this.endReached = true;
/* 359 */           return (size == 0) ? -1 : size;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 364 */     return size;
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
/*     */   public static boolean matches(byte[] signature, int length) {
/* 377 */     return (length >= 2 && signature[0] == 31 && signature[1] == -117);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 387 */     if (this.inf != null) {
/* 388 */       this.inf.end();
/* 389 */       this.inf = null;
/*     */     } 
/*     */     
/* 392 */     if (this.in != System.in) {
/* 393 */       this.in.close();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getCompressedCount() {
/* 402 */     return this.countingStream.getBytesRead();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\compressors\gzip\GzipCompressorInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */