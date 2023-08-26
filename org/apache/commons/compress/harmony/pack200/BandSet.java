/*     */ package org.apache.commons.compress.harmony.pack200;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.stream.IntStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class BandSet
/*     */ {
/*     */   protected final SegmentHeader segmentHeader;
/*     */   final int effort;
/*  40 */   private static final int[] effortThresholds = new int[] { 0, 0, 1000, 500, 100, 100, 100, 100, 100, 0 };
/*     */ 
/*     */ 
/*     */   
/*     */   private long[] canonicalLargest;
/*     */ 
/*     */   
/*     */   private long[] canonicalSmallest;
/*     */ 
/*     */ 
/*     */   
/*     */   public BandSet(int effort, SegmentHeader header) {
/*  52 */     this.effort = effort;
/*  53 */     this.segmentHeader = header;
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
/*     */   public byte[] encodeScalar(int[] band, BHSDCodec codec) throws Pack200Exception {
/*  74 */     return codec.encode(band);
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
/*     */   public byte[] encodeScalar(int value, BHSDCodec codec) throws Pack200Exception {
/*  86 */     return codec.encode(value);
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
/*     */   public byte[] encodeBandInt(String name, int[] ints, BHSDCodec defaultCodec) throws Pack200Exception {
/* 101 */     byte[] encodedBand = null;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 106 */     if (this.effort > 1 && ints.length >= effortThresholds[this.effort]) {
/* 107 */       BandAnalysisResults results = analyseBand(name, ints, defaultCodec);
/* 108 */       Codec betterCodec = results.betterCodec;
/* 109 */       encodedBand = results.encodedBand;
/* 110 */       if (betterCodec != null) {
/* 111 */         if (betterCodec instanceof BHSDCodec) {
/* 112 */           int[] specifierBand = CodecEncoding.getSpecifier(betterCodec, defaultCodec);
/* 113 */           int specifier = specifierBand[0];
/* 114 */           if (specifierBand.length > 1) {
/* 115 */             for (int i = 1; i < specifierBand.length; i++) {
/* 116 */               this.segmentHeader.appendBandCodingSpecifier(specifierBand[i]);
/*     */             }
/*     */           }
/* 119 */           if (defaultCodec.isSigned()) {
/* 120 */             specifier = -1 - specifier;
/*     */           } else {
/* 122 */             specifier += defaultCodec.getL();
/*     */           } 
/* 124 */           byte[] specifierEncoded = defaultCodec.encode(new int[] { specifier });
/* 125 */           byte[] band = new byte[specifierEncoded.length + encodedBand.length];
/* 126 */           System.arraycopy(specifierEncoded, 0, band, 0, specifierEncoded.length);
/* 127 */           System.arraycopy(encodedBand, 0, band, specifierEncoded.length, encodedBand.length);
/* 128 */           return band;
/*     */         } 
/* 130 */         if (betterCodec instanceof PopulationCodec) {
/* 131 */           IntStream.of(results.extraMetadata).forEach(this.segmentHeader::appendBandCodingSpecifier);
/* 132 */           return encodedBand;
/*     */         } 
/* 134 */         if (betterCodec instanceof RunCodec);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 141 */     if (ints.length > 0) {
/* 142 */       if (encodedBand == null) {
/* 143 */         encodedBand = defaultCodec.encode(ints);
/*     */       }
/* 145 */       int first = ints[0];
/* 146 */       if (defaultCodec.getB() != 1) {
/* 147 */         if (defaultCodec.isSigned() && first >= -256 && first <= -1) {
/* 148 */           int specifier = -1 - CodecEncoding.getSpecifierForDefaultCodec(defaultCodec);
/* 149 */           byte[] specifierEncoded = defaultCodec.encode(new int[] { specifier });
/* 150 */           byte[] band = new byte[specifierEncoded.length + encodedBand.length];
/* 151 */           System.arraycopy(specifierEncoded, 0, band, 0, specifierEncoded.length);
/* 152 */           System.arraycopy(encodedBand, 0, band, specifierEncoded.length, encodedBand.length);
/* 153 */           return band;
/*     */         } 
/* 155 */         if (!defaultCodec.isSigned() && first >= defaultCodec.getL() && first <= defaultCodec.getL() + 255) {
/* 156 */           int specifier = CodecEncoding.getSpecifierForDefaultCodec(defaultCodec) + defaultCodec.getL();
/* 157 */           byte[] specifierEncoded = defaultCodec.encode(new int[] { specifier });
/* 158 */           byte[] band = new byte[specifierEncoded.length + encodedBand.length];
/* 159 */           System.arraycopy(specifierEncoded, 0, band, 0, specifierEncoded.length);
/* 160 */           System.arraycopy(encodedBand, 0, band, specifierEncoded.length, encodedBand.length);
/* 161 */           return band;
/*     */         } 
/*     */       } 
/* 164 */       return encodedBand;
/*     */     } 
/* 166 */     return new byte[0];
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private BandAnalysisResults analyseBand(String name, int[] band, BHSDCodec defaultCodec) throws Pack200Exception {
/* 172 */     BandAnalysisResults results = new BandAnalysisResults();
/*     */     
/* 174 */     if (this.canonicalLargest == null) {
/* 175 */       this.canonicalLargest = new long[116];
/* 176 */       this.canonicalSmallest = new long[116];
/* 177 */       for (int i = 1; i < this.canonicalLargest.length; i++) {
/* 178 */         this.canonicalLargest[i] = CodecEncoding.getCanonicalCodec(i).largest();
/* 179 */         this.canonicalSmallest[i] = CodecEncoding.getCanonicalCodec(i).smallest();
/*     */       } 
/*     */     } 
/* 182 */     BandData bandData = new BandData(band);
/*     */ 
/*     */     
/* 185 */     byte[] encoded = defaultCodec.encode(band);
/* 186 */     results.encodedBand = encoded;
/*     */ 
/*     */     
/* 189 */     if (encoded.length <= band.length + 23 - 2 * this.effort) {
/* 190 */       return results;
/*     */     }
/*     */ 
/*     */     
/* 194 */     if (!bandData.anyNegatives() && bandData.largest <= Codec.BYTE1.largest()) {
/* 195 */       results.encodedBand = Codec.BYTE1.encode(band);
/* 196 */       results.betterCodec = Codec.BYTE1;
/* 197 */       return results;
/*     */     } 
/*     */ 
/*     */     
/* 201 */     if (this.effort > 3 && !name.equals("POPULATION")) {
/* 202 */       int numDistinctValues = bandData.numDistinctValues();
/* 203 */       float distinctValuesAsProportion = numDistinctValues / band.length;
/*     */ 
/*     */       
/* 206 */       if (numDistinctValues < 100 || distinctValuesAsProportion < 0.02D || (this.effort > 6 && distinctValuesAsProportion < 0.04D)) {
/*     */         
/* 208 */         encodeWithPopulationCodec(name, band, defaultCodec, bandData, results);
/* 209 */         if (timeToStop(results)) {
/* 210 */           return results;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 215 */     List<BHSDCodec[]> codecFamiliesToTry = (List)new ArrayList<>();
/*     */ 
/*     */     
/* 218 */     if (bandData.mainlyPositiveDeltas() && bandData.mainlySmallDeltas()) {
/* 219 */       codecFamiliesToTry.add(CanonicalCodecFamilies.deltaUnsignedCodecs2);
/*     */     }
/*     */     
/* 222 */     if (bandData.wellCorrelated()) {
/* 223 */       if (bandData.mainlyPositiveDeltas()) {
/* 224 */         codecFamiliesToTry.add(CanonicalCodecFamilies.deltaUnsignedCodecs1);
/* 225 */         codecFamiliesToTry.add(CanonicalCodecFamilies.deltaUnsignedCodecs3);
/* 226 */         codecFamiliesToTry.add(CanonicalCodecFamilies.deltaUnsignedCodecs4);
/* 227 */         codecFamiliesToTry.add(CanonicalCodecFamilies.deltaUnsignedCodecs5);
/* 228 */         codecFamiliesToTry.add(CanonicalCodecFamilies.nonDeltaUnsignedCodecs1);
/* 229 */         codecFamiliesToTry.add(CanonicalCodecFamilies.nonDeltaUnsignedCodecs3);
/* 230 */         codecFamiliesToTry.add(CanonicalCodecFamilies.nonDeltaUnsignedCodecs4);
/* 231 */         codecFamiliesToTry.add(CanonicalCodecFamilies.nonDeltaUnsignedCodecs5);
/* 232 */         codecFamiliesToTry.add(CanonicalCodecFamilies.nonDeltaUnsignedCodecs2);
/*     */       } else {
/* 234 */         codecFamiliesToTry.add(CanonicalCodecFamilies.deltaSignedCodecs1);
/* 235 */         codecFamiliesToTry.add(CanonicalCodecFamilies.deltaSignedCodecs3);
/* 236 */         codecFamiliesToTry.add(CanonicalCodecFamilies.deltaSignedCodecs2);
/* 237 */         codecFamiliesToTry.add(CanonicalCodecFamilies.deltaSignedCodecs4);
/* 238 */         codecFamiliesToTry.add(CanonicalCodecFamilies.deltaSignedCodecs5);
/* 239 */         codecFamiliesToTry.add(CanonicalCodecFamilies.nonDeltaSignedCodecs1);
/* 240 */         codecFamiliesToTry.add(CanonicalCodecFamilies.nonDeltaSignedCodecs2);
/*     */       } 
/* 242 */     } else if (bandData.anyNegatives()) {
/* 243 */       codecFamiliesToTry.add(CanonicalCodecFamilies.nonDeltaSignedCodecs1);
/* 244 */       codecFamiliesToTry.add(CanonicalCodecFamilies.nonDeltaSignedCodecs2);
/* 245 */       codecFamiliesToTry.add(CanonicalCodecFamilies.deltaSignedCodecs1);
/* 246 */       codecFamiliesToTry.add(CanonicalCodecFamilies.deltaSignedCodecs2);
/* 247 */       codecFamiliesToTry.add(CanonicalCodecFamilies.deltaSignedCodecs3);
/* 248 */       codecFamiliesToTry.add(CanonicalCodecFamilies.deltaSignedCodecs4);
/* 249 */       codecFamiliesToTry.add(CanonicalCodecFamilies.deltaSignedCodecs5);
/*     */     } else {
/* 251 */       codecFamiliesToTry.add(CanonicalCodecFamilies.nonDeltaUnsignedCodecs1);
/* 252 */       codecFamiliesToTry.add(CanonicalCodecFamilies.nonDeltaUnsignedCodecs3);
/* 253 */       codecFamiliesToTry.add(CanonicalCodecFamilies.nonDeltaUnsignedCodecs4);
/* 254 */       codecFamiliesToTry.add(CanonicalCodecFamilies.nonDeltaUnsignedCodecs5);
/* 255 */       codecFamiliesToTry.add(CanonicalCodecFamilies.nonDeltaUnsignedCodecs2);
/* 256 */       codecFamiliesToTry.add(CanonicalCodecFamilies.deltaUnsignedCodecs1);
/* 257 */       codecFamiliesToTry.add(CanonicalCodecFamilies.deltaUnsignedCodecs3);
/* 258 */       codecFamiliesToTry.add(CanonicalCodecFamilies.deltaUnsignedCodecs4);
/* 259 */       codecFamiliesToTry.add(CanonicalCodecFamilies.deltaUnsignedCodecs5);
/*     */     } 
/* 261 */     if (name.equalsIgnoreCase("cpint")) {
/* 262 */       System.out.print("");
/*     */     }
/*     */     
/* 265 */     for (BHSDCodec[] family : codecFamiliesToTry) {
/* 266 */       tryCodecs(name, band, defaultCodec, bandData, results, encoded, family);
/* 267 */       if (timeToStop(results)) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */     
/* 272 */     return results;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean timeToStop(BandAnalysisResults results) {
/* 278 */     if (this.effort > 6) {
/* 279 */       return (results.numCodecsTried >= this.effort * 2);
/*     */     }
/* 281 */     return (results.numCodecsTried >= this.effort);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void tryCodecs(String name, int[] band, BHSDCodec defaultCodec, BandData bandData, BandAnalysisResults results, byte[] encoded, BHSDCodec[] potentialCodecs) throws Pack200Exception {
/* 289 */     for (BHSDCodec potential : potentialCodecs) {
/* 290 */       if (potential.equals(defaultCodec)) {
/*     */         return;
/*     */       }
/*     */       
/* 294 */       if (potential.isDelta()) {
/* 295 */         if (potential.largest() >= bandData.largestDelta && potential.smallest() <= bandData.smallestDelta && potential
/* 296 */           .largest() >= bandData.largest && potential.smallest() <= bandData.smallest) {
/*     */           
/* 298 */           byte[] encoded2 = potential.encode(band);
/* 299 */           results.numCodecsTried++;
/* 300 */           byte[] specifierEncoded = defaultCodec.encode(CodecEncoding.getSpecifier(potential, null));
/* 301 */           int saved = encoded.length - encoded2.length - specifierEncoded.length;
/* 302 */           if (saved > results.saved) {
/* 303 */             results.betterCodec = potential;
/* 304 */             results.encodedBand = encoded2;
/* 305 */             results.saved = saved;
/*     */           } 
/*     */         } 
/* 308 */       } else if (potential.largest() >= bandData.largest && potential.smallest() <= bandData.smallest) {
/* 309 */         byte[] encoded2 = potential.encode(band);
/* 310 */         results.numCodecsTried++;
/* 311 */         byte[] specifierEncoded = defaultCodec.encode(CodecEncoding.getSpecifier(potential, null));
/* 312 */         int saved = encoded.length - encoded2.length - specifierEncoded.length;
/* 313 */         if (saved > results.saved) {
/* 314 */           results.betterCodec = potential;
/* 315 */           results.encodedBand = encoded2;
/* 316 */           results.saved = saved;
/*     */         } 
/*     */       } 
/* 319 */       if (timeToStop(results)) {
/*     */         return;
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
/*     */   private void encodeWithPopulationCodec(String name, int[] band, BHSDCodec defaultCodec, BandData bandData, BandAnalysisResults results) throws Pack200Exception {
/*     */     byte[] tokensEncoded;
/* 353 */     results.numCodecsTried = results.numCodecsTried + 3;
/* 354 */     Map<Integer, Integer> distinctValues = bandData.distinctValues;
/*     */     
/* 356 */     List<Integer> favored = new ArrayList<>();
/* 357 */     distinctValues.forEach((k, v) -> {
/*     */           if (v.intValue() > 2 || distinctValues.size() < 256) {
/*     */             favored.add(k);
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 364 */     if (distinctValues.size() > 255) {
/* 365 */       favored.sort((arg0, arg1) -> ((Integer)distinctValues.get(arg1)).compareTo((Integer)distinctValues.get(arg0)));
/*     */     }
/*     */     
/* 368 */     Map<Integer, Integer> favoredToIndex = new HashMap<>();
/* 369 */     for (int i = 0; i < favored.size(); i++) {
/* 370 */       favoredToIndex.put(favored.get(i), Integer.valueOf(i));
/*     */     }
/*     */     
/* 373 */     IntList unfavoured = new IntList();
/* 374 */     int[] tokens = new int[band.length];
/* 375 */     for (int j = 0; j < band.length; j++) {
/* 376 */       Integer favouredIndex = favoredToIndex.get(Integer.valueOf(band[j]));
/* 377 */       if (favouredIndex == null) {
/* 378 */         tokens[j] = 0;
/* 379 */         unfavoured.add(band[j]);
/*     */       } else {
/* 381 */         tokens[j] = favouredIndex.intValue() + 1;
/*     */       } 
/*     */     } 
/* 384 */     favored.add(favored.get(favored.size() - 1));
/* 385 */     int[] favouredBand = integerListToArray(favored);
/* 386 */     int[] unfavouredBand = unfavoured.toArray();
/*     */ 
/*     */     
/* 389 */     BandAnalysisResults favouredResults = analyseBand("POPULATION", favouredBand, defaultCodec);
/* 390 */     BandAnalysisResults unfavouredResults = analyseBand("POPULATION", unfavouredBand, defaultCodec);
/*     */     
/* 392 */     int tdefL = 0;
/* 393 */     int l = 0;
/* 394 */     Codec tokenCodec = null;
/*     */     
/* 396 */     int k = favored.size() - 1;
/* 397 */     if (k < 256) {
/* 398 */       tdefL = 1;
/* 399 */       tokensEncoded = Codec.BYTE1.encode(tokens);
/*     */     } else {
/* 401 */       BandAnalysisResults tokenResults = analyseBand("POPULATION", tokens, defaultCodec);
/* 402 */       tokenCodec = tokenResults.betterCodec;
/* 403 */       tokensEncoded = tokenResults.encodedBand;
/* 404 */       if (tokenCodec == null) {
/* 405 */         tokenCodec = defaultCodec;
/*     */       }
/* 407 */       l = ((BHSDCodec)tokenCodec).getL();
/* 408 */       int h = ((BHSDCodec)tokenCodec).getH();
/* 409 */       int s = ((BHSDCodec)tokenCodec).getS();
/* 410 */       int b = ((BHSDCodec)tokenCodec).getB();
/* 411 */       int d = ((BHSDCodec)tokenCodec).isDelta() ? 1 : 0;
/* 412 */       if (s == 0 && d == 0) {
/* 413 */         boolean canUseTDefL = true;
/* 414 */         if (b > 1) {
/* 415 */           BHSDCodec oneLowerB = new BHSDCodec(b - 1, h);
/* 416 */           if (oneLowerB.largest() >= k) {
/* 417 */             canUseTDefL = false;
/*     */           }
/*     */         } 
/* 420 */         if (canUseTDefL) {
/* 421 */           switch (l) {
/*     */             case 4:
/* 423 */               tdefL = 1;
/*     */               break;
/*     */             case 8:
/* 426 */               tdefL = 2;
/*     */               break;
/*     */             case 16:
/* 429 */               tdefL = 3;
/*     */               break;
/*     */             case 32:
/* 432 */               tdefL = 4;
/*     */               break;
/*     */             case 64:
/* 435 */               tdefL = 5;
/*     */               break;
/*     */             case 128:
/* 438 */               tdefL = 6;
/*     */               break;
/*     */             case 192:
/* 441 */               tdefL = 7;
/*     */               break;
/*     */             case 224:
/* 444 */               tdefL = 8;
/*     */               break;
/*     */             case 240:
/* 447 */               tdefL = 9;
/*     */               break;
/*     */             case 248:
/* 450 */               tdefL = 10;
/*     */               break;
/*     */             case 252:
/* 453 */               tdefL = 11;
/*     */               break;
/*     */           } 
/*     */         
/*     */         }
/*     */       } 
/*     */     } 
/* 460 */     byte[] favouredEncoded = favouredResults.encodedBand;
/* 461 */     byte[] unfavouredEncoded = unfavouredResults.encodedBand;
/* 462 */     Codec favouredCodec = favouredResults.betterCodec;
/* 463 */     Codec unfavouredCodec = unfavouredResults.betterCodec;
/*     */     
/* 465 */     int specifier = 141 + ((favouredCodec == null) ? 1 : 0) + 4 * tdefL + ((unfavouredCodec == null) ? 2 : 0);
/* 466 */     IntList extraBandMetadata = new IntList(3);
/* 467 */     if (favouredCodec != null) {
/* 468 */       IntStream.of(CodecEncoding.getSpecifier(favouredCodec, null)).forEach(extraBandMetadata::add);
/*     */     }
/* 470 */     if (tdefL == 0) {
/* 471 */       IntStream.of(CodecEncoding.getSpecifier(tokenCodec, null)).forEach(extraBandMetadata::add);
/*     */     }
/* 473 */     if (unfavouredCodec != null) {
/* 474 */       IntStream.of(CodecEncoding.getSpecifier(unfavouredCodec, null)).forEach(extraBandMetadata::add);
/*     */     }
/* 476 */     int[] extraMetadata = extraBandMetadata.toArray();
/* 477 */     byte[] extraMetadataEncoded = Codec.UNSIGNED5.encode(extraMetadata);
/* 478 */     if (defaultCodec.isSigned()) {
/* 479 */       specifier = -1 - specifier;
/*     */     } else {
/* 481 */       specifier += defaultCodec.getL();
/*     */     } 
/* 483 */     byte[] firstValueEncoded = defaultCodec.encode(new int[] { specifier });
/* 484 */     int totalBandLength = firstValueEncoded.length + favouredEncoded.length + tokensEncoded.length + unfavouredEncoded.length;
/*     */ 
/*     */     
/* 487 */     if (totalBandLength + extraMetadataEncoded.length < results.encodedBand.length) {
/* 488 */       results.saved = results.saved + results.encodedBand.length - totalBandLength + extraMetadataEncoded.length;
/* 489 */       byte[] encodedBand = new byte[totalBandLength];
/* 490 */       System.arraycopy(firstValueEncoded, 0, encodedBand, 0, firstValueEncoded.length);
/* 491 */       System.arraycopy(favouredEncoded, 0, encodedBand, firstValueEncoded.length, favouredEncoded.length);
/* 492 */       System.arraycopy(tokensEncoded, 0, encodedBand, firstValueEncoded.length + favouredEncoded.length, tokensEncoded.length);
/*     */       
/* 494 */       System.arraycopy(unfavouredEncoded, 0, encodedBand, firstValueEncoded.length + favouredEncoded.length + tokensEncoded.length, unfavouredEncoded.length);
/*     */       
/* 496 */       results.encodedBand = encodedBand;
/* 497 */       results.extraMetadata = extraMetadata;
/* 498 */       if (l != 0) {
/* 499 */         results.betterCodec = new PopulationCodec(favouredCodec, l, unfavouredCodec);
/*     */       } else {
/* 501 */         results.betterCodec = new PopulationCodec(favouredCodec, tokenCodec, unfavouredCodec);
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
/*     */   protected byte[] encodeFlags(String name, long[] flags, BHSDCodec loCodec, BHSDCodec hiCodec, boolean haveHiFlags) throws Pack200Exception {
/* 519 */     if (!haveHiFlags) {
/* 520 */       int[] arrayOfInt = new int[flags.length];
/* 521 */       Arrays.setAll(arrayOfInt, i -> (int)flags[i]);
/* 522 */       return encodeBandInt(name, arrayOfInt, loCodec);
/*     */     } 
/* 524 */     int[] hiBits = new int[flags.length];
/* 525 */     int[] loBits = new int[flags.length];
/* 526 */     for (int i = 0; i < flags.length; i++) {
/* 527 */       long l = flags[i];
/* 528 */       hiBits[i] = (int)(l >> 32L);
/* 529 */       loBits[i] = (int)l;
/*     */     } 
/* 531 */     byte[] hi = encodeBandInt(name, hiBits, hiCodec);
/* 532 */     byte[] lo = encodeBandInt(name, loBits, loCodec);
/* 533 */     byte[] total = new byte[hi.length + lo.length];
/* 534 */     System.arraycopy(hi, 0, total, 0, hi.length);
/* 535 */     System.arraycopy(lo, 0, total, hi.length + 1, lo.length);
/* 536 */     return total;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int[] integerListToArray(List<Integer> integerList) {
/* 546 */     return integerList.stream().mapToInt(Integer::intValue).toArray();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected long[] longListToArray(List<Long> longList) {
/* 556 */     return longList.stream().mapToLong(Long::longValue).toArray();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int[] cpEntryListToArray(List<? extends ConstantPoolEntry> list) {
/* 566 */     int[] array = new int[list.size()];
/* 567 */     for (int i = 0; i < array.length; i++) {
/* 568 */       array[i] = ((ConstantPoolEntry)list.get(i)).getIndex();
/* 569 */       if (array[i] < 0) {
/* 570 */         throw new IllegalArgumentException("Index should be > 0");
/*     */       }
/*     */     } 
/* 573 */     return array;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int[] cpEntryOrNullListToArray(List<? extends ConstantPoolEntry> list) {
/* 583 */     int[] array = new int[list.size()];
/* 584 */     for (int j = 0; j < array.length; j++) {
/* 585 */       ConstantPoolEntry cpEntry = list.get(j);
/* 586 */       array[j] = (cpEntry == null) ? 0 : (cpEntry.getIndex() + 1);
/* 587 */       if (cpEntry != null && cpEntry.getIndex() < 0) {
/* 588 */         throw new IllegalArgumentException("Index should be > 0");
/*     */       }
/*     */     } 
/* 591 */     return array;
/*     */   }
/*     */ 
/*     */   
/*     */   protected byte[] encodeFlags(String name, long[][] flags, BHSDCodec loCodec, BHSDCodec hiCodec, boolean haveHiFlags) throws Pack200Exception {
/* 596 */     return encodeFlags(name, flatten(flags), loCodec, hiCodec, haveHiFlags);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long[] flatten(long[][] flags) {
/* 603 */     int totalSize = 0;
/* 604 */     for (long[] flag : flags) {
/* 605 */       totalSize += flag.length;
/*     */     }
/* 607 */     long[] flatArray = new long[totalSize];
/* 608 */     int index = 0;
/* 609 */     for (long[] flag : flags) {
/* 610 */       for (long element : flag) {
/* 611 */         flatArray[index] = element;
/* 612 */         index++;
/*     */       } 
/*     */     } 
/* 615 */     return flatArray;
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract void pack(OutputStream paramOutputStream) throws IOException, Pack200Exception;
/*     */ 
/*     */   
/*     */   public class BandData
/*     */   {
/*     */     private final int[] band;
/* 625 */     private int smallest = Integer.MAX_VALUE;
/* 626 */     private int largest = Integer.MIN_VALUE;
/*     */     
/*     */     private int smallestDelta;
/*     */     private int largestDelta;
/* 630 */     private int deltaIsAscending = 0;
/* 631 */     private int smallDeltaCount = 0;
/*     */     
/* 633 */     private double averageAbsoluteDelta = 0.0D;
/* 634 */     private double averageAbsoluteValue = 0.0D;
/*     */ 
/*     */ 
/*     */     
/*     */     private Map<Integer, Integer> distinctValues;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public BandData(int[] band) {
/* 644 */       this.band = band;
/* 645 */       Integer one = Integer.valueOf(1);
/* 646 */       for (int i = 0; i < band.length; i++) {
/* 647 */         if (band[i] < this.smallest) {
/* 648 */           this.smallest = band[i];
/*     */         }
/* 650 */         if (band[i] > this.largest) {
/* 651 */           this.largest = band[i];
/*     */         }
/* 653 */         if (i != 0) {
/* 654 */           int delta = band[i] - band[i - 1];
/* 655 */           if (delta < this.smallestDelta) {
/* 656 */             this.smallestDelta = delta;
/*     */           }
/* 658 */           if (delta > this.largestDelta) {
/* 659 */             this.largestDelta = delta;
/*     */           }
/* 661 */           if (delta >= 0) {
/* 662 */             this.deltaIsAscending++;
/*     */           }
/* 664 */           this.averageAbsoluteDelta += Math.abs(delta) / (band.length - 1);
/* 665 */           if (Math.abs(delta) < 256) {
/* 666 */             this.smallDeltaCount++;
/*     */           }
/*     */         } else {
/* 669 */           this.smallestDelta = band[0];
/* 670 */           this.largestDelta = band[0];
/*     */         } 
/* 672 */         this.averageAbsoluteValue += Math.abs(band[i]) / band.length;
/* 673 */         if (BandSet.this.effort > 3) {
/* 674 */           if (this.distinctValues == null) {
/* 675 */             this.distinctValues = new HashMap<>();
/*     */           }
/* 677 */           Integer value = Integer.valueOf(band[i]);
/* 678 */           Integer count = this.distinctValues.get(value);
/* 679 */           if (count == null) {
/* 680 */             count = one;
/*     */           } else {
/* 682 */             count = Integer.valueOf(count.intValue() + 1);
/*     */           } 
/* 684 */           this.distinctValues.put(value, count);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean mainlySmallDeltas() {
/* 696 */       return (this.smallDeltaCount / this.band.length > 0.7F);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean wellCorrelated() {
/* 706 */       return (this.averageAbsoluteDelta * 3.1D < this.averageAbsoluteValue);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean mainlyPositiveDeltas() {
/* 716 */       return (this.deltaIsAscending / this.band.length > 0.95F);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean anyNegatives() {
/* 725 */       return (this.smallest < 0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int numDistinctValues() {
/* 734 */       if (this.distinctValues == null) {
/* 735 */         return this.band.length;
/*     */       }
/* 737 */       return this.distinctValues.size();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public class BandAnalysisResults
/*     */   {
/* 748 */     private int numCodecsTried = 0;
/*     */ 
/*     */     
/* 751 */     private int saved = 0;
/*     */     private int[] extraMetadata;
/*     */     private byte[] encodedBand;
/*     */     private Codec betterCodec;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\harmony\pack200\BandSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */