/*     */ package org.apache.commons.compress.harmony.unpack200;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.compress.harmony.pack200.Codec;
/*     */ import org.apache.commons.compress.harmony.pack200.Pack200Exception;
/*     */ import org.apache.commons.compress.harmony.unpack200.bytecode.CPClass;
/*     */ import org.apache.commons.compress.harmony.unpack200.bytecode.ClassConstantPool;
/*     */ import org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry;
/*     */ import org.apache.commons.compress.harmony.unpack200.bytecode.ConstantPoolEntry;
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
/*     */ public class IcBands
/*     */   extends BandSet
/*     */ {
/*     */   private IcTuple[] icAll;
/*     */   private final String[] cpUTF8;
/*     */   private final String[] cpClass;
/*     */   private Map<String, IcTuple> thisClassToTuple;
/*     */   private Map<String, List<IcTuple>> outerClassToTuples;
/*     */   
/*     */   public IcBands(Segment segment) {
/*  53 */     super(segment);
/*  54 */     this.cpClass = segment.getCpBands().getCpClass();
/*  55 */     this.cpUTF8 = segment.getCpBands().getCpUTF8();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void read(InputStream in) throws IOException, Pack200Exception {
/*  66 */     int innerClassCount = this.header.getInnerClassCount();
/*  67 */     int[] icThisClassInts = decodeBandInt("ic_this_class", in, Codec.UDELTA5, innerClassCount);
/*  68 */     String[] icThisClass = getReferences(icThisClassInts, this.cpClass);
/*  69 */     int[] icFlags = decodeBandInt("ic_flags", in, Codec.UNSIGNED5, innerClassCount);
/*  70 */     int outerClasses = SegmentUtils.countBit16(icFlags);
/*  71 */     int[] icOuterClassInts = decodeBandInt("ic_outer_class", in, Codec.DELTA5, outerClasses);
/*  72 */     String[] icOuterClass = new String[outerClasses];
/*  73 */     for (int i = 0; i < icOuterClass.length; i++) {
/*  74 */       if (icOuterClassInts[i] == 0) {
/*  75 */         icOuterClass[i] = null;
/*     */       } else {
/*  77 */         icOuterClass[i] = this.cpClass[icOuterClassInts[i] - 1];
/*     */       } 
/*     */     } 
/*  80 */     int[] icNameInts = decodeBandInt("ic_name", in, Codec.DELTA5, outerClasses);
/*  81 */     String[] icName = new String[outerClasses];
/*  82 */     for (int j = 0; j < icName.length; j++) {
/*  83 */       if (icNameInts[j] == 0) {
/*  84 */         icName[j] = null;
/*     */       } else {
/*  86 */         icName[j] = this.cpUTF8[icNameInts[j] - 1];
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  91 */     this.icAll = new IcTuple[icThisClass.length];
/*  92 */     int index = 0;
/*  93 */     for (int k = 0; k < icThisClass.length; k++) {
/*  94 */       String icTupleC = icThisClass[k];
/*  95 */       int icTupleF = icFlags[k];
/*  96 */       String icTupleC2 = null;
/*  97 */       String icTupleN = null;
/*  98 */       int cIndex = icThisClassInts[k];
/*  99 */       int c2Index = -1;
/* 100 */       int nIndex = -1;
/* 101 */       if ((icFlags[k] & 0x10000) != 0) {
/* 102 */         icTupleC2 = icOuterClass[index];
/* 103 */         icTupleN = icName[index];
/* 104 */         c2Index = icOuterClassInts[index] - 1;
/* 105 */         nIndex = icNameInts[index] - 1;
/* 106 */         index++;
/*     */       } 
/* 108 */       this.icAll[k] = new IcTuple(icTupleC, icTupleF, icTupleC2, icTupleN, cIndex, c2Index, nIndex, k);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void unpack() throws IOException, Pack200Exception {
/* 114 */     IcTuple[] allTuples = getIcTuples();
/* 115 */     this.thisClassToTuple = new HashMap<>(allTuples.length);
/* 116 */     this.outerClassToTuples = new HashMap<>(allTuples.length);
/* 117 */     for (IcTuple tuple : allTuples) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 122 */       Object result = this.thisClassToTuple.put(tuple.thisClassString(), tuple);
/* 123 */       if (result != null) {
/* 124 */         throw new Error("Collision detected in <thisClassString, IcTuple> mapping. There are at least two inner clases with the same name.");
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 132 */       if ((!tuple.isAnonymous() && !tuple.outerIsAnonymous()) || tuple.nestedExplicitFlagSet()) {
/*     */ 
/*     */         
/* 135 */         String key = tuple.outerClassString();
/* 136 */         List<IcTuple> bucket = this.outerClassToTuples.get(key);
/* 137 */         if (bucket == null) {
/* 138 */           bucket = new ArrayList<>();
/* 139 */           this.outerClassToTuples.put(key, bucket);
/*     */         } 
/* 141 */         bucket.add(tuple);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public IcTuple[] getIcTuples() {
/* 147 */     return this.icAll;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IcTuple[] getRelevantIcTuples(String className, ClassConstantPool cp) {
/* 158 */     Set<IcTuple> relevantTuplesContains = new HashSet<>();
/* 159 */     List<IcTuple> relevantTuples = new ArrayList<>();
/*     */     
/* 161 */     List<IcTuple> relevantCandidates = this.outerClassToTuples.get(className);
/* 162 */     if (relevantCandidates != null) {
/* 163 */       for (int index = 0; index < relevantCandidates.size(); index++) {
/* 164 */         IcTuple tuple = relevantCandidates.get(index);
/* 165 */         relevantTuplesContains.add(tuple);
/* 166 */         relevantTuples.add(tuple);
/*     */       } 
/*     */     }
/*     */     
/* 170 */     List<ClassFileEntry> entries = cp.entries();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 176 */     for (int eIndex = 0; eIndex < entries.size(); eIndex++) {
/* 177 */       ConstantPoolEntry entry = (ConstantPoolEntry)entries.get(eIndex);
/* 178 */       if (entry instanceof CPClass) {
/* 179 */         CPClass clazz = (CPClass)entry;
/* 180 */         IcTuple relevant = this.thisClassToTuple.get(clazz.name);
/* 181 */         if (relevant != null && relevantTuplesContains.add(relevant)) {
/* 182 */           relevantTuples.add(relevant);
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
/* 193 */     List<IcTuple> tuplesToScan = new ArrayList<>(relevantTuples);
/* 194 */     List<IcTuple> tuplesToAdd = new ArrayList<>();
/*     */     
/* 196 */     while (tuplesToScan.size() > 0) {
/*     */       
/* 198 */       tuplesToAdd.clear(); int index;
/* 199 */       for (index = 0; index < tuplesToScan.size(); index++) {
/* 200 */         IcTuple aRelevantTuple = tuplesToScan.get(index);
/* 201 */         IcTuple relevant = this.thisClassToTuple.get(aRelevantTuple.outerClassString());
/* 202 */         if (relevant != null && !aRelevantTuple.outerIsAnonymous()) {
/* 203 */           tuplesToAdd.add(relevant);
/*     */         }
/*     */       } 
/*     */       
/* 207 */       tuplesToScan.clear();
/* 208 */       for (index = 0; index < tuplesToAdd.size(); index++) {
/* 209 */         IcTuple tuple = tuplesToAdd.get(index);
/* 210 */         if (relevantTuplesContains.add(tuple)) {
/* 211 */           relevantTuples.add(tuple);
/* 212 */           tuplesToScan.add(tuple);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 221 */     relevantTuples.sort((arg0, arg1) -> {
/*     */           Integer index1 = Integer.valueOf(arg0.getTupleIndex());
/*     */           
/*     */           Integer index2 = Integer.valueOf(arg1.getTupleIndex());
/*     */           return index1.compareTo(index2);
/*     */         });
/* 227 */     return relevantTuples.<IcTuple>toArray(IcTuple.EMPTY_ARRAY);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\harmon\\unpack200\IcBands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */