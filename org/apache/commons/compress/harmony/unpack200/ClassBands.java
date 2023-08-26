/*      */ package org.apache.commons.compress.harmony.unpack200;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.List;
/*      */ import org.apache.commons.compress.harmony.pack200.Codec;
/*      */ import org.apache.commons.compress.harmony.pack200.Pack200Exception;
/*      */ import org.apache.commons.compress.harmony.unpack200.bytecode.Attribute;
/*      */ import org.apache.commons.compress.harmony.unpack200.bytecode.CPClass;
/*      */ import org.apache.commons.compress.harmony.unpack200.bytecode.CPNameAndType;
/*      */ import org.apache.commons.compress.harmony.unpack200.bytecode.CPUTF8;
/*      */ import org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry;
/*      */ import org.apache.commons.compress.harmony.unpack200.bytecode.ConstantValueAttribute;
/*      */ import org.apache.commons.compress.harmony.unpack200.bytecode.DeprecatedAttribute;
/*      */ import org.apache.commons.compress.harmony.unpack200.bytecode.EnclosingMethodAttribute;
/*      */ import org.apache.commons.compress.harmony.unpack200.bytecode.ExceptionsAttribute;
/*      */ import org.apache.commons.compress.harmony.unpack200.bytecode.LineNumberTableAttribute;
/*      */ import org.apache.commons.compress.harmony.unpack200.bytecode.LocalVariableTableAttribute;
/*      */ import org.apache.commons.compress.harmony.unpack200.bytecode.LocalVariableTypeTableAttribute;
/*      */ import org.apache.commons.compress.harmony.unpack200.bytecode.SignatureAttribute;
/*      */ import org.apache.commons.compress.harmony.unpack200.bytecode.SourceFileAttribute;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ClassBands
/*      */   extends BandSet
/*      */ {
/*      */   private int[] classFieldCount;
/*      */   private long[] classFlags;
/*      */   private long[] classAccessFlags;
/*      */   private int[][] classInterfacesInts;
/*      */   private int[] classMethodCount;
/*      */   private int[] classSuperInts;
/*      */   private String[] classThis;
/*      */   private int[] classThisInts;
/*      */   private ArrayList<Attribute>[] classAttributes;
/*      */   private int[] classVersionMajor;
/*      */   private int[] classVersionMinor;
/*      */   private IcTuple[][] icLocal;
/*      */   private List<Attribute>[] codeAttributes;
/*      */   private int[] codeHandlerCount;
/*      */   private int[] codeMaxNALocals;
/*      */   private int[] codeMaxStack;
/*      */   private ArrayList<Attribute>[][] fieldAttributes;
/*      */   private String[][] fieldDescr;
/*      */   private int[][] fieldDescrInts;
/*      */   private long[][] fieldFlags;
/*      */   private long[][] fieldAccessFlags;
/*      */   private ArrayList<Attribute>[][] methodAttributes;
/*      */   private String[][] methodDescr;
/*      */   private int[][] methodDescrInts;
/*      */   private long[][] methodFlags;
/*      */   private long[][] methodAccessFlags;
/*      */   private final AttributeLayoutMap attrMap;
/*      */   private final CpBands cpBands;
/*      */   private final SegmentOptions options;
/*      */   private final int classCount;
/*      */   private int[] methodAttrCalls;
/*      */   private int[][] codeHandlerStartP;
/*      */   private int[][] codeHandlerEndPO;
/*      */   private int[][] codeHandlerCatchPO;
/*      */   private int[][] codeHandlerClassRCN;
/*      */   private boolean[] codeHasAttributes;
/*      */   
/*      */   public ClassBands(Segment segment) {
/*  124 */     super(segment);
/*  125 */     this.attrMap = segment.getAttrDefinitionBands().getAttributeDefinitionMap();
/*  126 */     this.cpBands = segment.getCpBands();
/*  127 */     this.classCount = this.header.getClassCount();
/*  128 */     this.options = this.header.getOptions();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void read(InputStream in) throws IOException, Pack200Exception {
/*  139 */     int classCount = this.header.getClassCount();
/*  140 */     this.classThisInts = decodeBandInt("class_this", in, Codec.DELTA5, classCount);
/*  141 */     this.classThis = getReferences(this.classThisInts, this.cpBands.getCpClass());
/*  142 */     this.classSuperInts = decodeBandInt("class_super", in, Codec.DELTA5, classCount);
/*  143 */     int[] classInterfaceLengths = decodeBandInt("class_interface_count", in, Codec.DELTA5, classCount);
/*  144 */     this.classInterfacesInts = decodeBandInt("class_interface", in, Codec.DELTA5, classInterfaceLengths);
/*  145 */     this.classFieldCount = decodeBandInt("class_field_count", in, Codec.DELTA5, classCount);
/*  146 */     this.classMethodCount = decodeBandInt("class_method_count", in, Codec.DELTA5, classCount);
/*  147 */     parseFieldBands(in);
/*  148 */     parseMethodBands(in);
/*  149 */     parseClassAttrBands(in);
/*  150 */     parseCodeBands(in);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void unpack() {}
/*      */ 
/*      */ 
/*      */   
/*      */   private void parseFieldBands(InputStream in) throws IOException, Pack200Exception {
/*  160 */     this.fieldDescrInts = decodeBandInt("field_descr", in, Codec.DELTA5, this.classFieldCount);
/*  161 */     this.fieldDescr = getReferences(this.fieldDescrInts, this.cpBands.getCpDescriptor());
/*  162 */     parseFieldAttrBands(in);
/*      */   }
/*      */   
/*      */   private void parseFieldAttrBands(InputStream in) throws IOException, Pack200Exception {
/*  166 */     this.fieldFlags = parseFlags("field_flags", in, this.classFieldCount, Codec.UNSIGNED5, this.options.hasFieldFlagsHi());
/*  167 */     int fieldAttrCount = SegmentUtils.countBit16(this.fieldFlags);
/*  168 */     int[] fieldAttrCounts = decodeBandInt("field_attr_count", in, Codec.UNSIGNED5, fieldAttrCount);
/*  169 */     int[][] fieldAttrIndexes = decodeBandInt("field_attr_indexes", in, Codec.UNSIGNED5, fieldAttrCounts);
/*  170 */     int callCount = getCallCount(fieldAttrIndexes, this.fieldFlags, 1);
/*  171 */     int[] fieldAttrCalls = decodeBandInt("field_attr_calls", in, Codec.UNSIGNED5, callCount);
/*      */ 
/*      */     
/*  174 */     this.fieldAttributes = (ArrayList<Attribute>[][])new ArrayList[this.classCount][];
/*  175 */     for (int i = 0; i < this.classCount; i++) {
/*  176 */       this.fieldAttributes[i] = (ArrayList<Attribute>[])new ArrayList[(this.fieldFlags[i]).length];
/*  177 */       for (int m = 0; m < (this.fieldFlags[i]).length; m++) {
/*  178 */         this.fieldAttributes[i][m] = new ArrayList<>();
/*      */       }
/*      */     } 
/*      */     
/*  182 */     AttributeLayout constantValueLayout = this.attrMap.getAttributeLayout("ConstantValue", 1);
/*      */     
/*  184 */     int constantCount = SegmentUtils.countMatches(this.fieldFlags, constantValueLayout);
/*  185 */     int[] field_constantValue_KQ = decodeBandInt("field_ConstantValue_KQ", in, Codec.UNSIGNED5, constantCount);
/*      */     
/*  187 */     int constantValueIndex = 0;
/*      */     
/*  189 */     AttributeLayout signatureLayout = this.attrMap.getAttributeLayout("Signature", 1);
/*      */     
/*  191 */     int signatureCount = SegmentUtils.countMatches(this.fieldFlags, signatureLayout);
/*  192 */     int[] fieldSignatureRS = decodeBandInt("field_Signature_RS", in, Codec.UNSIGNED5, signatureCount);
/*  193 */     int signatureIndex = 0;
/*      */     
/*  195 */     AttributeLayout deprecatedLayout = this.attrMap.getAttributeLayout("Deprecated", 1);
/*      */ 
/*      */     
/*  198 */     for (int j = 0; j < this.classCount; j++) {
/*  199 */       for (int m = 0; m < (this.fieldFlags[j]).length; m++) {
/*  200 */         long flag = this.fieldFlags[j][m];
/*  201 */         if (deprecatedLayout.matches(flag)) {
/*  202 */           this.fieldAttributes[j][m].add(new DeprecatedAttribute());
/*      */         }
/*  204 */         if (constantValueLayout.matches(flag)) {
/*      */           
/*  206 */           long result = field_constantValue_KQ[constantValueIndex];
/*  207 */           String desc = this.fieldDescr[j][m];
/*  208 */           int colon = desc.indexOf(':');
/*  209 */           String type = desc.substring(colon + 1);
/*  210 */           if (type.equals("B") || type.equals("S") || type.equals("C") || type.equals("Z")) {
/*  211 */             type = "I";
/*      */           }
/*  213 */           ClassFileEntry value = constantValueLayout.getValue(result, type, this.cpBands.getConstantPool());
/*  214 */           this.fieldAttributes[j][m].add(new ConstantValueAttribute(value));
/*  215 */           constantValueIndex++;
/*      */         } 
/*  217 */         if (signatureLayout.matches(flag)) {
/*      */           
/*  219 */           long result = fieldSignatureRS[signatureIndex];
/*  220 */           String desc = this.fieldDescr[j][m];
/*  221 */           int colon = desc.indexOf(':');
/*  222 */           String type = desc.substring(colon + 1);
/*  223 */           CPUTF8 value = (CPUTF8)signatureLayout.getValue(result, type, this.cpBands.getConstantPool());
/*  224 */           this.fieldAttributes[j][m].add(new SignatureAttribute(value));
/*  225 */           signatureIndex++;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  231 */     int backwardsCallIndex = parseFieldMetadataBands(in, fieldAttrCalls);
/*  232 */     int limit = this.options.hasFieldFlagsHi() ? 62 : 31;
/*  233 */     AttributeLayout[] otherLayouts = new AttributeLayout[limit + 1];
/*  234 */     int[] counts = new int[limit + 1];
/*  235 */     List[] arrayOfList = new List[limit + 1]; int k;
/*  236 */     for (k = 0; k < limit; k++) {
/*  237 */       AttributeLayout layout = this.attrMap.getAttributeLayout(k, 1);
/*  238 */       if (layout != null && !layout.isDefaultLayout()) {
/*  239 */         otherLayouts[k] = layout;
/*  240 */         counts[k] = SegmentUtils.countMatches(this.fieldFlags, layout);
/*      */       } 
/*      */     } 
/*  243 */     for (k = 0; k < counts.length; k++) {
/*  244 */       if (counts[k] > 0) {
/*  245 */         NewAttributeBands bands = this.attrMap.getAttributeBands(otherLayouts[k]);
/*  246 */         arrayOfList[k] = bands.parseAttributes(in, counts[k]);
/*  247 */         int numBackwardsCallables = otherLayouts[k].numBackwardsCallables();
/*  248 */         if (numBackwardsCallables > 0) {
/*  249 */           int[] backwardsCalls = new int[numBackwardsCallables];
/*  250 */           System.arraycopy(fieldAttrCalls, backwardsCallIndex, backwardsCalls, 0, numBackwardsCallables);
/*  251 */           bands.setBackwardsCalls(backwardsCalls);
/*  252 */           backwardsCallIndex += numBackwardsCallables;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  258 */     for (k = 0; k < this.classCount; k++) {
/*  259 */       for (int m = 0; m < (this.fieldFlags[k]).length; m++) {
/*  260 */         long flag = this.fieldFlags[k][m];
/*  261 */         int othersAddedAtStart = 0;
/*  262 */         for (int n = 0; n < otherLayouts.length; n++) {
/*  263 */           if (otherLayouts[n] != null && otherLayouts[n].matches(flag)) {
/*      */             
/*  265 */             if (otherLayouts[n].getIndex() < 15) {
/*  266 */               this.fieldAttributes[k][m].add(othersAddedAtStart++, arrayOfList[n].get(0));
/*      */             } else {
/*  268 */               this.fieldAttributes[k][m].add(arrayOfList[n].get(0));
/*      */             } 
/*  270 */             arrayOfList[n].remove(0);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void parseMethodBands(InputStream in) throws IOException, Pack200Exception {
/*  278 */     this.methodDescrInts = decodeBandInt("method_descr", in, Codec.MDELTA5, this.classMethodCount);
/*  279 */     this.methodDescr = getReferences(this.methodDescrInts, this.cpBands.getCpDescriptor());
/*  280 */     parseMethodAttrBands(in);
/*      */   }
/*      */   
/*      */   private void parseMethodAttrBands(InputStream in) throws IOException, Pack200Exception {
/*  284 */     this.methodFlags = parseFlags("method_flags", in, this.classMethodCount, Codec.UNSIGNED5, this.options.hasMethodFlagsHi());
/*  285 */     int methodAttrCount = SegmentUtils.countBit16(this.methodFlags);
/*  286 */     int[] methodAttrCounts = decodeBandInt("method_attr_count", in, Codec.UNSIGNED5, methodAttrCount);
/*  287 */     int[][] methodAttrIndexes = decodeBandInt("method_attr_indexes", in, Codec.UNSIGNED5, methodAttrCounts);
/*  288 */     int callCount = getCallCount(methodAttrIndexes, this.methodFlags, 2);
/*  289 */     this.methodAttrCalls = decodeBandInt("method_attr_calls", in, Codec.UNSIGNED5, callCount);
/*      */ 
/*      */     
/*  292 */     this.methodAttributes = (ArrayList<Attribute>[][])new ArrayList[this.classCount][];
/*  293 */     for (int i = 0; i < this.classCount; i++) {
/*  294 */       this.methodAttributes[i] = (ArrayList<Attribute>[])new ArrayList[(this.methodFlags[i]).length];
/*  295 */       for (int n = 0; n < (this.methodFlags[i]).length; n++) {
/*  296 */         this.methodAttributes[i][n] = new ArrayList<>();
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  301 */     AttributeLayout methodExceptionsLayout = this.attrMap.getAttributeLayout("Exceptions", 2);
/*      */     
/*  303 */     int count = SegmentUtils.countMatches(this.methodFlags, methodExceptionsLayout);
/*  304 */     int[] numExceptions = decodeBandInt("method_Exceptions_n", in, Codec.UNSIGNED5, count);
/*  305 */     int[][] methodExceptionsRS = decodeBandInt("method_Exceptions_RC", in, Codec.UNSIGNED5, numExceptions);
/*      */ 
/*      */     
/*  308 */     AttributeLayout methodSignatureLayout = this.attrMap.getAttributeLayout("Signature", 2);
/*      */     
/*  310 */     int count1 = SegmentUtils.countMatches(this.methodFlags, methodSignatureLayout);
/*  311 */     int[] methodSignatureRS = decodeBandInt("method_signature_RS", in, Codec.UNSIGNED5, count1);
/*      */     
/*  313 */     AttributeLayout deprecatedLayout = this.attrMap.getAttributeLayout("Deprecated", 2);
/*      */ 
/*      */ 
/*      */     
/*  317 */     int methodExceptionsIndex = 0;
/*  318 */     int methodSignatureIndex = 0;
/*  319 */     for (int j = 0; j < this.methodAttributes.length; j++) {
/*  320 */       for (int n = 0; n < (this.methodAttributes[j]).length; n++) {
/*  321 */         long flag = this.methodFlags[j][n];
/*  322 */         if (methodExceptionsLayout.matches(flag)) {
/*  323 */           int i1 = numExceptions[methodExceptionsIndex];
/*  324 */           int[] exceptions = methodExceptionsRS[methodExceptionsIndex];
/*  325 */           CPClass[] exceptionClasses = new CPClass[i1];
/*  326 */           for (int i2 = 0; i2 < i1; i2++) {
/*  327 */             exceptionClasses[i2] = this.cpBands.cpClassValue(exceptions[i2]);
/*      */           }
/*  329 */           this.methodAttributes[j][n].add(new ExceptionsAttribute(exceptionClasses));
/*  330 */           methodExceptionsIndex++;
/*      */         } 
/*  332 */         if (methodSignatureLayout.matches(flag)) {
/*      */           
/*  334 */           long result = methodSignatureRS[methodSignatureIndex];
/*  335 */           String desc = this.methodDescr[j][n];
/*  336 */           int colon = desc.indexOf(':');
/*  337 */           String type = desc.substring(colon + 1);
/*      */ 
/*      */           
/*  340 */           if (type.equals("B") || type.equals("H")) {
/*  341 */             type = "I";
/*      */           }
/*  343 */           CPUTF8 value = (CPUTF8)methodSignatureLayout.getValue(result, type, this.cpBands
/*  344 */               .getConstantPool());
/*  345 */           this.methodAttributes[j][n].add(new SignatureAttribute(value));
/*  346 */           methodSignatureIndex++;
/*      */         } 
/*  348 */         if (deprecatedLayout.matches(flag)) {
/*  349 */           this.methodAttributes[j][n].add(new DeprecatedAttribute());
/*      */         }
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  355 */     int backwardsCallIndex = parseMethodMetadataBands(in, this.methodAttrCalls);
/*  356 */     int limit = this.options.hasMethodFlagsHi() ? 62 : 31;
/*  357 */     AttributeLayout[] otherLayouts = new AttributeLayout[limit + 1];
/*  358 */     int[] counts = new int[limit + 1];
/*  359 */     for (int k = 0; k < limit; k++) {
/*  360 */       AttributeLayout layout = this.attrMap.getAttributeLayout(k, 2);
/*  361 */       if (layout != null && !layout.isDefaultLayout()) {
/*  362 */         otherLayouts[k] = layout;
/*  363 */         counts[k] = SegmentUtils.countMatches(this.methodFlags, layout);
/*      */       } 
/*      */     } 
/*  366 */     List[] arrayOfList = new List[limit + 1]; int m;
/*  367 */     for (m = 0; m < counts.length; m++) {
/*  368 */       if (counts[m] > 0) {
/*  369 */         NewAttributeBands bands = this.attrMap.getAttributeBands(otherLayouts[m]);
/*  370 */         arrayOfList[m] = bands.parseAttributes(in, counts[m]);
/*  371 */         int numBackwardsCallables = otherLayouts[m].numBackwardsCallables();
/*  372 */         if (numBackwardsCallables > 0) {
/*  373 */           int[] backwardsCalls = new int[numBackwardsCallables];
/*  374 */           System.arraycopy(this.methodAttrCalls, backwardsCallIndex, backwardsCalls, 0, numBackwardsCallables);
/*  375 */           bands.setBackwardsCalls(backwardsCalls);
/*  376 */           backwardsCallIndex += numBackwardsCallables;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  382 */     for (m = 0; m < this.methodAttributes.length; m++) {
/*  383 */       for (int n = 0; n < (this.methodAttributes[m]).length; n++) {
/*  384 */         long flag = this.methodFlags[m][n];
/*  385 */         int othersAddedAtStart = 0;
/*  386 */         for (int i1 = 0; i1 < otherLayouts.length; i1++) {
/*  387 */           if (otherLayouts[i1] != null && otherLayouts[i1].matches(flag)) {
/*      */             
/*  389 */             if (otherLayouts[i1].getIndex() < 15) {
/*  390 */               this.methodAttributes[m][n].add(othersAddedAtStart++, arrayOfList[i1].get(0));
/*      */             } else {
/*  392 */               this.methodAttributes[m][n].add(arrayOfList[i1].get(0));
/*      */             } 
/*  394 */             arrayOfList[i1].remove(0);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private int getCallCount(int[][] methodAttrIndexes, long[][] flags, int context) {
/*  402 */     int callCount = 0;
/*  403 */     for (int i = 0; i < methodAttrIndexes.length; i++) {
/*  404 */       for (int k = 0; k < (methodAttrIndexes[i]).length; k++) {
/*  405 */         int index = methodAttrIndexes[i][k];
/*  406 */         AttributeLayout layout = this.attrMap.getAttributeLayout(index, context);
/*  407 */         callCount += layout.numBackwardsCallables();
/*      */       } 
/*      */     } 
/*  410 */     int layoutsUsed = 0; int j;
/*  411 */     for (j = 0; j < flags.length; j++) {
/*  412 */       for (int k = 0; k < (flags[j]).length; k++) {
/*  413 */         layoutsUsed = (int)(layoutsUsed | flags[j][k]);
/*      */       }
/*      */     } 
/*  416 */     for (j = 0; j < 26; j++) {
/*  417 */       if ((layoutsUsed & 1 << j) != 0) {
/*  418 */         AttributeLayout layout = this.attrMap.getAttributeLayout(j, context);
/*  419 */         callCount += layout.numBackwardsCallables();
/*      */       } 
/*      */     } 
/*  422 */     return callCount;
/*      */   }
/*      */   
/*      */   private void parseClassAttrBands(InputStream in) throws IOException, Pack200Exception {
/*  426 */     String[] cpUTF8 = this.cpBands.getCpUTF8();
/*  427 */     String[] cpClass = this.cpBands.getCpClass();
/*      */ 
/*      */     
/*  430 */     this.classAttributes = (ArrayList<Attribute>[])new ArrayList[this.classCount];
/*  431 */     Arrays.setAll(this.classAttributes, i -> new ArrayList());
/*      */     
/*  433 */     this.classFlags = parseFlags("class_flags", in, this.classCount, Codec.UNSIGNED5, this.options.hasClassFlagsHi());
/*  434 */     int classAttrCount = SegmentUtils.countBit16(this.classFlags);
/*  435 */     int[] classAttrCounts = decodeBandInt("class_attr_count", in, Codec.UNSIGNED5, classAttrCount);
/*  436 */     int[][] classAttrIndexes = decodeBandInt("class_attr_indexes", in, Codec.UNSIGNED5, classAttrCounts);
/*  437 */     int callCount = getCallCount(classAttrIndexes, new long[][] { this.classFlags }, 0);
/*  438 */     int[] classAttrCalls = decodeBandInt("class_attr_calls", in, Codec.UNSIGNED5, callCount);
/*      */     
/*  440 */     AttributeLayout deprecatedLayout = this.attrMap.getAttributeLayout("Deprecated", 0);
/*      */ 
/*      */     
/*  443 */     AttributeLayout sourceFileLayout = this.attrMap.getAttributeLayout("SourceFile", 0);
/*      */     
/*  445 */     int sourceFileCount = SegmentUtils.countMatches(this.classFlags, sourceFileLayout);
/*  446 */     int[] classSourceFile = decodeBandInt("class_SourceFile_RUN", in, Codec.UNSIGNED5, sourceFileCount);
/*      */ 
/*      */     
/*  449 */     AttributeLayout enclosingMethodLayout = this.attrMap.getAttributeLayout("EnclosingMethod", 0);
/*  450 */     int enclosingMethodCount = SegmentUtils.countMatches(this.classFlags, enclosingMethodLayout);
/*  451 */     int[] enclosingMethodRC = decodeBandInt("class_EnclosingMethod_RC", in, Codec.UNSIGNED5, enclosingMethodCount);
/*      */     
/*  453 */     int[] enclosingMethodRDN = decodeBandInt("class_EnclosingMethod_RDN", in, Codec.UNSIGNED5, enclosingMethodCount);
/*      */ 
/*      */     
/*  456 */     AttributeLayout signatureLayout = this.attrMap.getAttributeLayout("Signature", 0);
/*      */     
/*  458 */     int signatureCount = SegmentUtils.countMatches(this.classFlags, signatureLayout);
/*  459 */     int[] classSignature = decodeBandInt("class_Signature_RS", in, Codec.UNSIGNED5, signatureCount);
/*      */     
/*  461 */     int backwardsCallsUsed = parseClassMetadataBands(in, classAttrCalls);
/*      */     
/*  463 */     AttributeLayout innerClassLayout = this.attrMap.getAttributeLayout("InnerClasses", 0);
/*      */     
/*  465 */     int innerClassCount = SegmentUtils.countMatches(this.classFlags, innerClassLayout);
/*  466 */     int[] classInnerClassesN = decodeBandInt("class_InnerClasses_N", in, Codec.UNSIGNED5, innerClassCount);
/*  467 */     int[][] classInnerClassesRC = decodeBandInt("class_InnerClasses_RC", in, Codec.UNSIGNED5, classInnerClassesN);
/*      */     
/*  469 */     int[][] classInnerClassesF = decodeBandInt("class_InnerClasses_F", in, Codec.UNSIGNED5, classInnerClassesN);
/*      */     
/*  471 */     int flagsCount = 0;
/*  472 */     for (int i = 0; i < classInnerClassesF.length; i++) {
/*  473 */       for (int m = 0; m < (classInnerClassesF[i]).length; m++) {
/*  474 */         if (classInnerClassesF[i][m] != 0) {
/*  475 */           flagsCount++;
/*      */         }
/*      */       } 
/*      */     } 
/*  479 */     int[] classInnerClassesOuterRCN = decodeBandInt("class_InnerClasses_outer_RCN", in, Codec.UNSIGNED5, flagsCount);
/*      */     
/*  481 */     int[] classInnerClassesNameRUN = decodeBandInt("class_InnerClasses_name_RUN", in, Codec.UNSIGNED5, flagsCount);
/*      */ 
/*      */     
/*  484 */     AttributeLayout versionLayout = this.attrMap.getAttributeLayout("class-file version", 0);
/*      */     
/*  486 */     int versionCount = SegmentUtils.countMatches(this.classFlags, versionLayout);
/*  487 */     int[] classFileVersionMinorH = decodeBandInt("class_file_version_minor_H", in, Codec.UNSIGNED5, versionCount);
/*      */     
/*  489 */     int[] classFileVersionMajorH = decodeBandInt("class_file_version_major_H", in, Codec.UNSIGNED5, versionCount);
/*      */     
/*  491 */     if (versionCount > 0) {
/*  492 */       this.classVersionMajor = new int[this.classCount];
/*  493 */       this.classVersionMinor = new int[this.classCount];
/*      */     } 
/*  495 */     int defaultVersionMajor = this.header.getDefaultClassMajorVersion();
/*  496 */     int defaultVersionMinor = this.header.getDefaultClassMinorVersion();
/*      */ 
/*      */     
/*  499 */     int backwardsCallIndex = backwardsCallsUsed;
/*  500 */     int limit = this.options.hasClassFlagsHi() ? 62 : 31;
/*  501 */     AttributeLayout[] otherLayouts = new AttributeLayout[limit + 1];
/*  502 */     int[] counts = new int[limit + 1];
/*  503 */     List[] arrayOfList = new List[limit + 1]; int j;
/*  504 */     for (j = 0; j < limit; j++) {
/*  505 */       AttributeLayout layout = this.attrMap.getAttributeLayout(j, 0);
/*  506 */       if (layout != null && !layout.isDefaultLayout()) {
/*  507 */         otherLayouts[j] = layout;
/*  508 */         counts[j] = SegmentUtils.countMatches(this.classFlags, layout);
/*      */       } 
/*      */     } 
/*  511 */     for (j = 0; j < counts.length; j++) {
/*  512 */       if (counts[j] > 0) {
/*  513 */         NewAttributeBands bands = this.attrMap.getAttributeBands(otherLayouts[j]);
/*  514 */         arrayOfList[j] = bands.parseAttributes(in, counts[j]);
/*  515 */         int numBackwardsCallables = otherLayouts[j].numBackwardsCallables();
/*  516 */         if (numBackwardsCallables > 0) {
/*  517 */           int[] backwardsCalls = new int[numBackwardsCallables];
/*  518 */           System.arraycopy(classAttrCalls, backwardsCallIndex, backwardsCalls, 0, numBackwardsCallables);
/*  519 */           bands.setBackwardsCalls(backwardsCalls);
/*  520 */           backwardsCallIndex += numBackwardsCallables;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  526 */     int sourceFileIndex = 0;
/*  527 */     int enclosingMethodIndex = 0;
/*  528 */     int signatureIndex = 0;
/*  529 */     int innerClassIndex = 0;
/*  530 */     int innerClassC2NIndex = 0;
/*  531 */     int versionIndex = 0;
/*  532 */     this.icLocal = new IcTuple[this.classCount][];
/*  533 */     for (int k = 0; k < this.classCount; k++) {
/*  534 */       long flag = this.classFlags[k];
/*  535 */       if (deprecatedLayout.matches(this.classFlags[k])) {
/*  536 */         this.classAttributes[k].add(new DeprecatedAttribute());
/*      */       }
/*  538 */       if (sourceFileLayout.matches(flag)) {
/*  539 */         CPUTF8 cPUTF8; long result = classSourceFile[sourceFileIndex];
/*  540 */         ClassFileEntry value = sourceFileLayout.getValue(result, this.cpBands.getConstantPool());
/*  541 */         if (value == null) {
/*      */           
/*  543 */           String className = this.classThis[k].substring(this.classThis[k].lastIndexOf('/') + 1);
/*  544 */           className = className.substring(className.lastIndexOf('.') + 1);
/*      */ 
/*      */           
/*  547 */           char[] chars = className.toCharArray();
/*  548 */           int index = -1;
/*  549 */           for (int n = 0; n < chars.length; n++) {
/*  550 */             if (chars[n] <= '-') {
/*  551 */               index = n;
/*      */               break;
/*      */             } 
/*      */           } 
/*  555 */           if (index > -1) {
/*  556 */             className = className.substring(0, index);
/*      */           }
/*      */           
/*  559 */           cPUTF8 = this.cpBands.cpUTF8Value(className + ".java", true);
/*      */         } 
/*  561 */         this.classAttributes[k].add(new SourceFileAttribute(cPUTF8));
/*  562 */         sourceFileIndex++;
/*      */       } 
/*  564 */       if (enclosingMethodLayout.matches(flag)) {
/*  565 */         CPClass theClass = this.cpBands.cpClassValue(enclosingMethodRC[enclosingMethodIndex]);
/*  566 */         CPNameAndType theMethod = null;
/*  567 */         if (enclosingMethodRDN[enclosingMethodIndex] != 0) {
/*  568 */           theMethod = this.cpBands.cpNameAndTypeValue(enclosingMethodRDN[enclosingMethodIndex] - 1);
/*      */         }
/*  570 */         this.classAttributes[k].add(new EnclosingMethodAttribute(theClass, theMethod));
/*  571 */         enclosingMethodIndex++;
/*      */       } 
/*  573 */       if (signatureLayout.matches(flag)) {
/*  574 */         long result = classSignature[signatureIndex];
/*  575 */         CPUTF8 value = (CPUTF8)signatureLayout.getValue(result, this.cpBands.getConstantPool());
/*  576 */         this.classAttributes[k].add(new SignatureAttribute(value));
/*  577 */         signatureIndex++;
/*      */       } 
/*  579 */       if (innerClassLayout.matches(flag)) {
/*      */ 
/*      */         
/*  582 */         this.icLocal[k] = new IcTuple[classInnerClassesN[innerClassIndex]];
/*  583 */         for (int n = 0; n < (this.icLocal[k]).length; n++) {
/*  584 */           int icTupleCIndex = classInnerClassesRC[innerClassIndex][n];
/*  585 */           int icTupleC2Index = -1;
/*  586 */           int icTupleNIndex = -1;
/*      */           
/*  588 */           String icTupleC = cpClass[icTupleCIndex];
/*  589 */           int icTupleF = classInnerClassesF[innerClassIndex][n];
/*  590 */           String icTupleC2 = null;
/*  591 */           String icTupleN = null;
/*      */           
/*  593 */           if (icTupleF != 0) {
/*  594 */             icTupleC2Index = classInnerClassesOuterRCN[innerClassC2NIndex];
/*  595 */             icTupleNIndex = classInnerClassesNameRUN[innerClassC2NIndex];
/*  596 */             icTupleC2 = cpClass[icTupleC2Index];
/*  597 */             icTupleN = cpUTF8[icTupleNIndex];
/*  598 */             innerClassC2NIndex++;
/*      */           } else {
/*      */             
/*  601 */             IcBands icBands = this.segment.getIcBands();
/*  602 */             IcTuple[] icAll = icBands.getIcTuples();
/*  603 */             for (int i1 = 0; i1 < icAll.length; i1++) {
/*  604 */               if (icAll[i1].getC().equals(icTupleC)) {
/*  605 */                 icTupleF = icAll[i1].getF();
/*  606 */                 icTupleC2 = icAll[i1].getC2();
/*  607 */                 icTupleN = icAll[i1].getN();
/*      */                 
/*      */                 break;
/*      */               } 
/*      */             } 
/*      */           } 
/*  613 */           IcTuple icTuple = new IcTuple(icTupleC, icTupleF, icTupleC2, icTupleN, icTupleCIndex, icTupleC2Index, icTupleNIndex, n);
/*      */           
/*  615 */           this.icLocal[k][n] = icTuple;
/*      */         } 
/*  617 */         innerClassIndex++;
/*      */       } 
/*  619 */       if (versionLayout.matches(flag)) {
/*  620 */         this.classVersionMajor[k] = classFileVersionMajorH[versionIndex];
/*  621 */         this.classVersionMinor[k] = classFileVersionMinorH[versionIndex];
/*  622 */         versionIndex++;
/*  623 */       } else if (this.classVersionMajor != null) {
/*      */         
/*  625 */         this.classVersionMajor[k] = defaultVersionMajor;
/*  626 */         this.classVersionMinor[k] = defaultVersionMinor;
/*      */       } 
/*      */       
/*  629 */       for (int m = 0; m < otherLayouts.length; m++) {
/*  630 */         if (otherLayouts[m] != null && otherLayouts[m].matches(flag)) {
/*      */           
/*  632 */           this.classAttributes[k].add(arrayOfList[m].get(0));
/*  633 */           arrayOfList[m].remove(0);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void parseCodeBands(InputStream in) throws Pack200Exception, IOException {
/*  640 */     AttributeLayout layout = this.attrMap.getAttributeLayout("Code", 2);
/*      */ 
/*      */     
/*  643 */     int codeCount = SegmentUtils.countMatches(this.methodFlags, layout);
/*  644 */     int[] codeHeaders = decodeBandInt("code_headers", in, Codec.BYTE1, codeCount);
/*      */     
/*  646 */     boolean allCodeHasFlags = this.segment.getSegmentHeader().getOptions().hasAllCodeFlags();
/*  647 */     if (!allCodeHasFlags) {
/*  648 */       this.codeHasAttributes = new boolean[codeCount];
/*      */     }
/*  650 */     int codeSpecialHeader = 0;
/*  651 */     for (int i = 0; i < codeCount; i++) {
/*  652 */       if (codeHeaders[i] == 0) {
/*  653 */         codeSpecialHeader++;
/*  654 */         if (!allCodeHasFlags) {
/*  655 */           this.codeHasAttributes[i] = true;
/*      */         }
/*      */       } 
/*      */     } 
/*  659 */     int[] codeMaxStackSpecials = decodeBandInt("code_max_stack", in, Codec.UNSIGNED5, codeSpecialHeader);
/*  660 */     int[] codeMaxNALocalsSpecials = decodeBandInt("code_max_na_locals", in, Codec.UNSIGNED5, codeSpecialHeader);
/*      */     
/*  662 */     int[] codeHandlerCountSpecials = decodeBandInt("code_handler_count", in, Codec.UNSIGNED5, codeSpecialHeader);
/*      */ 
/*      */     
/*  665 */     this.codeMaxStack = new int[codeCount];
/*  666 */     this.codeMaxNALocals = new int[codeCount];
/*  667 */     this.codeHandlerCount = new int[codeCount];
/*  668 */     int special = 0;
/*  669 */     for (int j = 0; j < codeCount; j++) {
/*  670 */       int header = 0xFF & codeHeaders[j];
/*  671 */       if (header < 0) {
/*  672 */         throw new IllegalStateException("Shouldn't get here");
/*      */       }
/*  674 */       if (header == 0) {
/*  675 */         this.codeMaxStack[j] = codeMaxStackSpecials[special];
/*  676 */         this.codeMaxNALocals[j] = codeMaxNALocalsSpecials[special];
/*  677 */         this.codeHandlerCount[j] = codeHandlerCountSpecials[special];
/*  678 */         special++;
/*  679 */       } else if (header <= 144) {
/*  680 */         this.codeMaxStack[j] = (header - 1) % 12;
/*  681 */         this.codeMaxNALocals[j] = (header - 1) / 12;
/*  682 */         this.codeHandlerCount[j] = 0;
/*  683 */       } else if (header <= 208) {
/*  684 */         this.codeMaxStack[j] = (header - 145) % 8;
/*  685 */         this.codeMaxNALocals[j] = (header - 145) / 8;
/*  686 */         this.codeHandlerCount[j] = 1;
/*  687 */       } else if (header <= 255) {
/*  688 */         this.codeMaxStack[j] = (header - 209) % 7;
/*  689 */         this.codeMaxNALocals[j] = (header - 209) / 7;
/*  690 */         this.codeHandlerCount[j] = 2;
/*      */       } else {
/*  692 */         throw new IllegalStateException("Shouldn't get here either");
/*      */       } 
/*      */     } 
/*  695 */     this.codeHandlerStartP = decodeBandInt("code_handler_start_P", in, Codec.BCI5, this.codeHandlerCount);
/*  696 */     this.codeHandlerEndPO = decodeBandInt("code_handler_end_PO", in, Codec.BRANCH5, this.codeHandlerCount);
/*  697 */     this.codeHandlerCatchPO = decodeBandInt("code_handler_catch_PO", in, Codec.BRANCH5, this.codeHandlerCount);
/*  698 */     this.codeHandlerClassRCN = decodeBandInt("code_handler_class_RCN", in, Codec.UNSIGNED5, this.codeHandlerCount);
/*      */     
/*  700 */     int codeFlagsCount = allCodeHasFlags ? codeCount : codeSpecialHeader;
/*      */     
/*  702 */     this.codeAttributes = (List<Attribute>[])new List[codeFlagsCount];
/*  703 */     Arrays.setAll(this.codeAttributes, i -> new ArrayList());
/*  704 */     parseCodeAttrBands(in, codeFlagsCount);
/*      */   }
/*      */ 
/*      */   
/*      */   private void parseCodeAttrBands(InputStream in, int codeFlagsCount) throws IOException, Pack200Exception {
/*  709 */     long[] codeFlags = parseFlags("code_flags", in, codeFlagsCount, Codec.UNSIGNED5, this.segment
/*  710 */         .getSegmentHeader().getOptions().hasCodeFlagsHi());
/*  711 */     int codeAttrCount = SegmentUtils.countBit16(codeFlags);
/*  712 */     int[] codeAttrCounts = decodeBandInt("code_attr_count", in, Codec.UNSIGNED5, codeAttrCount);
/*  713 */     int[][] codeAttrIndexes = decodeBandInt("code_attr_indexes", in, Codec.UNSIGNED5, codeAttrCounts);
/*  714 */     int callCount = 0;
/*  715 */     for (int i = 0; i < codeAttrIndexes.length; i++) {
/*  716 */       for (int m = 0; m < (codeAttrIndexes[i]).length; m++) {
/*  717 */         int index = codeAttrIndexes[i][m];
/*  718 */         AttributeLayout layout = this.attrMap.getAttributeLayout(index, 3);
/*  719 */         callCount += layout.numBackwardsCallables();
/*      */       } 
/*      */     } 
/*  722 */     int[] codeAttrCalls = decodeBandInt("code_attr_calls", in, Codec.UNSIGNED5, callCount);
/*      */ 
/*      */     
/*  725 */     AttributeLayout lineNumberTableLayout = this.attrMap.getAttributeLayout("LineNumberTable", 3);
/*  726 */     int lineNumberTableCount = SegmentUtils.countMatches(codeFlags, lineNumberTableLayout);
/*  727 */     int[] lineNumberTableN = decodeBandInt("code_LineNumberTable_N", in, Codec.UNSIGNED5, lineNumberTableCount);
/*      */     
/*  729 */     int[][] lineNumberTableBciP = decodeBandInt("code_LineNumberTable_bci_P", in, Codec.BCI5, lineNumberTableN);
/*      */     
/*  731 */     int[][] lineNumberTableLine = decodeBandInt("code_LineNumberTable_line", in, Codec.UNSIGNED5, lineNumberTableN);
/*      */ 
/*      */ 
/*      */     
/*  735 */     AttributeLayout localVariableTableLayout = this.attrMap.getAttributeLayout("LocalVariableTable", 3);
/*      */     
/*  737 */     AttributeLayout localVariableTypeTableLayout = this.attrMap.getAttributeLayout("LocalVariableTypeTable", 3);
/*      */     
/*  739 */     int lengthLocalVariableNBand = SegmentUtils.countMatches(codeFlags, localVariableTableLayout);
/*  740 */     int[] localVariableTableN = decodeBandInt("code_LocalVariableTable_N", in, Codec.UNSIGNED5, lengthLocalVariableNBand);
/*      */     
/*  742 */     int[][] localVariableTableBciP = decodeBandInt("code_LocalVariableTable_bci_P", in, Codec.BCI5, localVariableTableN);
/*      */     
/*  744 */     int[][] localVariableTableSpanO = decodeBandInt("code_LocalVariableTable_span_O", in, Codec.BRANCH5, localVariableTableN);
/*      */     
/*  746 */     CPUTF8[][] localVariableTableNameRU = parseCPUTF8References("code_LocalVariableTable_name_RU", in, Codec.UNSIGNED5, localVariableTableN);
/*      */     
/*  748 */     CPUTF8[][] localVariableTableTypeRS = parseCPSignatureReferences("code_LocalVariableTable_type_RS", in, Codec.UNSIGNED5, localVariableTableN);
/*      */     
/*  750 */     int[][] localVariableTableSlot = decodeBandInt("code_LocalVariableTable_slot", in, Codec.UNSIGNED5, localVariableTableN);
/*      */ 
/*      */     
/*  753 */     int lengthLocalVariableTypeTableNBand = SegmentUtils.countMatches(codeFlags, localVariableTypeTableLayout);
/*      */     
/*  755 */     int[] localVariableTypeTableN = decodeBandInt("code_LocalVariableTypeTable_N", in, Codec.UNSIGNED5, lengthLocalVariableTypeTableNBand);
/*      */     
/*  757 */     int[][] localVariableTypeTableBciP = decodeBandInt("code_LocalVariableTypeTable_bci_P", in, Codec.BCI5, localVariableTypeTableN);
/*      */     
/*  759 */     int[][] localVariableTypeTableSpanO = decodeBandInt("code_LocalVariableTypeTable_span_O", in, Codec.BRANCH5, localVariableTypeTableN);
/*      */     
/*  761 */     CPUTF8[][] localVariableTypeTableNameRU = parseCPUTF8References("code_LocalVariableTypeTable_name_RU", in, Codec.UNSIGNED5, localVariableTypeTableN);
/*      */     
/*  763 */     CPUTF8[][] localVariableTypeTableTypeRS = parseCPSignatureReferences("code_LocalVariableTypeTable_type_RS", in, Codec.UNSIGNED5, localVariableTypeTableN);
/*      */     
/*  765 */     int[][] localVariableTypeTableSlot = decodeBandInt("code_LocalVariableTypeTable_slot", in, Codec.UNSIGNED5, localVariableTypeTableN);
/*      */ 
/*      */ 
/*      */     
/*  769 */     int backwardsCallIndex = 0;
/*  770 */     int limit = this.options.hasCodeFlagsHi() ? 62 : 31;
/*  771 */     AttributeLayout[] otherLayouts = new AttributeLayout[limit + 1];
/*  772 */     int[] counts = new int[limit + 1];
/*  773 */     List[] arrayOfList = new List[limit + 1]; int j;
/*  774 */     for (j = 0; j < limit; j++) {
/*  775 */       AttributeLayout layout = this.attrMap.getAttributeLayout(j, 3);
/*  776 */       if (layout != null && !layout.isDefaultLayout()) {
/*  777 */         otherLayouts[j] = layout;
/*  778 */         counts[j] = SegmentUtils.countMatches(codeFlags, layout);
/*      */       } 
/*      */     } 
/*  781 */     for (j = 0; j < counts.length; j++) {
/*  782 */       if (counts[j] > 0) {
/*  783 */         NewAttributeBands bands = this.attrMap.getAttributeBands(otherLayouts[j]);
/*  784 */         arrayOfList[j] = bands.parseAttributes(in, counts[j]);
/*  785 */         int numBackwardsCallables = otherLayouts[j].numBackwardsCallables();
/*  786 */         if (numBackwardsCallables > 0) {
/*  787 */           int[] backwardsCalls = new int[numBackwardsCallables];
/*  788 */           System.arraycopy(codeAttrCalls, backwardsCallIndex, backwardsCalls, 0, numBackwardsCallables);
/*  789 */           bands.setBackwardsCalls(backwardsCalls);
/*  790 */           backwardsCallIndex += numBackwardsCallables;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  795 */     int lineNumberIndex = 0;
/*  796 */     int lvtIndex = 0;
/*  797 */     int lvttIndex = 0;
/*  798 */     for (int k = 0; k < codeFlagsCount; k++) {
/*  799 */       if (lineNumberTableLayout.matches(codeFlags[k])) {
/*  800 */         LineNumberTableAttribute lnta = new LineNumberTableAttribute(lineNumberTableN[lineNumberIndex], lineNumberTableBciP[lineNumberIndex], lineNumberTableLine[lineNumberIndex]);
/*      */         
/*  802 */         lineNumberIndex++;
/*  803 */         this.codeAttributes[k].add(lnta);
/*      */       } 
/*  805 */       if (localVariableTableLayout.matches(codeFlags[k])) {
/*  806 */         LocalVariableTableAttribute lvta = new LocalVariableTableAttribute(localVariableTableN[lvtIndex], localVariableTableBciP[lvtIndex], localVariableTableSpanO[lvtIndex], localVariableTableNameRU[lvtIndex], localVariableTableTypeRS[lvtIndex], localVariableTableSlot[lvtIndex]);
/*      */ 
/*      */ 
/*      */         
/*  810 */         lvtIndex++;
/*  811 */         this.codeAttributes[k].add(lvta);
/*      */       } 
/*  813 */       if (localVariableTypeTableLayout.matches(codeFlags[k])) {
/*  814 */         LocalVariableTypeTableAttribute lvtta = new LocalVariableTypeTableAttribute(localVariableTypeTableN[lvttIndex], localVariableTypeTableBciP[lvttIndex], localVariableTypeTableSpanO[lvttIndex], localVariableTypeTableNameRU[lvttIndex], localVariableTypeTableTypeRS[lvttIndex], localVariableTypeTableSlot[lvttIndex]);
/*      */ 
/*      */ 
/*      */         
/*  818 */         lvttIndex++;
/*  819 */         this.codeAttributes[k].add(lvtta);
/*      */       } 
/*      */       
/*  822 */       for (int m = 0; m < otherLayouts.length; m++) {
/*  823 */         if (otherLayouts[m] != null && otherLayouts[m].matches(codeFlags[k])) {
/*      */           
/*  825 */           this.codeAttributes[k].add(arrayOfList[m].get(0));
/*  826 */           arrayOfList[m].remove(0);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private int parseFieldMetadataBands(InputStream in, int[] fieldAttrCalls) throws Pack200Exception, IOException {
/*  835 */     int backwardsCallsUsed = 0;
/*  836 */     String[] RxA = { "RVA", "RIA" };
/*      */ 
/*      */     
/*  839 */     AttributeLayout rvaLayout = this.attrMap.getAttributeLayout("RuntimeVisibleAnnotations", 1);
/*      */     
/*  841 */     AttributeLayout riaLayout = this.attrMap.getAttributeLayout("RuntimeInvisibleAnnotations", 1);
/*      */     
/*  843 */     int rvaCount = SegmentUtils.countMatches(this.fieldFlags, rvaLayout);
/*  844 */     int riaCount = SegmentUtils.countMatches(this.fieldFlags, riaLayout);
/*  845 */     int[] RxACount = { rvaCount, riaCount };
/*  846 */     int[] backwardsCalls = { 0, 0 };
/*  847 */     if (rvaCount > 0) {
/*  848 */       backwardsCalls[0] = fieldAttrCalls[0];
/*  849 */       backwardsCallsUsed++;
/*  850 */       if (riaCount > 0) {
/*  851 */         backwardsCalls[1] = fieldAttrCalls[1];
/*  852 */         backwardsCallsUsed++;
/*      */       } 
/*  854 */     } else if (riaCount > 0) {
/*  855 */       backwardsCalls[1] = fieldAttrCalls[0];
/*  856 */       backwardsCallsUsed++;
/*      */     } 
/*  858 */     MetadataBandGroup[] mb = parseMetadata(in, RxA, RxACount, backwardsCalls, "field");
/*  859 */     List<Attribute> rvaAttributes = mb[0].getAttributes();
/*  860 */     List<Attribute> riaAttributes = mb[1].getAttributes();
/*  861 */     int rvaAttributesIndex = 0;
/*  862 */     int riaAttributesIndex = 0;
/*  863 */     for (int i = 0; i < this.fieldFlags.length; i++) {
/*  864 */       for (int j = 0; j < (this.fieldFlags[i]).length; j++) {
/*  865 */         if (rvaLayout.matches(this.fieldFlags[i][j])) {
/*  866 */           this.fieldAttributes[i][j].add(rvaAttributes.get(rvaAttributesIndex++));
/*      */         }
/*  868 */         if (riaLayout.matches(this.fieldFlags[i][j])) {
/*  869 */           this.fieldAttributes[i][j].add(riaAttributes.get(riaAttributesIndex++));
/*      */         }
/*      */       } 
/*      */     } 
/*  873 */     return backwardsCallsUsed;
/*      */   }
/*      */ 
/*      */   
/*      */   private MetadataBandGroup[] parseMetadata(InputStream in, String[] RxA, int[] RxACount, int[] backwardsCallCounts, String contextName) throws IOException, Pack200Exception {
/*  878 */     MetadataBandGroup[] mbg = new MetadataBandGroup[RxA.length];
/*  879 */     for (int i = 0; i < RxA.length; i++) {
/*  880 */       mbg[i] = new MetadataBandGroup(RxA[i], this.cpBands);
/*  881 */       String rxa = RxA[i];
/*  882 */       if (rxa.indexOf('P') >= 0) {
/*  883 */         (mbg[i]).param_NB = decodeBandInt(contextName + "_" + rxa + "_param_NB", in, Codec.BYTE1, RxACount[i]);
/*      */       }
/*  885 */       int pairCount = 0;
/*  886 */       if (!rxa.equals("AD")) {
/*  887 */         (mbg[i]).anno_N = decodeBandInt(contextName + "_" + rxa + "_anno_N", in, Codec.UNSIGNED5, RxACount[i]);
/*  888 */         (mbg[i]).type_RS = parseCPSignatureReferences(contextName + "_" + rxa + "_type_RS", in, Codec.UNSIGNED5, (mbg[i]).anno_N);
/*      */         
/*  890 */         (mbg[i]).pair_N = decodeBandInt(contextName + "_" + rxa + "_pair_N", in, Codec.UNSIGNED5, (mbg[i]).anno_N);
/*  891 */         for (int m = 0; m < (mbg[i]).pair_N.length; m++) {
/*  892 */           for (int n = 0; n < ((mbg[i]).pair_N[m]).length; n++) {
/*  893 */             pairCount += (mbg[i]).pair_N[m][n];
/*      */           }
/*      */         } 
/*      */         
/*  897 */         (mbg[i]).name_RU = parseCPUTF8References(contextName + "_" + rxa + "_name_RU", in, Codec.UNSIGNED5, pairCount);
/*      */       } else {
/*      */         
/*  900 */         pairCount = RxACount[i];
/*      */       } 
/*  902 */       (mbg[i]).T = decodeBandInt(contextName + "_" + rxa + "_T", in, Codec.BYTE1, pairCount + backwardsCallCounts[i]);
/*      */       
/*  904 */       int ICount = 0, DCount = 0, FCount = 0, JCount = 0, cCount = 0, eCount = 0, sCount = 0, arrayCount = 0;
/*  905 */       int atCount = 0;
/*  906 */       for (int j = 0; j < (mbg[i]).T.length; j++) {
/*  907 */         char c = (char)(mbg[i]).T[j];
/*  908 */         switch (c) {
/*      */           case 'B':
/*      */           case 'C':
/*      */           case 'I':
/*      */           case 'S':
/*      */           case 'Z':
/*  914 */             ICount++;
/*      */             break;
/*      */           case 'D':
/*  917 */             DCount++;
/*      */             break;
/*      */           case 'F':
/*  920 */             FCount++;
/*      */             break;
/*      */           case 'J':
/*  923 */             JCount++;
/*      */             break;
/*      */           case 'c':
/*  926 */             cCount++;
/*      */             break;
/*      */           case 'e':
/*  929 */             eCount++;
/*      */             break;
/*      */           case 's':
/*  932 */             sCount++;
/*      */             break;
/*      */           case '[':
/*  935 */             arrayCount++;
/*      */             break;
/*      */           case '@':
/*  938 */             atCount++;
/*      */             break;
/*      */         } 
/*      */       } 
/*  942 */       (mbg[i]).caseI_KI = parseCPIntReferences(contextName + "_" + rxa + "_caseI_KI", in, Codec.UNSIGNED5, ICount);
/*  943 */       (mbg[i]).caseD_KD = parseCPDoubleReferences(contextName + "_" + rxa + "_caseD_KD", in, Codec.UNSIGNED5, DCount);
/*      */       
/*  945 */       (mbg[i]).caseF_KF = parseCPFloatReferences(contextName + "_" + rxa + "_caseF_KF", in, Codec.UNSIGNED5, FCount);
/*      */       
/*  947 */       (mbg[i]).caseJ_KJ = parseCPLongReferences(contextName + "_" + rxa + "_caseJ_KJ", in, Codec.UNSIGNED5, JCount);
/*  948 */       (mbg[i]).casec_RS = parseCPSignatureReferences(contextName + "_" + rxa + "_casec_RS", in, Codec.UNSIGNED5, cCount);
/*      */       
/*  950 */       (mbg[i]).caseet_RS = parseReferences(contextName + "_" + rxa + "_caseet_RS", in, Codec.UNSIGNED5, eCount, this.cpBands
/*  951 */           .getCpSignature());
/*  952 */       (mbg[i]).caseec_RU = parseReferences(contextName + "_" + rxa + "_caseec_RU", in, Codec.UNSIGNED5, eCount, this.cpBands
/*  953 */           .getCpUTF8());
/*  954 */       (mbg[i]).cases_RU = parseCPUTF8References(contextName + "_" + rxa + "_cases_RU", in, Codec.UNSIGNED5, sCount);
/*  955 */       (mbg[i]).casearray_N = decodeBandInt(contextName + "_" + rxa + "_casearray_N", in, Codec.UNSIGNED5, arrayCount);
/*      */       
/*  957 */       (mbg[i]).nesttype_RS = parseCPUTF8References(contextName + "_" + rxa + "_nesttype_RS", in, Codec.UNSIGNED5, atCount);
/*      */       
/*  959 */       (mbg[i]).nestpair_N = decodeBandInt(contextName + "_" + rxa + "_nestpair_N", in, Codec.UNSIGNED5, atCount);
/*  960 */       int nestPairCount = 0;
/*  961 */       for (int k = 0; k < (mbg[i]).nestpair_N.length; k++) {
/*  962 */         nestPairCount += (mbg[i]).nestpair_N[k];
/*      */       }
/*  964 */       (mbg[i]).nestname_RU = parseCPUTF8References(contextName + "_" + rxa + "_nestname_RU", in, Codec.UNSIGNED5, nestPairCount);
/*      */     } 
/*      */     
/*  967 */     return mbg;
/*      */   }
/*      */ 
/*      */   
/*      */   private int parseMethodMetadataBands(InputStream in, int[] methodAttrCalls) throws Pack200Exception, IOException {
/*  972 */     int backwardsCallsUsed = 0;
/*  973 */     String[] RxA = { "RVA", "RIA", "RVPA", "RIPA", "AD" };
/*  974 */     int[] rxaCounts = { 0, 0, 0, 0, 0 };
/*      */ 
/*      */     
/*  977 */     AttributeLayout rvaLayout = this.attrMap.getAttributeLayout("RuntimeVisibleAnnotations", 2);
/*  978 */     AttributeLayout riaLayout = this.attrMap.getAttributeLayout("RuntimeInvisibleAnnotations", 2);
/*      */     
/*  980 */     AttributeLayout rvpaLayout = this.attrMap.getAttributeLayout("RuntimeVisibleParameterAnnotations", 2);
/*      */     
/*  982 */     AttributeLayout ripaLayout = this.attrMap.getAttributeLayout("RuntimeInvisibleParameterAnnotations", 2);
/*      */     
/*  984 */     AttributeLayout adLayout = this.attrMap.getAttributeLayout("AnnotationDefault", 2);
/*      */     
/*  986 */     AttributeLayout[] rxaLayouts = { rvaLayout, riaLayout, rvpaLayout, ripaLayout, adLayout };
/*      */     
/*  988 */     Arrays.setAll(rxaCounts, i -> SegmentUtils.countMatches(this.methodFlags, rxaLayouts[i]));
/*  989 */     int[] backwardsCalls = new int[5];
/*  990 */     int methodAttrIndex = 0;
/*  991 */     for (int i = 0; i < backwardsCalls.length; i++) {
/*  992 */       if (rxaCounts[i] > 0) {
/*  993 */         backwardsCallsUsed++;
/*  994 */         backwardsCalls[i] = methodAttrCalls[methodAttrIndex];
/*  995 */         methodAttrIndex++;
/*      */       } else {
/*  997 */         backwardsCalls[i] = 0;
/*      */       } 
/*      */     } 
/* 1000 */     MetadataBandGroup[] mbgs = parseMetadata(in, RxA, rxaCounts, backwardsCalls, "method");
/* 1001 */     List[] arrayOfList = new List[RxA.length];
/* 1002 */     int[] attributeListIndexes = new int[RxA.length]; int j;
/* 1003 */     for (j = 0; j < mbgs.length; j++) {
/* 1004 */       arrayOfList[j] = mbgs[j].getAttributes();
/* 1005 */       attributeListIndexes[j] = 0;
/*      */     } 
/* 1007 */     for (j = 0; j < this.methodFlags.length; j++) {
/* 1008 */       for (int k = 0; k < (this.methodFlags[j]).length; k++) {
/* 1009 */         for (int m = 0; m < rxaLayouts.length; m++) {
/* 1010 */           if (rxaLayouts[m].matches(this.methodFlags[j][k])) {
/* 1011 */             attributeListIndexes[m] = attributeListIndexes[m] + 1; this.methodAttributes[j][k].add(arrayOfList[m].get(attributeListIndexes[m]));
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/* 1016 */     return backwardsCallsUsed;
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
/*      */   private int parseClassMetadataBands(InputStream in, int[] classAttrCalls) throws Pack200Exception, IOException {
/* 1030 */     int numBackwardsCalls = 0;
/* 1031 */     String[] RxA = { "RVA", "RIA" };
/*      */ 
/*      */     
/* 1034 */     AttributeLayout rvaLayout = this.attrMap.getAttributeLayout("RuntimeVisibleAnnotations", 0);
/*      */     
/* 1036 */     AttributeLayout riaLayout = this.attrMap.getAttributeLayout("RuntimeInvisibleAnnotations", 0);
/* 1037 */     int rvaCount = SegmentUtils.countMatches(this.classFlags, rvaLayout);
/* 1038 */     int riaCount = SegmentUtils.countMatches(this.classFlags, riaLayout);
/* 1039 */     int[] RxACount = { rvaCount, riaCount };
/* 1040 */     int[] backwardsCalls = { 0, 0 };
/* 1041 */     if (rvaCount > 0) {
/* 1042 */       numBackwardsCalls++;
/* 1043 */       backwardsCalls[0] = classAttrCalls[0];
/* 1044 */       if (riaCount > 0) {
/* 1045 */         numBackwardsCalls++;
/* 1046 */         backwardsCalls[1] = classAttrCalls[1];
/*      */       } 
/* 1048 */     } else if (riaCount > 0) {
/* 1049 */       numBackwardsCalls++;
/* 1050 */       backwardsCalls[1] = classAttrCalls[0];
/*      */     } 
/* 1052 */     MetadataBandGroup[] mbgs = parseMetadata(in, RxA, RxACount, backwardsCalls, "class");
/* 1053 */     List<Attribute> rvaAttributes = mbgs[0].getAttributes();
/* 1054 */     List<Attribute> riaAttributes = mbgs[1].getAttributes();
/* 1055 */     int rvaAttributesIndex = 0;
/* 1056 */     int riaAttributesIndex = 0;
/* 1057 */     for (int i = 0; i < this.classFlags.length; i++) {
/* 1058 */       if (rvaLayout.matches(this.classFlags[i])) {
/* 1059 */         this.classAttributes[i].add(rvaAttributes.get(rvaAttributesIndex++));
/*      */       }
/* 1061 */       if (riaLayout.matches(this.classFlags[i])) {
/* 1062 */         this.classAttributes[i].add(riaAttributes.get(riaAttributesIndex++));
/*      */       }
/*      */     } 
/* 1065 */     return numBackwardsCalls;
/*      */   }
/*      */   
/*      */   public ArrayList<Attribute>[] getClassAttributes() {
/* 1069 */     return this.classAttributes;
/*      */   }
/*      */   
/*      */   public int[] getClassFieldCount() {
/* 1073 */     return this.classFieldCount;
/*      */   }
/*      */   
/*      */   public long[] getRawClassFlags() {
/* 1077 */     return this.classFlags;
/*      */   }
/*      */   
/*      */   public long[] getClassFlags() {
/* 1081 */     if (this.classAccessFlags == null) {
/* 1082 */       long mask = 32767L; int i;
/* 1083 */       for (i = 0; i < 16; i++) {
/* 1084 */         AttributeLayout layout = this.attrMap.getAttributeLayout(i, 0);
/* 1085 */         if (layout != null && !layout.isDefaultLayout()) {
/* 1086 */           mask &= (1 << i ^ 0xFFFFFFFF);
/*      */         }
/*      */       } 
/* 1089 */       this.classAccessFlags = new long[this.classFlags.length];
/* 1090 */       for (i = 0; i < this.classFlags.length; i++) {
/* 1091 */         this.classAccessFlags[i] = this.classFlags[i] & mask;
/*      */       }
/*      */     } 
/* 1094 */     return this.classAccessFlags;
/*      */   }
/*      */   
/*      */   public int[][] getClassInterfacesInts() {
/* 1098 */     return this.classInterfacesInts;
/*      */   }
/*      */   
/*      */   public int[] getClassMethodCount() {
/* 1102 */     return this.classMethodCount;
/*      */   }
/*      */   
/*      */   public int[] getClassSuperInts() {
/* 1106 */     return this.classSuperInts;
/*      */   }
/*      */   
/*      */   public int[] getClassThisInts() {
/* 1110 */     return this.classThisInts;
/*      */   }
/*      */   
/*      */   public int[] getCodeMaxNALocals() {
/* 1114 */     return this.codeMaxNALocals;
/*      */   }
/*      */   
/*      */   public int[] getCodeMaxStack() {
/* 1118 */     return this.codeMaxStack;
/*      */   }
/*      */   
/*      */   public ArrayList<Attribute>[][] getFieldAttributes() {
/* 1122 */     return this.fieldAttributes;
/*      */   }
/*      */   
/*      */   public int[][] getFieldDescrInts() {
/* 1126 */     return this.fieldDescrInts;
/*      */   }
/*      */   
/*      */   public int[][] getMethodDescrInts() {
/* 1130 */     return this.methodDescrInts;
/*      */   }
/*      */   
/*      */   public long[][] getFieldFlags() {
/* 1134 */     if (this.fieldAccessFlags == null) {
/* 1135 */       long mask = 32767L; int i;
/* 1136 */       for (i = 0; i < 16; i++) {
/* 1137 */         AttributeLayout layout = this.attrMap.getAttributeLayout(i, 1);
/* 1138 */         if (layout != null && !layout.isDefaultLayout()) {
/* 1139 */           mask &= (1 << i ^ 0xFFFFFFFF);
/*      */         }
/*      */       } 
/* 1142 */       this.fieldAccessFlags = new long[this.fieldFlags.length][];
/* 1143 */       for (i = 0; i < this.fieldFlags.length; i++) {
/* 1144 */         this.fieldAccessFlags[i] = new long[(this.fieldFlags[i]).length];
/* 1145 */         for (int j = 0; j < (this.fieldFlags[i]).length; j++) {
/* 1146 */           this.fieldAccessFlags[i][j] = this.fieldFlags[i][j] & mask;
/*      */         }
/*      */       } 
/*      */     } 
/* 1150 */     return this.fieldAccessFlags;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ArrayList<List<Attribute>> getOrderedCodeAttributes() {
/* 1161 */     ArrayList<List<Attribute>> orderedAttributeList = new ArrayList<>(this.codeAttributes.length);
/* 1162 */     for (int classIndex = 0; classIndex < this.codeAttributes.length; classIndex++) {
/* 1163 */       List<Attribute> currentAttributes = new ArrayList<>(this.codeAttributes[classIndex].size());
/* 1164 */       for (int attributeIndex = 0; attributeIndex < this.codeAttributes[classIndex].size(); attributeIndex++) {
/* 1165 */         currentAttributes.add(this.codeAttributes[classIndex].get(attributeIndex));
/*      */       }
/* 1167 */       orderedAttributeList.add(currentAttributes);
/*      */     } 
/* 1169 */     return orderedAttributeList;
/*      */   }
/*      */   
/*      */   public ArrayList<Attribute>[][] getMethodAttributes() {
/* 1173 */     return this.methodAttributes;
/*      */   }
/*      */   
/*      */   public String[][] getMethodDescr() {
/* 1177 */     return this.methodDescr;
/*      */   }
/*      */   
/*      */   public long[][] getMethodFlags() {
/* 1181 */     if (this.methodAccessFlags == null) {
/* 1182 */       long mask = 32767L; int i;
/* 1183 */       for (i = 0; i < 16; i++) {
/* 1184 */         AttributeLayout layout = this.attrMap.getAttributeLayout(i, 2);
/* 1185 */         if (layout != null && !layout.isDefaultLayout()) {
/* 1186 */           mask &= (1 << i ^ 0xFFFFFFFF);
/*      */         }
/*      */       } 
/* 1189 */       this.methodAccessFlags = new long[this.methodFlags.length][];
/* 1190 */       for (i = 0; i < this.methodFlags.length; i++) {
/* 1191 */         this.methodAccessFlags[i] = new long[(this.methodFlags[i]).length];
/* 1192 */         for (int j = 0; j < (this.methodFlags[i]).length; j++) {
/* 1193 */           this.methodAccessFlags[i][j] = this.methodFlags[i][j] & mask;
/*      */         }
/*      */       } 
/*      */     } 
/* 1197 */     return this.methodAccessFlags;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int[] getClassVersionMajor() {
/* 1207 */     return this.classVersionMajor;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int[] getClassVersionMinor() {
/* 1217 */     return this.classVersionMinor;
/*      */   }
/*      */   
/*      */   public int[] getCodeHandlerCount() {
/* 1221 */     return this.codeHandlerCount;
/*      */   }
/*      */   
/*      */   public int[][] getCodeHandlerCatchPO() {
/* 1225 */     return this.codeHandlerCatchPO;
/*      */   }
/*      */   
/*      */   public int[][] getCodeHandlerClassRCN() {
/* 1229 */     return this.codeHandlerClassRCN;
/*      */   }
/*      */   
/*      */   public int[][] getCodeHandlerEndPO() {
/* 1233 */     return this.codeHandlerEndPO;
/*      */   }
/*      */   
/*      */   public int[][] getCodeHandlerStartP() {
/* 1237 */     return this.codeHandlerStartP;
/*      */   }
/*      */   
/*      */   public IcTuple[][] getIcLocal() {
/* 1241 */     return this.icLocal;
/*      */   }
/*      */   
/*      */   public boolean[] getCodeHasAttributes() {
/* 1245 */     return this.codeHasAttributes;
/*      */   }
/*      */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\harmon\\unpack200\ClassBands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */