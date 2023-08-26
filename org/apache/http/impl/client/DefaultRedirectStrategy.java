/*     */ package org.apache.http.impl.client;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.Locale;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.ProtocolException;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.client.CircularRedirectException;
/*     */ import org.apache.http.client.RedirectStrategy;
/*     */ import org.apache.http.client.config.RequestConfig;
/*     */ import org.apache.http.client.methods.HttpGet;
/*     */ import org.apache.http.client.methods.HttpHead;
/*     */ import org.apache.http.client.methods.HttpUriRequest;
/*     */ import org.apache.http.client.methods.RequestBuilder;
/*     */ import org.apache.http.client.protocol.HttpClientContext;
/*     */ import org.apache.http.client.utils.URIBuilder;
/*     */ import org.apache.http.client.utils.URIUtils;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.Asserts;
/*     */ import org.apache.http.util.TextUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class DefaultRedirectStrategy
/*     */   implements RedirectStrategy
/*     */ {
/*  76 */   private final Log log = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static final String REDIRECT_LOCATIONS = "http.protocol.redirect-locations";
/*     */ 
/*     */   
/*  84 */   public static final DefaultRedirectStrategy INSTANCE = new DefaultRedirectStrategy();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  89 */   private static final String[] REDIRECT_METHODS = new String[] { "GET", "HEAD" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
/* 103 */     Args.notNull(request, "HTTP request");
/* 104 */     Args.notNull(response, "HTTP response");
/*     */     
/* 106 */     int statusCode = response.getStatusLine().getStatusCode();
/* 107 */     String method = request.getRequestLine().getMethod();
/* 108 */     Header locationHeader = response.getFirstHeader("location");
/* 109 */     switch (statusCode) {
/*     */       case 302:
/* 111 */         return (isRedirectable(method) && locationHeader != null);
/*     */       case 301:
/*     */       case 307:
/* 114 */         return isRedirectable(method);
/*     */       case 303:
/* 116 */         return true;
/*     */     } 
/* 118 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URI getLocationURI(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
/* 126 */     Args.notNull(request, "HTTP request");
/* 127 */     Args.notNull(response, "HTTP response");
/* 128 */     Args.notNull(context, "HTTP context");
/*     */     
/* 130 */     HttpClientContext clientContext = HttpClientContext.adapt(context);
/*     */ 
/*     */     
/* 133 */     Header locationHeader = response.getFirstHeader("location");
/* 134 */     if (locationHeader == null)
/*     */     {
/* 136 */       throw new ProtocolException("Received redirect response " + response.getStatusLine() + " but no location header");
/*     */     }
/*     */ 
/*     */     
/* 140 */     String location = locationHeader.getValue();
/* 141 */     if (this.log.isDebugEnabled()) {
/* 142 */       this.log.debug("Redirect requested to location '" + location + "'");
/*     */     }
/*     */     
/* 145 */     RequestConfig config = clientContext.getRequestConfig();
/*     */     
/* 147 */     URI uri = createLocationURI(location);
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 152 */       if (!uri.isAbsolute()) {
/* 153 */         if (!config.isRelativeRedirectsAllowed()) {
/* 154 */           throw new ProtocolException("Relative redirect location '" + uri + "' not allowed");
/*     */         }
/*     */ 
/*     */         
/* 158 */         HttpHost target = clientContext.getTargetHost();
/* 159 */         Asserts.notNull(target, "Target host");
/* 160 */         URI requestURI = new URI(request.getRequestLine().getUri());
/* 161 */         URI absoluteRequestURI = URIUtils.rewriteURI(requestURI, target, false);
/* 162 */         uri = URIUtils.resolve(absoluteRequestURI, uri);
/*     */       } 
/* 164 */     } catch (URISyntaxException ex) {
/* 165 */       throw new ProtocolException(ex.getMessage(), ex);
/*     */     } 
/*     */     
/* 168 */     RedirectLocations redirectLocations = (RedirectLocations)clientContext.getAttribute("http.protocol.redirect-locations");
/*     */     
/* 170 */     if (redirectLocations == null) {
/* 171 */       redirectLocations = new RedirectLocations();
/* 172 */       context.setAttribute("http.protocol.redirect-locations", redirectLocations);
/*     */     } 
/* 174 */     if (!config.isCircularRedirectsAllowed() && 
/* 175 */       redirectLocations.contains(uri)) {
/* 176 */       throw new CircularRedirectException("Circular redirect to '" + uri + "'");
/*     */     }
/*     */     
/* 179 */     redirectLocations.add(uri);
/* 180 */     return uri;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected URI createLocationURI(String location) throws ProtocolException {
/*     */     try {
/* 188 */       URIBuilder b = new URIBuilder((new URI(location)).normalize());
/* 189 */       String host = b.getHost();
/* 190 */       if (host != null) {
/* 191 */         b.setHost(host.toLowerCase(Locale.ROOT));
/*     */       }
/* 193 */       String path = b.getPath();
/* 194 */       if (TextUtils.isEmpty(path)) {
/* 195 */         b.setPath("/");
/*     */       }
/* 197 */       return b.build();
/* 198 */     } catch (URISyntaxException ex) {
/* 199 */       throw new ProtocolException("Invalid redirect URI: " + location, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isRedirectable(String method) {
/* 207 */     for (String m : REDIRECT_METHODS) {
/* 208 */       if (m.equalsIgnoreCase(method)) {
/* 209 */         return true;
/*     */       }
/*     */     } 
/* 212 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpUriRequest getRedirect(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
/* 220 */     URI uri = getLocationURI(request, response, context);
/* 221 */     String method = request.getRequestLine().getMethod();
/* 222 */     if (method.equalsIgnoreCase("HEAD"))
/* 223 */       return (HttpUriRequest)new HttpHead(uri); 
/* 224 */     if (method.equalsIgnoreCase("GET")) {
/* 225 */       return (HttpUriRequest)new HttpGet(uri);
/*     */     }
/* 227 */     int status = response.getStatusLine().getStatusCode();
/* 228 */     if (status == 307) {
/* 229 */       return RequestBuilder.copy(request).setUri(uri).build();
/*     */     }
/* 231 */     return (HttpUriRequest)new HttpGet(uri);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\client\DefaultRedirectStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */