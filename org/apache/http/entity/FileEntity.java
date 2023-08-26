/*     */ package org.apache.http.entity;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
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
/*     */ @NotThreadSafe
/*     */ public class FileEntity
/*     */   extends AbstractHttpEntity
/*     */   implements Cloneable
/*     */ {
/*     */   protected final File file;
/*     */   
/*     */   @Deprecated
/*     */   public FileEntity(File file, String contentType) {
/*  55 */     this.file = (File)Args.notNull(file, "File");
/*  56 */     setContentType(contentType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileEntity(File file, ContentType contentType) {
/*  64 */     this.file = (File)Args.notNull(file, "File");
/*  65 */     if (contentType != null) {
/*  66 */       setContentType(contentType.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileEntity(File file) {
/*  75 */     this.file = (File)Args.notNull(file, "File");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRepeatable() {
/*  80 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getContentLength() {
/*  85 */     return this.file.length();
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream getContent() throws IOException {
/*  90 */     return new FileInputStream(this.file);
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeTo(OutputStream outstream) throws IOException {
/*  95 */     Args.notNull(outstream, "Output stream");
/*  96 */     InputStream instream = new FileInputStream(this.file);
/*     */     try {
/*  98 */       byte[] tmp = new byte[4096];
/*     */       int l;
/* 100 */       while ((l = instream.read(tmp)) != -1) {
/* 101 */         outstream.write(tmp, 0, l);
/*     */       }
/* 103 */       outstream.flush();
/*     */     } finally {
/* 105 */       instream.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStreaming() {
/* 116 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException {
/* 123 */     return super.clone();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\entity\FileEntity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */