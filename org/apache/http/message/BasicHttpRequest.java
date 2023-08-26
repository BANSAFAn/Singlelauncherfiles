/*     */ package org.apache.http.message;
/*     */ 
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpVersion;
/*     */ import org.apache.http.ProtocolVersion;
/*     */ import org.apache.http.RequestLine;
/*     */ import org.apache.http.annotation.NotThreadSafe;
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
/*     */ @NotThreadSafe
/*     */ public class BasicHttpRequest
/*     */   extends AbstractHttpMessage
/*     */   implements HttpRequest
/*     */ {
/*     */   private final String method;
/*     */   private final String uri;
/*     */   private RequestLine requestline;
/*     */   
/*     */   public BasicHttpRequest(String method, String uri) {
/*  59 */     this.method = (String)Args.notNull(method, "Method name");
/*  60 */     this.uri = (String)Args.notNull(uri, "Request URI");
/*  61 */     this.requestline = null;
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
/*     */   public BasicHttpRequest(String method, String uri, ProtocolVersion ver) {
/*  73 */     this(new BasicRequestLine(method, uri, ver));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicHttpRequest(RequestLine requestline) {
/*  83 */     this.requestline = (RequestLine)Args.notNull(requestline, "Request line");
/*  84 */     this.method = requestline.getMethod();
/*  85 */     this.uri = requestline.getUri();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProtocolVersion getProtocolVersion() {
/*  95 */     return getRequestLine().getProtocolVersion();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RequestLine getRequestLine() {
/* 105 */     if (this.requestline == null) {
/* 106 */       this.requestline = new BasicRequestLine(this.method, this.uri, (ProtocolVersion)HttpVersion.HTTP_1_1);
/*     */     }
/* 108 */     return this.requestline;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 113 */     return this.method + ' ' + this.uri + ' ' + this.headergroup;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\message\BasicHttpRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */