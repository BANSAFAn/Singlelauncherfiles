/*     */ package org.apache.commons.io;
/*     */ 
/*     */ import java.util.Objects;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum IOCase
/*     */ {
/*  44 */   SENSITIVE("Sensitive", true),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  49 */   INSENSITIVE("Insensitive", false),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  65 */   SYSTEM("System", !FilenameUtils.isSystemWindows());
/*     */   
/*     */   private static final long serialVersionUID = -6343169151696340687L;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final transient boolean sensitive;
/*     */ 
/*     */   
/*     */   public static boolean isCaseSensitive(IOCase caseSensitivity) {
/*  75 */     return (caseSensitivity != null && !caseSensitivity.isCaseSensitive());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static IOCase forName(String name) {
/*  95 */     for (IOCase ioCase : values()) {
/*  96 */       if (ioCase.getName().equals(name)) {
/*  97 */         return ioCase;
/*     */       }
/*     */     } 
/* 100 */     throw new IllegalArgumentException("Invalid IOCase name: " + name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   IOCase(String name, boolean sensitive) {
/* 110 */     this.name = name;
/* 111 */     this.sensitive = sensitive;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object readResolve() {
/* 121 */     return forName(this.name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 130 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCaseSensitive() {
/* 139 */     return this.sensitive;
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
/*     */ 
/*     */   
/*     */   public int checkCompareTo(String str1, String str2) {
/* 155 */     Objects.requireNonNull(str1, "str1");
/* 156 */     Objects.requireNonNull(str2, "str2");
/* 157 */     return this.sensitive ? str1.compareTo(str2) : str1.compareToIgnoreCase(str2);
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
/*     */ 
/*     */   
/*     */   public boolean checkEquals(String str1, String str2) {
/* 173 */     Objects.requireNonNull(str1, "str1");
/* 174 */     Objects.requireNonNull(str2, "str2");
/* 175 */     return this.sensitive ? str1.equals(str2) : str1.equalsIgnoreCase(str2);
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
/*     */   
/*     */   public boolean checkStartsWith(String str, String start) {
/* 190 */     return (str != null && start != null && str.regionMatches(!this.sensitive, 0, start, 0, start.length()));
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
/*     */   
/*     */   public boolean checkEndsWith(String str, String end) {
/* 205 */     if (str == null || end == null) {
/* 206 */       return false;
/*     */     }
/* 208 */     int endLen = end.length();
/* 209 */     return str.regionMatches(!this.sensitive, str.length() - endLen, end, 0, endLen);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int checkIndexOf(String str, int strStartIndex, String search) {
/* 229 */     int endIndex = str.length() - search.length();
/* 230 */     if (endIndex >= strStartIndex) {
/* 231 */       for (int i = strStartIndex; i <= endIndex; i++) {
/* 232 */         if (checkRegionMatches(str, i, search)) {
/* 233 */           return i;
/*     */         }
/*     */       } 
/*     */     }
/* 237 */     return -1;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean checkRegionMatches(String str, int strStartIndex, String search) {
/* 254 */     return str.regionMatches(!this.sensitive, strStartIndex, search, 0, search.length());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 264 */     return this.name;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\IOCase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */