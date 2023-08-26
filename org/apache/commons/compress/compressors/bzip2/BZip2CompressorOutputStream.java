/*      */ package org.apache.commons.compress.compressors.bzip2;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ import java.util.Arrays;
/*      */ import org.apache.commons.compress.compressors.CompressorOutputStream;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class BZip2CompressorOutputStream
/*      */   extends CompressorOutputStream
/*      */   implements BZip2Constants
/*      */ {
/*      */   public static final int MIN_BLOCKSIZE = 1;
/*      */   public static final int MAX_BLOCKSIZE = 9;
/*      */   private static final int GREATER_ICOST = 15;
/*      */   private static final int LESSER_ICOST = 0;
/*      */   private int last;
/*      */   private final int blockSize100k;
/*      */   private int bsBuff;
/*      */   private int bsLive;
/*      */   
/*      */   private static void hbMakeCodeLengths(byte[] len, int[] freq, Data dat, int alphaSize, int maxLen) {
/*  150 */     int[] heap = dat.heap;
/*  151 */     int[] weight = dat.weight;
/*  152 */     int[] parent = dat.parent;
/*      */     
/*  154 */     for (int i = alphaSize; --i >= 0;) {
/*  155 */       weight[i + 1] = ((freq[i] == 0) ? 1 : freq[i]) << 8;
/*      */     }
/*      */     
/*  158 */     for (boolean tooLong = true; tooLong; ) {
/*  159 */       tooLong = false;
/*      */       
/*  161 */       int nNodes = alphaSize;
/*  162 */       int nHeap = 0;
/*  163 */       heap[0] = 0;
/*  164 */       weight[0] = 0;
/*  165 */       parent[0] = -2;
/*      */       int j;
/*  167 */       for (j = 1; j <= alphaSize; j++) {
/*  168 */         parent[j] = -1;
/*  169 */         nHeap++;
/*  170 */         heap[nHeap] = j;
/*      */         
/*  172 */         int zz = nHeap;
/*  173 */         int tmp = heap[zz];
/*  174 */         while (weight[tmp] < weight[heap[zz >> 1]]) {
/*  175 */           heap[zz] = heap[zz >> 1];
/*  176 */           zz >>= 1;
/*      */         } 
/*  178 */         heap[zz] = tmp;
/*      */       } 
/*      */       
/*  181 */       while (nHeap > 1) {
/*  182 */         int n1 = heap[1];
/*  183 */         heap[1] = heap[nHeap];
/*  184 */         nHeap--;
/*      */         
/*  186 */         int yy = 0;
/*  187 */         int zz = 1;
/*  188 */         int tmp = heap[1];
/*      */         
/*      */         while (true) {
/*  191 */           yy = zz << 1;
/*      */           
/*  193 */           if (yy > nHeap) {
/*      */             break;
/*      */           }
/*      */           
/*  197 */           if (yy < nHeap && weight[heap[yy + 1]] < weight[heap[yy]])
/*      */           {
/*  199 */             yy++;
/*      */           }
/*      */           
/*  202 */           if (weight[tmp] < weight[heap[yy]]) {
/*      */             break;
/*      */           }
/*      */           
/*  206 */           heap[zz] = heap[yy];
/*  207 */           zz = yy;
/*      */         } 
/*      */         
/*  210 */         heap[zz] = tmp;
/*      */         
/*  212 */         int n2 = heap[1];
/*  213 */         heap[1] = heap[nHeap];
/*  214 */         nHeap--;
/*      */         
/*  216 */         yy = 0;
/*  217 */         zz = 1;
/*  218 */         tmp = heap[1];
/*      */         
/*      */         while (true) {
/*  221 */           yy = zz << 1;
/*      */           
/*  223 */           if (yy > nHeap) {
/*      */             break;
/*      */           }
/*      */           
/*  227 */           if (yy < nHeap && weight[heap[yy + 1]] < weight[heap[yy]])
/*      */           {
/*  229 */             yy++;
/*      */           }
/*      */           
/*  232 */           if (weight[tmp] < weight[heap[yy]]) {
/*      */             break;
/*      */           }
/*      */           
/*  236 */           heap[zz] = heap[yy];
/*  237 */           zz = yy;
/*      */         } 
/*      */         
/*  240 */         heap[zz] = tmp;
/*  241 */         nNodes++;
/*  242 */         parent[n2] = nNodes; parent[n1] = nNodes;
/*      */         
/*  244 */         int weight_n1 = weight[n1];
/*  245 */         int weight_n2 = weight[n2];
/*  246 */         weight[nNodes] = (weight_n1 & 0xFFFFFF00) + (weight_n2 & 0xFFFFFF00) | 1 + 
/*      */           
/*  248 */           Math.max(weight_n1 & 0xFF, weight_n2 & 0xFF);
/*      */         
/*  250 */         parent[nNodes] = -1;
/*  251 */         nHeap++;
/*  252 */         heap[nHeap] = nNodes;
/*      */         
/*  254 */         tmp = 0;
/*  255 */         zz = nHeap;
/*  256 */         tmp = heap[zz];
/*  257 */         int weight_tmp = weight[tmp];
/*  258 */         while (weight_tmp < weight[heap[zz >> 1]]) {
/*  259 */           heap[zz] = heap[zz >> 1];
/*  260 */           zz >>= 1;
/*      */         } 
/*  262 */         heap[zz] = tmp;
/*      */       } 
/*      */ 
/*      */       
/*  266 */       for (j = 1; j <= alphaSize; j++) {
/*  267 */         int m = 0;
/*  268 */         int k = j;
/*      */         int parent_k;
/*  270 */         while ((parent_k = parent[k]) >= 0) {
/*  271 */           k = parent_k;
/*  272 */           m++;
/*      */         } 
/*      */         
/*  275 */         len[j - 1] = (byte)m;
/*  276 */         if (m > maxLen) {
/*  277 */           tooLong = true;
/*      */         }
/*      */       } 
/*      */       
/*  281 */       if (tooLong) {
/*  282 */         for (j = 1; j < alphaSize; j++) {
/*  283 */           int k = weight[j] >> 8;
/*  284 */           k = 1 + (k >> 1);
/*  285 */           weight[j] = k << 8;
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  304 */   private final CRC crc = new CRC();
/*      */   
/*      */   private int nInUse;
/*      */   
/*      */   private int nMTF;
/*      */   
/*  310 */   private int currentChar = -1;
/*      */ 
/*      */   
/*      */   private int runLength;
/*      */ 
/*      */   
/*      */   private int blockCRC;
/*      */ 
/*      */   
/*      */   private int combinedCRC;
/*      */ 
/*      */   
/*      */   private final int allowableBlockSize;
/*      */ 
/*      */   
/*      */   private Data data;
/*      */ 
/*      */   
/*      */   private BlockSort blockSorter;
/*      */ 
/*      */   
/*      */   private OutputStream out;
/*      */ 
/*      */   
/*      */   private volatile boolean closed;
/*      */ 
/*      */ 
/*      */   
/*      */   public static int chooseBlockSize(long inputLength) {
/*  339 */     return (inputLength > 0L) ? 
/*  340 */       (int)Math.min(inputLength / 132000L + 1L, 9L) : 9;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BZip2CompressorOutputStream(OutputStream out) throws IOException {
/*  356 */     this(out, 9);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BZip2CompressorOutputStream(OutputStream out, int blockSize) throws IOException {
/*  378 */     if (blockSize < 1) {
/*  379 */       throw new IllegalArgumentException("blockSize(" + blockSize + ") < 1");
/*      */     }
/*  381 */     if (blockSize > 9) {
/*  382 */       throw new IllegalArgumentException("blockSize(" + blockSize + ") > 9");
/*      */     }
/*      */     
/*  385 */     this.blockSize100k = blockSize;
/*  386 */     this.out = out;
/*      */ 
/*      */     
/*  389 */     this.allowableBlockSize = this.blockSize100k * 100000 - 20;
/*  390 */     init();
/*      */   }
/*      */ 
/*      */   
/*      */   public void write(int b) throws IOException {
/*  395 */     if (this.closed) {
/*  396 */       throw new IOException("Closed");
/*      */     }
/*  398 */     write0(b);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void writeRun() throws IOException {
/*  415 */     int lastShadow = this.last;
/*      */     
/*  417 */     if (lastShadow < this.allowableBlockSize) {
/*  418 */       int currentCharShadow = this.currentChar;
/*  419 */       Data dataShadow = this.data;
/*  420 */       dataShadow.inUse[currentCharShadow] = true;
/*  421 */       byte ch = (byte)currentCharShadow;
/*      */       
/*  423 */       int runLengthShadow = this.runLength;
/*  424 */       this.crc.updateCRC(currentCharShadow, runLengthShadow);
/*      */       
/*  426 */       switch (runLengthShadow) {
/*      */         case 1:
/*  428 */           dataShadow.block[lastShadow + 2] = ch;
/*  429 */           this.last = lastShadow + 1;
/*      */           return;
/*      */         
/*      */         case 2:
/*  433 */           dataShadow.block[lastShadow + 2] = ch;
/*  434 */           dataShadow.block[lastShadow + 3] = ch;
/*  435 */           this.last = lastShadow + 2;
/*      */           return;
/*      */         
/*      */         case 3:
/*  439 */           block = dataShadow.block;
/*  440 */           block[lastShadow + 2] = ch;
/*  441 */           block[lastShadow + 3] = ch;
/*  442 */           block[lastShadow + 4] = ch;
/*  443 */           this.last = lastShadow + 3;
/*      */           return;
/*      */       } 
/*      */ 
/*      */       
/*  448 */       runLengthShadow -= 4;
/*  449 */       dataShadow.inUse[runLengthShadow] = true;
/*  450 */       byte[] block = dataShadow.block;
/*  451 */       block[lastShadow + 2] = ch;
/*  452 */       block[lastShadow + 3] = ch;
/*  453 */       block[lastShadow + 4] = ch;
/*  454 */       block[lastShadow + 5] = ch;
/*  455 */       block[lastShadow + 6] = (byte)runLengthShadow;
/*  456 */       this.last = lastShadow + 5;
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */       
/*  462 */       endBlock();
/*  463 */       initBlock();
/*  464 */       writeRun();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void finalize() throws Throwable {
/*  473 */     if (!this.closed) {
/*  474 */       System.err.println("Unclosed BZip2CompressorOutputStream detected, will *not* close it");
/*      */     }
/*  476 */     super.finalize();
/*      */   }
/*      */ 
/*      */   
/*      */   public void finish() throws IOException {
/*  481 */     if (!this.closed) {
/*  482 */       this.closed = true;
/*      */       try {
/*  484 */         if (this.runLength > 0) {
/*  485 */           writeRun();
/*      */         }
/*  487 */         this.currentChar = -1;
/*  488 */         endBlock();
/*  489 */         endCompression();
/*      */       } finally {
/*  491 */         this.out = null;
/*  492 */         this.blockSorter = null;
/*  493 */         this.data = null;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void close() throws IOException {
/*  500 */     if (!this.closed) {
/*  501 */       try (OutputStream outShadow = this.out) {
/*  502 */         finish();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void flush() throws IOException {
/*  509 */     OutputStream outShadow = this.out;
/*  510 */     if (outShadow != null) {
/*  511 */       outShadow.flush();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void init() throws IOException {
/*  522 */     bsPutUByte(66);
/*  523 */     bsPutUByte(90);
/*      */     
/*  525 */     this.data = new Data(this.blockSize100k);
/*  526 */     this.blockSorter = new BlockSort(this.data);
/*      */ 
/*      */     
/*  529 */     bsPutUByte(104);
/*  530 */     bsPutUByte(48 + this.blockSize100k);
/*      */     
/*  532 */     this.combinedCRC = 0;
/*  533 */     initBlock();
/*      */   }
/*      */ 
/*      */   
/*      */   private void initBlock() {
/*  538 */     this.crc.initializeCRC();
/*  539 */     this.last = -1;
/*      */ 
/*      */     
/*  542 */     boolean[] inUse = this.data.inUse;
/*  543 */     for (int i = 256; --i >= 0;) {
/*  544 */       inUse[i] = false;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private void endBlock() throws IOException {
/*  550 */     this.blockCRC = this.crc.getFinalCRC();
/*  551 */     this.combinedCRC = this.combinedCRC << 1 | this.combinedCRC >>> 31;
/*  552 */     this.combinedCRC ^= this.blockCRC;
/*      */ 
/*      */     
/*  555 */     if (this.last == -1) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/*  560 */     blockSort();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  573 */     bsPutUByte(49);
/*  574 */     bsPutUByte(65);
/*  575 */     bsPutUByte(89);
/*  576 */     bsPutUByte(38);
/*  577 */     bsPutUByte(83);
/*  578 */     bsPutUByte(89);
/*      */ 
/*      */     
/*  581 */     bsPutInt(this.blockCRC);
/*      */ 
/*      */     
/*  584 */     bsW(1, 0);
/*      */ 
/*      */     
/*  587 */     moveToFrontCodeAndSend();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void endCompression() throws IOException {
/*  597 */     bsPutUByte(23);
/*  598 */     bsPutUByte(114);
/*  599 */     bsPutUByte(69);
/*  600 */     bsPutUByte(56);
/*  601 */     bsPutUByte(80);
/*  602 */     bsPutUByte(144);
/*      */     
/*  604 */     bsPutInt(this.combinedCRC);
/*  605 */     bsFinishedWithStream();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getBlockSize() {
/*  613 */     return this.blockSize100k;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void write(byte[] buf, int offs, int len) throws IOException {
/*  619 */     if (offs < 0) {
/*  620 */       throw new IndexOutOfBoundsException("offs(" + offs + ") < 0.");
/*      */     }
/*  622 */     if (len < 0) {
/*  623 */       throw new IndexOutOfBoundsException("len(" + len + ") < 0.");
/*      */     }
/*  625 */     if (offs + len > buf.length) {
/*  626 */       throw new IndexOutOfBoundsException("offs(" + offs + ") + len(" + len + ") > buf.length(" + buf.length + ").");
/*      */     }
/*      */ 
/*      */     
/*  630 */     if (this.closed) {
/*  631 */       throw new IOException("Stream closed");
/*      */     }
/*      */     
/*  634 */     for (int hi = offs + len; offs < hi;) {
/*  635 */       write0(buf[offs++]);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void write0(int b) throws IOException {
/*  644 */     if (this.currentChar != -1) {
/*  645 */       b &= 0xFF;
/*  646 */       if (this.currentChar == b) {
/*  647 */         if (++this.runLength > 254) {
/*  648 */           writeRun();
/*  649 */           this.currentChar = -1;
/*  650 */           this.runLength = 0;
/*      */         } 
/*      */       } else {
/*      */         
/*  654 */         writeRun();
/*  655 */         this.runLength = 1;
/*  656 */         this.currentChar = b;
/*      */       } 
/*      */     } else {
/*  659 */       this.currentChar = b & 0xFF;
/*  660 */       this.runLength++;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static void hbAssignCodes(int[] code, byte[] length, int minLen, int maxLen, int alphaSize) {
/*  667 */     int vec = 0;
/*  668 */     for (int n = minLen; n <= maxLen; n++) {
/*  669 */       for (int i = 0; i < alphaSize; i++) {
/*  670 */         if ((length[i] & 0xFF) == n) {
/*  671 */           code[i] = vec;
/*  672 */           vec++;
/*      */         } 
/*      */       } 
/*  675 */       vec <<= 1;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void bsFinishedWithStream() throws IOException {
/*  680 */     while (this.bsLive > 0) {
/*  681 */       int ch = this.bsBuff >> 24;
/*  682 */       this.out.write(ch);
/*  683 */       this.bsBuff <<= 8;
/*  684 */       this.bsLive -= 8;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void bsW(int n, int v) throws IOException {
/*  689 */     OutputStream outShadow = this.out;
/*  690 */     int bsLiveShadow = this.bsLive;
/*  691 */     int bsBuffShadow = this.bsBuff;
/*      */     
/*  693 */     while (bsLiveShadow >= 8) {
/*  694 */       outShadow.write(bsBuffShadow >> 24);
/*  695 */       bsBuffShadow <<= 8;
/*  696 */       bsLiveShadow -= 8;
/*      */     } 
/*      */     
/*  699 */     this.bsBuff = bsBuffShadow | v << 32 - bsLiveShadow - n;
/*  700 */     this.bsLive = bsLiveShadow + n;
/*      */   }
/*      */   
/*      */   private void bsPutUByte(int c) throws IOException {
/*  704 */     bsW(8, c);
/*      */   }
/*      */   
/*      */   private void bsPutInt(int u) throws IOException {
/*  708 */     bsW(8, u >> 24 & 0xFF);
/*  709 */     bsW(8, u >> 16 & 0xFF);
/*  710 */     bsW(8, u >> 8 & 0xFF);
/*  711 */     bsW(8, u & 0xFF);
/*      */   }
/*      */   
/*      */   private void sendMTFValues() throws IOException {
/*  715 */     byte[][] len = this.data.sendMTFValues_len;
/*  716 */     int alphaSize = this.nInUse + 2;
/*      */     
/*  718 */     for (int t = 6; --t >= 0; ) {
/*  719 */       byte[] len_t = len[t];
/*  720 */       for (int v = alphaSize; --v >= 0;) {
/*  721 */         len_t[v] = 15;
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  727 */     int nGroups = (this.nMTF < 200) ? 2 : ((this.nMTF < 600) ? 3 : ((this.nMTF < 1200) ? 4 : ((this.nMTF < 2400) ? 5 : 6)));
/*      */ 
/*      */ 
/*      */     
/*  731 */     sendMTFValues0(nGroups, alphaSize);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  736 */     int nSelectors = sendMTFValues1(nGroups, alphaSize);
/*      */ 
/*      */     
/*  739 */     sendMTFValues2(nGroups, nSelectors);
/*      */ 
/*      */     
/*  742 */     sendMTFValues3(nGroups, alphaSize);
/*      */ 
/*      */     
/*  745 */     sendMTFValues4();
/*      */ 
/*      */     
/*  748 */     sendMTFValues5(nGroups, nSelectors);
/*      */ 
/*      */     
/*  751 */     sendMTFValues6(nGroups, alphaSize);
/*      */ 
/*      */     
/*  754 */     sendMTFValues7();
/*      */   }
/*      */   
/*      */   private void sendMTFValues0(int nGroups, int alphaSize) {
/*  758 */     byte[][] len = this.data.sendMTFValues_len;
/*  759 */     int[] mtfFreq = this.data.mtfFreq;
/*      */     
/*  761 */     int remF = this.nMTF;
/*  762 */     int gs = 0;
/*      */     
/*  764 */     for (int nPart = nGroups; nPart > 0; nPart--) {
/*  765 */       int tFreq = remF / nPart;
/*  766 */       int ge = gs - 1;
/*  767 */       int aFreq = 0;
/*      */       
/*  769 */       for (int a = alphaSize - 1; aFreq < tFreq && ge < a;) {
/*  770 */         aFreq += mtfFreq[++ge];
/*      */       }
/*      */       
/*  773 */       if (ge > gs && nPart != nGroups && nPart != 1 && (nGroups - nPart & 0x1) != 0)
/*      */       {
/*  775 */         aFreq -= mtfFreq[ge--];
/*      */       }
/*      */       
/*  778 */       byte[] len_np = len[nPart - 1];
/*  779 */       for (int v = alphaSize; --v >= 0; ) {
/*  780 */         if (v >= gs && v <= ge) {
/*  781 */           len_np[v] = 0; continue;
/*      */         } 
/*  783 */         len_np[v] = 15;
/*      */       } 
/*      */ 
/*      */       
/*  787 */       gs = ge + 1;
/*  788 */       remF -= aFreq;
/*      */     } 
/*      */   }
/*      */   
/*      */   private int sendMTFValues1(int nGroups, int alphaSize) {
/*  793 */     Data dataShadow = this.data;
/*  794 */     int[][] rfreq = dataShadow.sendMTFValues_rfreq;
/*  795 */     int[] fave = dataShadow.sendMTFValues_fave;
/*  796 */     short[] cost = dataShadow.sendMTFValues_cost;
/*  797 */     char[] sfmap = dataShadow.sfmap;
/*  798 */     byte[] selector = dataShadow.selector;
/*  799 */     byte[][] len = dataShadow.sendMTFValues_len;
/*  800 */     byte[] len_0 = len[0];
/*  801 */     byte[] len_1 = len[1];
/*  802 */     byte[] len_2 = len[2];
/*  803 */     byte[] len_3 = len[3];
/*  804 */     byte[] len_4 = len[4];
/*  805 */     byte[] len_5 = len[5];
/*  806 */     int nMTFShadow = this.nMTF;
/*      */     
/*  808 */     int nSelectors = 0;
/*      */     
/*  810 */     for (int iter = 0; iter < 4; iter++) {
/*  811 */       for (int i = nGroups; --i >= 0; ) {
/*  812 */         fave[i] = 0;
/*  813 */         int[] rfreqt = rfreq[i];
/*  814 */         for (int j = alphaSize; --j >= 0;) {
/*  815 */           rfreqt[j] = 0;
/*      */         }
/*      */       } 
/*      */       
/*  819 */       nSelectors = 0;
/*      */       int gs;
/*  821 */       for (gs = 0; gs < this.nMTF; ) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  829 */         int ge = Math.min(gs + 50 - 1, nMTFShadow - 1);
/*      */         
/*  831 */         if (nGroups == 6) {
/*      */ 
/*      */           
/*  834 */           short cost0 = 0;
/*  835 */           short cost1 = 0;
/*  836 */           short cost2 = 0;
/*  837 */           short cost3 = 0;
/*  838 */           short cost4 = 0;
/*  839 */           short cost5 = 0;
/*      */           
/*  841 */           for (int m = gs; m <= ge; m++) {
/*  842 */             int icv = sfmap[m];
/*  843 */             cost0 = (short)(cost0 + (len_0[icv] & 0xFF));
/*  844 */             cost1 = (short)(cost1 + (len_1[icv] & 0xFF));
/*  845 */             cost2 = (short)(cost2 + (len_2[icv] & 0xFF));
/*  846 */             cost3 = (short)(cost3 + (len_3[icv] & 0xFF));
/*  847 */             cost4 = (short)(cost4 + (len_4[icv] & 0xFF));
/*  848 */             cost5 = (short)(cost5 + (len_5[icv] & 0xFF));
/*      */           } 
/*      */           
/*  851 */           cost[0] = cost0;
/*  852 */           cost[1] = cost1;
/*  853 */           cost[2] = cost2;
/*  854 */           cost[3] = cost3;
/*  855 */           cost[4] = cost4;
/*  856 */           cost[5] = cost5;
/*      */         } else {
/*      */           
/*  859 */           for (int n = nGroups; --n >= 0;) {
/*  860 */             cost[n] = 0;
/*      */           }
/*      */           
/*  863 */           for (int m = gs; m <= ge; m++) {
/*  864 */             int icv = sfmap[m];
/*  865 */             for (int i1 = nGroups; --i1 >= 0;) {
/*  866 */               cost[i1] = (short)(cost[i1] + (len[i1][icv] & 0xFF));
/*      */             }
/*      */           } 
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  875 */         int bt = -1;
/*  876 */         for (int j = nGroups, bc = 999999999; --j >= 0; ) {
/*  877 */           int cost_t = cost[j];
/*  878 */           if (cost_t < bc) {
/*  879 */             bc = cost_t;
/*  880 */             bt = j;
/*      */           } 
/*      */         } 
/*      */         
/*  884 */         fave[bt] = fave[bt] + 1;
/*  885 */         selector[nSelectors] = (byte)bt;
/*  886 */         nSelectors++;
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  891 */         int[] rfreq_bt = rfreq[bt];
/*  892 */         for (int k = gs; k <= ge; k++) {
/*  893 */           rfreq_bt[sfmap[k]] = rfreq_bt[sfmap[k]] + 1;
/*      */         }
/*      */         
/*  896 */         gs = ge + 1;
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  902 */       for (int t = 0; t < nGroups; t++) {
/*  903 */         hbMakeCodeLengths(len[t], rfreq[t], this.data, alphaSize, 20);
/*      */       }
/*      */     } 
/*      */     
/*  907 */     return nSelectors;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void sendMTFValues2(int nGroups, int nSelectors) {
/*  913 */     Data dataShadow = this.data;
/*  914 */     byte[] pos = dataShadow.sendMTFValues2_pos;
/*      */     int i;
/*  916 */     for (i = nGroups; --i >= 0;) {
/*  917 */       pos[i] = (byte)i;
/*      */     }
/*      */     
/*  920 */     for (i = 0; i < nSelectors; i++) {
/*  921 */       byte ll_i = dataShadow.selector[i];
/*  922 */       byte tmp = pos[0];
/*  923 */       int j = 0;
/*      */       
/*  925 */       while (ll_i != tmp) {
/*  926 */         j++;
/*  927 */         byte tmp2 = tmp;
/*  928 */         tmp = pos[j];
/*  929 */         pos[j] = tmp2;
/*      */       } 
/*      */       
/*  932 */       pos[0] = tmp;
/*  933 */       dataShadow.selectorMtf[i] = (byte)j;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void sendMTFValues3(int nGroups, int alphaSize) {
/*  938 */     int[][] code = this.data.sendMTFValues_code;
/*  939 */     byte[][] len = this.data.sendMTFValues_len;
/*      */     
/*  941 */     for (int t = 0; t < nGroups; t++) {
/*  942 */       int minLen = 32;
/*  943 */       int maxLen = 0;
/*  944 */       byte[] len_t = len[t];
/*  945 */       for (int i = alphaSize; --i >= 0; ) {
/*  946 */         int l = len_t[i] & 0xFF;
/*  947 */         if (l > maxLen) {
/*  948 */           maxLen = l;
/*      */         }
/*  950 */         if (l < minLen) {
/*  951 */           minLen = l;
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  958 */       hbAssignCodes(code[t], len[t], minLen, maxLen, alphaSize);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void sendMTFValues4() throws IOException {
/*  963 */     boolean[] inUse = this.data.inUse;
/*  964 */     boolean[] inUse16 = this.data.sentMTFValues4_inUse16;
/*      */     int i;
/*  966 */     for (i = 16; --i >= 0; ) {
/*  967 */       inUse16[i] = false;
/*  968 */       int i16 = i * 16;
/*  969 */       for (int k = 16; --k >= 0;) {
/*  970 */         if (inUse[i16 + k]) {
/*  971 */           inUse16[i] = true;
/*      */         }
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  977 */     for (i = 0; i < 16; i++) {
/*  978 */       bsW(1, inUse16[i] ? 1 : 0);
/*      */     }
/*      */     
/*  981 */     OutputStream outShadow = this.out;
/*  982 */     int bsLiveShadow = this.bsLive;
/*  983 */     int bsBuffShadow = this.bsBuff;
/*      */     
/*  985 */     for (int j = 0; j < 16; j++) {
/*  986 */       if (inUse16[j]) {
/*  987 */         int i16 = j * 16;
/*  988 */         for (int k = 0; k < 16; k++) {
/*      */           
/*  990 */           while (bsLiveShadow >= 8) {
/*  991 */             outShadow.write(bsBuffShadow >> 24);
/*  992 */             bsBuffShadow <<= 8;
/*  993 */             bsLiveShadow -= 8;
/*      */           } 
/*  995 */           if (inUse[i16 + k]) {
/*  996 */             bsBuffShadow |= 1 << 32 - bsLiveShadow - 1;
/*      */           }
/*  998 */           bsLiveShadow++;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1003 */     this.bsBuff = bsBuffShadow;
/* 1004 */     this.bsLive = bsLiveShadow;
/*      */   }
/*      */ 
/*      */   
/*      */   private void sendMTFValues5(int nGroups, int nSelectors) throws IOException {
/* 1009 */     bsW(3, nGroups);
/* 1010 */     bsW(15, nSelectors);
/*      */     
/* 1012 */     OutputStream outShadow = this.out;
/* 1013 */     byte[] selectorMtf = this.data.selectorMtf;
/*      */     
/* 1015 */     int bsLiveShadow = this.bsLive;
/* 1016 */     int bsBuffShadow = this.bsBuff;
/*      */     
/* 1018 */     for (int i = 0; i < nSelectors; i++) {
/* 1019 */       for (int j = 0, hj = selectorMtf[i] & 0xFF; j < hj; j++) {
/*      */         
/* 1021 */         while (bsLiveShadow >= 8) {
/* 1022 */           outShadow.write(bsBuffShadow >> 24);
/* 1023 */           bsBuffShadow <<= 8;
/* 1024 */           bsLiveShadow -= 8;
/*      */         } 
/* 1026 */         bsBuffShadow |= 1 << 32 - bsLiveShadow - 1;
/* 1027 */         bsLiveShadow++;
/*      */       } 
/*      */ 
/*      */       
/* 1031 */       while (bsLiveShadow >= 8) {
/* 1032 */         outShadow.write(bsBuffShadow >> 24);
/* 1033 */         bsBuffShadow <<= 8;
/* 1034 */         bsLiveShadow -= 8;
/*      */       } 
/*      */       
/* 1037 */       bsLiveShadow++;
/*      */     } 
/*      */     
/* 1040 */     this.bsBuff = bsBuffShadow;
/* 1041 */     this.bsLive = bsLiveShadow;
/*      */   }
/*      */ 
/*      */   
/*      */   private void sendMTFValues6(int nGroups, int alphaSize) throws IOException {
/* 1046 */     byte[][] len = this.data.sendMTFValues_len;
/* 1047 */     OutputStream outShadow = this.out;
/*      */     
/* 1049 */     int bsLiveShadow = this.bsLive;
/* 1050 */     int bsBuffShadow = this.bsBuff;
/*      */     
/* 1052 */     for (int t = 0; t < nGroups; t++) {
/* 1053 */       byte[] len_t = len[t];
/* 1054 */       int curr = len_t[0] & 0xFF;
/*      */ 
/*      */       
/* 1057 */       while (bsLiveShadow >= 8) {
/* 1058 */         outShadow.write(bsBuffShadow >> 24);
/* 1059 */         bsBuffShadow <<= 8;
/* 1060 */         bsLiveShadow -= 8;
/*      */       } 
/* 1062 */       bsBuffShadow |= curr << 32 - bsLiveShadow - 5;
/* 1063 */       bsLiveShadow += 5;
/*      */       
/* 1065 */       for (int i = 0; i < alphaSize; i++) {
/* 1066 */         int lti = len_t[i] & 0xFF;
/* 1067 */         while (curr < lti) {
/*      */           
/* 1069 */           while (bsLiveShadow >= 8) {
/* 1070 */             outShadow.write(bsBuffShadow >> 24);
/* 1071 */             bsBuffShadow <<= 8;
/* 1072 */             bsLiveShadow -= 8;
/*      */           } 
/* 1074 */           bsBuffShadow |= 2 << 32 - bsLiveShadow - 2;
/* 1075 */           bsLiveShadow += 2;
/*      */           
/* 1077 */           curr++;
/*      */         } 
/*      */         
/* 1080 */         while (curr > lti) {
/*      */           
/* 1082 */           while (bsLiveShadow >= 8) {
/* 1083 */             outShadow.write(bsBuffShadow >> 24);
/* 1084 */             bsBuffShadow <<= 8;
/* 1085 */             bsLiveShadow -= 8;
/*      */           } 
/* 1087 */           bsBuffShadow |= 3 << 32 - bsLiveShadow - 2;
/* 1088 */           bsLiveShadow += 2;
/*      */           
/* 1090 */           curr--;
/*      */         } 
/*      */ 
/*      */         
/* 1094 */         while (bsLiveShadow >= 8) {
/* 1095 */           outShadow.write(bsBuffShadow >> 24);
/* 1096 */           bsBuffShadow <<= 8;
/* 1097 */           bsLiveShadow -= 8;
/*      */         } 
/*      */         
/* 1100 */         bsLiveShadow++;
/*      */       } 
/*      */     } 
/*      */     
/* 1104 */     this.bsBuff = bsBuffShadow;
/* 1105 */     this.bsLive = bsLiveShadow;
/*      */   }
/*      */   
/*      */   private void sendMTFValues7() throws IOException {
/* 1109 */     Data dataShadow = this.data;
/* 1110 */     byte[][] len = dataShadow.sendMTFValues_len;
/* 1111 */     int[][] code = dataShadow.sendMTFValues_code;
/* 1112 */     OutputStream outShadow = this.out;
/* 1113 */     byte[] selector = dataShadow.selector;
/* 1114 */     char[] sfmap = dataShadow.sfmap;
/* 1115 */     int nMTFShadow = this.nMTF;
/*      */     
/* 1117 */     int selCtr = 0;
/*      */     
/* 1119 */     int bsLiveShadow = this.bsLive;
/* 1120 */     int bsBuffShadow = this.bsBuff;
/*      */     
/* 1122 */     for (int gs = 0; gs < nMTFShadow; ) {
/* 1123 */       int ge = Math.min(gs + 50 - 1, nMTFShadow - 1);
/* 1124 */       int selector_selCtr = selector[selCtr] & 0xFF;
/* 1125 */       int[] code_selCtr = code[selector_selCtr];
/* 1126 */       byte[] len_selCtr = len[selector_selCtr];
/*      */       
/* 1128 */       while (gs <= ge) {
/* 1129 */         int sfmap_i = sfmap[gs];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1135 */         while (bsLiveShadow >= 8) {
/* 1136 */           outShadow.write(bsBuffShadow >> 24);
/* 1137 */           bsBuffShadow <<= 8;
/* 1138 */           bsLiveShadow -= 8;
/*      */         } 
/* 1140 */         int n = len_selCtr[sfmap_i] & 0xFF;
/* 1141 */         bsBuffShadow |= code_selCtr[sfmap_i] << 32 - bsLiveShadow - n;
/* 1142 */         bsLiveShadow += n;
/*      */         
/* 1144 */         gs++;
/*      */       } 
/*      */       
/* 1147 */       gs = ge + 1;
/* 1148 */       selCtr++;
/*      */     } 
/*      */     
/* 1151 */     this.bsBuff = bsBuffShadow;
/* 1152 */     this.bsLive = bsLiveShadow;
/*      */   }
/*      */   
/*      */   private void moveToFrontCodeAndSend() throws IOException {
/* 1156 */     bsW(24, this.data.origPtr);
/* 1157 */     generateMTFValues();
/* 1158 */     sendMTFValues();
/*      */   }
/*      */   
/*      */   private void blockSort() {
/* 1162 */     this.blockSorter.blockSort(this.data, this.last);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void generateMTFValues() {
/* 1173 */     int lastShadow = this.last;
/* 1174 */     Data dataShadow = this.data;
/* 1175 */     boolean[] inUse = dataShadow.inUse;
/* 1176 */     byte[] block = dataShadow.block;
/* 1177 */     int[] fmap = dataShadow.fmap;
/* 1178 */     char[] sfmap = dataShadow.sfmap;
/* 1179 */     int[] mtfFreq = dataShadow.mtfFreq;
/* 1180 */     byte[] unseqToSeq = dataShadow.unseqToSeq;
/* 1181 */     byte[] yy = dataShadow.generateMTFValues_yy;
/*      */ 
/*      */     
/* 1184 */     int nInUseShadow = 0;
/* 1185 */     for (int i = 0; i < 256; i++) {
/* 1186 */       if (inUse[i]) {
/* 1187 */         unseqToSeq[i] = (byte)nInUseShadow;
/* 1188 */         nInUseShadow++;
/*      */       } 
/*      */     } 
/* 1191 */     this.nInUse = nInUseShadow;
/*      */     
/* 1193 */     int eob = nInUseShadow + 1;
/*      */     
/* 1195 */     Arrays.fill(mtfFreq, 0, eob + 1, 0);
/*      */     
/* 1197 */     for (int j = nInUseShadow; --j >= 0;) {
/* 1198 */       yy[j] = (byte)j;
/*      */     }
/*      */     
/* 1201 */     int wr = 0;
/* 1202 */     int zPend = 0;
/*      */     
/* 1204 */     for (int k = 0; k <= lastShadow; k++) {
/* 1205 */       byte ll_i = unseqToSeq[block[fmap[k]] & 0xFF];
/* 1206 */       byte tmp = yy[0];
/* 1207 */       int m = 0;
/*      */       
/* 1209 */       while (ll_i != tmp) {
/* 1210 */         m++;
/* 1211 */         byte tmp2 = tmp;
/* 1212 */         tmp = yy[m];
/* 1213 */         yy[m] = tmp2;
/*      */       } 
/* 1215 */       yy[0] = tmp;
/*      */       
/* 1217 */       if (m == 0) {
/* 1218 */         zPend++;
/*      */       } else {
/* 1220 */         if (zPend > 0) {
/* 1221 */           zPend--;
/*      */           while (true) {
/* 1223 */             if ((zPend & 0x1) == 0) {
/* 1224 */               sfmap[wr] = Character.MIN_VALUE;
/* 1225 */               wr++;
/* 1226 */               mtfFreq[0] = mtfFreq[0] + 1;
/*      */             } else {
/* 1228 */               sfmap[wr] = '\001';
/* 1229 */               wr++;
/* 1230 */               mtfFreq[1] = mtfFreq[1] + 1;
/*      */             } 
/*      */             
/* 1233 */             if (zPend < 2) {
/*      */               break;
/*      */             }
/* 1236 */             zPend = zPend - 2 >> 1;
/*      */           } 
/* 1238 */           zPend = 0;
/*      */         } 
/* 1240 */         sfmap[wr] = (char)(m + 1);
/* 1241 */         wr++;
/* 1242 */         mtfFreq[m + 1] = mtfFreq[m + 1] + 1;
/*      */       } 
/*      */     } 
/*      */     
/* 1246 */     if (zPend > 0) {
/* 1247 */       zPend--;
/*      */       while (true) {
/* 1249 */         if ((zPend & 0x1) == 0) {
/* 1250 */           sfmap[wr] = Character.MIN_VALUE;
/* 1251 */           wr++;
/* 1252 */           mtfFreq[0] = mtfFreq[0] + 1;
/*      */         } else {
/* 1254 */           sfmap[wr] = '\001';
/* 1255 */           wr++;
/* 1256 */           mtfFreq[1] = mtfFreq[1] + 1;
/*      */         } 
/*      */         
/* 1259 */         if (zPend < 2) {
/*      */           break;
/*      */         }
/* 1262 */         zPend = zPend - 2 >> 1;
/*      */       } 
/*      */     } 
/*      */     
/* 1266 */     sfmap[wr] = (char)eob;
/* 1267 */     mtfFreq[eob] = mtfFreq[eob] + 1;
/* 1268 */     this.nMTF = wr + 1;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static final class Data
/*      */   {
/* 1275 */     final boolean[] inUse = new boolean[256];
/* 1276 */     final byte[] unseqToSeq = new byte[256];
/* 1277 */     final int[] mtfFreq = new int[258];
/* 1278 */     final byte[] selector = new byte[18002];
/* 1279 */     final byte[] selectorMtf = new byte[18002];
/*      */     
/* 1281 */     final byte[] generateMTFValues_yy = new byte[256];
/* 1282 */     final byte[][] sendMTFValues_len = new byte[6][258];
/*      */     
/* 1284 */     final int[][] sendMTFValues_rfreq = new int[6][258];
/*      */     
/* 1286 */     final int[] sendMTFValues_fave = new int[6];
/* 1287 */     final short[] sendMTFValues_cost = new short[6];
/* 1288 */     final int[][] sendMTFValues_code = new int[6][258];
/*      */     
/* 1290 */     final byte[] sendMTFValues2_pos = new byte[6];
/* 1291 */     final boolean[] sentMTFValues4_inUse16 = new boolean[16];
/*      */     
/* 1293 */     final int[] heap = new int[260];
/* 1294 */     final int[] weight = new int[516];
/* 1295 */     final int[] parent = new int[516];
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final byte[] block;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final int[] fmap;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final char[] sfmap;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int origPtr;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Data(int blockSize100k) {
/* 1321 */       int n = blockSize100k * 100000;
/* 1322 */       this.block = new byte[n + 1 + 20];
/* 1323 */       this.fmap = new int[n];
/* 1324 */       this.sfmap = new char[2 * n];
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\compressors\bzip2\BZip2CompressorOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */