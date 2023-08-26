/*     */ package org.apache.http.params;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class BasicHttpParams
/*     */   extends AbstractHttpParams
/*     */   implements Serializable, Cloneable
/*     */ {
/*     */   private static final long serialVersionUID = -7086398485908701455L;
/*  56 */   private final Map<String, Object> parameters = new ConcurrentHashMap<String, Object>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getParameter(String name) {
/*  64 */     return this.parameters.get(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpParams setParameter(String name, Object value) {
/*  69 */     if (name == null) {
/*  70 */       return this;
/*     */     }
/*  72 */     if (value != null) {
/*  73 */       this.parameters.put(name, value);
/*     */     } else {
/*  75 */       this.parameters.remove(name);
/*     */     } 
/*  77 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean removeParameter(String name) {
/*  83 */     if (this.parameters.containsKey(name)) {
/*  84 */       this.parameters.remove(name);
/*  85 */       return true;
/*     */     } 
/*  87 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParameters(String[] names, Object value) {
/*  98 */     for (String name : names) {
/*  99 */       setParameter(name, value);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isParameterSet(String name) {
/* 115 */     return (getParameter(name) != null);
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
/*     */   public boolean isParameterSetLocally(String name) {
/* 129 */     return (this.parameters.get(name) != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 136 */     this.parameters.clear();
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
/*     */   public HttpParams copy() {
/*     */     try {
/* 151 */       return (HttpParams)clone();
/* 152 */     } catch (CloneNotSupportedException ex) {
/* 153 */       throw new UnsupportedOperationException("Cloning not supported");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException {
/* 163 */     BasicHttpParams clone = (BasicHttpParams)super.clone();
/* 164 */     copyParams(clone);
/* 165 */     return clone;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void copyParams(HttpParams target) {
/* 176 */     for (Map.Entry<String, Object> me : this.parameters.entrySet()) {
/* 177 */       target.setParameter(me.getKey(), me.getValue());
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
/*     */ 
/*     */   
/*     */   public Set<String> getNames() {
/* 192 */     return new HashSet<String>(this.parameters.keySet());
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\params\BasicHttpParams.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */