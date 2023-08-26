/*     */ package org.apache.commons.compress.harmony.pack200;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ import org.objectweb.asm.Type;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*  38 */   private final Set<String> defaultAttributeNames = new HashSet<>();
/*     */   
/*  40 */   private final Set<CPUTF8> cp_Utf8 = new TreeSet<>();
/*  41 */   private final Set<CPInt> cp_Int = new TreeSet<>();
/*  42 */   private final Set<CPFloat> cp_Float = new TreeSet<>();
/*  43 */   private final Set<CPLong> cp_Long = new TreeSet<>();
/*  44 */   private final Set<CPDouble> cp_Double = new TreeSet<>();
/*  45 */   private final Set<CPString> cp_String = new TreeSet<>();
/*  46 */   private final Set<CPClass> cp_Class = new TreeSet<>();
/*  47 */   private final Set<CPSignature> cp_Signature = new TreeSet<>();
/*  48 */   private final Set<CPNameAndType> cp_Descr = new TreeSet<>();
/*  49 */   private final Set<CPMethodOrField> cp_Field = new TreeSet<>();
/*  50 */   private final Set<CPMethodOrField> cp_Method = new TreeSet<>();
/*  51 */   private final Set<CPMethodOrField> cp_Imethod = new TreeSet<>();
/*     */   
/*  53 */   private final Map<String, CPUTF8> stringsToCpUtf8 = new HashMap<>();
/*  54 */   private final Map<String, CPNameAndType> stringsToCpNameAndType = new HashMap<>();
/*  55 */   private final Map<String, CPClass> stringsToCpClass = new HashMap<>();
/*  56 */   private final Map<String, CPSignature> stringsToCpSignature = new HashMap<>();
/*  57 */   private final Map<String, CPMethodOrField> stringsToCpMethod = new HashMap<>();
/*  58 */   private final Map<String, CPMethodOrField> stringsToCpField = new HashMap<>();
/*  59 */   private final Map<String, CPMethodOrField> stringsToCpIMethod = new HashMap<>();
/*     */   
/*  61 */   private final Map<Object, CPConstant<?>> objectsToCPConstant = new HashMap<>();
/*     */   
/*     */   private final Segment segment;
/*     */   
/*     */   public CpBands(Segment segment, int effort) {
/*  66 */     super(effort, segment.getSegmentHeader());
/*  67 */     this.segment = segment;
/*  68 */     this.defaultAttributeNames.add("AnnotationDefault");
/*  69 */     this.defaultAttributeNames.add("RuntimeVisibleAnnotations");
/*  70 */     this.defaultAttributeNames.add("RuntimeInvisibleAnnotations");
/*  71 */     this.defaultAttributeNames.add("RuntimeVisibleParameterAnnotations");
/*  72 */     this.defaultAttributeNames.add("RuntimeInvisibleParameterAnnotations");
/*  73 */     this.defaultAttributeNames.add("Code");
/*  74 */     this.defaultAttributeNames.add("LineNumberTable");
/*  75 */     this.defaultAttributeNames.add("LocalVariableTable");
/*  76 */     this.defaultAttributeNames.add("LocalVariableTypeTable");
/*  77 */     this.defaultAttributeNames.add("ConstantValue");
/*  78 */     this.defaultAttributeNames.add("Deprecated");
/*  79 */     this.defaultAttributeNames.add("EnclosingMethod");
/*  80 */     this.defaultAttributeNames.add("Exceptions");
/*  81 */     this.defaultAttributeNames.add("InnerClasses");
/*  82 */     this.defaultAttributeNames.add("Signature");
/*  83 */     this.defaultAttributeNames.add("SourceFile");
/*     */   }
/*     */ 
/*     */   
/*     */   public void pack(OutputStream out) throws IOException, Pack200Exception {
/*  88 */     PackingUtils.log("Writing constant pool bands...");
/*  89 */     writeCpUtf8(out);
/*  90 */     writeCpInt(out);
/*  91 */     writeCpFloat(out);
/*  92 */     writeCpLong(out);
/*  93 */     writeCpDouble(out);
/*  94 */     writeCpString(out);
/*  95 */     writeCpClass(out);
/*  96 */     writeCpSignature(out);
/*  97 */     writeCpDescr(out);
/*  98 */     writeCpMethodOrField(this.cp_Field, out, "cp_Field");
/*  99 */     writeCpMethodOrField(this.cp_Method, out, "cp_Method");
/* 100 */     writeCpMethodOrField(this.cp_Imethod, out, "cp_Imethod");
/*     */   }
/*     */   
/*     */   private void writeCpUtf8(OutputStream out) throws IOException, Pack200Exception {
/* 104 */     PackingUtils.log("Writing " + this.cp_Utf8.size() + " UTF8 entries...");
/* 105 */     int[] cpUtf8Prefix = new int[this.cp_Utf8.size() - 2];
/* 106 */     int[] cpUtf8Suffix = new int[this.cp_Utf8.size() - 1];
/* 107 */     List<Character> chars = new ArrayList<>();
/* 108 */     List<Integer> bigSuffix = new ArrayList<>();
/* 109 */     List<Character> bigChars = new ArrayList<>();
/* 110 */     Object[] cpUtf8Array = this.cp_Utf8.toArray();
/* 111 */     String first = ((CPUTF8)cpUtf8Array[1]).getUnderlyingString();
/* 112 */     cpUtf8Suffix[0] = first.length();
/* 113 */     addCharacters(chars, first.toCharArray());
/* 114 */     for (int i = 2; i < cpUtf8Array.length; i++) {
/* 115 */       char[] previous = ((CPUTF8)cpUtf8Array[i - 1]).getUnderlyingString().toCharArray();
/* 116 */       String currentStr = ((CPUTF8)cpUtf8Array[i]).getUnderlyingString();
/* 117 */       char[] current = currentStr.toCharArray();
/* 118 */       int prefix = 0;
/* 119 */       for (int m = 0; m < previous.length && 
/* 120 */         previous[m] == current[m]; m++)
/*     */       {
/*     */         
/* 123 */         prefix++;
/*     */       }
/* 125 */       cpUtf8Prefix[i - 2] = prefix;
/* 126 */       currentStr = currentStr.substring(prefix);
/* 127 */       char[] suffix = currentStr.toCharArray();
/* 128 */       if (suffix.length > 1000) {
/*     */         
/* 130 */         cpUtf8Suffix[i - 1] = 0;
/* 131 */         bigSuffix.add(Integer.valueOf(suffix.length));
/* 132 */         addCharacters(bigChars, suffix);
/*     */       } else {
/* 134 */         cpUtf8Suffix[i - 1] = suffix.length;
/* 135 */         addCharacters(chars, suffix);
/*     */       } 
/*     */     } 
/* 138 */     int[] cpUtf8Chars = new int[chars.size()];
/* 139 */     int[] cpUtf8BigSuffix = new int[bigSuffix.size()];
/* 140 */     int[][] cpUtf8BigChars = new int[bigSuffix.size()][];
/* 141 */     Arrays.setAll(cpUtf8Chars, i -> ((Character)chars.get(i)).charValue());
/* 142 */     for (int j = 0; j < cpUtf8BigSuffix.length; j++) {
/* 143 */       int numBigChars = ((Integer)bigSuffix.get(j)).intValue();
/* 144 */       cpUtf8BigSuffix[j] = numBigChars;
/* 145 */       cpUtf8BigChars[j] = new int[numBigChars];
/* 146 */       Arrays.setAll(cpUtf8BigChars[j], j -> ((Character)bigChars.remove(0)).charValue());
/*     */     } 
/*     */     
/* 149 */     byte[] encodedBand = encodeBandInt("cpUtf8Prefix", cpUtf8Prefix, Codec.DELTA5);
/* 150 */     out.write(encodedBand);
/* 151 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from cpUtf8Prefix[" + cpUtf8Prefix.length + "]");
/*     */     
/* 153 */     encodedBand = encodeBandInt("cpUtf8Suffix", cpUtf8Suffix, Codec.UNSIGNED5);
/* 154 */     out.write(encodedBand);
/* 155 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from cpUtf8Suffix[" + cpUtf8Suffix.length + "]");
/*     */     
/* 157 */     encodedBand = encodeBandInt("cpUtf8Chars", cpUtf8Chars, Codec.CHAR3);
/* 158 */     out.write(encodedBand);
/* 159 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from cpUtf8Chars[" + cpUtf8Chars.length + "]");
/*     */     
/* 161 */     encodedBand = encodeBandInt("cpUtf8BigSuffix", cpUtf8BigSuffix, Codec.DELTA5);
/* 162 */     out.write(encodedBand);
/* 163 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from cpUtf8BigSuffix[" + cpUtf8BigSuffix.length + "]");
/*     */     
/* 165 */     for (int k = 0; k < cpUtf8BigChars.length; k++) {
/* 166 */       encodedBand = encodeBandInt("cpUtf8BigChars " + k, cpUtf8BigChars[k], Codec.DELTA5);
/* 167 */       out.write(encodedBand);
/* 168 */       PackingUtils.log("Wrote " + encodedBand.length + " bytes from cpUtf8BigChars" + k + "[" + (cpUtf8BigChars[k]).length + "]");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void addCharacters(List<Character> chars, char[] charArray) {
/* 174 */     for (char element : charArray) {
/* 175 */       chars.add(Character.valueOf(element));
/*     */     }
/*     */   }
/*     */   
/*     */   private void writeCpInt(OutputStream out) throws IOException, Pack200Exception {
/* 180 */     PackingUtils.log("Writing " + this.cp_Int.size() + " Integer entries...");
/* 181 */     int[] cpInt = new int[this.cp_Int.size()];
/* 182 */     int i = 0;
/* 183 */     for (CPInt integer : this.cp_Int) {
/* 184 */       cpInt[i] = integer.getInt();
/* 185 */       i++;
/*     */     } 
/* 187 */     byte[] encodedBand = encodeBandInt("cp_Int", cpInt, Codec.UDELTA5);
/* 188 */     out.write(encodedBand);
/* 189 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from cp_Int[" + cpInt.length + "]");
/*     */   }
/*     */   
/*     */   private void writeCpFloat(OutputStream out) throws IOException, Pack200Exception {
/* 193 */     PackingUtils.log("Writing " + this.cp_Float.size() + " Float entries...");
/* 194 */     int[] cpFloat = new int[this.cp_Float.size()];
/* 195 */     int i = 0;
/* 196 */     for (CPFloat fl : this.cp_Float) {
/* 197 */       cpFloat[i] = Float.floatToIntBits(fl.getFloat());
/* 198 */       i++;
/*     */     } 
/* 200 */     byte[] encodedBand = encodeBandInt("cp_Float", cpFloat, Codec.UDELTA5);
/* 201 */     out.write(encodedBand);
/* 202 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from cp_Float[" + cpFloat.length + "]");
/*     */   }
/*     */   
/*     */   private void writeCpLong(OutputStream out) throws IOException, Pack200Exception {
/* 206 */     PackingUtils.log("Writing " + this.cp_Long.size() + " Long entries...");
/* 207 */     int[] highBits = new int[this.cp_Long.size()];
/* 208 */     int[] loBits = new int[this.cp_Long.size()];
/* 209 */     int i = 0;
/* 210 */     for (CPLong lng : this.cp_Long) {
/* 211 */       long l = lng.getLong();
/* 212 */       highBits[i] = (int)(l >> 32L);
/* 213 */       loBits[i] = (int)l;
/* 214 */       i++;
/*     */     } 
/* 216 */     byte[] encodedBand = encodeBandInt("cp_Long_hi", highBits, Codec.UDELTA5);
/* 217 */     out.write(encodedBand);
/* 218 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from cp_Long_hi[" + highBits.length + "]");
/*     */     
/* 220 */     encodedBand = encodeBandInt("cp_Long_lo", loBits, Codec.DELTA5);
/* 221 */     out.write(encodedBand);
/* 222 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from cp_Long_lo[" + loBits.length + "]");
/*     */   }
/*     */   
/*     */   private void writeCpDouble(OutputStream out) throws IOException, Pack200Exception {
/* 226 */     PackingUtils.log("Writing " + this.cp_Double.size() + " Double entries...");
/* 227 */     int[] highBits = new int[this.cp_Double.size()];
/* 228 */     int[] loBits = new int[this.cp_Double.size()];
/* 229 */     int i = 0;
/* 230 */     for (CPDouble dbl : this.cp_Double) {
/* 231 */       long l = Double.doubleToLongBits(dbl.getDouble());
/* 232 */       highBits[i] = (int)(l >> 32L);
/* 233 */       loBits[i] = (int)l;
/* 234 */       i++;
/*     */     } 
/* 236 */     byte[] encodedBand = encodeBandInt("cp_Double_hi", highBits, Codec.UDELTA5);
/* 237 */     out.write(encodedBand);
/* 238 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from cp_Double_hi[" + highBits.length + "]");
/*     */     
/* 240 */     encodedBand = encodeBandInt("cp_Double_lo", loBits, Codec.DELTA5);
/* 241 */     out.write(encodedBand);
/* 242 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from cp_Double_lo[" + loBits.length + "]");
/*     */   }
/*     */   
/*     */   private void writeCpString(OutputStream out) throws IOException, Pack200Exception {
/* 246 */     PackingUtils.log("Writing " + this.cp_String.size() + " String entries...");
/* 247 */     int[] cpString = new int[this.cp_String.size()];
/* 248 */     int i = 0;
/* 249 */     for (CPString cpStr : this.cp_String) {
/* 250 */       cpString[i] = cpStr.getIndexInCpUtf8();
/* 251 */       i++;
/*     */     } 
/* 253 */     byte[] encodedBand = encodeBandInt("cpString", cpString, Codec.UDELTA5);
/* 254 */     out.write(encodedBand);
/* 255 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from cpString[" + cpString.length + "]");
/*     */   }
/*     */   
/*     */   private void writeCpClass(OutputStream out) throws IOException, Pack200Exception {
/* 259 */     PackingUtils.log("Writing " + this.cp_Class.size() + " Class entries...");
/* 260 */     int[] cpClass = new int[this.cp_Class.size()];
/* 261 */     int i = 0;
/* 262 */     for (CPClass cpCl : this.cp_Class) {
/* 263 */       cpClass[i] = cpCl.getIndexInCpUtf8();
/* 264 */       i++;
/*     */     } 
/* 266 */     byte[] encodedBand = encodeBandInt("cpClass", cpClass, Codec.UDELTA5);
/* 267 */     out.write(encodedBand);
/* 268 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from cpClass[" + cpClass.length + "]");
/*     */   }
/*     */   
/*     */   private void writeCpSignature(OutputStream out) throws IOException, Pack200Exception {
/* 272 */     PackingUtils.log("Writing " + this.cp_Signature.size() + " Signature entries...");
/* 273 */     int[] cpSignatureForm = new int[this.cp_Signature.size()];
/* 274 */     List<CPClass> classes = new ArrayList<>();
/* 275 */     int i = 0;
/* 276 */     for (CPSignature cpS : this.cp_Signature) {
/* 277 */       classes.addAll(cpS.getClasses());
/* 278 */       cpSignatureForm[i] = cpS.getIndexInCpUtf8();
/* 279 */       i++;
/*     */     } 
/* 281 */     int[] cpSignatureClasses = new int[classes.size()];
/* 282 */     Arrays.setAll(cpSignatureClasses, j -> ((CPClass)classes.get(j)).getIndex());
/*     */     
/* 284 */     byte[] encodedBand = encodeBandInt("cpSignatureForm", cpSignatureForm, Codec.DELTA5);
/* 285 */     out.write(encodedBand);
/* 286 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from cpSignatureForm[" + cpSignatureForm.length + "]");
/*     */     
/* 288 */     encodedBand = encodeBandInt("cpSignatureClasses", cpSignatureClasses, Codec.UDELTA5);
/* 289 */     out.write(encodedBand);
/*     */     
/* 291 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from cpSignatureClasses[" + cpSignatureClasses.length + "]");
/*     */   }
/*     */   
/*     */   private void writeCpDescr(OutputStream out) throws IOException, Pack200Exception {
/* 295 */     PackingUtils.log("Writing " + this.cp_Descr.size() + " Descriptor entries...");
/* 296 */     int[] cpDescrName = new int[this.cp_Descr.size()];
/* 297 */     int[] cpDescrType = new int[this.cp_Descr.size()];
/* 298 */     int i = 0;
/* 299 */     for (CPNameAndType nameAndType : this.cp_Descr) {
/* 300 */       cpDescrName[i] = nameAndType.getNameIndex();
/* 301 */       cpDescrType[i] = nameAndType.getTypeIndex();
/* 302 */       i++;
/*     */     } 
/*     */     
/* 305 */     byte[] encodedBand = encodeBandInt("cp_Descr_Name", cpDescrName, Codec.DELTA5);
/* 306 */     out.write(encodedBand);
/* 307 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from cp_Descr_Name[" + cpDescrName.length + "]");
/*     */     
/* 309 */     encodedBand = encodeBandInt("cp_Descr_Type", cpDescrType, Codec.UDELTA5);
/* 310 */     out.write(encodedBand);
/* 311 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from cp_Descr_Type[" + cpDescrType.length + "]");
/*     */   }
/*     */ 
/*     */   
/*     */   private void writeCpMethodOrField(Set<CPMethodOrField> cp, OutputStream out, String name) throws IOException, Pack200Exception {
/* 316 */     PackingUtils.log("Writing " + cp.size() + " Method and Field entries...");
/* 317 */     int[] cp_methodOrField_class = new int[cp.size()];
/* 318 */     int[] cp_methodOrField_desc = new int[cp.size()];
/* 319 */     int i = 0;
/* 320 */     for (CPMethodOrField mOrF : cp) {
/* 321 */       cp_methodOrField_class[i] = mOrF.getClassIndex();
/* 322 */       cp_methodOrField_desc[i] = mOrF.getDescIndex();
/* 323 */       i++;
/*     */     } 
/* 325 */     byte[] encodedBand = encodeBandInt(name + "_class", cp_methodOrField_class, Codec.DELTA5);
/* 326 */     out.write(encodedBand);
/* 327 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from " + name + "_class[" + cp_methodOrField_class.length + "]");
/*     */ 
/*     */     
/* 330 */     encodedBand = encodeBandInt(name + "_desc", cp_methodOrField_desc, Codec.UDELTA5);
/* 331 */     out.write(encodedBand);
/*     */     
/* 333 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from " + name + "_desc[" + cp_methodOrField_desc.length + "]");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void finaliseBands() {
/* 341 */     addCPUtf8("");
/* 342 */     removeSignaturesFromCpUTF8();
/* 343 */     addIndices();
/* 344 */     this.segmentHeader.setCp_Utf8_count(this.cp_Utf8.size());
/* 345 */     this.segmentHeader.setCp_Int_count(this.cp_Int.size());
/* 346 */     this.segmentHeader.setCp_Float_count(this.cp_Float.size());
/* 347 */     this.segmentHeader.setCp_Long_count(this.cp_Long.size());
/* 348 */     this.segmentHeader.setCp_Double_count(this.cp_Double.size());
/* 349 */     this.segmentHeader.setCp_String_count(this.cp_String.size());
/* 350 */     this.segmentHeader.setCp_Class_count(this.cp_Class.size());
/* 351 */     this.segmentHeader.setCp_Signature_count(this.cp_Signature.size());
/* 352 */     this.segmentHeader.setCp_Descr_count(this.cp_Descr.size());
/* 353 */     this.segmentHeader.setCp_Field_count(this.cp_Field.size());
/* 354 */     this.segmentHeader.setCp_Method_count(this.cp_Method.size());
/* 355 */     this.segmentHeader.setCp_Imethod_count(this.cp_Imethod.size());
/*     */   }
/*     */   
/*     */   private void removeSignaturesFromCpUTF8() {
/* 359 */     this.cp_Signature.forEach(signature -> {
/*     */           String sigStr = signature.getUnderlyingString();
/*     */           CPUTF8 utf8 = signature.getSignatureForm();
/*     */           String form = utf8.getUnderlyingString();
/*     */           if (!sigStr.equals(form)) {
/*     */             removeCpUtf8(sigStr);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private void addIndices() {
/* 370 */     for (Set<? extends ConstantPoolEntry> set : Arrays.<Set>asList(new Set[] { this.cp_Utf8, this.cp_Int, this.cp_Float, this.cp_Long, this.cp_Double, this.cp_String, this.cp_Class, this.cp_Signature, this.cp_Descr, this.cp_Field, this.cp_Method, this.cp_Imethod })) {
/*     */       
/* 372 */       int j = 0;
/* 373 */       for (ConstantPoolEntry entry : set) {
/* 374 */         entry.setIndex(j);
/* 375 */         j++;
/*     */       } 
/*     */     } 
/* 378 */     Map<CPClass, Integer> classNameToIndex = new HashMap<>();
/* 379 */     this.cp_Field.forEach(mOrF -> {
/*     */           CPClass cpClassName = mOrF.getClassName();
/*     */           Integer index = (Integer)classNameToIndex.get(cpClassName);
/*     */           if (index == null) {
/*     */             classNameToIndex.put(cpClassName, Integer.valueOf(1));
/*     */             mOrF.setIndexInClass(0);
/*     */           } else {
/*     */             int theIndex = index.intValue();
/*     */             mOrF.setIndexInClass(theIndex);
/*     */             classNameToIndex.put(cpClassName, Integer.valueOf(theIndex + 1));
/*     */           } 
/*     */         });
/* 391 */     classNameToIndex.clear();
/* 392 */     Map<CPClass, Integer> classNameToConstructorIndex = new HashMap<>();
/* 393 */     this.cp_Method.forEach(mOrF -> {
/*     */           CPClass cpClassName = mOrF.getClassName();
/*     */           Integer index = (Integer)classNameToIndex.get(cpClassName);
/*     */           if (index == null) {
/*     */             classNameToIndex.put(cpClassName, Integer.valueOf(1));
/*     */             mOrF.setIndexInClass(0);
/*     */           } else {
/*     */             int theIndex = index.intValue();
/*     */             mOrF.setIndexInClass(theIndex);
/*     */             classNameToIndex.put(cpClassName, Integer.valueOf(theIndex + 1));
/*     */           } 
/*     */           if (mOrF.getDesc().getName().equals("<init>")) {
/*     */             Integer constructorIndex = (Integer)classNameToConstructorIndex.get(cpClassName);
/*     */             if (constructorIndex == null) {
/*     */               classNameToConstructorIndex.put(cpClassName, Integer.valueOf(1));
/*     */               mOrF.setIndexInClassForConstructor(0);
/*     */             } else {
/*     */               int theIndex = constructorIndex.intValue();
/*     */               mOrF.setIndexInClassForConstructor(theIndex);
/*     */               classNameToConstructorIndex.put(cpClassName, Integer.valueOf(theIndex + 1));
/*     */             } 
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   private void removeCpUtf8(String string) {
/* 419 */     CPUTF8 utf8 = this.stringsToCpUtf8.get(string);
/* 420 */     if (utf8 != null && this.stringsToCpClass.get(string) == null) {
/* 421 */       this.stringsToCpUtf8.remove(string);
/* 422 */       this.cp_Utf8.remove(utf8);
/*     */     } 
/*     */   }
/*     */   
/*     */   void addCPUtf8(String utf8) {
/* 427 */     getCPUtf8(utf8);
/*     */   }
/*     */   
/*     */   public CPUTF8 getCPUtf8(String utf8) {
/* 431 */     if (utf8 == null) {
/* 432 */       return null;
/*     */     }
/* 434 */     CPUTF8 cpUtf8 = this.stringsToCpUtf8.get(utf8);
/* 435 */     if (cpUtf8 == null) {
/* 436 */       cpUtf8 = new CPUTF8(utf8);
/* 437 */       this.cp_Utf8.add(cpUtf8);
/* 438 */       this.stringsToCpUtf8.put(utf8, cpUtf8);
/*     */     } 
/* 440 */     return cpUtf8;
/*     */   }
/*     */   
/*     */   public CPSignature getCPSignature(String signature) {
/* 444 */     if (signature == null) {
/* 445 */       return null;
/*     */     }
/* 447 */     CPSignature cpS = this.stringsToCpSignature.get(signature);
/* 448 */     if (cpS == null) {
/* 449 */       CPUTF8 signatureUTF8; List<CPClass> cpClasses = new ArrayList<>();
/*     */       
/* 451 */       if (signature.length() > 1 && signature.indexOf('L') != -1) {
/* 452 */         List<String> classes = new ArrayList<>();
/* 453 */         char[] chars = signature.toCharArray();
/* 454 */         StringBuilder signatureString = new StringBuilder();
/* 455 */         for (int i = 0; i < chars.length; i++) {
/* 456 */           signatureString.append(chars[i]);
/* 457 */           if (chars[i] == 'L') {
/* 458 */             StringBuilder className = new StringBuilder();
/* 459 */             for (int j = i + 1; j < chars.length; j++) {
/* 460 */               char c = chars[j];
/* 461 */               if (!Character.isLetter(c) && !Character.isDigit(c) && c != '/' && c != '$' && c != '_') {
/*     */                 
/* 463 */                 classes.add(className.toString());
/* 464 */                 i = j - 1;
/*     */                 break;
/*     */               } 
/* 467 */               className.append(c);
/*     */             } 
/*     */           } 
/*     */         } 
/* 471 */         removeCpUtf8(signature);
/* 472 */         for (String className : classes) {
/* 473 */           CPClass cpClass = null;
/* 474 */           if (className != null) {
/* 475 */             className = className.replace('.', '/');
/* 476 */             cpClass = this.stringsToCpClass.get(className);
/* 477 */             if (cpClass == null) {
/* 478 */               CPUTF8 cpUtf8 = getCPUtf8(className);
/* 479 */               cpClass = new CPClass(cpUtf8);
/* 480 */               this.cp_Class.add(cpClass);
/* 481 */               this.stringsToCpClass.put(className, cpClass);
/*     */             } 
/*     */           } 
/* 484 */           cpClasses.add(cpClass);
/*     */         } 
/*     */         
/* 487 */         signatureUTF8 = getCPUtf8(signatureString.toString());
/*     */       } else {
/* 489 */         signatureUTF8 = getCPUtf8(signature);
/*     */       } 
/* 491 */       cpS = new CPSignature(signature, signatureUTF8, cpClasses);
/* 492 */       this.cp_Signature.add(cpS);
/* 493 */       this.stringsToCpSignature.put(signature, cpS);
/*     */     } 
/* 495 */     return cpS;
/*     */   }
/*     */   
/*     */   public CPClass getCPClass(String className) {
/* 499 */     if (className == null) {
/* 500 */       return null;
/*     */     }
/* 502 */     className = className.replace('.', '/');
/* 503 */     CPClass cpClass = this.stringsToCpClass.get(className);
/* 504 */     if (cpClass == null) {
/* 505 */       CPUTF8 cpUtf8 = getCPUtf8(className);
/* 506 */       cpClass = new CPClass(cpUtf8);
/* 507 */       this.cp_Class.add(cpClass);
/* 508 */       this.stringsToCpClass.put(className, cpClass);
/*     */     } 
/* 510 */     if (cpClass.isInnerClass()) {
/* 511 */       this.segment.getClassBands().currentClassReferencesInnerClass(cpClass);
/*     */     }
/* 513 */     return cpClass;
/*     */   }
/*     */   
/*     */   public void addCPClass(String className) {
/* 517 */     getCPClass(className);
/*     */   }
/*     */   
/*     */   public CPNameAndType getCPNameAndType(String name, String signature) {
/* 521 */     String descr = name + ":" + signature;
/* 522 */     CPNameAndType nameAndType = this.stringsToCpNameAndType.get(descr);
/* 523 */     if (nameAndType == null) {
/* 524 */       nameAndType = new CPNameAndType(getCPUtf8(name), getCPSignature(signature));
/* 525 */       this.stringsToCpNameAndType.put(descr, nameAndType);
/* 526 */       this.cp_Descr.add(nameAndType);
/*     */     } 
/* 528 */     return nameAndType;
/*     */   }
/*     */   
/*     */   public CPMethodOrField getCPField(CPClass cpClass, String name, String desc) {
/* 532 */     String key = cpClass.toString() + ":" + name + ":" + desc;
/* 533 */     CPMethodOrField cpF = this.stringsToCpField.get(key);
/* 534 */     if (cpF == null) {
/* 535 */       CPNameAndType nAndT = getCPNameAndType(name, desc);
/* 536 */       cpF = new CPMethodOrField(cpClass, nAndT);
/* 537 */       this.cp_Field.add(cpF);
/* 538 */       this.stringsToCpField.put(key, cpF);
/*     */     } 
/* 540 */     return cpF;
/*     */   }
/*     */   
/*     */   public CPConstant<?> getConstant(Object value) {
/* 544 */     CPConstant<?> constant = this.objectsToCPConstant.get(value);
/* 545 */     if (constant == null) {
/* 546 */       if (value instanceof Integer) {
/* 547 */         constant = new CPInt(((Integer)value).intValue());
/* 548 */         this.cp_Int.add((CPInt)constant);
/* 549 */       } else if (value instanceof Long) {
/* 550 */         constant = new CPLong(((Long)value).longValue());
/* 551 */         this.cp_Long.add((CPLong)constant);
/* 552 */       } else if (value instanceof Float) {
/* 553 */         constant = new CPFloat(((Float)value).floatValue());
/* 554 */         this.cp_Float.add((CPFloat)constant);
/* 555 */       } else if (value instanceof Double) {
/* 556 */         constant = new CPDouble(((Double)value).doubleValue());
/* 557 */         this.cp_Double.add((CPDouble)constant);
/* 558 */       } else if (value instanceof String) {
/* 559 */         constant = new CPString(getCPUtf8((String)value));
/* 560 */         this.cp_String.add((CPString)constant);
/* 561 */       } else if (value instanceof Type) {
/* 562 */         String className = ((Type)value).getClassName();
/* 563 */         if (className.endsWith("[]")) {
/* 564 */           className = "[L" + className.substring(0, className.length() - 2);
/* 565 */           while (className.endsWith("[]")) {
/* 566 */             className = "[" + className.substring(0, className.length() - 2);
/*     */           }
/* 568 */           className = className + ";";
/*     */         } 
/* 570 */         constant = getCPClass(className);
/*     */       } 
/* 572 */       this.objectsToCPConstant.put(value, constant);
/*     */     } 
/* 574 */     return constant;
/*     */   }
/*     */   
/*     */   public CPMethodOrField getCPMethod(CPClass cpClass, String name, String desc) {
/* 578 */     String key = cpClass.toString() + ":" + name + ":" + desc;
/* 579 */     CPMethodOrField cpM = this.stringsToCpMethod.get(key);
/* 580 */     if (cpM == null) {
/* 581 */       CPNameAndType nAndT = getCPNameAndType(name, desc);
/* 582 */       cpM = new CPMethodOrField(cpClass, nAndT);
/* 583 */       this.cp_Method.add(cpM);
/* 584 */       this.stringsToCpMethod.put(key, cpM);
/*     */     } 
/* 586 */     return cpM;
/*     */   }
/*     */   
/*     */   public CPMethodOrField getCPIMethod(CPClass cpClass, String name, String desc) {
/* 590 */     String key = cpClass.toString() + ":" + name + ":" + desc;
/* 591 */     CPMethodOrField cpIM = this.stringsToCpIMethod.get(key);
/* 592 */     if (cpIM == null) {
/* 593 */       CPNameAndType nAndT = getCPNameAndType(name, desc);
/* 594 */       cpIM = new CPMethodOrField(cpClass, nAndT);
/* 595 */       this.cp_Imethod.add(cpIM);
/* 596 */       this.stringsToCpIMethod.put(key, cpIM);
/*     */     } 
/* 598 */     return cpIM;
/*     */   }
/*     */   
/*     */   public CPMethodOrField getCPField(String owner, String name, String desc) {
/* 602 */     return getCPField(getCPClass(owner), name, desc);
/*     */   }
/*     */   
/*     */   public CPMethodOrField getCPMethod(String owner, String name, String desc) {
/* 606 */     return getCPMethod(getCPClass(owner), name, desc);
/*     */   }
/*     */   
/*     */   public CPMethodOrField getCPIMethod(String owner, String name, String desc) {
/* 610 */     return getCPIMethod(getCPClass(owner), name, desc);
/*     */   }
/*     */   
/*     */   public boolean existsCpClass(String className) {
/* 614 */     CPClass cpClass = this.stringsToCpClass.get(className);
/* 615 */     return (cpClass != null);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\harmony\pack200\CpBands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */