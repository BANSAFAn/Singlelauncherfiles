/*     */ package org.apache.http.client.utils;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.net.URI;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.BitSet;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Scanner;
/*     */ import org.apache.http.Consts;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HeaderElement;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.NameValuePair;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.entity.ContentType;
/*     */ import org.apache.http.message.BasicNameValuePair;
/*     */ import org.apache.http.message.ParserCursor;
/*     */ import org.apache.http.message.TokenParser;
/*     */ import org.apache.http.protocol.HTTP;
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
/*     */ @Immutable
/*     */ public class URLEncodedUtils
/*     */ {
/*     */   public static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
/*     */   private static final char QP_SEP_A = '&';
/*     */   private static final char QP_SEP_S = ';';
/*     */   private static final String NAME_VALUE_SEPARATOR = "=";
/*     */   
/*     */   public static List<NameValuePair> parse(URI uri, String charset) {
/*  91 */     String query = uri.getRawQuery();
/*  92 */     if (query != null && !query.isEmpty()) {
/*  93 */       return parse(query, Charset.forName(charset));
/*     */     }
/*  95 */     return Collections.emptyList();
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
/*     */   public static List<NameValuePair> parse(HttpEntity entity) throws IOException {
/*     */     CharArrayBuffer buf;
/* 112 */     ContentType contentType = ContentType.get(entity);
/* 113 */     if (contentType == null || !contentType.getMimeType().equalsIgnoreCase("application/x-www-form-urlencoded")) {
/* 114 */       return Collections.emptyList();
/*     */     }
/* 116 */     long len = entity.getContentLength();
/* 117 */     Args.check((len <= 2147483647L), "HTTP entity is too large");
/* 118 */     Charset charset = (contentType.getCharset() != null) ? contentType.getCharset() : HTTP.DEF_CONTENT_CHARSET;
/* 119 */     InputStream instream = entity.getContent();
/* 120 */     if (instream == null) {
/* 121 */       return Collections.emptyList();
/*     */     }
/*     */     
/*     */     try {
/* 125 */       buf = new CharArrayBuffer((len > 0L) ? (int)len : 1024);
/* 126 */       Reader reader = new InputStreamReader(instream, charset);
/* 127 */       char[] tmp = new char[1024];
/*     */       int l;
/* 129 */       while ((l = reader.read(tmp)) != -1) {
/* 130 */         buf.append(tmp, 0, l);
/*     */       }
/*     */     } finally {
/*     */       
/* 134 */       instream.close();
/*     */     } 
/* 136 */     if (buf.length() == 0) {
/* 137 */       return Collections.emptyList();
/*     */     }
/* 139 */     return parse(buf, charset, new char[] { '&' });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isEncoded(HttpEntity entity) {
/* 147 */     Header h = entity.getContentType();
/* 148 */     if (h != null) {
/* 149 */       HeaderElement[] elems = h.getElements();
/* 150 */       if (elems.length > 0) {
/* 151 */         String contentType = elems[0].getName();
/* 152 */         return contentType.equalsIgnoreCase("application/x-www-form-urlencoded");
/*     */       } 
/*     */     } 
/* 155 */     return false;
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
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static void parse(List<NameValuePair> parameters, Scanner scanner, String charset) {
/* 178 */     parse(parameters, scanner, "[&;]", charset);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static void parse(List<NameValuePair> parameters, Scanner scanner, String parameterSepartorPattern, String charset) {
/* 205 */     scanner.useDelimiter(parameterSepartorPattern);
/* 206 */     while (scanner.hasNext()) {
/*     */ 
/*     */       
/* 209 */       String name, value, token = scanner.next();
/* 210 */       int i = token.indexOf("=");
/* 211 */       if (i != -1) {
/* 212 */         name = decodeFormFields(token.substring(0, i).trim(), charset);
/* 213 */         value = decodeFormFields(token.substring(i + 1).trim(), charset);
/*     */       } else {
/* 215 */         name = decodeFormFields(token.trim(), charset);
/* 216 */         value = null;
/*     */       } 
/* 218 */       parameters.add(new BasicNameValuePair(name, value));
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
/*     */ 
/*     */   
/*     */   public static List<NameValuePair> parse(String s, Charset charset) {
/* 235 */     CharArrayBuffer buffer = new CharArrayBuffer(s.length());
/* 236 */     buffer.append(s);
/* 237 */     return parse(buffer, charset, new char[] { '&', ';' });
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
/*     */   public static List<NameValuePair> parse(String s, Charset charset, char... separators) {
/* 255 */     if (s == null) {
/* 256 */       return Collections.emptyList();
/*     */     }
/* 258 */     CharArrayBuffer buffer = new CharArrayBuffer(s.length());
/* 259 */     buffer.append(s);
/* 260 */     return parse(buffer, charset, separators);
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
/*     */   public static List<NameValuePair> parse(CharArrayBuffer buf, Charset charset, char... separators) {
/* 279 */     Args.notNull(buf, "Char array buffer");
/* 280 */     TokenParser tokenParser = TokenParser.INSTANCE;
/* 281 */     BitSet delimSet = new BitSet();
/* 282 */     for (char separator : separators) {
/* 283 */       delimSet.set(separator);
/*     */     }
/* 285 */     ParserCursor cursor = new ParserCursor(0, buf.length());
/* 286 */     List<NameValuePair> list = new ArrayList<NameValuePair>();
/* 287 */     while (!cursor.atEnd()) {
/* 288 */       delimSet.set(61);
/* 289 */       String name = tokenParser.parseToken(buf, cursor, delimSet);
/* 290 */       String value = null;
/* 291 */       if (!cursor.atEnd()) {
/* 292 */         int delim = buf.charAt(cursor.getPos());
/* 293 */         cursor.updatePos(cursor.getPos() + 1);
/* 294 */         if (delim == 61) {
/* 295 */           delimSet.clear(61);
/* 296 */           value = tokenParser.parseValue(buf, cursor, delimSet);
/* 297 */           if (!cursor.atEnd()) {
/* 298 */             cursor.updatePos(cursor.getPos() + 1);
/*     */           }
/*     */         } 
/*     */       } 
/* 302 */       if (!name.isEmpty()) {
/* 303 */         list.add(new BasicNameValuePair(decodeFormFields(name, charset), decodeFormFields(value, charset)));
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 308 */     return list;
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
/*     */   public static String format(List<? extends NameValuePair> parameters, String charset) {
/* 322 */     return format(parameters, '&', charset);
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
/*     */   public static String format(List<? extends NameValuePair> parameters, char parameterSeparator, String charset) {
/* 340 */     StringBuilder result = new StringBuilder();
/* 341 */     for (NameValuePair parameter : parameters) {
/* 342 */       String encodedName = encodeFormFields(parameter.getName(), charset);
/* 343 */       String encodedValue = encodeFormFields(parameter.getValue(), charset);
/* 344 */       if (result.length() > 0) {
/* 345 */         result.append(parameterSeparator);
/*     */       }
/* 347 */       result.append(encodedName);
/* 348 */       if (encodedValue != null) {
/* 349 */         result.append("=");
/* 350 */         result.append(encodedValue);
/*     */       } 
/*     */     } 
/* 353 */     return result.toString();
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
/*     */   public static String format(Iterable<? extends NameValuePair> parameters, Charset charset) {
/* 369 */     return format(parameters, '&', charset);
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
/*     */   public static String format(Iterable<? extends NameValuePair> parameters, char parameterSeparator, Charset charset) {
/* 387 */     StringBuilder result = new StringBuilder();
/* 388 */     for (NameValuePair parameter : parameters) {
/* 389 */       String encodedName = encodeFormFields(parameter.getName(), charset);
/* 390 */       String encodedValue = encodeFormFields(parameter.getValue(), charset);
/* 391 */       if (result.length() > 0) {
/* 392 */         result.append(parameterSeparator);
/*     */       }
/* 394 */       result.append(encodedName);
/* 395 */       if (encodedValue != null) {
/* 396 */         result.append("=");
/* 397 */         result.append(encodedValue);
/*     */       } 
/*     */     } 
/* 400 */     return result.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 409 */   private static final BitSet UNRESERVED = new BitSet(256);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 415 */   private static final BitSet PUNCT = new BitSet(256);
/*     */ 
/*     */   
/* 418 */   private static final BitSet USERINFO = new BitSet(256);
/*     */ 
/*     */   
/* 421 */   private static final BitSet PATHSAFE = new BitSet(256);
/*     */ 
/*     */   
/* 424 */   private static final BitSet URIC = new BitSet(256);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 434 */   private static final BitSet RESERVED = new BitSet(256);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 441 */   private static final BitSet URLENCODER = new BitSet(256);
/*     */   private static final int RADIX = 16;
/*     */   
/*     */   static {
/*     */     int i;
/* 446 */     for (i = 97; i <= 122; i++) {
/* 447 */       UNRESERVED.set(i);
/*     */     }
/* 449 */     for (i = 65; i <= 90; i++) {
/* 450 */       UNRESERVED.set(i);
/*     */     }
/*     */     
/* 453 */     for (i = 48; i <= 57; i++) {
/* 454 */       UNRESERVED.set(i);
/*     */     }
/* 456 */     UNRESERVED.set(95);
/* 457 */     UNRESERVED.set(45);
/* 458 */     UNRESERVED.set(46);
/* 459 */     UNRESERVED.set(42);
/* 460 */     URLENCODER.or(UNRESERVED);
/* 461 */     UNRESERVED.set(33);
/* 462 */     UNRESERVED.set(126);
/* 463 */     UNRESERVED.set(39);
/* 464 */     UNRESERVED.set(40);
/* 465 */     UNRESERVED.set(41);
/*     */     
/* 467 */     PUNCT.set(44);
/* 468 */     PUNCT.set(59);
/* 469 */     PUNCT.set(58);
/* 470 */     PUNCT.set(36);
/* 471 */     PUNCT.set(38);
/* 472 */     PUNCT.set(43);
/* 473 */     PUNCT.set(61);
/*     */     
/* 475 */     USERINFO.or(UNRESERVED);
/* 476 */     USERINFO.or(PUNCT);
/*     */ 
/*     */     
/* 479 */     PATHSAFE.or(UNRESERVED);
/* 480 */     PATHSAFE.set(47);
/* 481 */     PATHSAFE.set(59);
/* 482 */     PATHSAFE.set(58);
/* 483 */     PATHSAFE.set(64);
/* 484 */     PATHSAFE.set(38);
/* 485 */     PATHSAFE.set(61);
/* 486 */     PATHSAFE.set(43);
/* 487 */     PATHSAFE.set(36);
/* 488 */     PATHSAFE.set(44);
/*     */     
/* 490 */     RESERVED.set(59);
/* 491 */     RESERVED.set(47);
/* 492 */     RESERVED.set(63);
/* 493 */     RESERVED.set(58);
/* 494 */     RESERVED.set(64);
/* 495 */     RESERVED.set(38);
/* 496 */     RESERVED.set(61);
/* 497 */     RESERVED.set(43);
/* 498 */     RESERVED.set(36);
/* 499 */     RESERVED.set(44);
/* 500 */     RESERVED.set(91);
/* 501 */     RESERVED.set(93);
/*     */     
/* 503 */     URIC.or(RESERVED);
/* 504 */     URIC.or(UNRESERVED);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String urlEncode(String content, Charset charset, BitSet safechars, boolean blankAsPlus) {
/* 514 */     if (content == null) {
/* 515 */       return null;
/*     */     }
/* 517 */     StringBuilder buf = new StringBuilder();
/* 518 */     ByteBuffer bb = charset.encode(content);
/* 519 */     while (bb.hasRemaining()) {
/* 520 */       int b = bb.get() & 0xFF;
/* 521 */       if (safechars.get(b)) {
/* 522 */         buf.append((char)b); continue;
/* 523 */       }  if (blankAsPlus && b == 32) {
/* 524 */         buf.append('+'); continue;
/*     */       } 
/* 526 */       buf.append("%");
/* 527 */       char hex1 = Character.toUpperCase(Character.forDigit(b >> 4 & 0xF, 16));
/* 528 */       char hex2 = Character.toUpperCase(Character.forDigit(b & 0xF, 16));
/* 529 */       buf.append(hex1);
/* 530 */       buf.append(hex2);
/*     */     } 
/*     */     
/* 533 */     return buf.toString();
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
/*     */   private static String urlDecode(String content, Charset charset, boolean plusAsBlank) {
/* 548 */     if (content == null) {
/* 549 */       return null;
/*     */     }
/* 551 */     ByteBuffer bb = ByteBuffer.allocate(content.length());
/* 552 */     CharBuffer cb = CharBuffer.wrap(content);
/* 553 */     while (cb.hasRemaining()) {
/* 554 */       char c = cb.get();
/* 555 */       if (c == '%' && cb.remaining() >= 2) {
/* 556 */         char uc = cb.get();
/* 557 */         char lc = cb.get();
/* 558 */         int u = Character.digit(uc, 16);
/* 559 */         int l = Character.digit(lc, 16);
/* 560 */         if (u != -1 && l != -1) {
/* 561 */           bb.put((byte)((u << 4) + l)); continue;
/*     */         } 
/* 563 */         bb.put((byte)37);
/* 564 */         bb.put((byte)uc);
/* 565 */         bb.put((byte)lc); continue;
/*     */       } 
/* 567 */       if (plusAsBlank && c == '+') {
/* 568 */         bb.put((byte)32); continue;
/*     */       } 
/* 570 */       bb.put((byte)c);
/*     */     } 
/*     */     
/* 573 */     bb.flip();
/* 574 */     return charset.decode(bb).toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String decodeFormFields(String content, String charset) {
/* 585 */     if (content == null) {
/* 586 */       return null;
/*     */     }
/* 588 */     return urlDecode(content, (charset != null) ? Charset.forName(charset) : Consts.UTF_8, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String decodeFormFields(String content, Charset charset) {
/* 599 */     if (content == null) {
/* 600 */       return null;
/*     */     }
/* 602 */     return urlDecode(content, (charset != null) ? charset : Consts.UTF_8, true);
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
/*     */   private static String encodeFormFields(String content, String charset) {
/* 617 */     if (content == null) {
/* 618 */       return null;
/*     */     }
/* 620 */     return urlEncode(content, (charset != null) ? Charset.forName(charset) : Consts.UTF_8, URLENCODER, true);
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
/*     */   private static String encodeFormFields(String content, Charset charset) {
/* 635 */     if (content == null) {
/* 636 */       return null;
/*     */     }
/* 638 */     return urlEncode(content, (charset != null) ? charset : Consts.UTF_8, URLENCODER, true);
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
/*     */   static String encUserInfo(String content, Charset charset) {
/* 651 */     return urlEncode(content, charset, USERINFO, false);
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
/*     */   static String encUric(String content, Charset charset) {
/* 664 */     return urlEncode(content, charset, URIC, false);
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
/*     */   static String encPath(String content, Charset charset) {
/* 677 */     return urlEncode(content, charset, PATHSAFE, false);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\clien\\utils\URLEncodedUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */