/*      */ package org.apache.commons.compress.harmony.pack200;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Comparator;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import org.objectweb.asm.Label;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   private final CpBands cpBands;
/*      */   private final AttributeDefinitionBands attrBands;
/*      */   private final CPClass[] class_this;
/*      */   private final CPClass[] class_super;
/*      */   private final CPClass[][] class_interface;
/*      */   private final int[] class_interface_count;
/*      */   private final int[] major_versions;
/*      */   private final long[] class_flags;
/*      */   private int[] class_attr_calls;
/*   53 */   private final List<CPUTF8> classSourceFile = new ArrayList<>();
/*   54 */   private final List<ConstantPoolEntry> classEnclosingMethodClass = new ArrayList<>();
/*   55 */   private final List<ConstantPoolEntry> classEnclosingMethodDesc = new ArrayList<>();
/*   56 */   private final List<CPSignature> classSignature = new ArrayList<>();
/*      */   
/*   58 */   private final IntList classFileVersionMinor = new IntList();
/*   59 */   private final IntList classFileVersionMajor = new IntList();
/*      */   
/*      */   private final int[] class_field_count;
/*      */   private final CPNameAndType[][] field_descr;
/*      */   private final long[][] field_flags;
/*      */   private int[] field_attr_calls;
/*   65 */   private final List<CPConstant<?>> fieldConstantValueKQ = new ArrayList<>();
/*   66 */   private final List<CPSignature> fieldSignature = new ArrayList<>();
/*      */   
/*      */   private final int[] class_method_count;
/*      */   private final CPNameAndType[][] method_descr;
/*      */   private final long[][] method_flags;
/*      */   private int[] method_attr_calls;
/*   72 */   private final List<CPSignature> methodSignature = new ArrayList<>();
/*   73 */   private final IntList methodExceptionNumber = new IntList();
/*   74 */   private final List<CPClass> methodExceptionClasses = new ArrayList<>();
/*      */   
/*      */   private int[] codeHeaders;
/*   77 */   private final IntList codeMaxStack = new IntList();
/*   78 */   private final IntList codeMaxLocals = new IntList();
/*   79 */   private final IntList codeHandlerCount = new IntList();
/*   80 */   private final List codeHandlerStartP = new ArrayList();
/*   81 */   private final List codeHandlerEndPO = new ArrayList();
/*   82 */   private final List codeHandlerCatchPO = new ArrayList();
/*   83 */   private final List<CPClass> codeHandlerClass = new ArrayList<>();
/*   84 */   private final List<Long> codeFlags = new ArrayList<>();
/*      */   private int[] code_attr_calls;
/*   86 */   private final IntList codeLineNumberTableN = new IntList();
/*   87 */   private final List codeLineNumberTableBciP = new ArrayList();
/*   88 */   private final IntList codeLineNumberTableLine = new IntList();
/*   89 */   private final IntList codeLocalVariableTableN = new IntList();
/*   90 */   private final List codeLocalVariableTableBciP = new ArrayList();
/*   91 */   private final List codeLocalVariableTableSpanO = new ArrayList();
/*   92 */   private final List<ConstantPoolEntry> codeLocalVariableTableNameRU = new ArrayList<>();
/*   93 */   private final List<ConstantPoolEntry> codeLocalVariableTableTypeRS = new ArrayList<>();
/*   94 */   private final IntList codeLocalVariableTableSlot = new IntList();
/*   95 */   private final IntList codeLocalVariableTypeTableN = new IntList();
/*   96 */   private final List codeLocalVariableTypeTableBciP = new ArrayList();
/*   97 */   private final List codeLocalVariableTypeTableSpanO = new ArrayList();
/*   98 */   private final List<ConstantPoolEntry> codeLocalVariableTypeTableNameRU = new ArrayList<>();
/*   99 */   private final List<ConstantPoolEntry> codeLocalVariableTypeTableTypeRS = new ArrayList<>();
/*  100 */   private final IntList codeLocalVariableTypeTableSlot = new IntList();
/*      */   
/*      */   private final MetadataBandGroup class_RVA_bands;
/*      */   
/*      */   private final MetadataBandGroup class_RIA_bands;
/*      */   private final MetadataBandGroup field_RVA_bands;
/*      */   private final MetadataBandGroup field_RIA_bands;
/*      */   private final MetadataBandGroup method_RVA_bands;
/*      */   private final MetadataBandGroup method_RIA_bands;
/*      */   private final MetadataBandGroup method_RVPA_bands;
/*      */   private final MetadataBandGroup method_RIPA_bands;
/*      */   private final MetadataBandGroup method_AD_bands;
/*  112 */   private final List<NewAttributeBands> classAttributeBands = new ArrayList<>();
/*  113 */   private final List<NewAttributeBands> methodAttributeBands = new ArrayList<>();
/*  114 */   private final List<NewAttributeBands> fieldAttributeBands = new ArrayList<>();
/*  115 */   private final List<NewAttributeBands> codeAttributeBands = new ArrayList<>();
/*      */   
/*  117 */   private final List<Long> tempFieldFlags = new ArrayList<>();
/*  118 */   private final List<CPNameAndType> tempFieldDesc = new ArrayList<>();
/*  119 */   private final List<Long> tempMethodFlags = new ArrayList<>();
/*  120 */   private final List<CPNameAndType> tempMethodDesc = new ArrayList<>();
/*      */   
/*      */   private TempParamAnnotation tempMethodRVPA;
/*      */   
/*      */   private TempParamAnnotation tempMethodRIPA;
/*      */   private boolean anySyntheticClasses = false;
/*      */   private boolean anySyntheticFields = false;
/*      */   private boolean anySyntheticMethods = false;
/*      */   private final Segment segment;
/*  129 */   private final Map<CPClass, Set<CPClass>> classReferencesInnerClass = new HashMap<>();
/*      */   
/*      */   private final boolean stripDebug;
/*  132 */   private int index = 0;
/*      */   
/*  134 */   private int numMethodArgs = 0;
/*      */   
/*      */   private int[] class_InnerClasses_N;
/*      */   private CPClass[] class_InnerClasses_RC;
/*      */   private int[] class_InnerClasses_F;
/*      */   private List<CPClass> classInnerClassesOuterRCN;
/*      */   private List<CPUTF8> classInnerClassesNameRUN;
/*      */   
/*      */   public ClassBands(Segment segment, int numClasses, int effort, boolean stripDebug) throws IOException {
/*  143 */     super(effort, segment.getSegmentHeader());
/*  144 */     this.stripDebug = stripDebug;
/*  145 */     this.segment = segment;
/*  146 */     this.cpBands = segment.getCpBands();
/*  147 */     this.attrBands = segment.getAttrBands();
/*  148 */     this.class_this = new CPClass[numClasses];
/*  149 */     this.class_super = new CPClass[numClasses];
/*  150 */     this.class_interface_count = new int[numClasses];
/*  151 */     this.class_interface = new CPClass[numClasses][];
/*  152 */     this.class_field_count = new int[numClasses];
/*  153 */     this.class_method_count = new int[numClasses];
/*  154 */     this.field_descr = new CPNameAndType[numClasses][];
/*  155 */     this.field_flags = new long[numClasses][];
/*  156 */     this.method_descr = new CPNameAndType[numClasses][];
/*  157 */     this.method_flags = new long[numClasses][];
/*  158 */     for (int i = 0; i < numClasses; i++) {
/*  159 */       this.field_flags[i] = new long[0];
/*  160 */       this.method_flags[i] = new long[0];
/*      */     } 
/*      */     
/*  163 */     this.major_versions = new int[numClasses];
/*  164 */     this.class_flags = new long[numClasses];
/*      */     
/*  166 */     this.class_RVA_bands = new MetadataBandGroup("RVA", 0, this.cpBands, this.segmentHeader, effort);
/*  167 */     this.class_RIA_bands = new MetadataBandGroup("RIA", 0, this.cpBands, this.segmentHeader, effort);
/*  168 */     this.field_RVA_bands = new MetadataBandGroup("RVA", 1, this.cpBands, this.segmentHeader, effort);
/*  169 */     this.field_RIA_bands = new MetadataBandGroup("RIA", 1, this.cpBands, this.segmentHeader, effort);
/*  170 */     this.method_RVA_bands = new MetadataBandGroup("RVA", 2, this.cpBands, this.segmentHeader, effort);
/*      */     
/*  172 */     this.method_RIA_bands = new MetadataBandGroup("RIA", 2, this.cpBands, this.segmentHeader, effort);
/*      */     
/*  174 */     this.method_RVPA_bands = new MetadataBandGroup("RVPA", 2, this.cpBands, this.segmentHeader, effort);
/*      */     
/*  176 */     this.method_RIPA_bands = new MetadataBandGroup("RIPA", 2, this.cpBands, this.segmentHeader, effort);
/*      */     
/*  178 */     this.method_AD_bands = new MetadataBandGroup("AD", 2, this.cpBands, this.segmentHeader, effort);
/*      */     
/*  180 */     createNewAttributeBands();
/*      */   }
/*      */   
/*      */   private void createNewAttributeBands() throws IOException {
/*  184 */     for (AttributeDefinitionBands.AttributeDefinition def : this.attrBands.getClassAttributeLayouts()) {
/*  185 */       this.classAttributeBands.add(new NewAttributeBands(this.effort, this.cpBands, this.segment.getSegmentHeader(), def));
/*      */     }
/*  187 */     for (AttributeDefinitionBands.AttributeDefinition def : this.attrBands.getMethodAttributeLayouts()) {
/*  188 */       this.methodAttributeBands.add(new NewAttributeBands(this.effort, this.cpBands, this.segment.getSegmentHeader(), def));
/*      */     }
/*  190 */     for (AttributeDefinitionBands.AttributeDefinition def : this.attrBands.getFieldAttributeLayouts()) {
/*  191 */       this.fieldAttributeBands.add(new NewAttributeBands(this.effort, this.cpBands, this.segment.getSegmentHeader(), def));
/*      */     }
/*  193 */     for (AttributeDefinitionBands.AttributeDefinition def : this.attrBands.getCodeAttributeLayouts()) {
/*  194 */       this.codeAttributeBands.add(new NewAttributeBands(this.effort, this.cpBands, this.segment.getSegmentHeader(), def));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void addClass(int major, int flags, String className, String signature, String superName, String[] interfaces) {
/*  200 */     this.class_this[this.index] = this.cpBands.getCPClass(className);
/*  201 */     this.class_super[this.index] = this.cpBands.getCPClass(superName);
/*  202 */     this.class_interface_count[this.index] = interfaces.length;
/*  203 */     this.class_interface[this.index] = new CPClass[interfaces.length];
/*  204 */     Arrays.setAll(this.class_interface[this.index], i -> this.cpBands.getCPClass(interfaces[i]));
/*  205 */     this.major_versions[this.index] = major;
/*  206 */     this.class_flags[this.index] = flags;
/*  207 */     if (!this.anySyntheticClasses && (flags & 0x1000) != 0 && this.segment
/*  208 */       .getCurrentClassReader().hasSyntheticAttributes()) {
/*  209 */       this.cpBands.addCPUtf8("Synthetic");
/*  210 */       this.anySyntheticClasses = true;
/*      */     } 
/*  212 */     if ((flags & 0x20000) != 0) {
/*  213 */       flags &= 0xFFFDFFFF;
/*  214 */       flags |= 0x100000;
/*      */     } 
/*  216 */     if (signature != null) {
/*  217 */       this.class_flags[this.index] = this.class_flags[this.index] | 0x80000L;
/*  218 */       this.classSignature.add(this.cpBands.getCPSignature(signature));
/*      */     } 
/*      */   }
/*      */   
/*      */   public void currentClassReferencesInnerClass(CPClass inner) {
/*  223 */     if (this.index < this.class_this.length) {
/*  224 */       CPClass currentClass = this.class_this[this.index];
/*  225 */       if (currentClass != null && !currentClass.equals(inner) && 
/*  226 */         !isInnerClassOf(currentClass.toString(), inner)) {
/*  227 */         Set<CPClass> referencedInnerClasses = this.classReferencesInnerClass.get(currentClass);
/*  228 */         if (referencedInnerClasses == null) {
/*  229 */           referencedInnerClasses = new HashSet<>();
/*  230 */           this.classReferencesInnerClass.put(currentClass, referencedInnerClasses);
/*      */         } 
/*  232 */         referencedInnerClasses.add(inner);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean isInnerClassOf(String possibleInner, CPClass possibleOuter) {
/*  238 */     if (isInnerClass(possibleInner)) {
/*  239 */       String superClassName = possibleInner.substring(0, possibleInner.lastIndexOf('$'));
/*  240 */       if (superClassName.equals(possibleOuter.toString())) {
/*  241 */         return true;
/*      */       }
/*  243 */       return isInnerClassOf(superClassName, possibleOuter);
/*      */     } 
/*  245 */     return false;
/*      */   }
/*      */   
/*      */   private boolean isInnerClass(String possibleInner) {
/*  249 */     return (possibleInner.indexOf('$') != -1);
/*      */   }
/*      */   
/*      */   public void addField(int flags, String name, String desc, String signature, Object value) {
/*  253 */     flags &= 0xFFFF;
/*  254 */     this.tempFieldDesc.add(this.cpBands.getCPNameAndType(name, desc));
/*  255 */     if (signature != null) {
/*  256 */       this.fieldSignature.add(this.cpBands.getCPSignature(signature));
/*  257 */       flags |= 0x80000;
/*      */     } 
/*  259 */     if ((flags & 0x20000) != 0) {
/*  260 */       flags &= 0xFFFDFFFF;
/*  261 */       flags |= 0x100000;
/*      */     } 
/*  263 */     if (value != null) {
/*  264 */       this.fieldConstantValueKQ.add(this.cpBands.getConstant(value));
/*  265 */       flags |= 0x20000;
/*      */     } 
/*  267 */     if (!this.anySyntheticFields && (flags & 0x1000) != 0 && this.segment
/*  268 */       .getCurrentClassReader().hasSyntheticAttributes()) {
/*  269 */       this.cpBands.addCPUtf8("Synthetic");
/*  270 */       this.anySyntheticFields = true;
/*      */     } 
/*  272 */     this.tempFieldFlags.add(Long.valueOf(flags));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void finaliseBands() {
/*  281 */     int defaultMajorVersion = this.segmentHeader.getDefaultMajorVersion();
/*  282 */     for (int i = 0; i < this.class_flags.length; i++) {
/*  283 */       int major = this.major_versions[i];
/*  284 */       if (major != defaultMajorVersion) {
/*  285 */         this.class_flags[i] = this.class_flags[i] | 0x1000000L;
/*  286 */         this.classFileVersionMajor.add(major);
/*  287 */         this.classFileVersionMinor.add(0);
/*      */       } 
/*      */     } 
/*      */     
/*  291 */     this.codeHeaders = new int[this.codeHandlerCount.size()];
/*  292 */     int removed = 0;
/*  293 */     for (int j = 0; j < this.codeHeaders.length; j++) {
/*  294 */       int numHandlers = this.codeHandlerCount.get(j - removed);
/*  295 */       int maxLocals = this.codeMaxLocals.get(j - removed);
/*  296 */       int maxStack = this.codeMaxStack.get(j - removed);
/*  297 */       if (numHandlers == 0) {
/*  298 */         int header = maxLocals * 12 + maxStack + 1;
/*  299 */         if (header < 145 && maxStack < 12) {
/*  300 */           this.codeHeaders[j] = header;
/*      */         }
/*  302 */       } else if (numHandlers == 1) {
/*  303 */         int header = maxLocals * 8 + maxStack + 145;
/*  304 */         if (header < 209 && maxStack < 8) {
/*  305 */           this.codeHeaders[j] = header;
/*      */         }
/*  307 */       } else if (numHandlers == 2) {
/*  308 */         int header = maxLocals * 7 + maxStack + 209;
/*  309 */         if (header < 256 && maxStack < 7) {
/*  310 */           this.codeHeaders[j] = header;
/*      */         }
/*      */       } 
/*  313 */       if (this.codeHeaders[j] != 0) {
/*      */ 
/*      */         
/*  316 */         this.codeHandlerCount.remove(j - removed);
/*  317 */         this.codeMaxLocals.remove(j - removed);
/*  318 */         this.codeMaxStack.remove(j - removed);
/*  319 */         removed++;
/*  320 */       } else if (!this.segment.getSegmentHeader().have_all_code_flags()) {
/*  321 */         this.codeFlags.add(Long.valueOf(0L));
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  326 */     IntList innerClassesN = new IntList();
/*  327 */     List<IcBands.IcTuple> icLocal = new ArrayList<>(); int k;
/*  328 */     for (k = 0; k < this.class_this.length; k++) {
/*  329 */       CPClass cpClass = this.class_this[k];
/*  330 */       Set<CPClass> referencedInnerClasses = this.classReferencesInnerClass.get(cpClass);
/*  331 */       if (referencedInnerClasses != null) {
/*  332 */         int innerN = 0;
/*  333 */         List<IcBands.IcTuple> innerClasses = this.segment.getIcBands().getInnerClassesForOuter(cpClass.toString());
/*  334 */         if (innerClasses != null) {
/*  335 */           for (IcBands.IcTuple element : innerClasses) {
/*  336 */             referencedInnerClasses.remove(element.C);
/*      */           }
/*      */         }
/*  339 */         for (CPClass inner : referencedInnerClasses) {
/*  340 */           IcBands.IcTuple icTuple = this.segment.getIcBands().getIcTuple(inner);
/*  341 */           if (icTuple != null && !icTuple.isAnonymous()) {
/*      */             
/*  343 */             icLocal.add(icTuple);
/*  344 */             innerN++;
/*      */           } 
/*      */         } 
/*  347 */         if (innerN != 0) {
/*  348 */           innerClassesN.add(innerN);
/*  349 */           this.class_flags[k] = this.class_flags[k] | 0x800000L;
/*      */         } 
/*      */       } 
/*      */     } 
/*  353 */     this.class_InnerClasses_N = innerClassesN.toArray();
/*  354 */     this.class_InnerClasses_RC = new CPClass[icLocal.size()];
/*  355 */     this.class_InnerClasses_F = new int[icLocal.size()];
/*  356 */     this.classInnerClassesOuterRCN = new ArrayList<>();
/*  357 */     this.classInnerClassesNameRUN = new ArrayList<>();
/*  358 */     for (k = 0; k < this.class_InnerClasses_RC.length; k++) {
/*  359 */       IcBands.IcTuple icTuple = icLocal.get(k);
/*  360 */       this.class_InnerClasses_RC[k] = icTuple.C;
/*  361 */       if (icTuple.C2 == null && icTuple.N == null) {
/*  362 */         this.class_InnerClasses_F[k] = 0;
/*      */       } else {
/*  364 */         if (icTuple.F == 0) {
/*  365 */           this.class_InnerClasses_F[k] = 65536;
/*      */         } else {
/*  367 */           this.class_InnerClasses_F[k] = icTuple.F;
/*      */         } 
/*  369 */         this.classInnerClassesOuterRCN.add(icTuple.C2);
/*  370 */         this.classInnerClassesNameRUN.add(icTuple.N);
/*      */       } 
/*      */     } 
/*      */     
/*  374 */     IntList classAttrCalls = new IntList();
/*  375 */     IntList fieldAttrCalls = new IntList();
/*  376 */     IntList methodAttrCalls = new IntList();
/*  377 */     IntList codeAttrCalls = new IntList();
/*      */     
/*  379 */     if (this.class_RVA_bands.hasContent()) {
/*  380 */       classAttrCalls.add(this.class_RVA_bands.numBackwardsCalls());
/*      */     }
/*  382 */     if (this.class_RIA_bands.hasContent()) {
/*  383 */       classAttrCalls.add(this.class_RIA_bands.numBackwardsCalls());
/*      */     }
/*  385 */     if (this.field_RVA_bands.hasContent()) {
/*  386 */       fieldAttrCalls.add(this.field_RVA_bands.numBackwardsCalls());
/*      */     }
/*  388 */     if (this.field_RIA_bands.hasContent()) {
/*  389 */       fieldAttrCalls.add(this.field_RIA_bands.numBackwardsCalls());
/*      */     }
/*  391 */     if (this.method_RVA_bands.hasContent()) {
/*  392 */       methodAttrCalls.add(this.method_RVA_bands.numBackwardsCalls());
/*      */     }
/*  394 */     if (this.method_RIA_bands.hasContent()) {
/*  395 */       methodAttrCalls.add(this.method_RIA_bands.numBackwardsCalls());
/*      */     }
/*  397 */     if (this.method_RVPA_bands.hasContent()) {
/*  398 */       methodAttrCalls.add(this.method_RVPA_bands.numBackwardsCalls());
/*      */     }
/*  400 */     if (this.method_RIPA_bands.hasContent()) {
/*  401 */       methodAttrCalls.add(this.method_RIPA_bands.numBackwardsCalls());
/*      */     }
/*  403 */     if (this.method_AD_bands.hasContent()) {
/*  404 */       methodAttrCalls.add(this.method_AD_bands.numBackwardsCalls());
/*      */     }
/*      */ 
/*      */     
/*  408 */     Comparator<NewAttributeBands> comparator = (arg0, arg1) -> arg0.getFlagIndex() - arg1.getFlagIndex();
/*  409 */     this.classAttributeBands.sort(comparator);
/*  410 */     this.methodAttributeBands.sort(comparator);
/*  411 */     this.fieldAttributeBands.sort(comparator);
/*  412 */     this.codeAttributeBands.sort(comparator);
/*      */     
/*  414 */     for (NewAttributeBands bands : this.classAttributeBands) {
/*  415 */       if (bands.isUsedAtLeastOnce()) {
/*  416 */         for (int backwardsCallCount : bands.numBackwardsCalls()) {
/*  417 */           classAttrCalls.add(backwardsCallCount);
/*      */         }
/*      */       }
/*      */     } 
/*  421 */     for (NewAttributeBands bands : this.methodAttributeBands) {
/*  422 */       if (bands.isUsedAtLeastOnce()) {
/*  423 */         for (int backwardsCallCount : bands.numBackwardsCalls()) {
/*  424 */           methodAttrCalls.add(backwardsCallCount);
/*      */         }
/*      */       }
/*      */     } 
/*  428 */     for (NewAttributeBands bands : this.fieldAttributeBands) {
/*  429 */       if (bands.isUsedAtLeastOnce()) {
/*  430 */         for (int backwardsCallCount : bands.numBackwardsCalls()) {
/*  431 */           fieldAttrCalls.add(backwardsCallCount);
/*      */         }
/*      */       }
/*      */     } 
/*  435 */     for (NewAttributeBands bands : this.codeAttributeBands) {
/*  436 */       if (bands.isUsedAtLeastOnce()) {
/*  437 */         for (int backwardsCallCount : bands.numBackwardsCalls()) {
/*  438 */           codeAttrCalls.add(backwardsCallCount);
/*      */         }
/*      */       }
/*      */     } 
/*      */     
/*  443 */     this.class_attr_calls = classAttrCalls.toArray();
/*  444 */     this.field_attr_calls = fieldAttrCalls.toArray();
/*  445 */     this.method_attr_calls = methodAttrCalls.toArray();
/*  446 */     this.code_attr_calls = codeAttrCalls.toArray();
/*      */   }
/*      */ 
/*      */   
/*      */   public void pack(OutputStream out) throws IOException, Pack200Exception {
/*  451 */     PackingUtils.log("Writing class bands...");
/*      */     
/*  453 */     byte[] encodedBand = encodeBandInt("class_this", getInts(this.class_this), Codec.DELTA5);
/*  454 */     out.write(encodedBand);
/*  455 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from class_this[" + this.class_this.length + "]");
/*      */     
/*  457 */     encodedBand = encodeBandInt("class_super", getInts(this.class_super), Codec.DELTA5);
/*  458 */     out.write(encodedBand);
/*  459 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from class_super[" + this.class_super.length + "]");
/*      */     
/*  461 */     encodedBand = encodeBandInt("class_interface_count", this.class_interface_count, Codec.DELTA5);
/*  462 */     out.write(encodedBand);
/*  463 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from class_interface_count[" + this.class_interface_count.length + "]");
/*      */ 
/*      */     
/*  466 */     int totalInterfaces = sum(this.class_interface_count);
/*  467 */     int[] classInterface = new int[totalInterfaces];
/*  468 */     int k = 0;
/*  469 */     for (CPClass[] element : this.class_interface) {
/*  470 */       if (element != null) {
/*  471 */         for (CPClass cpClass : element) {
/*  472 */           classInterface[k] = cpClass.getIndex();
/*  473 */           k++;
/*      */         } 
/*      */       }
/*      */     } 
/*      */     
/*  478 */     encodedBand = encodeBandInt("class_interface", classInterface, Codec.DELTA5);
/*  479 */     out.write(encodedBand);
/*  480 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from class_interface[" + classInterface.length + "]");
/*      */     
/*  482 */     encodedBand = encodeBandInt("class_field_count", this.class_field_count, Codec.DELTA5);
/*  483 */     out.write(encodedBand);
/*      */     
/*  485 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from class_field_count[" + this.class_field_count.length + "]");
/*      */     
/*  487 */     encodedBand = encodeBandInt("class_method_count", this.class_method_count, Codec.DELTA5);
/*  488 */     out.write(encodedBand);
/*  489 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from class_method_count[" + this.class_method_count.length + "]");
/*      */ 
/*      */     
/*  492 */     int totalFields = sum(this.class_field_count);
/*  493 */     int[] fieldDescr = new int[totalFields];
/*  494 */     k = 0;
/*  495 */     for (int i = 0; i < this.index; i++) {
/*  496 */       for (int m = 0; m < (this.field_descr[i]).length; m++) {
/*  497 */         CPNameAndType descr = this.field_descr[i][m];
/*  498 */         fieldDescr[k] = descr.getIndex();
/*  499 */         k++;
/*      */       } 
/*      */     } 
/*      */     
/*  503 */     encodedBand = encodeBandInt("field_descr", fieldDescr, Codec.DELTA5);
/*  504 */     out.write(encodedBand);
/*  505 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from field_descr[" + fieldDescr.length + "]");
/*      */     
/*  507 */     writeFieldAttributeBands(out);
/*      */     
/*  509 */     int totalMethods = sum(this.class_method_count);
/*  510 */     int[] methodDescr = new int[totalMethods];
/*  511 */     k = 0;
/*  512 */     for (int j = 0; j < this.index; j++) {
/*  513 */       for (int m = 0; m < (this.method_descr[j]).length; m++) {
/*  514 */         CPNameAndType descr = this.method_descr[j][m];
/*  515 */         methodDescr[k] = descr.getIndex();
/*  516 */         k++;
/*      */       } 
/*      */     } 
/*      */     
/*  520 */     encodedBand = encodeBandInt("method_descr", methodDescr, Codec.MDELTA5);
/*  521 */     out.write(encodedBand);
/*  522 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from method_descr[" + methodDescr.length + "]");
/*      */     
/*  524 */     writeMethodAttributeBands(out);
/*  525 */     writeClassAttributeBands(out);
/*  526 */     writeCodeBands(out);
/*      */   }
/*      */   
/*      */   private int sum(int[] ints) {
/*  530 */     int sum = 0;
/*  531 */     for (int j : ints) {
/*  532 */       sum += j;
/*      */     }
/*  534 */     return sum;
/*      */   }
/*      */   
/*      */   private void writeFieldAttributeBands(OutputStream out) throws IOException, Pack200Exception {
/*  538 */     byte[] encodedBand = encodeFlags("field_flags", this.field_flags, Codec.UNSIGNED5, Codec.UNSIGNED5, this.segmentHeader
/*  539 */         .have_field_flags_hi());
/*  540 */     out.write(encodedBand);
/*  541 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from field_flags[" + this.field_flags.length + "]");
/*      */ 
/*      */ 
/*      */     
/*  545 */     encodedBand = encodeBandInt("field_attr_calls", this.field_attr_calls, Codec.UNSIGNED5);
/*  546 */     out.write(encodedBand);
/*      */     
/*  548 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from field_attr_calls[" + this.field_attr_calls.length + "]");
/*      */     
/*  550 */     encodedBand = encodeBandInt("fieldConstantValueKQ", cpEntryListToArray((List)this.fieldConstantValueKQ), Codec.UNSIGNED5);
/*  551 */     out.write(encodedBand);
/*  552 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from fieldConstantValueKQ[" + this.fieldConstantValueKQ
/*  553 */         .size() + "]");
/*      */     
/*  555 */     encodedBand = encodeBandInt("fieldSignature", cpEntryListToArray((List)this.fieldSignature), Codec.UNSIGNED5);
/*  556 */     out.write(encodedBand);
/*  557 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from fieldSignature[" + this.fieldSignature.size() + "]");
/*      */     
/*  559 */     this.field_RVA_bands.pack(out);
/*  560 */     this.field_RIA_bands.pack(out);
/*  561 */     for (NewAttributeBands bands : this.fieldAttributeBands) {
/*  562 */       bands.pack(out);
/*      */     }
/*      */   }
/*      */   
/*      */   private void writeMethodAttributeBands(OutputStream out) throws IOException, Pack200Exception {
/*  567 */     byte[] encodedBand = encodeFlags("method_flags", this.method_flags, Codec.UNSIGNED5, Codec.UNSIGNED5, this.segmentHeader
/*  568 */         .have_method_flags_hi());
/*  569 */     out.write(encodedBand);
/*  570 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from method_flags[" + this.method_flags.length + "]");
/*      */ 
/*      */ 
/*      */     
/*  574 */     encodedBand = encodeBandInt("method_attr_calls", this.method_attr_calls, Codec.UNSIGNED5);
/*  575 */     out.write(encodedBand);
/*      */     
/*  577 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from method_attr_calls[" + this.method_attr_calls.length + "]");
/*      */     
/*  579 */     encodedBand = encodeBandInt("methodExceptionNumber", this.methodExceptionNumber.toArray(), Codec.UNSIGNED5);
/*  580 */     out.write(encodedBand);
/*  581 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from methodExceptionNumber[" + this.methodExceptionNumber
/*  582 */         .size() + "]");
/*      */     
/*  584 */     encodedBand = encodeBandInt("methodExceptionClasses", cpEntryListToArray((List)this.methodExceptionClasses), Codec.UNSIGNED5);
/*      */     
/*  586 */     out.write(encodedBand);
/*  587 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from methodExceptionClasses[" + this.methodExceptionClasses
/*  588 */         .size() + "]");
/*      */     
/*  590 */     encodedBand = encodeBandInt("methodSignature", cpEntryListToArray((List)this.methodSignature), Codec.UNSIGNED5);
/*  591 */     out.write(encodedBand);
/*  592 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from methodSignature[" + this.methodSignature.size() + "]");
/*      */     
/*  594 */     this.method_RVA_bands.pack(out);
/*  595 */     this.method_RIA_bands.pack(out);
/*  596 */     this.method_RVPA_bands.pack(out);
/*  597 */     this.method_RIPA_bands.pack(out);
/*  598 */     this.method_AD_bands.pack(out);
/*  599 */     for (NewAttributeBands bands : this.methodAttributeBands) {
/*  600 */       bands.pack(out);
/*      */     }
/*      */   }
/*      */   
/*      */   private void writeClassAttributeBands(OutputStream out) throws IOException, Pack200Exception {
/*  605 */     byte[] encodedBand = encodeFlags("class_flags", this.class_flags, Codec.UNSIGNED5, Codec.UNSIGNED5, this.segmentHeader
/*  606 */         .have_class_flags_hi());
/*  607 */     out.write(encodedBand);
/*  608 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from class_flags[" + this.class_flags.length + "]");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  618 */     encodedBand = encodeBandInt("class_attr_calls", this.class_attr_calls, Codec.UNSIGNED5);
/*  619 */     out.write(encodedBand);
/*      */     
/*  621 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from class_attr_calls[" + this.class_attr_calls.length + "]");
/*      */     
/*  623 */     encodedBand = encodeBandInt("classSourceFile", cpEntryOrNullListToArray((List)this.classSourceFile), Codec.UNSIGNED5);
/*  624 */     out.write(encodedBand);
/*  625 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from classSourceFile[" + this.classSourceFile.size() + "]");
/*      */     
/*  627 */     encodedBand = encodeBandInt("class_enclosing_method_RC", cpEntryListToArray(this.classEnclosingMethodClass), Codec.UNSIGNED5);
/*      */     
/*  629 */     out.write(encodedBand);
/*  630 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from class_enclosing_method_RC[" + this.classEnclosingMethodClass
/*  631 */         .size() + "]");
/*      */     
/*  633 */     encodedBand = encodeBandInt("class_EnclosingMethod_RDN", cpEntryOrNullListToArray(this.classEnclosingMethodDesc), Codec.UNSIGNED5);
/*      */     
/*  635 */     out.write(encodedBand);
/*  636 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from class_EnclosingMethod_RDN[" + this.classEnclosingMethodDesc
/*  637 */         .size() + "]");
/*      */     
/*  639 */     encodedBand = encodeBandInt("class_Signature_RS", cpEntryListToArray((List)this.classSignature), Codec.UNSIGNED5);
/*  640 */     out.write(encodedBand);
/*      */     
/*  642 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from class_Signature_RS[" + this.classSignature.size() + "]");
/*      */     
/*  644 */     this.class_RVA_bands.pack(out);
/*  645 */     this.class_RIA_bands.pack(out);
/*      */     
/*  647 */     encodedBand = encodeBandInt("class_InnerClasses_N", this.class_InnerClasses_N, Codec.UNSIGNED5);
/*  648 */     out.write(encodedBand);
/*  649 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from class_InnerClasses_N[" + this.class_InnerClasses_N.length + "]");
/*      */ 
/*      */     
/*  652 */     encodedBand = encodeBandInt("class_InnerClasses_RC", getInts(this.class_InnerClasses_RC), Codec.UNSIGNED5);
/*  653 */     out.write(encodedBand);
/*  654 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from class_InnerClasses_RC[" + this.class_InnerClasses_RC.length + "]");
/*      */ 
/*      */     
/*  657 */     encodedBand = encodeBandInt("class_InnerClasses_F", this.class_InnerClasses_F, Codec.UNSIGNED5);
/*  658 */     out.write(encodedBand);
/*  659 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from class_InnerClasses_F[" + this.class_InnerClasses_F.length + "]");
/*      */ 
/*      */     
/*  662 */     encodedBand = encodeBandInt("class_InnerClasses_outer_RCN", cpEntryOrNullListToArray((List)this.classInnerClassesOuterRCN), Codec.UNSIGNED5);
/*      */     
/*  664 */     out.write(encodedBand);
/*  665 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from class_InnerClasses_outer_RCN[" + this.classInnerClassesOuterRCN
/*  666 */         .size() + "]");
/*      */     
/*  668 */     encodedBand = encodeBandInt("class_InnerClasses_name_RUN", cpEntryOrNullListToArray((List)this.classInnerClassesNameRUN), Codec.UNSIGNED5);
/*      */     
/*  670 */     out.write(encodedBand);
/*  671 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from class_InnerClasses_name_RUN[" + this.classInnerClassesNameRUN
/*  672 */         .size() + "]");
/*      */     
/*  674 */     encodedBand = encodeBandInt("classFileVersionMinor", this.classFileVersionMinor.toArray(), Codec.UNSIGNED5);
/*  675 */     out.write(encodedBand);
/*  676 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from classFileVersionMinor[" + this.classFileVersionMinor
/*  677 */         .size() + "]");
/*      */     
/*  679 */     encodedBand = encodeBandInt("classFileVersionMajor", this.classFileVersionMajor.toArray(), Codec.UNSIGNED5);
/*  680 */     out.write(encodedBand);
/*  681 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from classFileVersionMajor[" + this.classFileVersionMajor
/*  682 */         .size() + "]");
/*      */     
/*  684 */     for (NewAttributeBands classAttributeBand : this.classAttributeBands) {
/*  685 */       classAttributeBand.pack(out);
/*      */     }
/*      */   }
/*      */   
/*      */   private int[] getInts(CPClass[] cpClasses) {
/*  690 */     int[] ints = new int[cpClasses.length];
/*  691 */     for (int i = 0; i < ints.length; i++) {
/*  692 */       if (cpClasses[i] != null) {
/*  693 */         ints[i] = cpClasses[i].getIndex();
/*      */       }
/*      */     } 
/*  696 */     return ints;
/*      */   }
/*      */   
/*      */   private void writeCodeBands(OutputStream out) throws IOException, Pack200Exception {
/*  700 */     byte[] encodedBand = encodeBandInt("codeHeaders", this.codeHeaders, Codec.BYTE1);
/*  701 */     out.write(encodedBand);
/*  702 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from codeHeaders[" + this.codeHeaders.length + "]");
/*      */     
/*  704 */     encodedBand = encodeBandInt("codeMaxStack", this.codeMaxStack.toArray(), Codec.UNSIGNED5);
/*  705 */     out.write(encodedBand);
/*  706 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from codeMaxStack[" + this.codeMaxStack.size() + "]");
/*      */     
/*  708 */     encodedBand = encodeBandInt("codeMaxLocals", this.codeMaxLocals.toArray(), Codec.UNSIGNED5);
/*  709 */     out.write(encodedBand);
/*  710 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from codeMaxLocals[" + this.codeMaxLocals.size() + "]");
/*      */     
/*  712 */     encodedBand = encodeBandInt("codeHandlerCount", this.codeHandlerCount.toArray(), Codec.UNSIGNED5);
/*  713 */     out.write(encodedBand);
/*      */     
/*  715 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from codeHandlerCount[" + this.codeHandlerCount.size() + "]");
/*      */     
/*  717 */     encodedBand = encodeBandInt("codeHandlerStartP", integerListToArray(this.codeHandlerStartP), Codec.BCI5);
/*  718 */     out.write(encodedBand);
/*      */     
/*  720 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from codeHandlerStartP[" + this.codeHandlerStartP.size() + "]");
/*      */     
/*  722 */     encodedBand = encodeBandInt("codeHandlerEndPO", integerListToArray(this.codeHandlerEndPO), Codec.BRANCH5);
/*  723 */     out.write(encodedBand);
/*      */     
/*  725 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from codeHandlerEndPO[" + this.codeHandlerEndPO.size() + "]");
/*      */     
/*  727 */     encodedBand = encodeBandInt("codeHandlerCatchPO", integerListToArray(this.codeHandlerCatchPO), Codec.BRANCH5);
/*  728 */     out.write(encodedBand);
/*  729 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from codeHandlerCatchPO[" + this.codeHandlerCatchPO
/*  730 */         .size() + "]");
/*      */     
/*  732 */     encodedBand = encodeBandInt("codeHandlerClass", cpEntryOrNullListToArray((List)this.codeHandlerClass), Codec.UNSIGNED5);
/*  733 */     out.write(encodedBand);
/*      */     
/*  735 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from codeHandlerClass[" + this.codeHandlerClass.size() + "]");
/*      */     
/*  737 */     writeCodeAttributeBands(out);
/*      */   }
/*      */   
/*      */   private void writeCodeAttributeBands(OutputStream out) throws IOException, Pack200Exception {
/*  741 */     byte[] encodedBand = encodeFlags("codeFlags", longListToArray(this.codeFlags), Codec.UNSIGNED5, Codec.UNSIGNED5, this.segmentHeader
/*  742 */         .have_code_flags_hi());
/*  743 */     out.write(encodedBand);
/*  744 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from codeFlags[" + this.codeFlags.size() + "]");
/*      */ 
/*      */ 
/*      */     
/*  748 */     encodedBand = encodeBandInt("code_attr_calls", this.code_attr_calls, Codec.UNSIGNED5);
/*  749 */     out.write(encodedBand);
/*  750 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from code_attr_calls[" + this.code_attr_calls.length + "]");
/*      */     
/*  752 */     encodedBand = encodeBandInt("code_LineNumberTable_N", this.codeLineNumberTableN.toArray(), Codec.UNSIGNED5);
/*  753 */     out.write(encodedBand);
/*  754 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from code_LineNumberTable_N[" + this.codeLineNumberTableN
/*  755 */         .size() + "]");
/*      */     
/*  757 */     encodedBand = encodeBandInt("code_LineNumberTable_bci_P", integerListToArray(this.codeLineNumberTableBciP), Codec.BCI5);
/*      */     
/*  759 */     out.write(encodedBand);
/*  760 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from code_LineNumberTable_bci_P[" + this.codeLineNumberTableBciP
/*  761 */         .size() + "]");
/*      */     
/*  763 */     encodedBand = encodeBandInt("code_LineNumberTable_line", this.codeLineNumberTableLine.toArray(), Codec.UNSIGNED5);
/*  764 */     out.write(encodedBand);
/*  765 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from code_LineNumberTable_line[" + this.codeLineNumberTableLine
/*  766 */         .size() + "]");
/*      */     
/*  768 */     encodedBand = encodeBandInt("code_LocalVariableTable_N", this.codeLocalVariableTableN.toArray(), Codec.UNSIGNED5);
/*  769 */     out.write(encodedBand);
/*  770 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from code_LocalVariableTable_N[" + this.codeLocalVariableTableN
/*  771 */         .size() + "]");
/*      */     
/*  773 */     encodedBand = encodeBandInt("code_LocalVariableTable_bci_P", integerListToArray(this.codeLocalVariableTableBciP), Codec.BCI5);
/*      */     
/*  775 */     out.write(encodedBand);
/*  776 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from code_LocalVariableTable_bci_P[" + this.codeLocalVariableTableBciP
/*  777 */         .size() + "]");
/*      */     
/*  779 */     encodedBand = encodeBandInt("code_LocalVariableTable_span_O", integerListToArray(this.codeLocalVariableTableSpanO), Codec.BRANCH5);
/*      */     
/*  781 */     out.write(encodedBand);
/*  782 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from code_LocalVariableTable_span_O[" + this.codeLocalVariableTableSpanO
/*  783 */         .size() + "]");
/*      */     
/*  785 */     encodedBand = encodeBandInt("code_LocalVariableTable_name_RU", cpEntryListToArray(this.codeLocalVariableTableNameRU), Codec.UNSIGNED5);
/*      */     
/*  787 */     out.write(encodedBand);
/*  788 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from code_LocalVariableTable_name_RU[" + this.codeLocalVariableTableNameRU
/*  789 */         .size() + "]");
/*      */     
/*  791 */     encodedBand = encodeBandInt("code_LocalVariableTable_type_RS", cpEntryListToArray(this.codeLocalVariableTableTypeRS), Codec.UNSIGNED5);
/*      */     
/*  793 */     out.write(encodedBand);
/*  794 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from code_LocalVariableTable_type_RS[" + this.codeLocalVariableTableTypeRS
/*  795 */         .size() + "]");
/*      */     
/*  797 */     encodedBand = encodeBandInt("code_LocalVariableTable_slot", this.codeLocalVariableTableSlot.toArray(), Codec.UNSIGNED5);
/*      */     
/*  799 */     out.write(encodedBand);
/*  800 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from code_LocalVariableTable_slot[" + this.codeLocalVariableTableSlot
/*  801 */         .size() + "]");
/*      */     
/*  803 */     encodedBand = encodeBandInt("code_LocalVariableTypeTable_N", this.codeLocalVariableTypeTableN.toArray(), Codec.UNSIGNED5);
/*      */     
/*  805 */     out.write(encodedBand);
/*  806 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from code_LocalVariableTypeTable_N[" + this.codeLocalVariableTypeTableN
/*  807 */         .size() + "]");
/*      */     
/*  809 */     encodedBand = encodeBandInt("code_LocalVariableTypeTable_bci_P", 
/*  810 */         integerListToArray(this.codeLocalVariableTypeTableBciP), Codec.BCI5);
/*  811 */     out.write(encodedBand);
/*  812 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from code_LocalVariableTypeTable_bci_P[" + this.codeLocalVariableTypeTableBciP
/*  813 */         .size() + "]");
/*      */     
/*  815 */     encodedBand = encodeBandInt("code_LocalVariableTypeTable_span_O", 
/*  816 */         integerListToArray(this.codeLocalVariableTypeTableSpanO), Codec.BRANCH5);
/*  817 */     out.write(encodedBand);
/*  818 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from code_LocalVariableTypeTable_span_O[" + this.codeLocalVariableTypeTableSpanO
/*  819 */         .size() + "]");
/*      */     
/*  821 */     encodedBand = encodeBandInt("code_LocalVariableTypeTable_name_RU", 
/*  822 */         cpEntryListToArray(this.codeLocalVariableTypeTableNameRU), Codec.UNSIGNED5);
/*  823 */     out.write(encodedBand);
/*  824 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from code_LocalVariableTypeTable_name_RU[" + this.codeLocalVariableTypeTableNameRU
/*  825 */         .size() + "]");
/*      */     
/*  827 */     encodedBand = encodeBandInt("code_LocalVariableTypeTable_type_RS", 
/*  828 */         cpEntryListToArray(this.codeLocalVariableTypeTableTypeRS), Codec.UNSIGNED5);
/*  829 */     out.write(encodedBand);
/*  830 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from code_LocalVariableTypeTable_type_RS[" + this.codeLocalVariableTypeTableTypeRS
/*  831 */         .size() + "]");
/*      */     
/*  833 */     encodedBand = encodeBandInt("code_LocalVariableTypeTable_slot", this.codeLocalVariableTypeTableSlot.toArray(), Codec.UNSIGNED5);
/*      */     
/*  835 */     out.write(encodedBand);
/*  836 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from code_LocalVariableTypeTable_slot[" + this.codeLocalVariableTypeTableSlot
/*  837 */         .size() + "]");
/*      */     
/*  839 */     for (NewAttributeBands bands : this.codeAttributeBands) {
/*  840 */       bands.pack(out);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void addMethod(int flags, String name, String desc, String signature, String[] exceptions) {
/*  846 */     CPNameAndType nt = this.cpBands.getCPNameAndType(name, desc);
/*  847 */     this.tempMethodDesc.add(nt);
/*  848 */     if (signature != null) {
/*  849 */       this.methodSignature.add(this.cpBands.getCPSignature(signature));
/*  850 */       flags |= 0x80000;
/*      */     } 
/*  852 */     if (exceptions != null) {
/*  853 */       this.methodExceptionNumber.add(exceptions.length);
/*  854 */       for (String exception : exceptions) {
/*  855 */         this.methodExceptionClasses.add(this.cpBands.getCPClass(exception));
/*      */       }
/*  857 */       flags |= 0x40000;
/*      */     } 
/*  859 */     if ((flags & 0x20000) != 0) {
/*  860 */       flags &= 0xFFFDFFFF;
/*  861 */       flags |= 0x100000;
/*      */     } 
/*  863 */     this.tempMethodFlags.add(Long.valueOf(flags));
/*  864 */     this.numMethodArgs = countArgs(desc);
/*  865 */     if (!this.anySyntheticMethods && (flags & 0x1000) != 0 && this.segment
/*  866 */       .getCurrentClassReader().hasSyntheticAttributes()) {
/*  867 */       this.cpBands.addCPUtf8("Synthetic");
/*  868 */       this.anySyntheticMethods = true;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void endOfMethod() {
/*  873 */     if (this.tempMethodRVPA != null) {
/*  874 */       this.method_RVPA_bands.addParameterAnnotation(this.tempMethodRVPA.numParams, this.tempMethodRVPA.annoN, this.tempMethodRVPA.pairN, this.tempMethodRVPA.typeRS, this.tempMethodRVPA.nameRU, this.tempMethodRVPA.tags, this.tempMethodRVPA.values, this.tempMethodRVPA.caseArrayN, this.tempMethodRVPA.nestTypeRS, this.tempMethodRVPA.nestNameRU, this.tempMethodRVPA.nestPairN);
/*      */ 
/*      */ 
/*      */       
/*  878 */       this.tempMethodRVPA = null;
/*      */     } 
/*  880 */     if (this.tempMethodRIPA != null) {
/*  881 */       this.method_RIPA_bands.addParameterAnnotation(this.tempMethodRIPA.numParams, this.tempMethodRIPA.annoN, this.tempMethodRIPA.pairN, this.tempMethodRIPA.typeRS, this.tempMethodRIPA.nameRU, this.tempMethodRIPA.tags, this.tempMethodRIPA.values, this.tempMethodRIPA.caseArrayN, this.tempMethodRIPA.nestTypeRS, this.tempMethodRIPA.nestNameRU, this.tempMethodRIPA.nestPairN);
/*      */ 
/*      */ 
/*      */       
/*  885 */       this.tempMethodRIPA = null;
/*      */     } 
/*  887 */     if (this.codeFlags.size() > 0) {
/*  888 */       long latestCodeFlag = ((Long)this.codeFlags.get(this.codeFlags.size() - 1)).longValue();
/*  889 */       int latestLocalVariableTableN = this.codeLocalVariableTableN.get(this.codeLocalVariableTableN.size() - 1);
/*  890 */       if (latestCodeFlag == 4L && latestLocalVariableTableN == 0) {
/*  891 */         this.codeLocalVariableTableN.remove(this.codeLocalVariableTableN.size() - 1);
/*  892 */         this.codeFlags.remove(this.codeFlags.size() - 1);
/*  893 */         this.codeFlags.add(Long.valueOf(0L));
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   protected static int countArgs(String descriptor) {
/*  899 */     int bra = descriptor.indexOf('(');
/*  900 */     int ket = descriptor.indexOf(')');
/*  901 */     if (bra == -1 || ket == -1 || ket < bra) {
/*  902 */       throw new IllegalArgumentException("No arguments");
/*      */     }
/*      */     
/*  905 */     boolean inType = false;
/*  906 */     boolean consumingNextType = false;
/*  907 */     int count = 0;
/*  908 */     for (int i = bra + 1; i < ket; i++) {
/*  909 */       char charAt = descriptor.charAt(i);
/*  910 */       if (inType && charAt == ';') {
/*  911 */         inType = false;
/*  912 */         consumingNextType = false;
/*  913 */       } else if (!inType && charAt == 'L') {
/*  914 */         inType = true;
/*  915 */         count++;
/*  916 */       } else if (charAt == '[') {
/*  917 */         consumingNextType = true;
/*  918 */       } else if (!inType) {
/*      */         
/*  920 */         if (consumingNextType) {
/*  921 */           count++;
/*  922 */           consumingNextType = false;
/*  923 */         } else if (charAt == 'D' || charAt == 'J') {
/*  924 */           count += 2;
/*      */         } else {
/*  926 */           count++;
/*      */         } 
/*      */       } 
/*  929 */     }  return count;
/*      */   }
/*      */ 
/*      */   
/*      */   public void endOfClass() {
/*  934 */     int numFields = this.tempFieldDesc.size();
/*  935 */     this.class_field_count[this.index] = numFields;
/*  936 */     this.field_descr[this.index] = new CPNameAndType[numFields];
/*  937 */     this.field_flags[this.index] = new long[numFields];
/*  938 */     for (int i = 0; i < numFields; i++) {
/*  939 */       this.field_descr[this.index][i] = this.tempFieldDesc.get(i);
/*  940 */       this.field_flags[this.index][i] = ((Long)this.tempFieldFlags.get(i)).longValue();
/*      */     } 
/*  942 */     int numMethods = this.tempMethodDesc.size();
/*  943 */     this.class_method_count[this.index] = numMethods;
/*  944 */     this.method_descr[this.index] = new CPNameAndType[numMethods];
/*  945 */     this.method_flags[this.index] = new long[numMethods];
/*  946 */     for (int j = 0; j < numMethods; j++) {
/*  947 */       this.method_descr[this.index][j] = this.tempMethodDesc.get(j);
/*  948 */       this.method_flags[this.index][j] = ((Long)this.tempMethodFlags.get(j)).longValue();
/*      */     } 
/*  950 */     this.tempFieldDesc.clear();
/*  951 */     this.tempFieldFlags.clear();
/*  952 */     this.tempMethodDesc.clear();
/*  953 */     this.tempMethodFlags.clear();
/*  954 */     this.index++;
/*      */   }
/*      */   
/*      */   public void addSourceFile(String source) {
/*  958 */     String implicitSourceFileName = this.class_this[this.index].toString();
/*  959 */     if (implicitSourceFileName.indexOf('$') != -1) {
/*  960 */       implicitSourceFileName = implicitSourceFileName.substring(0, implicitSourceFileName.indexOf('$'));
/*      */     }
/*  962 */     implicitSourceFileName = implicitSourceFileName.substring(implicitSourceFileName.lastIndexOf('/') + 1) + ".java";
/*      */     
/*  964 */     if (source.equals(implicitSourceFileName)) {
/*  965 */       this.classSourceFile.add(null);
/*      */     } else {
/*  967 */       this.classSourceFile.add(this.cpBands.getCPUtf8(source));
/*      */     } 
/*  969 */     this.class_flags[this.index] = this.class_flags[this.index] | 0x20000L;
/*      */   }
/*      */   
/*      */   public void addEnclosingMethod(String owner, String name, String desc) {
/*  973 */     this.class_flags[this.index] = this.class_flags[this.index] | 0x40000L;
/*  974 */     this.classEnclosingMethodClass.add(this.cpBands.getCPClass(owner));
/*  975 */     this.classEnclosingMethodDesc.add((name == null) ? null : this.cpBands.getCPNameAndType(name, desc));
/*      */   }
/*      */ 
/*      */   
/*      */   public void addClassAttribute(NewAttribute attribute) {
/*  980 */     String attributeName = attribute.type;
/*  981 */     for (NewAttributeBands bands : this.classAttributeBands) {
/*  982 */       if (bands.getAttributeName().equals(attributeName)) {
/*  983 */         bands.addAttribute(attribute);
/*  984 */         int flagIndex = bands.getFlagIndex();
/*  985 */         this.class_flags[this.index] = this.class_flags[this.index] | (1 << flagIndex);
/*      */         return;
/*      */       } 
/*      */     } 
/*  989 */     throw new IllegalArgumentException("No suitable definition for " + attributeName);
/*      */   }
/*      */   
/*      */   public void addFieldAttribute(NewAttribute attribute) {
/*  993 */     String attributeName = attribute.type;
/*  994 */     for (NewAttributeBands bands : this.fieldAttributeBands) {
/*  995 */       if (bands.getAttributeName().equals(attributeName)) {
/*  996 */         bands.addAttribute(attribute);
/*  997 */         int flagIndex = bands.getFlagIndex();
/*  998 */         Long flags = this.tempFieldFlags.remove(this.tempFieldFlags.size() - 1);
/*  999 */         this.tempFieldFlags.add(Long.valueOf(flags.longValue() | (1 << flagIndex)));
/*      */         return;
/*      */       } 
/*      */     } 
/* 1003 */     throw new IllegalArgumentException("No suitable definition for " + attributeName);
/*      */   }
/*      */   
/*      */   public void addMethodAttribute(NewAttribute attribute) {
/* 1007 */     String attributeName = attribute.type;
/* 1008 */     for (NewAttributeBands bands : this.methodAttributeBands) {
/* 1009 */       if (bands.getAttributeName().equals(attributeName)) {
/* 1010 */         bands.addAttribute(attribute);
/* 1011 */         int flagIndex = bands.getFlagIndex();
/* 1012 */         Long flags = this.tempMethodFlags.remove(this.tempMethodFlags.size() - 1);
/* 1013 */         this.tempMethodFlags.add(Long.valueOf(flags.longValue() | (1 << flagIndex)));
/*      */         return;
/*      */       } 
/*      */     } 
/* 1017 */     throw new IllegalArgumentException("No suitable definition for " + attributeName);
/*      */   }
/*      */   
/*      */   public void addCodeAttribute(NewAttribute attribute) {
/* 1021 */     String attributeName = attribute.type;
/* 1022 */     for (NewAttributeBands bands : this.codeAttributeBands) {
/* 1023 */       if (bands.getAttributeName().equals(attributeName)) {
/* 1024 */         bands.addAttribute(attribute);
/* 1025 */         int flagIndex = bands.getFlagIndex();
/* 1026 */         Long flags = this.codeFlags.remove(this.codeFlags.size() - 1);
/* 1027 */         this.codeFlags.add(Long.valueOf(flags.longValue() | (1 << flagIndex)));
/*      */         return;
/*      */       } 
/*      */     } 
/* 1031 */     throw new IllegalArgumentException("No suitable definition for " + attributeName);
/*      */   }
/*      */   
/*      */   public void addMaxStack(int maxStack, int maxLocals) {
/* 1035 */     Long latestFlag = this.tempMethodFlags.remove(this.tempMethodFlags.size() - 1);
/* 1036 */     Long newFlag = Long.valueOf((latestFlag.intValue() | 0x20000));
/* 1037 */     this.tempMethodFlags.add(newFlag);
/* 1038 */     this.codeMaxStack.add(maxStack);
/* 1039 */     if ((newFlag.longValue() & 0x8L) == 0L) {
/* 1040 */       maxLocals--;
/*      */     }
/* 1042 */     maxLocals -= this.numMethodArgs;
/* 1043 */     this.codeMaxLocals.add(maxLocals);
/*      */   }
/*      */   
/*      */   public void addCode() {
/* 1047 */     this.codeHandlerCount.add(0);
/* 1048 */     if (!this.stripDebug) {
/* 1049 */       this.codeFlags.add(Long.valueOf(4L));
/* 1050 */       this.codeLocalVariableTableN.add(0);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void addHandler(Label start, Label end, Label handler, String type) {
/* 1055 */     int handlers = this.codeHandlerCount.remove(this.codeHandlerCount.size() - 1);
/* 1056 */     this.codeHandlerCount.add(handlers + 1);
/* 1057 */     this.codeHandlerStartP.add(start);
/* 1058 */     this.codeHandlerEndPO.add(end);
/* 1059 */     this.codeHandlerCatchPO.add(handler);
/* 1060 */     this.codeHandlerClass.add((type == null) ? null : this.cpBands.getCPClass(type));
/*      */   }
/*      */   
/*      */   public void addLineNumber(int line, Label start) {
/* 1064 */     Long latestCodeFlag = this.codeFlags.get(this.codeFlags.size() - 1);
/* 1065 */     if ((latestCodeFlag.intValue() & 0x2) == 0) {
/* 1066 */       this.codeFlags.remove(this.codeFlags.size() - 1);
/* 1067 */       this.codeFlags.add(Long.valueOf((latestCodeFlag.intValue() | 0x2)));
/* 1068 */       this.codeLineNumberTableN.add(1);
/*      */     } else {
/* 1070 */       this.codeLineNumberTableN.increment(this.codeLineNumberTableN.size() - 1);
/*      */     } 
/* 1072 */     this.codeLineNumberTableLine.add(line);
/* 1073 */     this.codeLineNumberTableBciP.add(start);
/*      */   }
/*      */ 
/*      */   
/*      */   public void addLocalVariable(String name, String desc, String signature, Label start, Label end, int indx) {
/* 1078 */     if (signature != null) {
/* 1079 */       Long latestCodeFlag = this.codeFlags.get(this.codeFlags.size() - 1);
/* 1080 */       if ((latestCodeFlag.intValue() & 0x8) == 0) {
/* 1081 */         this.codeFlags.remove(this.codeFlags.size() - 1);
/* 1082 */         this.codeFlags.add(Long.valueOf((latestCodeFlag.intValue() | 0x8)));
/* 1083 */         this.codeLocalVariableTypeTableN.add(1);
/*      */       } else {
/* 1085 */         this.codeLocalVariableTypeTableN.increment(this.codeLocalVariableTypeTableN.size() - 1);
/*      */       } 
/* 1087 */       this.codeLocalVariableTypeTableBciP.add(start);
/* 1088 */       this.codeLocalVariableTypeTableSpanO.add(end);
/* 1089 */       this.codeLocalVariableTypeTableNameRU.add(this.cpBands.getCPUtf8(name));
/* 1090 */       this.codeLocalVariableTypeTableTypeRS.add(this.cpBands.getCPSignature(signature));
/* 1091 */       this.codeLocalVariableTypeTableSlot.add(indx);
/*      */     } 
/*      */     
/* 1094 */     this.codeLocalVariableTableN.increment(this.codeLocalVariableTableN.size() - 1);
/* 1095 */     this.codeLocalVariableTableBciP.add(start);
/* 1096 */     this.codeLocalVariableTableSpanO.add(end);
/* 1097 */     this.codeLocalVariableTableNameRU.add(this.cpBands.getCPUtf8(name));
/* 1098 */     this.codeLocalVariableTableTypeRS.add(this.cpBands.getCPSignature(desc));
/* 1099 */     this.codeLocalVariableTableSlot.add(indx);
/*      */   }
/*      */   
/*      */   public void doBciRenumbering(IntList bciRenumbering, Map<Label, Integer> labelsToOffsets) {
/* 1103 */     renumberBci(this.codeLineNumberTableBciP, bciRenumbering, labelsToOffsets);
/* 1104 */     renumberBci(this.codeLocalVariableTableBciP, bciRenumbering, labelsToOffsets);
/* 1105 */     renumberOffsetBci(this.codeLocalVariableTableBciP, this.codeLocalVariableTableSpanO, bciRenumbering, labelsToOffsets);
/* 1106 */     renumberBci(this.codeLocalVariableTypeTableBciP, bciRenumbering, labelsToOffsets);
/* 1107 */     renumberOffsetBci(this.codeLocalVariableTypeTableBciP, this.codeLocalVariableTypeTableSpanO, bciRenumbering, labelsToOffsets);
/*      */     
/* 1109 */     renumberBci(this.codeHandlerStartP, bciRenumbering, labelsToOffsets);
/* 1110 */     renumberOffsetBci(this.codeHandlerStartP, this.codeHandlerEndPO, bciRenumbering, labelsToOffsets);
/* 1111 */     renumberDoubleOffsetBci(this.codeHandlerStartP, this.codeHandlerEndPO, this.codeHandlerCatchPO, bciRenumbering, labelsToOffsets);
/*      */ 
/*      */     
/* 1114 */     for (NewAttributeBands newAttributeBandSet : this.classAttributeBands) {
/* 1115 */       newAttributeBandSet.renumberBci(bciRenumbering, labelsToOffsets);
/*      */     }
/* 1117 */     for (NewAttributeBands newAttributeBandSet : this.methodAttributeBands) {
/* 1118 */       newAttributeBandSet.renumberBci(bciRenumbering, labelsToOffsets);
/*      */     }
/* 1120 */     for (NewAttributeBands newAttributeBandSet : this.fieldAttributeBands) {
/* 1121 */       newAttributeBandSet.renumberBci(bciRenumbering, labelsToOffsets);
/*      */     }
/* 1123 */     for (NewAttributeBands newAttributeBandSet : this.codeAttributeBands) {
/* 1124 */       newAttributeBandSet.renumberBci(bciRenumbering, labelsToOffsets);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private void renumberBci(List<Integer> list, IntList bciRenumbering, Map<Label, Integer> labelsToOffsets) {
/* 1130 */     for (int i = list.size() - 1; i >= 0; i--) {
/* 1131 */       Object label = list.get(i);
/* 1132 */       if (label instanceof Integer) {
/*      */         break;
/*      */       }
/* 1135 */       if (label instanceof Label) {
/* 1136 */         list.remove(i);
/* 1137 */         Integer bytecodeIndex = labelsToOffsets.get(label);
/* 1138 */         list.add(i, Integer.valueOf(bciRenumbering.get(bytecodeIndex.intValue())));
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void renumberOffsetBci(List<Integer> relative, List<Integer> list, IntList bciRenumbering, Map<Label, Integer> labelsToOffsets) {
/* 1145 */     for (int i = list.size() - 1; i >= 0; i--) {
/* 1146 */       Object label = list.get(i);
/* 1147 */       if (label instanceof Integer) {
/*      */         break;
/*      */       }
/* 1150 */       if (label instanceof Label) {
/* 1151 */         list.remove(i);
/* 1152 */         Integer bytecodeIndex = labelsToOffsets.get(label);
/*      */         
/* 1154 */         Integer renumberedOffset = Integer.valueOf(bciRenumbering.get(bytecodeIndex.intValue()) - ((Integer)relative.get(i)).intValue());
/* 1155 */         list.add(i, renumberedOffset);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void renumberDoubleOffsetBci(List<Integer> relative, List<Integer> firstOffset, List<Object> list, IntList bciRenumbering, Map<Label, Integer> labelsToOffsets) {
/* 1163 */     for (int i = list.size() - 1; i >= 0; i--) {
/* 1164 */       Object label = list.get(i);
/* 1165 */       if (label instanceof Integer) {
/*      */         break;
/*      */       }
/* 1168 */       if (label instanceof Label) {
/* 1169 */         list.remove(i);
/* 1170 */         Integer bytecodeIndex = labelsToOffsets.get(label);
/* 1171 */         Integer renumberedOffset = Integer.valueOf(bciRenumbering.get(bytecodeIndex.intValue()) - ((Integer)relative
/* 1172 */             .get(i)).intValue() - ((Integer)firstOffset.get(i)).intValue());
/* 1173 */         list.add(i, renumberedOffset);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean isAnySyntheticClasses() {
/* 1179 */     return this.anySyntheticClasses;
/*      */   }
/*      */   
/*      */   public boolean isAnySyntheticFields() {
/* 1183 */     return this.anySyntheticFields;
/*      */   }
/*      */   
/*      */   public boolean isAnySyntheticMethods() {
/* 1187 */     return this.anySyntheticMethods;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addParameterAnnotation(int parameter, String desc, boolean visible, List<String> nameRU, List<String> tags, List<Object> values, List<Integer> caseArrayN, List<String> nestTypeRS, List<String> nestNameRU, List<Integer> nestPairN) {
/* 1194 */     if (visible) {
/* 1195 */       if (this.tempMethodRVPA == null) {
/* 1196 */         this.tempMethodRVPA = new TempParamAnnotation(this.numMethodArgs);
/* 1197 */         this.tempMethodRVPA.addParameterAnnotation(parameter, desc, nameRU, tags, values, caseArrayN, nestTypeRS, nestNameRU, nestPairN);
/*      */       } 
/*      */       
/* 1200 */       Long flag = this.tempMethodFlags.remove(this.tempMethodFlags.size() - 1);
/* 1201 */       this.tempMethodFlags.add(Long.valueOf(flag.longValue() | 0x800000L));
/*      */     } else {
/* 1203 */       if (this.tempMethodRIPA == null) {
/* 1204 */         this.tempMethodRIPA = new TempParamAnnotation(this.numMethodArgs);
/* 1205 */         this.tempMethodRIPA.addParameterAnnotation(parameter, desc, nameRU, tags, values, caseArrayN, nestTypeRS, nestNameRU, nestPairN);
/*      */       } 
/*      */       
/* 1208 */       Long flag = this.tempMethodFlags.remove(this.tempMethodFlags.size() - 1);
/* 1209 */       this.tempMethodFlags.add(Long.valueOf(flag.longValue() | 0x1000000L));
/*      */     } 
/*      */   }
/*      */   
/*      */   private static class TempParamAnnotation
/*      */   {
/*      */     int numParams;
/*      */     int[] annoN;
/* 1217 */     IntList pairN = new IntList();
/* 1218 */     List<String> typeRS = new ArrayList<>();
/* 1219 */     List<String> nameRU = new ArrayList<>();
/* 1220 */     List<String> tags = new ArrayList<>();
/* 1221 */     List<Object> values = new ArrayList();
/* 1222 */     List<Integer> caseArrayN = new ArrayList<>();
/* 1223 */     List<String> nestTypeRS = new ArrayList<>();
/* 1224 */     List<String> nestNameRU = new ArrayList<>();
/* 1225 */     List<Integer> nestPairN = new ArrayList<>();
/*      */     
/*      */     public TempParamAnnotation(int numParams) {
/* 1228 */       this.numParams = numParams;
/* 1229 */       this.annoN = new int[numParams];
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void addParameterAnnotation(int parameter, String desc, List<String> nameRU, List<String> tags, List<Object> values, List<Integer> caseArrayN, List<String> nestTypeRS, List<String> nestNameRU, List<Integer> nestPairN) {
/* 1235 */       this.annoN[parameter] = this.annoN[parameter] + 1;
/* 1236 */       this.typeRS.add(desc);
/* 1237 */       this.pairN.add(nameRU.size());
/* 1238 */       this.nameRU.addAll(nameRU);
/* 1239 */       this.tags.addAll(tags);
/* 1240 */       this.values.addAll(values);
/* 1241 */       this.caseArrayN.addAll(caseArrayN);
/* 1242 */       this.nestTypeRS.addAll(nestTypeRS);
/* 1243 */       this.nestNameRU.addAll(nestNameRU);
/* 1244 */       this.nestPairN.addAll(nestPairN);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void addAnnotation(int context, String desc, boolean visible, List<String> nameRU, List<String> tags, List<Object> values, List<Integer> caseArrayN, List<String> nestTypeRS, List<String> nestNameRU, List<Integer> nestPairN) {
/*      */     Long flag;
/* 1251 */     switch (context) {
/*      */       case 0:
/* 1253 */         if (visible) {
/* 1254 */           this.class_RVA_bands.addAnnotation(desc, nameRU, tags, values, caseArrayN, nestTypeRS, nestNameRU, nestPairN);
/*      */           
/* 1256 */           if ((this.class_flags[this.index] & 0x200000L) != 0L) {
/* 1257 */             this.class_RVA_bands.incrementAnnoN(); break;
/*      */           } 
/* 1259 */           this.class_RVA_bands.newEntryInAnnoN();
/* 1260 */           this.class_flags[this.index] = this.class_flags[this.index] | 0x200000L;
/*      */           break;
/*      */         } 
/* 1263 */         this.class_RIA_bands.addAnnotation(desc, nameRU, tags, values, caseArrayN, nestTypeRS, nestNameRU, nestPairN);
/*      */         
/* 1265 */         if ((this.class_flags[this.index] & 0x400000L) != 0L) {
/* 1266 */           this.class_RIA_bands.incrementAnnoN(); break;
/*      */         } 
/* 1268 */         this.class_RIA_bands.newEntryInAnnoN();
/* 1269 */         this.class_flags[this.index] = this.class_flags[this.index] | 0x400000L;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 1:
/* 1274 */         if (visible) {
/* 1275 */           this.field_RVA_bands.addAnnotation(desc, nameRU, tags, values, caseArrayN, nestTypeRS, nestNameRU, nestPairN);
/*      */           
/* 1277 */           Long long_ = this.tempFieldFlags.remove(this.tempFieldFlags.size() - 1);
/* 1278 */           if ((long_.intValue() & 0x200000) != 0) {
/* 1279 */             this.field_RVA_bands.incrementAnnoN();
/*      */           } else {
/* 1281 */             this.field_RVA_bands.newEntryInAnnoN();
/*      */           } 
/* 1283 */           this.tempFieldFlags.add(Long.valueOf((long_.intValue() | 0x200000))); break;
/*      */         } 
/* 1285 */         this.field_RIA_bands.addAnnotation(desc, nameRU, tags, values, caseArrayN, nestTypeRS, nestNameRU, nestPairN);
/*      */         
/* 1287 */         flag = this.tempFieldFlags.remove(this.tempFieldFlags.size() - 1);
/* 1288 */         if ((flag.intValue() & 0x400000) != 0) {
/* 1289 */           this.field_RIA_bands.incrementAnnoN();
/*      */         } else {
/* 1291 */           this.field_RIA_bands.newEntryInAnnoN();
/*      */         } 
/* 1293 */         this.tempFieldFlags.add(Long.valueOf((flag.intValue() | 0x400000)));
/*      */         break;
/*      */       
/*      */       case 2:
/* 1297 */         if (visible) {
/* 1298 */           this.method_RVA_bands.addAnnotation(desc, nameRU, tags, values, caseArrayN, nestTypeRS, nestNameRU, nestPairN);
/*      */           
/* 1300 */           flag = this.tempMethodFlags.remove(this.tempMethodFlags.size() - 1);
/* 1301 */           if ((flag.intValue() & 0x200000) != 0) {
/* 1302 */             this.method_RVA_bands.incrementAnnoN();
/*      */           } else {
/* 1304 */             this.method_RVA_bands.newEntryInAnnoN();
/*      */           } 
/* 1306 */           this.tempMethodFlags.add(Long.valueOf((flag.intValue() | 0x200000))); break;
/*      */         } 
/* 1308 */         this.method_RIA_bands.addAnnotation(desc, nameRU, tags, values, caseArrayN, nestTypeRS, nestNameRU, nestPairN);
/*      */         
/* 1310 */         flag = this.tempMethodFlags.remove(this.tempMethodFlags.size() - 1);
/* 1311 */         if ((flag.intValue() & 0x400000) != 0) {
/* 1312 */           this.method_RIA_bands.incrementAnnoN();
/*      */         } else {
/* 1314 */           this.method_RIA_bands.newEntryInAnnoN();
/*      */         } 
/* 1316 */         this.tempMethodFlags.add(Long.valueOf((flag.intValue() | 0x400000)));
/*      */         break;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addAnnotationDefault(List<String> nameRU, List<String> tags, List<Object> values, List<Integer> caseArrayN, List<String> nestTypeRS, List<String> nestNameRU, List<Integer> nestPairN) {
/* 1325 */     this.method_AD_bands.addAnnotation(null, nameRU, tags, values, caseArrayN, nestTypeRS, nestNameRU, nestPairN);
/* 1326 */     Long flag = this.tempMethodFlags.remove(this.tempMethodFlags.size() - 1);
/* 1327 */     this.tempMethodFlags.add(Long.valueOf(flag.longValue() | 0x2000000L));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void removeCurrentClass() {
/* 1336 */     if ((this.class_flags[this.index] & 0x20000L) != 0L) {
/* 1337 */       this.classSourceFile.remove(this.classSourceFile.size() - 1);
/*      */     }
/* 1339 */     if ((this.class_flags[this.index] & 0x40000L) != 0L) {
/* 1340 */       this.classEnclosingMethodClass.remove(this.classEnclosingMethodClass.size() - 1);
/* 1341 */       this.classEnclosingMethodDesc.remove(this.classEnclosingMethodDesc.size() - 1);
/*      */     } 
/* 1343 */     if ((this.class_flags[this.index] & 0x80000L) != 0L) {
/* 1344 */       this.classSignature.remove(this.classSignature.size() - 1);
/*      */     }
/* 1346 */     if ((this.class_flags[this.index] & 0x200000L) != 0L) {
/* 1347 */       this.class_RVA_bands.removeLatest();
/*      */     }
/* 1349 */     if ((this.class_flags[this.index] & 0x400000L) != 0L) {
/* 1350 */       this.class_RIA_bands.removeLatest();
/*      */     }
/* 1352 */     for (Long flagsL : this.tempFieldFlags) {
/* 1353 */       long flags = flagsL.longValue();
/* 1354 */       if ((flags & 0x80000L) != 0L) {
/* 1355 */         this.fieldSignature.remove(this.fieldSignature.size() - 1);
/*      */       }
/* 1357 */       if ((flags & 0x20000L) != 0L) {
/* 1358 */         this.fieldConstantValueKQ.remove(this.fieldConstantValueKQ.size() - 1);
/*      */       }
/* 1360 */       if ((flags & 0x200000L) != 0L) {
/* 1361 */         this.field_RVA_bands.removeLatest();
/*      */       }
/* 1363 */       if ((flags & 0x400000L) != 0L) {
/* 1364 */         this.field_RIA_bands.removeLatest();
/*      */       }
/*      */     } 
/* 1367 */     for (Long flagsL : this.tempMethodFlags) {
/* 1368 */       long flags = flagsL.longValue();
/* 1369 */       if ((flags & 0x80000L) != 0L) {
/* 1370 */         this.methodSignature.remove(this.methodSignature.size() - 1);
/*      */       }
/* 1372 */       if ((flags & 0x40000L) != 0L) {
/* 1373 */         int exceptions = this.methodExceptionNumber.remove(this.methodExceptionNumber.size() - 1);
/* 1374 */         for (int i = 0; i < exceptions; i++) {
/* 1375 */           this.methodExceptionClasses.remove(this.methodExceptionClasses.size() - 1);
/*      */         }
/*      */       } 
/* 1378 */       if ((flags & 0x20000L) != 0L) {
/* 1379 */         this.codeMaxLocals.remove(this.codeMaxLocals.size() - 1);
/* 1380 */         this.codeMaxStack.remove(this.codeMaxStack.size() - 1);
/* 1381 */         int handlers = this.codeHandlerCount.remove(this.codeHandlerCount.size() - 1);
/* 1382 */         for (int i = 0; i < handlers; i++) {
/* 1383 */           int index = this.codeHandlerStartP.size() - 1;
/* 1384 */           this.codeHandlerStartP.remove(index);
/* 1385 */           this.codeHandlerEndPO.remove(index);
/* 1386 */           this.codeHandlerCatchPO.remove(index);
/* 1387 */           this.codeHandlerClass.remove(index);
/*      */         } 
/* 1389 */         if (!this.stripDebug) {
/* 1390 */           long cdeFlags = ((Long)this.codeFlags.remove(this.codeFlags.size() - 1)).longValue();
/* 1391 */           int numLocalVariables = this.codeLocalVariableTableN.remove(this.codeLocalVariableTableN.size() - 1);
/* 1392 */           for (int j = 0; j < numLocalVariables; j++) {
/* 1393 */             int location = this.codeLocalVariableTableBciP.size() - 1;
/* 1394 */             this.codeLocalVariableTableBciP.remove(location);
/* 1395 */             this.codeLocalVariableTableSpanO.remove(location);
/* 1396 */             this.codeLocalVariableTableNameRU.remove(location);
/* 1397 */             this.codeLocalVariableTableTypeRS.remove(location);
/* 1398 */             this.codeLocalVariableTableSlot.remove(location);
/*      */           } 
/* 1400 */           if ((cdeFlags & 0x8L) != 0L) {
/*      */             
/* 1402 */             int numLocalVariablesInTypeTable = this.codeLocalVariableTypeTableN.remove(this.codeLocalVariableTypeTableN.size() - 1);
/* 1403 */             for (int k = 0; k < numLocalVariablesInTypeTable; k++) {
/* 1404 */               int location = this.codeLocalVariableTypeTableBciP.size() - 1;
/* 1405 */               this.codeLocalVariableTypeTableBciP.remove(location);
/* 1406 */               this.codeLocalVariableTypeTableSpanO.remove(location);
/* 1407 */               this.codeLocalVariableTypeTableNameRU.remove(location);
/* 1408 */               this.codeLocalVariableTypeTableTypeRS.remove(location);
/* 1409 */               this.codeLocalVariableTypeTableSlot.remove(location);
/*      */             } 
/*      */           } 
/* 1412 */           if ((cdeFlags & 0x2L) != 0L) {
/* 1413 */             int numLineNumbers = this.codeLineNumberTableN.remove(this.codeLineNumberTableN.size() - 1);
/* 1414 */             for (int k = 0; k < numLineNumbers; k++) {
/* 1415 */               int location = this.codeLineNumberTableBciP.size() - 1;
/* 1416 */               this.codeLineNumberTableBciP.remove(location);
/* 1417 */               this.codeLineNumberTableLine.remove(location);
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/* 1422 */       if ((flags & 0x200000L) != 0L) {
/* 1423 */         this.method_RVA_bands.removeLatest();
/*      */       }
/* 1425 */       if ((flags & 0x400000L) != 0L) {
/* 1426 */         this.method_RIA_bands.removeLatest();
/*      */       }
/* 1428 */       if ((flags & 0x800000L) != 0L) {
/* 1429 */         this.method_RVPA_bands.removeLatest();
/*      */       }
/* 1431 */       if ((flags & 0x1000000L) != 0L) {
/* 1432 */         this.method_RIPA_bands.removeLatest();
/*      */       }
/* 1434 */       if ((flags & 0x2000000L) != 0L) {
/* 1435 */         this.method_AD_bands.removeLatest();
/*      */       }
/*      */     } 
/* 1438 */     this.class_this[this.index] = null;
/* 1439 */     this.class_super[this.index] = null;
/* 1440 */     this.class_interface_count[this.index] = 0;
/* 1441 */     this.class_interface[this.index] = null;
/* 1442 */     this.major_versions[this.index] = 0;
/* 1443 */     this.class_flags[this.index] = 0L;
/* 1444 */     this.tempFieldDesc.clear();
/* 1445 */     this.tempFieldFlags.clear();
/* 1446 */     this.tempMethodDesc.clear();
/* 1447 */     this.tempMethodFlags.clear();
/* 1448 */     if (this.index > 0) {
/* 1449 */       this.index--;
/*      */     }
/*      */   }
/*      */   
/*      */   public int numClassesProcessed() {
/* 1454 */     return this.index;
/*      */   }
/*      */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\harmony\pack200\ClassBands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */