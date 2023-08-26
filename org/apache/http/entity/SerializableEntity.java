/*     */ package org.apache.http.entity;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @NotThreadSafe
/*     */ public class SerializableEntity
/*     */   extends AbstractHttpEntity
/*     */ {
/*     */   private byte[] objSer;
/*     */   private Serializable objRef;
/*     */   
/*     */   public SerializableEntity(Serializable ser, boolean bufferize) throws IOException {
/*  66 */     Args.notNull(ser, "Source object");
/*  67 */     if (bufferize) {
/*  68 */       createBytes(ser);
/*     */     } else {
/*  70 */       this.objRef = ser;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SerializableEntity(Serializable ser) {
/*  79 */     Args.notNull(ser, "Source object");
/*  80 */     this.objRef = ser;
/*     */   }
/*     */   
/*     */   private void createBytes(Serializable ser) throws IOException {
/*  84 */     ByteArrayOutputStream baos = new ByteArrayOutputStream();
/*  85 */     ObjectOutputStream out = new ObjectOutputStream(baos);
/*  86 */     out.writeObject(ser);
/*  87 */     out.flush();
/*  88 */     this.objSer = baos.toByteArray();
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream getContent() throws IOException, IllegalStateException {
/*  93 */     if (this.objSer == null) {
/*  94 */       createBytes(this.objRef);
/*     */     }
/*  96 */     return new ByteArrayInputStream(this.objSer);
/*     */   }
/*     */ 
/*     */   
/*     */   public long getContentLength() {
/* 101 */     if (this.objSer == null) {
/* 102 */       return -1L;
/*     */     }
/* 104 */     return this.objSer.length;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRepeatable() {
/* 110 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStreaming() {
/* 115 */     return (this.objSer == null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeTo(OutputStream outstream) throws IOException {
/* 120 */     Args.notNull(outstream, "Output stream");
/* 121 */     if (this.objSer == null) {
/* 122 */       ObjectOutputStream out = new ObjectOutputStream(outstream);
/* 123 */       out.writeObject(this.objRef);
/* 124 */       out.flush();
/*     */     } else {
/* 126 */       outstream.write(this.objSer);
/* 127 */       outstream.flush();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\entity\SerializableEntity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */