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
/*     */ public class OrFileFilter
/*     */   extends AbstractFileFilter
/*     */   implements ConditionalFileFilter, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 5767770777065432721L;
/*     */   private final List<IOFileFilter> fileFilters;
/*     */   
/*     */   public OrFileFilter() {
/*  50 */     this(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private OrFileFilter(ArrayList<IOFileFilter> initialList) {
/*  59 */     this.fileFilters = Objects.<List<IOFileFilter>>requireNonNull(initialList, "initialList");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private OrFileFilter(int initialCapacity) {
/*  68 */     this(new ArrayList<>(initialCapacity));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OrFileFilter(IOFileFilter... fileFilters) {
/*  78 */     this(((IOFileFilter[])Objects.requireNonNull((T)fileFilters, "fileFilters")).length);
/*  79 */     addFileFilter(fileFilters);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OrFileFilter(IOFileFilter filter1, IOFileFilter filter2) {
/*  90 */     this(2);
/*  91 */     addFileFilter(filter1);
/*  92 */     addFileFilter(filter2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OrFileFilter(List<IOFileFilter> fileFilters) {
/* 102 */     this(new ArrayList<>(Objects.<Collection<? extends IOFileFilter>>requireNonNull(fileFilters, "fileFilters")));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean accept(File file) {
/* 110 */     for (IOFileFilter fileFilter : this.fileFilters) {
/* 111 */       if (fileFilter.accept(file)) {
/* 112 */         return true;
/*     */       }
/*     */     } 
/* 115 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean accept(File file, String name) {
/* 123 */     for (IOFileFilter fileFilter : this.fileFilters) {
/* 124 */       if (fileFilter.accept(file, name)) {
/* 125 */         return true;
/*     */       }
/*     */     } 
/* 128 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileVisitResult accept(Path file, BasicFileAttributes attributes) {
/* 136 */     for (IOFileFilter fileFilter : this.fileFilters) {
/* 137 */       if (fileFilter.accept(file, attributes) == FileVisitResult.CONTINUE) {
/* 138 */         return FileVisitResult.CONTINUE;
/*     */       }
/*     */     } 
/* 141 */     return FileVisitResult.TERMINATE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFileFilter(IOFileFilter fileFilter) {
/* 149 */     this.fileFilters.add(Objects.requireNonNull(fileFilter, "fileFilter"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFileFilter(IOFileFilter... fileFilters) {
/* 159 */     for (IOFileFilter fileFilter : (IOFileFilter[])Objects.<IOFileFilter[]>requireNonNull(fileFilters, "fileFilters")) {
/* 160 */       addFileFilter(fileFilter);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<IOFileFilter> getFileFilters() {
/* 169 */     return Collections.unmodifiableList(this.fileFilters);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean removeFileFilter(IOFileFilter fileFilter) {
/* 177 */     return this.fileFilters.remove(fileFilter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFileFilters(List<IOFileFilter> fileFilters) {
/* 185 */     this.fileFilters.clear();
/* 186 */     this.fileFilters.addAll(Objects.<Collection<? extends IOFileFilter>>requireNonNull(fileFilters, "fileFilters"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 196 */     StringBuilder buffer = new StringBuilder();
/* 197 */     buffer.append(super.toString());
/* 198 */     buffer.append("(");
/* 199 */     if (this.fileFilters != null) {
/* 200 */       for (int i = 0; i < this.fileFilters.size(); i++) {
/* 201 */         if (i > 0) {
/* 202 */           buffer.append(",");
/*     */         }
/* 204 */         buffer.append(this.fileFilters.get(i));
/*     */       } 
/*     */     }
/* 207 */     buffer.append(")");
/* 208 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\filefilter\OrFileFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */