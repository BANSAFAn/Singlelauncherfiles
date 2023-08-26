/*    */ package org.apache.http.impl.io;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.http.ConnectionClosedException;
/*    */ import org.apache.http.HttpException;
/*    */ import org.apache.http.HttpMessage;
/*    */ import org.apache.http.HttpRequestFactory;
/*    */ import org.apache.http.ParseException;
/*    */ import org.apache.http.RequestLine;
/*    */ import org.apache.http.annotation.NotThreadSafe;
/*    */ import org.apache.http.io.SessionInputBuffer;
/*    */ import org.apache.http.message.LineParser;
/*    */ import org.apache.http.message.ParserCursor;
/*    */ import org.apache.http.params.HttpParams;
/*    */ import org.apache.http.util.Args;
/*    */ import org.apache.http.util.CharArrayBuffer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Deprecated
/*    */ @NotThreadSafe
/*    */ public class HttpRequestParser
/*    */   extends AbstractMessageParser<HttpMessage>
/*    */ {
/*    */   private final HttpRequestFactory requestFactory;
/*    */   private final CharArrayBuffer lineBuf;
/*    */   
/*    */   public HttpRequestParser(SessionInputBuffer buffer, LineParser parser, HttpRequestFactory requestFactory, HttpParams params) {
/* 82 */     super(buffer, parser, params);
/* 83 */     this.requestFactory = (HttpRequestFactory)Args.notNull(requestFactory, "Request factory");
/* 84 */     this.lineBuf = new CharArrayBuffer(128);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected HttpMessage parseHead(SessionInputBuffer sessionBuffer) throws IOException, HttpException, ParseException {
/* 92 */     this.lineBuf.clear();
/* 93 */     int i = sessionBuffer.readLine(this.lineBuf);
/* 94 */     if (i == -1) {
/* 95 */       throw new ConnectionClosedException("Client closed connection");
/*    */     }
/* 97 */     ParserCursor cursor = new ParserCursor(0, this.lineBuf.length());
/* 98 */     RequestLine requestline = this.lineParser.parseRequestLine(this.lineBuf, cursor);
/* 99 */     return (HttpMessage)this.requestFactory.newHttpRequest(requestline);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\io\HttpRequestParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */