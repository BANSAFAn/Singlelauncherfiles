/*     */ package org.apache.commons.compress.harmony.unpack200.bytecode;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.TreeSet;
/*     */ import org.apache.commons.compress.harmony.unpack200.Segment;
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
/*     */ public class ClassConstantPool
/*     */ {
/*  36 */   protected HashSet<ClassFileEntry> entriesContainsSet = new HashSet<>();
/*  37 */   protected HashSet<ClassFileEntry> othersContainsSet = new HashSet<>();
/*     */   
/*  39 */   private final HashSet<ClassFileEntry> mustStartClassPool = new HashSet<>();
/*     */   
/*     */   protected Map<ClassFileEntry, Integer> indexCache;
/*     */   
/*  43 */   private final List<ClassFileEntry> others = new ArrayList<>(500);
/*  44 */   private final List<ClassFileEntry> entries = new ArrayList<>(500);
/*     */   
/*     */   private boolean resolved;
/*     */   
/*     */   public ClassFileEntry add(ClassFileEntry entry) {
/*  49 */     if (entry instanceof ByteCode) {
/*  50 */       return null;
/*     */     }
/*  52 */     if (entry instanceof ConstantPoolEntry) {
/*  53 */       if (this.entriesContainsSet.add(entry)) {
/*  54 */         this.entries.add(entry);
/*     */       }
/*  56 */     } else if (this.othersContainsSet.add(entry)) {
/*  57 */       this.others.add(entry);
/*     */     } 
/*     */     
/*  60 */     return entry;
/*     */   }
/*     */   
/*     */   public void addNestedEntries() {
/*  64 */     boolean added = true;
/*     */ 
/*     */     
/*  67 */     List<ClassFileEntry> parents = new ArrayList<>(512);
/*  68 */     List<ClassFileEntry> children = new ArrayList<>(512);
/*     */ 
/*     */     
/*  71 */     parents.addAll(this.entries);
/*  72 */     parents.addAll(this.others);
/*     */ 
/*     */ 
/*     */     
/*  76 */     while (added || parents.size() > 0) {
/*     */       
/*  78 */       children.clear();
/*     */       
/*  80 */       int entriesOriginalSize = this.entries.size();
/*  81 */       int othersOriginalSize = this.others.size();
/*     */ 
/*     */ 
/*     */       
/*  85 */       for (int indexParents = 0; indexParents < parents.size(); indexParents++) {
/*  86 */         ClassFileEntry entry = parents.get(indexParents);
/*     */ 
/*     */         
/*  89 */         ClassFileEntry[] entryChildren = entry.getNestedClassFileEntries();
/*  90 */         children.addAll(Arrays.asList(entryChildren));
/*     */         
/*  92 */         boolean isAtStart = (entry instanceof ByteCode && ((ByteCode)entry).nestedMustStartClassPool());
/*     */         
/*  94 */         if (isAtStart) {
/*  95 */           this.mustStartClassPool.addAll(Arrays.asList(entryChildren));
/*     */         }
/*     */ 
/*     */         
/*  99 */         add(entry);
/*     */       } 
/*     */       
/* 102 */       added = (this.entries.size() != entriesOriginalSize || this.others.size() != othersOriginalSize);
/*     */ 
/*     */ 
/*     */       
/* 106 */       parents.clear();
/* 107 */       parents.addAll(children);
/*     */     } 
/*     */   }
/*     */   
/*     */   public int indexOf(ClassFileEntry entry) {
/* 112 */     if (!this.resolved) {
/* 113 */       throw new IllegalStateException("Constant pool is not yet resolved; this does not make any sense");
/*     */     }
/* 115 */     if (null == this.indexCache) {
/* 116 */       throw new IllegalStateException("Index cache is not initialized!");
/*     */     }
/* 118 */     Integer entryIndex = this.indexCache.get(entry);
/*     */     
/* 120 */     if (entryIndex != null) {
/* 121 */       return entryIndex.intValue() + 1;
/*     */     }
/* 123 */     return -1;
/*     */   }
/*     */   
/*     */   public int size() {
/* 127 */     return this.entries.size();
/*     */   }
/*     */   
/*     */   public ClassFileEntry get(int i) {
/* 131 */     if (!this.resolved) {
/* 132 */       throw new IllegalStateException("Constant pool is not yet resolved; this does not make any sense");
/*     */     }
/* 134 */     return this.entries.get(--i);
/*     */   }
/*     */   
/*     */   public void resolve(Segment segment) {
/* 138 */     initialSort();
/* 139 */     sortClassPool();
/*     */     
/* 141 */     this.resolved = true;
/*     */     
/* 143 */     this.entries.forEach(entry -> entry.resolve(this));
/* 144 */     this.others.forEach(entry -> entry.resolve(this));
/*     */   }
/*     */ 
/*     */   
/*     */   private void initialSort() {
/* 149 */     TreeSet<ClassFileEntry> inCpAll = new TreeSet<>(Comparator.comparingInt(arg0 -> ((ConstantPoolEntry)arg0).getGlobalIndex()));
/*     */     
/* 151 */     TreeSet<ClassFileEntry> cpUtf8sNotInCpAll = new TreeSet<>(Comparator.comparing(arg0 -> ((CPUTF8)arg0).underlyingString()));
/*     */     
/* 153 */     TreeSet<ClassFileEntry> cpClassesNotInCpAll = new TreeSet<>(Comparator.comparing(arg0 -> ((CPClass)arg0).getName()));
/*     */     
/* 155 */     for (ClassFileEntry entry2 : this.entries) {
/* 156 */       ConstantPoolEntry entry = (ConstantPoolEntry)entry2;
/* 157 */       if (entry.getGlobalIndex() == -1) {
/* 158 */         if (entry instanceof CPUTF8) {
/* 159 */           cpUtf8sNotInCpAll.add(entry); continue;
/* 160 */         }  if (entry instanceof CPClass) {
/* 161 */           cpClassesNotInCpAll.add(entry); continue;
/*     */         } 
/* 163 */         throw new Error("error");
/*     */       } 
/*     */       
/* 166 */       inCpAll.add(entry);
/*     */     } 
/*     */     
/* 169 */     this.entries.clear();
/* 170 */     this.entries.addAll(inCpAll);
/* 171 */     this.entries.addAll(cpUtf8sNotInCpAll);
/* 172 */     this.entries.addAll(cpClassesNotInCpAll);
/*     */   }
/*     */   
/*     */   public List<ClassFileEntry> entries() {
/* 176 */     return Collections.unmodifiableList(this.entries);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void sortClassPool() {
/* 185 */     List<ClassFileEntry> startOfPool = new ArrayList<>(this.entries.size());
/* 186 */     List<ClassFileEntry> finalSort = new ArrayList<>(this.entries.size());
/*     */     
/* 188 */     for (ClassFileEntry entry : this.entries) {
/* 189 */       if (this.mustStartClassPool.contains(entry)) {
/* 190 */         startOfPool.add(entry); continue;
/*     */       } 
/* 192 */       finalSort.add(entry);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 198 */     this.indexCache = new HashMap<>(this.entries.size());
/* 199 */     int index = 0;
/*     */     
/* 201 */     this.entries.clear();
/*     */     
/* 203 */     for (ClassFileEntry entry : startOfPool) {
/* 204 */       this.indexCache.put(entry, Integer.valueOf(index));
/*     */       
/* 206 */       if (entry instanceof CPLong || entry instanceof CPDouble) {
/* 207 */         this.entries.add(entry);
/* 208 */         this.entries.add(entry);
/* 209 */         index += 2; continue;
/*     */       } 
/* 211 */       this.entries.add(entry);
/* 212 */       index++;
/*     */     } 
/*     */ 
/*     */     
/* 216 */     for (ClassFileEntry entry : finalSort) {
/* 217 */       this.indexCache.put(entry, Integer.valueOf(index));
/*     */       
/* 219 */       if (entry instanceof CPLong || entry instanceof CPDouble) {
/* 220 */         this.entries.add(entry);
/* 221 */         this.entries.add(entry);
/* 222 */         index += 2; continue;
/*     */       } 
/* 224 */       this.entries.add(entry);
/* 225 */       index++;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassFileEntry addWithNestedEntries(ClassFileEntry entry) {
/* 232 */     add(entry);
/* 233 */     for (ClassFileEntry nestedEntry : entry.getNestedClassFileEntries()) {
/* 234 */       addWithNestedEntries(nestedEntry);
/*     */     }
/* 236 */     return entry;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\harmon\\unpack200\bytecode\ClassConstantPool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */