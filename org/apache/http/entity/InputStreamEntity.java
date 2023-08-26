/*     */ package org.apache.http.entity;
/*     */ 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ @NotThreadSafe
/*     */ public class InputStreamEntity
/*     */   extends AbstractHttpEntity
/*     */ {
/*     */   private final InputStream content;
/*     */   private final long length;
/*     */   
/*     */   public InputStreamEntity(InputStream instream) {
/*  58 */     this(instream, -1L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStreamEntity(InputStream instream, long length) {
/*  69 */     this(instream, length, null);
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
/*     */   public InputStreamEntity(InputStream instream, ContentType contentType) {
/*  82 */     this(instream, -1L, contentType);
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
/*     */   public InputStreamEntity(InputStream instream, long length, ContentType contentType) {
/*  94 */     this.content = (InputStream)Args.notNull(instream, "Source input stream");
/*  95 */     this.length = length;
/*  96 */     if (contentType != null) {
/*  97 */       setContentType(contentType.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRepeatable() {
/* 103 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getContentLength() {
/* 111 */     return this.length;
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream getContent() throws IOException {
/* 116 */     return this.content;
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
/*     */   public void writeTo(OutputStream outstream) throws IOException {
/* 128 */     Args.notNull(outstream, "Output stream");
/* 129 */     InputStream instream = this.content;
/*     */     try {
/* 131 */       byte[] buffer = new byte[4096];
/*     */       
/* 133 */       if (this.length < 0L) {
/*     */         int l;
/* 135 */         while ((l = instream.read(buffer)) != -1) {
/* 136 */           outstream.write(buffer, 0, l);
/*     */         }
/*     */       } else {
/*     */         
/* 140 */         long remaining = this.length;
/* 141 */         while (remaining > 0L) {
/* 142 */           int l = instream.read(buffer, 0, (int)Math.min(4096L, remaining));
/* 143 */           if (l == -1) {
/*     */             break;
/*     */           }
/* 146 */           outstream.write(buffer, 0, l);
/* 147 */           remaining -= l;
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 151 */       instream.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStreaming() {
/* 157 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\entity\InputStreamEntity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */