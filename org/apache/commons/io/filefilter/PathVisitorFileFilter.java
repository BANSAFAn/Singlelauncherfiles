/*    */ package org.apache.commons.io.filefilter;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.FileVisitResult;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.attribute.BasicFileAttributes;
/*    */ import org.apache.commons.io.file.NoopPathVisitor;
/*    */ import org.apache.commons.io.file.PathUtils;
/*    */ import org.apache.commons.io.file.PathVisitor;
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
/*    */ public class PathVisitorFileFilter
/*    */   extends AbstractFileFilter
/*    */ {
/*    */   private final PathVisitor pathVisitor;
/*    */   
/*    */   public PathVisitorFileFilter(PathVisitor pathVisitor) {
/* 46 */     this.pathVisitor = (pathVisitor == null) ? (PathVisitor)NoopPathVisitor.INSTANCE : pathVisitor;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean accept(File file) {
/*    */     try {
/* 52 */       Path path = file.toPath();
/* 53 */       return (visitFile(path, 
/* 54 */           file.exists() ? PathUtils.readBasicFileAttributes(path) : null) == FileVisitResult.CONTINUE);
/* 55 */     } catch (IOException e) {
/* 56 */       return (handle(e) == FileVisitResult.CONTINUE);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean accept(File dir, String name) {
/*    */     try {
/* 63 */       Path path = dir.toPath().resolve(name);
/* 64 */       return (accept(path, PathUtils.readBasicFileAttributes(path)) == FileVisitResult.CONTINUE);
/* 65 */     } catch (IOException e) {
/* 66 */       return (handle(e) == FileVisitResult.CONTINUE);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public FileVisitResult accept(Path path, BasicFileAttributes attributes) {
/*    */     try {
/* 73 */       return Files.isDirectory(path, new java.nio.file.LinkOption[0]) ? this.pathVisitor.postVisitDirectory(path, null) : visitFile(path, attributes);
/* 74 */     } catch (IOException e) {
/* 75 */       return handle(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public FileVisitResult visitFile(Path path, BasicFileAttributes attributes) throws IOException {
/* 81 */     return this.pathVisitor.visitFile(path, attributes);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\filefilter\PathVisitorFileFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */