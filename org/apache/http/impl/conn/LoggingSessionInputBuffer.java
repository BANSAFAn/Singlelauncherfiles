/*     */ package org.apache.http.impl.conn;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.http.Consts;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.io.EofSensor;
/*     */ import org.apache.http.io.HttpTransportMetrics;
/*     */ import org.apache.http.io.SessionInputBuffer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ @Immutable
/*     */ public class LoggingSessionInputBuffer
/*     */   implements SessionInputBuffer, EofSensor
/*     */ {
/*     */   private final SessionInputBuffer in;
/*     */   private final EofSensor eofSensor;
/*     */   private final Wire wire;
/*     */   private final String charset;
/*     */   
/*     */   public LoggingSessionInputBuffer(SessionInputBuffer in, Wire wire, String charset) {
/*  68 */     this.in = in;
/*  69 */     this.eofSensor = (in instanceof EofSensor) ? (EofSensor)in : null;
/*  70 */     this.wire = wire;
/*  71 */     this.charset = (charset != null) ? charset : Consts.ASCII.name();
/*     */   }
/*     */   
/*     */   public LoggingSessionInputBuffer(SessionInputBuffer in, Wire wire) {
/*  75 */     this(in, wire, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDataAvailable(int timeout) throws IOException {
/*  80 */     return this.in.isDataAvailable(timeout);
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] b, int off, int len) throws IOException {
/*  85 */     int l = this.in.read(b, off, len);
/*  86 */     if (this.wire.enabled() && l > 0) {
/*  87 */       this.wire.input(b, off, l);
/*     */     }
/*  89 */     return l;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/*  94 */     int l = this.in.read();
/*  95 */     if (this.wire.enabled() && l != -1) {
/*  96 */       this.wire.input(l);
/*     */     }
/*  98 */     return l;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] b) throws IOException {
/* 103 */     int l = this.in.read(b);
/* 104 */     if (this.wire.enabled() && l > 0) {
/* 105 */       this.wire.input(b, 0, l);
/*     */     }
/* 107 */     return l;
/*     */   }
/*     */ 
/*     */   
/*     */   public String readLine() throws IOException {
/* 112 */     String s = this.in.readLine();
/* 113 */     if (this.wire.enabled() && s != null) {
/* 114 */       String tmp = s + "\r\n";
/* 115 */       this.wire.input(tmp.getBytes(this.charset));
/*     */     } 
/* 117 */     return s;
/*     */   }
/*     */ 
/*     */   
/*     */   public int readLine(CharArrayBuffer buffer) throws IOException {
/* 122 */     int l = this.in.readLine(buffer);
/* 123 */     if (this.wire.enabled() && l >= 0) {
/* 124 */       int pos = buffer.length() - l;
/* 125 */       String s = new String(buffer.buffer(), pos, l);
/* 126 */       String tmp = s + "\r\n";
/* 127 */       this.wire.input(tmp.getBytes(this.charset));
/*     */     } 
/* 129 */     return l;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpTransportMetrics getMetrics() {
/* 134 */     return this.in.getMetrics();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEof() {
/* 139 */     if (this.eofSensor != null) {
/* 140 */       return this.eofSensor.isEof();
/*     */     }
/* 142 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\conn\LoggingSessionInputBuffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */