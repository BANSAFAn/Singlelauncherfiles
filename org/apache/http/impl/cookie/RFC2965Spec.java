/*     */ package org.apache.http.impl.cookie;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HeaderElement;
/*     */ import org.apache.http.NameValuePair;
/*     */ import org.apache.http.annotation.Obsolete;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.cookie.ClientCookie;
/*     */ import org.apache.http.cookie.CommonCookieAttributeHandler;
/*     */ import org.apache.http.cookie.Cookie;
/*     */ import org.apache.http.cookie.CookieAttributeHandler;
/*     */ import org.apache.http.cookie.CookieOrigin;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ @Obsolete
/*     */ @ThreadSafe
/*     */ public class RFC2965Spec
/*     */   extends RFC2109Spec
/*     */ {
/*     */   public RFC2965Spec() {
/*  68 */     this((String[])null, false);
/*     */   }
/*     */   
/*     */   public RFC2965Spec(String[] datepatterns, boolean oneHeader) {
/*  72 */     super(oneHeader, new CommonCookieAttributeHandler[] { new RFC2965VersionAttributeHandler(), new BasicPathHandler(), new RFC2965DomainAttributeHandler(), new RFC2965PortAttributeHandler(), new BasicMaxAgeHandler(), new BasicSecureHandler(), new BasicCommentHandler(), new BasicExpiresHandler((datepatterns != null) ? (String[])datepatterns.clone() : DATE_PATTERNS), new RFC2965CommentUrlAttributeHandler(), new RFC2965DiscardAttributeHandler() });
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
/*     */   RFC2965Spec(boolean oneHeader, CommonCookieAttributeHandler... handlers) {
/*  88 */     super(oneHeader, handlers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Cookie> parse(Header header, CookieOrigin origin) throws MalformedCookieException {
/*  95 */     Args.notNull(header, "Header");
/*  96 */     Args.notNull(origin, "Cookie origin");
/*  97 */     if (!header.getName().equalsIgnoreCase("Set-Cookie2")) {
/*  98 */       throw new MalformedCookieException("Unrecognized cookie header '" + header.toString() + "'");
/*     */     }
/*     */     
/* 101 */     HeaderElement[] elems = header.getElements();
/* 102 */     return createCookies(elems, adjustEffectiveHost(origin));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<Cookie> parse(HeaderElement[] elems, CookieOrigin origin) throws MalformedCookieException {
/* 109 */     return createCookies(elems, adjustEffectiveHost(origin));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private List<Cookie> createCookies(HeaderElement[] elems, CookieOrigin origin) throws MalformedCookieException {
/* 115 */     List<Cookie> cookies = new ArrayList<Cookie>(elems.length);
/* 116 */     for (HeaderElement headerelement : elems) {
/* 117 */       String name = headerelement.getName();
/* 118 */       String value = headerelement.getValue();
/* 119 */       if (name == null || name.isEmpty()) {
/* 120 */         throw new MalformedCookieException("Cookie name may not be empty");
/*     */       }
/*     */       
/* 123 */       BasicClientCookie2 cookie = new BasicClientCookie2(name, value);
/* 124 */       cookie.setPath(getDefaultPath(origin));
/* 125 */       cookie.setDomain(getDefaultDomain(origin));
/* 126 */       cookie.setPorts(new int[] { origin.getPort() });
/*     */       
/* 128 */       NameValuePair[] attribs = headerelement.getParameters();
/*     */ 
/*     */ 
/*     */       
/* 132 */       Map<String, NameValuePair> attribmap = new HashMap<String, NameValuePair>(attribs.length);
/*     */       
/* 134 */       for (int j = attribs.length - 1; j >= 0; j--) {
/* 135 */         NameValuePair param = attribs[j];
/* 136 */         attribmap.put(param.getName().toLowerCase(Locale.ROOT), param);
/*     */       } 
/* 138 */       for (Map.Entry<String, NameValuePair> entry : attribmap.entrySet()) {
/* 139 */         NameValuePair attrib = entry.getValue();
/* 140 */         String s = attrib.getName().toLowerCase(Locale.ROOT);
/*     */         
/* 142 */         cookie.setAttribute(s, attrib.getValue());
/*     */         
/* 144 */         CookieAttributeHandler handler = findAttribHandler(s);
/* 145 */         if (handler != null) {
/* 146 */           handler.parse(cookie, attrib.getValue());
/*     */         }
/*     */       } 
/* 149 */       cookies.add(cookie);
/*     */     } 
/* 151 */     return cookies;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
/* 157 */     Args.notNull(cookie, "Cookie");
/* 158 */     Args.notNull(origin, "Cookie origin");
/* 159 */     super.validate(cookie, adjustEffectiveHost(origin));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean match(Cookie cookie, CookieOrigin origin) {
/* 164 */     Args.notNull(cookie, "Cookie");
/* 165 */     Args.notNull(origin, "Cookie origin");
/* 166 */     return super.match(cookie, adjustEffectiveHost(origin));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void formatCookieAsVer(CharArrayBuffer buffer, Cookie cookie, int version) {
/* 175 */     super.formatCookieAsVer(buffer, cookie, version);
/*     */     
/* 177 */     if (cookie instanceof ClientCookie) {
/*     */       
/* 179 */       String s = ((ClientCookie)cookie).getAttribute("port");
/* 180 */       if (s != null) {
/* 181 */         buffer.append("; $Port");
/* 182 */         buffer.append("=\"");
/* 183 */         if (!s.trim().isEmpty()) {
/* 184 */           int[] ports = cookie.getPorts();
/* 185 */           if (ports != null) {
/* 186 */             int len = ports.length;
/* 187 */             for (int i = 0; i < len; i++) {
/* 188 */               if (i > 0) {
/* 189 */                 buffer.append(",");
/*     */               }
/* 191 */               buffer.append(Integer.toString(ports[i]));
/*     */             } 
/*     */           } 
/*     */         } 
/* 195 */         buffer.append("\"");
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
/*     */ 
/*     */   
/*     */   private static CookieOrigin adjustEffectiveHost(CookieOrigin origin) {
/* 211 */     String host = origin.getHost();
/*     */ 
/*     */ 
/*     */     
/* 215 */     boolean isLocalHost = true;
/* 216 */     for (int i = 0; i < host.length(); i++) {
/* 217 */       char ch = host.charAt(i);
/* 218 */       if (ch == '.' || ch == ':') {
/* 219 */         isLocalHost = false;
/*     */         break;
/*     */       } 
/*     */     } 
/* 223 */     if (isLocalHost) {
/* 224 */       host = host + ".local";
/* 225 */       return new CookieOrigin(host, origin.getPort(), origin.getPath(), origin.isSecure());
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 231 */     return origin;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getVersion() {
/* 237 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public Header getVersionHeader() {
/* 242 */     CharArrayBuffer buffer = new CharArrayBuffer(40);
/* 243 */     buffer.append("Cookie2");
/* 244 */     buffer.append(": ");
/* 245 */     buffer.append("$Version=");
/* 246 */     buffer.append(Integer.toString(getVersion()));
/* 247 */     return (Header)new BufferedHeader(buffer);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 252 */     return "rfc2965";
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\cookie\RFC2965Spec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */