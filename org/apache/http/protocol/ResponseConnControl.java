/*     */ package org.apache.http.protocol;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.HttpResponseInterceptor;
/*     */ import org.apache.http.HttpVersion;
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
/*     */ public class ResponseConnControl
/*     */   implements HttpResponseInterceptor
/*     */ {
/*     */   public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
/*  62 */     Args.notNull(response, "HTTP response");
/*     */     
/*  64 */     HttpCoreContext corecontext = HttpCoreContext.adapt(context);
/*     */ 
/*     */     
/*  67 */     int status = response.getStatusLine().getStatusCode();
/*  68 */     if (status == 400 || status == 408 || status == 411 || status == 413 || status == 414 || status == 503 || status == 501) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  75 */       response.setHeader("Connection", "Close");
/*     */       return;
/*     */     } 
/*  78 */     Header explicit = response.getFirstHeader("Connection");
/*  79 */     if (explicit != null && "Close".equalsIgnoreCase(explicit.getValue())) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  85 */     HttpEntity entity = response.getEntity();
/*  86 */     if (entity != null) {
/*  87 */       ProtocolVersion ver = response.getStatusLine().getProtocolVersion();
/*  88 */       if (entity.getContentLength() < 0L && (!entity.isChunked() || ver.lessEquals((ProtocolVersion)HttpVersion.HTTP_1_0))) {
/*     */         
/*  90 */         response.setHeader("Connection", "Close");
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*  95 */     HttpRequest request = corecontext.getRequest();
/*  96 */     if (request != null) {
/*  97 */       Header header = request.getFirstHeader("Connection");
/*  98 */       if (header != null) {
/*  99 */         response.setHeader("Connection", header.getValue());
/* 100 */       } else if (request.getProtocolVersion().lessEquals((ProtocolVersion)HttpVersion.HTTP_1_0)) {
/* 101 */         response.setHeader("Connection", "Close");
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\protocol\ResponseConnControl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */