/*     */ package org.apache.http.impl;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.HttpResponseFactory;
/*     */ import org.apache.http.ProtocolVersion;
/*     */ import org.apache.http.ReasonPhraseCatalog;
/*     */ import org.apache.http.StatusLine;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.message.BasicHttpResponse;
/*     */ import org.apache.http.message.BasicStatusLine;
/*     */ import org.apache.http.protocol.HttpContext;
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
/*     */ @Immutable
/*     */ public class DefaultHttpResponseFactory
/*     */   implements HttpResponseFactory
/*     */ {
/*  51 */   public static final DefaultHttpResponseFactory INSTANCE = new DefaultHttpResponseFactory();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final ReasonPhraseCatalog reasonCatalog;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultHttpResponseFactory(ReasonPhraseCatalog catalog) {
/*  63 */     this.reasonCatalog = (ReasonPhraseCatalog)Args.notNull(catalog, "Reason phrase catalog");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultHttpResponseFactory() {
/*  71 */     this(EnglishReasonPhraseCatalog.INSTANCE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpResponse newHttpResponse(ProtocolVersion ver, int status, HttpContext context) {
/*  81 */     Args.notNull(ver, "HTTP version");
/*  82 */     Locale loc = determineLocale(context);
/*  83 */     String reason = this.reasonCatalog.getReason(status, loc);
/*  84 */     BasicStatusLine basicStatusLine = new BasicStatusLine(ver, status, reason);
/*  85 */     return (HttpResponse)new BasicHttpResponse((StatusLine)basicStatusLine, this.reasonCatalog, loc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpResponse newHttpResponse(StatusLine statusline, HttpContext context) {
/*  94 */     Args.notNull(statusline, "Status line");
/*  95 */     return (HttpResponse)new BasicHttpResponse(statusline, this.reasonCatalog, determineLocale(context));
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
/*     */   protected Locale determineLocale(HttpContext context) {
/* 108 */     return Locale.getDefault();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\DefaultHttpResponseFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */