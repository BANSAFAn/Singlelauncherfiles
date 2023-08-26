/*     */ package org.apache.http.protocol;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.http.annotation.GuardedBy;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.util.Args;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ThreadSafe
/*     */ public class UriPatternMatcher<T>
/*     */ {
/*     */   @GuardedBy("this")
/*  60 */   private final Map<String, T> map = new HashMap<String, T>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void register(String pattern, T obj) {
/*  70 */     Args.notNull(pattern, "URI request pattern");
/*  71 */     this.map.put(pattern, obj);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void unregister(String pattern) {
/*  80 */     if (pattern == null) {
/*     */       return;
/*     */     }
/*  83 */     this.map.remove(pattern);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public synchronized void setHandlers(Map<String, T> map) {
/*  91 */     Args.notNull(map, "Map of handlers");
/*  92 */     this.map.clear();
/*  93 */     this.map.putAll(map);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public synchronized void setObjects(Map<String, T> map) {
/* 101 */     Args.notNull(map, "Map of handlers");
/* 102 */     this.map.clear();
/* 103 */     this.map.putAll(map);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public synchronized Map<String, T> getObjects() {
/* 111 */     return this.map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized T lookup(String path) {
/* 121 */     Args.notNull(path, "Request path");
/*     */     
/* 123 */     T obj = this.map.get(path);
/* 124 */     if (obj == null) {
/*     */       
/* 126 */       String bestMatch = null;
/* 127 */       for (String pattern : this.map.keySet()) {
/* 128 */         if (matchUriRequestPattern(pattern, path))
/*     */         {
/* 130 */           if (bestMatch == null || bestMatch.length() < pattern.length() || (bestMatch.length() == pattern.length() && pattern.endsWith("*"))) {
/*     */ 
/*     */             
/* 133 */             obj = this.map.get(pattern);
/* 134 */             bestMatch = pattern;
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/* 139 */     return obj;
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
/*     */   protected boolean matchUriRequestPattern(String pattern, String path) {
/* 151 */     if (pattern.equals("*")) {
/* 152 */       return true;
/*     */     }
/* 154 */     return ((pattern.endsWith("*") && path.startsWith(pattern.substring(0, pattern.length() - 1))) || (pattern.startsWith("*") && path.endsWith(pattern.substring(1, pattern.length()))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 162 */     return this.map.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\protocol\UriPatternMatcher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */