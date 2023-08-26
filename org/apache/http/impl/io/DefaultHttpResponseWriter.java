/*    */ package org.apache.http.impl.io;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.http.HttpMessage;
/*    */ import org.apache.http.HttpResponse;
/*    */ import org.apache.http.annotation.NotThreadSafe;
/*    */ import org.apache.http.io.SessionOutputBuffer;
/*    */ import org.apache.http.message.LineFormatter;
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
/*    */ @NotThreadSafe
/*    */ public class DefaultHttpResponseWriter
/*    */   extends AbstractMessageWriter<HttpResponse>
/*    */ {
/*    */   public DefaultHttpResponseWriter(SessionOutputBuffer buffer, LineFormatter formatter) {
/* 56 */     super(buffer, formatter);
/*    */   }
/*    */   
/*    */   public DefaultHttpResponseWriter(SessionOutputBuffer buffer) {
/* 60 */     super(buffer, null);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void writeHeadLine(HttpResponse message) throws IOException {
/* 65 */     this.lineFormatter.formatStatusLine(this.lineBuf, message.getStatusLine());
/* 66 */     this.sessionBuffer.writeLine(this.lineBuf);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\io\DefaultHttpResponseWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */