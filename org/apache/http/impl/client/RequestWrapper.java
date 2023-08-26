/*     */ package org.apache.http.impl.client;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.ProtocolException;
/*     */ import org.apache.http.ProtocolVersion;
/*     */ import org.apache.http.RequestLine;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.client.methods.HttpUriRequest;
/*     */ import org.apache.http.message.AbstractHttpMessage;
/*     */ import org.apache.http.message.BasicRequestLine;
/*     */ import org.apache.http.params.HttpProtocolParams;
/*     */ import org.apache.http.util.Args;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @NotThreadSafe
/*     */ public class RequestWrapper
/*     */   extends AbstractHttpMessage
/*     */   implements HttpUriRequest
/*     */ {
/*     */   private final HttpRequest original;
/*     */   private URI uri;
/*     */   private String method;
/*     */   private ProtocolVersion version;
/*     */   private int execCount;
/*     */   
/*     */   public RequestWrapper(HttpRequest request) throws ProtocolException {
/*  70 */     Args.notNull(request, "HTTP request");
/*  71 */     this.original = request;
/*  72 */     setParams(request.getParams());
/*  73 */     setHeaders(request.getAllHeaders());
/*     */     
/*  75 */     if (request instanceof HttpUriRequest) {
/*  76 */       this.uri = ((HttpUriRequest)request).getURI();
/*  77 */       this.method = ((HttpUriRequest)request).getMethod();
/*  78 */       this.version = null;
/*     */     } else {
/*  80 */       RequestLine requestLine = request.getRequestLine();
/*     */       try {
/*  82 */         this.uri = new URI(requestLine.getUri());
/*  83 */       } catch (URISyntaxException ex) {
/*  84 */         throw new ProtocolException("Invalid request URI: " + requestLine.getUri(), ex);
/*     */       } 
/*     */       
/*  87 */       this.method = requestLine.getMethod();
/*  88 */       this.version = request.getProtocolVersion();
/*     */     } 
/*  90 */     this.execCount = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetHeaders() {
/*  95 */     this.headergroup.clear();
/*  96 */     setHeaders(this.original.getAllHeaders());
/*     */   }
/*     */ 
/*     */   
/*     */   public String getMethod() {
/* 101 */     return this.method;
/*     */   }
/*     */   
/*     */   public void setMethod(String method) {
/* 105 */     Args.notNull(method, "Method name");
/* 106 */     this.method = method;
/*     */   }
/*     */ 
/*     */   
/*     */   public ProtocolVersion getProtocolVersion() {
/* 111 */     if (this.version == null) {
/* 112 */       this.version = HttpProtocolParams.getVersion(getParams());
/*     */     }
/* 114 */     return this.version;
/*     */   }
/*     */   
/*     */   public void setProtocolVersion(ProtocolVersion version) {
/* 118 */     this.version = version;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public URI getURI() {
/* 124 */     return this.uri;
/*     */   }
/*     */   
/*     */   public void setURI(URI uri) {
/* 128 */     this.uri = uri;
/*     */   }
/*     */ 
/*     */   
/*     */   public RequestLine getRequestLine() {
/* 133 */     ProtocolVersion ver = getProtocolVersion();
/* 134 */     String uritext = null;
/* 135 */     if (this.uri != null) {
/* 136 */       uritext = this.uri.toASCIIString();
/*     */     }
/* 138 */     if (uritext == null || uritext.isEmpty()) {
/* 139 */       uritext = "/";
/*     */     }
/* 141 */     return (RequestLine)new BasicRequestLine(getMethod(), uritext, ver);
/*     */   }
/*     */ 
/*     */   
/*     */   public void abort() throws UnsupportedOperationException {
/* 146 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAborted() {
/* 151 */     return false;
/*     */   }
/*     */   
/*     */   public HttpRequest getOriginal() {
/* 155 */     return this.original;
/*     */   }
/*     */   
/*     */   public boolean isRepeatable() {
/* 159 */     return true;
/*     */   }
/*     */   
/*     */   public int getExecCount() {
/* 163 */     return this.execCount;
/*     */   }
/*     */   
/*     */   public void incrementExecCount() {
/* 167 */     this.execCount++;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\client\RequestWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */