/*    */ package org.apache.commons.io.file;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.file.DirectoryStream;
/*    */ import java.nio.file.FileVisitResult;
/*    */ import java.nio.file.Path;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DirectoryStreamFilter
/*    */   implements DirectoryStream.Filter<Path>
/*    */ {
/*    */   private final PathFilter pathFilter;
/*    */   
/*    */   public DirectoryStreamFilter(PathFilter pathFilter) {
/* 46 */     this.pathFilter = Objects.<PathFilter>requireNonNull(pathFilter, "pathFilter");
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean accept(Path path) throws IOException {
/* 51 */     return (this.pathFilter.accept(path, PathUtils.readBasicFileAttributes(path)) == FileVisitResult.CONTINUE);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PathFilter getPathFilter() {
/* 60 */     return this.pathFilter;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\file\DirectoryStreamFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */