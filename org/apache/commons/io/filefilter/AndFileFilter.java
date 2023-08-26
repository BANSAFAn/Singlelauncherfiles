/*     */ package org.apache.commons.io.filefilter;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.Serializable;
/*     */ import java.nio.file.FileVisitResult;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AndFileFilter
/*     */   extends AbstractFileFilter
/*     */   implements ConditionalFileFilter, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 7215974688563965257L;
/*     */   private final List<IOFileFilter> fileFilters;
/*     */   
/*     */   public AndFileFilter() {
/*  54 */     this(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private AndFileFilter(ArrayList<IOFileFilter> initialList) {
/*  63 */     this.fileFilters = Objects.<List<IOFileFilter>>requireNonNull(initialList, "initialList");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private AndFileFilter(int initialCapacity) {
/*  72 */     this(new ArrayList<>(initialCapacity));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AndFileFilter(IOFileFilter filter1, IOFileFilter filter2) {
/*  83 */     this(2);
/*  84 */     addFileFilter(filter1);
/*  85 */     addFileFilter(filter2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AndFileFilter(IOFileFilter... fileFilters) {
/*  95 */     this(((IOFileFilter[])Objects.requireNonNull((T)fileFilters, "fileFilters")).length);
/*  96 */     addFileFilter(fileFilters);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AndFileFilter(List<IOFileFilter> fileFilters) {
/* 107 */     this(new ArrayList<>(Objects.<Collection<? extends IOFileFilter>>requireNonNull(fileFilters, "fileFilters")));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean accept(File file) {
/* 115 */     if (isEmpty()) {
/* 116 */       return false;
/*     */     }
/* 118 */     for (IOFileFilter fileFilter : this.fileFilters) {
/* 119 */       if (!fileFilter.accept(file)) {
/* 120 */         return false;
/*     */       }
/*     */     } 
/* 123 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean accept(File file, String name) {
/* 131 */     if (isEmpty()) {
/* 132 */       return false;
/*     */     }
/* 134 */     for (IOFileFilter fileFilter : this.fileFilters) {
/* 135 */       if (!fileFilter.accept(file, name)) {
/* 136 */         return false;
/*     */       }
/*     */     } 
/* 139 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileVisitResult accept(Path file, BasicFileAttributes attributes) {
/* 148 */     if (isEmpty()) {
/* 149 */       return FileVisitResult.TERMINATE;
/*     */     }
/* 151 */     for (IOFileFilter fileFilter : this.fileFilters) {
/* 152 */       if (fileFilter.accept(file, attributes) != FileVisitResult.CONTINUE) {
/* 153 */         return FileVisitResult.TERMINATE;
/*     */       }
/*     */     } 
/* 156 */     return FileVisitResult.CONTINUE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFileFilter(IOFileFilter fileFilter) {
/* 164 */     this.fileFilters.add(Objects.requireNonNull(fileFilter, "fileFilter"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFileFilter(IOFileFilter... fileFilters) {
/* 174 */     for (IOFileFilter fileFilter : (IOFileFilter[])Objects.<IOFileFilter[]>requireNonNull(fileFilters, "fileFilters")) {
/* 175 */       addFileFilter(fileFilter);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<IOFileFilter> getFileFilters() {
/* 184 */     return Collections.unmodifiableList(this.fileFilters);
/*     */   }
/*     */   
/*     */   private boolean isEmpty() {
/* 188 */     return this.fileFilters.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean removeFileFilter(IOFileFilter ioFileFilter) {
/* 196 */     return this.fileFilters.remove(ioFileFilter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFileFilters(List<IOFileFilter> fileFilters) {
/* 204 */     this.fileFilters.clear();
/* 205 */     this.fileFilters.addAll(fileFilters);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 215 */     StringBuilder buffer = new StringBuilder();
/* 216 */     buffer.append(super.toString());
/* 217 */     buffer.append("(");
/* 218 */     for (int i = 0; i < this.fileFilters.size(); i++) {
/* 219 */       if (i > 0) {
/* 220 */         buffer.append(",");
/*     */       }
/* 222 */       buffer.append(this.fileFilters.get(i));
/*     */     } 
/* 224 */     buffer.append(")");
/* 225 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\filefilter\AndFileFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */