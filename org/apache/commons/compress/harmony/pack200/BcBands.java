/*     */ package org.apache.commons.compress.harmony.pack200;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.stream.Collectors;
/*     */ import org.objectweb.asm.Label;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BcBands
/*     */   extends BandSet
/*     */ {
/*     */   private final CpBands cpBands;
/*     */   private final Segment segment;
/*     */   private final IntList bcCodes;
/*     */   private final IntList bcCaseCount;
/*     */   private final IntList bcCaseValue;
/*     */   private final IntList bcByte;
/*     */   private final IntList bcShort;
/*     */   private final IntList bcLocal;
/*     */   private final List bcLabel;
/*     */   private final List<CPInt> bcIntref;
/*     */   private final List<CPFloat> bcFloatRef;
/*     */   private final List<CPLong> bcLongRef;
/*     */   private final List<CPDouble> bcDoubleRef;
/*     */   private final List<CPString> bcStringRef;
/*     */   private final List<CPClass> bcClassRef;
/*     */   private final List<CPMethodOrField> bcFieldRef;
/*     */   private final List<CPMethodOrField> bcMethodRef;
/*     */   private final List<CPMethodOrField> bcIMethodRef;
/*     */   private List bcThisField;
/*     */   
/*     */   public BcBands(CpBands cpBands, Segment segment, int effort) {
/*  38 */     super(effort, segment.getSegmentHeader());
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  43 */     this.bcCodes = new IntList();
/*  44 */     this.bcCaseCount = new IntList();
/*  45 */     this.bcCaseValue = new IntList();
/*  46 */     this.bcByte = new IntList();
/*  47 */     this.bcShort = new IntList();
/*  48 */     this.bcLocal = new IntList();
/*     */ 
/*     */     
/*  51 */     this.bcLabel = new ArrayList();
/*  52 */     this.bcIntref = new ArrayList<>();
/*  53 */     this.bcFloatRef = new ArrayList<>();
/*  54 */     this.bcLongRef = new ArrayList<>();
/*  55 */     this.bcDoubleRef = new ArrayList<>();
/*  56 */     this.bcStringRef = new ArrayList<>();
/*  57 */     this.bcClassRef = new ArrayList<>();
/*  58 */     this.bcFieldRef = new ArrayList<>();
/*  59 */     this.bcMethodRef = new ArrayList<>();
/*  60 */     this.bcIMethodRef = new ArrayList<>();
/*  61 */     this.bcThisField = new ArrayList();
/*  62 */     this.bcSuperField = new ArrayList();
/*  63 */     this.bcThisMethod = new ArrayList();
/*  64 */     this.bcSuperMethod = new ArrayList();
/*  65 */     this.bcInitRef = new ArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  80 */     this.bciRenumbering = new IntList();
/*  81 */     this.labelsToOffsets = new HashMap<>();
/*     */ 
/*     */     
/*  84 */     this.bcLabelRelativeOffsets = new IntList();
/*     */     this.cpBands = cpBands;
/*     */     this.segment = segment; } private final List bcSuperField; private List bcThisMethod; private List bcSuperMethod; private List bcInitRef; private String currentClass; private String superClass; private String currentNewClass; private static final int MULTIANEWARRAY = 197; private static final int ALOAD_0 = 42; private static final int WIDE = 196; private static final int INVOKEINTERFACE = 185; private static final int TABLESWITCH = 170; private static final int IINC = 132; private static final int LOOKUPSWITCH = 171; private static final int endMarker = 255; private final IntList bciRenumbering; private final Map<Label, Integer> labelsToOffsets; private int byteCodeOffset; private int renumberedOffset; private final IntList bcLabelRelativeOffsets; public void setCurrentClass(String name, String superName) {
/*  87 */     this.currentClass = name;
/*  88 */     this.superClass = superName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void finaliseBands() {
/*  96 */     this.bcThisField = getIndexInClass(this.bcThisField);
/*  97 */     this.bcThisMethod = getIndexInClass(this.bcThisMethod);
/*  98 */     this.bcSuperMethod = getIndexInClass(this.bcSuperMethod);
/*  99 */     this.bcInitRef = getIndexInClassForConstructor(this.bcInitRef);
/*     */   }
/*     */ 
/*     */   
/*     */   public void pack(OutputStream out) throws IOException, Pack200Exception {
/* 104 */     PackingUtils.log("Writing byte code bands...");
/* 105 */     byte[] encodedBand = encodeBandInt("bcCodes", this.bcCodes.toArray(), Codec.BYTE1);
/* 106 */     out.write(encodedBand);
/* 107 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from bcCodes[" + this.bcCodes.size() + "]");
/*     */     
/* 109 */     encodedBand = encodeBandInt("bcCaseCount", this.bcCaseCount.toArray(), Codec.UNSIGNED5);
/* 110 */     out.write(encodedBand);
/* 111 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from bcCaseCount[" + this.bcCaseCount.size() + "]");
/*     */     
/* 113 */     encodedBand = encodeBandInt("bcCaseValue", this.bcCaseValue.toArray(), Codec.DELTA5);
/* 114 */     out.write(encodedBand);
/* 115 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from bcCaseValue[" + this.bcCaseValue.size() + "]");
/*     */     
/* 117 */     encodedBand = encodeBandInt("bcByte", this.bcByte.toArray(), Codec.BYTE1);
/* 118 */     out.write(encodedBand);
/* 119 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from bcByte[" + this.bcByte.size() + "]");
/*     */     
/* 121 */     encodedBand = encodeBandInt("bcShort", this.bcShort.toArray(), Codec.DELTA5);
/* 122 */     out.write(encodedBand);
/* 123 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from bcShort[" + this.bcShort.size() + "]");
/*     */     
/* 125 */     encodedBand = encodeBandInt("bcLocal", this.bcLocal.toArray(), Codec.UNSIGNED5);
/* 126 */     out.write(encodedBand);
/* 127 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from bcLocal[" + this.bcLocal.size() + "]");
/*     */     
/* 129 */     encodedBand = encodeBandInt("bcLabel", integerListToArray(this.bcLabel), Codec.BRANCH5);
/* 130 */     out.write(encodedBand);
/* 131 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from bcLabel[" + this.bcLabel.size() + "]");
/*     */     
/* 133 */     encodedBand = encodeBandInt("bcIntref", cpEntryListToArray((List)this.bcIntref), Codec.DELTA5);
/* 134 */     out.write(encodedBand);
/* 135 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from bcIntref[" + this.bcIntref.size() + "]");
/*     */     
/* 137 */     encodedBand = encodeBandInt("bcFloatRef", cpEntryListToArray((List)this.bcFloatRef), Codec.DELTA5);
/* 138 */     out.write(encodedBand);
/* 139 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from bcFloatRef[" + this.bcFloatRef.size() + "]");
/*     */     
/* 141 */     encodedBand = encodeBandInt("bcLongRef", cpEntryListToArray((List)this.bcLongRef), Codec.DELTA5);
/* 142 */     out.write(encodedBand);
/* 143 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from bcLongRef[" + this.bcLongRef.size() + "]");
/*     */     
/* 145 */     encodedBand = encodeBandInt("bcDoubleRef", cpEntryListToArray((List)this.bcDoubleRef), Codec.DELTA5);
/* 146 */     out.write(encodedBand);
/* 147 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from bcDoubleRef[" + this.bcDoubleRef.size() + "]");
/*     */     
/* 149 */     encodedBand = encodeBandInt("bcStringRef", cpEntryListToArray((List)this.bcStringRef), Codec.DELTA5);
/* 150 */     out.write(encodedBand);
/* 151 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from bcStringRef[" + this.bcStringRef.size() + "]");
/*     */     
/* 153 */     encodedBand = encodeBandInt("bcClassRef", cpEntryOrNullListToArray((List)this.bcClassRef), Codec.UNSIGNED5);
/* 154 */     out.write(encodedBand);
/* 155 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from bcClassRef[" + this.bcClassRef.size() + "]");
/*     */     
/* 157 */     encodedBand = encodeBandInt("bcFieldRef", cpEntryListToArray((List)this.bcFieldRef), Codec.DELTA5);
/* 158 */     out.write(encodedBand);
/* 159 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from bcFieldRef[" + this.bcFieldRef.size() + "]");
/*     */     
/* 161 */     encodedBand = encodeBandInt("bcMethodRef", cpEntryListToArray((List)this.bcMethodRef), Codec.UNSIGNED5);
/* 162 */     out.write(encodedBand);
/* 163 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from bcMethodRef[" + this.bcMethodRef.size() + "]");
/*     */     
/* 165 */     encodedBand = encodeBandInt("bcIMethodRef", cpEntryListToArray((List)this.bcIMethodRef), Codec.DELTA5);
/* 166 */     out.write(encodedBand);
/* 167 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from bcIMethodRef[" + this.bcIMethodRef.size() + "]");
/*     */     
/* 169 */     encodedBand = encodeBandInt("bcThisField", integerListToArray(this.bcThisField), Codec.UNSIGNED5);
/* 170 */     out.write(encodedBand);
/* 171 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from bcThisField[" + this.bcThisField.size() + "]");
/*     */     
/* 173 */     encodedBand = encodeBandInt("bcSuperField", integerListToArray(this.bcSuperField), Codec.UNSIGNED5);
/* 174 */     out.write(encodedBand);
/* 175 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from bcSuperField[" + this.bcSuperField.size() + "]");
/*     */     
/* 177 */     encodedBand = encodeBandInt("bcThisMethod", integerListToArray(this.bcThisMethod), Codec.UNSIGNED5);
/* 178 */     out.write(encodedBand);
/* 179 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from bcThisMethod[" + this.bcThisMethod.size() + "]");
/*     */     
/* 181 */     encodedBand = encodeBandInt("bcSuperMethod", integerListToArray(this.bcSuperMethod), Codec.UNSIGNED5);
/* 182 */     out.write(encodedBand);
/* 183 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from bcSuperMethod[" + this.bcSuperMethod.size() + "]");
/*     */     
/* 185 */     encodedBand = encodeBandInt("bcInitRef", integerListToArray(this.bcInitRef), Codec.UNSIGNED5);
/* 186 */     out.write(encodedBand);
/* 187 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from bcInitRef[" + this.bcInitRef.size() + "]");
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
/*     */   private List<Integer> getIndexInClass(List<CPMethodOrField> cPMethodOrFieldList) {
/* 199 */     return (List<Integer>)cPMethodOrFieldList.stream().collect(Collectors.mapping(CPMethodOrField::getIndexInClass, Collectors.toList()));
/*     */   }
/*     */   
/*     */   private List<Integer> getIndexInClassForConstructor(List<CPMethodOrField> cPMethodList) {
/* 203 */     return (List<Integer>)cPMethodList.stream().collect(Collectors.mapping(CPMethodOrField::getIndexInClassForConstructor, Collectors.toList()));
/*     */   }
/*     */   public void visitEnd() {
/*     */     int i;
/* 207 */     for (i = 0; i < this.bciRenumbering.size(); i++) {
/* 208 */       if (this.bciRenumbering.get(i) == -1) {
/* 209 */         this.bciRenumbering.remove(i);
/* 210 */         this.bciRenumbering.add(i, ++this.renumberedOffset);
/*     */       } 
/*     */     } 
/* 213 */     if (this.renumberedOffset != 0) {
/* 214 */       if (this.renumberedOffset + 1 != this.bciRenumbering.size()) {
/* 215 */         throw new IllegalStateException("Mistake made with renumbering");
/*     */       }
/* 217 */       for (i = this.bcLabel.size() - 1; i >= 0; i--) {
/* 218 */         Object label = this.bcLabel.get(i);
/* 219 */         if (label instanceof Integer) {
/*     */           break;
/*     */         }
/* 222 */         if (label instanceof Label) {
/* 223 */           this.bcLabel.remove(i);
/* 224 */           Integer offset = this.labelsToOffsets.get(label);
/* 225 */           int relativeOffset = this.bcLabelRelativeOffsets.get(i);
/* 226 */           this.bcLabel.add(i, 
/* 227 */               Integer.valueOf(this.bciRenumbering.get(offset.intValue()) - this.bciRenumbering.get(relativeOffset)));
/*     */         } 
/*     */       } 
/* 230 */       this.bcCodes.add(255);
/* 231 */       this.segment.getClassBands().doBciRenumbering(this.bciRenumbering, this.labelsToOffsets);
/* 232 */       this.bciRenumbering.clear();
/* 233 */       this.labelsToOffsets.clear();
/* 234 */       this.byteCodeOffset = 0;
/* 235 */       this.renumberedOffset = 0;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void visitLabel(Label label) {
/* 240 */     this.labelsToOffsets.put(label, Integer.valueOf(this.byteCodeOffset));
/*     */   }
/*     */   
/*     */   public void visitFieldInsn(int opcode, String owner, String name, String desc) {
/* 244 */     this.byteCodeOffset += 3;
/* 245 */     updateRenumbering();
/* 246 */     boolean aload_0 = false;
/* 247 */     if (this.bcCodes.size() > 0 && this.bcCodes.get(this.bcCodes.size() - 1) == 42) {
/* 248 */       this.bcCodes.remove(this.bcCodes.size() - 1);
/* 249 */       aload_0 = true;
/*     */     } 
/* 251 */     CPMethodOrField cpField = this.cpBands.getCPField(owner, name, desc);
/* 252 */     if (aload_0) {
/* 253 */       opcode += 7;
/*     */     }
/* 255 */     if (owner.equals(this.currentClass)) {
/* 256 */       opcode += 24;
/* 257 */       this.bcThisField.add(cpField);
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 262 */       if (aload_0) {
/* 263 */         opcode -= 7;
/* 264 */         this.bcCodes.add(42);
/*     */       } 
/*     */ 
/*     */       
/* 268 */       this.bcFieldRef.add(cpField);
/*     */     } 
/* 270 */     aload_0 = false;
/* 271 */     this.bcCodes.add(opcode);
/*     */   }
/*     */   
/*     */   private void updateRenumbering() {
/* 275 */     if (this.bciRenumbering.isEmpty()) {
/* 276 */       this.bciRenumbering.add(0);
/*     */     }
/* 278 */     this.renumberedOffset++;
/* 279 */     for (int i = this.bciRenumbering.size(); i < this.byteCodeOffset; i++) {
/* 280 */       this.bciRenumbering.add(-1);
/*     */     }
/* 282 */     this.bciRenumbering.add(this.renumberedOffset);
/*     */   }
/*     */   
/*     */   public void visitIincInsn(int var, int increment) {
/* 286 */     if (var > 255 || increment > 255) {
/* 287 */       this.byteCodeOffset += 6;
/* 288 */       this.bcCodes.add(196);
/* 289 */       this.bcCodes.add(132);
/* 290 */       this.bcLocal.add(var);
/* 291 */       this.bcShort.add(increment);
/*     */     } else {
/* 293 */       this.byteCodeOffset += 3;
/* 294 */       this.bcCodes.add(132);
/* 295 */       this.bcLocal.add(var);
/* 296 */       this.bcByte.add(increment & 0xFF);
/*     */     } 
/* 298 */     updateRenumbering();
/*     */   }
/*     */   
/*     */   public void visitInsn(int opcode) {
/* 302 */     if (opcode >= 202) {
/* 303 */       throw new IllegalArgumentException("Non-standard bytecode instructions not supported");
/*     */     }
/* 305 */     this.bcCodes.add(opcode);
/* 306 */     this.byteCodeOffset++;
/* 307 */     updateRenumbering();
/*     */   }
/*     */   
/*     */   public void visitIntInsn(int opcode, int operand) {
/* 311 */     switch (opcode) {
/*     */       case 17:
/* 313 */         this.bcCodes.add(opcode);
/* 314 */         this.bcShort.add(operand);
/* 315 */         this.byteCodeOffset += 3;
/*     */         break;
/*     */       case 16:
/*     */       case 188:
/* 319 */         this.bcCodes.add(opcode);
/* 320 */         this.bcByte.add(operand & 0xFF);
/* 321 */         this.byteCodeOffset += 2; break;
/*     */     } 
/* 323 */     updateRenumbering();
/*     */   }
/*     */   
/*     */   public void visitJumpInsn(int opcode, Label label) {
/* 327 */     this.bcCodes.add(opcode);
/* 328 */     this.bcLabel.add(label);
/* 329 */     this.bcLabelRelativeOffsets.add(this.byteCodeOffset);
/* 330 */     this.byteCodeOffset += 3;
/* 331 */     updateRenumbering();
/*     */   }
/*     */   
/*     */   public void visitLdcInsn(Object cst) {
/* 335 */     CPConstant<?> constant = this.cpBands.getConstant(cst);
/* 336 */     if (this.segment.lastConstantHadWideIndex() || constant instanceof CPLong || constant instanceof CPDouble) {
/* 337 */       this.byteCodeOffset += 3;
/* 338 */       if (constant instanceof CPInt) {
/* 339 */         this.bcCodes.add(237);
/* 340 */         this.bcIntref.add((CPInt)constant);
/* 341 */       } else if (constant instanceof CPFloat) {
/* 342 */         this.bcCodes.add(238);
/* 343 */         this.bcFloatRef.add((CPFloat)constant);
/* 344 */       } else if (constant instanceof CPLong) {
/* 345 */         this.bcCodes.add(20);
/* 346 */         this.bcLongRef.add((CPLong)constant);
/* 347 */       } else if (constant instanceof CPDouble) {
/* 348 */         this.bcCodes.add(239);
/* 349 */         this.bcDoubleRef.add((CPDouble)constant);
/* 350 */       } else if (constant instanceof CPString) {
/* 351 */         this.bcCodes.add(19);
/* 352 */         this.bcStringRef.add((CPString)constant);
/* 353 */       } else if (constant instanceof CPClass) {
/* 354 */         this.bcCodes.add(236);
/* 355 */         this.bcClassRef.add((CPClass)constant);
/*     */       } else {
/* 357 */         throw new IllegalArgumentException("Constant should not be null");
/*     */       } 
/*     */     } else {
/* 360 */       this.byteCodeOffset += 2;
/* 361 */       if (constant instanceof CPInt) {
/* 362 */         this.bcCodes.add(234);
/* 363 */         this.bcIntref.add((CPInt)constant);
/* 364 */       } else if (constant instanceof CPFloat) {
/* 365 */         this.bcCodes.add(235);
/* 366 */         this.bcFloatRef.add((CPFloat)constant);
/* 367 */       } else if (constant instanceof CPString) {
/* 368 */         this.bcCodes.add(18);
/* 369 */         this.bcStringRef.add((CPString)constant);
/* 370 */       } else if (constant instanceof CPClass) {
/* 371 */         this.bcCodes.add(233);
/* 372 */         this.bcClassRef.add((CPClass)constant);
/*     */       } 
/*     */     } 
/* 375 */     updateRenumbering();
/*     */   }
/*     */   
/*     */   public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
/* 379 */     this.bcCodes.add(171);
/* 380 */     this.bcLabel.add(dflt);
/* 381 */     this.bcLabelRelativeOffsets.add(this.byteCodeOffset);
/* 382 */     this.bcCaseCount.add(keys.length);
/* 383 */     for (int i = 0; i < labels.length; i++) {
/* 384 */       this.bcCaseValue.add(keys[i]);
/* 385 */       this.bcLabel.add(labels[i]);
/* 386 */       this.bcLabelRelativeOffsets.add(this.byteCodeOffset);
/*     */     } 
/* 388 */     int padding = ((this.byteCodeOffset + 1) % 4 == 0) ? 0 : (4 - (this.byteCodeOffset + 1) % 4);
/* 389 */     this.byteCodeOffset += 1 + padding + 8 + 8 * keys.length;
/* 390 */     updateRenumbering();
/*     */   } public void visitMethodInsn(int opcode, String owner, String name, String desc) {
/*     */     boolean aload_0;
/*     */     CPMethodOrField cpIMethod;
/* 394 */     this.byteCodeOffset += 3;
/* 395 */     switch (opcode) {
/*     */       case 182:
/*     */       case 183:
/*     */       case 184:
/* 399 */         aload_0 = false;
/* 400 */         if (this.bcCodes.size() > 0 && this.bcCodes.get(this.bcCodes.size() - 1) == 42) {
/* 401 */           this.bcCodes.remove(this.bcCodes.size() - 1);
/* 402 */           aload_0 = true;
/* 403 */           opcode += 7;
/*     */         } 
/* 405 */         if (owner.equals(this.currentClass)) {
/* 406 */           opcode += 24;
/*     */ 
/*     */           
/* 409 */           if (name.equals("<init>") && opcode == 207) {
/* 410 */             opcode = 230;
/* 411 */             this.bcInitRef.add(this.cpBands.getCPMethod(owner, name, desc));
/*     */           } else {
/* 413 */             this.bcThisMethod.add(this.cpBands.getCPMethod(owner, name, desc));
/*     */           } 
/* 415 */         } else if (owner.equals(this.superClass)) {
/* 416 */           opcode += 38;
/*     */           
/* 418 */           if (name.equals("<init>") && opcode == 221) {
/* 419 */             opcode = 231;
/* 420 */             this.bcInitRef.add(this.cpBands.getCPMethod(owner, name, desc));
/*     */           } else {
/* 422 */             this.bcSuperMethod.add(this.cpBands.getCPMethod(owner, name, desc));
/*     */           } 
/*     */         } else {
/* 425 */           if (aload_0) {
/* 426 */             opcode -= 7;
/* 427 */             this.bcCodes.add(42);
/*     */           } 
/*     */ 
/*     */ 
/*     */           
/* 432 */           if (name.equals("<init>") && opcode == 183 && owner.equals(this.currentNewClass)) {
/* 433 */             opcode = 232;
/* 434 */             this.bcInitRef.add(this.cpBands.getCPMethod(owner, name, desc));
/*     */           } else {
/* 436 */             this.bcMethodRef.add(this.cpBands.getCPMethod(owner, name, desc));
/*     */           } 
/*     */         } 
/* 439 */         this.bcCodes.add(opcode);
/*     */         break;
/*     */       case 185:
/* 442 */         this.byteCodeOffset += 2;
/* 443 */         cpIMethod = this.cpBands.getCPIMethod(owner, name, desc);
/* 444 */         this.bcIMethodRef.add(cpIMethod);
/* 445 */         this.bcCodes.add(185);
/*     */         break;
/*     */     } 
/* 448 */     updateRenumbering();
/*     */   }
/*     */   
/*     */   public void visitMultiANewArrayInsn(String desc, int dimensions) {
/* 452 */     this.byteCodeOffset += 4;
/* 453 */     updateRenumbering();
/* 454 */     this.bcCodes.add(197);
/* 455 */     this.bcClassRef.add(this.cpBands.getCPClass(desc));
/* 456 */     this.bcByte.add(dimensions & 0xFF);
/*     */   }
/*     */   
/*     */   public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
/* 460 */     this.bcCodes.add(170);
/* 461 */     this.bcLabel.add(dflt);
/* 462 */     this.bcLabelRelativeOffsets.add(this.byteCodeOffset);
/* 463 */     this.bcCaseValue.add(min);
/* 464 */     int count = labels.length;
/* 465 */     this.bcCaseCount.add(count);
/* 466 */     for (int i = 0; i < count; i++) {
/* 467 */       this.bcLabel.add(labels[i]);
/* 468 */       this.bcLabelRelativeOffsets.add(this.byteCodeOffset);
/*     */     } 
/* 470 */     int padding = (this.byteCodeOffset % 4 == 0) ? 0 : (4 - this.byteCodeOffset % 4);
/* 471 */     this.byteCodeOffset += padding + 12 + 4 * labels.length;
/* 472 */     updateRenumbering();
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitTypeInsn(int opcode, String type) {
/* 477 */     this.byteCodeOffset += 3;
/* 478 */     updateRenumbering();
/* 479 */     this.bcCodes.add(opcode);
/* 480 */     this.bcClassRef.add(this.cpBands.getCPClass(type));
/* 481 */     if (opcode == 187) {
/* 482 */       this.currentNewClass = type;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitVarInsn(int opcode, int var) {
/* 488 */     if (var > 255) {
/* 489 */       this.byteCodeOffset += 4;
/* 490 */       this.bcCodes.add(196);
/* 491 */       this.bcCodes.add(opcode);
/* 492 */       this.bcLocal.add(var);
/* 493 */     } else if (var > 3 || opcode == 169) {
/* 494 */       this.byteCodeOffset += 2;
/* 495 */       this.bcCodes.add(opcode);
/* 496 */       this.bcLocal.add(var);
/*     */     } else {
/* 498 */       this.byteCodeOffset++;
/* 499 */       switch (opcode) {
/*     */         case 21:
/*     */         case 54:
/* 502 */           this.bcCodes.add(opcode + 5 + var);
/*     */           break;
/*     */         case 22:
/*     */         case 55:
/* 506 */           this.bcCodes.add(opcode + 8 + var);
/*     */           break;
/*     */         case 23:
/*     */         case 56:
/* 510 */           this.bcCodes.add(opcode + 11 + var);
/*     */           break;
/*     */         case 24:
/*     */         case 57:
/* 514 */           this.bcCodes.add(opcode + 14 + var);
/*     */           break;
/*     */         case 25:
/*     */         case 58:
/* 518 */           this.bcCodes.add(opcode + 17 + var);
/*     */           break;
/*     */       } 
/*     */     } 
/* 522 */     updateRenumbering();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\harmony\pack200\BcBands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */