/*     */ package org.apache.http.message;
/*     */ 
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HeaderIterator;
/*     */ import org.apache.http.HttpMessage;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.params.BasicHttpParams;
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
/*     */ 
/*     */ 
/*     */ @NotThreadSafe
/*     */ public abstract class AbstractHttpMessage
/*     */   implements HttpMessage
/*     */ {
/*     */   protected HeaderGroup headergroup;
/*     */   @Deprecated
/*     */   protected HttpParams params;
/*     */   
/*     */   @Deprecated
/*     */   protected AbstractHttpMessage(HttpParams params) {
/*  58 */     this.headergroup = new HeaderGroup();
/*  59 */     this.params = params;
/*     */   }
/*     */   
/*     */   protected AbstractHttpMessage() {
/*  63 */     this(null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsHeader(String name) {
/*  69 */     return this.headergroup.containsHeader(name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Header[] getHeaders(String name) {
/*  75 */     return this.headergroup.getHeaders(name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Header getFirstHeader(String name) {
/*  81 */     return this.headergroup.getFirstHeader(name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Header getLastHeader(String name) {
/*  87 */     return this.headergroup.getLastHeader(name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Header[] getAllHeaders() {
/*  93 */     return this.headergroup.getAllHeaders();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addHeader(Header header) {
/*  99 */     this.headergroup.addHeader(header);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addHeader(String name, String value) {
/* 105 */     Args.notNull(name, "Header name");
/* 106 */     this.headergroup.addHeader(new BasicHeader(name, value));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHeader(Header header) {
/* 112 */     this.headergroup.updateHeader(header);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHeader(String name, String value) {
/* 118 */     Args.notNull(name, "Header name");
/* 119 */     this.headergroup.updateHeader(new BasicHeader(name, value));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHeaders(Header[] headers) {
/* 125 */     this.headergroup.setHeaders(headers);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeHeader(Header header) {
/* 131 */     this.headergroup.removeHeader(header);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeHeaders(String name) {
/* 137 */     if (name == null) {
/*     */       return;
/*     */     }
/* 140 */     for (HeaderIterator i = this.headergroup.iterator(); i.hasNext(); ) {
/* 141 */       Header header = i.nextHeader();
/* 142 */       if (name.equalsIgnoreCase(header.getName())) {
/* 143 */         i.remove();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public HeaderIterator headerIterator() {
/* 151 */     return this.headergroup.iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public HeaderIterator headerIterator(String name) {
/* 157 */     return this.headergroup.iterator(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public HttpParams getParams() {
/* 166 */     if (this.params == null) {
/* 167 */       this.params = (HttpParams)new BasicHttpParams();
/*     */     }
/* 169 */     return this.params;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setParams(HttpParams params) {
/* 178 */     this.params = (HttpParams)Args.notNull(params, "HTTP parameters");
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\message\AbstractHttpMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */