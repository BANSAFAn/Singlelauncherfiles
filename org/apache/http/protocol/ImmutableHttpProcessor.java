/*     */ package org.apache.http.protocol;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpRequestInterceptor;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.HttpResponseInterceptor;
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
/*     */ @ThreadSafe
/*     */ public final class ImmutableHttpProcessor
/*     */   implements HttpProcessor
/*     */ {
/*     */   private final HttpRequestInterceptor[] requestInterceptors;
/*     */   private final HttpResponseInterceptor[] responseInterceptors;
/*     */   
/*     */   public ImmutableHttpProcessor(HttpRequestInterceptor[] requestInterceptors, HttpResponseInterceptor[] responseInterceptors) {
/*  54 */     if (requestInterceptors != null) {
/*  55 */       int l = requestInterceptors.length;
/*  56 */       this.requestInterceptors = new HttpRequestInterceptor[l];
/*  57 */       System.arraycopy(requestInterceptors, 0, this.requestInterceptors, 0, l);
/*     */     } else {
/*  59 */       this.requestInterceptors = new HttpRequestInterceptor[0];
/*     */     } 
/*  61 */     if (responseInterceptors != null) {
/*  62 */       int l = responseInterceptors.length;
/*  63 */       this.responseInterceptors = new HttpResponseInterceptor[l];
/*  64 */       System.arraycopy(responseInterceptors, 0, this.responseInterceptors, 0, l);
/*     */     } else {
/*  66 */       this.responseInterceptors = new HttpResponseInterceptor[0];
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableHttpProcessor(List<HttpRequestInterceptor> requestInterceptors, List<HttpResponseInterceptor> responseInterceptors) {
/*  77 */     if (requestInterceptors != null) {
/*  78 */       int l = requestInterceptors.size();
/*  79 */       this.requestInterceptors = requestInterceptors.<HttpRequestInterceptor>toArray(new HttpRequestInterceptor[l]);
/*     */     } else {
/*  81 */       this.requestInterceptors = new HttpRequestInterceptor[0];
/*     */     } 
/*  83 */     if (responseInterceptors != null) {
/*  84 */       int l = responseInterceptors.size();
/*  85 */       this.responseInterceptors = responseInterceptors.<HttpResponseInterceptor>toArray(new HttpResponseInterceptor[l]);
/*     */     } else {
/*  87 */       this.responseInterceptors = new HttpResponseInterceptor[0];
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public ImmutableHttpProcessor(HttpRequestInterceptorList requestInterceptors, HttpResponseInterceptorList responseInterceptors) {
/*  99 */     if (requestInterceptors != null) {
/* 100 */       int count = requestInterceptors.getRequestInterceptorCount();
/* 101 */       this.requestInterceptors = new HttpRequestInterceptor[count];
/* 102 */       for (int i = 0; i < count; i++) {
/* 103 */         this.requestInterceptors[i] = requestInterceptors.getRequestInterceptor(i);
/*     */       }
/*     */     } else {
/* 106 */       this.requestInterceptors = new HttpRequestInterceptor[0];
/*     */     } 
/* 108 */     if (responseInterceptors != null) {
/* 109 */       int count = responseInterceptors.getResponseInterceptorCount();
/* 110 */       this.responseInterceptors = new HttpResponseInterceptor[count];
/* 111 */       for (int i = 0; i < count; i++) {
/* 112 */         this.responseInterceptors[i] = responseInterceptors.getResponseInterceptor(i);
/*     */       }
/*     */     } else {
/* 115 */       this.responseInterceptors = new HttpResponseInterceptor[0];
/*     */     } 
/*     */   }
/*     */   
/*     */   public ImmutableHttpProcessor(HttpRequestInterceptor... requestInterceptors) {
/* 120 */     this(requestInterceptors, (HttpResponseInterceptor[])null);
/*     */   }
/*     */   
/*     */   public ImmutableHttpProcessor(HttpResponseInterceptor... responseInterceptors) {
/* 124 */     this((HttpRequestInterceptor[])null, responseInterceptors);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void process(HttpRequest request, HttpContext context) throws IOException, HttpException {
/* 131 */     for (HttpRequestInterceptor requestInterceptor : this.requestInterceptors) {
/* 132 */       requestInterceptor.process(request, context);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void process(HttpResponse response, HttpContext context) throws IOException, HttpException {
/* 140 */     for (HttpResponseInterceptor responseInterceptor : this.responseInterceptors)
/* 141 */       responseInterceptor.process(response, context); 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\protocol\ImmutableHttpProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */