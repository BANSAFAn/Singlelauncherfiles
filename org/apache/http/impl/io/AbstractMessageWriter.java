/*     */ package org.apache.http.impl.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HeaderIterator;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpMessage;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.io.HttpMessageWriter;
/*     */ import org.apache.http.io.SessionOutputBuffer;
/*     */ import org.apache.http.message.BasicLineFormatter;
/*     */ import org.apache.http.message.LineFormatter;
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
/*     */ @NotThreadSafe
/*     */ public abstract class AbstractMessageWriter<T extends HttpMessage>
/*     */   implements HttpMessageWriter<T>
/*     */ {
/*     */   protected final SessionOutputBuffer sessionBuffer;
/*     */   protected final CharArrayBuffer lineBuf;
/*     */   protected final LineFormatter lineFormatter;
/*     */   
/*     */   @Deprecated
/*     */   public AbstractMessageWriter(SessionOutputBuffer buffer, LineFormatter formatter, HttpParams params) {
/*  74 */     Args.notNull(buffer, "Session input buffer");
/*  75 */     this.sessionBuffer = buffer;
/*  76 */     this.lineBuf = new CharArrayBuffer(128);
/*  77 */     this.lineFormatter = (formatter != null) ? formatter : (LineFormatter)BasicLineFormatter.INSTANCE;
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
/*     */   public AbstractMessageWriter(SessionOutputBuffer buffer, LineFormatter formatter) {
/*  93 */     this.sessionBuffer = (SessionOutputBuffer)Args.notNull(buffer, "Session input buffer");
/*  94 */     this.lineFormatter = (formatter != null) ? formatter : (LineFormatter)BasicLineFormatter.INSTANCE;
/*  95 */     this.lineBuf = new CharArrayBuffer(128);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void writeHeadLine(T paramT) throws IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(T message) throws IOException, HttpException {
/* 109 */     Args.notNull(message, "HTTP message");
/* 110 */     writeHeadLine(message);
/* 111 */     for (HeaderIterator it = message.headerIterator(); it.hasNext(); ) {
/* 112 */       Header header = it.nextHeader();
/* 113 */       this.sessionBuffer.writeLine(this.lineFormatter.formatHeader(this.lineBuf, header));
/*     */     } 
/*     */     
/* 116 */     this.lineBuf.clear();
/* 117 */     this.sessionBuffer.writeLine(this.lineBuf);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\io\AbstractMessageWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */