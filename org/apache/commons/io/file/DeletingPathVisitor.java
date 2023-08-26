/*     */ package org.apache.commons.io.file;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.file.FileVisitResult;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.NoSuchFileException;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.util.Arrays;
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
/*     */ public class DeletingPathVisitor
/*     */   extends CountingPathVisitor
/*     */ {
/*     */   private final String[] skip;
/*     */   private final boolean overrideReadOnly;
/*     */   private final LinkOption[] linkOptions;
/*     */   
/*     */   public static DeletingPathVisitor withBigIntegerCounters() {
/*  45 */     return new DeletingPathVisitor(Counters.bigIntegerPathCounters(), new String[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DeletingPathVisitor withLongCounters() {
/*  54 */     return new DeletingPathVisitor(Counters.longPathCounters(), new String[0]);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public DeletingPathVisitor(Counters.PathCounters pathCounter, DeleteOption[] deleteOption, String... skip) {
/*  71 */     this(pathCounter, PathUtils.NOFOLLOW_LINK_OPTION_ARRAY, deleteOption, skip);
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
/*     */   public DeletingPathVisitor(Counters.PathCounters pathCounter, LinkOption[] linkOptions, DeleteOption[] deleteOption, String... skip) {
/*  85 */     super(pathCounter);
/*  86 */     String[] temp = (skip != null) ? (String[])skip.clone() : EMPTY_STRING_ARRAY;
/*  87 */     Arrays.sort((Object[])temp);
/*  88 */     this.skip = temp;
/*  89 */     this.overrideReadOnly = StandardDeleteOption.overrideReadOnly(deleteOption);
/*     */     
/*  91 */     this.linkOptions = (linkOptions == null) ? PathUtils.NOFOLLOW_LINK_OPTION_ARRAY : (LinkOption[])linkOptions.clone();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DeletingPathVisitor(Counters.PathCounters pathCounter, String... skip) {
/* 102 */     this(pathCounter, PathUtils.EMPTY_DELETE_OPTION_ARRAY, skip);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean accept(Path path) {
/* 112 */     return (Arrays.binarySearch((Object[])this.skip, Objects.toString(path.getFileName(), null)) < 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 117 */     if (this == obj) {
/* 118 */       return true;
/*     */     }
/* 120 */     if (!super.equals(obj)) {
/* 121 */       return false;
/*     */     }
/* 123 */     if (getClass() != obj.getClass()) {
/* 124 */       return false;
/*     */     }
/* 126 */     DeletingPathVisitor other = (DeletingPathVisitor)obj;
/* 127 */     return (this.overrideReadOnly == other.overrideReadOnly && Arrays.equals((Object[])this.skip, (Object[])other.skip));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 132 */     int prime = 31;
/* 133 */     int result = super.hashCode();
/* 134 */     result = 31 * result + Arrays.hashCode((Object[])this.skip);
/* 135 */     result = 31 * result + Objects.hash(new Object[] { Boolean.valueOf(this.overrideReadOnly) });
/* 136 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
/* 141 */     if (PathUtils.isEmptyDirectory(dir)) {
/* 142 */       Files.deleteIfExists(dir);
/*     */     }
/* 144 */     return super.postVisitDirectory(dir, exc);
/*     */   }
/*     */ 
/*     */   
/*     */   public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
/* 149 */     super.preVisitDirectory(dir, attrs);
/* 150 */     return accept(dir) ? FileVisitResult.CONTINUE : FileVisitResult.SKIP_SUBTREE;
/*     */   }
/*     */ 
/*     */   
/*     */   public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
/* 155 */     if (accept(file)) {
/*     */       
/* 157 */       if (Files.exists(file, this.linkOptions)) {
/* 158 */         if (this.overrideReadOnly) {
/* 159 */           PathUtils.setReadOnly(file, false, this.linkOptions);
/*     */         }
/* 161 */         Files.deleteIfExists(file);
/*     */       } 
/*     */       
/* 164 */       if (Files.isSymbolicLink(file)) {
/*     */         
/*     */         try {
/* 167 */           Files.delete(file);
/* 168 */         } catch (NoSuchFileException noSuchFileException) {}
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 173 */     updateFileCounters(file, attrs);
/* 174 */     return FileVisitResult.CONTINUE;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\file\DeletingPathVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */