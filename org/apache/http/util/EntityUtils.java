/*     */ package org.apache.http.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.UnsupportedCharsetException;
/*     */ import org.apache.http.HeaderElement;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.NameValuePair;
/*     */ import org.apache.http.ParseException;
/*     */ import org.apache.http.entity.ContentType;
/*     */ import org.apache.http.protocol.HTTP;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class EntityUtils
/*     */ {
/*     */   public static void consumeQuietly(HttpEntity entity) {
/*     */     try {
/*  67 */       consume(entity);
/*  68 */     } catch (IOException ignore) {}
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
/*     */   public static void consume(HttpEntity entity) throws IOException {
/*  82 */     if (entity == null) {
/*     */       return;
/*     */     }
/*  85 */     if (entity.isStreaming()) {
/*  86 */       InputStream instream = entity.getContent();
/*  87 */       if (instream != null) {
/*  88 */         instream.close();
/*     */       }
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
/*     */   public static void updateEntity(HttpResponse response, HttpEntity entity) throws IOException {
/* 106 */     Args.notNull(response, "Response");
/* 107 */     consume(response.getEntity());
/* 108 */     response.setEntity(entity);
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
/*     */   public static byte[] toByteArray(HttpEntity entity) throws IOException {
/* 121 */     Args.notNull(entity, "Entity");
/* 122 */     InputStream instream = entity.getContent();
/* 123 */     if (instream == null) {
/* 124 */       return null;
/*     */     }
/*     */     try {
/* 127 */       Args.check((entity.getContentLength() <= 2147483647L), "HTTP entity too large to be buffered in memory");
/*     */       
/* 129 */       int i = (int)entity.getContentLength();
/* 130 */       if (i < 0) {
/* 131 */         i = 4096;
/*     */       }
/* 133 */       ByteArrayBuffer buffer = new ByteArrayBuffer(i);
/* 134 */       byte[] tmp = new byte[4096];
/*     */       int l;
/* 136 */       while ((l = instream.read(tmp)) != -1) {
/* 137 */         buffer.append(tmp, 0, l);
/*     */       }
/* 139 */       return buffer.toByteArray();
/*     */     } finally {
/* 141 */       instream.close();
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
/*     */   @Deprecated
/*     */   public static String getContentCharSet(HttpEntity entity) throws ParseException {
/* 157 */     Args.notNull(entity, "Entity");
/* 158 */     String charset = null;
/* 159 */     if (entity.getContentType() != null) {
/* 160 */       HeaderElement[] values = entity.getContentType().getElements();
/* 161 */       if (values.length > 0) {
/* 162 */         NameValuePair param = values[0].getParameterByName("charset");
/* 163 */         if (param != null) {
/* 164 */           charset = param.getValue();
/*     */         }
/*     */       } 
/*     */     } 
/* 168 */     return charset;
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
/*     */   @Deprecated
/*     */   public static String getContentMimeType(HttpEntity entity) throws ParseException {
/* 185 */     Args.notNull(entity, "Entity");
/* 186 */     String mimeType = null;
/* 187 */     if (entity.getContentType() != null) {
/* 188 */       HeaderElement[] values = entity.getContentType().getElements();
/* 189 */       if (values.length > 0) {
/* 190 */         mimeType = values[0].getName();
/*     */       }
/*     */     } 
/* 193 */     return mimeType;
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
/*     */   public static String toString(HttpEntity entity, Charset defaultCharset) throws IOException, ParseException {
/* 214 */     Args.notNull(entity, "Entity");
/* 215 */     InputStream instream = entity.getContent();
/* 216 */     if (instream == null) {
/* 217 */       return null;
/*     */     }
/*     */     try {
/* 220 */       Args.check((entity.getContentLength() <= 2147483647L), "HTTP entity too large to be buffered in memory");
/*     */       
/* 222 */       int i = (int)entity.getContentLength();
/* 223 */       if (i < 0) {
/* 224 */         i = 4096;
/*     */       }
/* 226 */       Charset charset = null;
/*     */       try {
/* 228 */         ContentType contentType = ContentType.get(entity);
/* 229 */         if (contentType != null) {
/* 230 */           charset = contentType.getCharset();
/*     */         }
/* 232 */       } catch (UnsupportedCharsetException ex) {
/* 233 */         if (defaultCharset == null) {
/* 234 */           throw new UnsupportedEncodingException(ex.getMessage());
/*     */         }
/*     */       } 
/* 237 */       if (charset == null) {
/* 238 */         charset = defaultCharset;
/*     */       }
/* 240 */       if (charset == null) {
/* 241 */         charset = HTTP.DEF_CONTENT_CHARSET;
/*     */       }
/* 243 */       Reader reader = new InputStreamReader(instream, charset);
/* 244 */       CharArrayBuffer buffer = new CharArrayBuffer(i);
/* 245 */       char[] tmp = new char[1024];
/*     */       int l;
/* 247 */       while ((l = reader.read(tmp)) != -1) {
/* 248 */         buffer.append(tmp, 0, l);
/*     */       }
/* 250 */       return buffer.toString();
/*     */     } finally {
/* 252 */       instream.close();
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
/*     */   public static String toString(HttpEntity entity, String defaultCharset) throws IOException, ParseException {
/* 273 */     return toString(entity, (defaultCharset != null) ? Charset.forName(defaultCharset) : null);
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
/*     */   public static String toString(HttpEntity entity) throws IOException, ParseException {
/* 291 */     return toString(entity, (Charset)null);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\htt\\util\EntityUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */