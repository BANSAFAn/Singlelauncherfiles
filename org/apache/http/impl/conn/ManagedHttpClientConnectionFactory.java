/*     */ package org.apache.http.impl.conn;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.CodingErrorAction;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.HttpConnection;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.config.ConnectionConfig;
/*     */ import org.apache.http.conn.HttpConnectionFactory;
/*     */ import org.apache.http.conn.ManagedHttpClientConnection;
/*     */ import org.apache.http.conn.routing.HttpRoute;
/*     */ import org.apache.http.entity.ContentLengthStrategy;
/*     */ import org.apache.http.impl.entity.LaxContentLengthStrategy;
/*     */ import org.apache.http.impl.entity.StrictContentLengthStrategy;
/*     */ import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
/*     */ import org.apache.http.io.HttpMessageParserFactory;
/*     */ import org.apache.http.io.HttpMessageWriterFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class ManagedHttpClientConnectionFactory
/*     */   implements HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection>
/*     */ {
/*  60 */   private static final AtomicLong COUNTER = new AtomicLong();
/*     */   
/*  62 */   public static final ManagedHttpClientConnectionFactory INSTANCE = new ManagedHttpClientConnectionFactory();
/*     */   
/*  64 */   private final Log log = LogFactory.getLog(DefaultManagedHttpClientConnection.class);
/*  65 */   private final Log headerlog = LogFactory.getLog("org.apache.http.headers");
/*  66 */   private final Log wirelog = LogFactory.getLog("org.apache.http.wire");
/*     */ 
/*     */   
/*     */   private final HttpMessageWriterFactory<HttpRequest> requestWriterFactory;
/*     */ 
/*     */   
/*     */   private final HttpMessageParserFactory<HttpResponse> responseParserFactory;
/*     */ 
/*     */   
/*     */   private final ContentLengthStrategy incomingContentStrategy;
/*     */ 
/*     */   
/*     */   private final ContentLengthStrategy outgoingContentStrategy;
/*     */ 
/*     */   
/*     */   public ManagedHttpClientConnectionFactory(HttpMessageWriterFactory<HttpRequest> requestWriterFactory, HttpMessageParserFactory<HttpResponse> responseParserFactory, ContentLengthStrategy incomingContentStrategy, ContentLengthStrategy outgoingContentStrategy) {
/*  82 */     this.requestWriterFactory = (requestWriterFactory != null) ? requestWriterFactory : (HttpMessageWriterFactory<HttpRequest>)DefaultHttpRequestWriterFactory.INSTANCE;
/*     */     
/*  84 */     this.responseParserFactory = (responseParserFactory != null) ? responseParserFactory : DefaultHttpResponseParserFactory.INSTANCE;
/*     */     
/*  86 */     this.incomingContentStrategy = (incomingContentStrategy != null) ? incomingContentStrategy : (ContentLengthStrategy)LaxContentLengthStrategy.INSTANCE;
/*     */     
/*  88 */     this.outgoingContentStrategy = (outgoingContentStrategy != null) ? outgoingContentStrategy : (ContentLengthStrategy)StrictContentLengthStrategy.INSTANCE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ManagedHttpClientConnectionFactory(HttpMessageWriterFactory<HttpRequest> requestWriterFactory, HttpMessageParserFactory<HttpResponse> responseParserFactory) {
/*  95 */     this(requestWriterFactory, responseParserFactory, null, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public ManagedHttpClientConnectionFactory(HttpMessageParserFactory<HttpResponse> responseParserFactory) {
/* 100 */     this(null, responseParserFactory);
/*     */   }
/*     */   
/*     */   public ManagedHttpClientConnectionFactory() {
/* 104 */     this(null, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public ManagedHttpClientConnection create(HttpRoute route, ConnectionConfig config) {
/* 109 */     ConnectionConfig cconfig = (config != null) ? config : ConnectionConfig.DEFAULT;
/* 110 */     CharsetDecoder chardecoder = null;
/* 111 */     CharsetEncoder charencoder = null;
/* 112 */     Charset charset = cconfig.getCharset();
/* 113 */     CodingErrorAction malformedInputAction = (cconfig.getMalformedInputAction() != null) ? cconfig.getMalformedInputAction() : CodingErrorAction.REPORT;
/*     */     
/* 115 */     CodingErrorAction unmappableInputAction = (cconfig.getUnmappableInputAction() != null) ? cconfig.getUnmappableInputAction() : CodingErrorAction.REPORT;
/*     */     
/* 117 */     if (charset != null) {
/* 118 */       chardecoder = charset.newDecoder();
/* 119 */       chardecoder.onMalformedInput(malformedInputAction);
/* 120 */       chardecoder.onUnmappableCharacter(unmappableInputAction);
/* 121 */       charencoder = charset.newEncoder();
/* 122 */       charencoder.onMalformedInput(malformedInputAction);
/* 123 */       charencoder.onUnmappableCharacter(unmappableInputAction);
/*     */     } 
/* 125 */     String id = "http-outgoing-" + Long.toString(COUNTER.getAndIncrement());
/* 126 */     return new LoggingManagedHttpClientConnection(id, this.log, this.headerlog, this.wirelog, cconfig.getBufferSize(), cconfig.getFragmentSizeHint(), chardecoder, charencoder, cconfig.getMessageConstraints(), this.incomingContentStrategy, this.outgoingContentStrategy, this.requestWriterFactory, this.responseParserFactory);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\conn\ManagedHttpClientConnectionFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */