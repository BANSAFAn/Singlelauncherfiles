/*     */ package org.apache.http.impl.execchain;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Locale;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HeaderIterator;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.ProtocolVersion;
/*     */ import org.apache.http.StatusLine;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.client.methods.CloseableHttpResponse;
/*     */ import org.apache.http.params.HttpParams;
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
/*     */ class HttpResponseProxy
/*     */   implements CloseableHttpResponse
/*     */ {
/*     */   private final HttpResponse original;
/*     */   private final ConnectionHolder connHolder;
/*     */   
/*     */   public HttpResponseProxy(HttpResponse original, ConnectionHolder connHolder) {
/*  56 */     this.original = original;
/*  57 */     this.connHolder = connHolder;
/*  58 */     ResponseEntityProxy.enchance(original, connHolder);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*  63 */     if (this.connHolder != null) {
/*  64 */       this.connHolder.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public StatusLine getStatusLine() {
/*  70 */     return this.original.getStatusLine();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setStatusLine(StatusLine statusline) {
/*  75 */     this.original.setStatusLine(statusline);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setStatusLine(ProtocolVersion ver, int code) {
/*  80 */     this.original.setStatusLine(ver, code);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setStatusLine(ProtocolVersion ver, int code, String reason) {
/*  85 */     this.original.setStatusLine(ver, code, reason);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setStatusCode(int code) throws IllegalStateException {
/*  90 */     this.original.setStatusCode(code);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setReasonPhrase(String reason) throws IllegalStateException {
/*  95 */     this.original.setReasonPhrase(reason);
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpEntity getEntity() {
/* 100 */     return this.original.getEntity();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEntity(HttpEntity entity) {
/* 105 */     this.original.setEntity(entity);
/*     */   }
/*     */ 
/*     */   
/*     */   public Locale getLocale() {
/* 110 */     return this.original.getLocale();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLocale(Locale loc) {
/* 115 */     this.original.setLocale(loc);
/*     */   }
/*     */ 
/*     */   
/*     */   public ProtocolVersion getProtocolVersion() {
/* 120 */     return this.original.getProtocolVersion();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsHeader(String name) {
/* 125 */     return this.original.containsHeader(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public Header[] getHeaders(String name) {
/* 130 */     return this.original.getHeaders(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public Header getFirstHeader(String name) {
/* 135 */     return this.original.getFirstHeader(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public Header getLastHeader(String name) {
/* 140 */     return this.original.getLastHeader(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public Header[] getAllHeaders() {
/* 145 */     return this.original.getAllHeaders();
/*     */   }
/*     */ 
/*     */   
/*     */   public void addHeader(Header header) {
/* 150 */     this.original.addHeader(header);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addHeader(String name, String value) {
/* 155 */     this.original.addHeader(name, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setHeader(Header header) {
/* 160 */     this.original.setHeader(header);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setHeader(String name, String value) {
/* 165 */     this.original.setHeader(name, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setHeaders(Header[] headers) {
/* 170 */     this.original.setHeaders(headers);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeHeader(Header header) {
/* 175 */     this.original.removeHeader(header);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeHeaders(String name) {
/* 180 */     this.original.removeHeaders(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public HeaderIterator headerIterator() {
/* 185 */     return this.original.headerIterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public HeaderIterator headerIterator(String name) {
/* 190 */     return this.original.headerIterator(name);
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public HttpParams getParams() {
/* 196 */     return this.original.getParams();
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setParams(HttpParams params) {
/* 202 */     this.original.setParams(params);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 207 */     StringBuilder sb = new StringBuilder("HttpResponseProxy{");
/* 208 */     sb.append(this.original);
/* 209 */     sb.append('}');
/* 210 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\execchain\HttpResponseProxy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */