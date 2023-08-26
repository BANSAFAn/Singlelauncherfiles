/*     */ package org.apache.commons.compress.harmony.pack200;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.objectweb.asm.AnnotationVisitor;
/*     */ import org.objectweb.asm.Attribute;
/*     */ import org.objectweb.asm.ClassVisitor;
/*     */ import org.objectweb.asm.FieldVisitor;
/*     */ import org.objectweb.asm.Label;
/*     */ import org.objectweb.asm.MethodVisitor;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Segment
/*     */   extends ClassVisitor
/*     */ {
/*  43 */   public static int ASM_API = 262144;
/*     */   
/*     */   public Segment() {
/*  46 */     super(ASM_API);
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
/*  57 */     this.fieldVisitor = new SegmentFieldVisitor();
/*  58 */     this.methodVisitor = new SegmentMethodVisitor();
/*     */   }
/*     */   
/*     */   private SegmentHeader segmentHeader;
/*     */   private CpBands cpBands;
/*     */   private AttributeDefinitionBands attributeDefinitionBands;
/*     */   private IcBands icBands;
/*     */   private ClassBands classBands;
/*     */   private BcBands bcBands;
/*     */   private FileBands fileBands;
/*     */   private final SegmentFieldVisitor fieldVisitor;
/*     */   private final SegmentMethodVisitor methodVisitor;
/*     */   private Pack200ClassReader currentClassReader;
/*     */   private PackingOptions options;
/*     */   private boolean stripDebug;
/*     */   private Attribute[] nonStandardAttributePrototypes;
/*     */   
/*     */   public void pack(Archive.SegmentUnit segmentUnit, OutputStream out, PackingOptions options) throws IOException, Pack200Exception {
/*  76 */     this.options = options;
/*  77 */     this.stripDebug = options.isStripDebug();
/*  78 */     int effort = options.getEffort();
/*  79 */     this.nonStandardAttributePrototypes = options.getUnknownAttributePrototypes();
/*     */     
/*  81 */     PackingUtils.log("Start to pack a new segment with " + segmentUnit.fileListSize() + " files including " + segmentUnit
/*  82 */         .classListSize() + " classes");
/*     */     
/*  84 */     PackingUtils.log("Initialize a header for the segment");
/*  85 */     this.segmentHeader = new SegmentHeader();
/*  86 */     this.segmentHeader.setFile_count(segmentUnit.fileListSize());
/*  87 */     this.segmentHeader.setHave_all_code_flags(!this.stripDebug);
/*  88 */     if (!options.isKeepDeflateHint()) {
/*  89 */       this.segmentHeader.setDeflate_hint("true".equals(options.getDeflateHint()));
/*     */     }
/*     */     
/*  92 */     PackingUtils.log("Setup constant pool bands for the segment");
/*  93 */     this.cpBands = new CpBands(this, effort);
/*     */     
/*  95 */     PackingUtils.log("Setup attribute definition bands for the segment");
/*  96 */     this.attributeDefinitionBands = new AttributeDefinitionBands(this, effort, this.nonStandardAttributePrototypes);
/*     */     
/*  98 */     PackingUtils.log("Setup internal class bands for the segment");
/*  99 */     this.icBands = new IcBands(this.segmentHeader, this.cpBands, effort);
/*     */     
/* 101 */     PackingUtils.log("Setup class bands for the segment");
/* 102 */     this.classBands = new ClassBands(this, segmentUnit.classListSize(), effort, this.stripDebug);
/*     */     
/* 104 */     PackingUtils.log("Setup byte code bands for the segment");
/* 105 */     this.bcBands = new BcBands(this.cpBands, this, effort);
/*     */     
/* 107 */     PackingUtils.log("Setup file bands for the segment");
/* 108 */     this.fileBands = new FileBands(this.cpBands, this.segmentHeader, options, segmentUnit, effort);
/*     */     
/* 110 */     processClasses(segmentUnit, this.nonStandardAttributePrototypes);
/*     */     
/* 112 */     this.cpBands.finaliseBands();
/* 113 */     this.attributeDefinitionBands.finaliseBands();
/* 114 */     this.icBands.finaliseBands();
/* 115 */     this.classBands.finaliseBands();
/* 116 */     this.bcBands.finaliseBands();
/* 117 */     this.fileBands.finaliseBands();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 123 */     ByteArrayOutputStream bandsOutputStream = new ByteArrayOutputStream();
/*     */     
/* 125 */     PackingUtils.log("Packing...");
/* 126 */     int finalNumberOfClasses = this.classBands.numClassesProcessed();
/* 127 */     this.segmentHeader.setClass_count(finalNumberOfClasses);
/* 128 */     this.cpBands.pack(bandsOutputStream);
/* 129 */     if (finalNumberOfClasses > 0) {
/* 130 */       this.attributeDefinitionBands.pack(bandsOutputStream);
/* 131 */       this.icBands.pack(bandsOutputStream);
/* 132 */       this.classBands.pack(bandsOutputStream);
/* 133 */       this.bcBands.pack(bandsOutputStream);
/*     */     } 
/* 135 */     this.fileBands.pack(bandsOutputStream);
/*     */     
/* 137 */     ByteArrayOutputStream headerOutputStream = new ByteArrayOutputStream();
/* 138 */     this.segmentHeader.pack(headerOutputStream);
/*     */     
/* 140 */     headerOutputStream.writeTo(out);
/* 141 */     bandsOutputStream.writeTo(out);
/*     */     
/* 143 */     segmentUnit.addPackedByteAmount(headerOutputStream.size());
/* 144 */     segmentUnit.addPackedByteAmount(bandsOutputStream.size());
/*     */     
/* 146 */     PackingUtils.log("Wrote total of " + segmentUnit.getPackedByteAmount() + " bytes");
/* 147 */     PackingUtils.log("Transmitted " + segmentUnit.fileListSize() + " files of " + segmentUnit.getByteAmount() + " input bytes in a segment of " + segmentUnit
/* 148 */         .getPackedByteAmount() + " bytes");
/*     */   }
/*     */   
/*     */   private void processClasses(Archive.SegmentUnit segmentUnit, Attribute[] attributes) throws Pack200Exception {
/* 152 */     this.segmentHeader.setClass_count(segmentUnit.classListSize());
/* 153 */     for (Pack200ClassReader classReader : segmentUnit.getClassList()) {
/* 154 */       this.currentClassReader = classReader;
/* 155 */       int flags = 0;
/* 156 */       if (this.stripDebug) {
/* 157 */         flags |= 0x2;
/*     */       }
/*     */       try {
/* 160 */         classReader.accept(this, attributes, flags);
/* 161 */       } catch (PassException pe) {
/*     */ 
/*     */         
/* 164 */         this.classBands.removeCurrentClass();
/* 165 */         String name = classReader.getFileName();
/* 166 */         this.options.addPassFile(name);
/* 167 */         this.cpBands.addCPUtf8(name);
/* 168 */         boolean found = false;
/* 169 */         for (Archive.PackingFile file : segmentUnit.getFileList()) {
/* 170 */           if (file.getName().equals(name)) {
/* 171 */             found = true;
/* 172 */             file.setContents(classReader.b);
/*     */             break;
/*     */           } 
/*     */         } 
/* 176 */         if (!found) {
/* 177 */           throw new Pack200Exception("Error passing file " + name);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
/* 186 */     this.bcBands.setCurrentClass(name, superName);
/* 187 */     this.segmentHeader.addMajorVersion(version);
/* 188 */     this.classBands.addClass(version, access, name, signature, superName, interfaces);
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitSource(String source, String debug) {
/* 193 */     if (!this.stripDebug) {
/* 194 */       this.classBands.addSourceFile(source);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitOuterClass(String owner, String name, String desc) {
/* 200 */     this.classBands.addEnclosingMethod(owner, name, desc);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
/* 206 */     return new SegmentAnnotationVisitor(0, desc, visible);
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitAttribute(Attribute attribute) {
/* 211 */     if (attribute.isUnknown()) {
/* 212 */       String action = this.options.getUnknownAttributeAction();
/* 213 */       if (action.equals("pass")) {
/* 214 */         passCurrentClass();
/* 215 */       } else if (action.equals("error")) {
/* 216 */         throw new Error("Unknown attribute encountered");
/*     */       } 
/* 218 */     } else if (attribute instanceof NewAttribute) {
/* 219 */       NewAttribute newAttribute = (NewAttribute)attribute;
/* 220 */       if (newAttribute.isUnknown(0)) {
/* 221 */         String action = this.options.getUnknownClassAttributeAction(newAttribute.type);
/* 222 */         if (action.equals("pass")) {
/* 223 */           passCurrentClass();
/* 224 */         } else if (action.equals("error")) {
/* 225 */           throw new Error("Unknown attribute encountered");
/*     */         } 
/*     */       } 
/* 228 */       this.classBands.addClassAttribute(newAttribute);
/*     */     } else {
/* 230 */       throw new IllegalArgumentException("Unexpected attribute encountered: " + attribute.type);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitInnerClass(String name, String outerName, String innerName, int flags) {
/* 236 */     this.icBands.addInnerClass(name, outerName, innerName, flags);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public FieldVisitor visitField(int flags, String name, String desc, String signature, Object value) {
/* 242 */     this.classBands.addField(flags, name, desc, signature, value);
/* 243 */     return this.fieldVisitor;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodVisitor visitMethod(int flags, String name, String desc, String signature, String[] exceptions) {
/* 249 */     this.classBands.addMethod(flags, name, desc, signature, exceptions);
/* 250 */     return this.methodVisitor;
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitEnd() {
/* 255 */     this.classBands.endOfClass();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public class SegmentMethodVisitor
/*     */     extends MethodVisitor
/*     */   {
/*     */     public SegmentMethodVisitor() {
/* 266 */       super(Segment.ASM_API);
/*     */     }
/*     */ 
/*     */     
/*     */     public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
/* 271 */       return new Segment.SegmentAnnotationVisitor(2, desc, visible);
/*     */     }
/*     */ 
/*     */     
/*     */     public AnnotationVisitor visitAnnotationDefault() {
/* 276 */       return new Segment.SegmentAnnotationVisitor(2);
/*     */     }
/*     */ 
/*     */     
/*     */     public void visitAttribute(Attribute attribute) {
/* 281 */       if (attribute.isUnknown()) {
/* 282 */         String action = Segment.this.options.getUnknownAttributeAction();
/* 283 */         if (action.equals("pass")) {
/* 284 */           Segment.this.passCurrentClass();
/* 285 */         } else if (action.equals("error")) {
/* 286 */           throw new Error("Unknown attribute encountered");
/*     */         } 
/* 288 */       } else if (attribute instanceof NewAttribute) {
/* 289 */         NewAttribute newAttribute = (NewAttribute)attribute;
/* 290 */         if (attribute.isCodeAttribute()) {
/* 291 */           if (newAttribute.isUnknown(3)) {
/* 292 */             String action = Segment.this.options.getUnknownCodeAttributeAction(newAttribute.type);
/* 293 */             if (action.equals("pass")) {
/* 294 */               Segment.this.passCurrentClass();
/* 295 */             } else if (action.equals("error")) {
/* 296 */               throw new Error("Unknown attribute encountered");
/*     */             } 
/*     */           } 
/* 299 */           Segment.this.classBands.addCodeAttribute(newAttribute);
/*     */         } else {
/* 301 */           if (newAttribute.isUnknown(2)) {
/* 302 */             String action = Segment.this.options.getUnknownMethodAttributeAction(newAttribute.type);
/* 303 */             if (action.equals("pass")) {
/* 304 */               Segment.this.passCurrentClass();
/* 305 */             } else if (action.equals("error")) {
/* 306 */               throw new Error("Unknown attribute encountered");
/*     */             } 
/*     */           } 
/* 309 */           Segment.this.classBands.addMethodAttribute(newAttribute);
/*     */         } 
/*     */       } else {
/* 312 */         throw new IllegalArgumentException("Unexpected attribute encountered: " + attribute.type);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void visitCode() {
/* 318 */       Segment.this.classBands.addCode();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void visitFrame(int arg0, int arg1, Object[] arg2, int arg3, Object[] arg4) {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void visitLabel(Label label) {
/* 330 */       Segment.this.bcBands.visitLabel(label);
/*     */     }
/*     */ 
/*     */     
/*     */     public void visitLineNumber(int line, Label start) {
/* 335 */       if (!Segment.this.stripDebug) {
/* 336 */         Segment.this.classBands.addLineNumber(line, start);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
/* 343 */       if (!Segment.this.stripDebug) {
/* 344 */         Segment.this.classBands.addLocalVariable(name, desc, signature, start, end, index);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void visitMaxs(int maxStack, int maxLocals) {
/* 350 */       Segment.this.classBands.addMaxStack(maxStack, maxLocals);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) {
/* 356 */       return new Segment.SegmentAnnotationVisitor(2, parameter, desc, visible);
/*     */     }
/*     */ 
/*     */     
/*     */     public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
/* 361 */       Segment.this.classBands.addHandler(start, end, handler, type);
/*     */     }
/*     */ 
/*     */     
/*     */     public void visitEnd() {
/* 366 */       Segment.this.classBands.endOfMethod();
/* 367 */       Segment.this.bcBands.visitEnd();
/*     */     }
/*     */ 
/*     */     
/*     */     public void visitFieldInsn(int opcode, String owner, String name, String desc) {
/* 372 */       Segment.this.bcBands.visitFieldInsn(opcode, owner, name, desc);
/*     */     }
/*     */ 
/*     */     
/*     */     public void visitIincInsn(int var, int increment) {
/* 377 */       Segment.this.bcBands.visitIincInsn(var, increment);
/*     */     }
/*     */ 
/*     */     
/*     */     public void visitInsn(int opcode) {
/* 382 */       Segment.this.bcBands.visitInsn(opcode);
/*     */     }
/*     */ 
/*     */     
/*     */     public void visitIntInsn(int opcode, int operand) {
/* 387 */       Segment.this.bcBands.visitIntInsn(opcode, operand);
/*     */     }
/*     */ 
/*     */     
/*     */     public void visitJumpInsn(int opcode, Label label) {
/* 392 */       Segment.this.bcBands.visitJumpInsn(opcode, label);
/*     */     }
/*     */ 
/*     */     
/*     */     public void visitLdcInsn(Object cst) {
/* 397 */       Segment.this.bcBands.visitLdcInsn(cst);
/*     */     }
/*     */ 
/*     */     
/*     */     public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
/* 402 */       Segment.this.bcBands.visitLookupSwitchInsn(dflt, keys, labels);
/*     */     }
/*     */ 
/*     */     
/*     */     public void visitMethodInsn(int opcode, String owner, String name, String desc) {
/* 407 */       Segment.this.bcBands.visitMethodInsn(opcode, owner, name, desc);
/*     */     }
/*     */ 
/*     */     
/*     */     public void visitMultiANewArrayInsn(String desc, int dimensions) {
/* 412 */       Segment.this.bcBands.visitMultiANewArrayInsn(desc, dimensions);
/*     */     }
/*     */ 
/*     */     
/*     */     public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
/* 417 */       Segment.this.bcBands.visitTableSwitchInsn(min, max, dflt, labels);
/*     */     }
/*     */ 
/*     */     
/*     */     public void visitTypeInsn(int opcode, String type) {
/* 422 */       Segment.this.bcBands.visitTypeInsn(opcode, type);
/*     */     }
/*     */ 
/*     */     
/*     */     public void visitVarInsn(int opcode, int var) {
/* 427 */       Segment.this.bcBands.visitVarInsn(opcode, var);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassBands getClassBands() {
/* 433 */     return this.classBands;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public class SegmentAnnotationVisitor
/*     */     extends AnnotationVisitor
/*     */   {
/* 441 */     private int context = -1;
/* 442 */     private int parameter = -1;
/*     */     
/*     */     private String desc;
/*     */     private boolean visible;
/* 446 */     private final List<String> nameRU = new ArrayList<>();
/* 447 */     private final List<String> tags = new ArrayList<>();
/* 448 */     private final List<Object> values = new ArrayList();
/* 449 */     private final List<Integer> caseArrayN = new ArrayList<>();
/* 450 */     private final List<String> nestTypeRS = new ArrayList<>();
/* 451 */     private final List<String> nestNameRU = new ArrayList<>();
/* 452 */     private final List<Integer> nestPairN = new ArrayList<>();
/*     */     
/*     */     public SegmentAnnotationVisitor(int context, String desc, boolean visible) {
/* 455 */       super(Segment.ASM_API);
/* 456 */       this.context = context;
/* 457 */       this.desc = desc;
/* 458 */       this.visible = visible;
/*     */     }
/*     */     
/*     */     public SegmentAnnotationVisitor(int context) {
/* 462 */       super(Segment.ASM_API);
/* 463 */       this.context = context;
/*     */     }
/*     */ 
/*     */     
/*     */     public SegmentAnnotationVisitor(int context, int parameter, String desc, boolean visible) {
/* 468 */       super(Segment.ASM_API);
/* 469 */       this.context = context;
/* 470 */       this.parameter = parameter;
/* 471 */       this.desc = desc;
/* 472 */       this.visible = visible;
/*     */     }
/*     */ 
/*     */     
/*     */     public void visit(String name, Object value) {
/* 477 */       if (name == null) {
/* 478 */         name = "";
/*     */       }
/* 480 */       this.nameRU.add(name);
/* 481 */       Segment.this.addValueAndTag(value, this.tags, this.values);
/*     */     }
/*     */ 
/*     */     
/*     */     public AnnotationVisitor visitAnnotation(String name, String desc) {
/* 486 */       this.tags.add("@");
/* 487 */       if (name == null) {
/* 488 */         name = "";
/*     */       }
/* 490 */       this.nameRU.add(name);
/* 491 */       this.nestTypeRS.add(desc);
/* 492 */       this.nestPairN.add(Integer.valueOf(0));
/* 493 */       return new AnnotationVisitor(this.context, this.av)
/*     */         {
/*     */           public void visit(String name, Object value) {
/* 496 */             Integer numPairs = Segment.SegmentAnnotationVisitor.this.nestPairN.remove(Segment.SegmentAnnotationVisitor.this.nestPairN.size() - 1);
/* 497 */             Segment.SegmentAnnotationVisitor.this.nestPairN.add(Integer.valueOf(numPairs.intValue() + 1));
/* 498 */             Segment.SegmentAnnotationVisitor.this.nestNameRU.add(name);
/* 499 */             Segment.this.addValueAndTag(value, Segment.SegmentAnnotationVisitor.this.tags, Segment.SegmentAnnotationVisitor.this.values);
/*     */           }
/*     */ 
/*     */           
/*     */           public AnnotationVisitor visitAnnotation(String arg0, String arg1) {
/* 504 */             throw new UnsupportedOperationException("Not yet supported");
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           public AnnotationVisitor visitArray(String arg0) {
/* 510 */             throw new UnsupportedOperationException("Not yet supported");
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           public void visitEnd() {}
/*     */ 
/*     */ 
/*     */           
/*     */           public void visitEnum(String name, String desc, String value) {
/* 520 */             Integer numPairs = Segment.SegmentAnnotationVisitor.this.nestPairN.remove(Segment.SegmentAnnotationVisitor.this.nestPairN.size() - 1);
/* 521 */             Segment.SegmentAnnotationVisitor.this.nestPairN.add(Integer.valueOf(numPairs.intValue() + 1));
/* 522 */             Segment.SegmentAnnotationVisitor.this.tags.add("e");
/* 523 */             Segment.SegmentAnnotationVisitor.this.nestNameRU.add(name);
/* 524 */             Segment.SegmentAnnotationVisitor.this.values.add(desc);
/* 525 */             Segment.SegmentAnnotationVisitor.this.values.add(value);
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public AnnotationVisitor visitArray(String name) {
/* 532 */       this.tags.add("[");
/* 533 */       if (name == null) {
/* 534 */         name = "";
/*     */       }
/* 536 */       this.nameRU.add(name);
/* 537 */       this.caseArrayN.add(Integer.valueOf(0));
/* 538 */       return new Segment.ArrayVisitor(this.caseArrayN, this.tags, this.nameRU, this.values);
/*     */     }
/*     */ 
/*     */     
/*     */     public void visitEnd() {
/* 543 */       if (this.desc == null) {
/* 544 */         Segment.this.classBands.addAnnotationDefault(this.nameRU, this.tags, this.values, this.caseArrayN, this.nestTypeRS, this.nestNameRU, this.nestPairN);
/*     */       }
/* 546 */       else if (this.parameter != -1) {
/* 547 */         Segment.this.classBands.addParameterAnnotation(this.parameter, this.desc, this.visible, this.nameRU, this.tags, this.values, this.caseArrayN, this.nestTypeRS, this.nestNameRU, this.nestPairN);
/*     */       } else {
/*     */         
/* 550 */         Segment.this.classBands.addAnnotation(this.context, this.desc, this.visible, this.nameRU, this.tags, this.values, this.caseArrayN, this.nestTypeRS, this.nestNameRU, this.nestPairN);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void visitEnum(String name, String desc, String value) {
/* 557 */       this.tags.add("e");
/* 558 */       if (name == null) {
/* 559 */         name = "";
/*     */       }
/* 561 */       this.nameRU.add(name);
/* 562 */       this.values.add(desc);
/* 563 */       this.values.add(value);
/*     */     }
/*     */   }
/*     */   
/*     */   public class ArrayVisitor
/*     */     extends AnnotationVisitor {
/*     */     private final int indexInCaseArrayN;
/*     */     private final List<Integer> caseArrayN;
/*     */     private final List<Object> values;
/*     */     private final List<String> nameRU;
/*     */     private final List<String> tags;
/*     */     
/*     */     public ArrayVisitor(List<Integer> caseArrayN, List<String> tags, List<String> nameRU, List<Object> values) {
/* 576 */       super(Segment.ASM_API);
/*     */       
/* 578 */       this.caseArrayN = caseArrayN;
/* 579 */       this.tags = tags;
/* 580 */       this.nameRU = nameRU;
/* 581 */       this.values = values;
/* 582 */       this.indexInCaseArrayN = caseArrayN.size() - 1;
/*     */     }
/*     */ 
/*     */     
/*     */     public void visit(String name, Object value) {
/* 587 */       Integer numCases = this.caseArrayN.remove(this.indexInCaseArrayN);
/* 588 */       this.caseArrayN.add(this.indexInCaseArrayN, Integer.valueOf(numCases.intValue() + 1));
/* 589 */       if (name == null) {
/* 590 */         name = "";
/*     */       }
/* 592 */       Segment.this.addValueAndTag(value, this.tags, this.values);
/*     */     }
/*     */ 
/*     */     
/*     */     public AnnotationVisitor visitAnnotation(String arg0, String arg1) {
/* 597 */       throw new UnsupportedOperationException("Not yet supported");
/*     */     }
/*     */ 
/*     */     
/*     */     public AnnotationVisitor visitArray(String name) {
/* 602 */       this.tags.add("[");
/* 603 */       if (name == null) {
/* 604 */         name = "";
/*     */       }
/* 606 */       this.nameRU.add(name);
/* 607 */       this.caseArrayN.add(Integer.valueOf(0));
/* 608 */       return new ArrayVisitor(this.caseArrayN, this.tags, this.nameRU, this.values);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void visitEnd() {}
/*     */ 
/*     */     
/*     */     public void visitEnum(String name, String desc, String value) {
/* 617 */       Integer numCases = this.caseArrayN.remove(this.caseArrayN.size() - 1);
/* 618 */       this.caseArrayN.add(Integer.valueOf(numCases.intValue() + 1));
/* 619 */       this.tags.add("e");
/* 620 */       this.values.add(desc);
/* 621 */       this.values.add(value);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public class SegmentFieldVisitor
/*     */     extends FieldVisitor
/*     */   {
/*     */     public SegmentFieldVisitor() {
/* 632 */       super(Segment.ASM_API);
/*     */     }
/*     */ 
/*     */     
/*     */     public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
/* 637 */       return new Segment.SegmentAnnotationVisitor(1, desc, visible);
/*     */     }
/*     */ 
/*     */     
/*     */     public void visitAttribute(Attribute attribute) {
/* 642 */       if (attribute.isUnknown()) {
/* 643 */         String action = Segment.this.options.getUnknownAttributeAction();
/* 644 */         if (action.equals("pass")) {
/* 645 */           Segment.this.passCurrentClass();
/* 646 */         } else if (action.equals("error")) {
/* 647 */           throw new Error("Unknown attribute encountered");
/*     */         } 
/* 649 */       } else if (attribute instanceof NewAttribute) {
/* 650 */         NewAttribute newAttribute = (NewAttribute)attribute;
/* 651 */         if (newAttribute.isUnknown(1)) {
/* 652 */           String action = Segment.this.options.getUnknownFieldAttributeAction(newAttribute.type);
/* 653 */           if (action.equals("pass")) {
/* 654 */             Segment.this.passCurrentClass();
/* 655 */           } else if (action.equals("error")) {
/* 656 */             throw new Error("Unknown attribute encountered");
/*     */           } 
/*     */         } 
/* 659 */         Segment.this.classBands.addFieldAttribute(newAttribute);
/*     */       } else {
/* 661 */         throw new IllegalArgumentException("Unexpected attribute encountered: " + attribute.type);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void visitEnd() {}
/*     */   }
/*     */ 
/*     */   
/*     */   private void addValueAndTag(Object value, List<String> tags, List<Object> values) {
/* 672 */     if (value instanceof Integer) {
/* 673 */       tags.add("I");
/* 674 */       values.add(value);
/* 675 */     } else if (value instanceof Double) {
/* 676 */       tags.add("D");
/* 677 */       values.add(value);
/* 678 */     } else if (value instanceof Float) {
/* 679 */       tags.add("F");
/* 680 */       values.add(value);
/* 681 */     } else if (value instanceof Long) {
/* 682 */       tags.add("J");
/* 683 */       values.add(value);
/* 684 */     } else if (value instanceof Byte) {
/* 685 */       tags.add("B");
/* 686 */       values.add(Integer.valueOf(((Byte)value).intValue()));
/* 687 */     } else if (value instanceof Character) {
/* 688 */       tags.add("C");
/* 689 */       values.add(Integer.valueOf(((Character)value).charValue()));
/* 690 */     } else if (value instanceof Short) {
/* 691 */       tags.add("S");
/* 692 */       values.add(Integer.valueOf(((Short)value).intValue()));
/* 693 */     } else if (value instanceof Boolean) {
/* 694 */       tags.add("Z");
/* 695 */       values.add(Integer.valueOf(((Boolean)value).booleanValue() ? 1 : 0));
/* 696 */     } else if (value instanceof String) {
/* 697 */       tags.add("s");
/* 698 */       values.add(value);
/* 699 */     } else if (value instanceof Type) {
/* 700 */       tags.add("c");
/* 701 */       values.add(((Type)value).toString());
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean lastConstantHadWideIndex() {
/* 706 */     return this.currentClassReader.lastConstantHadWideIndex();
/*     */   }
/*     */   
/*     */   public CpBands getCpBands() {
/* 710 */     return this.cpBands;
/*     */   }
/*     */   
/*     */   public SegmentHeader getSegmentHeader() {
/* 714 */     return this.segmentHeader;
/*     */   }
/*     */   
/*     */   public AttributeDefinitionBands getAttrBands() {
/* 718 */     return this.attributeDefinitionBands;
/*     */   }
/*     */   
/*     */   public IcBands getIcBands() {
/* 722 */     return this.icBands;
/*     */   }
/*     */   
/*     */   public Pack200ClassReader getCurrentClassReader() {
/* 726 */     return this.currentClassReader;
/*     */   }
/*     */   
/*     */   private void passCurrentClass() {
/* 730 */     throw new PassException();
/*     */   }
/*     */   
/*     */   public static class PassException extends RuntimeException {
/*     */     private static final long serialVersionUID = 1L;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\harmony\pack200\Segment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */