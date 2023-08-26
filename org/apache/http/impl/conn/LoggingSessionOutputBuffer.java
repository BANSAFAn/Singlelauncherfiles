/*     */ package org.apache.http.impl.conn;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.http.Consts;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.io.HttpTransportMetrics;
/*     */ import org.apache.http.io.SessionOutputBuffer;
/*     */ import org.apache.http.util.CharArrayBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ @Immutable
/*     */ public class LoggingSessionOutputBuffer
/*     */   implements SessionOutputBuffer
/*     */ {
/*     */   private final SessionOutputBuffer out;
/*     */   private final Wire wire;
/*     */   private final String charset;
/*     */   
/*     */   public LoggingSessionOutputBuffer(SessionOutputBuffer out, Wire wire, String charset) {
/*  63 */     this.out = out;
/*  64 */     this.wire = wire;
/*  65 */     this.charset = (charset != null) ? charset : Consts.ASCII.name();
/*     */   }
/*     */   
/*     */   public LoggingSessionOutputBuffer(SessionOutputBuffer out, Wire wire) {
/*  69 */     this(out, wire, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(byte[] b, int off, int len) throws IOException {
/*  74 */     this.out.write(b, off, len);
/*  75 */     if (this.wire.enabled()) {
/*  76 */       this.wire.output(b, off, len);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(int b) throws IOException {
/*  82 */     this.out.write(b);
/*  83 */     if (this.wire.enabled()) {
/*  84 */       this.wire.output(b);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(byte[] b) throws IOException {
/*  90 */     this.out.write(b);
/*  91 */     if (this.wire.enabled()) {
/*  92 */       this.wire.output(b);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/*  98 */     this.out.flush();
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeLine(CharArrayBuffer buffer) throws IOException {
/* 103 */     this.out.writeLine(buffer);
/* 104 */     if (this.wire.enabled()) {
/* 105 */       String s = new String(buffer.buffer(), 0, buffer.length());
/* 106 */       String tmp = s + "\r\n";
/* 107 */       this.wire.output(tmp.getBytes(this.charset));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeLine(String s) throws IOException {
/* 113 */     this.out.writeLine(s);
/* 114 */     if (this.wire.enabled()) {
/* 115 */       String tmp = s + "\r\n";
/* 116 */       this.wire.output(tmp.getBytes(this.charset));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpTransportMetrics getMetrics() {
/* 122 */     return this.out.getMetrics();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\conn\LoggingSessionOutputBuffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */