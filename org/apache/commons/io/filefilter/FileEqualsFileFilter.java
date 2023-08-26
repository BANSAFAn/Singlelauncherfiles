/*    */ package org.apache.commons.io.filefilter;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.nio.file.FileVisitResult;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.attribute.BasicFileAttributes;
/*    */ import java.util.Objects;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FileEqualsFileFilter
/*    */   extends AbstractFileFilter
/*    */ {
/*    */   private final File file;
/*    */   private final Path path;
/*    */   
/*    */   public FileEqualsFileFilter(File file) {
/* 44 */     this.file = Objects.<File>requireNonNull(file, "file");
/* 45 */     this.path = file.toPath();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean accept(File file) {
/* 50 */     return Objects.equals(this.file, file);
/*    */   }
/*    */ 
/*    */   
/*    */   public FileVisitResult accept(Path path, BasicFileAttributes attributes) {
/* 55 */     return toFileVisitResult(Objects.equals(this.path, path), path);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\filefilter\FileEqualsFileFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */