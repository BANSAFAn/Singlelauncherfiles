/*     */ package org.apache.http.protocol;
/*     */ 
/*     */ import org.apache.http.HttpConnection;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
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
/*     */ public class HttpCoreContext
/*     */   implements HttpContext
/*     */ {
/*     */   public static final String HTTP_CONNECTION = "http.connection";
/*     */   public static final String HTTP_REQUEST = "http.request";
/*     */   public static final String HTTP_RESPONSE = "http.response";
/*     */   public static final String HTTP_TARGET_HOST = "http.target_host";
/*     */   public static final String HTTP_REQ_SENT = "http.request_sent";
/*     */   private final HttpContext context;
/*     */   
/*     */   public static HttpCoreContext create() {
/*  78 */     return new HttpCoreContext(new BasicHttpContext());
/*     */   }
/*     */   
/*     */   public static HttpCoreContext adapt(HttpContext context) {
/*  82 */     Args.notNull(context, "HTTP context");
/*  83 */     if (context instanceof HttpCoreContext) {
/*  84 */       return (HttpCoreContext)context;
/*     */     }
/*  86 */     return new HttpCoreContext(context);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpCoreContext(HttpContext context) {
/*  94 */     this.context = context;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpCoreContext() {
/*  99 */     this.context = new BasicHttpContext();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getAttribute(String id) {
/* 104 */     return this.context.getAttribute(id);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAttribute(String id, Object obj) {
/* 109 */     this.context.setAttribute(id, obj);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object removeAttribute(String id) {
/* 114 */     return this.context.removeAttribute(id);
/*     */   }
/*     */   
/*     */   public <T> T getAttribute(String attribname, Class<T> clazz) {
/* 118 */     Args.notNull(clazz, "Attribute class");
/* 119 */     Object obj = getAttribute(attribname);
/* 120 */     if (obj == null) {
/* 121 */       return null;
/*     */     }
/* 123 */     return clazz.cast(obj);
/*     */   }
/*     */   
/*     */   public <T extends HttpConnection> T getConnection(Class<T> clazz) {
/* 127 */     return (T)getAttribute("http.connection", clazz);
/*     */   }
/*     */   
/*     */   public HttpConnection getConnection() {
/* 131 */     return getAttribute("http.connection", HttpConnection.class);
/*     */   }
/*     */   
/*     */   public HttpRequest getRequest() {
/* 135 */     return getAttribute("http.request", HttpRequest.class);
/*     */   }
/*     */   
/*     */   public boolean isRequestSent() {
/* 139 */     Boolean b = getAttribute("http.request_sent", Boolean.class);
/* 140 */     return (b != null && b.booleanValue());
/*     */   }
/*     */   
/*     */   public HttpResponse getResponse() {
/* 144 */     return getAttribute("http.response", HttpResponse.class);
/*     */   }
/*     */   
/*     */   public void setTargetHost(HttpHost host) {
/* 148 */     setAttribute("http.target_host", host);
/*     */   }
/*     */   
/*     */   public HttpHost getTargetHost() {
/* 152 */     return getAttribute("http.target_host", HttpHost.class);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\protocol\HttpCoreContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */