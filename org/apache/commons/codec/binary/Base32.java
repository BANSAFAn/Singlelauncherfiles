/*     */ package org.apache.commons.codec.binary;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Base32
/*     */   extends BaseNCodec
/*     */ {
/*     */   private static final int BITS_PER_ENCODED_BYTE = 5;
/*     */   private static final int BYTES_PER_ENCODED_BLOCK = 8;
/*     */   private static final int BYTES_PER_UNENCODED_BLOCK = 5;
/*  60 */   private static final byte[] CHUNK_SEPARATOR = new byte[] { 13, 10 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  67 */   private static final byte[] DECODE_TABLE = new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  81 */   private static final byte[] ENCODE_TABLE = new byte[] { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 50, 51, 52, 53, 54, 55 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  92 */   private static final byte[] HEX_DECODE_TABLE = new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, -1, -1, -1, -1, -1, -1, -1, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 106 */   private static final byte[] HEX_ENCODE_TABLE = new byte[] { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int MASK_5BITS = 31;
/*     */ 
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
/*     */   private final byte[] decodeTable;
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
/*     */   private final byte[] encodeTable;
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
/*     */   
/*     */   public Base32() {
/* 159 */     this(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Base32(boolean useHex) {
/* 170 */     this(0, (byte[])null, useHex);
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
/*     */   public Base32(int lineLength) {
/* 185 */     this(lineLength, CHUNK_SEPARATOR);
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
/*     */   public Base32(int lineLength, byte[] lineSeparator) {
/* 207 */     this(lineLength, lineSeparator, false);
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
/*     */   public Base32(int lineLength, byte[] lineSeparator, boolean useHex) {
/* 232 */     super(5, 8, lineLength, (lineSeparator == null) ? 0 : lineSeparator.length);
/*     */ 
/*     */     
/* 235 */     if (useHex) {
/* 236 */       this.encodeTable = HEX_ENCODE_TABLE;
/* 237 */       this.decodeTable = HEX_DECODE_TABLE;
/*     */     } else {
/* 239 */       this.encodeTable = ENCODE_TABLE;
/* 240 */       this.decodeTable = DECODE_TABLE;
/*     */     } 
/* 242 */     if (lineLength > 0) {
/* 243 */       if (lineSeparator == null) {
/* 244 */         throw new IllegalArgumentException("lineLength " + lineLength + " > 0, but lineSeparator is null");
/*     */       }
/*     */       
/* 247 */       if (containsAlphabetOrPad(lineSeparator)) {
/* 248 */         String sep = StringUtils.newStringUtf8(lineSeparator);
/* 249 */         throw new IllegalArgumentException("lineSeparator must not contain Base32 characters: [" + sep + "]");
/*     */       } 
/* 251 */       this.encodeSize = 8 + lineSeparator.length;
/* 252 */       this.lineSeparator = new byte[lineSeparator.length];
/* 253 */       System.arraycopy(lineSeparator, 0, this.lineSeparator, 0, lineSeparator.length);
/*     */     } else {
/* 255 */       this.encodeSize = 8;
/* 256 */       this.lineSeparator = null;
/*     */     } 
/* 258 */     this.decodeSize = this.encodeSize - 1;
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
/*     */   void decode(byte[] in, int inPos, int inAvail, BaseNCodec.Context context) {
/* 287 */     if (context.eof) {
/*     */       return;
/*     */     }
/* 290 */     if (inAvail < 0) {
/* 291 */       context.eof = true;
/*     */     }
/* 293 */     for (int i = 0; i < inAvail; i++) {
/* 294 */       byte b = in[inPos++];
/* 295 */       if (b == 61) {
/*     */         
/* 297 */         context.eof = true;
/*     */         break;
/*     */       } 
/* 300 */       byte[] buffer = ensureBufferSize(this.decodeSize, context);
/* 301 */       if (b >= 0 && b < this.decodeTable.length) {
/* 302 */         int result = this.decodeTable[b];
/* 303 */         if (result >= 0) {
/* 304 */           context.modulus = (context.modulus + 1) % 8;
/*     */           
/* 306 */           context.lbitWorkArea = (context.lbitWorkArea << 5L) + result;
/* 307 */           if (context.modulus == 0) {
/* 308 */             buffer[context.pos++] = (byte)(int)(context.lbitWorkArea >> 32L & 0xFFL);
/* 309 */             buffer[context.pos++] = (byte)(int)(context.lbitWorkArea >> 24L & 0xFFL);
/* 310 */             buffer[context.pos++] = (byte)(int)(context.lbitWorkArea >> 16L & 0xFFL);
/* 311 */             buffer[context.pos++] = (byte)(int)(context.lbitWorkArea >> 8L & 0xFFL);
/* 312 */             buffer[context.pos++] = (byte)(int)(context.lbitWorkArea & 0xFFL);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 322 */     if (context.eof && context.modulus >= 2) {
/* 323 */       byte[] buffer = ensureBufferSize(this.decodeSize, context);
/*     */ 
/*     */       
/* 326 */       switch (context.modulus) {
/*     */         case 2:
/* 328 */           buffer[context.pos++] = (byte)(int)(context.lbitWorkArea >> 2L & 0xFFL);
/*     */           return;
/*     */         case 3:
/* 331 */           buffer[context.pos++] = (byte)(int)(context.lbitWorkArea >> 7L & 0xFFL);
/*     */           return;
/*     */         case 4:
/* 334 */           context.lbitWorkArea >>= 4L;
/* 335 */           buffer[context.pos++] = (byte)(int)(context.lbitWorkArea >> 8L & 0xFFL);
/* 336 */           buffer[context.pos++] = (byte)(int)(context.lbitWorkArea & 0xFFL);
/*     */           return;
/*     */         case 5:
/* 339 */           context.lbitWorkArea >>= 1L;
/* 340 */           buffer[context.pos++] = (byte)(int)(context.lbitWorkArea >> 16L & 0xFFL);
/* 341 */           buffer[context.pos++] = (byte)(int)(context.lbitWorkArea >> 8L & 0xFFL);
/* 342 */           buffer[context.pos++] = (byte)(int)(context.lbitWorkArea & 0xFFL);
/*     */           return;
/*     */         case 6:
/* 345 */           context.lbitWorkArea >>= 6L;
/* 346 */           buffer[context.pos++] = (byte)(int)(context.lbitWorkArea >> 16L & 0xFFL);
/* 347 */           buffer[context.pos++] = (byte)(int)(context.lbitWorkArea >> 8L & 0xFFL);
/* 348 */           buffer[context.pos++] = (byte)(int)(context.lbitWorkArea & 0xFFL);
/*     */           return;
/*     */         case 7:
/* 351 */           context.lbitWorkArea >>= 3L;
/* 352 */           buffer[context.pos++] = (byte)(int)(context.lbitWorkArea >> 24L & 0xFFL);
/* 353 */           buffer[context.pos++] = (byte)(int)(context.lbitWorkArea >> 16L & 0xFFL);
/* 354 */           buffer[context.pos++] = (byte)(int)(context.lbitWorkArea >> 8L & 0xFFL);
/* 355 */           buffer[context.pos++] = (byte)(int)(context.lbitWorkArea & 0xFFL);
/*     */           return;
/*     */       } 
/*     */       
/* 359 */       throw new IllegalStateException("Impossible modulus " + context.modulus);
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
/*     */   void encode(byte[] in, int inPos, int inAvail, BaseNCodec.Context context) {
/* 383 */     if (context.eof) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 388 */     if (inAvail < 0) {
/* 389 */       context.eof = true;
/* 390 */       if (0 == context.modulus && this.lineLength == 0) {
/*     */         return;
/*     */       }
/* 393 */       byte[] buffer = ensureBufferSize(this.encodeSize, context);
/* 394 */       int savedPos = context.pos;
/* 395 */       switch (context.modulus) {
/*     */         case 0:
/*     */           break;
/*     */         case 1:
/* 399 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 3L) & 0x1F];
/* 400 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea << 2L) & 0x1F];
/* 401 */           buffer[context.pos++] = 61;
/* 402 */           buffer[context.pos++] = 61;
/* 403 */           buffer[context.pos++] = 61;
/* 404 */           buffer[context.pos++] = 61;
/* 405 */           buffer[context.pos++] = 61;
/* 406 */           buffer[context.pos++] = 61;
/*     */           break;
/*     */         case 2:
/* 409 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 11L) & 0x1F];
/* 410 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 6L) & 0x1F];
/* 411 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 1L) & 0x1F];
/* 412 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea << 4L) & 0x1F];
/* 413 */           buffer[context.pos++] = 61;
/* 414 */           buffer[context.pos++] = 61;
/* 415 */           buffer[context.pos++] = 61;
/* 416 */           buffer[context.pos++] = 61;
/*     */           break;
/*     */         case 3:
/* 419 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 19L) & 0x1F];
/* 420 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 14L) & 0x1F];
/* 421 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 9L) & 0x1F];
/* 422 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 4L) & 0x1F];
/* 423 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea << 1L) & 0x1F];
/* 424 */           buffer[context.pos++] = 61;
/* 425 */           buffer[context.pos++] = 61;
/* 426 */           buffer[context.pos++] = 61;
/*     */           break;
/*     */         case 4:
/* 429 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 27L) & 0x1F];
/* 430 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 22L) & 0x1F];
/* 431 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 17L) & 0x1F];
/* 432 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 12L) & 0x1F];
/* 433 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 7L) & 0x1F];
/* 434 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 2L) & 0x1F];
/* 435 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea << 3L) & 0x1F];
/* 436 */           buffer[context.pos++] = 61;
/*     */           break;
/*     */         default:
/* 439 */           throw new IllegalStateException("Impossible modulus " + context.modulus);
/*     */       } 
/* 441 */       context.currentLinePos += context.pos - savedPos;
/*     */       
/* 443 */       if (this.lineLength > 0 && context.currentLinePos > 0) {
/* 444 */         System.arraycopy(this.lineSeparator, 0, buffer, context.pos, this.lineSeparator.length);
/* 445 */         context.pos += this.lineSeparator.length;
/*     */       } 
/*     */     } else {
/* 448 */       for (int i = 0; i < inAvail; i++) {
/* 449 */         byte[] buffer = ensureBufferSize(this.encodeSize, context);
/* 450 */         context.modulus = (context.modulus + 1) % 5;
/* 451 */         int b = in[inPos++];
/* 452 */         if (b < 0) {
/* 453 */           b += 256;
/*     */         }
/* 455 */         context.lbitWorkArea = (context.lbitWorkArea << 8L) + b;
/* 456 */         if (0 == context.modulus) {
/* 457 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 35L) & 0x1F];
/* 458 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 30L) & 0x1F];
/* 459 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 25L) & 0x1F];
/* 460 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 20L) & 0x1F];
/* 461 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 15L) & 0x1F];
/* 462 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 10L) & 0x1F];
/* 463 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 5L) & 0x1F];
/* 464 */           buffer[context.pos++] = this.encodeTable[(int)context.lbitWorkArea & 0x1F];
/* 465 */           context.currentLinePos += 8;
/* 466 */           if (this.lineLength > 0 && this.lineLength <= context.currentLinePos) {
/* 467 */             System.arraycopy(this.lineSeparator, 0, buffer, context.pos, this.lineSeparator.length);
/* 468 */             context.pos += this.lineSeparator.length;
/* 469 */             context.currentLinePos = 0;
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
/*     */   public boolean isInAlphabet(byte octet) {
/* 485 */     return (octet >= 0 && octet < this.decodeTable.length && this.decodeTable[octet] != -1);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\codec\binary\Base32.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */