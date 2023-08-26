/*     */ package org.apache.commons.compress.harmony.unpack200;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Arrays;
/*     */ import org.apache.commons.compress.harmony.pack200.BHSDCodec;
/*     */ import org.apache.commons.compress.harmony.pack200.Codec;
/*     */ import org.apache.commons.compress.harmony.pack200.CodecEncoding;
/*     */ import org.apache.commons.compress.harmony.pack200.Pack200Exception;
/*     */ import org.apache.commons.compress.harmony.pack200.PopulationCodec;
/*     */ import org.apache.commons.compress.harmony.unpack200.bytecode.CPClass;
/*     */ import org.apache.commons.compress.harmony.unpack200.bytecode.CPDouble;
/*     */ import org.apache.commons.compress.harmony.unpack200.bytecode.CPFieldRef;
/*     */ import org.apache.commons.compress.harmony.unpack200.bytecode.CPFloat;
/*     */ import org.apache.commons.compress.harmony.unpack200.bytecode.CPInteger;
/*     */ import org.apache.commons.compress.harmony.unpack200.bytecode.CPInterfaceMethodRef;
/*     */ import org.apache.commons.compress.harmony.unpack200.bytecode.CPLong;
/*     */ import org.apache.commons.compress.harmony.unpack200.bytecode.CPMethodRef;
/*     */ import org.apache.commons.compress.harmony.unpack200.bytecode.CPNameAndType;
/*     */ import org.apache.commons.compress.harmony.unpack200.bytecode.CPString;
/*     */ import org.apache.commons.compress.harmony.unpack200.bytecode.CPUTF8;
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
/*     */ public abstract class BandSet
/*     */ {
/*     */   protected Segment segment;
/*     */   protected SegmentHeader header;
/*     */   
/*     */   public void unpack(InputStream in) throws IOException, Pack200Exception {
/*  51 */     read(in);
/*  52 */     unpack();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BandSet(Segment segment) {
/*  60 */     this.segment = segment;
/*  61 */     this.header = segment.getSegmentHeader();
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
/*     */   public int[] decodeBandInt(String name, InputStream in, BHSDCodec codec, int count) throws IOException, Pack200Exception {
/*     */     int[] band;
/*     */     Codec codec1;
/*  82 */     BHSDCodec bHSDCodec = codec;
/*  83 */     if (codec.getB() == 1 || count == 0) {
/*  84 */       return codec.decodeInts(count, in);
/*     */     }
/*  86 */     int[] getFirst = codec.decodeInts(1, in);
/*  87 */     if (getFirst.length == 0) {
/*  88 */       return getFirst;
/*     */     }
/*  90 */     int first = getFirst[0];
/*  91 */     if (codec.isSigned() && first >= -256 && first <= -1) {
/*     */       
/*  93 */       codec1 = CodecEncoding.getCodec(-1 - first, this.header.getBandHeadersInputStream(), (Codec)codec);
/*  94 */       band = codec1.decodeInts(count, in);
/*  95 */     } else if (!codec.isSigned() && first >= codec.getL() && first <= codec.getL() + 255) {
/*     */       
/*  97 */       codec1 = CodecEncoding.getCodec(first - codec.getL(), this.header.getBandHeadersInputStream(), (Codec)codec);
/*  98 */       band = codec1.decodeInts(count, in);
/*     */     } else {
/*     */       
/* 101 */       band = codec.decodeInts(count - 1, in, first);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 108 */     if (codec1 instanceof PopulationCodec) {
/* 109 */       PopulationCodec popCodec = (PopulationCodec)codec1;
/* 110 */       int[] favoured = (int[])popCodec.getFavoured().clone();
/* 111 */       Arrays.sort(favoured);
/* 112 */       for (int i = 0; i < band.length; i++) {
/* 113 */         boolean favouredValue = (Arrays.binarySearch(favoured, band[i]) > -1);
/* 114 */         Codec theCodec = favouredValue ? popCodec.getFavouredCodec() : popCodec.getUnfavouredCodec();
/* 115 */         if (theCodec instanceof BHSDCodec && ((BHSDCodec)theCodec).isDelta()) {
/* 116 */           BHSDCodec bhsd = (BHSDCodec)theCodec;
/* 117 */           long cardinality = bhsd.cardinality();
/* 118 */           while (band[i] > bhsd.largest()) {
/* 119 */             band[i] = (int)(band[i] - cardinality);
/*     */           }
/* 121 */           while (band[i] < bhsd.smallest()) {
/* 122 */             band[i] = ExactMath.add(band[i], cardinality);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 127 */     return band;
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
/*     */   public int[][] decodeBandInt(String name, InputStream in, BHSDCodec defaultCodec, int[] counts) throws IOException, Pack200Exception {
/* 143 */     int[][] result = new int[counts.length][];
/* 144 */     int totalCount = 0;
/* 145 */     for (int count : counts) {
/* 146 */       totalCount += count;
/*     */     }
/* 148 */     int[] twoDResult = decodeBandInt(name, in, defaultCodec, totalCount);
/* 149 */     int index = 0;
/* 150 */     for (int i = 0; i < result.length; i++) {
/* 151 */       result[i] = new int[counts[i]];
/* 152 */       for (int j = 0; j < (result[i]).length; j++) {
/* 153 */         result[i][j] = twoDResult[index];
/* 154 */         index++;
/*     */       } 
/*     */     } 
/* 157 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public long[] parseFlags(String name, InputStream in, int count, BHSDCodec codec, boolean hasHi) throws IOException, Pack200Exception {
/* 162 */     return parseFlags(name, in, new int[] { count }, hasHi ? codec : null, codec)[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public long[][] parseFlags(String name, InputStream in, int[] counts, BHSDCodec codec, boolean hasHi) throws IOException, Pack200Exception {
/* 167 */     return parseFlags(name, in, counts, hasHi ? codec : null, codec);
/*     */   }
/*     */ 
/*     */   
/*     */   public long[] parseFlags(String name, InputStream in, int count, BHSDCodec hiCodec, BHSDCodec loCodec) throws IOException, Pack200Exception {
/* 172 */     return parseFlags(name, in, new int[] { count }, hiCodec, loCodec)[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public long[][] parseFlags(String name, InputStream in, int[] counts, BHSDCodec hiCodec, BHSDCodec loCodec) throws IOException, Pack200Exception {
/* 177 */     int lo[], count = counts.length;
/* 178 */     if (count == 0) {
/* 179 */       return new long[][] { {} };
/*     */     }
/* 181 */     int sum = 0;
/* 182 */     long[][] result = new long[count][];
/* 183 */     for (int i = 0; i < count; i++) {
/* 184 */       result[i] = new long[counts[i]];
/* 185 */       sum += counts[i];
/*     */     } 
/* 187 */     int[] hi = null;
/*     */     
/* 189 */     if (hiCodec != null) {
/* 190 */       hi = decodeBandInt(name, in, hiCodec, sum);
/* 191 */       lo = decodeBandInt(name, in, loCodec, sum);
/*     */     } else {
/* 193 */       lo = decodeBandInt(name, in, loCodec, sum);
/*     */     } 
/*     */     
/* 196 */     int index = 0;
/* 197 */     for (int j = 0; j < result.length; j++) {
/* 198 */       for (int k = 0; k < (result[j]).length; k++) {
/* 199 */         if (hi != null) {
/* 200 */           result[j][k] = hi[index] << 32L | lo[index] & 0xFFFFFFFFL;
/*     */         } else {
/* 202 */           result[j][k] = lo[index];
/*     */         } 
/* 204 */         index++;
/*     */       } 
/*     */     } 
/* 207 */     return result;
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
/*     */   public String[] parseReferences(String name, InputStream in, BHSDCodec codec, int count, String[] reference) throws IOException, Pack200Exception {
/* 227 */     return parseReferences(name, in, codec, new int[] { count }, reference)[0];
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
/*     */   public String[][] parseReferences(String name, InputStream in, BHSDCodec codec, int[] counts, String[] reference) throws IOException, Pack200Exception {
/* 248 */     int count = counts.length;
/* 249 */     if (count == 0) {
/* 250 */       return new String[][] { {} };
/*     */     }
/* 252 */     String[][] result = new String[count][];
/* 253 */     int sum = 0;
/* 254 */     for (int i = 0; i < count; i++) {
/* 255 */       result[i] = new String[counts[i]];
/* 256 */       sum += counts[i];
/*     */     } 
/*     */     
/* 259 */     String[] result1 = new String[sum];
/* 260 */     int[] indices = decodeBandInt(name, in, codec, sum);
/* 261 */     for (int i1 = 0; i1 < sum; i1++) {
/* 262 */       int index = indices[i1];
/* 263 */       if (index < 0 || index >= reference.length) {
/* 264 */         throw new Pack200Exception("Something has gone wrong during parsing references, index = " + index + ", array size = " + reference.length);
/*     */       }
/*     */       
/* 267 */       result1[i1] = reference[index];
/*     */     } 
/*     */     
/* 270 */     int pos = 0;
/* 271 */     for (int j = 0; j < count; j++) {
/* 272 */       int num = counts[j];
/* 273 */       result[j] = new String[num];
/* 274 */       System.arraycopy(result1, pos, result[j], 0, num);
/* 275 */       pos += num;
/*     */     } 
/* 277 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public CPInteger[] parseCPIntReferences(String name, InputStream in, BHSDCodec codec, int count) throws IOException, Pack200Exception {
/* 282 */     int[] reference = this.segment.getCpBands().getCpInt();
/* 283 */     int[] indices = decodeBandInt(name, in, codec, count);
/* 284 */     CPInteger[] result = new CPInteger[indices.length];
/* 285 */     for (int i1 = 0; i1 < count; i1++) {
/* 286 */       int index = indices[i1];
/* 287 */       if (index < 0 || index >= reference.length) {
/* 288 */         throw new Pack200Exception("Something has gone wrong during parsing references, index = " + index + ", array size = " + reference.length);
/*     */       }
/*     */       
/* 291 */       result[i1] = this.segment.getCpBands().cpIntegerValue(index);
/*     */     } 
/* 293 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public CPDouble[] parseCPDoubleReferences(String name, InputStream in, BHSDCodec codec, int count) throws IOException, Pack200Exception {
/* 298 */     int[] indices = decodeBandInt(name, in, codec, count);
/* 299 */     CPDouble[] result = new CPDouble[indices.length];
/* 300 */     for (int i1 = 0; i1 < count; i1++) {
/* 301 */       result[i1] = this.segment.getCpBands().cpDoubleValue(indices[i1]);
/*     */     }
/* 303 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public CPFloat[] parseCPFloatReferences(String name, InputStream in, BHSDCodec codec, int count) throws IOException, Pack200Exception {
/* 308 */     int[] indices = decodeBandInt(name, in, codec, count);
/* 309 */     CPFloat[] result = new CPFloat[indices.length];
/* 310 */     for (int i1 = 0; i1 < count; i1++) {
/* 311 */       result[i1] = this.segment.getCpBands().cpFloatValue(indices[i1]);
/*     */     }
/* 313 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public CPLong[] parseCPLongReferences(String name, InputStream in, BHSDCodec codec, int count) throws IOException, Pack200Exception {
/* 318 */     long[] reference = this.segment.getCpBands().getCpLong();
/* 319 */     int[] indices = decodeBandInt(name, in, codec, count);
/* 320 */     CPLong[] result = new CPLong[indices.length];
/* 321 */     for (int i1 = 0; i1 < count; i1++) {
/* 322 */       int index = indices[i1];
/* 323 */       if (index < 0 || index >= reference.length) {
/* 324 */         throw new Pack200Exception("Something has gone wrong during parsing references, index = " + index + ", array size = " + reference.length);
/*     */       }
/*     */       
/* 327 */       result[i1] = this.segment.getCpBands().cpLongValue(index);
/*     */     } 
/* 329 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public CPUTF8[] parseCPUTF8References(String name, InputStream in, BHSDCodec codec, int count) throws IOException, Pack200Exception {
/* 334 */     int[] indices = decodeBandInt(name, in, codec, count);
/* 335 */     CPUTF8[] result = new CPUTF8[indices.length];
/* 336 */     for (int i1 = 0; i1 < count; i1++) {
/* 337 */       int index = indices[i1];
/* 338 */       result[i1] = this.segment.getCpBands().cpUTF8Value(index);
/*     */     } 
/* 340 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public CPUTF8[][] parseCPUTF8References(String name, InputStream in, BHSDCodec codec, int[] counts) throws IOException, Pack200Exception {
/* 345 */     CPUTF8[][] result = new CPUTF8[counts.length][];
/* 346 */     int sum = 0;
/* 347 */     for (int i = 0; i < counts.length; i++) {
/* 348 */       result[i] = new CPUTF8[counts[i]];
/* 349 */       sum += counts[i];
/*     */     } 
/* 351 */     CPUTF8[] result1 = new CPUTF8[sum];
/* 352 */     int[] indices = decodeBandInt(name, in, codec, sum);
/* 353 */     for (int i1 = 0; i1 < sum; i1++) {
/* 354 */       int index = indices[i1];
/* 355 */       result1[i1] = this.segment.getCpBands().cpUTF8Value(index);
/*     */     } 
/* 357 */     int pos = 0;
/* 358 */     for (int j = 0; j < counts.length; j++) {
/* 359 */       int num = counts[j];
/* 360 */       result[j] = new CPUTF8[num];
/* 361 */       System.arraycopy(result1, pos, result[j], 0, num);
/* 362 */       pos += num;
/*     */     } 
/* 364 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public CPString[] parseCPStringReferences(String name, InputStream in, BHSDCodec codec, int count) throws IOException, Pack200Exception {
/* 369 */     int[] indices = decodeBandInt(name, in, codec, count);
/* 370 */     CPString[] result = new CPString[indices.length];
/* 371 */     for (int i1 = 0; i1 < count; i1++) {
/* 372 */       result[i1] = this.segment.getCpBands().cpStringValue(indices[i1]);
/*     */     }
/* 374 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public CPInterfaceMethodRef[] parseCPInterfaceMethodRefReferences(String name, InputStream in, BHSDCodec codec, int count) throws IOException, Pack200Exception {
/* 379 */     CpBands cpBands = this.segment.getCpBands();
/* 380 */     int[] indices = decodeBandInt(name, in, codec, count);
/* 381 */     CPInterfaceMethodRef[] result = new CPInterfaceMethodRef[indices.length];
/* 382 */     for (int i1 = 0; i1 < count; i1++) {
/* 383 */       result[i1] = cpBands.cpIMethodValue(indices[i1]);
/*     */     }
/* 385 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public CPMethodRef[] parseCPMethodRefReferences(String name, InputStream in, BHSDCodec codec, int count) throws IOException, Pack200Exception {
/* 390 */     CpBands cpBands = this.segment.getCpBands();
/* 391 */     int[] indices = decodeBandInt(name, in, codec, count);
/* 392 */     CPMethodRef[] result = new CPMethodRef[indices.length];
/* 393 */     for (int i1 = 0; i1 < count; i1++) {
/* 394 */       result[i1] = cpBands.cpMethodValue(indices[i1]);
/*     */     }
/* 396 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public CPFieldRef[] parseCPFieldRefReferences(String name, InputStream in, BHSDCodec codec, int count) throws IOException, Pack200Exception {
/* 401 */     CpBands cpBands = this.segment.getCpBands();
/* 402 */     int[] indices = decodeBandInt(name, in, codec, count);
/* 403 */     CPFieldRef[] result = new CPFieldRef[indices.length];
/* 404 */     for (int i1 = 0; i1 < count; i1++) {
/* 405 */       int index = indices[i1];
/* 406 */       result[i1] = cpBands.cpFieldValue(index);
/*     */     } 
/* 408 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public CPNameAndType[] parseCPDescriptorReferences(String name, InputStream in, BHSDCodec codec, int count) throws IOException, Pack200Exception {
/* 413 */     CpBands cpBands = this.segment.getCpBands();
/* 414 */     int[] indices = decodeBandInt(name, in, codec, count);
/* 415 */     CPNameAndType[] result = new CPNameAndType[indices.length];
/* 416 */     for (int i1 = 0; i1 < count; i1++) {
/* 417 */       int index = indices[i1];
/* 418 */       result[i1] = cpBands.cpNameAndTypeValue(index);
/*     */     } 
/* 420 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public CPUTF8[] parseCPSignatureReferences(String name, InputStream in, BHSDCodec codec, int count) throws IOException, Pack200Exception {
/* 425 */     int[] indices = decodeBandInt(name, in, codec, count);
/* 426 */     CPUTF8[] result = new CPUTF8[indices.length];
/* 427 */     for (int i1 = 0; i1 < count; i1++) {
/* 428 */       result[i1] = this.segment.getCpBands().cpSignatureValue(indices[i1]);
/*     */     }
/* 430 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   protected CPUTF8[][] parseCPSignatureReferences(String name, InputStream in, BHSDCodec codec, int[] counts) throws IOException, Pack200Exception {
/* 435 */     CPUTF8[][] result = new CPUTF8[counts.length][];
/* 436 */     int sum = 0;
/* 437 */     for (int i = 0; i < counts.length; i++) {
/* 438 */       result[i] = new CPUTF8[counts[i]];
/* 439 */       sum += counts[i];
/*     */     } 
/* 441 */     CPUTF8[] result1 = new CPUTF8[sum];
/* 442 */     int[] indices = decodeBandInt(name, in, codec, sum);
/* 443 */     for (int i1 = 0; i1 < sum; i1++) {
/* 444 */       result1[i1] = this.segment.getCpBands().cpSignatureValue(indices[i1]);
/*     */     }
/* 446 */     int pos = 0;
/* 447 */     for (int j = 0; j < counts.length; j++) {
/* 448 */       int num = counts[j];
/* 449 */       result[j] = new CPUTF8[num];
/* 450 */       System.arraycopy(result1, pos, result[j], 0, num);
/* 451 */       pos += num;
/*     */     } 
/* 453 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public CPClass[] parseCPClassReferences(String name, InputStream in, BHSDCodec codec, int count) throws IOException, Pack200Exception {
/* 458 */     int[] indices = decodeBandInt(name, in, codec, count);
/* 459 */     CPClass[] result = new CPClass[indices.length];
/* 460 */     for (int i1 = 0; i1 < count; i1++) {
/* 461 */       result[i1] = this.segment.getCpBands().cpClassValue(indices[i1]);
/*     */     }
/* 463 */     return result;
/*     */   }
/*     */   
/*     */   protected String[] getReferences(int[] ints, String[] reference) {
/* 467 */     String[] result = new String[ints.length];
/* 468 */     Arrays.setAll(result, i -> reference[ints[i]]);
/* 469 */     return result;
/*     */   }
/*     */   
/*     */   protected String[][] getReferences(int[][] ints, String[] reference) {
/* 473 */     String[][] result = new String[ints.length][];
/* 474 */     for (int i = 0; i < result.length; i++) {
/* 475 */       result[i] = new String[(ints[i]).length];
/* 476 */       for (int j = 0; j < (result[i]).length; j++) {
/* 477 */         result[i][j] = reference[ints[i][j]];
/*     */       }
/*     */     } 
/* 480 */     return result;
/*     */   }
/*     */   
/*     */   public abstract void read(InputStream paramInputStream) throws IOException, Pack200Exception;
/*     */   
/*     */   public abstract void unpack() throws IOException, Pack200Exception;
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\harmon\\unpack200\BandSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */