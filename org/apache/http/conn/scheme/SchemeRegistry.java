/*     */ package org.apache.http.conn.scheme;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.http.HttpHost;
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
/*     */ @Deprecated
/*     */ @ThreadSafe
/*     */ public final class SchemeRegistry
/*     */ {
/*  58 */   private final ConcurrentHashMap<String, Scheme> registeredSchemes = new ConcurrentHashMap<String, Scheme>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Scheme getScheme(String name) {
/*  72 */     Scheme found = get(name);
/*  73 */     if (found == null) {
/*  74 */       throw new IllegalStateException("Scheme '" + name + "' not registered.");
/*     */     }
/*     */     
/*  77 */     return found;
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
/*     */   public final Scheme getScheme(HttpHost host) {
/*  92 */     Args.notNull(host, "Host");
/*  93 */     return getScheme(host.getSchemeName());
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
/*     */   public final Scheme get(String name) {
/* 105 */     Args.notNull(name, "Scheme name");
/*     */ 
/*     */     
/* 108 */     Scheme found = this.registeredSchemes.get(name);
/* 109 */     return found;
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
/*     */   public final Scheme register(Scheme sch) {
/* 123 */     Args.notNull(sch, "Scheme");
/* 124 */     Scheme old = this.registeredSchemes.put(sch.getName(), sch);
/* 125 */     return old;
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
/*     */   public final Scheme unregister(String name) {
/* 137 */     Args.notNull(name, "Scheme name");
/*     */ 
/*     */     
/* 140 */     Scheme gone = this.registeredSchemes.remove(name);
/* 141 */     return gone;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final List<String> getSchemeNames() {
/* 150 */     return new ArrayList<String>(this.registeredSchemes.keySet());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setItems(Map<String, Scheme> map) {
/* 160 */     if (map == null) {
/*     */       return;
/*     */     }
/* 163 */     this.registeredSchemes.clear();
/* 164 */     this.registeredSchemes.putAll(map);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\conn\scheme\SchemeRegistry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */