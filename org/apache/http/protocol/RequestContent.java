/*     */ package org.apache.http.protocol;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpEntityEnclosingRequest;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpRequestInterceptor;
/*     */ import org.apache.http.HttpVersion;
/*     */ import org.apache.http.ProtocolException;
/*     */ import org.apache.http.ProtocolVersion;
/*     */ import org.apache.http.annotation.Immutable;
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
/*     */ @Immutable
/*     */ public class RequestContent
/*     */   implements HttpRequestInterceptor
/*     */ {
/*     */   private final boolean overwrite;
/*     */   
/*     */   public RequestContent() {
/*  64 */     this(false);
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
/*     */   public RequestContent(boolean overwrite) {
/*  80 */     this.overwrite = overwrite;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
/*  86 */     Args.notNull(request, "HTTP request");
/*  87 */     if (request instanceof HttpEntityEnclosingRequest) {
/*  88 */       if (this.overwrite) {
/*  89 */         request.removeHeaders("Transfer-Encoding");
/*  90 */         request.removeHeaders("Content-Length");
/*     */       } else {
/*  92 */         if (request.containsHeader("Transfer-Encoding")) {
/*  93 */           throw new ProtocolException("Transfer-encoding header already present");
/*     */         }
/*  95 */         if (request.containsHeader("Content-Length")) {
/*  96 */           throw new ProtocolException("Content-Length header already present");
/*     */         }
/*     */       } 
/*  99 */       ProtocolVersion ver = request.getRequestLine().getProtocolVersion();
/* 100 */       HttpEntity entity = ((HttpEntityEnclosingRequest)request).getEntity();
/* 101 */       if (entity == null) {
/* 102 */         request.addHeader("Content-Length", "0");
/*     */         
/*     */         return;
/*     */       } 
/* 106 */       if (entity.isChunked() || entity.getContentLength() < 0L) {
/* 107 */         if (ver.lessEquals((ProtocolVersion)HttpVersion.HTTP_1_0)) {
/* 108 */           throw new ProtocolException("Chunked transfer encoding not allowed for " + ver);
/*     */         }
/*     */         
/* 111 */         request.addHeader("Transfer-Encoding", "chunked");
/*     */       } else {
/* 113 */         request.addHeader("Content-Length", Long.toString(entity.getContentLength()));
/*     */       } 
/*     */       
/* 116 */       if (entity.getContentType() != null && !request.containsHeader("Content-Type"))
/*     */       {
/* 118 */         request.addHeader(entity.getContentType());
/*     */       }
/*     */       
/* 121 */       if (entity.getContentEncoding() != null && !request.containsHeader("Content-Encoding"))
/*     */       {
/* 123 */         request.addHeader(entity.getContentEncoding());
/*     */       }
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\protocol\RequestContent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */