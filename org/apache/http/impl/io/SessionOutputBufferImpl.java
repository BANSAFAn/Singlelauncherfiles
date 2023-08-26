/*     */ package org.apache.http.impl.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.io.BufferInfo;
/*     */ import org.apache.http.io.HttpTransportMetrics;
/*     */ import org.apache.http.io.SessionOutputBuffer;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.Asserts;
/*     */ import org.apache.http.util.ByteArrayBuffer;
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
/*     */ @NotThreadSafe
/*     */ public class SessionOutputBufferImpl
/*     */   implements SessionOutputBuffer, BufferInfo
/*     */ {
/*  60 */   private static final byte[] CRLF = new byte[] { 13, 10 };
/*     */ 
/*     */ 
/*     */   
/*     */   private final HttpTransportMetricsImpl metrics;
/*     */ 
/*     */ 
/*     */   
/*     */   private final ByteArrayBuffer buffer;
/*     */ 
/*     */ 
/*     */   
/*     */   private final int fragementSizeHint;
/*     */ 
/*     */ 
/*     */   
/*     */   private final CharsetEncoder encoder;
/*     */ 
/*     */   
/*     */   private OutputStream outstream;
/*     */ 
/*     */   
/*     */   private ByteBuffer bbuf;
/*     */ 
/*     */ 
/*     */   
/*     */   public SessionOutputBufferImpl(HttpTransportMetricsImpl metrics, int buffersize, int fragementSizeHint, CharsetEncoder charencoder) {
/*  87 */     Args.positive(buffersize, "Buffer size");
/*  88 */     Args.notNull(metrics, "HTTP transport metrcis");
/*  89 */     this.metrics = metrics;
/*  90 */     this.buffer = new ByteArrayBuffer(buffersize);
/*  91 */     this.fragementSizeHint = (fragementSizeHint >= 0) ? fragementSizeHint : 0;
/*  92 */     this.encoder = charencoder;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SessionOutputBufferImpl(HttpTransportMetricsImpl metrics, int buffersize) {
/*  98 */     this(metrics, buffersize, buffersize, null);
/*     */   }
/*     */   
/*     */   public void bind(OutputStream outstream) {
/* 102 */     this.outstream = outstream;
/*     */   }
/*     */   
/*     */   public boolean isBound() {
/* 106 */     return (this.outstream != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public int capacity() {
/* 111 */     return this.buffer.capacity();
/*     */   }
/*     */ 
/*     */   
/*     */   public int length() {
/* 116 */     return this.buffer.length();
/*     */   }
/*     */ 
/*     */   
/*     */   public int available() {
/* 121 */     return capacity() - length();
/*     */   }
/*     */   
/*     */   private void streamWrite(byte[] b, int off, int len) throws IOException {
/* 125 */     Asserts.notNull(this.outstream, "Output stream");
/* 126 */     this.outstream.write(b, off, len);
/*     */   }
/*     */   
/*     */   private void flushStream() throws IOException {
/* 130 */     if (this.outstream != null) {
/* 131 */       this.outstream.flush();
/*     */     }
/*     */   }
/*     */   
/*     */   private void flushBuffer() throws IOException {
/* 136 */     int len = this.buffer.length();
/* 137 */     if (len > 0) {
/* 138 */       streamWrite(this.buffer.buffer(), 0, len);
/* 139 */       this.buffer.clear();
/* 140 */       this.metrics.incrementBytesTransferred(len);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/* 146 */     flushBuffer();
/* 147 */     flushStream();
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(byte[] b, int off, int len) throws IOException {
/* 152 */     if (b == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 158 */     if (len > this.fragementSizeHint || len > this.buffer.capacity()) {
/*     */       
/* 160 */       flushBuffer();
/*     */       
/* 162 */       streamWrite(b, off, len);
/* 163 */       this.metrics.incrementBytesTransferred(len);
/*     */     } else {
/*     */       
/* 166 */       int freecapacity = this.buffer.capacity() - this.buffer.length();
/* 167 */       if (len > freecapacity)
/*     */       {
/* 169 */         flushBuffer();
/*     */       }
/*     */       
/* 172 */       this.buffer.append(b, off, len);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(byte[] b) throws IOException {
/* 178 */     if (b == null) {
/*     */       return;
/*     */     }
/* 181 */     write(b, 0, b.length);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(int b) throws IOException {
/* 186 */     if (this.fragementSizeHint > 0) {
/* 187 */       if (this.buffer.isFull()) {
/* 188 */         flushBuffer();
/*     */       }
/* 190 */       this.buffer.append(b);
/*     */     } else {
/* 192 */       flushBuffer();
/* 193 */       this.outstream.write(b);
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
/*     */   public void writeLine(String s) throws IOException {
/* 208 */     if (s == null) {
/*     */       return;
/*     */     }
/* 211 */     if (s.length() > 0) {
/* 212 */       if (this.encoder == null) {
/* 213 */         for (int i = 0; i < s.length(); i++) {
/* 214 */           write(s.charAt(i));
/*     */         }
/*     */       } else {
/* 217 */         CharBuffer cbuf = CharBuffer.wrap(s);
/* 218 */         writeEncoded(cbuf);
/*     */       } 
/*     */     }
/* 221 */     write(CRLF);
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
/*     */   public void writeLine(CharArrayBuffer charbuffer) throws IOException {
/* 235 */     if (charbuffer == null) {
/*     */       return;
/*     */     }
/* 238 */     if (this.encoder == null) {
/* 239 */       int off = 0;
/* 240 */       int remaining = charbuffer.length();
/* 241 */       while (remaining > 0) {
/* 242 */         int chunk = this.buffer.capacity() - this.buffer.length();
/* 243 */         chunk = Math.min(chunk, remaining);
/* 244 */         if (chunk > 0) {
/* 245 */           this.buffer.append(charbuffer, off, chunk);
/*     */         }
/* 247 */         if (this.buffer.isFull()) {
/* 248 */           flushBuffer();
/*     */         }
/* 250 */         off += chunk;
/* 251 */         remaining -= chunk;
/*     */       } 
/*     */     } else {
/* 254 */       CharBuffer cbuf = CharBuffer.wrap(charbuffer.buffer(), 0, charbuffer.length());
/* 255 */       writeEncoded(cbuf);
/*     */     } 
/* 257 */     write(CRLF);
/*     */   }
/*     */   
/*     */   private void writeEncoded(CharBuffer cbuf) throws IOException {
/* 261 */     if (!cbuf.hasRemaining()) {
/*     */       return;
/*     */     }
/* 264 */     if (this.bbuf == null) {
/* 265 */       this.bbuf = ByteBuffer.allocate(1024);
/*     */     }
/* 267 */     this.encoder.reset();
/* 268 */     while (cbuf.hasRemaining()) {
/* 269 */       CoderResult coderResult = this.encoder.encode(cbuf, this.bbuf, true);
/* 270 */       handleEncodingResult(coderResult);
/*     */     } 
/* 272 */     CoderResult result = this.encoder.flush(this.bbuf);
/* 273 */     handleEncodingResult(result);
/* 274 */     this.bbuf.clear();
/*     */   }
/*     */   
/*     */   private void handleEncodingResult(CoderResult result) throws IOException {
/* 278 */     if (result.isError()) {
/* 279 */       result.throwException();
/*     */     }
/* 281 */     this.bbuf.flip();
/* 282 */     while (this.bbuf.hasRemaining()) {
/* 283 */       write(this.bbuf.get());
/*     */     }
/* 285 */     this.bbuf.compact();
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpTransportMetrics getMetrics() {
/* 290 */     return this.metrics;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\io\SessionOutputBufferImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */