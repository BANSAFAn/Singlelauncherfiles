/*    */ package org.apache.http.impl.io;
/*    */ 
/*    */ import org.apache.http.HttpRequest;
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.io.HttpMessageWriter;
/*    */ import org.apache.http.io.HttpMessageWriterFactory;
/*    */ import org.apache.http.io.SessionOutputBuffer;
/*    */ import org.apache.http.message.BasicLineFormatter;
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
/*    */ @Immutable
/*    */ public class DefaultHttpRequestWriterFactory
/*    */   implements HttpMessageWriterFactory<HttpRequest>
/*    */ {
/* 46 */   public static final DefaultHttpRequestWriterFactory INSTANCE = new DefaultHttpRequestWriterFactory();
/*    */   
/*    */   private final LineFormatter lineFormatter;
/*    */ 
/*    */   
/*    */   public DefaultHttpRequestWriterFactory(LineFormatter lineFormatter) {
/* 52 */     this.lineFormatter = (lineFormatter != null) ? lineFormatter : (LineFormatter)BasicLineFormatter.INSTANCE;
/*    */   }
/*    */   
/*    */   public DefaultHttpRequestWriterFactory() {
/* 56 */     this(null);
/*    */   }
/*    */ 
/*    */   
/*    */   public HttpMessageWriter<HttpRequest> create(SessionOutputBuffer buffer) {
/* 61 */     return new DefaultHttpRequestWriter(buffer, this.lineFormatter);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\io\DefaultHttpRequestWriterFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */