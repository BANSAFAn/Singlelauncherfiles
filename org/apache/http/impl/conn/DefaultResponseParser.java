/*     */ package org.apache.http.impl.conn;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpMessage;
/*     */ import org.apache.http.HttpResponseFactory;
/*     */ import org.apache.http.NoHttpResponseException;
/*     */ import org.apache.http.ProtocolException;
/*     */ import org.apache.http.StatusLine;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.impl.io.AbstractMessageParser;
/*     */ import org.apache.http.io.SessionInputBuffer;
/*     */ import org.apache.http.message.LineParser;
/*     */ import org.apache.http.message.ParserCursor;
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
/*     */ @Deprecated
/*     */ @ThreadSafe
/*     */ public class DefaultResponseParser
/*     */   extends AbstractMessageParser<HttpMessage>
/*     */ {
/*  68 */   private final Log log = LogFactory.getLog(getClass());
/*     */ 
/*     */   
/*     */   private final HttpResponseFactory responseFactory;
/*     */   
/*     */   private final CharArrayBuffer lineBuf;
/*     */   
/*     */   private final int maxGarbageLines;
/*     */ 
/*     */   
/*     */   public DefaultResponseParser(SessionInputBuffer buffer, LineParser parser, HttpResponseFactory responseFactory, HttpParams params) {
/*  79 */     super(buffer, parser, params);
/*  80 */     Args.notNull(responseFactory, "Response factory");
/*  81 */     this.responseFactory = responseFactory;
/*  82 */     this.lineBuf = new CharArrayBuffer(128);
/*  83 */     this.maxGarbageLines = getMaxGarbageLines(params);
/*     */   }
/*     */   
/*     */   protected int getMaxGarbageLines(HttpParams params) {
/*  87 */     return params.getIntParameter("http.connection.max-status-line-garbage", 2147483647);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected HttpMessage parseHead(SessionInputBuffer sessionBuffer) throws IOException, HttpException {
/*  96 */     int count = 0;
/*  97 */     ParserCursor cursor = null;
/*     */     
/*     */     while (true) {
/* 100 */       this.lineBuf.clear();
/* 101 */       int i = sessionBuffer.readLine(this.lineBuf);
/* 102 */       if (i == -1 && count == 0)
/*     */       {
/* 104 */         throw new NoHttpResponseException("The target server failed to respond");
/*     */       }
/* 106 */       cursor = new ParserCursor(0, this.lineBuf.length());
/* 107 */       if (this.lineParser.hasProtocolVersion(this.lineBuf, cursor)) {
/*     */         break;
/*     */       }
/* 110 */       if (i == -1 || count >= this.maxGarbageLines)
/*     */       {
/* 112 */         throw new ProtocolException("The server failed to respond with a valid HTTP response");
/*     */       }
/*     */       
/* 115 */       if (this.log.isDebugEnabled()) {
/* 116 */         this.log.debug("Garbage in response: " + this.lineBuf.toString());
/*     */       }
/* 118 */       count++;
/*     */     } 
/*     */     
/* 121 */     StatusLine statusline = this.lineParser.parseStatusLine(this.lineBuf, cursor);
/* 122 */     return (HttpMessage)this.responseFactory.newHttpResponse(statusline, null);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\conn\DefaultResponseParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */