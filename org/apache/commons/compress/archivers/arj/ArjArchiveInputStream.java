/*     */ package org.apache.commons.compress.archivers.arj;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.zip.CRC32;
/*     */ import org.apache.commons.compress.archivers.ArchiveEntry;
/*     */ import org.apache.commons.compress.archivers.ArchiveException;
/*     */ import org.apache.commons.compress.archivers.ArchiveInputStream;
/*     */ import org.apache.commons.compress.utils.BoundedInputStream;
/*     */ import org.apache.commons.compress.utils.CRC32VerifyingInputStream;
/*     */ import org.apache.commons.compress.utils.Charsets;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ArjArchiveInputStream
/*     */   extends ArchiveInputStream
/*     */ {
/*     */   private static final int ARJ_MAGIC_1 = 96;
/*     */   private static final int ARJ_MAGIC_2 = 234;
/*     */   private final DataInputStream in;
/*     */   private final String charsetName;
/*     */   private final MainHeader mainHeader;
/*     */   private LocalFileHeader currentLocalFileHeader;
/*     */   private InputStream currentInputStream;
/*     */   
/*     */   public ArjArchiveInputStream(InputStream inputStream, String charsetName) throws ArchiveException {
/*  64 */     this.in = new DataInputStream(inputStream);
/*  65 */     this.charsetName = charsetName;
/*     */     try {
/*  67 */       this.mainHeader = readMainHeader();
/*  68 */       if ((this.mainHeader.arjFlags & 0x1) != 0) {
/*  69 */         throw new ArchiveException("Encrypted ARJ files are unsupported");
/*     */       }
/*  71 */       if ((this.mainHeader.arjFlags & 0x4) != 0) {
/*  72 */         throw new ArchiveException("Multi-volume ARJ files are unsupported");
/*     */       }
/*  74 */     } catch (IOException ioException) {
/*  75 */       throw new ArchiveException(ioException.getMessage(), ioException);
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
/*     */   public ArjArchiveInputStream(InputStream inputStream) throws ArchiveException {
/*  87 */     this(inputStream, "CP437");
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*  92 */     this.in.close();
/*     */   }
/*     */   
/*     */   private int read8(DataInputStream dataIn) throws IOException {
/*  96 */     int value = dataIn.readUnsignedByte();
/*  97 */     count(1);
/*  98 */     return value;
/*     */   }
/*     */   
/*     */   private int read16(DataInputStream dataIn) throws IOException {
/* 102 */     int value = dataIn.readUnsignedShort();
/* 103 */     count(2);
/* 104 */     return Integer.reverseBytes(value) >>> 16;
/*     */   }
/*     */   
/*     */   private int read32(DataInputStream dataIn) throws IOException {
/* 108 */     int value = dataIn.readInt();
/* 109 */     count(4);
/* 110 */     return Integer.reverseBytes(value);
/*     */   }
/*     */   
/*     */   private String readString(DataInputStream dataIn) throws IOException {
/* 114 */     try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
/*     */       int nextByte;
/* 116 */       while ((nextByte = dataIn.readUnsignedByte()) != 0) {
/* 117 */         buffer.write(nextByte);
/*     */       }
/* 119 */       return buffer.toString(Charsets.toCharset(this.charsetName).name());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private byte[] readRange(InputStream in, int len) throws IOException {
/* 125 */     byte[] b = IOUtils.readRange(in, len);
/* 126 */     count(b.length);
/* 127 */     if (b.length < len) {
/* 128 */       throw new EOFException();
/*     */     }
/* 130 */     return b;
/*     */   }
/*     */   
/*     */   private byte[] readHeader() throws IOException {
/* 134 */     boolean found = false;
/* 135 */     byte[] basicHeaderBytes = null;
/*     */     while (true) {
/* 137 */       int first = 0;
/* 138 */       int second = read8(this.in);
/*     */       do {
/* 140 */         first = second;
/* 141 */         second = read8(this.in);
/* 142 */       } while (first != 96 && second != 234);
/* 143 */       int basicHeaderSize = read16(this.in);
/* 144 */       if (basicHeaderSize == 0)
/*     */       {
/* 146 */         return null;
/*     */       }
/* 148 */       if (basicHeaderSize <= 2600) {
/* 149 */         basicHeaderBytes = readRange(this.in, basicHeaderSize);
/* 150 */         long basicHeaderCrc32 = read32(this.in) & 0xFFFFFFFFL;
/* 151 */         CRC32 crc32 = new CRC32();
/* 152 */         crc32.update(basicHeaderBytes);
/* 153 */         if (basicHeaderCrc32 == crc32.getValue()) {
/* 154 */           found = true;
/*     */         }
/*     */       } 
/* 157 */       if (found)
/* 158 */         return basicHeaderBytes; 
/*     */     } 
/*     */   }
/*     */   private MainHeader readMainHeader() throws IOException {
/* 162 */     byte[] basicHeaderBytes = readHeader();
/* 163 */     if (basicHeaderBytes == null) {
/* 164 */       throw new IOException("Archive ends without any headers");
/*     */     }
/* 166 */     DataInputStream basicHeader = new DataInputStream(new ByteArrayInputStream(basicHeaderBytes));
/*     */ 
/*     */     
/* 169 */     int firstHeaderSize = basicHeader.readUnsignedByte();
/* 170 */     byte[] firstHeaderBytes = readRange(basicHeader, firstHeaderSize - 1);
/* 171 */     pushedBackBytes(firstHeaderBytes.length);
/*     */     
/* 173 */     DataInputStream firstHeader = new DataInputStream(new ByteArrayInputStream(firstHeaderBytes));
/*     */ 
/*     */     
/* 176 */     MainHeader hdr = new MainHeader();
/* 177 */     hdr.archiverVersionNumber = firstHeader.readUnsignedByte();
/* 178 */     hdr.minVersionToExtract = firstHeader.readUnsignedByte();
/* 179 */     hdr.hostOS = firstHeader.readUnsignedByte();
/* 180 */     hdr.arjFlags = firstHeader.readUnsignedByte();
/* 181 */     hdr.securityVersion = firstHeader.readUnsignedByte();
/* 182 */     hdr.fileType = firstHeader.readUnsignedByte();
/* 183 */     hdr.reserved = firstHeader.readUnsignedByte();
/* 184 */     hdr.dateTimeCreated = read32(firstHeader);
/* 185 */     hdr.dateTimeModified = read32(firstHeader);
/* 186 */     hdr.archiveSize = 0xFFFFFFFFL & read32(firstHeader);
/* 187 */     hdr.securityEnvelopeFilePosition = read32(firstHeader);
/* 188 */     hdr.fileSpecPosition = read16(firstHeader);
/* 189 */     hdr.securityEnvelopeLength = read16(firstHeader);
/* 190 */     pushedBackBytes(20L);
/* 191 */     hdr.encryptionVersion = firstHeader.readUnsignedByte();
/* 192 */     hdr.lastChapter = firstHeader.readUnsignedByte();
/*     */     
/* 194 */     if (firstHeaderSize >= 33) {
/* 195 */       hdr.arjProtectionFactor = firstHeader.readUnsignedByte();
/* 196 */       hdr.arjFlags2 = firstHeader.readUnsignedByte();
/* 197 */       firstHeader.readUnsignedByte();
/* 198 */       firstHeader.readUnsignedByte();
/*     */     } 
/*     */     
/* 201 */     hdr.name = readString(basicHeader);
/* 202 */     hdr.comment = readString(basicHeader);
/*     */     
/* 204 */     int extendedHeaderSize = read16(this.in);
/* 205 */     if (extendedHeaderSize > 0) {
/* 206 */       hdr.extendedHeaderBytes = readRange(this.in, extendedHeaderSize);
/* 207 */       long extendedHeaderCrc32 = 0xFFFFFFFFL & read32(this.in);
/* 208 */       CRC32 crc32 = new CRC32();
/* 209 */       crc32.update(hdr.extendedHeaderBytes);
/* 210 */       if (extendedHeaderCrc32 != crc32.getValue()) {
/* 211 */         throw new IOException("Extended header CRC32 verification failure");
/*     */       }
/*     */     } 
/*     */     
/* 215 */     return hdr;
/*     */   }
/*     */   
/*     */   private LocalFileHeader readLocalFileHeader() throws IOException {
/* 219 */     byte[] basicHeaderBytes = readHeader();
/* 220 */     if (basicHeaderBytes == null) {
/* 221 */       return null;
/*     */     }
/* 223 */     try (DataInputStream basicHeader = new DataInputStream(new ByteArrayInputStream(basicHeaderBytes))) {
/*     */       
/* 225 */       int firstHeaderSize = basicHeader.readUnsignedByte();
/* 226 */       byte[] firstHeaderBytes = readRange(basicHeader, firstHeaderSize - 1);
/* 227 */       pushedBackBytes(firstHeaderBytes.length);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readExtraData(int firstHeaderSize, DataInputStream firstHeader, LocalFileHeader localFileHeader) throws IOException {
/* 274 */     if (firstHeaderSize >= 33) {
/* 275 */       localFileHeader.extendedFilePosition = read32(firstHeader);
/* 276 */       if (firstHeaderSize >= 45) {
/* 277 */         localFileHeader.dateTimeAccessed = read32(firstHeader);
/* 278 */         localFileHeader.dateTimeCreated = read32(firstHeader);
/* 279 */         localFileHeader.originalSizeEvenForVolumes = read32(firstHeader);
/* 280 */         pushedBackBytes(12L);
/*     */       } 
/* 282 */       pushedBackBytes(4L);
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
/*     */   public static boolean matches(byte[] signature, int length) {
/* 296 */     return (length >= 2 && (0xFF & signature[0]) == 96 && (0xFF & signature[1]) == 234);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getArchiveName() {
/* 306 */     return this.mainHeader.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getArchiveComment() {
/* 314 */     return this.mainHeader.comment;
/*     */   }
/*     */ 
/*     */   
/*     */   public ArjArchiveEntry getNextEntry() throws IOException {
/* 319 */     if (this.currentInputStream != null) {
/*     */       
/* 321 */       IOUtils.skip(this.currentInputStream, Long.MAX_VALUE);
/* 322 */       this.currentInputStream.close();
/* 323 */       this.currentLocalFileHeader = null;
/* 324 */       this.currentInputStream = null;
/*     */     } 
/*     */     
/* 327 */     this.currentLocalFileHeader = readLocalFileHeader();
/* 328 */     if (this.currentLocalFileHeader != null) {
/* 329 */       this.currentInputStream = (InputStream)new BoundedInputStream(this.in, this.currentLocalFileHeader.compressedSize);
/* 330 */       if (this.currentLocalFileHeader.method == 0) {
/* 331 */         this.currentInputStream = (InputStream)new CRC32VerifyingInputStream(this.currentInputStream, this.currentLocalFileHeader.originalSize, this.currentLocalFileHeader.originalCrc32);
/*     */       }
/*     */       
/* 334 */       return new ArjArchiveEntry(this.currentLocalFileHeader);
/*     */     } 
/* 336 */     this.currentInputStream = null;
/* 337 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canReadEntryData(ArchiveEntry ae) {
/* 342 */     return (ae instanceof ArjArchiveEntry && ((ArjArchiveEntry)ae)
/* 343 */       .getMethod() == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] b, int off, int len) throws IOException {
/* 348 */     if (len == 0) {
/* 349 */       return 0;
/*     */     }
/* 351 */     if (this.currentLocalFileHeader == null) {
/* 352 */       throw new IllegalStateException("No current arj entry");
/*     */     }
/* 354 */     if (this.currentLocalFileHeader.method != 0) {
/* 355 */       throw new IOException("Unsupported compression method " + this.currentLocalFileHeader.method);
/*     */     }
/* 357 */     return this.currentInputStream.read(b, off, len);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\archivers\arj\ArjArchiveInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */