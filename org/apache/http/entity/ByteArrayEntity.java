/*     */ package org.apache.http.entity;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import org.apache.http.annotation.NotThreadSafe;
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
/*     */ @NotThreadSafe
/*     */ public class ByteArrayEntity
/*     */   extends AbstractHttpEntity
/*     */   implements Cloneable
/*     */ {
/*     */   @Deprecated
/*     */   protected final byte[] content;
/*     */   private final byte[] b;
/*     */   private final int off;
/*     */   private final int len;
/*     */   
/*     */   public ByteArrayEntity(byte[] b, ContentType contentType) {
/*  60 */     Args.notNull(b, "Source byte array");
/*  61 */     this.content = b;
/*  62 */     this.b = b;
/*  63 */     this.off = 0;
/*  64 */     this.len = this.b.length;
/*  65 */     if (contentType != null) {
/*  66 */       setContentType(contentType.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteArrayEntity(byte[] b, int off, int len, ContentType contentType) {
/*  76 */     Args.notNull(b, "Source byte array");
/*  77 */     if (off < 0 || off > b.length || len < 0 || off + len < 0 || off + len > b.length)
/*     */     {
/*  79 */       throw new IndexOutOfBoundsException("off: " + off + " len: " + len + " b.length: " + b.length);
/*     */     }
/*  81 */     this.content = b;
/*  82 */     this.b = b;
/*  83 */     this.off = off;
/*  84 */     this.len = len;
/*  85 */     if (contentType != null) {
/*  86 */       setContentType(contentType.toString());
/*     */     }
/*     */   }
/*     */   
/*     */   public ByteArrayEntity(byte[] b) {
/*  91 */     this(b, (ContentType)null);
/*     */   }
/*     */   
/*     */   public ByteArrayEntity(byte[] b, int off, int len) {
/*  95 */     this(b, off, len, (ContentType)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRepeatable() {
/* 100 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getContentLength() {
/* 105 */     return this.len;
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream getContent() {
/* 110 */     return new ByteArrayInputStream(this.b, this.off, this.len);
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeTo(OutputStream outstream) throws IOException {
/* 115 */     Args.notNull(outstream, "Output stream");
/* 116 */     outstream.write(this.b, this.off, this.len);
/* 117 */     outstream.flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStreaming() {
/* 128 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException {
/* 133 */     return super.clone();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\entity\ByteArrayEntity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */