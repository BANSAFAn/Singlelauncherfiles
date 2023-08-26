/*     */ package org.apache.commons.compress.harmony.pack200;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
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
/*  34 */   private final Set<IcTuple> innerClasses = new TreeSet<>();
/*     */   private final CpBands cpBands;
/*  36 */   private int bit16Count = 0;
/*  37 */   private final Map<String, List<IcTuple>> outerToInner = new HashMap<>();
/*     */   
/*     */   public IcBands(SegmentHeader segmentHeader, CpBands cpBands, int effort) {
/*  40 */     super(effort, segmentHeader);
/*  41 */     this.cpBands = cpBands;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void finaliseBands() {
/*  49 */     this.segmentHeader.setIc_count(this.innerClasses.size());
/*     */   }
/*     */ 
/*     */   
/*     */   public void pack(OutputStream outputStream) throws IOException, Pack200Exception {
/*  54 */     PackingUtils.log("Writing internal class bands...");
/*  55 */     int[] ic_this_class = new int[this.innerClasses.size()];
/*  56 */     int[] ic_flags = new int[this.innerClasses.size()];
/*  57 */     int[] ic_outer_class = new int[this.bit16Count];
/*  58 */     int[] ic_name = new int[this.bit16Count];
/*     */     
/*  60 */     int index2 = 0;
/*  61 */     List<IcTuple> innerClassesList = new ArrayList<>(this.innerClasses);
/*  62 */     for (int i = 0; i < ic_this_class.length; i++) {
/*  63 */       IcTuple icTuple = innerClassesList.get(i);
/*  64 */       ic_this_class[i] = icTuple.C.getIndex();
/*  65 */       ic_flags[i] = icTuple.F;
/*  66 */       if ((icTuple.F & 0x10000) != 0) {
/*  67 */         ic_outer_class[index2] = (icTuple.C2 == null) ? 0 : (icTuple.C2.getIndex() + 1);
/*  68 */         ic_name[index2] = (icTuple.N == null) ? 0 : (icTuple.N.getIndex() + 1);
/*  69 */         index2++;
/*     */       } 
/*     */     } 
/*  72 */     byte[] encodedBand = encodeBandInt("ic_this_class", ic_this_class, Codec.UDELTA5);
/*  73 */     outputStream.write(encodedBand);
/*  74 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from ic_this_class[" + ic_this_class.length + "]");
/*     */     
/*  76 */     encodedBand = encodeBandInt("ic_flags", ic_flags, Codec.UNSIGNED5);
/*  77 */     outputStream.write(encodedBand);
/*  78 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from ic_flags[" + ic_flags.length + "]");
/*     */     
/*  80 */     encodedBand = encodeBandInt("ic_outer_class", ic_outer_class, Codec.DELTA5);
/*  81 */     outputStream.write(encodedBand);
/*  82 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from ic_outer_class[" + ic_outer_class.length + "]");
/*     */     
/*  84 */     encodedBand = encodeBandInt("ic_name", ic_name, Codec.DELTA5);
/*  85 */     outputStream.write(encodedBand);
/*  86 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from ic_name[" + ic_name.length + "]");
/*     */   }
/*     */   
/*     */   public void addInnerClass(String name, String outerName, String innerName, int flags) {
/*  90 */     if (outerName != null || innerName != null) {
/*  91 */       if (namesArePredictable(name, outerName, innerName)) {
/*  92 */         IcTuple innerClass = new IcTuple(this.cpBands.getCPClass(name), flags, null, null);
/*  93 */         addToMap(outerName, innerClass);
/*  94 */         this.innerClasses.add(innerClass);
/*     */       } else {
/*  96 */         flags |= 0x10000;
/*     */         
/*  98 */         IcTuple icTuple = new IcTuple(this.cpBands.getCPClass(name), flags, this.cpBands.getCPClass(outerName), this.cpBands.getCPUtf8(innerName));
/*  99 */         boolean added = this.innerClasses.add(icTuple);
/* 100 */         if (added) {
/* 101 */           this.bit16Count++;
/* 102 */           addToMap(outerName, icTuple);
/*     */         } 
/*     */       } 
/*     */     } else {
/* 106 */       IcTuple innerClass = new IcTuple(this.cpBands.getCPClass(name), flags, null, null);
/* 107 */       addToMap(getOuter(name), innerClass);
/* 108 */       this.innerClasses.add(innerClass);
/*     */     } 
/*     */   }
/*     */   
/*     */   public List<IcTuple> getInnerClassesForOuter(String outerClassName) {
/* 113 */     return this.outerToInner.get(outerClassName);
/*     */   }
/*     */   
/*     */   private String getOuter(String name) {
/* 117 */     return name.substring(0, name.lastIndexOf('$'));
/*     */   }
/*     */   
/*     */   private void addToMap(String outerName, IcTuple icTuple) {
/* 121 */     List<IcTuple> tuples = this.outerToInner.get(outerName);
/* 122 */     if (tuples == null) {
/* 123 */       tuples = new ArrayList<>();
/* 124 */       this.outerToInner.put(outerName, tuples);
/* 125 */       tuples.add(icTuple);
/*     */     } else {
/* 127 */       for (IcTuple tuple : tuples) {
/* 128 */         if (icTuple.equals(tuple)) {
/*     */           return;
/*     */         }
/*     */       } 
/* 132 */       tuples.add(icTuple);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean namesArePredictable(String name, String outerName, String innerName) {
/* 138 */     return (name.equals(outerName + '$' + innerName) && innerName.indexOf('$') == -1);
/*     */   }
/*     */   
/*     */   class IcTuple
/*     */     implements Comparable<IcTuple> {
/*     */     protected CPClass C;
/*     */     protected int F;
/*     */     protected CPClass C2;
/*     */     protected CPUTF8 N;
/*     */     
/*     */     public IcTuple(CPClass C, int F, CPClass C2, CPUTF8 N) {
/* 149 */       this.C = C;
/* 150 */       this.F = F;
/* 151 */       this.C2 = C2;
/* 152 */       this.N = N;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 157 */       if (o instanceof IcTuple) {
/* 158 */         IcTuple icT = (IcTuple)o;
/* 159 */         return (this.C.equals(icT.C) && this.F == icT.F && Objects.equals(this.C2, icT.C2) && 
/* 160 */           Objects.equals(this.N, icT.N));
/*     */       } 
/* 162 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 167 */       return this.C.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public int compareTo(IcTuple arg0) {
/* 172 */       return this.C.compareTo(arg0.C);
/*     */     }
/*     */     
/*     */     public boolean isAnonymous() {
/* 176 */       String className = this.C.toString();
/* 177 */       String innerName = className.substring(className.lastIndexOf('$') + 1);
/* 178 */       return Character.isDigit(innerName.charAt(0));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public IcTuple getIcTuple(CPClass inner) {
/* 184 */     for (IcTuple icTuple : this.innerClasses) {
/* 185 */       if (icTuple.C.equals(inner)) {
/* 186 */         return icTuple;
/*     */       }
/*     */     } 
/* 189 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\harmony\pack200\IcBands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */