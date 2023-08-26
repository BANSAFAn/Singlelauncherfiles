/*     */ package org.apache.http.cookie;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.config.Lookup;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.protocol.HttpContext;
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
/*     */ @Deprecated
/*     */ @ThreadSafe
/*     */ public final class CookieSpecRegistry
/*     */   implements Lookup<CookieSpecProvider>
/*     */ {
/*  61 */   private final ConcurrentHashMap<String, CookieSpecFactory> registeredSpecs = new ConcurrentHashMap<String, CookieSpecFactory>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void register(String name, CookieSpecFactory factory) {
/*  76 */     Args.notNull(name, "Name");
/*  77 */     Args.notNull(factory, "Cookie spec factory");
/*  78 */     this.registeredSpecs.put(name.toLowerCase(Locale.ENGLISH), factory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unregister(String id) {
/*  87 */     Args.notNull(id, "Id");
/*  88 */     this.registeredSpecs.remove(id.toLowerCase(Locale.ENGLISH));
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
/*     */   public CookieSpec getCookieSpec(String name, HttpParams params) throws IllegalStateException {
/* 105 */     Args.notNull(name, "Name");
/* 106 */     CookieSpecFactory factory = this.registeredSpecs.get(name.toLowerCase(Locale.ENGLISH));
/* 107 */     if (factory != null) {
/* 108 */       return factory.newInstance(params);
/*     */     }
/* 110 */     throw new IllegalStateException("Unsupported cookie spec: " + name);
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
/*     */   public CookieSpec getCookieSpec(String name) throws IllegalStateException {
/* 125 */     return getCookieSpec(name, null);
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
/*     */   public List<String> getSpecNames() {
/* 138 */     return new ArrayList<String>(this.registeredSpecs.keySet());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setItems(Map<String, CookieSpecFactory> map) {
/* 148 */     if (map == null) {
/*     */       return;
/*     */     }
/* 151 */     this.registeredSpecs.clear();
/* 152 */     this.registeredSpecs.putAll(map);
/*     */   }
/*     */ 
/*     */   
/*     */   public CookieSpecProvider lookup(final String name) {
/* 157 */     return new CookieSpecProvider()
/*     */       {
/*     */         public CookieSpec create(HttpContext context)
/*     */         {
/* 161 */           HttpRequest request = (HttpRequest)context.getAttribute("http.request");
/*     */           
/* 163 */           return CookieSpecRegistry.this.getCookieSpec(name, request.getParams());
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\cookie\CookieSpecRegistry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */