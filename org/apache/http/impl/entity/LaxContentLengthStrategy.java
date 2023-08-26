/*     */ package org.apache.http.impl.entity;
/*     */ 
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HeaderElement;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpMessage;
/*     */ import org.apache.http.ParseException;
/*     */ import org.apache.http.ProtocolException;
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
/*     */ public class LaxContentLengthStrategy
/*     */   implements ContentLengthStrategy
/*     */ {
/*  53 */   public static final LaxContentLengthStrategy INSTANCE = new LaxContentLengthStrategy();
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
/*     */   public LaxContentLengthStrategy(int implicitLen) {
/*  67 */     this.implicitLen = implicitLen;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LaxContentLengthStrategy() {
/*  75 */     this(-1);
/*     */   }
/*     */ 
/*     */   
/*     */   public long determineLength(HttpMessage message) throws HttpException {
/*  80 */     Args.notNull(message, "HTTP message");
/*     */     
/*  82 */     Header transferEncodingHeader = message.getFirstHeader("Transfer-Encoding");
/*     */ 
/*     */     
/*  85 */     if (transferEncodingHeader != null) {
/*     */       HeaderElement[] encodings;
/*     */       try {
/*  88 */         encodings = transferEncodingHeader.getElements();
/*  89 */       } catch (ParseException px) {
/*  90 */         throw new ProtocolException("Invalid Transfer-Encoding header value: " + transferEncodingHeader, px);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/*  95 */       int len = encodings.length;
/*  96 */       if ("identity".equalsIgnoreCase(transferEncodingHeader.getValue()))
/*  97 */         return -1L; 
/*  98 */       if (len > 0 && "chunked".equalsIgnoreCase(encodings[len - 1].getName()))
/*     */       {
/* 100 */         return -2L;
/*     */       }
/* 102 */       return -1L;
/*     */     } 
/*     */     
/* 105 */     Header contentLengthHeader = message.getFirstHeader("Content-Length");
/* 106 */     if (contentLengthHeader != null) {
/* 107 */       long contentlen = -1L;
/* 108 */       Header[] headers = message.getHeaders("Content-Length");
/* 109 */       for (int i = headers.length - 1; i >= 0; i--) {
/* 110 */         Header header = headers[i];
/*     */         try {
/* 112 */           contentlen = Long.parseLong(header.getValue());
/*     */           break;
/* 114 */         } catch (NumberFormatException ignore) {}
/*     */       } 
/*     */ 
/*     */       
/* 118 */       if (contentlen >= 0L) {
/* 119 */         return contentlen;
/*     */       }
/* 121 */       return -1L;
/*     */     } 
/*     */     
/* 124 */     return this.implicitLen;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\entity\LaxContentLengthStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */