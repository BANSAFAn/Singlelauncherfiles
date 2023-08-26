/*     */ package org.apache.commons.io.file;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
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
/*     */ public class AccumulatorPathVisitor
/*     */   extends CountingPathVisitor
/*     */ {
/*     */   public static AccumulatorPathVisitor withBigIntegerCounters() {
/*  67 */     return new AccumulatorPathVisitor(Counters.bigIntegerPathCounters());
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
/*     */   public static AccumulatorPathVisitor withBigIntegerCounters(PathFilter fileFilter, PathFilter dirFilter) {
/*  80 */     return new AccumulatorPathVisitor(Counters.bigIntegerPathCounters(), fileFilter, dirFilter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static AccumulatorPathVisitor withLongCounters() {
/*  89 */     return new AccumulatorPathVisitor(Counters.longPathCounters());
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
/*     */   public static AccumulatorPathVisitor withLongCounters(PathFilter fileFilter, PathFilter dirFilter) {
/* 101 */     return new AccumulatorPathVisitor(Counters.longPathCounters(), fileFilter, dirFilter);
/*     */   }
/*     */   
/* 104 */   private final List<Path> dirList = new ArrayList<>();
/*     */   
/* 106 */   private final List<Path> fileList = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AccumulatorPathVisitor() {
/* 114 */     super(Counters.noopPathCounters());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AccumulatorPathVisitor(Counters.PathCounters pathCounter) {
/* 123 */     super(pathCounter);
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
/*     */   public AccumulatorPathVisitor(Counters.PathCounters pathCounter, PathFilter fileFilter, PathFilter dirFilter) {
/* 136 */     super(pathCounter, fileFilter, dirFilter);
/*     */   }
/*     */   
/*     */   private void add(List<Path> list, Path dir) {
/* 140 */     list.add(dir.normalize());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 145 */     if (this == obj) {
/* 146 */       return true;
/*     */     }
/* 148 */     if (!super.equals(obj)) {
/* 149 */       return false;
/*     */     }
/* 151 */     if (!(obj instanceof AccumulatorPathVisitor)) {
/* 152 */       return false;
/*     */     }
/* 154 */     AccumulatorPathVisitor other = (AccumulatorPathVisitor)obj;
/* 155 */     return (Objects.equals(this.dirList, other.dirList) && Objects.equals(this.fileList, other.fileList));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Path> getDirList() {
/* 164 */     return this.dirList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Path> getFileList() {
/* 173 */     return this.fileList;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 178 */     int prime = 31;
/* 179 */     int result = super.hashCode();
/* 180 */     result = 31 * result + Objects.hash(new Object[] { this.dirList, this.fileList });
/* 181 */     return result;
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
/*     */   
/*     */   public List<Path> relativizeDirectories(Path parent, boolean sort, Comparator<? super Path> comparator) {
/* 195 */     return PathUtils.relativize(getDirList(), parent, sort, comparator);
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
/*     */   
/*     */   public List<Path> relativizeFiles(Path parent, boolean sort, Comparator<? super Path> comparator) {
/* 209 */     return PathUtils.relativize(getFileList(), parent, sort, comparator);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateDirCounter(Path dir, IOException exc) {
/* 214 */     super.updateDirCounter(dir, exc);
/* 215 */     add(this.dirList, dir);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateFileCounters(Path file, BasicFileAttributes attributes) {
/* 220 */     super.updateFileCounters(file, attributes);
/* 221 */     add(this.fileList, file);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\file\AccumulatorPathVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */