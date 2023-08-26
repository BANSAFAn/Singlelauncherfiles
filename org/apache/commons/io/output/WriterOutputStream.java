/*     */ package org.apache.commons.io.output;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Writer;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ import java.nio.charset.CodingErrorAction;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WriterOutputStream
/*     */   extends OutputStream
/*     */ {
/*     */   private static final int BUFFER_SIZE = 1024;
/*     */   private final Writer writer;
/*     */   private final CharsetDecoder decoder;
/*     */   private final boolean writeImmediately;
/*  86 */   private final ByteBuffer decoderIn = ByteBuffer.allocate(128);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final CharBuffer decoderOut;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WriterOutputStream(Writer writer, CharsetDecoder decoder) {
/* 105 */     this(writer, decoder, 1024, false);
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
/*     */   public WriterOutputStream(Writer writer, CharsetDecoder decoder, int bufferSize, boolean writeImmediately) {
/* 123 */     checkIbmJdkWithBrokenUTF16(decoder.charset());
/* 124 */     this.writer = writer;
/* 125 */     this.decoder = decoder;
/* 126 */     this.writeImmediately = writeImmediately;
/* 127 */     this.decoderOut = CharBuffer.allocate(bufferSize);
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
/*     */   public WriterOutputStream(Writer writer, Charset charset, int bufferSize, boolean writeImmediately) {
/* 144 */     this(writer, charset
/* 145 */         .newDecoder()
/* 146 */         .onMalformedInput(CodingErrorAction.REPLACE)
/* 147 */         .onUnmappableCharacter(CodingErrorAction.REPLACE)
/* 148 */         .replaceWith("?"), bufferSize, writeImmediately);
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
/*     */   public WriterOutputStream(Writer writer, Charset charset) {
/* 162 */     this(writer, charset, 1024, false);
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
/*     */   public WriterOutputStream(Writer writer, String charsetName, int bufferSize, boolean writeImmediately) {
/* 179 */     this(writer, Charset.forName(charsetName), bufferSize, writeImmediately);
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
/*     */   public WriterOutputStream(Writer writer, String charsetName) {
/* 191 */     this(writer, charsetName, 1024, false);
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
/*     */   @Deprecated
/*     */   public WriterOutputStream(Writer writer) {
/* 204 */     this(writer, Charset.defaultCharset(), 1024, false);
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
/*     */   public void write(byte[] b, int off, int len) throws IOException {
/* 217 */     while (len > 0) {
/* 218 */       int c = Math.min(len, this.decoderIn.remaining());
/* 219 */       this.decoderIn.put(b, off, c);
/* 220 */       processInput(false);
/* 221 */       len -= c;
/* 222 */       off += c;
/*     */     } 
/* 224 */     if (this.writeImmediately) {
/* 225 */       flushOutput();
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
/*     */   public void write(byte[] b) throws IOException {
/* 237 */     write(b, 0, b.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(int b) throws IOException {
/* 248 */     write(new byte[] { (byte)b }, 0, 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/* 259 */     flushOutput();
/* 260 */     this.writer.flush();
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
/* 271 */     processInput(true);
/* 272 */     flushOutput();
/* 273 */     this.writer.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processInput(boolean endOfInput) throws IOException {
/*     */     CoderResult coderResult;
/* 284 */     this.decoderIn.flip();
/*     */     
/*     */     while (true) {
/* 287 */       coderResult = this.decoder.decode(this.decoderIn, this.decoderOut, endOfInput);
/* 288 */       if (coderResult.isOverflow())
/* 289 */       { flushOutput(); continue; }  break;
/* 290 */     }  if (coderResult.isUnderflow()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 299 */       this.decoderIn.compact();
/*     */       return;
/*     */     } 
/*     */     throw new IOException("Unexpected coder result");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void flushOutput() throws IOException {
/* 308 */     if (this.decoderOut.position() > 0) {
/* 309 */       this.writer.write(this.decoderOut.array(), 0, this.decoderOut.position());
/* 310 */       this.decoderOut.rewind();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void checkIbmJdkWithBrokenUTF16(Charset charset) {
/* 320 */     if (!"UTF-16".equals(charset.name())) {
/*     */       return;
/*     */     }
/* 323 */     String TEST_STRING_2 = "vés";
/* 324 */     byte[] bytes = "vés".getBytes(charset);
/*     */     
/* 326 */     CharsetDecoder charsetDecoder2 = charset.newDecoder();
/* 327 */     ByteBuffer bb2 = ByteBuffer.allocate(16);
/* 328 */     CharBuffer cb2 = CharBuffer.allocate("vés".length());
/* 329 */     int len = bytes.length;
/* 330 */     for (int i = 0; i < len; i++) {
/* 331 */       bb2.put(bytes[i]);
/* 332 */       bb2.flip();
/*     */       try {
/* 334 */         charsetDecoder2.decode(bb2, cb2, (i == len - 1));
/* 335 */       } catch (IllegalArgumentException e) {
/* 336 */         throw new UnsupportedOperationException("UTF-16 requested when running on an IBM JDK with broken UTF-16 support. Please find a JDK that supports UTF-16 if you intend to use UF-16 with WriterOutputStream");
/*     */       } 
/*     */       
/* 339 */       bb2.compact();
/*     */     } 
/* 341 */     cb2.rewind();
/* 342 */     if (!"vés".equals(cb2.toString()))
/* 343 */       throw new UnsupportedOperationException("UTF-16 requested when running on an IBM JDK with broken UTF-16 support. Please find a JDK that supports UTF-16 if you intend to use UF-16 with WriterOutputStream"); 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\output\WriterOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */