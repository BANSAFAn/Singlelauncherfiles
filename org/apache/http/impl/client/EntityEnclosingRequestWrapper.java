/*     */ package org.apache.http.impl.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpEntityEnclosingRequest;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.ProtocolException;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.entity.HttpEntityWrapper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class EntityEnclosingRequestWrapper
/*     */   extends RequestWrapper
/*     */   implements HttpEntityEnclosingRequest
/*     */ {
/*     */   private HttpEntity entity;
/*     */   private boolean consumed;
/*     */   
/*     */   public EntityEnclosingRequestWrapper(HttpEntityEnclosingRequest request) throws ProtocolException {
/*  65 */     super((HttpRequest)request);
/*  66 */     setEntity(request.getEntity());
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpEntity getEntity() {
/*  71 */     return this.entity;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEntity(HttpEntity entity) {
/*  76 */     this.entity = (entity != null) ? (HttpEntity)new EntityWrapper(entity) : null;
/*  77 */     this.consumed = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean expectContinue() {
/*  82 */     Header expect = getFirstHeader("Expect");
/*  83 */     return (expect != null && "100-continue".equalsIgnoreCase(expect.getValue()));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRepeatable() {
/*  88 */     return (this.entity == null || this.entity.isRepeatable() || !this.consumed);
/*     */   }
/*     */   
/*     */   class EntityWrapper
/*     */     extends HttpEntityWrapper {
/*     */     EntityWrapper(HttpEntity entity) {
/*  94 */       super(entity);
/*     */     }
/*     */ 
/*     */     
/*     */     public void consumeContent() throws IOException {
/*  99 */       EntityEnclosingRequestWrapper.this.consumed = true;
/* 100 */       super.consumeContent();
/*     */     }
/*     */ 
/*     */     
/*     */     public InputStream getContent() throws IOException {
/* 105 */       EntityEnclosingRequestWrapper.this.consumed = true;
/* 106 */       return super.getContent();
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeTo(OutputStream outstream) throws IOException {
/* 111 */       EntityEnclosingRequestWrapper.this.consumed = true;
/* 112 */       super.writeTo(outstream);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\client\EntityEnclosingRequestWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */