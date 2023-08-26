/*     */ package org.apache.http.message;
/*     */ 
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpVersion;
/*     */ import org.apache.http.ParseException;
/*     */ import org.apache.http.ProtocolVersion;
/*     */ import org.apache.http.RequestLine;
/*     */ import org.apache.http.StatusLine;
/*     */ import org.apache.http.annotation.Immutable;
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
/*     */ public class BasicLineParser
/*     */   implements LineParser
/*     */ {
/*     */   @Deprecated
/*  71 */   public static final BasicLineParser DEFAULT = new BasicLineParser();
/*     */   
/*  73 */   public static final BasicLineParser INSTANCE = new BasicLineParser();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final ProtocolVersion protocol;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicLineParser(ProtocolVersion proto) {
/*  90 */     this.protocol = (proto != null) ? proto : (ProtocolVersion)HttpVersion.HTTP_1_1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicLineParser() {
/*  98 */     this(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ProtocolVersion parseProtocolVersion(String value, LineParser parser) throws ParseException {
/* 105 */     Args.notNull(value, "Value");
/*     */     
/* 107 */     CharArrayBuffer buffer = new CharArrayBuffer(value.length());
/* 108 */     buffer.append(value);
/* 109 */     ParserCursor cursor = new ParserCursor(0, value.length());
/* 110 */     return ((parser != null) ? parser : INSTANCE).parseProtocolVersion(buffer, cursor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProtocolVersion parseProtocolVersion(CharArrayBuffer buffer, ParserCursor cursor) throws ParseException {
/*     */     int major, minor;
/* 119 */     Args.notNull(buffer, "Char array buffer");
/* 120 */     Args.notNull(cursor, "Parser cursor");
/* 121 */     String protoname = this.protocol.getProtocol();
/* 122 */     int protolength = protoname.length();
/*     */     
/* 124 */     int indexFrom = cursor.getPos();
/* 125 */     int indexTo = cursor.getUpperBound();
/*     */     
/* 127 */     skipWhitespace(buffer, cursor);
/*     */     
/* 129 */     int i = cursor.getPos();
/*     */ 
/*     */     
/* 132 */     if (i + protolength + 4 > indexTo) {
/* 133 */       throw new ParseException("Not a valid protocol version: " + buffer.substring(indexFrom, indexTo));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 139 */     boolean ok = true;
/* 140 */     for (int j = 0; ok && j < protolength; j++) {
/* 141 */       ok = (buffer.charAt(i + j) == protoname.charAt(j));
/*     */     }
/* 143 */     if (ok) {
/* 144 */       ok = (buffer.charAt(i + protolength) == '/');
/*     */     }
/* 146 */     if (!ok) {
/* 147 */       throw new ParseException("Not a valid protocol version: " + buffer.substring(indexFrom, indexTo));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 152 */     i += protolength + 1;
/*     */     
/* 154 */     int period = buffer.indexOf(46, i, indexTo);
/* 155 */     if (period == -1) {
/* 156 */       throw new ParseException("Invalid protocol version number: " + buffer.substring(indexFrom, indexTo));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 162 */       major = Integer.parseInt(buffer.substringTrimmed(i, period));
/* 163 */     } catch (NumberFormatException e) {
/* 164 */       throw new ParseException("Invalid protocol major version number: " + buffer.substring(indexFrom, indexTo));
/*     */     } 
/*     */ 
/*     */     
/* 168 */     i = period + 1;
/*     */     
/* 170 */     int blank = buffer.indexOf(32, i, indexTo);
/* 171 */     if (blank == -1) {
/* 172 */       blank = indexTo;
/*     */     }
/*     */     
/*     */     try {
/* 176 */       minor = Integer.parseInt(buffer.substringTrimmed(i, blank));
/* 177 */     } catch (NumberFormatException e) {
/* 178 */       throw new ParseException("Invalid protocol minor version number: " + buffer.substring(indexFrom, indexTo));
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 183 */     cursor.updatePos(blank);
/*     */     
/* 185 */     return createProtocolVersion(major, minor);
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
/*     */   protected ProtocolVersion createProtocolVersion(int major, int minor) {
/* 200 */     return this.protocol.forVersion(major, minor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasProtocolVersion(CharArrayBuffer buffer, ParserCursor cursor) {
/* 209 */     Args.notNull(buffer, "Char array buffer");
/* 210 */     Args.notNull(cursor, "Parser cursor");
/* 211 */     int index = cursor.getPos();
/*     */     
/* 213 */     String protoname = this.protocol.getProtocol();
/* 214 */     int protolength = protoname.length();
/*     */     
/* 216 */     if (buffer.length() < protolength + 4)
/*     */     {
/* 218 */       return false;
/*     */     }
/*     */     
/* 221 */     if (index < 0) {
/*     */ 
/*     */       
/* 224 */       index = buffer.length() - 4 - protolength;
/* 225 */     } else if (index == 0) {
/*     */       
/* 227 */       while (index < buffer.length() && HTTP.isWhitespace(buffer.charAt(index)))
/*     */       {
/* 229 */         index++;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 234 */     if (index + protolength + 4 > buffer.length()) {
/* 235 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 240 */     boolean ok = true;
/* 241 */     for (int j = 0; ok && j < protolength; j++) {
/* 242 */       ok = (buffer.charAt(index + j) == protoname.charAt(j));
/*     */     }
/* 244 */     if (ok) {
/* 245 */       ok = (buffer.charAt(index + protolength) == '/');
/*     */     }
/*     */     
/* 248 */     return ok;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RequestLine parseRequestLine(String value, LineParser parser) throws ParseException {
/* 256 */     Args.notNull(value, "Value");
/*     */     
/* 258 */     CharArrayBuffer buffer = new CharArrayBuffer(value.length());
/* 259 */     buffer.append(value);
/* 260 */     ParserCursor cursor = new ParserCursor(0, value.length());
/* 261 */     return ((parser != null) ? parser : INSTANCE).parseRequestLine(buffer, cursor);
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
/*     */   public RequestLine parseRequestLine(CharArrayBuffer buffer, ParserCursor cursor) throws ParseException {
/* 279 */     Args.notNull(buffer, "Char array buffer");
/* 280 */     Args.notNull(cursor, "Parser cursor");
/* 281 */     int indexFrom = cursor.getPos();
/* 282 */     int indexTo = cursor.getUpperBound();
/*     */     
/*     */     try {
/* 285 */       skipWhitespace(buffer, cursor);
/* 286 */       int i = cursor.getPos();
/*     */       
/* 288 */       int blank = buffer.indexOf(32, i, indexTo);
/* 289 */       if (blank < 0) {
/* 290 */         throw new ParseException("Invalid request line: " + buffer.substring(indexFrom, indexTo));
/*     */       }
/*     */       
/* 293 */       String method = buffer.substringTrimmed(i, blank);
/* 294 */       cursor.updatePos(blank);
/*     */       
/* 296 */       skipWhitespace(buffer, cursor);
/* 297 */       i = cursor.getPos();
/*     */       
/* 299 */       blank = buffer.indexOf(32, i, indexTo);
/* 300 */       if (blank < 0) {
/* 301 */         throw new ParseException("Invalid request line: " + buffer.substring(indexFrom, indexTo));
/*     */       }
/*     */       
/* 304 */       String uri = buffer.substringTrimmed(i, blank);
/* 305 */       cursor.updatePos(blank);
/*     */       
/* 307 */       ProtocolVersion ver = parseProtocolVersion(buffer, cursor);
/*     */       
/* 309 */       skipWhitespace(buffer, cursor);
/* 310 */       if (!cursor.atEnd()) {
/* 311 */         throw new ParseException("Invalid request line: " + buffer.substring(indexFrom, indexTo));
/*     */       }
/*     */ 
/*     */       
/* 315 */       return createRequestLine(method, uri, ver);
/* 316 */     } catch (IndexOutOfBoundsException e) {
/* 317 */       throw new ParseException("Invalid request line: " + buffer.substring(indexFrom, indexTo));
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
/*     */ 
/*     */   
/*     */   protected RequestLine createRequestLine(String method, String uri, ProtocolVersion ver) {
/* 336 */     return new BasicRequestLine(method, uri, ver);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static StatusLine parseStatusLine(String value, LineParser parser) throws ParseException {
/* 344 */     Args.notNull(value, "Value");
/*     */     
/* 346 */     CharArrayBuffer buffer = new CharArrayBuffer(value.length());
/* 347 */     buffer.append(value);
/* 348 */     ParserCursor cursor = new ParserCursor(0, value.length());
/* 349 */     return ((parser != null) ? parser : INSTANCE).parseStatusLine(buffer, cursor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StatusLine parseStatusLine(CharArrayBuffer buffer, ParserCursor cursor) throws ParseException {
/* 358 */     Args.notNull(buffer, "Char array buffer");
/* 359 */     Args.notNull(cursor, "Parser cursor");
/* 360 */     int indexFrom = cursor.getPos();
/* 361 */     int indexTo = cursor.getUpperBound();
/*     */     try {
/*     */       int statusCode;
/*     */       String reasonPhrase;
/* 365 */       ProtocolVersion ver = parseProtocolVersion(buffer, cursor);
/*     */ 
/*     */       
/* 368 */       skipWhitespace(buffer, cursor);
/* 369 */       int i = cursor.getPos();
/*     */       
/* 371 */       int blank = buffer.indexOf(32, i, indexTo);
/* 372 */       if (blank < 0) {
/* 373 */         blank = indexTo;
/*     */       }
/*     */       
/* 376 */       String s = buffer.substringTrimmed(i, blank);
/* 377 */       for (int j = 0; j < s.length(); j++) {
/* 378 */         if (!Character.isDigit(s.charAt(j))) {
/* 379 */           throw new ParseException("Status line contains invalid status code: " + buffer.substring(indexFrom, indexTo));
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/*     */       try {
/* 385 */         statusCode = Integer.parseInt(s);
/* 386 */       } catch (NumberFormatException e) {
/* 387 */         throw new ParseException("Status line contains invalid status code: " + buffer.substring(indexFrom, indexTo));
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 392 */       i = blank;
/*     */       
/* 394 */       if (i < indexTo) {
/* 395 */         reasonPhrase = buffer.substringTrimmed(i, indexTo);
/*     */       } else {
/* 397 */         reasonPhrase = "";
/*     */       } 
/* 399 */       return createStatusLine(ver, statusCode, reasonPhrase);
/*     */     }
/* 401 */     catch (IndexOutOfBoundsException e) {
/* 402 */       throw new ParseException("Invalid status line: " + buffer.substring(indexFrom, indexTo));
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
/*     */ 
/*     */   
/*     */   protected StatusLine createStatusLine(ProtocolVersion ver, int status, String reason) {
/* 421 */     return new BasicStatusLine(ver, status, reason);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Header parseHeader(String value, LineParser parser) throws ParseException {
/* 429 */     Args.notNull(value, "Value");
/*     */     
/* 431 */     CharArrayBuffer buffer = new CharArrayBuffer(value.length());
/* 432 */     buffer.append(value);
/* 433 */     return ((parser != null) ? parser : INSTANCE).parseHeader(buffer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Header parseHeader(CharArrayBuffer buffer) throws ParseException {
/* 444 */     return (Header)new BufferedHeader(buffer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void skipWhitespace(CharArrayBuffer buffer, ParserCursor cursor) {
/* 452 */     int pos = cursor.getPos();
/* 453 */     int indexTo = cursor.getUpperBound();
/* 454 */     while (pos < indexTo && HTTP.isWhitespace(buffer.charAt(pos)))
/*     */     {
/* 456 */       pos++;
/*     */     }
/* 458 */     cursor.updatePos(pos);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\message\BasicLineParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */