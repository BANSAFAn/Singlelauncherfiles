/*     */ package org.apache.http.client.entity;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.InputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.NameValuePair;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.entity.BasicHttpEntity;
/*     */ import org.apache.http.entity.ByteArrayEntity;
/*     */ import org.apache.http.entity.ContentType;
/*     */ import org.apache.http.entity.FileEntity;
/*     */ import org.apache.http.entity.InputStreamEntity;
/*     */ import org.apache.http.entity.SerializableEntity;
/*     */ import org.apache.http.entity.StringEntity;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class EntityBuilder
/*     */ {
/*     */   private String text;
/*     */   private byte[] binary;
/*     */   private InputStream stream;
/*     */   private List<NameValuePair> parameters;
/*     */   private Serializable serializable;
/*     */   private File file;
/*     */   private ContentType contentType;
/*     */   private String contentEncoding;
/*     */   private boolean chunked;
/*     */   private boolean gzipCompress;
/*     */   
/*     */   public static EntityBuilder create() {
/*  85 */     return new EntityBuilder();
/*     */   }
/*     */   
/*     */   private void clearContent() {
/*  89 */     this.text = null;
/*  90 */     this.binary = null;
/*  91 */     this.stream = null;
/*  92 */     this.parameters = null;
/*  93 */     this.serializable = null;
/*  94 */     this.file = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getText() {
/* 101 */     return this.text;
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
/*     */   public EntityBuilder setText(String text) {
/* 114 */     clearContent();
/* 115 */     this.text = text;
/* 116 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getBinary() {
/* 124 */     return this.binary;
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
/*     */   public EntityBuilder setBinary(byte[] binary) {
/* 137 */     clearContent();
/* 138 */     this.binary = binary;
/* 139 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream getStream() {
/* 147 */     return this.stream;
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
/*     */   public EntityBuilder setStream(InputStream stream) {
/* 160 */     clearContent();
/* 161 */     this.stream = stream;
/* 162 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<NameValuePair> getParameters() {
/* 171 */     return this.parameters;
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
/*     */   public EntityBuilder setParameters(List<NameValuePair> parameters) {
/* 183 */     clearContent();
/* 184 */     this.parameters = parameters;
/* 185 */     return this;
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
/*     */   public EntityBuilder setParameters(NameValuePair... parameters) {
/* 197 */     return setParameters(Arrays.asList(parameters));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Serializable getSerializable() {
/* 205 */     return this.serializable;
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
/*     */   public EntityBuilder setSerializable(Serializable serializable) {
/* 218 */     clearContent();
/* 219 */     this.serializable = serializable;
/* 220 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getFile() {
/* 228 */     return this.file;
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
/*     */   public EntityBuilder setFile(File file) {
/* 241 */     clearContent();
/* 242 */     this.file = file;
/* 243 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ContentType getContentType() {
/* 250 */     return this.contentType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EntityBuilder setContentType(ContentType contentType) {
/* 257 */     this.contentType = contentType;
/* 258 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getContentEncoding() {
/* 265 */     return this.contentEncoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EntityBuilder setContentEncoding(String contentEncoding) {
/* 272 */     this.contentEncoding = contentEncoding;
/* 273 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isChunked() {
/* 280 */     return this.chunked;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EntityBuilder chunked() {
/* 287 */     this.chunked = true;
/* 288 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isGzipCompress() {
/* 295 */     return this.gzipCompress;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EntityBuilder gzipCompress() {
/* 302 */     this.gzipCompress = true;
/* 303 */     return this;
/*     */   }
/*     */   
/*     */   private ContentType getContentOrDefault(ContentType def) {
/* 307 */     return (this.contentType != null) ? this.contentType : def;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpEntity build() {
/*     */     BasicHttpEntity basicHttpEntity;
/* 315 */     if (this.text != null) {
/* 316 */       StringEntity stringEntity = new StringEntity(this.text, getContentOrDefault(ContentType.DEFAULT_TEXT));
/* 317 */     } else if (this.binary != null) {
/* 318 */       ByteArrayEntity byteArrayEntity = new ByteArrayEntity(this.binary, getContentOrDefault(ContentType.DEFAULT_BINARY));
/* 319 */     } else if (this.stream != null) {
/* 320 */       InputStreamEntity inputStreamEntity = new InputStreamEntity(this.stream, -1L, getContentOrDefault(ContentType.DEFAULT_BINARY));
/* 321 */     } else if (this.parameters != null) {
/* 322 */       UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(this.parameters, (this.contentType != null) ? this.contentType.getCharset() : null);
/*     */     }
/* 324 */     else if (this.serializable != null) {
/* 325 */       SerializableEntity serializableEntity = new SerializableEntity(this.serializable);
/* 326 */       serializableEntity.setContentType(ContentType.DEFAULT_BINARY.toString());
/* 327 */     } else if (this.file != null) {
/* 328 */       FileEntity fileEntity = new FileEntity(this.file, getContentOrDefault(ContentType.DEFAULT_BINARY));
/*     */     } else {
/* 330 */       basicHttpEntity = new BasicHttpEntity();
/*     */     } 
/* 332 */     if (basicHttpEntity.getContentType() != null && this.contentType != null) {
/* 333 */       basicHttpEntity.setContentType(this.contentType.toString());
/*     */     }
/* 335 */     basicHttpEntity.setContentEncoding(this.contentEncoding);
/* 336 */     basicHttpEntity.setChunked(this.chunked);
/* 337 */     if (this.gzipCompress) {
/* 338 */       return (HttpEntity)new GzipCompressingEntity((HttpEntity)basicHttpEntity);
/*     */     }
/* 340 */     return (HttpEntity)basicHttpEntity;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\client\entity\EntityBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */