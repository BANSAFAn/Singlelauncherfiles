/*     */ package org.apache.http.impl.execchain;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpEntityEnclosingRequest;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @NotThreadSafe
/*     */ class RequestEntityProxy
/*     */   implements HttpEntity
/*     */ {
/*     */   private final HttpEntity original;
/*     */   
/*     */   static void enhance(HttpEntityEnclosingRequest request) {
/*  48 */     HttpEntity entity = request.getEntity();
/*  49 */     if (entity != null && !entity.isRepeatable() && !isEnhanced(entity)) {
/*  50 */       request.setEntity(new RequestEntityProxy(entity));
/*     */     }
/*     */   }
/*     */   
/*     */   static boolean isEnhanced(HttpEntity entity) {
/*  55 */     return entity instanceof RequestEntityProxy;
/*     */   }
/*     */   
/*     */   static boolean isRepeatable(HttpRequest request) {
/*  59 */     if (request instanceof HttpEntityEnclosingRequest) {
/*  60 */       HttpEntity entity = ((HttpEntityEnclosingRequest)request).getEntity();
/*  61 */       if (entity != null) {
/*  62 */         if (isEnhanced(entity)) {
/*  63 */           RequestEntityProxy proxy = (RequestEntityProxy)entity;
/*  64 */           if (!proxy.isConsumed()) {
/*  65 */             return true;
/*     */           }
/*     */         } 
/*  68 */         return entity.isRepeatable();
/*     */       } 
/*     */     } 
/*  71 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean consumed = false;
/*     */ 
/*     */   
/*     */   RequestEntityProxy(HttpEntity original) {
/*  79 */     this.original = original;
/*     */   }
/*     */   
/*     */   public HttpEntity getOriginal() {
/*  83 */     return this.original;
/*     */   }
/*     */   
/*     */   public boolean isConsumed() {
/*  87 */     return this.consumed;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRepeatable() {
/*  92 */     return this.original.isRepeatable();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isChunked() {
/*  97 */     return this.original.isChunked();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getContentLength() {
/* 102 */     return this.original.getContentLength();
/*     */   }
/*     */ 
/*     */   
/*     */   public Header getContentType() {
/* 107 */     return this.original.getContentType();
/*     */   }
/*     */ 
/*     */   
/*     */   public Header getContentEncoding() {
/* 112 */     return this.original.getContentEncoding();
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream getContent() throws IOException, IllegalStateException {
/* 117 */     return this.original.getContent();
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeTo(OutputStream outstream) throws IOException {
/* 122 */     this.consumed = true;
/* 123 */     this.original.writeTo(outstream);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStreaming() {
/* 128 */     return this.original.isStreaming();
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void consumeContent() throws IOException {
/* 134 */     this.consumed = true;
/* 135 */     this.original.consumeContent();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 140 */     StringBuilder sb = new StringBuilder("RequestEntityProxy{");
/* 141 */     sb.append(this.original);
/* 142 */     sb.append('}');
/* 143 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\execchain\RequestEntityProxy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */