/*     */ package org.apache.http.message;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.HttpVersion;
/*     */ import org.apache.http.ProtocolVersion;
/*     */ import org.apache.http.ReasonPhraseCatalog;
/*     */ import org.apache.http.StatusLine;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @NotThreadSafe
/*     */ public class BasicHttpResponse
/*     */   extends AbstractHttpMessage
/*     */   implements HttpResponse
/*     */ {
/*     */   private StatusLine statusline;
/*     */   private ProtocolVersion ver;
/*     */   private int code;
/*     */   private String reasonPhrase;
/*     */   private HttpEntity entity;
/*     */   private final ReasonPhraseCatalog reasonCatalog;
/*     */   private Locale locale;
/*     */   
/*     */   public BasicHttpResponse(StatusLine statusline, ReasonPhraseCatalog catalog, Locale locale) {
/*  74 */     this.statusline = (StatusLine)Args.notNull(statusline, "Status line");
/*  75 */     this.ver = statusline.getProtocolVersion();
/*  76 */     this.code = statusline.getStatusCode();
/*  77 */     this.reasonPhrase = statusline.getReasonPhrase();
/*  78 */     this.reasonCatalog = catalog;
/*  79 */     this.locale = locale;
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
/*     */   public BasicHttpResponse(StatusLine statusline) {
/*  91 */     this.statusline = (StatusLine)Args.notNull(statusline, "Status line");
/*  92 */     this.ver = statusline.getProtocolVersion();
/*  93 */     this.code = statusline.getStatusCode();
/*  94 */     this.reasonPhrase = statusline.getReasonPhrase();
/*  95 */     this.reasonCatalog = null;
/*  96 */     this.locale = null;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicHttpResponse(ProtocolVersion ver, int code, String reason) {
/* 113 */     Args.notNegative(code, "Status code");
/* 114 */     this.statusline = null;
/* 115 */     this.ver = ver;
/* 116 */     this.code = code;
/* 117 */     this.reasonPhrase = reason;
/* 118 */     this.reasonCatalog = null;
/* 119 */     this.locale = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProtocolVersion getProtocolVersion() {
/* 126 */     return this.ver;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public StatusLine getStatusLine() {
/* 132 */     if (this.statusline == null) {
/* 133 */       this.statusline = new BasicStatusLine((this.ver != null) ? this.ver : (ProtocolVersion)HttpVersion.HTTP_1_1, this.code, (this.reasonPhrase != null) ? this.reasonPhrase : getReason(this.code));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 138 */     return this.statusline;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpEntity getEntity() {
/* 144 */     return this.entity;
/*     */   }
/*     */ 
/*     */   
/*     */   public Locale getLocale() {
/* 149 */     return this.locale;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStatusLine(StatusLine statusline) {
/* 155 */     this.statusline = (StatusLine)Args.notNull(statusline, "Status line");
/* 156 */     this.ver = statusline.getProtocolVersion();
/* 157 */     this.code = statusline.getStatusCode();
/* 158 */     this.reasonPhrase = statusline.getReasonPhrase();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStatusLine(ProtocolVersion ver, int code) {
/* 164 */     Args.notNegative(code, "Status code");
/* 165 */     this.statusline = null;
/* 166 */     this.ver = ver;
/* 167 */     this.code = code;
/* 168 */     this.reasonPhrase = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStatusLine(ProtocolVersion ver, int code, String reason) {
/* 175 */     Args.notNegative(code, "Status code");
/* 176 */     this.statusline = null;
/* 177 */     this.ver = ver;
/* 178 */     this.code = code;
/* 179 */     this.reasonPhrase = reason;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStatusCode(int code) {
/* 185 */     Args.notNegative(code, "Status code");
/* 186 */     this.statusline = null;
/* 187 */     this.code = code;
/* 188 */     this.reasonPhrase = null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReasonPhrase(String reason) {
/* 194 */     this.statusline = null;
/* 195 */     this.reasonPhrase = reason;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEntity(HttpEntity entity) {
/* 201 */     this.entity = entity;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLocale(Locale locale) {
/* 206 */     this.locale = (Locale)Args.notNull(locale, "Locale");
/* 207 */     this.statusline = null;
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
/*     */   protected String getReason(int code) {
/* 220 */     return (this.reasonCatalog != null) ? this.reasonCatalog.getReason(code, (this.locale != null) ? this.locale : Locale.getDefault()) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 226 */     StringBuilder sb = new StringBuilder();
/* 227 */     sb.append(getStatusLine());
/* 228 */     sb.append(' ');
/* 229 */     sb.append(this.headergroup);
/* 230 */     if (this.entity != null) {
/* 231 */       sb.append(' ');
/* 232 */       sb.append(this.entity);
/*     */     } 
/* 234 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\message\BasicHttpResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */