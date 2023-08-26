/*     */ package org.apache.http.client.methods;
/*     */ 
/*     */ import java.net.URI;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpEntityEnclosingRequest;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.ProtocolVersion;
/*     */ import org.apache.http.RequestLine;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.message.AbstractHttpMessage;
/*     */ import org.apache.http.message.BasicRequestLine;
/*     */ import org.apache.http.params.HttpParams;
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
/*     */ @NotThreadSafe
/*     */ public class HttpRequestWrapper
/*     */   extends AbstractHttpMessage
/*     */   implements HttpUriRequest
/*     */ {
/*     */   private final HttpRequest original;
/*     */   private final HttpHost target;
/*     */   private final String method;
/*     */   private RequestLine requestLine;
/*     */   private ProtocolVersion version;
/*     */   private URI uri;
/*     */   
/*     */   private HttpRequestWrapper(HttpRequest request, HttpHost target) {
/*  65 */     this.original = (HttpRequest)Args.notNull(request, "HTTP request");
/*  66 */     this.target = target;
/*  67 */     this.version = this.original.getRequestLine().getProtocolVersion();
/*  68 */     this.method = this.original.getRequestLine().getMethod();
/*  69 */     if (request instanceof HttpUriRequest) {
/*  70 */       this.uri = ((HttpUriRequest)request).getURI();
/*     */     } else {
/*  72 */       this.uri = null;
/*     */     } 
/*  74 */     setHeaders(request.getAllHeaders());
/*     */   }
/*     */ 
/*     */   
/*     */   public ProtocolVersion getProtocolVersion() {
/*  79 */     return (this.version != null) ? this.version : this.original.getProtocolVersion();
/*     */   }
/*     */   
/*     */   public void setProtocolVersion(ProtocolVersion version) {
/*  83 */     this.version = version;
/*  84 */     this.requestLine = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public URI getURI() {
/*  89 */     return this.uri;
/*     */   }
/*     */   
/*     */   public void setURI(URI uri) {
/*  93 */     this.uri = uri;
/*  94 */     this.requestLine = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getMethod() {
/*  99 */     return this.method;
/*     */   }
/*     */ 
/*     */   
/*     */   public void abort() throws UnsupportedOperationException {
/* 104 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAborted() {
/* 109 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public RequestLine getRequestLine() {
/* 114 */     if (this.requestLine == null) {
/*     */       String requestUri;
/* 116 */       if (this.uri != null) {
/* 117 */         requestUri = this.uri.toASCIIString();
/*     */       } else {
/* 119 */         requestUri = this.original.getRequestLine().getUri();
/*     */       } 
/* 121 */       if (requestUri == null || requestUri.isEmpty()) {
/* 122 */         requestUri = "/";
/*     */       }
/* 124 */       this.requestLine = (RequestLine)new BasicRequestLine(this.method, requestUri, getProtocolVersion());
/*     */     } 
/* 126 */     return this.requestLine;
/*     */   }
/*     */   
/*     */   public HttpRequest getOriginal() {
/* 130 */     return this.original;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpHost getTarget() {
/* 137 */     return this.target;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 142 */     return getRequestLine() + " " + this.headergroup;
/*     */   }
/*     */   
/*     */   static class HttpEntityEnclosingRequestWrapper
/*     */     extends HttpRequestWrapper
/*     */     implements HttpEntityEnclosingRequest {
/*     */     private HttpEntity entity;
/*     */     
/*     */     HttpEntityEnclosingRequestWrapper(HttpEntityEnclosingRequest request, HttpHost target) {
/* 151 */       super((HttpRequest)request, target);
/* 152 */       this.entity = request.getEntity();
/*     */     }
/*     */ 
/*     */     
/*     */     public HttpEntity getEntity() {
/* 157 */       return this.entity;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setEntity(HttpEntity entity) {
/* 162 */       this.entity = entity;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean expectContinue() {
/* 167 */       Header expect = getFirstHeader("Expect");
/* 168 */       return (expect != null && "100-continue".equalsIgnoreCase(expect.getValue()));
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
/*     */   public static HttpRequestWrapper wrap(HttpRequest request) {
/* 180 */     return wrap(request, (HttpHost)null);
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
/*     */   public static HttpRequestWrapper wrap(HttpRequest request, HttpHost target) {
/* 193 */     Args.notNull(request, "HTTP request");
/* 194 */     if (request instanceof HttpEntityEnclosingRequest) {
/* 195 */       return new HttpEntityEnclosingRequestWrapper((HttpEntityEnclosingRequest)request, target);
/*     */     }
/* 197 */     return new HttpRequestWrapper(request, target);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public HttpParams getParams() {
/* 208 */     if (this.params == null) {
/* 209 */       this.params = this.original.getParams().copy();
/*     */     }
/* 211 */     return this.params;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\client\methods\HttpRequestWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */