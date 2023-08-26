/*     */ package org.apache.commons.codec.language.bm;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Scanner;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Languages
/*     */ {
/*     */   public static final String ANY = "any";
/*     */   
/*     */   public static abstract class LanguageSet
/*     */   {
/*     */     public static LanguageSet from(Set<String> langs) {
/*  64 */       return langs.isEmpty() ? Languages.NO_LANGUAGES : new Languages.SomeLanguages(langs);
/*     */     }
/*     */ 
/*     */     
/*     */     public abstract boolean contains(String param1String);
/*     */     
/*     */     public abstract String getAny();
/*     */     
/*     */     public abstract boolean isEmpty();
/*     */     
/*     */     public abstract boolean isSingleton();
/*     */     
/*     */     public abstract LanguageSet restrictTo(LanguageSet param1LanguageSet);
/*     */   }
/*     */   
/*     */   public static final class SomeLanguages
/*     */     extends LanguageSet
/*     */   {
/*     */     private final Set<String> languages;
/*     */     
/*     */     private SomeLanguages(Set<String> languages) {
/*  85 */       this.languages = Collections.unmodifiableSet(languages);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(String language) {
/*  90 */       return this.languages.contains(language);
/*     */     }
/*     */ 
/*     */     
/*     */     public String getAny() {
/*  95 */       return this.languages.iterator().next();
/*     */     }
/*     */     
/*     */     public Set<String> getLanguages() {
/*  99 */       return this.languages;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 104 */       return this.languages.isEmpty();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isSingleton() {
/* 109 */       return (this.languages.size() == 1);
/*     */     }
/*     */ 
/*     */     
/*     */     public Languages.LanguageSet restrictTo(Languages.LanguageSet other) {
/* 114 */       if (other == Languages.NO_LANGUAGES)
/* 115 */         return other; 
/* 116 */       if (other == Languages.ANY_LANGUAGE) {
/* 117 */         return this;
/*     */       }
/* 119 */       SomeLanguages sl = (SomeLanguages)other;
/* 120 */       Set<String> ls = new HashSet<String>(Math.min(this.languages.size(), sl.languages.size()));
/* 121 */       for (String lang : this.languages) {
/* 122 */         if (sl.languages.contains(lang)) {
/* 123 */           ls.add(lang);
/*     */         }
/*     */       } 
/* 126 */       return from(ls);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 132 */       return "Languages(" + this.languages.toString() + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 139 */   private static final Map<NameType, Languages> LANGUAGES = new EnumMap<NameType, Languages>(NameType.class); private final Set<String> languages;
/*     */   
/*     */   static {
/* 142 */     for (NameType s : NameType.values()) {
/* 143 */       LANGUAGES.put(s, getInstance(langResourceName(s)));
/*     */     }
/*     */   }
/*     */   
/*     */   public static Languages getInstance(NameType nameType) {
/* 148 */     return LANGUAGES.get(nameType);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Languages getInstance(String languagesResourceName) {
/* 153 */     Set<String> ls = new HashSet<String>();
/* 154 */     InputStream langIS = Languages.class.getClassLoader().getResourceAsStream(languagesResourceName);
/*     */     
/* 156 */     if (langIS == null) {
/* 157 */       throw new IllegalArgumentException("Unable to resolve required resource: " + languagesResourceName);
/*     */     }
/*     */     
/* 160 */     Scanner lsScanner = new Scanner(langIS, "UTF-8");
/*     */     try {
/* 162 */       boolean inExtendedComment = false;
/* 163 */       while (lsScanner.hasNextLine()) {
/* 164 */         String line = lsScanner.nextLine().trim();
/* 165 */         if (inExtendedComment) {
/* 166 */           if (line.endsWith("*/"))
/* 167 */             inExtendedComment = false; 
/*     */           continue;
/*     */         } 
/* 170 */         if (line.startsWith("/*")) {
/* 171 */           inExtendedComment = true; continue;
/* 172 */         }  if (line.length() > 0) {
/* 173 */           ls.add(line);
/*     */         }
/*     */       } 
/*     */     } finally {
/*     */       
/* 178 */       lsScanner.close();
/*     */     } 
/*     */     
/* 181 */     return new Languages(Collections.unmodifiableSet(ls));
/*     */   }
/*     */   
/*     */   private static String langResourceName(NameType nameType) {
/* 185 */     return String.format("org/apache/commons/codec/language/bm/%s_languages.txt", new Object[] { nameType.getName() });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 193 */   public static final LanguageSet NO_LANGUAGES = new LanguageSet()
/*     */     {
/*     */       public boolean contains(String language) {
/* 196 */         return false;
/*     */       }
/*     */ 
/*     */       
/*     */       public String getAny() {
/* 201 */         throw new NoSuchElementException("Can't fetch any language from the empty language set.");
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean isEmpty() {
/* 206 */         return true;
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean isSingleton() {
/* 211 */         return false;
/*     */       }
/*     */ 
/*     */       
/*     */       public Languages.LanguageSet restrictTo(Languages.LanguageSet other) {
/* 216 */         return this;
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 221 */         return "NO_LANGUAGES";
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 228 */   public static final LanguageSet ANY_LANGUAGE = new LanguageSet()
/*     */     {
/*     */       public boolean contains(String language) {
/* 231 */         return true;
/*     */       }
/*     */ 
/*     */       
/*     */       public String getAny() {
/* 236 */         throw new NoSuchElementException("Can't fetch any language from the any language set.");
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean isEmpty() {
/* 241 */         return false;
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean isSingleton() {
/* 246 */         return false;
/*     */       }
/*     */ 
/*     */       
/*     */       public Languages.LanguageSet restrictTo(Languages.LanguageSet other) {
/* 251 */         return other;
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 256 */         return "ANY_LANGUAGE";
/*     */       }
/*     */     };
/*     */   
/*     */   private Languages(Set<String> languages) {
/* 261 */     this.languages = languages;
/*     */   }
/*     */   
/*     */   public Set<String> getLanguages() {
/* 265 */     return this.languages;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\codec\language\bm\Languages.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */