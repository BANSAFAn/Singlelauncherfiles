/*     */ package org.apache.commons.io.filefilter;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.Serializable;
/*     */ import java.nio.file.FileVisitResult;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.util.List;
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
/*     */ public class PrefixFileFilter
/*     */   extends AbstractFileFilter
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 8533897440809599867L;
/*     */   private final String[] prefixes;
/*     */   private final IOCase caseSensitivity;
/*     */   
/*     */   public PrefixFileFilter(List<String> prefixes) {
/*  84 */     this(prefixes, IOCase.SENSITIVE);
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
/*     */   public PrefixFileFilter(List<String> prefixes, IOCase caseSensitivity) {
/*  98 */     if (prefixes == null) {
/*  99 */       throw new IllegalArgumentException("The list of prefixes must not be null");
/*     */     }
/* 101 */     this.prefixes = prefixes.<String>toArray(EMPTY_STRING_ARRAY);
/* 102 */     this.caseSensitivity = (caseSensitivity == null) ? IOCase.SENSITIVE : caseSensitivity;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PrefixFileFilter(String prefix) {
/* 112 */     this(prefix, IOCase.SENSITIVE);
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
/*     */   public PrefixFileFilter(String... prefixes) {
/* 125 */     this(prefixes, IOCase.SENSITIVE);
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
/*     */   public PrefixFileFilter(String prefix, IOCase caseSensitivity) {
/* 138 */     if (prefix == null) {
/* 139 */       throw new IllegalArgumentException("The prefix must not be null");
/*     */     }
/* 141 */     this.prefixes = new String[] { prefix };
/* 142 */     this.caseSensitivity = (caseSensitivity == null) ? IOCase.SENSITIVE : caseSensitivity;
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
/*     */   public PrefixFileFilter(String[] prefixes, IOCase caseSensitivity) {
/* 155 */     if (prefixes == null) {
/* 156 */       throw new IllegalArgumentException("The array of prefixes must not be null");
/*     */     }
/* 158 */     this.prefixes = new String[prefixes.length];
/* 159 */     System.arraycopy(prefixes, 0, this.prefixes, 0, prefixes.length);
/* 160 */     this.caseSensitivity = (caseSensitivity == null) ? IOCase.SENSITIVE : caseSensitivity;
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
/* 171 */     return accept((file == null) ? null : file.getName());
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
/* 183 */     return accept(name);
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
/* 195 */     Path fileName = file.getFileName();
/* 196 */     return toFileVisitResult(accept((fileName == null) ? null : fileName.toFile()), file);
/*     */   }
/*     */   
/*     */   private boolean accept(String name) {
/* 200 */     for (String prefix : this.prefixes) {
/* 201 */       if (this.caseSensitivity.checkStartsWith(name, prefix)) {
/* 202 */         return true;
/*     */       }
/*     */     } 
/* 205 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 215 */     StringBuilder buffer = new StringBuilder();
/* 216 */     buffer.append(super.toString());
/* 217 */     buffer.append("(");
/* 218 */     if (this.prefixes != null) {
/* 219 */       for (int i = 0; i < this.prefixes.length; i++) {
/* 220 */         if (i > 0) {
/* 221 */           buffer.append(",");
/*     */         }
/* 223 */         buffer.append(this.prefixes[i]);
/*     */       } 
/*     */     }
/* 226 */     buffer.append(")");
/* 227 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\filefilter\PrefixFileFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */