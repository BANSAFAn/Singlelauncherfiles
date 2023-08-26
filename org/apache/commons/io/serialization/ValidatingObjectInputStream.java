/*     */ package org.apache.commons.io.serialization;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InvalidClassException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectStreamClass;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ValidatingObjectInputStream
/*     */   extends ObjectInputStream
/*     */ {
/*  46 */   private final List<ClassNameMatcher> acceptMatchers = new ArrayList<>();
/*  47 */   private final List<ClassNameMatcher> rejectMatchers = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ValidatingObjectInputStream(InputStream input) throws IOException {
/*  59 */     super(input);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void validateClassName(String name) throws InvalidClassException {
/*  68 */     for (ClassNameMatcher m : this.rejectMatchers) {
/*  69 */       if (m.matches(name)) {
/*  70 */         invalidClassNameFound(name);
/*     */       }
/*     */     } 
/*     */     
/*  74 */     boolean ok = false;
/*  75 */     for (ClassNameMatcher m : this.acceptMatchers) {
/*  76 */       if (m.matches(name)) {
/*  77 */         ok = true;
/*     */         break;
/*     */       } 
/*     */     } 
/*  81 */     if (!ok) {
/*  82 */       invalidClassNameFound(name);
/*     */     }
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
/*     */   protected void invalidClassNameFound(String className) throws InvalidClassException {
/*  95 */     throw new InvalidClassException("Class name not accepted: " + className);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Class<?> resolveClass(ObjectStreamClass osc) throws IOException, ClassNotFoundException {
/* 100 */     validateClassName(osc.getName());
/* 101 */     return super.resolveClass(osc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ValidatingObjectInputStream accept(Class<?>... classes) {
/* 112 */     for (Class<?> c : classes) {
/* 113 */       this.acceptMatchers.add(new FullClassNameMatcher(new String[] { c.getName() }));
/*     */     } 
/* 115 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ValidatingObjectInputStream reject(Class<?>... classes) {
/* 126 */     for (Class<?> c : classes) {
/* 127 */       this.rejectMatchers.add(new FullClassNameMatcher(new String[] { c.getName() }));
/*     */     } 
/* 129 */     return this;
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
/*     */   public ValidatingObjectInputStream accept(String... patterns) {
/* 141 */     for (String pattern : patterns) {
/* 142 */       this.acceptMatchers.add(new WildcardClassNameMatcher(pattern));
/*     */     }
/* 144 */     return this;
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
/*     */   public ValidatingObjectInputStream reject(String... patterns) {
/* 156 */     for (String pattern : patterns) {
/* 157 */       this.rejectMatchers.add(new WildcardClassNameMatcher(pattern));
/*     */     }
/* 159 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ValidatingObjectInputStream accept(Pattern pattern) {
/* 170 */     this.acceptMatchers.add(new RegexpClassNameMatcher(pattern));
/* 171 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ValidatingObjectInputStream reject(Pattern pattern) {
/* 182 */     this.rejectMatchers.add(new RegexpClassNameMatcher(pattern));
/* 183 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ValidatingObjectInputStream accept(ClassNameMatcher m) {
/* 194 */     this.acceptMatchers.add(m);
/* 195 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ValidatingObjectInputStream reject(ClassNameMatcher m) {
/* 206 */     this.rejectMatchers.add(m);
/* 207 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\serialization\ValidatingObjectInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */