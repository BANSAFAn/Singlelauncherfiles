/*     */ package org.apache.commons.compress.harmony.unpack200;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.compress.harmony.pack200.Codec;
/*     */ import org.apache.commons.compress.harmony.pack200.Pack200Exception;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CpBands
/*     */   extends BandSet
/*     */ {
/*     */   public SegmentConstantPool getConstantPool() {
/*  46 */     return this.pool;
/*     */   }
/*     */   
/*  49 */   private final SegmentConstantPool pool = new SegmentConstantPool(this);
/*     */   
/*     */   private String[] cpClass;
/*     */   
/*     */   private int[] cpClassInts;
/*     */   private int[] cpDescriptorNameInts;
/*     */   private int[] cpDescriptorTypeInts;
/*     */   private String[] cpDescriptor;
/*     */   private double[] cpDouble;
/*     */   private String[] cpFieldClass;
/*     */   private String[] cpFieldDescriptor;
/*     */   private int[] cpFieldClassInts;
/*     */   private int[] cpFieldDescriptorInts;
/*     */   private float[] cpFloat;
/*     */   private String[] cpIMethodClass;
/*     */   private String[] cpIMethodDescriptor;
/*     */   private int[] cpIMethodClassInts;
/*     */   private int[] cpIMethodDescriptorInts;
/*     */   private int[] cpInt;
/*     */   private long[] cpLong;
/*     */   private String[] cpMethodClass;
/*     */   private String[] cpMethodDescriptor;
/*     */   private int[] cpMethodClassInts;
/*     */   private int[] cpMethodDescriptorInts;
/*     */   private String[] cpSignature;
/*     */   private int[] cpSignatureInts;
/*     */   private String[] cpString;
/*     */   private int[] cpStringInts;
/*     */   private String[] cpUTF8;
/*  78 */   private final Map<String, CPUTF8> stringsToCPUTF8 = new HashMap<>();
/*  79 */   private final Map<String, CPString> stringsToCPStrings = new HashMap<>();
/*  80 */   private final Map<Long, CPLong> longsToCPLongs = new HashMap<>();
/*  81 */   private final Map<Integer, CPInteger> integersToCPIntegers = new HashMap<>();
/*  82 */   private final Map<Float, CPFloat> floatsToCPFloats = new HashMap<>();
/*  83 */   private final Map<String, CPClass> stringsToCPClass = new HashMap<>();
/*  84 */   private final Map<Double, CPDouble> doublesToCPDoubles = new HashMap<>();
/*  85 */   private final Map<String, CPNameAndType> descriptorsToCPNameAndTypes = new HashMap<>();
/*     */   
/*     */   private Map<String, Integer> mapClass;
/*     */   
/*     */   private Map<String, Integer> mapDescriptor;
/*     */   
/*     */   private Map<String, Integer> mapUTF8;
/*     */   
/*     */   private Map<String, Integer> mapSignature;
/*     */   private int intOffset;
/*     */   private int floatOffset;
/*     */   private int longOffset;
/*     */   private int doubleOffset;
/*     */   private int stringOffset;
/*     */   private int classOffset;
/*     */   private int signatureOffset;
/*     */   private int descrOffset;
/*     */   private int fieldOffset;
/*     */   private int methodOffset;
/*     */   private int imethodOffset;
/*     */   
/*     */   public CpBands(Segment segment) {
/* 107 */     super(segment);
/*     */   }
/*     */ 
/*     */   
/*     */   public void read(InputStream in) throws IOException, Pack200Exception {
/* 112 */     parseCpUtf8(in);
/* 113 */     parseCpInt(in);
/* 114 */     parseCpFloat(in);
/* 115 */     parseCpLong(in);
/* 116 */     parseCpDouble(in);
/* 117 */     parseCpString(in);
/* 118 */     parseCpClass(in);
/* 119 */     parseCpSignature(in);
/* 120 */     parseCpDescriptor(in);
/* 121 */     parseCpField(in);
/* 122 */     parseCpMethod(in);
/* 123 */     parseCpIMethod(in);
/*     */     
/* 125 */     this.intOffset = this.cpUTF8.length;
/* 126 */     this.floatOffset = this.intOffset + this.cpInt.length;
/* 127 */     this.longOffset = this.floatOffset + this.cpFloat.length;
/* 128 */     this.doubleOffset = this.longOffset + this.cpLong.length;
/* 129 */     this.stringOffset = this.doubleOffset + this.cpDouble.length;
/* 130 */     this.classOffset = this.stringOffset + this.cpString.length;
/* 131 */     this.signatureOffset = this.classOffset + this.cpClass.length;
/* 132 */     this.descrOffset = this.signatureOffset + this.cpSignature.length;
/* 133 */     this.fieldOffset = this.descrOffset + this.cpDescriptor.length;
/* 134 */     this.methodOffset = this.fieldOffset + this.cpFieldClass.length;
/* 135 */     this.imethodOffset = this.methodOffset + this.cpMethodClass.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unpack() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void parseCpClass(InputStream in) throws IOException, Pack200Exception {
/* 152 */     int cpClassCount = this.header.getCpClassCount();
/* 153 */     this.cpClassInts = decodeBandInt("cp_Class", in, Codec.UDELTA5, cpClassCount);
/* 154 */     this.cpClass = new String[cpClassCount];
/* 155 */     this.mapClass = new HashMap<>(cpClassCount);
/* 156 */     for (int i = 0; i < cpClassCount; i++) {
/* 157 */       this.cpClass[i] = this.cpUTF8[this.cpClassInts[i]];
/* 158 */       this.mapClass.put(this.cpClass[i], Integer.valueOf(i));
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
/*     */   private void parseCpDescriptor(InputStream in) throws IOException, Pack200Exception {
/* 173 */     int cpDescriptorCount = this.header.getCpDescriptorCount();
/* 174 */     this.cpDescriptorNameInts = decodeBandInt("cp_Descr_name", in, Codec.DELTA5, cpDescriptorCount);
/* 175 */     this.cpDescriptorTypeInts = decodeBandInt("cp_Descr_type", in, Codec.UDELTA5, cpDescriptorCount);
/* 176 */     String[] cpDescriptorNames = getReferences(this.cpDescriptorNameInts, this.cpUTF8);
/* 177 */     String[] cpDescriptorTypes = getReferences(this.cpDescriptorTypeInts, this.cpSignature);
/* 178 */     this.cpDescriptor = new String[cpDescriptorCount];
/* 179 */     this.mapDescriptor = new HashMap<>(cpDescriptorCount);
/* 180 */     for (int i = 0; i < cpDescriptorCount; i++) {
/* 181 */       this.cpDescriptor[i] = cpDescriptorNames[i] + ":" + cpDescriptorTypes[i];
/* 182 */       this.mapDescriptor.put(this.cpDescriptor[i], Integer.valueOf(i));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void parseCpDouble(InputStream in) throws IOException, Pack200Exception {
/* 187 */     int cpDoubleCount = this.header.getCpDoubleCount();
/* 188 */     long[] band = parseFlags("cp_Double", in, cpDoubleCount, Codec.UDELTA5, Codec.DELTA5);
/* 189 */     this.cpDouble = new double[band.length];
/* 190 */     Arrays.setAll(this.cpDouble, i -> Double.longBitsToDouble(band[i]));
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
/*     */   private void parseCpField(InputStream in) throws IOException, Pack200Exception {
/* 202 */     int cpFieldCount = this.header.getCpFieldCount();
/* 203 */     this.cpFieldClassInts = decodeBandInt("cp_Field_class", in, Codec.DELTA5, cpFieldCount);
/* 204 */     this.cpFieldDescriptorInts = decodeBandInt("cp_Field_desc", in, Codec.UDELTA5, cpFieldCount);
/* 205 */     this.cpFieldClass = new String[cpFieldCount];
/* 206 */     this.cpFieldDescriptor = new String[cpFieldCount];
/* 207 */     for (int i = 0; i < cpFieldCount; i++) {
/* 208 */       this.cpFieldClass[i] = this.cpClass[this.cpFieldClassInts[i]];
/* 209 */       this.cpFieldDescriptor[i] = this.cpDescriptor[this.cpFieldDescriptorInts[i]];
/*     */     } 
/*     */   }
/*     */   
/*     */   private void parseCpFloat(InputStream in) throws IOException, Pack200Exception {
/* 214 */     int cpFloatCount = this.header.getCpFloatCount();
/* 215 */     this.cpFloat = new float[cpFloatCount];
/* 216 */     int[] floatBits = decodeBandInt("cp_Float", in, Codec.UDELTA5, cpFloatCount);
/* 217 */     for (int i = 0; i < cpFloatCount; i++) {
/* 218 */       this.cpFloat[i] = Float.intBitsToFloat(floatBits[i]);
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
/*     */   private void parseCpIMethod(InputStream in) throws IOException, Pack200Exception {
/* 231 */     int cpIMethodCount = this.header.getCpIMethodCount();
/* 232 */     this.cpIMethodClassInts = decodeBandInt("cp_Imethod_class", in, Codec.DELTA5, cpIMethodCount);
/* 233 */     this.cpIMethodDescriptorInts = decodeBandInt("cp_Imethod_desc", in, Codec.UDELTA5, cpIMethodCount);
/* 234 */     this.cpIMethodClass = new String[cpIMethodCount];
/* 235 */     this.cpIMethodDescriptor = new String[cpIMethodCount];
/* 236 */     for (int i = 0; i < cpIMethodCount; i++) {
/* 237 */       this.cpIMethodClass[i] = this.cpClass[this.cpIMethodClassInts[i]];
/* 238 */       this.cpIMethodDescriptor[i] = this.cpDescriptor[this.cpIMethodDescriptorInts[i]];
/*     */     } 
/*     */   }
/*     */   
/*     */   private void parseCpInt(InputStream in) throws IOException, Pack200Exception {
/* 243 */     int cpIntCount = this.header.getCpIntCount();
/* 244 */     this.cpInt = decodeBandInt("cpInt", in, Codec.UDELTA5, cpIntCount);
/*     */   }
/*     */   
/*     */   private void parseCpLong(InputStream in) throws IOException, Pack200Exception {
/* 248 */     int cpLongCount = this.header.getCpLongCount();
/* 249 */     this.cpLong = parseFlags("cp_Long", in, cpLongCount, Codec.UDELTA5, Codec.DELTA5);
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
/*     */   private void parseCpMethod(InputStream in) throws IOException, Pack200Exception {
/* 261 */     int cpMethodCount = this.header.getCpMethodCount();
/* 262 */     this.cpMethodClassInts = decodeBandInt("cp_Method_class", in, Codec.DELTA5, cpMethodCount);
/* 263 */     this.cpMethodDescriptorInts = decodeBandInt("cp_Method_desc", in, Codec.UDELTA5, cpMethodCount);
/* 264 */     this.cpMethodClass = new String[cpMethodCount];
/* 265 */     this.cpMethodDescriptor = new String[cpMethodCount];
/* 266 */     for (int i = 0; i < cpMethodCount; i++) {
/* 267 */       this.cpMethodClass[i] = this.cpClass[this.cpMethodClassInts[i]];
/* 268 */       this.cpMethodDescriptor[i] = this.cpDescriptor[this.cpMethodDescriptorInts[i]];
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
/*     */   private void parseCpSignature(InputStream in) throws IOException, Pack200Exception {
/* 286 */     int cpSignatureCount = this.header.getCpSignatureCount();
/* 287 */     this.cpSignatureInts = decodeBandInt("cp_Signature_form", in, Codec.DELTA5, cpSignatureCount);
/* 288 */     String[] cpSignatureForm = getReferences(this.cpSignatureInts, this.cpUTF8);
/* 289 */     this.cpSignature = new String[cpSignatureCount];
/* 290 */     this.mapSignature = new HashMap<>();
/* 291 */     int lCount = 0;
/* 292 */     for (int i = 0; i < cpSignatureCount; i++) {
/* 293 */       String form = cpSignatureForm[i];
/* 294 */       char[] chars = form.toCharArray();
/* 295 */       for (char element : chars) {
/* 296 */         if (element == 'L') {
/* 297 */           this.cpSignatureInts[i] = -1;
/* 298 */           lCount++;
/*     */         } 
/*     */       } 
/*     */     } 
/* 302 */     String[] cpSignatureClasses = parseReferences("cp_Signature_classes", in, Codec.UDELTA5, lCount, this.cpClass);
/* 303 */     int index = 0;
/* 304 */     for (int j = 0; j < cpSignatureCount; j++) {
/* 305 */       String form = cpSignatureForm[j];
/* 306 */       int len = form.length();
/* 307 */       StringBuilder signature = new StringBuilder(64);
/* 308 */       ArrayList<String> list = new ArrayList<>();
/* 309 */       for (int k = 0; k < len; k++) {
/* 310 */         char c = form.charAt(k);
/* 311 */         signature.append(c);
/* 312 */         if (c == 'L') {
/* 313 */           String className = cpSignatureClasses[index];
/* 314 */           list.add(className);
/* 315 */           signature.append(className);
/* 316 */           index++;
/*     */         } 
/*     */       } 
/* 319 */       this.cpSignature[j] = signature.toString();
/* 320 */       this.mapSignature.put(signature.toString(), Integer.valueOf(j));
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
/*     */   private void parseCpString(InputStream in) throws IOException, Pack200Exception {
/* 338 */     int cpStringCount = this.header.getCpStringCount();
/* 339 */     this.cpStringInts = decodeBandInt("cp_String", in, Codec.UDELTA5, cpStringCount);
/* 340 */     this.cpString = new String[cpStringCount];
/* 341 */     Arrays.setAll(this.cpString, i -> this.cpUTF8[this.cpStringInts[i]]);
/*     */   }
/*     */   
/*     */   private void parseCpUtf8(InputStream in) throws IOException, Pack200Exception {
/* 345 */     int cpUTF8Count = this.header.getCpUTF8Count();
/* 346 */     this.cpUTF8 = new String[cpUTF8Count];
/* 347 */     this.mapUTF8 = new HashMap<>(cpUTF8Count + 1);
/* 348 */     this.cpUTF8[0] = "";
/* 349 */     this.mapUTF8.put("", Integer.valueOf(0));
/* 350 */     int[] prefix = decodeBandInt("cpUTF8Prefix", in, Codec.DELTA5, cpUTF8Count - 2);
/* 351 */     int charCount = 0;
/* 352 */     int bigSuffixCount = 0;
/* 353 */     int[] suffix = decodeBandInt("cpUTF8Suffix", in, Codec.UNSIGNED5, cpUTF8Count - 1);
/*     */     
/* 355 */     for (int element : suffix) {
/* 356 */       if (element == 0) {
/* 357 */         bigSuffixCount++;
/*     */       } else {
/* 359 */         charCount += element;
/*     */       } 
/*     */     } 
/* 362 */     char[] data = new char[charCount];
/* 363 */     int[] dataBand = decodeBandInt("cp_Utf8_chars", in, Codec.CHAR3, charCount);
/* 364 */     for (int i = 0; i < data.length; i++) {
/* 365 */       data[i] = (char)dataBand[i];
/*     */     }
/*     */ 
/*     */     
/* 369 */     int[] bigSuffixCounts = decodeBandInt("cp_Utf8_big_suffix", in, Codec.DELTA5, bigSuffixCount);
/* 370 */     int[][] bigSuffixDataBand = new int[bigSuffixCount][];
/* 371 */     for (int j = 0; j < bigSuffixDataBand.length; j++) {
/* 372 */       bigSuffixDataBand[j] = decodeBandInt("cp_Utf8_big_chars " + j, in, Codec.DELTA5, bigSuffixCounts[j]);
/*     */     }
/*     */ 
/*     */     
/* 376 */     char[][] bigSuffixData = new char[bigSuffixCount][]; int k;
/* 377 */     for (k = 0; k < bigSuffixDataBand.length; k++) {
/* 378 */       bigSuffixData[k] = new char[(bigSuffixDataBand[k]).length];
/* 379 */       for (int m = 0; m < (bigSuffixDataBand[k]).length; m++) {
/* 380 */         bigSuffixData[k][m] = (char)bigSuffixDataBand[k][m];
/*     */       }
/*     */     } 
/*     */     
/* 384 */     charCount = 0;
/* 385 */     bigSuffixCount = 0;
/* 386 */     for (k = 1; k < cpUTF8Count; k++) {
/* 387 */       String lastString = this.cpUTF8[k - 1];
/* 388 */       if (suffix[k - 1] == 0) {
/*     */ 
/*     */         
/* 391 */         this.cpUTF8[k] = lastString.substring(0, (k > 1) ? prefix[k - 2] : 0) + new String(bigSuffixData[bigSuffixCount++]);
/*     */         
/* 393 */         this.mapUTF8.put(this.cpUTF8[k], Integer.valueOf(k));
/*     */       } else {
/* 395 */         this.cpUTF8[k] = lastString.substring(0, (k > 1) ? prefix[k - 2] : 0) + new String(data, charCount, suffix[k - 1]);
/*     */         
/* 397 */         charCount += suffix[k - 1];
/* 398 */         this.mapUTF8.put(this.cpUTF8[k], Integer.valueOf(k));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public String[] getCpClass() {
/* 404 */     return this.cpClass;
/*     */   }
/*     */   
/*     */   public String[] getCpDescriptor() {
/* 408 */     return this.cpDescriptor;
/*     */   }
/*     */   
/*     */   public String[] getCpFieldClass() {
/* 412 */     return this.cpFieldClass;
/*     */   }
/*     */   
/*     */   public String[] getCpIMethodClass() {
/* 416 */     return this.cpIMethodClass;
/*     */   }
/*     */   
/*     */   public int[] getCpInt() {
/* 420 */     return this.cpInt;
/*     */   }
/*     */   
/*     */   public long[] getCpLong() {
/* 424 */     return this.cpLong;
/*     */   }
/*     */   
/*     */   public String[] getCpMethodClass() {
/* 428 */     return this.cpMethodClass;
/*     */   }
/*     */   
/*     */   public String[] getCpMethodDescriptor() {
/* 432 */     return this.cpMethodDescriptor;
/*     */   }
/*     */   
/*     */   public String[] getCpSignature() {
/* 436 */     return this.cpSignature;
/*     */   }
/*     */   
/*     */   public String[] getCpUTF8() {
/* 440 */     return this.cpUTF8;
/*     */   }
/*     */   
/*     */   public CPUTF8 cpUTF8Value(int index) {
/* 444 */     String string = this.cpUTF8[index];
/* 445 */     CPUTF8 cputf8 = this.stringsToCPUTF8.get(string);
/* 446 */     if (cputf8 == null) {
/* 447 */       cputf8 = new CPUTF8(string, index);
/* 448 */       this.stringsToCPUTF8.put(string, cputf8);
/* 449 */     } else if (cputf8.getGlobalIndex() > index) {
/* 450 */       cputf8.setGlobalIndex(index);
/*     */     } 
/* 452 */     return cputf8;
/*     */   }
/*     */   
/*     */   public CPUTF8 cpUTF8Value(String string) {
/* 456 */     return cpUTF8Value(string, true);
/*     */   }
/*     */   
/*     */   public CPUTF8 cpUTF8Value(String string, boolean searchForIndex) {
/* 460 */     CPUTF8 cputf8 = this.stringsToCPUTF8.get(string);
/* 461 */     if (cputf8 == null) {
/* 462 */       Integer index = null;
/* 463 */       if (searchForIndex) {
/* 464 */         index = this.mapUTF8.get(string);
/*     */       }
/* 466 */       if (index != null) {
/* 467 */         return cpUTF8Value(index.intValue());
/*     */       }
/* 469 */       if (searchForIndex) {
/* 470 */         index = this.mapSignature.get(string);
/*     */       }
/* 472 */       if (index != null) {
/* 473 */         return cpSignatureValue(index.intValue());
/*     */       }
/* 475 */       cputf8 = new CPUTF8(string, -1);
/* 476 */       this.stringsToCPUTF8.put(string, cputf8);
/*     */     } 
/* 478 */     return cputf8;
/*     */   }
/*     */   
/*     */   public CPString cpStringValue(int index) {
/* 482 */     String string = this.cpString[index];
/* 483 */     int utf8Index = this.cpStringInts[index];
/* 484 */     int globalIndex = this.stringOffset + index;
/* 485 */     CPString cpString = this.stringsToCPStrings.get(string);
/* 486 */     if (cpString == null) {
/* 487 */       cpString = new CPString(cpUTF8Value(utf8Index), globalIndex);
/* 488 */       this.stringsToCPStrings.put(string, cpString);
/*     */     } 
/* 490 */     return cpString;
/*     */   }
/*     */   
/*     */   public CPLong cpLongValue(int index) {
/* 494 */     Long l = Long.valueOf(this.cpLong[index]);
/* 495 */     CPLong cpLong = this.longsToCPLongs.get(l);
/* 496 */     if (cpLong == null) {
/* 497 */       cpLong = new CPLong(l, index + this.longOffset);
/* 498 */       this.longsToCPLongs.put(l, cpLong);
/*     */     } 
/* 500 */     return cpLong;
/*     */   }
/*     */   
/*     */   public CPInteger cpIntegerValue(int index) {
/* 504 */     Integer i = Integer.valueOf(this.cpInt[index]);
/* 505 */     CPInteger cpInteger = this.integersToCPIntegers.get(i);
/* 506 */     if (cpInteger == null) {
/* 507 */       cpInteger = new CPInteger(i, index + this.intOffset);
/* 508 */       this.integersToCPIntegers.put(i, cpInteger);
/*     */     } 
/* 510 */     return cpInteger;
/*     */   }
/*     */   
/*     */   public CPFloat cpFloatValue(int index) {
/* 514 */     Float f = Float.valueOf(this.cpFloat[index]);
/* 515 */     CPFloat cpFloat = this.floatsToCPFloats.get(f);
/* 516 */     if (cpFloat == null) {
/* 517 */       cpFloat = new CPFloat(f, index + this.floatOffset);
/* 518 */       this.floatsToCPFloats.put(f, cpFloat);
/*     */     } 
/* 520 */     return cpFloat;
/*     */   }
/*     */   
/*     */   public CPClass cpClassValue(int index) {
/* 524 */     String string = this.cpClass[index];
/* 525 */     int utf8Index = this.cpClassInts[index];
/* 526 */     int globalIndex = this.classOffset + index;
/* 527 */     CPClass cpString = this.stringsToCPClass.get(string);
/* 528 */     if (cpString == null) {
/* 529 */       cpString = new CPClass(cpUTF8Value(utf8Index), globalIndex);
/* 530 */       this.stringsToCPClass.put(string, cpString);
/*     */     } 
/* 532 */     return cpString;
/*     */   }
/*     */   
/*     */   public CPClass cpClassValue(String string) {
/* 536 */     CPClass cpString = this.stringsToCPClass.get(string);
/* 537 */     if (cpString == null) {
/* 538 */       Integer index = this.mapClass.get(string);
/* 539 */       if (index != null) {
/* 540 */         return cpClassValue(index.intValue());
/*     */       }
/* 542 */       cpString = new CPClass(cpUTF8Value(string, false), -1);
/* 543 */       this.stringsToCPClass.put(string, cpString);
/*     */     } 
/* 545 */     return cpString;
/*     */   }
/*     */   
/*     */   public CPDouble cpDoubleValue(int index) {
/* 549 */     Double dbl = Double.valueOf(this.cpDouble[index]);
/* 550 */     CPDouble cpDouble = this.doublesToCPDoubles.get(dbl);
/* 551 */     if (cpDouble == null) {
/* 552 */       cpDouble = new CPDouble(dbl, index + this.doubleOffset);
/* 553 */       this.doublesToCPDoubles.put(dbl, cpDouble);
/*     */     } 
/* 555 */     return cpDouble;
/*     */   }
/*     */   
/*     */   public CPNameAndType cpNameAndTypeValue(int index) {
/* 559 */     String descriptor = this.cpDescriptor[index];
/* 560 */     CPNameAndType cpNameAndType = this.descriptorsToCPNameAndTypes.get(descriptor);
/* 561 */     if (cpNameAndType == null) {
/* 562 */       int nameIndex = this.cpDescriptorNameInts[index];
/* 563 */       int descriptorIndex = this.cpDescriptorTypeInts[index];
/*     */       
/* 565 */       CPUTF8 name = cpUTF8Value(nameIndex);
/* 566 */       CPUTF8 descriptorU = cpSignatureValue(descriptorIndex);
/* 567 */       cpNameAndType = new CPNameAndType(name, descriptorU, index + this.descrOffset);
/* 568 */       this.descriptorsToCPNameAndTypes.put(descriptor, cpNameAndType);
/*     */     } 
/* 570 */     return cpNameAndType;
/*     */   }
/*     */   
/*     */   public CPInterfaceMethodRef cpIMethodValue(int index) {
/* 574 */     return new CPInterfaceMethodRef(cpClassValue(this.cpIMethodClassInts[index]), 
/* 575 */         cpNameAndTypeValue(this.cpIMethodDescriptorInts[index]), index + this.imethodOffset);
/*     */   }
/*     */   
/*     */   public CPMethodRef cpMethodValue(int index) {
/* 579 */     return new CPMethodRef(cpClassValue(this.cpMethodClassInts[index]), 
/* 580 */         cpNameAndTypeValue(this.cpMethodDescriptorInts[index]), index + this.methodOffset);
/*     */   }
/*     */   
/*     */   public CPFieldRef cpFieldValue(int index) {
/* 584 */     return new CPFieldRef(cpClassValue(this.cpFieldClassInts[index]), cpNameAndTypeValue(this.cpFieldDescriptorInts[index]), index + this.fieldOffset);
/*     */   }
/*     */ 
/*     */   
/*     */   public CPUTF8 cpSignatureValue(int index) {
/*     */     int globalIndex;
/* 590 */     if (this.cpSignatureInts[index] != -1) {
/* 591 */       globalIndex = this.cpSignatureInts[index];
/*     */     } else {
/* 593 */       globalIndex = index + this.signatureOffset;
/*     */     } 
/* 595 */     String string = this.cpSignature[index];
/* 596 */     CPUTF8 cpUTF8 = this.stringsToCPUTF8.get(string);
/* 597 */     if (cpUTF8 == null) {
/* 598 */       cpUTF8 = new CPUTF8(string, globalIndex);
/* 599 */       this.stringsToCPUTF8.put(string, cpUTF8);
/*     */     } 
/* 601 */     return cpUTF8;
/*     */   }
/*     */   
/*     */   public CPNameAndType cpNameAndTypeValue(String descriptor) {
/* 605 */     CPNameAndType cpNameAndType = this.descriptorsToCPNameAndTypes.get(descriptor);
/* 606 */     if (cpNameAndType == null) {
/* 607 */       Integer index = this.mapDescriptor.get(descriptor);
/* 608 */       if (index != null) {
/* 609 */         return cpNameAndTypeValue(index.intValue());
/*     */       }
/* 611 */       int colon = descriptor.indexOf(':');
/* 612 */       String nameString = descriptor.substring(0, colon);
/* 613 */       String descriptorString = descriptor.substring(colon + 1);
/*     */       
/* 615 */       CPUTF8 name = cpUTF8Value(nameString, true);
/* 616 */       CPUTF8 descriptorU = cpUTF8Value(descriptorString, true);
/* 617 */       cpNameAndType = new CPNameAndType(name, descriptorU, -1 + this.descrOffset);
/* 618 */       this.descriptorsToCPNameAndTypes.put(descriptor, cpNameAndType);
/*     */     } 
/* 620 */     return cpNameAndType;
/*     */   }
/*     */   
/*     */   public int[] getCpDescriptorNameInts() {
/* 624 */     return this.cpDescriptorNameInts;
/*     */   }
/*     */   
/*     */   public int[] getCpDescriptorTypeInts() {
/* 628 */     return this.cpDescriptorTypeInts;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\harmon\\unpack200\CpBands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */