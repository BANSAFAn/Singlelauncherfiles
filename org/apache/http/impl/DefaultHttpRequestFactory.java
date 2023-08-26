/*     */ package org.apache.http.impl;
/*     */ 
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpRequestFactory;
/*     */ import org.apache.http.MethodNotSupportedException;
/*     */ import org.apache.http.RequestLine;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.message.BasicHttpEntityEnclosingRequest;
/*     */ import org.apache.http.message.BasicHttpRequest;
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
/*     */ @Immutable
/*     */ public class DefaultHttpRequestFactory
/*     */   implements HttpRequestFactory
/*     */ {
/*  47 */   public static final DefaultHttpRequestFactory INSTANCE = new DefaultHttpRequestFactory();
/*     */   
/*  49 */   private static final String[] RFC2616_COMMON_METHODS = new String[] { "GET" };
/*     */ 
/*     */ 
/*     */   
/*  53 */   private static final String[] RFC2616_ENTITY_ENC_METHODS = new String[] { "POST", "PUT" };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  58 */   private static final String[] RFC2616_SPECIAL_METHODS = new String[] { "HEAD", "OPTIONS", "DELETE", "TRACE", "CONNECT" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isOneOf(String[] methods, String method) {
/*  72 */     for (String method2 : methods) {
/*  73 */       if (method2.equalsIgnoreCase(method)) {
/*  74 */         return true;
/*     */       }
/*     */     } 
/*  77 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpRequest newHttpRequest(RequestLine requestline) throws MethodNotSupportedException {
/*  83 */     Args.notNull(requestline, "Request line");
/*  84 */     String method = requestline.getMethod();
/*  85 */     if (isOneOf(RFC2616_COMMON_METHODS, method))
/*  86 */       return (HttpRequest)new BasicHttpRequest(requestline); 
/*  87 */     if (isOneOf(RFC2616_ENTITY_ENC_METHODS, method))
/*  88 */       return (HttpRequest)new BasicHttpEntityEnclosingRequest(requestline); 
/*  89 */     if (isOneOf(RFC2616_SPECIAL_METHODS, method)) {
/*  90 */       return (HttpRequest)new BasicHttpRequest(requestline);
/*     */     }
/*  92 */     throw new MethodNotSupportedException(method + " method not supported");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpRequest newHttpRequest(String method, String uri) throws MethodNotSupportedException {
/*  99 */     if (isOneOf(RFC2616_COMMON_METHODS, method))
/* 100 */       return (HttpRequest)new BasicHttpRequest(method, uri); 
/* 101 */     if (isOneOf(RFC2616_ENTITY_ENC_METHODS, method))
/* 102 */       return (HttpRequest)new BasicHttpEntityEnclosingRequest(method, uri); 
/* 103 */     if (isOneOf(RFC2616_SPECIAL_METHODS, method)) {
/* 104 */       return (HttpRequest)new BasicHttpRequest(method, uri);
/*     */     }
/* 106 */     throw new MethodNotSupportedException(method + " method not supported");
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\DefaultHttpRequestFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */