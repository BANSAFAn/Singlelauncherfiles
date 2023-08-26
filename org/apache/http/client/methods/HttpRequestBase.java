/*     */ package org.apache.http.client.methods;
/*     */ 
/*     */ import java.net.URI;
/*     */ import org.apache.http.ProtocolVersion;
/*     */ import org.apache.http.RequestLine;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.client.config.RequestConfig;
/*     */ import org.apache.http.message.BasicRequestLine;
/*     */ import org.apache.http.params.HttpProtocolParams;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class HttpRequestBase
/*     */   extends AbstractExecutionAwareRequest
/*     */   implements HttpUriRequest, Configurable
/*     */ {
/*     */   private ProtocolVersion version;
/*     */   private URI uri;
/*     */   private RequestConfig config;
/*     */   
/*     */   public abstract String getMethod();
/*     */   
/*     */   public void setProtocolVersion(ProtocolVersion version) {
/*  60 */     this.version = version;
/*     */   }
/*     */ 
/*     */   
/*     */   public ProtocolVersion getProtocolVersion() {
/*  65 */     return (this.version != null) ? this.version : HttpProtocolParams.getVersion(getParams());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URI getURI() {
/*  76 */     return this.uri;
/*     */   }
/*     */ 
/*     */   
/*     */   public RequestLine getRequestLine() {
/*  81 */     String method = getMethod();
/*  82 */     ProtocolVersion ver = getProtocolVersion();
/*  83 */     URI uriCopy = getURI();
/*  84 */     String uritext = null;
/*  85 */     if (uriCopy != null) {
/*  86 */       uritext = uriCopy.toASCIIString();
/*     */     }
/*  88 */     if (uritext == null || uritext.isEmpty()) {
/*  89 */       uritext = "/";
/*     */     }
/*  91 */     return (RequestLine)new BasicRequestLine(method, uritext, ver);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public RequestConfig getConfig() {
/*  97 */     return this.config;
/*     */   }
/*     */   
/*     */   public void setConfig(RequestConfig config) {
/* 101 */     this.config = config;
/*     */   }
/*     */   
/*     */   public void setURI(URI uri) {
/* 105 */     this.uri = uri;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void started() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void releaseConnection() {
/* 121 */     reset();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 126 */     return getMethod() + " " + getURI() + " " + getProtocolVersion();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\client\methods\HttpRequestBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */