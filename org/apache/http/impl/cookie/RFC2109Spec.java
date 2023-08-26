/*     */ package org.apache.http.impl.cookie;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HeaderElement;
/*     */ import org.apache.http.annotation.Obsolete;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.cookie.ClientCookie;
/*     */ import org.apache.http.cookie.CommonCookieAttributeHandler;
/*     */ import org.apache.http.cookie.Cookie;
/*     */ import org.apache.http.cookie.CookieOrigin;
/*     */ import org.apache.http.cookie.CookiePathComparator;
/*     */ import org.apache.http.cookie.CookieRestrictionViolationException;
/*     */ import org.apache.http.cookie.MalformedCookieException;
/*     */ import org.apache.http.message.BufferedHeader;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.CharArrayBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Obsolete
/*     */ @ThreadSafe
/*     */ public class RFC2109Spec
/*     */   extends CookieSpecBase
/*     */ {
/*  63 */   static final String[] DATE_PATTERNS = new String[] { "EEE, dd MMM yyyy HH:mm:ss zzz", "EEE, dd-MMM-yy HH:mm:ss zzz", "EEE MMM d HH:mm:ss yyyy" };
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean oneHeader;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RFC2109Spec(String[] datepatterns, boolean oneHeader) {
/*  73 */     super(new CommonCookieAttributeHandler[] { new RFC2109VersionHandler(), new BasicPathHandler(), new RFC2109DomainHandler(), new BasicMaxAgeHandler(), new BasicSecureHandler(), new BasicCommentHandler(), new BasicExpiresHandler((datepatterns != null) ? (String[])datepatterns.clone() : DATE_PATTERNS) });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  81 */     this.oneHeader = oneHeader;
/*     */   }
/*     */ 
/*     */   
/*     */   public RFC2109Spec() {
/*  86 */     this((String[])null, false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected RFC2109Spec(boolean oneHeader, CommonCookieAttributeHandler... handlers) {
/*  91 */     super(handlers);
/*  92 */     this.oneHeader = oneHeader;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Cookie> parse(Header header, CookieOrigin origin) throws MalformedCookieException {
/*  98 */     Args.notNull(header, "Header");
/*  99 */     Args.notNull(origin, "Cookie origin");
/* 100 */     if (!header.getName().equalsIgnoreCase("Set-Cookie")) {
/* 101 */       throw new MalformedCookieException("Unrecognized cookie header '" + header.toString() + "'");
/*     */     }
/*     */     
/* 104 */     HeaderElement[] elems = header.getElements();
/* 105 */     return parse(elems, origin);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
/* 111 */     Args.notNull(cookie, "Cookie");
/* 112 */     String name = cookie.getName();
/* 113 */     if (name.indexOf(' ') != -1) {
/* 114 */       throw new CookieRestrictionViolationException("Cookie name may not contain blanks");
/*     */     }
/* 116 */     if (name.startsWith("$")) {
/* 117 */       throw new CookieRestrictionViolationException("Cookie name may not start with $");
/*     */     }
/* 119 */     super.validate(cookie, origin);
/*     */   }
/*     */   
/*     */   public List<Header> formatCookies(List<Cookie> cookies) {
/*     */     List<Cookie> cookieList;
/* 124 */     Args.notEmpty(cookies, "List of cookies");
/*     */     
/* 126 */     if (cookies.size() > 1) {
/*     */       
/* 128 */       cookieList = new ArrayList<Cookie>(cookies);
/* 129 */       Collections.sort(cookieList, (Comparator<? super Cookie>)CookiePathComparator.INSTANCE);
/*     */     } else {
/* 131 */       cookieList = cookies;
/*     */     } 
/* 133 */     if (this.oneHeader) {
/* 134 */       return doFormatOneHeader(cookieList);
/*     */     }
/* 136 */     return doFormatManyHeaders(cookieList);
/*     */   }
/*     */ 
/*     */   
/*     */   private List<Header> doFormatOneHeader(List<Cookie> cookies) {
/* 141 */     int version = Integer.MAX_VALUE;
/*     */     
/* 143 */     for (Cookie cookie : cookies) {
/* 144 */       if (cookie.getVersion() < version) {
/* 145 */         version = cookie.getVersion();
/*     */       }
/*     */     } 
/* 148 */     CharArrayBuffer buffer = new CharArrayBuffer(40 * cookies.size());
/* 149 */     buffer.append("Cookie");
/* 150 */     buffer.append(": ");
/* 151 */     buffer.append("$Version=");
/* 152 */     buffer.append(Integer.toString(version));
/* 153 */     for (Cookie cooky : cookies) {
/* 154 */       buffer.append("; ");
/* 155 */       Cookie cookie = cooky;
/* 156 */       formatCookieAsVer(buffer, cookie, version);
/*     */     } 
/* 158 */     List<Header> headers = new ArrayList<Header>(1);
/* 159 */     headers.add(new BufferedHeader(buffer));
/* 160 */     return headers;
/*     */   }
/*     */   
/*     */   private List<Header> doFormatManyHeaders(List<Cookie> cookies) {
/* 164 */     List<Header> headers = new ArrayList<Header>(cookies.size());
/* 165 */     for (Cookie cookie : cookies) {
/* 166 */       int version = cookie.getVersion();
/* 167 */       CharArrayBuffer buffer = new CharArrayBuffer(40);
/* 168 */       buffer.append("Cookie: ");
/* 169 */       buffer.append("$Version=");
/* 170 */       buffer.append(Integer.toString(version));
/* 171 */       buffer.append("; ");
/* 172 */       formatCookieAsVer(buffer, cookie, version);
/* 173 */       headers.add(new BufferedHeader(buffer));
/*     */     } 
/* 175 */     return headers;
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
/*     */   protected void formatParamAsVer(CharArrayBuffer buffer, String name, String value, int version) {
/* 189 */     buffer.append(name);
/* 190 */     buffer.append("=");
/* 191 */     if (value != null) {
/* 192 */       if (version > 0) {
/* 193 */         buffer.append('"');
/* 194 */         buffer.append(value);
/* 195 */         buffer.append('"');
/*     */       } else {
/* 197 */         buffer.append(value);
/*     */       } 
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
/*     */   
/*     */   protected void formatCookieAsVer(CharArrayBuffer buffer, Cookie cookie, int version) {
/* 211 */     formatParamAsVer(buffer, cookie.getName(), cookie.getValue(), version);
/* 212 */     if (cookie.getPath() != null && 
/* 213 */       cookie instanceof ClientCookie && ((ClientCookie)cookie).containsAttribute("path")) {
/*     */       
/* 215 */       buffer.append("; ");
/* 216 */       formatParamAsVer(buffer, "$Path", cookie.getPath(), version);
/*     */     } 
/*     */     
/* 219 */     if (cookie.getDomain() != null && 
/* 220 */       cookie instanceof ClientCookie && ((ClientCookie)cookie).containsAttribute("domain")) {
/*     */       
/* 222 */       buffer.append("; ");
/* 223 */       formatParamAsVer(buffer, "$Domain", cookie.getDomain(), version);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getVersion() {
/* 230 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public Header getVersionHeader() {
/* 235 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 240 */     return "rfc2109";
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\cookie\RFC2109Spec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */