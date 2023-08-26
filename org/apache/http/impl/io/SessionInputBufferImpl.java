/*     */ package org.apache.http.impl.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ import org.apache.http.MessageConstraintException;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.config.MessageConstraints;
/*     */ import org.apache.http.io.BufferInfo;
/*     */ import org.apache.http.io.HttpTransportMetrics;
/*     */ import org.apache.http.io.SessionInputBuffer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class SessionInputBufferImpl
/*     */   implements SessionInputBuffer, BufferInfo
/*     */ {
/*     */   private final HttpTransportMetricsImpl metrics;
/*     */   private final byte[] buffer;
/*     */   private final ByteArrayBuffer linebuffer;
/*     */   private final int minChunkLimit;
/*     */   private final MessageConstraints constraints;
/*     */   private final CharsetDecoder decoder;
/*     */   private InputStream instream;
/*     */   private int bufferpos;
/*     */   private int bufferlen;
/*     */   private CharBuffer cbuf;
/*     */   
/*     */   public SessionInputBufferImpl(HttpTransportMetricsImpl metrics, int buffersize, int minChunkLimit, MessageConstraints constraints, CharsetDecoder chardecoder) {
/*  96 */     Args.notNull(metrics, "HTTP transport metrcis");
/*  97 */     Args.positive(buffersize, "Buffer size");
/*  98 */     this.metrics = metrics;
/*  99 */     this.buffer = new byte[buffersize];
/* 100 */     this.bufferpos = 0;
/* 101 */     this.bufferlen = 0;
/* 102 */     this.minChunkLimit = (minChunkLimit >= 0) ? minChunkLimit : 512;
/* 103 */     this.constraints = (constraints != null) ? constraints : MessageConstraints.DEFAULT;
/* 104 */     this.linebuffer = new ByteArrayBuffer(buffersize);
/* 105 */     this.decoder = chardecoder;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SessionInputBufferImpl(HttpTransportMetricsImpl metrics, int buffersize) {
/* 111 */     this(metrics, buffersize, buffersize, null, null);
/*     */   }
/*     */   
/*     */   public void bind(InputStream instream) {
/* 115 */     this.instream = instream;
/*     */   }
/*     */   
/*     */   public boolean isBound() {
/* 119 */     return (this.instream != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public int capacity() {
/* 124 */     return this.buffer.length;
/*     */   }
/*     */ 
/*     */   
/*     */   public int length() {
/* 129 */     return this.bufferlen - this.bufferpos;
/*     */   }
/*     */ 
/*     */   
/*     */   public int available() {
/* 134 */     return capacity() - length();
/*     */   }
/*     */   
/*     */   private int streamRead(byte[] b, int off, int len) throws IOException {
/* 138 */     Asserts.notNull(this.instream, "Input stream");
/* 139 */     return this.instream.read(b, off, len);
/*     */   }
/*     */ 
/*     */   
/*     */   public int fillBuffer() throws IOException {
/* 144 */     if (this.bufferpos > 0) {
/* 145 */       int i = this.bufferlen - this.bufferpos;
/* 146 */       if (i > 0) {
/* 147 */         System.arraycopy(this.buffer, this.bufferpos, this.buffer, 0, i);
/*     */       }
/* 149 */       this.bufferpos = 0;
/* 150 */       this.bufferlen = i;
/*     */     } 
/*     */     
/* 153 */     int off = this.bufferlen;
/* 154 */     int len = this.buffer.length - off;
/* 155 */     int l = streamRead(this.buffer, off, len);
/* 156 */     if (l == -1) {
/* 157 */       return -1;
/*     */     }
/* 159 */     this.bufferlen = off + l;
/* 160 */     this.metrics.incrementBytesTransferred(l);
/* 161 */     return l;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasBufferedData() {
/* 166 */     return (this.bufferpos < this.bufferlen);
/*     */   }
/*     */   
/*     */   public void clear() {
/* 170 */     this.bufferpos = 0;
/* 171 */     this.bufferlen = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/* 177 */     while (!hasBufferedData()) {
/* 178 */       int noRead = fillBuffer();
/* 179 */       if (noRead == -1) {
/* 180 */         return -1;
/*     */       }
/*     */     } 
/* 183 */     return this.buffer[this.bufferpos++] & 0xFF;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] b, int off, int len) throws IOException {
/* 188 */     if (b == null) {
/* 189 */       return 0;
/*     */     }
/* 191 */     if (hasBufferedData()) {
/* 192 */       int i = Math.min(len, this.bufferlen - this.bufferpos);
/* 193 */       System.arraycopy(this.buffer, this.bufferpos, b, off, i);
/* 194 */       this.bufferpos += i;
/* 195 */       return i;
/*     */     } 
/*     */ 
/*     */     
/* 199 */     if (len > this.minChunkLimit) {
/* 200 */       int read = streamRead(b, off, len);
/* 201 */       if (read > 0) {
/* 202 */         this.metrics.incrementBytesTransferred(read);
/*     */       }
/* 204 */       return read;
/*     */     } 
/*     */     
/* 207 */     while (!hasBufferedData()) {
/* 208 */       int noRead = fillBuffer();
/* 209 */       if (noRead == -1) {
/* 210 */         return -1;
/*     */       }
/*     */     } 
/* 213 */     int chunk = Math.min(len, this.bufferlen - this.bufferpos);
/* 214 */     System.arraycopy(this.buffer, this.bufferpos, b, off, chunk);
/* 215 */     this.bufferpos += chunk;
/* 216 */     return chunk;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int read(byte[] b) throws IOException {
/* 222 */     if (b == null) {
/* 223 */       return 0;
/*     */     }
/* 225 */     return read(b, 0, b.length);
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
/*     */   public int readLine(CharArrayBuffer charbuffer) throws IOException {
/* 245 */     Args.notNull(charbuffer, "Char array buffer");
/* 246 */     int maxLineLen = this.constraints.getMaxLineLength();
/* 247 */     int noRead = 0;
/* 248 */     boolean retry = true;
/* 249 */     while (retry) {
/*     */       
/* 251 */       int pos = -1;
/* 252 */       for (int i = this.bufferpos; i < this.bufferlen; i++) {
/* 253 */         if (this.buffer[i] == 10) {
/* 254 */           pos = i;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/* 259 */       if (maxLineLen > 0) {
/* 260 */         int currentLen = this.linebuffer.length() + ((pos > 0) ? pos : this.bufferlen) - this.bufferpos;
/*     */         
/* 262 */         if (currentLen >= maxLineLen) {
/* 263 */           throw new MessageConstraintException("Maximum line length limit exceeded");
/*     */         }
/*     */       } 
/*     */       
/* 267 */       if (pos != -1) {
/*     */         
/* 269 */         if (this.linebuffer.isEmpty())
/*     */         {
/* 271 */           return lineFromReadBuffer(charbuffer, pos);
/*     */         }
/* 273 */         retry = false;
/* 274 */         int len = pos + 1 - this.bufferpos;
/* 275 */         this.linebuffer.append(this.buffer, this.bufferpos, len);
/* 276 */         this.bufferpos = pos + 1;
/*     */         continue;
/*     */       } 
/* 279 */       if (hasBufferedData()) {
/* 280 */         int len = this.bufferlen - this.bufferpos;
/* 281 */         this.linebuffer.append(this.buffer, this.bufferpos, len);
/* 282 */         this.bufferpos = this.bufferlen;
/*     */       } 
/* 284 */       noRead = fillBuffer();
/* 285 */       if (noRead == -1) {
/* 286 */         retry = false;
/*     */       }
/*     */     } 
/*     */     
/* 290 */     if (noRead == -1 && this.linebuffer.isEmpty())
/*     */     {
/* 292 */       return -1;
/*     */     }
/* 294 */     return lineFromLineBuffer(charbuffer);
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
/*     */   private int lineFromLineBuffer(CharArrayBuffer charbuffer) throws IOException {
/* 313 */     int len = this.linebuffer.length();
/* 314 */     if (len > 0) {
/* 315 */       if (this.linebuffer.byteAt(len - 1) == 10) {
/* 316 */         len--;
/*     */       }
/*     */       
/* 319 */       if (len > 0 && 
/* 320 */         this.linebuffer.byteAt(len - 1) == 13) {
/* 321 */         len--;
/*     */       }
/*     */     } 
/*     */     
/* 325 */     if (this.decoder == null) {
/* 326 */       charbuffer.append(this.linebuffer, 0, len);
/*     */     } else {
/* 328 */       ByteBuffer bbuf = ByteBuffer.wrap(this.linebuffer.buffer(), 0, len);
/* 329 */       len = appendDecoded(charbuffer, bbuf);
/*     */     } 
/* 331 */     this.linebuffer.clear();
/* 332 */     return len;
/*     */   }
/*     */ 
/*     */   
/*     */   private int lineFromReadBuffer(CharArrayBuffer charbuffer, int position) throws IOException {
/* 337 */     int pos = position;
/* 338 */     int off = this.bufferpos;
/*     */     
/* 340 */     this.bufferpos = pos + 1;
/* 341 */     if (pos > off && this.buffer[pos - 1] == 13)
/*     */     {
/* 343 */       pos--;
/*     */     }
/* 345 */     int len = pos - off;
/* 346 */     if (this.decoder == null) {
/* 347 */       charbuffer.append(this.buffer, off, len);
/*     */     } else {
/* 349 */       ByteBuffer bbuf = ByteBuffer.wrap(this.buffer, off, len);
/* 350 */       len = appendDecoded(charbuffer, bbuf);
/*     */     } 
/* 352 */     return len;
/*     */   }
/*     */ 
/*     */   
/*     */   private int appendDecoded(CharArrayBuffer charbuffer, ByteBuffer bbuf) throws IOException {
/* 357 */     if (!bbuf.hasRemaining()) {
/* 358 */       return 0;
/*     */     }
/* 360 */     if (this.cbuf == null) {
/* 361 */       this.cbuf = CharBuffer.allocate(1024);
/*     */     }
/* 363 */     this.decoder.reset();
/* 364 */     int len = 0;
/* 365 */     while (bbuf.hasRemaining()) {
/* 366 */       CoderResult coderResult = this.decoder.decode(bbuf, this.cbuf, true);
/* 367 */       len += handleDecodingResult(coderResult, charbuffer, bbuf);
/*     */     } 
/* 369 */     CoderResult result = this.decoder.flush(this.cbuf);
/* 370 */     len += handleDecodingResult(result, charbuffer, bbuf);
/* 371 */     this.cbuf.clear();
/* 372 */     return len;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int handleDecodingResult(CoderResult result, CharArrayBuffer charbuffer, ByteBuffer bbuf) throws IOException {
/* 379 */     if (result.isError()) {
/* 380 */       result.throwException();
/*     */     }
/* 382 */     this.cbuf.flip();
/* 383 */     int len = this.cbuf.remaining();
/* 384 */     while (this.cbuf.hasRemaining()) {
/* 385 */       charbuffer.append(this.cbuf.get());
/*     */     }
/* 387 */     this.cbuf.compact();
/* 388 */     return len;
/*     */   }
/*     */ 
/*     */   
/*     */   public String readLine() throws IOException {
/* 393 */     CharArrayBuffer charbuffer = new CharArrayBuffer(64);
/* 394 */     int l = readLine(charbuffer);
/* 395 */     if (l != -1) {
/* 396 */       return charbuffer.toString();
/*     */     }
/* 398 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDataAvailable(int timeout) throws IOException {
/* 404 */     return hasBufferedData();
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpTransportMetrics getMetrics() {
/* 409 */     return this.metrics;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\io\SessionInputBufferImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */