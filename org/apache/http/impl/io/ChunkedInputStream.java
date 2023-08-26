/*     */ package org.apache.http.impl.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import org.apache.http.ConnectionClosedException;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.MalformedChunkCodingException;
/*     */ import org.apache.http.TruncatedChunkException;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.config.MessageConstraints;
/*     */ import org.apache.http.io.BufferInfo;
/*     */ import org.apache.http.io.SessionInputBuffer;
/*     */ import org.apache.http.util.Args;
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
/*     */ @NotThreadSafe
/*     */ public class ChunkedInputStream
/*     */   extends InputStream
/*     */ {
/*     */   private static final int CHUNK_LEN = 1;
/*     */   private static final int CHUNK_DATA = 2;
/*     */   private static final int CHUNK_CRLF = 3;
/*     */   private static final int CHUNK_INVALID = 2147483647;
/*     */   private static final int BUFFER_SIZE = 2048;
/*     */   private final SessionInputBuffer in;
/*     */   private final CharArrayBuffer buffer;
/*     */   private final MessageConstraints constraints;
/*     */   private int state;
/*     */   private int chunkSize;
/*     */   private int pos;
/*     */   private boolean eof = false;
/*     */   private boolean closed = false;
/*  90 */   private Header[] footers = new Header[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChunkedInputStream(SessionInputBuffer in, MessageConstraints constraints) {
/* 103 */     this.in = (SessionInputBuffer)Args.notNull(in, "Session input buffer");
/* 104 */     this.pos = 0;
/* 105 */     this.buffer = new CharArrayBuffer(16);
/* 106 */     this.constraints = (constraints != null) ? constraints : MessageConstraints.DEFAULT;
/* 107 */     this.state = 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChunkedInputStream(SessionInputBuffer in) {
/* 116 */     this(in, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public int available() throws IOException {
/* 121 */     if (this.in instanceof BufferInfo) {
/* 122 */       int len = ((BufferInfo)this.in).length();
/* 123 */       return Math.min(len, this.chunkSize - this.pos);
/*     */     } 
/* 125 */     return 0;
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
/*     */   public int read() throws IOException {
/* 143 */     if (this.closed) {
/* 144 */       throw new IOException("Attempted read from closed stream.");
/*     */     }
/* 146 */     if (this.eof) {
/* 147 */       return -1;
/*     */     }
/* 149 */     if (this.state != 2) {
/* 150 */       nextChunk();
/* 151 */       if (this.eof) {
/* 152 */         return -1;
/*     */       }
/*     */     } 
/* 155 */     int b = this.in.read();
/* 156 */     if (b != -1) {
/* 157 */       this.pos++;
/* 158 */       if (this.pos >= this.chunkSize) {
/* 159 */         this.state = 3;
/*     */       }
/*     */     } 
/* 162 */     return b;
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
/*     */   public int read(byte[] b, int off, int len) throws IOException {
/* 178 */     if (this.closed) {
/* 179 */       throw new IOException("Attempted read from closed stream.");
/*     */     }
/*     */     
/* 182 */     if (this.eof) {
/* 183 */       return -1;
/*     */     }
/* 185 */     if (this.state != 2) {
/* 186 */       nextChunk();
/* 187 */       if (this.eof) {
/* 188 */         return -1;
/*     */       }
/*     */     } 
/* 191 */     int bytesRead = this.in.read(b, off, Math.min(len, this.chunkSize - this.pos));
/* 192 */     if (bytesRead != -1) {
/* 193 */       this.pos += bytesRead;
/* 194 */       if (this.pos >= this.chunkSize) {
/* 195 */         this.state = 3;
/*     */       }
/* 197 */       return bytesRead;
/*     */     } 
/* 199 */     this.eof = true;
/* 200 */     throw new TruncatedChunkException("Truncated chunk ( expected size: " + this.chunkSize + "; actual size: " + this.pos + ")");
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
/*     */   public int read(byte[] b) throws IOException {
/* 215 */     return read(b, 0, b.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void nextChunk() throws IOException {
/* 223 */     if (this.state == Integer.MAX_VALUE) {
/* 224 */       throw new MalformedChunkCodingException("Corrupt data stream");
/*     */     }
/*     */     try {
/* 227 */       this.chunkSize = getChunkSize();
/* 228 */       if (this.chunkSize < 0) {
/* 229 */         throw new MalformedChunkCodingException("Negative chunk size");
/*     */       }
/* 231 */       this.state = 2;
/* 232 */       this.pos = 0;
/* 233 */       if (this.chunkSize == 0) {
/* 234 */         this.eof = true;
/* 235 */         parseTrailerHeaders();
/*     */       } 
/* 237 */     } catch (MalformedChunkCodingException ex) {
/* 238 */       this.state = Integer.MAX_VALUE;
/* 239 */       throw ex;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int getChunkSize() throws IOException {
/* 249 */     int bytesRead1, bytesRead2, separator, st = this.state;
/* 250 */     switch (st) {
/*     */       case 3:
/* 252 */         this.buffer.clear();
/* 253 */         bytesRead1 = this.in.readLine(this.buffer);
/* 254 */         if (bytesRead1 == -1) {
/* 255 */           throw new MalformedChunkCodingException("CRLF expected at end of chunk");
/*     */         }
/*     */         
/* 258 */         if (!this.buffer.isEmpty()) {
/* 259 */           throw new MalformedChunkCodingException("Unexpected content at the end of chunk");
/*     */         }
/*     */         
/* 262 */         this.state = 1;
/*     */       
/*     */       case 1:
/* 265 */         this.buffer.clear();
/* 266 */         bytesRead2 = this.in.readLine(this.buffer);
/* 267 */         if (bytesRead2 == -1) {
/* 268 */           throw new ConnectionClosedException("Premature end of chunk coded message body: closing chunk expected");
/*     */         }
/*     */         
/* 271 */         separator = this.buffer.indexOf(59);
/* 272 */         if (separator < 0) {
/* 273 */           separator = this.buffer.length();
/*     */         }
/*     */         try {
/* 276 */           return Integer.parseInt(this.buffer.substringTrimmed(0, separator), 16);
/* 277 */         } catch (NumberFormatException e) {
/* 278 */           throw new MalformedChunkCodingException("Bad chunk header");
/*     */         } 
/*     */     } 
/* 281 */     throw new IllegalStateException("Inconsistent codec state");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void parseTrailerHeaders() throws IOException {
/*     */     try {
/* 291 */       this.footers = AbstractMessageParser.parseHeaders(this.in, this.constraints.getMaxHeaderCount(), this.constraints.getMaxLineLength(), null);
/*     */ 
/*     */     
/*     */     }
/* 295 */     catch (HttpException ex) {
/* 296 */       MalformedChunkCodingException malformedChunkCodingException = new MalformedChunkCodingException("Invalid footer: " + ex.getMessage());
/*     */       
/* 298 */       malformedChunkCodingException.initCause((Throwable)ex);
/* 299 */       throw malformedChunkCodingException;
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
/*     */   public void close() throws IOException {
/* 311 */     if (!this.closed) {
/*     */       try {
/* 313 */         if (!this.eof && this.state != Integer.MAX_VALUE) {
/*     */           
/* 315 */           byte[] buff = new byte[2048];
/* 316 */           while (read(buff) >= 0);
/*     */         } 
/*     */       } finally {
/*     */         
/* 320 */         this.eof = true;
/* 321 */         this.closed = true;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public Header[] getFooters() {
/* 327 */     return (Header[])this.footers.clone();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\io\ChunkedInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */