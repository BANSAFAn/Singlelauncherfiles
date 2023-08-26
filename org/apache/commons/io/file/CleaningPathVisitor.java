/*     */ package org.apache.commons.io.file;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.file.FileVisitResult;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.LinkOption;
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
/*     */ 
/*     */ public class CleaningPathVisitor
/*     */   extends CountingPathVisitor
/*     */ {
/*     */   private final String[] skip;
/*     */   private final boolean overrideReadOnly;
/*     */   
/*     */   public static CountingPathVisitor withBigIntegerCounters() {
/*  44 */     return new CleaningPathVisitor(Counters.bigIntegerPathCounters(), new String[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CountingPathVisitor withLongCounters() {
/*  53 */     return new CleaningPathVisitor(Counters.longPathCounters(), new String[0]);
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
/*     */   public CleaningPathVisitor(Counters.PathCounters pathCounter, DeleteOption[] deleteOption, String... skip) {
/*  69 */     super(pathCounter);
/*  70 */     String[] temp = (skip != null) ? (String[])skip.clone() : EMPTY_STRING_ARRAY;
/*  71 */     Arrays.sort((Object[])temp);
/*  72 */     this.skip = temp;
/*  73 */     this.overrideReadOnly = StandardDeleteOption.overrideReadOnly(deleteOption);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CleaningPathVisitor(Counters.PathCounters pathCounter, String... skip) {
/*  83 */     this(pathCounter, PathUtils.EMPTY_DELETE_OPTION_ARRAY, skip);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean accept(Path path) {
/*  93 */     return (Arrays.binarySearch((Object[])this.skip, Objects.toString(path.getFileName(), null)) < 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  98 */     if (this == obj) {
/*  99 */       return true;
/*     */     }
/* 101 */     if (!super.equals(obj)) {
/* 102 */       return false;
/*     */     }
/* 104 */     if (getClass() != obj.getClass()) {
/* 105 */       return false;
/*     */     }
/* 107 */     CleaningPathVisitor other = (CleaningPathVisitor)obj;
/* 108 */     return (this.overrideReadOnly == other.overrideReadOnly && Arrays.equals((Object[])this.skip, (Object[])other.skip));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 113 */     int prime = 31;
/* 114 */     int result = super.hashCode();
/* 115 */     result = 31 * result + Arrays.hashCode((Object[])this.skip);
/* 116 */     result = 31 * result + Objects.hash(new Object[] { Boolean.valueOf(this.overrideReadOnly) });
/* 117 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attributes) throws IOException {
/* 122 */     super.preVisitDirectory(dir, attributes);
/* 123 */     return accept(dir) ? FileVisitResult.CONTINUE : FileVisitResult.SKIP_SUBTREE;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
/* 129 */     if (accept(file) && Files.exists(file, new LinkOption[] { LinkOption.NOFOLLOW_LINKS })) {
/* 130 */       if (this.overrideReadOnly) {
/* 131 */         PathUtils.setReadOnly(file, false, new LinkOption[] { LinkOption.NOFOLLOW_LINKS });
/*     */       }
/* 133 */       Files.deleteIfExists(file);
/*     */     } 
/* 135 */     updateFileCounters(file, attributes);
/* 136 */     return FileVisitResult.CONTINUE;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\file\CleaningPathVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */