/*     */ package org.apache.http.util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Args
/*     */ {
/*     */   public static void check(boolean expression, String message) {
/*  35 */     if (!expression) {
/*  36 */       throw new IllegalArgumentException(message);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void check(boolean expression, String message, Object... args) {
/*  41 */     if (!expression) {
/*  42 */       throw new IllegalArgumentException(String.format(message, args));
/*     */     }
/*     */   }
/*     */   
/*     */   public static void check(boolean expression, String message, Object arg) {
/*  47 */     if (!expression) {
/*  48 */       throw new IllegalArgumentException(String.format(message, new Object[] { arg }));
/*     */     }
/*     */   }
/*     */   
/*     */   public static <T> T notNull(T argument, String name) {
/*  53 */     if (argument == null) {
/*  54 */       throw new IllegalArgumentException(name + " may not be null");
/*     */     }
/*  56 */     return argument;
/*     */   }
/*     */   
/*     */   public static <T extends CharSequence> T notEmpty(T argument, String name) {
/*  60 */     if (argument == null) {
/*  61 */       throw new IllegalArgumentException(name + " may not be null");
/*     */     }
/*  63 */     if (TextUtils.isEmpty((CharSequence)argument)) {
/*  64 */       throw new IllegalArgumentException(name + " may not be empty");
/*     */     }
/*  66 */     return argument;
/*     */   }
/*     */   
/*     */   public static <T extends CharSequence> T notBlank(T argument, String name) {
/*  70 */     if (argument == null) {
/*  71 */       throw new IllegalArgumentException(name + " may not be null");
/*     */     }
/*  73 */     if (TextUtils.isBlank((CharSequence)argument)) {
/*  74 */       throw new IllegalArgumentException(name + " may not be blank");
/*     */     }
/*  76 */     return argument;
/*     */   }
/*     */   
/*     */   public static <T extends CharSequence> T containsNoBlanks(T argument, String name) {
/*  80 */     if (argument == null) {
/*  81 */       throw new IllegalArgumentException(name + " may not be null");
/*     */     }
/*  83 */     if (TextUtils.containsBlanks((CharSequence)argument)) {
/*  84 */       throw new IllegalArgumentException(name + " may not contain blanks");
/*     */     }
/*  86 */     return argument;
/*     */   }
/*     */   
/*     */   public static <E, T extends java.util.Collection<E>> T notEmpty(T argument, String name) {
/*  90 */     if (argument == null) {
/*  91 */       throw new IllegalArgumentException(name + " may not be null");
/*     */     }
/*  93 */     if (argument.isEmpty()) {
/*  94 */       throw new IllegalArgumentException(name + " may not be empty");
/*     */     }
/*  96 */     return argument;
/*     */   }
/*     */   
/*     */   public static int positive(int n, String name) {
/* 100 */     if (n <= 0) {
/* 101 */       throw new IllegalArgumentException(name + " may not be negative or zero");
/*     */     }
/* 103 */     return n;
/*     */   }
/*     */   
/*     */   public static long positive(long n, String name) {
/* 107 */     if (n <= 0L) {
/* 108 */       throw new IllegalArgumentException(name + " may not be negative or zero");
/*     */     }
/* 110 */     return n;
/*     */   }
/*     */   
/*     */   public static int notNegative(int n, String name) {
/* 114 */     if (n < 0) {
/* 115 */       throw new IllegalArgumentException(name + " may not be negative");
/*     */     }
/* 117 */     return n;
/*     */   }
/*     */   
/*     */   public static long notNegative(long n, String name) {
/* 121 */     if (n < 0L) {
/* 122 */       throw new IllegalArgumentException(name + " may not be negative");
/*     */     }
/* 124 */     return n;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\htt\\util\Args.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */