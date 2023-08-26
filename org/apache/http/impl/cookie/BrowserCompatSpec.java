/*     */ package org.apache.http.impl.cookie;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import org.apache.http.FormattedHeader;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HeaderElement;
/*     */ import org.apache.http.NameValuePair;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.cookie.CommonCookieAttributeHandler;
/*     */ import org.apache.http.cookie.Cookie;
/*     */ import org.apache.http.cookie.CookieAttributeHandler;
/*     */ import org.apache.http.cookie.CookieOrigin;
/*     */ import org.apache.http.cookie.MalformedCookieException;
/*     */ import org.apache.http.message.BasicHeaderElement;
/*     */ import org.apache.http.message.BasicHeaderValueFormatter;
/*     */ import org.apache.http.message.BufferedHeader;
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
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ @ThreadSafe
/*     */ public class BrowserCompatSpec
/*     */   extends CookieSpecBase
/*     */ {
/*  68 */   private static final String[] DEFAULT_DATE_PATTERNS = new String[] { "EEE, dd MMM yyyy HH:mm:ss zzz", "EEE, dd-MMM-yy HH:mm:ss zzz", "EEE MMM d HH:mm:ss yyyy", "EEE, dd-MMM-yyyy HH:mm:ss z", "EEE, dd-MMM-yyyy HH-mm-ss z", "EEE, dd MMM yy HH:mm:ss z", "EEE dd-MMM-yyyy HH:mm:ss z", "EEE dd MMM yyyy HH:mm:ss z", "EEE dd-MMM-yyyy HH-mm-ss z", "EEE dd-MMM-yy HH:mm:ss z", "EEE dd MMM yy HH:mm:ss z", "EEE,dd-MMM-yy HH:mm:ss z", "EEE,dd-MMM-yyyy HH:mm:ss z", "EEE, dd-MM-yyyy HH:mm:ss z" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BrowserCompatSpec(String[] datepatterns, BrowserCompatSpecFactory.SecurityLevel securityLevel) {
/*  87 */     super(new CommonCookieAttributeHandler[] { new BrowserCompatVersionAttributeHandler(), new BasicDomainHandler(), (securityLevel == BrowserCompatSpecFactory.SecurityLevel.SECURITYLEVEL_IE_MEDIUM) ? new BasicPathHandler() { public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {} } : new BasicPathHandler(), new BasicMaxAgeHandler(), new BasicSecureHandler(), new BasicCommentHandler(), new BasicExpiresHandler((datepatterns != null) ? (String[])datepatterns.clone() : DEFAULT_DATE_PATTERNS) });
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
/*     */   public BrowserCompatSpec(String[] datepatterns) {
/* 104 */     this(datepatterns, BrowserCompatSpecFactory.SecurityLevel.SECURITYLEVEL_DEFAULT);
/*     */   }
/*     */ 
/*     */   
/*     */   public BrowserCompatSpec() {
/* 109 */     this((String[])null, BrowserCompatSpecFactory.SecurityLevel.SECURITYLEVEL_DEFAULT);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Cookie> parse(Header header, CookieOrigin origin) throws MalformedCookieException {
/* 115 */     Args.notNull(header, "Header");
/* 116 */     Args.notNull(origin, "Cookie origin");
/* 117 */     String headername = header.getName();
/* 118 */     if (!headername.equalsIgnoreCase("Set-Cookie")) {
/* 119 */       throw new MalformedCookieException("Unrecognized cookie header '" + header.toString() + "'");
/*     */     }
/*     */     
/* 122 */     HeaderElement[] helems = header.getElements();
/* 123 */     boolean versioned = false;
/* 124 */     boolean netscape = false;
/* 125 */     for (HeaderElement helem : helems) {
/* 126 */       if (helem.getParameterByName("version") != null) {
/* 127 */         versioned = true;
/*     */       }
/* 129 */       if (helem.getParameterByName("expires") != null) {
/* 130 */         netscape = true;
/*     */       }
/*     */     } 
/* 133 */     if (netscape || !versioned) {
/*     */       CharArrayBuffer buffer;
/*     */       ParserCursor cursor;
/* 136 */       NetscapeDraftHeaderParser parser = NetscapeDraftHeaderParser.DEFAULT;
/*     */ 
/*     */       
/* 139 */       if (header instanceof FormattedHeader) {
/* 140 */         buffer = ((FormattedHeader)header).getBuffer();
/* 141 */         cursor = new ParserCursor(((FormattedHeader)header).getValuePos(), buffer.length());
/*     */       }
/*     */       else {
/*     */         
/* 145 */         String s = header.getValue();
/* 146 */         if (s == null) {
/* 147 */           throw new MalformedCookieException("Header value is null");
/*     */         }
/* 149 */         buffer = new CharArrayBuffer(s.length());
/* 150 */         buffer.append(s);
/* 151 */         cursor = new ParserCursor(0, buffer.length());
/*     */       } 
/* 153 */       HeaderElement elem = parser.parseHeader(buffer, cursor);
/* 154 */       String name = elem.getName();
/* 155 */       String value = elem.getValue();
/* 156 */       if (name == null || name.isEmpty()) {
/* 157 */         throw new MalformedCookieException("Cookie name may not be empty");
/*     */       }
/* 159 */       BasicClientCookie cookie = new BasicClientCookie(name, value);
/* 160 */       cookie.setPath(getDefaultPath(origin));
/* 161 */       cookie.setDomain(getDefaultDomain(origin));
/*     */ 
/*     */       
/* 164 */       NameValuePair[] attribs = elem.getParameters();
/* 165 */       for (int j = attribs.length - 1; j >= 0; j--) {
/* 166 */         NameValuePair attrib = attribs[j];
/* 167 */         String s = attrib.getName().toLowerCase(Locale.ROOT);
/* 168 */         cookie.setAttribute(s, attrib.getValue());
/* 169 */         CookieAttributeHandler handler = findAttribHandler(s);
/* 170 */         if (handler != null) {
/* 171 */           handler.parse(cookie, attrib.getValue());
/*     */         }
/*     */       } 
/*     */       
/* 175 */       if (netscape) {
/* 176 */         cookie.setVersion(0);
/*     */       }
/* 178 */       return (List)Collections.singletonList(cookie);
/*     */     } 
/* 180 */     return parse(helems, origin);
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isQuoteEnclosed(String s) {
/* 185 */     return (s != null && s.startsWith("\"") && s.endsWith("\""));
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Header> formatCookies(List<Cookie> cookies) {
/* 190 */     Args.notEmpty(cookies, "List of cookies");
/* 191 */     CharArrayBuffer buffer = new CharArrayBuffer(20 * cookies.size());
/* 192 */     buffer.append("Cookie");
/* 193 */     buffer.append(": ");
/* 194 */     for (int i = 0; i < cookies.size(); i++) {
/* 195 */       Cookie cookie = cookies.get(i);
/* 196 */       if (i > 0) {
/* 197 */         buffer.append("; ");
/*     */       }
/* 199 */       String cookieName = cookie.getName();
/* 200 */       String cookieValue = cookie.getValue();
/* 201 */       if (cookie.getVersion() > 0 && !isQuoteEnclosed(cookieValue)) {
/* 202 */         BasicHeaderValueFormatter.INSTANCE.formatHeaderElement(buffer, (HeaderElement)new BasicHeaderElement(cookieName, cookieValue), false);
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */         
/* 208 */         buffer.append(cookieName);
/* 209 */         buffer.append("=");
/* 210 */         if (cookieValue != null) {
/* 211 */           buffer.append(cookieValue);
/*     */         }
/*     */       } 
/*     */     } 
/* 215 */     List<Header> headers = new ArrayList<Header>(1);
/* 216 */     headers.add(new BufferedHeader(buffer));
/* 217 */     return headers;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getVersion() {
/* 222 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public Header getVersionHeader() {
/* 227 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 232 */     return "compatibility";
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\cookie\BrowserCompatSpec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */