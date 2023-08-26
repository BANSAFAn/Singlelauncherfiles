/*     */ package org.apache.http.client.methods;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import org.apache.http.Consts;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HeaderIterator;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpEntityEnclosingRequest;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.NameValuePair;
/*     */ import org.apache.http.ProtocolVersion;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.client.config.RequestConfig;
/*     */ import org.apache.http.client.entity.UrlEncodedFormEntity;
/*     */ import org.apache.http.client.utils.URIBuilder;
/*     */ import org.apache.http.client.utils.URLEncodedUtils;
/*     */ import org.apache.http.entity.ContentType;
/*     */ import org.apache.http.message.BasicHeader;
/*     */ import org.apache.http.message.BasicNameValuePair;
/*     */ import org.apache.http.message.HeaderGroup;
/*     */ import org.apache.http.protocol.HTTP;
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
/*     */ @NotThreadSafe
/*     */ public class RequestBuilder
/*     */ {
/*     */   private String method;
/*     */   private Charset charset;
/*     */   private ProtocolVersion version;
/*     */   private URI uri;
/*     */   private HeaderGroup headergroup;
/*     */   private HttpEntity entity;
/*     */   private List<NameValuePair> parameters;
/*     */   private RequestConfig config;
/*     */   
/*     */   RequestBuilder(String method) {
/*  84 */     this.charset = Consts.UTF_8;
/*  85 */     this.method = method;
/*     */   }
/*     */ 
/*     */   
/*     */   RequestBuilder(String method, URI uri) {
/*  90 */     this.method = method;
/*  91 */     this.uri = uri;
/*     */   }
/*     */ 
/*     */   
/*     */   RequestBuilder(String method, String uri) {
/*  96 */     this.method = method;
/*  97 */     this.uri = (uri != null) ? URI.create(uri) : null;
/*     */   }
/*     */   
/*     */   RequestBuilder() {
/* 101 */     this(null);
/*     */   }
/*     */   
/*     */   public static RequestBuilder create(String method) {
/* 105 */     Args.notBlank(method, "HTTP method");
/* 106 */     return new RequestBuilder(method);
/*     */   }
/*     */   
/*     */   public static RequestBuilder get() {
/* 110 */     return new RequestBuilder("GET");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RequestBuilder get(URI uri) {
/* 117 */     return new RequestBuilder("GET", uri);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RequestBuilder get(String uri) {
/* 124 */     return new RequestBuilder("GET", uri);
/*     */   }
/*     */   
/*     */   public static RequestBuilder head() {
/* 128 */     return new RequestBuilder("HEAD");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RequestBuilder head(URI uri) {
/* 135 */     return new RequestBuilder("HEAD", uri);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RequestBuilder head(String uri) {
/* 142 */     return new RequestBuilder("HEAD", uri);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RequestBuilder patch() {
/* 149 */     return new RequestBuilder("PATCH");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RequestBuilder patch(URI uri) {
/* 156 */     return new RequestBuilder("PATCH", uri);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RequestBuilder patch(String uri) {
/* 163 */     return new RequestBuilder("PATCH", uri);
/*     */   }
/*     */   
/*     */   public static RequestBuilder post() {
/* 167 */     return new RequestBuilder("POST");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RequestBuilder post(URI uri) {
/* 174 */     return new RequestBuilder("POST", uri);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RequestBuilder post(String uri) {
/* 181 */     return new RequestBuilder("POST", uri);
/*     */   }
/*     */   
/*     */   public static RequestBuilder put() {
/* 185 */     return new RequestBuilder("PUT");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RequestBuilder put(URI uri) {
/* 192 */     return new RequestBuilder("PUT", uri);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RequestBuilder put(String uri) {
/* 199 */     return new RequestBuilder("PUT", uri);
/*     */   }
/*     */   
/*     */   public static RequestBuilder delete() {
/* 203 */     return new RequestBuilder("DELETE");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RequestBuilder delete(URI uri) {
/* 210 */     return new RequestBuilder("DELETE", uri);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RequestBuilder delete(String uri) {
/* 217 */     return new RequestBuilder("DELETE", uri);
/*     */   }
/*     */   
/*     */   public static RequestBuilder trace() {
/* 221 */     return new RequestBuilder("TRACE");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RequestBuilder trace(URI uri) {
/* 228 */     return new RequestBuilder("TRACE", uri);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RequestBuilder trace(String uri) {
/* 235 */     return new RequestBuilder("TRACE", uri);
/*     */   }
/*     */   
/*     */   public static RequestBuilder options() {
/* 239 */     return new RequestBuilder("OPTIONS");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RequestBuilder options(URI uri) {
/* 246 */     return new RequestBuilder("OPTIONS", uri);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RequestBuilder options(String uri) {
/* 253 */     return new RequestBuilder("OPTIONS", uri);
/*     */   }
/*     */   
/*     */   public static RequestBuilder copy(HttpRequest request) {
/* 257 */     Args.notNull(request, "HTTP request");
/* 258 */     return (new RequestBuilder()).doCopy(request);
/*     */   }
/*     */   
/*     */   private RequestBuilder doCopy(HttpRequest request) {
/* 262 */     if (request == null) {
/* 263 */       return this;
/*     */     }
/* 265 */     this.method = request.getRequestLine().getMethod();
/* 266 */     this.version = request.getRequestLine().getProtocolVersion();
/*     */     
/* 268 */     if (this.headergroup == null) {
/* 269 */       this.headergroup = new HeaderGroup();
/*     */     }
/* 271 */     this.headergroup.clear();
/* 272 */     this.headergroup.setHeaders(request.getAllHeaders());
/*     */     
/* 274 */     this.parameters = null;
/* 275 */     this.entity = null;
/*     */     
/* 277 */     if (request instanceof HttpEntityEnclosingRequest) {
/* 278 */       HttpEntity originalEntity = ((HttpEntityEnclosingRequest)request).getEntity();
/* 279 */       ContentType contentType = ContentType.get(originalEntity);
/* 280 */       if (contentType != null && contentType.getMimeType().equals(ContentType.APPLICATION_FORM_URLENCODED.getMimeType())) {
/*     */         
/*     */         try {
/* 283 */           List<NameValuePair> formParams = URLEncodedUtils.parse(originalEntity);
/* 284 */           if (!formParams.isEmpty()) {
/* 285 */             this.parameters = formParams;
/*     */           }
/* 287 */         } catch (IOException ignore) {}
/*     */       } else {
/*     */         
/* 290 */         this.entity = originalEntity;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 295 */     if (request instanceof HttpUriRequest) {
/* 296 */       this.uri = ((HttpUriRequest)request).getURI();
/*     */     } else {
/* 298 */       this.uri = URI.create(request.getRequestLine().getUri());
/*     */     } 
/*     */     
/* 301 */     if (request instanceof Configurable) {
/* 302 */       this.config = ((Configurable)request).getConfig();
/*     */     } else {
/* 304 */       this.config = null;
/*     */     } 
/* 306 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RequestBuilder setCharset(Charset charset) {
/* 313 */     this.charset = charset;
/* 314 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Charset getCharset() {
/* 321 */     return this.charset;
/*     */   }
/*     */   
/*     */   public String getMethod() {
/* 325 */     return this.method;
/*     */   }
/*     */   
/*     */   public ProtocolVersion getVersion() {
/* 329 */     return this.version;
/*     */   }
/*     */   
/*     */   public RequestBuilder setVersion(ProtocolVersion version) {
/* 333 */     this.version = version;
/* 334 */     return this;
/*     */   }
/*     */   
/*     */   public URI getUri() {
/* 338 */     return this.uri;
/*     */   }
/*     */   
/*     */   public RequestBuilder setUri(URI uri) {
/* 342 */     this.uri = uri;
/* 343 */     return this;
/*     */   }
/*     */   
/*     */   public RequestBuilder setUri(String uri) {
/* 347 */     this.uri = (uri != null) ? URI.create(uri) : null;
/* 348 */     return this;
/*     */   }
/*     */   
/*     */   public Header getFirstHeader(String name) {
/* 352 */     return (this.headergroup != null) ? this.headergroup.getFirstHeader(name) : null;
/*     */   }
/*     */   
/*     */   public Header getLastHeader(String name) {
/* 356 */     return (this.headergroup != null) ? this.headergroup.getLastHeader(name) : null;
/*     */   }
/*     */   
/*     */   public Header[] getHeaders(String name) {
/* 360 */     return (this.headergroup != null) ? this.headergroup.getHeaders(name) : null;
/*     */   }
/*     */   
/*     */   public RequestBuilder addHeader(Header header) {
/* 364 */     if (this.headergroup == null) {
/* 365 */       this.headergroup = new HeaderGroup();
/*     */     }
/* 367 */     this.headergroup.addHeader(header);
/* 368 */     return this;
/*     */   }
/*     */   
/*     */   public RequestBuilder addHeader(String name, String value) {
/* 372 */     if (this.headergroup == null) {
/* 373 */       this.headergroup = new HeaderGroup();
/*     */     }
/* 375 */     this.headergroup.addHeader((Header)new BasicHeader(name, value));
/* 376 */     return this;
/*     */   }
/*     */   
/*     */   public RequestBuilder removeHeader(Header header) {
/* 380 */     if (this.headergroup == null) {
/* 381 */       this.headergroup = new HeaderGroup();
/*     */     }
/* 383 */     this.headergroup.removeHeader(header);
/* 384 */     return this;
/*     */   }
/*     */   
/*     */   public RequestBuilder removeHeaders(String name) {
/* 388 */     if (name == null || this.headergroup == null) {
/* 389 */       return this;
/*     */     }
/* 391 */     for (HeaderIterator i = this.headergroup.iterator(); i.hasNext(); ) {
/* 392 */       Header header = i.nextHeader();
/* 393 */       if (name.equalsIgnoreCase(header.getName())) {
/* 394 */         i.remove();
/*     */       }
/*     */     } 
/* 397 */     return this;
/*     */   }
/*     */   
/*     */   public RequestBuilder setHeader(Header header) {
/* 401 */     if (this.headergroup == null) {
/* 402 */       this.headergroup = new HeaderGroup();
/*     */     }
/* 404 */     this.headergroup.updateHeader(header);
/* 405 */     return this;
/*     */   }
/*     */   
/*     */   public RequestBuilder setHeader(String name, String value) {
/* 409 */     if (this.headergroup == null) {
/* 410 */       this.headergroup = new HeaderGroup();
/*     */     }
/* 412 */     this.headergroup.updateHeader((Header)new BasicHeader(name, value));
/* 413 */     return this;
/*     */   }
/*     */   
/*     */   public HttpEntity getEntity() {
/* 417 */     return this.entity;
/*     */   }
/*     */   
/*     */   public RequestBuilder setEntity(HttpEntity entity) {
/* 421 */     this.entity = entity;
/* 422 */     return this;
/*     */   }
/*     */   
/*     */   public List<NameValuePair> getParameters() {
/* 426 */     return (this.parameters != null) ? new ArrayList<NameValuePair>(this.parameters) : new ArrayList<NameValuePair>();
/*     */   }
/*     */ 
/*     */   
/*     */   public RequestBuilder addParameter(NameValuePair nvp) {
/* 431 */     Args.notNull(nvp, "Name value pair");
/* 432 */     if (this.parameters == null) {
/* 433 */       this.parameters = new LinkedList<NameValuePair>();
/*     */     }
/* 435 */     this.parameters.add(nvp);
/* 436 */     return this;
/*     */   }
/*     */   
/*     */   public RequestBuilder addParameter(String name, String value) {
/* 440 */     return addParameter((NameValuePair)new BasicNameValuePair(name, value));
/*     */   }
/*     */   
/*     */   public RequestBuilder addParameters(NameValuePair... nvps) {
/* 444 */     for (NameValuePair nvp : nvps) {
/* 445 */       addParameter(nvp);
/*     */     }
/* 447 */     return this;
/*     */   }
/*     */   
/*     */   public RequestConfig getConfig() {
/* 451 */     return this.config;
/*     */   }
/*     */   
/*     */   public RequestBuilder setConfig(RequestConfig config) {
/* 455 */     this.config = config;
/* 456 */     return this;
/*     */   }
/*     */   public HttpUriRequest build() {
/*     */     HttpRequestBase result;
/*     */     UrlEncodedFormEntity urlEncodedFormEntity;
/* 461 */     URI uriNotNull = (this.uri != null) ? this.uri : URI.create("/");
/* 462 */     HttpEntity entityCopy = this.entity;
/* 463 */     if (this.parameters != null && !this.parameters.isEmpty()) {
/* 464 */       if (entityCopy == null && ("POST".equalsIgnoreCase(this.method) || "PUT".equalsIgnoreCase(this.method))) {
/*     */         
/* 466 */         urlEncodedFormEntity = new UrlEncodedFormEntity(this.parameters, (this.charset != null) ? this.charset : HTTP.DEF_CONTENT_CHARSET);
/*     */       } else {
/*     */         try {
/* 469 */           uriNotNull = (new URIBuilder(uriNotNull)).setCharset(this.charset).addParameters(this.parameters).build();
/*     */ 
/*     */         
/*     */         }
/* 473 */         catch (URISyntaxException ex) {}
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 478 */     if (urlEncodedFormEntity == null) {
/* 479 */       result = new InternalRequest(this.method);
/*     */     } else {
/* 481 */       InternalEntityEclosingRequest request = new InternalEntityEclosingRequest(this.method);
/* 482 */       request.setEntity((HttpEntity)urlEncodedFormEntity);
/* 483 */       result = request;
/*     */     } 
/* 485 */     result.setProtocolVersion(this.version);
/* 486 */     result.setURI(uriNotNull);
/* 487 */     if (this.headergroup != null) {
/* 488 */       result.setHeaders(this.headergroup.getAllHeaders());
/*     */     }
/* 490 */     result.setConfig(this.config);
/* 491 */     return result;
/*     */   }
/*     */   
/*     */   static class InternalRequest
/*     */     extends HttpRequestBase
/*     */   {
/*     */     private final String method;
/*     */     
/*     */     InternalRequest(String method) {
/* 500 */       this.method = method;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getMethod() {
/* 505 */       return this.method;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static class InternalEntityEclosingRequest
/*     */     extends HttpEntityEnclosingRequestBase
/*     */   {
/*     */     private final String method;
/*     */     
/*     */     InternalEntityEclosingRequest(String method) {
/* 516 */       this.method = method;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getMethod() {
/* 521 */       return this.method;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\client\methods\RequestBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */