/*     */ package org.apache.http.impl.entity;
/*     */ 
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpMessage;
/*     */ import org.apache.http.HttpVersion;
/*     */ import org.apache.http.ProtocolException;
/*     */ import org.apache.http.ProtocolVersion;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.entity.ContentLengthStrategy;
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
/*     */ @Immutable
/*     */ public class StrictContentLengthStrategy
/*     */   implements ContentLengthStrategy
/*     */ {
/*  53 */   public static final StrictContentLengthStrategy INSTANCE = new StrictContentLengthStrategy();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int implicitLen;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StrictContentLengthStrategy(int implicitLen) {
/*  67 */     this.implicitLen = implicitLen;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StrictContentLengthStrategy() {
/*  75 */     this(-1);
/*     */   }
/*     */ 
/*     */   
/*     */   public long determineLength(HttpMessage message) throws HttpException {
/*  80 */     Args.notNull(message, "HTTP message");
/*     */ 
/*     */ 
/*     */     
/*  84 */     Header transferEncodingHeader = message.getFirstHeader("Transfer-Encoding");
/*  85 */     if (transferEncodingHeader != null) {
/*  86 */       String s = transferEncodingHeader.getValue();
/*  87 */       if ("chunked".equalsIgnoreCase(s)) {
/*  88 */         if (message.getProtocolVersion().lessEquals((ProtocolVersion)HttpVersion.HTTP_1_0)) {
/*  89 */           throw new ProtocolException("Chunked transfer encoding not allowed for " + message.getProtocolVersion());
/*     */         }
/*     */ 
/*     */         
/*  93 */         return -2L;
/*  94 */       }  if ("identity".equalsIgnoreCase(s)) {
/*  95 */         return -1L;
/*     */       }
/*  97 */       throw new ProtocolException("Unsupported transfer encoding: " + s);
/*     */     } 
/*     */ 
/*     */     
/* 101 */     Header contentLengthHeader = message.getFirstHeader("Content-Length");
/* 102 */     if (contentLengthHeader != null) {
/* 103 */       String s = contentLengthHeader.getValue();
/*     */       try {
/* 105 */         long len = Long.parseLong(s);
/* 106 */         if (len < 0L) {
/* 107 */           throw new ProtocolException("Negative content length: " + s);
/*     */         }
/* 109 */         return len;
/* 110 */       } catch (NumberFormatException e) {
/* 111 */         throw new ProtocolException("Invalid content length: " + s);
/*     */       } 
/*     */     } 
/* 114 */     return this.implicitLen;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\entity\StrictContentLengthStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */