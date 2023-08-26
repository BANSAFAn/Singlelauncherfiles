/*     */ package org.apache.http.impl.client;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.ProtocolException;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.client.CircularRedirectException;
/*     */ import org.apache.http.client.RedirectHandler;
/*     */ import org.apache.http.client.utils.URIUtils;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.Asserts;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ @Immutable
/*     */ public class DefaultRedirectHandler
/*     */   implements RedirectHandler
/*     */ {
/*  65 */   private final Log log = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String REDIRECT_LOCATIONS = "http.protocol.redirect-locations";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRedirectRequested(HttpResponse response, HttpContext context) {
/*     */     HttpRequest request;
/*     */     String method;
/*  77 */     Args.notNull(response, "HTTP response");
/*     */     
/*  79 */     int statusCode = response.getStatusLine().getStatusCode();
/*  80 */     switch (statusCode) {
/*     */       case 301:
/*     */       case 302:
/*     */       case 307:
/*  84 */         request = (HttpRequest)context.getAttribute("http.request");
/*     */         
/*  86 */         method = request.getRequestLine().getMethod();
/*  87 */         return (method.equalsIgnoreCase("GET") || method.equalsIgnoreCase("HEAD"));
/*     */       
/*     */       case 303:
/*  90 */         return true;
/*     */     } 
/*  92 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URI getLocationURI(HttpResponse response, HttpContext context) throws ProtocolException {
/*     */     URI uri;
/* 100 */     Args.notNull(response, "HTTP response");
/*     */     
/* 102 */     Header locationHeader = response.getFirstHeader("location");
/* 103 */     if (locationHeader == null)
/*     */     {
/* 105 */       throw new ProtocolException("Received redirect response " + response.getStatusLine() + " but no location header");
/*     */     }
/*     */ 
/*     */     
/* 109 */     String location = locationHeader.getValue();
/* 110 */     if (this.log.isDebugEnabled()) {
/* 111 */       this.log.debug("Redirect requested to location '" + location + "'");
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 116 */       uri = new URI(location);
/* 117 */     } catch (URISyntaxException ex) {
/* 118 */       throw new ProtocolException("Invalid redirect URI: " + location, ex);
/*     */     } 
/*     */     
/* 121 */     HttpParams params = response.getParams();
/*     */ 
/*     */     
/* 124 */     if (!uri.isAbsolute()) {
/* 125 */       if (params.isParameterTrue("http.protocol.reject-relative-redirect")) {
/* 126 */         throw new ProtocolException("Relative redirect location '" + uri + "' not allowed");
/*     */       }
/*     */ 
/*     */       
/* 130 */       HttpHost target = (HttpHost)context.getAttribute("http.target_host");
/*     */       
/* 132 */       Asserts.notNull(target, "Target host");
/*     */       
/* 134 */       HttpRequest request = (HttpRequest)context.getAttribute("http.request");
/*     */ 
/*     */       
/*     */       try {
/* 138 */         URI requestURI = new URI(request.getRequestLine().getUri());
/* 139 */         URI absoluteRequestURI = URIUtils.rewriteURI(requestURI, target, true);
/* 140 */         uri = URIUtils.resolve(absoluteRequestURI, uri);
/* 141 */       } catch (URISyntaxException ex) {
/* 142 */         throw new ProtocolException(ex.getMessage(), ex);
/*     */       } 
/*     */     } 
/*     */     
/* 146 */     if (params.isParameterFalse("http.protocol.allow-circular-redirects")) {
/*     */       URI uRI;
/* 148 */       RedirectLocations redirectLocations = (RedirectLocations)context.getAttribute("http.protocol.redirect-locations");
/*     */ 
/*     */       
/* 151 */       if (redirectLocations == null) {
/* 152 */         redirectLocations = new RedirectLocations();
/* 153 */         context.setAttribute("http.protocol.redirect-locations", redirectLocations);
/*     */       } 
/*     */ 
/*     */       
/* 157 */       if (uri.getFragment() != null) {
/*     */         try {
/* 159 */           HttpHost target = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
/*     */ 
/*     */ 
/*     */           
/* 163 */           uRI = URIUtils.rewriteURI(uri, target, true);
/* 164 */         } catch (URISyntaxException ex) {
/* 165 */           throw new ProtocolException(ex.getMessage(), ex);
/*     */         } 
/*     */       } else {
/* 168 */         uRI = uri;
/*     */       } 
/*     */       
/* 171 */       if (redirectLocations.contains(uRI)) {
/* 172 */         throw new CircularRedirectException("Circular redirect to '" + uRI + "'");
/*     */       }
/*     */       
/* 175 */       redirectLocations.add(uRI);
/*     */     } 
/*     */ 
/*     */     
/* 179 */     return uri;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\client\DefaultRedirectHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */