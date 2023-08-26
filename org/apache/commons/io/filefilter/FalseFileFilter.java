/*     */ package org.apache.commons.io.filefilter;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.Serializable;
/*     */ import java.nio.file.FileVisitResult;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
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
/*     */ public class FalseFileFilter
/*     */   implements IOFileFilter, Serializable
/*     */ {
/*  33 */   private static final String TO_STRING = Boolean.FALSE.toString();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  40 */   public static final IOFileFilter FALSE = new FalseFileFilter();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  46 */   public static final IOFileFilter INSTANCE = FALSE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final long serialVersionUID = 6210271677940926200L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean accept(File file) {
/*  64 */     return false;
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
/*  76 */     return false;
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
/*     */   public FileVisitResult accept(Path file, BasicFileAttributes attributes) {
/*  89 */     return FileVisitResult.TERMINATE;
/*     */   }
/*     */ 
/*     */   
/*     */   public IOFileFilter negate() {
/*  94 */     return TrueFileFilter.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  99 */     return TO_STRING;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public IOFileFilter and(IOFileFilter fileFilter) {
/* 105 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public IOFileFilter or(IOFileFilter fileFilter) {
/* 111 */     return fileFilter;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\filefilter\FalseFileFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */