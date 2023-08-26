/*     */ package org.apache.http.entity;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.UnsupportedCharsetException;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.protocol.HTTP;
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
/*     */ @NotThreadSafe
/*     */ public class StringEntity
/*     */   extends AbstractHttpEntity
/*     */   implements Cloneable
/*     */ {
/*     */   protected final byte[] content;
/*     */   
/*     */   public StringEntity(String string, ContentType contentType) throws UnsupportedCharsetException {
/*  67 */     Args.notNull(string, "Source string");
/*  68 */     Charset charset = (contentType != null) ? contentType.getCharset() : null;
/*  69 */     if (charset == null) {
/*  70 */       charset = HTTP.DEF_CONTENT_CHARSET;
/*     */     }
/*  72 */     this.content = string.getBytes(charset);
/*  73 */     if (contentType != null) {
/*  74 */       setContentType(contentType.toString());
/*     */     }
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
/*     */   @Deprecated
/*     */   public StringEntity(String string, String mimeType, String charset) throws UnsupportedEncodingException {
/*  97 */     Args.notNull(string, "Source string");
/*  98 */     String mt = (mimeType != null) ? mimeType : "text/plain";
/*  99 */     String cs = (charset != null) ? charset : "ISO-8859-1";
/* 100 */     this.content = string.getBytes(cs);
/* 101 */     setContentType(mt + "; charset=" + cs);
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
/*     */   public StringEntity(String string, String charset) throws UnsupportedCharsetException {
/* 118 */     this(string, ContentType.create(ContentType.TEXT_PLAIN.getMimeType(), charset));
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
/*     */   public StringEntity(String string, Charset charset) {
/* 134 */     this(string, ContentType.create(ContentType.TEXT_PLAIN.getMimeType(), charset));
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
/*     */   public StringEntity(String string) throws UnsupportedEncodingException {
/* 148 */     this(string, ContentType.DEFAULT_TEXT);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRepeatable() {
/* 153 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getContentLength() {
/* 158 */     return this.content.length;
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream getContent() throws IOException {
/* 163 */     return new ByteArrayInputStream(this.content);
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeTo(OutputStream outstream) throws IOException {
/* 168 */     Args.notNull(outstream, "Output stream");
/* 169 */     outstream.write(this.content);
/* 170 */     outstream.flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStreaming() {
/* 180 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException {
/* 185 */     return super.clone();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\entity\StringEntity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */