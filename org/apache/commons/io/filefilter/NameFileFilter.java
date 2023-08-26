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
/*     */ public class NameFileFilter
/*     */   extends AbstractFileFilter
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 176844364689077340L;
/*     */   private final String[] names;
/*     */   private final IOCase caseSensitivity;
/*     */   
/*     */   public NameFileFilter(List<String> names) {
/*  85 */     this(names, (IOCase)null);
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
/*     */   public NameFileFilter(List<String> names, IOCase caseSensitivity) {
/*  97 */     if (names == null) {
/*  98 */       throw new IllegalArgumentException("The list of names must not be null");
/*     */     }
/* 100 */     this.names = names.<String>toArray(EMPTY_STRING_ARRAY);
/* 101 */     this.caseSensitivity = toIOCase(caseSensitivity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NameFileFilter(String name) {
/* 111 */     this(name, IOCase.SENSITIVE);
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
/*     */   public NameFileFilter(String... names) {
/* 125 */     this(names, IOCase.SENSITIVE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NameFileFilter(String name, IOCase caseSensitivity) {
/* 136 */     if (name == null) {
/* 137 */       throw new IllegalArgumentException("The wildcard must not be null");
/*     */     }
/* 139 */     this.names = new String[] { name };
/* 140 */     this.caseSensitivity = toIOCase(caseSensitivity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NameFileFilter(String[] names, IOCase caseSensitivity) {
/* 151 */     if (names == null) {
/* 152 */       throw new IllegalArgumentException("The array of names must not be null");
/*     */     }
/* 154 */     this.names = new String[names.length];
/* 155 */     System.arraycopy(names, 0, this.names, 0, names.length);
/* 156 */     this.caseSensitivity = toIOCase(caseSensitivity);
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
/* 167 */     return acceptBaseName(file.getName());
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
/* 179 */     return acceptBaseName(name);
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
/* 191 */     return toFileVisitResult(acceptBaseName(Objects.toString(file.getFileName(), null)), file);
/*     */   }
/*     */   
/*     */   private boolean acceptBaseName(String baseName) {
/* 195 */     for (String testName : this.names) {
/* 196 */       if (this.caseSensitivity.checkEquals(baseName, testName)) {
/* 197 */         return true;
/*     */       }
/*     */     } 
/* 200 */     return false;
/*     */   }
/*     */   
/*     */   private IOCase toIOCase(IOCase caseSensitivity) {
/* 204 */     return (caseSensitivity == null) ? IOCase.SENSITIVE : caseSensitivity;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 214 */     StringBuilder buffer = new StringBuilder();
/* 215 */     buffer.append(super.toString());
/* 216 */     buffer.append("(");
/* 217 */     if (this.names != null) {
/* 218 */       for (int i = 0; i < this.names.length; i++) {
/* 219 */         if (i > 0) {
/* 220 */           buffer.append(",");
/*     */         }
/* 222 */         buffer.append(this.names[i]);
/*     */       } 
/*     */     }
/* 225 */     buffer.append(")");
/* 226 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\filefilter\NameFileFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */