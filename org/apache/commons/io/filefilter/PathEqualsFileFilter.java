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
/*    */ public class PathEqualsFileFilter
/*    */   extends AbstractFileFilter
/*    */ {
/*    */   private final Path path;
/*    */   
/*    */   public PathEqualsFileFilter(Path file) {
/* 43 */     this.path = file;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean accept(File file) {
/* 48 */     return Objects.equals(this.path, file.toPath());
/*    */   }
/*    */ 
/*    */   
/*    */   public FileVisitResult accept(Path path, BasicFileAttributes attributes) {
/* 53 */     return toFileVisitResult(Objects.equals(this.path, path), path);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\filefilter\PathEqualsFileFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */