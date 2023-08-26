/*     */ package org.apache.http.protocol;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.HttpResponseInterceptor;
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
/*     */ 
/*     */ @Immutable
/*     */ public class ResponseContent
/*     */   implements HttpResponseInterceptor
/*     */ {
/*     */   private final boolean overwrite;
/*     */   
/*     */   public ResponseContent() {
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
/*     */   public ResponseContent(boolean overwrite) {
/*  80 */     this.overwrite = overwrite;
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
/*     */   public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
/*  93 */     Args.notNull(response, "HTTP response");
/*  94 */     if (this.overwrite) {
/*  95 */       response.removeHeaders("Transfer-Encoding");
/*  96 */       response.removeHeaders("Content-Length");
/*     */     } else {
/*  98 */       if (response.containsHeader("Transfer-Encoding")) {
/*  99 */         throw new ProtocolException("Transfer-encoding header already present");
/*     */       }
/* 101 */       if (response.containsHeader("Content-Length")) {
/* 102 */         throw new ProtocolException("Content-Length header already present");
/*     */       }
/*     */     } 
/* 105 */     ProtocolVersion ver = response.getStatusLine().getProtocolVersion();
/* 106 */     HttpEntity entity = response.getEntity();
/* 107 */     if (entity != null) {
/* 108 */       long len = entity.getContentLength();
/* 109 */       if (entity.isChunked() && !ver.lessEquals((ProtocolVersion)HttpVersion.HTTP_1_0)) {
/* 110 */         response.addHeader("Transfer-Encoding", "chunked");
/* 111 */       } else if (len >= 0L) {
/* 112 */         response.addHeader("Content-Length", Long.toString(entity.getContentLength()));
/*     */       } 
/*     */       
/* 115 */       if (entity.getContentType() != null && !response.containsHeader("Content-Type"))
/*     */       {
/* 117 */         response.addHeader(entity.getContentType());
/*     */       }
/*     */       
/* 120 */       if (entity.getContentEncoding() != null && !response.containsHeader("Content-Encoding"))
/*     */       {
/* 122 */         response.addHeader(entity.getContentEncoding());
/*     */       }
/*     */     } else {
/* 125 */       int status = response.getStatusLine().getStatusCode();
/* 126 */       if (status != 204 && status != 304 && status != 205)
/*     */       {
/*     */         
/* 129 */         response.addHeader("Content-Length", "0");
/*     */       }
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\protocol\ResponseContent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */