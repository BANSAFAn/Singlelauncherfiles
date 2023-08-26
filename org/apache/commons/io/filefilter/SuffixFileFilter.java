/*     */ package org.apache.commons.io.filefilter;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.Serializable;
/*     */ import java.nio.file.FileVisitResult;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import org.apache.commons.io.IOCase;
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
/*     */ public class SuffixFileFilter
/*     */   extends AbstractFileFilter
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -3389157631240246157L;
/*     */   private final String[] suffixes;
/*     */   private final IOCase caseSensitivity;
/*     */   
/*     */   public SuffixFileFilter(List<String> suffixes) {
/*  86 */     this(suffixes, IOCase.SENSITIVE);
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
/*     */   public SuffixFileFilter(List<String> suffixes, IOCase caseSensitivity) {
/* 100 */     if (suffixes == null) {
/* 101 */       throw new IllegalArgumentException("The list of suffixes must not be null");
/*     */     }
/* 103 */     this.suffixes = suffixes.<String>toArray(EMPTY_STRING_ARRAY);
/* 104 */     this.caseSensitivity = (caseSensitivity == null) ? IOCase.SENSITIVE : caseSensitivity;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SuffixFileFilter(String suffix) {
/* 114 */     this(suffix, IOCase.SENSITIVE);
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
/*     */   public SuffixFileFilter(String... suffixes) {
/* 127 */     this(suffixes, IOCase.SENSITIVE);
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
/*     */   public SuffixFileFilter(String suffix, IOCase caseSensitivity) {
/* 140 */     if (suffix == null) {
/* 141 */       throw new IllegalArgumentException("The suffix must not be null");
/*     */     }
/* 143 */     this.suffixes = new String[] { suffix };
/* 144 */     this.caseSensitivity = (caseSensitivity == null) ? IOCase.SENSITIVE : caseSensitivity;
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
/*     */   public SuffixFileFilter(String[] suffixes, IOCase caseSensitivity) {
/* 157 */     if (suffixes == null) {
/* 158 */       throw new IllegalArgumentException("The array of suffixes must not be null");
/*     */     }
/* 160 */     this.suffixes = new String[suffixes.length];
/* 161 */     System.arraycopy(suffixes, 0, this.suffixes, 0, suffixes.length);
/* 162 */     this.caseSensitivity = (caseSensitivity == null) ? IOCase.SENSITIVE : caseSensitivity;
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
/* 173 */     return accept(file.getName());
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
/*     */   public boolean accept(File file, String name) {
/* 185 */     return accept(name);
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
/* 197 */     return toFileVisitResult(accept(Objects.toString(file.getFileName(), null)), file);
/*     */   }
/*     */   
/*     */   private boolean accept(String name) {
/* 201 */     for (String suffix : this.suffixes) {
/* 202 */       if (this.caseSensitivity.checkEndsWith(name, suffix)) {
/* 203 */         return true;
/*     */       }
/*     */     } 
/* 206 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 216 */     StringBuilder buffer = new StringBuilder();
/* 217 */     buffer.append(super.toString());
/* 218 */     buffer.append("(");
/* 219 */     if (this.suffixes != null) {
/* 220 */       for (int i = 0; i < this.suffixes.length; i++) {
/* 221 */         if (i > 0) {
/* 222 */           buffer.append(",");
/*     */         }
/* 224 */         buffer.append(this.suffixes[i]);
/*     */       } 
/*     */     }
/* 227 */     buffer.append(")");
/* 228 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\filefilter\SuffixFileFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */