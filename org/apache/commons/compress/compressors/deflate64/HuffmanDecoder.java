/*     */ package org.apache.commons.compress.compressors.deflate64;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.ByteOrder;
/*     */ import java.util.Arrays;
/*     */ import org.apache.commons.compress.utils.BitInputStream;
/*     */ import org.apache.commons.compress.utils.ByteUtils;
/*     */ import org.apache.commons.compress.utils.ExactMath;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class HuffmanDecoder
/*     */   implements Closeable
/*     */ {
/*  57 */   private static final short[] RUN_LENGTH_TABLE = new short[] { 96, 128, 160, 192, 224, 256, 288, 320, 353, 417, 481, 545, 610, 738, 866, 994, 1123, 1379, 1635, 1891, 2148, 2660, 3172, 3684, 4197, 5221, 6245, 7269, 112 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  83 */   private static final int[] DISTANCE_TABLE = new int[] { 16, 32, 48, 64, 81, 113, 146, 210, 275, 403, 532, 788, 1045, 1557, 2070, 3094, 4119, 6167, 8216, 12312, 16409, 24601, 32794, 49178, 65563, 98331, 131100, 196636, 262173, 393245, 524318, 786462 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  94 */   private static final int[] CODE_LENGTHS_ORDER = new int[] { 16, 17, 18, 0, 8, 7, 9, 6, 10, 5, 11, 4, 12, 3, 13, 2, 14, 1, 15 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 104 */   private static final int[] FIXED_LITERALS = new int[288]; static {
/* 105 */     Arrays.fill(FIXED_LITERALS, 0, 144, 8);
/* 106 */     Arrays.fill(FIXED_LITERALS, 144, 256, 9);
/* 107 */     Arrays.fill(FIXED_LITERALS, 256, 280, 7);
/* 108 */     Arrays.fill(FIXED_LITERALS, 280, 288, 8);
/*     */   }
/* 110 */   private static final int[] FIXED_DISTANCE = new int[32]; static {
/* 111 */     Arrays.fill(FIXED_DISTANCE, 5);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean finalBlock;
/*     */   private DecoderState state;
/*     */   private BitInputStream reader;
/*     */   private final InputStream in;
/* 119 */   private final DecodingMemory memory = new DecodingMemory();
/*     */   
/*     */   HuffmanDecoder(InputStream in) {
/* 122 */     this.reader = new BitInputStream(in, ByteOrder.LITTLE_ENDIAN);
/* 123 */     this.in = in;
/* 124 */     this.state = new InitialState();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 129 */     this.state = new InitialState();
/* 130 */     this.reader = null;
/*     */   }
/*     */   
/*     */   public int decode(byte[] b) throws IOException {
/* 134 */     return decode(b, 0, b.length);
/*     */   }
/*     */   
/*     */   public int decode(byte[] b, int off, int len) throws IOException {
/* 138 */     while (!this.finalBlock || this.state.hasData()) {
/* 139 */       if (this.state.state() == HuffmanState.INITIAL) {
/* 140 */         int[][] tables; this.finalBlock = (readBits(1) == 1L);
/* 141 */         int mode = (int)readBits(2);
/* 142 */         switch (mode) {
/*     */           case 0:
/* 144 */             switchToUncompressedState();
/*     */             continue;
/*     */           case 1:
/* 147 */             this.state = new HuffmanCodes(HuffmanState.FIXED_CODES, FIXED_LITERALS, FIXED_DISTANCE);
/*     */             continue;
/*     */           case 2:
/* 150 */             tables = readDynamicTables();
/* 151 */             this.state = new HuffmanCodes(HuffmanState.DYNAMIC_CODES, tables[0], tables[1]);
/*     */             continue;
/*     */         } 
/* 154 */         throw new IllegalStateException("Unsupported compression: " + mode);
/*     */       } 
/*     */       
/* 157 */       int r = this.state.read(b, off, len);
/* 158 */       if (r != 0) {
/* 159 */         return r;
/*     */       }
/*     */     } 
/*     */     
/* 163 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   long getBytesRead() {
/* 170 */     return this.reader.getBytesRead();
/*     */   }
/*     */   
/*     */   private void switchToUncompressedState() throws IOException {
/* 174 */     this.reader.alignWithByteBoundary();
/* 175 */     long bLen = readBits(16);
/* 176 */     long bNLen = readBits(16);
/* 177 */     if (((bLen ^ 0xFFFFL) & 0xFFFFL) != bNLen)
/*     */     {
/* 179 */       throw new IllegalStateException("Illegal LEN / NLEN values");
/*     */     }
/* 181 */     this.state = new UncompressedState(bLen);
/*     */   }
/*     */   
/*     */   private int[][] readDynamicTables() throws IOException {
/* 185 */     int[][] result = new int[2][];
/* 186 */     int literals = (int)(readBits(5) + 257L);
/* 187 */     result[0] = new int[literals];
/*     */     
/* 189 */     int distances = (int)(readBits(5) + 1L);
/* 190 */     result[1] = new int[distances];
/*     */     
/* 192 */     populateDynamicTables(this.reader, result[0], result[1]);
/* 193 */     return result;
/*     */   }
/*     */   
/*     */   int available() throws IOException {
/* 197 */     return this.state.available();
/*     */   }
/*     */   
/*     */   private static abstract class DecoderState {
/*     */     private DecoderState() {}
/*     */     
/*     */     abstract HuffmanState state();
/*     */     
/*     */     abstract int read(byte[] param1ArrayOfbyte, int param1Int1, int param1Int2) throws IOException;
/*     */     
/*     */     abstract boolean hasData();
/*     */     
/*     */     abstract int available() throws IOException; }
/*     */   
/*     */   private class UncompressedState extends DecoderState { private final long blockLength;
/*     */     private long read;
/*     */     
/*     */     private UncompressedState(long blockLength) {
/* 215 */       this.blockLength = blockLength;
/*     */     }
/*     */ 
/*     */     
/*     */     HuffmanState state() {
/* 220 */       return (this.read < this.blockLength) ? HuffmanState.STORED : HuffmanState.INITIAL;
/*     */     }
/*     */ 
/*     */     
/*     */     int read(byte[] b, int off, int len) throws IOException {
/* 225 */       if (len == 0) {
/* 226 */         return 0;
/*     */       }
/*     */       
/* 229 */       int max = (int)Math.min(this.blockLength - this.read, len);
/* 230 */       int readSoFar = 0;
/* 231 */       while (readSoFar < max) {
/*     */         int readNow;
/* 233 */         if (HuffmanDecoder.this.reader.bitsCached() > 0) {
/* 234 */           byte next = (byte)(int)HuffmanDecoder.this.readBits(8);
/* 235 */           b[off + readSoFar] = HuffmanDecoder.this.memory.add(next);
/* 236 */           readNow = 1;
/*     */         } else {
/* 238 */           readNow = HuffmanDecoder.this.in.read(b, off + readSoFar, max - readSoFar);
/* 239 */           if (readNow == -1) {
/* 240 */             throw new EOFException("Truncated Deflate64 Stream");
/*     */           }
/* 242 */           HuffmanDecoder.this.memory.add(b, off + readSoFar, readNow);
/*     */         } 
/* 244 */         this.read += readNow;
/* 245 */         readSoFar += readNow;
/*     */       } 
/* 247 */       return max;
/*     */     }
/*     */ 
/*     */     
/*     */     boolean hasData() {
/* 252 */       return (this.read < this.blockLength);
/*     */     }
/*     */ 
/*     */     
/*     */     int available() throws IOException {
/* 257 */       return (int)Math.min(this.blockLength - this.read, HuffmanDecoder.this.reader.bitsAvailable() / 8L);
/*     */     } }
/*     */   
/*     */   private static class InitialState extends DecoderState {
/*     */     private InitialState() {}
/*     */     
/*     */     HuffmanState state() {
/* 264 */       return HuffmanState.INITIAL;
/*     */     }
/*     */ 
/*     */     
/*     */     int read(byte[] b, int off, int len) throws IOException {
/* 269 */       if (len == 0) {
/* 270 */         return 0;
/*     */       }
/* 272 */       throw new IllegalStateException("Cannot read in this state");
/*     */     }
/*     */ 
/*     */     
/*     */     boolean hasData() {
/* 277 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     int available() {
/* 282 */       return 0;
/*     */     }
/*     */   }
/*     */   
/*     */   private class HuffmanCodes
/*     */     extends DecoderState {
/*     */     private boolean endOfBlock;
/*     */     private final HuffmanState state;
/*     */     private final HuffmanDecoder.BinaryTreeNode lengthTree;
/*     */     private final HuffmanDecoder.BinaryTreeNode distanceTree;
/*     */     private int runBufferPos;
/* 293 */     private byte[] runBuffer = ByteUtils.EMPTY_BYTE_ARRAY;
/*     */     private int runBufferLength;
/*     */     
/*     */     HuffmanCodes(HuffmanState state, int[] lengths, int[] distance) {
/* 297 */       this.state = state;
/* 298 */       this.lengthTree = HuffmanDecoder.buildTree(lengths);
/* 299 */       this.distanceTree = HuffmanDecoder.buildTree(distance);
/*     */     }
/*     */ 
/*     */     
/*     */     HuffmanState state() {
/* 304 */       return this.endOfBlock ? HuffmanState.INITIAL : this.state;
/*     */     }
/*     */ 
/*     */     
/*     */     int read(byte[] b, int off, int len) throws IOException {
/* 309 */       if (len == 0) {
/* 310 */         return 0;
/*     */       }
/* 312 */       return decodeNext(b, off, len);
/*     */     }
/*     */     
/*     */     private int decodeNext(byte[] b, int off, int len) throws IOException {
/* 316 */       if (this.endOfBlock) {
/* 317 */         return -1;
/*     */       }
/* 319 */       int result = copyFromRunBuffer(b, off, len);
/*     */       
/* 321 */       while (result < len) {
/* 322 */         int symbol = HuffmanDecoder.nextSymbol(HuffmanDecoder.this.reader, this.lengthTree);
/* 323 */         if (symbol < 256) {
/* 324 */           b[off + result++] = HuffmanDecoder.this.memory.add((byte)symbol); continue;
/* 325 */         }  if (symbol > 256) {
/* 326 */           int runMask = HuffmanDecoder.RUN_LENGTH_TABLE[symbol - 257];
/* 327 */           int run = runMask >>> 5;
/* 328 */           int runXtra = runMask & 0x1F;
/* 329 */           run = ExactMath.add(run, HuffmanDecoder.this.readBits(runXtra));
/*     */           
/* 331 */           int distSym = HuffmanDecoder.nextSymbol(HuffmanDecoder.this.reader, this.distanceTree);
/*     */           
/* 333 */           int distMask = HuffmanDecoder.DISTANCE_TABLE[distSym];
/* 334 */           int dist = distMask >>> 4;
/* 335 */           int distXtra = distMask & 0xF;
/* 336 */           dist = ExactMath.add(dist, HuffmanDecoder.this.readBits(distXtra));
/*     */           
/* 338 */           if (this.runBuffer.length < run) {
/* 339 */             this.runBuffer = new byte[run];
/*     */           }
/* 341 */           this.runBufferLength = run;
/* 342 */           this.runBufferPos = 0;
/* 343 */           HuffmanDecoder.this.memory.recordToBuffer(dist, run, this.runBuffer);
/*     */           
/* 345 */           result += copyFromRunBuffer(b, off + result, len - result); continue;
/*     */         } 
/* 347 */         this.endOfBlock = true;
/* 348 */         return result;
/*     */       } 
/*     */ 
/*     */       
/* 352 */       return result;
/*     */     }
/*     */     
/*     */     private int copyFromRunBuffer(byte[] b, int off, int len) {
/* 356 */       int bytesInBuffer = this.runBufferLength - this.runBufferPos;
/* 357 */       int copiedBytes = 0;
/* 358 */       if (bytesInBuffer > 0) {
/* 359 */         copiedBytes = Math.min(len, bytesInBuffer);
/* 360 */         System.arraycopy(this.runBuffer, this.runBufferPos, b, off, copiedBytes);
/* 361 */         this.runBufferPos += copiedBytes;
/*     */       } 
/* 363 */       return copiedBytes;
/*     */     }
/*     */ 
/*     */     
/*     */     boolean hasData() {
/* 368 */       return !this.endOfBlock;
/*     */     }
/*     */ 
/*     */     
/*     */     int available() {
/* 373 */       return this.runBufferLength - this.runBufferPos;
/*     */     }
/*     */   }
/*     */   
/*     */   private static int nextSymbol(BitInputStream reader, BinaryTreeNode tree) throws IOException {
/* 378 */     BinaryTreeNode node = tree;
/* 379 */     while (node != null && node.literal == -1) {
/* 380 */       long bit = readBits(reader, 1);
/* 381 */       node = (bit == 0L) ? node.leftNode : node.rightNode;
/*     */     } 
/* 383 */     return (node != null) ? node.literal : -1;
/*     */   }
/*     */   
/*     */   private static void populateDynamicTables(BitInputStream reader, int[] literals, int[] distances) throws IOException {
/* 387 */     int codeLengths = (int)(readBits(reader, 4) + 4L);
/*     */     
/* 389 */     int[] codeLengthValues = new int[19];
/* 390 */     for (int cLen = 0; cLen < codeLengths; cLen++) {
/* 391 */       codeLengthValues[CODE_LENGTHS_ORDER[cLen]] = (int)readBits(reader, 3);
/*     */     }
/*     */     
/* 394 */     BinaryTreeNode codeLengthTree = buildTree(codeLengthValues);
/*     */     
/* 396 */     int[] auxBuffer = new int[literals.length + distances.length];
/*     */     
/* 398 */     int value = -1;
/* 399 */     int length = 0;
/* 400 */     int off = 0;
/* 401 */     while (off < auxBuffer.length) {
/* 402 */       if (length > 0) {
/* 403 */         auxBuffer[off++] = value;
/* 404 */         length--; continue;
/*     */       } 
/* 406 */       int symbol = nextSymbol(reader, codeLengthTree);
/* 407 */       if (symbol < 16) {
/* 408 */         value = symbol;
/* 409 */         auxBuffer[off++] = value; continue;
/*     */       } 
/* 411 */       switch (symbol) {
/*     */         case 16:
/* 413 */           length = (int)(readBits(reader, 2) + 3L);
/*     */         
/*     */         case 17:
/* 416 */           value = 0;
/* 417 */           length = (int)(readBits(reader, 3) + 3L);
/*     */         
/*     */         case 18:
/* 420 */           value = 0;
/* 421 */           length = (int)(readBits(reader, 7) + 11L);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     } 
/* 430 */     System.arraycopy(auxBuffer, 0, literals, 0, literals.length);
/* 431 */     System.arraycopy(auxBuffer, literals.length, distances, 0, distances.length);
/*     */   }
/*     */   
/*     */   private static class BinaryTreeNode {
/*     */     private final int bits;
/* 436 */     int literal = -1;
/*     */     BinaryTreeNode leftNode;
/*     */     BinaryTreeNode rightNode;
/*     */     
/*     */     private BinaryTreeNode(int bits) {
/* 441 */       this.bits = bits;
/*     */     }
/*     */     
/*     */     void leaf(int symbol) {
/* 445 */       this.literal = symbol;
/* 446 */       this.leftNode = null;
/* 447 */       this.rightNode = null;
/*     */     }
/*     */     
/*     */     BinaryTreeNode left() {
/* 451 */       if (this.leftNode == null && this.literal == -1) {
/* 452 */         this.leftNode = new BinaryTreeNode(this.bits + 1);
/*     */       }
/* 454 */       return this.leftNode;
/*     */     }
/*     */     
/*     */     BinaryTreeNode right() {
/* 458 */       if (this.rightNode == null && this.literal == -1) {
/* 459 */         this.rightNode = new BinaryTreeNode(this.bits + 1);
/*     */       }
/* 461 */       return this.rightNode;
/*     */     }
/*     */   }
/*     */   
/*     */   private static BinaryTreeNode buildTree(int[] litTable) {
/* 466 */     int[] literalCodes = getCodes(litTable);
/*     */     
/* 468 */     BinaryTreeNode root = new BinaryTreeNode(0);
/*     */     
/* 470 */     for (int i = 0; i < litTable.length; i++) {
/* 471 */       int len = litTable[i];
/* 472 */       if (len != 0) {
/* 473 */         BinaryTreeNode node = root;
/* 474 */         int lit = literalCodes[len - 1];
/* 475 */         for (int p = len - 1; p >= 0; p--) {
/* 476 */           int bit = lit & 1 << p;
/* 477 */           node = (bit == 0) ? node.left() : node.right();
/* 478 */           if (node == null) {
/* 479 */             throw new IllegalStateException("node doesn't exist in Huffman tree");
/*     */           }
/*     */         } 
/* 482 */         node.leaf(i);
/* 483 */         literalCodes[len - 1] = literalCodes[len - 1] + 1;
/*     */       } 
/*     */     } 
/* 486 */     return root;
/*     */   }
/*     */   
/*     */   private static int[] getCodes(int[] litTable) {
/* 490 */     int max = 0;
/* 491 */     int[] blCount = new int[65];
/*     */     
/* 493 */     for (int aLitTable : litTable) {
/* 494 */       if (aLitTable < 0 || aLitTable > 64) {
/* 495 */         throw new IllegalArgumentException("Invalid code " + aLitTable + " in literal table");
/*     */       }
/*     */       
/* 498 */       max = Math.max(max, aLitTable);
/* 499 */       blCount[aLitTable] = blCount[aLitTable] + 1;
/*     */     } 
/* 501 */     blCount = Arrays.copyOf(blCount, max + 1);
/*     */     
/* 503 */     int code = 0;
/* 504 */     int[] nextCode = new int[max + 1];
/* 505 */     for (int i = 0; i <= max; i++) {
/* 506 */       code = code + blCount[i] << 1;
/* 507 */       nextCode[i] = code;
/*     */     } 
/*     */     
/* 510 */     return nextCode;
/*     */   }
/*     */   
/*     */   private static class DecodingMemory {
/*     */     private final byte[] memory;
/*     */     private final int mask;
/*     */     private int wHead;
/*     */     private boolean wrappedAround;
/*     */     
/*     */     private DecodingMemory() {
/* 520 */       this(16);
/*     */     }
/*     */     
/*     */     private DecodingMemory(int bits) {
/* 524 */       this.memory = new byte[1 << bits];
/* 525 */       this.mask = this.memory.length - 1;
/*     */     }
/*     */     
/*     */     byte add(byte b) {
/* 529 */       this.memory[this.wHead] = b;
/* 530 */       this.wHead = incCounter(this.wHead);
/* 531 */       return b;
/*     */     }
/*     */     
/*     */     void add(byte[] b, int off, int len) {
/* 535 */       for (int i = off; i < off + len; i++) {
/* 536 */         add(b[i]);
/*     */       }
/*     */     }
/*     */     
/*     */     void recordToBuffer(int distance, int length, byte[] buff) {
/* 541 */       if (distance > this.memory.length) {
/* 542 */         throw new IllegalStateException("Illegal distance parameter: " + distance);
/*     */       }
/* 544 */       int start = this.wHead - distance & this.mask;
/* 545 */       if (!this.wrappedAround && start >= this.wHead)
/* 546 */         throw new IllegalStateException("Attempt to read beyond memory: dist=" + distance); 
/*     */       int pos;
/* 548 */       for (int i = 0; i < length; i++, pos = incCounter(pos)) {
/* 549 */         buff[i] = add(this.memory[pos]);
/*     */       }
/*     */     }
/*     */     
/*     */     private int incCounter(int counter) {
/* 554 */       int newCounter = counter + 1 & this.mask;
/* 555 */       if (!this.wrappedAround && newCounter < counter) {
/* 556 */         this.wrappedAround = true;
/*     */       }
/* 558 */       return newCounter;
/*     */     }
/*     */   }
/*     */   
/*     */   private long readBits(int numBits) throws IOException {
/* 563 */     return readBits(this.reader, numBits);
/*     */   }
/*     */   
/*     */   private static long readBits(BitInputStream reader, int numBits) throws IOException {
/* 567 */     long r = reader.readBits(numBits);
/* 568 */     if (r == -1L) {
/* 569 */       throw new EOFException("Truncated Deflate64 Stream");
/*     */     }
/* 571 */     return r;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\compressors\deflate64\HuffmanDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */