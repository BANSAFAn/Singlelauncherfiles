/*     */ package org.slf4j.helpers;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.slf4j.spi.MDCAdapter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BasicMDCAdapter
/*     */   implements MDCAdapter
/*     */ {
/*  46 */   private InheritableThreadLocal inheritableThreadLocal = new InheritableThreadLocal();
/*     */   
/*     */   static boolean isJDK14() {
/*     */     try {
/*  50 */       String javaVersion = System.getProperty("java.version");
/*  51 */       return javaVersion.startsWith("1.4");
/*  52 */     } catch (SecurityException se) {
/*     */       
/*  54 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*  58 */   static boolean IS_JDK14 = isJDK14();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void put(String key, String val) {
/*  74 */     if (key == null) {
/*  75 */       throw new IllegalArgumentException("key cannot be null");
/*     */     }
/*  77 */     Map<?, ?> map = this.inheritableThreadLocal.get();
/*  78 */     if (map == null) {
/*  79 */       map = Collections.synchronizedMap(new HashMap<Object, Object>());
/*  80 */       this.inheritableThreadLocal.set(map);
/*     */     } 
/*  82 */     map.put(key, val);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String get(String key) {
/*  89 */     Map Map = this.inheritableThreadLocal.get();
/*  90 */     if (Map != null && key != null) {
/*  91 */       return (String)Map.get(key);
/*     */     }
/*  93 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove(String key) {
/* 101 */     Map map = this.inheritableThreadLocal.get();
/* 102 */     if (map != null) {
/* 103 */       map.remove(key);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 111 */     Map map = this.inheritableThreadLocal.get();
/* 112 */     if (map != null) {
/* 113 */       map.clear();
/*     */ 
/*     */       
/* 116 */       if (isJDK14()) {
/* 117 */         this.inheritableThreadLocal.set(null);
/*     */       } else {
/* 119 */         this.inheritableThreadLocal.remove();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set getKeys() {
/* 131 */     Map map = this.inheritableThreadLocal.get();
/* 132 */     if (map != null) {
/* 133 */       return map.keySet();
/*     */     }
/* 135 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map getCopyOfContextMap() {
/* 144 */     Map<?, ?> oldMap = this.inheritableThreadLocal.get();
/* 145 */     if (oldMap != null) {
/* 146 */       Map<?, ?> newMap = Collections.synchronizedMap(new HashMap<Object, Object>());
/* 147 */       synchronized (oldMap) {
/* 148 */         newMap.putAll(oldMap);
/*     */       } 
/* 150 */       return newMap;
/*     */     } 
/* 152 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setContextMap(Map<?, ?> contextMap) {
/* 157 */     Map<?, ?> map = Collections.synchronizedMap(new HashMap<Object, Object>(contextMap));
/* 158 */     this.inheritableThreadLocal.set(map);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\slf4j\helpers\BasicMDCAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */