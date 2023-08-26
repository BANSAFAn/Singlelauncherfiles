/*     */ package org.apache.http.client.entity;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.entity.HttpEntityWrapper;
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
/*     */ 
/*     */ public class DecompressingEntity
/*     */   extends HttpEntityWrapper
/*     */ {
/*     */   private static final int BUFFER_SIZE = 2048;
/*     */   private final InputStreamFactory inputStreamFactory;
/*     */   private InputStream content;
/*     */   
/*     */   public DecompressingEntity(HttpEntity wrapped, InputStreamFactory inputStreamFactory) {
/*  66 */     super(wrapped);
/*  67 */     this.inputStreamFactory = inputStreamFactory;
/*     */   }
/*     */   
/*     */   private InputStream getDecompressingStream() throws IOException {
/*  71 */     InputStream in = this.wrappedEntity.getContent();
/*  72 */     return new LazyDecompressingInputStream(in, this.inputStreamFactory);
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream getContent() throws IOException {
/*  77 */     if (this.wrappedEntity.isStreaming()) {
/*  78 */       if (this.content == null) {
/*  79 */         this.content = getDecompressingStream();
/*     */       }
/*  81 */       return this.content;
/*     */     } 
/*  83 */     return getDecompressingStream();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeTo(OutputStream outstream) throws IOException {
/*  89 */     Args.notNull(outstream, "Output stream");
/*  90 */     InputStream instream = getContent();
/*     */     try {
/*  92 */       byte[] buffer = new byte[2048];
/*     */       int l;
/*  94 */       while ((l = instream.read(buffer)) != -1) {
/*  95 */         outstream.write(buffer, 0, l);
/*     */       }
/*     */     } finally {
/*  98 */       instream.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Header getContentEncoding() {
/* 105 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long getContentLength() {
/* 111 */     return -1L;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\client\entity\DecompressingEntity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */