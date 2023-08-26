/*     */ package org.apache.http.impl.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpMessage;
/*     */ import org.apache.http.MessageConstraintException;
/*     */ import org.apache.http.ParseException;
/*     */ import org.apache.http.ProtocolException;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.config.MessageConstraints;
/*     */ import org.apache.http.io.HttpMessageParser;
/*     */ import org.apache.http.io.SessionInputBuffer;
/*     */ import org.apache.http.message.BasicLineParser;
/*     */ import org.apache.http.message.LineParser;
/*     */ import org.apache.http.params.HttpParamConfig;
/*     */ import org.apache.http.params.HttpParams;
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
/*     */ @NotThreadSafe
/*     */ public abstract class AbstractMessageParser<T extends HttpMessage>
/*     */   implements HttpMessageParser<T>
/*     */ {
/*     */   private static final int HEAD_LINE = 0;
/*     */   private static final int HEADERS = 1;
/*     */   private final SessionInputBuffer sessionBuffer;
/*     */   private final MessageConstraints messageConstraints;
/*     */   private final List<CharArrayBuffer> headerLines;
/*     */   protected final LineParser lineParser;
/*     */   private int state;
/*     */   private T message;
/*     */   
/*     */   @Deprecated
/*     */   public AbstractMessageParser(SessionInputBuffer buffer, LineParser parser, HttpParams params) {
/*  88 */     Args.notNull(buffer, "Session input buffer");
/*  89 */     Args.notNull(params, "HTTP parameters");
/*  90 */     this.sessionBuffer = buffer;
/*  91 */     this.messageConstraints = HttpParamConfig.getMessageConstraints(params);
/*  92 */     this.lineParser = (parser != null) ? parser : (LineParser)BasicLineParser.INSTANCE;
/*  93 */     this.headerLines = new ArrayList<CharArrayBuffer>();
/*  94 */     this.state = 0;
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
/*     */   public AbstractMessageParser(SessionInputBuffer buffer, LineParser lineParser, MessageConstraints constraints) {
/* 113 */     this.sessionBuffer = (SessionInputBuffer)Args.notNull(buffer, "Session input buffer");
/* 114 */     this.lineParser = (lineParser != null) ? lineParser : (LineParser)BasicLineParser.INSTANCE;
/* 115 */     this.messageConstraints = (constraints != null) ? constraints : MessageConstraints.DEFAULT;
/* 116 */     this.headerLines = new ArrayList<CharArrayBuffer>();
/* 117 */     this.state = 0;
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
/*     */   
/*     */   public static Header[] parseHeaders(SessionInputBuffer inbuffer, int maxHeaderCount, int maxLineLen, LineParser parser) throws HttpException, IOException {
/* 144 */     List<CharArrayBuffer> headerLines = new ArrayList<CharArrayBuffer>();
/* 145 */     return parseHeaders(inbuffer, maxHeaderCount, maxLineLen, (parser != null) ? parser : (LineParser)BasicLineParser.INSTANCE, headerLines);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Header[] parseHeaders(SessionInputBuffer inbuffer, int maxHeaderCount, int maxLineLen, LineParser parser, List<CharArrayBuffer> headerLines) throws HttpException, IOException {
/* 180 */     Args.notNull(inbuffer, "Session input buffer");
/* 181 */     Args.notNull(parser, "Line parser");
/* 182 */     Args.notNull(headerLines, "Header line list");
/*     */     
/* 184 */     CharArrayBuffer current = null;
/* 185 */     CharArrayBuffer previous = null;
/*     */     while (true) {
/* 187 */       if (current == null) {
/* 188 */         current = new CharArrayBuffer(64);
/*     */       } else {
/* 190 */         current.clear();
/*     */       } 
/* 192 */       int l = inbuffer.readLine(current);
/* 193 */       if (l == -1 || current.length() < 1) {
/*     */         break;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 200 */       if ((current.charAt(0) == ' ' || current.charAt(0) == '\t') && previous != null) {
/*     */ 
/*     */         
/* 203 */         int j = 0;
/* 204 */         while (j < current.length()) {
/* 205 */           char ch = current.charAt(j);
/* 206 */           if (ch != ' ' && ch != '\t') {
/*     */             break;
/*     */           }
/* 209 */           j++;
/*     */         } 
/* 211 */         if (maxLineLen > 0 && previous.length() + 1 + current.length() - j > maxLineLen)
/*     */         {
/* 213 */           throw new MessageConstraintException("Maximum line length limit exceeded");
/*     */         }
/* 215 */         previous.append(' ');
/* 216 */         previous.append(current, j, current.length() - j);
/*     */       } else {
/* 218 */         headerLines.add(current);
/* 219 */         previous = current;
/* 220 */         current = null;
/*     */       } 
/* 222 */       if (maxHeaderCount > 0 && headerLines.size() >= maxHeaderCount) {
/* 223 */         throw new MessageConstraintException("Maximum header count exceeded");
/*     */       }
/*     */     } 
/* 226 */     Header[] headers = new Header[headerLines.size()];
/* 227 */     for (int i = 0; i < headerLines.size(); i++) {
/* 228 */       CharArrayBuffer buffer = headerLines.get(i);
/*     */       try {
/* 230 */         headers[i] = parser.parseHeader(buffer);
/* 231 */       } catch (ParseException ex) {
/* 232 */         throw new ProtocolException(ex.getMessage());
/*     */       } 
/*     */     } 
/* 235 */     return headers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract T parseHead(SessionInputBuffer paramSessionInputBuffer) throws IOException, HttpException, ParseException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T parse() throws IOException, HttpException {
/*     */     Header[] headers;
/*     */     T result;
/* 257 */     int st = this.state;
/* 258 */     switch (st) {
/*     */       case 0:
/*     */         try {
/* 261 */           this.message = parseHead(this.sessionBuffer);
/* 262 */         } catch (ParseException px) {
/* 263 */           throw new ProtocolException(px.getMessage(), px);
/*     */         } 
/* 265 */         this.state = 1;
/*     */       
/*     */       case 1:
/* 268 */         headers = parseHeaders(this.sessionBuffer, this.messageConstraints.getMaxHeaderCount(), this.messageConstraints.getMaxLineLength(), this.lineParser, this.headerLines);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 274 */         this.message.setHeaders(headers);
/* 275 */         result = this.message;
/* 276 */         this.message = null;
/* 277 */         this.headerLines.clear();
/* 278 */         this.state = 0;
/* 279 */         return result;
/*     */     } 
/* 281 */     throw new IllegalStateException("Inconsistent parser state");
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\io\AbstractMessageParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */