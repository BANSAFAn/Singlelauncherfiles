/*     */ package org.apache.http.client.utils;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.apache.http.Consts;
/*     */ import org.apache.http.NameValuePair;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.conn.util.InetAddressUtils;
/*     */ import org.apache.http.message.BasicNameValuePair;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class URIBuilder
/*     */ {
/*     */   private String scheme;
/*     */   private String encodedSchemeSpecificPart;
/*     */   private String encodedAuthority;
/*     */   private String userInfo;
/*     */   private String encodedUserInfo;
/*     */   private String host;
/*     */   private int port;
/*     */   private String path;
/*     */   private String encodedPath;
/*     */   private String encodedQuery;
/*     */   private List<NameValuePair> queryParams;
/*     */   private String query;
/*     */   private Charset charset;
/*     */   private String fragment;
/*     */   private String encodedFragment;
/*     */   
/*     */   public URIBuilder() {
/*  71 */     this.port = -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URIBuilder(String string) throws URISyntaxException {
/*  82 */     digestURI(new URI(string));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URIBuilder(URI uri) {
/*  91 */     digestURI(uri);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URIBuilder setCharset(Charset charset) {
/*  98 */     this.charset = charset;
/*  99 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Charset getCharset() {
/* 106 */     return this.charset;
/*     */   }
/*     */   
/*     */   private List<NameValuePair> parseQuery(String query, Charset charset) {
/* 110 */     if (query != null && !query.isEmpty()) {
/* 111 */       return URLEncodedUtils.parse(query, charset);
/*     */     }
/* 113 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URI build() throws URISyntaxException {
/* 120 */     return new URI(buildString());
/*     */   }
/*     */   
/*     */   private String buildString() {
/* 124 */     StringBuilder sb = new StringBuilder();
/* 125 */     if (this.scheme != null) {
/* 126 */       sb.append(this.scheme).append(':');
/*     */     }
/* 128 */     if (this.encodedSchemeSpecificPart != null) {
/* 129 */       sb.append(this.encodedSchemeSpecificPart);
/*     */     } else {
/* 131 */       if (this.encodedAuthority != null) {
/* 132 */         sb.append("//").append(this.encodedAuthority);
/* 133 */       } else if (this.host != null) {
/* 134 */         sb.append("//");
/* 135 */         if (this.encodedUserInfo != null) {
/* 136 */           sb.append(this.encodedUserInfo).append("@");
/* 137 */         } else if (this.userInfo != null) {
/* 138 */           sb.append(encodeUserInfo(this.userInfo)).append("@");
/*     */         } 
/* 140 */         if (InetAddressUtils.isIPv6Address(this.host)) {
/* 141 */           sb.append("[").append(this.host).append("]");
/*     */         } else {
/* 143 */           sb.append(this.host);
/*     */         } 
/* 145 */         if (this.port >= 0) {
/* 146 */           sb.append(":").append(this.port);
/*     */         }
/*     */       } 
/* 149 */       if (this.encodedPath != null) {
/* 150 */         sb.append(normalizePath(this.encodedPath));
/* 151 */       } else if (this.path != null) {
/* 152 */         sb.append(encodePath(normalizePath(this.path)));
/*     */       } 
/* 154 */       if (this.encodedQuery != null) {
/* 155 */         sb.append("?").append(this.encodedQuery);
/* 156 */       } else if (this.queryParams != null) {
/* 157 */         sb.append("?").append(encodeUrlForm(this.queryParams));
/* 158 */       } else if (this.query != null) {
/* 159 */         sb.append("?").append(encodeUric(this.query));
/*     */       } 
/*     */     } 
/* 162 */     if (this.encodedFragment != null) {
/* 163 */       sb.append("#").append(this.encodedFragment);
/* 164 */     } else if (this.fragment != null) {
/* 165 */       sb.append("#").append(encodeUric(this.fragment));
/*     */     } 
/* 167 */     return sb.toString();
/*     */   }
/*     */   
/*     */   private void digestURI(URI uri) {
/* 171 */     this.scheme = uri.getScheme();
/* 172 */     this.encodedSchemeSpecificPart = uri.getRawSchemeSpecificPart();
/* 173 */     this.encodedAuthority = uri.getRawAuthority();
/* 174 */     this.host = uri.getHost();
/* 175 */     this.port = uri.getPort();
/* 176 */     this.encodedUserInfo = uri.getRawUserInfo();
/* 177 */     this.userInfo = uri.getUserInfo();
/* 178 */     this.encodedPath = uri.getRawPath();
/* 179 */     this.path = uri.getPath();
/* 180 */     this.encodedQuery = uri.getRawQuery();
/* 181 */     this.queryParams = parseQuery(uri.getRawQuery(), (this.charset != null) ? this.charset : Consts.UTF_8);
/* 182 */     this.encodedFragment = uri.getRawFragment();
/* 183 */     this.fragment = uri.getFragment();
/*     */   }
/*     */   
/*     */   private String encodeUserInfo(String userInfo) {
/* 187 */     return URLEncodedUtils.encUserInfo(userInfo, (this.charset != null) ? this.charset : Consts.UTF_8);
/*     */   }
/*     */   
/*     */   private String encodePath(String path) {
/* 191 */     return URLEncodedUtils.encPath(path, (this.charset != null) ? this.charset : Consts.UTF_8);
/*     */   }
/*     */   
/*     */   private String encodeUrlForm(List<NameValuePair> params) {
/* 195 */     return URLEncodedUtils.format(params, (this.charset != null) ? this.charset : Consts.UTF_8);
/*     */   }
/*     */   
/*     */   private String encodeUric(String fragment) {
/* 199 */     return URLEncodedUtils.encUric(fragment, (this.charset != null) ? this.charset : Consts.UTF_8);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URIBuilder setScheme(String scheme) {
/* 206 */     this.scheme = scheme;
/* 207 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URIBuilder setUserInfo(String userInfo) {
/* 215 */     this.userInfo = userInfo;
/* 216 */     this.encodedSchemeSpecificPart = null;
/* 217 */     this.encodedAuthority = null;
/* 218 */     this.encodedUserInfo = null;
/* 219 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URIBuilder setUserInfo(String username, String password) {
/* 227 */     return setUserInfo(username + ':' + password);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URIBuilder setHost(String host) {
/* 234 */     this.host = host;
/* 235 */     this.encodedSchemeSpecificPart = null;
/* 236 */     this.encodedAuthority = null;
/* 237 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URIBuilder setPort(int port) {
/* 244 */     this.port = (port < 0) ? -1 : port;
/* 245 */     this.encodedSchemeSpecificPart = null;
/* 246 */     this.encodedAuthority = null;
/* 247 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URIBuilder setPath(String path) {
/* 254 */     this.path = path;
/* 255 */     this.encodedSchemeSpecificPart = null;
/* 256 */     this.encodedPath = null;
/* 257 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URIBuilder removeQuery() {
/* 264 */     this.queryParams = null;
/* 265 */     this.query = null;
/* 266 */     this.encodedQuery = null;
/* 267 */     this.encodedSchemeSpecificPart = null;
/* 268 */     return this;
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
/*     */   @Deprecated
/*     */   public URIBuilder setQuery(String query) {
/* 282 */     this.queryParams = parseQuery(query, (this.charset != null) ? this.charset : Consts.UTF_8);
/* 283 */     this.query = null;
/* 284 */     this.encodedQuery = null;
/* 285 */     this.encodedSchemeSpecificPart = null;
/* 286 */     return this;
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
/*     */   public URIBuilder setParameters(List<NameValuePair> nvps) {
/* 300 */     if (this.queryParams == null) {
/* 301 */       this.queryParams = new ArrayList<NameValuePair>();
/*     */     } else {
/* 303 */       this.queryParams.clear();
/*     */     } 
/* 305 */     this.queryParams.addAll(nvps);
/* 306 */     this.encodedQuery = null;
/* 307 */     this.encodedSchemeSpecificPart = null;
/* 308 */     this.query = null;
/* 309 */     return this;
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
/*     */   public URIBuilder addParameters(List<NameValuePair> nvps) {
/* 323 */     if (this.queryParams == null) {
/* 324 */       this.queryParams = new ArrayList<NameValuePair>();
/*     */     }
/* 326 */     this.queryParams.addAll(nvps);
/* 327 */     this.encodedQuery = null;
/* 328 */     this.encodedSchemeSpecificPart = null;
/* 329 */     this.query = null;
/* 330 */     return this;
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
/*     */   public URIBuilder setParameters(NameValuePair... nvps) {
/* 344 */     if (this.queryParams == null) {
/* 345 */       this.queryParams = new ArrayList<NameValuePair>();
/*     */     } else {
/* 347 */       this.queryParams.clear();
/*     */     } 
/* 349 */     for (NameValuePair nvp : nvps) {
/* 350 */       this.queryParams.add(nvp);
/*     */     }
/* 352 */     this.encodedQuery = null;
/* 353 */     this.encodedSchemeSpecificPart = null;
/* 354 */     this.query = null;
/* 355 */     return this;
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
/*     */   public URIBuilder addParameter(String param, String value) {
/* 367 */     if (this.queryParams == null) {
/* 368 */       this.queryParams = new ArrayList<NameValuePair>();
/*     */     }
/* 370 */     this.queryParams.add(new BasicNameValuePair(param, value));
/* 371 */     this.encodedQuery = null;
/* 372 */     this.encodedSchemeSpecificPart = null;
/* 373 */     this.query = null;
/* 374 */     return this;
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
/*     */   public URIBuilder setParameter(String param, String value) {
/* 386 */     if (this.queryParams == null) {
/* 387 */       this.queryParams = new ArrayList<NameValuePair>();
/*     */     }
/* 389 */     if (!this.queryParams.isEmpty()) {
/* 390 */       for (Iterator<NameValuePair> it = this.queryParams.iterator(); it.hasNext(); ) {
/* 391 */         NameValuePair nvp = it.next();
/* 392 */         if (nvp.getName().equals(param)) {
/* 393 */           it.remove();
/*     */         }
/*     */       } 
/*     */     }
/* 397 */     this.queryParams.add(new BasicNameValuePair(param, value));
/* 398 */     this.encodedQuery = null;
/* 399 */     this.encodedSchemeSpecificPart = null;
/* 400 */     this.query = null;
/* 401 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URIBuilder clearParameters() {
/* 410 */     this.queryParams = null;
/* 411 */     this.encodedQuery = null;
/* 412 */     this.encodedSchemeSpecificPart = null;
/* 413 */     return this;
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
/*     */   public URIBuilder setCustomQuery(String query) {
/* 427 */     this.query = query;
/* 428 */     this.encodedQuery = null;
/* 429 */     this.encodedSchemeSpecificPart = null;
/* 430 */     this.queryParams = null;
/* 431 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URIBuilder setFragment(String fragment) {
/* 439 */     this.fragment = fragment;
/* 440 */     this.encodedFragment = null;
/* 441 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAbsolute() {
/* 448 */     return (this.scheme != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOpaque() {
/* 455 */     return (this.path == null);
/*     */   }
/*     */   
/*     */   public String getScheme() {
/* 459 */     return this.scheme;
/*     */   }
/*     */   
/*     */   public String getUserInfo() {
/* 463 */     return this.userInfo;
/*     */   }
/*     */   
/*     */   public String getHost() {
/* 467 */     return this.host;
/*     */   }
/*     */   
/*     */   public int getPort() {
/* 471 */     return this.port;
/*     */   }
/*     */   
/*     */   public String getPath() {
/* 475 */     return this.path;
/*     */   }
/*     */   
/*     */   public List<NameValuePair> getQueryParams() {
/* 479 */     if (this.queryParams != null) {
/* 480 */       return new ArrayList<NameValuePair>(this.queryParams);
/*     */     }
/* 482 */     return new ArrayList<NameValuePair>();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFragment() {
/* 487 */     return this.fragment;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 492 */     return buildString();
/*     */   }
/*     */   
/*     */   private static String normalizePath(String path) {
/* 496 */     String s = path;
/* 497 */     if (s == null) {
/* 498 */       return null;
/*     */     }
/* 500 */     int n = 0;
/* 501 */     for (; n < s.length() && 
/* 502 */       s.charAt(n) == '/'; n++);
/*     */ 
/*     */ 
/*     */     
/* 506 */     if (n > 1) {
/* 507 */       s = s.substring(n - 1);
/*     */     }
/* 509 */     return s;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\clien\\utils\URIBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */