/*     */ package org.apache.http.auth;
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
/*     */ @Deprecated
/*     */ @ThreadSafe
/*     */ public final class AuthSchemeRegistry
/*     */   implements Lookup<AuthSchemeProvider>
/*     */ {
/*  59 */   private final ConcurrentHashMap<String, AuthSchemeFactory> registeredSchemes = new ConcurrentHashMap<String, AuthSchemeFactory>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void register(String name, AuthSchemeFactory factory) {
/*  80 */     Args.notNull(name, "Name");
/*  81 */     Args.notNull(factory, "Authentication scheme factory");
/*  82 */     this.registeredSchemes.put(name.toLowerCase(Locale.ENGLISH), factory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unregister(String name) {
/*  92 */     Args.notNull(name, "Name");
/*  93 */     this.registeredSchemes.remove(name.toLowerCase(Locale.ENGLISH));
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
/*     */   public AuthScheme getAuthScheme(String name, HttpParams params) throws IllegalStateException {
/* 110 */     Args.notNull(name, "Name");
/* 111 */     AuthSchemeFactory factory = this.registeredSchemes.get(name.toLowerCase(Locale.ENGLISH));
/* 112 */     if (factory != null) {
/* 113 */       return factory.newInstance(params);
/*     */     }
/* 115 */     throw new IllegalStateException("Unsupported authentication scheme: " + name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> getSchemeNames() {
/* 126 */     return new ArrayList<String>(this.registeredSchemes.keySet());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setItems(Map<String, AuthSchemeFactory> map) {
/* 136 */     if (map == null) {
/*     */       return;
/*     */     }
/* 139 */     this.registeredSchemes.clear();
/* 140 */     this.registeredSchemes.putAll(map);
/*     */   }
/*     */ 
/*     */   
/*     */   public AuthSchemeProvider lookup(final String name) {
/* 145 */     return new AuthSchemeProvider()
/*     */       {
/*     */         public AuthScheme create(HttpContext context)
/*     */         {
/* 149 */           HttpRequest request = (HttpRequest)context.getAttribute("http.request");
/*     */           
/* 151 */           return AuthSchemeRegistry.this.getAuthScheme(name, request.getParams());
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\auth\AuthSchemeRegistry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */