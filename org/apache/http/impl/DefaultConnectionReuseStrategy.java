/*     */ package org.apache.http.impl;
/*     */ 
/*     */ import org.apache.http.ConnectionReuseStrategy;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HeaderIterator;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.HttpVersion;
/*     */ import org.apache.http.ParseException;
/*     */ import org.apache.http.ProtocolVersion;
/*     */ import org.apache.http.TokenIterator;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.message.BasicHeaderIterator;
/*     */ import org.apache.http.message.BasicTokenIterator;
/*     */ import org.apache.http.protocol.HttpContext;
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
/*     */ @Immutable
/*     */ public class DefaultConnectionReuseStrategy
/*     */   implements ConnectionReuseStrategy
/*     */ {
/*  67 */   public static final DefaultConnectionReuseStrategy INSTANCE = new DefaultConnectionReuseStrategy();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean keepAlive(HttpResponse response, HttpContext context) {
/*  77 */     Args.notNull(response, "HTTP response");
/*  78 */     Args.notNull(context, "HTTP context");
/*     */ 
/*     */ 
/*     */     
/*  82 */     ProtocolVersion ver = response.getStatusLine().getProtocolVersion();
/*  83 */     Header teh = response.getFirstHeader("Transfer-Encoding");
/*  84 */     if (teh != null) {
/*  85 */       if (!"chunked".equalsIgnoreCase(teh.getValue())) {
/*  86 */         return false;
/*     */       }
/*     */     }
/*  89 */     else if (canResponseHaveBody(response)) {
/*  90 */       Header[] clhs = response.getHeaders("Content-Length");
/*     */       
/*  92 */       if (clhs.length == 1) {
/*  93 */         Header clh = clhs[0];
/*     */         try {
/*  95 */           int contentLen = Integer.parseInt(clh.getValue());
/*  96 */           if (contentLen < 0) {
/*  97 */             return false;
/*     */           }
/*  99 */         } catch (NumberFormatException ex) {
/* 100 */           return false;
/*     */         } 
/*     */       } else {
/* 103 */         return false;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 111 */     Header[] connHeaders = response.getHeaders("Connection");
/* 112 */     if (connHeaders.length == 0) {
/* 113 */       connHeaders = response.getHeaders("Proxy-Connection");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 139 */     if (connHeaders.length != 0) {
/*     */       try {
/* 141 */         BasicTokenIterator basicTokenIterator = new BasicTokenIterator((HeaderIterator)new BasicHeaderIterator(connHeaders, null));
/* 142 */         boolean keepalive = false;
/* 143 */         while (basicTokenIterator.hasNext()) {
/* 144 */           String token = basicTokenIterator.nextToken();
/* 145 */           if ("Close".equalsIgnoreCase(token))
/* 146 */             return false; 
/* 147 */           if ("Keep-Alive".equalsIgnoreCase(token))
/*     */           {
/* 149 */             keepalive = true;
/*     */           }
/*     */         } 
/* 152 */         if (keepalive) {
/* 153 */           return true;
/*     */         
/*     */         }
/*     */       }
/* 157 */       catch (ParseException px) {
/*     */ 
/*     */         
/* 160 */         return false;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 165 */     return !ver.lessEquals((ProtocolVersion)HttpVersion.HTTP_1_0);
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
/*     */   protected TokenIterator createTokenIterator(HeaderIterator hit) {
/* 179 */     return (TokenIterator)new BasicTokenIterator(hit);
/*     */   }
/*     */   
/*     */   private boolean canResponseHaveBody(HttpResponse response) {
/* 183 */     int status = response.getStatusLine().getStatusCode();
/* 184 */     return (status >= 200 && status != 204 && status != 304 && status != 205);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\DefaultConnectionReuseStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */