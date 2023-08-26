/*     */ package org.apache.commons.compress.archivers.tar;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.UncheckedIOException;
/*     */ import java.math.BigInteger;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TarUtils
/*     */ {
/*     */   private static final int BYTE_MASK = 255;
/*  56 */   static final ZipEncoding DEFAULT_ENCODING = ZipEncodingHelper.getZipEncoding(null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   static final ZipEncoding FALLBACK_ENCODING = new ZipEncoding() {
/*     */       public boolean canEncode(String name) {
/*  64 */         return true;
/*     */       }
/*     */       
/*     */       public ByteBuffer encode(String name) {
/*  68 */         int length = name.length();
/*  69 */         byte[] buf = new byte[length];
/*     */ 
/*     */         
/*  72 */         for (int i = 0; i < length; i++) {
/*  73 */           buf[i] = (byte)name.charAt(i);
/*     */         }
/*  75 */         return ByteBuffer.wrap(buf);
/*     */       }
/*     */ 
/*     */       
/*     */       public String decode(byte[] buffer) {
/*  80 */         int length = buffer.length;
/*  81 */         StringBuilder result = new StringBuilder(length);
/*     */         
/*  83 */         for (byte b : buffer) {
/*  84 */           if (b == 0) {
/*     */             break;
/*     */           }
/*  87 */           result.append((char)(b & 0xFF));
/*     */         } 
/*     */         
/*  90 */         return result.toString();
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long parseOctal(byte[] buffer, int offset, int length) {
/* 120 */     long result = 0L;
/* 121 */     int end = offset + length;
/* 122 */     int start = offset;
/*     */     
/* 124 */     if (length < 2) {
/* 125 */       throw new IllegalArgumentException("Length " + length + " must be at least 2");
/*     */     }
/*     */     
/* 128 */     if (buffer[start] == 0) {
/* 129 */       return 0L;
/*     */     }
/*     */ 
/*     */     
/* 133 */     while (start < end && 
/* 134 */       buffer[start] == 32)
/*     */     {
/*     */       
/* 137 */       start++;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 144 */     byte trailer = buffer[end - 1];
/* 145 */     while (start < end && (trailer == 0 || trailer == 32)) {
/* 146 */       end--;
/* 147 */       trailer = buffer[end - 1];
/*     */     } 
/*     */     
/* 150 */     for (; start < end; start++) {
/* 151 */       byte currentByte = buffer[start];
/*     */       
/* 153 */       if (currentByte < 48 || currentByte > 55) {
/* 154 */         throw new IllegalArgumentException(exceptionMessage(buffer, offset, length, start, currentByte));
/*     */       }
/* 156 */       result = (result << 3L) + (currentByte - 48);
/*     */     } 
/*     */ 
/*     */     
/* 160 */     return result;
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
/*     */   public static long parseOctalOrBinary(byte[] buffer, int offset, int length) {
/* 183 */     if ((buffer[offset] & 0x80) == 0) {
/* 184 */       return parseOctal(buffer, offset, length);
/*     */     }
/* 186 */     boolean negative = (buffer[offset] == -1);
/* 187 */     if (length < 9) {
/* 188 */       return parseBinaryLong(buffer, offset, length, negative);
/*     */     }
/* 190 */     return parseBinaryBigInteger(buffer, offset, length, negative);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static long parseBinaryLong(byte[] buffer, int offset, int length, boolean negative) {
/* 196 */     if (length >= 9) {
/* 197 */       throw new IllegalArgumentException("At offset " + offset + ", " + length + " byte binary number exceeds maximum signed long value");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 202 */     long val = 0L;
/* 203 */     for (int i = 1; i < length; i++) {
/* 204 */       val = (val << 8L) + (buffer[offset + i] & 0xFF);
/*     */     }
/* 206 */     if (negative) {
/*     */       
/* 208 */       val--;
/* 209 */       val ^= (long)Math.pow(2.0D, (length - 1) * 8.0D) - 1L;
/*     */     } 
/* 211 */     return negative ? -val : val;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static long parseBinaryBigInteger(byte[] buffer, int offset, int length, boolean negative) {
/* 218 */     byte[] remainder = new byte[length - 1];
/* 219 */     System.arraycopy(buffer, offset + 1, remainder, 0, length - 1);
/* 220 */     BigInteger val = new BigInteger(remainder);
/* 221 */     if (negative)
/*     */     {
/* 223 */       val = val.add(BigInteger.valueOf(-1L)).not();
/*     */     }
/* 225 */     if (val.bitLength() > 63) {
/* 226 */       throw new IllegalArgumentException("At offset " + offset + ", " + length + " byte binary number exceeds maximum signed long value");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 231 */     return negative ? -val.longValue() : val.longValue();
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
/*     */   public static boolean parseBoolean(byte[] buffer, int offset) {
/* 245 */     return (buffer[offset] == 1);
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
/*     */   private static String exceptionMessage(byte[] buffer, int offset, int length, int current, byte currentByte) {
/* 258 */     String string = new String(buffer, offset, length, Charset.defaultCharset());
/*     */     
/* 260 */     string = string.replace("\000", "{NUL}");
/* 261 */     return "Invalid byte " + currentByte + " at offset " + (current - offset) + " in '" + string + "' len=" + length;
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
/*     */   public static String parseName(byte[] buffer, int offset, int length) {
/*     */     try {
/* 276 */       return parseName(buffer, offset, length, DEFAULT_ENCODING);
/* 277 */     } catch (IOException ex) {
/*     */       try {
/* 279 */         return parseName(buffer, offset, length, FALLBACK_ENCODING);
/* 280 */       } catch (IOException ex2) {
/*     */         
/* 282 */         throw new UncheckedIOException(ex2);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String parseName(byte[] buffer, int offset, int length, ZipEncoding encoding) throws IOException {
/* 305 */     int len = 0;
/* 306 */     for (int i = offset; len < length && buffer[i] != 0; i++) {
/* 307 */       len++;
/*     */     }
/* 309 */     if (len > 0) {
/* 310 */       byte[] b = new byte[len];
/* 311 */       System.arraycopy(buffer, offset, b, 0, len);
/* 312 */       return encoding.decode(b);
/*     */     } 
/* 314 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static TarArchiveStructSparse parseSparse(byte[] buffer, int offset) {
/* 325 */     long sparseOffset = parseOctalOrBinary(buffer, offset, 12);
/* 326 */     long sparseNumbytes = parseOctalOrBinary(buffer, offset + 12, 12);
/*     */     
/* 328 */     return new TarArchiveStructSparse(sparseOffset, sparseNumbytes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static List<TarArchiveStructSparse> readSparseStructs(byte[] buffer, int offset, int entries) throws IOException {
/* 336 */     List<TarArchiveStructSparse> sparseHeaders = new ArrayList<>();
/* 337 */     for (int i = 0; i < entries; i++) {
/*     */       
/*     */       try {
/* 340 */         TarArchiveStructSparse sparseHeader = parseSparse(buffer, offset + i * 24);
/*     */         
/* 342 */         if (sparseHeader.getOffset() < 0L) {
/* 343 */           throw new IOException("Corrupted TAR archive, sparse entry with negative offset");
/*     */         }
/* 345 */         if (sparseHeader.getNumbytes() < 0L) {
/* 346 */           throw new IOException("Corrupted TAR archive, sparse entry with negative numbytes");
/*     */         }
/* 348 */         sparseHeaders.add(sparseHeader);
/* 349 */       } catch (IllegalArgumentException ex) {
/*     */         
/* 351 */         throw new IOException("Corrupted TAR archive, sparse entry is invalid", ex);
/*     */       } 
/*     */     } 
/* 354 */     return Collections.unmodifiableList(sparseHeaders);
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
/*     */   public static int formatNameBytes(String name, byte[] buf, int offset, int length) {
/*     */     try {
/* 374 */       return formatNameBytes(name, buf, offset, length, DEFAULT_ENCODING);
/* 375 */     } catch (IOException ex) {
/*     */       try {
/* 377 */         return formatNameBytes(name, buf, offset, length, FALLBACK_ENCODING);
/*     */       }
/* 379 */       catch (IOException ex2) {
/*     */         
/* 381 */         throw new UncheckedIOException(ex2);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int formatNameBytes(String name, byte[] buf, int offset, int length, ZipEncoding encoding) throws IOException {
/* 408 */     int len = name.length();
/* 409 */     ByteBuffer b = encoding.encode(name);
/* 410 */     while (b.limit() > length && len > 0) {
/* 411 */       b = encoding.encode(name.substring(0, --len));
/*     */     }
/* 413 */     int limit = b.limit() - b.position();
/* 414 */     System.arraycopy(b.array(), b.arrayOffset(), buf, offset, limit);
/*     */ 
/*     */     
/* 417 */     for (int i = limit; i < length; i++) {
/* 418 */       buf[offset + i] = 0;
/*     */     }
/*     */     
/* 421 */     return offset + length;
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
/*     */   public static void formatUnsignedOctalString(long value, byte[] buffer, int offset, int length) {
/* 435 */     int remaining = length;
/* 436 */     remaining--;
/* 437 */     if (value == 0L) {
/* 438 */       buffer[offset + remaining--] = 48;
/*     */     } else {
/* 440 */       long val = value;
/* 441 */       for (; remaining >= 0 && val != 0L; remaining--) {
/*     */         
/* 443 */         buffer[offset + remaining] = (byte)(48 + (byte)(int)(val & 0x7L));
/* 444 */         val >>>= 3L;
/*     */       } 
/*     */       
/* 447 */       if (val != 0L) {
/* 448 */         throw new IllegalArgumentException(value + "=" + 
/* 449 */             Long.toOctalString(value) + " will not fit in octal number buffer of length " + length);
/*     */       }
/*     */     } 
/*     */     
/* 453 */     for (; remaining >= 0; remaining--) {
/* 454 */       buffer[offset + remaining] = 48;
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
/*     */   public static int formatOctalBytes(long value, byte[] buf, int offset, int length) {
/* 474 */     int idx = length - 2;
/* 475 */     formatUnsignedOctalString(value, buf, offset, idx);
/*     */     
/* 477 */     buf[offset + idx++] = 32;
/* 478 */     buf[offset + idx] = 0;
/*     */     
/* 480 */     return offset + length;
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
/*     */   public static int formatLongOctalBytes(long value, byte[] buf, int offset, int length) {
/* 499 */     int idx = length - 1;
/*     */     
/* 501 */     formatUnsignedOctalString(value, buf, offset, idx);
/* 502 */     buf[offset + idx] = 32;
/*     */     
/* 504 */     return offset + length;
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
/*     */   public static int formatLongOctalOrBinaryBytes(long value, byte[] buf, int offset, int length) {
/* 528 */     long maxAsOctalChar = (length == 8) ? 2097151L : 8589934591L;
/*     */     
/* 530 */     boolean negative = (value < 0L);
/* 531 */     if (!negative && value <= maxAsOctalChar) {
/* 532 */       return formatLongOctalBytes(value, buf, offset, length);
/*     */     }
/*     */     
/* 535 */     if (length < 9) {
/* 536 */       formatLongBinary(value, buf, offset, length, negative);
/*     */     } else {
/* 538 */       formatBigIntegerBinary(value, buf, offset, length, negative);
/*     */     } 
/*     */     
/* 541 */     buf[offset] = (byte)(negative ? 255 : 128);
/* 542 */     return offset + length;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void formatLongBinary(long value, byte[] buf, int offset, int length, boolean negative) {
/* 548 */     int bits = (length - 1) * 8;
/* 549 */     long max = 1L << bits;
/* 550 */     long val = Math.abs(value);
/* 551 */     if (val < 0L || val >= max) {
/* 552 */       throw new IllegalArgumentException("Value " + value + " is too large for " + length + " byte field.");
/*     */     }
/*     */     
/* 555 */     if (negative) {
/* 556 */       val ^= max - 1L;
/* 557 */       val++;
/* 558 */       val |= 255L << bits;
/*     */     } 
/* 560 */     for (int i = offset + length - 1; i >= offset; i--) {
/* 561 */       buf[i] = (byte)(int)val;
/* 562 */       val >>= 8L;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void formatBigIntegerBinary(long value, byte[] buf, int offset, int length, boolean negative) {
/* 570 */     BigInteger val = BigInteger.valueOf(value);
/* 571 */     byte[] b = val.toByteArray();
/* 572 */     int len = b.length;
/* 573 */     if (len > length - 1) {
/* 574 */       throw new IllegalArgumentException("Value " + value + " is too large for " + length + " byte field.");
/*     */     }
/*     */     
/* 577 */     int off = offset + length - len;
/* 578 */     System.arraycopy(b, 0, buf, off, len);
/* 579 */     byte fill = (byte)(negative ? 255 : 0);
/* 580 */     for (int i = offset + 1; i < off; i++) {
/* 581 */       buf[i] = fill;
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
/*     */   public static int formatCheckSumOctalBytes(long value, byte[] buf, int offset, int length) {
/* 601 */     int idx = length - 2;
/* 602 */     formatUnsignedOctalString(value, buf, offset, idx);
/*     */     
/* 604 */     buf[offset + idx++] = 0;
/* 605 */     buf[offset + idx] = 32;
/*     */     
/* 607 */     return offset + length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long computeCheckSum(byte[] buf) {
/* 617 */     long sum = 0L;
/*     */     
/* 619 */     for (byte element : buf) {
/* 620 */       sum += (0xFF & element);
/*     */     }
/*     */     
/* 623 */     return sum;
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
/*     */   public static boolean verifyCheckSum(byte[] header) {
/* 652 */     long storedSum = parseOctal(header, 148, 8);
/* 653 */     long unsignedSum = 0L;
/* 654 */     long signedSum = 0L;
/*     */     
/* 656 */     for (int i = 0; i < header.length; i++) {
/* 657 */       byte b = header[i];
/* 658 */       if (148 <= i && i < 156) {
/* 659 */         b = 32;
/*     */       }
/* 661 */       unsignedSum += (0xFF & b);
/* 662 */       signedSum += b;
/*     */     } 
/* 664 */     return (storedSum == unsignedSum || storedSum == signedSum);
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
/*     */   @Deprecated
/*     */   protected static Map<String, String> parsePaxHeaders(InputStream inputStream, List<TarArchiveStructSparse> sparseHeaders, Map<String, String> globalPaxHeaders) throws IOException {
/* 694 */     return parsePaxHeaders(inputStream, sparseHeaders, globalPaxHeaders, -1L);
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
/*     */   protected static Map<String, String> parsePaxHeaders(InputStream inputStream, List<TarArchiveStructSparse> sparseHeaders, Map<String, String> globalPaxHeaders, long headerSize) throws IOException {
/*     */     int ch;
/* 725 */     Map<String, String> headers = new HashMap<>(globalPaxHeaders);
/* 726 */     Long offset = null;
/*     */     
/* 728 */     int totalRead = 0;
/*     */     
/*     */     do {
/* 731 */       int len = 0;
/* 732 */       int read = 0;
/* 733 */       while ((ch = inputStream.read()) != -1) {
/* 734 */         read++;
/* 735 */         totalRead++;
/* 736 */         if (ch == 10) {
/*     */           break;
/*     */         }
/* 739 */         if (ch == 32) {
/*     */           
/* 741 */           ByteArrayOutputStream coll = new ByteArrayOutputStream();
/* 742 */           while ((ch = inputStream.read()) != -1) {
/* 743 */             read++;
/* 744 */             totalRead++;
/* 745 */             if (totalRead < 0 || (headerSize >= 0L && totalRead >= headerSize)) {
/*     */               break;
/*     */             }
/* 748 */             if (ch == 61) {
/* 749 */               String keyword = coll.toString("UTF-8");
/*     */               
/* 751 */               int restLen = len - read;
/* 752 */               if (restLen <= 1) {
/* 753 */                 headers.remove(keyword); break;
/* 754 */               }  if (headerSize >= 0L && restLen > headerSize - totalRead) {
/* 755 */                 throw new IOException("Paxheader value size " + restLen + " exceeds size of header record");
/*     */               }
/*     */               
/* 758 */               byte[] rest = IOUtils.readRange(inputStream, restLen);
/* 759 */               int got = rest.length;
/* 760 */               if (got != restLen) {
/* 761 */                 throw new IOException("Failed to read Paxheader. Expected " + restLen + " bytes, read " + got);
/*     */               }
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 767 */               totalRead += restLen;
/*     */               
/* 769 */               if (rest[restLen - 1] != 10) {
/* 770 */                 throw new IOException("Failed to read Paxheader.Value should end with a newline");
/*     */               }
/*     */               
/* 773 */               String value = new String(rest, 0, restLen - 1, StandardCharsets.UTF_8);
/* 774 */               headers.put(keyword, value);
/*     */ 
/*     */               
/* 777 */               if (keyword.equals("GNU.sparse.offset")) {
/* 778 */                 if (offset != null)
/*     */                 {
/* 780 */                   sparseHeaders.add(new TarArchiveStructSparse(offset.longValue(), 0L));
/*     */                 }
/*     */                 try {
/* 783 */                   offset = Long.valueOf(value);
/* 784 */                 } catch (NumberFormatException ex) {
/* 785 */                   throw new IOException("Failed to read Paxheader.GNU.sparse.offset contains a non-numeric value");
/*     */                 } 
/*     */                 
/* 788 */                 if (offset.longValue() < 0L) {
/* 789 */                   throw new IOException("Failed to read Paxheader.GNU.sparse.offset contains negative value");
/*     */                 }
/*     */               } 
/*     */ 
/*     */ 
/*     */               
/* 795 */               if (keyword.equals("GNU.sparse.numbytes")) {
/* 796 */                 long numbytes; if (offset == null) {
/* 797 */                   throw new IOException("Failed to read Paxheader.GNU.sparse.offset is expected before GNU.sparse.numbytes shows up.");
/*     */                 }
/*     */ 
/*     */                 
/*     */                 try {
/* 802 */                   numbytes = Long.parseLong(value);
/* 803 */                 } catch (NumberFormatException ex) {
/* 804 */                   throw new IOException("Failed to read Paxheader.GNU.sparse.numbytes contains a non-numeric value.");
/*     */                 } 
/*     */                 
/* 807 */                 if (numbytes < 0L) {
/* 808 */                   throw new IOException("Failed to read Paxheader.GNU.sparse.numbytes contains negative value");
/*     */                 }
/*     */                 
/* 811 */                 sparseHeaders.add(new TarArchiveStructSparse(offset.longValue(), numbytes));
/* 812 */                 offset = null;
/*     */               } 
/*     */               
/*     */               break;
/*     */             } 
/* 817 */             coll.write((byte)ch);
/*     */           } 
/*     */           
/*     */           break;
/*     */         } 
/*     */         
/* 823 */         if (ch < 48 || ch > 57) {
/* 824 */           throw new IOException("Failed to read Paxheader. Encountered a non-number while reading length");
/*     */         }
/*     */         
/* 827 */         len *= 10;
/* 828 */         len += ch - 48;
/*     */       } 
/* 830 */     } while (ch != -1);
/*     */ 
/*     */ 
/*     */     
/* 834 */     if (offset != null)
/*     */     {
/* 836 */       sparseHeaders.add(new TarArchiveStructSparse(offset.longValue(), 0L));
/*     */     }
/* 838 */     return headers;
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
/*     */   protected static List<TarArchiveStructSparse> parsePAX01SparseHeaders(String sparseMap) {
/*     */     try {
/* 856 */       return parseFromPAX01SparseHeaders(sparseMap);
/* 857 */     } catch (IOException ex) {
/* 858 */       throw new UncheckedIOException(ex.getMessage(), ex);
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
/*     */   protected static List<TarArchiveStructSparse> parseFromPAX01SparseHeaders(String sparseMap) throws IOException {
/* 874 */     List<TarArchiveStructSparse> sparseHeaders = new ArrayList<>();
/* 875 */     String[] sparseHeaderStrings = sparseMap.split(",");
/* 876 */     if (sparseHeaderStrings.length % 2 == 1) {
/* 877 */       throw new IOException("Corrupted TAR archive. Bad format in GNU.sparse.map PAX Header");
/*     */     }
/*     */     
/* 880 */     for (int i = 0; i < sparseHeaderStrings.length; i += 2) {
/*     */       long sparseOffset, sparseNumbytes;
/*     */       try {
/* 883 */         sparseOffset = Long.parseLong(sparseHeaderStrings[i]);
/* 884 */       } catch (NumberFormatException ex) {
/* 885 */         throw new IOException("Corrupted TAR archive. Sparse struct offset contains a non-numeric value");
/*     */       } 
/*     */       
/* 888 */       if (sparseOffset < 0L) {
/* 889 */         throw new IOException("Corrupted TAR archive. Sparse struct offset contains negative value");
/*     */       }
/*     */ 
/*     */       
/*     */       try {
/* 894 */         sparseNumbytes = Long.parseLong(sparseHeaderStrings[i + 1]);
/* 895 */       } catch (NumberFormatException ex) {
/* 896 */         throw new IOException("Corrupted TAR archive. Sparse struct numbytes contains a non-numeric value");
/*     */       } 
/*     */       
/* 899 */       if (sparseNumbytes < 0L) {
/* 900 */         throw new IOException("Corrupted TAR archive. Sparse struct numbytes contains negative value");
/*     */       }
/*     */       
/* 903 */       sparseHeaders.add(new TarArchiveStructSparse(sparseOffset, sparseNumbytes));
/*     */     } 
/*     */     
/* 906 */     return Collections.unmodifiableList(sparseHeaders);
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
/*     */   protected static List<TarArchiveStructSparse> parsePAX1XSparseHeaders(InputStream inputStream, int recordSize) throws IOException {
/* 922 */     List<TarArchiveStructSparse> sparseHeaders = new ArrayList<>();
/* 923 */     long bytesRead = 0L;
/*     */     
/* 925 */     long[] readResult = readLineOfNumberForPax1X(inputStream);
/* 926 */     long sparseHeadersCount = readResult[0];
/* 927 */     if (sparseHeadersCount < 0L)
/*     */     {
/* 929 */       throw new IOException("Corrupted TAR archive. Negative value in sparse headers block");
/*     */     }
/* 931 */     bytesRead += readResult[1];
/* 932 */     while (sparseHeadersCount-- > 0L) {
/* 933 */       readResult = readLineOfNumberForPax1X(inputStream);
/* 934 */       long sparseOffset = readResult[0];
/* 935 */       if (sparseOffset < 0L) {
/* 936 */         throw new IOException("Corrupted TAR archive. Sparse header block offset contains negative value");
/*     */       }
/*     */       
/* 939 */       bytesRead += readResult[1];
/*     */       
/* 941 */       readResult = readLineOfNumberForPax1X(inputStream);
/* 942 */       long sparseNumbytes = readResult[0];
/* 943 */       if (sparseNumbytes < 0L) {
/* 944 */         throw new IOException("Corrupted TAR archive. Sparse header block numbytes contains negative value");
/*     */       }
/*     */       
/* 947 */       bytesRead += readResult[1];
/* 948 */       sparseHeaders.add(new TarArchiveStructSparse(sparseOffset, sparseNumbytes));
/*     */     } 
/*     */ 
/*     */     
/* 952 */     long bytesToSkip = recordSize - bytesRead % recordSize;
/* 953 */     IOUtils.skip(inputStream, bytesToSkip);
/* 954 */     return sparseHeaders;
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
/*     */   private static long[] readLineOfNumberForPax1X(InputStream inputStream) throws IOException {
/* 967 */     long result = 0L;
/* 968 */     long bytesRead = 0L;
/*     */     int number;
/* 970 */     while ((number = inputStream.read()) != 10) {
/* 971 */       bytesRead++;
/* 972 */       if (number == -1) {
/* 973 */         throw new IOException("Unexpected EOF when reading parse information of 1.X PAX format");
/*     */       }
/* 975 */       if (number < 48 || number > 57) {
/* 976 */         throw new IOException("Corrupted TAR archive. Non-numeric value in sparse headers block");
/*     */       }
/* 978 */       result = result * 10L + (number - 48);
/*     */     } 
/* 980 */     bytesRead++;
/*     */     
/* 982 */     return new long[] { result, bytesRead };
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\archivers\tar\TarUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */