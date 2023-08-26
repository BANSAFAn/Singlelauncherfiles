/*     */ package org.apache.http.impl.conn;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.net.Socket;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import javax.net.ssl.SSLSocket;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.config.MessageConstraints;
/*     */ import org.apache.http.conn.ManagedHttpClientConnection;
/*     */ import org.apache.http.entity.ContentLengthStrategy;
/*     */ import org.apache.http.impl.DefaultBHttpClientConnection;
/*     */ import org.apache.http.io.HttpMessageParserFactory;
/*     */ import org.apache.http.io.HttpMessageWriterFactory;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class DefaultManagedHttpClientConnection
/*     */   extends DefaultBHttpClientConnection
/*     */   implements ManagedHttpClientConnection, HttpContext
/*     */ {
/*     */   private final String id;
/*     */   private final Map<String, Object> attributes;
/*     */   private volatile boolean shutdown;
/*     */   
/*     */   public DefaultManagedHttpClientConnection(String id, int buffersize, int fragmentSizeHint, CharsetDecoder chardecoder, CharsetEncoder charencoder, MessageConstraints constraints, ContentLengthStrategy incomingContentStrategy, ContentLengthStrategy outgoingContentStrategy, HttpMessageWriterFactory<HttpRequest> requestWriterFactory, HttpMessageParserFactory<HttpResponse> responseParserFactory) {
/*  76 */     super(buffersize, fragmentSizeHint, chardecoder, charencoder, constraints, incomingContentStrategy, outgoingContentStrategy, requestWriterFactory, responseParserFactory);
/*     */ 
/*     */     
/*  79 */     this.id = id;
/*  80 */     this.attributes = new ConcurrentHashMap<String, Object>();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultManagedHttpClientConnection(String id, int buffersize) {
/*  86 */     this(id, buffersize, buffersize, null, null, null, null, null, null, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getId() {
/*  91 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   public void shutdown() throws IOException {
/*  96 */     this.shutdown = true;
/*  97 */     super.shutdown();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getAttribute(String id) {
/* 102 */     return this.attributes.get(id);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object removeAttribute(String id) {
/* 107 */     return this.attributes.remove(id);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAttribute(String id, Object obj) {
/* 112 */     this.attributes.put(id, obj);
/*     */   }
/*     */ 
/*     */   
/*     */   public void bind(Socket socket) throws IOException {
/* 117 */     if (this.shutdown) {
/* 118 */       socket.close();
/*     */       
/* 120 */       throw new InterruptedIOException("Connection already shutdown");
/*     */     } 
/* 122 */     super.bind(socket);
/*     */   }
/*     */ 
/*     */   
/*     */   public Socket getSocket() {
/* 127 */     return super.getSocket();
/*     */   }
/*     */ 
/*     */   
/*     */   public SSLSession getSSLSession() {
/* 132 */     Socket socket = super.getSocket();
/* 133 */     if (socket instanceof SSLSocket) {
/* 134 */       return ((SSLSocket)socket).getSession();
/*     */     }
/* 136 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\conn\DefaultManagedHttpClientConnection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */