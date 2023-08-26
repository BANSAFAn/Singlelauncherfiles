/*     */ package org.apache.commons.codec.net;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.IllegalCharsetNameException;
/*     */ import java.nio.charset.UnsupportedCharsetException;
/*     */ import java.util.BitSet;
/*     */ import org.apache.commons.codec.BinaryDecoder;
/*     */ import org.apache.commons.codec.BinaryEncoder;
/*     */ import org.apache.commons.codec.Charsets;
/*     */ import org.apache.commons.codec.DecoderException;
/*     */ import org.apache.commons.codec.EncoderException;
/*     */ import org.apache.commons.codec.StringDecoder;
/*     */ import org.apache.commons.codec.StringEncoder;
/*     */ import org.apache.commons.codec.binary.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class QuotedPrintableCodec
/*     */   implements BinaryEncoder, BinaryDecoder, StringEncoder, StringDecoder
/*     */ {
/*     */   private final Charset charset;
/*  70 */   private static final BitSet PRINTABLE_CHARS = new BitSet(256);
/*     */   
/*     */   private static final byte ESCAPE_CHAR = 61;
/*     */   
/*     */   private static final byte TAB = 9;
/*     */   
/*     */   private static final byte SPACE = 32;
/*     */   
/*     */   static {
/*     */     int i;
/*  80 */     for (i = 33; i <= 60; i++) {
/*  81 */       PRINTABLE_CHARS.set(i);
/*     */     }
/*  83 */     for (i = 62; i <= 126; i++) {
/*  84 */       PRINTABLE_CHARS.set(i);
/*     */     }
/*  86 */     PRINTABLE_CHARS.set(9);
/*  87 */     PRINTABLE_CHARS.set(32);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public QuotedPrintableCodec() {
/*  94 */     this(Charsets.UTF_8);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public QuotedPrintableCodec(Charset charset) {
/* 105 */     this.charset = charset;
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
/*     */   public QuotedPrintableCodec(String charsetName) throws IllegalCharsetNameException, IllegalArgumentException, UnsupportedCharsetException {
/* 125 */     this(Charset.forName(charsetName));
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
/*     */   private static final void encodeQuotedPrintable(int b, ByteArrayOutputStream buffer) {
/* 137 */     buffer.write(61);
/* 138 */     char hex1 = Character.toUpperCase(Character.forDigit(b >> 4 & 0xF, 16));
/* 139 */     char hex2 = Character.toUpperCase(Character.forDigit(b & 0xF, 16));
/* 140 */     buffer.write(hex1);
/* 141 */     buffer.write(hex2);
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
/*     */   public static final byte[] encodeQuotedPrintable(BitSet printable, byte[] bytes) {
/* 157 */     if (bytes == null) {
/* 158 */       return null;
/*     */     }
/* 160 */     if (printable == null) {
/* 161 */       printable = PRINTABLE_CHARS;
/*     */     }
/* 163 */     ByteArrayOutputStream buffer = new ByteArrayOutputStream();
/* 164 */     for (byte c : bytes) {
/* 165 */       int b = c;
/* 166 */       if (b < 0) {
/* 167 */         b = 256 + b;
/*     */       }
/* 169 */       if (printable.get(b)) {
/* 170 */         buffer.write(b);
/*     */       } else {
/* 172 */         encodeQuotedPrintable(b, buffer);
/*     */       } 
/*     */     } 
/* 175 */     return buffer.toByteArray();
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
/*     */   public static final byte[] decodeQuotedPrintable(byte[] bytes) throws DecoderException {
/* 192 */     if (bytes == null) {
/* 193 */       return null;
/*     */     }
/* 195 */     ByteArrayOutputStream buffer = new ByteArrayOutputStream();
/* 196 */     for (int i = 0; i < bytes.length; i++) {
/* 197 */       int b = bytes[i];
/* 198 */       if (b == 61) {
/*     */         try {
/* 200 */           int u = Utils.digit16(bytes[++i]);
/* 201 */           int l = Utils.digit16(bytes[++i]);
/* 202 */           buffer.write((char)((u << 4) + l));
/* 203 */         } catch (ArrayIndexOutOfBoundsException e) {
/* 204 */           throw new DecoderException("Invalid quoted-printable encoding", e);
/*     */         } 
/*     */       } else {
/* 207 */         buffer.write(b);
/*     */       } 
/*     */     } 
/* 210 */     return buffer.toByteArray();
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
/*     */   public byte[] encode(byte[] bytes) {
/* 225 */     return encodeQuotedPrintable(PRINTABLE_CHARS, bytes);
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
/*     */   public byte[] decode(byte[] bytes) throws DecoderException {
/* 243 */     return decodeQuotedPrintable(bytes);
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
/*     */   public String encode(String str) throws EncoderException {
/* 262 */     return encode(str, getCharset());
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
/*     */   public String decode(String str, Charset charset) throws DecoderException {
/* 279 */     if (str == null) {
/* 280 */       return null;
/*     */     }
/* 282 */     return new String(decode(StringUtils.getBytesUsAscii(str)), charset);
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
/*     */   public String decode(String str, String charset) throws DecoderException, UnsupportedEncodingException {
/* 300 */     if (str == null) {
/* 301 */       return null;
/*     */     }
/* 303 */     return new String(decode(StringUtils.getBytesUsAscii(str)), charset);
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
/*     */   public String decode(String str) throws DecoderException {
/* 319 */     return decode(str, getCharset());
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
/*     */   public Object encode(Object obj) throws EncoderException {
/* 334 */     if (obj == null)
/* 335 */       return null; 
/* 336 */     if (obj instanceof byte[])
/* 337 */       return encode((byte[])obj); 
/* 338 */     if (obj instanceof String) {
/* 339 */       return encode((String)obj);
/*     */     }
/* 341 */     throw new EncoderException("Objects of type " + obj.getClass().getName() + " cannot be quoted-printable encoded");
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
/*     */   public Object decode(Object obj) throws DecoderException {
/* 360 */     if (obj == null)
/* 361 */       return null; 
/* 362 */     if (obj instanceof byte[])
/* 363 */       return decode((byte[])obj); 
/* 364 */     if (obj instanceof String) {
/* 365 */       return decode((String)obj);
/*     */     }
/* 367 */     throw new DecoderException("Objects of type " + obj.getClass().getName() + " cannot be quoted-printable decoded");
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
/*     */   public Charset getCharset() {
/* 380 */     return this.charset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDefaultCharset() {
/* 389 */     return this.charset.name();
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
/*     */   public String encode(String str, Charset charset) {
/* 406 */     if (str == null) {
/* 407 */       return null;
/*     */     }
/* 409 */     return StringUtils.newStringUsAscii(encode(str.getBytes(charset)));
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
/*     */   public String encode(String str, String charset) throws UnsupportedEncodingException {
/* 427 */     if (str == null) {
/* 428 */       return null;
/*     */     }
/* 430 */     return StringUtils.newStringUsAscii(encode(str.getBytes(charset)));
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\codec\net\QuotedPrintableCodec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */