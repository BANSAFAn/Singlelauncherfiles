/*     */ package org.apache.http.entity;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.message.BasicHeader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class AbstractHttpEntity
/*     */   implements HttpEntity
/*     */ {
/*     */   protected static final int OUTPUT_BUFFER_SIZE = 4096;
/*     */   protected Header contentType;
/*     */   protected Header contentEncoding;
/*     */   protected boolean chunked;
/*     */   
/*     */   public Header getContentType() {
/*  78 */     return this.contentType;
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
/*     */   public Header getContentEncoding() {
/*  91 */     return this.contentEncoding;
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
/*     */   public boolean isChunked() {
/* 103 */     return this.chunked;
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
/*     */   public void setContentType(Header contentType) {
/* 116 */     this.contentType = contentType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setContentType(String ctString) {
/*     */     BasicHeader basicHeader;
/* 128 */     Header h = null;
/* 129 */     if (ctString != null) {
/* 130 */       basicHeader = new BasicHeader("Content-Type", ctString);
/*     */     }
/* 132 */     setContentType((Header)basicHeader);
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
/*     */   public void setContentEncoding(Header contentEncoding) {
/* 145 */     this.contentEncoding = contentEncoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setContentEncoding(String ceString) {
/*     */     BasicHeader basicHeader;
/* 157 */     Header h = null;
/* 158 */     if (ceString != null) {
/* 159 */       basicHeader = new BasicHeader("Content-Encoding", ceString);
/*     */     }
/* 161 */     setContentEncoding((Header)basicHeader);
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
/*     */   public void setChunked(boolean b) {
/* 180 */     this.chunked = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void consumeContent() throws IOException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 197 */     StringBuilder sb = new StringBuilder();
/* 198 */     sb.append('[');
/* 199 */     if (this.contentType != null) {
/* 200 */       sb.append("Content-Type: ");
/* 201 */       sb.append(this.contentType.getValue());
/* 202 */       sb.append(',');
/*     */     } 
/* 204 */     if (this.contentEncoding != null) {
/* 205 */       sb.append("Content-Encoding: ");
/* 206 */       sb.append(this.contentEncoding.getValue());
/* 207 */       sb.append(',');
/*     */     } 
/* 209 */     long len = getContentLength();
/* 210 */     if (len >= 0L) {
/* 211 */       sb.append("Content-Length: ");
/* 212 */       sb.append(len);
/* 213 */       sb.append(',');
/*     */     } 
/* 215 */     sb.append("Chunked: ");
/* 216 */     sb.append(this.chunked);
/* 217 */     sb.append(']');
/* 218 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\entity\AbstractHttpEntity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */