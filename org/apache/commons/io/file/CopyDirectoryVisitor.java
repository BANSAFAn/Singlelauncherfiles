/*     */ package org.apache.commons.io.file;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.file.CopyOption;
/*     */ import java.nio.file.FileVisitResult;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.nio.file.attribute.FileAttribute;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CopyDirectoryVisitor
/*     */   extends CountingPathVisitor
/*     */ {
/*     */   private final CopyOption[] copyOptions;
/*     */   private final Path sourceDirectory;
/*     */   private final Path targetDirectory;
/*     */   
/*     */   public CopyDirectoryVisitor(Counters.PathCounters pathCounter, Path sourceDirectory, Path targetDirectory, CopyOption... copyOptions) {
/*  53 */     super(pathCounter);
/*  54 */     this.sourceDirectory = sourceDirectory;
/*  55 */     this.targetDirectory = targetDirectory;
/*  56 */     this.copyOptions = (copyOptions == null) ? PathUtils.EMPTY_COPY_OPTIONS : (CopyOption[])copyOptions.clone();
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
/*     */   public CopyDirectoryVisitor(Counters.PathCounters pathCounter, PathFilter fileFilter, PathFilter dirFilter, Path sourceDirectory, Path targetDirectory, CopyOption... copyOptions) {
/*  72 */     super(pathCounter, fileFilter, dirFilter);
/*  73 */     this.sourceDirectory = sourceDirectory;
/*  74 */     this.targetDirectory = targetDirectory;
/*  75 */     this.copyOptions = (copyOptions == null) ? PathUtils.EMPTY_COPY_OPTIONS : (CopyOption[])copyOptions.clone();
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
/*     */   protected void copy(Path sourceFile, Path targetFile) throws IOException {
/*  87 */     Files.copy(sourceFile, targetFile, this.copyOptions);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  92 */     if (this == obj) {
/*  93 */       return true;
/*     */     }
/*  95 */     if (!super.equals(obj)) {
/*  96 */       return false;
/*     */     }
/*  98 */     if (getClass() != obj.getClass()) {
/*  99 */       return false;
/*     */     }
/* 101 */     CopyDirectoryVisitor other = (CopyDirectoryVisitor)obj;
/* 102 */     return (Arrays.equals((Object[])this.copyOptions, (Object[])other.copyOptions) && Objects.equals(this.sourceDirectory, other.sourceDirectory) && 
/* 103 */       Objects.equals(this.targetDirectory, other.targetDirectory));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CopyOption[] getCopyOptions() {
/* 113 */     return (CopyOption[])this.copyOptions.clone();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path getSourceDirectory() {
/* 123 */     return this.sourceDirectory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path getTargetDirectory() {
/* 133 */     return this.targetDirectory;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 138 */     int prime = 31;
/* 139 */     int result = super.hashCode();
/* 140 */     result = 31 * result + Arrays.hashCode((Object[])this.copyOptions);
/* 141 */     result = 31 * result + Objects.hash(new Object[] { this.sourceDirectory, this.targetDirectory });
/* 142 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public FileVisitResult preVisitDirectory(Path directory, BasicFileAttributes attributes) throws IOException {
/* 148 */     Path newTargetDir = resolveRelativeAsString(directory);
/* 149 */     if (Files.notExists(newTargetDir, new java.nio.file.LinkOption[0])) {
/* 150 */       Files.createDirectory(newTargetDir, (FileAttribute<?>[])new FileAttribute[0]);
/*     */     }
/* 152 */     return super.preVisitDirectory(directory, attributes);
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
/*     */   private Path resolveRelativeAsString(Path directory) {
/* 165 */     return this.targetDirectory.resolve(this.sourceDirectory.relativize(directory).toString());
/*     */   }
/*     */ 
/*     */   
/*     */   public FileVisitResult visitFile(Path sourceFile, BasicFileAttributes attributes) throws IOException {
/* 170 */     Path targetFile = resolveRelativeAsString(sourceFile);
/* 171 */     copy(sourceFile, targetFile);
/* 172 */     return super.visitFile(targetFile, attributes);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\file\CopyDirectoryVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */