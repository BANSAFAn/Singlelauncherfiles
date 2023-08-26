/*     */ package org.apache.http.impl.cookie;
/*     */ 
/*     */ import java.util.List;
/*     */ import org.apache.http.FormattedHeader;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HeaderElement;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.cookie.CommonCookieAttributeHandler;
/*     */ import org.apache.http.cookie.Cookie;
/*     */ import org.apache.http.cookie.CookieOrigin;
/*     */ import org.apache.http.cookie.CookieSpec;
/*     */ import org.apache.http.cookie.MalformedCookieException;
/*     */ import org.apache.http.message.ParserCursor;
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
/*     */ 
/*     */ @ThreadSafe
/*     */ public class DefaultCookieSpec
/*     */   implements CookieSpec
/*     */ {
/*     */   private final RFC2965Spec strict;
/*     */   private final RFC2109Spec obsoleteStrict;
/*     */   private final NetscapeDraftSpec netscapeDraft;
/*     */   
/*     */   DefaultCookieSpec(RFC2965Spec strict, RFC2109Spec obsoleteStrict, NetscapeDraftSpec netscapeDraft) {
/*  63 */     this.strict = strict;
/*  64 */     this.obsoleteStrict = obsoleteStrict;
/*  65 */     this.netscapeDraft = netscapeDraft;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultCookieSpec(String[] datepatterns, boolean oneHeader) {
/*  71 */     this.strict = new RFC2965Spec(oneHeader, new CommonCookieAttributeHandler[] { new RFC2965VersionAttributeHandler(), new BasicPathHandler(), new RFC2965DomainAttributeHandler(), new RFC2965PortAttributeHandler(), new BasicMaxAgeHandler(), new BasicSecureHandler(), new BasicCommentHandler(), new RFC2965CommentUrlAttributeHandler(), new RFC2965DiscardAttributeHandler() });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  81 */     this.obsoleteStrict = new RFC2109Spec(oneHeader, new CommonCookieAttributeHandler[] { new RFC2109VersionHandler(), new BasicPathHandler(), new RFC2109DomainHandler(), new BasicMaxAgeHandler(), new BasicSecureHandler(), new BasicCommentHandler() });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  88 */     (new CommonCookieAttributeHandler[5])[0] = new BasicDomainHandler(); (new CommonCookieAttributeHandler[5])[1] = new BasicPathHandler(); (new CommonCookieAttributeHandler[5])[2] = new BasicSecureHandler(); (new CommonCookieAttributeHandler[5])[3] = new BasicCommentHandler(); (new String[1])[0] = "EEE, dd-MMM-yy HH:mm:ss z"; this.netscapeDraft = new NetscapeDraftSpec(new CommonCookieAttributeHandler[] { null, null, null, null, new BasicExpiresHandler((datepatterns != null) ? (String[])datepatterns.clone() : new String[1]) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultCookieSpec() {
/*  98 */     this(null, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Cookie> parse(Header header, CookieOrigin origin) throws MalformedCookieException {
/* 105 */     Args.notNull(header, "Header");
/* 106 */     Args.notNull(origin, "Cookie origin");
/* 107 */     HeaderElement[] helems = header.getElements();
/* 108 */     boolean versioned = false;
/* 109 */     boolean netscape = false;
/* 110 */     for (HeaderElement helem : helems) {
/* 111 */       if (helem.getParameterByName("version") != null) {
/* 112 */         versioned = true;
/*     */       }
/* 114 */       if (helem.getParameterByName("expires") != null) {
/* 115 */         netscape = true;
/*     */       }
/*     */     } 
/* 118 */     if (netscape || !versioned) {
/*     */       CharArrayBuffer buffer;
/*     */       ParserCursor cursor;
/* 121 */       NetscapeDraftHeaderParser parser = NetscapeDraftHeaderParser.DEFAULT;
/*     */ 
/*     */       
/* 124 */       if (header instanceof FormattedHeader) {
/* 125 */         buffer = ((FormattedHeader)header).getBuffer();
/* 126 */         cursor = new ParserCursor(((FormattedHeader)header).getValuePos(), buffer.length());
/*     */       }
/*     */       else {
/*     */         
/* 130 */         String s = header.getValue();
/* 131 */         if (s == null) {
/* 132 */           throw new MalformedCookieException("Header value is null");
/*     */         }
/* 134 */         buffer = new CharArrayBuffer(s.length());
/* 135 */         buffer.append(s);
/* 136 */         cursor = new ParserCursor(0, buffer.length());
/*     */       } 
/* 138 */       helems = new HeaderElement[] { parser.parseHeader(buffer, cursor) };
/* 139 */       return this.netscapeDraft.parse(helems, origin);
/*     */     } 
/* 141 */     if ("Set-Cookie2".equals(header.getName())) {
/* 142 */       return this.strict.parse(helems, origin);
/*     */     }
/* 144 */     return this.obsoleteStrict.parse(helems, origin);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
/* 153 */     Args.notNull(cookie, "Cookie");
/* 154 */     Args.notNull(origin, "Cookie origin");
/* 155 */     if (cookie.getVersion() > 0) {
/* 156 */       if (cookie instanceof org.apache.http.cookie.SetCookie2) {
/* 157 */         this.strict.validate(cookie, origin);
/*     */       } else {
/* 159 */         this.obsoleteStrict.validate(cookie, origin);
/*     */       } 
/*     */     } else {
/* 162 */       this.netscapeDraft.validate(cookie, origin);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean match(Cookie cookie, CookieOrigin origin) {
/* 168 */     Args.notNull(cookie, "Cookie");
/* 169 */     Args.notNull(origin, "Cookie origin");
/* 170 */     if (cookie.getVersion() > 0) {
/* 171 */       if (cookie instanceof org.apache.http.cookie.SetCookie2) {
/* 172 */         return this.strict.match(cookie, origin);
/*     */       }
/* 174 */       return this.obsoleteStrict.match(cookie, origin);
/*     */     } 
/*     */     
/* 177 */     return this.netscapeDraft.match(cookie, origin);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Header> formatCookies(List<Cookie> cookies) {
/* 183 */     Args.notNull(cookies, "List of cookies");
/* 184 */     int version = Integer.MAX_VALUE;
/* 185 */     boolean isSetCookie2 = true;
/* 186 */     for (Cookie cookie : cookies) {
/* 187 */       if (!(cookie instanceof org.apache.http.cookie.SetCookie2)) {
/* 188 */         isSetCookie2 = false;
/*     */       }
/* 190 */       if (cookie.getVersion() < version) {
/* 191 */         version = cookie.getVersion();
/*     */       }
/*     */     } 
/* 194 */     if (version > 0) {
/* 195 */       if (isSetCookie2) {
/* 196 */         return this.strict.formatCookies(cookies);
/*     */       }
/* 198 */       return this.obsoleteStrict.formatCookies(cookies);
/*     */     } 
/*     */     
/* 201 */     return this.netscapeDraft.formatCookies(cookies);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getVersion() {
/* 207 */     return this.strict.getVersion();
/*     */   }
/*     */ 
/*     */   
/*     */   public Header getVersionHeader() {
/* 212 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 217 */     return "default";
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\cookie\DefaultCookieSpec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */