/*     */ package org.apache.http.impl.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ import java.nio.charset.CodingErrorAction;
/*     */ import org.apache.http.Consts;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.io.BufferInfo;
/*     */ import org.apache.http.io.HttpTransportMetrics;
/*     */ import org.apache.http.io.SessionOutputBuffer;
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
/*     */ @Deprecated
/*     */ @NotThreadSafe
/*     */ public abstract class AbstractSessionOutputBuffer
/*     */   implements SessionOutputBuffer, BufferInfo
/*     */ {
/*  68 */   private static final byte[] CRLF = new byte[] { 13, 10 };
/*     */   
/*     */   private OutputStream outstream;
/*     */   
/*     */   private ByteArrayBuffer buffer;
/*     */   
/*     */   private Charset charset;
/*     */   
/*     */   private boolean ascii;
/*     */   
/*     */   private int minChunkLimit;
/*     */   
/*     */   private HttpTransportMetricsImpl metrics;
/*     */   
/*     */   private CodingErrorAction onMalformedCharAction;
/*     */   
/*     */   private CodingErrorAction onUnmappableCharAction;
/*     */   
/*     */   private CharsetEncoder encoder;
/*     */   private ByteBuffer bbuf;
/*     */   
/*     */   protected AbstractSessionOutputBuffer(OutputStream outstream, int buffersize, Charset charset, int minChunkLimit, CodingErrorAction malformedCharAction, CodingErrorAction unmappableCharAction) {
/*  90 */     Args.notNull(outstream, "Input stream");
/*  91 */     Args.notNegative(buffersize, "Buffer size");
/*  92 */     this.outstream = outstream;
/*  93 */     this.buffer = new ByteArrayBuffer(buffersize);
/*  94 */     this.charset = (charset != null) ? charset : Consts.ASCII;
/*  95 */     this.ascii = this.charset.equals(Consts.ASCII);
/*  96 */     this.encoder = null;
/*  97 */     this.minChunkLimit = (minChunkLimit >= 0) ? minChunkLimit : 512;
/*  98 */     this.metrics = createTransportMetrics();
/*  99 */     this.onMalformedCharAction = (malformedCharAction != null) ? malformedCharAction : CodingErrorAction.REPORT;
/*     */     
/* 101 */     this.onUnmappableCharAction = (unmappableCharAction != null) ? unmappableCharAction : CodingErrorAction.REPORT;
/*     */   }
/*     */ 
/*     */   
/*     */   public AbstractSessionOutputBuffer() {}
/*     */ 
/*     */   
/*     */   protected void init(OutputStream outstream, int buffersize, HttpParams params) {
/* 109 */     Args.notNull(outstream, "Input stream");
/* 110 */     Args.notNegative(buffersize, "Buffer size");
/* 111 */     Args.notNull(params, "HTTP parameters");
/* 112 */     this.outstream = outstream;
/* 113 */     this.buffer = new ByteArrayBuffer(buffersize);
/* 114 */     String charset = (String)params.getParameter("http.protocol.element-charset");
/* 115 */     this.charset = (charset != null) ? Charset.forName(charset) : Consts.ASCII;
/* 116 */     this.ascii = this.charset.equals(Consts.ASCII);
/* 117 */     this.encoder = null;
/* 118 */     this.minChunkLimit = params.getIntParameter("http.connection.min-chunk-limit", 512);
/* 119 */     this.metrics = createTransportMetrics();
/* 120 */     CodingErrorAction a1 = (CodingErrorAction)params.getParameter("http.malformed.input.action");
/*     */     
/* 122 */     this.onMalformedCharAction = (a1 != null) ? a1 : CodingErrorAction.REPORT;
/* 123 */     CodingErrorAction a2 = (CodingErrorAction)params.getParameter("http.unmappable.input.action");
/*     */     
/* 125 */     this.onUnmappableCharAction = (a2 != null) ? a2 : CodingErrorAction.REPORT;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected HttpTransportMetricsImpl createTransportMetrics() {
/* 132 */     return new HttpTransportMetricsImpl();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int capacity() {
/* 139 */     return this.buffer.capacity();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/* 146 */     return this.buffer.length();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int available() {
/* 153 */     return capacity() - length();
/*     */   }
/*     */   
/*     */   protected void flushBuffer() throws IOException {
/* 157 */     int len = this.buffer.length();
/* 158 */     if (len > 0) {
/* 159 */       this.outstream.write(this.buffer.buffer(), 0, len);
/* 160 */       this.buffer.clear();
/* 161 */       this.metrics.incrementBytesTransferred(len);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void flush() throws IOException {
/* 166 */     flushBuffer();
/* 167 */     this.outstream.flush();
/*     */   }
/*     */   
/*     */   public void write(byte[] b, int off, int len) throws IOException {
/* 171 */     if (b == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 177 */     if (len > this.minChunkLimit || len > this.buffer.capacity()) {
/*     */       
/* 179 */       flushBuffer();
/*     */       
/* 181 */       this.outstream.write(b, off, len);
/* 182 */       this.metrics.incrementBytesTransferred(len);
/*     */     } else {
/*     */       
/* 185 */       int freecapacity = this.buffer.capacity() - this.buffer.length();
/* 186 */       if (len > freecapacity)
/*     */       {
/* 188 */         flushBuffer();
/*     */       }
/*     */       
/* 191 */       this.buffer.append(b, off, len);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void write(byte[] b) throws IOException {
/* 196 */     if (b == null) {
/*     */       return;
/*     */     }
/* 199 */     write(b, 0, b.length);
/*     */   }
/*     */   
/*     */   public void write(int b) throws IOException {
/* 203 */     if (this.buffer.isFull()) {
/* 204 */       flushBuffer();
/*     */     }
/* 206 */     this.buffer.append(b);
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
/*     */   public void writeLine(String s) throws IOException {
/* 219 */     if (s == null) {
/*     */       return;
/*     */     }
/* 222 */     if (s.length() > 0) {
/* 223 */       if (this.ascii) {
/* 224 */         for (int i = 0; i < s.length(); i++) {
/* 225 */           write(s.charAt(i));
/*     */         }
/*     */       } else {
/* 228 */         CharBuffer cbuf = CharBuffer.wrap(s);
/* 229 */         writeEncoded(cbuf);
/*     */       } 
/*     */     }
/* 232 */     write(CRLF);
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
/*     */   public void writeLine(CharArrayBuffer charbuffer) throws IOException {
/* 245 */     if (charbuffer == null) {
/*     */       return;
/*     */     }
/* 248 */     if (this.ascii) {
/* 249 */       int off = 0;
/* 250 */       int remaining = charbuffer.length();
/* 251 */       while (remaining > 0) {
/* 252 */         int chunk = this.buffer.capacity() - this.buffer.length();
/* 253 */         chunk = Math.min(chunk, remaining);
/* 254 */         if (chunk > 0) {
/* 255 */           this.buffer.append(charbuffer, off, chunk);
/*     */         }
/* 257 */         if (this.buffer.isFull()) {
/* 258 */           flushBuffer();
/*     */         }
/* 260 */         off += chunk;
/* 261 */         remaining -= chunk;
/*     */       } 
/*     */     } else {
/* 264 */       CharBuffer cbuf = CharBuffer.wrap(charbuffer.buffer(), 0, charbuffer.length());
/* 265 */       writeEncoded(cbuf);
/*     */     } 
/* 267 */     write(CRLF);
/*     */   }
/*     */   
/*     */   private void writeEncoded(CharBuffer cbuf) throws IOException {
/* 271 */     if (!cbuf.hasRemaining()) {
/*     */       return;
/*     */     }
/* 274 */     if (this.encoder == null) {
/* 275 */       this.encoder = this.charset.newEncoder();
/* 276 */       this.encoder.onMalformedInput(this.onMalformedCharAction);
/* 277 */       this.encoder.onUnmappableCharacter(this.onUnmappableCharAction);
/*     */     } 
/* 279 */     if (this.bbuf == null) {
/* 280 */       this.bbuf = ByteBuffer.allocate(1024);
/*     */     }
/* 282 */     this.encoder.reset();
/* 283 */     while (cbuf.hasRemaining()) {
/* 284 */       CoderResult coderResult = this.encoder.encode(cbuf, this.bbuf, true);
/* 285 */       handleEncodingResult(coderResult);
/*     */     } 
/* 287 */     CoderResult result = this.encoder.flush(this.bbuf);
/* 288 */     handleEncodingResult(result);
/* 289 */     this.bbuf.clear();
/*     */   }
/*     */   
/*     */   private void handleEncodingResult(CoderResult result) throws IOException {
/* 293 */     if (result.isError()) {
/* 294 */       result.throwException();
/*     */     }
/* 296 */     this.bbuf.flip();
/* 297 */     while (this.bbuf.hasRemaining()) {
/* 298 */       write(this.bbuf.get());
/*     */     }
/* 300 */     this.bbuf.compact();
/*     */   }
/*     */   
/*     */   public HttpTransportMetrics getMetrics() {
/* 304 */     return this.metrics;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\io\AbstractSessionOutputBuffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */