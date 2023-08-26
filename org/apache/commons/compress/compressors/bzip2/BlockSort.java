/*      */ package org.apache.commons.compress.compressors.bzip2;
/*      */ 
/*      */ import java.util.Arrays;
/*      */ import java.util.BitSet;
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
/*      */ class BlockSort
/*      */ {
/*      */   private static final int FTAB_LENGTH = 65537;
/*      */   private static final int QSORT_STACK_SIZE = 1000;
/*      */   private static final int FALLBACK_QSORT_STACK_SIZE = 100;
/*  124 */   private static final int STACK_SIZE = Math.max(1000, 100);
/*      */ 
/*      */   
/*      */   private int workDone;
/*      */ 
/*      */   
/*      */   private int workLimit;
/*      */   
/*      */   private boolean firstAttempt;
/*      */   
/*  134 */   private final int[] stack_ll = new int[STACK_SIZE];
/*  135 */   private final int[] stack_hh = new int[STACK_SIZE];
/*  136 */   private final int[] stack_dd = new int[1000];
/*      */   
/*  138 */   private final int[] mainSort_runningOrder = new int[256];
/*  139 */   private final int[] mainSort_copy = new int[256];
/*  140 */   private final boolean[] mainSort_bigDone = new boolean[256];
/*      */   
/*  142 */   private final int[] ftab = new int[65537];
/*      */   
/*      */   private final char[] quadrant;
/*      */   
/*      */   private static final int FALLBACK_QSORT_SMALL_THRESH = 10;
/*      */   
/*      */   private int[] eclass;
/*      */ 
/*      */   
/*      */   BlockSort(BZip2CompressorOutputStream.Data data) {
/*  152 */     this.quadrant = data.sfmap;
/*      */   }
/*      */   
/*      */   void blockSort(BZip2CompressorOutputStream.Data data, int last) {
/*  156 */     this.workLimit = 30 * last;
/*  157 */     this.workDone = 0;
/*  158 */     this.firstAttempt = true;
/*      */     
/*  160 */     if (last + 1 < 10000) {
/*  161 */       fallbackSort(data, last);
/*      */     } else {
/*  163 */       mainSort(data, last);
/*      */       
/*  165 */       if (this.firstAttempt && this.workDone > this.workLimit) {
/*  166 */         fallbackSort(data, last);
/*      */       }
/*      */     } 
/*      */     
/*  170 */     int[] fmap = data.fmap;
/*  171 */     data.origPtr = -1;
/*  172 */     for (int i = 0; i <= last; i++) {
/*  173 */       if (fmap[i] == 0) {
/*  174 */         data.origPtr = i;
/*      */         break;
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
/*      */   final void fallbackSort(BZip2CompressorOutputStream.Data data, int last) {
/*  189 */     data.block[0] = data.block[last + 1];
/*  190 */     fallbackSort(data.fmap, data.block, last + 1); int i;
/*  191 */     for (i = 0; i < last + 1; i++) {
/*  192 */       data.fmap[i] = data.fmap[i] - 1;
/*      */     }
/*  194 */     for (i = 0; i < last + 1; i++) {
/*  195 */       if (data.fmap[i] == -1) {
/*  196 */         data.fmap[i] = last;
/*      */         break;
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
/*      */   private void fallbackSimpleSort(int[] fmap, int[] eclass, int lo, int hi) {
/*  273 */     if (lo == hi) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/*  278 */     if (hi - lo > 3) {
/*  279 */       for (int j = hi - 4; j >= lo; j--) {
/*  280 */         int tmp = fmap[j];
/*  281 */         int ec_tmp = eclass[tmp]; int k;
/*  282 */         for (k = j + 4; k <= hi && ec_tmp > eclass[fmap[k]]; 
/*  283 */           k += 4) {
/*  284 */           fmap[k - 4] = fmap[k];
/*      */         }
/*  286 */         fmap[k - 4] = tmp;
/*      */       } 
/*      */     }
/*      */     
/*  290 */     for (int i = hi - 1; i >= lo; i--) {
/*  291 */       int tmp = fmap[i];
/*  292 */       int ec_tmp = eclass[tmp]; int j;
/*  293 */       for (j = i + 1; j <= hi && ec_tmp > eclass[fmap[j]]; j++) {
/*  294 */         fmap[j - 1] = fmap[j];
/*      */       }
/*  296 */       fmap[j - 1] = tmp;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fswap(int[] fmap, int zz1, int zz2) {
/*  306 */     int zztmp = fmap[zz1];
/*  307 */     fmap[zz1] = fmap[zz2];
/*  308 */     fmap[zz2] = zztmp;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fvswap(int[] fmap, int yyp1, int yyp2, int yyn) {
/*  315 */     while (yyn > 0) {
/*  316 */       fswap(fmap, yyp1, yyp2);
/*  317 */       yyp1++; yyp2++; yyn--;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void fpush(int sp, int lz, int hz) {
/*  322 */     this.stack_ll[sp] = lz;
/*  323 */     this.stack_hh[sp] = hz;
/*      */   }
/*      */   
/*      */   private int[] fpop(int sp) {
/*  327 */     return new int[] { this.stack_ll[sp], this.stack_hh[sp] };
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
/*      */   private void fallbackQSort3(int[] fmap, int[] eclass, int loSt, int hiSt) {
/*  346 */     long r = 0L;
/*  347 */     int sp = 0;
/*  348 */     fpush(sp++, loSt, hiSt);
/*      */     
/*  350 */     while (sp > 0) {
/*  351 */       long med; int[] s = fpop(--sp);
/*  352 */       int lo = s[0], hi = s[1];
/*      */       
/*  354 */       if (hi - lo < 10) {
/*  355 */         fallbackSimpleSort(fmap, eclass, lo, hi);
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         continue;
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  366 */       r = (r * 7621L + 1L) % 32768L;
/*  367 */       long r3 = r % 3L;
/*      */       
/*  369 */       if (r3 == 0L) {
/*  370 */         med = eclass[fmap[lo]];
/*  371 */       } else if (r3 == 1L) {
/*  372 */         med = eclass[fmap[lo + hi >>> 1]];
/*      */       } else {
/*  374 */         med = eclass[fmap[hi]];
/*      */       } 
/*      */       
/*  377 */       int ltLo = lo, unLo = ltLo;
/*  378 */       int gtHi = hi, unHi = gtHi;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       while (true) {
/*  384 */         if (unLo <= unHi) {
/*      */ 
/*      */           
/*  387 */           int i = eclass[fmap[unLo]] - (int)med;
/*  388 */           if (i == 0) {
/*  389 */             fswap(fmap, unLo, ltLo);
/*  390 */             ltLo++; unLo++;
/*      */             continue;
/*      */           } 
/*  393 */           if (i <= 0) {
/*      */ 
/*      */             
/*  396 */             unLo++; continue;
/*      */           } 
/*      */         } 
/*  399 */         while (unLo <= unHi) {
/*      */ 
/*      */           
/*  402 */           int i = eclass[fmap[unHi]] - (int)med;
/*  403 */           if (i == 0) {
/*  404 */             fswap(fmap, unHi, gtHi);
/*  405 */             gtHi--; unHi--;
/*      */             continue;
/*      */           } 
/*  408 */           if (i < 0) {
/*      */             break;
/*      */           }
/*  411 */           unHi--;
/*      */         } 
/*  413 */         if (unLo > unHi) {
/*      */           break;
/*      */         }
/*  416 */         fswap(fmap, unLo, unHi); unLo++; unHi--;
/*      */       } 
/*      */       
/*  419 */       if (gtHi < ltLo) {
/*      */         continue;
/*      */       }
/*      */       
/*  423 */       int n = Math.min(ltLo - lo, unLo - ltLo);
/*  424 */       fvswap(fmap, lo, unLo - n, n);
/*  425 */       int m = Math.min(hi - gtHi, gtHi - unHi);
/*  426 */       fvswap(fmap, unHi + 1, hi - m + 1, m);
/*      */       
/*  428 */       n = lo + unLo - ltLo - 1;
/*  429 */       m = hi - gtHi - unHi + 1;
/*      */       
/*  431 */       if (n - lo > hi - m) {
/*  432 */         fpush(sp++, lo, n);
/*  433 */         fpush(sp++, m, hi); continue;
/*      */       } 
/*  435 */       fpush(sp++, m, hi);
/*  436 */       fpush(sp++, lo, n);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int[] getEclass() {
/*  447 */     if (this.eclass == null) {
/*  448 */       this.eclass = new int[this.quadrant.length / 2];
/*      */     }
/*  450 */     return this.eclass;
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
/*      */   final void fallbackSort(int[] fmap, byte[] block, int nblock) {
/*  471 */     int nNotDone, ftab[] = new int[257];
/*      */ 
/*      */ 
/*      */     
/*  475 */     int[] eclass = getEclass();
/*      */     int i;
/*  477 */     for (i = 0; i < nblock; i++) {
/*  478 */       eclass[i] = 0;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  484 */     for (i = 0; i < nblock; i++) {
/*  485 */       ftab[block[i] & 0xFF] = ftab[block[i] & 0xFF] + 1;
/*      */     }
/*  487 */     for (i = 1; i < 257; i++) {
/*  488 */       ftab[i] = ftab[i] + ftab[i - 1];
/*      */     }
/*      */     
/*  491 */     for (i = 0; i < nblock; i++) {
/*  492 */       int j = block[i] & 0xFF;
/*  493 */       int k = ftab[j] - 1;
/*  494 */       ftab[j] = k;
/*  495 */       fmap[k] = i;
/*      */     } 
/*      */     
/*  498 */     int nBhtab = 64 + nblock;
/*  499 */     BitSet bhtab = new BitSet(nBhtab);
/*  500 */     for (i = 0; i < 256; i++) {
/*  501 */       bhtab.set(ftab[i]);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  511 */     for (i = 0; i < 32; i++) {
/*  512 */       bhtab.set(nblock + 2 * i);
/*  513 */       bhtab.clear(nblock + 2 * i + 1);
/*      */     } 
/*      */ 
/*      */     
/*  517 */     int H = 1;
/*      */     
/*      */     do {
/*  520 */       int j = 0;
/*  521 */       for (i = 0; i < nblock; i++) {
/*  522 */         if (bhtab.get(i)) {
/*  523 */           j = i;
/*      */         }
/*  525 */         int k = fmap[i] - H;
/*  526 */         if (k < 0) {
/*  527 */           k += nblock;
/*      */         }
/*  529 */         eclass[k] = j;
/*      */       } 
/*      */       
/*  532 */       nNotDone = 0;
/*  533 */       int r = -1;
/*      */ 
/*      */       
/*      */       while (true) {
/*  537 */         int k = r + 1;
/*  538 */         k = bhtab.nextClearBit(k);
/*  539 */         int l = k - 1;
/*  540 */         if (l >= nblock) {
/*      */           break;
/*      */         }
/*  543 */         k = bhtab.nextSetBit(k + 1);
/*  544 */         r = k - 1;
/*  545 */         if (r >= nblock) {
/*      */           break;
/*      */         }
/*      */ 
/*      */         
/*  550 */         if (r > l) {
/*  551 */           nNotDone += r - l + 1;
/*  552 */           fallbackQSort3(fmap, eclass, l, r);
/*      */ 
/*      */           
/*  555 */           int cc = -1;
/*  556 */           for (i = l; i <= r; i++) {
/*  557 */             int cc1 = eclass[fmap[i]];
/*  558 */             if (cc != cc1) {
/*  559 */               bhtab.set(i);
/*  560 */               cc = cc1;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/*  566 */       H *= 2;
/*  567 */     } while (H <= nblock && nNotDone != 0);
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
/*  580 */   private static final int[] INCS = new int[] { 1, 4, 13, 40, 121, 364, 1093, 3280, 9841, 29524, 88573, 265720, 797161, 2391484 };
/*      */ 
/*      */   
/*      */   private static final int SMALL_THRESH = 20;
/*      */ 
/*      */   
/*      */   private static final int DEPTH_THRESH = 10;
/*      */ 
/*      */   
/*      */   private static final int WORK_FACTOR = 30;
/*      */   
/*      */   private static final int SETMASK = 2097152;
/*      */   
/*      */   private static final int CLEARMASK = -2097153;
/*      */ 
/*      */   
/*      */   private boolean mainSimpleSort(BZip2CompressorOutputStream.Data dataShadow, int lo, int hi, int d, int lastShadow) {
/*  597 */     int bigN = hi - lo + 1;
/*  598 */     if (bigN < 2) {
/*  599 */       return (this.firstAttempt && this.workDone > this.workLimit);
/*      */     }
/*      */     
/*  602 */     int hp = 0;
/*  603 */     while (INCS[hp] < bigN) {
/*  604 */       hp++;
/*      */     }
/*      */     
/*  607 */     int[] fmap = dataShadow.fmap;
/*  608 */     char[] quadrant = this.quadrant;
/*  609 */     byte[] block = dataShadow.block;
/*  610 */     int lastPlus1 = lastShadow + 1;
/*  611 */     boolean firstAttemptShadow = this.firstAttempt;
/*  612 */     int workLimitShadow = this.workLimit;
/*  613 */     int workDoneShadow = this.workDone;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  618 */     label97: while (--hp >= 0) {
/*  619 */       int h = INCS[hp];
/*  620 */       int mj = lo + h - 1;
/*      */       
/*  622 */       for (int i = lo + h; i <= hi; ) {
/*      */         
/*  624 */         for (int k = 3; i <= hi && --k >= 0; i++) {
/*  625 */           int v = fmap[i];
/*  626 */           int vd = v + d;
/*  627 */           int j = i;
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
/*  639 */           boolean onceRunned = false;
/*  640 */           int a = 0;
/*      */           
/*      */           while (true) {
/*  643 */             if (onceRunned) {
/*  644 */               fmap[j] = a;
/*  645 */               if ((j -= h) <= mj) {
/*      */                 break;
/*      */               }
/*      */             } else {
/*  649 */               onceRunned = true;
/*      */             } 
/*      */             
/*  652 */             a = fmap[j - h];
/*  653 */             int i1 = a + d;
/*  654 */             int i2 = vd;
/*      */ 
/*      */ 
/*      */             
/*  658 */             if (block[i1 + 1] == block[i2 + 1]) {
/*  659 */               if (block[i1 + 2] == block[i2 + 2]) {
/*  660 */                 if (block[i1 + 3] == block[i2 + 3]) {
/*  661 */                   if (block[i1 + 4] == block[i2 + 4]) {
/*  662 */                     if (block[i1 + 5] == block[i2 + 5]) {
/*  663 */                       i1 += 6; i2 += 6; if (block[i1] == block[i2]) {
/*  664 */                         int x = lastShadow;
/*  665 */                         while (x > 0) {
/*  666 */                           x -= 4;
/*  667 */                           if (block[i1 + 1] == block[i2 + 1]) {
/*  668 */                             if (quadrant[i1] == quadrant[i2]) {
/*  669 */                               if (block[i1 + 2] == block[i2 + 2]) {
/*  670 */                                 if (quadrant[i1 + 1] == quadrant[i2 + 1]) {
/*  671 */                                   if (block[i1 + 3] == block[i2 + 3]) {
/*  672 */                                     if (quadrant[i1 + 2] == quadrant[i2 + 2]) {
/*  673 */                                       if (block[i1 + 4] == block[i2 + 4]) {
/*  674 */                                         if (quadrant[i1 + 3] == quadrant[i2 + 3]) {
/*  675 */                                           i1 += 4; if (i1 >= lastPlus1) {
/*  676 */                                             i1 -= lastPlus1;
/*      */                                           }
/*  678 */                                           i2 += 4; if (i2 >= lastPlus1) {
/*  679 */                                             i2 -= lastPlus1;
/*      */                                           }
/*  681 */                                           workDoneShadow++;
/*      */                                           continue;
/*      */                                         } 
/*  684 */                                         if (quadrant[i1 + 3] > quadrant[i2 + 3]) {
/*      */                                           continue;
/*      */                                         }
/*      */                                         break;
/*      */                                       } 
/*  689 */                                       if ((block[i1 + 4] & 0xFF) > (block[i2 + 4] & 0xFF)) {
/*      */                                         continue;
/*      */                                       }
/*      */                                       break;
/*      */                                     } 
/*  694 */                                     if (quadrant[i1 + 2] > quadrant[i2 + 2]) {
/*      */                                       continue;
/*      */                                     }
/*      */                                     break;
/*      */                                   } 
/*  699 */                                   if ((block[i1 + 3] & 0xFF) > (block[i2 + 3] & 0xFF)) {
/*      */                                     continue;
/*      */                                   }
/*      */                                   break;
/*      */                                 } 
/*  704 */                                 if (quadrant[i1 + 1] > quadrant[i2 + 1]) {
/*      */                                   continue;
/*      */                                 }
/*      */                                 break;
/*      */                               } 
/*  709 */                               if ((block[i1 + 2] & 0xFF) > (block[i2 + 2] & 0xFF)) {
/*      */                                 continue;
/*      */                               }
/*      */                               break;
/*      */                             } 
/*  714 */                             if (quadrant[i1] > quadrant[i2]) {
/*      */                               continue;
/*      */                             }
/*      */                             break;
/*      */                           } 
/*  719 */                           if ((block[i1 + 1] & 0xFF) > (block[i2 + 1] & 0xFF));
/*      */                         } 
/*      */ 
/*      */                         
/*      */                         break;
/*      */                       } 
/*      */ 
/*      */                       
/*  727 */                       if ((block[i1] & 0xFF) > (block[i2] & 0xFF)) {
/*      */                         continue;
/*      */                       }
/*      */                       break;
/*      */                     } 
/*  732 */                     if ((block[i1 + 5] & 0xFF) > (block[i2 + 5] & 0xFF)) {
/*      */                       continue;
/*      */                     }
/*      */                     break;
/*      */                   } 
/*  737 */                   if ((block[i1 + 4] & 0xFF) > (block[i2 + 4] & 0xFF)) {
/*      */                     continue;
/*      */                   }
/*      */                   break;
/*      */                 } 
/*  742 */                 if ((block[i1 + 3] & 0xFF) > (block[i2 + 3] & 0xFF)) {
/*      */                   continue;
/*      */                 }
/*      */                 break;
/*      */               } 
/*  747 */               if ((block[i1 + 2] & 0xFF) > (block[i2 + 2] & 0xFF)) {
/*      */                 continue;
/*      */               }
/*      */               break;
/*      */             } 
/*  752 */             if ((block[i1 + 1] & 0xFF) > (block[i2 + 1] & 0xFF)) {
/*      */               continue;
/*      */             }
/*      */ 
/*      */             
/*      */             break;
/*      */           } 
/*      */           
/*  760 */           fmap[j] = v;
/*      */         } 
/*      */         
/*  763 */         if (firstAttemptShadow && i <= hi && workDoneShadow > workLimitShadow) {
/*      */           break label97;
/*      */         }
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  770 */     this.workDone = workDoneShadow;
/*  771 */     return (firstAttemptShadow && workDoneShadow > workLimitShadow);
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
/*      */   private static void vswap(int[] fmap, int p1, int p2, int n) {
/*  783 */     n += p1;
/*  784 */     while (p1 < n) {
/*  785 */       int t = fmap[p1];
/*  786 */       fmap[p1++] = fmap[p2];
/*  787 */       fmap[p2++] = t;
/*      */     } 
/*      */   }
/*      */   
/*      */   private static int med3(int a, int b, int c) {
/*  792 */     return (a < b) ? ((b < c) ? b : ((a < c) ? c : a)) : ((b > c) ? b : ((a > c) ? c : a));
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
/*      */   private void mainQSort3(BZip2CompressorOutputStream.Data dataShadow, int loSt, int hiSt, int dSt, int last) {
/*  806 */     int[] stack_ll = this.stack_ll;
/*  807 */     int[] stack_hh = this.stack_hh;
/*  808 */     int[] stack_dd = this.stack_dd;
/*  809 */     int[] fmap = dataShadow.fmap;
/*  810 */     byte[] block = dataShadow.block;
/*      */     
/*  812 */     stack_ll[0] = loSt;
/*  813 */     stack_hh[0] = hiSt;
/*  814 */     stack_dd[0] = dSt;
/*      */     
/*  816 */     for (int sp = 1; --sp >= 0; ) {
/*  817 */       int lo = stack_ll[sp];
/*  818 */       int hi = stack_hh[sp];
/*  819 */       int d = stack_dd[sp];
/*      */       
/*  821 */       if (hi - lo < 20 || d > 10) {
/*  822 */         if (mainSimpleSort(dataShadow, lo, hi, d, last))
/*      */           return; 
/*      */         continue;
/*      */       } 
/*  826 */       int d1 = d + 1;
/*  827 */       int med = med3(block[fmap[lo] + d1] & 0xFF, block[fmap[hi] + d1] & 0xFF, block[fmap[lo + hi >>> 1] + d1] & 0xFF);
/*      */ 
/*      */ 
/*      */       
/*  831 */       int unLo = lo;
/*  832 */       int unHi = hi;
/*  833 */       int ltLo = lo;
/*  834 */       int gtHi = hi;
/*      */       
/*      */       while (true) {
/*  837 */         if (unLo <= unHi) {
/*  838 */           int i = (block[fmap[unLo] + d1] & 0xFF) - med;
/*      */           
/*  840 */           if (i == 0) {
/*  841 */             int j = fmap[unLo];
/*  842 */             fmap[unLo++] = fmap[ltLo];
/*  843 */             fmap[ltLo++] = j; continue;
/*  844 */           }  if (i < 0) {
/*  845 */             unLo++;
/*      */             
/*      */             continue;
/*      */           } 
/*      */         } 
/*      */         
/*  851 */         while (unLo <= unHi) {
/*  852 */           int i = (block[fmap[unHi] + d1] & 0xFF) - med;
/*      */           
/*  854 */           if (i == 0) {
/*  855 */             int j = fmap[unHi];
/*  856 */             fmap[unHi--] = fmap[gtHi];
/*  857 */             fmap[gtHi--] = j; continue;
/*  858 */           }  if (i > 0) {
/*  859 */             unHi--;
/*      */           }
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/*  865 */         if (unLo > unHi) {
/*      */           break;
/*      */         }
/*  868 */         int temp = fmap[unLo];
/*  869 */         fmap[unLo++] = fmap[unHi];
/*  870 */         fmap[unHi--] = temp;
/*      */       } 
/*      */       
/*  873 */       if (gtHi < ltLo) {
/*  874 */         stack_ll[sp] = lo;
/*  875 */         stack_hh[sp] = hi;
/*  876 */         stack_dd[sp] = d1;
/*  877 */         sp++; continue;
/*      */       } 
/*  879 */       int n = Math.min(ltLo - lo, unLo - ltLo);
/*  880 */       vswap(fmap, lo, unLo - n, n);
/*  881 */       int m = Math.min(hi - gtHi, gtHi - unHi);
/*  882 */       vswap(fmap, unLo, hi - m + 1, m);
/*      */       
/*  884 */       n = lo + unLo - ltLo - 1;
/*  885 */       m = hi - gtHi - unHi + 1;
/*      */       
/*  887 */       stack_ll[sp] = lo;
/*  888 */       stack_hh[sp] = n;
/*  889 */       stack_dd[sp] = d;
/*  890 */       sp++;
/*      */       
/*  892 */       stack_ll[sp] = n + 1;
/*  893 */       stack_hh[sp] = m - 1;
/*  894 */       stack_dd[sp] = d1;
/*  895 */       sp++;
/*      */       
/*  897 */       stack_ll[sp] = m;
/*  898 */       stack_hh[sp] = hi;
/*  899 */       stack_dd[sp] = d;
/*  900 */       sp++;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final void mainSort(BZip2CompressorOutputStream.Data dataShadow, int lastShadow) {
/*  911 */     int[] runningOrder = this.mainSort_runningOrder;
/*  912 */     int[] copy = this.mainSort_copy;
/*  913 */     boolean[] bigDone = this.mainSort_bigDone;
/*  914 */     int[] ftab = this.ftab;
/*  915 */     byte[] block = dataShadow.block;
/*  916 */     int[] fmap = dataShadow.fmap;
/*  917 */     char[] quadrant = this.quadrant;
/*  918 */     int workLimitShadow = this.workLimit;
/*  919 */     boolean firstAttemptShadow = this.firstAttempt;
/*      */ 
/*      */     
/*  922 */     Arrays.fill(ftab, 0);
/*      */ 
/*      */ 
/*      */     
/*      */     int i;
/*      */ 
/*      */     
/*  929 */     for (i = 0; i < 20; i++) {
/*  930 */       block[lastShadow + i + 2] = block[i % (lastShadow + 1) + 1];
/*      */     }
/*  932 */     for (i = lastShadow + 20 + 1; --i >= 0;) {
/*  933 */       quadrant[i] = Character.MIN_VALUE;
/*      */     }
/*  935 */     block[0] = block[lastShadow + 1];
/*      */ 
/*      */ 
/*      */     
/*  939 */     int c1 = block[0] & 0xFF; int k;
/*  940 */     for (k = 0; k <= lastShadow; k++) {
/*  941 */       int c2 = block[k + 1] & 0xFF;
/*  942 */       ftab[(c1 << 8) + c2] = ftab[(c1 << 8) + c2] + 1;
/*  943 */       c1 = c2;
/*      */     } 
/*      */     
/*  946 */     for (k = 1; k <= 65536; k++) {
/*  947 */       ftab[k] = ftab[k] + ftab[k - 1];
/*      */     }
/*      */     
/*  950 */     c1 = block[1] & 0xFF;
/*  951 */     for (k = 0; k < lastShadow; k++) {
/*  952 */       int c2 = block[k + 2] & 0xFF;
/*  953 */       ftab[(c1 << 8) + c2] = ftab[(c1 << 8) + c2] - 1; fmap[ftab[(c1 << 8) + c2] - 1] = k;
/*  954 */       c1 = c2;
/*      */     } 
/*      */     
/*  957 */     ftab[((block[lastShadow + 1] & 0xFF) << 8) + (block[1] & 0xFF)] = ftab[((block[lastShadow + 1] & 0xFF) << 8) + (block[1] & 0xFF)] - 1; fmap[ftab[((block[lastShadow + 1] & 0xFF) << 8) + (block[1] & 0xFF)] - 1] = lastShadow;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  963 */     for (k = 256; --k >= 0; ) {
/*  964 */       bigDone[k] = false;
/*  965 */       runningOrder[k] = k;
/*      */     } 
/*      */ 
/*      */     
/*  969 */     for (int h = 364; h != 1; ) {
/*  970 */       h /= 3;
/*  971 */       for (int m = h; m <= 255; m++) {
/*  972 */         int vv = runningOrder[m];
/*  973 */         int a = ftab[vv + 1 << 8] - ftab[vv << 8];
/*  974 */         int b = h - 1;
/*  975 */         int n = m; int ro;
/*  976 */         for (ro = runningOrder[n - h]; ftab[ro + 1 << 8] - ftab[ro << 8] > a; ro = runningOrder[n - h]) {
/*      */           
/*  978 */           runningOrder[n] = ro;
/*  979 */           n -= h;
/*  980 */           if (n <= b) {
/*      */             break;
/*      */           }
/*      */         } 
/*  984 */         runningOrder[n] = vv;
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  991 */     for (int j = 0; j <= 255; j++) {
/*      */ 
/*      */ 
/*      */       
/*  995 */       int ss = runningOrder[j];
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       int m;
/*      */ 
/*      */ 
/*      */       
/* 1004 */       for (m = 0; m <= 255; m++) {
/* 1005 */         int sb = (ss << 8) + m;
/* 1006 */         int ftab_sb = ftab[sb];
/* 1007 */         if ((ftab_sb & 0x200000) != 2097152) {
/* 1008 */           int lo = ftab_sb & 0xFFDFFFFF;
/* 1009 */           int hi = (ftab[sb + 1] & 0xFFDFFFFF) - 1;
/* 1010 */           if (hi > lo) {
/* 1011 */             mainQSort3(dataShadow, lo, hi, 2, lastShadow);
/* 1012 */             if (firstAttemptShadow && this.workDone > workLimitShadow) {
/*      */               return;
/*      */             }
/*      */           } 
/*      */           
/* 1017 */           ftab[sb] = ftab_sb | 0x200000;
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1025 */       for (m = 0; m <= 255; m++) {
/* 1026 */         copy[m] = ftab[(m << 8) + ss] & 0xFFDFFFFF;
/*      */       }
/*      */       int hj;
/* 1029 */       for (m = ftab[ss << 8] & 0xFFDFFFFF, hj = ftab[ss + 1 << 8] & 0xFFDFFFFF; m < hj; m++) {
/* 1030 */         int fmap_j = fmap[m];
/* 1031 */         c1 = block[fmap_j] & 0xFF;
/* 1032 */         if (!bigDone[c1]) {
/* 1033 */           fmap[copy[c1]] = (fmap_j == 0) ? lastShadow : (fmap_j - 1);
/* 1034 */           copy[c1] = copy[c1] + 1;
/*      */         } 
/*      */       } 
/*      */       
/* 1038 */       for (m = 256; --m >= 0;) {
/* 1039 */         ftab[(m << 8) + ss] = ftab[(m << 8) + ss] | 0x200000;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1050 */       bigDone[ss] = true;
/*      */       
/* 1052 */       if (j < 255) {
/* 1053 */         int bbStart = ftab[ss << 8] & 0xFFDFFFFF;
/* 1054 */         int bbSize = (ftab[ss + 1 << 8] & 0xFFDFFFFF) - bbStart;
/* 1055 */         int shifts = 0;
/*      */         
/* 1057 */         while (bbSize >> shifts > 65534) {
/* 1058 */           shifts++;
/*      */         }
/*      */         
/* 1061 */         for (int n = 0; n < bbSize; n++) {
/* 1062 */           int a2update = fmap[bbStart + n];
/* 1063 */           char qVal = (char)(n >> shifts);
/* 1064 */           quadrant[a2update] = qVal;
/* 1065 */           if (a2update < 20)
/* 1066 */             quadrant[a2update + lastShadow + 1] = qVal; 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\compressors\bzip2\BlockSort.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */