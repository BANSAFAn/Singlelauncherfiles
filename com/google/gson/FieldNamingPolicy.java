/*     */ package com.google.gson;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.Locale;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum FieldNamingPolicy
/*     */   implements FieldNamingStrategy
/*     */ {
/*  37 */   IDENTITY {
/*     */     public String translateName(Field f) {
/*  39 */       return f.getName();
/*     */     }
/*     */   },
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  53 */   UPPER_CAMEL_CASE {
/*     */     public String translateName(Field f) {
/*  55 */       return null.upperCaseFirstLetter(f.getName());
/*     */     }
/*     */   },
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  72 */   UPPER_CAMEL_CASE_WITH_SPACES {
/*     */     public String translateName(Field f) {
/*  74 */       return null.upperCaseFirstLetter(null.separateCamelCase(f.getName(), " "));
/*     */     }
/*     */   },
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  90 */   LOWER_CASE_WITH_UNDERSCORES {
/*     */     public String translateName(Field f) {
/*  92 */       return null.separateCamelCase(f.getName(), "_").toLowerCase(Locale.ENGLISH);
/*     */     }
/*     */   },
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 113 */   LOWER_CASE_WITH_DASHES {
/*     */     public String translateName(Field f) {
/* 115 */       return null.separateCamelCase(f.getName(), "-").toLowerCase(Locale.ENGLISH);
/*     */     }
/*     */   },
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 136 */   LOWER_CASE_WITH_DOTS {
/*     */     public String translateName(Field f) {
/* 138 */       return null.separateCamelCase(f.getName(), ".").toLowerCase(Locale.ENGLISH);
/*     */     }
/*     */   };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String separateCamelCase(String name, String separator) {
/* 147 */     StringBuilder translation = new StringBuilder();
/* 148 */     for (int i = 0, length = name.length(); i < length; i++) {
/* 149 */       char character = name.charAt(i);
/* 150 */       if (Character.isUpperCase(character) && translation.length() != 0) {
/* 151 */         translation.append(separator);
/*     */       }
/* 153 */       translation.append(character);
/*     */     } 
/* 155 */     return translation.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String upperCaseFirstLetter(String name) {
/* 162 */     int firstLetterIndex = 0;
/* 163 */     int limit = name.length() - 1;
/* 164 */     for (; !Character.isLetter(name.charAt(firstLetterIndex)) && firstLetterIndex < limit; firstLetterIndex++);
/*     */     
/* 166 */     char firstLetter = name.charAt(firstLetterIndex);
/* 167 */     if (Character.isUpperCase(firstLetter)) {
/* 168 */       return name;
/*     */     }
/*     */     
/* 171 */     char uppercased = Character.toUpperCase(firstLetter);
/* 172 */     if (firstLetterIndex == 0) {
/* 173 */       return uppercased + name.substring(1);
/*     */     }
/*     */     
/* 176 */     return name.substring(0, firstLetterIndex) + uppercased + name.substring(firstLetterIndex + 1);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\com\google\gson\FieldNamingPolicy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */