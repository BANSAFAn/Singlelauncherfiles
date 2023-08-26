/*     */ package org.apache.commons.io.input;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.CharacterCodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ import java.nio.charset.CodingErrorAction;
/*     */ import java.util.Objects;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CharSequenceInputStream
/*     */   extends InputStream
/*     */ {
/*     */   private static final int BUFFER_SIZE = 2048;
/*     */   private static final int NO_MARK = -1;
/*     */   private final CharsetEncoder encoder;
/*     */   private final CharBuffer cbuf;
/*     */   private final ByteBuffer bbuf;
/*     */   private int mark_cbuf;
/*     */   private int mark_bbuf;
/*     */   
/*     */   public CharSequenceInputStream(CharSequence cs, Charset charset, int bufferSize) {
/*  64 */     this
/*     */       
/*  66 */       .encoder = charset.newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
/*     */     
/*  68 */     float maxBytesPerChar = this.encoder.maxBytesPerChar();
/*  69 */     if (bufferSize < maxBytesPerChar) {
/*  70 */       throw new IllegalArgumentException("Buffer size " + bufferSize + " is less than maxBytesPerChar " + maxBytesPerChar);
/*     */     }
/*     */     
/*  73 */     this.bbuf = ByteBuffer.allocate(bufferSize);
/*  74 */     this.bbuf.flip();
/*  75 */     this.cbuf = CharBuffer.wrap(cs);
/*  76 */     this.mark_cbuf = -1;
/*  77 */     this.mark_bbuf = -1;
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
/*     */   public CharSequenceInputStream(CharSequence cs, String charset, int bufferSize) {
/*  89 */     this(cs, Charset.forName(charset), bufferSize);
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
/*     */   public CharSequenceInputStream(CharSequence cs, Charset charset) {
/* 101 */     this(cs, charset, 2048);
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
/*     */   public CharSequenceInputStream(CharSequence cs, String charset) {
/* 113 */     this(cs, charset, 2048);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void fillBuffer() throws CharacterCodingException {
/* 123 */     this.bbuf.compact();
/* 124 */     CoderResult result = this.encoder.encode(this.cbuf, this.bbuf, true);
/* 125 */     if (result.isError()) {
/* 126 */       result.throwException();
/*     */     }
/* 128 */     this.bbuf.flip();
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] array, int off, int len) throws IOException {
/* 133 */     Objects.requireNonNull(array, "array");
/* 134 */     if (len < 0 || off + len > array.length) {
/* 135 */       throw new IndexOutOfBoundsException("Array Size=" + array.length + ", offset=" + off + ", length=" + len);
/*     */     }
/*     */     
/* 138 */     if (len == 0) {
/* 139 */       return 0;
/*     */     }
/* 141 */     if (!this.bbuf.hasRemaining() && !this.cbuf.hasRemaining()) {
/* 142 */       return -1;
/*     */     }
/* 144 */     int bytesRead = 0;
/* 145 */     while (len > 0) {
/* 146 */       if (this.bbuf.hasRemaining()) {
/* 147 */         int chunk = Math.min(this.bbuf.remaining(), len);
/* 148 */         this.bbuf.get(array, off, chunk);
/* 149 */         off += chunk;
/* 150 */         len -= chunk;
/* 151 */         bytesRead += chunk; continue;
/*     */       } 
/* 153 */       fillBuffer();
/* 154 */       if (!this.bbuf.hasRemaining() && !this.cbuf.hasRemaining()) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */     
/* 159 */     return (bytesRead == 0 && !this.cbuf.hasRemaining()) ? -1 : bytesRead;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/*     */     while (true) {
/* 165 */       if (this.bbuf.hasRemaining()) {
/* 166 */         return this.bbuf.get() & 0xFF;
/*     */       }
/* 168 */       fillBuffer();
/* 169 */       if (!this.bbuf.hasRemaining() && !this.cbuf.hasRemaining()) {
/* 170 */         return -1;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] b) throws IOException {
/* 177 */     return read(b, 0, b.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long skip(long n) throws IOException {
/* 185 */     long skipped = 0L;
/* 186 */     while (n > 0L && available() > 0) {
/* 187 */       read();
/* 188 */       n--;
/* 189 */       skipped++;
/*     */     } 
/* 191 */     return skipped;
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
/*     */   public int available() throws IOException {
/* 206 */     return this.bbuf.remaining() + this.cbuf.remaining();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void mark(int readlimit) {
/* 220 */     this.mark_cbuf = this.cbuf.position();
/* 221 */     this.mark_bbuf = this.bbuf.position();
/* 222 */     this.cbuf.mark();
/* 223 */     this.bbuf.mark();
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
/*     */   public synchronized void reset() throws IOException {
/* 241 */     if (this.mark_cbuf != -1) {
/*     */       
/* 243 */       if (this.cbuf.position() != 0) {
/* 244 */         this.encoder.reset();
/* 245 */         this.cbuf.rewind();
/* 246 */         this.bbuf.rewind();
/* 247 */         this.bbuf.limit(0);
/* 248 */         while (this.cbuf.position() < this.mark_cbuf) {
/* 249 */           this.bbuf.rewind();
/* 250 */           this.bbuf.limit(0);
/* 251 */           fillBuffer();
/*     */         } 
/*     */       } 
/* 254 */       if (this.cbuf.position() != this.mark_cbuf) {
/* 255 */         throw new IllegalStateException("Unexpected CharBuffer position: actual=" + this.cbuf.position() + " expected=" + this.mark_cbuf);
/*     */       }
/*     */       
/* 258 */       this.bbuf.position(this.mark_bbuf);
/* 259 */       this.mark_cbuf = -1;
/* 260 */       this.mark_bbuf = -1;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean markSupported() {
/* 266 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\input\CharSequenceInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */