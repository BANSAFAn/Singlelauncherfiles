/*     */ package org.apache.commons.io.filefilter;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileFilter;
/*     */ import java.io.FilenameFilter;
/*     */ import java.io.Serializable;
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
/*     */ public class DelegateFileFilter
/*     */   extends AbstractFileFilter
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -8723373124984771318L;
/*     */   private final FileFilter fileFilter;
/*     */   private final FilenameFilter filenameFilter;
/*     */   
/*     */   public DelegateFileFilter(FileFilter filter) {
/*  45 */     if (filter == null) {
/*  46 */       throw new IllegalArgumentException("The FileFilter must not be null");
/*     */     }
/*  48 */     this.fileFilter = filter;
/*  49 */     this.filenameFilter = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DelegateFileFilter(FilenameFilter filter) {
/*  58 */     if (filter == null) {
/*  59 */       throw new IllegalArgumentException("The FilenameFilter must not be null");
/*     */     }
/*  61 */     this.filenameFilter = filter;
/*  62 */     this.fileFilter = null;
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
/*  73 */     if (this.fileFilter != null) {
/*  74 */       return this.fileFilter.accept(file);
/*     */     }
/*  76 */     return super.accept(file);
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
/*  88 */     if (this.filenameFilter != null) {
/*  89 */       return this.filenameFilter.accept(dir, name);
/*     */     }
/*  91 */     return super.accept(dir, name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 101 */     String delegate = (this.fileFilter != null) ? this.fileFilter.toString() : this.filenameFilter.toString();
/* 102 */     return super.toString() + "(" + delegate + ")";
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\filefilter\DelegateFileFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */