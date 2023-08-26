/*     */ package org.apache.http.auth;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.LangUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class AuthScope
/*     */ {
/*  52 */   public static final String ANY_HOST = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int ANY_PORT = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   public static final String ANY_REALM = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  67 */   public static final String ANY_SCHEME = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  74 */   public static final AuthScope ANY = new AuthScope(ANY_HOST, -1, ANY_REALM, ANY_SCHEME);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String scheme;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String realm;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String host;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int port;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final HttpHost origin;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AuthScope(String host, int port, String realm, String schemeName) {
/* 109 */     this.host = (host == null) ? ANY_HOST : host.toLowerCase(Locale.ROOT);
/* 110 */     this.port = (port < 0) ? -1 : port;
/* 111 */     this.realm = (realm == null) ? ANY_REALM : realm;
/* 112 */     this.scheme = (schemeName == null) ? ANY_SCHEME : schemeName.toUpperCase(Locale.ROOT);
/* 113 */     this.origin = null;
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
/*     */   public AuthScope(HttpHost origin, String realm, String schemeName) {
/* 131 */     Args.notNull(origin, "Host");
/* 132 */     this.host = origin.getHostName().toLowerCase(Locale.ROOT);
/* 133 */     this.port = (origin.getPort() < 0) ? -1 : origin.getPort();
/* 134 */     this.realm = (realm == null) ? ANY_REALM : realm;
/* 135 */     this.scheme = (schemeName == null) ? ANY_SCHEME : schemeName.toUpperCase(Locale.ROOT);
/* 136 */     this.origin = origin;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AuthScope(HttpHost origin) {
/* 147 */     this(origin, ANY_REALM, ANY_SCHEME);
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
/*     */   public AuthScope(String host, int port, String realm) {
/* 161 */     this(host, port, realm, ANY_SCHEME);
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
/*     */   public AuthScope(String host, int port) {
/* 173 */     this(host, port, ANY_REALM, ANY_SCHEME);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AuthScope(AuthScope authscope) {
/* 181 */     Args.notNull(authscope, "Scope");
/* 182 */     this.host = authscope.getHost();
/* 183 */     this.port = authscope.getPort();
/* 184 */     this.realm = authscope.getRealm();
/* 185 */     this.scheme = authscope.getScheme();
/* 186 */     this.origin = authscope.getOrigin();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpHost getOrigin() {
/* 195 */     return this.origin;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getHost() {
/* 202 */     return this.host;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPort() {
/* 209 */     return this.port;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRealm() {
/* 216 */     return this.realm;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getScheme() {
/* 223 */     return this.scheme;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int match(AuthScope that) {
/* 234 */     int factor = 0;
/* 235 */     if (LangUtils.equals(this.scheme, that.scheme)) {
/* 236 */       factor++;
/*     */     }
/* 238 */     else if (this.scheme != ANY_SCHEME && that.scheme != ANY_SCHEME) {
/* 239 */       return -1;
/*     */     } 
/*     */     
/* 242 */     if (LangUtils.equals(this.realm, that.realm)) {
/* 243 */       factor += 2;
/*     */     }
/* 245 */     else if (this.realm != ANY_REALM && that.realm != ANY_REALM) {
/* 246 */       return -1;
/*     */     } 
/*     */     
/* 249 */     if (this.port == that.port) {
/* 250 */       factor += 4;
/*     */     }
/* 252 */     else if (this.port != -1 && that.port != -1) {
/* 253 */       return -1;
/*     */     } 
/*     */     
/* 256 */     if (LangUtils.equals(this.host, that.host)) {
/* 257 */       factor += 8;
/*     */     }
/* 259 */     else if (this.host != ANY_HOST && that.host != ANY_HOST) {
/* 260 */       return -1;
/*     */     } 
/*     */     
/* 263 */     return factor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 271 */     if (o == null) {
/* 272 */       return false;
/*     */     }
/* 274 */     if (o == this) {
/* 275 */       return true;
/*     */     }
/* 277 */     if (!(o instanceof AuthScope)) {
/* 278 */       return super.equals(o);
/*     */     }
/* 280 */     AuthScope that = (AuthScope)o;
/* 281 */     return (LangUtils.equals(this.host, that.host) && this.port == that.port && LangUtils.equals(this.realm, that.realm) && LangUtils.equals(this.scheme, that.scheme));
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
/*     */   public String toString() {
/* 293 */     StringBuilder buffer = new StringBuilder();
/* 294 */     if (this.scheme != null) {
/* 295 */       buffer.append(this.scheme.toUpperCase(Locale.ROOT));
/* 296 */       buffer.append(' ');
/*     */     } 
/* 298 */     if (this.realm != null) {
/* 299 */       buffer.append('\'');
/* 300 */       buffer.append(this.realm);
/* 301 */       buffer.append('\'');
/*     */     } else {
/* 303 */       buffer.append("<any realm>");
/*     */     } 
/* 305 */     if (this.host != null) {
/* 306 */       buffer.append('@');
/* 307 */       buffer.append(this.host);
/* 308 */       if (this.port >= 0) {
/* 309 */         buffer.append(':');
/* 310 */         buffer.append(this.port);
/*     */       } 
/*     */     } 
/* 313 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 321 */     int hash = 17;
/* 322 */     hash = LangUtils.hashCode(hash, this.host);
/* 323 */     hash = LangUtils.hashCode(hash, this.port);
/* 324 */     hash = LangUtils.hashCode(hash, this.realm);
/* 325 */     hash = LangUtils.hashCode(hash, this.scheme);
/* 326 */     return hash;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\auth\AuthScope.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */