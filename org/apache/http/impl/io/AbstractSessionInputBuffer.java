/*     */ package org.apache.http.impl.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ import java.nio.charset.CodingErrorAction;
/*     */ import org.apache.http.Consts;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.io.BufferInfo;
/*     */ import org.apache.http.io.HttpTransportMetrics;
/*     */ import org.apache.http.io.SessionInputBuffer;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.util.Args;
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
/*     */ @Deprecated
/*     */ @NotThreadSafe
/*     */ public abstract class AbstractSessionInputBuffer
/*     */   implements SessionInputBuffer, BufferInfo
/*     */ {
/*     */   private InputStream instream;
/*     */   private byte[] buffer;
/*     */   private ByteArrayBuffer linebuffer;
/*     */   private Charset charset;
/*     */   private boolean ascii;
/*     */   private int maxLineLen;
/*     */   private int minChunkLimit;
/*     */   private HttpTransportMetricsImpl metrics;
/*     */   private CodingErrorAction onMalformedCharAction;
/*     */   private CodingErrorAction onUnmappableCharAction;
/*     */   private int bufferpos;
/*     */   private int bufferlen;
/*     */   private CharsetDecoder decoder;
/*     */   private CharBuffer cbuf;
/*     */   
/*     */   protected void init(InputStream instream, int buffersize, HttpParams params) {
/*  96 */     Args.notNull(instream, "Input stream");
/*  97 */     Args.notNegative(buffersize, "Buffer size");
/*  98 */     Args.notNull(params, "HTTP parameters");
/*  99 */     this.instream = instream;
/* 100 */     this.buffer = new byte[buffersize];
/* 101 */     this.bufferpos = 0;
/* 102 */     this.bufferlen = 0;
/* 103 */     this.linebuffer = new ByteArrayBuffer(buffersize);
/* 104 */     String charset = (String)params.getParameter("http.protocol.element-charset");
/* 105 */     this.charset = (charset != null) ? Charset.forName(charset) : Consts.ASCII;
/* 106 */     this.ascii = this.charset.equals(Consts.ASCII);
/* 107 */     this.decoder = null;
/* 108 */     this.maxLineLen = params.getIntParameter("http.connection.max-line-length", -1);
/* 109 */     this.minChunkLimit = params.getIntParameter("http.connection.min-chunk-limit", 512);
/* 110 */     this.metrics = createTransportMetrics();
/* 111 */     CodingErrorAction a1 = (CodingErrorAction)params.getParameter("http.malformed.input.action");
/*     */     
/* 113 */     this.onMalformedCharAction = (a1 != null) ? a1 : CodingErrorAction.REPORT;
/* 114 */     CodingErrorAction a2 = (CodingErrorAction)params.getParameter("http.unmappable.input.action");
/*     */     
/* 116 */     this.onUnmappableCharAction = (a2 != null) ? a2 : CodingErrorAction.REPORT;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected HttpTransportMetricsImpl createTransportMetrics() {
/* 123 */     return new HttpTransportMetricsImpl();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int capacity() {
/* 130 */     return this.buffer.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/* 137 */     return this.bufferlen - this.bufferpos;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int available() {
/* 144 */     return capacity() - length();
/*     */   }
/*     */ 
/*     */   
/*     */   protected int fillBuffer() throws IOException {
/* 149 */     if (this.bufferpos > 0) {
/* 150 */       int i = this.bufferlen - this.bufferpos;
/* 151 */       if (i > 0) {
/* 152 */         System.arraycopy(this.buffer, this.bufferpos, this.buffer, 0, i);
/*     */       }
/* 154 */       this.bufferpos = 0;
/* 155 */       this.bufferlen = i;
/*     */     } 
/*     */     
/* 158 */     int off = this.bufferlen;
/* 159 */     int len = this.buffer.length - off;
/* 160 */     int l = this.instream.read(this.buffer, off, len);
/* 161 */     if (l == -1) {
/* 162 */       return -1;
/*     */     }
/* 164 */     this.bufferlen = off + l;
/* 165 */     this.metrics.incrementBytesTransferred(l);
/* 166 */     return l;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean hasBufferedData() {
/* 171 */     return (this.bufferpos < this.bufferlen);
/*     */   }
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/* 176 */     while (!hasBufferedData()) {
/* 177 */       int noRead = fillBuffer();
/* 178 */       if (noRead == -1) {
/* 179 */         return -1;
/*     */       }
/*     */     } 
/* 182 */     return this.buffer[this.bufferpos++] & 0xFF;
/*     */   }
/*     */   
/*     */   public int read(byte[] b, int off, int len) throws IOException {
/* 186 */     if (b == null) {
/* 187 */       return 0;
/*     */     }
/* 189 */     if (hasBufferedData()) {
/* 190 */       int i = Math.min(len, this.bufferlen - this.bufferpos);
/* 191 */       System.arraycopy(this.buffer, this.bufferpos, b, off, i);
/* 192 */       this.bufferpos += i;
/* 193 */       return i;
/*     */     } 
/*     */ 
/*     */     
/* 197 */     if (len > this.minChunkLimit) {
/* 198 */       int read = this.instream.read(b, off, len);
/* 199 */       if (read > 0) {
/* 200 */         this.metrics.incrementBytesTransferred(read);
/*     */       }
/* 202 */       return read;
/*     */     } 
/*     */     
/* 205 */     while (!hasBufferedData()) {
/* 206 */       int noRead = fillBuffer();
/* 207 */       if (noRead == -1) {
/* 208 */         return -1;
/*     */       }
/*     */     } 
/* 211 */     int chunk = Math.min(len, this.bufferlen - this.bufferpos);
/* 212 */     System.arraycopy(this.buffer, this.bufferpos, b, off, chunk);
/* 213 */     this.bufferpos += chunk;
/* 214 */     return chunk;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] b) throws IOException {
/* 219 */     if (b == null) {
/* 220 */       return 0;
/*     */     }
/* 222 */     return read(b, 0, b.length);
/*     */   }
/*     */   
/*     */   private int locateLF() {
/* 226 */     for (int i = this.bufferpos; i < this.bufferlen; i++) {
/* 227 */       if (this.buffer[i] == 10) {
/* 228 */         return i;
/*     */       }
/*     */     } 
/* 231 */     return -1;
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
/*     */   public int readLine(CharArrayBuffer charbuffer) throws IOException {
/* 250 */     Args.notNull(charbuffer, "Char array buffer");
/* 251 */     int noRead = 0;
/* 252 */     boolean retry = true;
/* 253 */     while (retry) {
/*     */       
/* 255 */       int i = locateLF();
/* 256 */       if (i != -1) {
/*     */         
/* 258 */         if (this.linebuffer.isEmpty())
/*     */         {
/* 260 */           return lineFromReadBuffer(charbuffer, i);
/*     */         }
/* 262 */         retry = false;
/* 263 */         int len = i + 1 - this.bufferpos;
/* 264 */         this.linebuffer.append(this.buffer, this.bufferpos, len);
/* 265 */         this.bufferpos = i + 1;
/*     */       } else {
/*     */         
/* 268 */         if (hasBufferedData()) {
/* 269 */           int len = this.bufferlen - this.bufferpos;
/* 270 */           this.linebuffer.append(this.buffer, this.bufferpos, len);
/* 271 */           this.bufferpos = this.bufferlen;
/*     */         } 
/* 273 */         noRead = fillBuffer();
/* 274 */         if (noRead == -1) {
/* 275 */           retry = false;
/*     */         }
/*     */       } 
/* 278 */       if (this.maxLineLen > 0 && this.linebuffer.length() >= this.maxLineLen) {
/* 279 */         throw new IOException("Maximum line length limit exceeded");
/*     */       }
/*     */     } 
/* 282 */     if (noRead == -1 && this.linebuffer.isEmpty())
/*     */     {
/* 284 */       return -1;
/*     */     }
/* 286 */     return lineFromLineBuffer(charbuffer);
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
/* 305 */     int len = this.linebuffer.length();
/* 306 */     if (len > 0) {
/* 307 */       if (this.linebuffer.byteAt(len - 1) == 10) {
/* 308 */         len--;
/*     */       }
/*     */       
/* 311 */       if (len > 0 && 
/* 312 */         this.linebuffer.byteAt(len - 1) == 13) {
/* 313 */         len--;
/*     */       }
/*     */     } 
/*     */     
/* 317 */     if (this.ascii) {
/* 318 */       charbuffer.append(this.linebuffer, 0, len);
/*     */     } else {
/* 320 */       ByteBuffer bbuf = ByteBuffer.wrap(this.linebuffer.buffer(), 0, len);
/* 321 */       len = appendDecoded(charbuffer, bbuf);
/*     */     } 
/* 323 */     this.linebuffer.clear();
/* 324 */     return len;
/*     */   }
/*     */ 
/*     */   
/*     */   private int lineFromReadBuffer(CharArrayBuffer charbuffer, int position) throws IOException {
/* 329 */     int off = this.bufferpos;
/* 330 */     int i = position;
/* 331 */     this.bufferpos = i + 1;
/* 332 */     if (i > off && this.buffer[i - 1] == 13)
/*     */     {
/* 334 */       i--;
/*     */     }
/* 336 */     int len = i - off;
/* 337 */     if (this.ascii) {
/* 338 */       charbuffer.append(this.buffer, off, len);
/*     */     } else {
/* 340 */       ByteBuffer bbuf = ByteBuffer.wrap(this.buffer, off, len);
/* 341 */       len = appendDecoded(charbuffer, bbuf);
/*     */     } 
/* 343 */     return len;
/*     */   }
/*     */ 
/*     */   
/*     */   private int appendDecoded(CharArrayBuffer charbuffer, ByteBuffer bbuf) throws IOException {
/* 348 */     if (!bbuf.hasRemaining()) {
/* 349 */       return 0;
/*     */     }
/* 351 */     if (this.decoder == null) {
/* 352 */       this.decoder = this.charset.newDecoder();
/* 353 */       this.decoder.onMalformedInput(this.onMalformedCharAction);
/* 354 */       this.decoder.onUnmappableCharacter(this.onUnmappableCharAction);
/*     */     } 
/* 356 */     if (this.cbuf == null) {
/* 357 */       this.cbuf = CharBuffer.allocate(1024);
/*     */     }
/* 359 */     this.decoder.reset();
/* 360 */     int len = 0;
/* 361 */     while (bbuf.hasRemaining()) {
/* 362 */       CoderResult coderResult = this.decoder.decode(bbuf, this.cbuf, true);
/* 363 */       len += handleDecodingResult(coderResult, charbuffer, bbuf);
/*     */     } 
/* 365 */     CoderResult result = this.decoder.flush(this.cbuf);
/* 366 */     len += handleDecodingResult(result, charbuffer, bbuf);
/* 367 */     this.cbuf.clear();
/* 368 */     return len;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int handleDecodingResult(CoderResult result, CharArrayBuffer charbuffer, ByteBuffer bbuf) throws IOException {
/* 375 */     if (result.isError()) {
/* 376 */       result.throwException();
/*     */     }
/* 378 */     this.cbuf.flip();
/* 379 */     int len = this.cbuf.remaining();
/* 380 */     while (this.cbuf.hasRemaining()) {
/* 381 */       charbuffer.append(this.cbuf.get());
/*     */     }
/* 383 */     this.cbuf.compact();
/* 384 */     return len;
/*     */   }
/*     */   
/*     */   public String readLine() throws IOException {
/* 388 */     CharArrayBuffer charbuffer = new CharArrayBuffer(64);
/* 389 */     int l = readLine(charbuffer);
/* 390 */     if (l != -1) {
/* 391 */       return charbuffer.toString();
/*     */     }
/* 393 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpTransportMetrics getMetrics() {
/* 398 */     return this.metrics;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\io\AbstractSessionInputBuffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */