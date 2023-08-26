/*     */ package org.apache.http.client.utils;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Stack;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.conn.routing.RouteInfo;
/*     */ import org.apache.http.util.Args;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class URIUtils
/*     */ {
/*     */   @Deprecated
/*     */   public static URI createURI(String scheme, String host, int port, String path, String query, String fragment) throws URISyntaxException {
/*  86 */     StringBuilder buffer = new StringBuilder();
/*  87 */     if (host != null) {
/*  88 */       if (scheme != null) {
/*  89 */         buffer.append(scheme);
/*  90 */         buffer.append("://");
/*     */       } 
/*  92 */       buffer.append(host);
/*  93 */       if (port > 0) {
/*  94 */         buffer.append(':');
/*  95 */         buffer.append(port);
/*     */       } 
/*     */     } 
/*  98 */     if (path == null || !path.startsWith("/")) {
/*  99 */       buffer.append('/');
/*     */     }
/* 101 */     if (path != null) {
/* 102 */       buffer.append(path);
/*     */     }
/* 104 */     if (query != null) {
/* 105 */       buffer.append('?');
/* 106 */       buffer.append(query);
/*     */     } 
/* 108 */     if (fragment != null) {
/* 109 */       buffer.append('#');
/* 110 */       buffer.append(fragment);
/*     */     } 
/* 112 */     return new URI(buffer.toString());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static URI rewriteURI(URI uri, HttpHost target, boolean dropFragment) throws URISyntaxException {
/* 135 */     Args.notNull(uri, "URI");
/* 136 */     if (uri.isOpaque()) {
/* 137 */       return uri;
/*     */     }
/* 139 */     URIBuilder uribuilder = new URIBuilder(uri);
/* 140 */     if (target != null) {
/* 141 */       uribuilder.setScheme(target.getSchemeName());
/* 142 */       uribuilder.setHost(target.getHostName());
/* 143 */       uribuilder.setPort(target.getPort());
/*     */     } else {
/* 145 */       uribuilder.setScheme(null);
/* 146 */       uribuilder.setHost(null);
/* 147 */       uribuilder.setPort(-1);
/*     */     } 
/* 149 */     if (dropFragment) {
/* 150 */       uribuilder.setFragment(null);
/*     */     }
/* 152 */     if (TextUtils.isEmpty(uribuilder.getPath())) {
/* 153 */       uribuilder.setPath("/");
/*     */     }
/* 155 */     return uribuilder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static URI rewriteURI(URI uri, HttpHost target) throws URISyntaxException {
/* 166 */     return rewriteURI(uri, target, false);
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
/*     */   public static URI rewriteURI(URI uri) throws URISyntaxException {
/* 181 */     Args.notNull(uri, "URI");
/* 182 */     if (uri.isOpaque()) {
/* 183 */       return uri;
/*     */     }
/* 185 */     URIBuilder uribuilder = new URIBuilder(uri);
/* 186 */     if (uribuilder.getUserInfo() != null) {
/* 187 */       uribuilder.setUserInfo(null);
/*     */     }
/* 189 */     if (TextUtils.isEmpty(uribuilder.getPath())) {
/* 190 */       uribuilder.setPath("/");
/*     */     }
/* 192 */     if (uribuilder.getHost() != null) {
/* 193 */       uribuilder.setHost(uribuilder.getHost().toLowerCase(Locale.ROOT));
/*     */     }
/* 195 */     uribuilder.setFragment(null);
/* 196 */     return uribuilder.build();
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
/*     */   public static URI rewriteURIForRoute(URI uri, RouteInfo route) throws URISyntaxException {
/* 211 */     if (uri == null) {
/* 212 */       return null;
/*     */     }
/* 214 */     if (route.getProxyHost() != null && !route.isTunnelled()) {
/*     */       
/* 216 */       if (!uri.isAbsolute()) {
/* 217 */         HttpHost target = route.getTargetHost();
/* 218 */         return rewriteURI(uri, target, true);
/*     */       } 
/* 220 */       return rewriteURI(uri);
/*     */     } 
/*     */ 
/*     */     
/* 224 */     if (uri.isAbsolute()) {
/* 225 */       return rewriteURI(uri, null, true);
/*     */     }
/* 227 */     return rewriteURI(uri);
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
/*     */   public static URI resolve(URI baseURI, String reference) {
/* 241 */     return resolve(baseURI, URI.create(reference));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static URI resolve(URI baseURI, URI reference) {
/*     */     URI resolved;
/* 253 */     Args.notNull(baseURI, "Base URI");
/* 254 */     Args.notNull(reference, "Reference URI");
/* 255 */     String s = reference.toASCIIString();
/* 256 */     if (s.startsWith("?")) {
/* 257 */       String baseUri = baseURI.toASCIIString();
/* 258 */       int i = baseUri.indexOf('?');
/* 259 */       baseUri = (i > -1) ? baseUri.substring(0, i) : baseUri;
/* 260 */       return URI.create(baseUri + s);
/*     */     } 
/* 262 */     boolean emptyReference = s.isEmpty();
/*     */     
/* 264 */     if (emptyReference) {
/* 265 */       resolved = baseURI.resolve(URI.create("#"));
/* 266 */       String resolvedString = resolved.toASCIIString();
/* 267 */       resolved = URI.create(resolvedString.substring(0, resolvedString.indexOf('#')));
/*     */     } else {
/* 269 */       resolved = baseURI.resolve(reference);
/*     */     } 
/*     */     try {
/* 272 */       return normalizeSyntax(resolved);
/* 273 */     } catch (URISyntaxException ex) {
/* 274 */       throw new IllegalArgumentException(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static URI normalizeSyntax(URI uri) throws URISyntaxException {
/* 286 */     if (uri.isOpaque() || uri.getAuthority() == null)
/*     */     {
/* 288 */       return uri;
/*     */     }
/* 290 */     Args.check(uri.isAbsolute(), "Base URI must be absolute");
/* 291 */     URIBuilder builder = new URIBuilder(uri);
/* 292 */     String path = builder.getPath();
/* 293 */     if (path != null && !path.equals("/")) {
/* 294 */       String[] inputSegments = path.split("/");
/* 295 */       Stack<String> outputSegments = new Stack<String>();
/* 296 */       for (String inputSegment : inputSegments) {
/* 297 */         if (!inputSegment.isEmpty() && !".".equals(inputSegment))
/*     */         {
/* 299 */           if ("..".equals(inputSegment)) {
/* 300 */             if (!outputSegments.isEmpty()) {
/* 301 */               outputSegments.pop();
/*     */             }
/*     */           } else {
/* 304 */             outputSegments.push(inputSegment);
/*     */           }  } 
/*     */       } 
/* 307 */       StringBuilder outputBuffer = new StringBuilder();
/* 308 */       for (String outputSegment : outputSegments) {
/* 309 */         outputBuffer.append('/').append(outputSegment);
/*     */       }
/* 311 */       if (path.lastIndexOf('/') == path.length() - 1)
/*     */       {
/* 313 */         outputBuffer.append('/');
/*     */       }
/* 315 */       builder.setPath(outputBuffer.toString());
/*     */     } 
/* 317 */     if (builder.getScheme() != null) {
/* 318 */       builder.setScheme(builder.getScheme().toLowerCase(Locale.ROOT));
/*     */     }
/* 320 */     if (builder.getHost() != null) {
/* 321 */       builder.setHost(builder.getHost().toLowerCase(Locale.ROOT));
/*     */     }
/* 323 */     return builder.build();
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
/*     */   public static HttpHost extractHost(URI uri) {
/* 336 */     if (uri == null) {
/* 337 */       return null;
/*     */     }
/* 339 */     HttpHost target = null;
/* 340 */     if (uri.isAbsolute()) {
/* 341 */       int port = uri.getPort();
/* 342 */       String host = uri.getHost();
/* 343 */       if (host == null) {
/*     */         
/* 345 */         host = uri.getAuthority();
/* 346 */         if (host != null) {
/*     */           
/* 348 */           int at = host.indexOf('@');
/* 349 */           if (at >= 0) {
/* 350 */             if (host.length() > at + 1) {
/* 351 */               host = host.substring(at + 1);
/*     */             } else {
/* 353 */               host = null;
/*     */             } 
/*     */           }
/*     */           
/* 357 */           if (host != null) {
/* 358 */             int colon = host.indexOf(':');
/* 359 */             if (colon >= 0) {
/* 360 */               int pos = colon + 1;
/* 361 */               int len = 0;
/* 362 */               for (int i = pos; i < host.length() && 
/* 363 */                 Character.isDigit(host.charAt(i)); i++) {
/* 364 */                 len++;
/*     */               }
/*     */ 
/*     */ 
/*     */               
/* 369 */               if (len > 0) {
/*     */                 try {
/* 371 */                   port = Integer.parseInt(host.substring(pos, pos + len));
/* 372 */                 } catch (NumberFormatException ex) {}
/*     */               }
/*     */               
/* 375 */               host = host.substring(0, colon);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 380 */       String scheme = uri.getScheme();
/* 381 */       if (!TextUtils.isBlank(host)) {
/*     */         try {
/* 383 */           target = new HttpHost(host, port, scheme);
/* 384 */         } catch (IllegalArgumentException ignore) {}
/*     */       }
/*     */     } 
/*     */     
/* 388 */     return target;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static URI resolve(URI originalURI, HttpHost target, List<URI> redirects) throws URISyntaxException {
/*     */     URIBuilder uribuilder;
/* 411 */     Args.notNull(originalURI, "Request URI");
/*     */     
/* 413 */     if (redirects == null || redirects.isEmpty()) {
/* 414 */       uribuilder = new URIBuilder(originalURI);
/*     */     } else {
/* 416 */       uribuilder = new URIBuilder(redirects.get(redirects.size() - 1));
/* 417 */       String frag = uribuilder.getFragment();
/*     */       
/* 419 */       for (int i = redirects.size() - 1; frag == null && i >= 0; i--) {
/* 420 */         frag = ((URI)redirects.get(i)).getFragment();
/*     */       }
/* 422 */       uribuilder.setFragment(frag);
/*     */     } 
/*     */     
/* 425 */     if (uribuilder.getFragment() == null) {
/* 426 */       uribuilder.setFragment(originalURI.getFragment());
/*     */     }
/*     */     
/* 429 */     if (target != null && !uribuilder.isAbsolute()) {
/* 430 */       uribuilder.setScheme(target.getSchemeName());
/* 431 */       uribuilder.setHost(target.getHostName());
/* 432 */       uribuilder.setPort(target.getPort());
/*     */     } 
/* 434 */     return uribuilder.build();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\clien\\utils\URIUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */