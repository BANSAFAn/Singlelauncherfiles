/*     */ package org.apache.commons.io.input;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReaderInputStream
/*     */   extends InputStream
/*     */ {
/*     */   private static final int DEFAULT_BUFFER_SIZE = 1024;
/*     */   private final Reader reader;
/*     */   private final CharsetEncoder encoder;
/*     */   private final CharBuffer encoderIn;
/*     */   private final ByteBuffer encoderOut;
/*     */   private CoderResult lastCoderResult;
/*     */   private boolean endOfInput;
/*     */   
/*     */   public ReaderInputStream(Reader reader, CharsetEncoder encoder) {
/* 113 */     this(reader, encoder, 1024);
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
/*     */   public ReaderInputStream(Reader reader, CharsetEncoder encoder, int bufferSize) {
/* 125 */     this.reader = reader;
/* 126 */     this.encoder = encoder;
/* 127 */     this.encoderIn = CharBuffer.allocate(bufferSize);
/* 128 */     this.encoderIn.flip();
/* 129 */     this.encoderOut = ByteBuffer.allocate(128);
/* 130 */     this.encoderOut.flip();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReaderInputStream(Reader reader, Charset charset, int bufferSize) {
/* 141 */     this(reader, charset
/* 142 */         .newEncoder()
/* 143 */         .onMalformedInput(CodingErrorAction.REPLACE)
/* 144 */         .onUnmappableCharacter(CodingErrorAction.REPLACE), bufferSize);
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
/*     */   public ReaderInputStream(Reader reader, Charset charset) {
/* 156 */     this(reader, charset, 1024);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReaderInputStream(Reader reader, String charsetName, int bufferSize) {
/* 167 */     this(reader, Charset.forName(charsetName), bufferSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReaderInputStream(Reader reader, String charsetName) {
/* 178 */     this(reader, charsetName, 1024);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public ReaderInputStream(Reader reader) {
/* 190 */     this(reader, Charset.defaultCharset());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void fillBuffer() throws IOException {
/* 200 */     if (!this.endOfInput && (this.lastCoderResult == null || this.lastCoderResult.isUnderflow())) {
/* 201 */       this.encoderIn.compact();
/* 202 */       int position = this.encoderIn.position();
/*     */ 
/*     */ 
/*     */       
/* 206 */       int c = this.reader.read(this.encoderIn.array(), position, this.encoderIn.remaining());
/* 207 */       if (c == -1) {
/* 208 */         this.endOfInput = true;
/*     */       } else {
/* 210 */         this.encoderIn.position(position + c);
/*     */       } 
/* 212 */       this.encoderIn.flip();
/*     */     } 
/* 214 */     this.encoderOut.compact();
/* 215 */     this.lastCoderResult = this.encoder.encode(this.encoderIn, this.encoderOut, this.endOfInput);
/* 216 */     this.encoderOut.flip();
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
/*     */   public int read(byte[] array, int off, int len) throws IOException {
/* 231 */     Objects.requireNonNull(array, "array");
/* 232 */     if (len < 0 || off < 0 || off + len > array.length) {
/* 233 */       throw new IndexOutOfBoundsException("Array Size=" + array.length + ", offset=" + off + ", length=" + len);
/*     */     }
/*     */     
/* 236 */     int read = 0;
/* 237 */     if (len == 0) {
/* 238 */       return 0;
/*     */     }
/* 240 */     while (len > 0) {
/* 241 */       if (this.encoderOut.hasRemaining()) {
/* 242 */         int c = Math.min(this.encoderOut.remaining(), len);
/* 243 */         this.encoderOut.get(array, off, c);
/* 244 */         off += c;
/* 245 */         len -= c;
/* 246 */         read += c; continue;
/*     */       } 
/* 248 */       fillBuffer();
/* 249 */       if (this.endOfInput && !this.encoderOut.hasRemaining()) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */     
/* 254 */     return (read == 0 && this.endOfInput) ? -1 : read;
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
/*     */   public int read(byte[] b) throws IOException {
/* 267 */     return read(b, 0, b.length);
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
/*     */   public int read() throws IOException {
/*     */     while (true) {
/* 280 */       if (this.encoderOut.hasRemaining()) {
/* 281 */         return this.encoderOut.get() & 0xFF;
/*     */       }
/* 283 */       fillBuffer();
/* 284 */       if (this.endOfInput && !this.encoderOut.hasRemaining()) {
/* 285 */         return -1;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 297 */     this.reader.close();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\input\ReaderInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */