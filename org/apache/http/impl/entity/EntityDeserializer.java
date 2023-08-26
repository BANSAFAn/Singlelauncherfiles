/*     */ package org.apache.http.impl.entity;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpMessage;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.entity.BasicHttpEntity;
/*     */ import org.apache.http.entity.ContentLengthStrategy;
/*     */ import org.apache.http.impl.io.ChunkedInputStream;
/*     */ import org.apache.http.impl.io.ContentLengthInputStream;
/*     */ import org.apache.http.impl.io.IdentityInputStream;
/*     */ import org.apache.http.io.SessionInputBuffer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ @Immutable
/*     */ public class EntityDeserializer
/*     */ {
/*     */   private final ContentLengthStrategy lenStrategy;
/*     */   
/*     */   public EntityDeserializer(ContentLengthStrategy lenStrategy) {
/*  72 */     this.lenStrategy = (ContentLengthStrategy)Args.notNull(lenStrategy, "Content length strategy");
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
/*     */   protected BasicHttpEntity doDeserialize(SessionInputBuffer inbuffer, HttpMessage message) throws HttpException, IOException {
/*  93 */     BasicHttpEntity entity = new BasicHttpEntity();
/*     */     
/*  95 */     long len = this.lenStrategy.determineLength(message);
/*  96 */     if (len == -2L) {
/*  97 */       entity.setChunked(true);
/*  98 */       entity.setContentLength(-1L);
/*  99 */       entity.setContent((InputStream)new ChunkedInputStream(inbuffer));
/* 100 */     } else if (len == -1L) {
/* 101 */       entity.setChunked(false);
/* 102 */       entity.setContentLength(-1L);
/* 103 */       entity.setContent((InputStream)new IdentityInputStream(inbuffer));
/*     */     } else {
/* 105 */       entity.setChunked(false);
/* 106 */       entity.setContentLength(len);
/* 107 */       entity.setContent((InputStream)new ContentLengthInputStream(inbuffer, len));
/*     */     } 
/*     */     
/* 110 */     Header contentTypeHeader = message.getFirstHeader("Content-Type");
/* 111 */     if (contentTypeHeader != null) {
/* 112 */       entity.setContentType(contentTypeHeader);
/*     */     }
/* 114 */     Header contentEncodingHeader = message.getFirstHeader("Content-Encoding");
/* 115 */     if (contentEncodingHeader != null) {
/* 116 */       entity.setContentEncoding(contentEncodingHeader);
/*     */     }
/* 118 */     return entity;
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
/*     */   public HttpEntity deserialize(SessionInputBuffer inbuffer, HttpMessage message) throws HttpException, IOException {
/* 138 */     Args.notNull(inbuffer, "Session input buffer");
/* 139 */     Args.notNull(message, "HTTP message");
/* 140 */     return (HttpEntity)doDeserialize(inbuffer, message);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\entity\EntityDeserializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */