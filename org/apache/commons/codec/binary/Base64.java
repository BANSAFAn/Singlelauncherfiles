/*     */ package org.apache.commons.codec.binary;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Base64
/*     */   extends BaseNCodec
/*     */ {
/*     */   private static final int BITS_PER_ENCODED_BYTE = 6;
/*     */   private static final int BYTES_PER_UNENCODED_BLOCK = 3;
/*     */   private static final int BYTES_PER_ENCODED_BLOCK = 4;
/*  71 */   static final byte[] CHUNK_SEPARATOR = new byte[] { 13, 10 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  80 */   private static final byte[] STANDARD_ENCODE_TABLE = new byte[] { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  93 */   private static final byte[] URL_SAFE_ENCODE_TABLE = new byte[] { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 112 */   private static final byte[] DECODE_TABLE = new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, 62, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, 63, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int MASK_6BITS = 63;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final byte[] encodeTable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 140 */   private final byte[] decodeTable = DECODE_TABLE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final byte[] lineSeparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int decodeSize;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int encodeSize;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Base64() {
/* 170 */     this(0);
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
/*     */   public Base64(boolean urlSafe) {
/* 189 */     this(76, CHUNK_SEPARATOR, urlSafe);
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
/*     */   public Base64(int lineLength) {
/* 212 */     this(lineLength, CHUNK_SEPARATOR);
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
/*     */   public Base64(int lineLength, byte[] lineSeparator) {
/* 239 */     this(lineLength, lineSeparator, false);
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
/*     */   public Base64(int lineLength, byte[] lineSeparator, boolean urlSafe) {
/* 270 */     super(3, 4, lineLength, (lineSeparator == null) ? 0 : lineSeparator.length);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 275 */     if (lineSeparator != null) {
/* 276 */       if (containsAlphabetOrPad(lineSeparator)) {
/* 277 */         String sep = StringUtils.newStringUtf8(lineSeparator);
/* 278 */         throw new IllegalArgumentException("lineSeparator must not contain base64 characters: [" + sep + "]");
/*     */       } 
/* 280 */       if (lineLength > 0) {
/* 281 */         this.encodeSize = 4 + lineSeparator.length;
/* 282 */         this.lineSeparator = new byte[lineSeparator.length];
/* 283 */         System.arraycopy(lineSeparator, 0, this.lineSeparator, 0, lineSeparator.length);
/*     */       } else {
/* 285 */         this.encodeSize = 4;
/* 286 */         this.lineSeparator = null;
/*     */       } 
/*     */     } else {
/* 289 */       this.encodeSize = 4;
/* 290 */       this.lineSeparator = null;
/*     */     } 
/* 292 */     this.decodeSize = this.encodeSize - 1;
/* 293 */     this.encodeTable = urlSafe ? URL_SAFE_ENCODE_TABLE : STANDARD_ENCODE_TABLE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isUrlSafe() {
/* 303 */     return (this.encodeTable == URL_SAFE_ENCODE_TABLE);
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
/*     */   void encode(byte[] in, int inPos, int inAvail, BaseNCodec.Context context) {
/* 329 */     if (context.eof) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 334 */     if (inAvail < 0) {
/* 335 */       context.eof = true;
/* 336 */       if (0 == context.modulus && this.lineLength == 0) {
/*     */         return;
/*     */       }
/* 339 */       byte[] buffer = ensureBufferSize(this.encodeSize, context);
/* 340 */       int savedPos = context.pos;
/* 341 */       switch (context.modulus) {
/*     */         case 0:
/*     */           break;
/*     */         
/*     */         case 1:
/* 346 */           buffer[context.pos++] = this.encodeTable[context.ibitWorkArea >> 2 & 0x3F];
/*     */           
/* 348 */           buffer[context.pos++] = this.encodeTable[context.ibitWorkArea << 4 & 0x3F];
/*     */           
/* 350 */           if (this.encodeTable == STANDARD_ENCODE_TABLE) {
/* 351 */             buffer[context.pos++] = 61;
/* 352 */             buffer[context.pos++] = 61;
/*     */           } 
/*     */           break;
/*     */         
/*     */         case 2:
/* 357 */           buffer[context.pos++] = this.encodeTable[context.ibitWorkArea >> 10 & 0x3F];
/* 358 */           buffer[context.pos++] = this.encodeTable[context.ibitWorkArea >> 4 & 0x3F];
/* 359 */           buffer[context.pos++] = this.encodeTable[context.ibitWorkArea << 2 & 0x3F];
/*     */           
/* 361 */           if (this.encodeTable == STANDARD_ENCODE_TABLE) {
/* 362 */             buffer[context.pos++] = 61;
/*     */           }
/*     */           break;
/*     */         default:
/* 366 */           throw new IllegalStateException("Impossible modulus " + context.modulus);
/*     */       } 
/* 368 */       context.currentLinePos += context.pos - savedPos;
/*     */       
/* 370 */       if (this.lineLength > 0 && context.currentLinePos > 0) {
/* 371 */         System.arraycopy(this.lineSeparator, 0, buffer, context.pos, this.lineSeparator.length);
/* 372 */         context.pos += this.lineSeparator.length;
/*     */       } 
/*     */     } else {
/* 375 */       for (int i = 0; i < inAvail; i++) {
/* 376 */         byte[] buffer = ensureBufferSize(this.encodeSize, context);
/* 377 */         context.modulus = (context.modulus + 1) % 3;
/* 378 */         int b = in[inPos++];
/* 379 */         if (b < 0) {
/* 380 */           b += 256;
/*     */         }
/* 382 */         context.ibitWorkArea = (context.ibitWorkArea << 8) + b;
/* 383 */         if (0 == context.modulus) {
/* 384 */           buffer[context.pos++] = this.encodeTable[context.ibitWorkArea >> 18 & 0x3F];
/* 385 */           buffer[context.pos++] = this.encodeTable[context.ibitWorkArea >> 12 & 0x3F];
/* 386 */           buffer[context.pos++] = this.encodeTable[context.ibitWorkArea >> 6 & 0x3F];
/* 387 */           buffer[context.pos++] = this.encodeTable[context.ibitWorkArea & 0x3F];
/* 388 */           context.currentLinePos += 4;
/* 389 */           if (this.lineLength > 0 && this.lineLength <= context.currentLinePos) {
/* 390 */             System.arraycopy(this.lineSeparator, 0, buffer, context.pos, this.lineSeparator.length);
/* 391 */             context.pos += this.lineSeparator.length;
/* 392 */             context.currentLinePos = 0;
/*     */           } 
/*     */         } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void decode(byte[] in, int inPos, int inAvail, BaseNCodec.Context context) {
/* 426 */     if (context.eof) {
/*     */       return;
/*     */     }
/* 429 */     if (inAvail < 0) {
/* 430 */       context.eof = true;
/*     */     }
/* 432 */     for (int i = 0; i < inAvail; i++) {
/* 433 */       byte[] buffer = ensureBufferSize(this.decodeSize, context);
/* 434 */       byte b = in[inPos++];
/* 435 */       if (b == 61) {
/*     */         
/* 437 */         context.eof = true;
/*     */         break;
/*     */       } 
/* 440 */       if (b >= 0 && b < DECODE_TABLE.length) {
/* 441 */         int result = DECODE_TABLE[b];
/* 442 */         if (result >= 0) {
/* 443 */           context.modulus = (context.modulus + 1) % 4;
/* 444 */           context.ibitWorkArea = (context.ibitWorkArea << 6) + result;
/* 445 */           if (context.modulus == 0) {
/* 446 */             buffer[context.pos++] = (byte)(context.ibitWorkArea >> 16 & 0xFF);
/* 447 */             buffer[context.pos++] = (byte)(context.ibitWorkArea >> 8 & 0xFF);
/* 448 */             buffer[context.pos++] = (byte)(context.ibitWorkArea & 0xFF);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 458 */     if (context.eof && context.modulus != 0) {
/* 459 */       byte[] buffer = ensureBufferSize(this.decodeSize, context);
/*     */ 
/*     */ 
/*     */       
/* 463 */       switch (context.modulus) {
/*     */         case 1:
/*     */           return;
/*     */ 
/*     */         
/*     */         case 2:
/* 469 */           context.ibitWorkArea >>= 4;
/* 470 */           buffer[context.pos++] = (byte)(context.ibitWorkArea & 0xFF);
/*     */         
/*     */         case 3:
/* 473 */           context.ibitWorkArea >>= 2;
/* 474 */           buffer[context.pos++] = (byte)(context.ibitWorkArea >> 8 & 0xFF);
/* 475 */           buffer[context.pos++] = (byte)(context.ibitWorkArea & 0xFF);
/*     */       } 
/*     */       
/* 478 */       throw new IllegalStateException("Impossible modulus " + context.modulus);
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
/*     */   @Deprecated
/*     */   public static boolean isArrayByteBase64(byte[] arrayOctet) {
/* 495 */     return isBase64(arrayOctet);
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
/*     */   public static boolean isBase64(byte octet) {
/* 507 */     return (octet == 61 || (octet >= 0 && octet < DECODE_TABLE.length && DECODE_TABLE[octet] != -1));
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
/*     */   public static boolean isBase64(String base64) {
/* 521 */     return isBase64(StringUtils.getBytesUtf8(base64));
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
/*     */   public static boolean isBase64(byte[] arrayOctet) {
/* 535 */     for (int i = 0; i < arrayOctet.length; i++) {
/* 536 */       if (!isBase64(arrayOctet[i]) && !isWhiteSpace(arrayOctet[i])) {
/* 537 */         return false;
/*     */       }
/*     */     } 
/* 540 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] encodeBase64(byte[] binaryData) {
/* 551 */     return encodeBase64(binaryData, false);
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
/*     */   public static String encodeBase64String(byte[] binaryData) {
/* 566 */     return StringUtils.newStringUtf8(encodeBase64(binaryData, false));
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
/*     */   public static byte[] encodeBase64URLSafe(byte[] binaryData) {
/* 579 */     return encodeBase64(binaryData, false, true);
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
/*     */   public static String encodeBase64URLSafeString(byte[] binaryData) {
/* 592 */     return StringUtils.newStringUtf8(encodeBase64(binaryData, false, true));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] encodeBase64Chunked(byte[] binaryData) {
/* 603 */     return encodeBase64(binaryData, true);
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
/*     */   public static byte[] encodeBase64(byte[] binaryData, boolean isChunked) {
/* 618 */     return encodeBase64(binaryData, isChunked, false);
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
/*     */   public static byte[] encodeBase64(byte[] binaryData, boolean isChunked, boolean urlSafe) {
/* 637 */     return encodeBase64(binaryData, isChunked, urlSafe, 2147483647);
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
/*     */   public static byte[] encodeBase64(byte[] binaryData, boolean isChunked, boolean urlSafe, int maxResultSize) {
/* 659 */     if (binaryData == null || binaryData.length == 0) {
/* 660 */       return binaryData;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 665 */     Base64 b64 = isChunked ? new Base64(urlSafe) : new Base64(0, CHUNK_SEPARATOR, urlSafe);
/* 666 */     long len = b64.getEncodedLength(binaryData);
/* 667 */     if (len > maxResultSize) {
/* 668 */       throw new IllegalArgumentException("Input array too big, the output array would be bigger (" + len + ") than the specified maximum size of " + maxResultSize);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 674 */     return b64.encode(binaryData);
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
/*     */   public static byte[] decodeBase64(String base64String) {
/* 686 */     return (new Base64()).decode(base64String);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] decodeBase64(byte[] base64Data) {
/* 697 */     return (new Base64()).decode(base64Data);
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
/*     */   public static BigInteger decodeInteger(byte[] pArray) {
/* 712 */     return new BigInteger(1, decodeBase64(pArray));
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
/*     */   public static byte[] encodeInteger(BigInteger bigInt) {
/* 726 */     if (bigInt == null) {
/* 727 */       throw new NullPointerException("encodeInteger called with null parameter");
/*     */     }
/* 729 */     return encodeBase64(toIntegerBytes(bigInt), false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static byte[] toIntegerBytes(BigInteger bigInt) {
/* 740 */     int bitlen = bigInt.bitLength();
/*     */     
/* 742 */     bitlen = bitlen + 7 >> 3 << 3;
/* 743 */     byte[] bigBytes = bigInt.toByteArray();
/*     */     
/* 745 */     if (bigInt.bitLength() % 8 != 0 && bigInt.bitLength() / 8 + 1 == bitlen / 8) {
/* 746 */       return bigBytes;
/*     */     }
/*     */     
/* 749 */     int startSrc = 0;
/* 750 */     int len = bigBytes.length;
/*     */ 
/*     */     
/* 753 */     if (bigInt.bitLength() % 8 == 0) {
/* 754 */       startSrc = 1;
/* 755 */       len--;
/*     */     } 
/* 757 */     int startDst = bitlen / 8 - len;
/* 758 */     byte[] resizedBytes = new byte[bitlen / 8];
/* 759 */     System.arraycopy(bigBytes, startSrc, resizedBytes, startDst, len);
/* 760 */     return resizedBytes;
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
/*     */   protected boolean isInAlphabet(byte octet) {
/* 772 */     return (octet >= 0 && octet < this.decodeTable.length && this.decodeTable[octet] != -1);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\codec\binary\Base64.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */