/*     */ package org.apache.commons.io.filefilter;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.Serializable;
/*     */ import java.nio.file.FileVisitResult;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import org.apache.commons.io.FilenameUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class WildcardFilter
/*     */   extends AbstractFileFilter
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -5037645902506953517L;
/*     */   private final String[] wildcards;
/*     */   
/*     */   public WildcardFilter(List<String> wildcards) {
/*  94 */     if (wildcards == null) {
/*  95 */       throw new IllegalArgumentException("The wildcard list must not be null");
/*     */     }
/*  97 */     this.wildcards = wildcards.<String>toArray(EMPTY_STRING_ARRAY);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WildcardFilter(String wildcard) {
/* 107 */     if (wildcard == null) {
/* 108 */       throw new IllegalArgumentException("The wildcard must not be null");
/*     */     }
/* 110 */     this.wildcards = new String[] { wildcard };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WildcardFilter(String... wildcards) {
/* 120 */     if (wildcards == null) {
/* 121 */       throw new IllegalArgumentException("The wildcard array must not be null");
/*     */     }
/* 123 */     this.wildcards = new String[wildcards.length];
/* 124 */     System.arraycopy(wildcards, 0, this.wildcards, 0, wildcards.length);
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
/* 135 */     if (file.isDirectory()) {
/* 136 */       return false;
/*     */     }
/*     */     
/* 139 */     for (String wildcard : this.wildcards) {
/* 140 */       if (FilenameUtils.wildcardMatch(file.getName(), wildcard)) {
/* 141 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 145 */     return false;
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
/*     */   public FileVisitResult accept(Path file, BasicFileAttributes attributes) {
/* 157 */     if (Files.isDirectory(file, new java.nio.file.LinkOption[0])) {
/* 158 */       return FileVisitResult.TERMINATE;
/*     */     }
/*     */     
/* 161 */     for (String wildcard : this.wildcards) {
/* 162 */       if (FilenameUtils.wildcardMatch(Objects.toString(file.getFileName(), null), wildcard)) {
/* 163 */         return FileVisitResult.CONTINUE;
/*     */       }
/*     */     } 
/*     */     
/* 167 */     return FileVisitResult.TERMINATE;
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
/* 179 */     if (dir != null && (new File(dir, name)).isDirectory()) {
/* 180 */       return false;
/*     */     }
/*     */     
/* 183 */     for (String wildcard : this.wildcards) {
/* 184 */       if (FilenameUtils.wildcardMatch(name, wildcard)) {
/* 185 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 189 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\filefilter\WildcardFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */