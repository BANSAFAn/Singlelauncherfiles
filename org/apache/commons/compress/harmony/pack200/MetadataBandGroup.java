/*     */ package org.apache.commons.compress.harmony.pack200;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MetadataBandGroup
/*     */   extends BandSet
/*     */ {
/*     */   public static final int CONTEXT_CLASS = 0;
/*     */   public static final int CONTEXT_FIELD = 1;
/*     */   public static final int CONTEXT_METHOD = 2;
/*     */   private final String type;
/*  35 */   private int numBackwardsCalls = 0;
/*     */   
/*  37 */   public IntList param_NB = new IntList();
/*  38 */   public IntList anno_N = new IntList();
/*  39 */   public List<CPSignature> type_RS = new ArrayList<>();
/*  40 */   public IntList pair_N = new IntList();
/*  41 */   public List<CPUTF8> name_RU = new ArrayList<>();
/*  42 */   public List<String> T = new ArrayList<>();
/*  43 */   public List<CPConstant<?>> caseI_KI = new ArrayList<>();
/*  44 */   public List<CPConstant<?>> caseD_KD = new ArrayList<>();
/*  45 */   public List<CPConstant<?>> caseF_KF = new ArrayList<>();
/*  46 */   public List<CPConstant<?>> caseJ_KJ = new ArrayList<>();
/*  47 */   public List<CPSignature> casec_RS = new ArrayList<>();
/*  48 */   public List<CPSignature> caseet_RS = new ArrayList<>();
/*  49 */   public List<CPUTF8> caseec_RU = new ArrayList<>();
/*  50 */   public List<CPUTF8> cases_RU = new ArrayList<>();
/*  51 */   public IntList casearray_N = new IntList();
/*  52 */   public List<CPSignature> nesttype_RS = new ArrayList<>();
/*  53 */   public IntList nestpair_N = new IntList();
/*  54 */   public List<CPUTF8> nestname_RU = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final CpBands cpBands;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int context;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MetadataBandGroup(String type, int context, CpBands cpBands, SegmentHeader segmentHeader, int effort) {
/*  70 */     super(effort, segmentHeader);
/*  71 */     this.type = type;
/*  72 */     this.cpBands = cpBands;
/*  73 */     this.context = context;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void pack(OutputStream out) throws IOException, Pack200Exception {
/*  83 */     PackingUtils.log("Writing metadata band group...");
/*  84 */     if (hasContent()) {
/*     */       String contextStr;
/*  86 */       if (this.context == 0) {
/*  87 */         contextStr = "Class";
/*  88 */       } else if (this.context == 1) {
/*  89 */         contextStr = "Field";
/*     */       } else {
/*  91 */         contextStr = "Method";
/*     */       } 
/*  93 */       byte[] encodedBand = null;
/*  94 */       if (!this.type.equals("AD")) {
/*  95 */         if (this.type.indexOf('P') != -1) {
/*     */           
/*  97 */           encodedBand = encodeBandInt(contextStr + "_" + this.type + " param_NB", this.param_NB.toArray(), Codec.BYTE1);
/*  98 */           out.write(encodedBand);
/*  99 */           PackingUtils.log("Wrote " + encodedBand.length + " bytes from " + contextStr + "_" + this.type + " anno_N[" + this.param_NB
/* 100 */               .size() + "]");
/*     */         } 
/* 102 */         encodedBand = encodeBandInt(contextStr + "_" + this.type + " anno_N", this.anno_N.toArray(), Codec.UNSIGNED5);
/* 103 */         out.write(encodedBand);
/* 104 */         PackingUtils.log("Wrote " + encodedBand.length + " bytes from " + contextStr + "_" + this.type + " anno_N[" + this.anno_N
/* 105 */             .size() + "]");
/*     */         
/* 107 */         encodedBand = encodeBandInt(contextStr + "_" + this.type + " type_RS", cpEntryListToArray((List)this.type_RS), Codec.UNSIGNED5);
/*     */         
/* 109 */         out.write(encodedBand);
/* 110 */         PackingUtils.log("Wrote " + encodedBand.length + " bytes from " + contextStr + "_" + this.type + " type_RS[" + this.type_RS
/* 111 */             .size() + "]");
/*     */         
/* 113 */         encodedBand = encodeBandInt(contextStr + "_" + this.type + " pair_N", this.pair_N.toArray(), Codec.UNSIGNED5);
/* 114 */         out.write(encodedBand);
/* 115 */         PackingUtils.log("Wrote " + encodedBand.length + " bytes from " + contextStr + "_" + this.type + " pair_N[" + this.pair_N
/* 116 */             .size() + "]");
/*     */         
/* 118 */         encodedBand = encodeBandInt(contextStr + "_" + this.type + " name_RU", cpEntryListToArray((List)this.name_RU), Codec.UNSIGNED5);
/*     */         
/* 120 */         out.write(encodedBand);
/* 121 */         PackingUtils.log("Wrote " + encodedBand.length + " bytes from " + contextStr + "_" + this.type + " name_RU[" + this.name_RU
/* 122 */             .size() + "]");
/*     */       } 
/* 124 */       encodedBand = encodeBandInt(contextStr + "_" + this.type + " T", tagListToArray(this.T), Codec.BYTE1);
/* 125 */       out.write(encodedBand);
/*     */       
/* 127 */       PackingUtils.log("Wrote " + encodedBand.length + " bytes from " + contextStr + "_" + this.type + " T[" + this.T.size() + "]");
/*     */       
/* 129 */       encodedBand = encodeBandInt(contextStr + "_" + this.type + " caseI_KI", cpEntryListToArray((List)this.caseI_KI), Codec.UNSIGNED5);
/*     */       
/* 131 */       out.write(encodedBand);
/* 132 */       PackingUtils.log("Wrote " + encodedBand.length + " bytes from " + contextStr + "_" + this.type + " caseI_KI[" + this.caseI_KI
/* 133 */           .size() + "]");
/*     */       
/* 135 */       encodedBand = encodeBandInt(contextStr + "_" + this.type + " caseD_KD", cpEntryListToArray((List)this.caseD_KD), Codec.UNSIGNED5);
/*     */       
/* 137 */       out.write(encodedBand);
/* 138 */       PackingUtils.log("Wrote " + encodedBand.length + " bytes from " + contextStr + "_" + this.type + " caseD_KD[" + this.caseD_KD
/* 139 */           .size() + "]");
/*     */       
/* 141 */       encodedBand = encodeBandInt(contextStr + "_" + this.type + " caseF_KF", cpEntryListToArray((List)this.caseF_KF), Codec.UNSIGNED5);
/*     */       
/* 143 */       out.write(encodedBand);
/* 144 */       PackingUtils.log("Wrote " + encodedBand.length + " bytes from " + contextStr + "_" + this.type + " caseF_KF[" + this.caseF_KF
/* 145 */           .size() + "]");
/*     */       
/* 147 */       encodedBand = encodeBandInt(contextStr + "_" + this.type + " caseJ_KJ", cpEntryListToArray((List)this.caseJ_KJ), Codec.UNSIGNED5);
/*     */       
/* 149 */       out.write(encodedBand);
/* 150 */       PackingUtils.log("Wrote " + encodedBand.length + " bytes from " + contextStr + "_" + this.type + " caseJ_KJ[" + this.caseJ_KJ
/* 151 */           .size() + "]");
/*     */       
/* 153 */       encodedBand = encodeBandInt(contextStr + "_" + this.type + " casec_RS", cpEntryListToArray((List)this.casec_RS), Codec.UNSIGNED5);
/*     */       
/* 155 */       out.write(encodedBand);
/* 156 */       PackingUtils.log("Wrote " + encodedBand.length + " bytes from " + contextStr + "_" + this.type + " casec_RS[" + this.casec_RS
/* 157 */           .size() + "]");
/*     */       
/* 159 */       encodedBand = encodeBandInt(contextStr + "_" + this.type + " caseet_RS", cpEntryListToArray((List)this.caseet_RS), Codec.UNSIGNED5);
/*     */       
/* 161 */       out.write(encodedBand);
/* 162 */       PackingUtils.log("Wrote " + encodedBand.length + " bytes from " + contextStr + "_" + this.type + " caseet_RS[" + this.caseet_RS
/* 163 */           .size() + "]");
/*     */       
/* 165 */       encodedBand = encodeBandInt(contextStr + "_" + this.type + " caseec_RU", cpEntryListToArray((List)this.caseec_RU), Codec.UNSIGNED5);
/*     */       
/* 167 */       out.write(encodedBand);
/* 168 */       PackingUtils.log("Wrote " + encodedBand.length + " bytes from " + contextStr + "_" + this.type + " caseec_RU[" + this.caseec_RU
/* 169 */           .size() + "]");
/*     */       
/* 171 */       encodedBand = encodeBandInt(contextStr + "_" + this.type + " cases_RU", cpEntryListToArray((List)this.cases_RU), Codec.UNSIGNED5);
/*     */       
/* 173 */       out.write(encodedBand);
/* 174 */       PackingUtils.log("Wrote " + encodedBand.length + " bytes from " + contextStr + "_" + this.type + " cases_RU[" + this.cases_RU
/* 175 */           .size() + "]");
/*     */       
/* 177 */       encodedBand = encodeBandInt(contextStr + "_" + this.type + " casearray_N", this.casearray_N.toArray(), Codec.UNSIGNED5);
/*     */       
/* 179 */       out.write(encodedBand);
/* 180 */       PackingUtils.log("Wrote " + encodedBand.length + " bytes from " + contextStr + "_" + this.type + " casearray_N[" + this.casearray_N
/* 181 */           .size() + "]");
/*     */       
/* 183 */       encodedBand = encodeBandInt(contextStr + "_" + this.type + " nesttype_RS", cpEntryListToArray((List)this.nesttype_RS), Codec.UNSIGNED5);
/*     */       
/* 185 */       out.write(encodedBand);
/* 186 */       PackingUtils.log("Wrote " + encodedBand.length + " bytes from " + contextStr + "_" + this.type + " nesttype_RS[" + this.nesttype_RS
/* 187 */           .size() + "]");
/*     */       
/* 189 */       encodedBand = encodeBandInt(contextStr + "_" + this.type + " nestpair_N", this.nestpair_N.toArray(), Codec.UNSIGNED5);
/* 190 */       out.write(encodedBand);
/* 191 */       PackingUtils.log("Wrote " + encodedBand.length + " bytes from " + contextStr + "_" + this.type + " nestpair_N[" + this.nestpair_N
/* 192 */           .size() + "]");
/*     */       
/* 194 */       encodedBand = encodeBandInt(contextStr + "_" + this.type + " nestname_RU", cpEntryListToArray((List)this.nestname_RU), Codec.UNSIGNED5);
/*     */       
/* 196 */       out.write(encodedBand);
/* 197 */       PackingUtils.log("Wrote " + encodedBand.length + " bytes from " + contextStr + "_" + this.type + " nestname_RU[" + this.nestname_RU
/* 198 */           .size() + "]");
/*     */     } 
/*     */   }
/*     */   
/*     */   private int[] tagListToArray(List<String> list) {
/* 203 */     return list.stream().mapToInt(s -> s.charAt(0)).toArray();
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
/*     */   public void addParameterAnnotation(int numParams, int[] annoN, IntList pairN, List<String> typeRS, List<String> nameRU, List<String> t, List<Object> values, List<Integer> caseArrayN, List<String> nestTypeRS, List<String> nestNameRU, List<Integer> nestPairN) {
/* 225 */     this.param_NB.add(numParams);
/* 226 */     for (int element : annoN) {
/* 227 */       this.anno_N.add(element);
/*     */     }
/* 229 */     this.pair_N.addAll(pairN);
/* 230 */     for (String desc : typeRS) {
/* 231 */       this.type_RS.add(this.cpBands.getCPSignature(desc));
/*     */     }
/* 233 */     for (String name : nameRU) {
/* 234 */       this.name_RU.add(this.cpBands.getCPUtf8(name));
/*     */     }
/* 236 */     Iterator<Object> valuesIterator = values.iterator();
/* 237 */     for (String tag : t) {
/* 238 */       this.T.add(tag);
/* 239 */       if (tag.equals("B") || tag.equals("C") || tag.equals("I") || tag.equals("S") || tag.equals("Z")) {
/* 240 */         Integer value = (Integer)valuesIterator.next();
/* 241 */         this.caseI_KI.add(this.cpBands.getConstant(value)); continue;
/* 242 */       }  if (tag.equals("D")) {
/* 243 */         Double value = (Double)valuesIterator.next();
/* 244 */         this.caseD_KD.add(this.cpBands.getConstant(value)); continue;
/* 245 */       }  if (tag.equals("F")) {
/* 246 */         Float value = (Float)valuesIterator.next();
/* 247 */         this.caseF_KF.add(this.cpBands.getConstant(value)); continue;
/* 248 */       }  if (tag.equals("J")) {
/* 249 */         Long value = (Long)valuesIterator.next();
/* 250 */         this.caseJ_KJ.add(this.cpBands.getConstant(value)); continue;
/* 251 */       }  if (tag.equals("c")) {
/* 252 */         String value = (String)valuesIterator.next();
/* 253 */         this.casec_RS.add(this.cpBands.getCPSignature(value)); continue;
/* 254 */       }  if (tag.equals("e")) {
/* 255 */         String value = (String)valuesIterator.next();
/* 256 */         String value2 = (String)valuesIterator.next();
/* 257 */         this.caseet_RS.add(this.cpBands.getCPSignature(value));
/* 258 */         this.caseec_RU.add(this.cpBands.getCPUtf8(value2)); continue;
/* 259 */       }  if (tag.equals("s")) {
/* 260 */         String value = (String)valuesIterator.next();
/* 261 */         this.cases_RU.add(this.cpBands.getCPUtf8(value));
/*     */       } 
/*     */     } 
/*     */     
/* 265 */     for (Integer element : caseArrayN) {
/* 266 */       int arraySize = element.intValue();
/* 267 */       this.casearray_N.add(arraySize);
/* 268 */       this.numBackwardsCalls += arraySize;
/*     */     } 
/* 270 */     for (String type : nestTypeRS) {
/* 271 */       this.nesttype_RS.add(this.cpBands.getCPSignature(type));
/*     */     }
/* 273 */     for (String name : nestNameRU) {
/* 274 */       this.nestname_RU.add(this.cpBands.getCPUtf8(name));
/*     */     }
/* 276 */     for (Integer numPairs : nestPairN) {
/* 277 */       this.nestpair_N.add(numPairs.intValue());
/* 278 */       this.numBackwardsCalls += numPairs.intValue();
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
/*     */   public void addAnnotation(String desc, List<String> nameRU, List<String> tags, List<Object> values, List<Integer> caseArrayN, List<String> nestTypeRS, List<String> nestNameRU, List<Integer> nestPairN) {
/* 297 */     this.type_RS.add(this.cpBands.getCPSignature(desc));
/* 298 */     this.pair_N.add(nameRU.size());
/*     */     
/* 300 */     for (String name : nameRU) {
/* 301 */       this.name_RU.add(this.cpBands.getCPUtf8(name));
/*     */     }
/*     */     
/* 304 */     Iterator<Object> valuesIterator = values.iterator();
/* 305 */     for (String tag : tags) {
/* 306 */       this.T.add(tag);
/* 307 */       if (tag.equals("B") || tag.equals("C") || tag.equals("I") || tag.equals("S") || tag.equals("Z")) {
/* 308 */         Integer value = (Integer)valuesIterator.next();
/* 309 */         this.caseI_KI.add(this.cpBands.getConstant(value)); continue;
/* 310 */       }  if (tag.equals("D")) {
/* 311 */         Double value = (Double)valuesIterator.next();
/* 312 */         this.caseD_KD.add(this.cpBands.getConstant(value)); continue;
/* 313 */       }  if (tag.equals("F")) {
/* 314 */         Float value = (Float)valuesIterator.next();
/* 315 */         this.caseF_KF.add(this.cpBands.getConstant(value)); continue;
/* 316 */       }  if (tag.equals("J")) {
/* 317 */         Long value = (Long)valuesIterator.next();
/* 318 */         this.caseJ_KJ.add(this.cpBands.getConstant(value)); continue;
/* 319 */       }  if (tag.equals("c")) {
/* 320 */         String value = (String)valuesIterator.next();
/* 321 */         this.casec_RS.add(this.cpBands.getCPSignature(value)); continue;
/* 322 */       }  if (tag.equals("e")) {
/* 323 */         String value = (String)valuesIterator.next();
/* 324 */         String value2 = (String)valuesIterator.next();
/* 325 */         this.caseet_RS.add(this.cpBands.getCPSignature(value));
/* 326 */         this.caseec_RU.add(this.cpBands.getCPUtf8(value2)); continue;
/* 327 */       }  if (tag.equals("s")) {
/* 328 */         String value = (String)valuesIterator.next();
/* 329 */         this.cases_RU.add(this.cpBands.getCPUtf8(value));
/*     */       } 
/*     */     } 
/*     */     
/* 333 */     for (Integer element : caseArrayN) {
/* 334 */       int arraySize = element.intValue();
/* 335 */       this.casearray_N.add(arraySize);
/* 336 */       this.numBackwardsCalls += arraySize;
/*     */     } 
/* 338 */     for (String element : nestTypeRS) {
/* 339 */       String type = element;
/* 340 */       this.nesttype_RS.add(this.cpBands.getCPSignature(type));
/*     */     } 
/* 342 */     for (String element : nestNameRU) {
/* 343 */       String name = element;
/* 344 */       this.nestname_RU.add(this.cpBands.getCPUtf8(name));
/*     */     } 
/* 346 */     for (Integer element : nestPairN) {
/* 347 */       Integer numPairs = element;
/* 348 */       this.nestpair_N.add(numPairs.intValue());
/* 349 */       this.numBackwardsCalls += numPairs.intValue();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasContent() {
/* 359 */     return (this.type_RS.size() > 0);
/*     */   }
/*     */   
/*     */   public int numBackwardsCalls() {
/* 363 */     return this.numBackwardsCalls;
/*     */   }
/*     */   
/*     */   public void incrementAnnoN() {
/* 367 */     this.anno_N.increment(this.anno_N.size() - 1);
/*     */   }
/*     */   
/*     */   public void newEntryInAnnoN() {
/* 371 */     this.anno_N.add(1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeLatest() {
/* 378 */     int latest = this.anno_N.remove(this.anno_N.size() - 1);
/* 379 */     for (int i = 0; i < latest; i++) {
/* 380 */       this.type_RS.remove(this.type_RS.size() - 1);
/* 381 */       int pairs = this.pair_N.remove(this.pair_N.size() - 1);
/* 382 */       for (int j = 0; j < pairs; j++) {
/* 383 */         removeOnePair();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void removeOnePair() {
/* 392 */     String tag = this.T.remove(this.T.size() - 1);
/* 393 */     if (tag.equals("B") || tag.equals("C") || tag.equals("I") || tag.equals("S") || tag.equals("Z")) {
/* 394 */       this.caseI_KI.remove(this.caseI_KI.size() - 1);
/* 395 */     } else if (tag.equals("D")) {
/* 396 */       this.caseD_KD.remove(this.caseD_KD.size() - 1);
/* 397 */     } else if (tag.equals("F")) {
/* 398 */       this.caseF_KF.remove(this.caseF_KF.size() - 1);
/* 399 */     } else if (tag.equals("J")) {
/* 400 */       this.caseJ_KJ.remove(this.caseJ_KJ.size() - 1);
/* 401 */     } else if (tag.equals("C")) {
/* 402 */       this.casec_RS.remove(this.casec_RS.size() - 1);
/* 403 */     } else if (tag.equals("e")) {
/* 404 */       this.caseet_RS.remove(this.caseet_RS.size() - 1);
/* 405 */       this.caseec_RU.remove(this.caseet_RS.size() - 1);
/* 406 */     } else if (tag.equals("s")) {
/* 407 */       this.cases_RU.remove(this.cases_RU.size() - 1);
/* 408 */     } else if (tag.equals("[")) {
/* 409 */       int arraySize = this.casearray_N.remove(this.casearray_N.size() - 1);
/* 410 */       this.numBackwardsCalls -= arraySize;
/* 411 */       for (int k = 0; k < arraySize; k++) {
/* 412 */         removeOnePair();
/*     */       }
/* 414 */     } else if (tag.equals("@")) {
/* 415 */       this.nesttype_RS.remove(this.nesttype_RS.size() - 1);
/* 416 */       int numPairs = this.nestpair_N.remove(this.nestpair_N.size() - 1);
/* 417 */       this.numBackwardsCalls -= numPairs;
/* 418 */       for (int i = 0; i < numPairs; i++)
/* 419 */         removeOnePair(); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\harmony\pack200\MetadataBandGroup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */