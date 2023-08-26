/*     */ package org.apache.http.protocol;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpRequestInterceptor;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.HttpResponseInterceptor;
/*     */ import org.apache.http.annotation.NotThreadSafe;
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
/*     */ @NotThreadSafe
/*     */ public final class BasicHttpProcessor
/*     */   implements HttpProcessor, HttpRequestInterceptorList, HttpResponseInterceptorList, Cloneable
/*     */ {
/*  59 */   protected final List<HttpRequestInterceptor> requestInterceptors = new ArrayList<HttpRequestInterceptor>();
/*  60 */   protected final List<HttpResponseInterceptor> responseInterceptors = new ArrayList<HttpResponseInterceptor>();
/*     */   
/*     */   public void addRequestInterceptor(HttpRequestInterceptor itcp) {
/*  63 */     if (itcp == null) {
/*     */       return;
/*     */     }
/*  66 */     this.requestInterceptors.add(itcp);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addRequestInterceptor(HttpRequestInterceptor itcp, int index) {
/*  71 */     if (itcp == null) {
/*     */       return;
/*     */     }
/*  74 */     this.requestInterceptors.add(index, itcp);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addResponseInterceptor(HttpResponseInterceptor itcp, int index) {
/*  79 */     if (itcp == null) {
/*     */       return;
/*     */     }
/*  82 */     this.responseInterceptors.add(index, itcp);
/*     */   }
/*     */   
/*     */   public void removeRequestInterceptorByClass(Class<? extends HttpRequestInterceptor> clazz) {
/*  86 */     Iterator<HttpRequestInterceptor> it = this.requestInterceptors.iterator();
/*  87 */     while (it.hasNext()) {
/*  88 */       Object request = it.next();
/*  89 */       if (request.getClass().equals(clazz)) {
/*  90 */         it.remove();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void removeResponseInterceptorByClass(Class<? extends HttpResponseInterceptor> clazz) {
/*  96 */     Iterator<HttpResponseInterceptor> it = this.responseInterceptors.iterator();
/*  97 */     while (it.hasNext()) {
/*  98 */       Object request = it.next();
/*  99 */       if (request.getClass().equals(clazz)) {
/* 100 */         it.remove();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public final void addInterceptor(HttpRequestInterceptor interceptor) {
/* 106 */     addRequestInterceptor(interceptor);
/*     */   }
/*     */   
/*     */   public final void addInterceptor(HttpRequestInterceptor interceptor, int index) {
/* 110 */     addRequestInterceptor(interceptor, index);
/*     */   }
/*     */   
/*     */   public int getRequestInterceptorCount() {
/* 114 */     return this.requestInterceptors.size();
/*     */   }
/*     */   
/*     */   public HttpRequestInterceptor getRequestInterceptor(int index) {
/* 118 */     if (index < 0 || index >= this.requestInterceptors.size()) {
/* 119 */       return null;
/*     */     }
/* 121 */     return this.requestInterceptors.get(index);
/*     */   }
/*     */   
/*     */   public void clearRequestInterceptors() {
/* 125 */     this.requestInterceptors.clear();
/*     */   }
/*     */   
/*     */   public void addResponseInterceptor(HttpResponseInterceptor itcp) {
/* 129 */     if (itcp == null) {
/*     */       return;
/*     */     }
/* 132 */     this.responseInterceptors.add(itcp);
/*     */   }
/*     */   
/*     */   public final void addInterceptor(HttpResponseInterceptor interceptor) {
/* 136 */     addResponseInterceptor(interceptor);
/*     */   }
/*     */   
/*     */   public final void addInterceptor(HttpResponseInterceptor interceptor, int index) {
/* 140 */     addResponseInterceptor(interceptor, index);
/*     */   }
/*     */   
/*     */   public int getResponseInterceptorCount() {
/* 144 */     return this.responseInterceptors.size();
/*     */   }
/*     */   
/*     */   public HttpResponseInterceptor getResponseInterceptor(int index) {
/* 148 */     if (index < 0 || index >= this.responseInterceptors.size()) {
/* 149 */       return null;
/*     */     }
/* 151 */     return this.responseInterceptors.get(index);
/*     */   }
/*     */   
/*     */   public void clearResponseInterceptors() {
/* 155 */     this.responseInterceptors.clear();
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInterceptors(List<?> list) {
/* 176 */     Args.notNull(list, "Inteceptor list");
/* 177 */     this.requestInterceptors.clear();
/* 178 */     this.responseInterceptors.clear();
/* 179 */     for (Object obj : list) {
/* 180 */       if (obj instanceof HttpRequestInterceptor) {
/* 181 */         addInterceptor((HttpRequestInterceptor)obj);
/*     */       }
/* 183 */       if (obj instanceof HttpResponseInterceptor) {
/* 184 */         addInterceptor((HttpResponseInterceptor)obj);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearInterceptors() {
/* 193 */     clearRequestInterceptors();
/* 194 */     clearResponseInterceptors();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void process(HttpRequest request, HttpContext context) throws IOException, HttpException {
/* 201 */     for (HttpRequestInterceptor interceptor : this.requestInterceptors) {
/* 202 */       interceptor.process(request, context);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void process(HttpResponse response, HttpContext context) throws IOException, HttpException {
/* 210 */     for (HttpResponseInterceptor interceptor : this.responseInterceptors) {
/* 211 */       interceptor.process(response, context);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void copyInterceptors(BasicHttpProcessor target) {
/* 222 */     target.requestInterceptors.clear();
/* 223 */     target.requestInterceptors.addAll(this.requestInterceptors);
/* 224 */     target.responseInterceptors.clear();
/* 225 */     target.responseInterceptors.addAll(this.responseInterceptors);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicHttpProcessor copy() {
/* 234 */     BasicHttpProcessor clone = new BasicHttpProcessor();
/* 235 */     copyInterceptors(clone);
/* 236 */     return clone;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException {
/* 241 */     BasicHttpProcessor clone = (BasicHttpProcessor)super.clone();
/* 242 */     copyInterceptors(clone);
/* 243 */     return clone;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\protocol\BasicHttpProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */