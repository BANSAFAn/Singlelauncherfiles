/*     */ package org.apache.commons.io.file;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.file.FileVisitResult;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.util.Objects;
/*     */ import org.apache.commons.io.filefilter.TrueFileFilter;
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
/*     */ public class CountingPathVisitor
/*     */   extends SimplePathVisitor
/*     */ {
/*  37 */   static final String[] EMPTY_STRING_ARRAY = new String[0];
/*     */   
/*     */   private final Counters.PathCounters pathCounters;
/*     */   
/*     */   private final PathFilter fileFilter;
/*     */   private final PathFilter dirFilter;
/*     */   
/*     */   public static CountingPathVisitor withBigIntegerCounters() {
/*  45 */     return new CountingPathVisitor(Counters.bigIntegerPathCounters());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CountingPathVisitor withLongCounters() {
/*  54 */     return new CountingPathVisitor(Counters.longPathCounters());
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
/*     */   public CountingPathVisitor(Counters.PathCounters pathCounter) {
/*  67 */     this(pathCounter, (PathFilter)TrueFileFilter.INSTANCE, (PathFilter)TrueFileFilter.INSTANCE);
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
/*     */   public CountingPathVisitor(Counters.PathCounters pathCounter, PathFilter fileFilter, PathFilter dirFilter) {
/*  79 */     this.pathCounters = Objects.<Counters.PathCounters>requireNonNull(pathCounter, "pathCounter");
/*  80 */     this.fileFilter = Objects.<PathFilter>requireNonNull(fileFilter, "fileFilter");
/*  81 */     this.dirFilter = Objects.<PathFilter>requireNonNull(dirFilter, "dirFilter");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  86 */     if (this == obj) {
/*  87 */       return true;
/*     */     }
/*  89 */     if (!(obj instanceof CountingPathVisitor)) {
/*  90 */       return false;
/*     */     }
/*  92 */     CountingPathVisitor other = (CountingPathVisitor)obj;
/*  93 */     return Objects.equals(this.pathCounters, other.pathCounters);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Counters.PathCounters getPathCounters() {
/* 102 */     return this.pathCounters;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 107 */     return Objects.hash(new Object[] { this.pathCounters });
/*     */   }
/*     */ 
/*     */   
/*     */   public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
/* 112 */     updateDirCounter(dir, exc);
/* 113 */     return FileVisitResult.CONTINUE;
/*     */   }
/*     */ 
/*     */   
/*     */   public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attributes) throws IOException {
/* 118 */     FileVisitResult accept = this.dirFilter.accept(dir, attributes);
/* 119 */     return (accept != FileVisitResult.CONTINUE) ? FileVisitResult.SKIP_SUBTREE : FileVisitResult.CONTINUE;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 124 */     return this.pathCounters.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void updateDirCounter(Path dir, IOException exc) {
/* 135 */     this.pathCounters.getDirectoryCounter().increment();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void updateFileCounters(Path file, BasicFileAttributes attributes) {
/* 145 */     this.pathCounters.getFileCounter().increment();
/* 146 */     this.pathCounters.getByteCounter().add(attributes.size());
/*     */   }
/*     */ 
/*     */   
/*     */   public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
/* 151 */     if (Files.exists(file, new java.nio.file.LinkOption[0]) && this.fileFilter.accept(file, attributes) == FileVisitResult.CONTINUE) {
/* 152 */       updateFileCounters(file, attributes);
/*     */     }
/* 154 */     return FileVisitResult.CONTINUE;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\file\CountingPathVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */