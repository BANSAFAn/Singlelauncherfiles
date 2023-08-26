/*     */ package org.apache.http.params;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
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
/*     */ @Deprecated
/*     */ public final class DefaultedHttpParams
/*     */   extends AbstractHttpParams
/*     */ {
/*     */   private final HttpParams local;
/*     */   private final HttpParams defaults;
/*     */   
/*     */   public DefaultedHttpParams(HttpParams local, HttpParams defaults) {
/*  60 */     this.local = (HttpParams)Args.notNull(local, "Local HTTP parameters");
/*  61 */     this.defaults = defaults;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpParams copy() {
/*  68 */     HttpParams clone = this.local.copy();
/*  69 */     return new DefaultedHttpParams(clone, this.defaults);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getParameter(String name) {
/*  78 */     Object obj = this.local.getParameter(name);
/*  79 */     if (obj == null && this.defaults != null) {
/*  80 */       obj = this.defaults.getParameter(name);
/*     */     }
/*  82 */     return obj;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean removeParameter(String name) {
/*  90 */     return this.local.removeParameter(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpParams setParameter(String name, Object value) {
/*  98 */     return this.local.setParameter(name, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpParams getDefaults() {
/* 106 */     return this.defaults;
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
/*     */   public Set<String> getNames() {
/* 122 */     Set<String> combined = new HashSet<String>(getNames(this.defaults));
/* 123 */     combined.addAll(getNames(this.local));
/* 124 */     return combined;
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
/*     */   public Set<String> getDefaultNames() {
/* 138 */     return new HashSet<String>(getNames(this.defaults));
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
/*     */   public Set<String> getLocalNames() {
/* 152 */     return new HashSet<String>(getNames(this.local));
/*     */   }
/*     */ 
/*     */   
/*     */   private Set<String> getNames(HttpParams params) {
/* 157 */     if (params instanceof HttpParamsNames) {
/* 158 */       return ((HttpParamsNames)params).getNames();
/*     */     }
/* 160 */     throw new UnsupportedOperationException("HttpParams instance does not implement HttpParamsNames");
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\params\DefaultedHttpParams.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */