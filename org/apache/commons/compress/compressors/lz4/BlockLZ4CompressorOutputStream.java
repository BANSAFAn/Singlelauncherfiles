/*     */ package org.apache.commons.compress.compressors.lz4;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.Deque;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import org.apache.commons.compress.compressors.CompressorOutputStream;
/*     */ import org.apache.commons.compress.compressors.lz77support.LZ77Compressor;
/*     */ import org.apache.commons.compress.compressors.lz77support.Parameters;
/*     */ import org.apache.commons.compress.utils.ByteUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BlockLZ4CompressorOutputStream
/*     */   extends CompressorOutputStream
/*     */ {
/*     */   private static final int MIN_BACK_REFERENCE_LENGTH = 4;
/*     */   private static final int MIN_OFFSET_OF_LAST_BACK_REFERENCE = 12;
/*     */   private final LZ77Compressor compressor;
/*     */   private final OutputStream os;
/*  82 */   private final byte[] oneByte = new byte[1];
/*     */   
/*     */   private boolean finished;
/*     */   
/*  86 */   private final Deque<Pair> pairs = new LinkedList<>();
/*     */ 
/*     */   
/*  89 */   private final Deque<byte[]> expandedBlocks = (Deque)new LinkedList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockLZ4CompressorOutputStream(OutputStream os) {
/*  98 */     this(os, createParameterBuilder().build());
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
/*     */   public BlockLZ4CompressorOutputStream(OutputStream os, Parameters params) {
/* 110 */     this.os = os;
/* 111 */     this.compressor = new LZ77Compressor(params, block -> {
/*     */           switch (block.getType()) {
/*     */             case LITERAL:
/*     */               addLiteralBlock((LZ77Compressor.LiteralBlock)block);
/*     */               break;
/*     */             case BACK_REFERENCE:
/*     */               addBackReference((LZ77Compressor.BackReference)block);
/*     */               break;
/*     */             case EOD:
/*     */               writeFinalLiteralBlock();
/*     */               break;
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(int b) throws IOException {
/* 129 */     this.oneByte[0] = (byte)(b & 0xFF);
/* 130 */     write(this.oneByte);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(byte[] data, int off, int len) throws IOException {
/* 135 */     this.compressor.compress(data, off, len);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*     */     try {
/* 141 */       finish();
/*     */     } finally {
/* 143 */       this.os.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void finish() throws IOException {
/* 153 */     if (!this.finished) {
/* 154 */       this.compressor.finish();
/* 155 */       this.finished = true;
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
/*     */   public void prefill(byte[] data, int off, int len) {
/* 169 */     if (len > 0) {
/* 170 */       byte[] b = Arrays.copyOfRange(data, off, off + len);
/* 171 */       this.compressor.prefill(b);
/* 172 */       recordLiteral(b);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addLiteralBlock(LZ77Compressor.LiteralBlock block) throws IOException {
/* 177 */     Pair last = writeBlocksAndReturnUnfinishedPair(block.getLength());
/* 178 */     recordLiteral(last.addLiteral(block));
/* 179 */     clearUnusedBlocksAndPairs();
/*     */   }
/*     */   
/*     */   private void addBackReference(LZ77Compressor.BackReference block) throws IOException {
/* 183 */     Pair last = writeBlocksAndReturnUnfinishedPair(block.getLength());
/* 184 */     last.setBackReference(block);
/* 185 */     recordBackReference(block);
/* 186 */     clearUnusedBlocksAndPairs();
/*     */   }
/*     */   
/*     */   private Pair writeBlocksAndReturnUnfinishedPair(int length) throws IOException {
/* 190 */     writeWritablePairs(length);
/* 191 */     Pair last = this.pairs.peekLast();
/* 192 */     if (last == null || last.hasBackReference()) {
/* 193 */       last = new Pair();
/* 194 */       this.pairs.addLast(last);
/*     */     } 
/* 196 */     return last;
/*     */   }
/*     */   
/*     */   private void recordLiteral(byte[] b) {
/* 200 */     this.expandedBlocks.addFirst(b);
/*     */   }
/*     */   
/*     */   private void clearUnusedBlocksAndPairs() {
/* 204 */     clearUnusedBlocks();
/* 205 */     clearUnusedPairs();
/*     */   }
/*     */   
/*     */   private void clearUnusedBlocks() {
/* 209 */     int blockLengths = 0;
/* 210 */     int blocksToKeep = 0;
/* 211 */     for (byte[] b : this.expandedBlocks) {
/* 212 */       blocksToKeep++;
/* 213 */       blockLengths += b.length;
/* 214 */       if (blockLengths >= 65536) {
/*     */         break;
/*     */       }
/*     */     } 
/* 218 */     int size = this.expandedBlocks.size();
/* 219 */     for (int i = blocksToKeep; i < size; i++) {
/* 220 */       this.expandedBlocks.removeLast();
/*     */     }
/*     */   }
/*     */   
/*     */   private void recordBackReference(LZ77Compressor.BackReference block) {
/* 225 */     this.expandedBlocks.addFirst(expand(block.getOffset(), block.getLength()));
/*     */   }
/*     */   
/*     */   private byte[] expand(int offset, int length) {
/* 229 */     byte[] expanded = new byte[length];
/* 230 */     if (offset == 1) {
/* 231 */       byte[] block = this.expandedBlocks.peekFirst();
/* 232 */       byte b = block[block.length - 1];
/* 233 */       if (b != 0) {
/* 234 */         Arrays.fill(expanded, b);
/*     */       }
/*     */     } else {
/* 237 */       expandFromList(expanded, offset, length);
/*     */     } 
/* 239 */     return expanded;
/*     */   }
/*     */   
/*     */   private void expandFromList(byte[] expanded, int offset, int length) {
/* 243 */     int offsetRemaining = offset;
/* 244 */     int lengthRemaining = length;
/* 245 */     int writeOffset = 0;
/* 246 */     while (lengthRemaining > 0) {
/*     */       int copyLen, copyOffset;
/* 248 */       byte[] block = null;
/*     */ 
/*     */       
/* 251 */       if (offsetRemaining > 0) {
/* 252 */         int blockOffset = 0;
/* 253 */         for (byte[] b : this.expandedBlocks) {
/* 254 */           if (b.length + blockOffset >= offsetRemaining) {
/* 255 */             block = b;
/*     */             break;
/*     */           } 
/* 258 */           blockOffset += b.length;
/*     */         } 
/* 260 */         if (block == null)
/*     */         {
/* 262 */           throw new IllegalStateException("Failed to find a block containing offset " + offset);
/*     */         }
/* 264 */         copyOffset = blockOffset + block.length - offsetRemaining;
/* 265 */         copyLen = Math.min(lengthRemaining, block.length - copyOffset);
/*     */       } else {
/*     */         
/* 268 */         block = expanded;
/* 269 */         copyOffset = -offsetRemaining;
/* 270 */         copyLen = Math.min(lengthRemaining, writeOffset + offsetRemaining);
/*     */       } 
/* 272 */       System.arraycopy(block, copyOffset, expanded, writeOffset, copyLen);
/* 273 */       offsetRemaining -= copyLen;
/* 274 */       lengthRemaining -= copyLen;
/* 275 */       writeOffset += copyLen;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void clearUnusedPairs() {
/* 280 */     int pairLengths = 0;
/* 281 */     int pairsToKeep = 0;
/* 282 */     for (Iterator<Pair> it = this.pairs.descendingIterator(); it.hasNext(); ) {
/* 283 */       Pair p = it.next();
/* 284 */       pairsToKeep++;
/* 285 */       pairLengths += p.length();
/* 286 */       if (pairLengths >= 65536) {
/*     */         break;
/*     */       }
/*     */     } 
/* 290 */     int size = this.pairs.size();
/* 291 */     for (int i = pairsToKeep; i < size; i++) {
/* 292 */       Pair p = this.pairs.peekFirst();
/* 293 */       if (!p.hasBeenWritten()) {
/*     */         break;
/*     */       }
/* 296 */       this.pairs.removeFirst();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void writeFinalLiteralBlock() throws IOException {
/* 301 */     rewriteLastPairs();
/* 302 */     for (Pair p : this.pairs) {
/* 303 */       if (!p.hasBeenWritten()) {
/* 304 */         p.writeTo(this.os);
/*     */       }
/*     */     } 
/* 307 */     this.pairs.clear();
/*     */   }
/*     */   
/*     */   private void writeWritablePairs(int lengthOfBlocksAfterLastPair) throws IOException {
/* 311 */     int unwrittenLength = lengthOfBlocksAfterLastPair; Iterator<Pair> it;
/* 312 */     for (it = this.pairs.descendingIterator(); it.hasNext(); ) {
/* 313 */       Pair p = it.next();
/* 314 */       if (p.hasBeenWritten()) {
/*     */         break;
/*     */       }
/* 317 */       unwrittenLength += p.length();
/*     */     } 
/* 319 */     for (it = this.pairs.iterator(); it.hasNext(); ) { Pair p = it.next();
/* 320 */       if (p.hasBeenWritten()) {
/*     */         continue;
/*     */       }
/* 323 */       unwrittenLength -= p.length();
/* 324 */       if (!p.canBeWritten(unwrittenLength)) {
/*     */         break;
/*     */       }
/* 327 */       p.writeTo(this.os); }
/*     */   
/*     */   }
/*     */   
/*     */   private void rewriteLastPairs() {
/* 332 */     LinkedList<Pair> lastPairs = new LinkedList<>();
/* 333 */     LinkedList<Integer> pairLength = new LinkedList<>();
/* 334 */     int offset = 0;
/* 335 */     for (Iterator<Pair> it = this.pairs.descendingIterator(); it.hasNext(); ) {
/* 336 */       Pair p = it.next();
/* 337 */       if (p.hasBeenWritten()) {
/*     */         break;
/*     */       }
/* 340 */       int len = p.length();
/* 341 */       pairLength.addFirst(Integer.valueOf(len));
/* 342 */       lastPairs.addFirst(p);
/* 343 */       offset += len;
/* 344 */       if (offset >= 12) {
/*     */         break;
/*     */       }
/*     */     } 
/* 348 */     lastPairs.forEach(this.pairs::remove);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 373 */     int lastPairsSize = lastPairs.size();
/* 374 */     int toExpand = 0;
/* 375 */     for (int i = 1; i < lastPairsSize; i++) {
/* 376 */       toExpand += ((Integer)pairLength.get(i)).intValue();
/*     */     }
/* 378 */     Pair replacement = new Pair();
/* 379 */     if (toExpand > 0) {
/* 380 */       replacement.prependLiteral(expand(toExpand, toExpand));
/*     */     }
/* 382 */     Pair splitCandidate = lastPairs.get(0);
/* 383 */     int stillNeeded = 12 - toExpand;
/* 384 */     int brLen = splitCandidate.hasBackReference() ? splitCandidate.backReferenceLength() : 0;
/* 385 */     if (splitCandidate.hasBackReference() && brLen >= 4 + stillNeeded) {
/* 386 */       replacement.prependLiteral(expand(toExpand + stillNeeded, stillNeeded));
/* 387 */       this.pairs.add(splitCandidate.splitWithNewBackReferenceLengthOf(brLen - stillNeeded));
/*     */     } else {
/* 389 */       if (splitCandidate.hasBackReference()) {
/* 390 */         replacement.prependLiteral(expand(toExpand + brLen, brLen));
/*     */       }
/* 392 */       splitCandidate.prependTo(replacement);
/*     */     } 
/* 394 */     this.pairs.add(replacement);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Parameters.Builder createParameterBuilder() {
/* 402 */     int maxLen = 65535;
/* 403 */     return Parameters.builder(65536)
/* 404 */       .withMinBackReferenceLength(4)
/* 405 */       .withMaxBackReferenceLength(65535)
/* 406 */       .withMaxOffset(65535)
/* 407 */       .withMaxLiteralLength(65535);
/*     */   }
/*     */   
/*     */   static final class Pair {
/* 411 */     private final Deque<byte[]> literals = (Deque)new LinkedList<>(); private int brOffset;
/*     */     private int brLength;
/*     */     private boolean written;
/*     */     
/*     */     private void prependLiteral(byte[] data) {
/* 416 */       this.literals.addFirst(data);
/*     */     }
/*     */     
/*     */     byte[] addLiteral(LZ77Compressor.LiteralBlock block) {
/* 420 */       byte[] copy = Arrays.copyOfRange(block.getData(), block.getOffset(), block
/* 421 */           .getOffset() + block.getLength());
/* 422 */       this.literals.add(copy);
/* 423 */       return copy;
/*     */     }
/*     */     
/*     */     void setBackReference(LZ77Compressor.BackReference block) {
/* 427 */       if (hasBackReference()) {
/* 428 */         throw new IllegalStateException();
/*     */       }
/* 430 */       this.brOffset = block.getOffset();
/* 431 */       this.brLength = block.getLength();
/*     */     }
/*     */     
/*     */     boolean hasBackReference() {
/* 435 */       return (this.brOffset > 0);
/*     */     }
/*     */     
/*     */     boolean canBeWritten(int lengthOfBlocksAfterThisPair) {
/* 439 */       return (hasBackReference() && lengthOfBlocksAfterThisPair >= 16);
/*     */     }
/*     */ 
/*     */     
/*     */     int length() {
/* 444 */       return literalLength() + this.brLength;
/*     */     }
/*     */     
/*     */     private boolean hasBeenWritten() {
/* 448 */       return this.written;
/*     */     }
/*     */     
/*     */     void writeTo(OutputStream out) throws IOException {
/* 452 */       int litLength = literalLength();
/* 453 */       out.write(lengths(litLength, this.brLength));
/* 454 */       if (litLength >= 15) {
/* 455 */         writeLength(litLength - 15, out);
/*     */       }
/* 457 */       for (byte[] b : this.literals) {
/* 458 */         out.write(b);
/*     */       }
/* 460 */       if (hasBackReference()) {
/* 461 */         ByteUtils.toLittleEndian(out, this.brOffset, 2);
/* 462 */         if (this.brLength - 4 >= 15) {
/* 463 */           writeLength(this.brLength - 4 - 15, out);
/*     */         }
/*     */       } 
/*     */       
/* 467 */       this.written = true;
/*     */     }
/*     */     
/*     */     private int literalLength() {
/* 471 */       return this.literals.stream().mapToInt(b -> b.length).sum();
/*     */     }
/*     */     
/*     */     private static int lengths(int litLength, int brLength) {
/* 475 */       int l = Math.min(litLength, 15);
/* 476 */       int br = (brLength < 4) ? 0 : ((brLength < 19) ? (brLength - 4) : 15);
/* 477 */       return l << 4 | br;
/*     */     }
/*     */     
/*     */     private static void writeLength(int length, OutputStream out) throws IOException {
/* 481 */       while (length >= 255) {
/* 482 */         out.write(255);
/* 483 */         length -= 255;
/*     */       } 
/* 485 */       out.write(length);
/*     */     }
/*     */     
/*     */     private int backReferenceLength() {
/* 489 */       return this.brLength;
/*     */     }
/*     */     
/*     */     private void prependTo(Pair other) {
/* 493 */       Iterator<byte[]> listBackwards = (Iterator)this.literals.descendingIterator();
/* 494 */       while (listBackwards.hasNext()) {
/* 495 */         other.prependLiteral(listBackwards.next());
/*     */       }
/*     */     }
/*     */     
/*     */     private Pair splitWithNewBackReferenceLengthOf(int newBackReferenceLength) {
/* 500 */       Pair p = new Pair();
/* 501 */       p.literals.addAll(this.literals);
/* 502 */       p.brOffset = this.brOffset;
/* 503 */       p.brLength = newBackReferenceLength;
/* 504 */       return p;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\compressors\lz4\BlockLZ4CompressorOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */