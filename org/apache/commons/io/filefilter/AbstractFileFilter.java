/*     */ package org.apache.commons.io.filefilter;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.FileVisitResult;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.util.Objects;
/*     */ import org.apache.commons.io.file.PathVisitor;
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
/*     */ public abstract class AbstractFileFilter
/*     */   implements IOFileFilter, PathVisitor
/*     */ {
/*     */   static FileVisitResult toFileVisitResult(boolean accept, Path path) {
/*  43 */     return accept ? FileVisitResult.CONTINUE : FileVisitResult.TERMINATE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean accept(File file) {
/*  54 */     Objects.requireNonNull(file, "file");
/*  55 */     return accept(file.getParentFile(), file.getName());
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
/*     */   public boolean accept(File dir, String name) {
/*  67 */     Objects.requireNonNull(name, "name");
/*  68 */     return accept(new File(dir, name));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected FileVisitResult handle(Throwable t) {
/*  79 */     return FileVisitResult.TERMINATE;
/*     */   }
/*     */ 
/*     */   
/*     */   public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
/*  84 */     return FileVisitResult.CONTINUE;
/*     */   }
/*     */ 
/*     */   
/*     */   public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attributes) throws IOException {
/*  89 */     return accept(dir, attributes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  99 */     return getClass().getSimpleName();
/*     */   }
/*     */ 
/*     */   
/*     */   public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
/* 104 */     return accept(file, attributes);
/*     */   }
/*     */ 
/*     */   
/*     */   public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
/* 109 */     return FileVisitResult.CONTINUE;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\filefilter\AbstractFileFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */