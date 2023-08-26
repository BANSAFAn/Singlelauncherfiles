/*     */ package org.apache.http.protocol;
/*     */ 
/*     */ import org.apache.http.HttpRequest;
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
/*     */ @ThreadSafe
/*     */ public class UriHttpRequestHandlerMapper
/*     */   implements HttpRequestHandlerMapper
/*     */ {
/*     */   private final UriPatternMatcher<HttpRequestHandler> matcher;
/*     */   
/*     */   protected UriHttpRequestHandlerMapper(UriPatternMatcher<HttpRequestHandler> matcher) {
/*  58 */     this.matcher = (UriPatternMatcher<HttpRequestHandler>)Args.notNull(matcher, "Pattern matcher");
/*     */   }
/*     */   
/*     */   public UriHttpRequestHandlerMapper() {
/*  62 */     this(new UriPatternMatcher<HttpRequestHandler>());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void register(String pattern, HttpRequestHandler handler) {
/*  73 */     Args.notNull(pattern, "Pattern");
/*  74 */     Args.notNull(handler, "Handler");
/*  75 */     this.matcher.register(pattern, handler);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unregister(String pattern) {
/*  84 */     this.matcher.unregister(pattern);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getRequestPath(HttpRequest request) {
/*  91 */     String uriPath = request.getRequestLine().getUri();
/*  92 */     int index = uriPath.indexOf("?");
/*  93 */     if (index != -1) {
/*  94 */       uriPath = uriPath.substring(0, index);
/*     */     } else {
/*  96 */       index = uriPath.indexOf("#");
/*  97 */       if (index != -1) {
/*  98 */         uriPath = uriPath.substring(0, index);
/*     */       }
/*     */     } 
/* 101 */     return uriPath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpRequestHandler lookup(HttpRequest request) {
/* 112 */     Args.notNull(request, "HTTP request");
/* 113 */     return this.matcher.lookup(getRequestPath(request));
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\protocol\UriHttpRequestHandlerMapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */